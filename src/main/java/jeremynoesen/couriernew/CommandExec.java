package jeremynoesen.couriernew;

import jeremynoesen.couriernew.courier.Courier;
import jeremynoesen.couriernew.courier.CourierOptions;
import jeremynoesen.couriernew.letter.LetterChecker;
import jeremynoesen.couriernew.letter.LetterCreation;
import jeremynoesen.couriernew.letter.LetterSender;
import jeremynoesen.couriernew.letter.Outgoing;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

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
            Set<GameMode> modes = CourierOptions.BLOCKED_GAMEMODES;
            Set<World> worlds = CourierOptions.BLOCKED_WORLDS;
            
            if (!worlds.contains(player.getWorld()) && !modes.contains(player.getGameMode())) {
                
                switch (label.toLowerCase()) {
                    
                    case "letter":
                        if (player.hasPermission("couriernew.letter")) {
                            if (args.length >= 1) {
                                StringBuilder sb = new StringBuilder();
                                for (String arg : args) {
                                    sb.append(arg).append(" ");
                                }
                                if (LetterChecker.isHoldingOwnLetter(player) &&
                                        !LetterChecker.wasSent(player.getInventory().getItemInMainHand()))
                                    LetterCreation.editBook(player, sb.toString());
                                else LetterCreation.writeBook(player, sb.toString());
                            } else
                                player.sendMessage(Message.ERROR_NO_MSG);
                        } else
                            player.sendMessage(Message.ERROR_NO_PERMS);
                        break;
                    
                    case "cnreload":
                        if (player.hasPermission("couriernew.reload")) {
                            Outgoing.saveAll();
                            Courier.getCouriers().keySet().forEach(Entity::remove);
                            Courier.getCouriers().clear();
                            Config.getMainConfig().reloadConfig();
                            Config.getOutgoingConfig().reloadConfig();
                            Config.getMessageConfig().reloadConfig();
                            CourierOptions.load();
                            Outgoing.loadAll();
                            Message.reloadMessages();
                            player.sendMessage(Message.SUCCESS_RELOADED);
                        } else
                            player.sendMessage(Message.ERROR_NO_PERMS);
                        break;
                    
                    case "post":
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
                        break;
                    
                    case "cnhelp":
                        if (player.hasPermission("couriernew.help")) {
                            player.sendMessage(Message.HELP);
                        } else
                            player.sendMessage(Message.ERROR_NO_PERMS);
                        break;
                    
                    case "shred":
                        if (player.hasPermission("couriernew.shred")) {
                            LetterCreation.delete(player);
                        } else
                            player.sendMessage(Message.ERROR_NO_PERMS);
                        break;
                    
                    case "shredall":
                        if (player.hasPermission("couriernew.shredall")) {
                            LetterCreation.deleteAll(player);
                        } else
                            player.sendMessage(Message.ERROR_NO_PERMS);
                        break;
                    
                    case "unread":
                        if (player.hasPermission("couriernew.unread")) {
                            if (Outgoing.getOutgoing().containsKey(player.getUniqueId()) &&
                                    Outgoing.getOutgoing().get(player.getUniqueId()).size() > 0) {
                                player.sendMessage(Message.SUCCESS_EXTRA_DELIVERIES);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (player.isOnline()) {
                                            LetterSender.spawnCourier(player);
                                        }
                                    }
                                }.runTaskLater(CourierNew.getInstance(), CourierOptions.RECEIVE_DELAY);
                            } else {
                                player.sendMessage(Message.ERROR_NO_MAIL);
                            }
                        } else
                            player.sendMessage(Message.ERROR_NO_PERMS);
                        break;
                }
            } else {
                player.sendMessage(Message.ERROR_WORLD);
            }
            return true;
        }
        return false;
    }
}
