package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.Iclipse.IMAPI.Data.dsp;


/**
 * Created by Yannick who could get really angry if somebody steal his code!
 * ~Yannick on 13.06.2019 at 20:41 o´ clock
 */
public class Gamemode {
    @IMCommand(
            name = "gamemode",
            aliases = {"gm"},
            noConsole = true,
            permissions = "im.cmd.gamemode",
            minArgs = 0,
            maxArgs = 2,
            usage = "gamemode.usage",
            description = "gamemode.description"
    )

    public void execute(CommandSender sender, String[] args) {
        if (args == null) {
            if (sender instanceof Player) {
                if (((Player) sender).getGameMode().equals(GameMode.SPECTATOR) || ((Player) sender).getGameMode().equals(GameMode.CREATIVE)) {
                    ((Player) sender).setGameMode(GameMode.SURVIVAL);
                } else {
                    ((Player) sender).setGameMode(GameMode.CREATIVE);
                }
                dsp.send(sender, "gamemode.changed.you", ((Player) sender).getGameMode().toString());
            } else {
                dsp.send(sender, "cmd.noconsole");
            }
        } else {
            for (int i = 0; i < args.length; i++) {
                System.out.println(i + ": " + args[i]);
            }
            if (args.length == 1) {
                final boolean[] player = {false};
                Bukkit.getOnlinePlayers().forEach(entry -> {
                    if (entry.getName().equalsIgnoreCase(args[0])) {
                        if (entry.getGameMode().equals(GameMode.SPECTATOR) || entry.getGameMode().equals(GameMode.CREATIVE)) {
                            entry.setGameMode(GameMode.SURVIVAL);
                        } else {
                            entry.setGameMode(GameMode.CREATIVE);
                        }
                        dsp.send(entry, "gamemode.changed.you", entry.getGameMode().toString());
                        dsp.send(sender, "gamemode.changed.other", entry.getDisplayName(), entry.getGameMode().toString());
                        player[0] = true;
                    }
                });
                if (player[0] == false) {
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
                            dsp.send(sender, "gamemode.notexists.mode", args[0]);
                            return;
                        }
                        dsp.send(sender, "gamemode.changed.you", ((Player) sender).getGameMode().toString());
                    } else {
                        dsp.send(sender, "cmd.noconsole");
                    }
                }
            } else if (args.length == 2) {
                final boolean[] player = {false};
                final Player[] p = new Player[1];
                Bukkit.getOnlinePlayers().forEach(entry -> {
                    if (entry.getName().equalsIgnoreCase(args[1])) {
                        if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("survival")) {
                            ((Player) sender).setGameMode(GameMode.SURVIVAL);
                        } else if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("creative")) {
                            ((Player) sender).setGameMode(GameMode.CREATIVE);
                        } else if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("adventure")) {
                            ((Player) sender).setGameMode(GameMode.ADVENTURE);
                        } else if (args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("spectator")) {
                            ((Player) sender).setGameMode(GameMode.SPECTATOR);
                        } else {
                            dsp.send(sender, "gamemode.notexists.mode", args[0]);
                            return;
                        }
                    }
                });
                if (player[0] = false) {
                    dsp.send(sender, "gamemode.notexists.player", args[1]);
                    return;
                }
                dsp.send(Bukkit.getPlayer(args[1]), "gamemode.changed.you", Bukkit.getPlayer(args[1]).getGameMode().toString());
                dsp.send(sender, "gamemode.changed.other", Bukkit.getPlayer(args[1]).getDisplayName(), Bukkit.getPlayer(args[1]).getGameMode().toString());
            } else {
                dsp.send(sender, "gamemode.usage");
            }
        }

    }
}
