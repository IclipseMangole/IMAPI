package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.command.CommandSender;

import static de.Iclipse.IMAPI.Data.dsp;
import static de.Iclipse.IMAPI.Data.restart;

public class IMRestart {
    @IMCommand(
            name = "imrestart",
            permissions = "im.cmd.restart",
            usage = "imrestart.usage",
            description = "imrestart.description",
            maxArgs = 2,
            minArgs = 0
    )
    public void execute(CommandSender sender, Integer i, String s) {
        String unit;
        if (i == null && s == null) {
            System.out.println("Both null");
            unit = "seconds";
            restart = 60;
            i = 60;
        } else if (i != null && s == null) {
            System.out.println("Unit null");
            restart = i * 60;
            unit = "minutes";
        } else {
            if (s.equalsIgnoreCase("s")) {
                System.out.println("Unit s");
                unit = "seconds";
                restart = i;
            } else if (s.equalsIgnoreCase("m")) {
                System.out.println("Unit m");
                unit = "minutes";
                restart = i * 60;
            } else if (s.equalsIgnoreCase("h")) {
                System.out.println("Both h");
                unit = "hours";
                restart = i * 60 * 60;
            } else {
                dsp.send(sender, "imrestart.wrongunit");
                return;
            }

        }
        dsp.send(sender, "imrestart.planned", "" + i, dsp.get("unit." + unit, sender));
    }
}
