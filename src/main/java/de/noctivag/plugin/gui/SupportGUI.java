package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SupportGUI extends CustomGUI {
    private final Player player;
    
    public SupportGUI(Plugin plugin, Player player) {
        super(plugin, "§b§lSupport", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.BOOK, "§a§lTicket erstellen", "§7Erstelle ein Support-Ticket");
        setItem(12, Material.PAPER, "§6§lMeine Tickets", "§7Zeige deine Tickets");
        setItem(14, Material.GOLD_INGOT, "§e§lTicket Status", "§7Zeige Ticket-Status");
        setItem(16, Material.EMERALD, "§b§lFAQ", "§7Zeige häufig gestellte Fragen");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
