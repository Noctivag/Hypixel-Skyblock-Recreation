package de.noctivag.skyblock.features.economy.auction;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.economy.auction.types.AuctionCategory;
import de.noctivag.skyblock.features.economy.auction.types.AuctionStatus;
import de.noctivag.skyblock.features.economy.auction.types.AuctionBid;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents an auction item
 */
public class AuctionItem {
    
    private final UUID auctionId;
    private final UUID sellerId;
    private final String itemId;
    private final int quantity;
    private final double startingBid;
    private final double binPrice;
    private LocalDateTime endTime;
    private AuctionStatus status;
    private final List<AuctionBid> bids;
    private final LocalDateTime createdAt;
    
    public AuctionItem(UUID auctionId, UUID sellerId, String itemId, int quantity, 
                      double startingBid, double binPrice, LocalDateTime endTime, AuctionStatus status) {
        this.auctionId = auctionId;
        this.sellerId = sellerId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.startingBid = startingBid;
        this.binPrice = binPrice;
        this.endTime = endTime;
        this.status = status;
        this.bids = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }
    
    /**
     * Add a bid to this auction
     */
    public void addBid(AuctionBid bid) {
        bids.add(bid);
        bids.sort((a, b) -> Double.compare(b.getBidAmount(), a.getBidAmount()));
    }
    
    /**
     * Get current highest bid
     */
    public double getCurrentBid() {
        return bids.isEmpty() ? startingBid : bids.get(0).getBidAmount();
    }
    
    /**
     * Get highest bidder
     */
    public UUID getHighestBidder() {
        return bids.isEmpty() ? null : bids.get(0).getBidderId();
    }
    
    /**
     * Check if auction has bids
     */
    public boolean hasBids() {
        return !bids.isEmpty();
    }
    
    /**
     * Get bid count
     */
    public int getBidCount() {
        return bids.size();
    }
    
    /**
     * Get time remaining
     */
    public long getTimeRemainingSeconds() {
        return java.time.Duration.between(LocalDateTime.now(), endTime).getSeconds();
    }
    
    /**
     * Get formatted time remaining
     */
    public String getFormattedTimeRemaining() {
        long seconds = getTimeRemainingSeconds();
        if (seconds <= 0) return "§cExpired";
        
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        
        if (hours > 0) {
            return String.format("§a%dh %dm %ds", hours, minutes, secs);
        } else if (minutes > 0) {
            return String.format("§a%dm %ds", minutes, secs);
        } else {
            return String.format("§a%ds", secs);
        }
    }
    
    /**
     * Get formatted starting bid
     */
    public String getFormattedStartingBid() {
        return "§e" + String.format("%.2f", startingBid) + " coins";
    }
    
    /**
     * Get formatted current bid
     */
    public String getFormattedCurrentBid() {
        return "§6" + String.format("%.2f", getCurrentBid()) + " coins";
    }
    
    /**
     * Get formatted bin price
     */
    public String getFormattedBinPrice() {
        if (binPrice <= 0) return "§7N/A";
        return "§a" + String.format("%.2f", binPrice) + " coins";
    }
    
    /**
     * Get item lore for display
     */
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        
        lore.add("§7Quantity: §e" + quantity);
        lore.add("§7Starting Bid: " + getFormattedStartingBid());
        lore.add("§7Current Bid: " + getFormattedCurrentBid());
        
        if (binPrice > 0) {
            lore.add("§7Buy It Now: " + getFormattedBinPrice());
        }
        
        lore.add("§7Time Remaining: " + getFormattedTimeRemaining());
        lore.add("§7Bids: §e" + getBidCount());
        
        if (hasBids()) {
            lore.add("§7Highest Bidder: §b" + getHighestBidder().toString().substring(0, 8) + "...");
        }
        
        lore.add("");
        lore.add("§7Status: " + status.getColorCode() + status.getDisplayName());
        
        return lore;
    }
    
    /**
     * Check if auction is active
     */
    public boolean isActive() {
        return status == AuctionStatus.ACTIVE && getTimeRemainingSeconds() > 0;
    }
    
    /**
     * Check if auction is expired
     */
    public boolean isExpired() {
        return getTimeRemainingSeconds() <= 0;
    }
    
    /**
     * Get auction category
     */
    public AuctionCategory getCategory() {
        // Simple category determination based on item ID
        String item = itemId.toLowerCase();
        if (item.contains("sword") || item.contains("bow") || item.contains("armor")) {
            return AuctionCategory.WEAPONS_ARMOR;
        } else if (item.contains("pickaxe") || item.contains("axe") || item.contains("shovel")) {
            return AuctionCategory.TOOLS;
        } else if (item.contains("pet")) {
            return AuctionCategory.PETS;
        } else if (item.contains("enchanted")) {
            return AuctionCategory.ENCHANTED;
        } else {
            return AuctionCategory.MATERIALS;
        }
    }
    
    // Getters and Setters
    public UUID getAuctionId() {
        return auctionId;
    }
    
    public UUID getSellerId() {
        return sellerId;
    }
    
    public String getItemId() {
        return itemId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public double getStartingBid() {
        return startingBid;
    }
    
    public double getBinPrice() {
        return binPrice;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public AuctionStatus getStatus() {
        return status;
    }
    
    public void setStatus(AuctionStatus status) {
        this.status = status;
    }
    
    public List<AuctionBid> getBids() {
        return new ArrayList<>(bids);
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public String toString() {
        return String.format("Auction{id=%s, item=%s, currentBid=%.2f, timeRemaining=%s}", 
            auctionId.toString().substring(0, 8), itemId, getCurrentBid(), getFormattedTimeRemaining());
    }
}
