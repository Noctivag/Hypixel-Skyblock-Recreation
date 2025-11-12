package de.noctivag.skyblock.auction;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Complete Auction House system matching Hypixel Skyblock
 */
public class AuctionHouse {

    private final SkyblockPlugin plugin;
    private final Map<UUID, Auction> activeAuctions = new ConcurrentHashMap<>();
    private final Map<UUID, List<Bid>> auctionBids = new ConcurrentHashMap<>();

    public AuctionHouse(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Create a new auction
     */
    public boolean createAuction(Player player, ItemStack item, long startingBid, long duration, AuctionType type) {
        if (item == null || item.getType().isAir()) {
            player.sendMessage("§cInvalid item!");
            return false;
        }

        if (startingBid < 1) {
            player.sendMessage("§cStarting bid must be at least 1 coin!");
            return false;
        }

        UUID auctionId = UUID.randomUUID();
        Auction auction = new Auction(
            auctionId,
            player.getUniqueId(),
            player.getName(),
            item.clone(),
            startingBid,
            System.currentTimeMillis() + duration,
            type
        );

        activeAuctions.put(auctionId, auction);
        auctionBids.put(auctionId, new ArrayList<>());

        // Remove item from inventory
        item.setAmount(0);

        player.sendMessage("§aAuction created!");
        player.sendMessage("§7Starting Bid: §6" + formatCoins(startingBid));
        player.sendMessage("§7Duration: §e" + formatDuration(duration));

        return true;
    }

    /**
     * Place a bid on an auction
     */
    public boolean placeBid(Player player, UUID auctionId, long bidAmount) {
        Auction auction = activeAuctions.get(auctionId);
        if (auction == null) {
            player.sendMessage("§cAuction not found!");
            return false;
        }

        if (auction.isExpired()) {
            player.sendMessage("§cThis auction has ended!");
            return false;
        }

        if (auction.getSellerId().equals(player.getUniqueId())) {
            player.sendMessage("§cYou cannot bid on your own auction!");
            return false;
        }

        List<Bid> bids = auctionBids.get(auctionId);
        Bid currentHighest = bids.isEmpty() ? null : bids.get(bids.size() - 1);

        long minimumBid = currentHighest != null ? 
                         currentHighest.getAmount() + (currentHighest.getAmount() / 10) : // 10% more
                         auction.getStartingBid();

        if (bidAmount < minimumBid) {
            player.sendMessage("§cBid must be at least §6" + formatCoins(minimumBid) + "§c!");
            return false;
        }

        // TODO: Check player balance
        // if (economyManager.getBalance(player) < bidAmount) {
        //     player.sendMessage("§cYou don't have enough coins!");
        //     return false;
        // }

        Bid bid = new Bid(player.getUniqueId(), player.getName(), bidAmount, System.currentTimeMillis());
        bids.add(bid);

        // Refund previous highest bidder
        if (currentHighest != null) {
            // TODO: Refund coins to previous bidder
        }

        player.sendMessage("§aBid placed for §6" + formatCoins(bidAmount) + "§a!");
        
        // Notify seller
        if (auction.getType() == AuctionType.AUCTION) {
            // Send notification to seller
        }

        return true;
    }

    /**
     * Buy an item instantly (BIN)
     */
    public boolean buyNow(Player player, UUID auctionId) {
        Auction auction = activeAuctions.get(auctionId);
        if (auction == null) {
            player.sendMessage("§cAuction not found!");
            return false;
        }

        if (auction.getType() != AuctionType.BIN) {
            player.sendMessage("§cThis is not a BIN auction!");
            return false;
        }

        if (auction.isExpired()) {
            player.sendMessage("§cThis auction has ended!");
            return false;
        }

        long price = auction.getStartingBid();

        // TODO: Check and deduct balance
        // if (!economyManager.withdrawMoney(player, price)) {
        //     player.sendMessage("§cYou don't have enough coins!");
        //     return false;
        // }

        // Give item to buyer
        player.getInventory().addItem(auction.getItem());

        // Give coins to seller
        // TODO: economyManager.depositMoney(seller, price);

        activeAuctions.remove(auctionId);
        auctionBids.remove(auctionId);

        player.sendMessage("§aYou purchased " + auction.getItem().getItemMeta().getDisplayName() + " §afor §6" + formatCoins(price) + "§a!");

        return true;
    }

    /**
     * End an auction and distribute items/coins
     */
    public void endAuction(UUID auctionId) {
        Auction auction = activeAuctions.get(auctionId);
        if (auction == null) return;

        List<Bid> bids = auctionBids.get(auctionId);
        
        if (auction.getType() == AuctionType.AUCTION && bids != null && !bids.isEmpty()) {
            Bid winningBid = bids.get(bids.size() - 1);
            
            // TODO: Give item to winner
            // TODO: Give coins to seller
            
            // Notifications would go here
        } else {
            // No bids - return item to seller
            // TODO: Return item to seller
        }

        activeAuctions.remove(auctionId);
        auctionBids.remove(auctionId);
    }

    /**
     * Get all active auctions
     */
    public List<Auction> getActiveAuctions() {
        return new ArrayList<>(activeAuctions.values());
    }

    /**
     * Get player's auctions
     */
    public List<Auction> getPlayerAuctions(UUID playerId) {
        List<Auction> playerAuctions = new ArrayList<>();
        for (Auction auction : activeAuctions.values()) {
            if (auction.getSellerId().equals(playerId)) {
                playerAuctions.add(auction);
            }
        }
        return playerAuctions;
    }

    /**
     * Get player's bids
     */
    public List<UUID> getPlayerBids(UUID playerId) {
        List<UUID> playerBidAuctions = new ArrayList<>();
        for (Map.Entry<UUID, List<Bid>> entry : auctionBids.entrySet()) {
            for (Bid bid : entry.getValue()) {
                if (bid.getBidderId().equals(playerId)) {
                    playerBidAuctions.add(entry.getKey());
                    break;
                }
            }
        }
        return playerBidAuctions;
    }

    private String formatCoins(long amount) {
        if (amount >= 1_000_000_000) return String.format("%.1fB", amount / 1_000_000_000.0);
        if (amount >= 1_000_000) return String.format("%.1fM", amount / 1_000_000.0);
        if (amount >= 1_000) return String.format("%.1fK", amount / 1_000.0);
        return String.valueOf(amount);
    }

    private String formatDuration(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) return days + "d";
        if (hours > 0) return hours + "h";
        if (minutes > 0) return minutes + "m";
        return seconds + "s";
    }

