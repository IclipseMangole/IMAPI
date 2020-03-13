package de.Iclipse.IMAPI;

import com.google.common.base.Joiner;
import de.Iclipse.IMAPI.Functions.Commands.cmd_color;
import de.Iclipse.IMAPI.Functions.Commands.cmd_help;
import de.Iclipse.IMAPI.Functions.Commands.cmd_lang;
import de.Iclipse.IMAPI.Functions.Commands.cmd_restart;
import de.Iclipse.IMAPI.Functions.Listener.ChatListener;
import de.Iclipse.IMAPI.Functions.Listener.Completer;
import de.Iclipse.IMAPI.Functions.Listener.JoinListener;
import de.Iclipse.IMAPI.Functions.MySQL.MySQL;
import de.Iclipse.IMAPI.Functions.MySQL.MySQL_User;
import de.Iclipse.IMAPI.Functions.Tablist;
import de.Iclipse.IMAPI.Util.Command.BukkitCommand;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.Dispatching.Dispatcher;
import de.Iclipse.IMAPI.Util.Dispatching.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.Iclipse.IMAPI.Data.*;
import static de.Iclipse.IMAPI.Util.Dispatching.ResourceBundle.*;

public class IMAPI extends JavaPlugin {
    @Override
    public void onLoad() {
        super.onLoad();
        Data.instance = this;
        MySQL.connect();
        loadResourceBundles();
        dsp = new Dispatcher(this);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        registerListener();
        registerCommands();
        createTables();
        tablist = new Tablist();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void registerListener(){
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new Completer(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
    }

    public void registerCommands(){
        commands = new HashMap<>();
        completions = new HashMap<>();
        register(new cmd_lang(), this);
        register(new cmd_help(), this);
        register(new cmd_restart(), this);
        register(new cmd_color(), this);
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

    public void createTables(){
        MySQL_User.createUserTable();
    }

    public static void loadResourceBundles(){
        loadResourceBundleDE("langDE");
        loadResourceBundleEN("langEN");
        Language.DE.setBundle(msgDE);
        Language.EN.setBundle(msgEN);
        System.out.println(Data.prefix + "Loaded languages!");
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
            commands.put(cmd, plugin);
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