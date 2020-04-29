package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import static de.Iclipse.IMAPI.Data.dsp;

public class Ping {
    @IMCommand(
            name = "ping",
            maxArgs = 0,
            noConsole = true,
            usage = "ping.usage",
            description = "ping.description",
            permissions = "im.cmd.ping"
    )
    public void ping(Player p) {
        dsp.send(p, "ping.message", ((CraftPlayer) p).getHandle().ping + "");
    }
}
