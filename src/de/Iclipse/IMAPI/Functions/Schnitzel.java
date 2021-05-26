package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Schnitzel {
    private final IMAPI imapi;
    private StringBuilder builder;

    public Schnitzel(IMAPI imapi) {
        this.imapi = imapi;
    }

    @IMCommand(
            name = "schnitzel",
            usage = "schnitzel.usage",
            description = "schnitzel.description",
            permissions = "im.cmd.schnitzel"
    )
    public void execute(CommandSender sender) {
        if (sender.hasPermission("im.cmd.schnitzel.*")) {
            builder = new StringBuilder();
            builder.append(imapi.getData().getDispatcher().get("schnitzel.overview", sender) + "\n");
            add(sender, "add");
            add(sender, "remove");
            add(sender, "set");
            add(sender, "get");
            sender.sendMessage(builder.toString());
        } else {
            if (sender instanceof Player) {
                imapi.getData().getDispatcher().send(sender, "schnitzel.get", "" + imapi.getData().getUserTable().getSchnitzel(UUIDFetcher.getUUID(sender.getName())));
            }else{
                imapi.getData().getDispatcher().send(sender, "cmd.noconsole");
            }
        }
    }

    @IMCommand(
            name = "add",
            usage = "schnitzel.add.usage",
            description = "schnitzel.add.description",
            maxArgs = 2,
            minArgs = 2,
            parent = "schnitzel",
            requiresOp = true,
            permissions = "im.cmd.schnitzel.add"
    )
    public void add(CommandSender sender, String name, int schnitzel) {
        if (imapi.getData().getUserTable().isUserExists(UUIDFetcher.getUUID(name))) {
            imapi.getData().getUserTable().addSchnitzel(UUIDFetcher.getUUID(name), schnitzel);
            imapi.getData().getDispatcher().send(sender, "schnitzel.add.success", name, "" + schnitzel);
        } else {
            imapi.getData().getDispatcher().send(sender, "schnitzel.notexist");
        }
    }

    @IMCommand(
            name = "remove",
            usage = "schnitzel.remove.usage",
            description = "schnitzel.remove.description",
            maxArgs = 2,
            minArgs = 2,
            parent = "schnitzel",
            requiresOp = true,
            permissions = "im.cmd.schnitzel.remove"
    )
    public void remove(CommandSender sender, String name, int schnitzel) {
        if (imapi.getData().getUserTable().isUserExists(UUIDFetcher.getUUID(name))) {
            imapi.getData().getUserTable().removeSchnitzel(UUIDFetcher.getUUID(name), schnitzel);
            imapi.getData().getDispatcher().send(sender, "schnitzel.remove.success", name, "" + schnitzel);
        } else {
            imapi.getData().getDispatcher().send(sender, "schnitzel.notexist");
        }
    }

    @IMCommand(
            name = "set",
            usage = "schnitzel.set.usage",
            description = "schnitzel.set.description",
            maxArgs = 2,
            minArgs = 2,
            parent = "schnitzel",
            requiresOp = true,
            permissions = "im.cmd.schnitzel.set"
    )
    public void set(CommandSender sender, String name, int schnitzel) {
        if (imapi.getData().getUserTable().isUserExists(UUIDFetcher.getUUID(name))) {
            imapi.getData().getUserTable().setSchnitzel(UUIDFetcher.getUUID(name), schnitzel);
            imapi.getData().getDispatcher().send(sender, "schnitzel.set.success", name, "" + schnitzel);
        } else {
            imapi.getData().getDispatcher().send(sender, "schnitzel.notexist");
        }
    }

    @IMCommand(
            name = "get",
            usage = "schnitzel.get.usage",
            description = "schnitzel.get.description",
            maxArgs = 1,
            minArgs = 1,
            parent = "schnitzel",
            requiresOp = true,
            permissions = "im.cmd.schnitzel.get"
    )
    public void get(CommandSender sender, String name) {
        if (imapi.getData().getUserTable().isUserExists(UUIDFetcher.getUUID(name))) {
            imapi.getData().getDispatcher().send(sender, "schnitzel.get.player", name, "" + imapi.getData().getUserTable().getSchnitzel(UUIDFetcher.getUUID(name)));
        } else {
            imapi.getData().getDispatcher().send(sender, "schnitzel.notexist");
        }
    }


    private void add(CommandSender sender, String command) {
        builder.append("\n" + imapi.getData().getSymbol() + "§e" + imapi.getData().getDispatcher().get("schnitzel." + command + ".usage", sender) + "§8: §7 " + imapi.getData().getDispatcher().get("schnitzel." + command + ".description", sender) + ChatColor.RESET);
    }
}
