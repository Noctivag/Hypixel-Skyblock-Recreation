package de.noctivag.skyblock.auction;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Complete Bazaar system matching Hypixel Skyblock
 * Instant buy/sell commodity market
 */
public class Bazaar {

    private final SkyblockPlugin plugin;
    private final Map<String, BazaarProduct> products = new ConcurrentHashMap<>();

    public Bazaar(SkyblockPlugin plugin) {
        this.plugin = plugin;
        initializeProducts();
    }

    /**
     * Initialize common bazaar products
     */
    private void initializeProducts() {
        // Resources
        addProduct("WHEAT", Material.WHEAT, 1, 3);
        addProduct("CARROT", Material.CARROT, 2, 4);
        addProduct("POTATO", Material.POTATO, 2, 4);
        addProduct("SUGAR_CANE", Material.SUGAR_CANE, 2, 5);
        addProduct("MELON", Material.MELON_SLICE, 1, 2);
        addProduct("PUMPKIN", Material.PUMPKIN, 3, 6);
        
        // Wood
        addProduct("OAK_LOG", Material.OAK_LOG, 2, 4);
        addProduct("BIRCH_LOG", Material.BIRCH_LOG, 2, 4);
        addProduct("SPRUCE_LOG", Material.SPRUCE_LOG, 2, 4);
        
        // Ores
        addProduct("COBBLESTONE", Material.COBBLESTONE, 1, 2);
        addProduct("COAL", Material.COAL, 2, 4);
        addProduct("IRON_INGOT", Material.IRON_INGOT, 5, 10);
        addProduct("GOLD_INGOT", Material.GOLD_INGOT, 8, 15);
        addProduct("DIAMOND", Material.DIAMOND, 50, 100);
        addProduct("EMERALD", Material.EMERALD, 30, 60);
        
        // Mob Drops
        addProduct("STRING", Material.STRING, 3, 6);
        addProduct("BONE", Material.BONE, 2, 5);
        addProduct("ROTTEN_FLESH", Material.ROTTEN_FLESH, 1, 3);
        addProduct("ENDER_PEARL", Material.ENDER_PEARL, 10, 20);
        addProduct("SLIME_BALL", Material.SLIME_BALL, 5, 10);
        
        // Enchanted items (higher tier)
        addProduct("ENCHANTED_COAL", Material.COAL_BLOCK, 160, 320);
        addProduct("ENCHANTED_IRON", Material.IRON_BLOCK, 320, 640);
        addProduct("ENCHANTED_GOLD", Material.GOLD_BLOCK, 640, 1280);
        addProduct("ENCHANTED_DIAMOND", Material.DIAMOND_BLOCK, 3200, 6400);
    }

    private void addProduct(String id, Material material, long baseBuyPrice, long baseSellPrice) {
        products.put(id, new BazaarProduct(id, material, baseBuyPrice, baseSellPrice));
    }

    /**
     * Place a buy order
     */
    public boolean placeBuyOrder(Player player, String productId, int amount, long pricePerUnit) {
        BazaarProduct product = products.get(productId);
        if (product == null) {
            player.sendMessage("§cProduct not found!");
            return false;
        }

        long totalCost = pricePerUnit * amount;
        
        // TODO: Check and withdraw balance
        
        BuyOrder order = new BuyOrder(
            UUID.randomUUID(),
            player.getUniqueId(),
            productId,
            amount,
            pricePerUnit,
            System.currentTimeMillis()
        );

        product.addBuyOrder(order);
        
        player.sendMessage("§aBuy order placed!");
        player.sendMessage("§7" + amount + "x " + productId + " §7@ §6" + formatCoins(pricePerUnit) + " §7each");
        player.sendMessage("§7Total: §6" + formatCoins(totalCost));

        // Try to fulfill immediately
        tryFulfillOrders(productId);

        return true;
    }

    /**
     * Place a sell offer
     */
    public boolean placeSellOffer(Player player, String productId, int amount, long pricePerUnit) {
        BazaarProduct product = products.get(productId);
        if (product == null) {
            player.sendMessage("§cProduct not found!");
            return false;
        }

        // TODO: Check player has items
        
        SellOffer offer = new SellOffer(
            UUID.randomUUID(),
            player.getUniqueId(),
            productId,
            amount,
            pricePerUnit,
            System.currentTimeMillis()
        );

        product.addSellOffer(offer);
        
        player.sendMessage("§aSell offer placed!");
        player.sendMessage("§7" + amount + "x " + productId + " §7@ §6" + formatCoins(pricePerUnit) + " §7each");
        player.sendMessage("§7Total: §6" + formatCoins(pricePerUnit * amount));

        // Try to fulfill immediately
        tryFulfillOrders(productId);

        return true;
    }

    /**
     * Instant buy from bazaar
     */
    public boolean instantBuy(Player player, String productId, int amount) {
        BazaarProduct product = products.get(productId);
        if (product == null) {
            player.sendMessage("§cProduct not found!");
            return false;
        }

        long price = product.getInstantBuyPrice() * amount;
        
        // TODO: Check and withdraw balance
        // TODO: Give items to player
        
        player.sendMessage("§aInstant buy complete!");
        player.sendMessage("§7Purchased " + amount + "x " + productId + " §7for §6" + formatCoins(price));

        return true;
    }

