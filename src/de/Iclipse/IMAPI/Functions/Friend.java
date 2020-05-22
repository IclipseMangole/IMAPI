package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Database.User;
import de.Iclipse.IMAPI.Database.UserSettings;
import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import net.alpenblock.bungeeperms.BungeePermsAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static de.Iclipse.IMAPI.Data.dsp;

public class Friend {
    private StringBuilder builder;

    @IMCommand(
            name = "friend",
            usage = "friend.usage",
            description = "friend.description",
            minArgs = 0,
            maxArgs = 0,
            noConsole = true,
            permissions = "im.cmd.friend"
    )
    public void execute(Player p) {
        builder = new StringBuilder();
        builder.append(Data.prefix + "§7§lHilfsübersicht:§r\n");
        addToOverview(p, "list");
        addToOverview(p, "add");
        addToOverview(p, "remove");
        addToOverview(p, "jump");
        p.sendMessage(builder.toString());
    }

    @IMCommand(
            name = "list",
            parent = "friend",
            usage = "friend.list.usage",
            description = "friend.list.description",
            minArgs = 0,
            maxArgs = 1,
            noConsole = true,
            permissions = "im.cmd.friend.list"
    )
    public void list(Player p, String pageString) {
        int page;
        if (pageString == null) {
            page = 0;
        } else {
            try {
                page = Integer.parseInt(pageString);
            } catch (NumberFormatException e) {
                dsp.send(p, "friend.list.usage");
                return;
            }
            if (page > 0) {
                page--;
            } else {
                dsp.send(p, "friend.list.usage");
            }
        }
        ArrayList<UUID> friends = de.Iclipse.IMAPI.Database.Friend.getFriendsSorted(UUIDFetcher.getUUID(p.getName()), UserSettings.getInt(UUIDFetcher.getUUID(p.getName()), "friend_sort"));
        ArrayList<UUID> shown = IMAPI.getPage(friends, 8, page);
        if (shown.size() > 0) {
            dsp.send(p, "friend.list.header", page + 1 + "", friends.size() / 8 + "");
            shown.forEach(user -> {
                if (User.isOnline(user)) {
                    dsp.send(p, "friend.list.format.online", BungeePermsAPI.userPrefix(user.toString(), IMAPI.getServerName(), null) + UUIDFetcher.getName(user), User.getServer(user));
                } else {
                    int days = (int) (System.currentTimeMillis() - User.getLastTime(user)) / (1000 * 60 * 60 * 24);
                    dsp.send(p, "friend.list.format.offline", UUIDFetcher.getName(user) + ": §cOffline", days + "");
                }
            });
        } else {
            dsp.send(p, "friend.list.empty");
        }
    }

    @IMCommand(
            name = "add",
            parent = "friend",
            usage = "friend.add.usage",
            description = "friend.add.description",
            minArgs = 1,
            maxArgs = 1,
            noConsole = true,
            permissions = "im.cmd.friend.add"
    )
    public void add(Player p, String friend) {
        UUID uuid = UUIDFetcher.getUUID(p.getName());
        UUID frienduuid;
        try {
            frienduuid = UUIDFetcher.getUUID(friend);
        } catch (Exception e) {
            dsp.send(p, "friend.add.notExisting");
            return;
        }
        if (User.isUserExists(frienduuid)) {
            if (!de.Iclipse.IMAPI.Database.Friend.isRequestedBy(frienduuid, uuid)) {
                if (!de.Iclipse.IMAPI.Database.Friend.areFriends(uuid, frienduuid)) {
                    if (!de.Iclipse.IMAPI.Database.Friend.isRequestedBy(uuid, frienduuid)) {
                        de.Iclipse.IMAPI.Database.Friend.createRequest(uuid, frienduuid);
                        if (User.isOnline(frienduuid)) {
                            sendBungeeMessage(p, UUIDFetcher.getName(frienduuid), dsp.get("friend.add.request", dsp.getLanguages().get(User.getLanguage(frienduuid)), true, p.getDisplayName()));
                            TextComponent base = new TextComponent(dsp.get("friend.add.request.click", dsp.getLanguages().get(User.getLanguage(frienduuid)), true));
                            TextComponent accept = new TextComponent(dsp.get("friend.add.request.click.accept", dsp.getLanguages().get(User.getLanguage(frienduuid))));
                            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend add " + p.getName()));
                            base.addExtra(accept);
                            TextComponent deny = new TextComponent(dsp.get("friend.add.request.click.deny", dsp.getLanguages().get(User.getLanguage(frienduuid))));
                            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend remove " + p.getName()));
                            base.addExtra(deny);
                            sendBungeeMessage(p, UUIDFetcher.getName(frienduuid), base.getText());
                        }
                    } else {
                        de.Iclipse.IMAPI.Database.Friend.accept(uuid, frienduuid);
                        if (User.isOnline(frienduuid)) {
                            dsp.send(p, "friend.add.accepted", BungeePermsAPI.userPrefix(frienduuid.toString(), IMAPI.getServerName(), null) + UUIDFetcher.getName(frienduuid));
                            dsp.get("friend.add.accepted", dsp.getLanguages().get(User.getLanguage(frienduuid)), true, p.getDisplayName());
                        } else {
                            dsp.send(p, "friend.add.accepted", "§e" + UUIDFetcher.getName(frienduuid));
                        }
                    }
                } else {
                    p.sendMessage(dsp.get("friend.add.alreadyFriends", p));
                }
            } else {
                p.sendMessage(dsp.get("friend.add.alreadyRequested", p));
            }
        } else {
            p.sendMessage(dsp.get("friend.add.notExisting", p));
        }
    }

