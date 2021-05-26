package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.IMAPI;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardObjective;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import static de.Iclipse.IMAPI.Util.ScoreboardSign.setField;

public class Scheduler {
    private final IMAPI imapi;
    private BukkitTask task;
    private int restart;
    private BukkitTask mysqlTask;

    public Scheduler(IMAPI imapi) {
        this.imapi = imapi;
        restart = -1;
        task = Bukkit.getScheduler().runTaskTimer(imapi, new Runnable() {
            @Override
            public void run() {
                if (restart >= 0) {
                    if (restart >= 30 * 60 && restart <= 90 * 60) {
                        if (restart % (30 * 60) == 0) {
                            Bukkit.getOnlinePlayers().forEach(entry -> {
                                imapi.getData().getDispatcher().send(entry, "imrestart.message", "" + restart / 60, imapi.getData().getDispatcher().get("unit.minutes", entry));
                            });
                            imapi.getData().getDispatcher().send(Bukkit.getConsoleSender(), "imrestart.message", "" + restart, imapi.getData().getDispatcher().get("unit.minutes", Bukkit.getConsoleSender()));
                        }
                    } else if (restart >= 5 * 60) {
                        if (restart % (5 * 60) == 0) {
                            Bukkit.getOnlinePlayers().forEach(entry -> {
                                imapi.getData().getDispatcher().send(entry, "imrestart.message", "" + restart / 60, imapi.getData().getDispatcher().get("unit.minutes", entry));
                            });
                            imapi.getData().getDispatcher().send(Bukkit.getConsoleSender(), "imrestart.message", "" + restart, imapi.getData().getDispatcher().get("unit.minutes", Bukkit.getConsoleSender()));
                        }
                    } else if (restart > 1 * 60) {
                        if (restart % (1 * 60) == 0) {
                            Bukkit.getOnlinePlayers().forEach(entry -> {
                                imapi.getData().getDispatcher().send(entry, "imrestart.message", "" + restart / 60, imapi.getData().getDispatcher().get("unit.minutes", entry));
                            });
                            imapi.getData().getDispatcher().send(Bukkit.getConsoleSender(), "imrestart.message", "" + restart, imapi.getData().getDispatcher().get("unit.minutes", Bukkit.getConsoleSender()));
                        }
                    } else if (restart > 10) {
                        if (restart % 15 == 0) {
                            Bukkit.getOnlinePlayers().forEach(entry -> {
                                imapi.getData().getDispatcher().send(entry, "imrestart.message", "" + restart, imapi.getData().getDispatcher().get("unit.seconds", entry));
                            });
                            imapi.getData().getDispatcher().send(Bukkit.getConsoleSender(), "imrestart.message", "" + restart, imapi.getData().getDispatcher().get("unit.seconds", Bukkit.getConsoleSender()));
                        }
                    } else if (restart > 0) {
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            imapi.getData().getDispatcher().send(entry, "imrestart.message", "" + restart, imapi.getData().getDispatcher().get("unit.seconds", entry));
                        });
                        imapi.getData().getDispatcher().send(Bukkit.getConsoleSender(), "imrestart.message", "" + restart, imapi.getData().getDispatcher().get("unit.seconds", Bukkit.getConsoleSender()));
                    } else {
                        if (Bukkit.getOnlinePlayers().size() > 0) {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
                                setField(packet, "a", p.getName());
                                setField(packet, "d", 1);
                                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
                                p.kickPlayer(imapi.getData().getDispatcher().get("imrestart.restart", p));
                            }
                        }
                        imapi.getData().getDispatcher().send(Bukkit.getConsoleSender(), "imrestart.restart");
                        Bukkit.shutdown();
                    }

                    restart--;
                }
                if (Bukkit.getOnlinePlayers().size() == 0) {
                    imapi.getData().getMySQL().close();
                }

            }
        }, 20, 20);
    }

    public int getRestart() {
        return restart;
    }

    public void setRestart(int restart) {
        this.restart = restart;
    }

    public void stop() {
        task.cancel();
    }


}
