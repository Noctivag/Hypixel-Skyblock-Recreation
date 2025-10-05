package de.noctivag.skyblock.economy;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.core.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;

/**
 * Trading System - Player-to-Player Trading
 * 
 * Verantwortlich für:
 * - Trade Requests
 * - Trade Negotiations
 * - Trade Execution
 * - Trade History
 * - Trade Security
 * - Trade Notifications
 */
public class TradingSystem {
    private final EconomySystem economySystem;
    private final Map<UUID, TradeRequest> pendingRequests = new ConcurrentHashMap<>();
    private final Map<UUID, TradeSession> activeTrades = new ConcurrentHashMap<>();
    private final Map<UUID, List<TradeHistory>> tradeHistory = new ConcurrentHashMap<>();
    
    // Trading Configuration
    private final long TRADE_REQUEST_TIMEOUT = 60 * 1000L; // 60 seconds
    private final int MAX_TRADE_ITEMS = 9;
    private final double TRADE_TAX = 0.02; // 2% tax
    
    public TradingSystem(EconomySystem economySystem) {
        this.economySystem = economySystem;
        startTradeTimeoutChecker();
    }
    
    private void startTradeTimeoutChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Check for expired trade requests
                List<UUID> expiredRequests = new ArrayList<>();
                for (Map.Entry<UUID, TradeRequest> entry : pendingRequests.entrySet()) {
                    if (entry.getValue().isExpired()) {
                        expiredRequests.add(entry.getKey());
                    }
                }
                
