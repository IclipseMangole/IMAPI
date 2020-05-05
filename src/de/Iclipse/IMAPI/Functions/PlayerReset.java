package de.Iclipse.IMAPI.Functions;

import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PlayerReset {
    public static void resetPlayer(Player p) {
        if (!Vanish.isVanish(UUIDFetcher.getUUID(p.getName()))) {
            if (p.getGameMode() != GameMode.CREATIVE) {
                p.setFlying(false);
                p.setAllowFlight(false);
                p.setGravity(false);
            }
            p.setGlowing(false);
            p.setAbsorptionAmount(0.0);
            p.setHealth(20.0);
            p.setFoodLevel(20);
            p.setCanPickupItems(true);
            p.getActivePotionEffects().forEach(effect -> p.removePotionEffect(effect.getType()));
            p.getInventory().clear();
            p.setExp(0);
            p.setLevel(0);
            Bukkit.getOnlinePlayers().forEach(o -> o.showPlayer(Data.instance, p));
            p.setSneaking(false);
            p.setGliding(false);
            p.setSwimming(false);
            p.setCollidable(true);
            p.resetPlayerWeather();
            p.setInvulnerable(false);
            p.setWalkSpeed(2.0f);
        }
    }
}