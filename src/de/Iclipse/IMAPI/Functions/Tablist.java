package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import net.alpenblock.bungeeperms.BungeePermsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.UUID;

import static de.Iclipse.IMAPI.Data.dsp;

/* ~Yannick on 09.06.2019 at 11:47 o´ clock
 */
public class Tablist {

    private Team a;
    private Team b;
    private Team c;


    private HashMap<Player, String> rankColor = new HashMap<>();
    public static Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();


    public Tablist() {
        this.a = scoreboard.getTeam("1a") == null ? scoreboard.registerNewTeam("1a") : scoreboard.getTeam("1a");
        this.b = scoreboard.getTeam("2b") == null ? scoreboard.registerNewTeam("2b") : scoreboard.getTeam("2b");
        this.c = scoreboard.getTeam("3c") == null ? scoreboard.registerNewTeam("3c") : scoreboard.getTeam("3c");


        this.a.setPrefix("§7[§4Admin§7]§4 ");
        this.b.setPrefix("§7[§cMod§7]§c ");
        this.c.setPrefix("§3 ");
        this.a.setColor(ChatColor.getByChar('4'));
        this.b.setColor(ChatColor.getByChar('c'));
        this.c.setColor(ChatColor.getByChar('3'));
    }


    public void setTablist(Player p) {
        p.setPlayerListHeader(getHeader(p));
        p.setPlayerListFooter(getFooter(p));
    }


    private String getHeader(Player p) {
        String line0 = "§8----------«§5 §lIM§8-§f§lNETWORK§r§8 »----------";
        String line1 = dsp.get("tablist.header1", p, p.getName());
        String line2 = dsp.get("tablist.header2", p, IMAPI.getServerName());

        return line0 + "\n" + line1 + "\n" + line2 + "\n ";
    }

    private String getFooter(Player p) {
        String line3 = "§8--------------------------------";
        String line0 = dsp.get("tablist.footer1", p);
        String line1 = dsp.get("tablist.footer2", p);
        String line2 = dsp.get("tablist.footer3", p);
        return "\n" + line0 + "\n" + line1 + "\n" + line2 + "\n" + line3;
    }


    public void setPlayer(Player p) {
        String team = "";
        if (p.hasPermission("im.color.admin")) {
            team = "1a";
        } else if (p.hasPermission("im.color.mod")) {
            team = "2b";
        } else {
            team = "3c";
        }
        if (!scoreboard.getTeam(team).hasEntry(p.getName())) scoreboard.getTeam(team).addEntry(p.getName());
        rankColor.put(p, scoreboard.getTeam(team).getPrefix());

        String name = "";
        name = scoreboard.getTeam(team).getPrefix() + p.getName();
        ChatColor.translateAlternateColorCodes('§', name);

        p.setPlayerListName(name);
        p.setDisplayName(name);
        p.setCustomName(name);
        p.setCustomNameVisible(true);
        Bukkit.getOnlinePlayers().forEach(pl -> pl.setScoreboard(scoreboard));
        Bukkit.getScheduler().runTaskTimer(Data.instance, () -> {
            Bukkit.getOnlinePlayers().forEach(pl -> pl.setScoreboard(scoreboard));
        }, 20, 20);
    }

    public String getPrefix(Player p) {
        if (p != null) {
            if (rankColor.containsKey(p)) {
                return rankColor.get(p);
            }
        }

        String team;
        if (p.hasPermission("im.color.admin")) {
            team = "1a";
        } else if (p.hasPermission("im.color.mod")) {
            team = "2b";
        } else {
            team = "3c";
        }
        return scoreboard.getTeam(team).getPrefix();
    }

    public String getPrefix(UUID uuid) {
        return BungeePermsAPI.groupDisplay(BungeePermsAPI.userMainGroup(UUIDFetcher.getName(uuid)), IMAPI.getServerName(), null).replace(ChatColor.RESET.toString(), "");
    }


    public String centered(String upperString, String toCenter) {
        String empty = "";
        for (int i = 0; i < (upperString.length() / 2) - (toCenter.length() / 2) - 1; i++) {
            empty += " ";
        }
        return "|" + empty + toCenter + empty + "|";
    }


}
