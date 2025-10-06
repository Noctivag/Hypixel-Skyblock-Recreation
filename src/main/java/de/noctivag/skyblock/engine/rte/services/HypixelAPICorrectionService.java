package de.noctivag.skyblock.engine.rte.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import de.noctivag.skyblock.engine.rte.RealTimeEconomyEngine;
import de.noctivag.skyblock.engine.rte.data.BazaarItemData;

/**
 * Hypixel API Correction Service - Fixes Critical Price Inversion Issue
 * 
 * Implements the corrected API endpoint that provides consistent economic data
 * with proper price definitions to ensure compatibility with client mods.
 * 
 * CRITICAL CORRECTION:
 * - quick_status.sellPrice: Price at which players SELL to the system
 * - quick_status.buyPrice: Price at which players BUY from the system
 * 
 * This ensures compatibility with popular client modifications and prevents
 * economic confusion that could lead to exploits.
 */
public class HypixelAPICorrectionService {
    
    private static final Logger logger = Logger.getLogger(HypixelAPICorrectionService.class.getName());
    
    private final RealTimeEconomyEngine engine;
    private final Gson gson;
    
    // API endpoint configuration
    private static final String API_BASE_URL = "/api/v1/bazaar";
    private static final String QUICK_STATUS_ENDPOINT = "/quick_status";
    private static final String FULL_STATUS_ENDPOINT = "/full_status";
    
    // Price correction cache
    private final Map<String, CorrectedPriceData> priceCache = new ConcurrentHashMap<>();
    private final Map<String, Long> lastUpdateTime = new ConcurrentHashMap<>();
    
    // Cache configuration
    private static final long CACHE_DURATION = 1000L; // 1 second
    private static final long MAX_CACHE_AGE = 5000L; // 5 seconds
    
    public HypixelAPICorrectionService(RealTimeEconomyEngine engine) {
        this.engine = engine;
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        
        initialize();
    }
    
    /**
     * Initialize the API correction service
     */
    public void initialize() {
        logger.info("Initializing Hypixel API Correction Service");
        
        // Start price update task
        startPriceUpdateTask();
        
        // Initialize default items
        initializeDefaultItems();
        
        logger.info("Hypixel API Correction Service initialized successfully");
    }
    
