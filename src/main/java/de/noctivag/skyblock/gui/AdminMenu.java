package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Admin Menu GUI
 */
public class AdminMenu extends CustomGUI {

    /**
     * Handle clicks in the AdminMenu
     */
    public void handleClick(org.bukkit.event.inventory.InventoryClickEvent event) {
        int slot = event.getRawSlot();
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (slot == 24) {
            // Mob Spawner öffnen
            de.noctivag.skyblock.gui.admin.MobSpawnerAdminGUI.open(player);
        } else if (slot == 49) {
            player.closeInventory();
        }
        // Weitere Admin-Buttons können hier ergänzt werden
    }
    
    public AdminMenu() {
        super("§cAdmin Menu", 54);
        setupItems();
    }
    
    public AdminMenu(SkyblockPlugin plugin) {
        super("§cAdmin Menu", 54);
        setupItems();
    }
    
    private Player player;
    
    @Override
    public void open() {
        if (player != null) {
            open(player);
        }
    }
    
    @Override
    public void setupItems() {
    // Add admin tools
    ItemStack tools = new ItemStack(Material.DIAMOND_SWORD);
    ItemMeta toolsMeta = tools.getItemMeta();
    net.kyori.adventure.text.Component toolsName = net.kyori.adventure.text.Component.text("Admin Tools").color(net.kyori.adventure.text.format.NamedTextColor.RED);
    java.util.List<net.kyori.adventure.text.Component> toolsLore = java.util.Arrays.asList(
        net.kyori.adventure.text.Component.text("Administrative tools").color(net.kyori.adventure.text.format.NamedTextColor.GRAY),
        net.kyori.adventure.text.Component.text("Click to access!").color(net.kyori.adventure.text.format.NamedTextColor.GRAY)
    );
    toolsMeta.displayName(toolsName);
    toolsMeta.lore(toolsLore);
    tools.setItemMeta(toolsMeta);
    inventory.setItem(22, tools);

    // Mob Spawner Button
    ItemStack mobSpawner = new ItemStack(Material.ZOMBIE_SPAWN_EGG);
    ItemMeta mobSpawnerMeta = mobSpawner.getItemMeta();
    net.kyori.adventure.text.Component mobSpawnerName = net.kyori.adventure.text.Component.text("Mob Spawner").color(net.kyori.adventure.text.format.NamedTextColor.GREEN);
    java.util.List<net.kyori.adventure.text.Component> mobSpawnerLore = java.util.Arrays.asList(
        net.kyori.adventure.text.Component.text("Spawn Hypixel-Mobs").color(net.kyori.adventure.text.format.NamedTextColor.GRAY),
        net.kyori.adventure.text.Component.text("Klicke zum Öffnen!").color(net.kyori.adventure.text.format.NamedTextColor.YELLOW)
    );
    mobSpawnerMeta.displayName(mobSpawnerName);
    mobSpawnerMeta.lore(mobSpawnerLore);
    mobSpawner.setItemMeta(mobSpawnerMeta);
    inventory.setItem(24, mobSpawner);
        
    // Close button
    ItemStack close = new ItemStack(Material.BARRIER);
    ItemMeta closeMeta = close.getItemMeta();
    net.kyori.adventure.text.Component closeName = net.kyori.adventure.text.Component.text("Close").color(net.kyori.adventure.text.format.NamedTextColor.RED);
    closeMeta.displayName(closeName);
    close.setItemMeta(closeMeta);
    inventory.setItem(49, close);
    }
    
    /**
     * Open the admin menu for a player
     */
    public static void openForPlayer(Player player) {
        AdminMenu menu = new AdminMenu();
        menu.open(player);
    }
}

