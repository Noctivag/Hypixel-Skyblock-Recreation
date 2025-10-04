package de.noctivag.plugin.skyblock.auction;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Auction House System für Hypixel SkyBlock
 */
public class AuctionHouse {
    
    private final Plugin plugin;
    private final Map<UUID, List<Auction>> playerAuctions = new ConcurrentHashMap<>();
    private final Map<UUID, List<Auction>> playerBids = new ConcurrentHashMap<>();
    private final List<Auction> activeAuctions = Collections.synchronizedList(new ArrayList<>());
    
    // Auction-Definition
    public static class Auction {
        private final UUID auctionId;
        private final UUID sellerId;
        private final ItemStack item;
        private final long startTime;
        private final long endTime;
        private final double startingBid;
        private final double binPrice; // Buy It Now Price
        private final List<Bid> bids;
        private final boolean isBin; // Buy It Now
        
        public Auction(UUID auctionId, UUID sellerId, ItemStack item, long duration, 
                      double startingBid, double binPrice, boolean isBin) {
            this.auctionId = auctionId;
            this.sellerId = sellerId;
            this.item = item;
            this.startTime = System.currentTimeMillis();
            this.endTime = startTime + duration;
            this.startingBid = startingBid;
            this.binPrice = binPrice;
            this.bids = Collections.synchronizedList(new ArrayList<>());
            this.isBin = isBin;
        }
        
        // Getters
        public UUID getAuctionId() { return auctionId; }
        public UUID getSellerId() { return sellerId; }
        public ItemStack getItem() { return item; }
        public long getStartTime() { return startTime; }
        public long getEndTime() { return endTime; }
        public double getStartingBid() { return startingBid; }
        public double getBinPrice() { return binPrice; }
        public List<Bid> getBids() { return bids; }
        public boolean isBin() { return isBin; }
        
        public boolean isExpired() {
            return System.currentTimeMillis() > endTime;
        }
        
        public Bid getHighestBid() {
            return bids.stream()
                .max(Comparator.comparing(Bid::getAmount))
                .orElse(null);
        }
        
        public double getCurrentPrice() {
            Bid highestBid = getHighestBid();
            return highestBid != null ? highestBid.getAmount() : startingBid;
        }
    }
    
    // Bid-Definition
    public static class Bid {
        private final UUID bidderId;
        private final double amount;
        private final long timestamp;
        
        public Bid(UUID bidderId, double amount) {
            this.bidderId = bidderId;
            this.amount = amount;
            this.timestamp = System.currentTimeMillis();
        }
        
        public UUID getBidderId() { return bidderId; }
        public double getAmount() { return amount; }
        public long getTimestamp() { return timestamp; }
    }
    
    public AuctionHouse(Plugin plugin) {
        this.plugin = plugin;
        startAuctionCleanup();
    }
    
    /**
     * Erstellt eine neue Auktion
     */
    public boolean createAuction(Player seller, ItemStack item, long duration, 
                                double startingBid, double binPrice, boolean isBin) {
        // Prüfe ob Spieler das Item hat
        if (!seller.getInventory().containsAtLeast(item, item.getAmount())) {
            seller.sendMessage("§cDu hast nicht genügend Items!");
            return false;
        }
        
        // Prüfe Mindestgebot
        if (startingBid < 1) {
            seller.sendMessage("§cMindestgebot muss mindestens 1 Coin betragen!");
            return false;
        }
        
        // Prüfe BIN-Preis
        if (isBin && binPrice < startingBid) {
            seller.sendMessage("§cBuy It Now Preis muss höher als Mindestgebot sein!");
            return false;
        }
        
        // Erstelle Auktion
        UUID auctionId = UUID.randomUUID();
        Auction auction = new Auction(auctionId, seller.getUniqueId(), item.clone(), 
                                    duration, startingBid, binPrice, isBin);
        
        // Entferne Item aus Inventar
        seller.getInventory().removeItem(item);
        
        // Füge zur aktiven Auktionen hinzu
        activeAuctions.add(auction);
        playerAuctions.computeIfAbsent(seller.getUniqueId(), k -> new ArrayList<>()).add(auction);
        
        seller.sendMessage("§aAuktion erfolgreich erstellt!");
        seller.sendMessage("§7Item: " + item.getAmount() + "x " + item.getType().name());
        seller.sendMessage("§7Mindestgebot: " + startingBid + " Coins");
        if (isBin) {
            seller.sendMessage("§7Buy It Now: " + binPrice + " Coins");
        }
        
        return true;
    }
    
    /**
     * Bietet auf eine Auktion
     */
    public boolean bidOnAuction(Player bidder, UUID auctionId, double amount) {
        Auction auction = getAuctionById(auctionId);
        if (auction == null) {
            bidder.sendMessage("§cAuktion nicht gefunden!");
            return false;
        }
        
        if (auction.isExpired()) {
            bidder.sendMessage("§cDiese Auktion ist bereits abgelaufen!");
            return false;
        }
        
        if (auction.getSellerId().equals(bidder.getUniqueId())) {
            bidder.sendMessage("§cDu kannst nicht auf deine eigene Auktion bieten!");
            return false;
        }
        
        double currentPrice = auction.getCurrentPrice();
        if (amount <= currentPrice) {
            bidder.sendMessage("§cDein Gebot muss höher als " + currentPrice + " Coins sein!");
            return false;
        }
        
        // Prüfe ob Spieler genügend Coins hat
        // Hier würde normalerweise das Economy-System verwendet werden
        // double playerCoins = getPlayerCoins(bidder.getUniqueId());
        // if (playerCoins < amount) {
        //     bidder.sendMessage("§cDu hast nicht genügend Coins!");
        //     return false;
        // }
        
        // Erstelle Gebot
        Bid bid = new Bid(bidder.getUniqueId(), amount);
        auction.getBids().add(bid);
        playerBids.computeIfAbsent(bidder.getUniqueId(), k -> new ArrayList<>()).add(auction);
        
        bidder.sendMessage("§aGebot erfolgreich abgegeben!");
        bidder.sendMessage("§7Gebot: " + amount + " Coins");
        
        // Benachrichtige vorherigen Höchstbietenden
        Bid previousHighest = auction.getBids().stream()
            .filter(b -> !b.getBidderId().equals(bidder.getUniqueId()))
            .max(Comparator.comparing(Bid::getAmount))
            .orElse(null);
        
        if (previousHighest != null) {
            Player previousBidder = plugin.getServer().getPlayer(previousHighest.getBidderId());
            if (previousBidder != null) {
                previousBidder.sendMessage("§eDein Gebot wurde überboten!");
                previousBidder.sendMessage("§7Neues Höchstgebot: " + amount + " Coins");
            }
        }
        
        return true;
    }
    
