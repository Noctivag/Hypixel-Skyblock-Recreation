package de.noctivag.skyblock.trading;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.database.DatabaseManager;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Trading System - Manages player-to-player trading
 */
public class TradingSystem implements Service {
    
    private final SkyblockPlugin plugin;
    private final DatabaseManager databaseManager;
    private SystemStatus status = SystemStatus.DISABLED;
    
    // Active trades
    private final Map<UUID, TradeSession> activeTrades = new ConcurrentHashMap<>();
    private final Map<UUID, UUID> pendingTradeRequests = new ConcurrentHashMap<>();
    
    // Trade settings
    private static final long TRADE_REQUEST_TIMEOUT = 30000; // 30 seconds
    private static final long TRADE_SESSION_TIMEOUT = 300000; // 5 minutes
    
    public TradingSystem(SkyblockPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing TradingSystem...");
        
        try {
            // Start cleanup task
            startCleanupTask();
            
            status = SystemStatus.RUNNING;
            plugin.getLogger().info("TradingSystem initialized successfully.");
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize TradingSystem", e);
            status = SystemStatus.ERROR;
        }
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down TradingSystem...");
        
        // Cancel all active trades
        for (TradeSession session : activeTrades.values()) {
            session.cancel("Server shutdown");
        }
        activeTrades.clear();
        pendingTradeRequests.clear();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("TradingSystem shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "TradingSystem";
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status == SystemStatus.DISABLED) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }
    
    /**
     * Start the cleanup task
     */
    private void startCleanupTask() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            
            // Clean up expired trade requests
            pendingTradeRequests.entrySet().removeIf(entry -> {
                UUID requester = entry.getKey();
                UUID target = entry.getValue();
                
                // Check if players are still online
                Player requesterPlayer = plugin.getServer().getPlayer(requester);
                Player targetPlayer = plugin.getServer().getPlayer(target);
                
                if (requesterPlayer == null || targetPlayer == null) {
                    return true; // Remove if either player is offline
                }
                
                // Check if request has expired
                // This would be implemented with timestamps in a real system
                return false; // For now, don't auto-expire
            });
            
