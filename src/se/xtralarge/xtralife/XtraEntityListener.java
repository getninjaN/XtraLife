package se.xtralarge.xtralife;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

public class XtraEntityListener extends EntityListener {

    public static XtraLife plugin;

    public XtraEntityListener(XtraLife instance) {
        plugin = instance;
    }

    @Override
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (player.hasPermission("xtralife.use")) {
                event.getDrops().clear();
                BuybackManager.saveInventory(player);
            }
        }
    }
}
