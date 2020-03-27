package de.Iclipse.IMAPI.Functions.Commands;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.Iclipse.IMAPI.Data.dsp;
import static de.Iclipse.IMAPI.Data.prefix;

public class cmd_world {
    StringBuilder builder;
    @IMCommand(
            name = "world",
            permissions = "im.cmd.world",
            usage = "world.world",
            description = "world.description",
            minArgs = 0,
            maxArgs = 0
    )
    public void world(CommandSender sender){
        builder = new StringBuilder();
        builder.append(prefix + "§7§lHilfsübersicht:§r\n");
        add(sender, "add");
        sender.sendMessage(builder.toString());
    }

    @IMCommand(
            name = "create",
            permissions = "im.cmd.world.create",
            usage = "world.create.description",
            description = "world.create.description",
            minArgs = 1,
            maxArgs = 1,
            parent = "world"
    )
    public void create(CommandSender sender){

    }



    private void add(CommandSender sender, String command) {
        builder.append("\n" + Data.symbol + "§e" + dsp.get("world." + command + ".usage", sender) + "§8: §7 " + dsp.get("world." + command + ".description", sender) + ChatColor.RESET);
    }
}
