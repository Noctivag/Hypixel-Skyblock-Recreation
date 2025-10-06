package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

/**
 * GUI for waypoint management
 */
public class WaypointManagerGUI {
    
    private final SkyblockPlugin plugin;
    private final Player player;
    
    public WaypointManagerGUI(SkyblockPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }
    
    public void openGUI(Player player) {
        // TODO: Implement waypoint manager GUI
        player.sendMessage("§7Waypoint manager GUI not yet implemented");
    }
}
