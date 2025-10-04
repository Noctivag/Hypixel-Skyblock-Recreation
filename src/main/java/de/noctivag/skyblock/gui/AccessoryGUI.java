package de.noctivag.skyblock.gui;

import org.bukkit.entity.Player;
import de.noctivag.skyblock.Plugin;

/**
 * Accessory GUI class - placeholder implementation
 */
public class AccessoryGUI {
    private final SkyblockPlugin plugin;
    private final Player player;
    
    public AccessoryGUI(SkyblockPlugin plugin, Player player, Object accessorySystem) {
        this.plugin = plugin;
        this.player = player;
    }
    
    public void openGUI(Player player) {
        player.sendMessage("§cAccessory GUI not implemented yet!");
    }
}
