package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.config.DatabaseConfig;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class BazaarService implements Service {

    private final SkyblockPlugin plugin;
    private final DatabaseConfig databaseConfig;
    private SystemStatus status = SystemStatus.DISABLED;
    private final Map<String, List<BuyOrder>> buyOrders = new ConcurrentHashMap<>();
    private final Map<String, List<SellOrder>> sellOrders = new ConcurrentHashMap<>();

    public BazaarService(SkyblockPlugin plugin, DatabaseConfig databaseConfig) {
        this.plugin = plugin;
        this.databaseConfig = databaseConfig;
        initializeBazaar();
        status = SystemStatus.RUNNING;
    }

    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        initializeBazaar();
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("BazaarService initialized.");
    }

    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        buyOrders.clear();
        sellOrders.clear();
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("BazaarService shut down.");
    }

    @Override
    public SystemStatus getStatus() {
        return status;
    }

    @Override
    public String getName() {
        return "BazaarService";
    }

    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status != SystemStatus.RUNNING) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }

    private void initializeBazaar() {
        // Initialize bazaar with some default items
        String[] defaultItems = {"wheat", "carrot", "potato", "coal", "iron_ingot", "gold_ingot", "diamond"};
        
        for (String item : defaultItems) {
            buyOrders.put(item, new ArrayList<>());
            sellOrders.put(item, new ArrayList<>());
        }

        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("BazaarService initialized with " + defaultItems.length + " default items.");
        }
    }

    public CompletableFuture<Boolean> placeBuyOrder(Player player, String item, double price, int quantity) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                BuyOrder order = new BuyOrder(player.getUniqueId(), item, price, quantity, System.currentTimeMillis());
                buyOrders.computeIfAbsent(item, k -> new ArrayList<>()).add(order);
                
                // Sort buy orders by price (highest first)
                buyOrders.get(item).sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
                
                if (plugin.getSettingsConfig().isDebugMode()) {
                    plugin.getLogger().info("Buy order placed: " + player.getName() + " - " + 
                        quantity + "x " + item + " at " + price + " coins each");
                }
                
                return true;
            } catch (Exception e) {
                plugin.getLogger().severe("Error placing buy order: " + e.getMessage());
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> placeSellOrder(Player player, String item, double price, int quantity) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SellOrder order = new SellOrder(player.getUniqueId(), item, price, quantity, System.currentTimeMillis());
                sellOrders.computeIfAbsent(item, k -> new ArrayList<>()).add(order);
                
                // Sort sell orders by price (lowest first)
                sellOrders.get(item).sort((a, b) -> Double.compare(a.getPrice(), b.getPrice()));
                
                if (plugin.getSettingsConfig().isDebugMode()) {
                    plugin.getLogger().info("Sell order placed: " + player.getName() + " - " + 
                        quantity + "x " + item + " at " + price + " coins each");
                }
                
                return true;
            } catch (Exception e) {
                plugin.getLogger().severe("Error placing sell order: " + e.getMessage());
                return false;
            }
        });
    }

    public CompletableFuture<BazaarResult> instantBuy(Player player, String item, int quantity) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<SellOrder> orders = sellOrders.getOrDefault(item, new ArrayList<>());
                if (orders.isEmpty()) {
                    return new BazaarResult(false, "No sell orders available for " + item);
                }

                double totalCost = 0;
                int remainingQuantity = quantity;
                List<SellOrder> ordersToRemove = new ArrayList<>();

                for (SellOrder order : orders) {
                    if (remainingQuantity <= 0) break;
                    
                    int orderQuantity = Math.min(remainingQuantity, order.getQuantity());
                    totalCost += orderQuantity * order.getPrice();
                    remainingQuantity -= orderQuantity;
                    
                    order.setQuantity(order.getQuantity() - orderQuantity);
                    if (order.getQuantity() <= 0) {
                        ordersToRemove.add(order);
                    }
                }

                if (remainingQuantity > 0) {
                    return new BazaarResult(false, "Not enough items available. Only " + (quantity - remainingQuantity) + " available.");
                }

                // Remove empty orders
                orders.removeAll(ordersToRemove);

                if (plugin.getSettingsConfig().isDebugMode()) {
                    plugin.getLogger().info("Instant buy completed: " + player.getName() + " - " + 
                        quantity + "x " + item + " for " + totalCost + " coins");
                }

                return new BazaarResult(true, "Successfully bought " + quantity + "x " + item + " for " + totalCost + " coins");
            } catch (Exception e) {
                plugin.getLogger().severe("Error in instant buy: " + e.getMessage());
                return new BazaarResult(false, "Error occurred during purchase");
            }
        });
    }

    public CompletableFuture<BazaarResult> instantSell(Player player, String item, int quantity) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<BuyOrder> orders = buyOrders.getOrDefault(item, new ArrayList<>());
                if (orders.isEmpty()) {
                    return new BazaarResult(false, "No buy orders available for " + item);
                }

                double totalEarnings = 0;
                int remainingQuantity = quantity;
                List<BuyOrder> ordersToRemove = new ArrayList<>();

                for (BuyOrder order : orders) {
                    if (remainingQuantity <= 0) break;
                    
                    int orderQuantity = Math.min(remainingQuantity, order.getQuantity());
                    totalEarnings += orderQuantity * order.getPrice();
                    remainingQuantity -= orderQuantity;
                    
                    order.setQuantity(order.getQuantity() - orderQuantity);
                    if (order.getQuantity() <= 0) {
                        ordersToRemove.add(order);
                    }
                }

                if (remainingQuantity > 0) {
                    return new BazaarResult(false, "Not enough buy orders available. Only " + (quantity - remainingQuantity) + " can be sold.");
                }

                // Remove empty orders
                orders.removeAll(ordersToRemove);

                if (plugin.getSettingsConfig().isDebugMode()) {
                    plugin.getLogger().info("Instant sell completed: " + player.getName() + " - " + 
                        quantity + "x " + item + " for " + totalEarnings + " coins");
                }

                return new BazaarResult(true, "Successfully sold " + quantity + "x " + item + " for " + totalEarnings + " coins");
            } catch (Exception e) {
                plugin.getLogger().severe("Error in instant sell: " + e.getMessage());
                return new BazaarResult(false, "Error occurred during sale");
            }
        });
    }

    public List<BuyOrder> getBuyOrders(String item) {
        return new ArrayList<>(buyOrders.getOrDefault(item, new ArrayList<>()));
    }

    public List<SellOrder> getSellOrders(String item) {
        return new ArrayList<>(sellOrders.getOrDefault(item, new ArrayList<>()));
    }

    public double getInstantBuyPrice(String item, int quantity) {
        List<SellOrder> orders = sellOrders.getOrDefault(item, new ArrayList<>());
        if (orders.isEmpty()) return -1;

        double totalCost = 0;
        int remainingQuantity = quantity;

        for (SellOrder order : orders) {
            if (remainingQuantity <= 0) break;
            
            int orderQuantity = Math.min(remainingQuantity, order.getQuantity());
            totalCost += orderQuantity * order.getPrice();
            remainingQuantity -= orderQuantity;
        }

        return remainingQuantity > 0 ? -1 : totalCost;
    }

    public double getInstantSellPrice(String item, int quantity) {
        List<BuyOrder> orders = buyOrders.getOrDefault(item, new ArrayList<>());
        if (orders.isEmpty()) return -1;

        double totalEarnings = 0;
        int remainingQuantity = quantity;

        for (BuyOrder order : orders) {
            if (remainingQuantity <= 0) break;
            
            int orderQuantity = Math.min(remainingQuantity, order.getQuantity());
            totalEarnings += orderQuantity * order.getPrice();
            remainingQuantity -= orderQuantity;
        }

        return remainingQuantity > 0 ? -1 : totalEarnings;
    }

    public Set<String> getAvailableItems() {
        return new HashSet<>(buyOrders.keySet());
    }

    // Inner classes for orders
    public static class BuyOrder {
        private final UUID playerId;
        private final String item;
        private final double price;
        private int quantity;
        private final long timestamp;

        public BuyOrder(UUID playerId, String item, double price, int quantity, long timestamp) {
            this.playerId = playerId;
            this.item = item;
            this.price = price;
            this.quantity = quantity;
            this.timestamp = timestamp;
        }

        public UUID getPlayerId() { return playerId; }
        public String getItem() { return item; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public long getTimestamp() { return timestamp; }
        
        // Additional methods for sorting and calculations
        public double getPricePerUnit() { return price; }
        public double getAmount() { return quantity; }
    }

    public static class SellOrder {
        private final UUID playerId;
        private final String item;
        private final double price;
        private int quantity;
        private final long timestamp;

        public SellOrder(UUID playerId, String item, double price, int quantity, long timestamp) {
            this.playerId = playerId;
            this.item = item;
            this.price = price;
            this.quantity = quantity;
            this.timestamp = timestamp;
        }

        public UUID getPlayerId() { return playerId; }
        public String getItem() { return item; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public long getTimestamp() { return timestamp; }
        
        // Additional methods for sorting and calculations
        public double getPricePerUnit() { return price; }
        public double getAmount() { return quantity; }
    }

    public static class BazaarResult {
        private final boolean success;
        private final String message;

        public BazaarResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
    
    /**
     * Holt die aktuellen Instant-Buy-Preise für ein Item
     * @param itemId Das Item-ID
     * @return Map mit Preis-Informationen
     */
    public Map<String, Double> getInstantBuyPrices(String itemId) {
        Map<String, Double> prices = new HashMap<>();
        List<SellOrder> orders = sellOrders.get(itemId);
        
        if (orders != null && !orders.isEmpty()) {
            // Sortiere nach Preis (niedrigster zuerst)
            orders.sort(Comparator.comparing(SellOrder::getPricePerUnit));
            
            double lowestPrice = orders.get(0).getPricePerUnit();
            double totalAvailable = orders.stream()
                .mapToDouble(SellOrder::getAmount)
                .sum();
            
            prices.put("price", lowestPrice);
            prices.put("available", totalAvailable);
        } else {
            prices.put("price", 0.0);
            prices.put("available", 0.0);
        }
        
        return prices;
    }
    
    /**
     * Holt die aktuellen Instant-Sell-Preise für ein Item
     * @param itemId Das Item-ID
     * @return Map mit Preis-Informationen
     */
    public Map<String, Double> getInstantSellPrices(String itemId) {
        Map<String, Double> prices = new HashMap<>();
        List<BuyOrder> orders = buyOrders.get(itemId);
        
        if (orders != null && !orders.isEmpty()) {
            // Sortiere nach Preis (höchster zuerst)
            orders.sort(Comparator.comparing(BuyOrder::getPricePerUnit).reversed());
            
            double highestPrice = orders.get(0).getPricePerUnit();
            double totalDemand = orders.stream()
                .mapToDouble(BuyOrder::getAmount)
                .sum();
            
            prices.put("price", highestPrice);
            prices.put("demand", totalDemand);
        } else {
            prices.put("price", 0.0);
            prices.put("demand", 0.0);
        }
        
        return prices;
    }
}