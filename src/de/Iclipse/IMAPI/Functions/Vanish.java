package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Database.Server;
import de.Iclipse.IMAPI.Database.UserSettings;
import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

import static de.Iclipse.IMAPI.Data.dsp;
import static de.Iclipse.IMAPI.Data.instance;

public class Vanish {
    @IMCommand(
            name = "vanish",
            aliases = {"vv"},
            description = "vanish.description",
            usage = "vanish.usage",
            noConsole = true,
            minArgs = 0,
            maxArgs = 0,
            permissions = "im.cmd.vanish"
    )
    public void execute(Player p){
            if(!isVanish(UUIDFetcher.getUUID(p.getName()))){
                setVanish(p, true);
                dsp.send(p, "vanish.vanish");
                Bukkit.getOnlinePlayers().forEach(entry ->{
                    entry.hidePlayer(instance, p);
                });
                if (Data.updatePlayers)
                    Server.setPlayers(IMAPI.getServerName(), Server.getPlayers(IMAPI.getServerName()) - 1);
            }else {
                setVanish(p, false);
                dsp.send(p, "vanish.visible");
                Bukkit.getOnlinePlayers().forEach(entry -> {
                    if (!UserSettings.getString(UUIDFetcher.getUUID(entry.getName()), "visibility").equals("NOBODY")) {
                        entry.showPlayer(instance, p);
                    }
                });
                if (Data.updatePlayers)
                    Server.setPlayers(IMAPI.getServerName(), Server.getPlayers(IMAPI.getServerName()) + 1);
            }
    }

    public static void setVanish(Player p, boolean vanish) {
        UserSettings.setBoolean(UUIDFetcher.getUUID(p.getName()), "vanish", vanish);
    }

    public static boolean isVanish(UUID uuid) {
        return UserSettings.getBoolean(uuid, "vanish");
    }

    public static ArrayList<Player> getVanishsOnServer() {
        ArrayList<Player> players = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(entry -> {
            if (isVanish(UUIDFetcher.getUUID(entry.getName()))) {
                players.add(entry);
            }
        });
        return players;
    }
}
