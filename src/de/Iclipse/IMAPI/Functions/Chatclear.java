package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.command.CommandSender;

public class Chatclear {
    @IMCommand(
            name = "chatclear",
            aliases = {"clear", "cc"},
            maxArgs = 0,
            usage = "chatclear.usage",
            description = "chatclear.description",
            permissions = "im.cmd.chatclear"
    )
    public void chatclear(CommandSender sender) {
        for (int i = 0; i < 18; i++) {
            sender.sendMessage("");
        }
    }
}