    @IMCommand(
            name = "remove",
            parent = "friend",
            usage = "friend.remove.usage",
            description = "friend.remove.description",
            minArgs = 1,
            maxArgs = 1,
            noConsole = true,
            permissions = "im.cmd.friend.remove"
    )
    public void remove(Player p, String friend) {
        UUID uuid = UUIDFetcher.getUUID(p.getName());
        UUID frienduuid;
        try {
            frienduuid = UUIDFetcher.getUUID(friend);
        } catch (Exception e) {
            dsp.send(p, "friend.remove.notExisting");
            return;
        }

        if (de.Iclipse.IMAPI.Database.Friend.areFriends(uuid, frienduuid)) {
            de.Iclipse.IMAPI.Database.Friend.deleteFriend(uuid, frienduuid);
            dsp.send(p, "friend.remove.successfull");
        } else {
            dsp.send(p, "friend.remove.noFriend");
        }
    }

    @IMCommand(
            name = "jump",
            usage = "friend.jump.usage",
            description = "friend.jump.description",
            minArgs = 1,
            maxArgs = 1,
            noConsole = true,
            permissions = "im.cmd.friend.jump",
            parent = "friend"
    )
    public void jump(Player p, String name) {
        UUID friend;
        try {
            friend = UUIDFetcher.getUUID(name);
        } catch (Exception e) {
            dsp.send(p, "friend.jump.noFriend");
            return;
        }
        if (friend != null) {
            if (de.Iclipse.IMAPI.Database.Friend.areFriends(UUIDFetcher.getUUID(p.getName()), friend)) {
                if (!User.getServer(friend).equals(IMAPI.getServerName())) {
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(b);


                    try {
                        out.writeUTF("Connect");
                        out.writeUTF(User.getServer(friend));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }


                    p.sendPluginMessage(Data.instance, "BungeeCord", b.toByteArray());
                    dsp.send(p, "friend.jump.successfull");
                } else {
                    dsp.send(p, "friend.jump.already");
                }
            } else {
                dsp.send(p, "friend.jump.noFriend");
            }
        } else {
            try {
                UUID uuid = UUIDFetcher.getUUID(name);
                if (de.Iclipse.IMAPI.Database.Friend.areFriends(UUIDFetcher.getUUID(p.getName()), uuid)) {
                    dsp.send(p, "friend.jump.notOnline");
                } else {
                    dsp.send(p, "friend.jump.noFriend");
                }
            } catch (Exception e) {
                dsp.send(p, "friend.jump.notExisting");
            }
        }
    }


    private void addToOverview(CommandSender sender, String command) {
        builder.append("\n" + Data.symbol + "§e" + dsp.get("friend." + command + ".usage", sender) + "§8: §7 " + dsp.get("friend." + command + ".description", sender) + ChatColor.RESET);
    }

    private void sendBungeeMessage(Player p, String receiver, String message) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Message");
            out.writeUTF(receiver);
            out.writeUTF(message);
            p.sendPluginMessage(Data.instance, "BungeeCord", b.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
