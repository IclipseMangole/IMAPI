package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Data;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import static de.Iclipse.IMAPI.Data.dsp;

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
                                dsp.send(entry, "imrestart.message", "" + Data.restart, dsp.get("unit.minutes", entry));
                            });
                            dsp.send(Bukkit.getConsoleSender(), "imrestart.message", "" + Data.restart, dsp.get("unit.minutes", Bukkit.getConsoleSender()));
                        }
                    } else if (Data.restart >= 5 * 60) {
                        if (Data.restart % (5 * 60) == 0) {
                            Bukkit.getOnlinePlayers().forEach(entry -> {
                                dsp.send(entry, "imrestart.message", "" + Data.restart, dsp.get("unit.minutes", entry));
                            });
                            dsp.send(Bukkit.getConsoleSender(), "imrestart.message", "" + Data.restart, dsp.get("unit.minutes", Bukkit.getConsoleSender()));
                        }
                    } else if (Data.restart >= 1 * 60) {
                        if (Data.restart % (1 * 60) == 0) {
                            Bukkit.getOnlinePlayers().forEach(entry -> {
                                dsp.send(entry, "imrestart.message", "" + Data.restart, dsp.get("unit.minutes", entry));
                            });
                            dsp.send(Bukkit.getConsoleSender(), "imrestart.message", "" + Data.restart, dsp.get("unit.minutes", Bukkit.getConsoleSender()));
                        }
                    } else if (Data.restart > 10) {
                        if (Data.restart % (15 * 60) == 0) {
                            Bukkit.getOnlinePlayers().forEach(entry -> {
                                dsp.send(entry, "imrestart.message", "" + Data.restart, dsp.get("unit.seconds", entry));
                            });
                            dsp.send(Bukkit.getConsoleSender(), "imrestart.message", "" + Data.restart, dsp.get("unit.minutes", Bukkit.getConsoleSender()));
                        }
                    } else if (Data.restart > 0) {
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            dsp.send(entry, "imrestart.message", "" + Data.restart, dsp.get("unit.seconds", entry));
                        });
                        dsp.send(Bukkit.getConsoleSender(), "imrestart.message", "" + Data.restart, dsp.get("unit.minutes", Bukkit.getConsoleSender()));
                    } else {
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            dsp.get("imrestart.restart", entry);
                        });
                        dsp.send(Bukkit.getConsoleSender(), "imrestart.restart");
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
