package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

public class ShopGUI extends CustomGUI {
    private final Player player;
    
    public ShopGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(SkyblockPlugin, "§a§lShop", 54);
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
