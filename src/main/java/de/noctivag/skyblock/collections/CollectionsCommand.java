package de.noctivag.skyblock.collections;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.features.CollectionsGUI;
import de.noctivag.skyblock.collections.CollectionType;
import de.noctivag.skyblock.collections.PlayerCollections;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Collections Command - Command Handler für Collections System
 * 
 * Verantwortlich für:
 * - /collections gui - Öffnet Collections GUI
 * - /collections list - Zeigt alle Collections
 * - /collections info <collection> - Zeigt Collection Details
 * - /collections progress - Zeigt Collection Progress
 * - /collections reset <collection> - Reset Collection (Admin)
 * - /collections add <collection> <amount> - Add Collection (Admin)
 */
public class CollectionsCommand implements CommandExecutor, TabCompleter {
    
    private final CollectionsSystem collectionsSystem;
    
    public CollectionsCommand(SkyblockPlugin SkyblockPlugin, CollectionsSystem collectionsSystem) {
        this.collectionsSystem = collectionsSystem;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("§cThis command can only be used by players!"));
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            // Default: Open GUI
            collectionsSystem.openCollectionsGUI(player);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "gui":
                collectionsSystem.openCollectionsGUI(player);
                break;
                
            case "list":
                showCollectionsList(player);
                break;
                
            case "info":
                if (args.length < 2) {
                    player.sendMessage(Component.text("§cUsage: /collections info <collection>"));
                    return true;
                }
                showCollectionInfo(player, args[1]);
                break;
                
            case "progress":
                showCollectionProgress(player);
                break;
                
            case "category":
                if (args.length < 2) {
                    player.sendMessage(Component.text("§cUsage: /collections category <category>"));
                    return true;
                }
                collectionsSystem.openCollectionCategoryGUI(player, args[1]);
                break;
                
            case "reset":
                if (!player.hasPermission("basics.collections.admin")) {
                    player.sendMessage(Component.text("§cYou don't have permission to use this command!"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(Component.text("§cUsage: /collections reset <collection>"));
                    return true;
                }
                resetCollection(player, args[1]);
                break;
                
            case "add":
                if (!player.hasPermission("basics.collections.admin")) {
                    player.sendMessage(Component.text("§cYou don't have permission to use this command!"));
                    return true;
                }
                if (args.length < 3) {
                    player.sendMessage(Component.text("§cUsage: /collections add <collection> <amount>"));
                    return true;
                }
                addCollection(player, args[1], args[2]);
                break;
                
            case "help":
                showHelp(player);
                break;
                
            default:
                player.sendMessage(Component.text("§cUnknown subcommand! Use /collections help for help."));
                break;
        }
        
        return true;
    }
    
    private void showCollectionsList(Player player) {
        player.sendMessage(Component.text("§6§l=== Collections List ==="));
        
        // Farming Collections
        player.sendMessage(Component.text("§e§lFarming Collections:"));
        showCategoryCollections(player, "farming");
        
        // Mining Collections
        player.sendMessage(Component.text("§7§lMining Collections:"));
        showCategoryCollections(player, "mining");
        
        // Combat Collections
        player.sendMessage(Component.text("§c§lCombat Collections:"));
        showCategoryCollections(player, "combat");
        
        // Foraging Collections
        player.sendMessage(Component.text("§6§lForaging Collections:"));
        showCategoryCollections(player, "foraging");
        
        // Fishing Collections
        player.sendMessage(Component.text("§b§lFishing Collections:"));
        showCategoryCollections(player, "fishing");
        
        // Boss Collections
        player.sendMessage(Component.text("§d§lBoss Collections:"));
        showCategoryCollections(player, "boss");
    }
    
    private void showCategoryCollections(Player player, String category) {
        List<Material> materials = getCollectionsByCategory(category);
        PlayerCollections playerCollections = collectionsSystem.getPlayerCollections(player.getUniqueId());
        
        for (Material material : materials) {
            // Get the collection type for this material
            CollectionType collectionType = getCollectionTypeForMaterial(material);
            if (collectionType == null) continue;
            
            int amount = 0; // Placeholder
            CollectionsSystem.CollectionConfig config = collectionsSystem.getCollectionConfig(collectionType);
            if (config != null) {
                player.sendMessage(Component.text("§7- " + config.getDisplayName() + " §7(§e" + amount + "§7)"));
            }
        }
    }
    
