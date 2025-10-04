package de.noctivag.plugin.economy;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.core.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bazaar System - Instant Trading System
 * 
 * Verantwortlich für:
 * - Instant Buy/Sell Orders
 * - Market Depth
 * - Price History
 * - Order Book Management
 * - Trading Volume Tracking
 * - Market Manipulation Protection
 */
public class BazaarSystem {
    private final EconomySystem economySystem;
    private final Map<String, BazaarItem> bazaarItems = new ConcurrentHashMap<>();
    private final Map<UUID, List<BazaarOrder>> playerOrders = new ConcurrentHashMap<>();
    private final Map<String, List<BazaarOrder>> buyOrders = new ConcurrentHashMap<>();
    private final Map<String, List<BazaarOrder>> sellOrders = new ConcurrentHashMap<>();
    
    // Bazaar Configuration
    private final double BAZAAR_FEE = 0.01; // 1% fee
    private final int MAX_ORDERS_PER_PLAYER = 14;
    private final int MAX_ORDERS_PER_ITEM = 1000;
    
    public BazaarSystem(EconomySystem economySystem) {
        this.economySystem = economySystem;
        initializeBazaarItems();
        startOrderProcessing();
    }
    
    private void initializeBazaarItems() {
        // Initialize bazaar items with default prices
        String[] items = {
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
        
        for (String item : items) {
            double basePrice = economySystem.getBasePrice(item);
            bazaarItems.put(item, new BazaarItem(item, basePrice, basePrice * 0.95, basePrice * 1.05));
        }
    }
    
    private void startOrderProcessing() {
        new BukkitRunnable() {
            @Override
            public void run() {
                processOrders();
                updateBazaarPrices();
            }
        }.runTaskTimer(economySystem.plugin, 0L, 20L * 5L); // Every 5 seconds
    }
    
    private void processOrders() {
        // Process buy orders
        for (Map.Entry<String, List<BazaarOrder>> entry : buyOrders.entrySet()) {
            String itemId = entry.getKey();
            List<BazaarOrder> orders = entry.getValue();
            
            // Sort by price (highest first)
            orders.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
            
            // Process orders
            for (BazaarOrder order : new ArrayList<>(orders)) {
                if (processBuyOrder(order)) {
                    orders.remove(order);
                }
            }
        }
        
        // Process sell orders
        for (Map.Entry<String, List<BazaarOrder>> entry : sellOrders.entrySet()) {
            String itemId = entry.getKey();
            List<BazaarOrder> orders = entry.getValue();
            
            // Sort by price (lowest first)
            orders.sort((a, b) -> Double.compare(a.getPrice(), b.getPrice()));
            
            // Process orders
            for (BazaarOrder order : new ArrayList<>(orders)) {
                if (processSellOrder(order)) {
                    orders.remove(order);
                }
            }
        }
    }
    
    private boolean processBuyOrder(BazaarOrder order) {
        // Find matching sell orders
        List<BazaarOrder> sellOrdersList = sellOrders.get(order.getItemId());
        if (sellOrdersList == null || sellOrdersList.isEmpty()) return false;
        
        // Find best sell order
        BazaarOrder bestSell = null;
        for (BazaarOrder sellOrder : sellOrdersList) {
            if (sellOrder.getPrice() <= order.getPrice()) {
                bestSell = sellOrder;
                break;
            }
        }
        
        if (bestSell == null) return false;
        
        // Execute trade
        executeTrade(order, bestSell);
        return true;
    }
    
    private boolean processSellOrder(BazaarOrder order) {
        // Find matching buy orders
        List<BazaarOrder> buyOrdersList = buyOrders.get(order.getItemId());
        if (buyOrdersList == null || buyOrdersList.isEmpty()) return false;
        
        // Find best buy order
        BazaarOrder bestBuy = null;
        for (BazaarOrder buyOrder : buyOrdersList) {
            if (buyOrder.getPrice() >= order.getPrice()) {
                bestBuy = buyOrder;
                break;
            }
        }
        
        if (bestBuy == null) return false;
        
        // Execute trade
        executeTrade(bestBuy, order);
        return true;
    }
    
    private void executeTrade(BazaarOrder buyOrder, BazaarOrder sellOrder) {
        // Calculate trade amount
        int tradeAmount = Math.min(buyOrder.getAmount(), sellOrder.getAmount());
        double tradePrice = sellOrder.getPrice(); // Use sell order price
        
        // Calculate fees
        double buyFee = tradeAmount * tradePrice * BAZAAR_FEE;
        double sellFee = tradeAmount * tradePrice * BAZAAR_FEE;
        
        // Update player balances
        PlayerProfile buyProfile = economySystem.corePlatform.getPlayerProfile(buyOrder.getPlayerId());
        PlayerProfile sellProfile = economySystem.corePlatform.getPlayerProfile(sellOrder.getPlayerId());
        
        if (buyProfile != null) {
            buyProfile.removeCoins(tradeAmount * tradePrice + buyFee);
        }
        
        if (sellProfile != null) {
            sellProfile.addCoins(tradeAmount * tradePrice - sellFee);
        }
        
        // Update order amounts
        buyOrder.setAmount(buyOrder.getAmount() - tradeAmount);
        sellOrder.setAmount(sellOrder.getAmount() - tradeAmount);
        
        // Remove completed orders
        if (buyOrder.getAmount() <= 0) {
            buyOrders.get(buyOrder.getItemId()).remove(buyOrder);
            playerOrders.get(buyOrder.getPlayerId()).remove(buyOrder);
        }
        
        if (sellOrder.getAmount() <= 0) {
            sellOrders.get(sellOrder.getItemId()).remove(sellOrder);
            playerOrders.get(sellOrder.getPlayerId()).remove(sellOrder);
        }
        
        // Update bazaar item prices
        BazaarItem bazaarItem = bazaarItems.get(buyOrder.getItemId());
        if (bazaarItem != null) {
            bazaarItem.addTrade(tradeAmount, tradePrice);
        }
    }
    
    private void updateBazaarPrices() {
        for (BazaarItem item : bazaarItems.values()) {
            item.updatePrices();
        }
    }
    
    public void createBuyOrder(Player player, String itemId, int amount, double price) {
        UUID playerId = player.getUniqueId();
        
        // Validate inputs
        if (amount <= 0 || price <= 0) {
            player.sendMessage("§cInvalid order parameters!");
            return;
        }
        
        BazaarItem bazaarItem = bazaarItems.get(itemId);
        if (bazaarItem == null) {
            player.sendMessage("§cThis item is not available in the bazaar!");
            return;
        }
        
        // Check if player has reached order limit
        List<BazaarOrder> playerOrderList = playerOrders.getOrDefault(playerId, new ArrayList<>());
        if (playerOrderList.size() >= MAX_ORDERS_PER_PLAYER) {
            player.sendMessage("§cYou have reached the maximum number of orders (" + MAX_ORDERS_PER_PLAYER + ")!");
            return;
        }
        
        // Check if item has reached order limit
        List<BazaarOrder> itemOrders = buyOrders.getOrDefault(itemId, new ArrayList<>());
        if (itemOrders.size() >= MAX_ORDERS_PER_ITEM) {
            player.sendMessage("§cThis item has reached the maximum number of orders!");
            return;
        }
        
        // Check if player has enough money
        double totalCost = amount * price;
        PlayerProfile profile = economySystem.corePlatform.getPlayerProfile(playerId);
        if (profile == null || !profile.hasBalance(totalCost)) {
            player.sendMessage("§cYou don't have enough coins!");
            return;
        }
        
        // Try to fulfill immediately with existing sell orders
        int remainingAmount = amount;
        double totalSpent = 0.0;
        
        List<BazaarOrder> sellOrdersList = sellOrders.getOrDefault(itemId, new ArrayList<>());
        List<BazaarOrder> ordersToRemove = new ArrayList<>();
        
        for (BazaarOrder sellOrder : new ArrayList<>(sellOrdersList)) {
            if (remainingAmount <= 0) break;
            if (sellOrder.getPrice() <= price) {
                int tradeAmount = Math.min(remainingAmount, sellOrder.getAmount());
                double tradeCost = tradeAmount * sellOrder.getPrice();
                
                // Execute instant trade
                executeInstantTrade(player, sellOrder.getPlayerId(), itemId, tradeAmount, sellOrder.getPrice());
                
                remainingAmount -= tradeAmount;
                totalSpent += tradeCost;
                
                // Update sell order
                sellOrder.setAmount(sellOrder.getAmount() - tradeAmount);
                if (sellOrder.getAmount() <= 0) {
                    ordersToRemove.add(sellOrder);
                }
            }
        }
        
        // Remove completed sell orders
        for (BazaarOrder order : ordersToRemove) {
            sellOrdersList.remove(order);
            playerOrders.get(order.getPlayerId()).remove(order);
        }
        
        // Create buy order for remaining amount if any
        if (remainingAmount > 0) {
            BazaarOrder order = new BazaarOrder(UUID.randomUUID(), playerId, itemId, remainingAmount, price, BazaarOrder.OrderType.BUY);
            
            // Add to collections
            playerOrderList.add(order);
            playerOrders.put(playerId, playerOrderList);
            
            itemOrders.add(order);
            buyOrders.put(itemId, itemOrders);
            
            // Reserve coins for remaining amount
            profile.removeCoins(remainingAmount * price);
        } else {
            // All items were bought instantly
            profile.removeCoins(totalSpent);
        }
        
        player.sendMessage("§a§lORDER PROCESSED!");
        player.sendMessage("§7Item: §e" + itemId);
        player.sendMessage("§7Total Amount: §e" + amount);
        if (remainingAmount > 0) {
            player.sendMessage("§7Instant Buy: §e" + (amount - remainingAmount));
            player.sendMessage("§7Order Created: §e" + remainingAmount);
        } else {
            player.sendMessage("§7All items bought instantly!");
        }
        player.sendMessage("§7Total Cost: §6" + (totalSpent + (remainingAmount * price)) + " coins");
    }
    
    public void createSellOrder(Player player, String itemId, int amount, double price) {
        UUID playerId = player.getUniqueId();
        
        // Validate inputs
        if (amount <= 0 || price <= 0) {
            player.sendMessage("§cInvalid order parameters!");
            return;
        }
        
        BazaarItem bazaarItem = bazaarItems.get(itemId);
        if (bazaarItem == null) {
            player.sendMessage("§cThis item is not available in the bazaar!");
            return;
        }
        
        // Check if player has reached order limit
        List<BazaarOrder> playerOrderList = playerOrders.getOrDefault(playerId, new ArrayList<>());
        if (playerOrderList.size() >= MAX_ORDERS_PER_PLAYER) {
            player.sendMessage("§cYou have reached the maximum number of orders (" + MAX_ORDERS_PER_PLAYER + ")!");
            return;
        }
        
        // Check if item has reached order limit
        List<BazaarOrder> itemOrders = sellOrders.getOrDefault(itemId, new ArrayList<>());
        if (itemOrders.size() >= MAX_ORDERS_PER_ITEM) {
            player.sendMessage("§cThis item has reached the maximum number of orders!");
            return;
        }
        
        // Check if player has enough items
        org.bukkit.Material material = org.bukkit.Material.valueOf(itemId);
        if (!player.getInventory().contains(material, amount)) {
            player.sendMessage("§cYou don't have enough items!");
            return;
        }
        
        // Try to fulfill immediately with existing buy orders
        int remainingAmount = amount;
        double totalEarned = 0.0;
        
        List<BazaarOrder> buyOrdersList = buyOrders.getOrDefault(itemId, new ArrayList<>());
        List<BazaarOrder> ordersToRemove = new ArrayList<>();
        
        for (BazaarOrder buyOrder : new ArrayList<>(buyOrdersList)) {
            if (remainingAmount <= 0) break;
            if (buyOrder.getPrice() >= price) {
                int tradeAmount = Math.min(remainingAmount, buyOrder.getAmount());
                double tradeEarnings = tradeAmount * buyOrder.getPrice();
                
                // Execute instant trade
                executeInstantTrade(buyOrder.getPlayerId(), player, itemId, tradeAmount, buyOrder.getPrice());
                
                remainingAmount -= tradeAmount;
                totalEarned += tradeEarnings;
                
                // Update buy order
                buyOrder.setAmount(buyOrder.getAmount() - tradeAmount);
                if (buyOrder.getAmount() <= 0) {
                    ordersToRemove.add(buyOrder);
                }
            }
        }
        
        // Remove completed buy orders
        for (BazaarOrder order : ordersToRemove) {
            buyOrdersList.remove(order);
            playerOrders.get(order.getPlayerId()).remove(order);
        }
        
        // Create sell order for remaining amount if any
        if (remainingAmount > 0) {
            BazaarOrder order = new BazaarOrder(UUID.randomUUID(), playerId, itemId, remainingAmount, price, BazaarOrder.OrderType.SELL);
            
            // Add to collections
            playerOrderList.add(order);
            playerOrders.put(playerId, playerOrderList);
            
            itemOrders.add(order);
            sellOrders.put(itemId, itemOrders);
            
            // Remove remaining items from inventory
            ItemStack toRemove = new ItemStack(material, remainingAmount);
            player.getInventory().removeItem(toRemove);
        } else {
            // All items were sold instantly - remove all items from inventory
            ItemStack toRemove = new ItemStack(material, amount);
            player.getInventory().removeItem(toRemove);
        }
        
        player.sendMessage("§a§lORDER PROCESSED!");
        player.sendMessage("§7Item: §e" + itemId);
        player.sendMessage("§7Total Amount: §e" + amount);
        if (remainingAmount > 0) {
            player.sendMessage("§7Instant Sell: §e" + (amount - remainingAmount));
            player.sendMessage("§7Order Created: §e" + remainingAmount);
        } else {
            player.sendMessage("§7All items sold instantly!");
        }
        player.sendMessage("§7Total Earnings: §6" + totalEarned + " coins");
    }
    
    private void executeInstantTrade(Player buyer, UUID sellerId, String itemId, int amount, double price) {
        executeInstantTrade(buyer.getUniqueId(), sellerId, itemId, amount, price);
    }
    
    private void executeInstantTrade(UUID buyerId, Player seller, String itemId, int amount, double price) {
        executeInstantTrade(buyerId, seller.getUniqueId(), itemId, amount, price);
    }
    
    private void executeInstantTrade(UUID buyerId, UUID sellerId, String itemId, int amount, double price) {
        // Calculate fees
        double totalValue = amount * price;
        double buyerFee = totalValue * BAZAAR_FEE;
        double sellerFee = totalValue * BAZAAR_FEE;
        
        // Update player balances
        PlayerProfile buyerProfile = economySystem.corePlatform.getPlayerProfile(buyerId);
        PlayerProfile sellerProfile = economySystem.corePlatform.getPlayerProfile(sellerId);
        
        if (buyerProfile != null) {
            buyerProfile.removeCoins(totalValue + buyerFee);
        }
        
        if (sellerProfile != null) {
            sellerProfile.addCoins(totalValue - sellerFee);
        }
        
        // Give items to buyer
        Player buyerPlayer = org.bukkit.Bukkit.getPlayer(buyerId);
        if (buyerPlayer != null) {
            org.bukkit.Material material = org.bukkit.Material.valueOf(itemId);
            buyerPlayer.getInventory().addItem(new org.bukkit.inventory.ItemStack(material, amount));
        }
        
        // Update bazaar item prices
        BazaarItem bazaarItem = bazaarItems.get(itemId);
        if (bazaarItem != null) {
            bazaarItem.addTrade(amount, price);
        }
    }
    
    public void cancelOrder(Player player, UUID orderId) {
        UUID playerId = player.getUniqueId();
        List<BazaarOrder> playerOrderList = playerOrders.get(playerId);
        if (playerOrderList == null) return;
        
        BazaarOrder order = null;
        for (BazaarOrder o : playerOrderList) {
            if (o.getId().equals(orderId)) {
                order = o;
                break;
            }
        }
        
        if (order == null) return;
        
        // Remove from collections
        playerOrderList.remove(order);
        
        if (order.getType() == BazaarOrder.OrderType.BUY) {
            buyOrders.get(order.getItemId()).remove(order);
            
            // Refund coins
            PlayerProfile profile = economySystem.corePlatform.getPlayerProfile(playerId);
            if (profile != null) {
                profile.addCoins(order.getAmount() * order.getPrice());
            }
        } else {
            sellOrders.get(order.getItemId()).remove(order);
            
            // Return items
            org.bukkit.Material material = org.bukkit.Material.valueOf(order.getItemId());
            player.getInventory().addItem(new ItemStack(material, order.getAmount()));
        }
        
        player.sendMessage("§c§lORDER CANCELLED!");
        player.sendMessage("§7Order ID: §e" + orderId);
    }
    
    public BazaarItem getBazaarItem(String itemId) {
        return bazaarItems.get(itemId);
    }
    
    public Map<String, BazaarItem> getAllBazaarItems() {
        return new HashMap<>(bazaarItems);
    }
    
    public List<BazaarOrder> getPlayerOrders(UUID playerId) {
        return playerOrders.getOrDefault(playerId, new ArrayList<>());
    }
    
    public List<BazaarOrder> getBuyOrders(String itemId) {
        return buyOrders.getOrDefault(itemId, new ArrayList<>());
    }
    
    public List<BazaarOrder> getSellOrders(String itemId) {
        return sellOrders.getOrDefault(itemId, new ArrayList<>());
    }
    
    public void instantBuy(Player player, String itemId, int amount) {
        BazaarItem bazaarItem = bazaarItems.get(itemId);
        if (bazaarItem == null) {
            player.sendMessage("§cThis item is not available in the bazaar!");
            return;
        }
        
        double price = bazaarItem.getInstantBuyPrice();
        double totalCost = amount * price;
        double fee = totalCost * BAZAAR_FEE;
        double totalWithFee = totalCost + fee;
        
        PlayerProfile profile = economySystem.corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null || !profile.hasBalance(totalWithFee)) {
            player.sendMessage("§cYou don't have enough coins!");
            player.sendMessage("§7Required: §6" + totalWithFee + " coins");
            return;
        }
        
        // Deduct coins
        profile.removeCoins(totalWithFee);
        
        // Give items
        org.bukkit.Material material = org.bukkit.Material.valueOf(itemId);
        player.getInventory().addItem(new org.bukkit.inventory.ItemStack(material, amount));
        
        // Update bazaar item
        bazaarItem.addTrade(amount, price);
        
        player.sendMessage("§a§lINSTANT BUY COMPLETED!");
        player.sendMessage("§7Item: §e" + itemId);
        player.sendMessage("§7Amount: §e" + amount);
        player.sendMessage("§7Price per unit: §6" + price + " coins");
        player.sendMessage("§7Total cost: §6" + totalCost + " coins");
        player.sendMessage("§7Fee: §6" + fee + " coins");
        player.sendMessage("§7Total paid: §6" + totalWithFee + " coins");
    }
    
