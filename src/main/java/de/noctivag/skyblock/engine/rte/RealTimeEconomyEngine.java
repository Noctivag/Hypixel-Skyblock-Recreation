package de.noctivag.skyblock.engine.rte;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.PlayerProfile;
import de.noctivag.skyblock.engine.rte.data.*;
import de.noctivag.skyblock.engine.rte.protocols.TransactionProtocol;
import de.noctivag.skyblock.engine.rte.services.EconomyDashboardService;
import de.noctivag.skyblock.engine.rte.services.HypixelAPICorrectionService;
import de.noctivag.skyblock.network.ServerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Transaction;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Real-Time Economy Engine (RTE) - Agent V Implementation
 * 
 * Verantwortlich für:
 * - Redis-basierte Bazaar/Auction House Backend
 * - Echtzeit-Preis-Synchronisation über alle Spielinstanzen
 * - Atomare Transaktionsprotokolle
 * - Hypixel API Korrektur-Service
 * - Administrative Economy Management
 * - Marktstabilität und Anti-Arbitrage
 */
public class RealTimeEconomyEngine {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final ServerManager serverManager;
    private final JedisPool jedisPool;
    
    // Core Services
    private final TransactionProtocol transactionProtocol;
    private final HypixelAPICorrectionService apiCorrectionService;
    private final EconomyDashboardService dashboardService;
    
    // Real-time data structures
    private final Map<String, BazaarItemData> bazaarItems = new ConcurrentHashMap<>();
    private final Map<String, AuctionItemData> auctionItems = new ConcurrentHashMap<>();
    private final Map<UUID, List<BazaarOrder>> playerOrders = new ConcurrentHashMap<>();
    private final Map<UUID, List<AuctionBid>> playerBids = new ConcurrentHashMap<>();
    
    // Market monitoring
    private final Map<String, MarketTrend> marketTrends = new ConcurrentHashMap<>();
    private final Map<String, PriceHistory> priceHistories = new ConcurrentHashMap<>();
    
    // Real-time synchronization
    private final Set<String> subscribedChannels = ConcurrentHashMap.newKeySet();
    private final Map<String, Long> lastPriceUpdate = new ConcurrentHashMap<>();
    
    // Configuration
    private static final double BAZAAR_FEE = 0.01; // 1% fee
    private static final double AUCTION_FEE = 0.05; // 5% fee
    private static final int MAX_ORDERS_PER_PLAYER = 14;
    private static final int MAX_ORDERS_PER_ITEM = 1000;
    private static final long PRICE_UPDATE_INTERVAL = 1000L; // 1 second
    private static final long MARKET_ANALYSIS_INTERVAL = 5000L; // 5 seconds
    
    // Redis keys
    private static final String BAZAAR_ITEMS_KEY = "rte:bazaar:items";
    private static final String AUCTION_ITEMS_KEY = "rte:auction:items";
    private static final String PRICE_UPDATES_CHANNEL = "rte:price_updates";
    private static final String ORDER_UPDATES_CHANNEL = "rte:order_updates";
    private static final String TRANSACTION_CHANNEL = "rte:transactions";
    private static final String MARKET_DATA_CHANNEL = "rte:market_data";
    
    public RealTimeEconomyEngine(SkyblockPlugin SkyblockPlugin, ServerManager serverManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.serverManager = serverManager;
        this.jedisPool = serverManager.getJedisPool();
        
        // Initialize services
        this.transactionProtocol = new TransactionProtocol(this);
        this.apiCorrectionService = new HypixelAPICorrectionService(this);
        this.dashboardService = new EconomyDashboardService(this);
        
        initializeEngine();
    }
    
    private void initializeEngine() {
        try {
            // Load existing market data from Redis
            loadMarketDataFromRedis();
            
            // Initialize price monitoring
            startPriceMonitoring();
            
            // Start market analysis
            startMarketAnalysis();
            
            // Subscribe to real-time updates
            subscribeToRealTimeUpdates();
            
            // Initialize Hypixel API correction
            apiCorrectionService.initialize();
            
            // Initialize dashboard service
            dashboardService.initialize();
            
            SkyblockPlugin.getLogger().info("Real-Time Economy Engine initialized successfully");
            
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to initialize Real-Time Economy Engine", e);
        }
    }
    
