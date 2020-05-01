package de.Iclipse.IMAPI.Listener;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Database.User;
import de.Iclipse.IMAPI.Database.UserSettings;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import net.minecraft.server.v1_15_R1.PacketPlayOutScoreboardObjective;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

import static de.Iclipse.IMAPI.Data.dsp;
import static de.Iclipse.IMAPI.Data.instance;
import static de.Iclipse.IMAPI.Util.ScoreboardSign.setField;
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
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
        setField(packet, "a", e.getPlayer().getName());
        setField(packet, "d", 1);
        ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(packet);
        Data.onlinetime.put(p, System.currentTimeMillis());
        Data.blocks.put(p, User.getBlocksPlaced(getUUID(p.getName())));
        createSettings(UUIDFetcher.getUUID(p.getName()));
        if (UserSettings.getBoolean(UUIDFetcher.getUUID(p.getName()), "vanish")) {
            Bukkit.getOnlinePlayers().forEach(entry -> {
                entry.hidePlayer(instance, p);
            });
            dsp.send(p, "vanish.join");
        }
    }


    public void createSettings(UUID uuid) {
        UserSettings.createUserSetting(uuid, "vanish", false);
        UserSettings.createUserSetting(uuid, "baro_maxPlayerBars", 2);
    }
}
