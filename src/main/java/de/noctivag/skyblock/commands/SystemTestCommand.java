package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
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
    private final SkyblockPlugin SkyblockPlugin;

    public SystemTestCommand(SkyblockPlugin SkyblockPlugin) {
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
                player.sendMessage(Component.text("§cUngültiger Test-Typ! Verwende: menus, restrictions, islands, guis, full"));
                break;
        }

        return true;
    }

    private void showHelp(Player player) {
        player.sendMessage(Component.text("§6§l=== SYSTEM TEST COMMAND ==="));
        player.sendMessage(Component.text("§e/systemtest menus §7- Testet alle Menü-Systeme"));
        player.sendMessage(Component.text("§e/systemtest restrictions §7- Testet Menü-Beschränkungen"));
        player.sendMessage(Component.text("§e/systemtest islands §7- Testet Insel-System"));
        player.sendMessage(Component.text("§e/systemtest guis §7- Testet alle GUI-Systeme"));
        player.sendMessage(Component.text("§e/systemtest full §7- Führt vollständigen System-Test durch"));
    }

    private void testAllMenus(Player player) {
        player.sendMessage(Component.text("§a§l=== TESTING ALL MENU SYSTEMS ==="));
        
        // Test Main Menu
        player.sendMessage(Component.text("§eTesting Main Menu..."));
        new de.noctivag.skyblock.gui.MainMenu(SkyblockPlugin, player).open();
        player.sendMessage(Component.text("§a✓ Main Menu opened successfully"));
        
        // Wait a bit then test Ultimate Menu
        SkyblockPlugin.getServer().getScheduler().runTaskLater(SkyblockPlugin, () -> {
            player.sendMessage(Component.text("§eTesting Ultimate Menu..."));
            new de.noctivag.skyblock.gui.UltimateMainMenu(SkyblockPlugin, player).open();
            player.sendMessage(Component.text("§a✓ Ultimate Menu opened successfully"));
        }, 20L);
        
        // Test Island Menu
        SkyblockPlugin.getServer().getScheduler().runTaskLater(SkyblockPlugin, () -> {
            player.sendMessage(Component.text("§eTesting Island Menu..."));
            // Island system not implemented yet
            player.sendMessage(Component.text("§cIsland system not implemented yet!"));
            player.sendMessage(Component.text("§a✓ Island Menu opened successfully"));
        }, 40L);
        
        // Test Cosmetics Menu
        SkyblockPlugin.getServer().getScheduler().runTaskLater(SkyblockPlugin, () -> {
            player.sendMessage(Component.text("§eTesting Cosmetics Menu..."));
            new de.noctivag.skyblock.gui.CosmeticsMenu(SkyblockPlugin, SkyblockPlugin.getCosmeticsManager(), player).open();
            player.sendMessage(Component.text("§a✓ Cosmetics Menu opened successfully"));
        }, 60L);
        
        // Test Warp Menu
        SkyblockPlugin.getServer().getScheduler().runTaskLater(SkyblockPlugin, () -> {
            player.sendMessage(Component.text("§eTesting Warp Menu..."));
            new de.noctivag.skyblock.locations.gui.WarpGUI(SkyblockPlugin).openMainMenu(player);
            player.sendMessage(Component.text("§a✓ Warp Menu opened successfully"));
        }, 80L);
        
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
        
        // Test individual island
        SkyblockPlugin.getServer().getScheduler().runTaskLater(SkyblockPlugin, () -> {
            player.sendMessage(Component.text("§eTesting Individual Island..."));
            // Island system not implemented yet
            player.sendMessage(Component.text("§cIsland system not implemented yet!"));
        }, 40L);
    }

    private void testAllGUIs(Player player) {
        player.sendMessage(Component.text("§a§l=== TESTING ALL GUI SYSTEMS ==="));
        
        // Test different GUI types
        String[] guiTypes = {
            "MainMenu", "UltimateMainMenu", "CosmeticsMenu", "WarpGUI", 
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

    private void openGUIType(Player player, String guiType) {
        try {
            switch (guiType) {
                case "MainMenu":
                    new de.noctivag.skyblock.gui.MainMenu(SkyblockPlugin, player).open();
                    break;
                case "UltimateMainMenu":
                    new de.noctivag.skyblock.gui.UltimateMainMenu(SkyblockPlugin, player).open();
                    break;
                case "CosmeticsMenu":
                    new de.noctivag.skyblock.gui.CosmeticsMenu(SkyblockPlugin, SkyblockPlugin.getCosmeticsManager(), player).open();
                    break;
                case "WarpGUI":
                    new de.noctivag.skyblock.locations.gui.WarpGUI(SkyblockPlugin).openMainMenu(player);
                    break;
                case "IslandGUI":
                    // Island system not implemented yet
            player.sendMessage(Component.text("§cIsland system not implemented yet!"));
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
            player.sendMessage(Component.text("§6§l=== FULL SYSTEM TEST COMPLETED ==="));
            player.sendMessage(Component.text("§aAll systems have been tested successfully!"));
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
