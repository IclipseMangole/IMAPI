package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Functions.MySQL.MySQL;
import de.Iclipse.IMAPI.Functions.MySQL.MySQL_News;
import de.Iclipse.IMAPI.Functions.MySQL.MySQL_User;
import de.Iclipse.IMAPI.Util.Dispatching.Language;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import de.Iclipse.IMAPI.Util.executor.ThreadExecutor;
import de.Iclipse.IMAPI.Util.menu.MenuItem;
import de.Iclipse.IMAPI.Util.menu.PopupMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.text.SimpleDateFormat;

import static de.Iclipse.IMAPI.Data.dsp;

public class NewsMenu extends PopupMenu {
    int i = 0;
    public NewsMenu(Player viewer) {
        super("Neuigkeiten", 6);
        ThreadExecutor.executeAsync(() -> {

            MySQL_News.getNews().forEach(news -> {
                boolean seen = MySQL_User.getLastNewsRead(viewer.getUniqueId()).isBefore(MySQL_News.getCreated(news));
                String name;
                if(seen){
                    name = dsp.get("news.new", MySQL_User.getLanguage(viewer.getUniqueId()), false) + MySQL_News.getTitle(news, MySQL_User.getLanguage(viewer.getUniqueId()));
                }else{
                    name = MySQL_News.getTitle(news, MySQL_User.getLanguage(viewer.getUniqueId()));
                }
                this.addMenuItem(new MenuItem(name, item(seen), "§7vom §e" + SimpleDateFormat.getInstance().format(MySQL_News.getCreated(news)) + " §7(von §e" + UUIDFetcher.getName(MySQL_News.getCreator(news)) + "§7)") {
                    @Override
                    public void onClick(Player player) {
                        viewer.openBook(book(news, MySQL_User.getLanguage(viewer.getUniqueId())));
                    }
                }, i++);
            });
        }).onDone(() -> {
            openMenu(viewer);
            i = 0;
        });
        MySQL_User.updateLastNewsRead(viewer.getUniqueId());
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
        meta.setTitle(MySQL_News.getText(news, lang));
        meta.setPages(MySQL_News.getText(news, lang));
        item.setItemMeta(meta);
        return item;
    }
}
