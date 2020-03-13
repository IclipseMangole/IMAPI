package de.Iclipse.IMAPI.Util.Dispatching;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.logging.Logger;

import static de.Iclipse.IMAPI.Functions.MySQL.MySQL_User.getLanguage;
import static de.Iclipse.IMAPI.Functions.MySQL.MySQL_User.isUserExists;

public abstract class Dispatch<R> {
    private String title;
    private Logger logger;
    private String textcolor;
    private String highlight;
    private String warning;
    private java.util.ResourceBundle bundleDE = ResourceBundle.msgDE;

    public Dispatch(String title, Logger logger) {
        this.title = title;
        this.logger = logger;
        this.textcolor = bundleDE.getString("color.text");
        this.highlight = bundleDE.getString("color.highlight");
        this.warning = bundleDE.getString("color.warning");
        Data.textcolor = this.textcolor;
        Data.highlight = this.highlight;
        Data.warning = this.warning;
    }

    public String get(String key, Language lang, Object... args) {
        return get(key, lang, false, args);
    }

    public String get(String key, Language lang, Boolean prefix, Object... args) {
        try {
            StringBuilder builder = new StringBuilder();
            if (prefix) builder.append(Data.prefix.replace("IM", title));


            builder.append(MessageFormat.format(
                    lang.getBundle()
                            .getString(key)
                    , args).replaceAll("%r", textcolor)
                    .replaceAll("%w", warning)
                    .replaceAll("%h", highlight)
                    .replaceAll("%z", "\n" + Data.symbol + " "));
            return builder.toString();
        } catch (MissingResourceException e) {
            return "Missing resource-key!";
        }
    }


    public void logInfo(String message, Object... args) {
        this.logger.info(this.get(message, Data.defaultLang, args));
    }

    public void logWarning(String message, Object... args) {
        this.logger.warning(this.get(message, Data.defaultLang, args));
    }

    public void logSevere(String message, Object... args) {
        this.logger.severe(this.get(message, Data.defaultLang, args));
    }

    public boolean isPlayerNull(R receiver, String player) {
        if (!isUserExists(UUIDFetcher.getUUID(player))) {
            if (receiver instanceof Player) {
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.invalidplayer", getLanguage(((Player) receiver).getUniqueId()), player));
                return true;
            } else {
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.invalidplayer", Data.defaultLang, player));
                return true;
            }
        } else {
            if(receiver instanceof Player) {
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.playeroffline", getLanguage(((Player) receiver).getUniqueId()), player));
                return false;
            }else{
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.playeroffline", Data.defaultLang, player));
                return false;
            }
        }
    }

    public void playerNull(R receiver, String player) {
        if (!isUserExists(UUIDFetcher.getUUID(player))) {
            if (receiver instanceof Player) {
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.invalidplayer", getLanguage(((Player) receiver).getUniqueId()),player));
            } else {
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.invalidplayer", Data.defaultLang, player));
            }
        } else {
            if (receiver instanceof Player) {
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.playeroffline", getLanguage(((Player) receiver).getUniqueId()),player));
            }else{
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.playeroffline", Data.defaultLang,player));
            }
        }
    }

    public void certain(String key, String permission, Object... args) {
        Bukkit.getOnlinePlayers().stream().filter(R -> R.hasPermission(permission)).forEach(R -> R.sendMessage(Data.prefix.replace("IM", title) + get(key, getLanguage(R.getUniqueId()), args)));
    }

    public void online(String key, Object... args) {
        Bukkit.getOnlinePlayers().forEach(R -> R.sendMessage(Data.prefix.replace("IM", title) + get(key, getLanguage(R.getUniqueId()), args)));
    }

    public void send(R receiver, String key, Object... args) {
        if (receiver instanceof Player) {
            sendTextMessage(receiver, Data.prefix + get(key, getLanguage(((Player) receiver).getUniqueId()), args));
        } else {

        }
    }

    public abstract void sendTextMessage(R receiver, String submit);

}
