package de.noctivag.skyblock.commands;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.FeatureToggleGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeatureCommand implements CommandExecutor {
    private final SkyblockPlugin SkyblockPlugin;

    public FeatureCommand(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("SkyblockPlugin.admin")) {
            player.sendMessage(Component.text("§cDazu hast du keine Berechtigung!"));
            return true;
        }

        new FeatureToggleGUI(SkyblockPlugin).openGUI(player);
        return true;
    }
}
