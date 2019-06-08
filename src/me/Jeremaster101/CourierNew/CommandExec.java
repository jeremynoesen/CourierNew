package me.Jeremaster101.CourierNew;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommandExec implements CommandExecutor {

    private PostLetter pl = new PostLetter();
    private LetterCreation lc = new LetterCreation();
    private IsLetter il = new IsLetter();
    private Message msg = new Message();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            List<String> modes = new ArrayList<>(Main.plugin.getConfig().getStringList("blocked-gamemodes"));
            List<String> worlds = new ArrayList<>(Main.plugin.getConfig().getStringList("blocked-worlds"));
            if (!worlds.contains(player.getWorld().getName()) && !modes.contains(player.getGameMode().toString())) {
                if (label.equalsIgnoreCase("letter")) {
                    if (player.hasPermission("couriernew.letter")) {
                        if (args.length >= 1) {
                            StringBuilder sb = new StringBuilder();
                            for (String arg : args) {
                                sb.append(arg).append(" ");
                            }
                            if (il.isHoldingOwnLetter(player)) lc.edit(player, sb.toString());
                            else lc.write(player, sb.toString());
                        } else player.sendMessage(msg.ERROR_NO_MSG);
                    } else
                        player.sendMessage(msg.ERROR_NO_PERMS);
                } else if (label.equalsIgnoreCase("courierreload")) {
                    if (player.hasPermission("couriernew.reload")) {
                        Main.plugin.reloadConfig();
                        player.sendMessage(msg.SUCCESS_RELOADED);
                    } else
                        player.sendMessage(msg.ERROR_NO_PERMS);

                } else if (label.equalsIgnoreCase("post")) {

                    if (args.length == 1) {
                        if (player.hasPermission("couriernew.post")) {
                            pl.send(player, args[0]);
                        } else
                            player.sendMessage(msg.ERROR_NO_PERMS);
                    } else
                        player.sendMessage(msg.ERROR_TOO_MANY_ARGS);

                } else if (label.equalsIgnoreCase("courier")) {

                    if (player.hasPermission("couriernew.reload")) {
                        player.sendMessage(msg.OP_HELP);
                    } else if (player.hasPermission("couriernew.courier")) {
                        player.sendMessage(msg.HELP);
                    } else
                        player.sendMessage(msg.ERROR_NO_PERMS);
                } else if (label.equalsIgnoreCase("shred")) {

                    if (player.hasPermission("couriernew.shred")) {
                        lc.delete(player);
                    } else
                        player.sendMessage(msg.ERROR_NO_PERMS);

                } else if (label.equalsIgnoreCase("shredall")) {

                    if (player.hasPermission("couriernew.shredall")) {
                        lc.deleteAll(player);
                    } else
                        player.sendMessage(msg.ERROR_NO_PERMS);

                } else if (label.equalsIgnoreCase("unread") || label.equalsIgnoreCase("postman")) {
                    if (player.hasPermission("couriernew.unread")) {
                        File outgoingyml = new File(Main.plugin.getDataFolder(), "outgoing.yml");
                        FileConfiguration outgoing = YamlConfiguration.loadConfiguration(outgoingyml);

                        if (outgoing.getList(player.getUniqueId().toString()) != null && outgoing.getList(player.getUniqueId().toString()).size() > 0) {
                            player.sendMessage(msg.SUCCESS_EXTRA_DELIVERIES);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (player.isOnline())
                                        pl.spawnPostman(player);
                                }
                            }.runTaskLater(Main.plugin, Main.plugin.getConfig().getLong("unread-delay"));
                        } else {
                            player.sendMessage(msg.ERROR_NO_MAIL);
                        }
                    } else
                        player.sendMessage(msg.ERROR_NO_PERMS);
                }
                return true;
            } else {
                player.sendMessage(msg.ERROR_WORLD);
                return true;
            }
        }
        return false;
    }
}
