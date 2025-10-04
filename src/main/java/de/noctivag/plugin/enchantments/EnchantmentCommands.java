package de.noctivag.plugin.enchantments;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
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
 * Enchantment Commands - Hypixel SkyBlock Style Enchantment Commands
 * 
 * Commands:
 * - /enchant <enchantment> <level> - Apply enchantment to held item
 * - /disenchant <enchantment> - Remove enchantment from held item
 * - /enchanttable - Open enchantment table GUI
 * - /enchantlist - List all available enchantments
 * - /enchantinfo <enchantment> - Get enchantment information
 */
public class EnchantmentCommands implements CommandExecutor, TabCompleter {
    
    private final Plugin plugin;
    private final CustomEnchantmentSystem enchantmentSystem;
    private final EnchantmentGUI enchantmentGUI;
    
    public EnchantmentCommands(Plugin plugin, CustomEnchantmentSystem enchantmentSystem, 
                              EnchantmentGUI enchantmentGUI) {
        this.plugin = plugin;
        this.enchantmentSystem = enchantmentSystem;
        this.enchantmentGUI = enchantmentGUI;
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
            case "table" -> openEnchantmentTable(player);
            case "list" -> listEnchantments(player);
            case "info" -> showEnchantmentInfo(player, args);
            case "apply" -> applyEnchantment(player, args);
            case "remove" -> removeEnchantment(player, args);
            case "clear" -> clearAllEnchantments(player);
            default -> sendHelpMessage(player);
        }
        
        return true;
    }
    
    private void openEnchantmentTable(Player player) {
        enchantmentGUI.openEnchantmentTable(player);
        player.sendMessage("§aOpened enchantment table!");
    }
    
    private void listEnchantments(Player player) {
        player.sendMessage("§6=== Available Enchantments ===");
        
        for (CustomEnchantmentSystem.CustomEnchantment enchantment : enchantmentSystem.getAllEnchantments()) {
            player.sendMessage("§d" + enchantment.getName() + " §7- " + enchantment.getDescription() + 
                             " §8(" + enchantment.getRarity().getDisplayName() + "§8)");
        }
        
        player.sendMessage("§7Use §e/enchant info <name> §7for detailed information!");
    }
    
    private void showEnchantmentInfo(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /enchant info <enchantment>");
            return;
        }
        
        String enchantmentName = args[1];
        CustomEnchantmentSystem.CustomEnchantment enchantment = 
            enchantmentSystem.getEnchantment(enchantmentName);
        
        if (enchantment == null) {
            player.sendMessage("§cEnchantment '" + enchantmentName + "' not found!");
            return;
        }
        
        player.sendMessage("§6=== " + enchantment.getName() + " ===");
        player.sendMessage("§7Description: §f" + enchantment.getDescription());
        player.sendMessage("§7Max Level: §c" + enchantment.getMaxLevel());
        player.sendMessage("§7Rarity: " + enchantment.getRarity().getDisplayName());
        player.sendMessage("§7Target: §e" + enchantment.getTarget().name());
    }
    
    private void applyEnchantment(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage("§cUsage: /enchant apply <enchantment> <level>");
            return;
        }
        
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            player.sendMessage("§cYou must hold an item to enchant!");
            return;
        }
        
        String enchantmentName = args[1];
        CustomEnchantmentSystem.CustomEnchantment enchantment = 
            enchantmentSystem.getEnchantment(enchantmentName);
        
        if (enchantment == null) {
            player.sendMessage("§cEnchantment '" + enchantmentName + "' not found!");
            return;
        }
        
        int level;
        try {
            level = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage("§cInvalid level! Please enter a number.");
            return;
        }
        
        if (level < 1 || level > enchantment.getMaxLevel()) {
            player.sendMessage("§cLevel must be between 1 and " + enchantment.getMaxLevel() + "!");
            return;
        }
        
        if (!enchantmentSystem.canEnchant(item, enchantment)) {
            player.sendMessage("§cThis enchantment cannot be applied to this item type!");
            return;
        }
        
        enchantmentSystem.applyEnchantment(item, enchantment, level);
        player.sendMessage("§aApplied " + enchantment.getName() + " " + level + " to your item!");
    }
    
    private void removeEnchantment(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /enchant remove <enchantment>");
            return;
        }
        
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            player.sendMessage("§cYou must hold an item to disenchant!");
            return;
        }
        
        String enchantmentName = args[1];
        CustomEnchantmentSystem.CustomEnchantment enchantment = 
            enchantmentSystem.getEnchantment(enchantmentName);
        
        if (enchantment == null) {
            player.sendMessage("§cEnchantment '" + enchantmentName + "' not found!");
            return;
        }
        
        if (!enchantmentSystem.hasEnchantment(item, enchantment)) {
            player.sendMessage("§cThis item doesn't have " + enchantment.getName() + "!");
            return;
        }
        
        // Remove enchantment by setting level to 0
        enchantmentSystem.applyEnchantment(item, enchantment, 0);
        player.sendMessage("§aRemoved " + enchantment.getName() + " from your item!");
    }
    
    private void clearAllEnchantments(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            player.sendMessage("§cYou must hold an item to clear enchantments!");
            return;
        }
        
        int removedCount = 0;
        for (CustomEnchantmentSystem.CustomEnchantment enchantment : enchantmentSystem.getAllEnchantments()) {
            if (enchantmentSystem.hasEnchantment(item, enchantment)) {
                enchantmentSystem.applyEnchantment(item, enchantment, 0);
                removedCount++;
            }
        }
        
        if (removedCount > 0) {
            player.sendMessage("§aRemoved " + removedCount + " enchantments from your item!");
        } else {
            player.sendMessage("§cThis item has no enchantments to remove!");
        }
    }
    
    private void sendHelpMessage(Player player) {
        player.sendMessage("§6=== Enchantment Commands ===");
        player.sendMessage("§e/enchant table §7- Open enchantment table GUI");
        player.sendMessage("§e/enchant list §7- List all available enchantments");
        player.sendMessage("§e/enchant info <name> §7- Get enchantment information");
        player.sendMessage("§e/enchant apply <name> <level> §7- Apply enchantment to held item");
        player.sendMessage("§e/enchant remove <name> §7- Remove enchantment from held item");
        player.sendMessage("§e/enchant clear §7- Remove all enchantments from held item");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("table", "list", "info", "apply", "remove", "clear");
            for (String subCommand : subCommands) {
                if (subCommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subCommand);
                }
            }
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("info") || 
                                       args[0].equalsIgnoreCase("apply") || 
                                       args[0].equalsIgnoreCase("remove"))) {
            for (CustomEnchantmentSystem.CustomEnchantment enchantment : enchantmentSystem.getAllEnchantments()) {
                if (enchantment.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(enchantment.getName());
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("apply")) {
            CustomEnchantmentSystem.CustomEnchantment enchantment = 
                enchantmentSystem.getEnchantment(args[1]);
            if (enchantment != null) {
                for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
                    completions.add(String.valueOf(i));
                }
            }
        }
        
        return completions;
    }
}
