package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

/**
 * GUI for viewing collection rewards
 */
public class CollectionRewardsGUI {
    
    private final SkyblockPlugin plugin;
    private final Player player;
    
    public CollectionRewardsGUI(SkyblockPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }
    
    public void openGUI(Player player) {
        // TODO: Implement collection rewards GUI
        player.sendMessage("§7Collection rewards GUI not yet implemented");
    }
}
