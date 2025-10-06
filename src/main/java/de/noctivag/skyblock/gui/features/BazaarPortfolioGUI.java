package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

/**
 * GUI for viewing bazaar portfolio
 */
public class BazaarPortfolioGUI {
    
    private final SkyblockPlugin plugin;
    private final Player player;
    
    public BazaarPortfolioGUI(SkyblockPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }
    
    public void openGUI(Player player) {
        // TODO: Implement bazaar portfolio GUI
        player.sendMessage("§7Bazaar portfolio GUI not yet implemented");
    }
}
