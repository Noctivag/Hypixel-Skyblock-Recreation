package de.noctivag.skyblock.compatibility;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;


/**
 * CompatibilityManager - Verwaltet Plugin-Kompatibilität
 * 
 * Features:
 * - API Version Prüfung
 * - Dependency Prüfung
 * - Server Version Prüfung
 * - Plugin Konflikte Erkennung
 * - Kompatibilitäts-Warnungen
 */
public class CompatibilityManager {
    
    private final SkyblockPlugin plugin;
    private final PerformanceOptimizer performanceOptimizer;
    private boolean isCompatible = true;
    private final StringBuilder compatibilityReport = new StringBuilder();
    
    public CompatibilityManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.performanceOptimizer = new PerformanceOptimizer(plugin);
        checkCompatibility();
    }
    
    /**
     * Führt eine umfassende Kompatibilitätsprüfung durch
     */
    private void checkCompatibility() {
        compatibilityReport.append("§6§l=== PLUGIN COMPATIBILITY CHECK ===\n");
        
        // API Version prüfen
        checkAPIVersion();
        
        // Server Version prüfen
        checkServerVersion();
        
        // Java Version prüfen
        checkJavaVersion();
        
        // Dependencies prüfen
        checkDependencies();
        
        // Plugin Konflikte prüfen
        checkPluginConflicts();
        
        // Performance prüfen
        checkPerformance();
        
        compatibilityReport.append("§6===============================\n");
        
        // Report ausgeben
        if (isCompatible) {
            plugin.getLogger().info("§aPlugin compatibility check passed!");
        } else {
            plugin.getLogger().warning("§cPlugin compatibility issues detected!");
        }
        
        plugin.getLogger().info(compatibilityReport.toString());
    }
    
    /**
     * Prüft die API Version
     */
    private void checkAPIVersion() {
        String apiVersion = Bukkit.getVersion();
        String bukkitVersion = Bukkit.getBukkitVersion();
        
        compatibilityReport.append("§7API Version: §a").append(apiVersion).append("\n");
        compatibilityReport.append("§7Bukkit Version: §a").append(bukkitVersion).append("\n");
        
        // Prüfe auf unterstützte Versionen
        if (apiVersion.contains("1.20") || apiVersion.contains("1.21")) {
            compatibilityReport.append("§7API Compatibility: §a✅ Supported\n");
        } else if (apiVersion.contains("1.19")) {
            compatibilityReport.append("§7API Compatibility: §e⚠️ Limited Support\n");
        } else {
            compatibilityReport.append("§7API Compatibility: §c❌ Not Supported\n");
            isCompatible = false;
        }
    }
    
    /**
     * Prüft die Server Version
     */
    private void checkServerVersion() {
        String serverName = Bukkit.getName();
        String serverVersion = Bukkit.getVersion();
        
        compatibilityReport.append("§7Server: §a").append(serverName).append("\n");
        compatibilityReport.append("§7Server Version: §a").append(serverVersion).append("\n");
        
        // Prüfe auf Paper/Spigot
        if (serverName.toLowerCase().contains("paper")) {
            compatibilityReport.append("§7Server Type: §a✅ Paper (Recommended)\n");
        } else if (serverName.toLowerCase().contains("spigot")) {
            compatibilityReport.append("§7Server Type: §e⚠️ Spigot (Limited Features)\n");
        } else if (serverName.toLowerCase().contains("craftbukkit")) {
            compatibilityReport.append("§7Server Type: §c❌ CraftBukkit (Not Recommended)\n");
            isCompatible = false;
        } else {
            compatibilityReport.append("§7Server Type: §e⚠️ Unknown\n");
        }
    }
    
    /**
     * Prüft die Java Version
     */
    private void checkJavaVersion() {
        String javaVersion = java.lang.System.getProperty("java.version");
        String javaVendor = java.lang.System.getProperty("java.vendor");
        
        compatibilityReport.append("§7Java Version: §a").append(javaVersion).append("\n");
        compatibilityReport.append("§7Java Vendor: §a").append(javaVendor).append("\n");
        
        // Prüfe Java Version
        if (javaVersion.startsWith("21")) {
            compatibilityReport.append("§7Java Compatibility: §a✅ Java 21 (Optimal)\n");
        } else if (javaVersion.startsWith("17")) {
            compatibilityReport.append("§7Java Compatibility: §a✅ Java 17 (Supported)\n");
        } else if (javaVersion.startsWith("19") || javaVersion.startsWith("20")) {
            compatibilityReport.append("§7Java Compatibility: §e⚠️ Java ").append(javaVersion.split("\\.")[0]).append(" (Limited Testing)\n");
        } else {
            compatibilityReport.append("§7Java Compatibility: §c❌ Java ").append(javaVersion.split("\\.")[0]).append(" (Not Supported)\n");
            isCompatible = false;
        }
    }
    
    /**
     * Prüft Dependencies
     */
    private void checkDependencies() {
        // External plugins are ignored on purpose - the plugin is designed to work standalone.
        compatibilityReport.append("§7External plugins: §a✅ Ignored (self-contained mode)\n");
        compatibilityReport.append("§7Note: WorldEdit/PlaceholderAPI/Vault checks removed - internal substitutes used for required features.\n");
    }
    
    /**
     * Prüft Plugin Konflikte
     */
    private void checkPluginConflicts() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        
        // Bekannte Konflikte
        String[] conflictingPlugins = {
            "Essentials", "EssentialsX", "CraftBukkit", "Bukkit"
        };
        
        boolean hasConflicts = false;
        for (String pluginName : conflictingPlugins) {
            if (pluginManager.getPlugin(pluginName) != null) {
                compatibilityReport.append("§7Conflict: §c❌ ").append(pluginName).append(" (May cause issues)\n");
                hasConflicts = true;
            }
        }
        
        if (!hasConflicts) {
            compatibilityReport.append("§7Plugin Conflicts: §a✅ None detected\n");
        }
    }
    
    /**
     * Prüft Performance
     */
    private void checkPerformance() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024; // MB
        long totalMemory = runtime.totalMemory() / 1024 / 1024; // MB
        int processors = runtime.availableProcessors();
        
        compatibilityReport.append("§7Max Memory: §a").append(maxMemory).append(" MB\n");
        compatibilityReport.append("§7Total Memory: §a").append(totalMemory).append(" MB\n");
        compatibilityReport.append("§7Processors: §a").append(processors).append("\n");
        
        // Performance Bewertung
        if (maxMemory >= 4096) {
            compatibilityReport.append("§7Memory: §a✅ Excellent\n");
        } else if (maxMemory >= 2048) {
            compatibilityReport.append("§7Memory: §a✅ Good\n");
        } else if (maxMemory >= 1024) {
            compatibilityReport.append("§7Memory: §e⚠️ Limited\n");
        } else {
            compatibilityReport.append("§7Memory: §c❌ Insufficient\n");
            isCompatible = false;
        }
        
        if (processors >= 4) {
            compatibilityReport.append("§7CPU: §a✅ Good\n");
        } else if (processors >= 2) {
            compatibilityReport.append("§7CPU: §e⚠️ Limited\n");
        } else {
            compatibilityReport.append("§7CPU: §c❌ Insufficient\n");
        }
    }
    
    /**
     * Gibt den Kompatibilitätsstatus zurück
     */
    public boolean isCompatible() {
        return isCompatible;
    }
    
    /**
     * Gibt den Kompatibilitätsbericht zurück
     */
    public String getCompatibilityReport() {
        return compatibilityReport.toString();
    }
    
    /**
     * Führt eine erneute Kompatibilitätsprüfung durch
     */
    public void recheckCompatibility() {
        isCompatible = true;
        compatibilityReport.setLength(0);
        checkCompatibility();
    }
    
    /**
     * Gibt Kompatibilitätsempfehlungen zurück
     */
    public String getRecommendations() {
        StringBuilder recommendations = new StringBuilder();
        recommendations.append("§6§l=== COMPATIBILITY RECOMMENDATIONS ===\n");

        if (!isCompatible) {
            recommendations.append("§c❌ Critical Issues Found:\n");
            recommendations.append("§7- Update to Java 17 or higher\n");
            recommendations.append("§7- Use Paper server for best compatibility\n");
            recommendations.append("§7- Ensure sufficient memory allocation\n");
        } else {
            recommendations.append("§a✅ Plugin is compatible with your server!\n");
        }

        recommendations.append("\n§e💡 Optimization Tips:\n");
        recommendations.append("§7- Use Paper server for better performance\n");
        recommendations.append("§7- Allocate at least 2GB RAM\n");
        recommendations.append("§7- No external economy required; internal economy enabled\n");
        recommendations.append("§7- Install PlaceholderAPI for placeholders\n");

        recommendations.append("§6===============================\n");

        return recommendations.toString();
    }
    
    /**
     * Gibt den PerformanceOptimizer zurück
     */
    public PerformanceOptimizer getPerformanceOptimizer() {
        return performanceOptimizer;
    }
}
