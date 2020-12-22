package jeremynoesen.couriernew.letter;

import jeremynoesen.couriernew.Config;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
    private static HashMap<OfflinePlayer, List<ItemStack>> outgoing = new HashMap<>();
    
    /**
     * reference to outgoing config
     */
    private static Config outgoingConfig = Config.getOutgoingConfig();
    
    
    /**
     * save outgoing letters to file for a player
     *
     * @param player player to save outgoing data
     */
    private static void savePlayer(OfflinePlayer player) {
        outgoingConfig.getConfig().set(player.getUniqueId().toString(), outgoing.get(player));
        outgoingConfig.saveConfig();
    }
    
    /**
     * save all outgoing letters to file
     */
    public static void saveAll() {
        for (String key : outgoingConfig.getConfig().getKeys(false)) {
            outgoingConfig.getConfig().set(key, null);
        }
        for (OfflinePlayer player : outgoing.keySet()) {
            savePlayer(player);
        }
    }
    
    /**
     * load outgoing letters for a player
     *
     * @param player player to load data for
     */
    private static void loadPlayer(OfflinePlayer player) {
        outgoing.put(player, (List<ItemStack>) outgoingConfig.getConfig().getList(player.getUniqueId().toString()));
    }
    
    /**
     * load all outgoing letters from file
     */
    public static void loadAll() {
        for (String key : outgoingConfig.getConfig().getKeys(false)) {
            loadPlayer(Bukkit.getOfflinePlayer(UUID.fromString(key)));
        }
    }
    
    /**
     * get the hashmap of outgoing letters
     *
     * @return outgoing letter hashmap
     */
    public static HashMap<OfflinePlayer, List<ItemStack>> getOutgoing() {
        return outgoing;
    }
}
