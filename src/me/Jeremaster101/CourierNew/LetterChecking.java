package me.Jeremaster101.CourierNew;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

/**
 * Methods to check for letters in a player's inventory
 */
public class LetterChecking {

    /**
     * Test if a player is holding a letter they wrote and have not sent yet.
     *
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
     *
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
     *
     * @param item item to check
     * @return true if item is a letter
     */
    boolean isLetter(ItemStack item) {
        return item != null && item.getType() == Material.WRITTEN_BOOK &&
                ((BookMeta) item.getItemMeta()).getTitle().contains("Letter from ");
    }

    boolean isPostman(Entity en) {
        return en instanceof Villager && ((Villager) en).getInventory().getItem(1) != null &&
                ((Villager) en).getInventory().getItem(1).getItemMeta().getDisplayName() != null &&
                ((Villager) en).getInventory().getItem(1).getItemMeta().getDisplayName().equals("POSTMAN");
    }

    boolean isPlayersPostman(Player p, Entity en) {
        return en instanceof Villager && ((Villager) en).getInventory().getItem(0) != null &&
                ((Villager) en).getInventory().getItem(0).getItemMeta().getDisplayName() != null &&
                ((Villager) en).getInventory().getItem(0).getItemMeta().getDisplayName().equals(p.getUniqueId().toString()) &&
                ((Villager) en).getInventory().getItem(1) != null &&
                ((Villager) en).getInventory().getItem(1).getItemMeta().getDisplayName() != null &&
                ((Villager) en).getInventory().getItem(1).getItemMeta().getDisplayName().equals("POSTMAN");
    }

    boolean isOtherPlayersPostman(Player p, Entity en) {
        return en instanceof Villager && ((Villager) en).getInventory().getItem(0) != null &&
                ((Villager) en).getInventory().getItem(0).getItemMeta().getDisplayName() != null &&
                !((Villager) en).getInventory().getItem(0).getItemMeta().getDisplayName().equals(p.getUniqueId().toString()) &&
                ((Villager) en).getInventory().getItem(1) != null &&
                ((Villager) en).getInventory().getItem(1).getItemMeta().getDisplayName() != null &&
                ((Villager) en).getInventory().getItem(1).getItemMeta().getDisplayName().equals("POSTMAN");
    }

    boolean isReceivedPostman(Entity en) {
        Message msg = new Message();
        return en instanceof Villager && en.getCustomName() != null && en.getCustomName().equals(msg.POSTMAN_NAME_RECEIVED) &&
                ((Villager) en).getInventory().getItem(1) != null &&
                ((Villager) en).getInventory().getItem(1).getItemMeta().getDisplayName() != null &&
                ((Villager) en).getInventory().getItem(1).getItemMeta().getDisplayName().equals("POSTMAN");
    }
}
