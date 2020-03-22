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
            aliases = "vv",
            description = "vanish.description",
            usage = "vanish.usage",
            noConsole = true,
            minArgs = 0,
            maxArgs = 1,
            permissions = "im.cmd.vanish"
    )
    public void execute(Player p, Integer vanish){
        if(vanish == null){
            if(getVanish(UUIDFetcher.getUUID(p.getName())) == 0){
                p.setGameMode(GameMode.CREATIVE);
                setVanish(p, 1);
                dsp.send(p, "vanish.vanish", "" + 1);
                Bukkit.getOnlinePlayers().forEach(entry ->{
                    entry.hidePlayer(instance, p);
                });
            }else{
                setVanish(p, 0);
                dsp.send(p, "vanish.visible");
                Bukkit.getOnlinePlayers().forEach(entry ->{
                    if(!MySQL_UserSettings.getString(UUIDFetcher.getUUID(entry.getName()), "visibility").equals("NOBODY")) {
                        entry.showPlayer(instance, p);
                    }
                });
            }
        }else{
            if(getVanish(UUIDFetcher.getUUID(p.getName())) != vanish){
                setVanish(p, vanish);
                if(vanish == 0) {
                    dsp.send(p, "vanish.visible");
                }else{
                    dsp.send(p, "vanish.vanish", "" + vanish);
                }
            }else{
                dsp.send(p, "vanish.same");
            }
        }
    }

    public void setVanish(Player p, int vanish){
        MySQL_UserSettings.setInt(UUIDFetcher.getUUID(p.getName()), "vanish", vanish);
    }

    public int getVanish(UUID uuid){
        return MySQL_UserSettings.getInt(uuid, "vanish");
    }
}
