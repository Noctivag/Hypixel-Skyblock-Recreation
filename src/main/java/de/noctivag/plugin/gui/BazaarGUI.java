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
 * Bazaar GUI - Bazaar und Markt
 */
public class BazaarGUI {
    
    private final Plugin plugin;
    
    public BazaarGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openBazaarGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lBazaar");
        
        // Browse Items
        setItem(gui, 10, Material.BOOK, "§a§lBrowse Items",
            "§7Items durchsuchen",
            "§7• All Items",
            "§7• Item Categories",
            "§7• Item Search",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.DIAMOND, "§b§lPopular Items",
            "§7Beliebte Items",
            "§7• Most Bought",
            "§7• Best Sellers",
            "§7• Trending Items",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.EMERALD, "§a§lNew Items",
            "§7Neue Items",
            "§7• Recently Added",
            "§7• Fresh Stock",
            "§7• New Sellers",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.CLOCK, "§e§lLimited Time",
            "§7Begrenzte Zeit",
            "§7• Flash Sales",
            "§7• Limited Offers",
            "§7• Special Deals",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.COMPASS, "§b§lSearch Items",
            "§7Items suchen",
            "§7• Item Search",
            "§7• Price Range",
            "§7• Seller Search",
            "",
            "§eKlicke zum Öffnen");
            
        // My Bazaar
        setItem(gui, 19, Material.CHEST, "§6§lMy Bazaar",
            "§7Mein Bazaar",
            "§7• Active Listings: §a0",
            "§7• Sold Items: §a0",
            "§7• Total Earnings: §a$0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.EMERALD, "§a§lSell Items",
            "§7Items verkaufen",
            "§7• List Item",
            "§7• Set Price",
            "§7• Set Quantity",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.GOLD_INGOT, "§6§lMy Orders",
            "§7Meine Bestellungen",
            "§7• Active Orders: §a0",
            "§7• Completed Orders: §a0",
            "§7• Total Spent: §a$0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.CLOCK, "§e§lOrder History",
            "§7Bestellungs-Verlauf",
            "§7• Purchase History",
            "§7• Sale History",
            "§7• Transaction History",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.BOOK, "§b§lBazaar Statistics",
            "§7Bazaar-Statistiken",
            "§7• Total Sales: §a0",
            "§7• Success Rate: §a0%",
            "§7• Average Price: §a$0",
            "",
            "§eKlicke zum Öffnen");
            
        // Item Categories
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
            
        setItem(gui, 31, Material.NETHER_STAR, "§5§lSpecial Items",
            "§7Besondere Items",
            "§7• Rare Items",
            "§7• Event Items",
            "§7• Limited Items",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.EMERALD, "§a§lMaterials",
            "§7Materialien",
            "§7• Ores",
            "§7• Gems",
            "§7• Special Materials",
            "",
            "§eKlicke zum Öffnen");
            
        // Bazaar Tools
        setItem(gui, 37, Material.BOOK, "§b§lBazaar Guide",
            "§7Bazaar-Anleitung",
            "§7• How to Buy",
            "§7• How to Sell",
            "§7• Bazaar Tips",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lBazaar Settings",
            "§7Bazaar-Einstellungen",
            "§7• Notification Settings",
            "§7• Auto-Buy Settings",
            "§7• Privacy Settings",
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
