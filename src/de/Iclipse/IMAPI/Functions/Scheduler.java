package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Data;
import net.minecraft.server.v1_15_R1.PacketPlayOutScoreboardObjective;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import static de.Iclipse.IMAPI.Data.dsp;
import static de.Iclipse.IMAPI.Util.ScoreboardSign.setField;

public class Scheduler {
    private static BukkitTask task;

    public static void startScheduler() {
        task = Bukkit.getScheduler().runTaskTimer(Data.instance, new Runnable() {
            @Override
            public void run() {
                if (Data.restart >= 0) {
                    if (Data.restart >= 30 * 60 && Data.restart <= 90 * 60) {
                        if (Data.restart % (30 * 60) == 0) {
                            Bukkit.getOnlinePlayers().forEach(entry -> {
                                dsp.send(entry, "imrestart.message", "" + Data.restart / 60, dsp.get("unit.minutes", entry));
                            });
                            dsp.send(Bukkit.getConsoleSender(), "imrestart.message", "" + Data.restart, dsp.get("unit.minutes", Bukkit.getConsoleSender()));
                        }
                    } else if (Data.restart >= 5 * 60) {
                        if (Data.restart % (5 * 60) == 0) {
                            Bukkit.getOnlinePlayers().forEach(entry -> {
                                dsp.send(entry, "imrestart.message", "" + Data.restart / 60, dsp.get("unit.minutes", entry));
                            });
                            dsp.send(Bukkit.getConsoleSender(), "imrestart.message", "" + Data.restart, dsp.get("unit.minutes", Bukkit.getConsoleSender()));
                        }
                    } else if (Data.restart > 1 * 60) {
                        if (Data.restart % (1 * 60) == 0) {
                            Bukkit.getOnlinePlayers().forEach(entry -> {
                                dsp.send(entry, "imrestart.message", "" + Data.restart / 60, dsp.get("unit.minutes", entry));
                            });
                            dsp.send(Bukkit.getConsoleSender(), "imrestart.message", "" + Data.restart, dsp.get("unit.minutes", Bukkit.getConsoleSender()));
                        }
                    } else if (Data.restart > 10) {
                        if (Data.restart % 15 == 0) {
                            Bukkit.getOnlinePlayers().forEach(entry -> {
                                dsp.send(entry, "imrestart.message", "" + Data.restart, dsp.get("unit.seconds", entry));
                            });
                            dsp.send(Bukkit.getConsoleSender(), "imrestart.message", "" + Data.restart, dsp.get("unit.seconds", Bukkit.getConsoleSender()));
                        }
                    } else if (Data.restart > 0) {
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            dsp.send(entry, "imrestart.message", "" + Data.restart, dsp.get("unit.seconds", entry));
                        });
                        dsp.send(Bukkit.getConsoleSender(), "imrestart.message", "" + Data.restart, dsp.get("unit.seconds", Bukkit.getConsoleSender()));
                    } else {
                        if (Bukkit.getOnlinePlayers().size() > 0) {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
                                setField(packet, "a", p.getName());
                                setField(packet, "d", 1);
                                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
                                p.kickPlayer(dsp.get("imrestart.restart", p));
                            }
                        }
                        dsp.send(Bukkit.getConsoleSender(), "imrestart.restart");
                        Bukkit.getWorlds().forEach(w -> w.save());
                        Bukkit.shutdown();
                    }

                    Data.restart--;
                }
            }
        }, 20, 20);
    }

    public static void stopScheduler() {
        task.cancel();
    }
}
