package jndev.couriernew.postman;

import jndev.couriernew.CourierNew;
import jndev.couriernew.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class Postman {
    
    Entity postman;
    Player player;
    PostmanChecker pc = new PostmanChecker();
    
    /**
     * Get NMS class from jar
     * @param name class name
     * @return NMS class
     */
    private static Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * make an entity follow a player with a set speed
     * @param player player entity will follow
     * @param entity entity to follow player
     * @param d speed
     */
    public void followPlayer(Player player, LivingEntity entity, double d) {
        final LivingEntity e = entity;
        final Player p = player;
        final float f = (float) d;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CourierNew.getInstance(), new Runnable() {
            @Override
            public void run() {
                //((EntityInsentient) ((CraftEntity) e).getHandle()).getNavigation().a(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), f);
            }
        }, 0 * 20, 2 * 20);
    }
    
    public Postman(Player player) {
        this.player = player;
    }
    
    /**
     * get mob type from config
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
        for (Entity all : recipient.getNearbyEntities(radius, radius, radius)) if (pc.isPostman(all)) return;
        
        int dist = CourierNew.getInstance().getConfig().getInt("postman-spawn-distance");
        
        Location loc = recipient.getLocation().add(recipient.getLocation().getDirection().setY(0).multiply(dist));
        postman = recipient.getWorld().spawnEntity(loc, getTypeFromConfig());
        
        File postmenyml = CourierNew.getPostmenYml();
        FileConfiguration postmen = CourierNew.getPostmen();
        
        postmen.set(postman.getUniqueId().toString(), recipient.getUniqueId());
        
        try {
            postmen.save(postmenyml);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
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
                    //followPlayer(recipient, (LivingEntity) postman, 1); //todo add ability to follow player
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
                    
                    try {
                        postmen.save(postmenyml);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
    
                    if (recipient.isOnline()) recipient.sendMessage(Message.SUCCESS_IGNORED);
                    postman.getWorld().playSound(postman.getLocation(), Sound.UI_TOAST_OUT, 1, 1);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            FileConfiguration outgoing = CourierNew.getOutgoing();
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
