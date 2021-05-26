package de.Iclipse.IMAPI.Listener;

import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardObjective;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static de.Iclipse.IMAPI.Util.ScoreboardSign.setField;


public class QuitListener implements Listener {

    private final IMAPI imapi;

    public QuitListener(IMAPI imapi) {
        this.imapi = imapi;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        imapi.getData().getUserTable().setOnlinetime(UUIDFetcher.getUUID(p.getName()), imapi.getData().getUserTable().getOnlinetime(UUIDFetcher.getUUID(p.getName())) + (System.currentTimeMillis() - imapi.getData().getOnlinetime().get(p)));
        imapi.getData().getOnlinetime().remove(p);
        imapi.getData().getUserTable().setLastTime(p, System.currentTimeMillis());
        imapi.getData().getUserTable().setBlocksPlaced(UUIDFetcher.getUUID(p.getName()), imapi.getData().getBlocks().get(p));
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
        setField(packet, "a", e.getPlayer().getName());
        setField(packet, "d", 1);
        ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(packet);
        imapi.getData().getUserTable().setServer(UUIDFetcher.getUUID(p.getName()), null);
        if (imapi.getData().isUpdatePlayers()) imapi.getData().getServerTable().setPlayers(imapi.getServerName(), imapi.getData().getServerTable().getPlayers(imapi.getServerName()) - 1);
        if (Bukkit.getOnlinePlayers().size() == 0) {
            imapi.getData().getMySQL().close();
        }
        p.openInventory(Bukkit.createInventory(null, 9));
    }
}
