package me.Jeremaster101.CourierNew.Postman;

import com.earth2me.essentials.Essentials;
import de.myzelyam.supervanish.SuperVanish;
import me.Jeremaster101.CourierNew.CourierNew;
import me.Jeremaster101.CourierNew.Message;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.kitteh.vanish.VanishPlugin;

import java.io.File;
import java.util.ArrayList;

/**
 * Methods to check info about a postman
 */
public class PostmanChecker {
    
    /**
     * @param en entity to check
     * @return true if entity is a postman
     */
    public boolean isPostman(Entity en) {
        File postmenyml = new File(CourierNew.plugin.getDataFolder(), "postmen.yml");
        FileConfiguration postmen = YamlConfiguration.loadConfiguration(postmenyml);
        return postmen.getKeys(false).contains(en.getUniqueId().toString());
    }
    
    /**
     * @param p  player to check ownership of the postman
     * @param en entity to check
     * @return true if entity is a postman for player p
     */
    public boolean isPlayersPostman(Player p, Entity en) {
        File postmenyml = new File(CourierNew.plugin.getDataFolder(), "postmen.yml");
        FileConfiguration postmen = YamlConfiguration.loadConfiguration(postmenyml);
        return isPostman(en) && postmen.get(en.getUniqueId().toString()).equals(p.getName());
    }
    
    /**
     * @param p  player to check owndership of the postman
     * @param en entity to check
     * @return true if entity is a postman but not for player p
     */
    public boolean isOtherPlayersPostman(Player p, Entity en) {
        File postmenyml = new File(CourierNew.plugin.getDataFolder(), "postmen.yml");
        FileConfiguration postmen = YamlConfiguration.loadConfiguration(postmenyml);
        return isPostman(en) && !postmen.get(en.getUniqueId().toString()).equals(p.getName());
    }
    
    /**
     * @param en entity to check
     * @return true if entity isa postman who already delivered mail
     */
    public boolean isReceivedPostman(Entity en) {
        Message msg = new Message();
        return isPostman(en) && en.getCustomName() != null && en.getCustomName().equals(Message.POSTMAN_NAME_RECEIVED);
    }
    
    /**
     * @param p player to check
     * @return true if not vanished or not in a restricted world or mode
     */
    public boolean canRecieveMail(Player p) {
        if (Bukkit.getPluginManager().isPluginEnabled("VanishNoPacket")) {
            if (((VanishPlugin) Bukkit.getPluginManager().getPlugin("VanishNoPacket")).getManager().isVanished
                    (p)) {
                p.sendMessage(Message.ERROR_VANISHED);
                return false;
            }
        }
        
        if (Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
            if (((Essentials) Bukkit.getPluginManager().getPlugin("Essentials")).getVanishedPlayers().contains(p.getName())) {
                p.sendMessage(Message.ERROR_VANISHED);
                return false;
            }
        }
        
        if (Bukkit.getPluginManager().isPluginEnabled("SuperVanish")) {
            if (((SuperVanish) Bukkit.getPluginManager().getPlugin("SuperVanish")).getVanishStateMgr().isVanished(p.getUniqueId())) {
                p.sendMessage(Message.ERROR_VANISHED);
                return false;
            }
        }
        
        ArrayList<String> worlds = new ArrayList<>(CourierNew.plugin.getConfig().getStringList("blocked-worlds"));
        ArrayList<String> modes = new ArrayList<>(CourierNew.plugin.getConfig().getStringList("blocked-gamemodes"));
        if (worlds.contains(p.getWorld().getName()) || modes.contains(p.getGameMode().toString())) {
            p.sendMessage(Message.ERROR_WORLD);
            return false;
        }
        return true;
    }
}
