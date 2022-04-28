package xyz.jeremynoesen.couriernew;

import org.bukkit.ChatColor;

/**
 * All messages used within the plugin
 *
 * @author Jeremy Noesen
 */
public class Message {
    
    private static final Config config = Config.getMessageConfig();
    
    public static String PREFIX;
    public static String SUCCESS_CREATED_HAND;
    public static String SUCCESS_CREATED_DROPPED;
    public static String SUCCESS_CREATED_ADDED;
    public static String SUCCESS_PAGE_ADDED;
    public static String SUCCESS_PAGE_EDITED;
    public static String SUCCESS_DELETED;
    public static String SUCCESS_DELETED_ALL;
    public static String SUCCESS_SENT;
    public static String SUCCESS_COURIER_ARRIVED;
    public static String SUCCESS_EXTRA_DELIVERIES;
    public static String SUCCESS_IGNORED;
    public static String SUCCESS_RELOADED;
    public static String ERROR_NO_PERMS;
    public static String ERROR_SENT_BEFORE;
    public static String ERROR_NO_MSG;
    public static String ERROR_NO_LETTER;
    public static String ERROR_NOT_YOUR_LETTER;
    public static String ERROR_UNKNOWN_ARGS;
    public static String ERROR_PLAYER_NO_EXIST;
    public static String ERROR_NO_MAIL;
    public static String ERROR_CANT_HOLD;
    public static String ERROR_VANISHED;
    public static String ERROR_WORLD;
    public static String COURIER_NAME;
    public static String COURIER_NAME_RECEIVED;
    public static String LETTER_FROM;
    
    public static String[] HELP = { //todo permission based help, cn help reload
            "",
            format("\n&8&l---------[&a&lCourier&2&lNew &7&lHelp&8&l]---------"),
            ChatColor.GRAY + "/letter <message>" + ChatColor.WHITE + ": Write or edit a letter",
            ChatColor.GRAY + "/post <player>" + ChatColor.WHITE + ": Send a letter to a player",
            ChatColor.GRAY + "/unread" + ChatColor.WHITE + ": Retrieve unread letters",
            ChatColor.GRAY + "/shred" + ChatColor.WHITE + ": Delete the letter in your hand",
            ChatColor.GRAY + "/shredall" + ChatColor.WHITE + ": Delete letters in your inventory",
            ChatColor.GRAY + "/cnhelp" + ChatColor.WHITE + ": List all CourierNew commands",
            ChatColor.GRAY + "/cnreload" + ChatColor.WHITE + ": Reload config and messages",
            ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "----------------------------------",
            ""
    };
    
    /**
     * Reloads all plugin messages
     */
    public static void reloadMessages() {
        PREFIX = format(config.getConfig().getString("PREFIX"));
        SUCCESS_CREATED_HAND = PREFIX + format(config.getConfig().getString("SUCCESS_CREATED_HAND"));
        SUCCESS_CREATED_DROPPED = PREFIX + format(config.getConfig().getString("SUCCESS_CREATED_DROPPED"));
        SUCCESS_CREATED_ADDED = PREFIX + format(config.getConfig().getString("SUCCESS_CREATED_ADDED"));
        SUCCESS_PAGE_ADDED = PREFIX + format(config.getConfig().getString("SUCCESS_PAGE_ADDED"));
        SUCCESS_PAGE_EDITED = PREFIX + format(config.getConfig().getString("SUCCESS_PAGE_EDITED"));
        SUCCESS_DELETED = PREFIX + format(config.getConfig().getString("SUCCESS_DELETED"));
        SUCCESS_DELETED_ALL = PREFIX + format(config.getConfig().getString("SUCCESS_DELETED_ALL"));
        SUCCESS_SENT = PREFIX + format(config.getConfig().getString("SUCCESS_SENT"));
        SUCCESS_COURIER_ARRIVED = PREFIX + format(config.getConfig().getString("SUCCESS_COURIER_ARRIVED"));
        SUCCESS_EXTRA_DELIVERIES = PREFIX + format(config.getConfig().getString("SUCCESS_EXTRA_DELIVERIES"));
        SUCCESS_IGNORED = PREFIX + format(config.getConfig().getString("SUCCESS_IGNORED"));
        SUCCESS_RELOADED = PREFIX + format(config.getConfig().getString("SUCCESS_RELOADED"));
        ERROR_NO_PERMS = PREFIX + format(config.getConfig().getString("ERROR_NO_PERMS"));
        ERROR_SENT_BEFORE = PREFIX + format(config.getConfig().getString("ERROR_SENT_BEFORE"));
        ERROR_NO_MSG = PREFIX + format(config.getConfig().getString("ERROR_NO_MSG"));
        ERROR_NO_LETTER = PREFIX + format(config.getConfig().getString("ERROR_NO_LETTER"));
        ERROR_NOT_YOUR_LETTER = PREFIX + format(config.getConfig().getString("ERROR_NOT_YOUR_LETTER"));
        ERROR_UNKNOWN_ARGS = PREFIX + format(config.getConfig().getString("ERROR_UNKNOWN_ARGS"));
        ERROR_PLAYER_NO_EXIST = PREFIX + format(config.getConfig().getString("ERROR_PLAYER_NO_EXIST"));
        ERROR_NO_MAIL = PREFIX + format(config.getConfig().getString("ERROR_NO_MAIL"));
        ERROR_CANT_HOLD = PREFIX + format(config.getConfig().getString("ERROR_CANT_HOLD"));
        ERROR_VANISHED = PREFIX + format(config.getConfig().getString("ERROR_VANISHED"));
        ERROR_WORLD = PREFIX + format(config.getConfig().getString("ERROR_WORLD"));
        COURIER_NAME = format(config.getConfig().getString("COURIER_NAME"));
        COURIER_NAME_RECEIVED = format(config.getConfig().getString("COURIER_NAME_RECEIVED"));
        LETTER_FROM = format(config.getConfig().getString("LETTER_FROM"));
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
     * Used to remove all minecraft color codes and line breaks from a message
     *
     * @param message message to remove all formatting from
     * @return unformatted message
     */
    public static String unformat(String message) {
        return message.replace("\\n", " ").replace("&0", "")
                .replace("&1", "").replace("&2", "")
                .replace("&3", "").replace("&4", "")
                .replace("&5", "").replace("&6", "")
                .replace("&7", "").replace("&8", "")
                .replace("&9", "").replace("&a", "")
                .replace("&b", "").replace("&c", "")
                .replace("&d", "").replace("&e", "")
                .replace("&f", "").replace("&k", "")
                .replace("&l", "").replace("&m", "")
                .replace("&n", "").replace("&o", "")
                .replace("&r", "");
    }
    
}