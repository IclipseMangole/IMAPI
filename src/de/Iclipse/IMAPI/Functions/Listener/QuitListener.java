package de.Iclipse.IMAPI.Functions.Listener;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Functions.MySQL.MySQL_User;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static de.Iclipse.IMAPI.Data.dsp;

public class QuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        MySQL_User.setOnlinetime(UUIDFetcher.getUUID(p.getName()),MySQL_User.getOnlinetime(UUIDFetcher.getUUID(p.getName())) +  (System.currentTimeMillis() - Data.onlinetime.get(p)));
        MySQL_User.setLastTime(p, System.currentTimeMillis());
        MySQL_User.setBlocksPlaced(UUIDFetcher.getUUID(p.getName()), Data.blocks.get(p));
        Bukkit.getOnlinePlayers().forEach(entry ->{
            if(!entry.equals(p)){
                dsp.send(entry, "quit.message", p.getDisplayName());
            }
        });
    }
}
