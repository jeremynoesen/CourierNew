package jndev.couriernew;

import jndev.couriernew.config.Config;
import jndev.couriernew.config.ConfigType;
import jndev.couriernew.config.Configs;
import org.bukkit.ChatColor;
import org.bukkit.map.MapPalette;

/**
 * All messages used within the getInstance()
 */
public class Message {
    
    private static final Config config = Configs.getConfig(ConfigType.MESSAGE);
    
    public static String PREFIX;
    public static String ERROR_NO_PERMS;
    public static String ERROR_SENT_BEFORE;
    public static String ERROR_NO_MSG;
    public static String ERROR_NO_LETTER;
    public static String ERROR_NOT_YOUR_LETTER;
    public static String SUCCESS_CREATED_HAND;
    public static String SUCCESS_CREATED_DROPPED;
    public static String SUCCESS_CREATED_ADDED;
    public static String SUCCESS_PAGE_ADDED;
    public static String SUCCESS_DELETED;
    public static String SUCCESS_DELETED_ALL;
    public static String ERROR_TOO_MANY_ARGS;
    public static String SUCCESS_SENT;
    public static String ERROR_PLAYER_NO_EXIST;
    public static String SUCCESS_POSTMAN_ARRIVED;
    public static String SUCCESS_EXTRA_DELIVERIES;
    public static String ERROR_NO_MAIL;
    public static String ERROR_CANT_HOLD;
    public static String ERROR_VANISHED;
    public static String SUCCESS_IGNORED;
    public static String SUCCESS_RELOADED;
    public static String ERROR_WORLD;
    public static String POSTMAN_NAME;
    public static String POSTMAN_NAME_RECEIVED;
    public static String ERROR_SEND_FAILED;
    public static String SUCCESS_PAGE_EDITED;
    public static String LETTER_FROM;
    public static String STARTUP = "\n\n" +
            ChatColor.DARK_GRAY + "███╗" + ChatColor.GREEN + " ██████╗" + ChatColor.DARK_GREEN + "███╗   ██╗" + ChatColor.DARK_GRAY + "███╗" + ChatColor.WHITE + "  CourierNew version " + CourierNew.getInstance().getDescription().getVersion() + " " + "has " + "been enabled!\n" +
            ChatColor.DARK_GRAY + "██╔╝" + ChatColor.GREEN + "██╔════╝" + ChatColor.DARK_GREEN + "████╗  ██║" + ChatColor.DARK_GRAY + "╚██║" + ChatColor.WHITE + "  CourierNew is written by Jeremaster101 and\n" +
            ChatColor.DARK_GRAY + "██║ " + ChatColor.GREEN + "██║     " + ChatColor.DARK_GREEN + "██╔██╗ ██║" + ChatColor.DARK_GRAY + " ██║" + ChatColor.WHITE + "  may not be modified or redistributed without\n" +
            ChatColor.DARK_GRAY + "██║ " + ChatColor.GREEN + "██║     " + ChatColor.DARK_GREEN + "██║╚██╗██║" + ChatColor.DARK_GRAY + " ██║" + ChatColor.WHITE + "  his consent. For help and support, join the\n" +
            ChatColor.DARK_GRAY + "███╗" + ChatColor.GREEN + "╚██████╗" + ChatColor.DARK_GREEN + "██║ ╚████║" + ChatColor.DARK_GRAY + "███║" + ChatColor.WHITE + "  support discord group: https://discord.gg/WhmQYR\n" +
            ChatColor.DARK_GRAY + "╚══╝" + ChatColor.GREEN + " ╚═════╝" + ChatColor.DARK_GREEN + "╚═╝  ╚═══╝" + ChatColor.DARK_GRAY + "╚══╝" + ChatColor.WHITE + "  Thank you for choosing CourierNew!\n";
    public static String CLEANING = PREFIX + ChatColor.GRAY + "Deleting leftover courier entities...";
    public static String DONE_CLEANING =
            PREFIX + ChatColor.GRAY + "Successfully deleted " + ChatColor.WHITE + "$COUNT$" + ChatColor.GRAY +
                    " courier entities!";
    public static String[] HELP = {
            "",
            format("\n&8&l---------[&a&lCourier&2&lNew &7&lHelp&8&l]---------"),
            ChatColor.GRAY + "/letter <message>" + ChatColor.WHITE + ": Write or edit a letter",
            ChatColor.GRAY + "/post <player>" + ChatColor.WHITE + ": Send a letter to a player",
            ChatColor.GRAY + "/unread" + ChatColor.WHITE + ": Retrieve unread letters",
            ChatColor.GRAY + "/cnhelp" + ChatColor.WHITE + ": List all CourierNew commands",
            ChatColor.GRAY + "/shred" + ChatColor.WHITE + ": Delete the letter in your hand",
            ChatColor.GRAY + "/shredall" + ChatColor.WHITE + ": Delete letters in your inventory",
            ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "----------------------------------",
            ""
    };
    public static String[] OP_HELP = {
            "",
            format("\n&8&l---------[&a&lCourier&2&lNew &7&lHelp&8&l]---------"),
            ChatColor.GRAY + "/letter <message>" + ChatColor.WHITE + ": Write or edit a letter",
            ChatColor.GRAY + "/post <player>" + ChatColor.WHITE + ": Send a letter to a player",
            ChatColor.GRAY + "/unread" + ChatColor.WHITE + ": Retrieve unread letters",
            ChatColor.GRAY + "/cnhelp" + ChatColor.WHITE + ": List all CourierNew commands",
            ChatColor.GRAY + "/shred" + ChatColor.WHITE + ": Delete the letter in your hand",
            ChatColor.GRAY + "/shredall" + ChatColor.WHITE + ": Delete letters in your inventory",
            ChatColor.GRAY + "/cnreload" + ChatColor.WHITE + ": Reload config and messages",
            ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "----------------------------------",
            ""
    };
    
