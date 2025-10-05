package de.noctivag.skyblock.skyblock.auction;

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
 * Comprehensive Auction House System inspired by Hypixel Skyblock
 * Features:
 * - Item selling and buying
 * - Bidding system
 * - Auction categories and filters
 * - Search functionality
 * - Auction history
 * - BIN (Buy It Now) and auction formats
 * - Auction fees and taxes
 * - Expired auction handling
 */
public class AuctionHouseSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerAuctionData> playerAuctionData = new ConcurrentHashMap<>();
    private final Map<String, Auction> activeAuctions = new ConcurrentHashMap<>();
    private final Map<String, Auction> expiredAuctions = new ConcurrentHashMap<>();
    private final Map<UUID, List<Auction>> playerAuctions = new ConcurrentHashMap<>();
    
    public AuctionHouseSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
        startAuctionUpdateTask();
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        if (!title.contains("Auction House")) return;
        
        event.setCancelled(true);
        
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;
        
        String displayName = clicked.getItemMeta().getDisplayName();
        
        // Handle auction house clicks
        handleAuctionHouseClick(player, displayName, event.getSlot());
    }
    
    private void startAuctionUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateAuctions();
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L * 60L); // Every minute
    }
    
    private void updateAuctions() {
        // Check for expired auctions
        List<String> expiredAuctionIds = new ArrayList<>();
        
        for (Map.Entry<String, Auction> entry : activeAuctions.entrySet()) {
            Auction auction = entry.getValue();
            
            if (auction.isExpired()) {
                expiredAuctionIds.add(entry.getKey());
                handleExpiredAuction(auction);
            }
        }
        
        // Remove expired auctions
        for (String auctionId : expiredAuctionIds) {
            Auction auction = activeAuctions.remove(auctionId);
            if (auction != null) {
                expiredAuctions.put(auctionId, auction);
            }
        }
    }
    
    private void handleExpiredAuction(Auction auction) {
        // Return item to seller if no bids
        if (auction.getBids().isEmpty()) {
            returnItemToSeller(auction);
        } else {
            // Complete auction with highest bid
            completeAuction(auction);
        }
    }
    
    private void returnItemToSeller(Auction auction) {
        Player seller = Bukkit.getPlayer(auction.getSellerId());
        if (seller != null) {
            seller.getInventory().addItem(auction.getItem());
            seller.sendMessage("§eYour auction for " + auction.getItem().getItemMeta().getDisplayName() + " expired and was returned.");
        }
    }
    
    private void completeAuction(Auction auction) {
        // Get highest bid
        Bid highestBid = auction.getHighestBid();
        if (highestBid == null) return;
        
        Player seller = Bukkit.getPlayer(auction.getSellerId());
        Player buyer = Bukkit.getPlayer(highestBid.getBidderId());
        
        if (seller != null && buyer != null) {
            // Give item to buyer
            buyer.getInventory().addItem(auction.getItem());
            
            // Give coins to seller (minus auction fee)
            double finalPrice = highestBid.getAmount() * (1.0 - getAuctionFee());
            // Add coins to seller
            
            // Send messages
            seller.sendMessage("§aYour auction sold for §6" + String.format("%.0f", finalPrice) + " coins!");
            buyer.sendMessage("§aYou won the auction for " + auction.getItem().getItemMeta().getDisplayName() + "!");
        }
    }
    
    private double getAuctionFee() {
        return 0.05; // 5% auction fee
    }
    
    public void openAuctionHouse(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lAuction House"));
        
        // Add categories
        addAuctionCategory(gui, 10, Material.DIAMOND_SWORD, "§c§lWeapons", "§7Swords, bows, and other weapons");
        addAuctionCategory(gui, 11, Material.DIAMOND_CHESTPLATE, "§b§lArmor", "§7Helmets, chestplates, and more");
        addAuctionCategory(gui, 12, Material.ENCHANTED_BOOK, "§d§lAccessories", "§7Talismans, rings, and artifacts");
        addAuctionCategory(gui, 13, Material.EMERALD, "§a§lTools", "§7Pickaxes, axes, and other tools");
        addAuctionCategory(gui, 14, Material.POTION, "§5§lConsumables", "§7Potions, food, and other consumables");
        addAuctionCategory(gui, 15, Material.BOOK, "§e§lMisc", "§7Other items and materials");
        
        // Add search and filters
        addAuctionItem(gui, 19, Material.COMPASS, "§b§lSearch", Arrays.asList("§7Search for specific items"));
        addAuctionItem(gui, 20, Material.HOPPER, "§7§lFilters", Arrays.asList("§7Filter auctions by criteria"));
        addAuctionItem(gui, 21, Material.CLOCK, "§e§lSort by Time", Arrays.asList("§7Sort by time remaining"));
        addAuctionItem(gui, 22, Material.GOLD_INGOT, "§6§lSort by Price", Arrays.asList("§7Sort by current bid"));
        
        // Add player actions
        addAuctionItem(gui, 28, Material.CHEST, "§a§lMy Auctions", Arrays.asList("§7View your active auctions"));
        addAuctionItem(gui, 29, Material.GOLD_INGOT, "§6§lMy Bids", Arrays.asList("§7View your active bids"));
        addAuctionItem(gui, 30, Material.BOOK, "§e§lAuction History", Arrays.asList("§7View your auction history"));
        addAuctionItem(gui, 31, Material.ANVIL, "§7§lCreate Auction", Arrays.asList("§7Create a new auction"));
        
        // Add navigation
        addAuctionItem(gui, 45, Material.ARROW, "§7§lPrevious Page", Arrays.asList("§7Go to previous page"));
        addAuctionItem(gui, 49, Material.BARRIER, "§c§lClose", Arrays.asList("§7Close the auction house"));
        addAuctionItem(gui, 53, Material.ARROW, "§7§lNext Page", Arrays.asList("§7Go to next page"));
        
        player.openInventory(gui);
    }
    
    public void createAuction(Player player, ItemStack item, double startingBid, double binPrice, int durationHours) {
        UUID playerId = player.getUniqueId();
        
        // Check if player has the item
        if (!player.getInventory().containsAtLeast(item, item.getAmount())) {
            player.sendMessage(Component.text("§cYou don't have this item!"));
            return;
        }
        
        // Remove item from player
        player.getInventory().removeItem(item);
        
        // Create auction
        Auction auction = new Auction(playerId, item, startingBid, binPrice, durationHours);
        activeAuctions.put(auction.getId(), auction);
        
        // Add to player auctions
        playerAuctions.computeIfAbsent(playerId, k -> new ArrayList<>()).add(auction);
        
        player.sendMessage("§aCreated auction for " + item.getItemMeta().getDisplayName() + "!");
        player.sendMessage("§7Starting bid: §6" + String.format("%.0f", startingBid) + " coins");
        if (binPrice > 0) {
            player.sendMessage("§7BIN price: §6" + String.format("%.0f", binPrice) + " coins");
        }
    }
    
    public void placeBid(Player player, String auctionId, double bidAmount) {
        Auction auction = activeAuctions.get(auctionId);
        if (auction == null) {
            player.sendMessage(Component.text("§cAuction not found!"));
            return;
        }
        
        if (auction.isExpired()) {
            player.sendMessage(Component.text("§cThis auction has expired!"));
            return;
        }
        
        // Check if player has enough coins
        // This would check player's coin balance
        
        // Check if bid is higher than current highest bid
        Bid currentHighest = auction.getHighestBid();
        if (currentHighest != null && bidAmount <= currentHighest.getAmount()) {
            player.sendMessage(Component.text("§cYour bid must be higher than the current highest bid!"));
            return;
        }
        
        // Place bid
        Bid bid = new Bid(player.getUniqueId(), bidAmount, java.lang.System.currentTimeMillis());
        auction.addBid(bid);
        
        player.sendMessage("§aBid placed: §6" + String.format("%.0f", bidAmount) + " coins");
        
        // Notify seller if online
        Player seller = Bukkit.getPlayer(auction.getSellerId());
        if (seller != null) {
            seller.sendMessage("§eSomeone bid on your auction: §6" + String.format("%.0f", bidAmount) + " coins");
        }
    }
    
    public void buyItNow(Player player, String auctionId) {
        Auction auction = activeAuctions.get(auctionId);
        if (auction == null) {
            player.sendMessage(Component.text("§cAuction not found!"));
            return;
        }
        
        if (auction.getBinPrice() <= 0) {
            player.sendMessage(Component.text("§cThis auction doesn't have a BIN price!"));
            return;
        }
        
        if (auction.isExpired()) {
            player.sendMessage(Component.text("§cThis auction has expired!"));
            return;
        }
        
        // Check if player has enough coins
        // This would check player's coin balance
        
        // Complete BIN purchase
        completeBinPurchase(auction, player);
    }
    
    private void completeBinPurchase(Auction auction, Player buyer) {
        Player seller = Bukkit.getPlayer(auction.getSellerId());
        
        if (seller != null) {
            // Give item to buyer
            buyer.getInventory().addItem(auction.getItem());
            
            // Give coins to seller (minus auction fee)
            double finalPrice = auction.getBinPrice() * (1.0 - getAuctionFee());
            // Add coins to seller
            
            // Remove auction
            activeAuctions.remove(auction.getId());
            
            // Send messages
            seller.sendMessage("§aYour auction sold for §6" + String.format("%.0f", finalPrice) + " coins!");
            buyer.sendMessage("§aYou bought " + auction.getItem().getItemMeta().getDisplayName() + " for §6" + String.format("%.0f", auction.getBinPrice()) + " coins!");
        }
    }
    
    private void handleAuctionHouseClick(Player player, String displayName, int slot) {
        if (displayName.contains("Weapons")) {
            openAuctionCategory(player, AuctionCategory.WEAPONS);
        } else if (displayName.contains("Armor")) {
            openAuctionCategory(player, AuctionCategory.ARMOR);
        } else if (displayName.contains("Accessories")) {
            openAuctionCategory(player, AuctionCategory.ACCESSORIES);
        } else if (displayName.contains("Tools")) {
            openAuctionCategory(player, AuctionCategory.TOOLS);
        } else if (displayName.contains("Consumables")) {
            openAuctionCategory(player, AuctionCategory.CONSUMABLES);
        } else if (displayName.contains("Misc")) {
            openAuctionCategory(player, AuctionCategory.MISC);
        } else if (displayName.contains("My Auctions")) {
            openPlayerAuctions(player);
        } else if (displayName.contains("My Bids")) {
            openPlayerBids(player);
        } else if (displayName.contains("Create Auction")) {
            openCreateAuction(player);
        } else if (displayName.contains("Close")) {
            player.closeInventory();
        }
    }
    
    private void openAuctionCategory(Player player, AuctionCategory category) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lAuction House - " + category.getDisplayName()));
        
        // Add auctions in this category
        List<Auction> categoryAuctions = getAuctionsByCategory(category);
        
        int slot = 0;
        for (Auction auction : categoryAuctions) {
            if (slot >= 45) break; // Don't exceed inventory size
            
            addAuctionItem(gui, slot, auction);
            slot++;
        }
        
        // Add navigation
        addAuctionItem(gui, 45, Material.ARROW, "§7§lBack", Arrays.asList("§7Return to main menu"));
        addAuctionItem(gui, 49, Material.BARRIER, "§c§lClose", Arrays.asList("§7Close the auction house"));
        
        player.openInventory(gui);
    }
    
    private void openPlayerAuctions(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§a§lMy Auctions"));
        
        List<Auction> playerAuctionList = playerAuctions.get(player.getUniqueId());
        if (playerAuctionList != null) {
            int slot = 0;
            for (Auction auction : playerAuctionList) {
                if (slot >= 45) break;
                
                addAuctionItem(gui, slot, auction);
                slot++;
            }
        }
        
        // Add navigation
        addAuctionItem(gui, 45, Material.ARROW, "§7§lBack", Arrays.asList("§7Return to main menu"));
        addAuctionItem(gui, 49, Material.BARRIER, "§c§lClose", Arrays.asList("§7Close the auction house"));
        
        player.openInventory(gui);
    }
    
    private void openPlayerBids(Player player) {
        // Open player's active bids
        player.sendMessage(Component.text("§eOpening your active bids..."));
    }
    
    private void openCreateAuction(Player player) {
        // Open create auction interface
        player.sendMessage(Component.text("§eOpening create auction interface..."));
    }
    
    private List<Auction> getAuctionsByCategory(AuctionCategory category) {
        List<Auction> categoryAuctions = new ArrayList<>();
        
        for (Auction auction : activeAuctions.values()) {
            if (getItemCategory(auction.getItem()) == category) {
                categoryAuctions.add(auction);
            }
        }
        
        // Sort by time remaining
        categoryAuctions.sort((a, b) -> Long.compare(a.getExpirationTime(), b.getExpirationTime()));
        
        return categoryAuctions;
    }
    
    private AuctionCategory getItemCategory(ItemStack item) {
        Material material = item.getType();
        
        if (material.name().contains("SWORD") || material.name().contains("BOW") || material.name().contains("AXE")) {
            return AuctionCategory.WEAPONS;
        } else if (material.name().contains("HELMET") || material.name().contains("CHESTPLATE") || 
                   material.name().contains("LEGGINGS") || material.name().contains("BOOTS")) {
            return AuctionCategory.ARMOR;
        } else if (material.name().contains("PICKAXE") || material.name().contains("SHOVEL") || 
                   material.name().contains("HOE") || material.name().contains("SHEARS")) {
            return AuctionCategory.TOOLS;
        } else if (material == Material.POTION || material == Material.GOLDEN_APPLE) {
            return AuctionCategory.CONSUMABLES;
        } else if (material == Material.ENCHANTED_BOOK || material == Material.EMERALD) {
            return AuctionCategory.ACCESSORIES;
        } else {
            return AuctionCategory.MISC;
        }
    }
    
    private void addAuctionCategory(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(Component.text(description), Component.text(""), Component.text("§eClick to browse!")));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    private void addAuctionItem(Inventory gui, int slot, Material material, String name, List<String> lore) {
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
    
    private void addAuctionItem(Inventory gui, int slot, Auction auction) {
        ItemStack item = auction.getItem().clone();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lore = new ArrayList<>();
            lore.add("§7Seller: §e" + Bukkit.getOfflinePlayer(auction.getSellerId()).getName());
            lore.add("§7Starting bid: §6" + String.format("%.0f", auction.getStartingBid()) + " coins");
            
            Bid highestBid = auction.getHighestBid();
            if (highestBid != null) {
                lore.add("§7Current bid: §6" + String.format("%.0f", highestBid.getAmount()) + " coins");
            } else {
                lore.add("§7Current bid: §7No bids");
            }
            
            if (auction.getBinPrice() > 0) {
                lore.add("§7BIN price: §6" + String.format("%.0f", auction.getBinPrice()) + " coins");
            }
            
            lore.add("§7Time left: §e" + formatTimeRemaining(auction.getExpirationTime()));
            lore.add("");
            lore.add("§eClick to bid!");
            if (auction.getBinPrice() > 0) {
                lore.add("§aShift-click to buy now!");
            }
            
            meta.lore(lore.stream().toList());
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    private String formatTimeRemaining(long expirationTime) {
        long timeLeft = expirationTime - java.lang.System.currentTimeMillis();
        if (timeLeft <= 0) return "§cExpired";
        
        long hours = timeLeft / (1000 * 60 * 60);
        long minutes = (timeLeft % (1000 * 60 * 60)) / (1000 * 60);
        
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }
    
    public enum AuctionCategory {
        WEAPONS("§cWeapons"),
        ARMOR("§bArmor"),
        ACCESSORIES("§dAccessories"),
        TOOLS("§aTools"),
        CONSUMABLES("§5Consumables"),
        MISC("§eMisc");
        
        private final String displayName;
        
        AuctionCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() { return displayName; }
    }
    
    public static class Auction {
        private final String id;
        private final UUID sellerId;
        private final ItemStack item;
        private final double startingBid;
        private final double binPrice;
        private final long expirationTime;
        private final List<Bid> bids;
        
        public Auction(UUID sellerId, ItemStack item, double startingBid, double binPrice, int durationHours) {
            this.id = UUID.randomUUID().toString();
            this.sellerId = sellerId;
            this.item = item;
            this.startingBid = startingBid;
            this.binPrice = binPrice;
            this.expirationTime = java.lang.System.currentTimeMillis() + (durationHours * 60 * 60 * 1000L);
            this.bids = new ArrayList<>();
        }
        
        public void addBid(Bid bid) {
            bids.add(bid);
        }
        
        public boolean isExpired() {
            return java.lang.System.currentTimeMillis() >= expirationTime;
        }
        
        public Bid getHighestBid() {
            return bids.stream().max(Comparator.comparing(Bid::getAmount)).orElse(null);
        }
        
        // Getters
        public String getId() { return id; }
        public UUID getSellerId() { return sellerId; }
        public ItemStack getItem() { return item; }
        public double getStartingBid() { return startingBid; }
        public double getBinPrice() { return binPrice; }
        public long getExpirationTime() { return expirationTime; }
        public List<Bid> getBids() { return bids; }
    }
    
    public static class Bid {
        private final UUID bidderId;
        private final double amount;
        private final long timestamp;
        
        public Bid(UUID bidderId, double amount, long timestamp) {
            this.bidderId = bidderId;
            this.amount = amount;
            this.timestamp = timestamp;
        }
        
        // Getters
        public UUID getBidderId() { return bidderId; }
        public double getAmount() { return amount; }
        public long getTimestamp() { return timestamp; }
    }
    
    public static class PlayerAuctionData {
        private final UUID playerId;
        private int auctionsCreated;
        private int auctionsSold;
        private int auctionsBought;
        private double totalSpent;
        private double totalEarned;
        
        public PlayerAuctionData(UUID playerId) {
            this.playerId = playerId;
            this.auctionsCreated = 0;
            this.auctionsSold = 0;
            this.auctionsBought = 0;
            this.totalSpent = 0.0;
            this.totalEarned = 0.0;
        }
        
        // Getters and setters
        public UUID getPlayerId() { return playerId; }
        public int getAuctionsCreated() { return auctionsCreated; }
        public void setAuctionsCreated(int auctionsCreated) { this.auctionsCreated = auctionsCreated; }
        public int getAuctionsSold() { return auctionsSold; }
        public void setAuctionsSold(int auctionsSold) { this.auctionsSold = auctionsSold; }
        public int getAuctionsBought() { return auctionsBought; }
        public void setAuctionsBought(int auctionsBought) { this.auctionsBought = auctionsBought; }
        public double getTotalSpent() { return totalSpent; }
        public void setTotalSpent(double totalSpent) { this.totalSpent = totalSpent; }
        public double getTotalEarned() { return totalEarned; }
        public void setTotalEarned(double totalEarned) { this.totalEarned = totalEarned; }
    }
}
