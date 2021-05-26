package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;



public class Language {

    private final IMAPI imapi;

    public Language(IMAPI imapi) {
        this.imapi = imapi;
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
            TextComponent base = new TextComponent(imapi.getData().getPrefix() + imapi.getData().getDispatcher().get("lang.lang", p) + ": ");
            imapi.getData().getDispatcher().getLanguages().forEach((name, bundle)->{
                TextComponent component = new TextComponent("§5" + name + imapi.getData().getDispatcher().get("color.text", imapi.getData().getDispatcher().getDefaultLang()) + ", ");
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lang " + name));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Switch to " + name).create()));
                base.addExtra(component);
            });
            p.spigot().sendMessage(base);
        }else {

            if (imapi.getData().getDispatcher().getLanguages().containsKey(s)) {
                if (!s.equals(imapi.getData().getUserTable().getLanguage(UUIDFetcher.getUUID(p.getName())))) {
                    imapi.getData().getUserTable().setLanguage(UUIDFetcher.getUUID(p.getName()), s);
                    imapi.getData().getTablist().setTablist(p);
                    imapi.getData().getDispatcher().send(p, "lang.changed");
                } else {
                    imapi.getData().getDispatcher().send(p, "lang.same");
                }
            } else {
                imapi.getData().getDispatcher().send(p, "lang.invalid");
            }
        }
    }
}
