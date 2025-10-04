package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TournamentGUI extends CustomGUI {
    private final Player player;
    
    public TournamentGUI(Plugin plugin, Player player) {
        super(plugin, "§6§lTournament", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.GOLD_INGOT, "§6§lAktuelle Turniere", "§7Zeige aktuelle Turniere");
        setItem(12, Material.DIAMOND, "§b§lBeitreten", "§7Tritt einem Turnier bei");
        setItem(14, Material.EMERALD, "§a§lErstellen", "§7Erstelle ein Turnier");
        setItem(16, Material.GOLD_INGOT, "§e§lRangliste", "§7Zeige die Turnier-Rangliste");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
