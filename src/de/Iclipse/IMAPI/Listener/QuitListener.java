package de.Iclipse.IMAPI.Listener;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Database.User;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import net.minecraft.server.v1_15_R1.PacketPlayOutScoreboardObjective;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static de.Iclipse.IMAPI.Util.ScoreboardSign.setField;


public class QuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        User.setOnlinetime(UUIDFetcher.getUUID(p.getName()), User.getOnlinetime(UUIDFetcher.getUUID(p.getName())) + (System.currentTimeMillis() - Data.onlinetime.get(p)));
        User.setLastTime(p, System.currentTimeMillis());
        User.setBlocksPlaced(UUIDFetcher.getUUID(p.getName()), Data.blocks.get(p));
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
        setField(packet, "a", e.getPlayer().getName());
        setField(packet, "d", 1);
        ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(packet);
    }
}