    /**
     * Kauft eine Auktion sofort (Buy It Now)
     */
    public boolean buyAuctionNow(Player buyer, UUID auctionId) {
        Auction auction = getAuctionById(auctionId);
        if (auction == null) {
            buyer.sendMessage("§cAuktion nicht gefunden!");
            return false;
        }
        
        if (!auction.isBin()) {
            buyer.sendMessage("§cDiese Auktion hat keinen Buy It Now Preis!");
            return false;
        }
        
        if (auction.isExpired()) {
            buyer.sendMessage("§cDiese Auktion ist bereits abgelaufen!");
            return false;
        }
        
        if (auction.getSellerId().equals(buyer.getUniqueId())) {
            buyer.sendMessage("§cDu kannst deine eigene Auktion nicht kaufen!");
            return false;
        }
        
        // Prüfe ob Spieler genügend Coins hat
        // Hier würde normalerweise das Economy-System verwendet werden
        
        // Kaufe Auktion
        completeAuction(auction, buyer.getUniqueId(), auction.getBinPrice());
        
        buyer.sendMessage("§aAuktion erfolgreich gekauft!");
        buyer.sendMessage("§7Preis: " + auction.getBinPrice() + " Coins");
        
        return true;
    }
    
    /**
     * Schließt eine Auktion ab
     */
    private void completeAuction(Auction auction, UUID buyerId, double price) {
        // Entferne aus aktiven Auktionen
        activeAuctions.remove(auction);
        
        // Gib Item an Käufer
        Player buyer = plugin.getServer().getPlayer(buyerId);
        if (buyer != null) {
            buyer.getInventory().addItem(auction.getItem());
        }
        
        // Gib Coins an Verkäufer
        Player seller = plugin.getServer().getPlayer(auction.getSellerId());
        if (seller != null) {
            seller.sendMessage("§aDeine Auktion wurde verkauft!");
            seller.sendMessage("§7Preis: " + price + " Coins");
            // Hier würde normalerweise das Economy-System verwendet werden
        }
        
        // Gib Coins an Höchstbietenden zurück (falls nicht BIN)
        if (!auction.isBin() && auction.getBids().size() > 1) {
            for (Bid bid : auction.getBids()) {
                if (!bid.getBidderId().equals(buyerId)) {
                    Player bidder = plugin.getServer().getPlayer(bid.getBidderId());
                    if (bidder != null) {
                        bidder.sendMessage("§eDein Gebot wurde zurückerstattet!");
                        // Hier würde normalerweise das Economy-System verwendet werden
                    }
                }
            }
        }
    }
    
    /**
     * Gibt eine Auktion anhand der ID zurück
     */
    public Auction getAuctionById(UUID auctionId) {
        return activeAuctions.stream()
            .filter(auction -> auction.getAuctionId().equals(auctionId))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Gibt alle aktiven Auktionen zurück
     */
    public List<Auction> getActiveAuctions() {
        return new ArrayList<>(activeAuctions);
    }
    
    /**
     * Gibt Auktionen eines Spielers zurück
     */
    public List<Auction> getPlayerAuctions(UUID playerId) {
        return new ArrayList<>(playerAuctions.getOrDefault(playerId, new ArrayList<>()));
    }
    
    /**
     * Gibt Gebote eines Spielers zurück
     */
    public List<Auction> getPlayerBids(UUID playerId) {
        return new ArrayList<>(playerBids.getOrDefault(playerId, new ArrayList<>()));
    }
    
    /**
     * Sucht Auktionen nach Item
     */
    public List<Auction> searchAuctions(String query) {
        return activeAuctions.stream()
            .filter(auction -> auction.getItem().getType().name().toLowerCase().contains(query.toLowerCase()))
            .toList();
    }
    
    /**
     * Startet Auktion-Cleanup
     */
    private void startAuctionCleanup() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            List<Auction> expiredAuctions = activeAuctions.stream()
                .filter(Auction::isExpired)
                .toList();
            
            for (Auction auction : expiredAuctions) {
                Bid highestBid = auction.getHighestBid();
                if (highestBid != null) {
                    completeAuction(auction, highestBid.getBidderId(), highestBid.getAmount());
                } else {
                    // Kein Gebot - gib Item an Verkäufer zurück
                    Player seller = plugin.getServer().getPlayer(auction.getSellerId());
                    if (seller != null) {
                        seller.getInventory().addItem(auction.getItem());
                        seller.sendMessage("§eDeine Auktion ist abgelaufen ohne Gebote!");
                    }
                    activeAuctions.remove(auction);
                }
            }
        }, 20L, 20L); // Jede Sekunde
    }
}
