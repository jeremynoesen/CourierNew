package me.Jeremaster101.CourierNew;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.map.MapPalette;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

/**
 * All messages used within the plugin
 */
public class Message {
    static String PREFIX = format(getConfig().getString("PREFIX"));
    private static File messageConfigFile = null;
    private static YamlConfiguration config = null;
    static String ERROR_NO_PERMS = PREFIX + format(getConfig().getString("ERROR_NO_PERMS"));
    static String ERROR_SENT_BEFORE = PREFIX + format(getConfig().getString("ERROR_SENT_BEFORE"));
    static String ERROR_NO_MSG = PREFIX + format(getConfig().getString("ERROR_NO_MSG"));
    static String ERROR_NO_LETTER = PREFIX + format(getConfig().getString("ERROR_NO_LETTER"));
    static String ERROR_NOT_YOUR_LETTER = PREFIX + format(getConfig().getString("ERROR_NOT_YOUR_LETTER"));
    static String SUCCESS_CREATED_HAND = PREFIX + format(getConfig().getString("SUCCESS_CREATED_HAND"));
    static String SUCCESS_CREATED_DROPPED = PREFIX + format(getConfig().getString(
            "SUCCESS_CREATED_DROPPED"));
    static String SUCCESS_CREATED_ADDED = PREFIX + format(getConfig().getString("SUCCESS_CREATED_ADDED"));
    static String SUCCESS_PAGE_ADDED = PREFIX + format(getConfig().getString("SUCCESS_PAGE_ADDED"));
    static String SUCCESS_DELETED = PREFIX + format(getConfig().getString("SUCCESS_DELETED"));
    static String SUCCESS_DELETED_ALL = PREFIX + format(getConfig().getString("SUCCESS_DELETED_ALL"));
    static String ERROR_TOO_MANY_ARGS = PREFIX + format(getConfig().getString("ERROR_TOO_MANY_ARGS"));
    static String SUCCESS_SENT = PREFIX + format(getConfig().getString("SUCCESS_SENT"));
    static String ERROR_PLAYER_NO_EXIST = PREFIX + format(getConfig().getString("ERROR_PLAYER_NO_EXIST"));
    static String SUCCESS_POSTMAN_ARRIVED = PREFIX + format(getConfig().getString(
            "SUCCESS_POSTMAN_ARRIVED"));
    static String SUCCESS_EXTRA_DELIVERIES = PREFIX + format(getConfig().getString(
            "SUCCESS_EXTRA_DELIVERIES"));
    static String ERROR_NO_MAIL = PREFIX + format(getConfig().getString("ERROR_NO_MAIL"));
    static String ERROR_CANT_HOLD = PREFIX + format(getConfig().getString("ERROR_CANT_HOLD"));
    static String ERROR_VANISHED = PREFIX + format(getConfig().getString("ERROR_VANISHED"));
    static String SUCCESS_IGNORED = PREFIX + format(getConfig().getString("SUCCESS_IGNORED"));
    static String SUCCESS_RELOADED = PREFIX + format(getConfig().getString("SUCCESS_RELOADED"));
    static String ERROR_WORLD = PREFIX + format(getConfig().getString("ERROR_WORLD"));
    static String POSTMAN_NAME = format(getConfig().getString("POSTMAN_NAME"));
    static String POSTMAN_NAME_RECEIVED = format(getConfig().getString("POSTMAN_NAME_RECEIVED"));
    static String ERROR_SEND_FAILED = PREFIX + format(getConfig().getString("ERROR_SEND_FAILED"));
    public String STARTUP = "\n\n" +
            ChatColor.DARK_GRAY + "███╗" + ChatColor.GREEN + " ██████╗" + ChatColor.DARK_GREEN + "███╗   ██╗" + ChatColor.DARK_GRAY + "███╗" + ChatColor.WHITE + "  CourierNew version " + CourierNew.plugin.getDescription().getVersion() + " " + "has " + "been enabled!\n" +
            ChatColor.DARK_GRAY + "██╔╝" + ChatColor.GREEN + "██╔════╝" + ChatColor.DARK_GREEN + "████╗  ██║" + ChatColor.DARK_GRAY + "╚██║" + ChatColor.WHITE + "  CourierNew is written by Jeremaster101 and\n" +
            ChatColor.DARK_GRAY + "██║ " + ChatColor.GREEN + "██║     " + ChatColor.DARK_GREEN + "██╔██╗ ██║" + ChatColor.DARK_GRAY + " ██║" + ChatColor.WHITE + "  may not be modified or redistributed without\n" +
            ChatColor.DARK_GRAY + "██║ " + ChatColor.GREEN + "██║     " + ChatColor.DARK_GREEN + "██║╚██╗██║" + ChatColor.DARK_GRAY + " ██║" + ChatColor.WHITE + "  his consent. For help and support, join the\n" +
            ChatColor.DARK_GRAY + "███╗" + ChatColor.GREEN + "╚██████╗" + ChatColor.DARK_GREEN + "██║ ╚████║" + ChatColor.DARK_GRAY + "███║" + ChatColor.WHITE + "  support discord group: https://discord.gg/WhmQYR\n" +
            ChatColor.DARK_GRAY + "╚══╝" + ChatColor.GREEN + " ╚═════╝" + ChatColor.DARK_GREEN + "╚═╝  ╚═══╝" + ChatColor.DARK_GRAY + "╚══╝" + ChatColor.WHITE + "  Thank you for choosing CourierNew!\n";
    String CLEANING = PREFIX + ChatColor.GRAY + "Deleting leftover postman entities...";
    String DONE_CLEANING =
            PREFIX + ChatColor.GRAY + "Successfully deleted " + ChatColor.WHITE + "$COUNT$" + ChatColor.GRAY +
                    " postman entities!";
    String[] HELP = new String[]{
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
    String[] OP_HELP = new String[]{
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
                .replace("&4", "§" + MapPalette.matchColor(170, 0 ,0) + ";")
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
     * Reloads the message file and reassigns the messages to the updated values
     */
    public static void reloadConfig() {
        if (messageConfigFile == null) {
            messageConfigFile = new File(CourierNew.plugin.getDataFolder(), "messages.yml");
        }

        config = YamlConfiguration.loadConfiguration(messageConfigFile);

        Reader defConfigStream = new InputStreamReader(CourierNew.plugin.getResource("messages.yml"),
                StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        config.setDefaults(defConfig);
        config.options().copyDefaults(true);
        saveConfig();

        PREFIX = format(config.getString("PREFIX"));
        ERROR_NO_PERMS = PREFIX + format(config.getString("ERROR_NO_PERMS"));
        ERROR_SENT_BEFORE = PREFIX + format(config.getString("ERROR_SENT_BEFORE"));
        ERROR_NO_MSG = PREFIX + format(config.getString("ERROR_NO_MSG"));
        ERROR_NO_LETTER = PREFIX + format(config.getString("ERROR_NO_LETTER"));
        ERROR_NOT_YOUR_LETTER = PREFIX + format(config.getString("ERROR_NOT_YOUR_LETTER"));
        SUCCESS_CREATED_HAND = PREFIX + format(config.getString("SUCCESS_CREATED_HAND"));
        SUCCESS_CREATED_DROPPED = PREFIX + format(config.getString("SUCCESS_CREATED_DROPPED"));
        SUCCESS_CREATED_ADDED = PREFIX + format(config.getString("SUCCESS_CREATED_ADDED"));
        SUCCESS_PAGE_ADDED = PREFIX + format(config.getString("SUCCESS_PAGE_ADDED"));
        SUCCESS_DELETED = PREFIX + format(config.getString("SUCCESS_DELETED"));
        SUCCESS_DELETED_ALL = PREFIX + format(config.getString("SUCCESS_DELETED_ALL"));
        ERROR_TOO_MANY_ARGS = PREFIX + format(config.getString("ERROR_TOO_MANY_ARGS"));
        SUCCESS_SENT = PREFIX + format(config.getString("SUCCESS_SENT"));
        ERROR_PLAYER_NO_EXIST = PREFIX + format(config.getString("ERROR_PLAYER_NO_EXIST"));
        SUCCESS_POSTMAN_ARRIVED = PREFIX + format(config.getString("SUCCESS_POSTMAN_ARRIVED"));
        SUCCESS_EXTRA_DELIVERIES = PREFIX + format(config.getString("SUCCESS_EXTRA_DELIVERIES"));
        ERROR_NO_MAIL = PREFIX + format(config.getString("ERROR_NO_MAIL"));
        ERROR_CANT_HOLD = PREFIX + format(config.getString("ERROR_CANT_HOLD"));
        ERROR_VANISHED = PREFIX + format(config.getString("ERROR_VANISHED"));
        SUCCESS_IGNORED = PREFIX + format(config.getString("SUCCESS_IGNORED"));
        SUCCESS_RELOADED = PREFIX + format(config.getString("SUCCESS_RELOADED"));
        ERROR_WORLD = PREFIX + format(config.getString("ERROR_WORLD"));
        POSTMAN_NAME = format(config.getString("POSTMAN_NAME"));
        POSTMAN_NAME_RECEIVED = format(config.getString("POSTMAN_NAME_RECEIVED"));
    }

    /**
     * Getter for the message config file
     *
     * @return message config
     */
    public static YamlConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    /**
     * Saves the message config file
     */
    public static void saveConfig() {
        if (config == null || messageConfigFile == null) {
            return;
        }
        try {
            getConfig().save(messageConfigFile);
        } catch (IOException ex) {
            CourierNew.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + messageConfigFile, ex);
        }
    }

    /**
     * Saves default message config when the server loads
     */
    public static void saveDefaultConfig() {
        if (messageConfigFile == null) {
            messageConfigFile = new File(CourierNew.plugin.getDataFolder(), "messages.yml");
        }
        if (!messageConfigFile.exists()) {
            CourierNew.plugin.saveResource("messages.yml", false);
            config = YamlConfiguration.loadConfiguration(messageConfigFile);
        }
    }

    /**
     * Used to remove all minecraft color codes and line breakes from a message
     *
     * @param message message to remove all formatting from
     * @return unformatted message
     */
    String unformat(String message) {
        return message.replace("\\n", " ").replace("&0", "").replace("&1", "").replace("&2", "").replace("&3", "")
                .replace("&4", "").replace("&5", "").replace("&6", "").replace("&7", "").replace("&8", "")
                .replace("&9", "").replace("&a", "").replace("&b", "").replace("&c", "").replace("&d", "")
                .replace("&e", "").replace("&f", "").replace("&k", "").replace("&l", "").replace("&m", "")
                .replace("&n", "").replace("&o", "").replace("&r", "");
    }

}