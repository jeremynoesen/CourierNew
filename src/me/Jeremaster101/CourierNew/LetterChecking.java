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
                ((BookMeta) player.getInventory().getItemInMainHand().getItemMeta()).getTitle().equals("Letter from " + player.getName());
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

    boolean wasSent(ItemStack item) {
        return item.getType() == Material.WRITTEN_BOOK && item.getItemMeta().getLore() != null &&
                item.getItemMeta().getLore().toString().contains("To ");
    }

    /**
     * @param en entity to check
     * @return true if entity is a postman
     */
    boolean isPostman(Entity en) {
        return en instanceof Villager && ((Villager) en).getInventory().getItem(1) != null &&
                ((Villager) en).getInventory().getItem(1).getItemMeta().getDisplayName() != null &&
                ((Villager) en).getInventory().getItem(1).getItemMeta().getDisplayName().equals("POSTMAN");
    }

    /**
     * @param p player to check ownership of the postman
     * @param en entity to check
     * @return true if entity is a postman for player p
     */
    boolean isPlayersPostman(Player p, Entity en) {
        return en instanceof Villager && ((Villager) en).getInventory().getItem(0) != null &&
                ((Villager) en).getInventory().getItem(0).getItemMeta().getDisplayName() != null &&
                ((Villager) en).getInventory().getItem(0).getItemMeta().getDisplayName().equals(p.getUniqueId().toString()) &&
                ((Villager) en).getInventory().getItem(1) != null &&
                ((Villager) en).getInventory().getItem(1).getItemMeta().getDisplayName() != null &&
                ((Villager) en).getInventory().getItem(1).getItemMeta().getDisplayName().equals("POSTMAN");
    }

    /**
     * @param p player to check owndership of the postman
     * @param en entity to check
     * @return true if entity is a postman but not for player p
     */
    boolean isOtherPlayersPostman(Player p, Entity en) {
        return en instanceof Villager && ((Villager) en).getInventory().getItem(0) != null &&
                ((Villager) en).getInventory().getItem(0).getItemMeta().getDisplayName() != null &&
                !((Villager) en).getInventory().getItem(0).getItemMeta().getDisplayName().equals(p.getUniqueId().toString()) &&
                ((Villager) en).getInventory().getItem(1) != null &&
                ((Villager) en).getInventory().getItem(1).getItemMeta().getDisplayName() != null &&
                ((Villager) en).getInventory().getItem(1).getItemMeta().getDisplayName().equals("POSTMAN");
    }

    /**
     * @param en entity to check
     * @return true if entity isa postman who already delivered mail
     */
    boolean isReceivedPostman(Entity en) {
        Message msg = new Message();
        return en instanceof Villager && en.getCustomName() != null && en.getCustomName().equals(Message.POSTMAN_NAME_RECEIVED) &&
                ((Villager) en).getInventory().getItem(1) != null &&
                ((Villager) en).getInventory().getItem(1).getItemMeta().getDisplayName() != null &&
                ((Villager) en).getInventory().getItem(1).getItemMeta().getDisplayName().equals("POSTMAN");
    }
}
