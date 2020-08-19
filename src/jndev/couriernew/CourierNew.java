package jndev.couriernew;

import jndev.couriernew.command.CommandExec;
import jndev.couriernew.config.ConfigType;
import jndev.couriernew.config.Configs;
import jndev.couriernew.courier.CourierChecker;
import jndev.couriernew.letter.LetterSender;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Main class for plugin, registers and initializes all files, listeners, and commands
 *
 * @author Jeremy Noesen
 */
public class CourierNew extends JavaPlugin {
    
    /**
     * plugin instance
     */
    public static CourierNew plugin;
    
    /**
     * get the plugin instance
     *
     * @return plugin instance
     */
    public static CourierNew getInstance() {
        return plugin;
    }
    
    /**
     * initialize configurations, load messages, register commands and permissions, and delete leftover postmen
     */
    public void onEnable() {
        plugin = this;
        
        Configs.getConfig(ConfigType.MESSAGE).saveDefaultConfig();
        Configs.getConfig(ConfigType.COURIERS).saveDefaultConfig();
        Configs.getConfig(ConfigType.OUTGOING).saveDefaultConfig();
        Configs.getConfig(ConfigType.MAIN).saveDefaultConfig();
        
        Message.reloadMessages();
        
        plugin.getServer().getConsoleSender().sendMessage(Message.STARTUP);
        
        PluginManager pm = Bukkit.getPluginManager();
        
        pm.registerEvents(new LetterSender(), this);
        
        pm.addPermission(new Permission("couriernew.letter"));
        pm.addPermission(new Permission("couriernew.post.one"));
        pm.addPermission(new Permission("couriernew.post.multiple"));
        pm.addPermission(new Permission("couriernew.post.allonline"));
        pm.addPermission(new Permission("couriernew.post.all"));
        pm.addPermission(new Permission("couriernew.help"));
        pm.addPermission(new Permission("couriernew.shred"));
        pm.addPermission(new Permission("couriernew.shredall"));
        pm.addPermission(new Permission("couriernew.unread"));
        pm.addPermission(new Permission("couriernew.reload"));
        
        getCommand("letter").setExecutor(new CommandExec());
        getCommand("post").setExecutor(new CommandExec());
        getCommand("cnhelp").setExecutor(new CommandExec());
        getCommand("shred").setExecutor(new CommandExec());
        getCommand("shredall").setExecutor(new CommandExec());
        getCommand("unread").setExecutor(new CommandExec());
        getCommand("cnreload").setExecutor(new CommandExec());
        
        new BukkitRunnable() {
            @Override
            public void run() {
                
                int count = 0;
                CourierChecker pc = new CourierChecker();
                plugin.getServer().getConsoleSender().sendMessage(Message.CLEANING);
                
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if (pc.isPostman(entity)) {
                            entity.remove();
                            count++;
                        }
                    }
                }
                
                plugin.getServer().getConsoleSender().sendMessage(Message.DONE_CLEANING.replace("$COUNT$",
                        Integer.toString(count)));
                
            }
        }.runTaskLater(plugin, 2);
        
        //saveResource("paper.png", false);
    }
    
    /**
     * nullify the plugin instance
     */
    public void onDisable() {
        plugin = null;
    }
}

//todo vault support

//todo item cost for letter

//todo cancelable letters

//todo add to letter not page