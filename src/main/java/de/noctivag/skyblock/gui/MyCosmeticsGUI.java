package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MyCosmeticsGUI extends CustomGUI {
    private final Player player;
    
    public MyCosmeticsGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, "§d§lMeine Cosmetics", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.DIAMOND, "§b§lAktive Cosmetics", "§7Zeige aktive Cosmetics");
        setItem(12, Material.EMERALD, "§a§lVerfügbare Cosmetics", "§7Zeige verfügbare Cosmetics");
        setItem(14, Material.GOLD_INGOT, "§6§lCosmetic Shop", "§7Öffne Cosmetic Shop");
        setItem(16, Material.CHEST, "§e§lCosmetic Kiste", "§7Öffne Cosmetic Kiste");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
