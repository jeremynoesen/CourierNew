package xyz.jeremynoesen.couriernew.letter;

import xyz.jeremynoesen.couriernew.CourierNew;
import xyz.jeremynoesen.couriernew.Message;
import xyz.jeremynoesen.couriernew.courier.Courier;
import xyz.jeremynoesen.couriernew.courier.CourierOptions;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Send the letters to players
 *
 * @author Jeremy Noesen
 */
public class LetterSender implements Listener {
    
    /**
     * Send a letter to a player. The letter will have the recipient added to the lore, preventing it from being sent
     * again. It also adds it to a yml file of letters to be received. If the player receiving is online, they may
     * receive their letter.
     *
     * @param sender    player sending the letter
     * @param recipient player(s) to receive the letter
     */
    @SuppressWarnings("deprecation")
    public static void send(Player sender, String recipient) {
        if (LetterChecker.isHoldingOwnLetter(sender) &&
                !LetterChecker.wasSent(sender.getInventory().getItemInMainHand())) {
            
            ItemStack letter = new ItemStack(sender.getInventory().getItemInMainHand());
            Collection<OfflinePlayer> offlinePlayers = null;
            ItemMeta im = letter.getItemMeta();
            List<String> lore = im.getLore();
            
            if (recipient.equals("*")) {
                
                if (sender.hasPermission("couriernew.post.allonline")) {
                    
                    lore.add(ChatColor.DARK_GRAY + "§TTo Everyone Online");
                    offlinePlayers = new ArrayList<>();
                    offlinePlayers.addAll(Bukkit.getOnlinePlayers());
                    sender.sendMessage(Message.SUCCESS_SENT
                            .replace("$PLAYER$", "Everyone Online"));
                    
                } else sender.sendMessage(Message.ERROR_NO_PERMS);
                
            } else if (recipient.equals("**")) {
                
                if (sender.hasPermission("couriernew.post.all")) {
                    
                    lore.add(ChatColor.DARK_GRAY + "§TTo Everyone");
                    offlinePlayers = new ArrayList<>();
                    offlinePlayers.addAll(Arrays.asList(Bukkit.getOfflinePlayers()));
                    sender.sendMessage(Message.SUCCESS_SENT
                            .replace("$PLAYER$", "Everyone"));
                    
                } else sender.sendMessage(Message.ERROR_NO_PERMS);
                
            } else if (recipient.contains(",")) {
                
                if (sender.hasPermission("couriernew.post.multiple")) {
                    
                    offlinePlayers = new ArrayList<>();
                    
                    for (String recipients : recipient.split(",")) {
                        OfflinePlayer op;
                        try {
                            op = Bukkit.getOfflinePlayer(recipients);
                        } catch (Exception e) {
                            sender.sendMessage(Message.ERROR_PLAYER_NO_EXIST
                                    .replace("$PLAYER$", recipients));
                            return;
                        }
                        offlinePlayers.add(op);
                    }
                    
                    lore.add(ChatColor.DARK_GRAY + "§TTo Multiple Players");
                    sender.sendMessage(Message.SUCCESS_SENT
                            .replace("$PLAYER$", "Multiple Players"));
                    
                } else sender.sendMessage(Message.ERROR_NO_PERMS);
                
            } else {
                
                if (sender.hasPermission("couriernew.post.one")) {
                    OfflinePlayer op;
                    try {
                        op = Bukkit.getOfflinePlayer(recipient);
                    } catch (Exception e) {
                        sender.sendMessage(Message.ERROR_PLAYER_NO_EXIST
                                .replace("$PLAYER$", recipient));
                        return;
                    }
                    offlinePlayers = new ArrayList<>();
                    offlinePlayers.add(op);
                    lore.add(ChatColor.DARK_GRAY + "§TTo " + op.getName());
                    sender.sendMessage(Message.SUCCESS_SENT
                            .replace("$PLAYER$", op.getName()));
                    
                } else sender.sendMessage(Message.ERROR_NO_PERMS);
            }
            
            if (offlinePlayers != null && offlinePlayers.size() > 0) {
                
                im.setLore(lore);
                letter.setItemMeta(im);
                
                for (OfflinePlayer op : offlinePlayers) {
                    
                    if (offlinePlayers.size() > 1 && op.equals(sender)) continue;
                    
                    if (!Outgoing.getOutgoing().containsKey(op.getUniqueId()))
                        Outgoing.getOutgoing().put(op.getUniqueId(), new ArrayList<>());
                    Outgoing.getOutgoing().get(op.getUniqueId()).add(new ItemStack(letter));
                    
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (op.isOnline()) {
                                spawnCourier((Player) op);
                            }
                        }
                    }.runTaskLater(CourierNew.getInstance(), CourierOptions.RECEIVE_DELAY);
                }
                
                sender.getInventory().getItemInMainHand().setAmount(0);
            }
            
        } else if (LetterChecker.isHoldingOwnLetter(sender)) {
            sender.sendMessage(Message.ERROR_SENT_BEFORE);
        } else if (LetterChecker.isHoldingLetter(sender)) {
            sender.sendMessage(Message.ERROR_NOT_YOUR_LETTER);
        } else
            sender.sendMessage(Message.ERROR_NO_LETTER);
    }
    
    /**
     * When clicking the courier, retrieve the letters from the file and give all of them to the player. If they have
     * space in their inventory, give them all, starting with their hand if they aren't holding anything. Letters not
     * taken will be delivered later.
     *
     * @param recipient player receiving the mail
     */
    public static void receive(Player recipient) {
        if (Outgoing.getOutgoing().containsKey(recipient.getUniqueId()) && Outgoing.getOutgoing().get(recipient.getUniqueId()).size() > 0) {
            CopyOnWriteArrayList<ItemStack> letters = new CopyOnWriteArrayList<>(Outgoing.getOutgoing().get(recipient.getUniqueId()));
            
            for (ItemStack letter : letters) {
                if (recipient.getInventory().firstEmpty() < 0) {
                    recipient.sendMessage(Message.ERROR_CANT_HOLD);
                    break;
                } else if (recipient.getInventory().getItemInMainHand().getAmount() == 0) {
                    recipient.getInventory().setItemInMainHand(letter);
                    letters.remove(0);
                } else {
                    recipient.getInventory().addItem(letter);
                    letters.remove(0);
                }
            }
            
            recipient.updateInventory();
            
            Outgoing.getOutgoing().put(recipient.getUniqueId(), letters);
        }
    }
    
    /**
     * Create the courier entity for a player if they are not vanished or in blocked gamemodes or worlds
     *
     * @param recipient player receiving letters
     */
    public static void spawnCourier(Player recipient) {
        if (Courier.canSpawn(recipient)) {
            Courier courier = new Courier(recipient);
            courier.spawn();
        }
    }
    
    /**
     * Check when a player right clicks their courier entity
     */
    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent e) {
        Entity en = e.getRightClicked();
        if (Courier.getCouriers().containsKey(en)) {
            if (!CourierOptions.PROTECTED_COURIER || (Courier.getCouriers().get(en).getRecipient().equals(e.getPlayer())
                    && !Courier.getCouriers().get(en).isDelivered())) {
                Courier.getCouriers().get(en).setDelivered();
                e.setCancelled(true);
                receive(e.getPlayer());
                en.getWorld().playSound(en.getLocation(), Sound.BLOCK_WOOL_BREAK, 1, 1);
                en.getWorld().spawnParticle(Particle.VILLAGER_HAPPY,
                        en.getLocation().add(0, en.getHeight() / 2, 0), 20,
                        en.getWidth() / 2, en.getHeight() / 2, en.getWidth() / 2);
            } else if (!Courier.getCouriers().get(en).getRecipient().equals(e.getPlayer())) {
                e.setCancelled(true);
                en.getWorld().playSound(en.getLocation(), Sound.UI_TOAST_OUT, 1, 1);
            } else if (Courier.getCouriers().get(en).isDelivered()) e.setCancelled(true);
        }
    }
    
    /**
     * Check when a player joins so they can retrieve unread mail
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline() && Outgoing.getOutgoing().containsKey(player.getUniqueId())
                        && Outgoing.getOutgoing().get(player.getUniqueId()).size() > 0)
                    spawnCourier(player);
            }
        }.runTaskLater(CourierNew.getInstance(), CourierOptions.RECEIVE_DELAY);
    }
    
    /**
     * If a player is coming from a blocked world, they will retrieve their mail
     */
    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Set<World> worlds = CourierOptions.BLOCKED_WORLDS;
        World to = e.getTo().getWorld();
        World from = e.getFrom().getWorld();
        Player recipient = e.getPlayer();
        
        if (worlds.contains(from) && !worlds.contains(to)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (recipient.isOnline() && Outgoing.getOutgoing().containsKey(recipient.getUniqueId())
                            && Outgoing.getOutgoing().get(recipient.getUniqueId()).size() > 0)
                        spawnCourier(recipient);
                }
            }.runTaskLater(CourierNew.getInstance(), CourierOptions.RECEIVE_DELAY);
        }
    }
    
    /**
     * If a player is changing from a blocked gamemode, they will retrieve their mail
     */
    @EventHandler
    public void onGamemode(PlayerGameModeChangeEvent e) {
        Set<GameMode> modes = CourierOptions.BLOCKED_GAMEMODES;
        GameMode to = e.getNewGameMode();
        GameMode from = e.getPlayer().getGameMode();
        Player recipient = e.getPlayer();
        if (modes.contains(from) && !modes.contains(to)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (recipient.isOnline() && Outgoing.getOutgoing().containsKey(recipient.getUniqueId())
                            && Outgoing.getOutgoing().get(recipient.getUniqueId()).size() > 0)
                        spawnCourier(recipient);
                }
            }.runTaskLater(CourierNew.getInstance(), CourierOptions.RECEIVE_DELAY);
        }
    }
    
    /**
     * prevent villager couriers from changing profession
     */
    @EventHandler
    public void onVillagerProfession(VillagerCareerChangeEvent e) {
        if (Courier.getCouriers().keySet().contains(e.getEntity())) e.setCancelled(true);
    }
    
    /**
     * prevent players from running non-couriernew commands while holding a letter
     */
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (LetterChecker.isHoldingLetter(e.getPlayer()) &&
                !e.getMessage().contains("letter") &&
                !e.getMessage().contains("cnreload") &&
                !e.getMessage().contains("post") &&
                !e.getMessage().contains("cnhelp") &&
                !e.getMessage().contains("shred") &&
                !e.getMessage().contains("shredall") &&
                !e.getMessage().contains("unread")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(Message.ERROR_NO_PERMS);
        }
    }
}
