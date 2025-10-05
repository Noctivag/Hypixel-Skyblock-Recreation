package de.noctivag.skyblock.auction;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
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
import java.util.stream.Collectors;

/**
 * Dark Auction System - Hypixel Skyblock Style
 */
public class DarkAuctionSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerDarkAuctionData> playerDarkAuctionData = new ConcurrentHashMap<>();
    private final Map<DarkAuctionItem, DarkAuctionConfig> auctionConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> auctionTasks = new ConcurrentHashMap<>();
    
    public DarkAuctionSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeAuctionConfigs();
        startDarkAuctionUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeAuctionConfigs() {
        auctionConfigs.put(DarkAuctionItem.DARK_ARTIFACT, new DarkAuctionConfig(
            "Dark Artifact", "§5Dark Artifact", Material.NETHER_STAR,
            "§7A powerful dark artifact.",
            DarkAuctionCategory.ARTIFACTS, DarkAuctionRarity.LEGENDARY, 1000000,
            Arrays.asList("§7- Powerful dark magic", "§7- Unique abilities"),
            Arrays.asList("§7- 1,000,000 Coins", "§7- Dark Auction only")
        ));
        
        auctionConfigs.put(DarkAuctionItem.SHADOW_FURY, new DarkAuctionConfig(
            "Shadow Fury", "§5Shadow Fury", Material.DIAMOND_SWORD,
            "§7A legendary shadow sword.",
            DarkAuctionCategory.WEAPONS, DarkAuctionRarity.LEGENDARY, 500000,
            Arrays.asList("§7- High damage", "§7- Shadow abilities"),
            Arrays.asList("§7- 500,000 Coins", "§7- Dark Auction only")
        ));
        
        auctionConfigs.put(DarkAuctionItem.DARK_ARMOR, new DarkAuctionConfig(
            "Dark Armor", "§5Dark Armor", Material.NETHERITE_CHESTPLATE,
            "§7A powerful dark armor set.",
            DarkAuctionCategory.ARMOR, DarkAuctionRarity.EPIC, 750000,
            Arrays.asList("§7- High defense", "§7- Dark protection"),
            Arrays.asList("§7- 750,000 Coins", "§7- Dark Auction only")
        ));
    }
    
    private void startDarkAuctionUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerDarkAuctionData> entry : playerDarkAuctionData.entrySet()) {
                    PlayerDarkAuctionData auctionData = entry.getValue();
                    auctionData.update();
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L * 60L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Dark Auction")) {
            openDarkAuctionGUI(player);
        }
    }
    
    public void openDarkAuctionGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§5§lDark Auction"));
        
        addGUIItem(gui, 10, Material.NETHER_STAR, "§5§lDark Artifact", "§7A powerful dark artifact.");
        addGUIItem(gui, 11, Material.DIAMOND_SWORD, "§5§lShadow Fury", "§7A legendary shadow sword.");
        addGUIItem(gui, 12, Material.NETHERITE_CHESTPLATE, "§5§lDark Armor", "§7A powerful dark armor set.");
        
        player.openInventory(gui);
        player.sendMessage(Component.text("§aDark Auction GUI geöffnet!"));
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
    
    public PlayerDarkAuctionData getPlayerDarkAuctionData(UUID playerId) {
        return playerDarkAuctionData.computeIfAbsent(playerId, k -> new PlayerDarkAuctionData(playerId));
    }
    
    public enum DarkAuctionItem {
        DARK_ARTIFACT, SHADOW_FURY, DARK_ARMOR, DARK_BOW, DARK_STAFF,
        SHADOW_CRYSTAL, DARK_GEM, SHADOW_ESSENCE, DARK_POWDER
    }
    
    public enum DarkAuctionCategory {
        ARTIFACTS("§5Artifacts", "§7Dark artifacts"),
        WEAPONS("§cWeapons", "§7Dark weapons"),
        ARMOR("§9Armor", "§7Dark armor"),
        ACCESSORIES("§6Accessories", "§7Dark accessories"),
        CONSUMABLES("§aConsumables", "§7Dark consumables");
        
        private final String displayName;
        private final String description;
        
        DarkAuctionCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum DarkAuctionRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        DarkAuctionRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class DarkAuctionConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final DarkAuctionCategory category;
        private final DarkAuctionRarity rarity;
        private final int basePrice;
        private final List<String> features;
        private final List<String> requirements;
        
        public DarkAuctionConfig(String name, String displayName, Material icon, String description,
                                DarkAuctionCategory category, DarkAuctionRarity rarity, int basePrice,
                                List<String> features, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
            this.category = category;
            this.rarity = rarity;
            this.basePrice = basePrice;
            this.features = features;
            this.requirements = requirements;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public DarkAuctionCategory getCategory() { return category; }
        public DarkAuctionRarity getRarity() { return rarity; }
        public int getBasePrice() { return basePrice; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerDarkAuctionData {
        private final UUID playerId;
        private final Map<DarkAuctionItem, Integer> purchasedItems = new HashMap<>();
        private final Map<DarkAuctionItem, Integer> bidHistory = new HashMap<>();
        private long lastUpdate;
        
        public PlayerDarkAuctionData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void addPurchasedItem(DarkAuctionItem item, int amount) {
            purchasedItems.put(item, purchasedItems.getOrDefault(item, 0) + amount);
        }
        
        public void addBid(DarkAuctionItem item, int amount) {
            bidHistory.put(item, bidHistory.getOrDefault(item, 0) + amount);
        }
        
        public int getPurchasedItems(DarkAuctionItem item) {
            return purchasedItems.getOrDefault(item, 0);
        }
        
        public int getBidHistory(DarkAuctionItem item) {
            return bidHistory.getOrDefault(item, 0);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
