package jeremynoesen.couriernew;

import jeremynoesen.couriernew.command.CommandExec;
import jeremynoesen.couriernew.courier.CourierOptions;
import jeremynoesen.couriernew.courier.Courier;
import jeremynoesen.couriernew.letter.LetterSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
     * initialize configurations, load messages, register commands and permissions
     */
    public void onEnable() {
        plugin = this;
        
        Config.getMessageConfig().saveDefaultConfig();
        Config.getOutgoingConfig().saveDefaultConfig();
        Config.getMainConfig().saveDefaultConfig();
        
        CourierOptions.load();
        Message.reloadMessages();
        
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
    }
    
    /**
     * nullify the plugin instance
     */
    public void onDisable() {
        Courier.getCouriers().keySet().forEach(Entity::remove);
        plugin = null;
    }
}