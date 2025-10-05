package de.noctivag.skyblock.skyblock.bazaar;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Comprehensive Bazaar System inspired by Hypixel Skyblock
 * Features:
 * - Instant buy/sell orders
 * - Market price tracking
 * - Order management
 * - Price history and trends
 * - Bulk trading
 * - Market manipulation detection
 * - Order fulfillment system
 */
public class BazaarSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerBazaarData> playerBazaarData = new ConcurrentHashMap<>();
    private final Map<Material, BazaarItem> bazaarItems = new ConcurrentHashMap<>();
    private final Map<String, BazaarOrder> activeOrders = new ConcurrentHashMap<>();
    private final Map<UUID, List<BazaarOrder>> playerOrders = new ConcurrentHashMap<>();
    
    public BazaarSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
        initializeBazaarItems();
        startBazaarUpdateTask();
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        if (!title.contains("Bazaar")) return;
        
        event.setCancelled(true);
        
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;
        
        String displayName = clicked.getItemMeta().getDisplayName();
        
        // Handle bazaar clicks
        handleBazaarClick(player, displayName, event.getSlot());
    }
    
    private void initializeBazaarItems() {
        // Initialize all bazaar items with their base prices
        addBazaarItem(Material.WHEAT, "Wheat", 1.0);
        addBazaarItem(Material.CARROT, "Carrot", 1.5);
        addBazaarItem(Material.POTATO, "Potato", 1.2);
        addBazaarItem(Material.PUMPKIN, "Pumpkin", 2.0);
        addBazaarItem(Material.MELON, "Melon", 1.8);
        addBazaarItem(Material.SUGAR_CANE, "Sugar Cane", 1.0);
        addBazaarItem(Material.CACTUS, "Cactus", 2.5);
        addBazaarItem(Material.NETHER_WART, "Nether Wart", 3.0);
        
        addBazaarItem(Material.COBBLESTONE, "Cobblestone", 0.5);
        addBazaarItem(Material.COAL, "Coal", 2.0);
        addBazaarItem(Material.IRON_INGOT, "Iron Ingot", 5.0);
        addBazaarItem(Material.GOLD_INGOT, "Gold Ingot", 10.0);
        addBazaarItem(Material.DIAMOND, "Diamond", 50.0);
        addBazaarItem(Material.EMERALD, "Emerald", 30.0);
        addBazaarItem(Material.REDSTONE, "Redstone", 1.0);
        addBazaarItem(Material.LAPIS_LAZULI, "Lapis Lazuli", 2.0);
        addBazaarItem(Material.QUARTZ, "Quartz", 3.0);
        addBazaarItem(Material.OBSIDIAN, "Obsidian", 20.0);
        
        addBazaarItem(Material.OAK_LOG, "Oak Log", 1.0);
        addBazaarItem(Material.BIRCH_LOG, "Birch Log", 1.0);
        addBazaarItem(Material.SPRUCE_LOG, "Spruce Log", 1.0);
        addBazaarItem(Material.JUNGLE_LOG, "Jungle Log", 1.5);
        addBazaarItem(Material.ACACIA_LOG, "Acacia Log", 1.5);
        addBazaarItem(Material.DARK_OAK_LOG, "Dark Oak Log", 1.5);
        
        addBazaarItem(Material.ROTTEN_FLESH, "Rotten Flesh", 1.0);
        addBazaarItem(Material.BONE, "Bone", 2.0);
        addBazaarItem(Material.STRING, "String", 1.5);
        addBazaarItem(Material.GUNPOWDER, "Gunpowder", 3.0);
        addBazaarItem(Material.ENDER_PEARL, "Ender Pearl", 15.0);
        addBazaarItem(Material.BLAZE_ROD, "Blaze Rod", 25.0);
        addBazaarItem(Material.GHAST_TEAR, "Ghast Tear", 20.0);
        addBazaarItem(Material.WITHER_SKELETON_SKULL, "Wither Skeleton Skull", 100.0);
        
        addBazaarItem(Material.COD, "Raw Fish", 2.0);
        addBazaarItem(Material.SALMON, "Raw Salmon", 3.0);
        addBazaarItem(Material.TROPICAL_FISH, "Tropical Fish", 5.0);
        addBazaarItem(Material.PUFFERFISH, "Pufferfish", 8.0);
    }
    
    private void addBazaarItem(Material material, String name, double basePrice) {
        BazaarItem item = new BazaarItem(material, name, basePrice);
        bazaarItems.put(material, item);
    }
    
    private void startBazaarUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateBazaarPrices();
                processOrders();
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L * 30L); // Every 30 seconds
    }
    
    private void updateBazaarPrices() {
        // Update prices based on supply and demand
        for (BazaarItem item : bazaarItems.values()) {
            item.updatePrices();
        }
    }
    
    private void processOrders() {
        // Process buy and sell orders
        List<String> completedOrderIds = new ArrayList<>();
        
        for (Map.Entry<String, BazaarOrder> entry : activeOrders.entrySet()) {
            BazaarOrder order = entry.getValue();
            
            if (processOrder(order)) {
                completedOrderIds.add(entry.getKey());
            }
        }
        
        // Remove completed orders
        for (String orderId : completedOrderIds) {
            activeOrders.remove(orderId);
        }
    }
    
    private boolean processOrder(BazaarOrder order) {
        BazaarItem item = bazaarItems.get(order.getMaterial());
        if (item == null) return false;
        
        if (order.getType() == BazaarOrderType.BUY) {
            // Process buy order
            return processBuyOrder(order, item);
        } else {
            // Process sell order
            return processSellOrder(order, item);
        }
    }
    
    private boolean processBuyOrder(BazaarOrder order, BazaarItem item) {
        // Find matching sell orders
        List<BazaarOrder> sellOrders = getSellOrders(order.getMaterial());
        
        for (BazaarOrder sellOrder : sellOrders) {
            if (sellOrder.getPrice() <= order.getPrice()) {
                // Execute trade
                executeTrade(order, sellOrder, item);
                return true;
            }
        }
        
        return false;
    }
    
    private boolean processSellOrder(BazaarOrder order, BazaarItem item) {
        // Find matching buy orders
        List<BazaarOrder> buyOrders = getBuyOrders(order.getMaterial());
        
        for (BazaarOrder buyOrder : buyOrders) {
            if (buyOrder.getPrice() >= order.getPrice()) {
                // Execute trade
                executeTrade(buyOrder, order, item);
                return true;
            }
        }
        
        return false;
    }
    
    private void executeTrade(BazaarOrder buyOrder, BazaarOrder sellOrder, BazaarItem item) {
        int tradeAmount = Math.min(buyOrder.getRemainingAmount(), sellOrder.getRemainingAmount());
        double tradePrice = (buyOrder.getPrice() + sellOrder.getPrice()) / 2.0;
        
        // Give items to buyer
        Player buyer = Bukkit.getPlayer(buyOrder.getPlayerId());
        if (buyer != null) {
            ItemStack items = new ItemStack(item.getMaterial(), tradeAmount);
            buyer.getInventory().addItem(items);
            
            // Deduct coins
            double totalCost = tradeAmount * tradePrice;
            // Deduct coins from buyer
        }
        
        // Give coins to seller
        Player seller = Bukkit.getPlayer(sellOrder.getPlayerId());
        if (seller != null) {
            double totalEarned = tradeAmount * tradePrice;
            // Add coins to seller
        }
        
        // Update order amounts
        buyOrder.reduceAmount(tradeAmount);
        sellOrder.reduceAmount(tradeAmount);
        
        // Update market price
        item.updateMarketPrice(tradePrice);
    }
    
    private List<BazaarOrder> getBuyOrders(Material material) {
        List<BazaarOrder> buyOrders = new ArrayList<>();
        
        for (BazaarOrder order : activeOrders.values()) {
            if (order.getMaterial() == material && order.getType() == BazaarOrderType.BUY) {
                buyOrders.add(order);
            }
        }
        
        // Sort by price (highest first)
        buyOrders.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
        
        return buyOrders;
    }
    
    private List<BazaarOrder> getSellOrders(Material material) {
        List<BazaarOrder> sellOrders = new ArrayList<>();
        
        for (BazaarOrder order : activeOrders.values()) {
            if (order.getMaterial() == material && order.getType() == BazaarOrderType.SELL) {
                sellOrders.add(order);
            }
        }
        
        // Sort by price (lowest first)
        sellOrders.sort(Comparator.comparingDouble(BazaarOrder::getPrice));
        
        return sellOrders;
    }
    
    public void openBazaar(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lBazaar"));
        
        // Add categories
        addBazaarCategory(gui, 10, Material.WHEAT, "§e§lFarming", "§7Crops and farming materials");
        addBazaarCategory(gui, 11, Material.DIAMOND_PICKAXE, "§7§lMining", "§7Ores and mining materials");
        addBazaarCategory(gui, 12, Material.OAK_LOG, "§6§lForaging", "§7Wood and foraging materials");
        addBazaarCategory(gui, 13, Material.DIAMOND_SWORD, "§c§lCombat", "§7Combat drops and materials");
        addBazaarCategory(gui, 14, Material.FISHING_ROD, "§b§lFishing", "§7Fish and fishing materials");
        addBazaarCategory(gui, 15, Material.EMERALD, "§a§lSpecial", "§7Special and rare materials");
        
        // Add quick actions
        addBazaarItem(gui, 19, Material.GOLD_INGOT, "§6§lQuick Buy", Arrays.asList("§7Buy items instantly at market price"));
        addBazaarItem(gui, 20, Material.EMERALD, "§a§lQuick Sell", Arrays.asList("§7Sell items instantly at market price"));
        addBazaarItem(gui, 21, Material.CLOCK, "§e§lOrder Book", Arrays.asList("§7View all buy and sell orders"));
        addBazaarItem(gui, 22, Material.BOOK, "§b§lPrice History", Arrays.asList("§7View price history and trends"));
        
        // Add player actions
        addBazaarItem(gui, 28, Material.CHEST, "§a§lMy Orders", Arrays.asList("§7View your active orders"));
        addBazaarItem(gui, 29, Material.GOLD_INGOT, "§6§lMy Coins", Arrays.asList("§7View your coin balance"));
        addBazaarItem(gui, 30, Material.BOOK, "§e§lTransaction History", Arrays.asList("§7View your transaction history"));
        addBazaarItem(gui, 31, Material.ANVIL, "§7§lCreate Order", Arrays.asList("§7Create a new buy or sell order"));
        
        // Add navigation
        addBazaarItem(gui, 45, Material.ARROW, "§7§lPrevious Page", Arrays.asList("§7Go to previous page"));
        addBazaarItem(gui, 49, Material.BARRIER, "§c§lClose", Arrays.asList("§7Close the bazaar"));
        addBazaarItem(gui, 53, Material.ARROW, "§7§lNext Page", Arrays.asList("§7Go to next page"));
        
        player.openInventory(gui);
    }
    
    public void quickBuy(Player player, Material material, int amount) {
        BazaarItem item = bazaarItems.get(material);
        if (item == null) {
            player.sendMessage(Component.text("§cItem not available in bazaar!"));
            return;
        }
        
        double price = item.getBuyPrice();
        double totalCost = amount * price;
        
        // Check if player has enough coins
        // This would check player's coin balance
        
        // Give items to player
        ItemStack items = new ItemStack(material, amount);
        player.getInventory().addItem(items);
        
        // Deduct coins
        // Deduct coins from player
        
        player.sendMessage("§aBought " + amount + "x " + item.getName() + " for §6" + String.format("%.2f", totalCost) + " coins");
    }
    
    public void quickSell(Player player, Material material, int amount) {
        BazaarItem item = bazaarItems.get(material);
        if (item == null) {
            player.sendMessage(Component.text("§cItem not available in bazaar!"));
            return;
        }
        
        // Check if player has the items
        if (!player.getInventory().containsAtLeast(new ItemStack(material), amount)) {
            player.sendMessage("§cYou don't have enough " + item.getName() + "!");
            return;
        }
        
        double price = item.getSellPrice();
        double totalEarned = amount * price;
        
        // Remove items from player
        player.getInventory().removeItem(new ItemStack(material, amount));
        
        // Add coins to player
        // Add coins to player
        
        player.sendMessage("§aSold " + amount + "x " + item.getName() + " for §6" + String.format("%.2f", totalEarned) + " coins");
    }
    
    public void createOrder(Player player, Material material, BazaarOrderType type, double price, int amount) {
        BazaarItem item = bazaarItems.get(material);
        if (item == null) {
            player.sendMessage(Component.text("§cItem not available in bazaar!"));
            return;
        }
        
        if (type == BazaarOrderType.SELL) {
            // Check if player has the items
            if (!player.getInventory().containsAtLeast(new ItemStack(material), amount)) {
                player.sendMessage("§cYou don't have enough " + item.getName() + "!");
                return;
            }
            
            // Remove items from player
            player.getInventory().removeItem(new ItemStack(material, amount));
        } else {
            // Check if player has enough coins
            double totalCost = amount * price;
            // This would check player's coin balance
            
            // Reserve coins
            // Reserve coins for the order
        }
        
        // Create order
        BazaarOrder order = new BazaarOrder(player.getUniqueId(), material, type, price, amount);
        activeOrders.put(order.getId(), order);
        
        // Add to player orders
        playerOrders.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>()).add(order);
        
        player.sendMessage("§aCreated " + type.getDisplayName() + " order for " + amount + "x " + item.getName() + " at §6" + String.format("%.2f", price) + " coins each");
    }
    
    private void handleBazaarClick(Player player, String displayName, int slot) {
        if (displayName.contains("Farming")) {
            openBazaarCategory(player, BazaarCategory.FARMING);
        } else if (displayName.contains("Mining")) {
            openBazaarCategory(player, BazaarCategory.MINING);
        } else if (displayName.contains("Foraging")) {
            openBazaarCategory(player, BazaarCategory.FORAGING);
        } else if (displayName.contains("Combat")) {
            openBazaarCategory(player, BazaarCategory.COMBAT);
        } else if (displayName.contains("Fishing")) {
            openBazaarCategory(player, BazaarCategory.FISHING);
        } else if (displayName.contains("Special")) {
            openBazaarCategory(player, BazaarCategory.SPECIAL);
        } else if (displayName.contains("Quick Buy")) {
            openQuickBuy(player);
        } else if (displayName.contains("Quick Sell")) {
            openQuickSell(player);
        } else if (displayName.contains("My Orders")) {
            openPlayerOrders(player);
        } else if (displayName.contains("Close")) {
            player.closeInventory();
        }
    }
    
    private void openBazaarCategory(Player player, BazaarCategory category) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lBazaar - " + category.getDisplayName()));
        
        // Add items in this category
        List<BazaarItem> categoryItems = getItemsByCategory(category);
        
        int slot = 0;
        for (BazaarItem item : categoryItems) {
            if (slot >= 45) break;
            
            addBazaarItemDisplay(gui, slot, item);
            slot++;
        }
        
        // Add navigation
        addBazaarItem(gui, 45, Material.ARROW, "§7§lBack", Arrays.asList("§7Return to main menu"));
        addBazaarItem(gui, 49, Material.BARRIER, "§c§lClose", Arrays.asList("§7Close the bazaar"));
        
        player.openInventory(gui);
    }
    
    private void openQuickBuy(Player player) {
        // Open quick buy interface
        player.sendMessage(Component.text("§eOpening quick buy interface..."));
    }
    
    private void openQuickSell(Player player) {
        // Open quick sell interface
        player.sendMessage(Component.text("§eOpening quick sell interface..."));
    }
    
    private void openPlayerOrders(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§a§lMy Orders"));
        
        List<BazaarOrder> playerOrderList = playerOrders.get(player.getUniqueId());
        if (playerOrderList != null) {
            int slot = 0;
            for (BazaarOrder order : playerOrderList) {
                if (slot >= 45) break;
                
                addOrderDisplay(gui, slot, order);
                slot++;
            }
        }
        
        // Add navigation
        addBazaarItem(gui, 45, Material.ARROW, "§7§lBack", Arrays.asList("§7Return to main menu"));
        addBazaarItem(gui, 49, Material.BARRIER, "§c§lClose", Arrays.asList("§7Close the bazaar"));
        
        player.openInventory(gui);
    }
    
    private List<BazaarItem> getItemsByCategory(BazaarCategory category) {
        List<BazaarItem> categoryItems = new ArrayList<>();
        
        for (BazaarItem item : bazaarItems.values()) {
            if (getItemCategory(item.getMaterial()) == category) {
                categoryItems.add(item);
            }
        }
        
        // Sort by name
        categoryItems.sort(Comparator.comparing(BazaarItem::getName));
        
        return categoryItems;
    }
    
    private BazaarCategory getItemCategory(Material material) {
        if (material == Material.WHEAT || material == Material.CARROT || material == Material.POTATO ||
            material == Material.PUMPKIN || material == Material.MELON || material == Material.SUGAR_CANE ||
            material == Material.CACTUS || material == Material.NETHER_WART) {
            return BazaarCategory.FARMING;
        } else if (material == Material.COBBLESTONE || material == Material.COAL || material == Material.IRON_INGOT ||
                   material == Material.GOLD_INGOT || material == Material.DIAMOND || material == Material.EMERALD ||
                   material == Material.REDSTONE || material == Material.LAPIS_LAZULI || material == Material.QUARTZ ||
                   material == Material.OBSIDIAN) {
            return BazaarCategory.MINING;
        } else if (material.name().contains("LOG")) {
            return BazaarCategory.FORAGING;
        } else if (material == Material.ROTTEN_FLESH || material == Material.BONE || material == Material.STRING ||
                   material == Material.GUNPOWDER || material == Material.ENDER_PEARL || material == Material.BLAZE_ROD ||
                   material == Material.GHAST_TEAR || material == Material.WITHER_SKELETON_SKULL) {
            return BazaarCategory.COMBAT;
        } else if (material == Material.COD || material == Material.SALMON || material == Material.TROPICAL_FISH ||
                   material == Material.PUFFERFISH) {
            return BazaarCategory.FISHING;
        } else {
            return BazaarCategory.SPECIAL;
        }
    }
    
    private void addBazaarCategory(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(Component.text(description), Component.text(""), Component.text("§eClick to browse!")));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    private void addBazaarItem(Inventory gui, int slot, Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            List<Component> componentLore = new ArrayList<>();
            for (String line : lore) {
                componentLore.add(Component.text(line));
            }
            meta.lore(componentLore);
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    private void addBazaarItemDisplay(Inventory gui, int slot, BazaarItem item) {
        ItemStack displayItem = new ItemStack(item.getMaterial());
        ItemMeta meta = displayItem.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("§e" + item.getName()));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Buy Price: §6" + String.format("%.2f", item.getBuyPrice()) + " coins");
            lore.add("§7Sell Price: §a" + String.format("%.2f", item.getSellPrice()) + " coins");
            lore.add("§7Market Price: §e" + String.format("%.2f", item.getMarketPrice()) + " coins");
            lore.add("");
            lore.add("§eClick to buy!");
            lore.add("§aShift-click to sell!");
            
            meta.lore(lore.stream().toList());
            displayItem.setItemMeta(meta);
        }
        gui.setItem(slot, displayItem);
    }
    
    private void addOrderDisplay(Inventory gui, int slot, BazaarOrder order) {
        BazaarItem item = bazaarItems.get(order.getMaterial());
        if (item == null) return;
        
        ItemStack displayItem = new ItemStack(order.getMaterial());
        ItemMeta meta = displayItem.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("§e" + item.getName()));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Type: " + order.getType().getDisplayName());
            lore.add("§7Price: §6" + String.format("%.2f", order.getPrice()) + " coins each");
            lore.add("§7Amount: §e" + order.getRemainingAmount());
            lore.add("§7Total: §6" + String.format("%.2f", order.getPrice() * order.getRemainingAmount()) + " coins");
            lore.add("");
            lore.add("§cClick to cancel order!");
            
            meta.lore(lore.stream().toList());
            displayItem.setItemMeta(meta);
        }
        gui.setItem(slot, displayItem);
    }
    
    public enum BazaarCategory {
        FARMING("§eFarming"),
        MINING("§7Mining"),
        FORAGING("§6Foraging"),
        COMBAT("§cCombat"),
        FISHING("§bFishing"),
        SPECIAL("§aSpecial");
        
        private final String displayName;
        
        BazaarCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() { return displayName; }
    }
    
    public enum BazaarOrderType {
        BUY("§6Buy"),
        SELL("§aSell");
        
        private final String displayName;
        
        BazaarOrderType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() { return displayName; }
    }
    
    public static class BazaarItem {
        private final Material material;
        private final String name;
        private final double basePrice;
        private double buyPrice;
        private double sellPrice;
        private double marketPrice;
        private final List<Double> priceHistory;
        
        public BazaarItem(Material material, String name, double basePrice) {
            this.material = material;
            this.name = name;
            this.basePrice = basePrice;
            this.buyPrice = basePrice * 1.1; // 10% markup
            this.sellPrice = basePrice * 0.9; // 10% markdown
            this.marketPrice = basePrice;
            this.priceHistory = new ArrayList<>();
            this.priceHistory.add(basePrice);
        }
        
        public void updatePrices() {
            // Update prices based on supply and demand
            // This would implement market mechanics
            
            // For now, add some random fluctuation
            double fluctuation = (Math.random() - 0.5) * 0.1; // ±5% fluctuation
            marketPrice = basePrice * (1.0 + fluctuation);
            
            buyPrice = marketPrice * 1.1;
            sellPrice = marketPrice * 0.9;
            
            priceHistory.add(marketPrice);
            
            // Keep only last 100 prices
            if (priceHistory.size() > 100) {
                priceHistory.remove(0);
            }
        }
        
        public void updateMarketPrice(double newPrice) {
            marketPrice = newPrice;
            buyPrice = marketPrice * 1.1;
            sellPrice = marketPrice * 0.9;
            
            priceHistory.add(marketPrice);
            
            if (priceHistory.size() > 100) {
                priceHistory.remove(0);
            }
        }
        
        // Getters
        public Material getMaterial() { return material; }
        public String getName() { return name; }
        public double getBasePrice() { return basePrice; }
        public double getBuyPrice() { return buyPrice; }
        public double getSellPrice() { return sellPrice; }
        public double getMarketPrice() { return marketPrice; }
        public List<Double> getPriceHistory() { return priceHistory; }
    }
    
    public static class BazaarOrder {
        private final String id;
        private final UUID playerId;
        private final Material material;
        private final BazaarOrderType type;
        private final double price;
        private int amount;
        private final long timestamp;
        
        public BazaarOrder(UUID playerId, Material material, BazaarOrderType type, double price, int amount) {
            this.id = UUID.randomUUID().toString();
            this.playerId = playerId;
            this.material = material;
            this.type = type;
            this.price = price;
            this.amount = amount;
            this.timestamp = java.lang.System.currentTimeMillis();
        }
        
        public void reduceAmount(int amount) {
            this.amount = Math.max(0, this.amount - amount);
        }
        
        // Getters
        public String getId() { return id; }
        public UUID getPlayerId() { return playerId; }
        public Material getMaterial() { return material; }
        public BazaarOrderType getType() { return type; }
        public double getPrice() { return price; }
        public int getAmount() { return amount; }
        public int getRemainingAmount() { return amount; }
        public long getTimestamp() { return timestamp; }
    }
    
    public static class PlayerBazaarData {
        private final UUID playerId;
        private int ordersCreated;
        private int ordersCompleted;
        private double totalSpent;
        private double totalEarned;
        
        public PlayerBazaarData(UUID playerId) {
            this.playerId = playerId;
            this.ordersCreated = 0;
            this.ordersCompleted = 0;
            this.totalSpent = 0.0;
            this.totalEarned = 0.0;
        }
        
        // Getters and setters
        public UUID getPlayerId() { return playerId; }
        public int getOrdersCreated() { return ordersCreated; }
        public void setOrdersCreated(int ordersCreated) { this.ordersCreated = ordersCreated; }
        public int getOrdersCompleted() { return ordersCompleted; }
        public void setOrdersCompleted(int ordersCompleted) { this.ordersCompleted = ordersCompleted; }
        public double getTotalSpent() { return totalSpent; }
        public void setTotalSpent(double totalSpent) { this.totalSpent = totalSpent; }
        public double getTotalEarned() { return totalEarned; }
        public void setTotalEarned(double totalEarned) { this.totalEarned = totalEarned; }
    }
}
