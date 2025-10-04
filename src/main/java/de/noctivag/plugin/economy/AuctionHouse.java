package de.noctivag.plugin.economy;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.core.PlayerProfile;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Auction House - Erweiterte Auktionsfunktionen
 * 
 * Verantwortlich für:
 * - Auktionen erstellen und verwalten
 * - Bieten und BIN (Buy It Now)
 * - Auktions-Timer
 * - Gebühren-System
 * - Auktions-Historie
 * - Suchfunktionen
 */
public class AuctionHouse {
    private final EconomySystem economySystem;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, Auction> auctions = new ConcurrentHashMap<>();
    private final Map<UUID, List<Auction>> playerAuctions = new ConcurrentHashMap<>();
    private final Map<UUID, List<Auction>> playerBids = new ConcurrentHashMap<>();
    private final Map<UUID, List<Auction>> playerBought = new ConcurrentHashMap<>();
    
    // Auction Configuration
    private final double AUCTION_FEE = 0.05; // 5% fee
    private final double BIN_FEE = 0.02; // 2% fee for BIN
    private final int MAX_AUCTIONS_PER_PLAYER = 7;
    private final long AUCTION_DURATION = 24 * 60 * 60 * 1000L; // 24 hours
    
    public AuctionHouse(EconomySystem economySystem, MultiServerDatabaseManager databaseManager) {
        this.economySystem = economySystem;
        this.databaseManager = databaseManager;
        loadAuctions();
        startAuctionTimer();
    }
    
    private void loadAuctions() {
        // Load active auctions from database
        databaseManager.executeQuery("SELECT * FROM auction_house WHERE is_sold = false AND is_cancelled = false").thenAccept(resultSet -> {
            try {
                while (resultSet.next()) {
                    String auctionId = resultSet.getString("auction_id");
                    UUID sellerUuid = UUID.fromString(resultSet.getString("seller_uuid"));
                    String itemData = resultSet.getString("item_data");
                    double startingBid = resultSet.getDouble("starting_bid");
                    Double currentBid = resultSet.getObject("current_bid", Double.class);
                    String highestBidderStr = resultSet.getString("highest_bidder");
                    UUID highestBidder = highestBidderStr != null ? UUID.fromString(highestBidderStr) : null;
                    long endTime = resultSet.getTimestamp("end_time").getTime();
                    
                    // Parse item data and create auction
                    // This would need proper JSON parsing for ItemStack
                    Auction auction = new Auction(UUID.fromString(auctionId), sellerUuid, null, startingBid, currentBid, endTime);
                    auctions.put(auction.getId(), auction);
                }
            } catch (Exception e) {
                // Handle error
            }
        });
    }
    
    private void startAuctionTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Check for expired auctions
                List<UUID> expiredAuctions = new ArrayList<>();
                for (Map.Entry<UUID, Auction> entry : auctions.entrySet()) {
                    if (entry.getValue().isExpired()) {
                        expiredAuctions.add(entry.getKey());
                    }
                }
                
