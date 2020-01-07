package me.Jeremaster101.CourierNew.Postman;

import com.sun.corba.se.impl.naming.namingutil.CorbalocURL;
import me.Jeremaster101.CourierNew.CourierNew;
import me.Jeremaster101.CourierNew.Message;
import org.apache.logging.log4j.core.Core;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class Postman {
    
    Entity postman;
    Player player;
    PostmanChecker pc = new PostmanChecker();
    
    public Postman(Player player) {
        this.player = player;
    }
    
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
    
    public void spawn() {
        Player recipient = this.player;
        double radius = CourierNew.plugin.getConfig().getDouble("check-before-spawning-postman-radius");
        for (Entity all : recipient.getNearbyEntities(radius, radius, radius)) if (pc.isPostman(all)) return;
        
        int dist = CourierNew.getInstance().getConfig().getInt("postman-spawn-distance");
        
        Location loc = recipient.getLocation().add(recipient.getLocation().getDirection().setY(0).multiply(dist));
        postman = recipient.getWorld().spawnEntity(loc, getTypeFromConfig());
        
        File postmenyml = new File(CourierNew.plugin.getDataFolder(), "postmen.yml");
        FileConfiguration postmen = YamlConfiguration.loadConfiguration(postmenyml);
        
        postmen.set(postman.getUniqueId().toString(), recipient.getName());
        
        try {
            postmen.save(postmenyml);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        postman.setCustomName(Message.POSTMAN_NAME.replace("$PLAYER$", recipient.getName()));
        postman.setCustomNameVisible(false);
        postman.setInvulnerable(true);
        recipient.sendMessage(Message.SUCCESS_POSTMAN_ARRIVED);
        postman.getWorld().playSound(postman.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        
        new BukkitRunnable() {
            @Override
            public void run() {
                postman.setFallDistance(0);
                if (postman.isOnGround() && postman.getWorld() == recipient.getWorld())
                    postman.teleport(postman.getLocation().setDirection(recipient.getLocation().subtract(postman.getLocation()).toVector()));
                if (postman.isDead()) this.cancel();
            }
        }.runTaskTimer(CourierNew.plugin, 0, 1);
        
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
                    
                    recipient.sendMessage(Message.SUCCESS_IGNORED);
                    postman.getWorld().playSound(postman.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 1);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            File outgoingyml = new File(CourierNew.plugin.getDataFolder(), "outgoing.yml");
                            FileConfiguration outgoing = YamlConfiguration.loadConfiguration(outgoingyml);
                            if (recipient.isOnline() && outgoing.getList(recipient.getUniqueId().toString()) != null
                                    && outgoing.getList(recipient.getUniqueId().toString()).size() > 0)
                                spawn();
                        }
                    }.runTaskLater(CourierNew.plugin, CourierNew.plugin.getConfig().getLong("resend-delay"));
                }
            }
        }.runTaskLater(CourierNew.plugin, CourierNew.plugin.getConfig().getLong("remove-postman-ignored-delay"));
    }
    
}