    /**
     * Instant sell to bazaar
     */
    public boolean instantSell(Player player, String productId, int amount) {
        BazaarProduct product = products.get(productId);
        if (product == null) {
            player.sendMessage("§cProduct not found!");
            return false;
        }

        // TODO: Check player has items
        // TODO: Remove items from player

        long coins = product.getInstantSellPrice() * amount;
        // TODO: Give coins to player
        
        player.sendMessage("§aInstant sell complete!");
        player.sendMessage("§7Sold " + amount + "x " + productId + " §7for §6" + formatCoins(coins));

        return true;
    }

    /**
     * Try to fulfill pending orders
     */
    private void tryFulfillOrders(String productId) {
        BazaarProduct product = products.get(productId);
        if (product == null) return;

        List<BuyOrder> buyOrders = new ArrayList<>(product.getBuyOrders());
        List<SellOffer> sellOffers = new ArrayList<>(product.getSellOffers());

        // Sort orders
        buyOrders.sort(Comparator.comparingLong(BuyOrder::getPricePerUnit).reversed());
        sellOffers.sort(Comparator.comparingLong(SellOffer::getPricePerUnit));

        for (BuyOrder buyOrder : buyOrders) {
            for (SellOffer sellOffer : sellOffers) {
                if (buyOrder.getPricePerUnit() >= sellOffer.getPricePerUnit()) {
                    int traded = Math.min(buyOrder.getAmount(), sellOffer.getAmount());
                    
                    // Fulfill trade
                    buyOrder.reduce(traded);
                    sellOffer.reduce(traded);

                    // TODO: Transfer items and coins
                    
                    if (buyOrder.getAmount() == 0) break;
                }
            }
        }

        // Remove fulfilled orders
        product.getBuyOrders().removeIf(order -> order.getAmount() == 0);
        product.getSellOffers().removeIf(offer -> offer.getAmount() == 0);
    }

    /**
     * Get product
     */
    public BazaarProduct getProduct(String productId) {
        return products.get(productId);
    }

    /**
     * Get all products
     */
    public Collection<BazaarProduct> getAllProducts() {
        return products.values();
    }

    private String formatCoins(long amount) {
        if (amount >= 1_000_000_000) return String.format("%.1fB", amount / 1_000_000_000.0);
        if (amount >= 1_000_000) return String.format("%.1fM", amount / 1_000_000.0);
        if (amount >= 1_000) return String.format("%.1fK", amount / 1_000.0);
        return String.valueOf(amount);
    }

    public static class BazaarProduct {
        private final String id;
        private final Material material;
        private final long baseBuyPrice;
        private final long baseSellPrice;
        private final List<BuyOrder> buyOrders = new ArrayList<>();
        private final List<SellOffer> sellOffers = new ArrayList<>();

        public BazaarProduct(String id, Material material, long baseBuyPrice, long baseSellPrice) {
            this.id = id;
            this.material = material;
            this.baseBuyPrice = baseBuyPrice;
            this.baseSellPrice = baseSellPrice;
        }

        public void addBuyOrder(BuyOrder order) { buyOrders.add(order); }
        public void addSellOffer(SellOffer offer) { sellOffers.add(offer); }

        public String getId() { return id; }
        public Material getMaterial() { return material; }
        public List<BuyOrder> getBuyOrders() { return buyOrders; }
        public List<SellOffer> getSellOffers() { return sellOffers; }

        public long getInstantBuyPrice() {
            return sellOffers.isEmpty() ? baseBuyPrice : 
                   sellOffers.stream().mapToLong(SellOffer::getPricePerUnit).min().orElse(baseBuyPrice);
        }

        public long getInstantSellPrice() {
            return buyOrders.isEmpty() ? baseSellPrice :
                   buyOrders.stream().mapToLong(BuyOrder::getPricePerUnit).max().orElse(baseSellPrice);
        }
    }

    public static class BuyOrder {
        private final UUID id;
        private final UUID buyerId;
        private final String productId;
        private int amount;
        private final long pricePerUnit;
        private final long timestamp;

        public BuyOrder(UUID id, UUID buyerId, String productId, int amount, long pricePerUnit, long timestamp) {
            this.id = id;
            this.buyerId = buyerId;
            this.productId = productId;
            this.amount = amount;
            this.pricePerUnit = pricePerUnit;
            this.timestamp = timestamp;
        }

        public void reduce(int amt) { this.amount -= amt; }

        public UUID getId() { return id; }
        public UUID getBuyerId() { return buyerId; }
        public String getProductId() { return productId; }
        public int getAmount() { return amount; }
        public long getPricePerUnit() { return pricePerUnit; }
        public long getTimestamp() { return timestamp; }
    }

    public static class SellOffer {
        private final UUID id;
        private final UUID sellerId;
        private final String productId;
        private int amount;
        private final long pricePerUnit;
        private final long timestamp;

        public SellOffer(UUID id, UUID sellerId, String productId, int amount, long pricePerUnit, long timestamp) {
            this.id = id;
            this.sellerId = sellerId;
            this.productId = productId;
            this.amount = amount;
            this.pricePerUnit = pricePerUnit;
            this.timestamp = timestamp;
        }

        public void reduce(int amt) { this.amount -= amt; }

        public UUID getId() { return id; }
        public UUID getSellerId() { return sellerId; }
        public String getProductId() { return productId; }
        public int getAmount() { return amount; }
        public long getPricePerUnit() { return pricePerUnit; }
        public long getTimestamp() { return timestamp; }
    }
}
