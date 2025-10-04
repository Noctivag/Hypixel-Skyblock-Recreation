package de.noctivag.skyblock.maintenance;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * MaintenanceManager - Koordiniert alle Wartungstools
 * 
 * Features:
 * - Zentrale Verwaltung aller Wartungstools
 * - Automatische Wartungszyklen
 * - Wartungsberichte
 * - Auto-Fix-Funktionen
 * - Performance-Überwachung
 */
public class MaintenanceManager {
    
    private final SkyblockPlugin plugin;
    private final BrokenRedirectDetector brokenRedirectDetector;
    private final OrphanedSystemDetector orphanedSystemDetector;
    private final UnusedResourceDetector unusedResourceDetector;
    private final PluginStatisticsSystem statisticsSystem;
    private final AdvancedSearchSystem searchSystem;
    private final SystemDocumentationGenerator documentationGenerator;
    
    private boolean maintenanceMode = false;
    private long lastMaintenanceRun = 0;
    private final Map<String, Long> maintenanceHistory = new HashMap<>();
    
    public MaintenanceManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.brokenRedirectDetector = new BrokenRedirectDetector(plugin);
        this.orphanedSystemDetector = new OrphanedSystemDetector(plugin);
        this.unusedResourceDetector = new UnusedResourceDetector(plugin);
        this.statisticsSystem = new PluginStatisticsSystem(plugin);
        this.searchSystem = new AdvancedSearchSystem(plugin);
        this.documentationGenerator = new SystemDocumentationGenerator(plugin);
        
