package de.Iclipse.IMAPI;

import de.Iclipse.IMAPI.Functions.Tablist;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.Dispatching.Dispatcher;
import de.Iclipse.IMAPI.Util.Dispatching.Language;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;

public class Data {
    public static String symbol = "§8 » §7";
    public static String prefix = "§5IM " + symbol;
    public static Language defaultLang = Language.DE;
    public static Dispatcher dsp;
    public static HashMap<IMCommand, Plugin> commands;
    public static HashMap<IMCommand, HashMap<Integer, ArrayList<String>>> completions;
    public static String textcolor;
    public static String highlight;
    public static String warning;
    public static Tablist tablist;


    public static Plugin instance;
}