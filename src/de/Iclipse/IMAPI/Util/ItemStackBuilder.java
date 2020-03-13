package de.Iclipse.IMAPI.Util;

import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Yannick on 30.10.2019 with IntelliJ for PXCode.
 */
public class ItemStackBuilder {

    private ItemStack itemstack;

    public ItemStackBuilder(ItemStack itemstack) {
        this.itemstack = removeAttributes(itemstack.clone());
        this.antiBug();
    }

    public ItemStackBuilder(Material type) {
        this.itemstack = removeAttributes(new ItemStack(type));
        this.antiBug();
    }

    public ItemStackBuilder(Material type, int amount) {
        this.itemstack = removeAttributes(new ItemStack(type, amount));
        this.antiBug();
    }

    public ItemStackBuilder(Material type, int amount, short damage) {
        this.itemstack = removeAttributes(new ItemStack(type, amount, damage));
        this.antiBug();
    }

    public ItemStackBuilder setAmount(int amount) {
        this.itemstack.setAmount(amount);
        return this;
    }

    private void antiBug() {
    }

    public ItemStackBuilder setData(byte data) {
        this.itemstack.getData().setData(data);
        return this;
    }

    public ItemStackBuilder addDisplayName(String string) {
        ItemMeta meta = this.itemstack.getItemMeta();
        if(meta != null) {
            meta.setDisplayName(string);
        }

        this.itemstack.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder addLore(List<String> lore) {
        ItemMeta meta = this.itemstack.getItemMeta();
        if(meta != null) {
            meta.setLore(lore);
        }

        this.itemstack.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder addLore(String string) {
        return this.addLore(buildLore(string));
    }

    public ItemStackBuilder addLore(List<String> addlore, int i) {
        Object lore = this.getItemstack().getItemMeta() != null?this.getItemstack().getItemMeta().getLore():new ArrayList();
        ((List)lore).addAll(i, addlore);
        this.addLore((List)lore);
        return this;
    }

    public ItemStackBuilder addEnchantment(Enchantment ench, int level) {
        this.itemstack.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemStackBuilder setColor(Color color) {
        if(this.itemstack.getItemMeta() instanceof LeatherArmorMeta) {
            LeatherArmorMeta meta = (LeatherArmorMeta)this.itemstack.getItemMeta();
            meta.setColor(color);
            this.itemstack.setItemMeta(meta);
        }

        return this;
    }

    public ItemStackBuilder setColor(DyeColor baseColor) {
        if(this.itemstack.getItemMeta() instanceof BannerMeta) {
            BannerMeta meta = (BannerMeta)this.getItemstack().getItemMeta();
            meta.setBaseColor(baseColor);
            this.itemstack.setItemMeta(meta);
        }

        return this;
    }

    public ItemStackBuilder clone() {
        ItemStackBuilder builder = new ItemStackBuilder(this.getItemstack());
        return builder;
    }

    public ItemStack getItemstack() {
        return this.itemstack;
    }

    public static ItemStack removeAttributes(ItemStack item) {
        net.minecraft.server.v1_15_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        if(nmsStack == null) {
            return item;
        } else {
            NBTTagCompound tag;
            if(!nmsStack.hasTag()) {
                tag = new NBTTagCompound();
                nmsStack.setTag(tag);
            } else {
                tag = nmsStack.getTag();
            }

            tag.set("AttributeModifiers", new NBTTagList());
            nmsStack.setTag(tag);
            return CraftItemStack.asCraftMirror(nmsStack);
        }
    }

    public static List<String> buildLore(String lore) {
        return buildLore(lore, "§7", 1, 33);
    }

    public static List<String> buildLore(String lore, String color, int emptylines, int charsperline) {
        if(lore.equals("")) {
            return null;
        } else {
            List split = Arrays.asList(lore.split(" "));
            ArrayList list = new ArrayList();

            for(int line = 0; line < emptylines; ++line) {
                list.add("");
            }

            String var9 = color;
            Iterator var7 = split.iterator();

            while(true) {
                String string;
                do {
                    if(!var7.hasNext()) {
                        var9 = var9.substring(0, var9.length() - 1);
                        list.add(var9);
                        return list;
                    }

                    string = (String)var7.next();
                    if(var9.length() + string.length() <= charsperline && !string.equals("$a")) {
                        break;
                    }

                    if(var9.length() > 2) {
                        var9 = var9.substring(0, var9.length() - 1);
                    }

                    list.add(var9);
                    var9 = color;
                } while(string.equals("$a"));

                var9 = var9.concat(string);
                var9 = var9.concat(" ");
            }
        }
    }

}
