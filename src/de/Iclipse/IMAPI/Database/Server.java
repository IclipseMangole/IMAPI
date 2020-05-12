package de.Iclipse.IMAPI.Database;

import de.Iclipse.IMAPI.Functions.Servers.State;
import de.Iclipse.IMAPI.Functions.Vanish;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Server {
    private Server() {
    }

    public static void createServerTable() {
        MySQL.update("CREATE TABLE IF NOT EXISTS server(name VARCHAR(20), port INT(10), mode VARCHAR(20), state VARCHAR(10), players INT(3), maxplayers INT(3), PRIMARY KEY (name))");
    }

    public static void createServer(String name, int port, int maxPlayer) {
        MySQL.update("INSERT INTO server (name, port, mode, state, players, maxplayers) VALUES ('" + name + "', " + port + ", 'NONE', '" + State.Online.name() + "', " + (Bukkit.getOnlinePlayers().size() - Vanish.getVanishsOnServer().size()) + ", " + maxPlayer + ")");
    }

    public static void deleteServer(String name) {
        MySQL.update("DELETE FROM server WHERE name = '" + name + "'");
    }

    public static ArrayList<String> getServers() {
        ArrayList<String> list = new ArrayList<>();
        try {
            ResultSet resultSet = MySQL.querry("SELECT name FROM server WHERE 1");
            while (resultSet.next()) {
                list.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<String> getServers(String mode) {
        ArrayList<String> list = new ArrayList<>();
        try {
            ResultSet resultSet = MySQL.querry("SELECT name FROM server WHERE mode = '" + mode + "'");
            while (resultSet.next()) {
                list.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static int getPort(String name) {
        try {
            ResultSet resultSet = MySQL.querry("SELECT port FROM server WHERE name = '" + name + "'");
            if (resultSet.next()) {
                return resultSet.getInt("port");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getMode(String name) {
        try {
            ResultSet resultSet = MySQL.querry("SELECT mode FROM server WHERE name = '" + name + "'");
            if (resultSet.next()) {
                String s = resultSet.getString("mode");
                if (!s.equals("NONE")) {
                    return s;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setMode(String name, String mode) {
        MySQL.update("UPDATE server SET mode = '" + mode + "' WHERE name = '" + name + "'");
    }

    public static State getState(String name) {
        try {
            ResultSet resultSet = MySQL.querry("SELECT state FROM server WHERE name = '" + name + "'");
            if (resultSet.next()) {
                return State.valueOf(resultSet.getString("state"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setState(String name, State status) {
        MySQL.update("UPDATE server SET state = '" + status.name() + "' WHERE name = '" + name + "'");
    }


    public static int getPlayers(String name) {
        try {
            ResultSet resultSet = MySQL.querry("SELECT players FROM server WHERE name = '" + name + "'");
            if (resultSet.next()) {
                return resultSet.getInt("players");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void setPlayers(String name, int players) {
        MySQL.update("UPDATE server SET players = '" + players + "' WHERE name = '" + name + "'");
    }

    public static int getMaxPlayers(String name) {
        try {
            ResultSet resultSet = MySQL.querry("SELECT maxplayers FROM server WHERE name = '" + name + "'");
            if (resultSet.next()) {
                return resultSet.getInt("maxplayers");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void setMaxPlayers(String name, int maxplayers) {
        MySQL.update("UPDATE server SET maxplayers = '" + maxplayers + "' WHERE name = '" + name + "'");
    }

}
