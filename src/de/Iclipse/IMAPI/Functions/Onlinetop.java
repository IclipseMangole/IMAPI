package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Database.User;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.command.CommandSender;

import java.util.UUID;

import static de.Iclipse.IMAPI.Data.dsp;
import static de.Iclipse.IMAPI.Data.tablist;

public class Onlinetop {
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
        builder.append(dsp.get("onlinetop.title", sender, true, 5 + "") + "\n");
        User.getOnlineTop(5).forEach((uuid, onlinetime) -> {
            builder.append(dsp.get("onlinetop.row", sender, true, tablist.getPrefix(uuid) + UUIDFetcher.getName(uuid), convertToString(sender, uuid, onlinetime)) + "\n");
        });
        sender.sendMessage(builder.toString());
    }

    public static String convertToString(CommandSender p, UUID uuid, long onlinetime) {
        String s = "";
        long seconds = onlinetime / 1000;
        if (seconds / 60.0 > 1) {
            if ((seconds / (60.0 * 60.0)) > 1) {
                if (seconds / (60.0 * 60.0 * 24) > 1) {
                    if (seconds / (60.0 * 60.0 * 24 * 7) > 1) {
                        if (seconds / (60.0 * 60.0 * 24 * 30) > 1) {
                            if (seconds / (60.0 * 60.0 * 24 * 365) > 1) {
                                s += (int) (seconds / (60.0 * 60.0 * 24 * 365)) + " " + dsp.get("unit.years", p) + " ";
                                seconds %= (60.0 * 60.0 * 24 * 365);
                            }
                            s += (int) (seconds / (60.0 * 60.0 * 24 * 30)) + " " + dsp.get("unit.months", p) + " ";
                            seconds %= (60.0 * 60.0 * 24 * 30);
                        }
                        s += (int) (seconds / (60.0 * 60.0 * 24 * 7)) + " " + dsp.get("unit.weeks", p) + " ";
                        seconds %= (60.0 * 60.0 * 24 * 7);
                    }
                    s += (int) (seconds / (60.0 * 60.0 * 24)) + " " + dsp.get("unit.days", p) + " ";
                    seconds %= (60.0 * 60.0 * 24 * 24);
                }
                s += (int) (seconds / (60.0 * 60.0)) + " " + dsp.get("unit.hours", p) + " ";
                seconds %= (60.0 * 60.0);
            }
            s += (int) (seconds / 60.0) + " " + dsp.get("unit.minutes", p) + " ";
        } else {
            return seconds + " " + dsp.get("unit.seconds", p);
        }
        return s;
    }
}
