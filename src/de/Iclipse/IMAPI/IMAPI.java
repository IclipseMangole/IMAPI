package de.Iclipse.IMAPI;

//  ╔══════════════════════════════════════╗
//  ║      ___       ___                   ║
//  ║     /  /___   /  /(_)____ ____  __   ║
//  ║    /  // __/ /  // // ) // ___// )\  ║                                  
//  ║   /  // /__ /  // //  _/(__  )/ __/  ║                                                                         
//  ║  /__/ \___//__//_//_/  /____/ \___/  ║                                              
//  ╚══════════════════════════════════════╝

import de.Iclipse.IMAPI.Functions.*;
import de.Iclipse.IMAPI.Functions.News.News;
import de.Iclipse.IMAPI.Functions.Servers.Mode;
import de.Iclipse.IMAPI.Functions.Servers.State;
import de.Iclipse.IMAPI.Listener.BlockListener;
import de.Iclipse.IMAPI.Listener.ChatListener;
import de.Iclipse.IMAPI.Listener.JoinListener;
import de.Iclipse.IMAPI.Listener.QuitListener;
import de.Iclipse.IMAPI.Util.executor.ThreadExecutor;
import de.Iclipse.IMAPI.Util.executor.types.BukkitExecutor;
import de.Iclipse.IMAPI.Util.menu.PopupMenuAPI;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardObjective;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static de.Iclipse.IMAPI.Util.ScoreboardSign.setField;

/**
 * Created by Iclipse on 27.11.2020
 */
public class IMAPI extends JavaPlugin {

    private static IMAPI instance;

    public static IMAPI getInstance(){
        return instance;
    }

    private Data data;


    @Override
    public void onLoad() {
        ThreadExecutor.setExecutor(new BukkitExecutor(this));
    }

    @Override
    public void onEnable() {
        instance = this;
        data = new Data(this);
        data.createTables();
        data.loadResourceBundles();
        data.initCounters();
        registerCommands();
        registerListener();
        if (!data.getServerTable().getServers().contains(getServerName())) {
            data.getServerTable().createServer(getServerName(), Bukkit.getPort(), 100);
        } else {
            data.getServerTable().setState(getServerName(), State.Online);
        }
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
        data.getServerTable().setMap(getServerName(), null);
        data.getServerTable().setState(getServerName(), State.Offline);
        data.getServerTable().setPlayers(getServerName(), 0);
        data.saveCounters();
        data.getMySQL().close();
        data.getScheduler().stop();
        if (Bukkit.getOnlinePlayers().size() > 0) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
                setField(packet, "a", p.getName());
                setField(packet, "d", 1);
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            }
        }
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
    }

    public Data getData() {
        return data;
    }

    public String getServerName() {
        return getDataFolder().getAbsoluteFile().getParentFile().getParentFile().getName();
    }

    private void registerListener(){
        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(this), this);
        Bukkit.getPluginManager().registerEvents(new QuitListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PopupMenuAPI(), this);
    }

    private void registerCommands(){
        data.getCommandRegistration().register(new Language(this), this);
        data.getCommandRegistration().register(new Help(this), this);
        data.getCommandRegistration().register(new IMRestart(this), this);
        data.getCommandRegistration().register(new Color(this), this);
        data.getCommandRegistration().register(new News(this), this);
        data.getCommandRegistration().register(new Gamemode(this), this);
        data.getCommandRegistration().register(new Schnitzel(this), this);
        data.getCommandRegistration().register(new Vanish(this), this);
        data.getCommandRegistration().register(new Chatclear(), this);
        data.getCommandRegistration().register(new Ping(this), this);
        data.getCommandRegistration().register(new Serverstats(this), this);
        data.getCommandRegistration().register(new Mode(this), this);
        data.getCommandRegistration().register(new Playerinfo(this), this);
        data.getCommandRegistration().register(new Onlinetop(this), this);
    }

}
