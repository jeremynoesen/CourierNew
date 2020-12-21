package jeremynoesen.couriernew.command;

import jeremynoesen.couriernew.CourierNew;
import jeremynoesen.couriernew.Message;
import jeremynoesen.couriernew.Config;
import jeremynoesen.couriernew.letter.LetterChecker;
import jeremynoesen.couriernew.letter.LetterCreation;
import jeremynoesen.couriernew.letter.LetterSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Command class, runs commands if not in blocked worlds or gamemodes.
 *
 * @author Jeremy Noesen
 */
public class CommandExec implements CommandExecutor {
    
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
                            if (LetterChecker.isHoldingOwnLetter(player) && !LetterChecker.wasSent(player.getInventory().getItemInMainHand()))
                                LetterCreation.editBook(player, sb.toString());
                            else LetterCreation.writeBook(player, sb.toString());
                            //lc.writeMap(player, sb.toString());
                        } else
                            player.sendMessage(Message.ERROR_NO_MSG);
                    } else
                        player.sendMessage(Message.ERROR_NO_PERMS);
                    
                }
                
                
                if (label.equalsIgnoreCase("cnreload")) {
                    
                    if (player.hasPermission("couriernew.reload")) {
                        Config.getMainConfig().reloadConfig();
                        Config.getOutgoingConfig().reloadConfig();
                        Config.getMessageConfig().reloadConfig();
                        
                        //todo delete all couriers
                        
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
                            LetterSender.send(player, args[0]);
                        } else
                            player.sendMessage(Message.ERROR_NO_PERMS);
                    } else
                        player.sendMessage(Message.ERROR_TOO_MANY_ARGS);
                    
                }
                
                
                if (label.equalsIgnoreCase("cnhelp")) {
                    
                    if (player.hasPermission("couriernew.help")) {
                        if (player.hasPermission("couriernew.reload")) {
                            player.sendMessage(Message.OP_HELP);
                        } else player.sendMessage(Message.HELP);
                        
                    } else
                        player.sendMessage(Message.ERROR_NO_PERMS);
                    
                }
                
                
                if (label.equalsIgnoreCase("shred")) {
                    
                    if (player.hasPermission("couriernew.shred")) {
                        LetterCreation.delete(player);
                    } else
                        player.sendMessage(Message.ERROR_NO_PERMS);
                    
                }
                
                
                if (label.equalsIgnoreCase("shredall")) {
                    
                    if (player.hasPermission("couriernew.shredall")) {
                        LetterCreation.deleteAll(player);
                    } else
                        player.sendMessage(Message.ERROR_NO_PERMS);
                    
                }
                
                
                if (label.equalsIgnoreCase("unread")) {
                    
                    if (player.hasPermission("couriernew.unread")) {
                        FileConfiguration outgoing = Config.getOutgoingConfig().getConfig();
                        
                        if (outgoing.getList(player.getUniqueId().toString()) != null && outgoing.getList(player.getUniqueId().toString()).size() > 0) {
                            player.sendMessage(Message.SUCCESS_EXTRA_DELIVERIES);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (player.isOnline()) {
                                        LetterSender.spawnCourier(player);
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
