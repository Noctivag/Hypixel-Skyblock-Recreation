package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.gui.accessory.AccessoryBagGUI;
import de.noctivag.skyblock.items.AccessoryBag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccessoryBagCommand implements CommandExecutor {
    // Demo: AccessoryBag pro Spieler (später persistent machen)
    private static final Map<UUID, AccessoryBag> bags = new HashMap<>();

    public static AccessoryBag getBag(Player player) {
        return bags.computeIfAbsent(player.getUniqueId(), k -> new AccessoryBag());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cNur Spieler können dieses Kommando nutzen.");
            return true;
        }
        AccessoryBag bag = getBag(player);
        // Stat-Boni aus AccessoryBag anwenden
        de.noctivag.skyblock.stats.StatManager.clearModifiers(player);
        for (de.noctivag.skyblock.items.Accessory acc : bag.getAll()) {
            for (var entry : acc.getAllStats().entrySet()) {
                de.noctivag.skyblock.stats.StatManager.addModifier(player, entry.getKey(), entry.getValue());
            }
        }
        new AccessoryBagGUI(player, bag);
        return true;
    }
}
