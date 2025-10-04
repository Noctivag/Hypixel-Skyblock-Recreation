package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class EconomyGUI extends CustomGUI {
    private final Player player;
    
    public EconomyGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, "§6§lEconomy", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.GOLD_INGOT, "§6§lBalance", "§7Zeige dein Guthaben");
        setItem(12, Material.EMERALD, "§a§lPay", "§7Zahle einem Spieler");
        setItem(14, Material.DIAMOND, "§b§lShop", "§7Öffne den Shop");
        setItem(16, Material.ANVIL, "§e§lAuction House", "§7Öffne das Auktionshaus");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
