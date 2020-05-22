package de.Iclipse.IMAPI.Database;

import de.Iclipse.IMAPI.Util.StringConvert;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Sign {
    private Sign() {
    }

    public static void createSignTable() {
        MySQL.update("CREATE TABLE IF NOT EXISTS sign(id MEDIUMINT NOT NULL AUTO_INCREMENT, mode VARCHAR(20), location VARCHAR(100), server VARCHAR(20), PRIMARY KEY (id))");
    }

    public static void createSign(String mode, Location loc) {
        MySQL.update("INSERT INTO sign (mode, location, server) VALUES ('" + mode + "', '" + StringConvert.toString(loc) + "', 'NONE')");
    }

    public static int getId(Location loc) {
        ResultSet rs = MySQL.querry("SELECT id FROM sign WHERE location = '" + StringConvert.toString(loc) + "'");
        try {
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Location getLocation(int id) {
        ResultSet rs = MySQL.querry("SELECT location FROM sign WHERE id = " + id);
        try {
            if (rs.next()) {
                return StringConvert.toLocation(rs.getString("location"), "world");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isSign(Location loc) {
        ResultSet rs = MySQL.querry("SELECT id FROM sign WHERE location = '" + StringConvert.toString(loc) + "'");
        try {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void deleteSign(int id) {
        MySQL.update("DELETE FROM sign WHERE id = " + id);
    }

    public static ArrayList<Integer> getSigns() {
        ArrayList<Integer> list = new ArrayList<>();
        ResultSet rs = MySQL.querry("SELECT id FROM sign WHERE 1");
        try {
            while (rs.next()) {
                list.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String getMode(int id) {
        ResultSet rs = MySQL.querry("SELECT mode FROM sign WHERE id = " + id);
        try {
            if (rs.next()) {
                return rs.getString("mode");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean hasOtherSignThisServer(String server) {
        ResultSet rs = MySQL.querry("SELECT id FROM sign WHERE server = '" + server + "'");
        try {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getServer(int id) {
        ResultSet rs = MySQL.querry("SELECT server FROM sign WHERE id = " + id);
        try {
            if (rs.next()) {
                String s = rs.getString("server");
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

    public static void setServer(int id, String server) {
        if (server == null) {
            server = "NONE";
        }
        MySQL.update("UPDATE sign SET server = '" + server + "' WHERE id = " + id);
    }
}
