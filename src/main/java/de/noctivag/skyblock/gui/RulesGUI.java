package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class RulesGUI extends CustomGUI {
    private final Player player;
    
    public RulesGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, "§e§lRegeln", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.BOOK, "§a§lAllgemeine Regeln", "§7Zeige allgemeine Regeln");
        setItem(12, Material.DIAMOND_SWORD, "§c§lPvP Regeln", "§7Zeige PvP-Regeln");
        setItem(14, Material.CHEST, "§6§lChat Regeln", "§7Zeige Chat-Regeln");
        setItem(16, Material.EMERALD, "§b§lBuild Regeln", "§7Zeige Build-Regeln");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
