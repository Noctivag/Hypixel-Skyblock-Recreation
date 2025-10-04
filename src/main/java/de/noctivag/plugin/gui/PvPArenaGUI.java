package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PvPArenaGUI extends CustomGUI {
    private final Player player;
    
    public PvPArenaGUI(Plugin plugin, Player player) {
        super(plugin, "§c§lPvP Arena", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        // PvP arenas
        setItem(10, Material.DIAMOND_SWORD, "§c§lArena 1",
            "§7Klassische PvP Arena",
            "§eSpieler: §f2-8");
            
        setItem(12, Material.BOW, "§6§lArena 2",
            "§7Bogen-PvP Arena",
            "§eSpieler: §f2-4");
            
        setItem(14, Material.TRIDENT, "§b§lArena 3",
            "§7Wasser-PvP Arena",
            "§eSpieler: §f2-6");
            
        setItem(16, Material.NETHERITE_SWORD, "§5§lArena 4",
            "§7Elite PvP Arena",
            "§eSpieler: §f2-10");
            
        // Back button
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
