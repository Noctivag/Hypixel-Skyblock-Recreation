package de.noctivag.plugin.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.gui.FeatureToggleGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeatureCommand implements CommandExecutor {
    private final Plugin plugin;

    public FeatureCommand(Plugin plugin) {
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

        new FeatureToggleGUI(plugin).openGUI(player);
        return true;
    }
}
