package de.noctivag.plugin.shop;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Community Shop System - Hypixel Skyblock Style
 */
public class CommunityShopSystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerCommunityShopData> playerCommunityShopData = new ConcurrentHashMap<>();
    private final Map<CommunityShopItem, CommunityShopConfig> shopConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> shopTasks = new ConcurrentHashMap<>();
    
    public CommunityShopSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeShopConfigs();
        startCommunityShopUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeShopConfigs() {
        shopConfigs.put(CommunityShopItem.COMMUNITY_UPGRADE, new CommunityShopConfig(
            "Community Upgrade", "§aCommunity Upgrade", Material.EMERALD,
            "§7Upgrade the community with various improvements.",
            CommunityShopCategory.UPGRADES, CommunityShopRarity.COMMON, 10000,
            Arrays.asList("§7- Community benefits", "§7- Shared upgrades"),
            Arrays.asList("§7- 10,000 Coins", "§7- Community contribution")
        ));
        
        shopConfigs.put(CommunityShopItem.SHARED_STORAGE, new CommunityShopConfig(
            "Shared Storage", "§bShared Storage", Material.CHEST,
            "§7Access to shared community storage.",
            CommunityShopCategory.STORAGE, CommunityShopRarity.COMMON, 5000,
            Arrays.asList("§7- Shared storage", "§7- Community access"),
            Arrays.asList("§7- 5,000 Coins", "§7- Community contribution")
        ));
        
        shopConfigs.put(CommunityShopItem.COMMUNITY_EVENT, new CommunityShopConfig(
            "Community Event", "§6Community Event", Material.FIREWORK_ROCKET,
            "§7Host a community event.",
            CommunityShopCategory.EVENTS, CommunityShopRarity.RARE, 25000,
            Arrays.asList("§7- Community events", "§7- Shared rewards"),
            Arrays.asList("§7- 25,000 Coins", "§7- Community contribution")
        ));
    }
    
    private void startCommunityShopUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerCommunityShopData> entry : playerCommunityShopData.entrySet()) {
                    PlayerCommunityShopData shopData = entry.getValue();
                    shopData.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L * 60L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Community Shop")) {
            openCommunityShopGUI(player);
        }
    }
    
    public void openCommunityShopGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§a§lCommunity Shop");
        
        addGUIItem(gui, 10, Material.EMERALD, "§a§lCommunity Upgrade", "§7Upgrade the community.");
        addGUIItem(gui, 11, Material.CHEST, "§b§lShared Storage", "§7Access shared storage.");
        addGUIItem(gui, 12, Material.FIREWORK_ROCKET, "§6§lCommunity Event", "§7Host community events.");
        
        player.openInventory(gui);
        player.sendMessage("§aCommunity Shop GUI geöffnet!");
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(description));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public PlayerCommunityShopData getPlayerCommunityShopData(UUID playerId) {
        return playerCommunityShopData.computeIfAbsent(playerId, k -> new PlayerCommunityShopData(playerId));
    }
    
    public enum CommunityShopItem {
        COMMUNITY_UPGRADE, SHARED_STORAGE, COMMUNITY_EVENT, COMMUNITY_FARM,
        COMMUNITY_MINE, COMMUNITY_BAZAAR, COMMUNITY_AUCTION, COMMUNITY_BANK
    }
    
    public enum CommunityShopCategory {
        UPGRADES("§aUpgrades", "§7Community upgrades"),
        STORAGE("§bStorage", "§7Shared storage"),
        EVENTS("§6Events", "§7Community events"),
        FARMING("§2Farming", "§7Community farming"),
        MINING("§7Mining", "§7Community mining"),
        TRADING("§eTrading", "§7Community trading");
        
        private final String displayName;
        private final String description;
        
        CommunityShopCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum CommunityShopRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0);
        
        private final String displayName;
        private final double multiplier;
        
        CommunityShopRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class CommunityShopConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final CommunityShopCategory category;
        private final CommunityShopRarity rarity;
        private final int price;
        private final List<String> features;
        private final List<String> requirements;
        
        public CommunityShopConfig(String name, String displayName, Material icon, String description,
                                 CommunityShopCategory category, CommunityShopRarity rarity, int price,
                                 List<String> features, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
            this.category = category;
            this.rarity = rarity;
            this.price = price;
            this.features = features;
            this.requirements = requirements;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public CommunityShopCategory getCategory() { return category; }
        public CommunityShopRarity getRarity() { return rarity; }
        public int getPrice() { return price; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerCommunityShopData {
        private final UUID playerId;
        private final Map<CommunityShopItem, Integer> purchasedItems = new HashMap<>();
        private final Map<CommunityShopItem, Integer> contributionHistory = new HashMap<>();
        private long lastUpdate;
        
        public PlayerCommunityShopData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void addPurchasedItem(CommunityShopItem item, int amount) {
            purchasedItems.put(item, purchasedItems.getOrDefault(item, 0) + amount);
        }
        
        public void addContribution(CommunityShopItem item, int amount) {
            contributionHistory.put(item, contributionHistory.getOrDefault(item, 0) + amount);
        }
        
        public int getPurchasedItems(CommunityShopItem item) {
            return purchasedItems.getOrDefault(item, 0);
        }
        
        public int getContributionHistory(CommunityShopItem item) {
            return contributionHistory.getOrDefault(item, 0);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
