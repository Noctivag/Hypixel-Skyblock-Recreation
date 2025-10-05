package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import net.kyori.adventure.text.Component;
import java.util.stream.Collectors;

/**
 * Auction House GUI - Auktionshaus
 */
public class AuctionHouseGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public AuctionHouseGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void openAuctionHouseGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lAuction House"));
        
        // Browse Auctions
        setItem(gui, 10, Material.BOOK, "§a§lBrowse Auctions",
            "§7Auktionen durchsuchen",
            "§7• Active Auctions",
            "§7• Auction Categories",
            "§7• Auction Search",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.DIAMOND, "§b§lPopular Auctions",
            "§7Beliebte Auktionen",
            "§7• Most Bidded",
            "§7• Highest Prices",
            "§7• Ending Soon",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.EMERALD, "§a§lNew Auctions",
            "§7Neue Auktionen",
            "§7• Recently Listed",
            "§7• Fresh Items",
            "§7• New Sellers",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.CLOCK, "§e§lEnding Soon",
            "§7Endet bald",
            "§7• Auctions Ending",
            "§7• Last Chance",
            "§7• Final Bids",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.COMPASS, "§b§lSearch Auctions",
            "§7Auktionen suchen",
            "§7• Item Search",
            "§7• Price Range",
            "§7• Seller Search",
            "",
            "§eKlicke zum Öffnen");
            
        // My Auctions
        setItem(gui, 19, Material.CHEST, "§6§lMy Auctions",
            "§7Meine Auktionen",
            "§7• Active Auctions: §a0",
            "§7• Sold Items: §a0",
            "§7• Total Earnings: §a$0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.EMERALD, "§a§lCreate Auction",
            "§7Auktion erstellen",
            "§7• List Item",
            "§7• Set Starting Price",
            "§7• Set Duration",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.GOLD_INGOT, "§6§lMy Bids",
            "§7Meine Gebote",
            "§7• Active Bids: §a0",
            "§7• Won Auctions: §a0",
            "§7• Total Spent: §a$0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.CLOCK, "§e§lAuction History",
            "§7Auktions-Verlauf",
            "§7• Bidding History",
            "§7• Selling History",
            "§7• Transaction History",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.BOOK, "§b§lAuction Statistics",
            "§7Auktions-Statistiken",
            "§7• Total Auctions: §a0",
            "§7• Success Rate: §a0%",
            "§7• Average Price: §a$0",
            "",
            "§eKlicke zum Öffnen");
            
        // Auction Categories
        setItem(gui, 28, Material.DIAMOND_SWORD, "§c§lWeapons",
            "§7Waffen-Auktionen",
            "§7• Swords",
            "§7• Bows",
            "§7• Special Weapons",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.DIAMOND_CHESTPLATE, "§b§lArmor",
            "§7Rüstungs-Auktionen",
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
            "§7Material-Auktionen",
            "§7• Ores",
            "§7• Gems",
            "§7• Special Materials",
            "",
            "§eKlicke zum Öffnen");
            
        // Auction Tools
        setItem(gui, 37, Material.BOOK, "§b§lAuction Guide",
            "§7Auktions-Anleitung",
            "§7• How to Bid",
            "§7• How to Sell",
            "§7• Auction Tips",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lAuction Settings",
            "§7Auktions-Einstellungen",
            "§7• Notification Settings",
            "§7• Auto-Bid Settings",
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
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(lore).stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
}
