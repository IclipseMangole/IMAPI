package de.Iclipse.IMAPI.Functions.Commands;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class cmd_apireload {
    @IMCommand(
            name = "apireload",
            permissions = "im.cmd.apireload",
            maxArgs = 0
    )
    public void execute(CommandSender sender){
        sender.sendMessage(Data.prefix + "Wird reloadet!");
        Bukkit.getScheduler().runTaskLater(Data.instance, new Runnable() {
            @Override
            public void run() {
                Data.instance.getPluginLoader().enablePlugin(Data.instance);
            }
        }, 20);
        Data.instance.getPluginLoader().disablePlugin(Data.instance);
    }
}
