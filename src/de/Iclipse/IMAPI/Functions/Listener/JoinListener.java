package de.Iclipse.IMAPI.Functions.Listener;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Functions.MySQL.MySQL_User;
import de.Iclipse.IMAPI.Functions.MySQL.MySQL_UserSettings;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

import static de.Iclipse.IMAPI.Data.dsp;
import static de.Iclipse.IMAPI.Data.instance;
import static de.Iclipse.IMAPI.Util.UUIDFetcher.getUUID;

public class JoinListener implements Listener {
    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if (!e.getHostname().equalsIgnoreCase("134.255.235.155:25565")) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage(dsp.get("proxyjoin.blocked", e.getPlayer()));
            System.out.println(e.getAddress());
            System.out.println(e.getHostname());
            System.out.println(e.getRealAddress());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Data.onlinetime.put(p, System.currentTimeMillis());
        Data.blocks.put(p, MySQL_User.getBlocksPlaced(getUUID(p.getName())));
        createSettings(UUIDFetcher.getUUID(p.getName()));
        if (MySQL_UserSettings.getBoolean(UUIDFetcher.getUUID(p.getName()), "vanish")) {
            Bukkit.getOnlinePlayers().forEach(entry -> {
                entry.hidePlayer(instance, p);
            });
            dsp.send(p, "vanish.join");
        }
    }


    public void createSettings(UUID uuid) {
        MySQL_UserSettings.createUserSetting(uuid, "vanish", false);
    }
}
