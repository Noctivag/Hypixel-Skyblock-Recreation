package de.noctivag.skyblock.gui;

import org.bukkit.entity.Player;
import de.noctivag.skyblock.Plugin;

/**
 * Reforge GUI class - placeholder implementation
 */
public class ReforgeGUI {
    private final SkyblockPlugin plugin;
    private final Player player;
    
    public ReforgeGUI(SkyblockPlugin plugin, Player player, Object reforgeSystem, Object reforgeStoneSystem, Object statModificationSystem) {
        this.plugin = plugin;
        this.player = player;
    }
    
    public void openGUI(Player player) {
        player.sendMessage("§cReforge GUI not implemented yet!");
    }
}