        startMaintenanceScheduler();
    }
    
    /**
     * Startet den Wartungsplaner
     */
    private void startMaintenanceScheduler() {
        // Run maintenance every 6 hours - use virtual thread for Folia compatibility
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(20L * 60 * 60 * 6 * 50); // Initial delay: 6 hours = 21,600,000 ms
                while (plugin.isEnabled()) {
                    if (!maintenanceMode) {
                        runScheduledMaintenance();
                    }
                    Thread.sleep(20L * 60 * 60 * 6 * 50); // Every 6 hours = 21,600,000 ms
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Run statistics update every hour - use virtual thread for Folia compatibility
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(20L * 60 * 60 * 50); // Initial delay: 1 hour = 3,600,000 ms
                while (plugin.isEnabled()) {
                    statisticsSystem.generateAllStatistics();
                    Thread.sleep(20L * 60 * 60 * 50); // Every 1 hour = 3,600,000 ms
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    /**
     * Führt geplante Wartung durch
     */
    public void runScheduledMaintenance() {
        plugin.getLogger().info("§e[Maintenance] Starting scheduled maintenance...");
        
        // Run all maintenance checks
        brokenRedirectDetector.performFullCheck();
        orphanedSystemDetector.performFullCheck();
        unusedResourceDetector.performFullCheck();
        
        // Generate statistics
        statisticsSystem.generateAllStatistics();
        
        // Update maintenance history
        lastMaintenanceRun = System.currentTimeMillis();
        maintenanceHistory.put("scheduled", lastMaintenanceRun);
        
        plugin.getLogger().info("§a[Maintenance] Scheduled maintenance completed!");
    }
    
    /**
     * Führt vollständige Wartung durch
     */
    public void runFullMaintenance() {
        plugin.getLogger().info("§e[Maintenance] Starting full maintenance...");
        
        maintenanceMode = true;
        
        try {
            // Run all maintenance checks
            brokenRedirectDetector.performFullCheck();
            orphanedSystemDetector.performFullCheck();
            unusedResourceDetector.performFullCheck();
            
            // Generate statistics
            statisticsSystem.generateAllStatistics();
            
            // Generate documentation
            documentationGenerator.generateAllDocumentation();
            
            // Update maintenance history
            lastMaintenanceRun = System.currentTimeMillis();
            maintenanceHistory.put("full", lastMaintenanceRun);
            
            plugin.getLogger().info("§a[Maintenance] Full maintenance completed!");
            
        } finally {
            maintenanceMode = false;
        }
    }
    
    /**
     * Führt Auto-Fix durch
     */
    public void runAutoFix() {
        plugin.getLogger().info("§e[Maintenance] Starting auto-fix...");
        
        int totalFixed = 0;
        
        // Fix broken redirects
        brokenRedirectDetector.autoFix();
        totalFixed += brokenRedirectDetector.getStatistics().get("total");
        
        // Cleanup orphaned systems
        orphanedSystemDetector.autoCleanup();
        totalFixed += orphanedSystemDetector.getStatistics().get("total");
        
        // Cleanup unused resources
        unusedResourceDetector.autoCleanup();
        totalFixed += unusedResourceDetector.getStatistics().get("total");
        
        // Update maintenance history
        maintenanceHistory.put("auto_fix", System.currentTimeMillis());
        
        plugin.getLogger().info("§a[Maintenance] Auto-fix completed! Fixed " + totalFixed + " issues.");
    }
    
    /**
     * Generiert einen Wartungsbericht
     */
    public void generateMaintenanceReport() {
        plugin.getLogger().info("§6=== MAINTENANCE REPORT ===");
        
        // System status
        plugin.getLogger().info("§eSystem Status:");
        plugin.getLogger().info("  §7Maintenance Mode: §a" + (maintenanceMode ? "Active" : "Inactive"));
        plugin.getLogger().info("  §7Last Maintenance: §a" + formatTimestamp(lastMaintenanceRun));
        plugin.getLogger().info("  §7Server Uptime: §a" + formatUptime(System.currentTimeMillis() - statisticsSystem.getPerformanceMetrics().get("startup_time")));
        
        // Broken redirects
        Map<String, Integer> brokenRedirects = brokenRedirectDetector.getStatistics();
        plugin.getLogger().info("§eBroken Redirects:");
        plugin.getLogger().info("  §7Total Issues: §a" + brokenRedirects.get("total"));
        plugin.getLogger().info("  §7Command Redirects: §a" + brokenRedirects.get("command_redirects"));
        plugin.getLogger().info("  §7GUI Links: §a" + brokenRedirects.get("gui_links"));
        plugin.getLogger().info("  §7Warps: §a" + brokenRedirects.get("warps"));
        plugin.getLogger().info("  §7Permissions: §a" + brokenRedirects.get("permissions"));
        
        // Orphaned systems
        Map<String, Integer> orphanedSystems = orphanedSystemDetector.getStatistics();
        plugin.getLogger().info("§eOrphaned Systems:");
        plugin.getLogger().info("  §7Total Items: §a" + orphanedSystems.get("total"));
        plugin.getLogger().info("  §7Orphaned Systems: §a" + orphanedSystems.get("orphaned_systems"));
        plugin.getLogger().info("  §7Orphaned Database Entries: §a" + orphanedSystems.get("orphaned_database_entries"));
        plugin.getLogger().info("  §7Orphaned Commands: §a" + orphanedSystems.get("orphaned_commands"));
        plugin.getLogger().info("  §7Orphaned Permissions: §a" + orphanedSystems.get("orphaned_permissions"));
        plugin.getLogger().info("  §7Orphaned Configs: §a" + orphanedSystems.get("orphaned_configs"));
        
        // Unused resources
        Map<String, Integer> unusedResources = unusedResourceDetector.getStatistics();
        plugin.getLogger().info("§eUnused Resources:");
        plugin.getLogger().info("  §7Total Resources: §a" + unusedResources.get("total"));
        plugin.getLogger().info("  §7Unused Configs: §a" + unusedResources.get("unused_configs"));
        plugin.getLogger().info("  §7Unused Database Tables: §a" + unusedResources.get("unused_database_tables"));
        plugin.getLogger().info("  §7Unused Permissions: §a" + unusedResources.get("unused_permissions"));
        plugin.getLogger().info("  §7Unused Commands: §a" + unusedResources.get("unused_commands"));
        plugin.getLogger().info("  §7Unused Files: §a" + unusedResources.get("unused_files"));
        plugin.getLogger().info("  §7Unused Classes: §a" + unusedResources.get("unused_classes"));
        
        // Statistics
        Map<String, Object> stats = statisticsSystem.getAllStatistics();
        plugin.getLogger().info("§eSystem Statistics:");
        plugin.getLogger().info("  §7Online Players: §a" + stats.get("online_players"));
        plugin.getLogger().info("  §7Total Players: §a" + stats.get("total_players"));
        plugin.getLogger().info("  §7Memory Usage: §a" + String.format("%.1f", stats.get("memory_usage_percent")) + "%");
        plugin.getLogger().info("  §7TPS: §a" + statisticsSystem.getPerformanceMetrics().get("tps"));
        
        // Search index
        Map<String, Integer> searchStats = searchSystem.getIndexStatistics();
        plugin.getLogger().info("§eSearch Index:");
        plugin.getLogger().info("  §7Total Entries: §a" + searchStats.get("total_entries"));
        plugin.getLogger().info("  §7Categories: §a" + searchStats.get("total_categories"));
        plugin.getLogger().info("  §7Tags: §a" + searchStats.get("total_tags"));
        
        plugin.getLogger().info("§6==========================");
    }
    
    /**
     * Öffnet das Wartungs-GUI
     */
    public void openMaintenanceGUI(Player player) {
        // This would open a GUI with all maintenance tools
        // For now, we'll just send a message
        player.sendMessage("§6§lMaintenance Manager");
        player.sendMessage("§7Available Tools:");
        player.sendMessage("§e• Broken Redirect Detector");
        player.sendMessage("§e• Orphaned System Detector");
        player.sendMessage("§e• Unused Resource Detector");
        player.sendMessage("§e• Plugin Statistics System");
        player.sendMessage("§e• Advanced Search System");
        player.sendMessage("§e• System Documentation Generator");
    }
    
    /**
     * Gibt den Wartungsstatus zurück
     */
    public boolean isMaintenanceMode() {
        return maintenanceMode;
    }
    
    /**
     * Setzt den Wartungsmodus
     */
    public void setMaintenanceMode(boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }
    
    /**
     * Gibt die Wartungshistorie zurück
     */
    public Map<String, Long> getMaintenanceHistory() {
        return new HashMap<>(maintenanceHistory);
    }
    
    /**
     * Gibt die letzten Wartungszeiten zurück
     */
    public long getLastMaintenanceRun() {
        return lastMaintenanceRun;
    }
    
    /**
     * Gibt alle Wartungstools zurück
     */
    public Map<String, Object> getMaintenanceTools() {
        Map<String, Object> tools = new HashMap<>();
        tools.put("brokenRedirectDetector", brokenRedirectDetector);
        tools.put("orphanedSystemDetector", orphanedSystemDetector);
        tools.put("unusedResourceDetector", unusedResourceDetector);
        tools.put("statisticsSystem", statisticsSystem);
        tools.put("searchSystem", searchSystem);
        tools.put("documentationGenerator", documentationGenerator);
        return tools;
    }
    
    /**
     * Erstellt ein GUI-Item für den Wartungsmanager
     */
    public ItemStack createMaintenanceItem() {
        ItemStack item = new ItemStack(org.bukkit.Material.ANVIL);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§6§lMaintenance Manager");
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Maintenance Status: §a" + (maintenanceMode ? "Active" : "Inactive"));
            lore.add("§7Last Maintenance: §a" + formatTimestamp(lastMaintenanceRun));
            lore.add("");
            lore.add("§7Available Tools:");
            lore.add("§e• Broken Redirect Detector");
            lore.add("§e• Orphaned System Detector");
            lore.add("§e• Unused Resource Detector");
            lore.add("§e• Plugin Statistics System");
            lore.add("§e• Advanced Search System");
            lore.add("§e• System Documentation Generator");
            lore.add("");
            lore.add("§eClick to open maintenance GUI");
            lore.add("§eRight-click to run full maintenance");
            
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Formatiert einen Zeitstempel
     */
    private String formatTimestamp(long timestamp) {
        if (timestamp == 0) {
            return "Never";
        }
        
        long diff = System.currentTimeMillis() - timestamp;
        if (diff < 60000) { // Less than 1 minute
            return "Just now";
        } else if (diff < 3600000) { // Less than 1 hour
            return (diff / 60000) + " minutes ago";
        } else if (diff < 86400000) { // Less than 1 day
            return (diff / 3600000) + " hours ago";
        } else {
            return (diff / 86400000) + " days ago";
        }
    }
    
    /**
     * Formatiert die Uptime
     */
    private String formatUptime(long uptime) {
        long seconds = uptime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) {
            return days + "d " + (hours % 24) + "h " + (minutes % 60) + "m";
        } else if (hours > 0) {
            return hours + "h " + (minutes % 60) + "m";
        } else if (minutes > 0) {
            return minutes + "m " + (seconds % 60) + "s";
        } else {
            return seconds + "s";
        }
    }
}
