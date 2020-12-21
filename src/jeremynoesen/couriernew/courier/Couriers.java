package jeremynoesen.couriernew.courier;

import com.earth2me.essentials.Essentials;
import de.myzelyam.supervanish.SuperVanish;
import jeremynoesen.couriernew.Message;
import jeremynoesen.couriernew.config.ConfigOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.kitteh.vanish.VanishPlugin;

import java.util.HashSet;
import java.util.Set;

/**
 * manager class for all courier entities
 *
 * @author Jeremy Noesen
 */
public class Couriers {
    
    /**
     * set of all alive courier entities
     */
    private static Set<Entity> couriers = new HashSet<>();
    
    /**
     * check if an entity is a courier
     *
     * @param entity entity to check
     * @return true if the entity is a courier
     */
    public static boolean isCourier(Entity entity) {
        return couriers.contains(entity);
    }
    
    /**
     * add a courier to the alive couriers list
     *
     * @param courier courier to add
     */
    public static void add(Courier courier) {
        couriers.add(courier.getCourier());
    }
    
    /**
     * remove a courier from the alive couriers list
     *
     * @param courier courier to remove
     */
    public static void remove(Courier courier) {
        couriers.remove(courier.getCourier());
    }
    
    public static void removeAll() {
        for(Entity courier : couriers) {
            courier.remove();
            couriers.remove(courier);
        }
    }
    
    /**
     * check if a courier is allowed to spawn for the specified player
     *
     * @param recipient player to spawn courier for
     * @return true if the courier is allowed to spawn
     */
    public static boolean canSpawn(Player recipient) {
        if (!recipient.isOnline())
            return false;
        
        if (Bukkit.getPluginManager().isPluginEnabled("VanishNoPacket")) {
            if (((VanishPlugin) Bukkit.getPluginManager().getPlugin("VanishNoPacket")).getManager().isVanished
                    (recipient)) {
                recipient.sendMessage(Message.ERROR_VANISHED);
                return false;
            }
        }
        
        if (Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
            if (((Essentials) Bukkit.getPluginManager().getPlugin("Essentials"))
                    .getVanishedPlayers().contains(recipient.getName())) {
                recipient.sendMessage(Message.ERROR_VANISHED);
                return false;
            }
        }
        
        if (Bukkit.getPluginManager().isPluginEnabled("SuperVanish")) {
            if (((SuperVanish) Bukkit.getPluginManager().getPlugin("SuperVanish"))
                    .getVanishStateMgr().isVanished(recipient.getUniqueId())) {
                recipient.sendMessage(Message.ERROR_VANISHED);
                return false;
            }
        }
        
        if (ConfigOptions.BLOCKED_WORLDS.contains(recipient.getWorld()) ||
                ConfigOptions.BLOCKED_GAMEMODES.contains(recipient.getGameMode())) {
            recipient.sendMessage(Message.ERROR_WORLD);
            return false;
        }
        return true;
    }
    
}
