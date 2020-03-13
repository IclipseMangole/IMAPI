package de.Iclipse.IMAPI.Functions.Commands;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Functions.MySQL.MySQL_User;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.Dispatching.Language;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

import static de.Iclipse.IMAPI.Data.*;

public class cmd_help {

    @IMCommand(
            name = "help",
            permissions = "im.cmd.help",
            usage = "help.usage",
            description = "help.description",
            minArgs = 0,
            maxArgs = 1
    )
    public void execute(CommandSender sender, String plugin, String pageString){
        int commandsPerPage = 7;
        if(plugin == null){
            ArrayList<Plugin> implugins = new ArrayList<>();
            Data.commands.forEach((cmd, pl) ->{
                if(!implugins.contains(pl)) implugins.add(pl);
            });
            final String[] message = {Data.prefix + "Plugins: "};
            implugins.forEach(entry ->{
                message[0] = message[0] + Data.textcolor + entry.getName() + ", ";
            });
            sender.sendMessage(message);
        }else{
            final boolean[] contains = new boolean[1];
            Data.commands.forEach((cmd, pl) ->{
                if(pl.getName().equals(plugin)){
                    contains[0] = true;
                }
            });
            if(contains[0]) {
                if (pageString == null) {
                    pageString = "1";
                }
                int page = Integer.parseInt(pageString);
                System.out.println(page);
                ArrayList<IMCommand> permittedCmds = new ArrayList<>();
                Data.commands.forEach((cmd, pl) -> {
                    if (pl.getName().equals(plugin)) {
                        for (String permission : cmd.permissions()) {
                            if (!sender.hasPermission(permission)) {
                                return;
                            }
                        }
                        permittedCmds.add(cmd);
                    }
                });
                if (!(Math.ceil((double) permittedCmds.size() / (double) commandsPerPage) < page)) {
                    dsp.send(sender, "help.header", pageString, Math.ceil((double) permittedCmds.size() / (double) commandsPerPage));
                    permittedCmds.forEach(entry -> {
                        if(sender instanceof Player){
                        TextComponent component = new TextComponent(prefix + "§5" + dsp.get(entry.usage(), MySQL_User.getLanguage(((Player) sender).getUniqueId())) + ": " + highlight + dsp.get(entry.description(), MySQL_User.getLanguage(((Player) sender).getUniqueId()), false));
                        component.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/" + entry.name()));
                        component.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/" + entry.name()).create()));
                        sender.spigot().sendMessage(component);
                        }else {
                            dsp.sendTextMessage(sender, prefix + "§5" + dsp.get(entry.usage(), Language.DE, false) + ": " + highlight + dsp.get(entry.description(), Language.DE, false));
                        }
                        });
                } else {
                    dsp.send(sender, "help.pagenotexist");
                }
            }else{
                dsp.send(sender, "help.pluginnotexist");
            }
        }
    }
}