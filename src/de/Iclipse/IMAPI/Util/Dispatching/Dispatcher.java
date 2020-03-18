package de.Iclipse.IMAPI.Util.Dispatching;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Dispatcher extends Dispatch<CommandSender> {

    private String title = "";

    public Dispatcher(JavaPlugin plugin) {
        super(plugin.getName(), plugin.getLogger());
        title = plugin.getName();
    }

    public Dispatcher(String title, Logger logger) {
        super(title, logger);
        this.title = title;
    }

    @Override
    public void online(String key, String... args) {
        super.online(key, args);
    }

    @Override
    public void certain(String key, String permission, String... args) {
        super.certain(key, permission, args);
    }

    @Override
    public void send(CommandSender p, String key, String... args) {
        super.send(p, key, args);
    }

    @Override
    public void sendTextMessage(CommandSender receiver, String message) {
        receiver.sendMessage(message);
    }

    @Override
    public String get(String key, Language lang,Boolean prefix, String... args) {
        return super.get(key, lang, prefix, args);
    }

    @Override
    public String get(String key, Language lang, String... args) {
        return get(key, lang, false, args);
    }

    @Override
    public void logInfo(String message, String... args) {
        super.logInfo(message, args);
    }

    @Override
    public boolean isPlayerNull(CommandSender reciever, String player) {
        return super.isPlayerNull(reciever, player);
    }

    @Override
    public void playerNull(CommandSender reciever, String player) {
        super.playerNull(reciever, player);
    }

    @Override
    public void logSevere(String message, String... args) {
        super.logSevere(message, args);
    }

    @Override
    public void logWarning(String message, String... args) {
        super.logWarning(message, args);
    }

    public String getTitle() {
        return title;
    }

}
