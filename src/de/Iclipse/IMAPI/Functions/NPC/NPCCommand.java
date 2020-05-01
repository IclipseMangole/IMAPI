package de.Iclipse.IMAPI.Functions.NPC;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.Iclipse.IMAPI.Data.dsp;

public class NPCCommand {
    StringBuilder builder;

    @IMCommand(
            name = "npc",
            maxArgs = 0,
            minArgs = 0,
            permissions = "im.cmd.npc",
            noConsole = true,
            usage = "npc.usage",
            description = "npc.description"
    )
    public void npc(Player p) {
        builder = new StringBuilder();
        add(p, "create");
        dsp.send(p, builder.toString());
    }

    @IMCommand(
            name = "create",
            parent = "npc",
            minArgs = 1,
            maxArgs = 1,
            usage = "npc.create.usage",
            description = "npc.create.description",
            noConsole = true,
            permissions = "im.cmd.npc.*"
    )
    public void create(Player p, String name) {
        NPC npc = new NPC(UUIDFetcher.getUUID(name), name, p.getLocation());
        npc.show();
        dsp.send(p, "npc.create.message");
    }

    private void add(CommandSender sender, String command) {
        builder.append("\n" + Data.symbol + "§e" + dsp.get("npc." + command + ".usage", sender) + "§8: §7 " + dsp.get("npc." + command + ".description", sender) + ChatColor.RESET);
    }
}
