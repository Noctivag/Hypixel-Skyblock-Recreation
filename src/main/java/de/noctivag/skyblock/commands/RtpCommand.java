package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public class RtpCommand implements CommandExecutor {
    private final SkyblockPlugin SkyblockPlugin;

    public RtpCommand(SkyblockPlugin SkyblockPlugin) { this.SkyblockPlugin = SkyblockPlugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) { sender.sendMessage("Players only"); return true; }
        World w = player.getWorld();
        // TODO: Implement proper ConfigManager interface
        // int radius = ((ConfigManager) SkyblockPlugin.getConfigManager()).getConfig().getInt("rtp.radius", 5000);
        int radius = 5000; // Default radius
        int x = ThreadLocalRandom.current().nextInt(-radius, radius + 1);
        int z = ThreadLocalRandom.current().nextInt(-radius, radius + 1);
        int y = w.getHighestBlockYAt(x, z) + 1;
        Location dest = new Location(w, x + 0.5, y, z + 0.5);
        // TODO: Implement proper TeleportManager interface
        // ((TeleportManager) SkyblockPlugin.getTeleportManager()).setLastLocation(player, player.getLocation());
        player.teleport(dest);
        player.sendMessage("§aZufällige Teleportation: §7" + x + ", " + z);
        return true;
    }
}


