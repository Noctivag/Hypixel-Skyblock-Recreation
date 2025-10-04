package de.noctivag.plugin.economy;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Auction House - Hypixel Skyblock Style
 */
public class AdvancedAuctionHouse implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, Auction> auctions = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> auctionTasks = new ConcurrentHashMap<>();
    
    public AdvancedAuctionHouse(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        startAuctionUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void startAuctionUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, Auction> entry : auctions.entrySet()) {
                    Auction auction = entry.getValue();
                    auction.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ENCHANTING_TABLE) {
            openAuctionGUI(player);
        }
    }
    
    private void openAuctionGUI(Player player) {
        player.sendMessage("§aAuction House GUI geöffnet!");
    }
    
    public void createAuction(Player player, ItemStack item, double startingBid, double binPrice) {
        UUID auctionId = UUID.randomUUID();
        Auction auction = new Auction(auctionId, player.getUniqueId(), item, startingBid, binPrice);
        auctions.put(auctionId, auction);
        
        player.sendMessage("§aAuktion erstellt!");
    }
    
    public void bidOnAuction(Player player, UUID auctionId, double bidAmount) {
        Auction auction = auctions.get(auctionId);
        if (auction == null) {
            player.sendMessage("§cAuktion nicht gefunden!");
            return;
        }
        
        if (auction.bid(player.getUniqueId(), bidAmount)) {
            player.sendMessage("§aGebot abgegeben!");
        } else {
            player.sendMessage("§cGebot zu niedrig!");
        }
    }
    
    public void buyNow(Player player, UUID auctionId) {
        Auction auction = auctions.get(auctionId);
        if (auction == null) {
            player.sendMessage("§cAuktion nicht gefunden!");
            return;
        }
        
        if (auction.buyNow(player.getUniqueId())) {
            player.sendMessage("§aItem gekauft!");
            auctions.remove(auctionId);
        } else {
            player.sendMessage("§cNicht genug Geld!");
        }
    }
    
    public List<Auction> getActiveAuctions() {
        return new ArrayList<>(auctions.values());
    }
    
    public static class Auction {
        private final UUID id;
        private final UUID seller;
        private final ItemStack item;
        private final double startingBid;
        private final double binPrice;
        private double currentBid;
        private UUID highestBidder;
        private final long endTime;
        private boolean isSold;
        private boolean isCancelled;
        private long lastUpdate;
        
        public Auction(UUID id, UUID seller, ItemStack item, double startingBid, double binPrice) {
            this.id = id;
            this.seller = seller;
            this.item = item;
            this.startingBid = startingBid;
            this.binPrice = binPrice;
            this.currentBid = startingBid;
            this.endTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000); // 24 hours
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;
            
            if (timeDiff >= 60000) {
                saveToDatabase();
                lastUpdate = currentTime;
            }
            
            if (currentTime >= endTime && !isSold && !isCancelled) {
                endAuction();
            }
        }
        
        private void saveToDatabase() {
            // Save auction data to database
        }
        
        private void endAuction() {
            if (highestBidder != null) {
                // Give item to highest bidder
                // Give money to seller
                isSold = true;
            } else {
                // Return item to seller
                isCancelled = true;
            }
        }
        
        public boolean bid(UUID bidder, double bidAmount) {
            if (bidAmount > currentBid) {
                currentBid = bidAmount;
                highestBidder = bidder;
                return true;
            }
            return false;
        }
        
        public boolean buyNow(UUID buyer) {
            if (binPrice > 0) {
                // Check if buyer has enough money
                // Give item to buyer
                // Give money to seller
                isSold = true;
                return true;
            }
            return false;
        }
        
        public UUID getId() { return id; }
        public UUID getSeller() { return seller; }
        public ItemStack getItem() { return item; }
        public double getStartingBid() { return startingBid; }
        public double getBinPrice() { return binPrice; }
        public double getCurrentBid() { return currentBid; }
        public UUID getHighestBidder() { return highestBidder; }
        public long getEndTime() { return endTime; }
        public boolean isSold() { return isSold; }
        public boolean isCancelled() { return isCancelled; }
    }
}
