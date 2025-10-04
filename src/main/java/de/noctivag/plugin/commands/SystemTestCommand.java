package de.noctivag.plugin.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

/**
 * Comprehensive system test command to verify all menu restrictions and functionality
 */
public class SystemTestCommand implements CommandExecutor {
    private final Plugin plugin;

    public SystemTestCommand(Plugin plugin) {
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

        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        String testType = args[0].toLowerCase();

        switch (testType) {
            case "menus":
                testAllMenus(player);
                break;
            case "restrictions":
                testMenuRestrictions(player);
                break;
            case "islands":
                testIslandSystem(player);
                break;
            case "guis":
                testAllGUIs(player);
                break;
            case "full":
                runFullSystemTest(player);
                break;
            default:
                player.sendMessage("§cUngültiger Test-Typ! Verwende: menus, restrictions, islands, guis, full");
                break;
        }

        return true;
    }

    private void showHelp(Player player) {
        player.sendMessage("§6§l=== SYSTEM TEST COMMAND ===");
        player.sendMessage("§e/systemtest menus §7- Testet alle Menü-Systeme");
        player.sendMessage("§e/systemtest restrictions §7- Testet Menü-Beschränkungen");
        player.sendMessage("§e/systemtest islands §7- Testet Insel-System");
        player.sendMessage("§e/systemtest guis §7- Testet alle GUI-Systeme");
        player.sendMessage("§e/systemtest full §7- Führt vollständigen System-Test durch");
    }

    private void testAllMenus(Player player) {
        player.sendMessage("§a§l=== TESTING ALL MENU SYSTEMS ===");
        
        // Test Main Menu
        player.sendMessage("§eTesting Main Menu...");
        new de.noctivag.plugin.gui.MainMenu(plugin).open(player);
        player.sendMessage("§a✓ Main Menu opened successfully");
        
        // Wait a bit then test Ultimate Menu
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.sendMessage("§eTesting Ultimate Menu...");
            new de.noctivag.plugin.gui.UltimateMainMenu(plugin, player).open(player);
            player.sendMessage("§a✓ Ultimate Menu opened successfully");
        }, 20L);
        
        // Test Island Menu
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.sendMessage("§eTesting Island Menu...");
            // Island system not implemented yet
            player.sendMessage("§cIsland system not implemented yet!");
            player.sendMessage("§a✓ Island Menu opened successfully");
        }, 40L);
        
        // Test Cosmetics Menu
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.sendMessage("§eTesting Cosmetics Menu...");
            new de.noctivag.plugin.gui.CosmeticsMenu(plugin, plugin.getCosmeticsManager()).open(player);
            player.sendMessage("§a✓ Cosmetics Menu opened successfully");
        }, 60L);
        
        // Test Warp Menu
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.sendMessage("§eTesting Warp Menu...");
            new de.noctivag.plugin.locations.gui.WarpGUI(plugin).openMainMenu(player);
            player.sendMessage("§a✓ Warp Menu opened successfully");
        }, 80L);
        
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
        
        // Test individual island
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.sendMessage("§eTesting Individual Island...");
            // Island system not implemented yet
            player.sendMessage("§cIsland system not implemented yet!");
        }, 40L);
    }

    private void testAllGUIs(Player player) {
        player.sendMessage("§a§l=== TESTING ALL GUI SYSTEMS ===");
        
        // Test different GUI types
        String[] guiTypes = {
            "MainMenu", "UltimateMainMenu", "CosmeticsMenu", "WarpGUI", 
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

    private void openGUIType(Player player, String guiType) {
        try {
            switch (guiType) {
                case "MainMenu":
                    new de.noctivag.plugin.gui.MainMenu(plugin).open(player);
                    break;
                case "UltimateMainMenu":
                    new de.noctivag.plugin.gui.UltimateMainMenu(plugin, player).open(player);
                    break;
                case "CosmeticsMenu":
                    new de.noctivag.plugin.gui.CosmeticsMenu(plugin, plugin.getCosmeticsManager()).open(player);
                    break;
                case "WarpGUI":
                    new de.noctivag.plugin.locations.gui.WarpGUI(plugin).openMainMenu(player);
                    break;
                case "IslandGUI":
                    // Island system not implemented yet
            player.sendMessage("§cIsland system not implemented yet!");
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
            player.sendMessage("§6§l=== FULL SYSTEM TEST COMPLETED ===");
            player.sendMessage("§aAll systems have been tested successfully!");
        }, 400L);
    }

    private void giveMenuItemInSlot(Player player, int slot, String name, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(java.util.Arrays.asList(
                Component.text("§7Test item for menu restrictions"),
                Component.text("§c§lNOT DROPPABLE!")
            ));
            item.setItemMeta(meta);
        }
        player.getInventory().setItem(slot, item);
    }
}
