package de.Iclipse.IMAPI.Listener;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Database.Server;
import de.Iclipse.IMAPI.Database.User;
import de.Iclipse.IMAPI.Database.UserSettings;
import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import net.minecraft.server.v1_15_R1.PacketPlayOutScoreboardObjective;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

import static de.Iclipse.IMAPI.Data.dsp;
import static de.Iclipse.IMAPI.Data.instance;
import static de.Iclipse.IMAPI.Util.ScoreboardSign.setField;
import static de.Iclipse.IMAPI.Util.UUIDFetcher.getUUID;

public class JoinListener implements Listener {
    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if (!e.getHostname().equalsIgnoreCase("207.180.241.195:25565")) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage(dsp.get("proxyjoin.blocked", e.getPlayer()));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
        setField(packet, "a", e.getPlayer().getName());
        setField(packet, "d", 1);
        ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(packet);
        Data.onlinetime.put(p, System.currentTimeMillis());
        Data.blocks.put(p, User.getBlocksPlaced(getUUID(p.getName())));
        createSettings(UUIDFetcher.getUUID(p.getName()));
        Data.tablist.setTablist(p);
        Data.tablist.setPlayer(p);
        if (UserSettings.getBoolean(UUIDFetcher.getUUID(p.getName()), "vanish")) {
            Bukkit.getOnlinePlayers().forEach(entry -> {
                entry.hidePlayer(instance, p);
            });
            dsp.send(p, "vanish.join");
        } else {
            if (Data.updatePlayers)
                Server.setPlayers(IMAPI.getServerName(), Server.getPlayers(IMAPI.getServerName()) + 1);
        }
        User.setLastTime(p, -1);
        User.setServer(UUIDFetcher.getUUID(p.getName()), IMAPI.getServerName());

        /*
        System.out.println("BungeePerms UserPrefix: " + BungeePermsAPI.userPrefix(p.getName(), IMAPI.getServerName() + p.getName(), null).replace(ChatColor.RESET.toString(), "") + p.getName());
        System.out.println("BungeePerms GroupPrefix: " + BungeePermsAPI.groupPrefix(BungeePermsAPI.userMainGroup(p.getName()), IMAPI.getServerName(), null).replace(ChatColor.RESET.toString(), "") + p.getName());
        System.out.println("BungeePerms GroupDisplay: " + BungeePermsAPI.groupDisplay(BungeePermsAPI.userMainGroup(p.getName()), IMAPI.getServerName(), null).replace(ChatColor.RESET.toString(), "") + p.getName());
        System.out.println("getPrefix of Permission (UUID)" + Data.tablist.getPrefix(UUIDFetcher.getUUID(p.getName())).replace(ChatColor.RESET.toString(), "") + p.getName());
        System.out.println("getPrefix of Permission (Player)" + Data.tablist.getPrefix(p).replace(ChatColor.RESET.toString(), "") + p.getName());
        */
    }


    public void createSettings(UUID uuid) {
        UserSettings.createUserSetting(uuid, "profile_page", 0); //0: Friend, 1: Settings, 2: Lobbyinventory 3: News

        //BARO
        UserSettings.createUserSetting(uuid, "baro_maxPlayerBars", 2);
        UserSettings.createUserSetting(uuid, "baro_barSettingZone", false); //true = zone, false = events
        UserSettings.createUserSetting(uuid, "baro_borderParticle", Particle.SPELL_WITCH.name());
        //Sound
        //UserSettings.createUserSetting(uuid, "baro_sound_");

        //Bedwars

        //Bingo

        //Friend
        UserSettings.createUserSetting(uuid, "friend_sort", 3);

        UserSettings.createUserSetting(uuid, "friend_message", 0);
        UserSettings.createUserSetting(uuid, "friend_message_users", true);
        UserSettings.createUserSetting(uuid, "friend_message_teammembers", true);
        UserSettings.createUserSetting(uuid, "friend_message_partymembers", true);
        UserSettings.createUserSetting(uuid, "friend_message_friends", true);
        UserSettings.createUserSetting(uuid, "friend_message_favorites", true);

        UserSettings.createUserSetting(uuid, "friend_request_users", true);
        UserSettings.createUserSetting(uuid, "friend_request_teammembers", true);
        UserSettings.createUserSetting(uuid, "friend_request_partymembers", true);

        UserSettings.createUserSetting(uuid, "friend_jump", 0);


        //Party
        UserSettings.createUserSetting(uuid, "party_invite_users", true);
        UserSettings.createUserSetting(uuid, "party_invite_teammembers", true);
        UserSettings.createUserSetting(uuid, "party_invite_friends", true);
        UserSettings.createUserSetting(uuid, "party_invite_favorites", true);

        //Visibility
        UserSettings.createUserSetting(uuid, "visibility", 0);
        UserSettings.createUserSetting(uuid, "visibility_message_users", true);
        UserSettings.createUserSetting(uuid, "visibility_message_teammembers", true);
        UserSettings.createUserSetting(uuid, "visibility_message_partymembers", true);
        UserSettings.createUserSetting(uuid, "visibility_message_friends", true);
        UserSettings.createUserSetting(uuid, "visibility_message_favorites", true);
        //Status
        UserSettings.createUserSetting(uuid, "status_line1", "");
        UserSettings.createUserSetting(uuid, "status_line2", "");
        //Language
        UserSettings.createUserSetting(uuid, "language", "DE");
        //Design
        UserSettings.createUserSetting(uuid, "design_primary", "BLACK");
        UserSettings.createUserSetting(uuid, "design_secondary", "YELLOW");

        UserSettings.createUserSetting(uuid, "vanish", false);
    }
}
