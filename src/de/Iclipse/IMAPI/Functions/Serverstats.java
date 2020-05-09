package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.command.CommandSender;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Serverstats {
    @IMCommand(
            name = "serverstats",
            minArgs = 0,
            maxArgs = 0,
            description = "serverstats.description",
            usage = "serverstats.usage",
            permissions = "im.cmd.serverstats"
    )
    public void serversetats(CommandSender sender) {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.getName().startsWith("get")
                    && Modifier.isPublic(method.getModifiers())) {
                Object value;
                try {
                    value = method.invoke(operatingSystemMXBean);
                } catch (Exception e) {
                    value = e;
                } // try
                sender.sendMessage(Data.prefix + method.getName() + " = " + value);
            } // if
        } // for
    }
}
