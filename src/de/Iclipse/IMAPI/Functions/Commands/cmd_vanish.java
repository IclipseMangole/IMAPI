package de.Iclipse.IMAPI.Functions.Commands;

import de.Iclipse.IMAPI.Functions.MySQL.MySQL_UserSettings;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.UUID;

import static de.Iclipse.IMAPI.Data.dsp;
import static de.Iclipse.IMAPI.Data.instance;

public class cmd_vanish {
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
            }else{
                setVanish(p, false);
                dsp.send(p, "vanish.visible");
                Bukkit.getOnlinePlayers().forEach(entry ->{
                    if(!MySQL_UserSettings.getString(UUIDFetcher.getUUID(entry.getName()), "visibility").equals("NOBODY")) {
                        entry.showPlayer(instance, p);
                    }
                });
            }
    }

    public void setVanish(Player p, boolean vanish){
        MySQL_UserSettings.setBoolean(UUIDFetcher.getUUID(p.getName()), "vanish", vanish);
    }

    public boolean isVanish(UUID uuid){
        return MySQL_UserSettings.getBoolean(uuid, "vanish");
    }
}
