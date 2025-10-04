package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class JoinMessageGUI extends CustomGUI {
    private final Player player;
    
    public JoinMessageGUI(Plugin plugin, Player player) {
        super(plugin, "§e§lJoin Message", 54);
        this.player = player;
        setupItems();
    }
    
    public JoinMessageGUI(Plugin plugin) {
        super(plugin, "§e§lJoin Message", 54);
        this.player = null;
        setupItems();
    }
    
    private void setupItems() {
        // Join message presets
        setItem(10, Material.BOOK, "§a§lStandard",
            "§7Standard Join Message",
            "§eKosten: §fKostenlos");
            
        setItem(12, Material.ENCHANTED_BOOK, "§6§lMagisch",
            "§7Magische Join Message",
            "§eKosten: §f100 Coins");
            
        setItem(14, Material.FIREWORK_ROCKET, "§c§lFeuerwerk",
            "§7Feuerwerk Join Message",
            "§eKosten: §f200 Coins");
            
        setItem(16, Material.HEART_OF_THE_SEA, "§b§lOzean",
            "§7Ozean Join Message",
            "§eKosten: §f150 Coins");
            
        setItem(28, Material.DRAGON_EGG, "§5§lDrache",
            "§7Drachen Join Message",
            "§eKosten: §f500 Coins");
            
        setItem(30, Material.NETHER_STAR, "§e§lStern",
            "§7Sternen Join Message",
            "§eKosten: §f300 Coins");
            
        // Back button
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
