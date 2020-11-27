package de.Iclipse.IMAPI.Util;

import de.Iclipse.IMAPI.Data;
import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Actionbar {
    public static void online(String message, boolean prefix) {
        Bukkit.getOnlinePlayers().forEach(p -> send(p, prefix ? Data.prefix : "" + message));
    }

    public static void online(String message) {
        online(message, false);
    }

    public static void send(Player p, String message, boolean prefix) {
        send(p, prefix ? Data.prefix : "" + message);
    }

    public static void send(Player p, String message) {
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\" }"), ChatMessageType.GAME_INFO, p.getUniqueId()));
    }

}