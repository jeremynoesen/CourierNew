package jeremynoesen.couriernew.config;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.util.HashSet;
import java.util.Set;

/**
 * config options from the main config. all times are in ticks
 *
 * @author Jeremy Noesen
 */
public class Options {
    
    /**
     * delay between sending a letter, joining the server, switching out of a blocked gamemode, or coming from a blocked
     * world, to the recipient receiving the letter
     */
    public static int RECEIVE_DELAY;
    
    /**
     * delay between respawning the courier when mail is not taken
     */
    public static int RESEND_DELAY;
    
    /**
     * delay before removing courier after spawning
     */
    public static int REMOVE_DELAY;
    
    /**
     * how far away to spawn the courier from the player, in blocks
     */
    public static int SPAWN_DISTANCE;
    
    /**
     * whether the courier should be protected to the recipient or not, allowing others to receive mail from a courier
     * that is not theirs
     */
    public static boolean PROTECTED_COURIER;
    
    /**
     * entity type to use as the courier
     */
    public static EntityType COURIER_ENTITY_TYPE;
    
    /**
     * gamemodes that disallow receiving mail
     */
    public static Set<GameMode> BLOCKED_GAMEMODES;
    
    /**
     * worlds that disallow receiving mail
     */
    public static Set<World> BLOCKED_WORLDS;
    
    /**
     * load config options from the config file
     */
    public static void load() {
        YamlConfiguration config = Config.getMainConfig().getConfig();
        RECEIVE_DELAY = config.getInt("receive-delay");
        RESEND_DELAY = config.getInt("resend-delay");
        REMOVE_DELAY = config.getInt("remove-delay");
        SPAWN_DISTANCE = config.getInt("spawn-distance");
        PROTECTED_COURIER = config.getBoolean("protected-courier");
        COURIER_ENTITY_TYPE = EntityType.valueOf(config.getString("courier-entity-type"));
        BLOCKED_GAMEMODES = new HashSet<>();
        BLOCKED_WORLDS = new HashSet<>();
        for(String s : config.getStringList("blocked-gamemodes")) BLOCKED_GAMEMODES.add(GameMode.valueOf(s));
        for(String s : config.getStringList("blocked-worlds")) BLOCKED_WORLDS.add(Bukkit.getWorld(s));
    }
    
}
