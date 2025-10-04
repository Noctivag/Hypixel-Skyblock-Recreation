package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class StatisticsGUI extends CustomGUI {
    private final Player player;
    
    public StatisticsGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, "§b§lStatistiken", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.DIAMOND_SWORD, "§c§lKampf Statistiken", "§7Zeige Kampf-Statistiken");
        setItem(12, Material.DIAMOND_PICKAXE, "§6§lMining Statistiken", "§7Zeige Mining-Statistiken");
        setItem(14, Material.FISHING_ROD, "§b§lFishing Statistiken", "§7Zeige Fishing-Statistiken");
        setItem(16, Material.EXPERIENCE_BOTTLE, "§a§lLevel Statistiken", "§7Zeige Level-Statistiken");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