    /**
     * Reloads all plugin messages
     */
    public static void reloadMessages() {
        PREFIX = format(config.getConfig().getString("PREFIX"));
        ERROR_NO_PERMS = PREFIX + format(config.getConfig().getString("ERROR_NO_PERMS"));
        ERROR_SENT_BEFORE = PREFIX + format(config.getConfig().getString("ERROR_SENT_BEFORE"));
        ERROR_NO_MSG = PREFIX + format(config.getConfig().getString("ERROR_NO_MSG"));
        ERROR_NO_LETTER = PREFIX + format(config.getConfig().getString("ERROR_NO_LETTER"));
        ERROR_NOT_YOUR_LETTER = PREFIX + format(config.getConfig().getString("ERROR_NOT_YOUR_LETTER"));
        SUCCESS_CREATED_HAND = PREFIX + format(config.getConfig().getString("SUCCESS_CREATED_HAND"));
        SUCCESS_CREATED_DROPPED = PREFIX + format(config.getConfig().getString("SUCCESS_CREATED_DROPPED"));
        SUCCESS_CREATED_ADDED = PREFIX + format(config.getConfig().getString("SUCCESS_CREATED_ADDED"));
        SUCCESS_PAGE_ADDED = PREFIX + format(config.getConfig().getString("SUCCESS_PAGE_ADDED"));
        SUCCESS_DELETED = PREFIX + format(config.getConfig().getString("SUCCESS_DELETED"));
        SUCCESS_DELETED_ALL = PREFIX + format(config.getConfig().getString("SUCCESS_DELETED_ALL"));
        ERROR_TOO_MANY_ARGS = PREFIX + format(config.getConfig().getString("ERROR_TOO_MANY_ARGS"));
        SUCCESS_SENT = PREFIX + format(config.getConfig().getString("SUCCESS_SENT"));
        ERROR_PLAYER_NO_EXIST = PREFIX + format(config.getConfig().getString("ERROR_PLAYER_NO_EXIST"));
        SUCCESS_POSTMAN_ARRIVED = PREFIX + format(config.getConfig().getString("SUCCESS_POSTMAN_ARRIVED"));
        SUCCESS_EXTRA_DELIVERIES = PREFIX + format(config.getConfig().getString("SUCCESS_EXTRA_DELIVERIES"));
        ERROR_NO_MAIL = PREFIX + format(config.getConfig().getString("ERROR_NO_MAIL"));
        ERROR_CANT_HOLD = PREFIX + format(config.getConfig().getString("ERROR_CANT_HOLD"));
        ERROR_VANISHED = PREFIX + format(config.getConfig().getString("ERROR_VANISHED"));
        SUCCESS_IGNORED = PREFIX + format(config.getConfig().getString("SUCCESS_IGNORED"));
        SUCCESS_RELOADED = PREFIX + format(config.getConfig().getString("SUCCESS_RELOADED"));
        ERROR_WORLD = PREFIX + format(config.getConfig().getString("ERROR_WORLD"));
        POSTMAN_NAME = format(config.getConfig().getString("POSTMAN_NAME"));
        POSTMAN_NAME_RECEIVED = format(config.getConfig().getString("POSTMAN_NAME_RECEIVED"));
        LETTER_FROM = format(config.getConfig().getString("LETTER_FROM"));
        SUCCESS_PAGE_EDITED = PREFIX + format(config.getConfig().getString("SUCCESS_PAGE_EDITED"));
    }
    
    
    /**
     * Apply color codes and line breaks to a message
     *
     * @param msg message to format with color codes and line breaks
     * @return formatted message
     */
    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg.replace("\\n", "\n"));
    }
    
    /**
     * Apply color codes and line breaks to a message for maps
     *
     * @param msg message to format with color codes and line breaks
     * @return formatted message
     */
    @SuppressWarnings("deprecation")
    public static String formatMap(String msg) {
        return msg.replace("\\n", "\n")
                .replace("&0", "§" + MapPalette.matchColor(0, 0, 0) + ";")
                .replace("&1", "§" + MapPalette.matchColor(0, 0, 170) + ";")
                .replace("&2", "§" + MapPalette.matchColor(0, 170, 0) + ";")
                .replace("&3", "§" + MapPalette.matchColor(0, 170, 170) + ";")
                .replace("&4", "§" + MapPalette.matchColor(170, 0, 0) + ";")
                .replace("&5", "§" + MapPalette.matchColor(170, 0, 170) + ";")
                .replace("&6", "§" + MapPalette.matchColor(255, 170, 0) + ";")
                .replace("&7", "§" + MapPalette.matchColor(170, 170, 170) + ";")
                .replace("&8", "§" + MapPalette.matchColor(85, 85, 85) + ";")
                .replace("&9", "§" + MapPalette.matchColor(85, 85, 255) + ";")
                .replace("&a", "§" + MapPalette.matchColor(85, 255, 85) + ";")
                .replace("&b", "§" + MapPalette.matchColor(85, 255, 255) + ";")
                .replace("&c", "§" + MapPalette.matchColor(255, 85, 85) + ";")
                .replace("&d", "§" + MapPalette.matchColor(255, 85, 255) + ";")
                .replace("&e", "§" + MapPalette.matchColor(255, 255, 85) + ";")
                .replace("&f", "§" + MapPalette.matchColor(255, 255, 255) + ";")
                .replace("&k", "\u00A7k")
                .replace("&l", "\u00A7l")
                .replace("&m", "\u00A7m")
                .replace("&n", "\u00A7n")
                .replace("&o", "\u00A7o")
                .replace("&r", "§" + MapPalette.matchColor(0, 0, 0) + ";");
    }
    
    /**
     * Used to remove all minecraft color codes and line breakes from a message
     *
     * @param message message to remove all formatting from
     * @return unformatted message
     */
    public static String unformat(String message) {
        return message.replace("\\n", " ").replace("&0", "").replace("&1", "").replace("&2", "").replace("&3", "")
                .replace("&4", "").replace("&5", "").replace("&6", "").replace("&7", "").replace("&8", "")
                .replace("&9", "").replace("&a", "").replace("&b", "").replace("&c", "").replace("&d", "")
                .replace("&e", "").replace("&f", "").replace("&k", "").replace("&l", "").replace("&m", "")
                .replace("&n", "").replace("&o", "").replace("&r", "");
    }
    
}