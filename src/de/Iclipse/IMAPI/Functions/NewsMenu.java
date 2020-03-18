package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Functions.MySQL.MySQL_News;
import de.Iclipse.IMAPI.Functions.MySQL.MySQL_User;
import de.Iclipse.IMAPI.Util.Dispatching.Language;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import de.Iclipse.IMAPI.Util.executor.ThreadExecutor;
import de.Iclipse.IMAPI.Util.menu.MenuItem;
import de.Iclipse.IMAPI.Util.menu.PopupMenu;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.text.SimpleDateFormat;
import java.util.Date;

import static de.Iclipse.IMAPI.Data.dsp;
import static de.Iclipse.IMAPI.Data.textcolor;
import static de.Iclipse.IMAPI.Util.UUIDFetcher.getUUID;

public class NewsMenu extends PopupMenu {
    int i = 0;
    public NewsMenu(Player viewer) {
        super(dsp.get("news.title", MySQL_User.getLanguage(getUUID(viewer.getName()))), 6);
        ThreadExecutor.executeAsync(() -> {
            final boolean[] seen = new boolean[1];
            if(MySQL_News.getNews().size() > 0) {
                MySQL_News.getNews().forEach(news -> {
                    seen[0] = MySQL_User.getLastNewsRead(getUUID(viewer.getName())).isAfter(MySQL_News.getCreated(news));
                    String name;
                    if (!seen[0]) {
                        name = dsp.get("news.new", MySQL_User.getLanguage(getUUID(viewer.getName())), false) + MySQL_News.getTitle(news, MySQL_User.getLanguage(getUUID(viewer.getName())));
                    } else {
                        name = textcolor + MySQL_News.getTitle(news, MySQL_User.getLanguage(getUUID(viewer.getName())));
                    }
                    this.addMenuItem(new MenuItem(name, item(seen[0]), "§7vom §e" + MySQL_News.getCreated(news).toLocalDate() + " §7(von §e" + UUIDFetcher.getName(MySQL_News.getCreator(news)) + "§7)") {
                        @Override
                        public void onClick(Player player) {
                            viewer.openBook(book(news, MySQL_User.getLanguage(getUUID(viewer.getName()))));
                        }
                    }, i++);
                });
            }else{
                dsp.send(viewer, "news.empty");
                return;
            }
        }).onDone(() -> {
            openMenu(viewer);
            MySQL_User.updateLastNewsRead(getUUID(viewer.getName()));
            i = 0;
        });
    }

    public static ItemStack item(boolean seen){
        ItemStack item;
        if(!seen){
            item = new ItemStack(Material.ENCHANTED_BOOK);
        }else {
            item = new ItemStack(Material.BOOK);
        }
        return item;
    }

    public static ItemStack book(int news, Language lang){
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.setAuthor(UUIDFetcher.getName(MySQL_News.getCreator(news)));
        meta.setTitle(MySQL_News.getTitle(news, lang));
        meta.addPage(ChatColor.translateAlternateColorCodes('$' ,MySQL_News.getText(news, lang)));
        item.setItemMeta(meta);
        return item;
    }
}
