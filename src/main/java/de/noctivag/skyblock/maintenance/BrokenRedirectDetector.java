package de.noctivag.skyblock.maintenance;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BrokenRedirectDetector - Erkennt defekte Command-Weiterleitungen und GUI-Links
 * 
 * Features:
 * - Command-Alias-Validierung
 * - GUI-Link-Überprüfung
 * - Warp-Destination-Validierung
 * - Permission-Validierung
 * - Auto-Fix-Funktionen
 */
public class BrokenRedirectDetector {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<String, List<String>> brokenRedirects = new ConcurrentHashMap<>();
    private final Map<String, List<String>> brokenGUILinks = new ConcurrentHashMap<>();
    private final Map<String, List<String>> brokenWarps = new ConcurrentHashMap<>();
    private final Map<String, List<String>> brokenPermissions = new ConcurrentHashMap<>();
    
    public BrokenRedirectDetector(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    /**
     * Führt eine vollständige Überprüfung aller Weiterleitungen durch
     */
    public void performFullCheck() {
        SkyblockPlugin.getLogger().info("§e[Maintenance] Starting broken redirect detection...");
        
        // Clear previous results
        brokenRedirects.clear();
        brokenGUILinks.clear();
        brokenWarps.clear();
        brokenPermissions.clear();
        
        // Check command redirects
        checkCommandRedirects();
        
        // Check GUI links
        checkGUILinks();
        
        // Check warp destinations
        checkWarpDestinations();
        
        // Check permissions
        checkPermissions();
        
        // Generate report
        generateReport();
        
        SkyblockPlugin.getLogger().info("§a[Maintenance] Broken redirect detection completed!");
    }
    
    /**
     * Überprüft alle Command-Aliases auf Gültigkeit
     */
    private void checkCommandRedirects() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            
            // Get all registered commands
            Map<String, Command> knownCommands = commandMap.getKnownCommands();
            
            for (Map.Entry<String, Command> entry : knownCommands.entrySet()) {
                String alias = entry.getKey();
                Command command = entry.getValue();
                
                // Check if command is from our SkyblockPlugin
                if (command instanceof PluginCommand) {
                    PluginCommand pluginCommand = (PluginCommand) command;
                    if (pluginCommand.getPlugin().equals(SkyblockPlugin)) {
                        // Check if command executor exists
                        if (pluginCommand.getExecutor() == null) {
                            addBrokenRedirect("command_executor", alias, "Command executor is null");
                        }
                        
                        // Check if command has valid permission
                        if (pluginCommand.getPermission() != null && !pluginCommand.getPermission().isEmpty()) {
                            if (!SkyblockPlugin.getServer().getPluginManager().getPermissions().contains(pluginCommand.getPermission())) {
                                addBrokenRedirect("command_permission", alias, "Permission '" + pluginCommand.getPermission() + "' not found");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            SkyblockPlugin.getLogger().warning("Error checking command redirects: " + e.getMessage());
        }
    }
    
    /**
     * Überprüft alle GUI-Links auf Gültigkeit
     */
    private void checkGUILinks() {
        // Check SkyblockPlugin.yml for command definitions
        File pluginFile = new File(SkyblockPlugin.getDataFolder().getParentFile(), "SkyblockPlugin.yml");
        if (pluginFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(pluginFile);
            
            if (config.contains("commands")) {
                for (String commandName : config.getConfigurationSection("commands").getKeys(false)) {
                    String path = "commands." + commandName;
                    
                    // Check if command has description
                    if (!config.contains(path + ".description")) {
                        addBrokenGUILink("missing_description", commandName, "Command description is missing");
                    }
                    
                    // Check if command has usage
                    if (!config.contains(path + ".usage")) {
                        addBrokenGUILink("missing_usage", commandName, "Command usage is missing");
                    }
                    
                    // Check if command has permission
                    if (!config.contains(path + ".permission")) {
                        addBrokenGUILink("missing_permission", commandName, "Command permission is missing");
                    }
                }
            }
        }
        
        // Check for GUI items that reference non-existent commands
        checkGUICommandReferences();
    }
    
    /**
     * Überprüft GUI-Items auf Command-Referenzen
     */
    private void checkGUICommandReferences() {
        // This would need to be implemented based on your GUI system
        // For now, we'll check common GUI patterns
        
        // Check if GUI classes exist and are properly implemented
        String[] guiClasses = {
            "de.noctivag.skyblock.gui.AdvancedGUISystem",
            "de.noctivag.skyblock.gui.UltimateMainMenu",
            "de.noctivag.skyblock.gui.FeatureBookGUI",
            "de.noctivag.skyblock.gui.AdminMenu"
        };
        
        for (String className : guiClasses) {
            try {
                Class.forName(className);
            } catch (ClassNotFoundException e) {
                addBrokenGUILink("missing_gui_class", className, "GUI class not found");
            }
        }
    }
    
    /**
     * Überprüft alle Warp-Destinationen auf Gültigkeit
     */
    private void checkWarpDestinations() {
        // Check warp files
        File warpsFile = new File(SkyblockPlugin.getDataFolder(), "warps.yml");
        if (warpsFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(warpsFile);
            
            if (config.contains("warps")) {
                for (String warpName : config.getConfigurationSection("warps").getKeys(false)) {
                    String path = "warps." + warpName;
                    
                    // Check if warp has location
                    if (!config.contains(path + ".location")) {
                        addBrokenWarp("missing_location", warpName, "Warp location is missing");
                    }
                    
                    // Check if warp has world
                    if (!config.contains(path + ".world")) {
                        addBrokenWarp("missing_world", warpName, "Warp world is missing");
                    }
                    
                    // Check if world exists
                    if (config.contains(path + ".world")) {
                        String worldName = config.getString(path + ".world");
                        if (Bukkit.getWorld(worldName) == null) {
                            addBrokenWarp("invalid_world", warpName, "World '" + worldName + "' does not exist");
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Überprüft alle Permissions auf Gültigkeit
     */
    private void checkPermissions() {
        // Check SkyblockPlugin.yml for permission definitions
        File pluginFile = new File(SkyblockPlugin.getDataFolder().getParentFile(), "SkyblockPlugin.yml");
        if (pluginFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(pluginFile);
            
            if (config.contains("permissions")) {
                for (String permissionName : config.getConfigurationSection("permissions").getKeys(false)) {
                    String path = "permissions." + permissionName;
                    
                    // Check if permission has description
                    if (!config.contains(path + ".description")) {
                        addBrokenPermission("missing_description", permissionName, "Permission description is missing");
                    }
                    
                    // Check if permission has default value
                    if (!config.contains(path + ".default")) {
                        addBrokenPermission("missing_default", permissionName, "Permission default value is missing");
                    }
                }
            }
        }
    }
    
    /**
     * Fügt einen defekten Redirect hinzu
     */
    private void addBrokenRedirect(String type, String name, String reason) {
        brokenRedirects.computeIfAbsent(type, k -> new ArrayList<>()).add(name + " - " + reason);
    }
    
    /**
     * Fügt einen defekten GUI-Link hinzu
     */
    private void addBrokenGUILink(String type, String name, String reason) {
        brokenGUILinks.computeIfAbsent(type, k -> new ArrayList<>()).add(name + " - " + reason);
    }
    
    /**
     * Fügt einen defekten Warp hinzu
     */
    private void addBrokenWarp(String type, String name, String reason) {
        brokenWarps.computeIfAbsent(type, k -> new ArrayList<>()).add(name + " - " + reason);
    }
    
    /**
     * Fügt eine defekte Permission hinzu
     */
    private void addBrokenPermission(String type, String name, String reason) {
        brokenPermissions.computeIfAbsent(type, k -> new ArrayList<>()).add(name + " - " + reason);
    }
    
    /**
     * Generiert einen Bericht über alle gefundenen Probleme
     */
    private void generateReport() {
        SkyblockPlugin.getLogger().info("§6=== BROKEN REDIRECT DETECTION REPORT ===");
        
        // Command redirects
        if (!brokenRedirects.isEmpty()) {
            SkyblockPlugin.getLogger().warning("§cBroken Command Redirects:");
            for (Map.Entry<String, List<String>> entry : brokenRedirects.entrySet()) {
                SkyblockPlugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    SkyblockPlugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // GUI links
        if (!brokenGUILinks.isEmpty()) {
            SkyblockPlugin.getLogger().warning("§cBroken GUI Links:");
            for (Map.Entry<String, List<String>> entry : brokenGUILinks.entrySet()) {
                SkyblockPlugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    SkyblockPlugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // Warps
        if (!brokenWarps.isEmpty()) {
            SkyblockPlugin.getLogger().warning("§cBroken Warps:");
            for (Map.Entry<String, List<String>> entry : brokenWarps.entrySet()) {
                SkyblockPlugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    SkyblockPlugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // Permissions
        if (!brokenPermissions.isEmpty()) {
            SkyblockPlugin.getLogger().warning("§cBroken Permissions:");
            for (Map.Entry<String, List<String>> entry : brokenPermissions.entrySet()) {
                SkyblockPlugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    SkyblockPlugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // Summary
        int totalIssues = brokenRedirects.values().stream().mapToInt(List::size).sum() +
                         brokenGUILinks.values().stream().mapToInt(List::size).sum() +
                         brokenWarps.values().stream().mapToInt(List::size).sum() +
                         brokenPermissions.values().stream().mapToInt(List::size).sum();
        
        if (totalIssues == 0) {
            SkyblockPlugin.getLogger().info("§aNo broken redirects found! System is healthy.");
        } else {
            SkyblockPlugin.getLogger().warning("§cTotal issues found: " + totalIssues);
        }
        
        SkyblockPlugin.getLogger().info("§6==========================================");
    }
    
    /**
     * Automatische Reparatur von einfachen Problemen
     */
    public void autoFix() {
        SkyblockPlugin.getLogger().info("§e[Maintenance] Starting auto-fix for broken redirects...");
        
        int fixedIssues = 0;
        
        // Fix missing command descriptions
        fixedIssues += fixMissingCommandDescriptions();
        
        // Fix missing warp worlds
        fixedIssues += fixMissingWarpWorlds();
        
        // Fix missing permissions
        fixedIssues += fixMissingPermissions();
        
        SkyblockPlugin.getLogger().info("§a[Maintenance] Auto-fix completed! Fixed " + fixedIssues + " issues.");
    }
    
    /**
     * Repariert fehlende Command-Beschreibungen
     */
    private int fixMissingCommandDescriptions() {
        int fixed = 0;
        
        try {
            File pluginFile = new File(SkyblockPlugin.getDataFolder().getParentFile(), "SkyblockPlugin.yml");
            if (pluginFile.exists()) {
                FileConfiguration config = YamlConfiguration.loadConfiguration(pluginFile);
                
                if (config.contains("commands")) {
                    for (String commandName : config.getConfigurationSection("commands").getKeys(false)) {
                        String path = "commands." + commandName;
                        
                        if (!config.contains(path + ".description")) {
                            config.set(path + ".description", "SkyblockPlugin command: " + commandName);
                            fixed++;
                        }
                    }
                    
                    if (fixed > 0) {
                        config.save(pluginFile);
                    }
                }
            }
        } catch (Exception e) {
            SkyblockPlugin.getLogger().warning("Error fixing command descriptions: " + e.getMessage());
        }
        
        return fixed;
    }
    
    /**
     * Repariert fehlende Warp-Welten
     */
    private int fixMissingWarpWorlds() {
        int fixed = 0;
        
        try {
            File warpsFile = new File(SkyblockPlugin.getDataFolder(), "warps.yml");
            if (warpsFile.exists()) {
                FileConfiguration config = YamlConfiguration.loadConfiguration(warpsFile);
                
                if (config.contains("warps")) {
                    for (String warpName : config.getConfigurationSection("warps").getKeys(false)) {
                        String path = "warps." + warpName;
                        
                        if (!config.contains(path + ".world")) {
                            config.set(path + ".world", "world");
                            fixed++;
                        }
                    }
                    
                    if (fixed > 0) {
                        config.save(warpsFile);
                    }
                }
            }
        } catch (Exception e) {
            SkyblockPlugin.getLogger().warning("Error fixing warp worlds: " + e.getMessage());
        }
        
        return fixed;
    }
    
    /**
     * Repariert fehlende Permissions
     */
    private int fixMissingPermissions() {
        int fixed = 0;
        
        try {
            File pluginFile = new File(SkyblockPlugin.getDataFolder().getParentFile(), "SkyblockPlugin.yml");
            if (pluginFile.exists()) {
                FileConfiguration config = YamlConfiguration.loadConfiguration(pluginFile);
                
                if (config.contains("permissions")) {
                    for (String permissionName : config.getConfigurationSection("permissions").getKeys(false)) {
                        String path = "permissions." + permissionName;
                        
                        if (!config.contains(path + ".description")) {
                            config.set(path + ".description", "SkyblockPlugin permission: " + permissionName);
                            fixed++;
                        }
                        
                        if (!config.contains(path + ".default")) {
                            config.set(path + ".default", "false");
                            fixed++;
                        }
                    }
                    
                    if (fixed > 0) {
                        config.save(pluginFile);
                    }
                }
            }
        } catch (Exception e) {
            SkyblockPlugin.getLogger().warning("Error fixing permissions: " + e.getMessage());
        }
        
        return fixed;
    }
    
    /**
     * Gibt Statistiken über gefundene Probleme zurück
     */
    public Map<String, Integer> getStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("command_redirects", brokenRedirects.values().stream().mapToInt(List::size).sum());
        stats.put("gui_links", brokenGUILinks.values().stream().mapToInt(List::size).sum());
        stats.put("warps", brokenWarps.values().stream().mapToInt(List::size).sum());
        stats.put("permissions", brokenPermissions.values().stream().mapToInt(List::size).sum());
        
        int total = stats.values().stream().mapToInt(Integer::intValue).sum();
        stats.put("total", total);
        
        return stats;
    }
    
    /**
     * Erstellt ein GUI-Item für den Maintenance-Report
     */
    public ItemStack createReportItem() {
        ItemStack item = new ItemStack(org.bukkit.Material.REDSTONE_TORCH);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            Map<String, Integer> stats = getStatistics();
            int totalIssues = stats.get("total");
            
            meta.displayName(Component.text("§c§lBroken Redirects Report"));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Total Issues: §c" + totalIssues);
            lore.add("§7Command Redirects: §e" + stats.get("command_redirects"));
            lore.add("§7GUI Links: §e" + stats.get("gui_links"));
            lore.add("§7Warps: §e" + stats.get("warps"));
            lore.add("§7Permissions: §e" + stats.get("permissions"));
            lore.add("");
            lore.add("§eClick to view detailed report");
            lore.add("§eRight-click to auto-fix");
            
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        
        return item;
    }
}
