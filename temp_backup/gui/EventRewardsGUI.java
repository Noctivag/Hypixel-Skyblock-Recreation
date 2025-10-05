package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

public class EventRewardsGUI extends CustomGUI {
    private final Player player;
    
    public EventRewardsGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(SkyblockPlugin, "§a§lEvent Belohnungen", 54);
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
