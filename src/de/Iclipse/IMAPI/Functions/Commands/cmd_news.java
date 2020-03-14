package de.Iclipse.IMAPI.Functions.Commands;

import de.Iclipse.IMAPI.Functions.MySQL.MySQL_News;
import de.Iclipse.IMAPI.Functions.NewsMenu;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

import static de.Iclipse.IMAPI.Data.dsp;

public class cmd_news implements Listener {
    public static HashMap<Player, String> title = new HashMap<>();
    public static HashMap<Player, String> text = new HashMap<>();

    @IMCommand(
            name = "news",
            usage = "news.usage",
            description = "news.description",
            minArgs = 0,
            maxArgs = 0,
            permissions = "im.command.news",
            noConsole = true
    )
    public void news(Player p) {
        new NewsMenu(p);
    }

    @IMCommand(
            name = "cancel",
            parent = "news",
            usage = "news.cancel.usage",
            description = "news.cancel.description",
            maxArgs = 0,
            minArgs = 0,
            noConsole = true,
            permissions = "im.command.news.write"
    )
    public void cancel(Player p) {
    }

    @IMCommand(
            name = "write",
            parent = "news",
            usage = "news.write.usage",
            description = "news.write.description",
            maxArgs = 2,
            minArgs = 2,
            noConsole = true,
            permissions = "im.command.news.write"
    )
    public void write(Player p, String titleDE, String titleEN) {

    }

    @IMCommand(
            name = "delete",
            parent = "news",
            usage = "news.delete.usage",
            description = "news.delete.description",
            maxArgs = 1,
            minArgs = 1,
            noConsole = true,
            permissions = "im.command.news.delete"

    )
    public void delete(Player p, String title) {
        if (MySQL_News.isTitleExists(title)) {
            MySQL_News.deleteNews(MySQL_News.getID(title));
            dsp.send(p, "news.delete.successfull");
        } else {
            dsp.send(p, "news.delete.notfound");
        }
    }

    @EventHandler
    public void onBookClose(){

    }

}
