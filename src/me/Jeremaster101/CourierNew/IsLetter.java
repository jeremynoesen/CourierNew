package me.Jeremaster101.CourierNew;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class IsLetter {

    boolean isHoldingOwnLetter(Player player) {
        return player.getInventory().getItemInMainHand() != null &&
                player.getInventory().getItemInMainHand().getType() == Material.WRITTEN_BOOK &&
                ((BookMeta) player.getInventory().getItemInMainHand().getItemMeta()).getAuthor().equals(player.getName()) &&
                ((BookMeta) player.getInventory().getItemInMainHand().getItemMeta()).getTitle().equals("Letter from " + player.getName()) &&
                !player.getInventory().getItemInMainHand().getItemMeta().getLore().get(player.getInventory().getItemInMainHand()
                        .getItemMeta().getLore().size() - 1).contains("To ");
    }

    boolean isHoldingLetter(Player player) {
        return player.getInventory().getItemInMainHand() != null &&
                player.getInventory().getItemInMainHand().getType() == Material.WRITTEN_BOOK &&
                ((BookMeta) player.getInventory().getItemInMainHand().getItemMeta()).getTitle().contains("Letter from ");
    }

    boolean isLetter(ItemStack item) {
        return item != null && item.getType() == Material.WRITTEN_BOOK &&
                ((BookMeta) item.getItemMeta()).getTitle().contains("Letter from ");
    }
}
