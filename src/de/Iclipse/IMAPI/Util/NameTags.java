package de.Iclipse.IMAPI.Util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;




public class NameTags {




    public static class NameTagMeta {




        private String teamname;
        private String prefix;
        private String suffix;
        private List<String> names = new ArrayList<>();




        public NameTagMeta(String teamname, String prefix, String suffix) {
            this.teamname = teamname;
            this.prefix = (prefix.length() <= 16 ? prefix : prefix.substring(0, 16));
            this.suffix = (suffix.length() <= 16 ? suffix : suffix.substring(0, 16));
        }




        public void removeName(String s) {
            names.remove(s);
        }




        public void addName(String name) {
            this.names.add(name);
        }




        public void setNames(List<String> names) {
            this.names = names;
        }




        public List<String> getNames() {
            return names;
        }




        public String getTeamName() {
            return teamname;
        }




        public String getPrefix() {
            return prefix;
        }




        public String getSuffix() {
            return suffix;
        }




    }




    private List<NameTagMeta> teams = new ArrayList<>();




    public NameTags(List<NameTagMeta> teams) {
        this.teams = teams;
    }




    public void removePlayer(String playername) {
        teams.forEach(meta -> {
            if (meta.getNames().contains(playername)) {
                meta.removeName(playername);
            }
        });
    }

    public void deletePlayer(Player p){
        sendPacket(p, new PacketPlayOutScoreboardTeam());
    }




    public void addPlayer(String teamname, String playername) {
        teams.forEach(meta -> {
            if (meta.getTeamName().equals(teamname)) {
                if (!meta.getNames().contains(playername)) {
                    meta.addName(playername);
                }
                return;
            }
        });
    }




    public void updateNameTags() {
        teams.forEach(meta -> {
            try {
                Object packet = getNMSClass("PacketPlayOutScoreboardTeam").getConstructor().newInstance();


                    set(packet, "a", meta.getTeamName());
                    set(packet, "b", new ChatComponentText(meta.getTeamName()));
                    set(packet, "c", new ChatComponentText(meta.getPrefix()));
                    set(packet, "d", new ChatComponentText(meta.getSuffix()));
                    set(packet, "e", ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS.e);
                    set(packet, "f", ScoreboardTeamBase.EnumTeamPush.NEVER);
                    set(packet, "g", EnumChatFormat.RESET);
                    meta.getNames().forEach(s -> {
                        if (Bukkit.getPlayer(s) == null)
                            meta.removeName(s);
                    });
                    set(packet, "h", meta.getNames());
                    set(packet, "i", 1);
                    set(packet, "i", 0);
                    Bukkit.getOnlinePlayers().forEach(player -> sendPacket(player, packet));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }




    private void set(Object object, String fieldname, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldname);
            field.setAccessible(true);
            field.set(object, value);
            field.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




    private void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        }




        catch (Exception e) {
            e.printStackTrace();
        }
    }







    private Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        }




        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }




}