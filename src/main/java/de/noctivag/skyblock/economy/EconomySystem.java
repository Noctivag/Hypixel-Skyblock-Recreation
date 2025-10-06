package de.noctivag.skyblock.economy;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.core.CorePlatform;
import de.noctivag.skyblock.core.PlayerProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Economy System - Erweiterte Wirtschaft mit Auktionen, Bazaar, NPCs und Itemhandel
 * 
 * Verantwortlich für:
 * - Auction House System
 * - Bazaar Trading System
 * - NPC Shops
 * - Item Trading
 * - Coin Management
 * - Market Analysis
 * - Price Fluctuations
 */
public class EconomySystem {
    public final SkyblockPlugin SkyblockPlugin;
    public final CorePlatform corePlatform;
    private final AuctionHouse auctionHouse;
    private final BazaarSystem bazaarSystem;
    private final NPCShopSystem npcShopSystem;
    private final TradingSystem tradingSystem;
    private final MarketAnalyzer marketAnalyzer;
    
    // Economy Configuration
    private final Map<String, Double> basePrices = new HashMap<>();
    private final Map<String, Double> currentPrices = new ConcurrentHashMap<>();
    private final Map<String, MarketTrend> marketTrends = new ConcurrentHashMap<>();
    
    public EconomySystem(SkyblockPlugin SkyblockPlugin, CorePlatform corePlatform) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.corePlatform = corePlatform;
        this.auctionHouse = new AuctionHouse(this, (MultiServerDatabaseManager) SkyblockPlugin.getDatabaseManager());
        this.bazaarSystem = new BazaarSystem(this);
        this.npcShopSystem = new NPCShopSystem(this);
        this.tradingSystem = new TradingSystem(this);
        this.marketAnalyzer = new MarketAnalyzer(this);
        
