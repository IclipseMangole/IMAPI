package de.Iclipse.IMAPI.Functions.NPC;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.Iclipse.IMAPI.Data;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.*;

import static de.Iclipse.IMAPI.Util.Reflections.sendPacket;

public class NPCNMS {
    public static ArrayList<NPCNMS> npcsForAll = new ArrayList<>();
    public static HashMap<NPCNMS, ArrayList<Player>> npcsForSome = new HashMap<>();

    private Location loc;
    private UUID uuid;
    private String name;
    private String[] textures;
    private GameProfile gameProfile;
    private EntityPlayer entityPlayer;
    private boolean dinnerbone;

    public NPCNMS(UUID uuid, String name, String[] textures, Location loc) {
        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldServer = ((CraftWorld) loc.getWorld()).getHandle();
        this.name = name;
        this.uuid = uuid;
        this.loc = loc;
        this.textures = textures;

        this.gameProfile = new GameProfile(uuid, name);
        this.gameProfile.getProperties().put("textures", new Property("textures", textures[0], textures[1]));
        this.entityPlayer = new EntityPlayer(minecraftServer, worldServer, gameProfile, new PlayerInteractManager(worldServer));
        this.entityPlayer.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }


    public NPCNMS(UUID uuid, String name, Location loc) {
        this(uuid, name, TextureFetcher.getSkin(uuid), loc);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    private void toTablist(Player p) {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
        sendPacket(packet, p);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Data.instance, new Runnable() {
            @Override
            public void run() {
                PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
                sendPacket(packet, p);
            }
        }, 5);
    }

    private void toTablist() {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
        sendPacket(packet);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Data.instance, new Runnable() {
            @Override
            public void run() {
                PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
                sendPacket(packet);
            }
        }, 5);
    }


    private PacketPlayOutNamedEntitySpawn getPacketPlayOutNamedEntitySpawn() {
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn(entityPlayer);
        return packet;
    }

    private PacketPlayOutEntityHeadRotation getPacketPlayOutEntityHeadRotation() {
        PacketPlayOutEntityHeadRotation packet = new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) (entityPlayer.yaw * 256 / 360));
        return packet;
    }

    private PacketPlayOutEntityMetadata getPlayOutEntityMetadata() {
        DataWatcher data = entityPlayer.getDataWatcher();
        System.out.println("Watcher: " + data.get(DataWatcherRegistry.a.a(16)).toString());

        byte b = 0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40;
        data.set(DataWatcherRegistry.a.a(16), b);
        System.out.println("Watcher: " + data.get(DataWatcherRegistry.a.a(16)).toString());


        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(entityPlayer.getId(), data, false);
        return packet;
    }

    private PacketPlayOutEntityDestroy getPacketPlayOutEntityDestroy() {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityPlayer.getId());
        return packet;
    }

    public void show(Player p) {
        toTablist(p);
        sendPacket(getPlayOutEntityMetadata());
        sendPacket(getPacketPlayOutNamedEntitySpawn(), p);
        sendPacket(getPacketPlayOutEntityHeadRotation(), p);
        if (npcsForSome.containsKey(this)) {
            ArrayList<Player> list = new ArrayList<>();
            list.add(p);
            npcsForSome.replace(this, list);
        }
    }

    public void show() {
        toTablist();
        sendPacket(getPlayOutEntityMetadata());
        sendPacket(getPacketPlayOutNamedEntitySpawn());
        sendPacket(getPacketPlayOutEntityHeadRotation());
        npcsForAll.add(this);
    }

    public void remove() {
        sendPacket(getPacketPlayOutEntityDestroy());
        for (NPCNMS npc : npcsForAll) {
            npcsForAll.remove(npc);
        }
        for (Iterator<Map.Entry<NPCNMS, ArrayList<Player>>> iterator = npcsForSome.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<NPCNMS, ArrayList<Player>> entry = iterator.next();
            NPCNMS npc = entry.getKey();
            npcsForSome.remove(npc);

        }
    }
}
