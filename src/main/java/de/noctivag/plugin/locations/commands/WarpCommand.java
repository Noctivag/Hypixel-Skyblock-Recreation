package de.noctivag.plugin.locations.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.locations.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {
    private final Plugin plugin;

    public WarpCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // Liste alle verfügbaren Warps auf
            java.util.List<String> warps = plugin.getLocationManager().getWarpNames();
            if (warps.isEmpty()) {
                player.sendMessage("§cEs sind keine Warps verfügbar!");
            } else {
                StringBuilder message = new StringBuilder("§7Verfügbare Warps:\n");
                for (String warpName : warps) {
                    de.noctivag.plugin.locations.Warp warp = plugin.getLocationManager().getWarp(warpName);
                    if (warp.getPermission().isEmpty() || player.hasPermission(warp.getPermission())) {
                        message.append("§e").append(warpName);
                        if (!warp.getDescription().isEmpty()) {
                            message.append(" §8- §7").append(warp.getDescription());
                        }
                        message.append("\n");
                    }
                }
                player.sendMessage(message.toString());
            }
            return true;
        }

        String warpName = args[0];
        de.noctivag.plugin.locations.Warp warp = plugin.getLocationManager().getWarp(warpName);

        if (warp == null) {
            player.sendMessage("§cDer Warp §e" + warpName + " §cexistiert nicht!");
            return true;
        }

        if (!warp.getPermission().isEmpty() && !player.hasPermission(warp.getPermission())) {
            player.sendMessage("§cDu hast keine Berechtigung, diesen Warp zu benutzen!");
            return true;
        }

        player.teleport(warp.getLocation());
        player.sendMessage("§aDu wurdest zum Warp §e" + warpName + " §ateleportiert!");
        return true;
    }
}
