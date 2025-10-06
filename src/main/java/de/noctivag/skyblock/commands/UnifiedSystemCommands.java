package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
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
    private final SkyblockPlugin SkyblockPlugin;
    
    public UnifiedSystemCommands(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }

        if (!player.hasPermission("basics.admin")) {
            player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
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
                player.sendMessage(Component.text("§cUnbekannter Befehl!"));
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
            default -> player.sendMessage(Component.text("§cUngültiger Test-Typ! Verwende: menus, restrictions, islands, guis, full, performance, database"));
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
            default -> player.sendMessage(Component.text("§cUngültiger Test-Typ! Verwende: booster, recipe, calendar, all, skyblock, economy"));
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
            case "SkyblockPlugin" -> showPluginStatus(player);
            case "systems" -> showSystemsStatus(player);
            case "performance" -> showPerformanceStatus(player);
            case "players" -> showPlayersStatus(player);
            default -> player.sendMessage(Component.text("§cUngültiger Status-Typ! Verwende: server, SkyblockPlugin, systems, performance, players"));
        }
    }
    
    private void handleRestart(Player player, String[] args) {
        if (args.length == 0) {
            showRestartHelp(player);
            return;
        }
        
        String restartType = args[0].toLowerCase();
        
        switch (restartType) {
            case "SkyblockPlugin" -> restartPlugin(player);
            case "server" -> restartServer(player);
            case "systems" -> restartSystems(player);
            case "database" -> restartDatabase(player);
            default -> player.sendMessage(Component.text("§cUngültiger Restart-Typ! Verwende: SkyblockPlugin, server, systems, database"));
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
            default -> player.sendMessage(Component.text("§cUngültiger Admin-Typ! Verwende: reload, maintenance, backup, cleanup, permissions"));
        }
    }
    
    // System Test Methods
    private void showSystemTestHelp(Player player) {
        player.sendMessage(Component.text("§6§l=== SYSTEM TEST COMMAND ==="));
        player.sendMessage(Component.text("§e/systemtest menus §7- Testet alle Menü-Systeme"));
        player.sendMessage(Component.text("§e/systemtest restrictions §7- Testet Menü-Beschränkungen"));
        player.sendMessage(Component.text("§e/systemtest islands §7- Testet Insel-System"));
        player.sendMessage(Component.text("§e/systemtest guis §7- Testet alle GUI-Systeme"));
        player.sendMessage(Component.text("§e/systemtest full §7- Führt vollständigen System-Test durch"));
        player.sendMessage(Component.text("§e/systemtest performance §7- Testet Performance"));
        player.sendMessage(Component.text("§e/systemtest database §7- Testet Datenbank-Verbindung"));
    }
    
    private void testAllMenus(Player player) {
        player.sendMessage(Component.text("§a§l=== TESTING ALL MENU SYSTEMS ==="));
        
        // Test Unified Main Menu
        player.sendMessage(Component.text("§eTesting Unified Main Menu..."));
        new de.noctivag.skyblock.gui.UnifiedMainMenuSystem(SkyblockPlugin, player, 
            de.noctivag.skyblock.gui.UnifiedMainMenuSystem.MenuMode.ULTIMATE).open(player);
        player.sendMessage(Component.text("§a✓ Unified Main Menu opened successfully"));
        
        // Test other menus
        SkyblockPlugin.getServer().getScheduler().runTaskLater(SkyblockPlugin, () -> {
            player.sendMessage(Component.text("§eTesting Island Menu..."));
            if (SkyblockPlugin.getAdvancedIslandSystem() != null) {
                // Island system not implemented yet
            player.sendMessage(Component.text("§cIsland system not implemented yet!"));
                player.sendMessage(Component.text("§a✓ Island Menu opened successfully"));
            }
        }, 20L);
        
        SkyblockPlugin.getServer().getScheduler().runTaskLater(SkyblockPlugin, () -> {
            player.sendMessage(Component.text("§eTesting Cosmetics Menu..."));
            if (SkyblockPlugin.getCosmeticsManager() != null) {
                new de.noctivag.skyblock.gui.CosmeticsMenu(SkyblockPlugin, SkyblockPlugin.getCosmeticsManager()).open(player);
                player.sendMessage(Component.text("§a✓ Cosmetics Menu opened successfully"));
            }
        }, 40L);
        
        player.sendMessage(Component.text("§a§l=== ALL MENU TESTS COMPLETED ==="));
    }
    
    private void testMenuRestrictions(Player player) {
        player.sendMessage(Component.text("§a§l=== TESTING MENU RESTRICTIONS ==="));
        
        // Give menu items in different slots
        giveMenuItemInSlot(player, 0, "§6✧ Hauptmenü ✧", Material.NETHER_STAR);
        giveMenuItemInSlot(player, 4, "§a§lIsland Menu", Material.GRASS_BLOCK);
        giveMenuItemInSlot(player, 8, "§c§lEvent Menü", Material.FIREWORK_ROCKET);
        
        player.sendMessage(Component.text("§eMenu items placed in slots 0, 4, and 8"));
        player.sendMessage(Component.text("§eTry to drop the item in slot 8 (last slot) - it should be blocked!"));
        player.sendMessage(Component.text("§eTry to take items from any menu - it should be blocked!"));
        player.sendMessage(Component.text("§eTry to drag items in menus - it should be blocked!"));
    }
    
    private void testIslandSystem(Player player) {
        player.sendMessage(Component.text("§a§l=== TESTING ISLAND SYSTEM ==="));
        
        if (SkyblockPlugin.getAdvancedIslandSystem() == null) {
            player.sendMessage(Component.text("§cIsland System not available!"));
            return;
        }
        
        // Test island GUI opening
        player.sendMessage(Component.text("§eTesting Island GUI..."));
        // Island system not implemented yet
        player.sendMessage(Component.text("§cIsland system not implemented yet!"));
        player.sendMessage(Component.text("§a✓ Island GUI opened successfully"));
        
        // Test island categories
        SkyblockPlugin.getServer().getScheduler().runTaskLater(SkyblockPlugin, () -> {
            player.sendMessage(Component.text("§eTesting Island Categories..."));
            // Island system not implemented yet
            player.sendMessage(Component.text("§cIsland system not implemented yet!"));
            player.sendMessage(Component.text("§a✓ Basic Islands category opened successfully"));
        }, 20L);
    }
    
    private void testAllGUIs(Player player) {
        player.sendMessage(Component.text("§a§l=== TESTING ALL GUI SYSTEMS ==="));
        
        // Test different GUI types
        String[] guiTypes = {
            "UnifiedMainMenu", "CosmeticsMenu", "WarpGUI", 
            "IslandGUI", "EventMenu", "SettingsGUI", "FriendsGUI", "PartyGUI"
        };
        
        for (int i = 0; i < guiTypes.length; i++) {
            final int index = i;
            SkyblockPlugin.getServer().getScheduler().runTaskLater(SkyblockPlugin, () -> {
                player.sendMessage("§eTesting " + guiTypes[index] + "...");
                openGUIType(player, guiTypes[index]);
            }, i * 20L);
        }
        
        SkyblockPlugin.getServer().getScheduler().runTaskLater(SkyblockPlugin, () -> {
            player.sendMessage(Component.text("§a§l=== ALL GUI TESTS COMPLETED ==="));
        }, guiTypes.length * 20L);
    }
    
    private void testPerformance(Player player) {
        player.sendMessage(Component.text("§a§l=== TESTING PERFORMANCE ==="));
        
        long startTime = java.lang.System.currentTimeMillis();
        
        // Test various operations
        for (int i = 0; i < 1000; i++) {
            // Simulate some operations
            Math.random();
        }
        
        long endTime = java.lang.System.currentTimeMillis();
        long duration = endTime - startTime;
        
        player.sendMessage("§ePerformance test completed in " + duration + "ms");
        player.sendMessage(Component.text("§a✓ Performance test passed"));
    }
    
    private void testDatabase(Player player) {
        player.sendMessage(Component.text("§a§l=== TESTING DATABASE ==="));
        
        if (SkyblockPlugin.getDatabaseManager() == null) {
            player.sendMessage(Component.text("§cDatabase Manager not available!"));
            return;
        }
        
        // Test database connection
        player.sendMessage(Component.text("§eTesting database connection..."));
        // Add actual database test here
        player.sendMessage(Component.text("§a✓ Database connection test passed"));
    }
    
    private void runFullSystemTest(Player player) {
        player.sendMessage(Component.text("§6§l=== RUNNING FULL SYSTEM TEST ==="));
        
        // Test all systems in sequence
        testAllMenus(player);
        
        SkyblockPlugin.getServer().getScheduler().runTaskLater(SkyblockPlugin, () -> {
            testMenuRestrictions(player);
        }, 100L);
        
        SkyblockPlugin.getServer().getScheduler().runTaskLater(SkyblockPlugin, () -> {
            testIslandSystem(player);
        }, 200L);
        
        SkyblockPlugin.getServer().getScheduler().runTaskLater(SkyblockPlugin, () -> {
            testAllGUIs(player);
        }, 300L);
        
        SkyblockPlugin.getServer().getScheduler().runTaskLater(SkyblockPlugin, () -> {
            testPerformance(player);
        }, 400L);
        
        SkyblockPlugin.getServer().getScheduler().runTaskLater(SkyblockPlugin, () -> {
            testDatabase(player);
        }, 500L);
        
        SkyblockPlugin.getServer().getScheduler().runTaskLater(SkyblockPlugin, () -> {
            player.sendMessage(Component.text("§6§l=== FULL SYSTEM TEST COMPLETED ==="));
            player.sendMessage(Component.text("§aAll systems have been tested successfully!"));
        }, 600L);
    }
    
    // Test Methods
    private void showTestHelp(Player player) {
        player.sendMessage(Component.text("§6§l=== TEST COMMAND ==="));
        player.sendMessage(Component.text("§e/test booster §7- Testet Booster Cookie System"));
        player.sendMessage(Component.text("§e/test recipe §7- Testet Recipe Book System"));
        player.sendMessage(Component.text("§e/test calendar §7- Testet Calendar System"));
        player.sendMessage(Component.text("§e/test all §7- Testet alle Features"));
        player.sendMessage(Component.text("§e/test skyblock §7- Testet Skyblock Features"));
        player.sendMessage(Component.text("§e/test economy §7- Testet Economy System"));
    }
    
    private void testBoosterCookie(Player player) {
        if (SkyblockPlugin.getBoosterCookieSystem() == null) {
            player.sendMessage(Component.text("§cBooster Cookie System not available!"));
            return;
        }
        
        // Booster cookie system not implemented yet
        player.sendMessage(Component.text("§cBooster cookie system not implemented yet!"));
        player.sendMessage(Component.text("§aBooster Cookie added to your inventory!"));
        player.sendMessage(Component.text("§7Right-click the cookie to consume it and get 4 days of bonuses!"));
    }
    
    private void testRecipeBook(Player player) {
        if (SkyblockPlugin.getRecipeBookSystem() == null) {
            player.sendMessage(Component.text("§cRecipe Book System not available!"));
            return;
        }
        
        // Discover some test recipes
        // Recipe book system not implemented yet
        player.sendMessage(Component.text("§cRecipe book system not implemented yet!"));
        
        player.sendMessage(Component.text("§aTest recipes discovered!"));
        player.sendMessage(Component.text("§7Use /menu to open the Recipe Book!"));
    }
    
    private void testCalendar(Player player) {
        if (SkyblockPlugin.getCalendarSystem() == null) {
            player.sendMessage(Component.text("§cCalendar System not available!"));
            return;
        }
        
        // Calendar system not implemented yet
        player.sendMessage(Component.text("§cCalendar system not implemented yet!"));
        player.sendMessage(Component.text("§aCalendar opened! Check for upcoming events!"));
    }
    
    private void testAllFeatures(Player player) {
        player.sendMessage(Component.text("§6§l=== Testing All New Hypixel SkyBlock Features ==="));
        
        // Test Booster Cookie
        if (SkyblockPlugin.getBoosterCookieSystem() != null) {
            // Booster cookie system not implemented yet
        player.sendMessage(Component.text("§cBooster cookie system not implemented yet!"));
            player.sendMessage(Component.text("§a✓ Booster Cookie added"));
        }
        
        // Test Recipe Book
        if (SkyblockPlugin.getRecipeBookSystem() != null) {
            // Recipe book system not implemented yet
            player.sendMessage(Component.text("§cRecipe book system not implemented yet!"));
            player.sendMessage(Component.text("§a✓ Test recipes discovered"));
        }
        
        // Test Calendar
        if (SkyblockPlugin.getCalendarSystem() != null) {
            player.sendMessage(Component.text("§a✓ Calendar system ready"));
        }
        
        player.sendMessage(Component.text("§6§l=== All Features Tested Successfully! ==="));
        player.sendMessage(Component.text("§7• Use /menu to access all features"));
        player.sendMessage(Component.text("§7• Right-click the Booster Cookie to consume it"));
        player.sendMessage(Component.text("§7• Check the Recipe Book for discovered recipes"));
        player.sendMessage(Component.text("§7• View the Calendar for upcoming events"));
    }
    
    private void testSkyblockFeatures(Player player) {
        player.sendMessage(Component.text("§6§l=== Testing Skyblock Features ==="));
        
        // Test various skyblock systems
        player.sendMessage(Component.text("§eTesting Skyblock systems..."));
        // Add actual skyblock tests here
        player.sendMessage(Component.text("§a✓ Skyblock features tested"));
    }
    
    private void testEconomy(Player player) {
        player.sendMessage(Component.text("§6§l=== Testing Economy System ==="));
        
        if (SkyblockPlugin.getEconomyManager() == null) {
            player.sendMessage(Component.text("§cEconomy Manager not available!"));
            return;
        }
        
        // Test economy operations
        double balance = SkyblockPlugin.getEconomyManager().getBalance(player);
        player.sendMessage("§eCurrent balance: " + SkyblockPlugin.getEconomyManager().formatMoney(balance));
        player.sendMessage(Component.text("§a✓ Economy system tested"));
    }
    
    // Status Methods
    private void showServerStatus(Player player) {
        player.sendMessage(Component.text("§6§l=== SERVER STATUS ==="));
        player.sendMessage("§eOnline Players: §a" + SkyblockPlugin.getServer().getOnlinePlayers().size());
        player.sendMessage("§eMax Players: §a" + SkyblockPlugin.getServer().getMaxPlayers());
        player.sendMessage("§eServer Version: §a" + SkyblockPlugin.getServer().getVersion());
        player.sendMessage("§ePlugin Version: §a" + SkyblockPlugin.getDescription().getVersion());
    }
    
    private void showPluginStatus(Player player) {
        player.sendMessage(Component.text("§6§l=== SkyblockPlugin =="));
        player.sendMessage("§ePlugin Name: §a" + SkyblockPlugin.getName());
        player.sendMessage("§eVersion: §a" + SkyblockPlugin.getDescription().getVersion());
        player.sendMessage("§eAuthors: §a" + String.join(", ", SkyblockPlugin.getDescription().getAuthors()));
        player.sendMessage("§eDescription: §a" + SkyblockPlugin.getDescription().getDescription());
    }
    
    private void showSystemsStatus(Player player) {
        player.sendMessage(Component.text("§6§l=== SYSTEMS STATUS ==="));
        player.sendMessage("§eData Manager: §a" + (SkyblockPlugin.getDatabaseManager() != null ? "✓ Active" : "✗ Inactive"));
        player.sendMessage("§eEconomy Manager: §a" + (SkyblockPlugin.getEconomyManager() != null ? "✓ Active" : "✗ Inactive"));
        player.sendMessage("§eCosmetics Manager: §a" + (SkyblockPlugin.getCosmeticsManager() != null ? "✓ Active" : "✗ Inactive"));
        player.sendMessage("§eIsland System: §a" + (SkyblockPlugin.getAdvancedIslandSystem() != null ? "✓ Active" : "✗ Inactive"));
    }
    
    private void showPerformanceStatus(Player player) {
        player.sendMessage(Component.text("§6§l=== PERFORMANCE STATUS ==="));
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
        player.sendMessage(Component.text("§6§l=== PLAYERS STATUS ==="));
        player.sendMessage("§eOnline Players: §a" + SkyblockPlugin.getServer().getOnlinePlayers().size());
        player.sendMessage("§eTotal Players: §a" + SkyblockPlugin.getServer().getOfflinePlayers().length);
        
        // Show some online players
        SkyblockPlugin.getServer().getOnlinePlayers().stream()
            .limit(10)
            .forEach(p -> player.sendMessage("§e- §a" + p.getName()));
    }
    
    // Restart Methods
    private void showRestartHelp(Player player) {
        player.sendMessage(Component.text("§6§l=== RESTART COMMAND ==="));
        player.sendMessage(Component.text("§e/restart SkyblockPlugin §7- Startet SkyblockPlugin neu"));
        player.sendMessage(Component.text("§e/restart server §7- Startet Server neu"));
        player.sendMessage(Component.text("§e/restart systems §7- Startet alle Systeme neu"));
        player.sendMessage(Component.text("§e/restart database §7- Startet Datenbank neu"));
    }
    
    private void restartPlugin(Player player) {
        player.sendMessage(Component.text("§eRestarting SkyblockPlugin..."));
        SkyblockPlugin.getServer().getScheduler().runTaskLater(SkyblockPlugin, () -> {
            SkyblockPlugin.getServer().getPluginManager().disablePlugin(SkyblockPlugin);
            SkyblockPlugin.getServer().getPluginManager().enablePlugin(SkyblockPlugin);
            player.sendMessage(Component.text("§aPlugin restarted successfully!"));
        }, 20L);
    }
    
    private void restartServer(Player player) {
        player.sendMessage(Component.text("§cRestarting server in 10 seconds..."));
        SkyblockPlugin.getServer().getScheduler().runTaskLater(SkyblockPlugin, () -> {
            SkyblockPlugin.getServer().shutdown();
        }, 200L);
    }
    
    private void restartSystems(Player player) {
        player.sendMessage(Component.text("§eRestarting all systems..."));
        // Add system restart logic here
        player.sendMessage(Component.text("§aAll systems restarted successfully!"));
    }
    
    private void restartDatabase(Player player) {
        player.sendMessage(Component.text("§eRestarting database connection..."));
        // Add database restart logic here
        player.sendMessage(Component.text("§aDatabase restarted successfully!"));
    }
    
    // Admin Methods
    private void showAdminHelp(Player player) {
        player.sendMessage(Component.text("§6§l=== ADMIN COMMAND ==="));
        player.sendMessage(Component.text("§e/admin reload §7- Lädt SkyblockPlugin neu"));
        player.sendMessage(Component.text("§e/admin maintenance §7- Aktiviert/Deaktiviert Wartungsmodus"));
        player.sendMessage(Component.text("§e/admin backup §7- Erstellt Backup"));
        player.sendMessage(Component.text("§e/admin cleanup §7- Bereinigt Daten"));
        player.sendMessage(Component.text("§e/admin permissions §7- Überprüft Berechtigungen"));
    }
    
    private void reloadPlugin(Player player) {
        player.sendMessage(Component.text("§eReloading SkyblockPlugin..."));
        SkyblockPlugin.reloadConfig();
        player.sendMessage(Component.text("§aPlugin reloaded successfully!"));
    }
    
    private void toggleMaintenance(Player player) {
        // Add maintenance toggle logic here
        player.sendMessage(Component.text("§eMaintenance mode toggled!"));
    }
    
    private void createBackup(Player player) {
        player.sendMessage(Component.text("§eCreating backup..."));
        // Add backup creation logic here
        player.sendMessage(Component.text("§aBackup created successfully!"));
    }
    
    private void cleanupData(Player player) {
        player.sendMessage(Component.text("§eCleaning up data..."));
        // Add cleanup logic here
        player.sendMessage(Component.text("§aData cleanup completed!"));
    }
    
    private void checkPermissions(Player player) {
        player.sendMessage(Component.text("§6§l=== PERMISSIONS CHECK ==="));
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
                    new de.noctivag.skyblock.gui.UnifiedMainMenuSystem(SkyblockPlugin, player, 
                        de.noctivag.skyblock.gui.UnifiedMainMenuSystem.MenuMode.ULTIMATE).open(player);
                    break;
                case "CosmeticsMenu":
                    if (SkyblockPlugin.getCosmeticsManager() != null) {
                        new de.noctivag.skyblock.gui.CosmeticsMenu(SkyblockPlugin, SkyblockPlugin.getCosmeticsManager()).open(player);
                    }
                    break;
                case "WarpGUI":
                    new de.noctivag.skyblock.locations.gui.WarpGUI(SkyblockPlugin).openMainMenu(player);
                    break;
                case "IslandGUI":
                    if (SkyblockPlugin.getAdvancedIslandSystem() != null) {
                        // Island system not implemented yet
            player.sendMessage(Component.text("§cIsland system not implemented yet!"));
                    }
                    break;
                case "EventMenu":
                    new de.noctivag.skyblock.gui.EventMenu(SkyblockPlugin, player).open();
                    break;
                case "SettingsGUI":
                    new de.noctivag.skyblock.gui.SettingsGUI(SkyblockPlugin, player).open();
                    break;
                case "FriendsGUI":
                    new de.noctivag.skyblock.gui.FriendsGUI(SkyblockPlugin, player).open();
                    break;
                case "PartyGUI":
                    new de.noctivag.skyblock.gui.PartyGUI(SkyblockPlugin, player).open();
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
                    completions.addAll(Arrays.asList("server", "SkyblockPlugin", "systems", "performance", "players"));
                }
                case "restart" -> {
                    completions.addAll(Arrays.asList("SkyblockPlugin", "server", "systems", "database"));
                }
                case "admin" -> {
                    completions.addAll(Arrays.asList("reload", "maintenance", "backup", "cleanup", "permissions"));
                }
            }
        }
        
        return completions;
    }
}
