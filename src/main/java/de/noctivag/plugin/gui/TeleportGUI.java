package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TeleportGUI extends CustomGUI {
    private final Player player;
    
    public TeleportGUI(Plugin plugin, Player player) {
        super(plugin, "§d§lTeleport Menu", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        // Teleport options
        setItem(10, Material.PLAYER_HEAD, "§a§lZu Spieler",
            "§7Teleportiere zu einem Spieler",
            "§eKosten: §f10 Coins");
            
        setItem(12, Material.COMPASS, "§6§lZu Koordinaten",
            "§7Teleportiere zu Koordinaten",
            "§eKosten: §f20 Coins");
            
        setItem(14, Material.ENDER_PEARL, "§b§lZufälliger Teleport",
            "§7Teleportiere zufällig",
            "§eKosten: §f50 Coins");
            
        setItem(16, Material.BEACON, "§c§lZu Base",
            "§7Teleportiere zu deiner Base",
            "§eKosten: §fKostenlos");
            
        setItem(28, Material.ENDER_EYE, "§5§lZu Team",
            "§7Teleportiere zu deinem Team",
            "§eKosten: §f30 Coins");
            
        setItem(30, Material.NETHER_STAR, "§e§lZu Event",
            "§7Teleportiere zum aktuellen Event",
            "§eKosten: §fKostenlos");
            
        // Back button
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
