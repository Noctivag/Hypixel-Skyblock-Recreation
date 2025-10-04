package de.noctivag.skyblock.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommands implements CommandExecutor {
    private final SkyblockPlugin plugin;

    public SpawnCommands(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String name = command.getName().toLowerCase();
        if (name.equals("setspawn")) {
            if (!(sender instanceof Player player)) { sender.sendMessage("Players only"); return true; }
            if (!player.hasPermission("basicsplugin.setspawn")) { player.sendMessage("§cKeine Berechtigung."); return true; }
            // TODO: Implement proper TeleportManager interface
            // ((TeleportManager) plugin.getTeleportManager()).setSpawn(player.getLocation());
            player.sendMessage("§aSpawn gesetzt.");
            return true;
        }

        if (name.equals("spawn")) {
            if (!(sender instanceof Player player)) { sender.sendMessage("Players only"); return true; }
            // TODO: Implement proper TeleportManager interface
            // Location spawn = ((TeleportManager) plugin.getTeleportManager()).getSpawn();
            Location spawn = null; // Placeholder
            if (spawn == null) { player.sendMessage("§cSpawn nicht gesetzt."); return true; }
            // ((TeleportManager) plugin.getTeleportManager()).setLastLocation(player, player.getLocation());
            player.teleport(spawn);
            player.sendMessage("§aTeleportiere zum Spawn.");
            return true;
        }
        return true;
    }
}


