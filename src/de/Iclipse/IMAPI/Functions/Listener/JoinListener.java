package de.Iclipse.IMAPI.Functions.Listener;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Functions.MySQL.MySQL_User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static de.Iclipse.IMAPI.Data.dsp;
import static de.Iclipse.IMAPI.Data.tablist;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(MySQL_User.isUserExists(p.getUniqueId())){
            dsp.send(p, "join.old", p.getName());
        }else{
            MySQL_User.createUser(p.getUniqueId());
            dsp.send(p, "join.new", p.getName());
        }
        tablist.setPlayer(e.getPlayer());
        tablist.setTablist(e.getPlayer().getUniqueId());
        e.setJoinMessage(null);
        Bukkit.getOnlinePlayers().forEach(entry ->{
            if(!entry.equals(p)){
                dsp.send(entry, "join.message", p.getDisplayName());
            }
        });
        Data.onlinetime.put(p, System.currentTimeMillis());
        Data.blocks.put(p, MySQL_User.getBlocksPlaced(p.getUniqueId()));
    }
}
