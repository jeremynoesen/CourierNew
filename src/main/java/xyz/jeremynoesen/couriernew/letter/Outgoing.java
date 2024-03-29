package xyz.jeremynoesen.couriernew.letter;

import xyz.jeremynoesen.couriernew.Config;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * class to handle loading and saving outgoing letters
 *
 * @author Jeremy Noesen
 */
public class Outgoing {
    
    /**
     * hashmap of outgoing letters for players
     */
    private static HashMap<UUID, List<ItemStack>> outgoing = new HashMap<>();
    
    /**
     * reference to outgoing config
     */
    private static Config outgoingConfig = Config.getOutgoingConfig();
    
    
    /**
     * save outgoing letters to file for a player
     *
     * @param player player to save outgoing data
     */
    private static void savePlayer(UUID player) {
        if(outgoing.containsKey(player) && outgoing.get(player).size() > 0)
        outgoingConfig.getConfig().set(player.toString(), outgoing.get(player));
    }
    
    /**
     * save all outgoing letters to file
     */
    public static void saveAll() {
        for (String key : outgoingConfig.getConfig().getKeys(false)) {
            outgoingConfig.getConfig().set(key, null);
        }
        for (UUID player : outgoing.keySet()) {
            savePlayer(player);
        }
        outgoingConfig.saveConfig();
    }
    
    /**
     * load outgoing letters for a player
     *
     * @param player player to load data for
     */
    private static void loadPlayer(UUID player) {
        outgoing.put(player, (List<ItemStack>) outgoingConfig.getConfig().getList(player.toString()));
    }
    
    /**
     * load all outgoing letters from file
     */
    public static void loadAll() {
        for (String key : outgoingConfig.getConfig().getKeys(false)) {
            loadPlayer(UUID.fromString(key));
        }
    }
    
    /**
     * get the hashmap of outgoing letters
     *
     * @return outgoing letter hashmap
     */
    public static HashMap<UUID, List<ItemStack>> getOutgoing() {
        return outgoing;
    }
}
