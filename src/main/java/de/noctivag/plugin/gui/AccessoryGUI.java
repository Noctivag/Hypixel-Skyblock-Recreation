package de.noctivag.plugin.gui;

import org.bukkit.entity.Player;
import de.noctivag.plugin.Plugin;

/**
 * Accessory GUI class - placeholder implementation
 */
public class AccessoryGUI {
    private final Plugin plugin;
    private final Player player;
    
    public AccessoryGUI(Plugin plugin, Player player, Object accessorySystem) {
        this.plugin = plugin;
        this.player = player;
    }
    
    public void openGUI(Player player) {
        player.sendMessage("§cAccessory GUI not implemented yet!");
    }
}
