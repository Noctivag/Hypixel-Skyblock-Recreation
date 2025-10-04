package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ModerationGUI extends CustomGUI {
    private final Player player;
    
    public ModerationGUI(Plugin plugin, Player player) {
        super(plugin, "§c§lModeration", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.BARRIER, "§c§lSpieler bannen", "§7Banne einen Spieler");
        setItem(12, Material.BARRIER, "§6§lSpieler muten", "§7Mute einen Spieler");
        setItem(14, Material.BARRIER, "§e§lSpieler kicken", "§7Kicke einen Spieler");
        setItem(16, Material.BOOK, "§b§lReports", "§7Verwalte Reports");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
