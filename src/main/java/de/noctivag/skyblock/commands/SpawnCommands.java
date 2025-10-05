package de.noctivag.skyblock.commands;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommands implements CommandExecutor {
    private final SkyblockPlugin SkyblockPlugin;

    public SpawnCommands(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String name = command.getName().toLowerCase();
        if (name.equals("setspawn")) {
            if (!(sender instanceof Player player)) { sender.sendMessage("Players only"); return true; }
            if (!player.hasPermission("basicsplugin.setspawn")) { player.sendMessage(Component.text("§cKeine Berechtigung.")); return true; }
            // TODO: Implement proper TeleportManager interface
            // ((TeleportManager) SkyblockPlugin.getTeleportManager()).setSpawn(player.getLocation());
            player.sendMessage(Component.text("§aSpawn gesetzt."));
            return true;
        }

        if (name.equals("spawn")) {
            if (!(sender instanceof Player player)) { sender.sendMessage("Players only"); return true; }
            // TODO: Implement proper TeleportManager interface
            // Location spawn = ((TeleportManager) SkyblockPlugin.getTeleportManager()).getSpawn();
            Location spawn = null; // Placeholder
            if (spawn == null) { player.sendMessage(Component.text("§cSpawn nicht gesetzt.")); return true; }
            // ((TeleportManager) SkyblockPlugin.getTeleportManager()).setLastLocation(player, player.getLocation());
            player.teleport(spawn);
            player.sendMessage(Component.text("§aTeleportiere zum Spawn."));
            return true;
        }
        return true;
    }
}