            // Clean up expired trade sessions
            activeTrades.entrySet().removeIf(entry -> {
                TradeSession session = entry.getValue();
                if (session.isExpired()) {
                    session.cancel("Trade session expired");
                    return true;
                }
                return false;
            });
            
        }, 0L, 20L); // Run every second
    }
    
    /**
     * Send a trade request
     */
    public boolean sendTradeRequest(Player requester, Player target) {
        if (requester == null || target == null) {
            return false;
        }
        
        if (requester.equals(target)) {
            requester.sendMessage("§cYou cannot trade with yourself!");
            return false;
        }
        
        if (isInTrade(requester)) {
            requester.sendMessage("§cYou are already in a trade!");
            return false;
        }
        
        if (isInTrade(target)) {
            requester.sendMessage("§c" + target.getName() + " is already in a trade!");
            return false;
        }
        
        if (hasPendingRequest(requester)) {
            requester.sendMessage("§cYou already have a pending trade request!");
            return false;
        }
        
        if (hasPendingRequest(target)) {
            requester.sendMessage("§c" + target.getName() + " already has a pending trade request!");
            return false;
        }
        
        // Send the request
        pendingTradeRequests.put(requester.getUniqueId(), target.getUniqueId());
        
        requester.sendMessage("§aTrade request sent to " + target.getName() + "!");
        target.sendMessage("§a" + requester.getName() + " wants to trade with you!");
        target.sendMessage("§7Type §e/trade accept " + requester.getName() + " §7to accept or §e/trade deny " + requester.getName() + " §7to deny.");
        
        return true;
    }
    
    /**
     * Accept a trade request
     */
    public boolean acceptTradeRequest(Player accepter, Player requester) {
        if (accepter == null || requester == null) {
            return false;
        }
        
        UUID requesterId = requester.getUniqueId();
        UUID accepterId = accepter.getUniqueId();
        
        if (!pendingTradeRequests.containsKey(requesterId) || 
            !pendingTradeRequests.get(requesterId).equals(accepterId)) {
            accepter.sendMessage("§cNo pending trade request from " + requester.getName() + "!");
            return false;
        }
        
        // Remove the pending request
        pendingTradeRequests.remove(requesterId);
        
        // Create trade session
        TradeSession session = new TradeSession(requester, accepter, this);
        activeTrades.put(requesterId, session);
        activeTrades.put(accepterId, session);
        
        // Open trade GUI for both players
        session.openTradeGUI();
        
        requester.sendMessage("§a" + accepter.getName() + " accepted your trade request!");
        accepter.sendMessage("§aTrade session started with " + requester.getName() + "!");
        
        return true;
    }
    
    /**
     * Deny a trade request
     */
    public boolean denyTradeRequest(Player denier, Player requester) {
        if (denier == null || requester == null) {
            return false;
        }
        
        UUID requesterId = requester.getUniqueId();
        UUID denierId = denier.getUniqueId();
        
        if (!pendingTradeRequests.containsKey(requesterId) || 
            !pendingTradeRequests.get(requesterId).equals(denierId)) {
            denier.sendMessage("§cNo pending trade request from " + requester.getName() + "!");
            return false;
        }
        
        // Remove the pending request
        pendingTradeRequests.remove(requesterId);
        
        requester.sendMessage("§c" + denier.getName() + " denied your trade request!");
        denier.sendMessage("§aTrade request denied.");
        
        return true;
    }
    
    /**
     * Check if a player is in a trade
     */
    public boolean isInTrade(Player player) {
        return activeTrades.containsKey(player.getUniqueId());
    }
    
    /**
     * Check if a player has a pending trade request
     */
    public boolean hasPendingRequest(Player player) {
        return pendingTradeRequests.containsKey(player.getUniqueId()) || 
               pendingTradeRequests.containsValue(player.getUniqueId());
    }
    
    /**
     * Get the trade session for a player
     */
    public TradeSession getTradeSession(Player player) {
        return activeTrades.get(player.getUniqueId());
    }
    
    /**
     * Remove a trade session
     */
    public void removeTradeSession(TradeSession session) {
        if (session != null) {
            activeTrades.remove(session.getPlayer1().getUniqueId());
            activeTrades.remove(session.getPlayer2().getUniqueId());
        }
    }
    
    /**
     * Get all active trade sessions
     */
    public Collection<TradeSession> getActiveTrades() {
        return activeTrades.values();
    }
    
    /**
     * Get the number of active trades
     */
    public int getActiveTradeCount() {
        return activeTrades.size() / 2; // Each trade has 2 entries
    }
    
    /**
     * Get the number of pending requests
     */
    public int getPendingRequestCount() {
        return pendingTradeRequests.size();
    }
    
    /**
     * Cancel all trades for a player (when they disconnect)
     */
    public void cancelAllTradesForPlayer(Player player) {
        TradeSession session = getTradeSession(player);
        if (session != null) {
            session.cancel(player.getName() + " disconnected");
        }
        
        // Cancel pending requests
        pendingTradeRequests.entrySet().removeIf(entry -> {
            UUID requester = entry.getKey();
            UUID target = entry.getValue();
            
            if (requester.equals(player.getUniqueId())) {
                Player targetPlayer = plugin.getServer().getPlayer(target);
                if (targetPlayer != null) {
                    targetPlayer.sendMessage("§c" + player.getName() + " disconnected. Trade request cancelled.");
                }
                return true;
            }
            
            if (target.equals(player.getUniqueId())) {
                Player requesterPlayer = plugin.getServer().getPlayer(requester);
                if (requesterPlayer != null) {
                    requesterPlayer.sendMessage("§c" + player.getName() + " disconnected. Trade request cancelled.");
                }
                return true;
            }
            
            return false;
        });
    }
}
