package de.Iclipse.IMAPI.Functions.MySQL;

import de.Iclipse.IMAPI.Util.Dispatching.Language;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class MySQL_News {
    public static void createNewsTable(){
        MySQL.update("CREATE TABLE IF NOT EXISTS news (id MEDIUMINT NOT NULL AUTO_INCREMENT, titleDE VARCHAR(30), titleEN VARCHAR(30), textDE VARCHAR(1000), textEN VARCHAR(300), creator VARCHAR(50), created DATETIME, PRIMARY KEY (id))");
    }

    public static void createNews(String titleDE, String titleEN, String messageDE, String messageEN, UUID uuid){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = Date.from(Instant.now());
        MySQL.update("INSERT INTO news(NULL, '" + titleDE + "', '" + titleEN + "', '" + messageDE + "', '" + messageEN + "', '" + uuid.toString() + "', '" + sdf.format(time) + "'");
    }

    public static void deleteNews(int id){
        MySQL.update("DELETE FROM news WHERE id = " + id);
    }

    public static boolean isTitleExists(String title){
        try{
            ResultSet rs = MySQL.querry("SELECT id FROM user WHERE titleDE = '" + title + "' OR titleEN = '" + title + "'");
            return rs.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    public static int getID(String title){
        try{
            ResultSet rs = MySQL.querry("SELECT id FROM user WHERE titleDE = '" + title + "' OR titleEN = '" + title + "'");
            while(rs.next()){
                return rs.getInt("id");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public static String getTitle(int id, Language lang){
        try {
            ResultSet rs = MySQL.querry("SELECT title" + lang.getShortcut() + " from news WHERE id = '" + id + "'");
            while (rs.next()) {
                return rs.getString("textDE" + lang.getShortcut());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    public static String getText(int id, Language lang){
        try {
            ResultSet rs = MySQL.querry("SELECT text" + lang.getShortcut() + " from news WHERE id = " + id + "");
            while (rs.next()) {
                return rs.getString("textDE" + lang.getShortcut());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    public static ArrayList<Integer> getNews(){
        ArrayList<Integer> list = new ArrayList<>();
        try {
            ResultSet rs = MySQL.querry("SELECT id from news WHERE 1");
            while (rs.next()) {
                list.add(rs.getInt("id"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    public static UUID getCreator(int id){
        try {
            ResultSet rs = MySQL.querry("SELECT creator from news WHERE id = " + id + "");
            while (rs.next()) {
                return UUID.fromString(rs.getString("creator"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static LocalDateTime getCreated(int id) {
        ResultSet rs = MySQL.querry("SELECT  created FROM user WHERE id = " + id);
        try {
            while (rs.next()) {
                return LocalDateTime.of(rs.getDate("created").toLocalDate(), rs.getTime("created").toLocalTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}