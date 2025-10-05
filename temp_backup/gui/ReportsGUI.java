package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

public class ReportsGUI extends CustomGUI {
    private final Player player;
    
    public ReportsGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(SkyblockPlugin, "§c§lReports", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.BOOK, "§a§lReport erstellen", "§7Erstelle einen Report");
        setItem(12, Material.PAPER, "§6§lMeine Reports", "§7Zeige deine Reports");
        setItem(14, Material.GOLD_INGOT, "§e§lReport Status", "§7Zeige Report-Status");
        setItem(16, Material.EMERALD, "§b§lReport Belohnung", "§7Zeige Report-Belohnungen");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
