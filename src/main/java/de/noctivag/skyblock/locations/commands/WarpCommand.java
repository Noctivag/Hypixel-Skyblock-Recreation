package de.noctivag.skyblock.locations.commands;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.locations.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {
    private final SkyblockPlugin SkyblockPlugin;

    public WarpCommand(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
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
            Object locationManager = SkyblockPlugin.getLocationManager();
            java.util.List<String> warps = new java.util.ArrayList<>();
            try {
                warps = (java.util.List<String>) locationManager.getClass().getMethod("getWarpNames").invoke(locationManager);
            } catch (Exception e) {
                // If method doesn't exist, warps will be empty
            }
            if (warps.isEmpty()) {
                player.sendMessage(Component.text("§cEs sind keine Warps verfügbar!"));
            } else {
                StringBuilder message = new StringBuilder("§7Verfügbare Warps:\n");
                for (String warpName : warps) {
                    de.noctivag.skyblock.locations.Warp warp = null;
                    try {
                        warp = (de.noctivag.skyblock.locations.Warp) locationManager.getClass().getMethod("getWarp", String.class).invoke(locationManager, warpName);
                    } catch (Exception e) {
                        // If method doesn't exist, warp will be null
                    }
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
        Object locationManager = SkyblockPlugin.getLocationManager();
        de.noctivag.skyblock.locations.Warp warp = null;
        try {
            warp = (de.noctivag.skyblock.locations.Warp) locationManager.getClass().getMethod("getWarp", String.class).invoke(locationManager, warpName);
        } catch (Exception e) {
            // If method doesn't exist, warp will be null
        }

        if (warp == null) {
            player.sendMessage("§cDer Warp §e" + warpName + " §cexistiert nicht!");
            return true;
        }

        if (!warp.getPermission().isEmpty() && !player.hasPermission(warp.getPermission())) {
            player.sendMessage(Component.text("§cDu hast keine Berechtigung, diesen Warp zu benutzen!"));
            return true;
        }

        player.teleport(warp.getLocation());
        player.sendMessage("§aDu wurdest zum Warp §e" + warpName + " §ateleportiert!");
        return true;
    }
}
