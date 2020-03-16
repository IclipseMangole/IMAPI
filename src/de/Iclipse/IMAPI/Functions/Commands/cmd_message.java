package de.Iclipse.IMAPI.Functions.Commands;

import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.command.CommandSender;

import static de.Iclipse.IMAPI.Data.dsp;

public class cmd_message {
    @IMCommand(
            name = "message",
            minArgs = 2,
            permissions = "im.command.msg",
            usage = "message.usage",
            description = "message.description",
            aliases = {"msg", "tell"}
    )
    public void execute(CommandSender sender, String receiver, String... strings){
        try{

        }catch (Exception e){
            dsp.send(sender, "message.noreceiver");
        }
    }
}
