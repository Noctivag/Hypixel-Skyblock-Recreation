package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class EventStatisticsGUI extends CustomGUI {
    private final Player player;
    
    public EventStatisticsGUI(Plugin plugin, Player player) {
        super(plugin, "§e§lEvent Statistiken", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.GOLD_INGOT, "§6§lTeilnahmen", "§7Zeige deine Event-Teilnahmen");
        setItem(12, Material.DIAMOND, "§b§lSiege", "§7Zeige deine Event-Siege");
        setItem(14, Material.EMERALD, "§a§lBelohnungen", "§7Zeige erhaltene Belohnungen");
        setItem(16, Material.GOLD_INGOT, "§e§lRangliste", "§7Zeige Event-Rangliste");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
