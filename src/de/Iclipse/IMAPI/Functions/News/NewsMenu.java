package de.Iclipse.IMAPI.Functions.News;

import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import de.Iclipse.IMAPI.Util.executor.ThreadExecutor;
import de.Iclipse.IMAPI.Util.menu.MenuItem;
import de.Iclipse.IMAPI.Util.menu.PopupMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import static de.Iclipse.IMAPI.Util.UUIDFetcher.getUUID;

public class NewsMenu extends PopupMenu {
    
    private final IMAPI imapi;
    private int i = 0;
    public NewsMenu(Player viewer, IMAPI imapi) {
        super(imapi.getData().getDispatcher().get("news.title", viewer), 6);
        this.imapi = imapi;
        ThreadExecutor.executeAsync(() -> {
            final boolean[] seen = new boolean[1];
            if (imapi.getData().getNewsTable().getNews().size() > 0) {
                imapi.getData().getNewsTable().getNews().forEach(news -> {
                    seen[0] = imapi.getData().getUserTable().getLastNewsRead(getUUID(viewer.getName())).isAfter(imapi.getData().getNewsTable().getCreated(news));
                    String name;
                    if (!seen[0]) {
                        name = imapi.getData().getDispatcher().get("news.new", viewer) + imapi.getData().getNewsTable().getTitle(news, imapi.getData().getUserTable().getLanguage(getUUID(viewer.getName())));
                    } else {
                        name = imapi.getData().getDispatcher() + imapi.getData().getNewsTable().getTitle(news, imapi.getData().getUserTable().getLanguage(getUUID(viewer.getName())));
                    }
                    this.addMenuItem(new MenuItem(name, item(seen[0]), "§7vom §e" + imapi.getData().getNewsTable().getCreated(news).toLocalDate() + " §7(von §e" + UUIDFetcher.getName(imapi.getData().getNewsTable().getCreator(news)) + "§7)") {
                        @Override
                        public void onClick(Player player) {
                            viewer.openBook(book(news, imapi.getData().getUserTable().getLanguage(getUUID(viewer.getName()))));
                        }
                    }, i++);
                });
            }else{
                imapi.getData().getDispatcher().send(viewer, "news.empty");
            }
        }).onDone(() -> {
            openMenu(viewer);
            imapi.getData().getUserTable().updateLastNewsRead(getUUID(viewer.getName()));
            i = 0;
        });
    }

    public ItemStack item(boolean seen){
        ItemStack item;
        if(!seen){
            item = new ItemStack(Material.ENCHANTED_BOOK);
        }else {
            item = new ItemStack(Material.BOOK);
        }
        return item;
    }

    public ItemStack book(int news, String lang) {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.setAuthor(UUIDFetcher.getName(imapi.getData().getNewsTable().getCreator(news)));
        meta.setTitle(imapi.getData().getNewsTable().getTitle(news, lang));
        meta.addPage(ChatColor.translateAlternateColorCodes('$', imapi.getData().getNewsTable().getText(news, lang)));
        item.setItemMeta(meta);
        return item;
    }
}
