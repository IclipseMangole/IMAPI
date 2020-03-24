package de.Iclipse.IMAPI.Functions.Commands;

import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmd_servers {

    @IMCommand(
            name = "servers",
            maxArgs = 0,
            permissions = {"im.cmd.servers"}
    )
    public void execute(CommandSender sender){
        System.out.println("Wird ausgeführt!");
        StringBuilder builder = new StringBuilder();
        IMAPI.pml.getServers().forEach(entry ->{
            builder.append(entry + ", ");
        });
        sender.sendMessage("Servers: " + builder.toString());
    }
}
