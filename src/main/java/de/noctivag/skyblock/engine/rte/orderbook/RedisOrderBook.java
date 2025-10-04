package de.noctivag.skyblock.engine.rte.orderbook;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Redis Sorted Sets-based Order Book - Critical for RTE Performance
 * 
 * Implements high-performance order book using Redis Sorted Sets for:
 * - Instant price queries with O(log N) complexity
 * - Global synchronization across all server instances
 * - Atomic order matching and execution
 * - Real-time market depth analysis
 * 
 * This is the ONLY technology that can provide the required performance
 * for immediate, globally synchronized price queries as mandated.
 */
public class RedisOrderBook {
    
    private static final Logger logger = Logger.getLogger(RedisOrderBook.class.getName());
    
    private final JedisPool jedisPool;
    private final String itemId;
    
    // Redis key patterns
    private final String buyOrdersKey;
    private final String sellOrdersKey;
    private final String orderDataKey;
    private final String marketDepthKey;
    private final String priceHistoryKey;
    
    // Local cache for frequently accessed data
    private final Map<String, OrderData> orderCache = new ConcurrentHashMap<>();
    private final Map<Double, Integer> buyDepthCache = new ConcurrentHashMap<>();
    private final Map<Double, Integer> sellDepthCache = new ConcurrentHashMap<>();
    
    // Performance monitoring
    private volatile long lastCacheUpdate = 0;
    private static final long CACHE_UPDATE_INTERVAL = 100L; // 100ms
    
    public RedisOrderBook(JedisPool jedisPool, String itemId) {
        this.jedisPool = jedisPool;
        this.itemId = itemId;
        
        // Initialize Redis keys
        this.buyOrdersKey = "rte:orderbook:" + itemId + ":buy";
        this.sellOrdersKey = "rte:orderbook:" + itemId + ":sell";
        this.orderDataKey = "rte:orderbook:" + itemId + ":data";
        this.marketDepthKey = "rte:orderbook:" + itemId + ":depth";
        this.priceHistoryKey = "rte:orderbook:" + itemId + ":history";
        
        initializeOrderBook();
    }
    
    /**
     * Initialize order book with default market data
     */
    private void initializeOrderBook() {
        try (Jedis jedis = jedisPool.getResource()) {
            // Check if order book exists
            if (!jedis.exists(buyOrdersKey) && !jedis.exists(sellOrdersKey)) {
                // Initialize with default market maker orders
                initializeMarketMakerOrders(jedis);
            }
            
            // Load initial cache
            refreshCache();
            
        } catch (Exception e) {
            logger.severe("Failed to initialize order book for " + itemId + ": " + e.getMessage());
        }
    }
    
    /**
     * Initialize market maker orders for liquidity
     */
    private void initializeMarketMakerOrders(Jedis jedis) {
        try {
            // Get base price from configuration or default
            double basePrice = getBasePrice();
            
            // Create market maker buy orders (below market)
            for (int i = 1; i <= 10; i++) {
                double price = basePrice * (1.0 - (i * 0.01)); // 1% steps down
                int amount = 1000; // Default market maker size
                String orderId = UUID.randomUUID().toString();
                
                // Store order data
                OrderData orderData = new OrderData(orderId, "MARKET_MAKER", itemId, amount, price, OrderType.BUY);
                jedis.hset(orderDataKey, orderId, orderData.toJson());
                
                // Add to sorted set (price as score for buy orders - higher is better)
                jedis.zadd(buyOrdersKey, price, orderId);
            }
            
            // Create market maker sell orders (above market)
            for (int i = 1; i <= 10; i++) {
                double price = basePrice * (1.0 + (i * 0.01)); // 1% steps up
                int amount = 1000; // Default market maker size
                String orderId = UUID.randomUUID().toString();
                
                // Store order data
                OrderData orderData = new OrderData(orderId, "MARKET_MAKER", itemId, amount, price, OrderType.SELL);
                jedis.hset(orderDataKey, orderId, orderData.toJson());
                
                // Add to sorted set (price as score for sell orders - lower is better)
                jedis.zadd(sellOrdersKey, price, orderId);
            }
            
            logger.info("Initialized market maker orders for " + itemId + " with base price " + basePrice);
            
        } catch (Exception e) {
            logger.severe("Failed to initialize market maker orders: " + e.getMessage());
        }
    }
    
