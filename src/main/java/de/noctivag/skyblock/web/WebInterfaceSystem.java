package de.noctivag.skyblock.web;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Web interface system for the skyblock plugin
 */
public class WebInterfaceSystem implements Service {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    private final ApiSystem apiSystem;
    private final String apiKey;
    
    public WebInterfaceSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.apiSystem = new ApiSystem(plugin);
        this.apiKey = generateApiKey();
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing WebInterfaceSystem...");
        
        try {
            // Initialize API system
            apiSystem.initialize();
            
            status = SystemStatus.RUNNING;
            plugin.getLogger().info("WebInterfaceSystem initialized (simplified version)");
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize WebInterfaceSystem", e);
            status = SystemStatus.ERROR;
        }
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down WebInterfaceSystem...");
        
        // Shutdown API system
        apiSystem.shutdown();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("WebInterfaceSystem shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "WebInterfaceSystem";
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
     * Validate API key
     */
    private boolean validateApiKey(String providedKey) {
        return apiKey.equals(providedKey);
    }
    
    /**
     * Generate API key
     */
    private String generateApiKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * Create main page HTML
     */
    private String createMainPage() {
        return "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
                "<title>Skyblock Server</title>" +
                "<style>" +
                    "body { font-family: Arial, sans-serif; margin: 40px; background-color: #f0f0f0; }" +
                    ".container { max-width: 800px; margin: 0 auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }" +
                    "h1 { color: #333; text-align: center; }" +
                    ".stats { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; margin: 20px 0; }" +
                    ".stat-card { background: #f8f9fa; padding: 15px; border-radius: 5px; text-align: center; }" +
                    ".stat-value { font-size: 24px; font-weight: bold; color: #007bff; }" +
                    ".stat-label { color: #666; margin-top: 5px; }" +
                "</style>" +
            "</head>" +
            "<body>" +
                "<div class=\"container\">" +
                    "<h1>🏝️ Skyblock Server</h1>" +
                    "<div class=\"stats\">" +
                        "<div class=\"stat-card\">" +
                            "<div class=\"stat-value\">" + Bukkit.getOnlinePlayers().size() + "</div>" +
                            "<div class=\"stat-label\">Online Players</div>" +
                        "</div>" +
                        "<div class=\"stat-card\">" +
                            "<div class=\"stat-value\">" + Bukkit.getMaxPlayers() + "</div>" +
                            "<div class=\"stat-label\">Max Players</div>" +
                        "</div>" +
                        "<div class=\"stat-card\">" +
                            "<div class=\"stat-value\">" + Bukkit.getServer().getVersion() + "</div>" +
                            "<div class=\"stat-label\">Server Version</div>" +
                        "</div>" +
                    "</div>" +
                    "<p style=\"text-align: center; color: #666;\">" +
                        "Welcome to our Skyblock server! Use the API endpoints to access player data and server information." +
                    "</p>" +
                "</div>" +
            "</body>" +
            "</html>";
    }
    
    /**
     * Create player stats page HTML
     */
    private String createPlayerStatsPage(Player player) {
        return "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
                "<title>Player Stats - " + player.getName() + "</title>" +
                "<style>" +
                    "body { font-family: Arial, sans-serif; margin: 40px; background-color: #f0f0f0; }" +
                    ".container { max-width: 600px; margin: 0 auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }" +
                    "h1 { color: #333; text-align: center; }" +
                    ".player-info { background: #f8f9fa; padding: 15px; border-radius: 5px; margin: 20px 0; }" +
                    ".info-row { display: flex; justify-content: space-between; margin: 10px 0; }" +
                    ".info-label { font-weight: bold; color: #333; }" +
                    ".info-value { color: #666; }" +
                "</style>" +
            "</head>" +
            "<body>" +
                "<div class=\"container\">" +
                    "<h1>👤 Player Stats</h1>" +
                    "<div class=\"player-info\">" +
                        "<div class=\"info-row\">" +
                            "<span class=\"info-label\">Name:</span>" +
                            "<span class=\"info-value\">" + player.getName() + "</span>" +
                        "</div>" +
                        "<div class=\"info-row\">" +
                            "<span class=\"info-label\">UUID:</span>" +
                            "<span class=\"info-value\">" + player.getUniqueId() + "</span>" +
                        "</div>" +
                        "<div class=\"info-row\">" +
                            "<span class=\"info-label\">Health:</span>" +
                            "<span class=\"info-value\">" + player.getHealth() + "/" + player.getMaxHealth() + "</span>" +
                        "</div>" +
                        "<div class=\"info-row\">" +
                            "<span class=\"info-label\">Level:</span>" +
                            "<span class=\"info-value\">" + player.getLevel() + "</span>" +
                        "</div>" +
                        "<div class=\"info-row\">" +
                            "<span class=\"info-label\">Location:</span>" +
                            "<span class=\"info-value\">" + player.getLocation().getWorld().getName() + " " + 
                            player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + "</span>" +
                        "</div>" +
                    "</div>" +
                "</div>" +
            "</body>" +
            "</html>";
    }
    
    /**
     * Create error page HTML
     */
    private String createErrorPage(String message) {
        return "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
                "<title>Error</title>" +
                "<style>" +
                    "body { font-family: Arial, sans-serif; margin: 40px; background-color: #f0f0f0; }" +
                    ".container { max-width: 600px; margin: 0 auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); text-align: center; }" +
                    "h1 { color: #dc3545; }" +
                    "p { color: #666; }" +
                "</style>" +
            "</head>" +
            "<body>" +
                "<div class=\"container\">" +
                    "<h1>❌ Error</h1>" +
                    "<p>" + message + "</p>" +
                "</div>" +
            "</body>" +
            "</html>";
    }
    
    /**
     * Get API system
     */
    public ApiSystem getApiSystem() {
        return apiSystem;
    }
    
    /**
     * Get API key
     */
    public String getApiKey() {
        return apiKey;
    }
}