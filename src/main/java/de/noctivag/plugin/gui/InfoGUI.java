package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InfoGUI extends CustomGUI {
    private final Player player;
    
    public InfoGUI(Plugin plugin, Player player) {
        super(plugin, "§b§lInformationen", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.BOOK, "§a§lServer Info", "§7Zeige Server-Informationen");
        setItem(12, Material.PLAYER_HEAD, "§6§lSpieler Info", "§7Zeige Spieler-Informationen");
        setItem(14, Material.GOLD_INGOT, "§e§lFeatures", "§7Zeige verfügbare Features");
        setItem(16, Material.EMERALD, "§b§lUpdates", "§7Zeige aktuelle Updates");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
