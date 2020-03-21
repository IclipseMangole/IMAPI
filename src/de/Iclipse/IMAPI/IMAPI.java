package de.Iclipse.IMAPI;

import com.google.common.base.Joiner;
import de.Iclipse.IMAPI.Functions.Commands.*;
import de.Iclipse.IMAPI.Functions.Listener.*;
import de.Iclipse.IMAPI.Functions.MySQL.MySQL;
import de.Iclipse.IMAPI.Functions.MySQL.MySQL_News;
import de.Iclipse.IMAPI.Functions.MySQL.MySQL_User;
import de.Iclipse.IMAPI.Functions.MySQL.MySQL_UserSettings;
import de.Iclipse.IMAPI.Util.Command.BukkitCommand;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.Dispatching.Dispatcher;
import de.Iclipse.IMAPI.Util.Dispatching.Language;
import de.Iclipse.IMAPI.Util.executor.ThreadExecutor;
import de.Iclipse.IMAPI.Util.executor.types.BukkitExecutor;
import de.Iclipse.IMAPI.Util.menu.PopupMenuAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.*;

import static de.Iclipse.IMAPI.Data.*;
import static de.Iclipse.IMAPI.Util.Dispatching.ResourceBundle.loadResourceBundleDE;
import static de.Iclipse.IMAPI.Util.Dispatching.ResourceBundle.loadResourceBundleEN;
import static de.Iclipse.IMAPI.Util.Dispatching.ResourceBundle.msgDE;
import static de.Iclipse.IMAPI.Util.Dispatching.ResourceBundle.msgEN;

public class IMAPI extends JavaPlugin {

    @Override
    public void onEnable() {
        Data.instance = this;
        ThreadExecutor.setExecutor(new BukkitExecutor());
        MySQL.connect();
        loadResourceBundles();
        dsp = new Dispatcher(this);
        registerListener();
        registerCommands();
        createTables();
        blocks = new HashMap<>();
        onlinetime = new HashMap<>();
        initCounters();

    }

    @Override
    public void onDisable() {
        super.onDisable();
        saveCounters();
        MySQL.close();
    }

    public void registerListener() {
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new Completer(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new QuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new PopupMenuAPI(), this);
    }

    public void registerCommands() {
        commands = new HashMap<>();
        completions = new HashMap<>();
        register(new cmd_lang(), this);
        register(new cmd_help(), this);
        register(new cmd_restart(), this);
        register(new cmd_color(), this);
        register(new cmd_news(), this);
        register(new cmd_gamemode(), this);
        register(new cmd_schnitzel(), this);
        register(new cmd_apireload(), this);
    }

    /*
    public void registerCompleter(){
            TabCompleter completer = new Completer();
            System.out.println(completer.toString());
            PluginCommand command = this.getCommand(cmd.name());
            System.out.println(command.toString());
            this.getCommand(cmd.name())
                    .setTabCompleter(completer);
    }
    */

    public void createTables() {
        MySQL_User.createUserTable();
        MySQL_News.createNewsTable();
        MySQL_UserSettings.createUserSettingsTable();
    }

    public void initCounters() {
        if (Bukkit.getOnlinePlayers().size() > 0) {
            Bukkit.getOnlinePlayers().forEach(entry -> {
                onlinetime.put(entry, System.currentTimeMillis());
            });
            Bukkit.getOnlinePlayers().forEach(entry -> {
                blocks.put(entry, MySQL_User.getBlocksPlaced(entry.getUniqueId()));
            });
        }
    }

    public static void saveCounters() {
        if (onlinetime.size() > 0) {
            onlinetime.forEach((p, start) -> {
                MySQL_User.setOnlinetime(p.getUniqueId(), MySQL_User.getOnlinetime(p.getUniqueId()) + (System.currentTimeMillis() - start));
            });
        }
        if (blocks.size() > 0) {
            blocks.forEach((p, b) -> {
                MySQL_User.setBlocksPlaced(p.getUniqueId(), b);
            });
        }
    }

    public static void loadResourceBundles(){
        try {
            loadResourceBundleDE("langDE");
            loadResourceBundleEN("langEN");
            Language.DE.setBundle(msgDE);
            Language.EN.setBundle(msgEN);
            System.out.println(Data.prefix + "Loaded languages!");
        }catch(MissingResourceException | NullPointerException e){
            System.out.println("Reload oder Bundle not found!");
            dispatching = false;
        }
    }


    public static void register(Class functionClass, JavaPlugin plugin) {
        try {
            register(functionClass, functionClass.newInstance(), plugin);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * Registriert Listener und Commands aus einer Klasse für ein Plugin
     *
     * @param function Object der Klasse welche registriert werden soll
     * @param plugin   Plugin für welches die Klasse registriert wird
     */
    public static void register(Object function, JavaPlugin plugin) {
        register(function.getClass(), function, plugin);
    }

    /**
     * Registriert Listener und Commands aus einer Klasse für ein Plugin
     *
     * @param functionClass Klasse welche registriert werden soll
     * @param function      Object der Klasse welche registriert werden soll
     * @param plugin        Plugin für welches die Klasse registriert wird
     */
    public static void register(Class functionClass, Object function, JavaPlugin plugin) {
        Method[] methods = functionClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(IMCommand.class))
                registerCommand(function, method, plugin);
        }

        if (function instanceof Listener) {
            Bukkit.getPluginManager().registerEvents((Listener) function, plugin);
        }
    }

    private static Map<String, Command> commandMap = new HashMap<>();
    private static List<Object[]> unavailableSubcommands = new ArrayList<>();

    private static void registerCommand(Object function, Method method, JavaPlugin plugin) {
        IMCommand cmd = method.getAnnotation(IMCommand.class);

        if (cmd.parent().length == 0) {
            commands.put(cmd, plugin.getName());
            BukkitCommand tBukkitCommand = new BukkitCommand(plugin, function, method, cmd);
            tBukkitCommand.register();
            commandMap.put(tBukkitCommand.getName(), tBukkitCommand);


            for (Object[] unavailableSubcommand : unavailableSubcommands) {
                Method oldMethod = (Method) unavailableSubcommand[1];
                IMCommand old = oldMethod.getAnnotation(IMCommand.class);
                if (old.parent()[0].equalsIgnoreCase(cmd.name()))
                    registerCommand(unavailableSubcommand[0], oldMethod, plugin);
            }

        } else {
            Command pluginCommand = commandMap.get(cmd.parent()[0]);
            if (pluginCommand == null) {
                unavailableSubcommands.add(new Object[]{function, method});
                Joiner.on(" ").join(cmd.parent() + " " + cmd.name(), cmd.parent()[0]);
            } else {
                if (pluginCommand instanceof BukkitCommand) {
                    ((BukkitCommand) pluginCommand).getProcessor().addSubCommand(cmd, function, method);
                } else {
                    Joiner.on(" ").join(cmd.parent() + " " + cmd.name(), cmd.parent()[0]);
                }
            }
        }
    }


}
