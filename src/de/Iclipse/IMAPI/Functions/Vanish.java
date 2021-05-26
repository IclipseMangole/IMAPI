package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;


public class Vanish {
    
    private final IMAPI imapi;

    public Vanish(IMAPI imapi) {
        this.imapi = imapi;
    }

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
                imapi.getData().getDispatcher().send(p, "vanish.vanish");
                Bukkit.getOnlinePlayers().forEach(entry ->{
                    entry.hidePlayer(imapi, p);
                });
                if (imapi.getData().isUpdatePlayers())
                    imapi.getData().getServerTable().setPlayers(imapi.getServerName(), imapi.getData().getServerTable().getPlayers(imapi.getServerName()) - 1);
            }else {
                setVanish(p, false);
                imapi.getData().getDispatcher().send(p, "vanish.visible");
                Bukkit.getOnlinePlayers().forEach(entry -> {
                    if (!imapi.getData().getUserSettingsTable().getString(UUIDFetcher.getUUID(entry.getName()), "visibility").equals("NOBODY")) {
                        entry.showPlayer(imapi, p);
                    }
                });
                if (imapi.getData().isUpdatePlayers())
                    imapi.getData().getServerTable().setPlayers(imapi.getServerName(), imapi.getData().getServerTable().getPlayers(imapi.getServerName()) + 1);
            }
    }

    public static void setVanish(Player p, boolean vanish) {
        IMAPI.getInstance().getData().getUserSettingsTable().setBoolean(UUIDFetcher.getUUID(p.getName()), "vanish", vanish);
    }

    public static boolean isVanish(UUID uuid) {
        return IMAPI.getInstance().getData().getUserSettingsTable().getBoolean(uuid, "vanish");
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
