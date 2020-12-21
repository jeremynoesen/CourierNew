package jeremynoesen.couriernew.courier;

import jeremynoesen.couriernew.CourierNew;
import jeremynoesen.couriernew.Message;
import jeremynoesen.couriernew.config.ConfigType;
import jeremynoesen.couriernew.config.Configs;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Courier {
    
    Entity postman;
    Player player;
    
    public Courier(Player player) {
        this.player = player;
    }
    
    /**
     * get mob type from config
     *
     * @return entitytype, default villager
     */
    EntityType getTypeFromConfig() {
        if (CourierNew.getInstance().getConfig().get("postman-entity-type") != null) {
            try {
                return EntityType.valueOf(CourierNew.getInstance().getConfig().getString("postman-entity-type"));
            } catch (Exception e) {
                return EntityType.VILLAGER;
            }
        } else
            return EntityType.VILLAGER;
    }
    
    /**
     * spawn the postman entity
     */
    public void spawn() {
        Player recipient = this.player;
        double radius = CourierNew.getInstance().getConfig().getDouble("check-before-spawning-postman-radius");
        for (Entity all : recipient.getNearbyEntities(radius, radius, radius))
            if (CourierChecker.isCourier(all)) return;
        
        int dist = CourierNew.getInstance().getConfig().getInt("postman-spawn-distance");
        
        Location loc = recipient.getLocation().add(recipient.getLocation().getDirection().setY(0).multiply(dist));
        postman = recipient.getWorld().spawnEntity(loc, getTypeFromConfig());
        
        FileConfiguration postmen = Configs.getConfig(ConfigType.COURIERS).getConfig();
        
        postmen.set(postman.getUniqueId().toString(), recipient.getUniqueId());
        
        Configs.getConfig(ConfigType.COURIERS).saveConfig();
        
        postman.setCustomName(Message.POSTMAN_NAME.replace("$PLAYER$", recipient.getName()));
        postman.setCustomNameVisible(false);
        postman.setInvulnerable(true);
        recipient.sendMessage(Message.SUCCESS_POSTMAN_ARRIVED);
        postman.getWorld().playSound(postman.getLocation(), Sound.UI_TOAST_IN, 1, 1);
        
        new BukkitRunnable() {
            @Override
            public void run() {
                postman.setFallDistance(0);
                if (postman.isOnGround() && postman.getWorld() == recipient.getWorld()) {
                    postman.teleport(postman.getLocation().setDirection(recipient.getLocation().subtract(postman.getLocation()).toVector()));
                    ((LivingEntity) postman).setAI(false);
                }
                if (postman.isDead()) this.cancel();
            }
        }.runTaskTimer(CourierNew.getInstance(), 0, 1);
        
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!postman.isDead()) {
                    postman.remove();
                    
                    postmen.set(postman.getUniqueId().toString(), null);
                    
                    Configs.getConfig(ConfigType.COURIERS).saveConfig();
                    
                    if (recipient.isOnline()) recipient.sendMessage(Message.SUCCESS_IGNORED);
                    postman.getWorld().playSound(postman.getLocation(), Sound.UI_TOAST_OUT, 1, 1);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            FileConfiguration outgoing = Configs.getConfig(ConfigType.OUTGOING).getConfig();
                            if (recipient.isOnline() && outgoing.getList(recipient.getUniqueId().toString()) != null
                                    && outgoing.getList(recipient.getUniqueId().toString()).size() > 0)
                                spawn();
                        }
                    }.runTaskLater(CourierNew.getInstance(), CourierNew.getInstance().getConfig().getLong("resend-delay"));
                }
            }
        }.runTaskLater(CourierNew.getInstance(), CourierNew.getInstance().getConfig().getLong("remove-postman-ignored-delay"));
    }
    
}
