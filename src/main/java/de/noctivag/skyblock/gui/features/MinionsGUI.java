package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.minions.MinionType;
import de.noctivag.skyblock.minions.PlayerMinions;
import de.noctivag.skyblock.minions.PlacedMinion;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Minions GUI - Displays player minions and management options
 */
public class MinionsGUI extends Menu {
    
    public MinionsGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8§lMinions", 54);
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // Get player minions data
        PlayerMinions playerMinions = plugin.getMinionsSystem().getPlayerMinions(player);
        if (playerMinions == null) {
            player.sendMessage("§cError loading minions data!");
            return;
        }
        
        // Minion Slots Info
        setupMinionSlotsInfo(4, playerMinions);
        
        // Category Buttons
        setupCategoryItem(19, Material.WHEAT, "Farming", "🌾", "&a", playerMinions);
        setupCategoryItem(21, Material.COAL, "Mining", "⛏", "&6", playerMinions);
        setupCategoryItem(23, Material.OAK_LOG, "Foraging", "🌲", "&2", playerMinions);
        setupCategoryItem(25, Material.COD, "Fishing", "🎣", "&9", playerMinions);
        setupCategoryItem(28, Material.ROTTEN_FLESH, "Combat", "⚔", "&c", playerMinions);
        setupCategoryItem(30, Material.DRAGON_HEAD, "Special", "✨", "&d", playerMinions);
        
        // Statistics
        setupStatisticsItem(40, playerMinions);
        
