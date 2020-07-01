package de.Iclipse.IMAPI.Listener;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Database.MySQL;
import de.Iclipse.IMAPI.Database.Server;
import de.Iclipse.IMAPI.Database.User;
import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import net.minecraft.server.v1_16_R1.PacketPlayOutScoreboardObjective;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static de.Iclipse.IMAPI.Util.ScoreboardSign.setField;


public class QuitListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        User.setOnlinetime(UUIDFetcher.getUUID(p.getName()), User.getOnlinetime(UUIDFetcher.getUUID(p.getName())) + (System.currentTimeMillis() - Data.onlinetime.get(p)));
        Data.onlinetime.remove(p);
        User.setLastTime(p, System.currentTimeMillis());
        User.setBlocksPlaced(UUIDFetcher.getUUID(p.getName()), Data.blocks.get(p));
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
        setField(packet, "a", e.getPlayer().getName());
        setField(packet, "d", 1);
        ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(packet);
        User.setServer(UUIDFetcher.getUUID(p.getName()), null);
        if (Data.updatePlayers) Server.setPlayers(IMAPI.getServerName(), Server.getPlayers(IMAPI.getServerName()) - 1);
        if (Bukkit.getOnlinePlayers().size() == 0) {
            MySQL.close();
        }
        p.openInventory(Bukkit.createInventory(null, 9));
    }
}
