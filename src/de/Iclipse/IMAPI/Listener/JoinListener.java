package de.Iclipse.IMAPI.Listener;

import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

import static de.Iclipse.IMAPI.Util.UUIDFetcher.getUUID;

public class JoinListener implements Listener {
    
    private final IMAPI imapi;

    public JoinListener(IMAPI imapi) {
        this.imapi = imapi;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {

        if (!e.getHostname().equalsIgnoreCase("75.119.142.165:25565")) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage(imapi.getData().getDispatcher().get("proxyjoin.blocked", e.getPlayer()));
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        /*
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
        setField(packet, "a", e.getPlayer().getName());
        setField(packet, "d", 1);
        ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(packet);
         */
        imapi.getData().getOnlinetime().put(p, System.currentTimeMillis());
        imapi.getData().getBlocks().put(p, imapi.getData().getUserTable().getBlocksPlaced(getUUID(p.getName())));
        createSettings(UUIDFetcher.getUUID(p.getName()));
        imapi.getData().getTablist().setTablist(p);
        imapi.getData().getTablist().setPlayer(p);
        if (imapi.getData().getUserSettingsTable().getBoolean(UUIDFetcher.getUUID(p.getName()), "vanish")) {
            Bukkit.getOnlinePlayers().forEach(entry -> entry.hidePlayer(imapi, p));
            imapi.getData().getDispatcher().send(p, "vanish.join");
        } else {
            if (imapi.getData().isUpdatePlayers())
                imapi.getData().getServerTable().setPlayers(imapi.getServerName(), imapi.getData().getServerTable().getPlayers(imapi.getServerName()) + 1);
        }
        imapi.getData().getUserTable().setLastTime(p, -1);
        imapi.getData().getUserTable().setServer(UUIDFetcher.getUUID(p.getName()), imapi.getServerName());

        /*
        System.out.println("BungeePerms UserPrefix: " + BungeePermsAPI.userPrefix(p.getName(), IMAPI.getServerName() + p.getName(), null).replace(ChatColor.RESET.toString(), "") + p.getName());
        System.out.println("BungeePerms GroupPrefix: " + BungeePermsAPI.groupPrefix(BungeePermsAPI.userMainGroup(p.getName()), IMAPI.getServerName(), null).replace(ChatColor.RESET.toString(), "") + p.getName());
        System.out.println("BungeePerms GroupDisplay: " + BungeePermsAPI.groupDisplay(BungeePermsAPI.userMainGroup(p.getName()), IMAPI.getServerName(), null).replace(ChatColor.RESET.toString(), "") + p.getName());
        System.out.println("getPrefix of Permission (UUID)" + Data.tablist.getPrefix(UUIDFetcher.getUUID(p.getName())).replace(ChatColor.RESET.toString(), "") + p.getName());
        System.out.println("getPrefix of Permission (Player)" + Data.tablist.getPrefix(p).replace(ChatColor.RESET.toString(), "") + p.getName());
        */
    }


    public void createSettings(UUID uuid) {
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "profile_page", 0); //0: Friend, 1: Settings, 2: Lobbyinventory 3: News

        //BARO
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "baro_maxPlayerBars", 2);
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "baro_barSettingZone", false); //true = zone, false = events
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "baro_borderParticle", Particle.SPELL_WITCH.name());
        //Sound
        //imapi.getData().getUserSettingsTable().createUserSetting(uuid, "baro_sound_");

        //Bedwars

        //Bingo

        //Friend
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "friend_sort", 3);

        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "friend_message", 0);
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "friend_message_users", true);
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "friend_message_teammembers", true);
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "friend_message_partymembers", true);
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "friend_message_friends", true);
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "friend_message_favorites", true);

        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "friend_request_users", true);
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "friend_request_teammembers", true);
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "friend_request_partymembers", true);

        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "friend_jump", 0);


        //Party
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "party_invite_users", true);
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "party_invite_teammembers", true);
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "party_invite_friends", true);
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "party_invite_favorites", true);

        //Visibility
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "visibility", 0);
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "visibility_message_users", true);
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "visibility_message_teammembers", true);
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "visibility_message_partymembers", true);
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "visibility_message_friends", true);
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "visibility_message_favorites", true);
        //Status
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "status_line1", "");
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "status_line2", "");
        //Language
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "language", "DE");
        //Design
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "design_primary", "BLACK");
        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "design_secondary", "YELLOW");

        imapi.getData().getUserSettingsTable().createUserSetting(uuid, "vanish", false);
    }
}
