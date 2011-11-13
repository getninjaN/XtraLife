package se.xtralarge.xtralife;

import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BuybackManager {

    private static ConcurrentHashMap<String, DeathSnapshot> deathSnapshots = new ConcurrentHashMap<String, DeathSnapshot>();

    public static void saveInventory(Player player) {
        DeathSnapshot snapshot = new DeathSnapshot(player.getInventory().getContents(), player.getInventory().getArmorContents(), player.getLocation());
        deathSnapshots.put(player.getName(), snapshot);
    }

    public static void restoreInventory(Player player) {
        if (deathSnapshots.containsKey(player.getName())) {
            DeathSnapshot snapshot = deathSnapshots.get(player.getName());
            player.teleport(snapshot.getPlaceOfDeath());
            player.getInventory().setArmorContents(snapshot.getArmorContents());
            player.getInventory().setContents(snapshot.getContents());
            deathSnapshots.remove(player.getName());
        }
    }

    public static boolean hasInventory(String playerName) {
        if (deathSnapshots.containsKey(playerName)) {
            return true;
        } else {
            return false;
        }
    }

    static void dimissInventory(Player player) {
        if (hasInventory(player.getName())) {
            DeathSnapshot snapshot = deathSnapshots.get(player.getName());
            Location deathLocation = snapshot.getPlaceOfDeath();
            
            XtraLife.log.info("XtraLife: Player " + player.getName() + " dismissed snapshot");
            
            for (ItemStack stack : snapshot.getArmorContents()) {
                if (stack != null && stack.getAmount() > 0) {
                    XtraLife.log.info("XtraLife: dropping: " + stack.toString());
                    deathLocation.getWorld().dropItemNaturally(deathLocation, stack);
                }
            }

            for (ItemStack stack : snapshot.getContents()) {
                if (stack != null && stack.getAmount() > 0) {
                    XtraLife.log.info("XtraLife: dropping: " + stack.toString());
                    deathLocation.getWorld().dropItemNaturally(deathLocation, stack);
                }
            }

            deathSnapshots.remove(player.getName());
        }
    }
}
