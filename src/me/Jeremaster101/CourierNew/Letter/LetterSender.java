package me.Jeremaster101.CourierNew.Letter;

import me.Jeremaster101.CourierNew.CourierNew;
import me.Jeremaster101.CourierNew.Message;
import me.Jeremaster101.CourierNew.Postman.Postman;
import me.Jeremaster101.CourierNew.Postman.PostmanChecker;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Send the letters to players
 */
public class LetterSender implements Listener {
    
    private LetterChecker lc = new LetterChecker();
    private PostmanChecker pc = new PostmanChecker();
    private Message msg = new Message();
    
    
    /**
     * Send a letter to a player. The letter will have the recipient added to the lore, preventing it from being sent
     * again. It also adds it to a yml file of letters to be recieved. If the player recieving is online, they may
     * recieve their letter.
     *
     * @param sender    player sending the letter
     * @param recipient player(s) to recieve the letter
     */
    @SuppressWarnings("deprecation")
    public void send(Player sender, String recipient) {
        if (lc.isHoldingOwnLetter(sender) && !lc.wasSent(sender.getInventory().getItemInMainHand())) {
            File outgoingyml = new File(CourierNew.plugin.getDataFolder(), "outgoing.yml");
            FileConfiguration outgoing = YamlConfiguration.loadConfiguration(outgoingyml);
            ItemStack letter = sender.getInventory().getItemInMainHand();
            
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
                        
                        try {
                            outgoing.save(outgoingyml);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (recplayer.isOnline()) {
                                    spawnPostman((Player) recplayer);
                                }
                            }
                        }.runTaskLater(CourierNew.plugin, CourierNew.plugin.getConfig().getLong("send-recieve-delay"));
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
                        
                        try {
                            outgoing.save(outgoingyml);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (recplayer.isOnline()) {
                                    spawnPostman((Player) recplayer);
                                }
                            }
                        }.runTaskLater(CourierNew.plugin, CourierNew.plugin.getConfig().getLong("send-recieve-delay"));
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
                        
