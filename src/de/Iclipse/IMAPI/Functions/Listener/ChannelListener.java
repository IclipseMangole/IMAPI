package de.Iclipse.IMAPI.Functions.Listener;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.IMAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ChannelListener  {

    private static HashMap<Player, Object> obj = new HashMap<Player, Object>();
    public static ArrayList<String> servers;
    public final IMAPI plugin;

    public ChannelListener(IMAPI plugin){
        this.plugin = plugin;
    }


    public synchronized ArrayList<String> getServers() {
        Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        System.out.println(p.getName());
        sendToBungeeCord(p, "GetServers", null);
        try {
            wait();
        } catch (InterruptedException e) {
        }

        return servers;

    }

    public void sendToBungeeCord(Player p, String subchannel, String arg) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        try {
            output.writeUTF(subchannel);
            if (arg != null) {
                output.writeUTF(arg);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        p.sendPluginMessage(plugin, "im:main", stream.toByteArray());
    }
}
