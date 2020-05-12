package de.Iclipse.IMAPI.Functions.Servers;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Database.Server;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

import static de.Iclipse.IMAPI.Data.dsp;
import static de.Iclipse.IMAPI.Database.Mode.*;

public class Mode {
    StringBuilder builder;

    @IMCommand(
            name = "mode",
            maxArgs = 0,
            usage = "mode.usage",
            description = "mode.description",
            permissions = "im.cmd.mode"
    )
    public void mode(CommandSender sender) {
        builder = new StringBuilder();
        builder.append(dsp.get("mode.overview", sender) + "\n");
        add(sender, "create");
        add(sender, "delete");
        add(sender, "list");
        add(sender, "add");
        add(sender, "remove");
        add(sender, "servers");
        sender.sendMessage(builder.toString());
    }

    @IMCommand(
            name = "create",
            parent = "mode",
            maxArgs = 1,
            minArgs = 1,
            usage = "mode.create.usage",
            description = "mode.create.description",
            permissions = "im.cmd.mode.create"
    )
    public void create(CommandSender sender, String name) {
        if (!isModeExists(name)) {
            registerMode(name);
            dsp.send(sender, "mode.create.successfull");
        } else {
            dsp.send(sender, "mode.create.already");
        }
    }

    @IMCommand(
            name = "delete",
            parent = "mode",
            maxArgs = 1,
            minArgs = 1,
            usage = "mode.delete.usage",
            description = "mode.delete.description",
            permissions = "im.cmd.mode.delete"
    )
    public void delete(CommandSender sender, String name) {
        if (isModeExists(name)) {
            if (Server.getServers(name).size() > 0) {
                Server.getServers().forEach(server -> {
                    if (Server.getMode(server).equals(name)) {
                        Server.setMode(server, null);
                    }
                });
            }
            deleteMode(name);
            dsp.send(sender, "mode.delete.successfull");
        } else {
            dsp.send(sender, "mode.delete.notexists");
        }
    }

    @IMCommand(
            name = "list",
            parent = "mode",
            maxArgs = 0,
            usage = "mode.list.usage",
            description = "mode.list.description",
            permissions = "im.cmd.mode.list"
    )
    public void list(CommandSender sender) {
        ArrayList<String> list = getModes();
        if (list.size() > 0) {
            String modes = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                modes += "," + list.get(i);
            }
            dsp.send(sender, "mode.list.format", modes);
        } else {
            dsp.send(sender, "mode.list.empty");
        }
    }

    @IMCommand(
            name = "add",
            parent = "mode",
            maxArgs = 2,
            minArgs = 2,
            usage = "mode.add.usage",
            description = "mode.add.description",
            permissions = "im.cmd.mode.add"
    )
    public void add(CommandSender sender, String mode, String server) {
        if (isModeExists(mode)) {
            if (Server.getServers().contains(server)) {
                if (Server.getMode(server) != null) {
                    if (!Server.getMode(server).equals(mode)) {
                        Server.setMode(server, mode);
                        dsp.send(sender, "mode.add.successfull");
                    } else {
                        dsp.send(sender, "mode.add.already");
                    }
                } else {
                    Server.setMode(server, mode);
                    dsp.send(sender, "mode.add.successfull");
                }
            } else {
                dsp.send(sender, "mode.add.servernotexists");
            }
        } else {
            dsp.send(sender, "mode.add.notexists");
        }
    }


    @IMCommand(
            name = "remove",
            parent = "mode",
            maxArgs = 2,
            minArgs = 2,
            usage = "mode.remove.usage",
            description = "mode.remove.description",
            permissions = "im.cmd.mode.remove"
    )
    public void remove(CommandSender sender, String mode, String server) {
        if (isModeExists(mode)) {
            if (Server.getServers().contains(server)) {
                if (Server.getMode(server).equals(mode)) {
                    Server.setMode(server, "NONE");
                    dsp.send(sender, "mode.remove.successfull");
                } else {
                    dsp.send(sender, "mode.remove.otherMode");
                }
            } else {
                dsp.send(sender, "mode.remove.servernotexists");
            }
        } else {
            dsp.send(sender, "mode.remove.notexists");
        }
    }

    @IMCommand(
            name = "servers",
            parent = "mode",
            maxArgs = 1,
            minArgs = 1,
            usage = "mode.servers.usage",
            description = "mode.servers.description",
            permissions = "im.cmd.mode.servers"
    )
    public void servers(CommandSender sender, String mode) {
        if (isModeExists(mode)) {
            ArrayList<String> list = Server.getServers(mode);
            if (list.size() > 0) {
                String servers = list.get(0);
                for (int i = 1; i < list.size(); i++) {
                    servers += ", " + list.get(i);
                }
                dsp.send(sender, "mode.servers.format", servers);
            } else {
                dsp.send(sender, "mode.servers.empty");
            }
        } else {
            dsp.send(sender, "mode.servers.notexists");
        }
    }


    private void add(CommandSender sender, String command) {
        builder.append("\n" + Data.symbol + "§e" + dsp.get("mode." + command + ".usage", sender) + "§8: §7 " + dsp.get("mode." + command + ".description", sender) + ChatColor.RESET);
    }
}
