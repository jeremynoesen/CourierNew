package me.Jeremaster101.CourierNew;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

/**
 * Methods to check for letters in a player's inventory
 */
public class IsLetter {

    /**
     * Test if a player is holding a letter they wrote and have not sent yet.
     * @param player player holding the letter
     * @return true if player is holding their own unsent letter
     */
    boolean isHoldingOwnLetter(Player player) {
        return player.getInventory().getItemInMainHand() != null &&
                player.getInventory().getItemInMainHand().getType() == Material.WRITTEN_BOOK &&
                ((BookMeta) player.getInventory().getItemInMainHand().getItemMeta()).getAuthor().equals(player.getName()) &&
                ((BookMeta) player.getInventory().getItemInMainHand().getItemMeta()).getTitle().equals("Letter from " + player.getName()) &&
                !player.getInventory().getItemInMainHand().getItemMeta().getLore().get(player.getInventory().getItemInMainHand()
                        .getItemMeta().getLore().size() - 1).contains("To ");
    }

    /**
     * Check if a player is holding a letter in general
     * @param player player holding letter
     * @return true if player is holding a letter
     */
    boolean isHoldingLetter(Player player) {
        return player.getInventory().getItemInMainHand() != null &&
                player.getInventory().getItemInMainHand().getType() == Material.WRITTEN_BOOK &&
                ((BookMeta) player.getInventory().getItemInMainHand().getItemMeta()).getTitle().contains("Letter from ");
    }

    /**
     * Check if item is a letter
     * @param item item to check
     * @return true if item is a letter
     */
    boolean isLetter(ItemStack item) {
        return item != null && item.getType() == Material.WRITTEN_BOOK &&
                ((BookMeta) item.getItemMeta()).getTitle().contains("Letter from ");
    }
}
