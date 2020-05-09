package de.Iclipse.IMAPI.Database;

public class Server {
    public static void createServerTable() {
        MySQL.update("CREATE TABLE IF NOT EXISTS server (name VARCHAR(20), port INT(10), maxplayer )");
    }

}
