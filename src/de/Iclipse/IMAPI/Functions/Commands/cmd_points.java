package de.Iclipse.IMAPI.Functions.Commands;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Functions.MySQL.MySQL_User;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.Iclipse.IMAPI.Data.prefix;

public class cmd_points {
    StringBuilder builder;

    @IMCommand(
            name = "points",
            usage = "/points",
            description = "Verwaltet die Points der Spieler"
    )
    public void execute(CommandSender sender) {
        builder = new StringBuilder();
        builder.append(prefix + "§7§lHilfsübersicht:§r\n");
        add("give", "Gibt Points an einen anderen Spieler!");
        add("add", "Fürgt Points hinzu");
        add("remove", "Entfernt Points");
        add("set", "Stetzt Points");
        add("get", "Gibt die Points aus");
        sender.sendMessage(builder.toString());
    }

    @IMCommand(
            name = "give",
            usage = "/points give <Points> <Player>",
            minArgs = 2,
            maxArgs = 2,
            parent = "points",
            noConsole = true
    )
    public void give(Player p, int points, String name) {
        if (MySQL_User.isUserExists(UUIDFetcher.getUUID(name))) {
            if (MySQL_User.getPoints(UUIDFetcher.getUUID(p.getName())) >= points) {
                MySQL_User.removePoints(UUIDFetcher.getUUID(p.getName()), points);
                MySQL_User.addPoints(UUIDFetcher.getUUID(name), points);
                Bukkit.getOnlinePlayers().forEach(entry -> {
                    if (entry.getName().equals(name)) {
                        entry.sendMessage(prefix + "§aDir wurden §e" + points + "§a von §e" + p.getName() + "§a gegeben!");
                    }
                });
                p.sendMessage(prefix + "§aDu hast §e" + points + "§a Points an §e" + name + "§a abgegeben!");
            } else {
                p.sendMessage(prefix + "§cDu hast nicht genug Points!");
            }
        } else {
            p.sendMessage(prefix + "Der Spieler existiert nicht!");
        }
    }

    @IMCommand(
            name = "add",
            usage = "/points add <Points> <Player>",
            maxArgs = 2,
            minArgs = 2,
            parent = "points",
            requiresOp = true
    )
    public void add(CommandSender sender, int points, String name) {
        if (MySQL_User.isUserExists(UUIDFetcher.getUUID(name))) {
            MySQL_User.addPoints(UUIDFetcher.getUUID(name), points);
            sender.sendMessage("§e" + points + "§a Points wurden hinzugefügt!");
        } else {
            sender.sendMessage(prefix + "Der Spieler existiert nicht!");
        }
    }

    @IMCommand(
            name = "remove",
            usage = "/points remove <Points> <Player>",
            maxArgs = 2,
            minArgs = 2,
            parent = "points",
            requiresOp = true
    )
    public void remove(CommandSender sender, int points, String name) {
        if (MySQL_User.isUserExists(UUIDFetcher.getUUID(name))) {
            MySQL_User.removePoints(UUIDFetcher.getUUID(name), points);
            sender.sendMessage("§e" + points + "§a Points wurden abgezogen!");
        } else {
            sender.sendMessage(prefix + "Der Spieler existiert nicht!");
        }
    }

    @IMCommand(
            name = "set",
            usage = "/points set <Points> <Player>",
            maxArgs = 2,
            minArgs = 2,
            parent = "points",
            requiresOp = true
    )
    public void set(CommandSender sender, int points, String name) {
        if (MySQL_User.isUserExists(UUIDFetcher.getUUID(name))) {
            MySQL_User.setPoints(UUIDFetcher.getUUID(name), points);
            sender.sendMessage("§aDer Kontostand des Users wurde auf §e " + points + "§a Points gesetzt!");
        } else {
            sender.sendMessage(prefix + "Der Spieler existiert nicht!");
        }
    }

    @IMCommand(
            name = "get",
            usage = "/points get <Player>",
            maxArgs = 1,
            minArgs = 1,
            parent = "points",
            requiresOp = true
    )
    public void get(CommandSender sender, String name) {
        if (MySQL_User.isUserExists(UUIDFetcher.getUUID(name))) {
            sender.sendMessage("§aDer Kontostand des Users beträgt:§e " + MySQL_User.getPoints(UUIDFetcher.getUUID(name)) + "§a Points");
        } else {
            sender.sendMessage(prefix + "Der Spieler existiert nicht!");
        }
    }


    private void add(String command, String usage) {
        builder.append("\n" + Data.symbol + "§e/points " + command + "§8: §7 " + usage + ChatColor.RESET + "\n");
    }
}