    /**
     * Start periodic price update task
     */
    private void startPriceUpdateTask() {
        Timer timer = new Timer("APICorrectionPriceUpdate", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateAllPrices();
            }
        }, 0L, CACHE_DURATION);
    }
    
    /**
     * Initialize default bazaar items
     */
    private void initializeDefaultItems() {
        String[] defaultItems = {
            "COBBLESTONE", "COAL", "IRON_INGOT", "GOLD_INGOT", "DIAMOND", "EMERALD",
            "REDSTONE", "LAPIS_LAZULI", "QUARTZ", "OBSIDIAN", "WHEAT", "CARROT",
            "POTATO", "BEETROOT", "SUGAR_CANE", "CACTUS", "PUMPKIN", "MELON",
            "OAK_LOG", "BIRCH_LOG", "SPRUCE_LOG", "JUNGLE_LOG", "ACACIA_LOG", "DARK_OAK_LOG",
            "SAND", "GRAVEL", "CLAY", "SOUL_SAND", "NETHERRACK", "NETHER_BRICK",
            "END_STONE", "PRISMARINE_SHARD", "PRISMARINE_CRYSTALS", "SPONGE", "WET_SPONGE",
            "SEA_LANTERN", "KELP", "DRIED_KELP", "HEART_OF_THE_SEA", "NAUTILUS_SHELL",
            "TRIDENT", "PHANTOM_MEMBRANE", "ELYTRA", "DRAGON_HEAD", "DRAGON_EGG",
            "NETHER_STAR", "WITHER_SKELETON_SKULL", "WITHER_ROSE", "TOTEM_OF_UNDYING",
            "SHULKER_SHELL", "END_CRYSTAL", "ENDER_EYE", "ENDER_PEARL", "ENDER_CHEST"
        };
        
        for (String itemId : defaultItems) {
            initializeItem(itemId);
        }
    }
    
    /**
     * Initialize item with default prices
     */
    private void initializeItem(String itemId) {
        double basePrice = getBasePriceForItem(itemId);
        
        CorrectedPriceData priceData = new CorrectedPriceData(
            itemId,
            basePrice * 0.95, // sellPrice (players sell to system)
            basePrice * 1.05, // buyPrice (players buy from system)
            basePrice,        // averagePrice
            0,               // volume
            0.0,             // priceChange
            java.lang.System.currentTimeMillis()
        );
        
        priceCache.put(itemId, priceData);
        lastUpdateTime.put(itemId, java.lang.System.currentTimeMillis());
    }
    
    /**
     * Get base price for item
     */
    private double getBasePriceForItem(String itemId) {
        return switch (itemId) {
            case "DIAMOND" -> 10.0;
            case "EMERALD" -> 8.0;
            case "GOLD_INGOT" -> 5.0;
            case "IRON_INGOT" -> 2.0;
            case "COAL" -> 1.0;
            case "REDSTONE" -> 1.5;
            case "LAPIS_LAZULI" -> 2.0;
            case "QUARTZ" -> 3.0;
            case "OBSIDIAN" -> 4.0;
            case "WHEAT" -> 0.5;
            case "CARROT" -> 0.5;
            case "POTATO" -> 0.5;
            case "BEETROOT" -> 0.5;
            case "SUGAR_CANE" -> 0.5;
            case "CACTUS" -> 0.5;
            case "PUMPKIN" -> 1.0;
            case "MELON" -> 1.0;
            case "OAK_LOG" -> 0.5;
            case "BIRCH_LOG" -> 0.5;
            case "SPRUCE_LOG" -> 0.5;
            case "JUNGLE_LOG" -> 0.5;
            case "ACACIA_LOG" -> 0.5;
            case "DARK_OAK_LOG" -> 0.5;
            case "SAND" -> 0.5;
            case "GRAVEL" -> 0.5;
            case "CLAY" -> 1.0;
            case "SOUL_SAND" -> 2.0;
            case "NETHERRACK" -> 0.5;
            case "NETHER_BRICK" -> 1.0;
            case "END_STONE" -> 2.0;
            case "PRISMARINE_SHARD" -> 3.0;
            case "PRISMARINE_CRYSTALS" -> 5.0;
            case "SPONGE" -> 10.0;
            case "WET_SPONGE" -> 8.0;
            case "SEA_LANTERN" -> 15.0;
            case "KELP" -> 0.5;
            case "DRIED_KELP" -> 1.0;
            case "HEART_OF_THE_SEA" -> 100.0;
            case "NAUTILUS_SHELL" -> 50.0;
            case "TRIDENT" -> 200.0;
            case "PHANTOM_MEMBRANE" -> 25.0;
            case "ELYTRA" -> 500.0;
            case "DRAGON_HEAD" -> 1000.0;
            case "DRAGON_EGG" -> 10000.0;
            case "NETHER_STAR" -> 100.0;
            case "WITHER_SKELETON_SKULL" -> 50.0;
            case "WITHER_ROSE" -> 25.0;
            case "TOTEM_OF_UNDYING" -> 100.0;
            case "SHULKER_SHELL" -> 75.0;
            case "END_CRYSTAL" -> 200.0;
            case "ENDER_EYE" -> 10.0;
            case "ENDER_PEARL" -> 5.0;
            case "ENDER_CHEST" -> 50.0;
            default -> 1.0;
        };
    }
    
    /**
     * Update all item prices
     */
    private void updateAllPrices() {
        for (String itemId : priceCache.keySet()) {
            updateItemPrice(itemId);
        }
    }
    
    /**
     * Update price for specific item
     */
    private void updateItemPrice(String itemId) {
        try {
            // Get current market data from order book
            Map<String, BazaarItemData> bazaarItems = engine.getBazaarItems();
            BazaarItemData itemData = bazaarItems.get(itemId);
            
            if (itemData != null) {
                // Get current prices from order book
                double instantBuyPrice = itemData.getInstantBuyPrice();
                double instantSellPrice = itemData.getInstantSellPrice();
                double averagePrice = itemData.getAveragePrice();
                int volume = itemData.getTotalVolume();
                
                // Calculate price change
                CorrectedPriceData oldData = priceCache.get(itemId);
                double priceChange = 0.0;
                if (oldData != null) {
                    priceChange = ((averagePrice - oldData.getAveragePrice()) / oldData.getAveragePrice()) * 100;
                }
                
                // Create corrected price data
                CorrectedPriceData newData = new CorrectedPriceData(
                    itemId,
                    instantSellPrice, // CORRECTED: sellPrice = instant sell price
                    instantBuyPrice,  // CORRECTED: buyPrice = instant buy price
                    averagePrice,
                    volume,
                    priceChange,
                    java.lang.System.currentTimeMillis()
                );
                
                priceCache.put(itemId, newData);
                lastUpdateTime.put(itemId, java.lang.System.currentTimeMillis());
                
            } else {
                // Use cached data if no market data available
                CorrectedPriceData cachedData = priceCache.get(itemId);
                if (cachedData != null) {
                    cachedData.setLastUpdate(java.lang.System.currentTimeMillis());
                }
            }
            
        } catch (Exception e) {
            logger.severe("Failed to update price for " + itemId + ": " + e.getMessage());
        }
    }
    
    /**
     * Get corrected quick status for item
     */
    public CompletableFuture<String> getQuickStatus(String itemId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                CorrectedPriceData priceData = getPriceData(itemId);
                if (priceData == null) {
                    return createErrorResponse("Item not found: " + itemId);
                }
                
                JsonObject response = new JsonObject();
                response.addProperty("success", true);
                
                JsonObject productInfo = new JsonObject();
                productInfo.addProperty("product_id", itemId);
                
                JsonObject quickStatus = new JsonObject();
                // CRITICAL CORRECTION: Proper price definitions
                quickStatus.addProperty("sellPrice", priceData.getSellPrice()); // Players sell TO system
                quickStatus.addProperty("buyPrice", priceData.getBuyPrice());   // Players buy FROM system
                quickStatus.addProperty("sellVolume", priceData.getVolume());
                quickStatus.addProperty("buyVolume", priceData.getVolume());
                quickStatus.addProperty("sellMovingWeek", priceData.getVolume());
                quickStatus.addProperty("buyMovingWeek", priceData.getVolume());
                quickStatus.addProperty("sellOrders", 10); // Default order count
                quickStatus.addProperty("buyOrders", 10);  // Default order count
                
                productInfo.add("quick_status", quickStatus);
                response.add("product_info", productInfo);
                
                return gson.toJson(response);
                
            } catch (Exception e) {
                logger.severe("Failed to get quick status for " + itemId + ": " + e.getMessage());
                return createErrorResponse("Internal server error");
            }
        });
    }
    
    /**
     * Get corrected full status for item
     */
    public CompletableFuture<String> getFullStatus(String itemId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                CorrectedPriceData priceData = getPriceData(itemId);
                if (priceData == null) {
                    return createErrorResponse("Item not found: " + itemId);
                }
                
                JsonObject response = new JsonObject();
                response.addProperty("success", true);
                
                JsonObject productInfo = new JsonObject();
                productInfo.addProperty("product_id", itemId);
                
                // Full status with detailed information
                JsonObject fullStatus = new JsonObject();
                
                // CORRECTED: Proper price definitions
                fullStatus.addProperty("sellPrice", priceData.getSellPrice()); // Players sell TO system
                fullStatus.addProperty("buyPrice", priceData.getBuyPrice());   // Players buy FROM system
                fullStatus.addProperty("sellVolume", priceData.getVolume());
                fullStatus.addProperty("buyVolume", priceData.getVolume());
                fullStatus.addProperty("sellMovingWeek", priceData.getVolume());
                fullStatus.addProperty("buyMovingWeek", priceData.getVolume());
                fullStatus.addProperty("sellOrders", 10);
                fullStatus.addProperty("buyOrders", 10);
                
                // Additional market data
                fullStatus.addProperty("averagePrice", priceData.getAveragePrice());
                fullStatus.addProperty("priceChange", priceData.getPriceChange());
                fullStatus.addProperty("lastUpdate", priceData.getLastUpdate());
                
                // Market depth information
                JsonObject marketDepth = new JsonObject();
                marketDepth.addProperty("buyDepth", 1000);
                marketDepth.addProperty("sellDepth", 1000);
                fullStatus.add("marketDepth", marketDepth);
                
                productInfo.add("full_status", fullStatus);
                response.add("product_info", productInfo);
                
                return gson.toJson(response);
                
            } catch (Exception e) {
                logger.severe("Failed to get full status for " + itemId + ": " + e.getMessage());
                return createErrorResponse("Internal server error");
            }
        });
    }
    
    /**
     * Get all bazaar items with corrected prices
     */
    public CompletableFuture<String> getAllItems() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonObject response = new JsonObject();
                response.addProperty("success", true);
                
                JsonObject products = new JsonObject();
                
                for (Map.Entry<String, CorrectedPriceData> entry : priceCache.entrySet()) {
                    String itemId = entry.getKey();
                    CorrectedPriceData priceData = entry.getValue();
                    
                    JsonObject productInfo = new JsonObject();
                    productInfo.addProperty("product_id", itemId);
                    
                    JsonObject quickStatus = new JsonObject();
                    // CRITICAL CORRECTION: Proper price definitions
                    quickStatus.addProperty("sellPrice", priceData.getSellPrice()); // Players sell TO system
                    quickStatus.addProperty("buyPrice", priceData.getBuyPrice());   // Players buy FROM system
                    quickStatus.addProperty("sellVolume", priceData.getVolume());
                    quickStatus.addProperty("buyVolume", priceData.getVolume());
                    quickStatus.addProperty("sellMovingWeek", priceData.getVolume());
                    quickStatus.addProperty("buyMovingWeek", priceData.getVolume());
                    quickStatus.addProperty("sellOrders", 10);
                    quickStatus.addProperty("buyOrders", 10);
                    
                    productInfo.add("quick_status", quickStatus);
                    products.add(itemId, productInfo);
                }
                
                response.add("products", products);
                
                return gson.toJson(response);
                
            } catch (Exception e) {
                logger.severe("Failed to get all items: " + e.getMessage());
                return createErrorResponse("Internal server error");
            }
        });
    }
    
    /**
     * Get price data for item with cache validation
     */
    private CorrectedPriceData getPriceData(String itemId) {
        CorrectedPriceData priceData = priceCache.get(itemId);
        Long lastUpdate = lastUpdateTime.get(itemId);
        
        if (priceData == null || lastUpdate == null) {
            return null;
        }
        
        // Check if cache is still valid
        if (java.lang.System.currentTimeMillis() - lastUpdate > MAX_CACHE_AGE) {
            // Update price if cache is stale
            updateItemPrice(itemId);
            priceData = priceCache.get(itemId);
        }
        
        return priceData;
    }
    
    /**
     * Create error response
     */
    private String createErrorResponse(String message) {
        JsonObject response = new JsonObject();
        response.addProperty("success", false);
        response.addProperty("error", message);
        return gson.toJson(response);
    }
    
    /**
     * Validate API request
     */
    public boolean validateRequest(String endpoint, Map<String, String> parameters) {
        // Validate endpoint
        if (!endpoint.startsWith(API_BASE_URL)) {
            return false;
        }
        
        // Validate parameters
        if (endpoint.contains(QUICK_STATUS_ENDPOINT) || endpoint.contains(FULL_STATUS_ENDPOINT)) {
            String productId = parameters.get("productId");
            if (productId == null || productId.isEmpty()) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Get API endpoint for item
     */
    public String getQuickStatusEndpoint(String itemId) {
        return API_BASE_URL + QUICK_STATUS_ENDPOINT + "?productId=" + itemId;
    }
    
    /**
     * Get full status endpoint for item
     */
    public String getFullStatusEndpoint(String itemId) {
        return API_BASE_URL + FULL_STATUS_ENDPOINT + "?productId=" + itemId;
    }
    
    /**
     * Get all items endpoint
     */
    public String getAllItemsEndpoint() {
        return API_BASE_URL;
    }
    
    /**
     * Get corrected price data for item
     */
    public CompletableFuture<CorrectedPriceData> getCorrectedPriceData(String itemId) {
        return CompletableFuture.supplyAsync(() -> {
            return getPriceData(itemId);
        });
    }
    
    /**
     * Update price data for item (for external updates)
     */
    public void updatePriceData(String itemId, double sellPrice, double buyPrice, double averagePrice, int volume) {
        CorrectedPriceData priceData = new CorrectedPriceData(
            itemId,
            sellPrice,    // CORRECTED: sellPrice = players sell to system
            buyPrice,     // CORRECTED: buyPrice = players buy from system
            averagePrice,
            volume,
            0.0, // Price change will be calculated
            java.lang.System.currentTimeMillis()
        );
        
        priceCache.put(itemId, priceData);
        lastUpdateTime.put(itemId, java.lang.System.currentTimeMillis());
    }
    
    /**
     * Get cache statistics
     */
    public Map<String, Object> getCacheStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("cachedItems", priceCache.size());
        stats.put("cacheDuration", CACHE_DURATION);
        stats.put("maxCacheAge", MAX_CACHE_AGE);
        
        long now = java.lang.System.currentTimeMillis();
        int staleItems = 0;
        for (Long lastUpdate : lastUpdateTime.values()) {
            if (now - lastUpdate > MAX_CACHE_AGE) {
                staleItems++;
            }
        }
        stats.put("staleItems", staleItems);
        
        return stats;
    }
    
    // Data class for corrected price data
    public static class CorrectedPriceData {
        private final String itemId;
        private final double sellPrice;    // CORRECTED: Price at which players SELL to system
        private final double buyPrice;     // CORRECTED: Price at which players BUY from system
        private final double averagePrice;
        private final int volume;
        private final double priceChange;
        private long lastUpdate;
        
        public CorrectedPriceData(String itemId, double sellPrice, double buyPrice, 
                                double averagePrice, int volume, double priceChange, long lastUpdate) {
            this.itemId = itemId;
            this.sellPrice = sellPrice;
            this.buyPrice = buyPrice;
            this.averagePrice = averagePrice;
            this.volume = volume;
            this.priceChange = priceChange;
            this.lastUpdate = lastUpdate;
        }
        
        // Getters
        public String getItemId() { return itemId; }
        public double getSellPrice() { return sellPrice; }
        public double getBuyPrice() { return buyPrice; }
        public double getAveragePrice() { return averagePrice; }
        public int getVolume() { return volume; }
        public double getPriceChange() { return priceChange; }
        public long getLastUpdate() { return lastUpdate; }
        
        public void setLastUpdate(long lastUpdate) { this.lastUpdate = lastUpdate; }
    }
}
