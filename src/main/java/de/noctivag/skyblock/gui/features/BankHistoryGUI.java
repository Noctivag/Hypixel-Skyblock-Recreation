package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

/**
 * GUI for viewing bank transaction history
 */
public class BankHistoryGUI {
    
    private final SkyblockPlugin plugin;
    private final Player player;
    
    public BankHistoryGUI(SkyblockPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }
    
    public void openGUI(Player player) {
        // TODO: Implement bank history GUI
        player.sendMessage("§7Bank history GUI not yet implemented");
    }
}
