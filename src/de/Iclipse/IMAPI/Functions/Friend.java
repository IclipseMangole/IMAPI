package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import de.Iclipse.IMAPI.Util.PageUtils;
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

import static de.Iclipse.IMAPI.Util.UUIDFetcher.*;


public class Friend {
    private StringBuilder builder;
    private final IMAPI imapi;
    
    public Friend(IMAPI imapi){
        this.imapi = imapi;
    }

    @IMCommand(
            name = "friend",
            usage = "friend.usage",
            description = "friend.description",
            noConsole = true,
            permissions = "im.cmd.friend"
    )
    public void execute(Player p) {
        builder = new StringBuilder();
        builder.append(imapi.getData().getPrefix()).append("§7§lHilfsübersicht:§r\n");
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
                imapi.getData().getDispatcher().send(p, "friend.list.usage");
                return;
            }
            if (page > 0) {
                page--;
            } else {
                imapi.getData().getDispatcher().send(p, "friend.list.usage");
            }
        }
        ArrayList<UUID> friends = imapi.getData().getFriendTable().getFriendsSorted(getUUID(p.getName()), imapi.getData().getUserSettingsTable().getInt(getUUID(p.getName()), "friend_sort"));
        ArrayList<UUID> shown = PageUtils.getPage(friends, 8, page);
        if (shown.size() > 0) {
            imapi.getData().getDispatcher().send(p, "friend.list.header", page + 1 + "", friends.size() / 8 + "");
            shown.forEach(user -> {
                if (imapi.getData().getUserTable().isOnline(user)) {
                    imapi.getData().getDispatcher().send(p, "friend.list.format.online", imapi.getData().getTablist().getPrefix(getUUID(p.getName())) + getName(user), imapi.getData().getUserTable().getServer(user));
                } else {
                    int days = (int) (System.currentTimeMillis() - imapi.getData().getUserTable().getLastTime(user)) / (1000 * 60 * 60 * 24);
                    imapi.getData().getDispatcher().send(p, "friend.list.format.offline", getName(user) + ": §cOffline", days + "");
                }
            });
        } else {
            imapi.getData().getDispatcher().send(p, "friend.list.empty");
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
        UUID uuid = getUUID(p.getName());
        UUID frienduuid;
        try {
            frienduuid = getUUID(friend);
        } catch (Exception e) {
            imapi.getData().getDispatcher().send(p, "friend.add.notExisting");
            return;
        }
        if (imapi.getData().getUserTable().isUserExists(frienduuid)) {
            if (!imapi.getData().getFriendTable().isRequestedBy(frienduuid, uuid)) {
                if (!imapi.getData().getFriendTable().areFriends(uuid, frienduuid)) {
                    if (!imapi.getData().getFriendTable().isRequestedBy(uuid, frienduuid)) {
                        imapi.getData().getFriendTable().createRequest(uuid, frienduuid);
                        if (imapi.getData().getUserTable().isOnline(frienduuid)) {
                            sendBungeeMessage(p, getName(frienduuid), imapi.getData().getDispatcher().get("friend.add.request", imapi.getData().getDispatcher().getLanguages().get(imapi.getData().getUserTable().getLanguage(frienduuid)), true, p.getDisplayName()));
                            TextComponent base = new TextComponent(imapi.getData().getDispatcher().get("friend.add.request.click", imapi.getData().getDispatcher().getLanguages().get(imapi.getData().getUserTable().getLanguage(frienduuid)), true));
                            TextComponent accept = new TextComponent(imapi.getData().getDispatcher().get("friend.add.request.click.accept", imapi.getData().getDispatcher().getLanguages().get(imapi.getData().getUserTable().getLanguage(frienduuid))));
                            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend add " + p.getName()));
                            base.addExtra(accept);
                            TextComponent deny = new TextComponent(imapi.getData().getDispatcher().get("friend.add.request.click.deny", imapi.getData().getDispatcher().getLanguages().get(imapi.getData().getUserTable().getLanguage(frienduuid))));
                            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend remove " + p.getName()));
                            base.addExtra(deny);
                            sendBungeeMessage(p, getName(frienduuid), base.getText());
                        }
                    } else {
                        imapi.getData().getFriendTable().accept(uuid, frienduuid);
                        if (imapi.getData().getUserTable().isOnline(frienduuid)) {
                            imapi.getData().getDispatcher().send(p, "friend.add.accepted", imapi.getData().getTablist().getPrefix(getUUID(p.getName())) + getName(frienduuid));
                            imapi.getData().getDispatcher().get("friend.add.accepted", imapi.getData().getDispatcher().getLanguages().get(imapi.getData().getUserTable().getLanguage(frienduuid)), true, p.getDisplayName());
                        } else {
                            imapi.getData().getDispatcher().send(p, "friend.add.accepted", "§e" + getName(frienduuid));
                        }
                    }
                } else {
                    p.sendMessage(imapi.getData().getDispatcher().get("friend.add.alreadyFriends", p));
                }
            } else {
                p.sendMessage(imapi.getData().getDispatcher().get("friend.add.alreadyRequested", p));
            }
        } else {
            p.sendMessage(imapi.getData().getDispatcher().get("friend.add.notExisting", p));
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
        UUID uuid = getUUID(p.getName());
        UUID frienduuid;
        try {
            frienduuid = getUUID(friend);
        } catch (Exception e) {
            imapi.getData().getDispatcher().send(p, "friend.remove.notExisting");
            return;
        }

        if (imapi.getData().getFriendTable().areFriends(uuid, frienduuid)) {
            imapi.getData().getFriendTable().deleteFriend(uuid, frienduuid);
            imapi.getData().getDispatcher().send(p, "friend.remove.successfull");
        } else {
            imapi.getData().getDispatcher().send(p, "friend.remove.noFriend");
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
            friend = getUUID(name);
        } catch (Exception e) {
            imapi.getData().getDispatcher().send(p, "friend.jump.noFriend");
            return;
        }
        if (friend != null) {
            if (imapi.getData().getFriendTable().areFriends(getUUID(p.getName()), friend)) {
                if (!imapi.getData().getUserTable().getServer(friend).equals(imapi.getServerName())) {
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(b);


                    try {
                        out.writeUTF("Connect");
                        out.writeUTF(imapi.getData().getUserTable().getServer(friend));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }


                    p.sendPluginMessage(imapi, "BungeeCord", b.toByteArray());
                    imapi.getData().getDispatcher().send(p, "friend.jump.successfull");
                } else {
                    imapi.getData().getDispatcher().send(p, "friend.jump.already");
                }
            } else {
                imapi.getData().getDispatcher().send(p, "friend.jump.noFriend");
            }
        } else {
            try {
                UUID uuid = getUUID(name);
                if (imapi.getData().getFriendTable().areFriends(getUUID(p.getName()), uuid)) {
                    imapi.getData().getDispatcher().send(p, "friend.jump.notOnline");
                } else {
                    imapi.getData().getDispatcher().send(p, "friend.jump.noFriend");
                }
            } catch (Exception e) {
                imapi.getData().getDispatcher().send(p, "friend.jump.notExisting");
            }
        }
    }


    private void addToOverview(CommandSender sender, String command) {
        builder.append("\n").append(imapi.getData().getSymbol()).append("§e").append(imapi.getData().getDispatcher().get("friend." + command + ".usage", sender)).append("§8: §7 ").append(imapi.getData().getDispatcher().get("friend." + command + ".description", sender)).append(ChatColor.RESET);
    }

    private void sendBungeeMessage(Player p, String receiver, String message) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Message");
            out.writeUTF(receiver);
            out.writeUTF(message);
            p.sendPluginMessage(imapi, "BungeeCord", b.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
