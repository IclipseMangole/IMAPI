package de.Iclipse.IMAPI.Functions.Commands;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Functions.MySQL.MySQL_User;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.Dispatching.Dispatch;
import de.Iclipse.IMAPI.Util.Dispatching.Language;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

import static de.Iclipse.IMAPI.Data.*;


public class cmd_lang {

    public void registerLang(){
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
            TextComponent base = new TextComponent(prefix + dsp.get("lang.lang", MySQL_User.getLanguage(UUIDFetcher.getUUID(p.getName()))) + ": ");
            for (Language lang : Language.values()) {
                TextComponent component = new TextComponent("§5" + lang.getShortcut() + dsp.get("color.text", defaultLang) +  ", ");
                component.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/lang " + lang.getShortcut()));
                component.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(lang.getTranslation() ).create() ) );
                base.addExtra(component);
            }
            p.spigot().sendMessage(base);
        }else{
            for (Language lang : Language.values()) {
                if(lang.getShortcut().equals(s)){
                    if(!lang.equals(MySQL_User.getLanguage(UUIDFetcher.getUUID(p.getName())))) {
                        MySQL_User.setLanguage(UUIDFetcher.getUUID(p.getName()), lang);
                        dsp.send(p, "lang.changed");
                    }else{
                        dsp.send(p, "lang.same");
                    }
                    return;
                }
            }
            dsp.send(p, "lang.invalid");
        }
    }
}
