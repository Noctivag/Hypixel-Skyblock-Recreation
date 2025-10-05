package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackCommand implements CommandExecutor {
    private final SkyblockPlugin SkyblockPlugin;

    public BackCommand(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this.");
            return true;
        }
        // TODO: Implement proper TeleportManager interface
        // boolean ok = ((TeleportManager) SkyblockPlugin.getTeleportManager()).back(player);
        boolean ok = false; // Placeholder - no teleport functionality yet
        player.sendMessage(ok ? "§aTeleportiere zur letzten Position." : "§cKeine letzte Position gefunden.");
        return true;
    }
}


