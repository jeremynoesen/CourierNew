package me.Jeremaster101.CourierNew;

import org.bukkit.ChatColor;
import org.fusesource.jansi.Ansi;

/**
 * All messages used within the plugin
 */
public class Message {

    String ERROR_NO_PERMS = ChatColor.RED + "You do not have permission to do this!";
    String ERROR_SENT_BEFORE = ChatColor.RED + "You can not resend a letter!";
    String ERROR_NO_MSG = ChatColor.RED + "Please write a message!";
    String ERROR_NO_LETTER = ChatColor.RED + "You are not holding a letter!";
    String ERROR_NOT_YOUR_LETTER = ChatColor.RED + "This letter was not written by you!";
    String SUCCESS_CREATED_HAND = ChatColor.GREEN + "Letter created!";
    String SUCCESS_CREATED_DROPPED = ChatColor.GREEN + "Inventory full! Letter was dropped!";
    String SUCCESS_CREATED_ADDED = ChatColor.GREEN + "Letter added to inventory!";
    String SUCCESS_PAGE_ADDED = ChatColor.GREEN + "Successfully added a page!";
    String SUCCESS_DELETED = ChatColor.GREEN + "Letter deleted!";
    String SUCCESS_DELETED_ALL = ChatColor.GREEN + "All letters were deleted from your inventory!";
    String ERROR_TOO_MANY_ARGS = ChatColor.RED + "Too many arguments!";
    String SUCCESS_SENT = ChatColor.GREEN + "Letter sent to $PLAYER$!";
    String ERROR_PLAYER_NO_EXIST = ChatColor.RED + "Player $PLAYER$ does not exist!";
    String POSTMAN_NAME = ChatColor.WHITE + "" + ChatColor.BOLD + "$PLAYER$'s Postman";
    String POSTMAN_NAME_RECEIVED = ChatColor.WHITE + "" + ChatColor.BOLD + "Mail Delivered!";
    String SUCCESS_POSTMAN_ARRIVED = ChatColor.GREEN + "A postman has arrived with your mail!";
    String SUCCESS_EXTRA_DELIVERIES = ChatColor.GREEN + "The postman will make extra deliveries!";
    String ERROR_NO_MAIL = ChatColor.RED + "There is currently no mail!";
    String ERROR_CANT_HOLD = ChatColor.RED + "You do not have enough space to recieve any more mail! The postman will hold on to this mail for now.";
    String CLEANING =
            Ansi.ansi().fg(Ansi.Color.GREEN) + "[CourierNew] Deleting postman entities..." + Ansi.ansi().fg(Ansi.Color.DEFAULT);
    String DONE_CLEANING = Ansi.ansi().fg(Ansi.Color.GREEN) + "[CourierNew] $COUNT$ postman entities have been " +
            "deleted from all worlds!" + Ansi.ansi().fg(Ansi.Color.DEFAULT);
    String ERROR_VANISHED = ChatColor.RED + "A postman tried to deliver mail, but you are vanished!";
    String SUCCESS_IGNORED = ChatColor.RED + "You didn't get your mail in time! The postman will return shortly.";
    String SUCCESS_RELOADED = ChatColor.GREEN + "Config reloaded!";
    String ERROR_WORLD = ChatColor.RED + "You are not allowed to send and recieve letters in this world or gamemode!";

    String[] HELP = new String[]{
            ChatColor.GRAY + "" + ChatColor.BOLD + "CourierNew Help",
            ChatColor.GRAY + "----------------------------------------",
            ChatColor.BOLD + "/letter <message> - " + ChatColor.RESET + "Write a letter or add a page to a letter",
            ChatColor.BOLD + "/post <player> - " + ChatColor.RESET + "Send a letter to a player",
            ChatColor.BOLD + "/postman | /unread - " + ChatColor.RESET + "Retrieve unread mail",
            ChatColor.BOLD + "/courier - " + ChatColor.RESET + "Courier help command",
            ChatColor.BOLD + "/shred - " + ChatColor.RESET + "Delete the letter in your hand",
            ChatColor.BOLD + "/shredall - " + ChatColor.RESET + "Delete all letters in your inventory",
            ChatColor.GRAY + "----------------------------------------"
    };

    String[] OP_HELP = new String[]{
            ChatColor.GRAY + "" + ChatColor.BOLD + "CourierNew Help",
            ChatColor.GRAY + "----------------------------------------",
            ChatColor.BOLD + "/letter <message> - " + ChatColor.RESET + "Write a letter or add a page to a letter",
            ChatColor.BOLD + "/post <player> - " + ChatColor.RESET + "Send a letter to a player",
            ChatColor.BOLD + "/postman | /unread - " + ChatColor.RESET + "Retrieve unread mail",
            ChatColor.BOLD + "/courier - " + ChatColor.RESET + "Courier help command",
            ChatColor.BOLD + "/shred - " + ChatColor.RESET + "Delete the letter in your hand",
            ChatColor.BOLD + "/shredall - " + ChatColor.RESET + "Delete all letters in your inventory",
            ChatColor.BOLD + "/courierreload - " + ChatColor.RESET + "Reload config",
            ChatColor.GRAY + "----------------------------------------"
    };

    /**
     * Apply color codes and line breaks to a message
     * @param message message to format with color codes and line breaks
     * @return formatted message
     */
    String format(String message) {
        return message.replace("&0", ChatColor.BLACK + "")
                .replace("&1", ChatColor.DARK_BLUE + "")
                .replace("&2", ChatColor.DARK_GREEN + "")
                .replace("&3", ChatColor.DARK_AQUA + "")
                .replace("&4", ChatColor.DARK_RED + "")
                .replace("&5", ChatColor.DARK_PURPLE + "")
                .replace("&6", ChatColor.GOLD + "")
                .replace("&7", ChatColor.GRAY + "")
                .replace("&8", ChatColor.DARK_GRAY + "")
                .replace("&9", ChatColor.BLUE + "")
                .replace("&a", ChatColor.GREEN + "")
                .replace("&b", ChatColor.AQUA + "")
                .replace("&c", ChatColor.RED + "")
                .replace("&d", ChatColor.LIGHT_PURPLE + "")
                .replace("&e", ChatColor.YELLOW + "")
                .replace("&f", ChatColor.WHITE + "")
                .replace("&r", ChatColor.RESET + "")
                .replace("&l", ChatColor.BOLD + "")
                .replace("&m", ChatColor.STRIKETHROUGH + "")
                .replace("&n", ChatColor.UNDERLINE + "")
                .replace("&o", ChatColor.ITALIC + "")
                .replace("\\n", ChatColor.RESET + "\n");
    }

    /**
     * Used to remove all minecraft color codes and line breakes from a message
     * @param message message to remove all formatting from
     * @return unformatted message
     */
    String unformat(String message) {
        return message.replace("\\n", " ")
                .replace("&0", "").replace("&1", "").replace("&2", "")
                .replace("&3", "").replace("&4", "").replace("&5", "")
                .replace("&6", "").replace("&7", "").replace("&8", "")
                .replace("&9", "").replace("&a", "").replace("&b", "")
                .replace("&c", "").replace("&d", "").replace("&e", "")
                .replace("&f", "").replace("&r", "").replace("&l", "")
                .replace("&m", "").replace("&n", "").replace("&o", "");
    }
}
