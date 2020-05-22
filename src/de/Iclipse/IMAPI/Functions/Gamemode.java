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

    public void execute(CommandSender sender, String arg0, String arg1) {
        if (arg0 == null && arg1 == null) {
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
            if (arg1 == null) {
                final boolean[] player = {false};
                Bukkit.getOnlinePlayers().forEach(entry -> {
                    if (entry.getName().equalsIgnoreCase(arg0)) {
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
                        if (arg0.equalsIgnoreCase("0") || arg0.equalsIgnoreCase("survival")) {
                            ((Player) sender).setGameMode(GameMode.SURVIVAL);
                        } else if (arg0.equalsIgnoreCase("1") || arg0.equalsIgnoreCase("creative")) {
                            ((Player) sender).setGameMode(GameMode.CREATIVE);
                        } else if (arg0.equalsIgnoreCase("2") || arg0.equalsIgnoreCase("adventure")) {
                            ((Player) sender).setGameMode(GameMode.ADVENTURE);
                        } else if (arg0.equalsIgnoreCase("3") || arg0.equalsIgnoreCase("spectator")) {
                            ((Player) sender).setGameMode(GameMode.SPECTATOR);
                        } else {
                            dsp.send(sender, "gamemode.notexists.mode", arg0);
                            return;
                        }
                        dsp.send(sender, "gamemode.changed.you", ((Player) sender).getGameMode().toString());
                    } else {
                        dsp.send(sender, "cmd.noconsole");
                    }
                }
            } else {
                final boolean[] player = {false};
                Bukkit.getOnlinePlayers().forEach(entry -> {
                    if (entry.getName().equalsIgnoreCase(arg1)) {
                        if (arg0.equalsIgnoreCase("0") || arg0.equalsIgnoreCase("survival")) {
                            entry.setGameMode(GameMode.SURVIVAL);
                        } else if (arg0.equalsIgnoreCase("1") || arg0.equalsIgnoreCase("creative")) {
                            entry.setGameMode(GameMode.CREATIVE);
                        } else if (arg0.equalsIgnoreCase("2") || arg0.equalsIgnoreCase("adventure")) {
                            entry.setGameMode(GameMode.ADVENTURE);
                        } else if (arg0.equalsIgnoreCase("3") || arg0.equalsIgnoreCase("spectator")) {
                            entry.setGameMode(GameMode.SPECTATOR);
                        } else {
                            dsp.send(sender, "gamemode.notexists.mode", arg0);
                            return;
                        }
                        dsp.send(Bukkit.getPlayer(arg1), "gamemode.changed.you", Bukkit.getPlayer(arg1).getGameMode().toString());
                        dsp.send(sender, "gamemode.changed.other", Bukkit.getPlayer(arg1).getDisplayName(), Bukkit.getPlayer(arg1).getGameMode().toString());
                        player[0] = true;
                    } else if (entry.getName().equalsIgnoreCase(arg0)) {
                        if (arg1.equalsIgnoreCase("0") || arg1.equalsIgnoreCase("survival")) {
                            entry.setGameMode(GameMode.SURVIVAL);
                        } else if (arg1.equalsIgnoreCase("1") || arg1.equalsIgnoreCase("creative")) {
                            entry.setGameMode(GameMode.CREATIVE);
                        } else if (arg1.equalsIgnoreCase("2") || arg1.equalsIgnoreCase("adventure")) {
                            entry.setGameMode(GameMode.ADVENTURE);
                        } else if (arg1.equalsIgnoreCase("3") || arg1.equalsIgnoreCase("spectator")) {
                            entry.setGameMode(GameMode.SPECTATOR);
                        } else {
                            dsp.send(sender, "gamemode.notexists.mode", arg1);
                            return;
                        }
                        dsp.send(Bukkit.getPlayer(arg0), "gamemode.changed.you", Bukkit.getPlayer(arg0).getGameMode().toString());
                        dsp.send(sender, "gamemode.changed.other", Bukkit.getPlayer(arg0).getDisplayName(), Bukkit.getPlayer(arg0).getGameMode().toString());
                        player[0] = true;
                    }
                });
                if (player[0] = false) {
                    dsp.send(sender, "gamemode.notexists.player", arg0 + "/" + arg1);
                    return;
                }
            }
        }

    }
}
