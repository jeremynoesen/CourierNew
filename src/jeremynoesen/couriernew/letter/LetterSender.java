package jeremynoesen.couriernew.letter;

import jeremynoesen.couriernew.CourierNew;
import jeremynoesen.couriernew.Message;
import jeremynoesen.couriernew.config.Config;
import jeremynoesen.couriernew.courier.Courier;
import jeremynoesen.couriernew.courier.Couriers;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Send the letters to players
 */
public class LetterSender implements Listener {
    
    private static final Config outgoingConfig = Config.getOutgoingConfig();
    
    /**
     * Send a letter to a player. The letter will have the recipient added to the lore, preventing it from being sent
     * again. It also adds it to a yml file of letters to be recieved. If the player recieving is online, they may
     * recieve their letter.
     *
     * @param sender    player sending the letter
     * @param recipient player(s) to recieve the letter
     */
    @SuppressWarnings("deprecation")
    public static void send(Player sender, String recipient) {
        if (LetterChecker.isHoldingOwnLetter(sender) && !LetterChecker.wasSent(sender.getInventory().getItemInMainHand())) {
            ItemStack letter = sender.getInventory().getItemInMainHand();
            FileConfiguration outgoing = outgoingConfig.getConfig();
            if (recipient.equals("*")) {
                
                if (sender.hasPermission("couriernew.post.allonline")) {
                    
                    for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
                        ItemStack letterToAll = new ItemStack(letter.getType());
                        letterToAll.setItemMeta(letter.getItemMeta());
                        ArrayList<String> lore = new ArrayList<>(letterToAll.getItemMeta().getLore());
                        UUID uuid;
                        List<ItemStack> letters;
                        
                        try {
                            uuid = op.getUniqueId();
                            if (!op.isOnline()) continue;
                        } catch (Exception e) {
                            continue;
                        }
                        
                        OfflinePlayer recplayer = Bukkit.getOfflinePlayer(uuid);
                        
                        if (recplayer.getName().equals(sender.getName())) continue;
                        
                        if (lore.get(lore.size() - 1).contains("§TTo ")) { //§TTo shows up as "To" in the lore, but doesnt block people from sending letters containing the word.
                            ItemMeta im = letterToAll.getItemMeta();
                            lore.set(lore.size() - 1, ChatColor.DARK_GRAY + "§TTo " + recplayer.getName());
                            im.setLore(lore);
                            letterToAll.setItemMeta(im);
                        } else {
                            ItemMeta im = letterToAll.getItemMeta();
                            lore.add(ChatColor.DARK_GRAY + "§TTo " + recplayer.getName());
                            im.setLore(lore);
                            letterToAll.setItemMeta(im);
                        }
                        
                        if (outgoing.get(uuid.toString()) == null) {
                            letters = new ArrayList<>();
                        } else
                            letters = (List<ItemStack>) outgoing.getList(uuid.toString());
                        
                        letters.add(letterToAll);
                        outgoing.set(uuid.toString(), letters);
                        
                        Config.getOutgoingConfig().saveConfig();
                        
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (recplayer.isOnline()) {
                                    spawnCourier((Player) recplayer);
                                }
                            }
                        }.runTaskLater(CourierNew.getInstance(), CourierNew.getInstance().getConfig().getLong("send-recieve-delay"));
                    }
                    
                    sender.getInventory().getItemInMainHand().setAmount(0);
                    sender.sendMessage(Message.SUCCESS_SENT.replace("$PLAYER$", "all online players"));
                    
                } else sender.sendMessage(Message.ERROR_NO_PERMS);
                
            } else if (recipient.equals("**")) {
                
                if (sender.hasPermission("couriernew.post.all")) {
                    
                    for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
                        ItemStack letterToAll = new ItemStack(letter.getType());
                        letterToAll.setItemMeta(letter.getItemMeta());
                        ArrayList<String> lore = new ArrayList<>(letterToAll.getItemMeta().getLore());
                        UUID uuid;
                        List<ItemStack> letters;
                        
                        try {
                            uuid = op.getUniqueId();
                        } catch (Exception e) {
                            continue;
                        }
                        
                        OfflinePlayer recplayer = Bukkit.getOfflinePlayer(uuid);
                        
                        if (recplayer.getName().equals(sender.getName())) continue;
                        
                        if (lore.get(lore.size() - 1).contains("§TTo ")) {
                            ItemMeta im = letterToAll.getItemMeta();
                            lore.set(lore.size() - 1, ChatColor.DARK_GRAY + "§TTo " + recplayer.getName());
                            im.setLore(lore);
                            letterToAll.setItemMeta(im);
                        } else {
                            ItemMeta im = letterToAll.getItemMeta();
                            lore.add(ChatColor.DARK_GRAY + "§TTo " + recplayer.getName());
                            im.setLore(lore);
                            letterToAll.setItemMeta(im);
                        }
                        
                        if (outgoing.get(uuid.toString()) == null) {
                            letters = new ArrayList<>();
                        } else
                            letters = (List<ItemStack>) outgoing.getList(uuid.toString());
                        
                        letters.add(letterToAll);
                        outgoing.set(uuid.toString(), letters);
                        
                        Config.getOutgoingConfig().saveConfig();
                        
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (recplayer.isOnline()) {
                                    spawnCourier((Player) recplayer);
                                }
                            }
                        }.runTaskLater(CourierNew.getInstance(), CourierNew.getInstance().getConfig().getLong("send-recieve-delay"));
                    }
                    
                    sender.getInventory().getItemInMainHand().setAmount(0);
                    sender.sendMessage(Message.SUCCESS_SENT.replace("$PLAYER$", "all players of this server"));
                    
                } else sender.sendMessage(Message.ERROR_NO_PERMS);
                
            } else if (recipient.contains(",")) {
                
                if (sender.hasPermission("couriernew.post.multiple")) {
                    
                    ArrayList<String> lore = new ArrayList<>(letter.getItemMeta().getLore());
                    ArrayList<String> success = new ArrayList<>();
                    ArrayList<String> failed = new ArrayList<>();
                    
                    for (String recipients : recipient.split(",")) {
                        OfflinePlayer op = Bukkit.getOfflinePlayer(recipients);
                        ItemStack letterToAll = new ItemStack(letter.getType());
                        letterToAll.setItemMeta(letter.getItemMeta());
                        UUID uuid;
                        List<ItemStack> letters;
                        
                        try {
                            uuid = op.getUniqueId();
                        } catch (Exception e) {
                            failed.add(recipients);
                            continue;
                        }
                        
                        OfflinePlayer recplayer = Bukkit.getOfflinePlayer(uuid);
                        
                        if (lore.get(lore.size() - 1).contains("§TTo ")) {
                            ItemMeta im = letterToAll.getItemMeta();
                            lore.set(lore.size() - 1, ChatColor.DARK_GRAY + "§TTo " + recplayer.getName());
                            im.setLore(lore);
                            letterToAll.setItemMeta(im);
                        } else {
                            ItemMeta im = letterToAll.getItemMeta();
                            lore.add(ChatColor.DARK_GRAY + "§TTo " + recplayer.getName());
                            im.setLore(lore);
                            letterToAll.setItemMeta(im);
                        }
                        
                        if (outgoing.get(uuid.toString()) == null) {
                            letters = new ArrayList<>();
                        } else
                            letters = (List<ItemStack>) outgoing.getList(uuid.toString());
                        
                        letters.add(letterToAll);
                        outgoing.set(uuid.toString(), letters);
                        success.add(recipients);
                        
                        Config.getOutgoingConfig().saveConfig();
                        
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (recplayer.isOnline()) {
                                    spawnCourier((Player) recplayer);
                                }
                            }
                        }.runTaskLater(CourierNew.getInstance(), CourierNew.getInstance().getConfig().getLong("send-recieve-delay"));
                    }
                    
                    if (success.size() > 0) {
                        sender.getInventory().getItemInMainHand().setAmount(0);
                        sender.sendMessage(Message.SUCCESS_SENT.replace("$PLAYER$",
                                success.toString().replace("[", "").replace("]", "")));
                    }
                    
                    if (failed.size() > 0) {
                        sender.sendMessage(Message.ERROR_SEND_FAILED.replace("$PLAYER$",
                                failed.toString().replace("[", "").replace("]", "")));
                    }
                    
                } else sender.sendMessage(Message.ERROR_NO_PERMS);
                
            } else {
                
                if (sender.hasPermission("couriernew.post.one")) {
                    
                    OfflinePlayer op = Bukkit.getOfflinePlayer(recipient);
                    UUID uuid;
                    List<ItemStack> letters;
                    
                    try {
                        uuid = op.getUniqueId();
                    } catch (Exception e) {
                        sender.sendMessage(Message.ERROR_PLAYER_NO_EXIST.replace("$PLAYER$", recipient));
                        return;
                    }
                    
                    OfflinePlayer recplayer = Bukkit.getOfflinePlayer(uuid);
                    
                    ArrayList<String> lore = new ArrayList<>(letter.getItemMeta().getLore());
                    ItemMeta im = letter.getItemMeta();
                    lore.add(ChatColor.DARK_GRAY + "§TTo " + recplayer.getName());
                    im.setLore(lore);
                    letter.setItemMeta(im);
                    
                    if (outgoing.get(uuid.toString()) == null) {
                        letters = new ArrayList<>();
                    } else
                        letters = (List<ItemStack>) outgoing.getList(uuid.toString());
                    
                    letters.add(letter);
                    outgoing.set(uuid.toString(), letters);
                    
                    Config.getOutgoingConfig().saveConfig();
                    
                    sender.getInventory().getItemInMainHand().setAmount(0);
                    sender.sendMessage(Message.SUCCESS_SENT.replace("$PLAYER$", recplayer.getName()));
                    
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (recplayer.isOnline()) {
                                spawnCourier((Player) recplayer);
                            }
                        }
                    }.runTaskLater(CourierNew.getInstance(), CourierNew.getInstance().getConfig().getLong("send-recieve-delay"));
                } else sender.sendMessage(Message.ERROR_NO_PERMS);
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
     * @param recipient player recieving the mail
     */
    public static void receive(Player recipient) {
        FileConfiguration outgoing = outgoingConfig.getConfig();
        UUID uuid = recipient.getUniqueId();
        
        if (outgoing.getList(uuid.toString()) != null && outgoing.getList(uuid.toString()).size() > 0) {
            List<ItemStack> letters = (List<ItemStack>) outgoing.getList(uuid.toString());
            
            for (ItemStack letter : letters.toArray(new ItemStack[0])) {
                if (recipient.getInventory().firstEmpty() < 0) {
                    recipient.sendMessage(Message.ERROR_CANT_HOLD);
                    break;
                } else if (recipient.getInventory().getItemInMainHand().getType() == Material.AIR) {
                    recipient.getInventory().setItemInMainHand(letter);
                    letters.remove(0);
                } else {
                    recipient.getInventory().addItem(letter);
                    letters.remove(0);
                }
            }
            
            outgoing.set(uuid.toString(), letters);
            
            Config.getOutgoingConfig().saveConfig();
        }
    }
    
    /**
     * Create the courier entity for a player if they are not vanished or in blocked gamemodes or worlds
     *
     * @param recipient player recieving letters
     */
    public static void spawnCourier(Player recipient) {
        
        if (Couriers.canRecieveMail(recipient)) {
            
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
        if ((CourierChecker.isCourier(en) && !CourierNew.getInstance().getConfig().getBoolean("protected-courier")) || CourierChecker.isPlayersCourier(e.getPlayer(), en) && !CourierChecker.isReceivedCourier(en)) {
            en.setCustomName(Message.POSTMAN_NAME_RECEIVED);
            e.setCancelled(true);
            receive(e.getPlayer());
            en.getWorld().playSound(en.getLocation(), Sound.BLOCK_WOOL_BREAK, 1, 1);
            en.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, en.getLocation().add(0, en.getHeight() / 2, 0), 20, en.getWidth() / 2, en.getHeight(), en.getWidth() / 2);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!en.isDead()) {
                        en.remove();
                        Configs.getConfig(ConfigType.COURIERS).getConfig().set(en.getUniqueId().toString(), null);
                    }
                }
            }.runTaskLater(CourierNew.getInstance(), CourierNew.getInstance().getConfig().getLong("remove-courier-recieved-delay"));
        } else if (CourierChecker.isOtherPlayersCourier(e.getPlayer(), en)) {
            e.setCancelled(true);
            en.getWorld().playSound(en.getLocation(), Sound.UI_TOAST_OUT, 1, 1);
        } else if (CourierChecker.isReceivedCourier(en)) e.setCancelled(true);
    }
    
    /**
     * Check when a player joins so they can retrieve unread mail
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        FileConfiguration outgoing = outgoingConfig.getConfig();
        
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline() && outgoing.getList(player.getUniqueId().toString()) != null &&
                        outgoing.getList(player.getUniqueId().toString()).size() > 0) {
                    spawnCourier(player);
                }
            }
        }.runTaskLater(CourierNew.getInstance(), CourierNew.getInstance().getConfig().getLong("join-recieve-delay"));
    }
    
    /**
     * If a player is coming from a blocked world, they will retrieve their mail
     */
    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        List<String> worlds = new ArrayList<>(CourierNew.getInstance().getConfig().getStringList("blocked-worlds"));
        String to = e.getTo().getWorld().getName();
        String from = e.getFrom().getWorld().getName();
        Player recipient = e.getPlayer();
        
        if (worlds.contains(from) && !worlds.contains(to)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    FileConfiguration outgoing = outgoingConfig.getConfig();
                    if (recipient.isOnline() && outgoing.getList(recipient.getUniqueId().toString()) != null
                            && outgoing.getList(recipient.getUniqueId().toString()).size() > 0)
                        spawnCourier(recipient);
                }
            }.runTaskLater(CourierNew.getInstance(), CourierNew.getInstance().getConfig().getLong("resend-from-blocked-delay"));
        }
    }
    
    /**
     * If a player is changing from a blocked gamemode, they will retrieve their mail
     */
    @EventHandler
    public void onGamemode(PlayerGameModeChangeEvent e) {
        List<String> modes = new ArrayList<>(CourierNew.getInstance().getConfig().getStringList("blocked-gamemodes"));
        GameMode to = e.getNewGameMode();
        GameMode from = e.getPlayer().getGameMode();
        Player recipient = e.getPlayer();
        if (modes.contains(from.toString()) && !modes.contains(to.toString())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    FileConfiguration outgoing = outgoingConfig.getConfig();
                    if (recipient.isOnline() && outgoing.getList(recipient.getUniqueId().toString()) != null
                            && outgoing.getList(recipient.getUniqueId().toString()).size() > 0)
                        spawnCourier(recipient);
                }
            }.runTaskLater(CourierNew.getInstance(), CourierNew.getInstance().getConfig().getLong("resend-from-blocked-delay"));
        }
    }
    
    /**
     * prevent villager couriers from changing profession
     */
    @EventHandler
    public void onVillagerProfession(VillagerCareerChangeEvent e) {
        if (CourierChecker.isCourier(e.getEntity())) e.setCancelled(true);
    }
}
