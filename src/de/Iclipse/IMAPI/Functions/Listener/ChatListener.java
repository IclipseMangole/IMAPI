package de.Iclipse.IMAPI.Functions.Listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Yannick who could get really angry if somebody steal his code!
 * ~Yannick on 09.06.2019 at 22:54 o´ clock
 */
public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        if (e.getPlayer().hasPermission("im.chat.color")) {
            e.setMessage(ChatColor.translateAlternateColorCodes('$', e.getMessage()));
        }

        String message = "";
        message += e.getPlayer().getDisplayName();
        message += "§8»§7 ";
        message += e.getMessage();

        e.setFormat(message);


    }
}
