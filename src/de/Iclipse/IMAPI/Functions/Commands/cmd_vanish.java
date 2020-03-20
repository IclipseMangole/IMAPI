package de.Iclipse.IMAPI.Functions.Commands;

import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.entity.Player;

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

        }
    }
}