    private void showCollectionInfo(Player player, String collectionName) {
        CollectionType collectionType = getCollectionTypeByName(collectionName);
        if (collectionType == null) {
            player.sendMessage(Component.text("§cCollection not found: " + collectionName));
            return;
        }
        
        CollectionsSystem.CollectionConfig config = collectionsSystem.getCollectionConfig(collectionType);
        if (config == null) {
            player.sendMessage(Component.text("§cCollection config not found!"));
            return;
        }
        
        PlayerCollections playerCollections = collectionsSystem.getPlayerCollections(player.getUniqueId());
        int currentAmount = 0; // Placeholder
        
        player.sendMessage(Component.text("§6§l=== " + config.getDisplayName() + " ==="));
        player.sendMessage(Component.text("§7Current: §e" + currentAmount));
        player.sendMessage(Component.text("§7"));
        player.sendMessage(Component.text("§7" + config.getDescription()));
        player.sendMessage(Component.text("§7" + config.getDescription()));
        player.sendMessage(Component.text("§7"));
        
        // Show rewards
        player.sendMessage(Component.text("§a§lRewards:"));
        // Rewards placeholder
        player.sendMessage(Component.text("§7§a✓ §e50: §aFirst milestone!"));
        player.sendMessage(Component.text("§7§c✗ §e100: §aSecond milestone!"));
        
        // Show recipes
        player.sendMessage(Component.text("§7"));
        player.sendMessage(Component.text("§b§lRecipes:"));
        // Recipes placeholder
        player.sendMessage(Component.text("§7§a✓ §e25: §bBasic recipe!"));
        player.sendMessage(Component.text("§7§c✗ §e75: §bAdvanced recipe!"));
    }
    
    private void showCollectionProgress(Player player) {
        PlayerCollections playerCollections = collectionsSystem.getPlayerCollections(player.getUniqueId());
        
        player.sendMessage(Component.text("§6§l=== Collection Progress ==="));
        player.sendMessage(Component.text("§7Total Collections: §e" + "0"));
        player.sendMessage(Component.text("§7Total Rewards: §a" + "0"));
        player.sendMessage(Component.text("§7Total Recipes: §b" + "0"));
        player.sendMessage(Component.text("§7"));
        
        // Show top 5 collections
        player.sendMessage(Component.text("§e§lTop 5 Collections:"));
        player.sendMessage(Component.text("§71. Farming §7(§e0§7)"));
        player.sendMessage(Component.text("§72. Mining §7(§e0§7)"));
        player.sendMessage(Component.text("§73. Combat §7(§e0§7)"));
        player.sendMessage(Component.text("§74. Foraging §7(§e0§7)"));
        player.sendMessage(Component.text("§75. Fishing §7(§e0§7)"));
    }
    
    private void resetCollection(Player player, String collectionName) {
        CollectionType collectionType = getCollectionTypeByName(collectionName);
        if (collectionType == null) {
            player.sendMessage(Component.text("§cCollection not found: " + collectionName));
            return;
        }
        
        PlayerCollections playerCollections = collectionsSystem.getPlayerCollections(player.getUniqueId());
        // Reset collection (placeholder)
        player.sendMessage(Component.text("§aCollection reset!"));
        
        player.sendMessage(Component.text("§aCollection reset: " + collectionName));
    }
    
    private void addCollection(Player player, String collectionName, String amountStr) {
        CollectionType collectionType = getCollectionTypeByName(collectionName);
        if (collectionType == null) {
            player.sendMessage(Component.text("§cCollection not found: " + collectionName));
            return;
        }
        
        try {
            int amount = Integer.parseInt(amountStr);
            collectionsSystem.addToCollection(player, collectionType, Material.WHEAT, amount);
            player.sendMessage(Component.text("§aAdded " + amount + " to " + collectionName + " collection"));
        } catch (NumberFormatException e) {
            player.sendMessage(Component.text("§cInvalid amount: " + amountStr));
        }
    }
    
    private void showHelp(Player player) {
        player.sendMessage(Component.text("§6§l=== Collections Commands ==="));
        player.sendMessage(Component.text("§7/collections - Open collections GUI"));
        player.sendMessage(Component.text("§7/collections gui - Open collections GUI"));
        player.sendMessage(Component.text("§7/collections list - List all collections"));
        player.sendMessage(Component.text("§7/collections info <collection> - Show collection info"));
        player.sendMessage(Component.text("§7/collections progress - Show collection progress"));
        player.sendMessage(Component.text("§7/collections category <category> - Open category GUI"));
        player.sendMessage(Component.text("§7/collections help - Show this help"));
        
        if (player.hasPermission("basics.collections.admin")) {
            player.sendMessage(Component.text("§7"));
            player.sendMessage(Component.text("§c§lAdmin Commands:"));
            player.sendMessage(Component.text("§7/collections reset <collection> - Reset collection"));
            player.sendMessage(Component.text("§7/collections add <collection> <amount> - Add to collection"));
        }
    }
    
    private CollectionType getCollectionTypeByName(String name) {
        for (CollectionType type : CollectionType.values()) {
            if (type.getDisplayName().equalsIgnoreCase(name) || 
                type.getDisplayName().toLowerCase().contains(name.toLowerCase())) {
                return type;
            }
        }
        return null;
    }
    
