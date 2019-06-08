package me.Jeremaster101.CourierNew;

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
 */
class LetterCreation {

    private Message msg = new Message();
    private IsLetter il = new IsLetter();

    /**
     * Create a new letter with a specified message and places it in the player's inventory. Also set's the lore of
     * the item to a preview of the message
     * @param player player writing the letter
     * @param message the message the player is writing to the letter
     */
    void write(Player player, String message) {
        String finalMessage = msg.format(message);

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta bm = (BookMeta) book.getItemMeta();

        bm.setAuthor(player.getName());
        bm.setTitle("Letter from " + player.getName());
        ArrayList<String> pages = new ArrayList<>();
        pages.add(finalMessage);
        bm.setPages(pages);

        StringBuilder line1 = new StringBuilder();
        StringBuilder line2 = new StringBuilder();
        StringBuilder line3 = new StringBuilder();
        int i = 0;
        String plainMessage = msg.unformat(message);
        String[] words = plainMessage.split("");
        for (String letter : words) {
            if (0 <= i && i <= 29) {
                line1.append(letter);
            } else if (30 <= i && i <= 59) {
                line2.append(letter);
            } else if (60 <= i && i <= 89) {
                line3.append(letter);
            }
            i++;
        }



        ArrayList<String> lore = new ArrayList<>();
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss MM/dd/yy");
        String dateNow = formatter.format(currentDate.getTime());
        lore.add(line1.toString());
        if (!line2.toString().equals("")) lore.add(line2.toString());
        if (!line3.toString().equals("")) lore.add(line3.toString());
        lore.add(ChatColor.DARK_GRAY + dateNow + " (" + bm.getPages().size() + ")");
        bm.setLore(lore);
        book.setItemMeta(bm);

        if (player.getInventory().firstEmpty() < 0) {
            player.getWorld().dropItemNaturally(player.getEyeLocation(), book);
            player.sendMessage(msg.SUCCESS_CREATED_DROPPED);
        } else if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            player.getInventory().setItemInMainHand(book);
            player.sendMessage(msg.SUCCESS_CREATED_HAND);
        } else {
            player.getInventory().addItem(book);
            player.sendMessage(msg.SUCCESS_CREATED_ADDED);
        }
    }

    /**
     * Add a new page to an existing letter that the player is writing. Adds page count to lore
     * @param player player editing the letter
     * @param message message player is adding to the letter
     */
    void edit(Player player, String message) {
        String finalMessage = msg.format(message);

        ItemStack writtenBook = player.getInventory().getItemInMainHand();
        BookMeta wbm = (BookMeta) writtenBook.getItemMeta();

        ArrayList<String> lore = new ArrayList<>(wbm.getLore());
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss MM/dd/yy");
        String dateNow = formatter.format(currentDate.getTime());
        lore.set((wbm.getLore().size() - 1), ChatColor.DARK_GRAY + dateNow + " (" + (wbm.getPages().size() + 1) + ")");
        wbm.setLore(lore);

        ArrayList<String> allPages = new ArrayList<>(wbm.getPages());
        allPages.add(finalMessage);
        wbm.setPages(allPages);
        writtenBook.setItemMeta(wbm);

        player.getInventory().setItemInMainHand(writtenBook);
        player.sendMessage(msg.SUCCESS_PAGE_ADDED);
    }

    /**
     * Delete a letter in the player's hand
     * @param player player deleting the letter in their hand
     */
    void delete(Player player) {
        if (il.isHoldingLetter(player)) {
            player.getInventory().getItemInMainHand().setAmount(0);
            player.sendMessage(msg.SUCCESS_DELETED);
        } else
            player.sendMessage(msg.ERROR_NO_LETTER);
    }

    /**
     * Delete all letters in the player's inventory
     * @param player player deleting the letters in their inventory
     */
    void deleteAll(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (il.isLetter(item)) item.setAmount(0);
        }
        player.sendMessage(msg.SUCCESS_DELETED_ALL);
    }
}
