package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

/**
 * GUI for dungeon chests
 */
public class DungeonChestsGUI {
    
    private final SkyblockPlugin plugin;
    private final Player player;
    
    public DungeonChestsGUI(SkyblockPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }
    
    public void openGUI(Player player) {
        // TODO: Implement dungeon chests GUI
        player.sendMessage("§7Dungeon chests GUI not yet implemented");
    }
}
