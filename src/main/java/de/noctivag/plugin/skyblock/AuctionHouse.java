package de.noctivag.plugin.skyblock;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AuctionHouse {
    private final Plugin plugin;
    private final Map<UUID, Auction> auctions = new ConcurrentHashMap<>();
    private final Map<UUID, List<Auction>> playerAuctions = new ConcurrentHashMap<>();
    private final Map<UUID, List<Auction>> playerBids = new ConcurrentHashMap<>();
    
    public AuctionHouse(Plugin plugin) {
        this.plugin = plugin;
        startAuctionTimer();
    }
    
    public void createAuction(Player player, ItemStack item, double startingBid, double binPrice, int durationHours) {
        UUID auctionId = UUID.randomUUID();
        long endTime = System.currentTimeMillis() + (durationHours * 60 * 60 * 1000L);
        
        Auction auction = new Auction(auctionId, player.getUniqueId(), item, startingBid, binPrice, endTime);
        auctions.put(auctionId, auction);
        
        playerAuctions.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>()).add(auction);
        
        player.sendMessage("§a§lAUCTION CREATED!");
        player.sendMessage("§7Item: §e" + item.getType().name());
        player.sendMessage("§7Starting Bid: §6" + plugin.getEconomyManager().formatMoney(startingBid));
        player.sendMessage("§7BIN Price: §6" + plugin.getEconomyManager().formatMoney(binPrice));
        player.sendMessage("§7Duration: §e" + durationHours + " hours");
    }
    
    public boolean placeBid(Player player, UUID auctionId, double bidAmount) {
        Auction auction = auctions.get(auctionId);
        if (auction == null) return false;
        
        if (auction.isExpired()) return false;
        if (bidAmount <= auction.getCurrentBid()) return false;
        if (bidAmount <= auction.getStartingBid()) return false;
        
        // Check if player has enough money
        if (!plugin.getEconomyManager().hasBalance(player, bidAmount)) return false;
        
        // Refund previous bidder
        if (auction.getCurrentBidder() != null) {
            Player previousBidder = plugin.getServer().getPlayer(auction.getCurrentBidder());
            if (previousBidder != null) {
                plugin.getEconomyManager().giveMoney(previousBidder, auction.getCurrentBid());
                previousBidder.sendMessage("§cYour bid was outbid!");
            }
        }
        
        // Place new bid
        plugin.getEconomyManager().withdrawMoney(player, bidAmount);
        auction.setCurrentBid(bidAmount);
        auction.setCurrentBidder(player.getUniqueId());
        
        playerBids.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>()).add(auction);
        
        player.sendMessage("§a§lBID PLACED!");
        player.sendMessage("§7Auction: §e" + auction.getItem().getType().name());
        player.sendMessage("§7Your Bid: §6" + plugin.getEconomyManager().formatMoney(bidAmount));
        
        return true;
    }
    
    public boolean buyNow(Player player, UUID auctionId) {
        Auction auction = auctions.get(auctionId);
        if (auction == null) return false;
        
        if (auction.isExpired()) return false;
        if (auction.getBinPrice() <= 0) return false;
        
        // Check if player has enough money
        if (!plugin.getEconomyManager().hasBalance(player, auction.getBinPrice())) return false;
        
        // Complete the auction
        plugin.getEconomyManager().withdrawMoney(player, auction.getBinPrice());
        plugin.getEconomyManager().giveMoney(plugin.getServer().getPlayer(auction.getSeller()), auction.getBinPrice());
        
        // Give item to buyer
        player.getInventory().addItem(auction.getItem());
        
        // Remove auction
        auctions.remove(auctionId);
        playerAuctions.get(auction.getSeller()).remove(auction);
        
        player.sendMessage("§a§lITEM PURCHASED!");
        player.sendMessage("§7Item: §e" + auction.getItem().getType().name());
        player.sendMessage("§7Price: §6" + plugin.getEconomyManager().formatMoney(auction.getBinPrice()));
        
        return true;
    }
    
    public void collectAuction(Player player, UUID auctionId) {
        Auction auction = auctions.get(auctionId);
        if (auction == null) return;
        
        if (!auction.getSeller().equals(player.getUniqueId())) return;
        if (!auction.isExpired()) return;
        
        if (auction.getCurrentBidder() != null) {
            // Auction was sold
            plugin.getEconomyManager().giveMoney(player, auction.getCurrentBid());
            player.sendMessage("§a§lAUCTION SOLD!");
            player.sendMessage("§7Item: §e" + auction.getItem().getType().name());
            player.sendMessage("§7Sold for: §6" + plugin.getEconomyManager().formatMoney(auction.getCurrentBid()));
        } else {
            // Auction expired without bids
            player.getInventory().addItem(auction.getItem());
            player.sendMessage("§c§lAUCTION EXPIRED!");
            player.sendMessage("§7Item returned: §e" + auction.getItem().getType().name());
        }
        
        auctions.remove(auctionId);
        playerAuctions.get(player.getUniqueId()).remove(auction);
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
                    Auction auction = auctions.get(auctionId);
                    if (auction != null) {
                        Player seller = plugin.getServer().getPlayer(auction.getSeller());
                        if (seller != null) {
                            seller.sendMessage("§c§lAUCTION EXPIRED!");
                            seller.sendMessage("§7Item: §e" + auction.getItem().getType().name());
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Check every second
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
    
    public static class Auction {
        private final UUID id;
        private final UUID seller;
        private final ItemStack item;
        private final double startingBid;
        private final double binPrice;
        private final long endTime;
        private double currentBid;
        private UUID currentBidder;
        
        public Auction(UUID id, UUID seller, ItemStack item, double startingBid, double binPrice, long endTime) {
            this.id = id;
            this.seller = seller;
            this.item = item;
            this.startingBid = startingBid;
            this.binPrice = binPrice;
            this.endTime = endTime;
            this.currentBid = startingBid;
            this.currentBidder = null;
        }
        
        public boolean isExpired() {
            return System.currentTimeMillis() >= endTime;
        }
        
        public long getTimeRemaining() {
            return Math.max(0, endTime - System.currentTimeMillis());
        }
        
        // Getters and setters
        public UUID getId() { return id; }
        public UUID getSeller() { return seller; }
        public ItemStack getItem() { return item; }
        public double getStartingBid() { return startingBid; }
        public double getBinPrice() { return binPrice; }
        public long getEndTime() { return endTime; }
        public double getCurrentBid() { return currentBid; }
        public UUID getCurrentBidder() { return currentBidder; }
        
        public void setCurrentBid(double currentBid) { this.currentBid = currentBid; }
        public void setCurrentBidder(UUID currentBidder) { this.currentBidder = currentBidder; }
    }
}
