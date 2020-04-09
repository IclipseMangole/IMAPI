package de.Iclipse.IMAPI.Functions.Commands;

import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.command.CommandSender;

import static de.Iclipse.IMAPI.Data.dsp;
import static de.Iclipse.IMAPI.Data.restart;

public class cmd_restart {
    @IMCommand(
            name = "restart",
            permissions = "im.cmd.restart",
            usage = "restart.usage",
            description = "restart.description",
            maxArgs = 2,
            minArgs = 0
    )
    public void execute(CommandSender sender, Integer i, String s) {
        if (i == null && s == null) {
            restart = 60;
        } else if (i != null && s == null) {
            restart = i * 60;
        } else {
            String unit;
            if (s.equalsIgnoreCase("s")) {
                unit = "seconds";
                restart = i;
            } else if (s.equalsIgnoreCase("m")) {
                unit = "minutes";
                restart = i * 60;
            } else if (s.equalsIgnoreCase("h")) {
                unit = "hours";
                restart = i * 60 * 60;
            } else {
                dsp.send(sender, "restart.wrongunit");
                return;
            }

            dsp.send(sender, "restart.both", "" + i, dsp.get("unit." + unit, sender));
        }
    }
}
