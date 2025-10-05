package de.noctivag.skyblock.core.architecture;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Distributed Hypixel API Service - Migrated to Distributed Architecture
 * 
 * This service has been migrated from the monolithic structure to work with
 * the new distributed microservice architecture, integrating with:
 * - MongoDB for persistent price data
 * - Redis cluster for real-time caching
 * - DataMutex for transaction safety
 * - Distributed state synchronization
 * 
 * CRITICAL CORRECTION MAINTAINED:
 * - quick_status.sellPrice: Price at which players SELL to the system
 * - quick_status.buyPrice: Price at which players BUY from the system
 * 
 * This ensures compatibility with popular client modifications and prevents
 * economic confusion that could lead to exploits.
 */
public class DistributedHypixelAPIService {
    
    private static final Logger logger = Logger.getLogger(DistributedHypixelAPIService.class.getName());
    
    // Distributed architecture components
    private final MongoDBSchema mongoDBSchema;
    private final StateSynchronizationLayer stateLayer;
    private final DataMutexService dataMutexService;
    private final Gson gson;
    
    // API endpoint configuration
    private static final String API_BASE_URL = "/api/v1/bazaar";
    private static final String QUICK_STATUS_ENDPOINT = "/quick_status";
    private static final String FULL_STATUS_ENDPOINT = "/full_status";
    
    // Cache configuration
    private static final long CACHE_DURATION = 1000L; // 1 second
    private static final long MAX_CACHE_AGE = 5000L; // 5 seconds
    
    // Price update configuration
    private static final int PRICE_UPDATE_INTERVAL = 1; // seconds
    private static final int MAX_PRICE_CHANGE_PERCENT = 10; // 10% max change per update
    
    public DistributedHypixelAPIService(MongoDBSchema mongoDBSchema, 
                                      StateSynchronizationLayer stateLayer,
                                      DataMutexService dataMutexService) {
        this.mongoDBSchema = mongoDBSchema;
        this.stateLayer = stateLayer;
        this.dataMutexService = dataMutexService;
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    }
    
    /**
     * Initialize the distributed API service
     */
    public CompletableFuture<Void> initialize() {
        logger.info("Initializing Distributed Hypixel API Service");
        
        return CompletableFuture.runAsync(() -> {
            try {
                // Initialize default items in MongoDB
                initializeDefaultItems();
                
                // Start distributed price update task
                startDistributedPriceUpdateTask();
                
                logger.info("Distributed Hypixel API Service initialized successfully");
                
            } catch (Exception e) {
                logger.severe("Failed to initialize Distributed Hypixel API Service: " + e.getMessage());
                throw new RuntimeException("API service initialization failed", e);
            }
        });
    }
    
