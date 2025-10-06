package de.noctivag.skyblock.collections;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.collections.CollectionType;
import de.noctivag.skyblock.collections.PlayerCollections;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.stream.Collectors;


// Collection Reward Class
class CollectionReward {
    private final int requiredAmount;
    private final String description;
    private final String rewardId;
    
    public CollectionReward(int requiredAmount, String description, String rewardId) {
        this.requiredAmount = requiredAmount;
        this.description = description;
        this.rewardId = rewardId;
    }
    
    public int getRequiredAmount() { return requiredAmount; }
    public String getDescription() { return description; }
    public String getRewardId() { return rewardId; }
}

/**
 * Collections GUI System - Vollständige Collections-Anzeige
 * 
 * Features:
 * - Alle Collection-Typen anzeigen
 * - Collection-Progress und Belohnungen
 * - Collection-Rezepte
 * - Collection-Statistiken
 * - Collection-Kategorien
 */
public class CollectionsGUI implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final CollectionsSystem collectionsSystem;
    
    public CollectionsGUI(SkyblockPlugin SkyblockPlugin, CollectionsSystem collectionsSystem) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.collectionsSystem = collectionsSystem;
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    public SkyblockPlugin getPlugin() { return SkyblockPlugin; }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().title().toString();
        
        if (title.contains("Collections")) {
            event.setCancelled(true);
            handleCollectionsGUIClick(player, event.getSlot());
        }
    }
    
    private void handleCollectionsGUIClick(Player player, int slot) {
        switch (slot) {
            case 10:
                openFarmingCollectionsGUI(player);
                break;
            case 11:
                openMiningCollectionsGUI(player);
                break;
            case 12:
                openCombatCollectionsGUI(player);
                break;
            case 13:
                openForagingCollectionsGUI(player);
                break;
            case 14:
                openFishingCollectionsGUI(player);
                break;
            case 49:
                player.closeInventory();
                break;
            case 53:
                openCollectionsStatisticsGUI(player);
                break;
        }
    }
    
    public void openCollectionsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§b§lCollections"));
        
        // Add collection categories
        addGUIItem(gui, 10, Material.WHEAT, "§a§lFarming Collections", "§7View farming collections and rewards.");
        addGUIItem(gui, 11, Material.DIAMOND_PICKAXE, "§6§lMining Collections", "§7View mining collections and rewards.");
        addGUIItem(gui, 12, Material.DIAMOND_SWORD, "§c§lCombat Collections", "§7View combat collections and rewards.");
        addGUIItem(gui, 13, Material.OAK_LOG, "§2§lForaging Collections", "§7View foraging collections and rewards.");
        addGUIItem(gui, 14, Material.FISHING_ROD, "§b§lFishing Collections", "§7View fishing collections and rewards.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the collections menu.");
        addGUIItem(gui, 53, Material.BOOK, "§b§lStatistics", "§7View collection statistics.");
        
        player.openInventory(gui);
        player.sendMessage(Component.text("§aCollections GUI geöffnet!"));
    }
    
    public void openCategoryGUI(Player player, String category) {
        switch (category.toLowerCase()) {
            case "farming":
                openFarmingCollectionsGUI(player);
                break;
            case "mining":
                openMiningCollectionsGUI(player);
                break;
            case "combat":
                openCombatCollectionsGUI(player);
                break;
            case "foraging":
                openForagingCollectionsGUI(player);
                break;
            case "fishing":
                openFishingCollectionsGUI(player);
                break;
            default:
                openCollectionsGUI(player);
                break;
        }
    }
    
    public void openFarmingCollectionsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§a§lFarming Collections"));
        
        // Add farming collections
        addCollectionItem(gui, 10, CollectionType.WHEAT, player);
        addCollectionItem(gui, 11, CollectionType.CARROT, player);
        addCollectionItem(gui, 12, CollectionType.POTATO, player);
        addCollectionItem(gui, 13, CollectionType.PUMPKIN, player);
        addCollectionItem(gui, 14, CollectionType.MELON, player);
        addCollectionItem(gui, 15, CollectionType.SEEDS, player);
        addCollectionItem(gui, 16, CollectionType.MUSHROOM, player);
        addCollectionItem(gui, 17, CollectionType.CACTUS, player);
        
        addCollectionItem(gui, 19, CollectionType.SUGAR_CANE, player);
        addCollectionItem(gui, 20, CollectionType.NETHER_WART, player);
        addCollectionItem(gui, 21, CollectionType.COCOA_BEANS, player);
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the farming collections menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lBack", "§7Go back to collections menu.");
        
        player.openInventory(gui);
    }
    
    public void openMiningCollectionsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lMining Collections"));
        
        // Add mining collections
        addCollectionItem(gui, 10, CollectionType.COBBLESTONE, player);
        addCollectionItem(gui, 11, CollectionType.COAL, player);
        addCollectionItem(gui, 12, CollectionType.IRON, player);
        addCollectionItem(gui, 13, CollectionType.GOLD, player);
        addCollectionItem(gui, 14, CollectionType.DIAMOND, player);
        addCollectionItem(gui, 15, CollectionType.LAPIS_LAZULI, player);
        addCollectionItem(gui, 16, CollectionType.EMERALD, player);
        addCollectionItem(gui, 17, CollectionType.REDSTONE, player);
        
        addCollectionItem(gui, 19, CollectionType.QUARTZ, player);
        addCollectionItem(gui, 20, CollectionType.OBSIDIAN, player);
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the mining collections menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lBack", "§7Go back to collections menu.");
        
        player.openInventory(gui);
    }
    
    public void openCombatCollectionsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§c§lCombat Collections"));
        
        // Add combat collections
        addCollectionItem(gui, 10, CollectionType.ROTTEN_FLESH, player);
        addCollectionItem(gui, 11, CollectionType.BONE, player);
        addCollectionItem(gui, 12, CollectionType.STRING, player);
        addCollectionItem(gui, 13, CollectionType.SPIDER_EYE, player);
        addCollectionItem(gui, 14, CollectionType.ENDER_PEARL, player);
        addCollectionItem(gui, 15, CollectionType.GHAST_TEAR, player);
        addCollectionItem(gui, 16, CollectionType.BLAZE_ROD, player);
        addCollectionItem(gui, 17, CollectionType.MAGMA_CREAM, player);
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the combat collections menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lBack", "§7Go back to collections menu.");
        
        player.openInventory(gui);
    }
    
    public void openForagingCollectionsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§2§lForaging Collections"));
        
        // Add foraging collections
        addCollectionItem(gui, 10, CollectionType.OAK_LOG, player);
        addCollectionItem(gui, 11, CollectionType.BIRCH_LOG, player);
        addCollectionItem(gui, 12, CollectionType.SPRUCE_LOG, player);
        addCollectionItem(gui, 13, CollectionType.JUNGLE_LOG, player);
        addCollectionItem(gui, 14, CollectionType.ACACIA_LOG, player);
        addCollectionItem(gui, 15, CollectionType.DARK_OAK_LOG, player);
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the foraging collections menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lBack", "§7Go back to collections menu.");
        
        player.openInventory(gui);
    }
    
    public void openFishingCollectionsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§b§lFishing Collections"));
        
        // Add fishing collections
        addCollectionItem(gui, 10, CollectionType.RAW_FISH, player);
        addCollectionItem(gui, 11, CollectionType.RAW_SALMON, player);
        addCollectionItem(gui, 12, CollectionType.CLOWNFISH, player);
        addCollectionItem(gui, 13, CollectionType.PUFFERFISH, player);
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the fishing collections menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lBack", "§7Go back to collections menu.");
        
        player.openInventory(gui);
    }
    
    public void openCollectionsStatisticsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§b§lCollection Statistics"));
        
        // Add total statistics
        addTotalStatisticsItem(gui, 22, player);
        
        // Add category statistics
        addCategoryStatisticsItem(gui, 10, "Farming", Material.WHEAT, player);
        addCategoryStatisticsItem(gui, 11, "Mining", Material.DIAMOND_PICKAXE, player);
        addCategoryStatisticsItem(gui, 12, "Combat", Material.DIAMOND_SWORD, player);
        addCategoryStatisticsItem(gui, 13, "Foraging", Material.OAK_LOG, player);
        addCategoryStatisticsItem(gui, 14, "Fishing", Material.FISHING_ROD, player);
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the statistics menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lBack", "§7Go back to collections menu.");
        
        player.openInventory(gui);
    }
    
    private void addCollectionItem(Inventory gui, int slot, CollectionType collectionType, Player player) {
        CollectionsSystem.CollectionConfig config = ((de.noctivag.skyblock.collections.CollectionsSystem) SkyblockPlugin.getCollectionsSystem()).getCollectionConfig(collectionType);
        if (config == null) return;
        
        ItemStack item = new ItemStack(config.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(config.getDisplayName()));
            
            List<String> lore = new ArrayList<>();
            lore.add(config.getDescription());
            lore.add("");
            
            // Get player collection data
            // PlayerCollections playerCollections = collectionsSystem.getPlayerCollections(player.getUniqueId());
            int amount = 0; // Placeholder - would need proper CollectionType mapping
            int nextReward = getNextRewardAmount(collectionType, amount);
            
            lore.add("§7Collected: §a" + amount);
            lore.add("§7Next Reward: §e" + nextReward);
            lore.add("");
            lore.add("§7Rewards:");
            for (CollectionReward reward : getCollectionRewards(collectionType)) {
                String status = amount >= reward.getRequiredAmount() ? "§a✓" : "§7✗";
                lore.add("  " + status + " §7" + reward.getDescription());
            }
            lore.add("");
            lore.add("§eClick to view details!");
            
            meta.lore(lore.stream().map(Component::text).toList());
            item.setItemMeta(meta);
        }
        
        gui.setItem(slot, item);
    }
    
    private void addTotalStatisticsItem(Inventory gui, int slot, Player player) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6§lTotal Collection Statistics"));
            
            List<String> lore = new ArrayList<>();
            
            // Get player collection data
            // PlayerCollections playerCollections = collectionsSystem.getPlayerCollections(player.getUniqueId());
            
            lore.add("§7Total Collections: §e" + getTotalCollections(null));
            lore.add("§7Total Items Collected: §a" + getTotalItemsCollected(null));
            lore.add("§7Collections Completed: §b" + getCompletedCollections(null));
            lore.add("§7Completion Rate: §d" + String.format("%.1f", getCompletionRate(null)) + "%");
            lore.add("");
            lore.add("§7Top Collections:");
            for (Map.Entry<CollectionType, Integer> entry : getTopCollections(null, 5).entrySet()) {
                lore.add("  §7" + entry.getKey().getDisplayName() + ": §e" + entry.getValue());
            }
            
            meta.lore(lore.stream().map(Component::text).toList());
            item.setItemMeta(meta);
        }
        
        gui.setItem(slot, item);
    }
    
    private void addCategoryStatisticsItem(Inventory gui, int slot, String category, Material material, Player player) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6§l" + category + " Statistics"));
            
            List<String> lore = new ArrayList<>();
            
            // Get player collection data
            // PlayerCollections playerCollections = collectionsSystem.getPlayerCollections(player.getUniqueId());
            
            lore.add("§7" + category + " Collections: §e" + getCategoryCollectionCount(category));
            lore.add("§7" + category + " Items: §a" + getCategoryItemCount(category, null));
            lore.add("§7" + category + " Completion: §b" + String.format("%.1f", getCategoryCompletion(category, null)) + "%");
            
            meta.lore(lore.stream().map(Component::text).toList());
            item.setItemMeta(meta);
        }
        
        gui.setItem(slot, item);
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(Component.text(description)));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    // Helper methods
    private int getNextRewardAmount(CollectionType collectionType, int currentAmount) {
        // This would be implemented based on the collection rewards
        return currentAmount + 100; // Placeholder
    }
    
    private List<CollectionReward> getCollectionRewards(CollectionType collectionType) {
        // This would be implemented based on the collection configs
        return Arrays.asList(
            new CollectionReward(100, "§a+1% XP Boost", "xp_boost_1"),
            new CollectionReward(500, "§a+2% XP Boost", "xp_boost_2"),
            new CollectionReward(1000, "§a+3% XP Boost", "xp_boost_3")
        );
    }
    
    private int getCategoryCollectionCount(String category) {
        // This would be implemented based on the collection types
        return switch (category) {
            case "Farming" -> 11;
            case "Mining" -> 10;
            case "Combat" -> 8;
            case "Foraging" -> 6;
            case "Fishing" -> 4;
            default -> 0;
        };
    }
    
    private int getCategoryItemCount(String category, PlayerCollections playerCollections) {
        // This would be implemented based on the collection types
        return 0; // Placeholder
    }
    
    private double getCategoryCompletion(String category, PlayerCollections playerCollections) {
        // This would be implemented based on the collection types
        return 0.0; // Placeholder
    }
    
    // Helper methods
    private int getTotalCollections(PlayerCollections playerCollections) {
        return CollectionType.values().length;
    }
    
    private int getTotalItemsCollected(PlayerCollections playerCollections) {
        int total = 0;
        // for (CollectionType collectionType : CollectionType.values()) {
        //     total += 0; // Placeholder - would need proper CollectionType mapping
        // }
        return total;
    }
    
    private int getCompletedCollections(PlayerCollections playerCollections) {
        int completed = 0;
        // for (CollectionType collectionType : CollectionType.values()) {
        //     if (0 >= 1000) { // Placeholder - would need proper CollectionType mapping
        //         completed++;
        //     }
        // }
        return completed;
    }
    
    private double getCompletionRate(PlayerCollections playerCollections) {
        int total = getTotalCollections(playerCollections);
        int completed = getCompletedCollections(playerCollections);
        return total > 0 ? (double) completed / total * 100 : 0.0;
    }
    
    private Map<CollectionType, Integer> getTopCollections(PlayerCollections playerCollections, int limit) {
        Map<CollectionType, Integer> topCollections = new HashMap<>();
        
        for (CollectionType collectionType : CollectionType.values()) {
            topCollections.put(collectionType, 0); // Placeholder - would need proper CollectionType mapping
        }
        
        return topCollections.entrySet().stream()
                .sorted(Map.Entry.<CollectionType, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);
    }
}
