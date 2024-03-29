package xyz.jeremynoesen.couriernew;

import xyz.jeremynoesen.couriernew.command.CommandExec;
import xyz.jeremynoesen.couriernew.command.CommandTabComplete;
import xyz.jeremynoesen.couriernew.courier.CourierOptions;
import xyz.jeremynoesen.couriernew.courier.Courier;
import xyz.jeremynoesen.couriernew.letter.LetterSender;
import xyz.jeremynoesen.couriernew.letter.Outgoing;
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
        Outgoing.loadAll();
        Message.reloadMessages();
        
        PluginManager pm = Bukkit.getPluginManager();
        
        pm.registerEvents(new LetterSender(), plugin);

        pm.addPermission(new Permission("couriernew.letter"));
        pm.addPermission(new Permission("couriernew.post.one"));
        pm.addPermission(new Permission("couriernew.post.multiple"));
        pm.addPermission(new Permission("couriernew.post.allonline"));
        pm.addPermission(new Permission("couriernew.post.all"));
        pm.addPermission(new Permission("couriernew.unread"));
        pm.addPermission(new Permission("couriernew.shred"));
        pm.addPermission(new Permission("couriernew.shredall"));
        pm.addPermission(new Permission("couriernew.help"));
        pm.addPermission(new Permission("couriernew.reload"));
        
        CommandExec commandExec = new CommandExec();
        
        getCommand("letter").setExecutor(commandExec);
        getCommand("post").setExecutor(commandExec);
        getCommand("shred").setExecutor(commandExec);
        getCommand("shredall").setExecutor(commandExec);
        getCommand("unread").setExecutor(commandExec);
        getCommand("couriernew").setExecutor(commandExec);

        getCommand("couriernew").setTabCompleter(new CommandTabComplete());
    }
    
    /**
     * nullify the plugin instance
     */
    public void onDisable() {
        Courier.getCouriers().keySet().forEach(Entity::remove);
        Outgoing.saveAll();
        plugin = null;
    }
}
