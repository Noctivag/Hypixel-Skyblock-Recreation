package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;

import org.bukkit.entity.Player;
import de.noctivag.skyblock.SkyblockPlugin;
import net.kyori.adventure.text.Component;

/**
 * Accessory GUI class - placeholder implementation
 */
public class AccessoryGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final Player player;
    
    public AccessoryGUI(SkyblockPlugin SkyblockPlugin, Player player, Object accessorySystem) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.player = player;
    }
    
    public void open(Player player) {
        openGUI(player);
    }
    
    public void openGUI(Player player) {
        player.sendMessage(Component.text("§cAccessory GUI not implemented yet!"));
    }
}