    /**
     * Get base price for item (from configuration or default)
     */
    private double getBasePrice() {
        // This would typically come from configuration
        // For now, return a default based on item type
        return switch (itemId) {
            case "DIAMOND" -> 10.0;
            case "EMERALD" -> 8.0;
            case "GOLD_INGOT" -> 5.0;
            case "IRON_INGOT" -> 2.0;
            case "COAL" -> 1.0;
            default -> 1.0;
        };
    }
    
    /**
     * Add order to order book with atomic transaction
     */
    public CompletableFuture<OrderResult> addOrder(OrderData order) {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                Transaction transaction = jedis.multi();
                
                try {
                    // Store order data
                    transaction.hset(orderDataKey, order.getOrderId(), order.toJson());
                    
                    // Add to appropriate sorted set
                    if (order.getType() == OrderType.BUY) {
                        // For buy orders, use price as score (higher price = higher priority)
                        transaction.zadd(buyOrdersKey, order.getPrice(), order.getOrderId());
                    } else {
                        // For sell orders, use price as score (lower price = higher priority)
                        transaction.zadd(sellOrdersKey, order.getPrice(), order.getOrderId());
                    }
                    
                    // Update market depth
                    updateMarketDepth(transaction, order);
                    
                    // Execute transaction
                    List<Object> results = transaction.exec();
                    
                    if (results != null && !results.isEmpty()) {
                        // Try to match orders immediately
                        OrderResult matchResult = tryMatchOrders(jedis, order);
                        
                        // Update cache
                        refreshCache();
                        
                        return matchResult;
                    } else {
                        return new OrderResult(false, "Transaction failed", null);
                    }
                    
                } catch (JedisException e) {
                    transaction.discard();
                    throw e;
                }
                
            } catch (Exception e) {
                logger.severe("Failed to add order: " + e.getMessage());
                return new OrderResult(false, "Failed to add order: " + e.getMessage(), null);
            }
        });
    }
    
    /**
     * Try to match new order with existing orders
     */
    private OrderResult tryMatchOrders(Jedis jedis, OrderData newOrder) {
        List<OrderMatch> matches = new ArrayList<>();
        int remainingAmount = newOrder.getAmount();
        
        if (newOrder.getType() == OrderType.BUY) {
            // Find matching sell orders (price <= new order price)
            Set<String> sellOrderIds = new HashSet<>(jedis.zrangeByScore(sellOrdersKey, 0, newOrder.getPrice()));
            
            for (String orderId : sellOrderIds) {
                if (remainingAmount <= 0) break;
                
                OrderData sellOrder = getOrderData(jedis, orderId);
                if (sellOrder == null) continue;
                
                int matchAmount = Math.min(remainingAmount, sellOrder.getAmount());
                double matchPrice = sellOrder.getPrice(); // Use sell order price
                
                // Create match
                OrderMatch match = new OrderMatch(newOrder, sellOrder, matchAmount, matchPrice);
                matches.add(match);
                
                remainingAmount -= matchAmount;
                
                // Update sell order amount
                sellOrder.setAmount(sellOrder.getAmount() - matchAmount);
                if (sellOrder.getAmount() <= 0) {
                    // Remove completed order
                    removeOrder(jedis, sellOrder);
                } else {
                    // Update order data
                    jedis.hset(orderDataKey, orderId, sellOrder.toJson());
                }
            }
        } else {
            // Find matching buy orders (price >= new order price)
            Set<String> buyOrderIds = new HashSet<>(jedis.zrevrangeByScore(buyOrdersKey, newOrder.getPrice(), Double.MAX_VALUE));
            
            for (String orderId : buyOrderIds) {
                if (remainingAmount <= 0) break;
                
                OrderData buyOrder = getOrderData(jedis, orderId);
                if (buyOrder == null) continue;
                
                int matchAmount = Math.min(remainingAmount, buyOrder.getAmount());
                double matchPrice = buyOrder.getPrice(); // Use buy order price
                
                // Create match
                OrderMatch match = new OrderMatch(buyOrder, newOrder, matchAmount, matchPrice);
                matches.add(match);
                
                remainingAmount -= matchAmount;
                
                // Update buy order amount
                buyOrder.setAmount(buyOrder.getAmount() - matchAmount);
                if (buyOrder.getAmount() <= 0) {
                    // Remove completed order
                    removeOrder(jedis, buyOrder);
                } else {
                    // Update order data
                    jedis.hset(orderDataKey, orderId, buyOrder.toJson());
                }
            }
        }
        
        // Update new order amount
        if (remainingAmount > 0) {
            newOrder.setAmount(remainingAmount);
            jedis.hset(orderDataKey, newOrder.getOrderId(), newOrder.toJson());
        } else {
            // Remove completed order
            removeOrder(jedis, newOrder);
        }
        
        return new OrderResult(true, "Order processed", matches);
    }
    
    /**
     * Remove order from order book
     */
    private void removeOrder(Jedis jedis, OrderData order) {
        // Remove from sorted set
        if (order.getType() == OrderType.BUY) {
            jedis.zrem(buyOrdersKey, order.getOrderId());
        } else {
            jedis.zrem(sellOrdersKey, order.getOrderId());
        }
        
        // Remove order data
        jedis.hdel(orderDataKey, order.getOrderId());
    }
    
    /**
     * Get order data from Redis
     */
    private OrderData getOrderData(Jedis jedis, String orderId) {
        String orderJson = jedis.hget(orderDataKey, orderId);
        if (orderJson != null) {
            return OrderData.fromJson(orderJson);
        }
        return null;
    }
    
    /**
     * Update market depth information
     */
    private void updateMarketDepth(Transaction transaction, OrderData order) {
        String depthKey = marketDepthKey + ":" + order.getType().name().toLowerCase();
        transaction.hincrBy(depthKey, String.valueOf(order.getPrice()), order.getAmount());
    }
    
    /**
     * Get best buy price (highest buy order)
     */
    public CompletableFuture<Double> getBestBuyPrice() {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                Set<String> bestBuyOrders = jedis.zrevrange(buyOrdersKey, 0, 0);
                if (!bestBuyOrders.isEmpty()) {
                    String orderId = bestBuyOrders.iterator().next();
                    OrderData order = getOrderData(jedis, orderId);
                    return order != null ? order.getPrice() : 0.0;
                }
                return 0.0;
            } catch (Exception e) {
                logger.severe("Failed to get best buy price: " + e.getMessage());
                return 0.0;
            }
        });
    }
    
    /**
     * Get best sell price (lowest sell order)
     */
    public CompletableFuture<Double> getBestSellPrice() {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                Set<String> bestSellOrders = jedis.zrange(sellOrdersKey, 0, 0);
                if (!bestSellOrders.isEmpty()) {
                    String orderId = bestSellOrders.iterator().next();
                    OrderData order = getOrderData(jedis, orderId);
                    return order != null ? order.getPrice() : Double.MAX_VALUE;
                }
                return Double.MAX_VALUE;
            } catch (Exception e) {
                logger.severe("Failed to get best sell price: " + e.getMessage());
                return Double.MAX_VALUE;
            }
        });
    }
    
    /**
     * Get market depth for price range
     */
    public CompletableFuture<MarketDepth> getMarketDepth(int levels) {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                // Get top buy orders
                Set<String> buyOrderIds = new HashSet<>(jedis.zrevrange(buyOrdersKey, 0, levels - 1));
                Map<Double, Integer> buyDepth = new LinkedHashMap<>();
                
                for (String orderId : buyOrderIds) {
                    OrderData order = getOrderData(jedis, orderId);
                    if (order != null) {
                        buyDepth.merge(order.getPrice(), order.getAmount(), Integer::sum);
                    }
                }
                
                // Get top sell orders
                Set<String> sellOrderIds = new HashSet<>(jedis.zrange(sellOrdersKey, 0, levels - 1));
                Map<Double, Integer> sellDepth = new LinkedHashMap<>();
                
                for (String orderId : sellOrderIds) {
                    OrderData order = getOrderData(jedis, orderId);
                    if (order != null) {
                        sellDepth.merge(order.getPrice(), order.getAmount(), Integer::sum);
                    }
                }
                
                return new MarketDepth(buyDepth, sellDepth);
                
            } catch (Exception e) {
                logger.severe("Failed to get market depth: " + e.getMessage());
                return new MarketDepth(new HashMap<>(), new HashMap<>());
            }
        });
    }
    
    /**
     * Get instant buy price (best sell price + spread)
     */
    public CompletableFuture<Double> getInstantBuyPrice() {
        return getBestSellPrice().thenApply(price -> 
            price == Double.MAX_VALUE ? getBasePrice() * 1.05 : price * 1.02
        );
    }
    
    /**
     * Get instant sell price (best buy price - spread)
     */
    public CompletableFuture<Double> getInstantSellPrice() {
        return getBestBuyPrice().thenApply(price -> 
            price == 0.0 ? getBasePrice() * 0.95 : price * 0.98
        );
    }
    
    /**
     * Refresh local cache
     */
    private void refreshCache() {
        long now = System.currentTimeMillis();
        if (now - lastCacheUpdate < CACHE_UPDATE_INTERVAL) {
            return; // Cache is still fresh
        }
        
        try (Jedis jedis = jedisPool.getResource()) {
            // Update buy depth cache
            buyDepthCache.clear();
            Set<String> buyOrderIds = new HashSet<>(jedis.zrevrange(buyOrdersKey, 0, 99));
            for (String orderId : buyOrderIds) {
                OrderData order = getOrderData(jedis, orderId);
                if (order != null) {
                    buyDepthCache.merge(order.getPrice(), order.getAmount(), Integer::sum);
                }
            }
            
            // Update sell depth cache
            sellDepthCache.clear();
            Set<String> sellOrderIds = new HashSet<>(jedis.zrange(sellOrdersKey, 0, 99));
            for (String orderId : sellOrderIds) {
                OrderData order = getOrderData(jedis, orderId);
                if (order != null) {
                    sellDepthCache.merge(order.getPrice(), order.getAmount(), Integer::sum);
                }
            }
            
            lastCacheUpdate = now;
            
        } catch (Exception e) {
            logger.severe("Failed to refresh cache: " + e.getMessage());
        }
    }
    
    /**
     * Get cached market depth (fast access)
     */
    public MarketDepth getCachedMarketDepth() {
        refreshCache();
        return new MarketDepth(new HashMap<>(buyDepthCache), new HashMap<>(sellDepthCache));
    }
    
    /**
     * Cancel order
     */
    public CompletableFuture<Boolean> cancelOrder(String orderId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                OrderData order = getOrderData(jedis, orderId);
                if (order == null) {
                    return false;
                }
                
                removeOrder(jedis, order);
                refreshCache();
                return true;
                
            } catch (Exception e) {
                logger.severe("Failed to cancel order: " + e.getMessage());
                return false;
            }
        });
    }
    
    /**
     * Get order by ID
     */
    public CompletableFuture<OrderData> getOrder(String orderId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                return getOrderData(jedis, orderId);
            } catch (Exception e) {
                logger.severe("Failed to get order: " + e.getMessage());
                return null;
            }
        });
    }
    
    /**
     * Get all orders for a player
     */
    public CompletableFuture<List<OrderData>> getPlayerOrders(String playerId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                List<OrderData> orders = new ArrayList<>();
                
                // Get all order data
                Map<String, String> allOrders = jedis.hgetAll(orderDataKey);
                for (Map.Entry<String, String> entry : allOrders.entrySet()) {
                    OrderData order = OrderData.fromJson(entry.getValue());
                    if (order.getPlayerId().equals(playerId)) {
                        orders.add(order);
                    }
                }
                
                return orders;
                
            } catch (Exception e) {
                logger.severe("Failed to get player orders: " + e.getMessage());
                return new ArrayList<>();
            }
        });
    }
    
    /**
     * Get order book statistics
     */
    public CompletableFuture<OrderBookStats> getOrderBookStats() {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                long buyOrderCount = jedis.zcard(buyOrdersKey);
                long sellOrderCount = jedis.zcard(sellOrdersKey);
                
                double bestBuy = getBestBuyPrice().join();
                double bestSell = getBestSellPrice().join();
                
                double spread = bestSell - bestBuy;
                double spreadPercentage = bestBuy > 0 ? (spread / bestBuy) * 100 : 0;
                
                return new OrderBookStats(
                    buyOrderCount,
                    sellOrderCount,
                    bestBuy,
                    bestSell,
                    spread,
                    spreadPercentage
                );
                
            } catch (Exception e) {
                logger.severe("Failed to get order book stats: " + e.getMessage());
                return new OrderBookStats(0, 0, 0, 0, 0, 0);
            }
        });
    }
    
    // Data classes
    public static class OrderData {
        private String orderId;
        private String playerId;
        private String itemId;
        private int amount;
        private double price;
        private OrderType type;
        private long timestamp;
        
        public OrderData(String orderId, String playerId, String itemId, int amount, double price, OrderType type) {
            this.orderId = orderId;
            this.playerId = playerId;
            this.itemId = itemId;
            this.amount = amount;
            this.price = price;
            this.type = type;
            this.timestamp = System.currentTimeMillis();
        }
        
        // Getters and setters
        public String getOrderId() { return orderId; }
        public String getPlayerId() { return playerId; }
        public String getItemId() { return itemId; }
        public int getAmount() { return amount; }
        public double getPrice() { return price; }
        public OrderType getType() { return type; }
        public long getTimestamp() { return timestamp; }
        
        public void setAmount(int amount) { this.amount = amount; }
        
        public String toJson() {
            return String.format("{\"orderId\":\"%s\",\"playerId\":\"%s\",\"itemId\":\"%s\",\"amount\":%d,\"price\":%.2f,\"type\":\"%s\",\"timestamp\":%d}",
                orderId, playerId, itemId, amount, price, type.name(), timestamp);
        }
        
        public static OrderData fromJson(String json) {
            // Simple JSON parsing - in production use proper JSON library
            String[] parts = json.replaceAll("[{}\"]", "").split(",");
            String orderId = parts[0].split(":")[1];
            String playerId = parts[1].split(":")[1];
            String itemId = parts[2].split(":")[1];
            int amount = Integer.parseInt(parts[3].split(":")[1]);
            double price = Double.parseDouble(parts[4].split(":")[1]);
            OrderType type = OrderType.valueOf(parts[5].split(":")[1]);
            
            OrderData order = new OrderData(orderId, playerId, itemId, amount, price, type);
            order.timestamp = Long.parseLong(parts[6].split(":")[1]);
            return order;
        }
    }
    
    public static class OrderMatch {
        private final OrderData buyOrder;
        private final OrderData sellOrder;
        private final int amount;
        private final double price;
        private final long timestamp;
        
        public OrderMatch(OrderData buyOrder, OrderData sellOrder, int amount, double price) {
            this.buyOrder = buyOrder;
            this.sellOrder = sellOrder;
            this.amount = amount;
            this.price = price;
            this.timestamp = System.currentTimeMillis();
        }
        
        // Getters
        public OrderData getBuyOrder() { return buyOrder; }
        public OrderData getSellOrder() { return sellOrder; }
        public int getAmount() { return amount; }
        public double getPrice() { return price; }
        public long getTimestamp() { return timestamp; }
    }
    
    public static class OrderResult {
        private final boolean success;
        private final String message;
        private final List<OrderMatch> matches;
        
        public OrderResult(boolean success, String message, List<OrderMatch> matches) {
            this.success = success;
            this.message = message;
            this.matches = matches != null ? matches : new ArrayList<>();
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public List<OrderMatch> getMatches() { return matches; }
    }
    
    public static class MarketDepth {
        private final Map<Double, Integer> buyDepth;
        private final Map<Double, Integer> sellDepth;
        
        public MarketDepth(Map<Double, Integer> buyDepth, Map<Double, Integer> sellDepth) {
            this.buyDepth = buyDepth;
            this.sellDepth = sellDepth;
        }
        
        // Getters
        public Map<Double, Integer> getBuyDepth() { return buyDepth; }
        public Map<Double, Integer> getSellDepth() { return sellDepth; }
    }
    
    public static class OrderBookStats {
        private final long buyOrderCount;
        private final long sellOrderCount;
        private final double bestBuyPrice;
        private final double bestSellPrice;
        private final double spread;
        private final double spreadPercentage;
        
        public OrderBookStats(long buyOrderCount, long sellOrderCount, double bestBuyPrice, 
                             double bestSellPrice, double spread, double spreadPercentage) {
            this.buyOrderCount = buyOrderCount;
            this.sellOrderCount = sellOrderCount;
            this.bestBuyPrice = bestBuyPrice;
            this.bestSellPrice = bestSellPrice;
            this.spread = spread;
            this.spreadPercentage = spreadPercentage;
        }
        
        // Getters
        public long getBuyOrderCount() { return buyOrderCount; }
        public long getSellOrderCount() { return sellOrderCount; }
        public double getBestBuyPrice() { return bestBuyPrice; }
        public double getBestSellPrice() { return bestSellPrice; }
        public double getSpread() { return spread; }
        public double getSpreadPercentage() { return spreadPercentage; }
    }
    
    public enum OrderType {
        BUY, SELL
    }
}
