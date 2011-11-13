package se.xtralarge.xtralife;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class DeathSnapshot {

    private ItemStack[] contents;
    private ItemStack[] armorContents;
    private Location placeOfDeath;
    private int timeOfDeath;

    DeathSnapshot(ItemStack[] contents, ItemStack[] armorContents, Location placeOfDeath) {
        this.contents = contents;
        this.armorContents = armorContents;
        this.placeOfDeath = placeOfDeath;
        this.timeOfDeath = (int) (System.currentTimeMillis() / 1000);
        //XtraLife.log.info("New DeathSnapshot at timestamp: " + this.timeOfDeath);
    }

    public int getTimeOfDeath() {
        return timeOfDeath;
    }

    public Location getPlaceOfDeath() {
        return placeOfDeath;
    }

    public ItemStack[] getArmorContents() {
        return armorContents;
    }

    public ItemStack[] getContents() {
        return contents;
    }
}
