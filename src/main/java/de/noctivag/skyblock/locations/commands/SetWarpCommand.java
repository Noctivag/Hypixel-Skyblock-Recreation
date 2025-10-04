package de.noctivag.skyblock.locations.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand implements CommandExecutor {
    private final SkyblockPlugin plugin;

    public SetWarpCommand(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("plugin.admin")) {
            player.sendMessage("§cDazu hast du keine Berechtigung!");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§cVerwendung: /setwarp <Name> [Berechtigung] [Beschreibung]");
            return true;
        }

        String warpName = args[0];
        String permission = args.length > 1 ? args[1] : "";

        // Kombiniere alle verbleibenden Argumente als Beschreibung
        StringBuilder description = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            description.append(args[i]).append(" ");
        }

        plugin.getLocationManager().setWarp(warpName, player.getLocation(),
            permission, description.toString().trim());

        player.sendMessage("§aWarp §e" + warpName + " §awurde gesetzt!");
        if (!permission.isEmpty()) {
            player.sendMessage("§7Berechtigung: §e" + permission);
        }
        return true;
    }
}
