package de.Iclipse.IMAPI.Functions.Commands;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.Iclipse.IMAPI.Data.dsp;
import static de.Iclipse.IMAPI.Data.prefix;

public class cmd_world {
    StringBuilder builder;
    @IMCommand(
            name = "world",
            permissions = "im.cmd.world",
            usage = "world.world",
            description = "world.description",
            minArgs = 0,
            maxArgs = 0
    )
    public void world(CommandSender sender){
        builder = new StringBuilder();
        builder.append(prefix + "§7§lHilfsübersicht:§r\n");
        add(sender, "create");
        add(sender, "teleport");
        add(sender, "list");
        sender.sendMessage(builder.toString());
    }

    @IMCommand(
            name = "create",
            permissions = "im.cmd.world.create",
            usage = "world.create.description",
            description = "world.create.description",
            minArgs = 1,
            maxArgs = 1,
            parent = "world"
    )
    public void create(CommandSender sender, String name){
        Bukkit.getWorlds().forEach(entry ->{
            if(entry.getName().equalsIgnoreCase(name)){
                dsp.send(sender, "world.create.exists");
                return;
            }
        });
        Bukkit.getServer().createWorld(new WorldCreator(name));
        dsp.send(sender, "world.create.success");
    }

    @IMCommand(
            name = "teleport",
            permissions = "im.cmd.world.teleport",
            usage = "world.teleport.usage",
            description = "world.teleport.description",
            minArgs = 1,
            maxArgs = 1,
            noConsole = true,
            parent = "world"
    )
    public void teleport(Player p, String name){
        Bukkit.getWorlds().forEach(entry ->{
            if(entry.getName().equalsIgnoreCase(name)){
                dsp.send(p, "world.teleport.success");
                p.teleport(entry.getSpawnLocation());
                return;
            }
        });
        dsp.send(p, "world.teleort.notfound");
    }

    @IMCommand(
            name = "list",
            permissions = "im.cmd.world.list",
            usage = "world.list.usage",
            description = "world.list.description",
            minArgs = 0,
            maxArgs = 0,
            parent = "world"
    )
    public void list(CommandSender sender){
        StringBuilder builder = new StringBuilder();
        builder.append(dsp.get("world.name", sender));
        Bukkit.getWorlds().forEach(entry ->{
            builder.append("§e" + entry.getName() + "§7,");
        });
        sender.sendMessage(builder.toString());
    }


    private void add(CommandSender sender, String command) {
        builder.append("\n" + Data.symbol + "§e" + dsp.get("world." + command + ".usage", sender) + "§8: §7 " + dsp.get("world." + command + ".description", sender) + ChatColor.RESET);
    }
}
