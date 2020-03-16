package de.Iclipse.IMAPI.Functions.Commands;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.Iclipse.IMAPI.Data.prefix;


/**
 * Created by Yannick who could get really angry if somebody steal his code!
 * ~Yannick on 13.06.2019 at 20:41 o´ clock
 */
public class cmd_gamemode {
    @IMCommand(
            name = "gamemode",
            aliases = {"gm"},
            noConsole = true,
            permissions = "px.gamemode",
            minArgs = 0,
            maxArgs = 2,
            usage = "/gamemode (Gamemode) (Player)"
    )

    public void execute(CommandSender sender, String[] args) {
        if (args == null) {
            if (sender instanceof Player) {
                if (((Player) sender).getGameMode().equals(GameMode.SPECTATOR) || ((Player) sender).getGameMode().equals(GameMode.CREATIVE)) {
                    ((Player) sender).setGameMode(GameMode.SURVIVAL);
                } else {
                    ((Player) sender).setGameMode(GameMode.CREATIVE);
                }
                sender.sendMessage(prefix + "Dein Gamemode ist nun §3" + ((Player) sender).getGameMode().toString());
            } else {
                sender.sendMessage(prefix + "§cDu kannst deinen gamemode nicht ändern!");
            }
        } else if (args.length == 1) {
            final boolean[] player = {false};
            Bukkit.getOnlinePlayers().forEach(entry -> {
                if (entry.getName().equalsIgnoreCase(args[0])) {
                    if (entry.getGameMode().equals(GameMode.SPECTATOR) || entry.getGameMode().equals(GameMode.CREATIVE)) {
                        entry.setGameMode(GameMode.SURVIVAL);
                    } else {
                        entry.setGameMode(GameMode.CREATIVE);
                    }
                    entry.sendMessage(prefix + "Dein Gamemode ist nun §3" + entry.getGameMode().toString());
                    sender.sendMessage(prefix + "Gamemode von §3" + entry.getName() + "§7 wurde erfolgreich zu §3" + ((Player) sender).getGameMode().toString() + "§7 geändert!");
                    player[0] = true;
                }
            });
            if (player[0] = false) {
                if (sender instanceof Player) {
                    if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("survival")) {
                        ((Player) sender).setGameMode(GameMode.SURVIVAL);
                    } else if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("creative")) {
                        ((Player) sender).setGameMode(GameMode.CREATIVE);
                    } else if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("adventure")) {
                        ((Player) sender).setGameMode(GameMode.ADVENTURE);
                    } else if (args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("spectator")) {
                        ((Player) sender).setGameMode(GameMode.SPECTATOR);
                    } else {
                        sender.sendMessage(prefix + "Der Gamemode §3" + args[0] + "§7 existiert nicht!");
                        return;
                    }
                    sender.sendMessage(prefix + "Dein Gamemode ist nun §3" + ((Player) sender).getGameMode().toString());
                } else {
                    sender.sendMessage(prefix + "§cDu kannst deinen Gamemode nicht ändern!");
                }
            }
        } else {
            final boolean[] player = {false};
            final Player[] p = new Player[1];
            Bukkit.getOnlinePlayers().forEach(entry -> {
                if (entry.getName().equalsIgnoreCase(args[1])) {
                    if (entry.getGameMode().equals(GameMode.SPECTATOR) || entry.getGameMode().equals(GameMode.CREATIVE)) {
                        entry.setGameMode(GameMode.SURVIVAL);
                    } else {
                        entry.setGameMode(GameMode.CREATIVE);
                    }
                    entry.sendMessage(prefix + "Dein Gamemode ist nun §3" + entry.getGameMode().toString());
                    sender.sendMessage(prefix + "Gamemode von §3" + entry.getName() + "§7 wurde erfolgreich zu §3" + ((Player) sender).getGameMode().toString() + "§7 geändert!");
                    player[0] = true;
                    p[0] = entry;
                }
            });
            if (player[0] = false) {
                sender.sendMessage(prefix + "Der Spieler §3" + args[1] + "§7 ist nicht online!");
                return;
            }

            if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("survival")) {
                ((Player) sender).setGameMode(GameMode.SURVIVAL);
            } else if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("creative")) {
                ((Player) sender).setGameMode(GameMode.CREATIVE);
            } else if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("adventure")) {
                ((Player) sender).setGameMode(GameMode.ADVENTURE);
            } else if (args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("spectator")) {
                ((Player) sender).setGameMode(GameMode.SPECTATOR);
            } else {
                sender.sendMessage(prefix + "Der Gamemode §3" + args[0] + "§7 existiert nicht!");
                return;
            }
            sender.sendMessage(prefix + "Gamemode von §3" + p[0].getName() + "§7 wurde erfolgreich zu §3" + ((Player) sender).getGameMode().toString() + "§7 geändert!");
        }


    }
}
