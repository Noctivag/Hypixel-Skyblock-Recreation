package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class KitShopGUI extends CustomGUI {
    private final Player player;
    
    public KitShopGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, "§6§lKit Shop", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        // Placeholder items for kit shop
        setItem(10, Material.DIAMOND_SWORD, "§c§lPvP Kit", 
            "§7Ein Kit für PvP-Kämpfe",
            "§ePreis: §f1000 Coins");
            
        setItem(12, Material.DIAMOND_PICKAXE, "§6§lMining Kit",
            "§7Ein Kit für das Mining",
            "§ePreis: §f500 Coins");
            
        setItem(14, Material.FISHING_ROD, "§b§lFishing Kit",
            "§7Ein Kit für das Angeln",
            "§ePreis: §f300 Coins");
            
        setItem(16, Material.BOW, "§a§lArcher Kit",
            "§7Ein Kit für Bogenschützen",
            "§ePreis: §f800 Coins");
            
        // Back button
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