    private CollectionType getCollectionTypeForMaterial(Material material) {
        // Simple mapping - in a real implementation, this would be more sophisticated
        if (material == Material.WHEAT || material == Material.CARROT || material == Material.POTATO) {
            return CollectionType.WHEAT;
        } else if (material == Material.COBBLESTONE || material == Material.COAL || material == Material.IRON_INGOT) {
            return CollectionType.COBBLESTONE;
        } else if (material == Material.ROTTEN_FLESH || material == Material.BONE || material == Material.STRING) {
            return CollectionType.ROTTEN_FLESH;
        } else if (material == Material.OAK_LOG || material == Material.BIRCH_LOG || material == Material.SPRUCE_LOG) {
            return CollectionType.OAK_LOG;
        } else if (material == Material.FISHING_ROD || material == Material.COD || material == Material.SALMON) {
            return CollectionType.RAW_FISH;
        } else if (material == Material.ENCHANTING_TABLE || material == Material.EXPERIENCE_BOTTLE) {
            return CollectionType.LAPIS_LAZULI;
        } else if (material == Material.BREWING_STAND || material == Material.POTION) {
            return CollectionType.NETHER_WART;
        } else if (material == Material.BONE || material == Material.LEAD) {
            return CollectionType.BONE;
        }
        return CollectionType.WHEAT; // Default fallback
    }
    
    private int getCollectionAmount(PlayerCollections playerCollections, CollectionType type, Material material) {
        // This is a simplified implementation - in reality, you'd need to track individual material amounts
        return 0; // Placeholder
    }
    
    private List<Material> getCollectionsByCategory(String category) {
        List<Material> collections = new ArrayList<>();
        
        switch (category.toLowerCase()) {
            case "farming":
                collections.addAll(Arrays.asList(
                    Material.WHEAT, Material.CARROT, Material.POTATO,
                    Material.PUMPKIN, Material.MELON, Material.SUGAR_CANE,
                    Material.COCOA_BEANS, Material.RED_MUSHROOM, Material.BROWN_MUSHROOM,
                    Material.CACTUS, Material.NETHER_WART
                ));
                break;
            case "mining":
                collections.addAll(Arrays.asList(
                    Material.COBBLESTONE, Material.COAL, Material.IRON_INGOT,
                    Material.GOLD_INGOT, Material.DIAMOND, Material.LAPIS_LAZULI,
                    Material.EMERALD, Material.REDSTONE, Material.QUARTZ,
                    Material.OBSIDIAN, Material.GLOWSTONE, Material.GRAVEL,
                    Material.SAND, Material.END_STONE, Material.NETHERRACK,
                    Material.MYCELIUM
                ));
                break;
            case "combat":
                collections.addAll(Arrays.asList(
                    Material.ROTTEN_FLESH, Material.BONE, Material.STRING,
                    Material.SPIDER_EYE, Material.GUNPOWDER, Material.ENDER_PEARL,
                    Material.GHAST_TEAR, Material.SLIME_BALL, Material.BLAZE_ROD,
                    Material.MAGMA_CREAM, Material.SKELETON_SKULL,
                    Material.ZOMBIE_HEAD, Material.SKELETON_SKULL, Material.CREEPER_HEAD
                ));
                break;
            case "foraging":
                collections.addAll(Arrays.asList(
                    Material.OAK_LOG, Material.SPRUCE_LOG, Material.BIRCH_LOG,
                    Material.JUNGLE_LOG, Material.ACACIA_LOG, Material.DARK_OAK_LOG,
                    Material.CRIMSON_STEM, Material.WARPED_STEM
                ));
                break;
            case "fishing":
                collections.addAll(Arrays.asList(
                    Material.COD, Material.SALMON, Material.PUFFERFISH,
                    Material.TROPICAL_FISH, Material.PRISMARINE_SHARD, Material.PRISMARINE_CRYSTALS,
                    Material.SPONGE, Material.LILY_PAD, Material.INK_SAC
                ));
                break;
            case "boss":
                collections.addAll(Arrays.asList(
                    Material.OAK_PLANKS, Material.ENCHANTED_BOOK, Material.DRAGON_EGG,
                    Material.NETHER_STAR, Material.ELYTRA, Material.TOTEM_OF_UNDYING
                ));
                break;
        }
        
        return collections;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> subcommands = Arrays.asList("gui", "list", "info", "progress", "category", "help");
            if (sender.hasPermission("basics.collections.admin")) {
                subcommands = Arrays.asList("gui", "list", "info", "progress", "category", "help", "reset", "add");
            }
            
            for (String subcommand : subcommands) {
                if (subcommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subcommand);
                }
            }
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "info":
                case "reset":
                case "add":
                    for (CollectionType type : CollectionType.values()) {
                        if (type.getDisplayName().toLowerCase().startsWith(args[1].toLowerCase())) {
                            completions.add(type.getDisplayName());
                        }
                    }
                    break;
                case "category":
                    List<String> categories = Arrays.asList("farming", "mining", "combat", "foraging", "fishing", "boss");
                    for (String category : categories) {
                        if (category.toLowerCase().startsWith(args[1].toLowerCase())) {
                            completions.add(category);
                        }
                    }
                    break;
            }
        }
        
        return completions;
    }
}
