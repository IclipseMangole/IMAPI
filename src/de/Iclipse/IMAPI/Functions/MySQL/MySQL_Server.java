package de.Iclipse.IMAPI.Functions.MySQL;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class MySQL_Server {
        public static void createServerTable() {
            MySQL.update("CREATE TABLE IF NOT EXISTS servers(servername VARCHAR(20), status VARCHAR(30), maxplayers INT(4), players VARCHAR(10000), specs VARCHAR(1000), port INT(7))");
        }

        public static ArrayList<String> getServers(){
            ArrayList<String> list = new ArrayList<>();
            try {
                ResultSet rs = MySQL.querry("SELECT servername FROM `servers` WHERE 1");
                while(rs.next()){
                    list.add(rs.getString("servername"));
                }
                return list;
            }catch(Exception e){
                e.printStackTrace();
            }
            return list;
        }

        public static void registerServer(String servername, String status, int maxplayers) {
            MySQL.update("INSERT INTO `servers` VALUES ('" + servername + "', '" + status + "', " + maxplayers + ", 'NONE', 'NONE', " + Bukkit.getServer().getPort() + " )");
        }

        public static boolean isServerRegistered(String servername){
            try {
                ResultSet rs = MySQL.querry("SELECT status FROM `servers` WHERE servername = '" + servername + "'");
                return rs.next();
            }catch(SQLException e){
                return false;
            }
        }

        public static void changeServerStatus(String servername, String status) {
            MySQL.update("UPDATE `servers` SET status = '" + status + "' WHERE servername = '" + servername + "'");
        }

        public static String getStatus(String servername) {
            try {
                System.out.println("SELECT status FROM `servers` WHERE servername = '" + servername + "'");
                ResultSet rs = MySQL.querry("SELECT status FROM `servers` WHERE servername = '" + servername + "'");
                while (rs.next()) {
                    return rs.getString("status");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static void setMaxPlayers(String servername, int maxplayers) {
            MySQL.update("UPDATE `servers` SET maxplayers = " + maxplayers + " WHERE servername = '" + servername + "'");
        }

        public static Integer getMaxPlayers(String servername) {
            try {
                ResultSet rs = MySQL.querry("SELECT maxplayers FROM `servers` WHERE servername = '" + servername + "'");
                while (rs.next()) {
                    return rs.getInt("maxplayers");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        }


        public static ArrayList<UUID> getPlayers(String servername){
            ArrayList<UUID> list = new ArrayList<>();
            try {
                ResultSet rs = MySQL.querry("SELECT players FROM `servers` WHERE servername = '" + servername + "'");
                while (rs.next()) {
                    String s = rs.getString("players");
                    if(!s.equalsIgnoreCase("NONE")) {
                        String[] li = s.split(",");
                        for (int i = 0; i < li.length; i++) {
                            System.out.println("Test: " + li[i]);
                            list.add(i, UUID.fromString(li[i]));
                        }
                    }
                    return list;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
        }

        public static int getPort(String servername){
            try{
                ResultSet rs = MySQL.querry("SELECT port FROM `servers` WHERE servername = '" + servername + "'");
                return rs.getInt("port");
            }catch(SQLException e){
                return -1;
            }
        }

        public static void addPlayer(String servername, UUID uuid){
            ArrayList list = getPlayers(servername);
            list.add(uuid);
            setPlayers(servername, list);
        }

        public static void addPlayer(String servername, Player player){
            ArrayList list = getPlayers(servername);
            list.add(player.getUniqueId());
            setPlayers(servername, list);
        }

        public static void removePlayer(String servername, UUID uuid){
            ArrayList list = getPlayers(servername);
            list.remove(uuid);
            setPlayers(servername, list);
        }

        public static void removePlayer(String servername, Player player){
            ArrayList list = getPlayers(servername);
            list.remove(player.getUniqueId());
            setPlayers(servername, list);
        }

        public static void setPlayers(String servername, ArrayList<UUID> list){
            final String[] l = {""};
            if (list == null) {
                l[0] = "NONE";
            }else {
                list.forEach(entry -> {
                    l[0] = l[0] + entry + ",";
                });
            }
            MySQL.update("UPDATE `servers` SET players = '" + l[0] + "' WHERE servername = '" + servername + "'");
        }


        public static ArrayList<UUID> getSpecs(String servername){
            try {
                ArrayList<UUID> list = new ArrayList<>();
                ResultSet rs = MySQL.querry("SELECT specs FROM `servers` WHERE servername = '" + servername + "'");
                while (rs.next()) {
                    String s = rs.getString("specs");
                    if(!s.equalsIgnoreCase("NONE")) {
                        String[] li = s.split(",");
                        for (int i = 0; i < li.length; i++) {
                            list.add(i, UUID.fromString(li[i]));
                        }
                    }
                    return list;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }


        public static void addSpec(String servername, UUID uuid){
            ArrayList list = getSpecs(servername);
            list.add(uuid);
            setSpecs(servername, list);
        }

        public static void addSpec(String servername, Player player){
            ArrayList list = getSpecs(servername);
            list.add(player.getUniqueId());
            setSpecs(servername, list);
        }

        public static void removeSpec(String servername, UUID uuid){
            ArrayList list = getSpecs(servername);
            list.remove(uuid);
            setSpecs(servername, list);
        }

        public static void removeSpec(String servername, Player player){
            ArrayList list = getSpecs(servername);
            list.remove(player.getUniqueId());
            setSpecs(servername, list);
        }


        public static void setSpecs(String servername, ArrayList<UUID> list){
            final String[] l = {""};
            if(list.size() == 0){
                l[0] = "NONE";
            }else {
                list.forEach(entry -> {
                    l[0] = l[0] + entry + ",";
                });
            }
            MySQL.update("UPDATE `servers` SET players = '" + l[0] + "' WHERE servername = '" + servername + "'");
        }

    }
