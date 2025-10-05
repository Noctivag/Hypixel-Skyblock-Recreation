package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

public class EventStatisticsGUI extends CustomGUI {
    private final Player player;
    
    public EventStatisticsGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(SkyblockPlugin, "§e§lEvent Statistiken", 54);
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
