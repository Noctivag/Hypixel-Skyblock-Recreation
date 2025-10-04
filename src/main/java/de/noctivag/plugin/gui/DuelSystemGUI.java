package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DuelSystemGUI extends CustomGUI {
    private final Player player;
    
    public DuelSystemGUI(Plugin plugin, Player player) {
        super(plugin, "§e§lDuel System", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.DIAMOND_SWORD, "§c§lHerausfordern", "§7Fordere einen Spieler heraus");
        setItem(12, Material.SHIELD, "§a§lAktive Duelle", "§7Zeige aktive Duelle");
        setItem(14, Material.GOLD_INGOT, "§6§lDuel Statistiken", "§7Zeige deine Duel-Statistiken");
        setItem(16, Material.BOOK, "§b§lDuel Regeln", "§7Zeige die Duel-Regeln");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