        // Close Button
        setCloseButton(49);
    }
    
    private void setupMinionSlotsInfo(int slot, PlayerMinions playerMinions) {
        int placed = playerMinions.getTotalPlacedMinions();
        int max = playerMinions.getMaxMinionSlots();
        int available = playerMinions.getAvailableMinionSlots();
        
        String rarity = getRarityForSlots(placed, max);
        String[] lore = {
            "&7Minion Slots",
            "&7Placed: &a" + placed + "/" + max,
            "&7Available: &a" + available,
            "",
            "&7Minion slots allow you to place",
            "&7and manage automated minions",
            "&7that collect resources for you.",
            "",
            "&eClick to view minion slots"
        };
        
        setItem(slot, Material.ENDER_CHEST, "&6⚡ Minion Slots", rarity, lore);
    }
    
    private void setupCategoryItem(int slot, Material material, String category, String icon, String color, PlayerMinions playerMinions) {
        int placedCount = 0;
        int totalCount = 0;
        double efficiency = 0.0;
        
        // Count minions in this category
        for (PlacedMinion minion : playerMinions.getAllPlacedMinions()) {
            if (minion.getMinionTier().getMinionType().getCategory().equalsIgnoreCase(category)) {
                placedCount++;
                efficiency += minion.getProductionRate();
            }
        }
        
        totalCount = playerMinions.getTotalMinionCountByCategory(category);
        
        String rarity = getRarityForCategory(placedCount);
        String[] lore = {
            "&7" + category + " Minions",
            "&7Placed: &a" + placedCount,
            "&7Total: &a" + totalCount,
            "&7Efficiency: &a" + String.format("%.2f", efficiency) + "/hour",
            "",
            "&7Click to view " + category.toLowerCase() + " minions",
            "&eKlicke zum Öffnen"
        };
        
        setItem(slot, material, color + icon + " " + category + " Minions", rarity, lore);
    }
    
    private void setupStatisticsItem(int slot, PlayerMinions playerMinions) {
        PlayerMinions.MinionStatistics stats = playerMinions.getMinionStatistics();
        
        String rarity = getRarityForStats(stats.getTotalPlacedMinions());
        String[] lore = {
            "&7Minion Statistics",
            "",
            "&7Placed Minions: &a" + stats.getTotalPlacedMinions() + "/" + playerMinions.getMaxMinionSlots(),
            "&7Total Minions: &a" + stats.getTotalMinionCount(),
            "&7Total Efficiency: &a" + String.format("%.2f", stats.getTotalEfficiency()) + "/hour",
            "&7Daily Production: &a" + String.format("%.0f", stats.getDailyProduction()) + " items",
            "&7Weekly Production: &a" + String.format("%.0f", stats.getWeeklyProduction()) + " items",
            "",
            "&7Click to view detailed statistics",
            "&eKlicke zum Öffnen"
        };
        
        setItem(slot, Material.BOOK, "&6📊 Minion Statistics", rarity, lore);
    }
    
    private String getRarityForSlots(int placed, int max) {
        double percentage = (double) placed / max;
        if (percentage >= 1.0) return "mythic";
        if (percentage >= 0.8) return "legendary";
        if (percentage >= 0.6) return "epic";
        if (percentage >= 0.4) return "rare";
        if (percentage >= 0.2) return "uncommon";
        return "common";
    }
    
    private String getRarityForCategory(int count) {
        if (count >= 10) return "mythic";
        if (count >= 7) return "legendary";
        if (count >= 5) return "epic";
        if (count >= 3) return "rare";
        if (count >= 1) return "uncommon";
        return "common";
    }
    
    private String getRarityForStats(int count) {
        if (count >= 20) return "mythic";
        if (count >= 15) return "legendary";
        if (count >= 10) return "epic";
        if (count >= 5) return "rare";
        if (count >= 1) return "uncommon";
        return "common";
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        // Get player minions data
        PlayerMinions playerMinions = plugin.getMinionsSystem().getPlayerMinions(player);
        if (playerMinions == null) {
            player.sendMessage("§cError loading minions data!");
            return;
        }
        
        switch (slot) {
            case 4:
                showMinionSlotsInfo(playerMinions);
                break;
            case 19:
                showCategoryDetails("Farming", playerMinions);
                break;
            case 21:
                showCategoryDetails("Mining", playerMinions);
                break;
            case 23:
                showCategoryDetails("Foraging", playerMinions);
                break;
            case 25:
                showCategoryDetails("Fishing", playerMinions);
                break;
            case 28:
                showCategoryDetails("Combat", playerMinions);
                break;
            case 30:
                showCategoryDetails("Special", playerMinions);
                break;
            case 40:
                showDetailedStatistics(playerMinions);
                break;
            case 49:
                close();
                break;
        }
    }
    
    private void showMinionSlotsInfo(PlayerMinions playerMinions) {
        player.sendMessage("§8§m--------------------------------");
        player.sendMessage("§6⚡ Minion Slots");
        player.sendMessage("§8§m--------------------------------");
        player.sendMessage("§7Placed Minions: §a" + playerMinions.getTotalPlacedMinions() + "/" + playerMinions.getMaxMinionSlots());
        player.sendMessage("§7Available Slots: §a" + playerMinions.getAvailableMinionSlots());
        player.sendMessage("");
        player.sendMessage("§7Minion slots allow you to place");
        player.sendMessage("§7and manage automated minions");
        player.sendMessage("§7that collect resources for you.");
        player.sendMessage("");
        player.sendMessage("§7To get more minion slots:");
        player.sendMessage("§7- Complete minion quests");
        player.sendMessage("§7- Purchase from the community shop");
        player.sendMessage("§7- Reach certain collection milestones");
        player.sendMessage("§8§m--------------------------------");
    }
    
    private void showCategoryDetails(String category, PlayerMinions playerMinions) {
        player.sendMessage("§8§m--------------------------------");
        player.sendMessage("§6" + category + " Minions");
        player.sendMessage("§8§m--------------------------------");
        
        // Get minions in this category
        MinionType[] minionTypes = MinionType.getByCategory(category);
        if (minionTypes.length == 0) {
            player.sendMessage("§7No minions found for this category.");
            return;
        }
        
        int placedCount = 0;
        int totalCount = 0;
        double efficiency = 0.0;
        
        for (MinionType minionType : minionTypes) {
            int count = playerMinions.getMinionCount(minionType);
            int placed = playerMinions.getPlacedMinionsByType(minionType).size();
            
            if (count > 0 || placed > 0) {
                placedCount += placed;
                totalCount += count;
                
                // Calculate efficiency for placed minions
                for (PlacedMinion minion : playerMinions.getPlacedMinionsByType(minionType)) {
                    efficiency += minion.getProductionRate();
                }
                
                String status = placed > 0 ? "§a✓ Placed (" + placed + ")" : "§7Not Placed";
                player.sendMessage(minionType.getColor() + minionType.getIcon() + " " + minionType.getDisplayName() + 
                    " §7- " + status + " §7(Total: " + count + ")");
            }
        }
        
        player.sendMessage("");
        player.sendMessage("§7Category Summary:");
        player.sendMessage("§7Placed: §a" + placedCount);
        player.sendMessage("§7Total: §a" + totalCount);
        player.sendMessage("§7Efficiency: §a" + String.format("%.2f", efficiency) + "/hour");
        player.sendMessage("§8§m--------------------------------");
    }
    
    private void showDetailedStatistics(PlayerMinions playerMinions) {
        PlayerMinions.MinionStatistics stats = playerMinions.getMinionStatistics();
        
        player.sendMessage("§8§m--------------------------------");
        player.sendMessage("§6📊 Detailed Minion Statistics");
        player.sendMessage("§8§m--------------------------------");
        
        // Basic stats
        player.sendMessage("§7Placed Minions: §a" + stats.getTotalPlacedMinions() + "/" + playerMinions.getMaxMinionSlots());
        player.sendMessage("§7Total Minions: §a" + stats.getTotalMinionCount());
        player.sendMessage("§7Total Efficiency: §a" + String.format("%.2f", stats.getTotalEfficiency()) + "/hour");
        player.sendMessage("");
        
        // Production stats
        player.sendMessage("§7Production Estimates:");
        player.sendMessage("§7Daily: §a" + String.format("%.0f", stats.getDailyProduction()) + " items");
        player.sendMessage("§7Weekly: §a" + String.format("%.0f", stats.getWeeklyProduction()) + " items");
        player.sendMessage("§7Monthly: §a" + String.format("%.0f", stats.getMonthlyProduction()) + " items");
        player.sendMessage("");
        
        // Category breakdown
        player.sendMessage("§7Category Breakdown:");
        for (String category : new String[]{"Farming", "Mining", "Foraging", "Fishing", "Combat", "Special"}) {
            int placed = 0;
            double efficiency = 0.0;
            
            for (PlacedMinion minion : playerMinions.getAllPlacedMinions()) {
                if (minion.getMinionTier().getMinionType().getCategory().equalsIgnoreCase(category)) {
                    placed++;
                    efficiency += minion.getProductionRate();
                }
            }
            
            if (placed > 0) {
                player.sendMessage("§7" + category + ": §a" + placed + " minions, §a" + String.format("%.2f", efficiency) + "/hour");
            }
        }
        
        player.sendMessage("§8§m--------------------------------");
    }
}
