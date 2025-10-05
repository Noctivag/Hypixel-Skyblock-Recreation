package de.noctivag.skyblock.web;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * API system for providing data to web interface
 */
public class ApiSystem implements Service {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    
    public ApiSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing ApiSystem...");
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("ApiSystem initialized.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down ApiSystem...");
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("ApiSystem shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "ApiSystem";
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
     * Get all players data
     */
    public Map<String, Object> getPlayersData() {
        Map<String, Object> data = new HashMap<>();
        
        List<Map<String, Object>> players = Bukkit.getOnlinePlayers().stream()
            .map(this::getPlayerData)
            .collect(Collectors.toList());
        
        data.put("players", players);
        data.put("total_online", Bukkit.getOnlinePlayers().size());
        data.put("max_players", Bukkit.getMaxPlayers());
        data.put("server_version", Bukkit.getServer().getVersion());
        data.put("timestamp", System.currentTimeMillis());
        
        return data;
    }
    
    /**
     * Get specific player data
     */
    public Map<String, Object> getPlayerData(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return null;
        }
        
        return getPlayerData(player);
    }
    
    /**
     * Get player data
     */
    public Map<String, Object> getPlayerData(Player player) {
        Map<String, Object> data = new HashMap<>();
        
        data.put("name", player.getName());
        data.put("uuid", player.getUniqueId().toString());
        data.put("health", player.getHealth());
        data.put("max_health", player.getMaxHealth());
        data.put("level", player.getLevel());
        data.put("exp", player.getExp());
        data.put("food_level", player.getFoodLevel());
        data.put("saturation", player.getSaturation());
        data.put("gamemode", player.getGameMode().name());
        data.put("world", player.getWorld().getName());
        data.put("location", Map.of(
            "x", player.getLocation().getX(),
            "y", player.getLocation().getY(),
            "z", player.getLocation().getZ(),
            "yaw", player.getLocation().getYaw(),
            "pitch", player.getLocation().getPitch()
        ));
        data.put("online_time", System.currentTimeMillis() - player.getFirstPlayed());
        data.put("last_seen", System.currentTimeMillis());
        
        // Add skyblock-specific data if available
        addSkyblockData(data, player);
        
        return data;
    }
    
    /**
     * Add skyblock-specific data
     */
    private void addSkyblockData(Map<String, Object> data, Player player) {
        try {
            // Get player profile from database
            var playerData = plugin.getDatabaseManager().getPlayerData(player.getUniqueId());
            if (playerData != null) {
                data.put("skyblock_level", playerData.getLevel());
                data.put("skyblock_coins", playerData.getCoins());
                data.put("last_login", playerData.getLastLogin());
                data.put("total_play_time", playerData.getTotalPlayTime());
            }
            
            // Add quest system data if available
            if (plugin.getQuestSystem() != null) {
                var questData = plugin.getQuestSystem().getPlayerQuestData(player.getUniqueId());
                if (questData != null) {
                    data.put("active_quests", questData.getActiveQuests().size());
                    data.put("completed_quests", questData.getCompletedQuests().size());
                }
            }
            
            // Add slayer system data if available
            // Implementation would depend on slayer system availability
            
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to add skyblock data for player " + player.getName() + ": " + e.getMessage());
        }
    }
    
    /**
     * Get bazaar data
     */
    public Map<String, Object> getBazaarData() {
        Map<String, Object> data = new HashMap<>();
        
        // Placeholder bazaar data
        data.put("items", Map.of(
            "diamond", Map.of(
                "buy_price", 8.5,
                "sell_price", 8.0,
                "volume", 1000,
                "orders", 50
            ),
            "emerald", Map.of(
                "buy_price", 12.0,
                "sell_price", 11.5,
                "volume", 500,
                "orders", 25
            )
        ));
        
        data.put("last_updated", System.currentTimeMillis());
        data.put("total_items", 2);
        
        return data;
    }
    
    /**
     * Get auctions data
     */
    public Map<String, Object> getAuctionsData() {
        Map<String, Object> data = new HashMap<>();
        
        // Placeholder auctions data
        data.put("auctions", List.of(
            Map.of(
                "id", "auction_1",
                "seller", "Player1",
                "item", "Diamond Sword",
                "price", 1000.0,
                "time_left", 3600,
                "bids", 5
            ),
            Map.of(
                "id", "auction_2",
                "seller", "Player2",
                "item", "Enchanted Book",
                "price", 500.0,
                "time_left", 1800,
                "bids", 2
            )
        ));
        
        data.put("total_auctions", 2);
        data.put("last_updated", System.currentTimeMillis());
        
        return data;
    }
    
    /**
     * Get server statistics
     */
    public Map<String, Object> getServerStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("online_players", Bukkit.getOnlinePlayers().size());
        stats.put("max_players", Bukkit.getMaxPlayers());
        stats.put("server_version", Bukkit.getServer().getVersion());
        stats.put("bukkit_version", Bukkit.getBukkitVersion());
        stats.put("java_version", System.getProperty("java.version"));
        stats.put("uptime", System.currentTimeMillis() - plugin.getServer().getServerTickManager().getServerTickTime());
        stats.put("tps", 20.0); // Placeholder
        stats.put("memory_used", Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        stats.put("memory_max", Runtime.getRuntime().maxMemory());
        
        return stats;
    }
    
    /**
     * Get world data
     */
    public Map<String, Object> getWorldData() {
        Map<String, Object> data = new HashMap<>();
        
        List<Map<String, Object>> worlds = Bukkit.getWorlds().stream()
            .map(world -> Map.of(
                "name", world.getName(),
                "players", world.getPlayers().size(),
                "environment", world.getEnvironment().name(),
                "time", world.getTime(),
                "weather", world.hasStorm() ? "storm" : "clear"
            ))
            .collect(Collectors.toList());
        
        data.put("worlds", worlds);
        data.put("total_worlds", Bukkit.getWorlds().size());
        
        return data;
    }
    
    /**
     * Get plugin information
     */
    public Map<String, Object> getPluginInfo() {
        Map<String, Object> info = new HashMap<>();
        
        info.put("name", plugin.getDescription().getName());
        info.put("version", plugin.getDescription().getVersion());
        info.put("author", plugin.getDescription().getAuthors());
        info.put("description", plugin.getDescription().getDescription());
        info.put("api_version", plugin.getDescription().getAPIVersion());
        info.put("folia_supported", plugin.getDescription().getPrefixes().contains("folia-supported"));
        
        return info;
    }
}