    /**
     * Load market data from Redis on startup
     */
    private void loadMarketDataFromRedis() {
        try (Jedis jedis = jedisPool.getResource()) {
            // Load bazaar items
            Map<String, String> bazaarData = jedis.hgetAll(BAZAAR_ITEMS_KEY);
            for (Map.Entry<String, String> entry : bazaarData.entrySet()) {
                BazaarItemData item = BazaarItemData.fromJson(entry.getValue());
                bazaarItems.put(entry.getKey(), item);
            }
            
            // Load auction items
            Map<String, String> auctionData = jedis.hgetAll(AUCTION_ITEMS_KEY);
            for (Map.Entry<String, String> entry : auctionData.entrySet()) {
                AuctionItemData item = AuctionItemData.fromJson(entry.getValue());
                auctionItems.put(entry.getKey(), item);
            }
            
            SkyblockPlugin.getLogger().info("Loaded " + bazaarItems.size() + " bazaar items and " + 
                                  auctionItems.size() + " auction items from Redis");
            
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to load market data from Redis", e);
        }
    }
    
    /**
     * Start real-time price monitoring
     */
    private void startPriceMonitoring() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateMarketPrices();
                broadcastPriceUpdates();
            }
        }.runTaskTimerAsynchronously(SkyblockPlugin, 0L, PRICE_UPDATE_INTERVAL / 50L);
    }
    
    /**
     * Start market analysis and trend detection
     */
    private void startMarketAnalysis() {
        new BukkitRunnable() {
            @Override
            public void run() {
                analyzeMarketTrends();
                detectMarketManipulation();
                updateMarketStatistics();
            }
        }.runTaskTimerAsynchronously(SkyblockPlugin, 0L, MARKET_ANALYSIS_INTERVAL / 50L);
    }
    
    /**
     * Subscribe to real-time updates from other servers
     */
    private void subscribeToRealTimeUpdates() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try (Jedis jedis = jedisPool.getResource()) {
                    RealTimeUpdateListener listener = new RealTimeUpdateListener();
                    jedis.subscribe(listener, 
                        PRICE_UPDATES_CHANNEL,
                        ORDER_UPDATES_CHANNEL,
                        TRANSACTION_CHANNEL,
                        MARKET_DATA_CHANNEL
                    );
                } catch (Exception e) {
                    SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to subscribe to real-time updates", e);
                }
            }
        }.runTaskAsynchronously(SkyblockPlugin);
    }
    
    /**
     * Update market prices based on order book and trading activity
     */
    private void updateMarketPrices() {
        try (Jedis jedis = jedisPool.getResource()) {
            for (Map.Entry<String, BazaarItemData> entry : bazaarItems.entrySet()) {
                String itemId = entry.getKey();
                BazaarItemData item = entry.getValue();
                
                // Calculate new prices based on order book depth
                PriceData newPrices = calculateOptimalPrices(itemId, item);
                
                // Update item prices
                item.updatePrices(newPrices);
                
                // Store in Redis
                jedis.hset(BAZAAR_ITEMS_KEY, itemId, item.toJson());
                
                // Update price history
                updatePriceHistory(itemId, newPrices);
                
                lastPriceUpdate.put(itemId, java.lang.System.currentTimeMillis());
            }
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to update market prices", e);
        }
    }
    
    /**
     * Calculate optimal buy/sell prices based on order book depth
     */
    private PriceData calculateOptimalPrices(String itemId, BazaarItemData item) {
        try (Jedis jedis = jedisPool.getResource()) {
            // Get current order book
            List<BazaarOrder> buyOrders = getBuyOrders(itemId);
            List<BazaarOrder> sellOrders = getSellOrders(itemId);
            
            // Calculate instant buy price (highest sell order + spread)
            double instantBuyPrice = item.getInstantBuyPrice();
            if (!sellOrders.isEmpty()) {
                instantBuyPrice = sellOrders.get(0).getPrice() * 1.02; // 2% spread
            }
            
            // Calculate instant sell price (lowest buy order - spread)
            double instantSellPrice = item.getInstantSellPrice();
            if (!buyOrders.isEmpty()) {
                instantSellPrice = buyOrders.get(0).getPrice() * 0.98; // 2% spread
            }
            
            // Calculate average price from recent trades
            double averagePrice = calculateAveragePrice(itemId);
            
            return new PriceData(instantBuyPrice, instantSellPrice, averagePrice);
            
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to calculate optimal prices for " + itemId, e);
            return new PriceData(item.getInstantBuyPrice(), item.getInstantSellPrice(), item.getAveragePrice());
        }
    }
    
    /**
     * Calculate average price from recent trades
     */
    private double calculateAveragePrice(String itemId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String historyKey = "rte:price_history:" + itemId;
            List<String> recentPrices = jedis.lrange(historyKey, 0, 99); // Last 100 trades
            
            if (recentPrices.isEmpty()) {
                return bazaarItems.get(itemId).getAveragePrice();
            }
            
            double sum = 0.0;
            for (String priceStr : recentPrices) {
                sum += Double.parseDouble(priceStr);
            }
            
            return sum / recentPrices.size();
            
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to calculate average price for " + itemId, e);
            return bazaarItems.get(itemId).getAveragePrice();
        }
    }
    
    /**
     * Update price history for trend analysis
     */
    private void updatePriceHistory(String itemId, PriceData prices) {
        try (Jedis jedis = jedisPool.getResource()) {
            String historyKey = "rte:price_history:" + itemId;
            long timestamp = java.lang.System.currentTimeMillis();
            
            // Store price data with timestamp
            String priceData = timestamp + ":" + prices.getAveragePrice();
            jedis.lpush(historyKey, priceData);
            jedis.ltrim(historyKey, 0, 999); // Keep last 1000 entries
            jedis.expire(historyKey, 86400 * 7); // 7 days expiration
            
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to update price history for " + itemId, e);
        }
    }
    
    /**
     * Broadcast price updates to all connected servers
     */
    private void broadcastPriceUpdates() {
        try (Jedis jedis = jedisPool.getResource()) {
            for (Map.Entry<String, BazaarItemData> entry : bazaarItems.entrySet()) {
                String itemId = entry.getKey();
                BazaarItemData item = entry.getValue();
                
                // Only broadcast if price changed significantly
                if (hasSignificantPriceChange(itemId, item)) {
                    PriceUpdateMessage message = new PriceUpdateMessage(
                        itemId,
                        item.getInstantBuyPrice(),
                        item.getInstantSellPrice(),
                        item.getAveragePrice(),
                        java.lang.System.currentTimeMillis()
                    );
                    
                    jedis.publish(PRICE_UPDATES_CHANNEL, message.toJson());
                }
            }
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to broadcast price updates", e);
        }
    }
    
    /**
     * Check if price has changed significantly (more than 1%)
     */
    private boolean hasSignificantPriceChange(String itemId, BazaarItemData item) {
        Long lastUpdate = lastPriceUpdate.get(itemId);
        if (lastUpdate == null) return true;
        
        // Check if enough time has passed
        return (java.lang.System.currentTimeMillis() - lastUpdate) > PRICE_UPDATE_INTERVAL;
    }
    
    /**
     * Analyze market trends and detect anomalies
     */
    private void analyzeMarketTrends() {
        for (Map.Entry<String, BazaarItemData> entry : bazaarItems.entrySet()) {
            String itemId = entry.getKey();
            BazaarItemData item = entry.getValue();
            
            // Analyze price trends
            MarketTrend trend = analyzePriceTrend(itemId);
            marketTrends.put(itemId, trend);
            
            // Detect potential manipulation
            if (trend.isManipulationDetected()) {
                handleMarketManipulation(itemId, trend);
            }
        }
    }
    
    /**
     * Analyze price trend for a specific item
     */
    private MarketTrend analyzePriceTrend(String itemId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String historyKey = "rte:price_history:" + itemId;
            List<String> recentPrices = jedis.lrange(historyKey, 0, 99);
            
            if (recentPrices.size() < 10) {
                return new MarketTrend(MarketTrend.TrendType.STABLE, 0.0, false);
            }
            
            // Calculate price change over time
            double[] prices = new double[recentPrices.size()];
            for (int i = 0; i < recentPrices.size(); i++) {
                String[] parts = recentPrices.get(i).split(":");
                prices[i] = Double.parseDouble(parts[1]);
            }
            
            // Calculate trend
            double change = (prices[0] - prices[prices.length - 1]) / prices[prices.length - 1] * 100;
            
            MarketTrend.TrendType trendType;
            if (change > 5.0) trendType = MarketTrend.TrendType.RISING;
            else if (change < -5.0) trendType = MarketTrend.TrendType.FALLING;
            else trendType = MarketTrend.TrendType.STABLE;
            
            // Detect manipulation (rapid price changes)
            boolean manipulationDetected = Math.abs(change) > 20.0;
            
            return new MarketTrend(trendType, change, manipulationDetected);
            
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to analyze price trend for " + itemId, e);
            return new MarketTrend(MarketTrend.TrendType.STABLE, 0.0, false);
        }
    }
    
    /**
     * Detect and handle market manipulation
     */
    private void detectMarketManipulation() {
        for (Map.Entry<String, MarketTrend> entry : marketTrends.entrySet()) {
            String itemId = entry.getKey();
            MarketTrend trend = entry.getValue();
            
            if (trend.isManipulationDetected()) {
                handleMarketManipulation(itemId, trend);
            }
        }
    }
    
    /**
     * Handle detected market manipulation
     */
    private void handleMarketManipulation(String itemId, MarketTrend trend) {
        SkyblockPlugin.getLogger().warning("Market manipulation detected for " + itemId + 
                                 ": " + trend.getChangePercentage() + "% change");
        
        // Notify dashboard service
        dashboardService.reportMarketManipulation(itemId, trend);
        
        // Implement protective measures
        implementMarketProtection(itemId);
    }
    
    /**
     * Implement market protection measures
     */
    private void implementMarketProtection(String itemId) {
        try (Jedis jedis = jedisPool.getResource()) {
            // Temporarily increase fees
            String protectionKey = "rte:protection:" + itemId;
            jedis.hset(protectionKey, "fee_multiplier", "2.0");
            jedis.hset(protectionKey, "order_limit", "5");
            jedis.expire(protectionKey, 3600); // 1 hour protection
            
            // Notify all servers
            MarketProtectionMessage message = new MarketProtectionMessage(
                itemId, 
                MarketProtectionMessage.ProtectionType.MANIPULATION_DETECTED,
                java.lang.System.currentTimeMillis()
            );
            jedis.publish(MARKET_DATA_CHANNEL, message.toJson());
            
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to implement market protection for " + itemId, e);
        }
    }
    
    /**
     * Update market statistics
     */
    private void updateMarketStatistics() {
        try (Jedis jedis = jedisPool.getResource()) {
            // Calculate total market volume
            double totalVolume = 0.0;
            int totalOrders = 0;
            
            for (BazaarItemData item : bazaarItems.values()) {
                totalVolume += item.getTotalVolume();
                totalOrders += item.getOrderCount();
            }
            
            // Store statistics
            String statsKey = "rte:market_stats";
            jedis.hset(statsKey, "total_volume", String.valueOf(totalVolume));
            jedis.hset(statsKey, "total_orders", String.valueOf(totalOrders));
            jedis.hset(statsKey, "active_items", String.valueOf(bazaarItems.size()));
            jedis.hset(statsKey, "last_update", String.valueOf(java.lang.System.currentTimeMillis()));
            
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to update market statistics", e);
        }
    }
    
    /**
     * Create a new bazaar order with atomic transaction
     */
    public CompletableFuture<BazaarOrderResult> createBazaarOrder(Player player, String itemId, 
                                                                 int amount, double price, BazaarOrder.OrderType type) {
        return CompletableFuture.supplyAsync(() -> {
            return transactionProtocol.executeAtomicTransaction(() -> {
                return processBazaarOrder(player, itemId, amount, price, type);
            });
        });
    }
    
    /**
     * Process bazaar order with validation and execution
     */
    private BazaarOrderResult processBazaarOrder(Player player, String itemId, int amount, 
                                                double price, BazaarOrder.OrderType type) {
        UUID playerId = player.getUniqueId();
        
        // Validate inputs
        if (amount <= 0 || price <= 0) {
            return new BazaarOrderResult(false, "Invalid order parameters", null);
        }
        
        BazaarItemData item = bazaarItems.get(itemId);
        if (item == null) {
            return new BazaarOrderResult(false, "Item not available in bazaar", null);
        }
        
        // Check order limits
        if (getPlayerOrderCount(playerId) >= MAX_ORDERS_PER_PLAYER) {
            return new BazaarOrderResult(false, "Maximum orders reached", null);
        }
        
        // Check if player has sufficient resources
        if (type == BazaarOrder.OrderType.BUY) {
            double totalCost = amount * price * (1 + BAZAAR_FEE);
            PlayerProfile profile = SkyblockPlugin.getCorePlatform().getPlayerProfile(playerId);
            if (profile == null || !profile.hasBalance(totalCost)) {
                return new BazaarOrderResult(false, "Insufficient funds", null);
            }
        } else {
            // Check inventory for sell orders
            org.bukkit.Material material = org.bukkit.Material.valueOf(itemId);
            if (!player.getInventory().contains(material, amount)) {
                return new BazaarOrderResult(false, "Insufficient items", null);
            }
        }
        
        // Create order
        BazaarOrder order = new BazaarOrder(
            UUID.randomUUID(),
            playerId,
            itemId,
            amount,
            price,
            type,
            java.lang.System.currentTimeMillis()
        );
        
        // Store order
        storeBazaarOrder(order);
        
        // Try to match with existing orders
        BazaarOrderResult matchResult = tryMatchOrder(order);
        
        // Broadcast order update
        broadcastOrderUpdate(order);
        
        return matchResult;
    }
    
    /**
     * Try to match order with existing orders
     */
    private BazaarOrderResult tryMatchOrder(BazaarOrder newOrder) {
        List<BazaarOrder> matchingOrders = findMatchingOrders(newOrder);
        
        if (matchingOrders.isEmpty()) {
            return new BazaarOrderResult(true, "Order created successfully", new java.util.ArrayList<>());
        }
        
        // Execute matches
        int totalMatched = 0;
        double totalValue = 0.0;
        
        for (BazaarOrder matchingOrder : matchingOrders) {
            if (newOrder.getAmount() <= 0) break;
            
            int matchAmount = Math.min(newOrder.getAmount(), matchingOrder.getAmount());
            double matchPrice = matchingOrder.getPrice();
            
            // Execute trade
            executeTrade(newOrder, matchingOrder, matchAmount, matchPrice);
            
            totalMatched += matchAmount;
            totalValue += matchAmount * matchPrice;
            
            newOrder.setAmount(newOrder.getAmount() - matchAmount);
            matchingOrder.setAmount(matchingOrder.getAmount() - matchAmount);
            
            // Remove completed orders
            if (matchingOrder.getAmount() <= 0) {
                removeBazaarOrder(matchingOrder);
            }
        }
        
        // Remove completed new order
        if (newOrder.getAmount() <= 0) {
            removeBazaarOrder(newOrder);
        }
        
        return new BazaarOrderResult(true, 
            "Order matched: " + totalMatched + " items for " + totalValue + " coins", 
            new java.util.ArrayList<>());
    }
    
    /**
     * Execute trade between two orders
     */
    private void executeTrade(BazaarOrder order1, BazaarOrder order2, int amount, double price) {
        // Determine buyer and seller
        BazaarOrder buyer = order1.getType() == BazaarOrder.OrderType.BUY ? order1 : order2;
        BazaarOrder seller = order1.getType() == BazaarOrder.OrderType.SELL ? order1 : order2;
        
        // Calculate fees
        double totalValue = amount * price;
        double buyerFee = totalValue * BAZAAR_FEE;
        double sellerFee = totalValue * BAZAAR_FEE;
        
        // Update player balances
        PlayerProfile buyerProfile = SkyblockPlugin.getCorePlatform().getPlayerProfile(buyer.getPlayerId());
        PlayerProfile sellerProfile = SkyblockPlugin.getCorePlatform().getPlayerProfile(seller.getPlayerId());
        
        if (buyerProfile != null) {
            buyerProfile.removeCoins(totalValue + buyerFee);
        }
        
        if (sellerProfile != null) {
            sellerProfile.addCoins(totalValue - sellerFee);
        }
        
        // Give items to buyer
        Player buyerPlayer = Bukkit.getPlayer(buyer.getPlayerId());
        if (buyerPlayer != null) {
            org.bukkit.Material material = org.bukkit.Material.valueOf(buyer.getItemId());
            buyerPlayer.getInventory().addItem(new org.bukkit.inventory.ItemStack(material, amount));
        }
        
        // Remove items from seller
        Player sellerPlayer = Bukkit.getPlayer(seller.getPlayerId());
        if (sellerPlayer != null) {
            org.bukkit.Material material = org.bukkit.Material.valueOf(seller.getItemId());
            ItemStack toRemove = new ItemStack(material, amount);
            sellerPlayer.getInventory().removeItem(toRemove);
        }
        
        // Update bazaar item statistics
        BazaarItemData item = bazaarItems.get(buyer.getItemId());
        if (item != null) {
            item.addTrade(amount, price);
        }
        
        // Broadcast transaction
        broadcastTransaction(buyer, seller, amount, price);
    }
    
    /**
     * Find matching orders for a new order
     */
    private List<BazaarOrder> findMatchingOrders(BazaarOrder newOrder) {
        List<BazaarOrder> matchingOrders = new ArrayList<>();
        
        if (newOrder.getType() == BazaarOrder.OrderType.BUY) {
            // Find sell orders with price <= new order price
            List<BazaarOrder> sellOrders = getSellOrders(newOrder.getItemId());
            for (BazaarOrder sellOrder : sellOrders) {
                if (sellOrder.getPrice() <= newOrder.getPrice()) {
                    matchingOrders.add(sellOrder);
                }
            }
            // Sort by price (lowest first)
            matchingOrders.sort(Comparator.comparingDouble(BazaarOrder::getPrice));
        } else {
            // Find buy orders with price >= new order price
            List<BazaarOrder> buyOrders = getBuyOrders(newOrder.getItemId());
            for (BazaarOrder buyOrder : buyOrders) {
                if (buyOrder.getPrice() >= newOrder.getPrice()) {
                    matchingOrders.add(buyOrder);
                }
            }
            // Sort by price (highest first)
            matchingOrders.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
        }
        
        return matchingOrders;
    }
    
    /**
     * Store bazaar order in Redis
     */
    private void storeBazaarOrder(BazaarOrder order) {
        try (Jedis jedis = jedisPool.getResource()) {
            String orderKey = "rte:orders:" + order.getId();
            jedis.hset(orderKey, "id", order.getId().toString());
            jedis.hset(orderKey, "player_id", order.getPlayerId().toString());
            jedis.hset(orderKey, "item_id", order.getItemId());
            jedis.hset(orderKey, "amount", String.valueOf(order.getAmount()));
            jedis.hset(orderKey, "price", String.valueOf(order.getPrice()));
            jedis.hset(orderKey, "type", order.getType().name());
            jedis.hset(orderKey, "created_at", String.valueOf(order.getCreatedAt()));
            jedis.expire(orderKey, 86400 * 7); // 7 days expiration
            
            // Add to player orders
            String playerOrdersKey = "rte:player_orders:" + order.getPlayerId();
            jedis.sadd(playerOrdersKey, order.getId().toString());
            
            // Add to item orders
            String itemOrdersKey = "rte:item_orders:" + order.getItemId() + ":" + order.getType().name().toLowerCase();
            jedis.sadd(itemOrdersKey, order.getId().toString());
            
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to store bazaar order", e);
        }
    }
    
    /**
     * Remove bazaar order from Redis
     */
    private void removeBazaarOrder(BazaarOrder order) {
        try (Jedis jedis = jedisPool.getResource()) {
            String orderKey = "rte:orders:" + order.getId();
            jedis.del(orderKey);
            
            // Remove from player orders
            String playerOrdersKey = "rte:player_orders:" + order.getPlayerId();
            jedis.srem(playerOrdersKey, order.getId().toString());
            
            // Remove from item orders
            String itemOrdersKey = "rte:item_orders:" + order.getItemId() + ":" + order.getType().name().toLowerCase();
            jedis.srem(itemOrdersKey, order.getId().toString());
            
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to remove bazaar order", e);
        }
    }
    
    /**
     * Get buy orders for an item
     */
    private List<BazaarOrder> getBuyOrders(String itemId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String itemOrdersKey = "rte:item_orders:" + itemId + ":buy";
            Set<String> orderIds = jedis.smembers(itemOrdersKey);
            
            List<BazaarOrder> orders = new ArrayList<>();
            for (String orderId : orderIds) {
                String orderKey = "rte:orders:" + orderId;
                Map<String, String> orderData = jedis.hgetAll(orderKey);
                
                if (!orderData.isEmpty()) {
                    BazaarOrder order = BazaarOrder.fromRedisData(orderData);
                    orders.add(order);
                }
            }
            
            // Sort by price (highest first)
            orders.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
            return orders;
            
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to get buy orders for " + itemId, e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Get sell orders for an item
     */
    private List<BazaarOrder> getSellOrders(String itemId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String itemOrdersKey = "rte:item_orders:" + itemId + ":sell";
            Set<String> orderIds = jedis.smembers(itemOrdersKey);
            
            List<BazaarOrder> orders = new ArrayList<>();
            for (String orderId : orderIds) {
                String orderKey = "rte:orders:" + orderId;
                Map<String, String> orderData = jedis.hgetAll(orderKey);
                
                if (!orderData.isEmpty()) {
                    BazaarOrder order = BazaarOrder.fromRedisData(orderData);
                    orders.add(order);
                }
            }
            
            // Sort by price (lowest first)
            orders.sort(Comparator.comparingDouble(BazaarOrder::getPrice));
            return orders;
            
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to get sell orders for " + itemId, e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Get player order count
     */
    private int getPlayerOrderCount(UUID playerId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String playerOrdersKey = "rte:player_orders:" + playerId;
            Long count = jedis.scard(playerOrdersKey);
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to get player order count", e);
            return 0;
        }
    }
    
    /**
     * Broadcast order update to all servers
     */
    private void broadcastOrderUpdate(BazaarOrder order) {
        try (Jedis jedis = jedisPool.getResource()) {
            OrderUpdateMessage message = new OrderUpdateMessage(
                order.getId(),
                order.getPlayerId(),
                order.getItemId(),
                order.getAmount(),
                order.getPrice(),
                order.getType(),
                java.lang.System.currentTimeMillis()
            );
            
            jedis.publish(ORDER_UPDATES_CHANNEL, message.toJson());
            
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to broadcast order update", e);
        }
    }
    
    /**
     * Broadcast transaction to all servers
     */
    private void broadcastTransaction(BazaarOrder buyer, BazaarOrder seller, int amount, double price) {
        try (Jedis jedis = jedisPool.getResource()) {
            TransactionMessage message = new TransactionMessage(
                buyer.getPlayerId(),
                seller.getPlayerId(),
                buyer.getItemId(),
                amount,
                price,
                java.lang.System.currentTimeMillis()
            );
            
            jedis.publish(TRANSACTION_CHANNEL, message.toJson());
            
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to broadcast transaction", e);
        }
    }
    
    /**
     * Real-time update listener for Redis pub/sub
     */
    private class RealTimeUpdateListener extends JedisPubSub {
        @Override
        public void onMessage(String channel, String message) {
            try {
                switch (channel) {
                    case PRICE_UPDATES_CHANNEL:
                        handlePriceUpdate(message);
                        break;
                    case ORDER_UPDATES_CHANNEL:
                        handleOrderUpdate(message);
                        break;
                    case TRANSACTION_CHANNEL:
                        handleTransactionUpdate(message);
                        break;
                    case MARKET_DATA_CHANNEL:
                        handleMarketDataUpdate(message);
                        break;
                }
            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to handle real-time update", e);
            }
        }
        
        private void handlePriceUpdate(String message) {
            PriceUpdateMessage priceUpdate = PriceUpdateMessage.fromJson(message);
            BazaarItemData item = bazaarItems.get(priceUpdate.getItemId());
            
            if (item != null) {
                item.updatePrices(new PriceData(
                    priceUpdate.getInstantBuyPrice(),
                    priceUpdate.getInstantSellPrice(),
                    priceUpdate.getAveragePrice()
                ));
            }
        }
        
        private void handleOrderUpdate(String message) {
            OrderUpdateMessage orderUpdate = OrderUpdateMessage.fromJson(message);
            // Update local order cache if needed
        }
        
        private void handleTransactionUpdate(String message) {
            TransactionMessage transaction = TransactionMessage.fromJson(message);
            // Update local transaction cache if needed
        }
        
        private void handleMarketDataUpdate(String message) {
            // Handle market protection and other market data updates
        }
    }
    
    // Getters for services
    public TransactionProtocol getTransactionProtocol() {
        return transactionProtocol;
    }
    
    public HypixelAPICorrectionService getApiCorrectionService() {
        return apiCorrectionService;
    }
    
    public EconomyDashboardService getDashboardService() {
        return dashboardService;
    }
    
    public Map<String, BazaarItemData> getBazaarItems() {
        return new HashMap<>(bazaarItems);
    }
    
    public Map<String, AuctionItemData> getAuctionItems() {
        return new HashMap<>(auctionItems);
    }
    
    public Map<String, MarketTrend> getMarketTrends() {
        return new HashMap<>(marketTrends);
    }
    
    public JedisPool getJedisPool() {
        return jedisPool;
    }
    
    public SkyblockPlugin getPlugin() {
        return SkyblockPlugin;
    }
}
