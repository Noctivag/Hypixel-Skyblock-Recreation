package de.noctivag.skyblock.minions;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Minion Management System - Erweiterte Minion-Verwaltung
 * 
 * Features:
 * - Minion Placement und Removal
 * - Minion Upgrades Management
 * - Minion Fuel Management
 * - Minion Storage Management
 * - Minion Auto-Sell Configuration
 * - Minion Statistics
 */
public class MinionManagementSystem implements Listener {
    
    private final SkyblockPlugin plugin;
    private final AdvancedMinionSystem minionSystem;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, MinionPlacement> minionPlacements = new ConcurrentHashMap<>();
    private final Map<UUID, MinionStatistics> minionStats = new ConcurrentHashMap<>();
    
    public MinionManagementSystem(SkyblockPlugin plugin, AdvancedMinionSystem minionSystem, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.minionSystem = minionSystem;
        this.databaseManager = databaseManager;
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    public Plugin getPlugin() { return plugin; }
    public MultiServerDatabaseManager getDatabaseManager() { return databaseManager; }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().title().toString();
        
        if (title.contains("Minions")) {
            event.setCancelled(true);
            handleMinionGUIClick(player, event.getSlot());
        }
    }
    
    private void handleMinionGUIClick(Player player, int slot) {
        switch (slot) {
            case 18: // My Minions
                openMyMinionsGUI(player);
                break;
            case 19: // Upgrades
                openUpgradesGUI(player);
                break;
            case 20: // Fuel
                openFuelGUI(player);
                break;
            case 21: // Storage
                openStorageGUI(player);
                break;
            case 22: // Auto-Sell
                openAutoSellGUI(player);
                break;
        }
    }
    
    public void openMyMinionsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lMy Minions"));
        
        List<AdvancedMinionSystem.Minion> playerMinions = minionSystem.getPlayerMinions(player.getUniqueId());
        
        int slot = 0;
        for (AdvancedMinionSystem.Minion minion : playerMinions) {
            if (slot >= 45) break; // Max 45 slots for minions
            
            ItemStack minionItem = createMinionItem(minion);
            gui.setItem(slot, minionItem);
            slot++;
        }
        
        // Add placeholder message if no minions
        if (playerMinions.isEmpty()) {
            addGUIItem(gui, 22, Material.BARRIER, "§c§lNo Minions", "§7You don't have any minions yet!");
        }
        
        // Add management buttons
        addGUIItem(gui, 45, Material.EMERALD, "§a§lPlace Minion", "§7Place a minion on your island.");
        addGUIItem(gui, 46, Material.REDSTONE, "§c§lRemove Minion", "§7Remove a minion from your island.");
        addGUIItem(gui, 47, Material.ANVIL, "§e§lUpgrade Minion", "§7Upgrade your minion.");
        addGUIItem(gui, 48, Material.COAL, "§8§lAdd Fuel", "§7Add fuel to your minion.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the minion menu.");
        addGUIItem(gui, 50, Material.CHEST, "§6§lCollect Resources", "§7Collect resources from your minions.");
        addGUIItem(gui, 51, Material.HOPPER, "§7§lAuto-Sell", "§7Configure auto-selling.");
        addGUIItem(gui, 52, Material.BOOK, "§b§lStatistics", "§7View minion statistics.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lBack", "§7Go back to main menu.");
        
