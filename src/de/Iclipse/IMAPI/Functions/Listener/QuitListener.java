package de.Iclipse.IMAPI.Functions.Listener;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Functions.MySQL.MySQL_User;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class QuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        MySQL_User.setOnlinetime(UUIDFetcher.getUUID(p.getName()),MySQL_User.getOnlinetime(UUIDFetcher.getUUID(p.getName())) +  (System.currentTimeMillis() - Data.onlinetime.get(p)));
        MySQL_User.setLastTime(p, System.currentTimeMillis());
        MySQL_User.setBlocksPlaced(UUIDFetcher.getUUID(p.getName()), Data.blocks.get(p));
    }
}
