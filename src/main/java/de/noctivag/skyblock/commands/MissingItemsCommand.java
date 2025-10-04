package de.noctivag.skyblock.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.gui.MissingItemsGUI;
import de.noctivag.skyblock.items.MissingItemsSystem;
import de.noctivag.skyblock.items.MissingItemsSystem.ItemCategory;
import de.noctivag.skyblock.items.MissingItemsSystem.MissingItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * MissingItemsCommand - Verwaltet fehlende Items
 */
public class MissingItemsCommand implements CommandExecutor, TabCompleter {
    
    private final MissingItemsSystem missingItemsSystem;
    private final MissingItemsGUI missingItemsGUI;
    
    public MissingItemsCommand(SkyblockPlugin plugin) {
        this.missingItemsSystem = new MissingItemsSystem(plugin);
        this.missingItemsGUI = new MissingItemsGUI(plugin);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("basicsplugin.missingitems")) {
            sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
            return true;
        }
        
        if (args.length == 0) {
            if (sender instanceof Player) {
                missingItemsGUI.openMainMenu((Player) sender);
            } else {
                sendHelp(sender);
            }
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "list":
                listMissingItems(sender);
                break;
            case "stats":
                showStatistics(sender);
                break;
            case "category":
                if (args.length > 1) {
                    showCategoryItems(sender, args[1]);
                } else {
                    sender.sendMessage("§cVerwendung: /missingitems category <category>");
                }
                break;
            case "item":
                if (args.length > 1) {
                    showItemDetails(sender, args[1]);
                } else {
                    sender.sendMessage("§cVerwendung: /missingitems item <item>");
                }
                break;
            case "gui":
                if (sender instanceof Player) {
                    missingItemsGUI.openMainMenu((Player) sender);
                } else {
                    sender.sendMessage("§cDieser Befehl kann nur von Spielern verwendet werden!");
                }
                break;
            case "help":
                sendHelp(sender);
                break;
            default:
                sender.sendMessage("§cUnbekannter Unterbefehl: " + subCommand);
                sendHelp(sender);
                break;
        }
        
