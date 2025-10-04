package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class WebsiteGUI extends CustomGUI {
    private final Player player;
    
    public WebsiteGUI(Plugin plugin, Player player) {
        super(plugin, "§b§lWebsite", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.BOOK, "§b§lWebsite Link", "§7Kopiere Website-Link");
        setItem(12, Material.PAPER, "§6§lForum", "§7Öffne das Forum");
        setItem(14, Material.GOLD_INGOT, "§e§lVoting", "§7Vote für den Server");
        setItem(16, Material.EMERALD, "§a§lStore", "§7Öffne den Store");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
