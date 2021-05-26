package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import net.alpenblock.bungeeperms.BungeePermsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.UUID;


/* ~Yannick on 09.06.2019 at 11:47 o´ clock
 */
public class Tablist {

    private final IMAPI imapi;

    public Tablist(IMAPI imapi) {
        this.imapi = imapi;
    }


    public void setTablist(Player p) {
        p.setPlayerListHeader(getHeader(p));
        p.setPlayerListFooter(getFooter(p));
    }


    private String getHeader(Player p) {
        String line0 = "§8----------«§5 §lIM§8-§f§lNETWORK§r§8 »----------";
        String line1 = imapi.getData().getDispatcher().get("tablist.header1", p, p.getName());
        String line2 = imapi.getData().getDispatcher().get("tablist.header2", p, imapi.getServerName());

        return line0 + "\n" + line1 + "\n" + line2 + "\n ";
    }

    private String getFooter(Player p) {
        String line3 = "§8--------------------------------";
        String line0 = imapi.getData().getDispatcher().get("tablist.footer1", p);
        String line1 = imapi.getData().getDispatcher().get("tablist.footer2", p);
        String line2 = imapi.getData().getDispatcher().get("tablist.footer3", p);
        return "\n" + line0 + "\n" + line1 + "\n" + line2 + "\n" + line3;
    }


    public void setPlayer(Player p) {
        Scoreboard scoreboard = createScoreboard();

        String team = "";
        if (p.hasPermission("im.color.admin")) {
            team = "1a";
        } else if (p.hasPermission("im.color.mod")) {
            team = "2b";
        } else {
            team = "3c";
        }
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer == p) {
                p.setScoreboard(scoreboard);
            }
            Scoreboard scoreboard1 = onlinePlayer.getScoreboard();
            if (!scoreboard1.getTeam(team).hasEntry(p.getName())) {
                scoreboard1.getTeam(team).addEntry(p.getName());
            }
        }

        String name;
        name = scoreboard.getTeam(team).getPrefix() + p.getName();
        ChatColor.translateAlternateColorCodes('§', name);

        p.setPlayerListName(name);
        p.setDisplayName(name);
        p.setCustomName(name);
        p.setCustomNameVisible(true);
    }

    public Scoreboard createScoreboard() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Team a = scoreboard.registerNewTeam("1a");
        Team b = scoreboard.registerNewTeam("2b");
        Team c = scoreboard.registerNewTeam("3c");


        a.setPrefix("§7[§4Admin§7]§4 ");
        b.setPrefix("§7[§cMod§7]§c ");
        c.setPrefix("§3 ");
        a.setColor(ChatColor.getByChar('4'));
        b.setColor(ChatColor.getByChar('c'));
        c.setColor(ChatColor.getByChar('3'));
        return scoreboard;
    }

    public String getPrefix(UUID uuid) {
        return BungeePermsAPI.groupDisplay(BungeePermsAPI.userMainGroup(UUIDFetcher.getName(uuid)), imapi.getServerName(), null).replace(ChatColor.RESET.toString(), "");
    }


}
