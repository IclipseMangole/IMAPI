package de.Iclipse.IMAPI.Functions.MySQL;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySQL_Mode {
    public static void createModeTable(){
        MySQL.update("CREATE TABLE IF NOT EXISTS mode(modename VARCHAR(20), servers VARCHAR(10000), xk DOUBLE, yk DOUBLE, zk DOUBLE, yaw FLOAT, pitch FLOAT, name VARCHAR(30), slot INT(3), displayname VARCHAR(30))");
    }

    public static void registerMode(String modename){
        MySQL.update("INSERT INTO `mode` VALUES ('" + modename + "','NONE',-1.0,-1.0,-1.0,-1.0,-1.0,-1,-1,'" + modename + "')");
    }

    public static ArrayList<String> getModes(){
        ArrayList<String> list = new ArrayList<>();
        try{
            ResultSet rs = MySQL.querry("SELECT modename FROM `mode` WHERE 1");
            while(rs.next()){
                String s = rs.getString("modename");
                list.add(s);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    public static void deleteMode(String modename){
        MySQL.update("DELETE FROM `mode` WHERE modename = '" + modename + "'");
    }

    public static boolean isModeExists(String modename){
        try {
            ResultSet rs = MySQL.querry("SELECT displayname FROM `mode` WHERE modename = '" + modename + "'");
            return rs.next();
        }catch(SQLException e){
            return false;
        }
    }

    public static void setModeName(String modename, String newmodename) {
        MySQL.update("UPDATE `mode` SET modename = '" + newmodename + "' WHERE modename = '" + modename + "'");
    }

    public static String getMode(String server) {
        for (String entry : getModes()) {
            if (getServers(entry).contains(server)) {
                return entry;
            }
        }
        return null;
    }

    public static boolean hasServers(String modename) {
        if (getServers(modename).size() == 0) {
            return false;
        }
        return true;
    }

    public static boolean hasServer(String modename, String servername) {
        try {
            return getServers(modename).contains(servername);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static void setServers(String modename, ArrayList<String> list){
        final String[] l = {""};
        list.forEach(entry ->{
            //TODO
        });
        MySQL.update("UPDATE `mode`SET servers = '" + l[0] + "' WHERE modename = '" + modename + "'");
    }

    public static ArrayList<String> getServers(String modename){
        ArrayList<String> list = new ArrayList<>();
        try{
            String[] s;
            ResultSet rs = MySQL.querry("SELECT servers FROM `mode` WHERE modename = '" + modename + "'");
            while(rs.next()) {
                s = rs.getString("servers").split(",");
                if (s[0].equalsIgnoreCase("NONE")) {
                    return null;
                }
                for (int i = 0; i < s.length; i++) {
                    list.add(s[i]);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    public static void addServer(String modename, String servername){
        ArrayList<String> list = getServers(modename);
        System.out.println(servername);
        list.add(servername);
        setServers(modename, list);
    }

    public static void removeServer(String modename, String servername){
        ArrayList<String> list = getServers(modename);
        list.remove(servername);
        setServers(modename, list);
    }

    public static void removeServers(String modename, ArrayList<String> list){
        ArrayList<String> servers = getServers(modename);
        servers.removeAll(list);
        setServers(modename, servers);
    }

    public static void removeAllServers(String modename){
        ArrayList<String> list = new ArrayList<>();
        setServers(modename, list);
    }

    public static void setItem(String modename, String name){
        MySQL.update("UPDATE `mode` SET item = " + name + " WHERE modename = '" + modename + "'");
    }

    public static void setItem(String modename, ItemStack item){
        MySQL.update("UPDATE `mode` SET item = " + item.getType().name() + " WHERE modename = '" + modename + "'");
    }
    public static boolean hasItem(String modename){
        try {
            ResultSet rs = MySQL.querry("SELECT item FROM `mode` WHERE modename = '" + modename + "'");
            while (rs.next()) {
                int id = rs.getInt("item");
                if (id == -1) {
                    return false;
                }
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static ItemStack getItem(String modename){
        String name = getItemName(modename);
        if (!name.equals("AIR")) {
            ItemStack item = new ItemStack(Material.getMaterial(name));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(getDisplayname(modename));
            item.setItemMeta(meta);
            return item;
        }
        return null;
    }

    public static String getItemName(String modename) {
        try {
            ResultSet rs = MySQL.querry("SELECT item FROM `mode` WHERE modename = '" + modename + "'");
            while (rs.next()) {
                return rs.getString("item");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setSlot(String modename, int slot) {
        MySQL.update("UPDATE `mode`SET slot = " + slot + " WHERE modename = '" + modename + "'");
    }

    public static int getSlot(String modename) {
        try {
            ResultSet rs = MySQL.querry("SELECT slot FROM `mode` WHERE modename = '" + modename + "'");
            while (rs.next()) {
                return rs.getInt("slot");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean hasSlot(String modename) {
        try {
            ResultSet rs = MySQL.querry("SELECT slot FROM `mode` WHERE modename = '" + modename + "'");
            while (rs.next()) {
                if (rs.getInt("slot") != -1) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasLocation(String modename){
        Location loc = getLocation(modename);
        if (loc.getX() == -1 && loc.getY() == -1 && loc.getZ() == -1 && loc.getYaw() == -1 && loc.getPitch() == -1) {
            return false;
        }
        return true;
    }


    public static void setLocation(String modename, Location loc){
        MySQL.update("UPDATE `mode` SET xk = " + loc.getX() + ", yk = " + loc.getY() + ", zk = " + loc.getZ() + ", yaw = " + loc.getYaw() + ", pitch = " + loc.getPitch() + " WHERE modename = '" + modename + "'");
    }

    public static void setLocation(String modename, Player p){
        Location loc = p.getLocation();
        MySQL.update("UPDATE `mode` SET xk = " + loc.getX() + ", yk = " + loc.getY() + ", zk = " + loc.getZ() + ", yaw = " + loc.getYaw() + ", pitch = " + loc.getPitch() + " WHERE modename = '" + modename + "'");
    }

    public static Location getLocation(String modename){
        Location loc = new Location(Bukkit.getWorld("world"), 0, 0, 0, 0, 0);
        Double xk;
        Double zk;
        Double yk;
        Float yaw;
        Float pitch;
        try{
            ResultSet rs = MySQL.querry("SELECT xk, yk, zk, yaw, pitch FROM `mode` WHERE modename = '" + modename + "'");
            while(rs.next()) {
                xk = rs.getDouble("xk");
                yk = rs.getDouble("yk");
                zk = rs.getDouble("zk");
                yaw = rs.getFloat("yaw");
                pitch = rs.getFloat("pitch");
                return new Location(Bukkit.getWorld("world"), xk, yk, zk, yaw, pitch);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return loc;
    }

    public static void setDisplayname(String modename, String displayname){
        displayname = displayname.replace("$", "§");
        MySQL.update("UPDATE `mode`SET displayname = '" + displayname + "' WHERE modename = '" + modename + "'");
    }

    public static String getDisplayname(String modename){
        try {
            ResultSet rs = MySQL.querry("SELECT displayname FROM `mode` WHERE modename = '" + modename + "'");
            while(rs.next()) {
                String s = rs.getString("displayname");
                return ChatColor.translateAlternateColorCodes('&', s);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
