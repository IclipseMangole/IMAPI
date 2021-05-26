package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.command.CommandSender;


public class Onlinetop {
    
    private final IMAPI imapi;

    public Onlinetop(IMAPI imapi) {
        this.imapi = imapi;
    }

    @IMCommand(
            name = "onlinetop",
            description = "onlinetop.description",
            usage = "onlinetop.usage",
            minArgs = 0,
            maxArgs = 0,
            permissions = "im.cmd.onlinetop"
    )
    public void execute(CommandSender sender) {
        StringBuilder builder = new StringBuilder();
        builder.append(imapi.getData().getDispatcher().get("onlinetop.title", sender, true, 5 + "") + "\n");
        imapi.getData().getUserTable().getOnlineTop(5).forEach((uuid, onlinetime) -> {
            builder.append(imapi.getData().getDispatcher().get("onlinetop.row", sender, true, imapi.getData().getTablist().getPrefix(uuid) + UUIDFetcher.getName(uuid), convertToString(sender, onlinetime)) + "\n");
        });
        sender.sendMessage(builder.toString());
    }

    public String convertToString(CommandSender p, long onlinetime) {
        String s = "";
        long seconds = onlinetime / 1000;
        if (seconds / 60.0 > 1) {
            if ((seconds / (60.0 * 60.0)) > 1) {
                if (seconds / (60.0 * 60.0 * 24) > 1) {
                    if (seconds / (60.0 * 60.0 * 24 * 7) > 1) {
                        if (seconds / (60.0 * 60.0 * 24 * 30) > 1) {
                            if (seconds / (60.0 * 60.0 * 24 * 365) > 1) {
                                s += (int) (seconds / (60.0 * 60.0 * 24 * 365)) + " " + imapi.getData().getDispatcher().get("unit.years", p) + " ";
                                seconds %= (60.0 * 60.0 * 24 * 365);
                            }
                            s += (int) (seconds / (60.0 * 60.0 * 24 * 30)) + " " + imapi.getData().getDispatcher().get("unit.months", p) + " ";
                            seconds %= (60.0 * 60.0 * 24 * 30);
                        }
                        s += (int) (seconds / (60.0 * 60.0 * 24 * 7)) + " " + imapi.getData().getDispatcher().get("unit.weeks", p) + " ";
                        seconds %= (60.0 * 60.0 * 24 * 7);
                    }
                    s += (int) (seconds / (60.0 * 60.0 * 24)) + " " + imapi.getData().getDispatcher().get("unit.days", p) + " ";
                    seconds %= (60.0 * 60.0 * 24 * 24);
                }
                s += (int) (seconds / (60.0 * 60.0)) + " " + imapi.getData().getDispatcher().get("unit.hours", p) + " ";
                seconds %= (60.0 * 60.0);
            }
            s += (int) (seconds / 60.0) + " " + imapi.getData().getDispatcher().get("unit.minutes", p) + " ";
        } else {
            return seconds + " " + imapi.getData().getDispatcher().get("unit.seconds", p);
        }
        return s;
    }
}
