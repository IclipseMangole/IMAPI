package de.Iclipse.IMAPI.Functions.Commands;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Functions.MySQL.MySQL_User;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.Dispatching.Language;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.Iclipse.IMAPI.Data.*;

public class cmd_schnitzel {
    StringBuilder builder;

    @IMCommand(
            name = "schnitzel",
            usage = "schnitzel.usage",
            description = "schnitzel.description",
            permissions = "im.cmd.schnitzel"
    )
    public void execute(CommandSender sender) {
        if(sender.hasPermission("im.cmd.schnitzel.*")) {
            builder = new StringBuilder();
            builder.append(prefix + "§7§lHilfsübersicht:§r\n");
            add(sender, "add");
            add(sender, "remove");
            add(sender, "set");
            add(sender, "get");
            sender.sendMessage(builder.toString());
        }else{
            if(sender instanceof Player){
                dsp.send(sender, "schnitzel.get", "" + MySQL_User.getSchnitzel(UUIDFetcher.getUUID(sender.getName())));
            }else{
                dsp.send(sender, "cmd.noconsole");
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
        if (MySQL_User.isUserExists(UUIDFetcher.getUUID(name))) {
            MySQL_User.addSchnitzel(UUIDFetcher.getUUID(name), schnitzel);
            dsp.send(sender, "schnitzel.add.success", name, "" + schnitzel);
        } else {
            dsp.send(sender, "schnitzel.notexist");
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
        if (MySQL_User.isUserExists(UUIDFetcher.getUUID(name))) {
            MySQL_User.removeSchnitzel(UUIDFetcher.getUUID(name), schnitzel);
            dsp.send(sender, "schnitzel.remove.success", name, "" + schnitzel);
        } else {
            dsp.send(sender, "schnitzel.notexist");
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
        if (MySQL_User.isUserExists(UUIDFetcher.getUUID(name))) {
            MySQL_User.setSchnitzel(UUIDFetcher.getUUID(name), schnitzel);
            dsp.send(sender, "schnitzel.set.success", name, "" + schnitzel);
        } else {
            dsp.send(sender, "schnitzel.notexist");
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
        if (MySQL_User.isUserExists(UUIDFetcher.getUUID(name))) {
            dsp.send(sender, "schnitzel.get.player", name, "" + MySQL_User.getSchnitzel(UUIDFetcher.getUUID(name)));
        } else {
            dsp.send(sender, "schnitzel.notexist");
        }
    }


    private void add(CommandSender sender, String command) {
        Language lang;
        if(sender instanceof Player){
            lang = MySQL_User.getLanguage(UUIDFetcher.getUUID(sender.getName()));
        }else{
            lang = defaultLang;
        }
        builder.append("\n" + Data.symbol + "§e" + dsp.get("schnitzel." + command + ".usage", lang) + "§8: §7 " + dsp.get("schnitzel." + command + ".description", lang) + ChatColor.RESET);
    }
}
