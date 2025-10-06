package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

/**
 * GUI for master mode dungeons
 */
public class MasterModeGUI {
    
    private final SkyblockPlugin plugin;
    private final Player player;
    
    public MasterModeGUI(SkyblockPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }
    
    public void openGUI(Player player) {
        // TODO: Implement master mode GUI
        player.sendMessage("§7Master mode GUI not yet implemented");
    }
}
