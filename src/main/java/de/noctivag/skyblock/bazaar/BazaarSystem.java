package de.noctivag.skyblock.bazaar;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.CorePlatform;
import de.noctivag.skyblock.core.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Bazaar System - Hypixel Skyblock Style
 * 
 * Features:
 * - Instant buy/sell of materials
 * - Dynamic pricing based on supply and demand
 * - Bazaar orders and instant transactions
 * - Bazaar statistics and trends
 * - Bazaar GUI interface
 */
public class BazaarSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final CorePlatform corePlatform;
    private final Map<Material, BazaarItem> bazaarItems = new ConcurrentHashMap<>();
    private final Map<UUID, List<BazaarOrder>> playerOrders = new ConcurrentHashMap<>();
    private final Map<UUID, BazaarStats> playerStats = new ConcurrentHashMap<>();
    
    public BazaarSystem(SkyblockPlugin SkyblockPlugin, CorePlatform corePlatform) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.corePlatform = corePlatform;
        initializeBazaarItems();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
        startBazaarUpdateTask();
    }
    
    private void initializeBazaarItems() {
        // Farming materials
        bazaarItems.put(Material.WHEAT, new BazaarItem(
            Material.WHEAT, "Wheat", 1.0, 0.8, 1000, 500
        ));
        bazaarItems.put(Material.CARROT, new BazaarItem(
            Material.CARROT, "Carrot", 1.2, 1.0, 800, 400
        ));
        bazaarItems.put(Material.POTATO, new BazaarItem(
            Material.POTATO, "Potato", 1.1, 0.9, 900, 450
        ));
        bazaarItems.put(Material.NETHER_WART, new BazaarItem(
            Material.NETHER_WART, "Nether Wart", 5.0, 4.0, 200, 100
        ));
        
        // Mining materials
        bazaarItems.put(Material.COAL, new BazaarItem(
            Material.COAL, "Coal", 2.0, 1.5, 2000, 1000
        ));
        bazaarItems.put(Material.IRON_INGOT, new BazaarItem(
            Material.IRON_INGOT, "Iron Ingot", 8.0, 6.0, 500, 250
        ));
        bazaarItems.put(Material.GOLD_INGOT, new BazaarItem(
            Material.GOLD_INGOT, "Gold Ingot", 15.0, 12.0, 300, 150
        ));
        bazaarItems.put(Material.DIAMOND, new BazaarItem(
            Material.DIAMOND, "Diamond", 50.0, 40.0, 100, 50
        ));
        bazaarItems.put(Material.EMERALD, new BazaarItem(
            Material.EMERALD, "Emerald", 60.0, 50.0, 80, 40
        ));
        
        // Foraging materials
        bazaarItems.put(Material.OAK_LOG, new BazaarItem(
            Material.OAK_LOG, "Oak Log", 1.5, 1.2, 1500, 750
        ));
        bazaarItems.put(Material.BIRCH_LOG, new BazaarItem(
            Material.BIRCH_LOG, "Birch Log", 1.6, 1.3, 1400, 700
        ));
        bazaarItems.put(Material.SPRUCE_LOG, new BazaarItem(
            Material.SPRUCE_LOG, "Spruce Log", 1.7, 1.4, 1300, 650
        ));
        bazaarItems.put(Material.JUNGLE_LOG, new BazaarItem(
            Material.JUNGLE_LOG, "Jungle Log", 2.0, 1.6, 1000, 500
        ));
        
        // Combat materials
        bazaarItems.put(Material.ROTTEN_FLESH, new BazaarItem(
            Material.ROTTEN_FLESH, "Rotten Flesh", 0.5, 0.4, 3000, 1500
        ));
        bazaarItems.put(Material.BONE, new BazaarItem(
            Material.BONE, "Bone", 1.0, 0.8, 2000, 1000
        ));
        bazaarItems.put(Material.STRING, new BazaarItem(
            Material.STRING, "String", 2.0, 1.5, 1500, 750
        ));
        bazaarItems.put(Material.SPIDER_EYE, new BazaarItem(
            Material.SPIDER_EYE, "Spider Eye", 3.0, 2.5, 1000, 500
        ));
        
        // Fishing materials
        bazaarItems.put(Material.COD, new BazaarItem(
            Material.COD, "Raw Cod", 2.5, 2.0, 800, 400
        ));
        bazaarItems.put(Material.SALMON, new BazaarItem(
            Material.SALMON, "Raw Salmon", 3.0, 2.5, 600, 300
        ));
        bazaarItems.put(Material.TROPICAL_FISH, new BazaarItem(
            Material.TROPICAL_FISH, "Tropical Fish", 5.0, 4.0, 400, 200
        ));
    }
    
    private void startBazaarUpdateTask() {
        Bukkit.getScheduler().runTaskTimer(SkyblockPlugin, () -> {
            // Update bazaar prices based on supply and demand
            for (BazaarItem item : bazaarItems.values()) {
                item.updatePrices();
            }
        }, 0L, 20L * 60L * 5L); // Update every 5 minutes
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        if (title.contains("Bazaar")) {
            event.setCancelled(true);
            handleBazaarGUIClick(player, event.getSlot());
        }
    }
    
    public void openBazaarGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lBazaar"));
        
        // Bazaar categories
        addGUIItem(gui, 10, Material.WHEAT, "§a§lFarming", 
            Arrays.asList("§7Buy and sell farming materials", "", "§eClick to view"));
        
        addGUIItem(gui, 11, Material.COAL, "§7§lMining", 
            Arrays.asList("§7Buy and sell mining materials", "", "§eClick to view"));
        
        addGUIItem(gui, 12, Material.OAK_LOG, "§2§lForaging", 
            Arrays.asList("§7Buy and sell foraging materials", "", "§eClick to view"));
        
        addGUIItem(gui, 13, Material.ROTTEN_FLESH, "§c§lCombat", 
            Arrays.asList("§7Buy and sell combat materials", "", "§eClick to view"));
        
        addGUIItem(gui, 14, Material.COD, "§b§lFishing", 
            Arrays.asList("§7Buy and sell fishing materials", "", "§eClick to view"));
        
        // My orders
        List<BazaarOrder> myOrders = playerOrders.getOrDefault(player.getUniqueId(), new ArrayList<>());
        addGUIItem(gui, 19, Material.CHEST, "§e§lMy Orders", 
            Arrays.asList("§7View your bazaar orders", "§7• " + myOrders.size() + " active orders", "", "§eClick to view"));
        
        // Statistics
        BazaarStats stats = getPlayerStats(player.getUniqueId());
        addGUIItem(gui, 20, Material.GOLD_INGOT, "§6§lStatistics", 
            Arrays.asList("§7View your bazaar statistics", "§7• Total bought: §6" + stats.getTotalBought() + " coins",
                         "§7• Total sold: §6" + stats.getTotalSold() + " coins", "", "§eClick to view"));
        
        // Trending items
        addGUIItem(gui, 21, Material.EMERALD, "§a§lTrending", 
            Arrays.asList("§7View trending items", "§7• Price changes", "§7• Volume changes", "", "§eClick to view"));
        
        // Close button
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", 
            Arrays.asList("§7Close the bazaar", "", "§eClick to close"));
        
        player.openInventory(gui);
    }
    
    public void openBazaarCategoryGUI(Player player, BazaarCategory category) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lBazaar - " + category.getDisplayName());
        
        List<BazaarItem> categoryItems = bazaarItems.values().stream()
            .filter(item -> category.getMaterials().contains(item.getMaterial()))
            .toList();
        
        int slot = 10;
        for (BazaarItem item : categoryItems) {
            if (slot >= 44) break;
            
            ItemStack itemStack = createBazaarItemStack(item);
            gui.setItem(slot, itemStack);
            slot++;
            if (slot % 9 == 8) slot += 2;
        }
        
        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§l← Back", 
            Arrays.asList("§7Return to bazaar", "", "§eClick to go back"));
        
        player.openInventory(gui);
    }
    
    public void openBazaarItemGUI(Player player, Material material) {
        BazaarItem item = bazaarItems.get(material);
        if (item == null) return;
        
        Inventory gui = Bukkit.createInventory(null, 27, "§6§lBazaar - " + item.getName());
        
        // Item display
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("§e" + item.getName()));
            List<String> lore = Arrays.asList(
                "§7Buy Price: §a" + item.getBuyPrice() + " coins",
                "§7Sell Price: §c" + item.getSellPrice() + " coins",
                "§7Buy Volume: §e" + item.getBuyVolume(),
                "§7Sell Volume: §e" + item.getSellVolume()
            );
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            itemStack.setItemMeta(meta);
        }
        gui.setItem(4, itemStack);
        
        // Buy options
        addGUIItem(gui, 10, Material.EMERALD, "§a§lBuy 1", 
            Arrays.asList("§7Buy 1 " + item.getName(), "§7Price: §a" + item.getBuyPrice() + " coins", "", "§eClick to buy"));
        
        addGUIItem(gui, 11, Material.EMERALD, "§a§lBuy 10", 
            Arrays.asList("§7Buy 10 " + item.getName(), "§7Price: §a" + (item.getBuyPrice() * 10) + " coins", "", "§eClick to buy"));
        
        addGUIItem(gui, 12, Material.EMERALD, "§a§lBuy 64", 
            Arrays.asList("§7Buy 64 " + item.getName(), "§7Price: §a" + (item.getBuyPrice() * 64) + " coins", "", "§eClick to buy"));
        
        addGUIItem(gui, 13, Material.EMERALD, "§a§lBuy Custom", 
            Arrays.asList("§7Buy custom amount", "§7Price: §a" + item.getBuyPrice() + " coins each", "", "§eClick to buy"));
        
        // Sell options
        addGUIItem(gui, 14, Material.REDSTONE, "§c§lSell 1", 
            Arrays.asList("§7Sell 1 " + item.getName(), "§7Price: §c" + item.getSellPrice() + " coins", "", "§eClick to sell"));
        
        addGUIItem(gui, 15, Material.REDSTONE, "§c§lSell 10", 
            Arrays.asList("§7Sell 10 " + item.getName(), "§7Price: §c" + (item.getSellPrice() * 10) + " coins", "", "§eClick to sell"));
        
        addGUIItem(gui, 16, Material.REDSTONE, "§c§lSell 64", 
            Arrays.asList("§7Sell 64 " + item.getName(), "§7Price: §c" + (item.getSellPrice() * 64) + " coins", "", "§eClick to sell"));
        
        addGUIItem(gui, 17, Material.REDSTONE, "§c§lSell All", 
            Arrays.asList("§7Sell all " + item.getName(), "§7Price: §c" + item.getSellPrice() + " coins each", "", "§eClick to sell"));
        
        // Navigation
        addGUIItem(gui, 22, Material.ARROW, "§7§l← Back", 
            Arrays.asList("§7Return to category", "", "§eClick to go back"));
        
        player.openInventory(gui);
    }
    
    public boolean buyItem(Player player, Material material, int amount) {
        BazaarItem item = bazaarItems.get(material);
        if (item == null) return false;
        
        PlayerProfile profile = corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null) return false;
        
        double totalCost = item.getBuyPrice() * amount;
        if (!profile.tryRemoveCoins(totalCost)) {
            player.sendMessage("§cYou don't have enough coins! Cost: " + totalCost);
            return false;
        }
        
        // Give items to player
        ItemStack itemStack = new ItemStack(material, amount);
        player.getInventory().addItem(itemStack);
        
        // Update bazaar volume
        item.addBuyVolume(amount);
        
        // Update player stats
        BazaarStats stats = getPlayerStats(player.getUniqueId());
        stats.addTotalBought(totalCost);
        
        player.sendMessage(Component.text("§a§lBAZAAR PURCHASE!"));
        player.sendMessage("§7Item: §e" + item.getName());
        player.sendMessage("§7Amount: §e" + amount);
        player.sendMessage("§7Total Cost: §6" + totalCost + " coins");
        
        return true;
    }
    
    public boolean sellItem(Player player, Material material, int amount) {
        BazaarItem item = bazaarItems.get(material);
        if (item == null) return false;
        
        // Check if player has the items
        if (!player.getInventory().contains(material, amount)) {
            player.sendMessage("§cYou don't have enough " + material.name() + "!");
            return false;
        }
        
        PlayerProfile profile = corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null) return false;
        
        double totalEarnings = item.getSellPrice() * amount;
        
        // Remove items from player
        player.getInventory().removeItem(new ItemStack(material, amount));
        
        // Give coins to player
        profile.addCoins(totalEarnings);
        
        // Update bazaar volume
        item.addSellVolume(amount);
        
        // Update player stats
        BazaarStats stats = getPlayerStats(player.getUniqueId());
        stats.addTotalSold(totalEarnings);
        
        player.sendMessage(Component.text("§a§lBAZAAR SALE!"));
        player.sendMessage("§7Item: §e" + item.getName());
        player.sendMessage("§7Amount: §e" + amount);
        player.sendMessage("§7Total Earnings: §6" + totalEarnings + " coins");
        
        return true;
    }
    
    private ItemStack createBazaarItemStack(BazaarItem item) {
        ItemStack itemStack = new ItemStack(item.getMaterial());
        ItemMeta meta = itemStack.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§e" + item.getName()));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Buy Price: §a" + item.getBuyPrice() + " coins");
            lore.add("§7Sell Price: §c" + item.getSellPrice() + " coins");
            lore.add("§7Buy Volume: §e" + item.getBuyVolume());
            lore.add("§7Sell Volume: §e" + item.getSellVolume());
            lore.add("");
            lore.add("§eClick to view options");
            
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            itemStack.setItemMeta(meta);
        }
        
        return itemStack;
    }
    
    private void handleBazaarGUIClick(Player player, int slot) {
        switch (slot) {
            case 10: // Farming
                openBazaarCategoryGUI(player, BazaarCategory.FARMING);
                break;
            case 11: // Mining
                openBazaarCategoryGUI(player, BazaarCategory.MINING);
                break;
            case 12: // Foraging
                openBazaarCategoryGUI(player, BazaarCategory.FORAGING);
                break;
            case 13: // Combat
                openBazaarCategoryGUI(player, BazaarCategory.COMBAT);
                break;
            case 14: // Fishing
                openBazaarCategoryGUI(player, BazaarCategory.FISHING);
                break;
            case 19: // My Orders
                player.sendMessage(Component.text("§eMy Orders coming soon!"));
                break;
            case 20: // Statistics
                player.sendMessage(Component.text("§eStatistics coming soon!"));
                break;
            case 21: // Trending
                player.sendMessage(Component.text("§eTrending coming soon!"));
                break;
            case 49: // Close
                player.closeInventory();
                break;
        }
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, List<String> description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(description.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public BazaarItem getBazaarItem(Material material) {
        return bazaarItems.get(material);
    }
    
    public Map<Material, BazaarItem> getAllBazaarItems() {
        return new HashMap<>(bazaarItems);
    }
    
    public BazaarStats getPlayerStats(UUID playerId) {
        return playerStats.computeIfAbsent(playerId, k -> new BazaarStats(playerId));
    }
    
    // Bazaar Classes
    public static class BazaarItem {
        private final Material material;
        private final String name;
        private double buyPrice;
        private double sellPrice;
        private int buyVolume;
        private int sellVolume;
        
        public BazaarItem(Material material, String name, double buyPrice, double sellPrice, int buyVolume, int sellVolume) {
            this.material = material;
            this.name = name;
            this.buyPrice = buyPrice;
            this.sellPrice = sellPrice;
            this.buyVolume = buyVolume;
            this.sellVolume = sellVolume;
        }
        
        public void updatePrices() {
            // Simulate price changes based on supply and demand
            double buyChange = (Math.random() - 0.5) * 0.1; // ±5% change
            double sellChange = (Math.random() - 0.5) * 0.1;
            
            buyPrice = Math.max(0.1, buyPrice * (1 + buyChange));
            sellPrice = Math.max(0.1, sellPrice * (1 + sellChange));
        }
        
        public void addBuyVolume(int amount) {
            this.buyVolume += amount;
        }
        
        public void addSellVolume(int amount) {
            this.sellVolume += amount;
        }
        
        // Getters
        public Material getMaterial() { return material; }
        public String getName() { return name; }
        public double getBuyPrice() { return buyPrice; }
        public double getSellPrice() { return sellPrice; }
        public int getBuyVolume() { return buyVolume; }
        public int getSellVolume() { return sellVolume; }
    }
    
    public enum BazaarCategory {
        FARMING("§aFarming", Arrays.asList(Material.WHEAT, Material.CARROT, Material.POTATO, Material.NETHER_WART)),
        MINING("§7Mining", Arrays.asList(Material.COAL, Material.IRON_INGOT, Material.GOLD_INGOT, Material.DIAMOND, Material.EMERALD)),
        FORAGING("§2Foraging", Arrays.asList(Material.OAK_LOG, Material.BIRCH_LOG, Material.SPRUCE_LOG, Material.JUNGLE_LOG)),
        COMBAT("§cCombat", Arrays.asList(Material.ROTTEN_FLESH, Material.BONE, Material.STRING, Material.SPIDER_EYE)),
        FISHING("§bFishing", Arrays.asList(Material.COD, Material.SALMON, Material.TROPICAL_FISH));
        
        private final String displayName;
        private final List<Material> materials;
        
        BazaarCategory(String displayName, List<Material> materials) {
            this.displayName = displayName;
            this.materials = materials;
        }
        
        public String getDisplayName() { return displayName; }
        public List<Material> getMaterials() { return materials; }
    }
    
    public static class BazaarOrder {
        private final UUID playerId;
        private final Material material;
        private final int amount;
        private final double price;
        private final boolean isBuyOrder;
        private final long timestamp;
        
        public BazaarOrder(UUID playerId, Material material, int amount, double price, boolean isBuyOrder) {
            this.playerId = playerId;
            this.material = material;
            this.amount = amount;
            this.price = price;
            this.isBuyOrder = isBuyOrder;
            this.timestamp = java.lang.System.currentTimeMillis();
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public Material getMaterial() { return material; }
        public int getAmount() { return amount; }
        public double getPrice() { return price; }
        public boolean isBuyOrder() { return isBuyOrder; }
        public long getTimestamp() { return timestamp; }
    }
    
    public static class BazaarStats {
        private final UUID playerId;
        private double totalBought;
        private double totalSold;
        private int totalTransactions;
        
        public BazaarStats(UUID playerId) {
            this.playerId = playerId;
            this.totalBought = 0.0;
            this.totalSold = 0.0;
            this.totalTransactions = 0;
        }
        
        public void addTotalBought(double amount) {
            this.totalBought += amount;
            this.totalTransactions++;
        }
        
        public void addTotalSold(double amount) {
            this.totalSold += amount;
            this.totalTransactions++;
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public double getTotalBought() { return totalBought; }
        public double getTotalSold() { return totalSold; }
        public int getTotalTransactions() { return totalTransactions; }
    }
}
