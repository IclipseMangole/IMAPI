package de.Iclipse.IMAPI.Functions.Servers;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class Mode {
    private StringBuilder builder;
    private final IMAPI imapi;

    public Mode(IMAPI imapi){
        this.imapi = imapi;
    }

    @IMCommand(
            name = "mode",
            maxArgs = 0,
            usage = "mode.usage",
            description = "mode.description",
            permissions = "im.cmd.mode"
    )
    public void mode(CommandSender sender) {
        builder = new StringBuilder();
        builder.append(imapi.getData().getDispatcher().get("mode.overview", sender) + "\n");
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
        if (!imapi.getData().getModeTable().isModeExists(name)) {
            imapi.getData().getModeTable().registerMode(name);
            imapi.getData().getDispatcher().send(sender, "mode.create.successfull");
        } else {
            imapi.getData().getDispatcher().send(sender, "mode.create.already");
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
        if (imapi.getData().getModeTable().isModeExists(name)) {
            if (imapi.getData().getServerTable().getServers(name).size() > 0) {
                imapi.getData().getServerTable().getServers().forEach(server -> {
                    if (imapi.getData().getServerTable().getMode(server).equals(name)) {
                        imapi.getData().getServerTable().setMode(server, null);
                    }
                });
            }
            imapi.getData().getModeTable().deleteMode(name);
            imapi.getData().getDispatcher().send(sender, "mode.delete.successfull");
        } else {
            imapi.getData().getDispatcher().send(sender, "mode.delete.notexists");
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
        ArrayList<String> list = imapi.getData().getModeTable().getModes();
        if (list.size() > 0) {
            StringBuilder modes = new StringBuilder(list.get(0));
            for (int i = 1; i < list.size(); i++) {
                modes.append(",").append(list.get(i));
            }
            imapi.getData().getDispatcher().send(sender, "mode.list.format", modes.toString());
        } else {
            imapi.getData().getDispatcher().send(sender, "mode.list.empty");
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
        if (imapi.getData().getModeTable().isModeExists(mode)) {
            if (imapi.getData().getServerTable().getServers().contains(server)) {
                if (imapi.getData().getServerTable().getMode(server) != null) {
                    if (!imapi.getData().getServerTable().getMode(server).equals(mode)) {
                        imapi.getData().getServerTable().setMode(server, mode);
                        imapi.getData().getDispatcher().send(sender, "mode.add.successfull");
                    } else {
                        imapi.getData().getDispatcher().send(sender, "mode.add.already");
                    }
                } else {
                    imapi.getData().getServerTable().setMode(server, mode);
                    imapi.getData().getDispatcher().send(sender, "mode.add.successfull");
                }
            } else {
                imapi.getData().getDispatcher().send(sender, "mode.add.servernotexists");
            }
        } else {
            imapi.getData().getDispatcher().send(sender, "mode.add.notexists");
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
        if (imapi.getData().getModeTable().isModeExists(mode)) {
            if (imapi.getData().getServerTable().getServers().contains(server)) {
                if (imapi.getData().getServerTable().getMode(server).equals(mode)) {
                    imapi.getData().getServerTable().setMode(server, "NONE");
                    imapi.getData().getDispatcher().send(sender, "mode.remove.successfull");
                } else {
                    imapi.getData().getDispatcher().send(sender, "mode.remove.otherMode");
                }
            } else {
                imapi.getData().getDispatcher().send(sender, "mode.remove.servernotexists");
            }
        } else {
            imapi.getData().getDispatcher().send(sender, "mode.remove.notexists");
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
        if (imapi.getData().getModeTable().isModeExists(mode)) {
            ArrayList<String> list = imapi.getData().getServerTable().getServers(mode);
            if (list.size() > 0) {
                StringBuilder servers = new StringBuilder(list.get(0));
                for (int i = 1; i < list.size(); i++) {
                    servers.append(", ").append(list.get(i));
                }
                imapi.getData().getDispatcher().send(sender, "mode.servers.format", servers.toString());
            } else {
                imapi.getData().getDispatcher().send(sender, "mode.servers.empty");
            }
        } else {
            imapi.getData().getDispatcher().send(sender, "mode.servers.notexists");
        }
    }


    private void add(CommandSender sender, String command) {
        builder.append("\n").append(imapi.getData().getSymbol()).append("§e").append(imapi.getData().getDispatcher().get("mode." + command + ".usage", sender)).append("§8: §7 ").append(imapi.getData().getDispatcher().get("mode." + command + ".description", sender)).append(ChatColor.RESET);
    }
}
