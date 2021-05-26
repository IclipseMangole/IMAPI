package de.Iclipse.IMAPI;

//  ╔══════════════════════════════════════╗
//  ║      ___       ___                   ║
//  ║     /  /___   /  /(_)____ ____  __   ║
//  ║    /  // __/ /  // // ) // ___// )\  ║                                  
//  ║   /  // /__ /  // //  _/(__  )/ __/  ║                                                                         
//  ║  /__/ \___//__//_//_/  /____/ \___/  ║                                              
//  ╚══════════════════════════════════════╝

import de.Iclipse.IMAPI.Database.*;
import de.Iclipse.IMAPI.Functions.Scheduler;
import de.Iclipse.IMAPI.Functions.Tablist;
import de.Iclipse.IMAPI.Util.Command.CommandRegistration;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.Dispatching.Dispatcher;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * Created by Iclipse on 27.11.2020
 */
public class Data {

    private final IMAPI imapi;
    
    private final MySQL mySQL;
    private final CommandRegistration commandRegistration;
    private Dispatcher dispatcher;
    private ResourceBundle langDE;
    private ResourceBundle langEN;
    private final Tablist tablist;
    private final Scheduler scheduler;
    private final ChatColor textcolor = ChatColor.of("#ffffff");
    private final ChatColor highlight = ChatColor.of("#a42eff");
    private final ChatColor warning = ChatColor.of("#ff0000");

    private boolean dispatching = true;
    private boolean updatePlayers = true;


    //Lists
    private final HashMap<Player, Long> onlinetime = new HashMap<>();
    private final HashMap<Player, Integer> blocks = new HashMap<>();
    private final HashMap<String, ItemStack> heads = new HashMap<>();
    private final HashMap<IMCommand, String> commands = new HashMap<>();

    //Tables
    private FriendTable friendTable;
    private ModeTable modeTable;
    private NewsTable newsTable;
    private ServerTable serverTable;
    private SignTable signTable;
    private UserSettingsTable userSettingsTable;
    private UserTable userTable;

    public Data(IMAPI imapi){
        this.imapi = imapi;
        this.mySQL = new MySQL(imapi);
        this.commandRegistration = new CommandRegistration(imapi);
        this.tablist = new Tablist(imapi);
        this.scheduler = new Scheduler(imapi);

    }

    public void createTables(){
        friendTable = new FriendTable(imapi);
        modeTable = new ModeTable(imapi);
        newsTable = new NewsTable(imapi);
        serverTable = new ServerTable(imapi);
        signTable = new SignTable(imapi);
        userSettingsTable = new UserSettingsTable(imapi);
        userTable = new UserTable(imapi);
    }

    public void loadResourceBundles() {
        HashMap<String, ResourceBundle> langs = new HashMap<>();
        try {
            langDE = ResourceBundle.getBundle("i18n.langDE");
            langEN = ResourceBundle.getBundle("i18n.langEN");
        } catch (MissingResourceException e) {
            dispatching = false;
        } catch (Exception e) {
            System.out.println("Reload oder Bundle not found!");
            dispatching = false;
        }
        langs.put("DE", langDE);
        langs.put("EN", langEN);
        dispatcher = new Dispatcher(imapi,
                langs);
    }

    public void initCounters(){
        if (Bukkit.getOnlinePlayers().size() > 0) {
            Bukkit.getOnlinePlayers().forEach(entry -> {
                onlinetime.put(entry, System.currentTimeMillis());
            });
            Bukkit.getOnlinePlayers().forEach(entry -> {
                blocks.put(entry, userTable.getBlocksPlaced(UUIDFetcher.getUUID(entry.getName())));
            });
        }
    }

    public void saveCounters(){
        if (onlinetime.size() > 0) {
            onlinetime.forEach((p, start) -> {
                userTable.setOnlinetime(UUIDFetcher.getUUID(p.getName()), userTable.getOnlinetime(UUIDFetcher.getUUID(p.getName())) + (System.currentTimeMillis() - start));
            });
        }
        if (blocks.size() > 0) {
            blocks.forEach((p, b) -> {
                userTable.setBlocksPlaced(UUIDFetcher.getUUID(p.getName()), b);
            });
        }
    }

    public IMAPI getIMAPI() {
        return imapi;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public CommandRegistration getCommandRegistration() {
        return commandRegistration;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public boolean isUpdatePlayers() {
        return updatePlayers;
    }

    public boolean isDispatching() {
        return dispatching;
    }

    public Tablist getTablist() {
        return tablist;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public String getSymbol() {
        String symbol = "§8 » §7";
        return symbol;
    }

    public String getPrefix() {
        String prefix = "§5IM§f" + getSymbol();
        return prefix;
    }

    public String getNoperm() {
        String noperm = "§4No permissions!";
        return noperm;
    }

    public String getNoConsole() {
        String noConsole = "§4No Console!";
        return noConsole;
    }

    public ChatColor getHighlight() {
        return highlight;
    }

    public ChatColor getTextcolor() {
        return textcolor;
    }

    public ChatColor getWarning() {
        return warning;
    }

    public HashMap<Player, Integer> getBlocks() {
        return blocks;
    }

    public HashMap<Player, Long> getOnlinetime() {
        return onlinetime;
    }

    public HashMap<String, ItemStack> getHeads() {
        return heads;
    }

    public HashMap<IMCommand, String> getCommands() {
        return commands;
    }

    public FriendTable getFriendTable() {
        return friendTable;
    }

    public ModeTable getModeTable() {
        return modeTable;
    }

    public NewsTable getNewsTable() {
        return newsTable;
    }

    public ServerTable getServerTable() {
        return serverTable;
    }

    public SignTable getSignTable() {
        return signTable;
    }

    public UserSettingsTable getUserSettingsTable() {
        return userSettingsTable;
    }

    public UserTable getUserTable() {
        return userTable;
    }

}
