package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Inventory GUI - Inventar-Verwaltung und -Organisation
 */
public class InventoryGUI {
    
    private final SkyblockPlugin plugin;
    
    public InventoryGUI(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void openInventoryGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lInventory");
        
        // Inventory Management
        setItem(gui, 10, Material.CHEST, "§a§lMain Inventory",
            "§7Haupt-Inventar",
            "§7• View Items",
            "§7• Organize Items",
            "§7• Sort Items",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.ENDER_CHEST, "§d§lEnder Chest",
            "§7Ender-Truhe",
            "§7• View Ender Items",
            "§7• Organize Ender Items",
            "§7• Sort Ender Items",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.SHULKER_BOX, "§b§lShulker Boxes",
            "§7Shulker-Boxen",
            "§7• View Shulker Items",
            "§7• Organize Shulker Items",
            "§7• Sort Shulker Items",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.BARREL, "§6§lStorage Barrels",
            "§7Lager-Fässer",
            "§7• View Barrel Items",
            "§7• Organize Barrel Items",
            "§7• Sort Barrel Items",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.HOPPER, "§7§lItem Hoppers",
            "§7Item-Trichter",
            "§7• View Hopper Items",
            "§7• Organize Hopper Items",
            "§7• Sort Hopper Items",
            "",
            "§eKlicke zum Öffnen");
            
        // Inventory Tools
        setItem(gui, 19, Material.EMERALD, "§a§lAuto Sort",
            "§7Automatisches Sortieren",
            "§7• Sort All Items",
            "§7• Sort by Category",
            "§7• Sort by Value",
            "",
            "§eKlicke zum Ausführen");
            
        setItem(gui, 20, Material.DIAMOND, "§b§lItem Search",
            "§7Item-Suche",
            "§7• Search Items",
            "§7• Filter Items",
            "§7• Find Items",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.GOLD_INGOT, "§6§lItem Value",
            "§7Item-Wert",
            "§7• Calculate Value",
            "§7• Value Analysis",
            "§7• Value Reports",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.NETHER_STAR, "§d§lItem Transfer",
            "§7Item-Transfer",
            "§7• Transfer Items",
            "§7• Move Items",
            "§7• Copy Items",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.CLOCK, "§e§lItem History",
            "§7Item-Verlauf",
            "§7• Item History",
            "§7• Item Statistics",
            "§7• Item Reports",
            "",
            "§eKlicke zum Öffnen");
            
        // Inventory Categories
        setItem(gui, 28, Material.DIAMOND_SWORD, "§c§lWeapons",
            "§7Waffen",
            "§7• Swords",
            "§7• Bows",
            "§7• Special Weapons",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.DIAMOND_CHESTPLATE, "§b§lArmor",
            "§7Rüstung",
            "§7• Helmets",
            "§7• Chestplates",
            "§7• Complete Sets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.ENCHANTED_BOOK, "§d§lEnchanted Books",
            "§7Verzauberte Bücher",
            "§7• Enchantment Books",
            "§7• Rare Enchants",
            "§7• Custom Enchants",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.EMERALD, "§a§lMaterials",
            "§7Materialien",
            "§7• Ores",
            "§7• Gems",
            "§7• Special Materials",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.NETHER_STAR, "§5§lSpecial Items",
            "§7Besondere Items",
            "§7• Rare Items",
            "§7• Event Items",
            "§7• Limited Items",
            "",
            "§eKlicke zum Öffnen");
            
        // Inventory Settings
        setItem(gui, 37, Material.BOOK, "§b§lInventory Guide",
            "§7Inventar-Anleitung",
            "§7• Inventory Basics",
            "§7• Organization Tips",
            "§7• Inventory Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lInventory Statistics",
            "§7Inventar-Statistiken",
            "§7• Total Items: §a0",
            "§7• Inventory Value: §a$0",
            "§7• Storage Usage: §a0%",
            "",
            "§eKlicke zum Öffnen");
            
        // Navigation
        setItem(gui, 45, Material.ARROW, "§7« Back",
            "§7Zurück zum Hauptmenü");
            
        setItem(gui, 49, Material.BARRIER, "§c§lClose",
            "§7GUI schließen");
        
        player.openInventory(gui);
    }
    
    private void setItem(Inventory gui, int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
}
