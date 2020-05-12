package de.Iclipse.IMAPI.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Mode {
    public static void createModeTable() {
        MySQL.update("CREATE TABLE IF NOT EXISTS mode(name VARCHAR(20), PRIMARY KEY(name))");
    }

    public static void registerMode(String name) {
        MySQL.update("INSERT INTO `mode` VALUES ('" + name + "')");
    }

    public static ArrayList<String> getModes() {
        ArrayList<String> list = new ArrayList<>();
        try {
            ResultSet rs = MySQL.querry("SELECT name FROM mode WHERE 1");
            while (rs.next()) {
                String s = rs.getString("name");
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void deleteMode(String name) {
        MySQL.update("DELETE FROM `mode` WHERE name = '" + name + "'");
    }

    public static boolean isModeExists(String name) {
        try {
            ResultSet rs = MySQL.querry("SELECT name FROM `mode` WHERE name = '" + name + "'");
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public static void setModeName(String name, String newname) {
        MySQL.update("UPDATE `mode` SET modename = '" + newname + "' WHERE modename = '" + name + "'");
    }

}
