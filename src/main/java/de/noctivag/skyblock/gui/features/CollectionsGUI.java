package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.collections.CollectionType;
import de.noctivag.skyblock.collections.PlayerCollections;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Collections GUI - Displays all available collections
 */
public class CollectionsGUI extends Menu {
    
    public CollectionsGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8§lCollections", 54);
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // Get player collections data
        PlayerCollections playerCollections = plugin.getCollectionsSystem().getPlayerCollections(player);
        if (playerCollections == null) {
            player.sendMessage("§cError loading collections data!");
            return;
        }
        
        // Farming Collections
        setupCategoryItem(10, Material.WHEAT, "Farming", "🌾", "&a", playerCollections);
        
        // Mining Collections
        setupCategoryItem(12, Material.COAL, "Mining", "⛏", "&6", playerCollections);
        
        // Combat Collections
        setupCategoryItem(14, Material.ROTTEN_FLESH, "Combat", "⚔", "&c", playerCollections);
        
        // Foraging Collections
        setupCategoryItem(16, Material.OAK_LOG, "Foraging", "🌲", "&2", playerCollections);
        
        // Fishing Collections
        setupCategoryItem(19, Material.COD, "Fishing", "🎣", "&9", playerCollections);
        
        // Special Collections
        setupCategoryItem(21, Material.DRAGON_HEAD, "Special", "✨", "&d", playerCollections);
        
        // Close Button
        setCloseButton(49);
    }
    
    private void setupCategoryItem(int slot, Material material, String category, String icon, String color, PlayerCollections playerCollections) {
        long totalCollections = playerCollections.getTotalCollectionsByCategory(category);
        int totalMilestones = 0;
        
        // Count total milestones for this category
        for (CollectionType collection : CollectionType.getByCategory(category)) {
            totalMilestones += playerCollections.getCurrentMilestoneLevel(collection);
        }
        
        String rarity = getRarityForTotal(totalCollections);
        String[] lore = {
            "&7" + category + " Collections",
            "&7Total Collected: " + color + formatNumber(totalCollections),
            "&7Milestones: &a" + totalMilestones,
            "",
            "&7Click to view " + category.toLowerCase() + " collections",
            "&eKlicke zum Öffnen"
        };
        
        setItem(slot, material, color + icon + " " + category + " Collections", rarity, lore);
    }
    
    private String getRarityForTotal(long total) {
        if (total >= 1000000) return "mythic";
        if (total >= 100000) return "legendary";
        if (total >= 10000) return "epic";
        if (total >= 1000) return "rare";
        if (total >= 100) return "uncommon";
        return "common";
    }
    
    private String formatNumber(long number) {
        if (number >= 1_000_000_000_000L) {
            return String.format("%.1fT", number / 1_000_000_000_000.0);
        } else if (number >= 1_000_000_000L) {
            return String.format("%.1fB", number / 1_000_000_000.0);
        } else if (number >= 1_000_000L) {
            return String.format("%.1fM", number / 1_000_000.0);
        } else if (number >= 1_000L) {
            return String.format("%.1fK", number / 1_000.0);
        } else {
            return String.valueOf(number);
        }
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        // Get player collections data
        PlayerCollections playerCollections = plugin.getCollectionsSystem().getPlayerCollections(player);
        if (playerCollections == null) {
            player.sendMessage("§cError loading collections data!");
            return;
        }
        
        switch (slot) {
            case 10:
                showCategoryDetails("Farming", playerCollections);
                break;
            case 12:
                showCategoryDetails("Mining", playerCollections);
                break;
            case 14:
                showCategoryDetails("Combat", playerCollections);
                break;
            case 16:
                showCategoryDetails("Foraging", playerCollections);
                break;
            case 19:
                showCategoryDetails("Fishing", playerCollections);
                break;
            case 21:
                showCategoryDetails("Special", playerCollections);
                break;
            case 49:
                close();
                break;
        }
    }
    
    private void showCategoryDetails(String category, PlayerCollections playerCollections) {
        player.sendMessage("§8§m--------------------------------");
        player.sendMessage("§6" + category + " Collections");
        player.sendMessage("§8§m--------------------------------");
        
        CollectionType[] collections = CollectionType.getByCategory(category);
        if (collections.length == 0) {
            player.sendMessage("§7No collections found for this category.");
            return;
        }
        
        for (CollectionType collection : collections) {
            long amount = playerCollections.getCollection(collection);
            int milestoneLevel = playerCollections.getCurrentMilestoneLevel(collection);
            long nextMilestone = playerCollections.getNextMilestoneRequirement(collection);
            long progress = playerCollections.getProgressToNextMilestone(collection);
            long totalNeeded = playerCollections.getTotalProgressNeededForNextMilestone(collection);
            
            String status = nextMilestone == -1 ? "§aMAX LEVEL" : "§7" + progress + "/" + totalNeeded;
            
            player.sendMessage(collection.getColor() + collection.getIcon() + " " + collection.getDisplayName() + 
                " §7- Level " + milestoneLevel + " §7(" + formatNumber(amount) + ") " + status);
        }
        
        long totalForCategory = playerCollections.getTotalCollectionsByCategory(category);
        player.sendMessage("");
        player.sendMessage("§7Total " + category + " Collections: §a" + formatNumber(totalForCategory));
        player.sendMessage("§8§m--------------------------------");
    }
}