    /**
     * Get quick status for item (real-time from Redis cache)
     */
    public CompletableFuture<JsonObject> getQuickStatus(String itemId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Try to get from Redis cache first
                return stateLayer.getBazaarPrices().thenApply(prices -> {
                    StateSynchronizationLayer.BazaarPrice price = prices.get(itemId);
                    if (price != null) {
                        return createQuickStatusResponse(itemId, price);
                    }
                    
                    // Fallback to MongoDB if not in cache
                    return getQuickStatusFromDatabase(itemId).join();
                }).join();
                
            } catch (Exception e) {
                logger.severe("Error getting quick status for item: " + itemId + " - " + e.getMessage());
                return createErrorResponse("Failed to get price data");
            }
        });
    }
    
    /**
     * Get full status for item (from MongoDB with orderbook)
     */
    public CompletableFuture<JsonObject> getFullStatus(String itemId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Use distributed lock to ensure consistency
                return dataMutexService.executeWithLock(
                    "bazaar_item:" + itemId,
                    30,
                    () -> {
                        try {
                            // Get current price from cache
                            StateSynchronizationLayer.BazaarPrice currentPrice = 
                                stateLayer.getBazaarPrices().join().get(itemId);
                            
                            if (currentPrice == null) {
                                return createErrorResponse("Item not found");
                            }
                            
                            // Get orderbook from MongoDB
                            List<MongoDBSchema.BazaarOrder> buyOrders = 
                                mongoDBSchema.getBazaarOrders(itemId, true, 10).join();
                            List<MongoDBSchema.BazaarOrder> sellOrders = 
                                mongoDBSchema.getBazaarOrders(itemId, false, 10).join();
                            
                            return createFullStatusResponse(itemId, currentPrice, buyOrders, sellOrders);
                            
                        } catch (Exception e) {
                            logger.severe("Error in full status operation: " + e.getMessage());
                            return createErrorResponse("Internal server error");
                        }
                    }
                ).join();
                
            } catch (Exception e) {
                logger.severe("Error getting full status for item: " + itemId + " - " + e.getMessage());
                return createErrorResponse("Failed to get full status");
            }
        });
    }
    
    /**
     * Update item prices (distributed operation)
     */
    public CompletableFuture<Boolean> updateItemPrices(String itemId, double newBuyPrice, double newSellPrice) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Use distributed lock to prevent race conditions
                return dataMutexService.executeWithLock(
                    "bazaar_item:" + itemId,
                    15,
                    () -> {
                        try {
                            // Validate price changes
                            StateSynchronizationLayer.BazaarPrice currentPrice = 
                                stateLayer.getBazaarPrices().join().get(itemId);
                            
                            if (currentPrice != null) {
                                double buyChangePercent = Math.abs(newBuyPrice - currentPrice.getBuyPrice()) / currentPrice.getBuyPrice() * 100;
                                double sellChangePercent = Math.abs(newSellPrice - currentPrice.getSellPrice()) / currentPrice.getSellPrice() * 100;
                                
                                if (buyChangePercent > MAX_PRICE_CHANGE_PERCENT || sellChangePercent > MAX_PRICE_CHANGE_PERCENT) {
                                    logger.warning("Price change too large for item " + itemId + 
                                        ": buy " + buyChangePercent + "%, sell " + sellChangePercent + "%");
                                    return false;
                                }
                            }
                            
                            // Create new price data
                            StateSynchronizationLayer.BazaarPrice newPrice = 
                                new StateSynchronizationLayer.BazaarPrice(
                                    itemId, newBuyPrice, newSellPrice, java.lang.System.currentTimeMillis()
                                );
                            
                            // Update Redis cache
                            Map<String, StateSynchronizationLayer.BazaarPrice> prices = 
                                stateLayer.getBazaarPrices().join();
                            prices.put(itemId, newPrice);
                            stateLayer.updateBazaarPrices(prices).join();
                            
                            logger.info("Updated prices for item " + itemId + 
                                ": buy=" + newBuyPrice + ", sell=" + newSellPrice);
                            
                            return true;
                            
                        } catch (Exception e) {
                            logger.severe("Error updating item prices: " + e.getMessage());
                            return false;
                        }
                    }
                ).join();
                
            } catch (Exception e) {
                logger.severe("Error in price update operation: " + e.getMessage());
                return false;
            }
        });
    }
    
    /**
     * Create bazaar order (distributed operation)
     */
    public CompletableFuture<String> createBazaarOrder(UUID playerUUID, String itemId, 
                                                     int amount, double price, boolean isBuyOrder) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Use distributed lock to ensure consistency
                return dataMutexService.executeWithLock(
                    "bazaar_item:" + itemId,
                    30,
                    () -> {
                        try {
                            // Create order in MongoDB
                            String orderId = mongoDBSchema.createBazaarOrder(
                                playerUUID, itemId, amount, price, isBuyOrder
                            ).join();
                            
                            logger.info("Created bazaar order: " + orderId + 
                                " for player: " + playerUUID + 
                                ", item: " + itemId + 
                                ", amount: " + amount + 
                                ", price: " + price + 
                                ", buyOrder: " + isBuyOrder);
                            
                            return orderId;
                            
                        } catch (Exception e) {
                            logger.severe("Error creating bazaar order: " + e.getMessage());
                            throw new RuntimeException("Failed to create order", e);
                        }
                    }
                ).join();
                
            } catch (Exception e) {
                logger.severe("Error in order creation operation: " + e.getMessage());
                throw new RuntimeException("Order creation failed", e);
            }
        });
    }
    
    /**
     * Initialize default items in distributed system
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
            initializeItemInDistributedSystem(itemId);
        }
    }
    
    /**
     * Initialize item in distributed system
     */
    private void initializeItemInDistributedSystem(String itemId) {
        try {
            double basePrice = getBasePriceForItem(itemId);
            
            // Create initial price data
            StateSynchronizationLayer.BazaarPrice initialPrice = 
                new StateSynchronizationLayer.BazaarPrice(
                    itemId,
                    basePrice * 1.05, // buyPrice (players buy from system)
                    basePrice * 0.95, // sellPrice (players sell to system)
                    java.lang.System.currentTimeMillis()
                );
            
            // Update Redis cache
            Map<String, StateSynchronizationLayer.BazaarPrice> prices = 
                stateLayer.getBazaarPrices().join();
            prices.put(itemId, initialPrice);
            stateLayer.updateBazaarPrices(prices).join();
            
            logger.fine("Initialized item in distributed system: " + itemId + " with base price: " + basePrice);
            
        } catch (Exception e) {
            logger.warning("Failed to initialize item in distributed system: " + itemId + " - " + e.getMessage());
        }
    }
    
    /**
     * Start distributed price update task
     */
    private void startDistributedPriceUpdateTask() {
        Timer timer = new Timer("DistributedPriceUpdate", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateAllPricesDistributed();
            }
        }, 0L, CACHE_DURATION);
    }
    
    /**
     * Update all prices using distributed architecture
     */
    private void updateAllPricesDistributed() {
        try {
            // Get current prices from Redis cache
            Map<String, StateSynchronizationLayer.BazaarPrice> currentPrices = 
                stateLayer.getBazaarPrices().join();
            
            // Update each item price
            for (String itemId : currentPrices.keySet()) {
                updateItemPriceDistributed(itemId);
            }
            
        } catch (Exception e) {
            logger.warning("Error in distributed price update: " + e.getMessage());
        }
    }
    
    /**
     * Update single item price using distributed architecture
     */
    private void updateItemPriceDistributed(String itemId) {
        try {
            // Get current price
            StateSynchronizationLayer.BazaarPrice currentPrice = 
                stateLayer.getBazaarPrices().join().get(itemId);
            
            if (currentPrice == null) {
                return;
            }
            
            // Simulate price movement (in real implementation, this would be based on market data)
            double volatility = getVolatilityForItem(itemId);
            double buyChange = (Math.random() - 0.5) * volatility * currentPrice.getBuyPrice();
            double sellChange = (Math.random() - 0.5) * volatility * currentPrice.getSellPrice();
            
            double newBuyPrice = Math.max(0.01, currentPrice.getBuyPrice() + buyChange);
            double newSellPrice = Math.max(0.01, currentPrice.getSellPrice() + sellChange);
            
            // Ensure sell price is always less than buy price
            if (newSellPrice >= newBuyPrice) {
                newSellPrice = newBuyPrice * 0.95;
            }
            
            // Update price
            updateItemPrices(itemId, newBuyPrice, newSellPrice);
            
        } catch (Exception e) {
            logger.warning("Error updating price for item " + itemId + ": " + e.getMessage());
        }
    }
    
    /**
     * Get volatility for item (higher for rare items)
     */
    private double getVolatilityForItem(String itemId) {
        return switch (itemId) {
            case "DIAMOND", "EMERALD", "NETHER_STAR" -> 0.05; // 5% volatility
            case "GOLD_INGOT", "IRON_INGOT", "REDSTONE" -> 0.03; // 3% volatility
            case "COAL", "COBBLESTONE", "WHEAT" -> 0.01; // 1% volatility
            default -> 0.02; // 2% default volatility
        };
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
            case "OAK_LOG" -> 1.0;
            case "BIRCH_LOG" -> 1.0;
            case "SPRUCE_LOG" -> 1.0;
            case "JUNGLE_LOG" -> 1.0;
            case "ACACIA_LOG" -> 1.0;
            case "DARK_OAK_LOG" -> 1.0;
            case "SAND" -> 0.5;
            case "GRAVEL" -> 0.5;
            case "CLAY" -> 1.0;
            case "SOUL_SAND" -> 2.0;
            case "NETHERRACK" -> 0.5;
            case "NETHER_BRICK" -> 2.0;
            case "END_STONE" -> 3.0;
            case "PRISMARINE_SHARD" -> 4.0;
            case "PRISMARINE_CRYSTALS" -> 6.0;
            case "SPONGE" -> 8.0;
            case "WET_SPONGE" -> 8.0;
            case "SEA_LANTERN" -> 10.0;
            case "KELP" -> 1.0;
            case "DRIED_KELP" -> 1.0;
            case "HEART_OF_THE_SEA" -> 50.0;
            case "NAUTILUS_SHELL" -> 20.0;
            case "TRIDENT" -> 100.0;
            case "PHANTOM_MEMBRANE" -> 25.0;
            case "ELYTRA" -> 200.0;
            case "DRAGON_HEAD" -> 500.0;
            case "DRAGON_EGG" -> 1000.0;
            case "NETHER_STAR" -> 100.0;
            case "WITHER_SKELETON_SKULL" -> 75.0;
            case "WITHER_ROSE" -> 150.0;
            case "TOTEM_OF_UNDYING" -> 300.0;
            case "SHULKER_SHELL" -> 40.0;
            case "END_CRYSTAL" -> 60.0;
            case "ENDER_EYE" -> 15.0;
            case "ENDER_PEARL" -> 5.0;
            case "ENDER_CHEST" -> 80.0;
            default -> 1.0;
        };
    }
    
    /**
     * Get quick status from database (fallback)
     */
    private CompletableFuture<JsonObject> getQuickStatusFromDatabase(String itemId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // This would query MongoDB for the latest price
                // For now, return a default response
                double basePrice = getBasePriceForItem(itemId);
                
                StateSynchronizationLayer.BazaarPrice price = 
                    new StateSynchronizationLayer.BazaarPrice(
                        itemId, basePrice * 1.05, basePrice * 0.95, java.lang.System.currentTimeMillis()
                    );
                
                return createQuickStatusResponse(itemId, price);
                
            } catch (Exception e) {
                logger.severe("Error getting quick status from database: " + e.getMessage());
                return createErrorResponse("Database error");
            }
        });
    }
    
    /**
     * Create quick status response
     */
    private JsonObject createQuickStatusResponse(String itemId, StateSynchronizationLayer.BazaarPrice price) {
        JsonObject response = new JsonObject();
        response.addProperty("success", true);
        response.addProperty("itemId", itemId);
        
        JsonObject quickStatus = new JsonObject();
        quickStatus.addProperty("sellPrice", price.getSellPrice()); // Players SELL to system
        quickStatus.addProperty("buyPrice", price.getBuyPrice());   // Players BUY from system
        quickStatus.addProperty("sellVolume", 0);
        quickStatus.addProperty("buyVolume", 0);
        quickStatus.addProperty("timestamp", price.getTimestamp());
        
        response.add("quick_status", quickStatus);
        return response;
    }
    
    /**
     * Create full status response
     */
    private JsonObject createFullStatusResponse(String itemId, 
                                              StateSynchronizationLayer.BazaarPrice currentPrice,
                                              List<MongoDBSchema.BazaarOrder> buyOrders,
                                              List<MongoDBSchema.BazaarOrder> sellOrders) {
        JsonObject response = new JsonObject();
        response.addProperty("success", true);
        response.addProperty("itemId", itemId);
        
        JsonObject quickStatus = new JsonObject();
        quickStatus.addProperty("sellPrice", currentPrice.getSellPrice());
        quickStatus.addProperty("buyPrice", currentPrice.getBuyPrice());
        quickStatus.addProperty("sellVolume", 0);
        quickStatus.addProperty("buyVolume", 0);
        quickStatus.addProperty("timestamp", currentPrice.getTimestamp());
        
        response.add("quick_status", quickStatus);
        
        // Add orderbook data
        JsonObject orderbook = new JsonObject();
        
        JsonObject buyOrdersJson = new JsonObject();
        for (MongoDBSchema.BazaarOrder order : buyOrders) {
            JsonObject orderJson = new JsonObject();
            orderJson.addProperty("amount", order.amount);
            orderJson.addProperty("price", order.price);
            orderJson.addProperty("timestamp", order.createdAt.getTime());
            buyOrdersJson.add(order.orderId, orderJson);
        }
        
        JsonObject sellOrdersJson = new JsonObject();
        for (MongoDBSchema.BazaarOrder order : sellOrders) {
            JsonObject orderJson = new JsonObject();
            orderJson.addProperty("amount", order.amount);
            orderJson.addProperty("price", order.price);
            orderJson.addProperty("timestamp", order.createdAt.getTime());
            sellOrdersJson.add(order.orderId, orderJson);
        }
        
        orderbook.add("buyOrders", buyOrdersJson);
        orderbook.add("sellOrders", sellOrdersJson);
        response.add("orderbook", orderbook);
        
        return response;
    }
    
    /**
     * Create error response
     */
    private JsonObject createErrorResponse(String errorMessage) {
        JsonObject response = new JsonObject();
        response.addProperty("success", false);
        response.addProperty("error", errorMessage);
        response.addProperty("timestamp", java.lang.System.currentTimeMillis());
        return response;
    }
}
