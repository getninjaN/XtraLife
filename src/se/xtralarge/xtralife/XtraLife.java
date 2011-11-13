package se.xtralarge.xtralife;

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class XtraLife extends JavaPlugin {

    private final XtraEntityListener entityListener = new XtraEntityListener(this);
    private final XtraPlayerListener playerListener = new XtraPlayerListener(this);
    public static final Logger log = Logger.getLogger("Minecraft");
    public static final String chatPrefix = ChatColor.GOLD + "XtraLife: " + ChatColor.WHITE;
    public static int restoreFee = 0;

    @Override
    public void onDisable() {
        // fixa deathsnapshot serializeble osv spara/ladda 
    }

    @Override
    public void onEnable() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener, Event.Priority.Normal, this);

        log.info(this.getDescription().getFullName() + " is enabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            sender.sendMessage(("XtraLife is only for real players."));
            return true;
        }
        
        if(!player.hasPermission("xtralife.use")) {
            return true;
        }


        if (command.getName().equalsIgnoreCase("xtralife") || command.getName().equalsIgnoreCase("xtradeath")) {
            if (!BuybackManager.hasInventory(player.getName())) {
                player.sendMessage(XtraLife.chatPrefix + "You have no saved inventory.. :-(");
                return true;
            }

            if (command.getName().equalsIgnoreCase("xtralife")) {
                handleLifeCommand(player);
            } else {
                handleDeathCommand(player);
            }

            return true;
        }


        return false;
    }

    private void handleLifeCommand(Player player) {
        // kontroll om man har permission/restore-snapshot/credits. dra credits. restore. :)
        // kanske neka ifall man har nåt i inventory osv..
        BuybackManager.restoreInventory(player);
        player.sendMessage(XtraLife.chatPrefix + "Your inventory/location was " + ChatColor.GREEN + "restored.");
        player.sendMessage(XtraLife.chatPrefix + ChatColor.RED + XtraLife.restoreFee + " credits was withdrawn.");
    }

    private void handleDeathCommand(Player player) {
        BuybackManager.dimissInventory(player);
        player.sendMessage(XtraLife.chatPrefix + "Your items have been dropped at " + ChatColor.RED + "death location");
    }
}