        return true;
    }
    
    private void listMissingItems(CommandSender sender) {
        sender.sendMessage("§6§l=== MISSING ITEMS LIST ===");
        
        Map<ItemCategory, List<MissingItem>> categorizedItems = missingItemsSystem.getMissingItemsByCategory();
        
        for (ItemCategory category : ItemCategory.values()) {
            List<MissingItem> items = categorizedItems.get(category);
            int itemCount = items != null ? items.size() : 0;
            
            sender.sendMessage("§e" + category.getDisplayName() + " §7(" + itemCount + " items):");
            
            if (items != null && !items.isEmpty()) {
                for (MissingItem item : items) {
                    sender.sendMessage("  §7- " + item.getDisplayName() + " §7(" + item.getRarity().getDisplayName() + ")");
                }
            } else {
                sender.sendMessage("  §7- Keine Items in dieser Kategorie");
            }
            
            sender.sendMessage("");
        }
        
        sender.sendMessage("§6==========================");
    }
    
    private void showStatistics(CommandSender sender) {
        sender.sendMessage("§6§l=== MISSING ITEMS STATISTICS ===");
        
        int totalItems = missingItemsSystem.getMissingItemCount();
        Map<ItemCategory, List<MissingItem>> categorizedItems = missingItemsSystem.getMissingItemsByCategory();
        
        sender.sendMessage("§7Total Missing Items: §a" + totalItems);
        sender.sendMessage("§7Categories: §a" + categorizedItems.size());
        sender.sendMessage("");
        
        sender.sendMessage("§eItems by Category:");
        for (ItemCategory category : ItemCategory.values()) {
            List<MissingItem> items = categorizedItems.get(category);
            int itemCount = items != null ? items.size() : 0;
            double percentage = totalItems > 0 ? (double) itemCount / totalItems * 100 : 0;
            
            sender.sendMessage("§7" + category.getDisplayName() + ": §a" + itemCount + " §7(" + String.format("%.1f", percentage) + "%)");
        }
        
        sender.sendMessage("");
        
        // Rarity statistics
        Map<String, Integer> rarityStats = new HashMap<>();
        for (MissingItem item : missingItemsSystem.getAllMissingItems().values()) {
            String rarity = item.getRarity().getDisplayName();
            rarityStats.put(rarity, rarityStats.getOrDefault(rarity, 0) + 1);
        }
        
        sender.sendMessage("§eItems by Rarity:");
        for (Map.Entry<String, Integer> entry : rarityStats.entrySet()) {
            double percentage = totalItems > 0 ? (double) entry.getValue() / totalItems * 100 : 0;
            sender.sendMessage("§7" + entry.getKey() + ": §a" + entry.getValue() + " §7(" + String.format("%.1f", percentage) + "%)");
        }
        
        sender.sendMessage("§6================================");
    }
    
    private void showCategoryItems(CommandSender sender, String categoryName) {
        ItemCategory category = null;
        for (ItemCategory cat : ItemCategory.values()) {
            if (cat.name().equalsIgnoreCase(categoryName) || 
                cat.getDisplayName().toLowerCase().contains(categoryName.toLowerCase())) {
                category = cat;
                break;
            }
        }
        
        if (category == null) {
            sender.sendMessage("§cUnbekannte Kategorie: " + categoryName);
            sender.sendMessage("§7Verfügbare Kategorien: " + Arrays.toString(ItemCategory.values()));
            return;
        }
        
        Map<ItemCategory, List<MissingItem>> categorizedItems = missingItemsSystem.getMissingItemsByCategory();
        List<MissingItem> items = categorizedItems.get(category);
        
        if (items == null || items.isEmpty()) {
            sender.sendMessage("§cKeine Items in der Kategorie " + category.getDisplayName() + " gefunden!");
            return;
        }
        
        sender.sendMessage("§6§l=== " + category.getDisplayName().toUpperCase() + " ITEMS ===");
        sender.sendMessage("§7Items in dieser Kategorie: §a" + items.size());
        sender.sendMessage("");
        
        for (MissingItem item : items) {
            sender.sendMessage("§e" + item.getDisplayName() + " §7(" + item.getRarity().getDisplayName() + ")");
            sender.sendMessage("  §7" + item.getDescription());
            sender.sendMessage("  §7Base Damage: §a" + item.getBaseDamage());
            sender.sendMessage("");
        }
        
        sender.sendMessage("§6==========================");
    }
    
    private void showItemDetails(CommandSender sender, String itemName) {
        MissingItem item = null;
        for (MissingItem missingItem : missingItemsSystem.getAllMissingItems().values()) {
            if (missingItem.getName().equalsIgnoreCase(itemName) || 
                missingItem.getDisplayName().toLowerCase().contains(itemName.toLowerCase())) {
                item = missingItem;
                break;
            }
        }
        
        if (item == null) {
            sender.sendMessage("§cItem nicht gefunden: " + itemName);
            return;
        }
        
        sender.sendMessage("§6§l=== " + item.getDisplayName().toUpperCase() + " ===");
        sender.sendMessage("§7Name: §a" + item.getName());
        sender.sendMessage("§7Display Name: " + item.getDisplayName());
        sender.sendMessage("§7Material: §a" + item.getMaterial().name());
        sender.sendMessage("§7Description: §7" + item.getDescription());
        sender.sendMessage("§7Rarity: " + item.getRarity().getDisplayName());
        sender.sendMessage("§7Category: " + item.getCategory().getDisplayName());
        sender.sendMessage("§7Base Damage: §a" + item.getBaseDamage());
        sender.sendMessage("§7Multiplier: §a" + item.getRarity().getMultiplier() + "x");
        sender.sendMessage("");
        
        sender.sendMessage("§eFeatures:");
        for (String feature : item.getFeatures()) {
            sender.sendMessage("  " + feature);
        }
        sender.sendMessage("");
        
        sender.sendMessage("§eRequirements:");
        for (String requirement : item.getRequirements()) {
            sender.sendMessage("  " + requirement);
        }
        
        sender.sendMessage("§6==========================");
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6§l=== MISSING ITEMS COMMAND HELP ===");
        sender.sendMessage("§7/missingitems §8- Öffnet das GUI (nur für Spieler)");
        sender.sendMessage("§7/missingitems list §8- Zeigt alle fehlenden Items");
        sender.sendMessage("§7/missingitems stats §8- Zeigt Statistiken");
        sender.sendMessage("§7/missingitems category <category> §8- Zeigt Items einer Kategorie");
        sender.sendMessage("§7/missingitems item <item> §8- Zeigt Item-Details");
        sender.sendMessage("§7/missingitems gui §8- Öffnet das GUI");
        sender.sendMessage("§7/missingitems help §8- Zeigt diese Hilfe");
        sender.sendMessage("");
        sender.sendMessage("§eVerfügbare Kategorien:");
        for (ItemCategory category : ItemCategory.values()) {
            sender.sendMessage("§7- " + category.name() + " §7(" + category.getDisplayName() + ")");
        }
        sender.sendMessage("§6================================");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> subCommands = Arrays.asList(
                "list", "stats", "category", "item", "gui", "help"
            );
            
            for (String subCommand : subCommands) {
                if (subCommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subCommand);
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("category")) {
                for (ItemCategory category : ItemCategory.values()) {
                    if (category.name().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(category.name());
                    }
                }
            } else if (args[0].equalsIgnoreCase("item")) {
                for (MissingItem item : missingItemsSystem.getAllMissingItems().values()) {
                    if (item.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(item.getName());
                    }
                }
            }
        }
        
        return completions;
    }
}
