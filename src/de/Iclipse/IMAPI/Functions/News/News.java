package de.Iclipse.IMAPI.Functions.News;

import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutCustomPayload;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Arrays;
import java.util.HashMap;

public class News implements Listener {

    private final HashMap<Player, ItemStack> writers = new HashMap<>();
    private final IMAPI imapi;
    
    public News(IMAPI imapi){
        this.imapi = imapi;
    }

    @IMCommand(
            name = "news",
            usage = "news.usage",
            description = "news.description",
            minArgs = 0,
            maxArgs = 0,
            permissions = "im.cmd.news",
            noConsole = true
    )
    public void news(Player p) {
        new NewsMenu(p, imapi);
    }


    @IMCommand(
            name = "write",
            parent = "news",
            usage = "news.write.usage",
            description = "news.write.description",
            maxArgs = 0,
            minArgs = 0,
            noConsole = true,
            permissions = "im.cmd.news.write"
    )
    public void write(Player p) {
        if(!writers.containsKey(p)) {
            writers.put(p, p.getInventory().getItemInMainHand());
            p.getInventory().setItemInMainHand(book(p));
        }else{
            imapi.getData().getDispatcher().send(p, "news.alreadywriting");
        }
    }

    @IMCommand(
            name = "delete",
            parent = "news",
            usage = "news.delete.usage",
            description = "news.delete.description",
            maxArgs = 1,
            minArgs = 1,
            noConsole = true,
            permissions = "im.cmd.news.delete"

    )
    public void delete(Player p, String title) {
        if (imapi.getData().getNewsTable().isTitleExists(title)) {
            imapi.getData().getNewsTable().deleteNews(imapi.getData().getNewsTable().getID(title));
            imapi.getData().getDispatcher().send(p, "news.delete.successfull");
        } else {
            imapi.getData().getDispatcher().send(p, "news.delete.notfound");
        }
    }

    @EventHandler
    public void onBookClose(PlayerEditBookEvent e){
        System.out.println("PlayerEditBook");
        Player p = e.getPlayer();
        if(writers.containsKey(p)){
            BookMeta meta = e.getNewBookMeta();
            if(meta.getTitle().contains("/")) {
                if(meta.getPageCount() == 2) {
                    imapi.getData().getNewsTable().createNews(meta.getTitle().split("/")[0], meta.getTitle().split("/")[1], meta.getPage(1), meta.getPage(2), UUIDFetcher.getUUID(p.getName()));
                    p.getInventory().setItemInMainHand(writers.get(p));
                    writers.remove(p);
                    imapi.getData().getDispatcher().send(p, "news.write.successfull");
                }else{
                    imapi.getData().getDispatcher().send(p, "news.textwrong");
                }
            }else{
                imapi.getData().getDispatcher().send(p, "news.titlewrong");
            }
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onBookSpawn(ItemSpawnEvent e){
        if(e.getEntity().getItemStack().getType().equals(Material.WRITABLE_BOOK)){
            writers.forEach((p, item)->{
                if(e.getEntity().getItemStack().equals(book(p))){
                    imapi.getData().getDispatcher().send(p, "news.cancelled");
                    p.getInventory().setItemInMainHand(writers.get(p));
                    e.setCancelled(true);
                    writers.remove(p);
                }
            });
        }
    }


    public void openBook(ItemStack book, Player p) {
        int slot = p.getInventory().getHeldItemSlot();
        ItemStack old = p.getInventory().getItem(slot);
        p.getInventory().setItem(slot, book);

        ByteBuf buf = Unpooled.buffer(256);
        buf.setByte(0, (byte)0);
        buf.writerIndex(1);

        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload( new MinecraftKey("minecraft:book_open"), new PacketDataSerializer(buf));
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
        p.getInventory().setItem(slot, old);
    }

    public static ItemStack book(Player p) {
        ItemStack is = new ItemStack(Material.WRITABLE_BOOK, 1);
        BookMeta meta = (BookMeta) is.getItemMeta();
        meta.setDisplayName("§dNews");
        meta.setLore(Arrays.asList(new String[]{"§dUsed by " + p.getName() + "to write news"}));
        return is;
    }

}
