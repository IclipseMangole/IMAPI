package de.Iclipse.IMAPI.Functions.NPC;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ShowNPCs implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        NPC.npcsForAll.forEach(npc -> npc.show(e.getPlayer()));
    }
}
