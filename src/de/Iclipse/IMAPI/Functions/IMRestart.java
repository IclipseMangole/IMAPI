package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.command.CommandSender;


public class IMRestart {

    private final IMAPI imapi;

    public IMRestart(IMAPI imapi) {
        this.imapi = imapi;
    }

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
        int restart = -1;
        if (i == null && s == null) {
            unit = "seconds";
            restart = 60;
            i = 60;
        } else if (i != null && s == null) {
            restart = i * 60;
            unit = "minutes";
        } else {
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
                imapi.getData().getDispatcher().send(sender, "imrestart.wrongunit");
                return;
            }

        }
        if(restart != -1) imapi.getData().getScheduler().setRestart(restart);
        imapi.getData().getDispatcher().send(sender, "imrestart.planned", "" + i, imapi.getData().getDispatcher().get("unit." + unit, sender));
    }
}
