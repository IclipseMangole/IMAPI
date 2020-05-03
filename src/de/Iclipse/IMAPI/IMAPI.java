package de.Iclipse.IMAPI;

import com.comphenix.protocol.ProtocolLibrary;
import com.google.common.base.Joiner;
import de.Iclipse.IMAPI.Database.MySQL;
import de.Iclipse.IMAPI.Database.User;
import de.Iclipse.IMAPI.Database.UserSettings;
import de.Iclipse.IMAPI.Functions.*;
import de.Iclipse.IMAPI.Functions.NPC.NPCCommand;
import de.Iclipse.IMAPI.Functions.NPC.ShowNPCs;
import de.Iclipse.IMAPI.Functions.News.News;
import de.Iclipse.IMAPI.Listener.BlockListener;
import de.Iclipse.IMAPI.Listener.ChatListener;
import de.Iclipse.IMAPI.Listener.JoinListener;
import de.Iclipse.IMAPI.Listener.QuitListener;
import de.Iclipse.IMAPI.Util.Command.BukkitCommand;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.Dispatching.Dispatcher;
import de.Iclipse.IMAPI.Util.executor.ThreadExecutor;
import de.Iclipse.IMAPI.Util.executor.types.BukkitExecutor;
import de.Iclipse.IMAPI.Util.menu.PopupMenuAPI;
import net.minecraft.server.v1_15_R1.PacketPlayOutScoreboardObjective;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static de.Iclipse.IMAPI.Data.*;
import static de.Iclipse.IMAPI.Functions.Scheduler.startScheduler;
import static de.Iclipse.IMAPI.Functions.Scheduler.stopScheduler;
import static de.Iclipse.IMAPI.Util.ScoreboardSign.setField;

public class IMAPI extends JavaPlugin {

    @Override
    public void onLoad() {
        Data.instance = this;
        ThreadExecutor.setExecutor(new BukkitExecutor());
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        saveCounters();
        MySQL.close();
        stopScheduler();
        if (Bukkit.getOnlinePlayers().size() > 0) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
                setField(packet, "a", p.getName());
                setField(packet, "d", 1);
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    public void registerListener() {
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new QuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new PopupMenuAPI(), this);
        Bukkit.getPluginManager().registerEvents(new ShowNPCs(), this);
    }

    @Override
    public void onEnable() {
        MySQL.connect();
        loadResourceBundles();
        registerListener();
        registerCommands();
        createTables();
        initCounters();
        startScheduler();
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
        User.createUserTable();
        de.Iclipse.IMAPI.Database.News.createNewsTable();
        UserSettings.createUserSettingsTable();
    }

    public void initCounters() {
        blocks = new HashMap<>();
        onlinetime = new HashMap<>();
        if (Bukkit.getOnlinePlayers().size() > 0) {
            Bukkit.getOnlinePlayers().forEach(entry -> {
                onlinetime.put(entry, System.currentTimeMillis());
            });
            Bukkit.getOnlinePlayers().forEach(entry -> {
                blocks.put(entry, User.getBlocksPlaced(entry.getUniqueId()));
            });
        }
    }

    public static void saveCounters() {
        if (onlinetime.size() > 0) {
            onlinetime.forEach((p, start) -> {
                User.setOnlinetime(p.getUniqueId(), User.getOnlinetime(p.getUniqueId()) + (System.currentTimeMillis() - start));
            });
        }
        if (blocks.size() > 0) {
            blocks.forEach((p, b) -> {
                User.setBlocksPlaced(p.getUniqueId(), b);
            });
        }
    }

    public void loadResourceBundles(){
        try {
            HashMap<String, ResourceBundle> langs = new HashMap<>();
            langDE = ResourceBundle.getBundle("i18n.langDE");
            langEN = ResourceBundle.getBundle("i18n.langEN");
            langs.put("DE", langDE);
            langs.put("EN", langEN);
            dsp = new Dispatcher(this,
                    langs);
        } catch(MissingResourceException e){
            e.printStackTrace();
            dispatching = false;
        } catch(NullPointerException e){
            e.printStackTrace();
            System.out.println("Reload oder Bundle not found!");
            dispatching = false;
        }
    }

    /*
    public void registerChannels(){
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "im:main");
        pml = new ChannelListener(this);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "im:main",  this);
        this.getServer().getMessenger().getIncomingChannelRegistrations(this).forEach(entry ->{
            System.out.println(entry.getChannel());
            System.out.println(entry.getListener().getClass());
        });
    }


    @Override
    public synchronized void onPluginMessageReceived(String channel, Player player, byte[] message) {
        System.out.println("Nachricht angekommen!");
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String subchannel = in.readUTF();
            System.out.println(subchannel);


            if (subchannel.equals("GetServers")) {
                String input = in.readUTF();
                System.out.println(input);
                ChannelListener.servers = (ArrayList<String>) Arrays.asList(input.split(", "));

                notifyAll();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
     */

    public void registerCommands() {
        commands = new HashMap<>();
        completions = new HashMap<>();
        register(new Language(), this);
        register(new Help(), this);
        register(new IMRestart(), this);
        register(new Color(), this);
        register(new News(), this);
        register(new Gamemode(), this);
        register(new Schnitzel(), this);
        register(new Vanish(), this);
        //register(new cmd_servers(), this);
        register(new Chatclear(), this);
        register(new Ping(), this);
        register(new NPCCommand(), this);
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

    public static void copyFilesInDirectory(File from, File to) throws IOException {
        if (!to.exists()) {
            to.mkdirs();
        }
        for (File file : from.listFiles()) {
            if (file.isDirectory()) {
                copyFilesInDirectory(file, new File(to.getAbsolutePath() + "/" + file.getName()));
            } else {
                File n = new File(to.getAbsolutePath() + "/" + file.getName());
                Files.copy(file.toPath(), n.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    public static void deleteFile(File f) {
        if (f.isDirectory()) {
            for (int i = 0; i < f.listFiles().length; i++) {
                deleteFile(f.listFiles()[i]);
            }
        }
        f.delete();
    }


}
