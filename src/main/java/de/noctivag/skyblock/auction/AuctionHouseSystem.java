package de.noctivag.skyblock.auction;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.CorePlatform;
import de.noctivag.skyblock.core.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Auction House System - Hypixel Skyblock Style
 * 
 * Features:
 * - Create auctions for items
 * - Bid on auctions
 * - Auction categories and filtering
 * - Auction history and statistics
 * - Auction GUI interface
 * - Automatic auction completion
 */
public class AuctionHouseSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final CorePlatform corePlatform;
    private final Map<UUID, List<Auction>> playerAuctions = new ConcurrentHashMap<>();
    private final Map<UUID, List<Auction>> playerBids = new ConcurrentHashMap<>();
    private final List<Auction> activeAuctions = new ArrayList<>();
    private final Map<UUID, AuctionHouseStats> playerStats = new ConcurrentHashMap<>();
    
    public AuctionHouseSystem(SkyblockPlugin SkyblockPlugin, CorePlatform corePlatform) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.corePlatform = corePlatform;
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
        startAuctionUpdateTask();
    }
    
    private void startAuctionUpdateTask() {
        Bukkit.getScheduler().runTaskTimer(SkyblockPlugin, () -> {
            long currentTime = java.lang.System.currentTimeMillis();
            Iterator<Auction> iterator = activeAuctions.iterator();
            
            while (iterator.hasNext()) {
                Auction auction = iterator.next();
                if (currentTime >= auction.getEndTime()) {
                    completeAuction(auction);
                    iterator.remove();
                }
            }
        }, 0L, 20L * 60L); // Check every minute
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        if (title.contains("Auction House")) {
            event.setCancelled(true);
            handleAuctionGUIClick(player, event.getSlot());
        }
    }
    
    public void openAuctionHouseGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lAuction House"));
        
        // Main categories
        addGUIItem(gui, 10, Material.DIAMOND_SWORD, "§c§lWeapons", 
            Arrays.asList("§7Browse weapon auctions", "", "§eClick to view"));
        addGUIItem(gui, 11, Material.IRON_CHESTPLATE, "§b§lArmor", 
            Arrays.asList("§7Browse armor auctions", "", "§eClick to view"));
        addGUIItem(gui, 12, Material.DIAMOND_PICKAXE, "§7§lTools", 
            Arrays.asList("§7Browse tool auctions", "", "§eClick to view"));
        addGUIItem(gui, 13, Material.GOLD_INGOT, "§6§lAccessories", 
            Arrays.asList("§7Browse accessory auctions", "", "§eClick to view"));
        addGUIItem(gui, 14, Material.WHEAT, "§a§lMaterials", 
            Arrays.asList("§7Browse material auctions", "", "§eClick to view"));
        
        // My auctions and bids
        addGUIItem(gui, 19, Material.CHEST, "§e§lMy Auctions", 
            Arrays.asList("§7View your active auctions", "", "§eClick to view"));
        addGUIItem(gui, 20, Material.EMERALD, "§a§lMy Bids", 
            Arrays.asList("§7View your active bids", "", "§eClick to view"));
        addGUIItem(gui, 21, Material.BOOK, "§9§lAuction History", 
            Arrays.asList("§7View your auction history", "", "§eClick to view"));
        
        // Create auction
        addGUIItem(gui, 22, Material.ANVIL, "§d§lCreate Auction", 
            Arrays.asList("§7Create a new auction", "", "§eClick to create"));
        
        // Statistics
        AuctionHouseStats stats = getPlayerStats(player.getUniqueId());
        addGUIItem(gui, 28, Material.GOLD_INGOT, "§6§lMy Statistics", 
            Arrays.asList("§7Auctions Created: §e" + stats.getAuctionsCreated(),
                         "§7Auctions Won: §e" + stats.getAuctionsWon(),
                         "§7Total Spent: §6" + stats.getTotalSpent() + " coins",
                         "§7Total Earned: §6" + stats.getTotalEarned() + " coins", "", "§eClick to view"));
        
        // Close button
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", 
            Arrays.asList("§7Close the auction house", "", "§eClick to close"));
        
        player.openInventory(gui);
    }
    
    public void openCategoryGUI(Player player, AuctionCategory category) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lAuction House - " + category.getDisplayName());
        
        List<Auction> categoryAuctions = getAuctionsByCategory(category);
        int slot = 10;
        
        for (Auction auction : categoryAuctions) {
            if (slot >= 44) break;
            
            ItemStack auctionItem = createAuctionItem(auction);
            gui.setItem(slot, auctionItem);
            slot++;
            if (slot % 9 == 8) slot += 2; // Skip to next row
        }
        
        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§l← Back", 
            Arrays.asList("§7Return to main auction house", "", "§eClick to go back"));
        
        player.openInventory(gui);
    }
    
    public void openMyAuctionsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§e§lMy Auctions"));
        
        List<Auction> myAuctions = playerAuctions.getOrDefault(player.getUniqueId(), new ArrayList<>());
        int slot = 10;
        
        for (Auction auction : myAuctions) {
            if (slot >= 44) break;
            
            ItemStack auctionItem = createAuctionItem(auction);
            gui.setItem(slot, auctionItem);
            slot++;
            if (slot % 9 == 8) slot += 2;
        }
        
        addGUIItem(gui, 45, Material.ARROW, "§7§l← Back", 
            Arrays.asList("§7Return to main auction house", "", "§eClick to go back"));
        
        player.openInventory(gui);
    }
    
    public boolean createAuction(Player player, ItemStack item, double startingBid, double buyoutPrice, int durationHours) {
        PlayerProfile profile = corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null) return false;
        
        // Check if player has the item
        if (!player.getInventory().containsAtLeast(item, item.getAmount())) {
            player.sendMessage(Component.text("§cYou don't have this item!"));
            return false;
        }
        
        // Check minimum bid
        if (startingBid < 1) {
            player.sendMessage(Component.text("§cStarting bid must be at least 1 coin!"));
            return false;
        }
        
        // Create auction
        Auction auction = new Auction(
            UUID.randomUUID(),
            player.getUniqueId(),
            item.clone(),
            startingBid,
            buyoutPrice,
            java.lang.System.currentTimeMillis() + (durationHours * 60 * 60 * 1000L),
            AuctionCategory.getCategoryForItem(item)
        );
        
        // Add to active auctions
        activeAuctions.add(auction);
        
        // Add to player's auctions
        playerAuctions.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>()).add(auction);
        
        // Remove item from player
        player.getInventory().removeItem(item);
        
        // Update statistics
        AuctionHouseStats stats = getPlayerStats(player.getUniqueId());
        stats.incrementAuctionsCreated();
        
        player.sendMessage(Component.text("§a§lAUCTION CREATED!"));
        player.sendMessage("§7Item: §e" + item.getType().name());
        player.sendMessage("§7Starting Bid: §6" + startingBid + " coins");
        if (buyoutPrice > 0) {
            player.sendMessage("§7Buyout Price: §6" + buyoutPrice + " coins");
        }
        player.sendMessage("§7Duration: §e" + durationHours + " hours");
        
        return true;
    }
    
    public boolean placeBid(Player player, Auction auction, double bidAmount) {
        PlayerProfile profile = corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null) return false;
        
        // Check if player has enough coins
        if (!profile.tryRemoveCoins(bidAmount)) {
            player.sendMessage(Component.text("§cYou don't have enough coins!"));
            return false;
        }
        
        // Check if bid is higher than current bid
        if (bidAmount <= auction.getCurrentBid()) {
            player.sendMessage(Component.text("§cYour bid must be higher than the current bid!"));
            profile.addCoins(bidAmount); // Refund
            return false;
        }
        
        // Refund previous bidder if any
        if (auction.getCurrentBidder() != null) {
            PlayerProfile previousBidder = corePlatform.getPlayerProfile(auction.getCurrentBidder());
            if (previousBidder != null) {
                previousBidder.addCoins(auction.getCurrentBid());
            }
        }
        
        // Update auction
        auction.setCurrentBid(bidAmount);
        auction.setCurrentBidder(player.getUniqueId());
        
        // Add to player's bids
        playerBids.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>()).add(auction);
        
        player.sendMessage(Component.text("§a§lBID PLACED!"));
        player.sendMessage("§7Auction: §e" + auction.getItem().getType().name());
        player.sendMessage("§7Bid: §6" + bidAmount + " coins");
        
        return true;
    }
    
    public boolean buyoutAuction(Player player, Auction auction) {
        if (auction.getBuyoutPrice() <= 0) {
            player.sendMessage(Component.text("§cThis auction doesn't have a buyout price!"));
            return false;
        }
        
        PlayerProfile profile = corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null) return false;
        
        // Check if player has enough coins
        if (!profile.tryRemoveCoins(auction.getBuyoutPrice())) {
            player.sendMessage(Component.text("§cYou don't have enough coins!"));
            return false;
        }
        
        // Complete auction immediately
        completeAuction(auction, player.getUniqueId(), auction.getBuyoutPrice());
        
        player.sendMessage(Component.text("§a§lAUCTION BOUGHT OUT!"));
        player.sendMessage("§7Item: §e" + auction.getItem().getType().name());
        player.sendMessage("§7Price: §6" + auction.getBuyoutPrice() + " coins");
        
        return true;
    }
    
    private void completeAuction(Auction auction) {
        completeAuction(auction, auction.getCurrentBidder(), auction.getCurrentBid());
    }
    
    private void completeAuction(Auction auction, UUID winnerId, double finalPrice) {
        // Give item to winner
        if (winnerId != null) {
            Player winner = Bukkit.getPlayer(winnerId);
            if (winner != null) {
                winner.getInventory().addItem(auction.getItem());
                winner.sendMessage("§a§lAUCTION WON!");
                winner.sendMessage("§7Item: §e" + auction.getItem().getType().name());
                winner.sendMessage("§7Price: §6" + finalPrice + " coins");
                
                // Update statistics
                AuctionHouseStats stats = getPlayerStats(winnerId);
                stats.incrementAuctionsWon();
                stats.addTotalSpent(finalPrice);
            }
        } else {
            // No winner, return item to seller
            Player seller = Bukkit.getPlayer(auction.getSellerId());
            if (seller != null) {
                seller.getInventory().addItem(auction.getItem());
                seller.sendMessage("§e§lAUCTION EXPIRED!");
                seller.sendMessage("§7Item: §e" + auction.getItem().getType().name());
                seller.sendMessage("§7Your item has been returned.");
            }
        }
        
        // Pay seller
        PlayerProfile sellerProfile = corePlatform.getPlayerProfile(auction.getSellerId());
        if (sellerProfile != null && finalPrice > 0) {
            sellerProfile.addCoins(finalPrice);
            
            // Update statistics
            AuctionHouseStats stats = getPlayerStats(auction.getSellerId());
            stats.addTotalEarned(finalPrice);
        }
        
        // Remove from active auctions
        activeAuctions.remove(auction);
        
        // Remove from player auctions
        List<Auction> sellerAuctions = playerAuctions.get(auction.getSellerId());
        if (sellerAuctions != null) {
            sellerAuctions.remove(auction);
        }
        
        // Remove from player bids
        if (winnerId != null) {
            List<Auction> winnerBids = playerBids.get(winnerId);
            if (winnerBids != null) {
                winnerBids.remove(auction);
            }
        }
    }
    
    private List<Auction> getAuctionsByCategory(AuctionCategory category) {
        return activeAuctions.stream()
            .filter(auction -> auction.getCategory() == category)
            .toList();
    }
    
    private ItemStack createAuctionItem(Auction auction) {
        ItemStack item = auction.getItem().clone();
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            List<String> lore = meta.lore() != null ? 
                meta.lore().stream().map(Component::toString).collect(java.util.stream.Collectors.toList()) : 
                new ArrayList<>();
            if (lore == null) lore = new ArrayList<>();
            
            lore.add("");
            lore.add("§7Seller: §e" + Bukkit.getOfflinePlayer(auction.getSellerId()).getName());
            lore.add("§7Current Bid: §6" + auction.getCurrentBid() + " coins");
            if (auction.getBuyoutPrice() > 0) {
                lore.add("§7Buyout: §6" + auction.getBuyoutPrice() + " coins");
            }
            lore.add("§7Time Left: §e" + formatTimeLeft(auction.getEndTime()));
            lore.add("");
            lore.add("§eLeft-click to bid!");
            if (auction.getBuyoutPrice() > 0) {
                lore.add("§aRight-click to buyout!");
            }
            
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    private String formatTimeLeft(long endTime) {
        long timeLeft = endTime - java.lang.System.currentTimeMillis();
        if (timeLeft <= 0) return "Expired";
        
        long hours = timeLeft / (1000 * 60 * 60);
        long minutes = (timeLeft % (1000 * 60 * 60)) / (1000 * 60);
        
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }
    
    private void handleAuctionGUIClick(Player player, int slot) {
        switch (slot) {
            case 10: // Weapons
                openCategoryGUI(player, AuctionCategory.WEAPONS);
                break;
            case 11: // Armor
                openCategoryGUI(player, AuctionCategory.ARMOR);
                break;
            case 12: // Tools
                openCategoryGUI(player, AuctionCategory.TOOLS);
                break;
            case 13: // Accessories
                openCategoryGUI(player, AuctionCategory.ACCESSORIES);
                break;
            case 14: // Materials
                openCategoryGUI(player, AuctionCategory.MATERIALS);
                break;
            case 19: // My Auctions
                openMyAuctionsGUI(player);
                break;
            case 20: // My Bids
                player.sendMessage(Component.text("§eMy Bids coming soon!"));
                break;
            case 21: // Auction History
                player.sendMessage(Component.text("§eAuction History coming soon!"));
                break;
            case 22: // Create Auction
                player.sendMessage(Component.text("§eCreate Auction coming soon!"));
                break;
            case 28: // Statistics
                player.sendMessage(Component.text("§eStatistics coming soon!"));
                break;
            case 49: // Close
                player.closeInventory();
                break;
        }
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, List<String> description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(description.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public AuctionHouseStats getPlayerStats(UUID playerId) {
        return playerStats.computeIfAbsent(playerId, k -> new AuctionHouseStats(playerId));
    }
    
    // Auction Classes
    public static class Auction {
        private final UUID id;
        private final UUID sellerId;
        private final ItemStack item;
        private final double startingBid;
        private final double buyoutPrice;
        private final long endTime;
        private final AuctionCategory category;
        private double currentBid;
        private UUID currentBidder;
        
        public Auction(UUID id, UUID sellerId, ItemStack item, double startingBid, double buyoutPrice, 
                      long endTime, AuctionCategory category) {
            this.id = id;
            this.sellerId = sellerId;
            this.item = item;
            this.startingBid = startingBid;
            this.buyoutPrice = buyoutPrice;
            this.endTime = endTime;
            this.category = category;
            this.currentBid = startingBid;
            this.currentBidder = null;
        }
        
        // Getters and Setters
        public UUID getId() { return id; }
        public UUID getSellerId() { return sellerId; }
        public ItemStack getItem() { return item; }
        public double getStartingBid() { return startingBid; }
        public double getBuyoutPrice() { return buyoutPrice; }
        public long getEndTime() { return endTime; }
        public AuctionCategory getCategory() { return category; }
        public double getCurrentBid() { return currentBid; }
        public void setCurrentBid(double currentBid) { this.currentBid = currentBid; }
        public UUID getCurrentBidder() { return currentBidder; }
        public void setCurrentBidder(UUID currentBidder) { this.currentBidder = currentBidder; }
    }
    
    public enum AuctionCategory {
        WEAPONS("§cWeapons", Material.DIAMOND_SWORD),
        ARMOR("§bArmor", Material.IRON_CHESTPLATE),
        TOOLS("§7Tools", Material.DIAMOND_PICKAXE),
        ACCESSORIES("§6Accessories", Material.GOLD_INGOT),
        MATERIALS("§aMaterials", Material.WHEAT),
        PETS("§dPets", Material.WOLF_SPAWN_EGG),
        MISC("§fMiscellaneous", Material.CHEST);
        
        private final String displayName;
        private final Material icon;
        
        AuctionCategory(String displayName, Material icon) {
            this.displayName = displayName;
            this.icon = icon;
        }
        
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        
        public static AuctionCategory getCategoryForItem(ItemStack item) {
            Material material = item.getType();
            String materialName = material.name();
            
            if (materialName.contains("SWORD") || materialName.contains("BOW") || materialName.contains("AXE")) {
                return WEAPONS;
            } else if (materialName.contains("HELMET") || materialName.contains("CHESTPLATE") || 
                      materialName.contains("LEGGINGS") || materialName.contains("BOOTS")) {
                return ARMOR;
            } else if (materialName.contains("PICKAXE") || materialName.contains("SHOVEL") || 
                      materialName.contains("HOE")) {
                return TOOLS;
            } else if (materialName.contains("SPAWN_EGG")) {
                return PETS;
            } else {
                return MATERIALS;
            }
        }
    }
    
    public static class AuctionHouseStats {
        private final UUID playerId;
        private int auctionsCreated;
        private int auctionsWon;
        private double totalSpent;
        private double totalEarned;
        
        public AuctionHouseStats(UUID playerId) {
            this.playerId = playerId;
            this.auctionsCreated = 0;
            this.auctionsWon = 0;
            this.totalSpent = 0.0;
            this.totalEarned = 0.0;
        }
        
        public void incrementAuctionsCreated() { this.auctionsCreated++; }
        public void incrementAuctionsWon() { this.auctionsWon++; }
        public void addTotalSpent(double amount) { this.totalSpent += amount; }
        public void addTotalEarned(double amount) { this.totalEarned += amount; }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public int getAuctionsCreated() { return auctionsCreated; }
        public int getAuctionsWon() { return auctionsWon; }
        public double getTotalSpent() { return totalSpent; }
        public double getTotalEarned() { return totalEarned; }
    }
}
