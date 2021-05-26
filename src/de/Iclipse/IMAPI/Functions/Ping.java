package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Ping {

    private final IMAPI imapi;

    public Ping(IMAPI imapi) {
        this.imapi = imapi;
    }

    @IMCommand(
            name = "ping",
            maxArgs = 0,
            noConsole = true,
            usage = "ping.usage",
            description = "ping.description",
            permissions = "im.cmd.ping"
    )
    public void ping(Player p) {
        imapi.getData().getDispatcher().send(p, "ping.message", ((CraftPlayer) p).getHandle().ping + "");
    }
}