        initializeEconomy();
    }
    
    private void initializeEconomy() {
        initializeBasePrices();
        startPriceUpdateTimer();
        startMarketAnalysis();
    }
    
    private void initializeBasePrices() {
        // Initialize base prices for all items
        basePrices.put("COBBLESTONE", 0.5);
        basePrices.put("COAL", 2.0);
        basePrices.put("IRON_INGOT", 5.0);
        basePrices.put("GOLD_INGOT", 8.0);
        basePrices.put("DIAMOND", 15.0);
        basePrices.put("EMERALD", 12.0);
        basePrices.put("REDSTONE", 1.0);
        basePrices.put("LAPIS_LAZULI", 1.5);
        basePrices.put("QUARTZ", 3.0);
        basePrices.put("OBSIDIAN", 20.0);
        basePrices.put("WHEAT", 0.5);
        basePrices.put("CARROT", 0.5);
        basePrices.put("POTATO", 0.5);
        basePrices.put("BEETROOT", 0.5);
        basePrices.put("SUGAR_CANE", 1.0);
        basePrices.put("CACTUS", 1.0);
        basePrices.put("PUMPKIN", 2.0);
        basePrices.put("MELON", 1.0);
        basePrices.put("OAK_LOG", 1.0);
        basePrices.put("BIRCH_LOG", 1.0);
        basePrices.put("SPRUCE_LOG", 1.0);
        basePrices.put("JUNGLE_LOG", 1.0);
        basePrices.put("ACACIA_LOG", 1.0);
        basePrices.put("DARK_OAK_LOG", 1.0);
        basePrices.put("SAND", 0.3);
        basePrices.put("GRAVEL", 0.3);
        basePrices.put("CLAY", 1.0);
        basePrices.put("SOUL_SAND", 2.0);
        basePrices.put("NETHERRACK", 0.5);
        basePrices.put("NETHER_BRICK", 1.0);
        basePrices.put("END_STONE", 3.0);
        basePrices.put("PRISMARINE_SHARD", 5.0);
        basePrices.put("PRISMARINE_CRYSTALS", 8.0);
        basePrices.put("SPONGE", 15.0);
        basePrices.put("WET_SPONGE", 20.0);
        basePrices.put("SEA_LANTERN", 12.0);
        basePrices.put("KELP", 0.5);
        basePrices.put("DRIED_KELP", 1.0);
        basePrices.put("HEART_OF_THE_SEA", 50.0);
        basePrices.put("NAUTILUS_SHELL", 25.0);
        basePrices.put("TRIDENT", 100.0);
        basePrices.put("PHANTOM_MEMBRANE", 8.0);
        basePrices.put("ELYTRA", 200.0);
        basePrices.put("DRAGON_HEAD", 500.0);
        basePrices.put("DRAGON_EGG", 1000.0);
        basePrices.put("NETHER_STAR", 300.0);
        basePrices.put("WITHER_SKELETON_SKULL", 150.0);
        basePrices.put("WITHER_ROSE", 100.0);
        basePrices.put("TOTEM_OF_UNDYING", 250.0);
        basePrices.put("SHULKER_SHELL", 80.0);
        basePrices.put("END_CRYSTAL", 200.0);
        basePrices.put("ENDER_EYE", 15.0);
        basePrices.put("ENDER_PEARL", 10.0);
        basePrices.put("ENDER_CHEST", 50.0);
        basePrices.put("ANVIL", 30.0);
        basePrices.put("ENCHANTING_TABLE", 40.0);
        basePrices.put("BREWING_STAND", 20.0);
        basePrices.put("CAULDRON", 10.0);
        basePrices.put("HOPPER", 25.0);
        basePrices.put("DROPPER", 15.0);
        basePrices.put("DISPENSER", 20.0);
        basePrices.put("PISTON", 12.0);
        basePrices.put("STICKY_PISTON", 18.0);
        basePrices.put("REDSTONE", 1.0);
        basePrices.put("REDSTONE_TORCH", 2.0);
        basePrices.put("REDSTONE_LAMP", 8.0);
        basePrices.put("REDSTONE_BLOCK", 9.0);
        basePrices.put("REPEATER", 3.0);
        basePrices.put("COMPARATOR", 5.0);
        basePrices.put("OBSERVER", 15.0);
        basePrices.put("DAYLIGHT_DETECTOR", 10.0);
        basePrices.put("TRIPWIRE_HOOK", 2.0);
        basePrices.put("TRIPWIRE", 0.5);
        basePrices.put("LEVER", 1.0);
        basePrices.put("STONE_BUTTON", 0.5);
        basePrices.put("WOODEN_BUTTON", 0.5);
        basePrices.put("STONE_PRESSURE_PLATE", 1.0);
        basePrices.put("WOODEN_PRESSURE_PLATE", 1.0);
        basePrices.put("HEAVY_WEIGHTED_PRESSURE_PLATE", 2.0);
        basePrices.put("LIGHT_WEIGHTED_PRESSURE_PLATE", 2.0);
        basePrices.put("TARGET", 8.0);
        basePrices.put("LECTERN", 15.0);
        basePrices.put("JUKEBOX", 20.0);
        basePrices.put("NOTE_BLOCK", 5.0);
        
        // Initialize current prices
        currentPrices.putAll(basePrices);
    }
    
    private void startPriceUpdateTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updatePrices();
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L * 60L * 5L); // Every 5 minutes
    }
    
    private void startMarketAnalysis() {
        new BukkitRunnable() {
            @Override
            public void run() {
                marketAnalyzer.analyzeMarket();
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L * 60L * 10L); // Every 10 minutes
    }
    
    private void updatePrices() {
        // Update prices based on supply and demand
        for (Map.Entry<String, Double> entry : basePrices.entrySet()) {
            String itemId = entry.getKey();
            double basePrice = entry.getValue();
            
            // Get market trend
            MarketTrend trend = marketTrends.get(itemId);
            if (trend == null) {
                trend = new MarketTrend();
                marketTrends.put(itemId, trend);
            }
            
            // Calculate new price based on trend
            double newPrice = calculateNewPrice(basePrice, trend);
            currentPrices.put(itemId, newPrice);
        }
    }
    
    private double calculateNewPrice(double basePrice, MarketTrend trend) {
        double trendFactor = trend.getTrendFactor();
        double volatility = trend.getVolatility();
        
        // Apply trend and volatility
        double newPrice = basePrice * (1.0 + trendFactor);
        
        // Add some randomness
        Random random = new Random();
        double randomFactor = (random.nextDouble() - 0.5) * volatility;
        newPrice *= (1.0 + randomFactor);
        
        // Ensure price doesn't go below 0.1 or above 10x base price
        return Math.max(0.1, Math.min(basePrice * 10, newPrice));
    }
    
    public void buyItem(Player player, String itemId, int amount) {
        PlayerProfile profile = corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null) return;
        
        double price = getCurrentPrice(itemId);
        double totalCost = price * amount;
        
        if (!profile.tryRemoveCoins(totalCost)) {
            player.sendMessage("§cYou don't have enough coins! Cost: " + totalCost);
            return;
        }
        
        // Give item to player
        Material material = Material.valueOf(itemId);
        player.getInventory().addItem(new ItemStack(material, amount));
        
        // Update market trend
        updateMarketTrend(itemId, -amount); // Negative because buying increases demand
        
        player.sendMessage(Component.text("§a§lITEM PURCHASED!"));
        player.sendMessage("§7Item: §e" + itemId);
        player.sendMessage("§7Amount: §e" + amount);
        player.sendMessage("§7Total Cost: §6" + totalCost + " coins");
    }
    
    public void sellItem(Player player, String itemId, int amount) {
        PlayerProfile profile = corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null) return;
        
        Material material = Material.valueOf(itemId);
        if (!player.getInventory().contains(material, amount)) {
            player.sendMessage(Component.text("§cYou don't have enough items!"));
            return;
        }
        
        double price = getCurrentPrice(itemId);
        double totalEarnings = price * amount;
        
        // Remove items from inventory
        ItemStack toRemove = new ItemStack(material, amount);
        player.getInventory().removeItem(toRemove);
        
        // Give coins to player
        profile.addCoins(totalEarnings);
        
        // Update market trend
        updateMarketTrend(itemId, amount); // Positive because selling increases supply
        
        player.sendMessage(Component.text("§a§lITEM SOLD!"));
        player.sendMessage("§7Item: §e" + itemId);
        player.sendMessage("§7Amount: §e" + amount);
        player.sendMessage("§7Total Earnings: §6" + totalEarnings + " coins");
    }
    
    private void updateMarketTrend(String itemId, int amount) {
        MarketTrend trend = marketTrends.get(itemId);
        if (trend == null) {
            trend = new MarketTrend();
            marketTrends.put(itemId, trend);
        }
        
        trend.updateTrend(amount);
    }
    
    public double getCurrentPrice(String itemId) {
        return currentPrices.getOrDefault(itemId, 1.0);
    }
    
    public double getBasePrice(String itemId) {
        return basePrices.getOrDefault(itemId, 1.0);
    }
    
    public MarketTrend getMarketTrend(String itemId) {
        return marketTrends.get(itemId);
    }
    
    public Map<String, Double> getAllCurrentPrices() {
        return new HashMap<>(currentPrices);
    }
    
    public Map<String, MarketTrend> getAllMarketTrends() {
        return new HashMap<>(marketTrends);
    }
    
    // Getters
    public AuctionHouse getAuctionHouse() { return auctionHouse; }
    public BazaarSystem getBazaarSystem() { return bazaarSystem; }
    public NPCShopSystem getNPCShopSystem() { return npcShopSystem; }
    public TradingSystem getTradingSystem() { return tradingSystem; }
    public MarketAnalyzer getMarketAnalyzer() { return marketAnalyzer; }
    
    // Market Trend Class
    public static class MarketTrend {
        private double trendFactor = 0.0; // -1.0 to 1.0
        private double volatility = 0.1; // 0.0 to 1.0
        private final List<Double> priceHistory = new ArrayList<>();
        private final List<Integer> supplyDemandHistory = new ArrayList<>();
        
        public void updateTrend(int supplyDemandChange) {
            supplyDemandHistory.add(supplyDemandChange);
            
            // Keep only last 100 entries
            if (supplyDemandHistory.size() > 100) {
                supplyDemandHistory.remove(0);
            }
            
            // Calculate trend factor based on recent supply/demand
            if (supplyDemandHistory.size() >= 10) {
                double recentAverage = supplyDemandHistory.subList(supplyDemandHistory.size() - 10, supplyDemandHistory.size())
                    .stream().mapToInt(Integer::intValue).average().orElse(0.0);
                
                // Normalize to -1.0 to 1.0
                trendFactor = Math.max(-1.0, Math.min(1.0, recentAverage / 100.0));
            }
            
            // Update volatility based on price changes
            if (priceHistory.size() >= 2) {
                double recentChange = Math.abs(priceHistory.get(priceHistory.size() - 1) - priceHistory.get(priceHistory.size() - 2));
                volatility = Math.max(0.05, Math.min(0.5, recentChange / 10.0));
            }
        }
        
        public void addPrice(double price) {
            priceHistory.add(price);
            
            // Keep only last 100 entries
            if (priceHistory.size() > 100) {
                priceHistory.remove(0);
            }
        }
        
        public double getTrendFactor() { return trendFactor; }
        public double getVolatility() { return volatility; }
        public List<Double> getPriceHistory() { return new ArrayList<>(priceHistory); }
        public List<Integer> getSupplyDemandHistory() { return new ArrayList<>(supplyDemandHistory); }
    }
}
