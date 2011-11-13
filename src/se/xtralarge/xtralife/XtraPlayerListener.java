package se.xtralarge.xtralife;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class XtraPlayerListener extends PlayerListener {

    public static XtraLife plugin;

    public XtraPlayerListener(XtraLife instance) {
        plugin = instance;
    }

    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!event.getPlayer().hasPermission("xtralife.use")) {
            return;
        }
        event.getPlayer().sendMessage(XtraLife.chatPrefix + "A snapshot with your inventory has been " + ChatColor.GREEN + "saved.");
        event.getPlayer().sendMessage(XtraLife.chatPrefix + "Type " + ChatColor.YELLOW + "/xtralife" + ChatColor.WHITE + " and you will be teleported back");
        event.getPlayer().sendMessage(XtraLife.chatPrefix + "and have your inventory restored.");
        event.getPlayer().sendMessage(XtraLife.chatPrefix + "The fee for this is " + ChatColor.RED + XtraLife.restoreFee + ChatColor.WHITE + " credits");
        event.getPlayer().sendMessage(XtraLife.chatPrefix + "If you dont want this, type " + ChatColor.YELLOW + "/xtradeath");
    }
}