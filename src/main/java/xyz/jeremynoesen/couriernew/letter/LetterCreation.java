package xyz.jeremynoesen.couriernew.letter;

import xyz.jeremynoesen.couriernew.Message;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Methods to create, edit, and delete letters
 *
 * @author Jeremy Noesen
 */
public class LetterCreation {
    
    /**
     * Create a new letter with a specified message and places it in the player's inventory. Also set's the lore of the
     * item to a preview of the message
     *
     * @param player  player writing the letter
     * @param message the message the player is writing to the letter
     */
    public static void writeBook(Player player, String message) {
        String finalMessage = Message.format(message);
        
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta bm = (BookMeta) book.getItemMeta();
        
        bm.setAuthor(player.getName());
        bm.setTitle(Message.LETTER_FROM + player.getName());
        ArrayList<String> pages = new ArrayList<>();
        pages.add(finalMessage);
        bm.setPages(pages);
        
        String plainMessage = Message.unformat(message);
        
        String wrapped = WordUtils.wrap(plainMessage, 30, "<split>", true);
        
        while (wrapped.startsWith("/")) wrapped = wrapped.replaceFirst("/", "");
        
        String[] lines = wrapped.split("<split>");
        
        ArrayList<String> lore = new ArrayList<>();
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss MM/dd/yy");
        String dateNow = formatter.format(currentDate.getTime());
        lore.add(lines[0]);
        if (lines.length >= 2) lore.add(lines[1]);
        if (lines.length >= 3) lore.add(lines[2]);
        lore.add(ChatColor.DARK_GRAY + dateNow + " (" + bm.getPages().size() + ")");
        bm.setLore(lore);
        book.setItemMeta(bm);
        
        if (player.getInventory().firstEmpty() < 0) {
            player.getWorld().dropItemNaturally(player.getEyeLocation(), book);
            player.sendMessage(Message.SUCCESS_CREATED_DROPPED);
        } else if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            player.getInventory().setItemInMainHand(book);
            player.sendMessage(Message.SUCCESS_CREATED_HAND);
        } else {
            player.getInventory().addItem(book);
            player.sendMessage(Message.SUCCESS_CREATED_ADDED);
        }
    }
    
    /**
     * Add a new page to an existing letter that the player is writing. Adds page count to lore
     *
     * @param player  player editing the letter
     * @param message message player is adding to the letter
     */
    public static void editBook(Player player, String message) {
        String finalMessage = Message.format(message);
        
        ItemStack writtenBook = player.getInventory().getItemInMainHand();
        BookMeta wbm = (BookMeta) writtenBook.getItemMeta();
        
        ArrayList<String> lore = new ArrayList<>(wbm.getLore());
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss MM/dd/yy");
        String dateNow = formatter.format(currentDate.getTime());
        
        ArrayList<String> pages = new ArrayList<>(wbm.getPages());
        if (pages.get(pages.size() - 1).length() < 256 && pages.get(pages.size() - 1).length() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(pages.get(pages.size() - 1));
            sb.append(finalMessage);
            pages.set(pages.size() - 1, sb.toString());
            player.sendMessage(Message.SUCCESS_PAGE_EDITED);
        } else {
            pages.add(finalMessage);
            player.sendMessage(Message.SUCCESS_PAGE_ADDED);
            lore.set((wbm.getLore().size() - 1), ChatColor.DARK_GRAY + dateNow + " (" + (wbm.getPages().size() + 1) + ")");
            wbm.setLore(lore);
        }
        wbm.setPages(pages);
        writtenBook.setItemMeta(wbm);
        
        player.getInventory().setItemInMainHand(writtenBook);
    }
    
    /**
     * Delete a letter in the player's hand
     *
     * @param player player deleting the letter in their hand
     */
    public static void delete(Player player) {
        if (LetterChecker.isHoldingLetter(player)) {
            player.getInventory().getItemInMainHand().setAmount(0);
            player.sendMessage(Message.SUCCESS_DELETED);
        } else
            player.sendMessage(Message.ERROR_NO_LETTER);
    }
    
    /**
     * Delete all letters in the player's inventory
     *
     * @param player player deleting the letters in their inventory
     */
    public static void deleteAll(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (LetterChecker.isLetter(item)) item.setAmount(0);
        }
        player.sendMessage(Message.SUCCESS_DELETED_ALL);
    }
}
