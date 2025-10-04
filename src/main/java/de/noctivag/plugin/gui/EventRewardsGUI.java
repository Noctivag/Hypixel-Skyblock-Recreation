package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class EventRewardsGUI extends CustomGUI {
    private final Player player;
    
    public EventRewardsGUI(Plugin plugin, Player player) {
        super(plugin, "§a§lEvent Belohnungen", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.GOLD_INGOT, "§6§lVerfügbare Belohnungen", "§7Zeige verfügbare Belohnungen");
        setItem(12, Material.DIAMOND, "§b§lErhaltene Belohnungen", "§7Zeige erhaltene Belohnungen");
        setItem(14, Material.EMERALD, "§a§lBelohnung einlösen", "§7Löse Belohnungen ein");
        setItem(16, Material.CHEST, "§e§lBelohnungskiste", "§7Öffne Belohnungskiste");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
