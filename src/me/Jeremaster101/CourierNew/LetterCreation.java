package me.Jeremaster101.CourierNew;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Methods to create, edit, and delete letters
 */
class LetterCreation {

    private Message msg = new Message();
    private LetterChecking il = new LetterChecking();

    /*void writeMap(Player player, String message) {
        String formattedMessage = Message.formatMap(message);
        ItemStack map = new ItemStack(Material.FILLED_MAP, 1);
        MapMeta mapMeta = (MapMeta) map.getItemMeta();
        MapView mv = Bukkit.createMap(player.getWorld());
        mapMeta.setMapView(mv);
        for (MapRenderer mr : mapMeta.getMapView().getRenderers()) mv.removeRenderer(mr);
        mapMeta.setColor(Color.BLACK);
        mapMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
        mapMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        try {
            Image img = ImageIO.read(new File(CourierNew.plugin.getDataFolder(), "paper" + ".png"));
            mv.addRenderer(new MapRenderer() {
                @Override
                public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
                    mapCanvas.drawImage(0, 0, img);
                    mapCanvas.drawText(5, 5, MinecraftFont.Font, formattedMessage);
                }
            });
            map.setItemMeta(mapMeta);
            player.getInventory().addItem(map);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //todo text wrapping
        //todo save text to file along with map ID
        //todo redraw maps on server start
    }*/

    /**
     * Create a new letter with a specified message and places it in the player's inventory. Also set's the lore of
     * the item to a preview of the message
     *
     * @param player  player writing the letter
     * @param message the message the player is writing to the letter
     */
    void writeBook(Player player, String message) {
        String finalMessage = Message.format(message);

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta bm = (BookMeta) book.getItemMeta();

        bm.setAuthor(player.getName());
        bm.setTitle("Letter from " + player.getName()); //todo allow customizing of title in message config
        ArrayList<String> pages = new ArrayList<>();
        //String colorWrapped = WordUtils.wrap(finalMessage, 246, "<split>", true);//todo make \n include in char count
        //String[] multiPages = colorWrapped.split("<split>");
        //pages.addAll(Arrays.asList(multiPages));
        pages.add(finalMessage);
        bm.setPages(pages);

        String plainMessage = msg.unformat(message);

        String wrapped = WordUtils.wrap(plainMessage, 30, "<split>", true);
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
    void editBook(Player player, String message) {
        String finalMessage = Message.format(message);

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
        player.sendMessage(Message.SUCCESS_PAGE_ADDED);
    }

    /**
     * Delete a letter in the player's hand
     *
     * @param player player deleting the letter in their hand
     */
    void delete(Player player) {
        if (il.isHoldingLetter(player)) {
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
    void deleteAll(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (il.isLetter(item)) item.setAmount(0);
        }
        player.sendMessage(Message.SUCCESS_DELETED_ALL);
    }
}