                // Process expired auctions
                for (UUID auctionId : expiredAuctions) {
                    processExpiredAuction(auctionId);
                }
            }
        }.runTaskTimer(economySystem.plugin, 0L, 20L); // Check every second
    }
    
    public void createAuction(Player player, ItemStack item, double startingBid, double binPrice, int durationHours) {
        UUID playerId = player.getUniqueId();
        
        // Validate inputs
        if (item == null || item.getType().isAir()) {
            player.sendMessage("§cInvalid item!");
            return;
        }
        
        if (startingBid <= 0 || binPrice <= 0 || durationHours <= 0) {
            player.sendMessage("§cInvalid auction parameters!");
            return;
        }
        
        if (binPrice < startingBid) {
            player.sendMessage("§cBIN price must be higher than starting bid!");
            return;
        }
        
        // Check if player has reached auction limit
        List<Auction> playerAuctionList = playerAuctions.getOrDefault(playerId, new ArrayList<>());
        if (playerAuctionList.size() >= MAX_AUCTIONS_PER_PLAYER) {
            player.sendMessage("§cYou have reached the maximum number of auctions (" + MAX_AUCTIONS_PER_PLAYER + ")!");
            return;
        }
        
        // Check if player has the item
        if (!player.getInventory().containsAtLeast(item, item.getAmount())) {
            player.sendMessage("§cYou don't have enough items!");
            return;
        }
        
        // Calculate auction fee
        double fee = startingBid * AUCTION_FEE;
        PlayerProfile profile = economySystem.corePlatform.getPlayerProfile(playerId);
        if (profile == null || !profile.hasBalance(fee)) {
            player.sendMessage("§cYou don't have enough coins for the auction fee! Fee: " + fee);
            return;
        }
        
        // Create auction
        UUID auctionId = UUID.randomUUID();
        long endTime = System.currentTimeMillis() + (durationHours * 60 * 60 * 1000L);
        
        Auction auction = new Auction(auctionId, playerId, item.clone(), startingBid, binPrice, endTime);
        auctions.put(auctionId, auction);
        
        // Add to player's auctions
        playerAuctionList.add(auction);
        playerAuctions.put(playerId, playerAuctionList);
        
        // Remove item from player's inventory
        player.getInventory().removeItem(item);
        
        // Charge auction fee
        profile.tryRemoveCoins(fee);
        
        // Save to database
        saveAuctionToDatabase(auction);
        
        player.sendMessage("§a§lAUCTION CREATED!");
        player.sendMessage("§7Item: §e" + item.getType().name());
        player.sendMessage("§7Starting Bid: §6" + startingBid + " coins");
        player.sendMessage("§7BIN Price: §6" + binPrice + " coins");
        player.sendMessage("§7Duration: §e" + durationHours + " hours");
        player.sendMessage("§7Fee: §6" + fee + " coins");
        player.sendMessage("§7Auction ID: §e" + auctionId.toString().substring(0, 8));
    }
    
    public boolean placeBid(Player player, UUID auctionId, double bidAmount) {
        Auction auction = auctions.get(auctionId);
        if (auction == null) {
            player.sendMessage("§cAuction not found!");
            return false;
        }
        
        // Validate auction state
        if (auction.isExpired()) {
            player.sendMessage("§cThis auction has expired!");
            return false;
        }
        
        if (auction.getSeller().equals(player.getUniqueId())) {
            player.sendMessage("§cYou cannot bid on your own auction!");
            return false;
        }
        
        // Validate bid amount
        double minimumBid = Math.max(auction.getCurrentBid() + 1, auction.getStartingBid());
        if (bidAmount < minimumBid) {
            player.sendMessage("§cBid must be at least " + minimumBid + " coins!");
            return false;
        }
        
        // Synchronized bidding to prevent race conditions
        synchronized (auction) {
            // Double-check auction state after acquiring lock
            if (auction.isExpired()) {
                player.sendMessage("§cThis auction has expired!");
                return false;
            }
            
            // Check if player has enough money
            PlayerProfile profile = economySystem.corePlatform.getPlayerProfile(player.getUniqueId());
            if (profile == null || !profile.hasBalance(bidAmount)) {
                player.sendMessage("§cYou don't have enough coins!");
                return false;
            }
            
            // Refund previous bidder
            if (auction.getCurrentBidder() != null && !auction.getCurrentBidder().equals(player.getUniqueId())) {
                PlayerProfile previousProfile = economySystem.corePlatform.getPlayerProfile(auction.getCurrentBidder());
                if (previousProfile != null) {
                    previousProfile.addCoins(auction.getCurrentBid());
                    
                    // Notify previous bidder
                    Player previousBidder = Bukkit.getPlayer(auction.getCurrentBidder());
                    if (previousBidder != null) {
                        previousBidder.sendMessage("§cYour bid was outbid on " + auction.getItem().getType().name() + "!");
                        previousBidder.sendMessage("§7Refunded: §6" + auction.getCurrentBid() + " coins");
                    }
                }
            }
            
            // Place new bid
            profile.tryRemoveCoins(bidAmount);
            auction.setCurrentBid(bidAmount);
            auction.setCurrentBidder(player.getUniqueId());
            
            // Add to player's bids
            playerBids.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>()).add(auction);
            
            // Update database
            updateBidInDatabase(auction);
            
            player.sendMessage("§a§lBID PLACED!");
            player.sendMessage("§7Auction: §e" + auction.getItem().getType().name());
            player.sendMessage("§7Your Bid: §6" + bidAmount + " coins");
            player.sendMessage("§7Time Left: §e" + formatTime(auction.getTimeRemaining()));
        }
        
        return true;
    }
    
    public boolean buyNow(Player player, UUID auctionId) {
        Auction auction = auctions.get(auctionId);
        if (auction == null) return false;
        
        if (auction.isExpired()) return false;
        if (auction.getBinPrice() <= 0) return false;
        
        // Check if player has enough money
        PlayerProfile profile = economySystem.corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null || !profile.hasBalance(auction.getBinPrice())) return false;
        
        // Calculate BIN fee
        double fee = auction.getBinPrice() * BIN_FEE;
        double totalCost = auction.getBinPrice() + fee;
        
        if (!profile.tryRemoveCoins(totalCost)) return false;
        
        // Complete the auction
        PlayerProfile sellerProfile = economySystem.corePlatform.getPlayerProfile(auction.getSeller());
        if (sellerProfile != null) {
            sellerProfile.addCoins(auction.getBinPrice());
        }
        
        // Give item to buyer
        player.getInventory().addItem(auction.getItem());
        
        // Remove auction
        auctions.remove(auctionId);
        playerAuctions.get(auction.getSeller()).remove(auction);
        
        // Add to player's bought items
        playerBought.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>()).add(auction);
        
        player.sendMessage("§a§lITEM PURCHASED!");
        player.sendMessage("§7Item: §e" + auction.getItem().getType().name());
        player.sendMessage("§7Price: §6" + auction.getBinPrice() + " coins");
        player.sendMessage("§7Fee: §6" + fee + " coins");
        
        return true;
    }
    
    public void collectAuction(Player player, UUID auctionId) {
        Auction auction = auctions.get(auctionId);
        if (auction == null) return;
        
        if (!auction.getSeller().equals(player.getUniqueId())) return;
        if (!auction.isExpired()) return;
        
        if (auction.getCurrentBidder() != null) {
            // Auction was sold
            PlayerProfile profile = economySystem.corePlatform.getPlayerProfile(player.getUniqueId());
            if (profile != null) {
                profile.addCoins(auction.getCurrentBid());
            }
            player.sendMessage("§a§lAUCTION SOLD!");
            player.sendMessage("§7Item: §e" + auction.getItem().getType().name());
            player.sendMessage("§7Sold for: §6" + auction.getCurrentBid() + " coins");
        } else {
            // Auction expired without bids
            player.getInventory().addItem(auction.getItem());
            player.sendMessage("§c§lAUCTION EXPIRED!");
            player.sendMessage("§7Item returned: §e" + auction.getItem().getType().name());
        }
        
        auctions.remove(auctionId);
        playerAuctions.get(player.getUniqueId()).remove(auction);
    }
    
    private void processExpiredAuction(UUID auctionId) {
        Auction auction = auctions.get(auctionId);
        if (auction == null) return;
        
        Player seller = Bukkit.getPlayer(auction.getSeller());
        if (seller != null) {
            seller.sendMessage("§c§lAUCTION EXPIRED!");
            seller.sendMessage("§7Item: §e" + auction.getItem().getType().name());
        }
        
        // Remove from active auctions
        auctions.remove(auctionId);
        playerAuctions.get(auction.getSeller()).remove(auction);
        
        // Update database
        updateAuctionInDatabase(auctionId, true, false);
    }
    
    private void saveAuctionToDatabase(Auction auction) {
        String sql = "INSERT INTO auction_house (auction_id, seller_uuid, item_data, starting_bid, bin_price, current_bid, highest_bidder, end_time, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        databaseManager.executeUpdate(sql, 
            auction.getId().toString(),
            auction.getSeller().toString(),
            serializeItemStack(auction.getItem()),
            auction.getStartingBid(),
            auction.getBinPrice(),
            auction.getCurrentBid(),
            auction.getCurrentBidder() != null ? auction.getCurrentBidder().toString() : null,
            new java.sql.Timestamp(auction.getEndTime()),
            new java.sql.Timestamp(auction.getCreatedAt())
        );
    }
    
    private void updateAuctionInDatabase(UUID auctionId, boolean isSold, boolean isCancelled) {
        String sql = "UPDATE auction_house SET is_sold = ?, is_cancelled = ? WHERE auction_id = ?";
        databaseManager.executeUpdate(sql, isSold, isCancelled, auctionId.toString());
    }
    
    private void updateBidInDatabase(Auction auction) {
        String sql = "UPDATE auction_house SET current_bid = ?, highest_bidder = ? WHERE auction_id = ?";
        databaseManager.executeUpdate(sql, 
            auction.getCurrentBid(),
            auction.getCurrentBidder() != null ? auction.getCurrentBidder().toString() : null,
            auction.getId().toString()
        );
    }
    
    private String serializeItemStack(ItemStack item) {
        // Simple serialization - in a real implementation you'd use proper JSON
        return item.getType().name() + ":" + item.getAmount();
    }
    
    public void openAuctionGUI(Player player) {
        openAuctionGUI(player, 0);
    }
    
    public void openAuctionGUI(Player player, int page) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lAuction House - Page " + (page + 1));
        
        List<Auction> allAuctions = getActiveAuctions();
        int startIndex = page * 45;
        int endIndex = Math.min(startIndex + 45, allAuctions.size());
        
        // Add auction items
        for (int i = startIndex; i < endIndex; i++) {
            Auction auction = allAuctions.get(i);
            ItemStack displayItem = auction.getItem().clone();
            ItemMeta meta = displayItem.getItemMeta();
            
            if (meta != null) {
                List<String> lore = new ArrayList<>();
                lore.add("§7Seller: §e" + Bukkit.getOfflinePlayer(auction.getSeller()).getName());
                lore.add("§7Starting Bid: §6" + auction.getStartingBid() + " coins");
                lore.add("§7Current Bid: §6" + auction.getCurrentBid() + " coins");
                lore.add("§7BIN Price: §6" + auction.getBinPrice() + " coins");
                lore.add("§7Time Left: §e" + formatTime(auction.getTimeRemaining()));
                lore.add("");
                lore.add("§eLeft Click to Bid");
                lore.add("§eRight Click to Buy Now");
                
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
        
        if (endIndex < allAuctions.size()) {
            ItemStack nextPage = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = nextPage.getItemMeta();
            if (nextMeta != null) {
                nextMeta.setDisplayName("§7Next Page");
                nextPage.setItemMeta(nextMeta);
            }
            gui.setItem(53, nextPage);
        }
        
        // Add search and filter options
        ItemStack searchButton = new ItemStack(Material.COMPASS);
        ItemMeta searchMeta = searchButton.getItemMeta();
        if (searchMeta != null) {
            searchMeta.setDisplayName("§aSearch Auctions");
            searchMeta.setLore(Arrays.asList("§7Click to search for specific items"));
            searchButton.setItemMeta(searchMeta);
        }
        gui.setItem(47, searchButton);
        
        ItemStack createAuction = new ItemStack(Material.EMERALD);
        ItemMeta createMeta = createAuction.getItemMeta();
        if (createMeta != null) {
            createMeta.setDisplayName("§aCreate Auction");
            createMeta.setLore(Arrays.asList("§7Click to create a new auction"));
            createAuction.setItemMeta(createMeta);
        }
        gui.setItem(51, createAuction);
        
        player.openInventory(gui);
    }
    
    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) {
            return days + "d " + (hours % 24) + "h";
        } else if (hours > 0) {
            return hours + "h " + (minutes % 60) + "m";
        } else if (minutes > 0) {
            return minutes + "m " + (seconds % 60) + "s";
        } else {
            return seconds + "s";
        }
    }
    
    public List<Auction> searchAuctions(String searchTerm, String category, String sortBy, boolean ascending) {
        List<Auction> results = new ArrayList<>(auctions.values());
        
        // Filter by search term
        if (searchTerm != null && !searchTerm.isEmpty()) {
            results.removeIf(auction -> !auction.getItem().getType().name().toLowerCase().contains(searchTerm.toLowerCase()));
        }
        
        // Filter by category
        if (category != null && !category.isEmpty()) {
            results.removeIf(auction -> !getItemCategory(auction.getItem().getType()).equals(category));
        }
        
        // Sort results
        switch (sortBy) {
            case "price" -> results.sort((a, b) -> ascending ? 
                Double.compare(a.getCurrentBid(), b.getCurrentBid()) : 
                Double.compare(b.getCurrentBid(), a.getCurrentBid()));
            case "time" -> results.sort((a, b) -> ascending ? 
                Long.compare(a.getTimeRemaining(), b.getTimeRemaining()) : 
                Long.compare(b.getTimeRemaining(), a.getTimeRemaining()));
            case "name" -> results.sort((a, b) -> ascending ? 
                a.getItem().getType().name().compareTo(b.getItem().getType().name()) : 
                b.getItem().getType().name().compareTo(a.getItem().getType().name()));
        }
        
        return results;
    }
    
    private String getItemCategory(org.bukkit.Material material) {
        return switch (material) {
            case DIAMOND_SWORD, DIAMOND_AXE, DIAMOND_PICKAXE, DIAMOND_SHOVEL, DIAMOND_HOE -> "WEAPONS";
            case DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS -> "ARMOR";
            case DIAMOND, EMERALD, GOLD_INGOT, IRON_INGOT -> "ORES";
            case WHEAT, CARROT, POTATO, BEETROOT -> "FARMING";
            case OAK_LOG, BIRCH_LOG, SPRUCE_LOG, JUNGLE_LOG -> "FORAGING";
            case COAL, REDSTONE, LAPIS_LAZULI, QUARTZ -> "MINING";
            default -> "MISC";
        };
    }
    
    public List<Auction> getActiveAuctions() {
        return new ArrayList<>(auctions.values());
    }
    
    public List<Auction> getPlayerAuctions(UUID playerId) {
        return playerAuctions.getOrDefault(playerId, new ArrayList<>());
    }
    
    public List<Auction> getPlayerBids(UUID playerId) {
        return playerBids.getOrDefault(playerId, new ArrayList<>());
    }
    
    public List<Auction> getPlayerBought(UUID playerId) {
        return playerBought.getOrDefault(playerId, new ArrayList<>());
    }
    
    // Auction Class
    public static class Auction {
        private final UUID id;
        private final UUID seller;
        private final ItemStack item;
        private final double startingBid;
        private final double binPrice;
        private final long endTime;
        private double currentBid;
        private UUID currentBidder;
        private final long createdAt;
        
        public Auction(UUID id, UUID seller, ItemStack item, double startingBid, double binPrice, long endTime) {
            this.id = id;
            this.seller = seller;
            this.item = item;
            this.startingBid = startingBid;
            this.binPrice = binPrice;
            this.endTime = endTime;
            this.currentBid = startingBid;
            this.currentBidder = null;
            this.createdAt = System.currentTimeMillis();
        }
        
        public boolean isExpired() {
            return System.currentTimeMillis() >= endTime;
        }
        
        public long getTimeRemaining() {
            return Math.max(0, endTime - System.currentTimeMillis());
        }
        
        // Getters and Setters
        public UUID getId() { return id; }
        public UUID getSeller() { return seller; }
        public ItemStack getItem() { return item; }
        public double getStartingBid() { return startingBid; }
        public double getBinPrice() { return binPrice; }
        public long getEndTime() { return endTime; }
        public double getCurrentBid() { return currentBid; }
        public UUID getCurrentBidder() { return currentBidder; }
        public long getCreatedAt() { return createdAt; }
        
        public void setCurrentBid(double currentBid) { this.currentBid = currentBid; }
        public void setCurrentBidder(UUID currentBidder) { this.currentBidder = currentBidder; }
    }
}
