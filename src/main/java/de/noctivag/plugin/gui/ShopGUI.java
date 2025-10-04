package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ShopGUI extends CustomGUI {
    private final Player player;
    
    public ShopGUI(Plugin plugin, Player player) {
        super(plugin, "§a§lShop", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.DIAMOND_SWORD, "§c§lWaffen", "§7Kaufe Waffen");
        setItem(12, Material.DIAMOND_CHESTPLATE, "§6§lRüstung", "§7Kaufe Rüstung");
        setItem(14, Material.DIAMOND_PICKAXE, "§b§lWerkzeuge", "§7Kaufe Werkzeuge");
        setItem(16, Material.EMERALD, "§a§lBlöcke", "§7Kaufe Blöcke");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
