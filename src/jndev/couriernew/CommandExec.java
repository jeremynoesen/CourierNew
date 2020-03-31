package jndev.couriernew;

import jndev.couriernew.letter.LetterChecker;
import jndev.couriernew.letter.LetterCreation;
import jndev.couriernew.letter.LetterSender;
import jndev.couriernew.postman.PostmanChecker;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Command class, runs commands if not in blocked worlds or gamemodes.
 */
public class CommandExec implements CommandExecutor {
    
    private LetterSender ls = new LetterSender();
    private PostmanChecker pc = new PostmanChecker();
    private LetterCreation lc = new LetterCreation();
    private LetterChecker il = new LetterChecker();
    private Message msg = new Message();
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            
            Player player = (Player) sender;
            List<String> modes = new ArrayList<>(CourierNew.getInstance().getConfig().getStringList("blocked-gamemodes"));
            List<String> worlds = new ArrayList<>(CourierNew.getInstance().getConfig().getStringList("blocked-worlds"));
            
            if (!worlds.contains(player.getWorld().getName()) && !modes.contains(player.getGameMode().toString())) {
                
                if (label.equalsIgnoreCase("letter")) {
                    
                    if (player.hasPermission("couriernew.letter")) {
                        if (args.length >= 1) {
                            StringBuilder sb = new StringBuilder();
                            for (String arg : args) {
                                sb.append(arg).append(" ");
                            }
                            if (il.isHoldingOwnLetter(player) && !il.wasSent(player.getInventory().getItemInMainHand()))
                                lc.editBook(player, sb.toString());
                            else lc.writeBook(player, sb.toString());
                            //lc.writeMap(player, sb.toString());
                        } else
                            player.sendMessage(Message.ERROR_NO_MSG);
                    } else
                        player.sendMessage(Message.ERROR_NO_PERMS);
                    
                }
                
                
                if (label.equalsIgnoreCase("cnreload")) {
                    
                    if (player.hasPermission("couriernew.reload")) {
                        CourierNew.getInstance().reloadConfig();
                        Message.reloadConfig();
                        
                        int count = 0;
                        CourierNew.getInstance().getServer().getConsoleSender().sendMessage(msg.CLEANING);
                        
                        for (World world : Bukkit.getWorlds()) {
                            for (Entity entity : world.getEntities()) {
                                if (pc.isPostman(entity)) {
                                    entity.remove();
                                    count++;
                                }
                            }
                        }
                        
                        CourierNew.getInstance().getServer().getConsoleSender().sendMessage(msg.DONE_CLEANING.replace("$COUNT$",
                                Integer.toString(count)));
                        player.sendMessage(Message.SUCCESS_RELOADED);
                    } else
                        player.sendMessage(Message.ERROR_NO_PERMS);
                    
                }
                
                
                if (label.equalsIgnoreCase("post")) {
                    
                    if (args.length == 1) {
                        if (player.hasPermission("couriernew.post.one") || player.hasPermission("couriernew" +
                                ".post.multiple") ||
                                player.hasPermission("couriernew.post.allonline") || player.hasPermission("couriernew" +
                                ".post.all")) {
                            ls.send(player, args[0]);
                        } else
                            player.sendMessage(Message.ERROR_NO_PERMS);
                    } else
                        player.sendMessage(Message.ERROR_TOO_MANY_ARGS);
                    
                }
                
                
                if (label.equalsIgnoreCase("cnhelp")) {
                    
                    if (player.hasPermission("couriernew.help")) {
                        if (player.hasPermission("couriernew.reload")) {
                            player.sendMessage(msg.OP_HELP);
                        } else player.sendMessage(msg.HELP);
                        
                    } else
                        player.sendMessage(Message.ERROR_NO_PERMS);
                    
                }
                
                
                if (label.equalsIgnoreCase("shred")) {
                    
                    if (player.hasPermission("couriernew.shred")) {
                        lc.delete(player);
                    } else
                        player.sendMessage(Message.ERROR_NO_PERMS);
                    
                }
                
                
                if (label.equalsIgnoreCase("shredall")) {
                    
                    if (player.hasPermission("couriernew.shredall")) {
                        lc.deleteAll(player);
                    } else
                        player.sendMessage(Message.ERROR_NO_PERMS);
                    
                }
                
                
                if (label.equalsIgnoreCase("unread")) {
                    
                    if (player.hasPermission("couriernew.unread")) {
                        FileConfiguration outgoing = CourierNew.getOutgoing();
                        
                        if (outgoing.getList(player.getUniqueId().toString()) != null && outgoing.getList(player.getUniqueId().toString()).size() > 0) {
                            player.sendMessage(Message.SUCCESS_EXTRA_DELIVERIES);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (player.isOnline()) {
                                        ls.spawnPostman(player);
                                    }
                                }
                            }.runTaskLater(CourierNew.getInstance(), CourierNew.getInstance().getConfig().getLong("unread-delay"));
                        } else {
                            player.sendMessage(Message.ERROR_NO_MAIL);
                        }
                    } else
                        player.sendMessage(Message.ERROR_NO_PERMS);
                }
                return true;
                
                
            } else {
                player.sendMessage(Message.ERROR_WORLD);
                return true;
            }
        }
        return false;
    }
}
