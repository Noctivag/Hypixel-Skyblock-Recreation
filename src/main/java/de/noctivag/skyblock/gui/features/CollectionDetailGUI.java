package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

/**
 * GUI for viewing collection details
 */
public class CollectionDetailGUI {
    
    private final SkyblockPlugin plugin;
    private final Player player;
    private final String collectionName;
    
    public CollectionDetailGUI(SkyblockPlugin plugin, Player player, String collectionName) {
        this.plugin = plugin;
        this.player = player;
        this.collectionName = collectionName;
    }
    
    public void openGUI(Player player) {
        // TODO: Implement collection detail GUI
        player.sendMessage("§7Collection detail GUI not yet implemented for: " + collectionName);
    }
}
