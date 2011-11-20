package se.xtralarge.xtralife;

import com.nijiko.coelho.iConomy.iConomy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    public static int restoreFee = 15;
    private static List<String> playerGodModeList = Collections.synchronizedList(new ArrayList<String>());
    private static final long godModeLength = 400; // 400/20(tic) = 20 sec
    private iConomy iConomyPlugin;

    @Override
    public void onDisable() {
        log.info(this.getDescription().getFullName() + " is disabled!");
    }

    @Override
    public void onEnable() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener, Event.Priority.Normal, this);
        iConomyPlugin = (iConomy) this.getServer().getPluginManager().getPlugin("iConomy");
        if(iConomyPlugin == null) {
            XtraLife.restoreFee = 0;
        }
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

        if (!player.hasPermission("xtralife.use")) {
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
        if(XtraLife.restoreFee > 0) {
            if(iConomy.getBank().getAccount(player.getName()).hasEnough(XtraLife.restoreFee)) {
                iConomy.getBank().getAccount(player.getName()).subtract(XtraLife.restoreFee);
                iConomy.getBank().getAccount("Corningstone").add(restoreFee);
            } else {
                player.sendMessage(XtraLife.chatPrefix + "You cannot afford to buy back. Need " + ChatColor.RED + XtraLife.restoreFee + " c");
                return;
            }
        }
        
        this.getServer().getScheduler().scheduleAsyncDelayedTask(this, new RemoveGodModeTask(player.getName(), this), godModeLength);
        XtraLife.addGodMode(player.getName());
        BuybackManager.restoreInventory(player);
        player.sendMessage(XtraLife.chatPrefix + "Your inventory/location was " + ChatColor.GREEN + "restored.");
        player.sendMessage(XtraLife.chatPrefix + ChatColor.RED + XtraLife.restoreFee + " credits was withdrawn.");
    }

    private void handleDeathCommand(Player player) {
        BuybackManager.dismissInventory(player);
        player.sendMessage(XtraLife.chatPrefix + "Your items have been dropped at " + ChatColor.RED + "death location");
    }

    public static void removeGodMode(String playername) {
        if (playerGodModeList.contains(playername)) {
            playerGodModeList.remove(playername);
        }
    }

    public static void addGodMode(String playername) {
        if (!playerGodModeList.contains(playername)) {
            playerGodModeList.add(playername);
        }
    }

    public static boolean hasGodMode(String playername) {
        if (playerGodModeList.contains(playername)) {
            return true;
        } else {
            return false;
        }
    }

    private class RemoveGodModeTask implements Runnable {

        private String playername;
        private XtraLife instance;

        RemoveGodModeTask(String playername, XtraLife instance) {
            this.playername = playername;
            this.instance = instance;
        }

        @Override
        public void run() {
            XtraLife.removeGodMode(this.playername);
            Player player = instance.getServer().getPlayer(this.playername);
            if (player != null) {
                player.sendMessage(XtraLife.chatPrefix + ChatColor.RED + "No more godmode for you!");
            }
        }
    }
}
