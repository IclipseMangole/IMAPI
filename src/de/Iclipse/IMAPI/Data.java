package de.Iclipse.IMAPI;

import com.comphenix.protocol.ProtocolManager;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.Dispatching.Dispatcher;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Data {
    public static String symbol = "§8 » §7";
    public static String prefix = "§5IM" + symbol;
    public static ResourceBundle langDE;
    public static ResourceBundle langEN;
    public static Dispatcher dsp;
    public static HashMap<IMCommand, String> commands;
    public static HashMap<IMCommand, HashMap<Integer, ArrayList<String>>> completions;
    public static String textcolor;
    public static String highlight;
    public static String warning;
    public static HashMap<Player, Long> onlinetime;
    public static HashMap<Player, Integer> blocks;
    public static boolean dispatching = true;
    public static int restart = -1;
    public static boolean fakerestart = false;
    public static ProtocolManager protocolManager;


    public static Plugin instance;
}