                // Remove expired requests
                for (UUID requestId : expiredRequests) {
                    TradeRequest request = pendingRequests.remove(requestId);
                    if (request != null) {
                        Player player = Bukkit.getPlayer(request.getRequester());
                        if (player != null) {
                            player.sendMessage("§cYour trade request to " + request.getTargetName() + " has expired!");
                        }
                    }
                }
            }
        }.runTaskTimer(economySystem.SkyblockPlugin, 0L, 20L * 10L); // Check every 10 seconds
    }
    
    public void sendTradeRequest(Player requester, Player target) {
        if (requester.equals(target)) {
            requester.sendMessage("§cYou cannot trade with yourself!");
            return;
        }
        
        if (activeTrades.containsKey(requester.getUniqueId()) || activeTrades.containsKey(target.getUniqueId())) {
            requester.sendMessage("§cOne of you is already in a trade!");
            return;
        }
        
        if (pendingRequests.containsKey(requester.getUniqueId())) {
            requester.sendMessage("§cYou already have a pending trade request!");
            return;
        }
        
        // Create trade request
        TradeRequest request = new TradeRequest(requester.getUniqueId(), target.getUniqueId(), 
            requester.getName(), target.getName());
        pendingRequests.put(requester.getUniqueId(), request);
        
        // Send messages
        requester.sendMessage("§a§lTRADE REQUEST SENT!");
        requester.sendMessage("§7To: §e" + target.getName());
        requester.sendMessage("§7Use §e/trade accept " + requester.getName() + " §7to accept");
        
        target.sendMessage("§a§lTRADE REQUEST RECEIVED!");
        target.sendMessage("§7From: §e" + requester.getName());
        target.sendMessage("§7Use §e/trade accept " + requester.getName() + " §7to accept");
        target.sendMessage("§7Use §e/trade decline " + requester.getName() + " §7to decline");
    }
    
    public boolean acceptTradeRequest(Player accepter, String requesterName) {
        // Find the trade request
        TradeRequest request = null;
        for (TradeRequest req : pendingRequests.values()) {
            if (req.getRequesterName().equals(requesterName) && req.getTarget().equals(accepter.getUniqueId())) {
                request = req;
                break;
            }
        }
        
        if (request == null) {
            accepter.sendMessage("§cNo trade request found from " + requesterName + "!");
            return false;
        }
        
        // Remove from pending requests
        pendingRequests.remove(request.getRequester());
        
        // Create trade session
        TradeSession session = new TradeSession(request.getRequester(), accepter.getUniqueId());
        activeTrades.put(request.getRequester(), session);
        activeTrades.put(accepter.getUniqueId(), session);
        
        // Open trade GUI for both players
        Player requester = Bukkit.getPlayer(request.getRequester());
        if (requester != null) {
            openTradeGUI(requester, session);
        }
        openTradeGUI(accepter, session);
        
        // Send messages
        accepter.sendMessage("§a§lTRADE ACCEPTED!");
        accepter.sendMessage("§7Trading with: §e" + requesterName);
        
        if (requester != null) {
            requester.sendMessage("§a§lTRADE ACCEPTED!");
            requester.sendMessage("§7Trading with: §e" + accepter.getName());
        }
        
        return true;
    }
    
    public boolean declineTradeRequest(Player decliner, String requesterName) {
        // Find the trade request
        TradeRequest request = null;
        for (TradeRequest req : pendingRequests.values()) {
            if (req.getRequesterName().equals(requesterName) && req.getTarget().equals(decliner.getUniqueId())) {
                request = req;
                break;
            }
        }
        
        if (request == null) {
            decliner.sendMessage("§cNo trade request found from " + requesterName + "!");
            return false;
        }
        
        // Remove from pending requests
        pendingRequests.remove(request.getRequester());
        
        // Send messages
        decliner.sendMessage("§c§lTRADE DECLINED!");
        decliner.sendMessage("§7Declined trade with: §e" + requesterName);
        
        Player requester = Bukkit.getPlayer(request.getRequester());
        if (requester != null) {
            requester.sendMessage("§c§lTRADE DECLINED!");
            requester.sendMessage("§7" + decliner.getName() + " declined your trade request");
        }
        
        return true;
    }
    
    public void openTradeGUI(Player player, TradeSession session) {
        // Create trade GUI
        org.bukkit.inventory.Inventory inventory = org.bukkit.Bukkit.createInventory(null, 54, "§6§lTrading with " + getOtherPlayerName(player, session));
        
        // Add trade items
        List<ItemStack> playerItems = session.getPlayerItems(player.getUniqueId());
        for (int i = 0; i < playerItems.size() && i < 9; i++) {
            inventory.setItem(i, playerItems.get(i));
        }
        
        // Add other player's items
        List<ItemStack> otherItems = session.getPlayerItems(getOtherPlayerId(player, session));
        for (int i = 0; i < otherItems.size() && i < 9; i++) {
            inventory.setItem(i + 9, otherItems.get(i));
        }
        
        // Add control buttons
        ItemStack readyButton = new ItemStack(org.bukkit.Material.LIME_CONCRETE);
        org.bukkit.inventory.meta.ItemMeta readyMeta = readyButton.getItemMeta();
        if (readyMeta != null) {
            readyMeta.displayName(net.kyori.adventure.text.Component.text("§a§lReady"));
            readyButton.setItemMeta(readyMeta);
        }
        inventory.setItem(45, readyButton);
        
        ItemStack cancelButton = new ItemStack(org.bukkit.Material.RED_CONCRETE);
        org.bukkit.inventory.meta.ItemMeta cancelMeta = cancelButton.getItemMeta();
        if (cancelMeta != null) {
            cancelMeta.displayName(net.kyori.adventure.text.Component.text("§c§lCancel"));
            cancelButton.setItemMeta(cancelMeta);
        }
        inventory.setItem(53, cancelButton);
        
        player.openInventory(inventory);
    }
    
    public void addTradeItem(Player player, ItemStack item) {
        TradeSession session = activeTrades.get(player.getUniqueId());
        if (session == null) return;
        
        if (session.getPlayerItems(player.getUniqueId()).size() >= MAX_TRADE_ITEMS) {
            player.sendMessage("§cYou can only trade up to " + MAX_TRADE_ITEMS + " items!");
            return;
        }
        
        session.addPlayerItem(player.getUniqueId(), item);
        
        // Update GUI for both players
        Player otherPlayer = Bukkit.getPlayer(getOtherPlayerId(player, session));
        if (otherPlayer != null) {
            openTradeGUI(otherPlayer, session);
        }
        openTradeGUI(player, session);
        
        player.sendMessage(Component.text("§a§lITEM ADDED TO TRADE!"));
        player.sendMessage("§7Item: §e" + item.getType().name());
        player.sendMessage("§7Amount: §e" + item.getAmount());
    }
    
    public void removeTradeItem(Player player, int slot) {
        TradeSession session = activeTrades.get(player.getUniqueId());
        if (session == null) return;
        
        List<ItemStack> playerItems = session.getPlayerItems(player.getUniqueId());
        if (slot < 0 || slot >= playerItems.size()) return;
        
        ItemStack removedItem = playerItems.remove(slot);
        player.getInventory().addItem(removedItem);
        
        // Update GUI for both players
        Player otherPlayer = Bukkit.getPlayer(getOtherPlayerId(player, session));
        if (otherPlayer != null) {
            openTradeGUI(otherPlayer, session);
        }
        openTradeGUI(player, session);
        
        player.sendMessage(Component.text("§c§lITEM REMOVED FROM TRADE!"));
        player.sendMessage("§7Item: §e" + removedItem.getType().name());
        player.sendMessage("§7Amount: §e" + removedItem.getAmount());
    }
    
    public void setTradeReady(Player player, boolean ready) {
        TradeSession session = activeTrades.get(player.getUniqueId());
        if (session == null) return;
        
        session.setPlayerReady(player.getUniqueId(), ready);
        
        // Check if both players are ready
        if (session.isBothPlayersReady()) {
            executeTrade(session);
        } else {
            // Update GUI for both players
            Player otherPlayer = Bukkit.getPlayer(getOtherPlayerId(player, session));
            if (otherPlayer != null) {
                openTradeGUI(otherPlayer, session);
            }
            openTradeGUI(player, session);
            
            if (ready) {
                player.sendMessage(Component.text("§a§lYOU ARE READY!"));
                player.sendMessage(Component.text("§7Waiting for the other player..."));
            } else {
                player.sendMessage(Component.text("§c§lYOU ARE NOT READY!"));
            }
        }
    }
    
    public void cancelTrade(Player player) {
        TradeSession session = activeTrades.get(player.getUniqueId());
        if (session == null) return;
        
        // Return items to players
        returnItemsToPlayers(session);
        
        // Remove from active trades
        activeTrades.remove(session.getPlayer1());
        activeTrades.remove(session.getPlayer2());
        
        // Send messages
        player.sendMessage(Component.text("§c§lTRADE CANCELLED!"));
        
        Player otherPlayer = Bukkit.getPlayer(getOtherPlayerId(player, session));
        if (otherPlayer != null) {
            otherPlayer.sendMessage("§c§lTRADE CANCELLED!");
            otherPlayer.sendMessage("§7" + player.getName() + " cancelled the trade");
        }
    }
    
    private void executeTrade(TradeSession session) {
        // Calculate trade value for tax
        double totalValue = calculateTradeValue(session);
        double tax = totalValue * TRADE_TAX;
        
        // Execute the trade
        Player player1 = Bukkit.getPlayer(session.getPlayer1());
        Player player2 = Bukkit.getPlayer(session.getPlayer2());
        
        if (player1 == null || player2 == null) {
            cancelTrade(player1 != null ? player1 : player2);
            return;
        }
        
        // Give items to players
        for (ItemStack item : session.getPlayerItems(session.getPlayer1())) {
            player2.getInventory().addItem(item);
        }
        
        for (ItemStack item : session.getPlayerItems(session.getPlayer2())) {
            player1.getInventory().addItem(item);
        }
        
        // Apply tax if applicable
        if (tax > 0) {
            PlayerProfile profile1 = economySystem.corePlatform.getPlayerProfile(session.getPlayer1());
            PlayerProfile profile2 = economySystem.corePlatform.getPlayerProfile(session.getPlayer2());
            
            if (profile1 != null) {
                profile1.removeCoins(tax / 2);
            }
            if (profile2 != null) {
                profile2.removeCoins(tax / 2);
            }
        }
        
        // Record trade history
        recordTradeHistory(session);
        
        // Remove from active trades
        activeTrades.remove(session.getPlayer1());
        activeTrades.remove(session.getPlayer2());
        
        // Send success messages
        player1.sendMessage("§a§lTRADE COMPLETED!");
        player1.sendMessage("§7Traded with: §e" + player2.getName());
        if (tax > 0) {
            player1.sendMessage("§7Trade tax: §6" + (tax / 2) + " coins");
        }
        
        player2.sendMessage("§a§lTRADE COMPLETED!");
        player2.sendMessage("§7Traded with: §e" + player1.getName());
        if (tax > 0) {
            player2.sendMessage("§7Trade tax: §6" + (tax / 2) + " coins");
        }
    }
    
    private void returnItemsToPlayers(TradeSession session) {
        Player player1 = Bukkit.getPlayer(session.getPlayer1());
        Player player2 = Bukkit.getPlayer(session.getPlayer2());
        
        if (player1 != null) {
            for (ItemStack item : session.getPlayerItems(session.getPlayer1())) {
                player1.getInventory().addItem(item);
            }
        }
        
        if (player2 != null) {
            for (ItemStack item : session.getPlayerItems(session.getPlayer2())) {
                player2.getInventory().addItem(item);
            }
        }
    }
    
    private double calculateTradeValue(TradeSession session) {
        double totalValue = 0.0;
        
        for (ItemStack item : session.getPlayerItems(session.getPlayer1())) {
            totalValue += economySystem.getCurrentPrice(item.getType().name()) * item.getAmount();
        }
        
        for (ItemStack item : session.getPlayerItems(session.getPlayer2())) {
            totalValue += economySystem.getCurrentPrice(item.getType().name()) * item.getAmount();
        }
        
        return totalValue;
    }
    
    private void recordTradeHistory(TradeSession session) {
        TradeHistory history = new TradeHistory(session.getPlayer1(), session.getPlayer2(), 
            session.getPlayerItems(session.getPlayer1()), session.getPlayerItems(session.getPlayer2()));
        
        tradeHistory.computeIfAbsent(session.getPlayer1(), k -> new ArrayList<>()).add(history);
        tradeHistory.computeIfAbsent(session.getPlayer2(), k -> new ArrayList<>()).add(history);
    }
    
    private UUID getOtherPlayerId(Player player, TradeSession session) {
        return player.getUniqueId().equals(session.getPlayer1()) ? session.getPlayer2() : session.getPlayer1();
    }
    
    private String getOtherPlayerName(Player player, TradeSession session) {
        UUID otherId = getOtherPlayerId(player, session);
        Player otherPlayer = Bukkit.getPlayer(otherId);
        return otherPlayer != null ? otherPlayer.getName() : "Unknown";
    }
    
    public List<TradeHistory> getTradeHistory(UUID playerId) {
        return tradeHistory.getOrDefault(playerId, new ArrayList<>());
    }
    
    public TradeSession getActiveTrade(UUID playerId) {
        return activeTrades.get(playerId);
    }
    
    public TradeRequest getPendingRequest(UUID playerId) {
        return pendingRequests.get(playerId);
    }
    
    // Trade Request Class
    public static class TradeRequest {
        private final UUID requester;
        private final UUID target;
        private final String requesterName;
        private final String targetName;
        private final long createdAt;
        
        public TradeRequest(UUID requester, UUID target, String requesterName, String targetName) {
            this.requester = requester;
            this.target = target;
            this.requesterName = requesterName;
            this.targetName = targetName;
            this.createdAt = java.lang.System.currentTimeMillis();
        }
        
        public boolean isExpired() {
            return java.lang.System.currentTimeMillis() - createdAt > 60 * 1000L; // 60 seconds
        }
        
        public UUID getRequester() { return requester; }
        public UUID getTarget() { return target; }
        public String getRequesterName() { return requesterName; }
        public String getTargetName() { return targetName; }
        public long getCreatedAt() { return createdAt; }
    }
    
    // Trade Session Class
    public static class TradeSession {
        private final UUID player1;
        private final UUID player2;
        private final Map<UUID, List<ItemStack>> playerItems = new HashMap<>();
        private final Map<UUID, Boolean> playerReady = new HashMap<>();
        
        public TradeSession(UUID player1, UUID player2) {
            this.player1 = player1;
            this.player2 = player2;
            this.playerItems.put(player1, new ArrayList<>());
            this.playerItems.put(player2, new ArrayList<>());
            this.playerReady.put(player1, false);
            this.playerReady.put(player2, false);
        }
        
        public void addPlayerItem(UUID playerId, ItemStack item) {
            playerItems.get(playerId).add(item);
        }
        
        public List<ItemStack> getPlayerItems(UUID playerId) {
            return playerItems.get(playerId);
        }
        
        public void setPlayerReady(UUID playerId, boolean ready) {
            playerReady.put(playerId, ready);
        }
        
        public boolean isPlayerReady(UUID playerId) {
            return playerReady.get(playerId);
        }
        
        public boolean isBothPlayersReady() {
            return playerReady.get(player1) && playerReady.get(player2);
        }
        
        public UUID getPlayer1() { return player1; }
        public UUID getPlayer2() { return player2; }
    }
    
    // Trade History Class
    public static class TradeHistory {
        private final UUID player1;
        private final UUID player2;
        private final List<ItemStack> player1Items;
        private final List<ItemStack> player2Items;
        private final long timestamp;
        
        public TradeHistory(UUID player1, UUID player2, List<ItemStack> player1Items, List<ItemStack> player2Items) {
            this.player1 = player1;
            this.player2 = player2;
            this.player1Items = new ArrayList<>(player1Items);
            this.player2Items = new ArrayList<>(player2Items);
            this.timestamp = java.lang.System.currentTimeMillis();
        }
        
        public UUID getPlayer1() { return player1; }
        public UUID getPlayer2() { return player2; }
        public List<ItemStack> getPlayer1Items() { return new ArrayList<>(player1Items); }
        public List<ItemStack> getPlayer2Items() { return new ArrayList<>(player2Items); }
        public long getTimestamp() { return timestamp; }
    }
}
