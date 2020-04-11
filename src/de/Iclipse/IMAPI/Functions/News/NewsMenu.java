package de.Iclipse.IMAPI.Functions.News;

import de.Iclipse.IMAPI.Database.News;
import de.Iclipse.IMAPI.Database.User;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import de.Iclipse.IMAPI.Util.executor.ThreadExecutor;
import de.Iclipse.IMAPI.Util.menu.MenuItem;
import de.Iclipse.IMAPI.Util.menu.PopupMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import static de.Iclipse.IMAPI.Data.dsp;
import static de.Iclipse.IMAPI.Data.textcolor;
import static de.Iclipse.IMAPI.Util.UUIDFetcher.getUUID;

public class NewsMenu extends PopupMenu {
    int i = 0;
    public NewsMenu(Player viewer) {
        super(dsp.get("news.title", viewer), 6);
        ThreadExecutor.executeAsync(() -> {
            final boolean[] seen = new boolean[1];
            if (News.getNews().size() > 0) {
                News.getNews().forEach(news -> {
                    seen[0] = User.getLastNewsRead(getUUID(viewer.getName())).isAfter(News.getCreated(news));
                    String name;
                    if (!seen[0]) {
                        name = dsp.get("news.new", viewer) + News.getTitle(news, User.getLanguage(getUUID(viewer.getName())));
                    } else {
                        name = textcolor + News.getTitle(news, User.getLanguage(getUUID(viewer.getName())));
                    }
                    this.addMenuItem(new MenuItem(name, item(seen[0]), "§7vom §e" + News.getCreated(news).toLocalDate() + " §7(von §e" + UUIDFetcher.getName(News.getCreator(news)) + "§7)") {
                        @Override
                        public void onClick(Player player) {
                            viewer.openBook(book(news, User.getLanguage(getUUID(viewer.getName()))));
                        }
                    }, i++);
                });
            }else{
                dsp.send(viewer, "news.empty");
                return;
            }
        }).onDone(() -> {
            openMenu(viewer);
            User.updateLastNewsRead(getUUID(viewer.getName()));
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

    public static ItemStack book(int news, String lang) {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.setAuthor(UUIDFetcher.getName(News.getCreator(news)));
        meta.setTitle(News.getTitle(news, lang));
        meta.addPage(ChatColor.translateAlternateColorCodes('$', News.getText(news, lang)));
        item.setItemMeta(meta);
        return item;
    }
}
