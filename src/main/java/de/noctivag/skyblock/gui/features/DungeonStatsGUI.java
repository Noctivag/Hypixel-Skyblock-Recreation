package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

/**
 * GUI for dungeon statistics
 */
public class DungeonStatsGUI {
    
    private final SkyblockPlugin plugin;
    private final Player player;
    
    public DungeonStatsGUI(SkyblockPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }
    
    public void openGUI(Player player) {
        // TODO: Implement dungeon stats GUI
        player.sendMessage("§7Dungeon stats GUI not yet implemented");
    }
}