        player.openInventory(gui);
    }
    
    public void openUpgradesGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§e§lMinion Upgrades"));
        
        // Add upgrade types
        addGUIItem(gui, 10, Material.REDSTONE, "§a§lSpeed Upgrade", "§7Increases minion speed by 10%");
        addGUIItem(gui, 11, Material.CHEST, "§b§lStorage Upgrade", "§7Increases minion storage capacity");
        addGUIItem(gui, 12, Material.COMPARATOR, "§e§lCompactor", "§7Compacts items automatically");
        addGUIItem(gui, 13, Material.DIAMOND_BLOCK, "§6§lSuper Compactor", "§7Super compacts items automatically");
        addGUIItem(gui, 14, Material.FURNACE, "§c§lAuto Smelter", "§7Smelts items automatically");
        addGUIItem(gui, 15, Material.DIAMOND, "§b§lDiamond Spreading", "§7Generates diamonds");
        addGUIItem(gui, 16, Material.HOPPER, "§7§lBudget Hopper", "§7Auto-sells items");
        addGUIItem(gui, 17, Material.ENCHANTED_BOOK, "§d§lEnchanted Hopper", "§7Auto-sells items with better prices");
        
        // Add navigation
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the upgrades menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lBack", "§7Go back to main menu.");
        
        player.openInventory(gui);
    }
    
    public void openFuelGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§8§lMinion Fuel"));
        
        // Add fuel types
        addGUIItem(gui, 10, Material.COAL, "§8§lCoal", "§7+5% Speed for 1 hour");
        addGUIItem(gui, 11, Material.COAL_BLOCK, "§8§lEnchanted Coal", "§7+10% Speed for 2 hours");
        addGUIItem(gui, 12, Material.CHARCOAL, "§8§lEnchanted Charcoal", "§7+15% Speed for 3 hours");
        addGUIItem(gui, 13, Material.LAVA_BUCKET, "§c§lEnchanted Lava Bucket", "§7+25% Speed for 24 hours");
        addGUIItem(gui, 14, Material.COAL_BLOCK, "§8§lEnchanted Coal Block", "§7+50% Speed for 48 hours");
        
        // Add navigation
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the fuel menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lBack", "§7Go back to main menu.");
        
        player.openInventory(gui);
    }
    
    public void openStorageGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§7§lMinion Storage"));
        
        List<AdvancedMinionSystem.Minion> playerMinions = minionSystem.getPlayerMinions(player.getUniqueId());
        
        int slot = 0;
        for (AdvancedMinionSystem.Minion minion : playerMinions) {
            if (slot >= 45) break;
            
            AdvancedMinionSystem.PlayerMinionData data = minionSystem.getPlayerMinionData(player.getUniqueId());
            Map<String, Integer> resources = data.getMinionResources(minion.getName());
            
            ItemStack storageItem = createStorageItem(minion, resources);
            gui.setItem(slot, storageItem);
            slot++;
        }
        
        // Add management buttons
        addGUIItem(gui, 45, Material.CHEST, "§6§lCollect All", "§7Collect resources from all minions.");
        addGUIItem(gui, 46, Material.HOPPER, "§7§lAuto-Collect", "§7Enable auto-collection.");
        addGUIItem(gui, 47, Material.EMERALD, "§a§lAuto-Sell", "§7Enable auto-selling.");
        addGUIItem(gui, 48, Material.REDSTONE, "§c§lDisable Auto-Sell", "§7Disable auto-selling.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the storage menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lBack", "§7Go back to main menu.");
        
        player.openInventory(gui);
    }
    
    public void openAutoSellGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§a§lAuto-Sell Configuration"));
        
        // Add auto-sell options
        addGUIItem(gui, 10, Material.EMERALD, "§a§lEnable Auto-Sell", "§7Enable auto-selling for all minions");
        addGUIItem(gui, 11, Material.REDSTONE, "§c§lDisable Auto-Sell", "§7Disable auto-selling for all minions");
        addGUIItem(gui, 12, Material.GOLD_INGOT, "§6§lSet Prices", "§7Configure auto-sell prices");
        addGUIItem(gui, 13, Material.CLOCK, "§e§lAuto-Sell Timer", "§7Configure auto-sell frequency");
        addGUIItem(gui, 14, Material.BOOK, "§b§lAuto-Sell Log", "§7View auto-sell history");
        
        // Add navigation
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the auto-sell menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lBack", "§7Go back to main menu.");
        
        player.openInventory(gui);
    }
    
    private ItemStack createMinionItem(AdvancedMinionSystem.Minion minion) {
        ItemStack item = new ItemStack(minion.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(minion.getDisplayName()));
            
            List<String> lore = new ArrayList<>();
            lore.add(minion.getDescription());
            lore.add("");
            lore.add("§7Type: " + minion.getType().getDisplayName());
            lore.add("§7Rarity: " + minion.getRarity().getDisplayName());
            lore.add("§7Level: §e" + minion.getLevel());
            lore.add("§7Status: " + (minion.isActive() ? "§aActive" : "§cInactive"));
            lore.add("§7Auto-Sell: " + (minion.isAutoSellEnabled() ? "§aEnabled" : "§cDisabled"));
            lore.add("");
            lore.add("§7Resources:");
            for (String resource : minion.getResources()) {
                lore.add("  " + resource);
            }
            lore.add("");
            lore.add("§eClick to manage this minion!");
            
            meta.lore(lore.stream().map(Component::text).toList());
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    private ItemStack createStorageItem(AdvancedMinionSystem.Minion minion, Map<String, Integer> resources) {
        ItemStack item = new ItemStack(minion.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6" + minion.getName() + " Storage"));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Storage capacity: §e" + resources.size() + "§7/§e" + minion.getMaxStorage());
            lore.add("");
            
            if (resources.isEmpty()) {
                lore.add("§7No resources stored.");
            } else {
                lore.add("§7Stored resources:");
                for (Map.Entry<String, Integer> entry : resources.entrySet()) {
                    lore.add("  §7- " + entry.getKey() + ": §e" + entry.getValue());
                }
            }
            
            lore.add("");
            lore.add("§eClick to collect resources!");
            
            meta.lore(lore.stream().map(Component::text).toList());
            item.setItemMeta(meta);
        }
        
        return item;
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
    
    // Minion Placement System
    public static class MinionPlacement {
        private final String minionId;
        private final UUID playerId;
        private final String worldName;
        private final double x, y, z;
        private final long placedAt;
        
        public MinionPlacement(String minionId, UUID playerId, String worldName, double x, double y, double z) {
            this.minionId = minionId;
            this.playerId = playerId;
            this.worldName = worldName;
            this.x = x;
            this.y = y;
            this.z = z;
            this.placedAt = System.currentTimeMillis();
        }
        
        public String getMinionId() { return minionId; }
        public UUID getPlayerId() { return playerId; }
        public String getWorldName() { return worldName; }
        public double getX() { return x; }
        public double getY() { return y; }
        public double getZ() { return z; }
        public long getPlacedAt() { return placedAt; }
    }
    
    // Minion Statistics System
    public static class MinionStatistics {
        private final UUID playerId;
        private final Map<String, Long> totalResourcesProduced = new HashMap<>();
        private final Map<String, Double> totalCoinsEarned = new HashMap<>();
        private final Map<String, Long> totalUptime = new HashMap<>();
        private final Map<String, Integer> totalUpgrades = new HashMap<>();
        private final Map<String, Integer> totalFuelUsed = new HashMap<>();
        
        public MinionStatistics(UUID playerId) {
            this.playerId = playerId;
        }
        
        public void addResourcesProduced(String minionName, long amount) {
            totalResourcesProduced.put(minionName, totalResourcesProduced.getOrDefault(minionName, 0L) + amount);
        }
        
        public void addCoinsEarned(String minionName, double amount) {
            totalCoinsEarned.put(minionName, totalCoinsEarned.getOrDefault(minionName, 0.0) + amount);
        }
        
        public void addUptime(String minionName, long time) {
            totalUptime.put(minionName, totalUptime.getOrDefault(minionName, 0L) + time);
        }
        
        public void addUpgrade(String minionName) {
            totalUpgrades.put(minionName, totalUpgrades.getOrDefault(minionName, 0) + 1);
        }
        
        public void addFuelUsed(String minionName) {
            totalFuelUsed.put(minionName, totalFuelUsed.getOrDefault(minionName, 0) + 1);
        }
        
        public Map<String, Long> getTotalResourcesProduced() { return new HashMap<>(totalResourcesProduced); }
        public Map<String, Double> getTotalCoinsEarned() { return new HashMap<>(totalCoinsEarned); }
        public Map<String, Long> getTotalUptime() { return new HashMap<>(totalUptime); }
        public Map<String, Integer> getTotalUpgrades() { return new HashMap<>(totalUpgrades); }
        public Map<String, Integer> getTotalFuelUsed() { return new HashMap<>(totalFuelUsed); }
        
        public UUID getPlayerId() { return playerId; }
    }
    
    public MinionPlacement getMinionPlacement(String minionId) {
        return minionPlacements.get(UUID.fromString(minionId));
    }
    
    public void placeMinion(UUID playerId, AdvancedMinionSystem.Minion minion, String worldName, double x, double y, double z) {
        MinionPlacement placement = new MinionPlacement(minion.getId(), playerId, worldName, x, y, z);
        minionPlacements.put(UUID.fromString(minion.getId()), placement);
    }
    
    public void removeMinion(String minionId) {
        minionPlacements.remove(UUID.fromString(minionId));
    }
    
    public MinionStatistics getMinionStatistics(UUID playerId) {
        return minionStats.computeIfAbsent(playerId, k -> new MinionStatistics(playerId));
    }
    
    public void updateMinionStatistics(UUID playerId, String minionName, long resourcesProduced, double coinsEarned) {
        MinionStatistics stats = getMinionStatistics(playerId);
        stats.addResourcesProduced(minionName, resourcesProduced);
        stats.addCoinsEarned(minionName, coinsEarned);
    }
}
