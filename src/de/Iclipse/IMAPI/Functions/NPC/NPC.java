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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static de.Iclipse.IMAPI.Util.Reflections.sendPacket;

public class NPC {
    public static ArrayList<NPC> npcsForAll = new ArrayList<>();
    public static HashMap<NPC, ArrayList<Player>> npcsForSome = new HashMap<>();

    private Location loc;
    private UUID uuid;
    private String name;
    private String[] textures;
    private GameProfile gameProfile;
    private EntityPlayer entityPlayer;
    private boolean dinnerbone;

    public NPC(UUID uuid, String name, String[] textures, Location loc) {
        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldServer = ((CraftWorld) loc.getWorld()).getHandle();
        this.name = name;
        this.uuid = uuid;
        this.loc = loc;
        this.textures = textures;
        System.out.println("Textures: " + textures[0]);
        System.out.println("Signature " + textures[1]);

        this.gameProfile = new GameProfile(uuid, name);
        this.gameProfile.getProperties().put("textures", new Property("textures", textures[0], textures[1]));
        this.entityPlayer = new EntityPlayer(minecraftServer, worldServer, gameProfile, new PlayerInteractManager(worldServer));
        this.entityPlayer.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }


    public NPC(UUID uuid, String name, Location loc) {
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

    private PacketPlayOutEntityMetadata getCapePacket() {
        DataWatcher data = entityPlayer.getDataWatcher();
        entityPlayer.getDataWatcher().set(DataWatcherRegistry.a.a(16), (byte) 0x01);
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(entityPlayer.getId(), data, false);
        return packet;
    }

    private PacketPlayOutNamedEntitySpawn getPacketPlayOutNamedEntitySpawn() {
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn(entityPlayer);
        return packet;
    }

    private PacketPlayOutEntityHeadRotation getPacketPlayOutEntityHeadRotation() {
        PacketPlayOutEntityHeadRotation packet = new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) (entityPlayer.yaw * 256 / 360));
        return packet;
    }

    public void show(Player p) {
        toTablist(p);
        //sendPacket(getCapePacket(), p);
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
        //sendPacket(getCapePacket());
        sendPacket(getPacketPlayOutNamedEntitySpawn());
        sendPacket(getPacketPlayOutEntityHeadRotation());
        npcsForAll.add(this);
    }

}
