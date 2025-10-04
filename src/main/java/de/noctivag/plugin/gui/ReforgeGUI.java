package de.noctivag.plugin.gui;

import org.bukkit.entity.Player;
import de.noctivag.plugin.Plugin;

/**
 * Reforge GUI class - placeholder implementation
 */
public class ReforgeGUI {
    private final Plugin plugin;
    private final Player player;
    
    public ReforgeGUI(Plugin plugin, Player player, Object reforgeSystem, Object reforgeStoneSystem, Object statModificationSystem) {
        this.plugin = plugin;
        this.player = player;
    }
    
    public void openGUI(Player player) {
        player.sendMessage("§cReforge GUI not implemented yet!");
    }
}
