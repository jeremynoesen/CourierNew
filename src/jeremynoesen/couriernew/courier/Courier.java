package jeremynoesen.couriernew.courier;

import jeremynoesen.couriernew.CourierNew;
import jeremynoesen.couriernew.Message;
import jeremynoesen.couriernew.config.Options;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * courier used to send mail
 *
 * @author Jeremy Noesen
 */
public class Courier {
    
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
     * spawn the courier entity
     */
    public void spawn() {
        double dist = Options.SPAWN_DISTANCE * 2;
        for (Entity entity : recipient.getNearbyEntities(dist, dist, dist))
            if (Couriers.isCourier(entity)) return;
    
        Location loc = recipient.getLocation().add(recipient.getLocation().getDirection().setY(0).multiply(dist));
        courier = recipient.getWorld().spawnEntity(loc, Options.COURIER_ENTITY_TYPE);
        Couriers.add(this);
    
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
                    }.runTaskLater(CourierNew.getInstance(), Options.RESEND_DELAY);
                }
            }
        }.runTaskLater(CourierNew.getInstance(), Options.REMOVE_DELAY);
    }
    
    /**
     * remove the courier entity
     */
    public void remove() {
        courier.remove();
        Couriers.remove(this);
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
    
}
