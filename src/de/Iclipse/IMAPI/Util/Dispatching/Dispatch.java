package de.Iclipse.IMAPI.Util.Dispatching;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static de.Iclipse.IMAPI.Data.dispatching;
import static de.Iclipse.IMAPI.Database.User.getLanguage;
import static de.Iclipse.IMAPI.Database.User.isUserExists;

public abstract class Dispatch<R> {
    private String title;
    private Logger logger;
    private String textcolor;
    private String highlight;
    private String warning;
    private ResourceBundle defaultLang;
    private HashMap<String, ResourceBundle> langs;

    public Dispatch(String title, Logger logger, HashMap<String, ResourceBundle> langs) {
        this.title = title;
        this.logger = logger;
        this.langs = langs;
        final boolean[] first = {true};
        langs.forEach((name, bundle) ->{
            if(first[0]) {
                defaultLang = bundle;
                first[0] = false;
            }
        });
        if(dispatching) {
            this.textcolor = defaultLang.getString("color.text");
            this.highlight = defaultLang.getString("color.highlight");
            this.warning = defaultLang.getString("color.warning");
        }else{
            this.textcolor = "§7";
            this.highlight = "§e";
            this.warning = "§c";
        }
        Data.textcolor = this.textcolor;
        Data.highlight = this.highlight;
        Data.warning = this.warning;
    }

    public String get(String key, ResourceBundle lang, String... args) {
        return get(key, lang, false, args);
    }

    public String get(String key, java.util.ResourceBundle lang, Boolean prefix, String... args) {
        try {
            StringBuilder builder = new StringBuilder();
            if (prefix) builder.append(Data.prefix.replace("IM", title));


            builder.append(MessageFormat.format(lang.getString(key), args).replaceAll("%r", textcolor)
                    .replaceAll("%w", warning)
                    .replaceAll("%h", highlight)
                    .replaceAll("%z", "\n" + Data.symbol + " "));
            return builder.toString();
        } catch (MissingResourceException | NullPointerException e) {
            return key;
        }
    }

    public String get(String key, CommandSender p, String... args){
        if(p instanceof Player) {
            return get(key, langs.get(getLanguage(UUIDFetcher.getUUID(p.getName()))), args);
        }else{
            return get(key, defaultLang, args);
        }
    }

    public String get(String key, CommandSender p, Boolean prefix, String... args){
        if(p instanceof Player) {
            return get(key, langs.get(getLanguage(UUIDFetcher.getUUID(p.getName()))), prefix, args);
        }else{
            return get(key, defaultLang, prefix, args);
        }
    }


    public void logInfo(String message, String... args) {
        this.logger.info(this.get(message, defaultLang, args));
    }

    public void logWarning(String message, String... args) {
        this.logger.warning(this.get(message, defaultLang, args));
    }

    public void logSevere(String message, String... args) {
        this.logger.severe(this.get(message, defaultLang, args));
    }

    public boolean isPlayerNull(R receiver, String player) {
        if (!isUserExists(UUIDFetcher.getUUID(player))) {
            if (receiver instanceof Player) {
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.invalidplayer", langs.get(getLanguage(UUIDFetcher.getUUID(((Player) receiver).getName()))), player));
                return true;
            } else {
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.invalidplayer", defaultLang, player));
                return true;
            }
        } else {
            if(receiver instanceof Player) {
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.playeroffline", langs.get(getLanguage(UUIDFetcher.getUUID(((Player) receiver).getName()))), player));
                return false;
            }else{
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.playeroffline", defaultLang, player));
                return false;
            }
        }
    }

    public void playerNull(R receiver, String player) {
        if (!isUserExists(UUIDFetcher.getUUID(player))) {
            if (receiver instanceof Player) {
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.invalidplayer", langs.get(getLanguage(UUIDFetcher.getUUID(((Player) receiver).getName()))),player));
            } else {
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.invalidplayer", defaultLang, player));
            }
        } else {
            if (receiver instanceof Player) {
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.playeroffline", langs.get(getLanguage(UUIDFetcher.getUUID(((Player) receiver).getName()))),player));
            }else{
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.playeroffline", defaultLang,player));
            }
        }
    }

    public void certain(String key, String permission, String... args) {
        Bukkit.getOnlinePlayers().stream().filter(R -> R.hasPermission(permission)).forEach(R -> R.sendMessage(Data.prefix.replace("IM", title) + get(key, langs.get(getLanguage(UUIDFetcher.getUUID(R.getName()))), args)));
    }

    public void online(String key, String... args) {
        Bukkit.getOnlinePlayers().forEach(R -> R.sendMessage(Data.prefix.replace("IM", title) + get(key, langs.get(getLanguage(UUIDFetcher.getUUID(R.getName()))), args)));
    }

    public void send(R receiver, String key, String... args) {
        if (receiver instanceof Player) {
            sendTextMessage(receiver, Data.prefix + get(key, langs.get(getLanguage(UUIDFetcher.getUUID(((Player) receiver).getName()))), args));
        } else {
            sendTextMessage(receiver, Data.prefix + get(key, defaultLang, args));
        }
    }

    public HashMap<String, ResourceBundle> getLanguages(){
        return langs;
    }

    public ResourceBundle getDefaultLang(){
        return defaultLang;
    }


    public abstract void sendTextMessage(R receiver, String submit);


}
