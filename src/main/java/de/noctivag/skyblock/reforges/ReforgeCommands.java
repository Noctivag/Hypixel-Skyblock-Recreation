package de.noctivag.skyblock.reforges;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Reforge Commands - Hypixel SkyBlock Style Reforge Commands
 * 
 * Commands:
 * - /reforge <reforge> - Apply reforge to held item
 * - /reforge remove - Remove reforge from held item
 * - /reforge table - Open reforge table GUI
 * - /reforge list - List all available reforges
 * - /reforge info <reforge> - Get reforge information
 * - /reforge preview <reforge> - Preview reforge stats
 */
public class ReforgeCommands implements CommandExecutor, TabCompleter {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final ReforgeSystem reforgeSystem;
    private final ReforgeGUI reforgeGUI;
    
    public ReforgeCommands(SkyblockPlugin SkyblockPlugin, ReforgeSystem reforgeSystem, ReforgeGUI reforgeGUI) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.reforgeSystem = reforgeSystem;
        this.reforgeGUI = reforgeGUI;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }
        
        if (args.length == 0) {
            sendHelpMessage(player);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "table" -> openReforgeTable(player);
            case "list" -> listReforges(player);
            case "info" -> showReforgeInfo(player, args);
            case "apply" -> applyReforge(player, args);
            case "remove" -> removeReforge(player);
            case "preview" -> previewReforge(player, args);
            default -> sendHelpMessage(player);
        }
        
        return true;
    }
    
    private void openReforgeTable(Player player) {
        reforgeGUI.openReforgeTable(player);
        player.sendMessage(Component.text("§aOpened reforge table!"));
    }
    
    private void listReforges(Player player) {
        player.sendMessage(Component.text("§6=== Available Reforges ==="));
        
        for (ReforgeSystem.Reforge reforge : reforgeSystem.getAllReforges()) {
            player.sendMessage("§d" + reforge.getName() + " §7- " + reforge.getDescription() + 
                             " §8(" + reforge.getRarity().getDisplayName() + "§8)");
        }
        
        player.sendMessage(Component.text("§7Use §e/reforge info <name> §7for detailed information!"));
    }
    
    private void showReforgeInfo(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("§cUsage: /reforge info <reforge>"));
            return;
        }
        
        String reforgeName = args[1];
        ReforgeSystem.Reforge reforge = reforgeSystem.getReforge(reforgeName);
        
        if (reforge == null) {
            player.sendMessage("§cReforge '" + reforgeName + "' not found!");
            return;
        }
        
        player.sendMessage("§6=== " + reforge.getName() + " Reforge ===");
        player.sendMessage("§7Description: §f" + reforge.getDescription());
        player.sendMessage("§7Rarity: " + reforge.getRarity().getDisplayName());
        player.sendMessage("§7Base Cost: §6" + String.format("%.0f", reforge.getBaseCost()) + " coins");
        player.sendMessage(Component.text("§7Compatible Items:"));
        
        for (String materialName : reforge.getCompatibleMaterials().stream()
                .map(m -> m.name()).limit(10).toList()) {
            player.sendMessage("§7- " + materialName);
        }
        
        if (reforge.getCompatibleMaterials().size() > 10) {
            player.sendMessage("§7... and " + (reforge.getCompatibleMaterials().size() - 10) + " more");
        }
        
        player.sendMessage("");
        player.sendMessage(Component.text("§7Stats:"));
        for (ReforgeSystem.ReforgeStat stat : reforge.getStats()) {
            player.sendMessage("§7- " + stat.getName() + ": §a+" + 
                             String.format("%.1f", stat.getBaseValue()) + " §7(base), §a+" + 
                             String.format("%.1f", stat.getBonusPerLevel()) + " §7per level");
        }
    }
    
    private void applyReforge(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("§cUsage: /reforge apply <reforge>"));
            return;
        }
        
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            player.sendMessage(Component.text("§cYou must hold an item to reforge!"));
            return;
        }
        
        String reforgeName = args[1];
        ReforgeSystem.Reforge reforge = reforgeSystem.getReforge(reforgeName);
        
        if (reforge == null) {
            player.sendMessage("§cReforge '" + reforgeName + "' not found!");
            return;
        }
        
        if (!reforgeSystem.canReforge(item, reforge)) {
            player.sendMessage(Component.text("§cThis reforge cannot be applied to this item type!"));
            return;
        }
        
        double cost = reforgeSystem.calculateReforgeCost(reforge, item);
        
        // Check if player has enough coins (simplified)
        if (hasEnoughCoins(player, cost)) {
            // Remove current reforge if any
            ReforgeSystem.Reforge currentReforge = reforgeSystem.getItemReforge(item);
            if (currentReforge != null) {
                player.sendMessage("§eRemoved old reforge: " + currentReforge.getName());
            }
            
            // Apply new reforge
            reforgeSystem.applyReforge(item, reforge);
            
            // Deduct coins (simplified)
            player.sendMessage("§aApplied " + reforge.getName() + " reforge to your item for " + 
                             String.format("%.0f", cost) + " coins!");
            
            // Show stat changes
            showReforgeStats(player, reforge, item);
        } else {
            player.sendMessage("§cYou don't have enough coins! Cost: " + 
                             String.format("%.0f", cost) + " coins");
        }
    }
    
    private void removeReforge(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            player.sendMessage(Component.text("§cYou must hold an item to remove reforge!"));
            return;
        }
        
        ReforgeSystem.Reforge currentReforge = reforgeSystem.getItemReforge(item);
        if (currentReforge == null) {
            player.sendMessage(Component.text("§cThis item doesn't have a reforge to remove!"));
            return;
        }
        
        reforgeSystem.removeReforge(item);
        player.sendMessage("§aRemoved " + currentReforge.getName() + " reforge from your item!");
    }
    
    private void previewReforge(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("§cUsage: /reforge preview <reforge>"));
            return;
        }
        
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            player.sendMessage(Component.text("§cYou must hold an item to preview reforge!"));
            return;
        }
        
        String reforgeName = args[1];
        ReforgeSystem.Reforge reforge = reforgeSystem.getReforge(reforgeName);
        
        if (reforge == null) {
            player.sendMessage("§cReforge '" + reforgeName + "' not found!");
            return;
        }
        
        if (!reforgeSystem.canReforge(item, reforge)) {
            player.sendMessage(Component.text("§cThis reforge cannot be applied to this item type!"));
            return;
        }
        
        reforgeGUI.showReforgePreview(player, item, reforge);
    }
    
    private boolean hasEnoughCoins(Player player, double amount) {
        // Simplified coin check - in real implementation, check player's balance
        return true; // For demo purposes
    }
    
    private void showReforgeStats(Player player, ReforgeSystem.Reforge reforge, ItemStack item) {
        player.sendMessage(Component.text("§7Stats added:"));
        
        java.util.Map<String, Double> stats = reforgeSystem.calculateReforgeStats(reforge, item);
        for (java.util.Map.Entry<String, Double> stat : stats.entrySet()) {
            player.sendMessage("§7- " + stat.getKey() + ": §a+" + String.format("%.1f", stat.getValue()));
        }
    }
    
    private void sendHelpMessage(Player player) {
        player.sendMessage(Component.text("§6=== Reforge Commands ==="));
        player.sendMessage(Component.text("§e/reforge table §7- Open reforge table GUI"));
        player.sendMessage(Component.text("§e/reforge list §7- List all available reforges"));
        player.sendMessage(Component.text("§e/reforge info <name> §7- Get reforge information"));
        player.sendMessage(Component.text("§e/reforge apply <name> §7- Apply reforge to held item"));
        player.sendMessage(Component.text("§e/reforge remove §7- Remove reforge from held item"));
        player.sendMessage(Component.text("§e/reforge preview <name> §7- Preview reforge stats"));
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("table", "list", "info", "apply", "remove", "preview");
            for (String subCommand : subCommands) {
                if (subCommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subCommand);
                }
            }
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("info") || 
                                       args[0].equalsIgnoreCase("apply") || 
                                       args[0].equalsIgnoreCase("preview"))) {
            for (ReforgeSystem.Reforge reforge : reforgeSystem.getAllReforges()) {
                if (reforge.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(reforge.getName());
                }
            }
        }
        
        return completions;
    }
}
