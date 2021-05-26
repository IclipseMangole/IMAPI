package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;


public class Help {

    private final IMAPI imapi;

    public Help(IMAPI imapi) {
        this.imapi = imapi;
    }

    @IMCommand(
            name = "help",
            permissions = "im.cmd.help",
            usage = "help.usage",
            description = "help.description",
            minArgs = 0,
            maxArgs = 1
    )
    public void execute(CommandSender sender, String plugin, String pageString) {
        int commandsPerPage = 7;
        if (plugin == null) {
            ArrayList<String> implugins = new ArrayList<>();
            imapi.getData().getCommands().forEach((cmd, pl) -> {
                if (!implugins.contains(pl)) implugins.add(pl);
            });
            final String[] message = {imapi.getData().getPrefix() + "Plugins: "};
            implugins.forEach(entry -> {
                message[0] = message[0] + imapi.getData().getTextcolor() + entry + ", ";
            });
            sender.sendMessage(message);
        } else {
            final boolean[] contains = new boolean[1];
            imapi.getData().getCommands().forEach((cmd, pl) -> {
                if (pl.equals(plugin)) {
                    contains[0] = true;
                }
            });
            if (contains[0]) {
                if (pageString == null) {
                    pageString = "1";
                }
                int page = Integer.parseInt(pageString);
                System.out.println(page);
                ArrayList<IMCommand> permittedCmds = new ArrayList<>();
                imapi.getData().getCommands().forEach((cmd, pl) -> {
                    if (pl.equals(plugin)) {
                        for (String permission : cmd.permissions()) {
                            if (!sender.hasPermission(permission)) {
                                return;
                            }
                        }
                        permittedCmds.add(cmd);
                    }
                });
                if (!(Math.ceil((double) permittedCmds.size() / (double) commandsPerPage) < page)) {
                    imapi.getData().getDispatcher().send(sender, "help.header", pageString, "" + Math.ceil((double) permittedCmds.size() / (double) commandsPerPage));
                    permittedCmds.forEach(entry -> {
                        if (sender instanceof Player) {
                            TextComponent component = new TextComponent(imapi.getData().getPrefix() + "§5" + imapi.getData().getDispatcher().get(entry.usage(), sender, false) + ": " + imapi.getData().getHighlight() + imapi.getData().getDispatcher().get(entry.description(), sender, false));
                            component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + entry.name()));
                            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/" + entry.name()).create()));
                            sender.spigot().sendMessage(component);
                        } else {
                            imapi.getData().getDispatcher().sendTextMessage(sender, imapi.getData().getPrefix() + "§5" + imapi.getData().getDispatcher().get(entry.usage(), sender, false) + ": " + imapi.getData().getHighlight() + imapi.getData().getDispatcher().get(entry.description(), sender, false));
                        }
                    });
                } else {
                    imapi.getData().getDispatcher().send(sender, "help.pagenotexist");
                }
            } else {
                imapi.getData().getDispatcher().send(sender, "help.pluginnotexist");
            }
        }
    }
}
