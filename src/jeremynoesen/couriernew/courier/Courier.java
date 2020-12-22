package jeremynoesen.couriernew.courier;

import com.earth2me.essentials.Essentials;
import de.myzelyam.supervanish.SuperVanish;
import jeremynoesen.couriernew.CourierNew;
import jeremynoesen.couriernew.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.kitteh.vanish.VanishPlugin;

import java.util.HashMap;

/**
 * courier used to send mail
 *
 * @author Jeremy Noesen
 */
public class Courier {
    
    /**
     * set of all alive courier entities
     */
    private static HashMap<Entity, Courier> couriers = new HashMap<>();
    
    /**
     * courier entity
     */
    private Entity courier;
    
    /**
     * player receiving mail
     */
    private Player recipient;
    
    /**
     * whether the recipient has received their mail
     */
    private boolean delivered;
    
    /**
     * create a new courier entity to deliver mail for a player
     *
     * @param recipient player receiving mail
     */
    public Courier(Player recipient) {
        this.recipient = recipient;
        this.delivered = false;
    }
    
    /**
     * get a set of all alive courier entities
     *
     * @return set of all alive courier entities
     */
    public static HashMap<Entity, Courier> getCouriers() {
        return couriers;
    }
    
    /**
     * spawn the courier entity
     */
    public void spawn() {
        double dist = CourierOptions.SPAWN_DISTANCE * 2;
        for (Entity entity : recipient.getNearbyEntities(dist, dist, dist))
            if (couriers.containsKey(entity)) return;
    
        Location loc = recipient.getLocation().add(recipient.getLocation().getDirection().setY(0).multiply(dist));
        courier = recipient.getWorld().spawnEntity(loc, CourierOptions.COURIER_ENTITY_TYPE);
        couriers.put(courier, this);
    
        courier.setCustomName(Message.POSTMAN_NAME.replace("$PLAYER$", recipient.getName()));
        courier.setCustomNameVisible(false);
        courier.setInvulnerable(true);
        recipient.sendMessage(Message.SUCCESS_POSTMAN_ARRIVED);
        courier.getWorld().playSound(courier.getLocation(), Sound.UI_TOAST_IN, 1, 1);
    
        new BukkitRunnable() {
            @Override
            public void run() {
                courier.setFallDistance(0);
                if (courier.isOnGround() && courier.getWorld() == recipient.getWorld()) {
                    courier.teleport(courier.getLocation().setDirection(recipient.getLocation().subtract(courier.getLocation()).toVector()));
                    ((LivingEntity) courier).setAI(false);
                }
                if (courier.isDead()) this.cancel();
            }
        }.runTaskTimer(CourierNew.getInstance(), 0, 1);
        
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!courier.isDead()) {
                    remove();
                
                    if (recipient.isOnline()) recipient.sendMessage(Message.SUCCESS_IGNORED);
                    courier.getWorld().playSound(courier.getLocation(), Sound.UI_TOAST_OUT, 1, 1);
                    
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (recipient.isOnline()) //todo also check that player has mail to be delivered
                                spawn();
                        }
                    }.runTaskLater(CourierNew.getInstance(), CourierOptions.RESEND_DELAY);
                }
            }
        }.runTaskLater(CourierNew.getInstance(), CourierOptions.REMOVE_DELAY);
    }
    
    /**
     * remove the courier entity
     */
    public void remove() {
        couriers.remove(courier);
        courier.remove();
    }
    
    /**
     * set the status of the courier to delivered
     */
    public void setDelivered() {
        delivered = true;
        courier.setCustomName(Message.POSTMAN_NAME_RECEIVED);
    }
    
    /**
     * check if mail was delivered
     *
     * @return true if mail was delivered
     */
    public boolean isDelivered() {
        return delivered;
    }
    
    /**
     * get the recipient for the courier
     *
     * @return recipient for courier
     */
    public Player getRecipient() {
        return recipient;
    }
    
    /**
     * get the courier entity
     *
     * @return courier entity
     */
    public Entity getCourier() {
        return courier;
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
        
        if (CourierOptions.BLOCKED_WORLDS.contains(recipient.getWorld()) ||
                CourierOptions.BLOCKED_GAMEMODES.contains(recipient.getGameMode())) {
            recipient.sendMessage(Message.ERROR_WORLD);
            return false;
        }
        return true;
    }
}