    public void instantSell(Player player, String itemId, int amount) {
        BazaarItem bazaarItem = bazaarItems.get(itemId);
        if (bazaarItem == null) {
            player.sendMessage("§cThis item is not available in the bazaar!");
            return;
        }
        
        org.bukkit.Material material = org.bukkit.Material.valueOf(itemId);
        if (!player.getInventory().contains(material, amount)) {
            player.sendMessage("§cYou don't have enough items!");
            return;
        }
        
        double price = bazaarItem.getInstantSellPrice();
        double totalEarnings = amount * price;
        double fee = totalEarnings * BAZAAR_FEE;
        double netEarnings = totalEarnings - fee;
        
        // Remove items
        ItemStack toRemove = new ItemStack(material, amount);
        player.getInventory().removeItem(toRemove);
        
        // Add coins
        PlayerProfile profile = economySystem.corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile != null) {
            profile.addCoins(netEarnings);
        }
        
        // Update bazaar item
        bazaarItem.addTrade(amount, price);
        
        player.sendMessage("§a§lINSTANT SELL COMPLETED!");
        player.sendMessage("§7Item: §e" + itemId);
        player.sendMessage("§7Amount: §e" + amount);
        player.sendMessage("§7Price per unit: §6" + price + " coins");
        player.sendMessage("§7Total earnings: §6" + totalEarnings + " coins");
        player.sendMessage("§7Fee: §6" + fee + " coins");
        player.sendMessage("§7Net earnings: §6" + netEarnings + " coins");
    }
    
    public void openBazaarGUI(Player player) {
        openBazaarGUI(player, 0);
    }
    
    public void openBazaarGUI(Player player, int page) {
        org.bukkit.inventory.Inventory gui = org.bukkit.Bukkit.createInventory(null, 54, "§6§lBazaar - Page " + (page + 1));
        
        List<String> itemIds = new ArrayList<>(bazaarItems.keySet());
        int startIndex = page * 45;
        int endIndex = Math.min(startIndex + 45, itemIds.size());
        
        // Add bazaar items
        for (int i = startIndex; i < endIndex; i++) {
            String itemId = itemIds.get(i);
            BazaarItem item = bazaarItems.get(itemId);
            
            org.bukkit.Material material = org.bukkit.Material.valueOf(itemId);
            ItemStack displayItem = new ItemStack(material);
            ItemMeta meta = displayItem.getItemMeta();
            
            if (meta != null) {
                List<String> lore = new ArrayList<>();
                lore.add("§7Instant Buy Price: §6" + item.getInstantBuyPrice() + " coins");
                lore.add("§7Instant Sell Price: §6" + item.getInstantSellPrice() + " coins");
                lore.add("§7Average Price: §6" + item.getAveragePrice() + " coins");
                lore.add("§7Volume: §e" + item.getTotalVolume());
                lore.add("");
                lore.add("§eLeft Click to Buy");
                lore.add("§eRight Click to Sell");
                lore.add("§eShift + Click for Orders");
                
                meta.setLore(lore);
                displayItem.setItemMeta(meta);
            }
            
            gui.setItem(i - startIndex, displayItem);
        }
        
        // Add navigation items
        if (page > 0) {
            ItemStack prevPage = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = prevPage.getItemMeta();
            if (prevMeta != null) {
                prevMeta.setDisplayName("§7Previous Page");
                prevPage.setItemMeta(prevMeta);
            }
            gui.setItem(45, prevPage);
        }
        
        ItemStack closeButton = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeButton.getItemMeta();
        if (closeMeta != null) {
            closeMeta.setDisplayName("§cClose");
            closeButton.setItemMeta(closeMeta);
        }
        gui.setItem(49, closeButton);
        
        if (endIndex < itemIds.size()) {
            ItemStack nextPage = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = nextPage.getItemMeta();
            if (nextMeta != null) {
                nextMeta.setDisplayName("§7Next Page");
                nextPage.setItemMeta(nextMeta);
            }
            gui.setItem(53, nextPage);
        }
        
        // Add order management
        ItemStack myOrders = new ItemStack(Material.BOOK);
        ItemMeta ordersMeta = myOrders.getItemMeta();
        if (ordersMeta != null) {
            ordersMeta.setDisplayName("§aMy Orders");
            ordersMeta.setLore(Arrays.asList("§7View and manage your orders"));
            myOrders.setItemMeta(ordersMeta);
        }
        gui.setItem(47, myOrders);
        
        ItemStack marketOverview = new ItemStack(Material.COMPASS);
        ItemMeta marketMeta = marketOverview.getItemMeta();
        if (marketMeta != null) {
            marketMeta.setDisplayName("§bMarket Overview");
            marketMeta.setLore(Arrays.asList("§7View market trends and statistics"));
            marketOverview.setItemMeta(marketMeta);
        }
        gui.setItem(51, marketOverview);
        
        player.openInventory(gui);
    }
    
    // Bazaar Item Class
    public static class BazaarItem {
        private final String itemId;
        private double instantBuyPrice;
        private double instantSellPrice;
        private double averagePrice;
        private final List<Double> priceHistory = new ArrayList<>();
        private final List<Integer> volumeHistory = new ArrayList<>();
        private int totalVolume = 0;
        private double totalValue = 0.0;
        
        public BazaarItem(String itemId, double instantBuyPrice, double instantSellPrice, double averagePrice) {
            this.itemId = itemId;
            this.instantBuyPrice = instantBuyPrice;
            this.instantSellPrice = instantSellPrice;
            this.averagePrice = averagePrice;
        }
        
        public void addTrade(int volume, double price) {
            totalVolume += volume;
            totalValue += volume * price;
            
            // Update price history
            priceHistory.add(price);
            volumeHistory.add(volume);
            
            // Keep only last 100 entries
            if (priceHistory.size() > 100) {
                priceHistory.remove(0);
                volumeHistory.remove(0);
            }
        }
        
        public void updatePrices() {
            if (priceHistory.isEmpty()) return;
            
            // Calculate average price
            averagePrice = totalValue / totalVolume;
            
            // Update instant prices based on market depth
            instantBuyPrice = averagePrice * 1.05; // 5% markup
            instantSellPrice = averagePrice * 0.95; // 5% markdown
        }
        
        // Getters
        public String getItemId() { return itemId; }
        public double getInstantBuyPrice() { return instantBuyPrice; }
        public double getInstantSellPrice() { return instantSellPrice; }
        public double getAveragePrice() { return averagePrice; }
        public List<Double> getPriceHistory() { return new ArrayList<>(priceHistory); }
        public List<Integer> getVolumeHistory() { return new ArrayList<>(volumeHistory); }
        public int getTotalVolume() { return totalVolume; }
        public double getTotalValue() { return totalValue; }
    }
    
    // Bazaar Order Class
    public static class BazaarOrder {
        private final UUID id;
        private final UUID playerId;
        private final String itemId;
        private int amount;
        private final double price;
        private final OrderType type;
        private final long createdAt;
        
        public BazaarOrder(UUID id, UUID playerId, String itemId, int amount, double price, OrderType type) {
            this.id = id;
            this.playerId = playerId;
            this.itemId = itemId;
            this.amount = amount;
            this.price = price;
            this.type = type;
            this.createdAt = System.currentTimeMillis();
        }
        
        // Getters and Setters
        public UUID getId() { return id; }
        public UUID getPlayerId() { return playerId; }
        public String getItemId() { return itemId; }
        public int getAmount() { return amount; }
        public double getPrice() { return price; }
        public OrderType getType() { return type; }
        public long getCreatedAt() { return createdAt; }
        
        public void setAmount(int amount) { this.amount = amount; }
        
        public enum OrderType {
            BUY, SELL
        }
    }
}