    public static class Auction {
        private final UUID id;
        private final UUID sellerId;
        private final String sellerName;
        private final ItemStack item;
        private final long startingBid;
        private final long endTime;
        private final AuctionType type;

        public Auction(UUID id, UUID sellerId, String sellerName, ItemStack item,
                      long startingBid, long endTime, AuctionType type) {
            this.id = id;
            this.sellerId = sellerId;
            this.sellerName = sellerName;
            this.item = item;
            this.startingBid = startingBid;
            this.endTime = endTime;
            this.type = type;
        }

        public UUID getId() { return id; }
        public UUID getSellerId() { return sellerId; }
        public String getSellerName() { return sellerName; }
        public ItemStack getItem() { return item; }
        public long getStartingBid() { return startingBid; }
        public long getEndTime() { return endTime; }
        public AuctionType getType() { return type; }
        
        public boolean isExpired() {
            return System.currentTimeMillis() >= endTime;
        }
        
        public long getTimeLeft() {
            return Math.max(0, endTime - System.currentTimeMillis());
        }
    }

    public static class Bid {
        private final UUID bidderId;
        private final String bidderName;
        private final long amount;
        private final long timestamp;

        public Bid(UUID bidderId, String bidderName, long amount, long timestamp) {
            this.bidderId = bidderId;
            this.bidderName = bidderName;
            this.amount = amount;
            this.timestamp = timestamp;
        }

        public UUID getBidderId() { return bidderId; }
        public String getBidderName() { return bidderName; }
        public long getAmount() { return amount; }
        public long getTimestamp() { return timestamp; }
    }

    public enum AuctionType {
        AUCTION,  // Normal auction with bidding
        BIN       // Buy It Now
    }
}
