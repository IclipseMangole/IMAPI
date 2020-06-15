package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Database.User;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import net.alpenblock.bungeeperms.BungeePermsAPI;
import org.bukkit.command.CommandSender;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.UUID;

import static de.Iclipse.IMAPI.Data.dsp;

public class Playerinfo {
    @IMCommand(
            name = "playerinfo",
            maxArgs = 1,
            minArgs = 1,
            description = "",
            permissions = "im.cmd.playerinfo"
    )
    public void execute(CommandSender sender, String name) {
        UUID uuid;
        try {
            uuid = UUIDFetcher.getUUID(name);
        } catch (Exception e) {
            dsp.send(sender, "playerinfo.notExists");
            return;
        }
        if (User.isUserExists(uuid)) {
            dsp.send(sender, "playerinfo.title");
            dsp.send(sender, "playerinfo.name", name);
            dsp.send(sender, "playerinfo.rank", Data.tablist.getPrefix(uuid) + BungeePermsAPI.userMainGroup(name));
            dsp.send(sender, "playerinfo.onlinetime", (User.getOnlinetime(uuid) / (1000 * 60 * 60)) + " " + dsp.get("unit.hours", sender));
            dsp.send(sender, "playerinfo.lastseen", getLastSeen(sender, uuid));
            dsp.send(sender, "playerinfo.firstjoin", User.getFirstTime(uuid).format(getFormatter()) + "");
        } else {
            dsp.send(sender, "playerinfo.notExists");
        }
    }


    public static String getLastSeen(CommandSender p, UUID uuid) {
        String s = "";
        if (!User.isOnline(uuid)) {
            long seconds = (System.currentTimeMillis() - User.getLastTime(uuid)) / 1000;
            if (seconds / 60.0 > 1) {
                if ((seconds / (60.0 * 60.0)) > 1) {
                    if (seconds / (60.0 * 60.0 * 24) > 1) {
                        if (seconds / (60.0 * 60.0 * 24 * 7) > 1) {
                            if (seconds / (60.0 * 60.0 * 24 * 30) > 1) {
                                if (seconds / (60.0 * 60.0 * 24 * 365) > 1) {
                                    return (int) (seconds / (60.0 * 60.0 * 24 * 365)) + " " + dsp.get("unit.years", p);
                                }
                                return (int) (seconds / (60.0 * 60.0 * 24 * 30)) + " " + dsp.get("unit.months", p);
                            }
                            return (int) (seconds / (60.0 * 60.0 * 24 * 7)) + " " + dsp.get("unit.weeks", p);
                        }
                        return (int) (seconds / (60.0 * 60.0 * 24)) + " " + dsp.get("unit.days", p);
                    }
                    return (int) (seconds / (60.0 * 60.0)) + " " + dsp.get("unit.hours", p);
                }
                return (int) (seconds / 60.0) + " " + dsp.get("unit.minutes", p);
            }
            return seconds + " " + dsp.get("unit.seconds", p);
        } else {
            return dsp.get("playerinfo.online", p);
        }
    }

    public DateTimeFormatter getFormatter() {
        return new DateTimeFormatterBuilder()
                .appendValue(ChronoField.DAY_OF_MONTH, 2, 2, SignStyle.NOT_NEGATIVE)
                .appendLiteral(".")
                .appendValue(ChronoField.MONTH_OF_YEAR, 2, 2, SignStyle.NOT_NEGATIVE)
                .appendLiteral(".")
                .appendValue(ChronoField.YEAR, 4, 4, SignStyle.NOT_NEGATIVE)
                .appendLiteral(' ')
                .appendValue(ChronoField.HOUR_OF_DAY, 2, 2, SignStyle.NOT_NEGATIVE)
                .appendLiteral(":")
                .appendValue(ChronoField.MINUTE_OF_HOUR, 2, 2, SignStyle.NOT_NEGATIVE)
                .toFormatter();
    }
}
