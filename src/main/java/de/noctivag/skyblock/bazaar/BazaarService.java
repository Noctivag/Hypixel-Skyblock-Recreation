package de.noctivag.skyblock.bazaar;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Bazaar Service - Service for managing the bazaar
 */
public class BazaarService implements Service {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    private final Map<UUID, BazaarOrder> playerOrders = new HashMap<>();
    
    public BazaarService(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing BazaarService...");
        
        // Initialize bazaar system
        // Additional initialization logic can be added here
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("BazaarService initialized.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down BazaarService...");
        
        // Save all bazaar data
        for (BazaarOrder order : playerOrders.values()) {
            // Save order data to database
            plugin.getLogger().log(Level.INFO, "Saving bazaar order: " + order.getOrderId());
        }
        
        playerOrders.clear();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("BazaarService shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "BazaarService";
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
     * Create a buy order
     */
    public boolean createBuyOrder(Player player, String itemId, int amount, double pricePerUnit) {
        UUID orderId = UUID.randomUUID();
        BazaarOrder order = new BazaarOrder(orderId, player.getUniqueId(), itemId, amount, pricePerUnit, BazaarOrderType.BUY);
        
        playerOrders.put(orderId, order);
        player.sendMessage("§aCreated buy order for " + amount + "x " + itemId + " at " + pricePerUnit + " coins per unit");
        plugin.getLogger().log(Level.INFO, "Player " + player.getName() + " created buy order: " + orderId);
        
        return true;
    }
    
    /**
     * Create a sell order
     */
    public boolean createSellOrder(Player player, String itemId, int amount, double pricePerUnit) {
        UUID orderId = UUID.randomUUID();
        BazaarOrder order = new BazaarOrder(orderId, player.getUniqueId(), itemId, amount, pricePerUnit, BazaarOrderType.SELL);
        
        playerOrders.put(orderId, order);
        player.sendMessage("§aCreated sell order for " + amount + "x " + itemId + " at " + pricePerUnit + " coins per unit");
        plugin.getLogger().log(Level.INFO, "Player " + player.getName() + " created sell order: " + orderId);
        
        return true;
    }
    
    /**
     * Cancel an order
     */
    public boolean cancelOrder(Player player, UUID orderId) {
        BazaarOrder order = playerOrders.get(orderId);
        if (order != null && order.getPlayerId().equals(player.getUniqueId())) {
            playerOrders.remove(orderId);
            player.sendMessage("§cCancelled order: " + orderId);
            plugin.getLogger().log(Level.INFO, "Player " + player.getName() + " cancelled order: " + orderId);
            return true;
        }
        
        player.sendMessage("§cOrder not found or not owned by you!");
        return false;
    }
    
    /**
     * Get a player's orders
     */
    public Map<UUID, BazaarOrder> getPlayerOrders(Player player) {
        Map<UUID, BazaarOrder> playerOrders = new HashMap<>();
        for (BazaarOrder order : this.playerOrders.values()) {
            if (order.getPlayerId().equals(player.getUniqueId())) {
                playerOrders.put(order.getOrderId(), order);
            }
        }
        return playerOrders;
    }
    
    /**
     * Get all orders
     */
    public Map<UUID, BazaarOrder> getAllOrders() {
        return new HashMap<>(playerOrders);
    }
}
