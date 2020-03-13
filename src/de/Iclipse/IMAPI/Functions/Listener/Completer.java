package de.Iclipse.IMAPI.Functions.Listener;

import de.Iclipse.IMAPI.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.List;

public class Completer implements TabCompleter, Listener {

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

    @EventHandler
    public void onComplete(TabCompleteEvent e){
        ArrayList<String> list = new ArrayList<>();
        CommandSender sender = e.getSender();
        String[] args = e.getBuffer().split(" ");
        System.out.println("Länge: " + args.length);
        if(args.length == 1){
            if(args[0].startsWith("/")){
                System.out.println("Starts with /");
                Data.completions.forEach((command, map) ->{
                    boolean hasPermission = true;
                    for (String permission : command.permissions()) {
                        if(!sender.hasPermission(permission)){
                            hasPermission = false;
                        }
                    }
                    if(hasPermission){
                        list.add(command.name());
                    }
                });
            }
        }
        e.setCompletions(list);
        return;
    }
}
