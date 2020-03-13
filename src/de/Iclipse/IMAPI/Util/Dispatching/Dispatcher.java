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
    public void online(String key, Object... args) {
        super.online(key, args);
    }

    @Override
    public void certain(String key, String permission, Object... args) {
        super.certain(key, permission, args);
    }

    @Override
    public void send(CommandSender p, String key, Object... args) {
        super.send(p, key, args);
    }

    @Override
    public void sendTextMessage(CommandSender receiver, String message) {
        receiver.sendMessage(message);
    }

    @Override
    public String get(String key, Language lang,Boolean prefix, Object... params) {
        return super.get(key, lang, prefix, params);
    }

    @Override
    public String get(String key, Language lang, Object... params) {
        return get(key, lang, false, params);
    }

    @Override
    public void logInfo(String message, Object... args) {
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
    public void logSevere(String message, Object... args) {
        super.logSevere(message, args);
    }

    @Override
    public void logWarning(String message, Object... args) {
        super.logWarning(message, args);
    }

    public String getTitle() {
        return title;
    }

}
