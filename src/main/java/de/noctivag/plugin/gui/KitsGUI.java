package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Kits GUI - Kits und Belohnungen
 */
public class KitsGUI {
    
    private final Plugin plugin;
    
    public KitsGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openKitsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lKits & Belohnungen");
        
        // Starter Kits
        setItem(gui, 10, Material.WOODEN_SWORD, "§a§lStarter Kit",
            "§7Perfekt für Anfänger",
            "§7• Wooden Sword",
            "§7• Wooden Pickaxe",
            "§7• 64x Bread",
            "§7• 10x Coins",
            "",
            "§eKlicke zum Erhalten");
            
        setItem(gui, 11, Material.STONE_SWORD, "§7§lStone Kit",
            "§7Bessere Ausrüstung",
            "§7• Stone Sword",
            "§7• Stone Pickaxe",
            "§7• 32x Cooked Beef",
            "§7• 25x Coins",
            "",
            "§eKlicke zum Erhalten");
            
        setItem(gui, 12, Material.IRON_SWORD, "§f§lIron Kit",
            "§7Solide Ausrüstung",
            "§7• Iron Sword",
            "§7• Iron Pickaxe",
            "§7• 16x Golden Carrot",
            "§7• 50x Coins",
            "",
            "§eKlicke zum Erhalten");
            
        setItem(gui, 13, Material.DIAMOND_SWORD, "§b§lDiamond Kit",
            "§7Hochwertige Ausrüstung",
            "§7• Diamond Sword",
            "§7• Diamond Pickaxe",
            "§7• 8x Golden Apple",
            "§7• 100x Coins",
            "",
            "§eKlicke zum Erhalten");
            
        setItem(gui, 14, Material.NETHERITE_SWORD, "§5§lNetherite Kit",
            "§7Premium Ausrüstung",
            "§7• Netherite Sword",
            "§7• Netherite Pickaxe",
            "§7• 4x Enchanted Golden Apple",
            "§7• 250x Coins",
            "",
            "§eKlicke zum Erhalten");
            
        // Special Kits
        setItem(gui, 19, Material.BOW, "§c§lArcher Kit",
            "§7Für Bogenschützen",
            "§7• Bow (Power V)",
            "§7• 64x Arrows",
            "§7• Leather Armor",
            "§7• 75x Coins",
            "",
            "§eKlicke zum Erhalten");
            
        setItem(gui, 20, Material.TRIDENT, "§9§lOcean Kit",
            "§7Für Wasserkämpfe",
            "§7• Trident (Impaling V)",
            "§7• Depth Strider Boots",
            "§7• Water Breathing Potion",
            "§7• 75x Coins",
            "",
            "§eKlicke zum Erhalten");
            
        setItem(gui, 21, Material.FIRE_CHARGE, "§6§lFire Kit",
            "§7Für Feuer-Kämpfe",
            "§7• Fire Aspect Sword",
            "§7• Fire Resistance Potion",
            "§7• Flint & Steel",
            "§7• 75x Coins",
            "",
            "§eKlicke zum Erhalten");
            
        setItem(gui, 22, Material.ENDER_PEARL, "§d§lEnd Kit",
            "§7Für End-Kämpfe",
            "§7• Ender Pearl",
            "§7• End Stone",
            "§7• End Crystal",
            "§7• 75x Coins",
            "",
            "§eKlicke zum Erhalten");
            
        setItem(gui, 23, Material.NETHER_STAR, "§5§lNether Kit",
            "§7Für Nether-Kämpfe",
            "§7• Netherite Armor",
            "§7• Fire Resistance Potion",
            "§7• Nether Wart",
            "§7• 75x Coins",
            "",
            "§eKlicke zum Erhalten");
            
        // Daily Kits
        setItem(gui, 28, Material.CLOCK, "§e§lDaily Kit",
            "§7Tägliche Belohnungen",
            "§7• 1x Random Item",
            "§7• 10x Coins",
            "§7• 1x XP Bottle",
            "§7• 1x Food Item",
            "",
            "§eKlicke zum Erhalten");
            
        setItem(gui, 29, Material.CLOCK, "§a§lWeekly Kit",
            "§7Wöchentliche Belohnungen",
            "§7• 5x Random Items",
            "§7• 100x Coins",
            "§7• 10x XP Bottles",
            "§7• 1x Special Item",
            "",
            "§eKlicke zum Erhalten");
            
        setItem(gui, 30, Material.END_CRYSTAL, "§9§lMonthly Kit",
            "§7Monatliche Belohnungen",
            "§7• 20x Random Items",
            "§7• 1000x Coins",
            "§7• 50x XP Bottles",
            "§7• 5x Special Items",
            "",
            "§eKlicke zum Erhalten");
            
        // Kit Information
        setItem(gui, 37, Material.BOOK, "§b§lKit Information",
            "§7Informationen über Kits",
            "§7• Kit Cooldowns",
            "§7• Kit Requirements",
            "§7• Kit History",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.EMERALD, "§a§lKit Shop",
            "§7Kaufe zusätzliche Kits",
            "§7• Premium Kits",
            "§7• Special Kits",
            "§7• Custom Kits",
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
