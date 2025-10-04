package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BattlePassGUI extends CustomGUI {
    private final Player player;
    
    public BattlePassGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, "§d§lBattle Pass", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.BOOK, "§a§lFreie Belohnungen", "§7Zeige freie Belohnungen");
        setItem(12, Material.ENCHANTED_BOOK, "§6§lPremium Belohnungen", "§7Zeige Premium-Belohnungen");
        setItem(14, Material.EXPERIENCE_BOTTLE, "§b§lXP Fortschritt", "§7Zeige deinen Fortschritt");
        setItem(16, Material.DIAMOND, "§c§lKaufen", "§7Kaufe Premium Battle Pass");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
