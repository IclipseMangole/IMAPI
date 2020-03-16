package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.UUID;


/**
 * Created by Yannick who could get really angry if somebody steal his code!
 * ~Yannick on 09.06.2019 at 11:47 o´ clock
 */
public class Tablist {
    static String header;
    static String footer;
    static String port;
    static String ranks;

    public final Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
    private Team a;
    private Team b;
    private Team c;
    private Team d;
    private Team e;
    private Team f;
    private Team g;
    private Team h;


    private HashMap<Player, String> rankColor = new HashMap<>();


    public Tablist() {
        header = "§8« " + Data.prefix.replaceAll(Data.symbol, "") + "§8 »";
        footer = "§7Server: §e" + Data.instance.getDataFolder().getAbsoluteFile().getParentFile().getParentFile().getName();
        port = "\n§7Port: §e" + Bukkit.getPort();
        ranks = "§4Admin §cMod \n §3User";

        this.a = sb.getTeam("1a") == null ? sb.registerNewTeam("1a") : sb.getTeam("1a");
        this.b = sb.getTeam("2b") == null ? sb.registerNewTeam("2b") : sb.getTeam("2b");
        this.c = sb.getTeam("3c") == null ? sb.registerNewTeam("3c") : sb.getTeam("3c");


        this.a.setPrefix("§7[§4Admin§7]§4 ");
        this.b.setPrefix("§7[§cMod§7]§c ");
        this.c.setPrefix("§3 ");
    }


    public void setTablist(Player p) {

        if (p.hasPermission("im.tab.serversettings")) {
            p.setPlayerListHeader(header + port);
        } else p.setPlayerListHeader(header);

        p.setPlayerListFooter(ranks + "\n" + footer);
    }


    public void setPlayer(Player p) {
        String team = "";
        team = "";
        if (p.hasPermission("im.color.admin")) {
            team = "1a";
        } else if (p.hasPermission("im.color.mod")) {
            team = "2b";
        } else  {
            team = "3c";
        }
        if (!sb.getTeam(team).hasPlayer(Bukkit.getOfflinePlayer(p.getUniqueId())))
            sb.getTeam(team).addPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()));
        if (!sb.getTeam(team).hasEntry(p.getName())) sb.getTeam(team).addEntry(p.getName());
        rankColor.put(p, sb.getTeam(team).getPrefix());

        String name = "";
        name = String.valueOf(sb.getTeam(team).getPrefix()) + p.getName();
        System.out.println(name);
        ChatColor.translateAlternateColorCodes('§', name);

        p.setPlayerListName(name);
        p.setDisplayName(name);
        p.setCustomName(name);
        p.setCustomNameVisible(true);
        p.setScoreboard(sb);
        Bukkit.getScheduler().runTaskTimer(Data.instance, () -> {
            Bukkit.getOnlinePlayers().forEach(pl -> pl.setScoreboard(sb));
        }, 1, 1);
    }

    public String getPrefix(Player p) {
        if (rankColor.containsKey(p)) {
            return rankColor.get(p);
        } else {
            String team;
            if (p.hasPermission("px.color.admin")) {
                team = "1a";
            } else if (p.hasPermission("px.color.mod")) {
                team = "2b";
            } else if (p.hasPermission("px.color.dev")) {
                team = "3c";
            } else if (p.hasPermission("px.color.sup")) {
                team = "4d";
            } else if (p.hasPermission("px.color.builder")) {
                team = "5e";
            } else if (p.hasPermission("px.color.youtuber")) {
                team = "6f";
            } else if (p.hasPermission("px.color.premium")) {
                team = "7g";
            } else {
                team = "8h";
            }
            return sb.getTeam(team).getPrefix();
        }
    }


}
