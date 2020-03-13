package de.Iclipse.IMAPI.Functions.Commands;

import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.entity.Player;

import static de.Iclipse.IMAPI.Data.dsp;

public class cmd_restart {
    @IMCommand(
            name = "restart",
            permissions = "im.cmd.restart",
            usage = "restart.usage",
            description = "restart.description",
            maxArgs = 2,
            minArgs = 0
    )
    public void execute(Player p, Integer i, String s){
        if(i == null && s == null){

        }else if(i != null && s == null){

        }else{
            dsp.send(p, "restart.both");
        }
    }
}
