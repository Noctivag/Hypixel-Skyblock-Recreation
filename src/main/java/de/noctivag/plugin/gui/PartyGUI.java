package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PartyGUI extends CustomGUI {
    private final Player player;
    
    public PartyGUI(Plugin plugin, Player player) {
        super(plugin, "§a§lParty System", 54);
        this.player = player;
        setupItems();
    }
    
    public PartyGUI(Plugin plugin) {
        super(plugin, "§a§lParty System", 54);
        this.player = null;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.PLAYER_HEAD, "§a§lParty erstellen", "§7Erstelle eine neue Party");
        setItem(12, Material.BOOK, "§6§lParty beitreten", "§7Tritt einer Party bei");
        setItem(14, Material.GOLD_INGOT, "§e§lParty verwalten", "§7Verwalte deine Party");
        setItem(16, Material.EMERALD, "§b§lParty verlassen", "§7Verlasse deine Party");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