                        try {
                            outgoing.save(outgoingyml);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (recplayer.isOnline()) {
                                    spawnPostman((Player) recplayer);
                                }
                            }
                        }.runTaskLater(CourierNew.plugin, CourierNew.plugin.getConfig().getLong("send-recieve-delay"));
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
                    
                    try {
                        outgoing.save(outgoingyml);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                    sender.getInventory().getItemInMainHand().setAmount(0);
                    sender.sendMessage(Message.SUCCESS_SENT.replace("$PLAYER$", recplayer.getName()));
                    
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (recplayer.isOnline()) {
                                spawnPostman((Player) recplayer);
                            }
                        }
                    }.runTaskLater(CourierNew.plugin, CourierNew.plugin.getConfig().getLong("send-recieve-delay"));
                } else sender.sendMessage(Message.ERROR_NO_PERMS);
            }
        } else if (lc.isHoldingOwnLetter(sender)) {
            sender.sendMessage(Message.ERROR_SENT_BEFORE);
        } else if (lc.isHoldingLetter(sender)) {
            sender.sendMessage(Message.ERROR_NOT_YOUR_LETTER);
        } else
            sender.sendMessage(Message.ERROR_NO_LETTER);
    }
    
    /**
     * When clicking the postman, retrieve the letters from the file and give all of them to the player. If they have
     * space in their inventory, give them all, starting with their hand if they aren't holding anything. Letters not
     * taken will be delivered later.
     *
     * @param recipient player recieving the mail
     */
    public void receive(Player recipient) {
        File outgoingyml = new File(CourierNew.plugin.getDataFolder(), "outgoing.yml");
        FileConfiguration outgoing = YamlConfiguration.loadConfiguration(outgoingyml);
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
            
            try {
                outgoing.save(outgoingyml);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
    
    /**
     * Create the postman entity for a player if they are not vanished or in blocked gamemodes or worlds
     *
     * @param recipient player recieving letters
     */
    public void spawnPostman(Player recipient) {
        
        if (pc.canRecieveMail(recipient)) {
            
            Postman postman = new Postman(recipient);
            postman.spawn();
        }
    }
    
    /**
     * Check when a player right clicks their postman entity
     */
    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent e) {
        Entity en = e.getRightClicked();
        if ((en instanceof Villager && !CourierNew.plugin.getConfig().getBoolean("protected-postman")) || pc.isPlayersPostman(e.getPlayer(), en)) {
            en.setCustomName(Message.POSTMAN_NAME_RECEIVED);
            e.setCancelled(true);
            receive(e.getPlayer());
            en.getWorld().playSound(en.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            en.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, en.getLocation().add(0, en.getHeight()/2, 0), 20, en.getWidth()/2, en.getHeight(), en.getWidth()/2);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!en.isDead()) en.remove();
                }
            }.runTaskLater(CourierNew.plugin, CourierNew.plugin.getConfig().getLong("remove-postman-recieved-delay"));
        } else if (pc.isOtherPlayersPostman(e.getPlayer(), en)) {
            e.setCancelled(true);
            en.getWorld().playSound(en.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 1);
        } else if (pc.isReceivedPostman(en)) e.setCancelled(true);
    }
    
    /**
     * Check when a player joins so they can retrieve unread mail
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        File outgoingyml = new File(CourierNew.plugin.getDataFolder(), "outgoing.yml");
        FileConfiguration outgoing = YamlConfiguration.loadConfiguration(outgoingyml);
        
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline() && outgoing.getList(player.getUniqueId().toString()) != null &&
                        outgoing.getList(player.getUniqueId().toString()).size() > 0) {
                    spawnPostman(player);
                }
            }
        }.runTaskLater(CourierNew.plugin, CourierNew.plugin.getConfig().getLong("join-recieve-delay"));
    }
    
    /**
     * If a player is coming from a blocked world, they will retrieve their mail
     */
    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        List<String> worlds = new ArrayList<>(CourierNew.plugin.getConfig().getStringList("blocked-worlds"));
        String to = e.getTo().getWorld().getName();
        String from = e.getFrom().getWorld().getName();
        Player recipient = e.getPlayer();
        
        if (worlds.contains(from) && !worlds.contains(to)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    File outgoingyml = new File(CourierNew.plugin.getDataFolder(), "outgoing.yml");
                    FileConfiguration outgoing = YamlConfiguration.loadConfiguration(outgoingyml);
                    if (recipient.isOnline() && outgoing.getList(recipient.getUniqueId().toString()) != null
                            && outgoing.getList(recipient.getUniqueId().toString()).size() > 0)
                        spawnPostman(recipient);
                }
            }.runTaskLater(CourierNew.plugin, CourierNew.plugin.getConfig().getLong("resend-from-blocked-delay"));
        }
    }
    
    /**
     * If a player is changing from a blocked gamemode, they will retrieve their mail
     */
    @EventHandler
    public void onGamemode(PlayerGameModeChangeEvent e) {
        List<String> modes = new ArrayList<>(CourierNew.plugin.getConfig().getStringList("blocked-gamemodes"));
        GameMode to = e.getNewGameMode();
        GameMode from = e.getPlayer().getGameMode();
        Player recipient = e.getPlayer();
        if (modes.contains(from.toString()) && !modes.contains(to.toString())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    File outgoingyml = new File(CourierNew.plugin.getDataFolder(), "outgoing.yml");
                    FileConfiguration outgoing = YamlConfiguration.loadConfiguration(outgoingyml);
                    if (recipient.isOnline() && outgoing.getList(recipient.getUniqueId().toString()) != null
                            && outgoing.getList(recipient.getUniqueId().toString()).size() > 0)
                        spawnPostman(recipient);
                }
            }.runTaskLater(CourierNew.plugin, CourierNew.plugin.getConfig().getLong("resend-from-blocked-delay"));
        }
    }
    
    @EventHandler
    public void onVillagerProfession(VillagerCareerChangeEvent e) {
        if (pc.isPostman(e.getEntity())) e.setCancelled(true);
    }
    //todo make listener class
}
