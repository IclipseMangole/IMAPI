package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import net.alpenblock.bungeeperms.BungeePermsAPI;
import org.bukkit.command.CommandSender;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.UUID;


public class Playerinfo {

    private final IMAPI imapi;

    public Playerinfo(IMAPI imapi) {
        this.imapi = imapi;
    }

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
            imapi.getData().getDispatcher().send(sender, "playerinfo.notExists");
            return;
        }
        if (imapi.getData().getUserTable().isUserExists(uuid)) {
            imapi.getData().getDispatcher().send(sender, "playerinfo.title");
            imapi.getData().getDispatcher().send(sender, "playerinfo.name", name);
            imapi.getData().getDispatcher().send(sender, "playerinfo.rank", imapi.getData().getTablist().getPrefix(uuid) + BungeePermsAPI.userMainGroup(name));
            imapi.getData().getDispatcher().send(sender, "playerinfo.onlinetime", (imapi.getData().getUserTable().getOnlinetime(uuid) / (1000 * 60 * 60)) + " " + imapi.getData().getDispatcher().get("unit.hours", sender));
            imapi.getData().getDispatcher().send(sender, "playerinfo.lastseen", getLastSeen(sender, uuid));
            imapi.getData().getDispatcher().send(sender, "playerinfo.firstjoin", imapi.getData().getUserTable().getFirstTime(uuid).format(getFormatter()) + "");
        } else {
            imapi.getData().getDispatcher().send(sender, "playerinfo.notExists");
        }
    }


    public String getLastSeen(CommandSender p, UUID uuid) {
        String s = "";
        if (!imapi.getData().getUserTable().isOnline(uuid)) {
            long seconds = (System.currentTimeMillis() - imapi.getData().getUserTable().getLastTime(uuid)) / 1000;
            if (seconds / 60.0 > 1) {
                if ((seconds / (60.0 * 60.0)) > 1) {
                    if (seconds / (60.0 * 60.0 * 24) > 1) {
                        if (seconds / (60.0 * 60.0 * 24 * 7) > 1) {
                            if (seconds / (60.0 * 60.0 * 24 * 30) > 1) {
                                if (seconds / (60.0 * 60.0 * 24 * 365) > 1) {
                                    return (int) (seconds / (60.0 * 60.0 * 24 * 365)) + " " + imapi.getData().getDispatcher().get("unit.years", p);
                                }
                                return (int) (seconds / (60.0 * 60.0 * 24 * 30)) + " " + imapi.getData().getDispatcher().get("unit.months", p);
                            }
                            return (int) (seconds / (60.0 * 60.0 * 24 * 7)) + " " + imapi.getData().getDispatcher().get("unit.weeks", p);
                        }
                        return (int) (seconds / (60.0 * 60.0 * 24)) + " " + imapi.getData().getDispatcher().get("unit.days", p);
                    }
                    return (int) (seconds / (60.0 * 60.0)) + " " + imapi.getData().getDispatcher().get("unit.hours", p);
                }
                return (int) (seconds / 60.0) + " " + imapi.getData().getDispatcher().get("unit.minutes", p);
            }
            return seconds + " " + imapi.getData().getDispatcher().get("unit.seconds", p);
        } else {
            return imapi.getData().getDispatcher().get("playerinfo.online", p);
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
