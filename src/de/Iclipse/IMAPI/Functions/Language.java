package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Database.User;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import static de.Iclipse.IMAPI.Data.dsp;
import static de.Iclipse.IMAPI.Data.prefix;


public class Language {

    public void registerLang() {
    }

    @IMCommand(
            name = "lang",
            permissions = "im.cmd.lang",
            usage = "lang.usage",
            description = "lang.description",
            minArgs = 0,
            maxArgs = 1,
            noConsole = true
    )
    public void execute(Player p, String s){
        if(s == null){
            TextComponent base = new TextComponent(prefix + dsp.get("lang.lang", p) + ": ");
            dsp.getLanguages().forEach((name, bundle)->{
                TextComponent component = new TextComponent("§5" + name + dsp.get("color.text", dsp.getDefaultLang()) +  ", ");
                component.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/lang " + name));
                component.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Switch to " + name).create() ) );
                base.addExtra(component);
            });
            p.spigot().sendMessage(base);
        }else {

            if (dsp.getLanguages().containsKey(s)) {
                if (!s.equals(User.getLanguage(UUIDFetcher.getUUID(p.getName())))) {
                    User.setLanguage(UUIDFetcher.getUUID(p.getName()), s);
                    Data.tablist.setTablist(p);
                    dsp.send(p, "lang.changed");
                } else {
                    dsp.send(p, "lang.same");
                }
            } else {
                dsp.send(p, "lang.invalid");
            }
        }
    }
}
