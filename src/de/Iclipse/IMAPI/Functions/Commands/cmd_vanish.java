package de.Iclipse.IMAPI.Functions.Commands;

import de.Iclipse.IMAPI.Functions.MySQL.MySQL_UserSettings;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.entity.Player;

import java.util.UUID;

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
                setVanish(p, 1);
            }else{
                setVanish(p, 0);
            }
        }else{

        }
    }

    public void setVanish(Player p, int vanish){
        MySQL_UserSettings.setInt(UUIDFetcher.getUUID(p.getName()), "vanish", vanish);
    }

    public int getVanish(UUID uuid){
        return MySQL_UserSettings.getInt(uuid, "vanish");
    }
}
