package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DiscordGUI extends CustomGUI {
    private final Player player;
    
    public DiscordGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, "§5§lDiscord", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.BOOK, "§5§lDiscord Link", "§7Kopiere Discord-Link");
        setItem(12, Material.PAPER, "§6§lDiscord Regeln", "§7Zeige Discord-Regeln");
        setItem(14, Material.GOLD_INGOT, "§e§lDiscord Events", "§7Zeige Discord-Events");
        setItem(16, Material.EMERALD, "§b§lDiscord Belohnungen", "§7Zeige Discord-Belohnungen");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
