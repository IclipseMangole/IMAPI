package de.Iclipse.IMAPI.Listener;

import de.Iclipse.IMAPI.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BlockListener implements Listener {
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        Data.blocks.replace(p, Data.blocks.get(p) + 1);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Data.blocks.replace(p, Data.blocks.get(p) + 1);
    }

    @EventHandler
    public void handleNavigatorGUIClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();

        if (e.getView().getTitle().equals("Name")) {
            e.setCancelled(true);
            switch (e.getCurrentItem().getType()) {
                case NETHER_STAR:
                    p.sendMessage("Test");
                    break;

                case FIREWORK_STAR:
                    p.sendMessage("test");
                    p.closeInventory();
                    break;
                default:
                    p.sendMessage("NIX");
                    break;
            }
        }
    }
}
