package de.Iclipse.IMAPI.Listener;

import de.Iclipse.IMAPI.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        Data.blocks.replace(p, Data.blocks.get(p) + 1);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        Data.blocks.replace(p, Data.blocks.get(p) + 1);
    }
}
