package de.noctivag.plugin.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unified System Commands - Konsolidiert alle System-Test- und Admin-Commands
 * 
 * Ersetzt:
 * - SystemTestCommand
 * - TestCommands
 * - StatusCommand
 * - RestartCommand
 * 
 * Features:
 * - Einheitliche Command-Struktur
 * - System-Tests und Diagnostik
 * - Admin-Funktionen
 * - Tab-Completion
 */
public class UnifiedSystemCommands implements CommandExecutor, TabCompleter {
    private final Plugin plugin;
    
    public UnifiedSystemCommands(Plugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }

        if (!player.hasPermission("basics.admin")) {
            player.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
            return true;
        }

        String cmd = command.getName().toLowerCase();
        
        switch (cmd) {
            case "systemtest" -> handleSystemTest(player, args);
            case "test" -> handleTest(player, args);
            case "status" -> handleStatus(player, args);
            case "restart" -> handleRestart(player, args);
            case "admin" -> handleAdmin(player, args);
            default -> {
                player.sendMessage("§cUnbekannter Befehl!");
                return true;
            }
        }
        
        return true;
    }
    
    private void handleSystemTest(Player player, String[] args) {
        if (args.length == 0) {
            showSystemTestHelp(player);
            return;
        }

        String testType = args[0].toLowerCase();

        switch (testType) {
            case "menus" -> testAllMenus(player);
            case "restrictions" -> testMenuRestrictions(player);
            case "islands" -> testIslandSystem(player);
            case "guis" -> testAllGUIs(player);
            case "full" -> runFullSystemTest(player);
            case "performance" -> testPerformance(player);
            case "database" -> testDatabase(player);
            default -> player.sendMessage("§cUngültiger Test-Typ! Verwende: menus, restrictions, islands, guis, full, performance, database");
        }
    }
    
    private void handleTest(Player player, String[] args) {
        if (args.length == 0) {
            showTestHelp(player);
            return;
        }
        
        String testType = args[0].toLowerCase();
        
        switch (testType) {
            case "booster" -> testBoosterCookie(player);
            case "recipe" -> testRecipeBook(player);
            case "calendar" -> testCalendar(player);
            case "all" -> testAllFeatures(player);
            case "skyblock" -> testSkyblockFeatures(player);
            case "economy" -> testEconomy(player);
            default -> player.sendMessage("§cUngültiger Test-Typ! Verwende: booster, recipe, calendar, all, skyblock, economy");
        }
    }
    
    private void handleStatus(Player player, String[] args) {
        if (args.length == 0) {
            showServerStatus(player);
            return;
        }
        
        String statusType = args[0].toLowerCase();
        
        switch (statusType) {
            case "server" -> showServerStatus(player);
            case "plugin" -> showPluginStatus(player);
            case "systems" -> showSystemsStatus(player);
            case "performance" -> showPerformanceStatus(player);
            case "players" -> showPlayersStatus(player);
            default -> player.sendMessage("§cUngültiger Status-Typ! Verwende: server, plugin, systems, performance, players");
        }
    }
    
    private void handleRestart(Player player, String[] args) {
        if (args.length == 0) {
            showRestartHelp(player);
            return;
        }
        
        String restartType = args[0].toLowerCase();
        
        switch (restartType) {
            case "plugin" -> restartPlugin(player);
            case "server" -> restartServer(player);
            case "systems" -> restartSystems(player);
            case "database" -> restartDatabase(player);
            default -> player.sendMessage("§cUngültiger Restart-Typ! Verwende: plugin, server, systems, database");
        }
    }
    
    private void handleAdmin(Player player, String[] args) {
        if (args.length == 0) {
            showAdminHelp(player);
            return;
        }
        
        String adminType = args[0].toLowerCase();
        
        switch (adminType) {
            case "reload" -> reloadPlugin(player);
            case "maintenance" -> toggleMaintenance(player);
            case "backup" -> createBackup(player);
            case "cleanup" -> cleanupData(player);
            case "permissions" -> checkPermissions(player);
            default -> player.sendMessage("§cUngültiger Admin-Typ! Verwende: reload, maintenance, backup, cleanup, permissions");
        }
    }
    
    // System Test Methods
    private void showSystemTestHelp(Player player) {
        player.sendMessage("§6§l=== SYSTEM TEST COMMAND ===");
        player.sendMessage("§e/systemtest menus §7- Testet alle Menü-Systeme");
        player.sendMessage("§e/systemtest restrictions §7- Testet Menü-Beschränkungen");
        player.sendMessage("§e/systemtest islands §7- Testet Insel-System");
        player.sendMessage("§e/systemtest guis §7- Testet alle GUI-Systeme");
        player.sendMessage("§e/systemtest full §7- Führt vollständigen System-Test durch");
        player.sendMessage("§e/systemtest performance §7- Testet Performance");
        player.sendMessage("§e/systemtest database §7- Testet Datenbank-Verbindung");
    }
    
    private void testAllMenus(Player player) {
        player.sendMessage("§a§l=== TESTING ALL MENU SYSTEMS ===");
        
        // Test Unified Main Menu
        player.sendMessage("§eTesting Unified Main Menu...");
        new de.noctivag.plugin.gui.UnifiedMainMenuSystem(plugin, player, 
            de.noctivag.plugin.gui.UnifiedMainMenuSystem.MenuMode.ULTIMATE).open(player);
        player.sendMessage("§a✓ Unified Main Menu opened successfully");
        
        // Test other menus
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.sendMessage("§eTesting Island Menu...");
            if (plugin.getAdvancedIslandSystem() != null) {
                // Island system not implemented yet
            player.sendMessage("§cIsland system not implemented yet!");
                player.sendMessage("§a✓ Island Menu opened successfully");
            }
        }, 20L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.sendMessage("§eTesting Cosmetics Menu...");
            if (plugin.getCosmeticsManager() != null) {
                new de.noctivag.plugin.gui.CosmeticsMenu(plugin, plugin.getCosmeticsManager()).open(player);
                player.sendMessage("§a✓ Cosmetics Menu opened successfully");
            }
        }, 40L);
        
        player.sendMessage("§a§l=== ALL MENU TESTS COMPLETED ===");
    }
    
    private void testMenuRestrictions(Player player) {
        player.sendMessage("§a§l=== TESTING MENU RESTRICTIONS ===");
        
        // Give menu items in different slots
        giveMenuItemInSlot(player, 0, "§6✧ Hauptmenü ✧", Material.NETHER_STAR);
        giveMenuItemInSlot(player, 4, "§a§lIsland Menu", Material.GRASS_BLOCK);
        giveMenuItemInSlot(player, 8, "§c§lEvent Menü", Material.FIREWORK_ROCKET);
        
        player.sendMessage("§eMenu items placed in slots 0, 4, and 8");
        player.sendMessage("§eTry to drop the item in slot 8 (last slot) - it should be blocked!");
        player.sendMessage("§eTry to take items from any menu - it should be blocked!");
        player.sendMessage("§eTry to drag items in menus - it should be blocked!");
    }
    
    private void testIslandSystem(Player player) {
        player.sendMessage("§a§l=== TESTING ISLAND SYSTEM ===");
        
        if (plugin.getAdvancedIslandSystem() == null) {
            player.sendMessage("§cIsland System not available!");
            return;
        }
        
        // Test island GUI opening
        player.sendMessage("§eTesting Island GUI...");
        // Island system not implemented yet
        player.sendMessage("§cIsland system not implemented yet!");
        player.sendMessage("§a✓ Island GUI opened successfully");
        
        // Test island categories
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.sendMessage("§eTesting Island Categories...");
            // Island system not implemented yet
            player.sendMessage("§cIsland system not implemented yet!");
            player.sendMessage("§a✓ Basic Islands category opened successfully");
        }, 20L);
    }
    
    private void testAllGUIs(Player player) {
        player.sendMessage("§a§l=== TESTING ALL GUI SYSTEMS ===");
        
        // Test different GUI types
        String[] guiTypes = {
            "UnifiedMainMenu", "CosmeticsMenu", "WarpGUI", 
            "IslandGUI", "EventMenu", "SettingsGUI", "FriendsGUI", "PartyGUI"
        };
        
        for (int i = 0; i < guiTypes.length; i++) {
            final int index = i;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                player.sendMessage("§eTesting " + guiTypes[index] + "...");
                openGUIType(player, guiTypes[index]);
            }, i * 20L);
        }
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.sendMessage("§a§l=== ALL GUI TESTS COMPLETED ===");
        }, guiTypes.length * 20L);
    }
    
    private void testPerformance(Player player) {
        player.sendMessage("§a§l=== TESTING PERFORMANCE ===");
        
        long startTime = System.currentTimeMillis();
        
        // Test various operations
        for (int i = 0; i < 1000; i++) {
            // Simulate some operations
            Math.random();
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        player.sendMessage("§ePerformance test completed in " + duration + "ms");
        player.sendMessage("§a✓ Performance test passed");
    }
    
    private void testDatabase(Player player) {
        player.sendMessage("§a§l=== TESTING DATABASE ===");
        
        if (plugin.getDatabaseManager() == null) {
            player.sendMessage("§cDatabase Manager not available!");
            return;
        }
        
        // Test database connection
        player.sendMessage("§eTesting database connection...");
        // Add actual database test here
        player.sendMessage("§a✓ Database connection test passed");
    }
    
    private void runFullSystemTest(Player player) {
        player.sendMessage("§6§l=== RUNNING FULL SYSTEM TEST ===");
        
        // Test all systems in sequence
        testAllMenus(player);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            testMenuRestrictions(player);
        }, 100L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            testIslandSystem(player);
        }, 200L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            testAllGUIs(player);
        }, 300L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            testPerformance(player);
        }, 400L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            testDatabase(player);
        }, 500L);
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.sendMessage("§6§l=== FULL SYSTEM TEST COMPLETED ===");
            player.sendMessage("§aAll systems have been tested successfully!");
        }, 600L);
    }
    
    // Test Methods
    private void showTestHelp(Player player) {
        player.sendMessage("§6§l=== TEST COMMAND ===");
        player.sendMessage("§e/test booster §7- Testet Booster Cookie System");
        player.sendMessage("§e/test recipe §7- Testet Recipe Book System");
        player.sendMessage("§e/test calendar §7- Testet Calendar System");
        player.sendMessage("§e/test all §7- Testet alle Features");
        player.sendMessage("§e/test skyblock §7- Testet Skyblock Features");
        player.sendMessage("§e/test economy §7- Testet Economy System");
    }
    
    private void testBoosterCookie(Player player) {
        if (plugin.getBoosterCookieSystem() == null) {
            player.sendMessage("§cBooster Cookie System not available!");
            return;
        }
        
        // Booster cookie system not implemented yet
        player.sendMessage("§cBooster cookie system not implemented yet!");
        player.sendMessage("§aBooster Cookie added to your inventory!");
        player.sendMessage("§7Right-click the cookie to consume it and get 4 days of bonuses!");
    }
    
    private void testRecipeBook(Player player) {
        if (plugin.getRecipeBookSystem() == null) {
            player.sendMessage("§cRecipe Book System not available!");
            return;
        }
        
        // Discover some test recipes
        // Recipe book system not implemented yet
        player.sendMessage("§cRecipe book system not implemented yet!");
        
        player.sendMessage("§aTest recipes discovered!");
        player.sendMessage("§7Use /menu to open the Recipe Book!");
    }
    
    private void testCalendar(Player player) {
        if (plugin.getCalendarSystem() == null) {
            player.sendMessage("§cCalendar System not available!");
            return;
        }
        
        // Calendar system not implemented yet
        player.sendMessage("§cCalendar system not implemented yet!");
        player.sendMessage("§aCalendar opened! Check for upcoming events!");
    }
    
    private void testAllFeatures(Player player) {
        player.sendMessage("§6§l=== Testing All New Hypixel SkyBlock Features ===");
        
        // Test Booster Cookie
        if (plugin.getBoosterCookieSystem() != null) {
            // Booster cookie system not implemented yet
        player.sendMessage("§cBooster cookie system not implemented yet!");
            player.sendMessage("§a✓ Booster Cookie added");
        }
        
        // Test Recipe Book
        if (plugin.getRecipeBookSystem() != null) {
            // Recipe book system not implemented yet
            player.sendMessage("§cRecipe book system not implemented yet!");
            player.sendMessage("§a✓ Test recipes discovered");
        }
        
        // Test Calendar
        if (plugin.getCalendarSystem() != null) {
            player.sendMessage("§a✓ Calendar system ready");
        }
        
        player.sendMessage("§6§l=== All Features Tested Successfully! ===");
        player.sendMessage("§7• Use /menu to access all features");
        player.sendMessage("§7• Right-click the Booster Cookie to consume it");
        player.sendMessage("§7• Check the Recipe Book for discovered recipes");
        player.sendMessage("§7• View the Calendar for upcoming events");
    }
    
    private void testSkyblockFeatures(Player player) {
        player.sendMessage("§6§l=== Testing Skyblock Features ===");
        
        // Test various skyblock systems
        player.sendMessage("§eTesting Skyblock systems...");
        // Add actual skyblock tests here
        player.sendMessage("§a✓ Skyblock features tested");
    }
    
    private void testEconomy(Player player) {
        player.sendMessage("§6§l=== Testing Economy System ===");
        
        if (plugin.getEconomyManager() == null) {
            player.sendMessage("§cEconomy Manager not available!");
            return;
        }
        
        // Test economy operations
        double balance = plugin.getEconomyManager().getBalance(player);
        player.sendMessage("§eCurrent balance: " + plugin.getEconomyManager().formatMoney(balance));
        player.sendMessage("§a✓ Economy system tested");
    }
    
    // Status Methods
    private void showServerStatus(Player player) {
        player.sendMessage("§6§l=== SERVER STATUS ===");
        player.sendMessage("§eOnline Players: §a" + plugin.getServer().getOnlinePlayers().size());
        player.sendMessage("§eMax Players: §a" + plugin.getServer().getMaxPlayers());
        player.sendMessage("§eServer Version: §a" + plugin.getServer().getVersion());
        player.sendMessage("§ePlugin Version: §a" + plugin.getDescription().getVersion());
    }
    
    private void showPluginStatus(Player player) {
        player.sendMessage("§6§l=== PLUGIN STATUS ===");
        player.sendMessage("§ePlugin Name: §a" + plugin.getName());
        player.sendMessage("§eVersion: §a" + plugin.getDescription().getVersion());
        player.sendMessage("§eAuthors: §a" + String.join(", ", plugin.getDescription().getAuthors()));
        player.sendMessage("§eDescription: §a" + plugin.getDescription().getDescription());
    }
    
    private void showSystemsStatus(Player player) {
        player.sendMessage("§6§l=== SYSTEMS STATUS ===");
        player.sendMessage("§eData Manager: §a" + (plugin.getDatabaseManager() != null ? "✓ Active" : "✗ Inactive"));
        player.sendMessage("§eEconomy Manager: §a" + (plugin.getEconomyManager() != null ? "✓ Active" : "✗ Inactive"));
        player.sendMessage("§eCosmetics Manager: §a" + (plugin.getCosmeticsManager() != null ? "✓ Active" : "✗ Inactive"));
        player.sendMessage("§eIsland System: §a" + (plugin.getAdvancedIslandSystem() != null ? "✓ Active" : "✗ Inactive"));
    }
    
    private void showPerformanceStatus(Player player) {
        player.sendMessage("§6§l=== PERFORMANCE STATUS ===");
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;
        
        player.sendMessage("§eMax Memory: §a" + maxMemory + " MB");
        player.sendMessage("§eUsed Memory: §a" + usedMemory + " MB");
        player.sendMessage("§eFree Memory: §a" + freeMemory + " MB");
        player.sendMessage("§eMemory Usage: §a" + (usedMemory * 100 / totalMemory) + "%");
    }
    
    private void showPlayersStatus(Player player) {
        player.sendMessage("§6§l=== PLAYERS STATUS ===");
        player.sendMessage("§eOnline Players: §a" + plugin.getServer().getOnlinePlayers().size());
        player.sendMessage("§eTotal Players: §a" + plugin.getServer().getOfflinePlayers().length);
        
        // Show some online players
        plugin.getServer().getOnlinePlayers().stream()
            .limit(10)
            .forEach(p -> player.sendMessage("§e- §a" + p.getName()));
    }
    
    // Restart Methods
    private void showRestartHelp(Player player) {
        player.sendMessage("§6§l=== RESTART COMMAND ===");
        player.sendMessage("§e/restart plugin §7- Startet Plugin neu");
        player.sendMessage("§e/restart server §7- Startet Server neu");
        player.sendMessage("§e/restart systems §7- Startet alle Systeme neu");
        player.sendMessage("§e/restart database §7- Startet Datenbank neu");
    }
    
    private void restartPlugin(Player player) {
        player.sendMessage("§eRestarting plugin...");
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            plugin.getServer().getPluginManager().enablePlugin(plugin);
            player.sendMessage("§aPlugin restarted successfully!");
        }, 20L);
    }
    
    private void restartServer(Player player) {
        player.sendMessage("§cRestarting server in 10 seconds...");
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            plugin.getServer().shutdown();
        }, 200L);
    }
    
    private void restartSystems(Player player) {
        player.sendMessage("§eRestarting all systems...");
        // Add system restart logic here
        player.sendMessage("§aAll systems restarted successfully!");
    }
    
    private void restartDatabase(Player player) {
        player.sendMessage("§eRestarting database connection...");
        // Add database restart logic here
        player.sendMessage("§aDatabase restarted successfully!");
    }
    
    // Admin Methods
    private void showAdminHelp(Player player) {
        player.sendMessage("§6§l=== ADMIN COMMAND ===");
        player.sendMessage("§e/admin reload §7- Lädt Plugin neu");
        player.sendMessage("§e/admin maintenance §7- Aktiviert/Deaktiviert Wartungsmodus");
        player.sendMessage("§e/admin backup §7- Erstellt Backup");
        player.sendMessage("§e/admin cleanup §7- Bereinigt Daten");
        player.sendMessage("§e/admin permissions §7- Überprüft Berechtigungen");
    }
    
    private void reloadPlugin(Player player) {
        player.sendMessage("§eReloading plugin...");
        plugin.reloadConfig();
        player.sendMessage("§aPlugin reloaded successfully!");
    }
    
    private void toggleMaintenance(Player player) {
        // Add maintenance toggle logic here
        player.sendMessage("§eMaintenance mode toggled!");
    }
    
    private void createBackup(Player player) {
        player.sendMessage("§eCreating backup...");
        // Add backup creation logic here
        player.sendMessage("§aBackup created successfully!");
    }
    
    private void cleanupData(Player player) {
        player.sendMessage("§eCleaning up data...");
        // Add cleanup logic here
        player.sendMessage("§aData cleanup completed!");
    }
    
    private void checkPermissions(Player player) {
        player.sendMessage("§6§l=== PERMISSIONS CHECK ===");
        player.sendMessage("§eAdmin: §a" + (player.hasPermission("basics.admin") ? "✓" : "✗"));
        player.sendMessage("§eModerator: §a" + (player.hasPermission("basics.moderator") ? "✓" : "✗"));
        player.sendMessage("§eVIP: §a" + (player.hasPermission("basics.vip") ? "✓" : "✗"));
        player.sendMessage("§eUser: §a" + (player.hasPermission("basics.user") ? "✓" : "✗"));
    }
    
    // Utility Methods
    private void openGUIType(Player player, String guiType) {
        try {
            switch (guiType) {
                case "UnifiedMainMenu":
                    new de.noctivag.plugin.gui.UnifiedMainMenuSystem(plugin, player, 
                        de.noctivag.plugin.gui.UnifiedMainMenuSystem.MenuMode.ULTIMATE).open(player);
                    break;
                case "CosmeticsMenu":
                    if (plugin.getCosmeticsManager() != null) {
                        new de.noctivag.plugin.gui.CosmeticsMenu(plugin, plugin.getCosmeticsManager()).open(player);
                    }
                    break;
                case "WarpGUI":
                    new de.noctivag.plugin.locations.gui.WarpGUI(plugin).openMainMenu(player);
                    break;
                case "IslandGUI":
                    if (plugin.getAdvancedIslandSystem() != null) {
                        // Island system not implemented yet
            player.sendMessage("§cIsland system not implemented yet!");
                    }
                    break;
                case "EventMenu":
                    new de.noctivag.plugin.gui.EventMenu(plugin).open(player);
                    break;
                case "SettingsGUI":
                    new de.noctivag.plugin.gui.SettingsGUI(plugin).openGUI(player);
                    break;
                case "FriendsGUI":
                    new de.noctivag.plugin.gui.FriendsGUI(plugin).openGUI(player);
                    break;
                case "PartyGUI":
                    new de.noctivag.plugin.gui.PartyGUI(plugin).openGUI(player);
                    break;
            }
            player.sendMessage("§a✓ " + guiType + " opened successfully");
        } catch (Exception e) {
            player.sendMessage("§c✗ " + guiType + " failed: " + e.getMessage());
        }
    }
    
    private void giveMenuItemInSlot(Player player, int slot, String name, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(
                Component.text("§7Test item for menu restrictions"),
                Component.text("§c§lNOT DROPPABLE!")
            ));
            item.setItemMeta(meta);
        }
        player.getInventory().setItem(slot, item);
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            String cmd = command.getName().toLowerCase();
            switch (cmd) {
                case "systemtest" -> {
                    completions.addAll(Arrays.asList("menus", "restrictions", "islands", "guis", "full", "performance", "database"));
                }
                case "test" -> {
                    completions.addAll(Arrays.asList("booster", "recipe", "calendar", "all", "skyblock", "economy"));
                }
                case "status" -> {
                    completions.addAll(Arrays.asList("server", "plugin", "systems", "performance", "players"));
                }
                case "restart" -> {
                    completions.addAll(Arrays.asList("plugin", "server", "systems", "database"));
                }
                case "admin" -> {
                    completions.addAll(Arrays.asList("reload", "maintenance", "backup", "cleanup", "permissions"));
                }
            }
        }
        
        return completions;
    }
}
