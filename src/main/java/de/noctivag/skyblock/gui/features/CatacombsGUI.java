package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

/**
 * GUI for catacombs dungeons
 */
public class CatacombsGUI {
    
    private final SkyblockPlugin plugin;
    private final Player player;
    
    public CatacombsGUI(SkyblockPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }
    
    public void openGUI(Player player) {
        // TODO: Implement catacombs GUI
        player.sendMessage("§7Catacombs GUI not yet implemented");
    }
}
