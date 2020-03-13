package de.Iclipse.IMAPI.Util.Command;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Functions.Listener.Completer;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BukkitCommand extends Command implements CommandExecutor, TabCompleter {
    private JavaPlugin plugin;
    private Object function;
    private IMCommand command;
    private GlobalCommand<CommandSender> processor;

    public BukkitCommand(JavaPlugin plugin, Object function, Method method, IMCommand baseCommand) {
        super(baseCommand.name(), baseCommand.description(), baseCommand.usage(), Arrays.asList(baseCommand.aliases()));
        this.plugin = plugin;
        this.function = function;
        this.command = baseCommand;
        this.processor = new GlobalCommand<CommandSender>(baseCommand, function, method, Player.class) {
            @Override
            public boolean checkPermission(CommandSender sender, String permission) {
                return sender.hasPermission(permission);
            }
        };
    }

    public void register() {
        PluginCommand cmd = plugin.getCommand(command.name());
        if (cmd != null) {
            String usage = command.usage();
            if (usage.isEmpty()) {
                usage = cmd.getUsage();
            }

            cmd.setUsage(usage);
            cmd.setExecutor(this);
            cmd.setTabCompleter(new Completer());
            if (command.aliases().length > 0) {
                cmd.setAliases(Arrays.asList(command.aliases()));
            }
        } else {
            try {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);

                CommandMap commandMap = (CommandMap) (field.get(Bukkit.getPluginManager()));
                commandMap.register(command.name(), this);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
        processor.process(sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        final ArrayList<String>[] list = new ArrayList[]{new ArrayList<>()};
        Data.completions.forEach((cmd, map) ->{
            if(cmd.name().equals(command.getName())){
                list[0] = map.get(strings.length);
            }
        });
        return list[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        processor.process(sender, args);
        return true;
    }


    public GlobalCommand<CommandSender> getProcessor() {
        return processor;
    }
}
