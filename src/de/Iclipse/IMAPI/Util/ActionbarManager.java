package de.Iclipse.IMAPI.Util;

import de.Iclipse.IMAPI.Data;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_15_R1.ChatMessageType;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by Yannick on 29.10.2019 with IntelliJ for PXCode.
 */
public class ActionbarManager {
    final static HashMap<String, Integer> Count = new HashMap<String,Integer>();

    public static void sendActionBar(Player player, String Nachricht)
    {
        Count.forEach((entry1, entry2) ->{
            if(entry1.equals(player.getName())){
                Count.remove(entry1);
            }
        });
        final String NachrichtNeu = Nachricht.replace("_", " ");
        String s = ChatColor.translateAlternateColorCodes('&', NachrichtNeu);
        IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + s +
                "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, ChatMessageType.GAME_INFO);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(bar);
    }

    public static void sendActionBarTime(final Player player, final String Nachricht,final Integer Zeit)
    {


        final String NachrichtNeu = Nachricht.replace("_", " ");

        if(!Count.containsKey(player.getName())){
            String s = ChatColor.translateAlternateColorCodes('&', NachrichtNeu);
            IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + s +
                    "\"}");
            PacketPlayOutChat bar = new PacketPlayOutChat(icbc, ChatMessageType.GAME_INFO);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(bar);
        }

        Bukkit.getScheduler().runTaskLater(Data.instance, new Runnable() {
            @Override
            public void run() {
                String s = ChatColor.translateAlternateColorCodes('&', NachrichtNeu);
                IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + s +
                        "\"}");
                PacketPlayOutChat bar = new PacketPlayOutChat(icbc, ChatMessageType.GAME_INFO);
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket(bar);

                if(!Count.containsKey(player.getName())){
                    Count.put(player.getName(),0);
                }
                int count = Count.get(player.getName());
                int newCount = count+20;
                Count.put(player.getName(), newCount);

                if(newCount < Zeit-20){
                    ActionbarManager.wait(player,Nachricht,Zeit);
                }else{
                    Count.remove(player.getName());
                }
            }
        }, 10);
    }

    private static void wait(final Player player, final String Nachricht,final Integer Zeit){
        Bukkit.getScheduler().runTaskLater(Data.instance, new Runnable() {
            @Override
            public void run() {
                sendActionBarTime(player,Nachricht,Zeit);
            }
        }, 10);

    }
}
