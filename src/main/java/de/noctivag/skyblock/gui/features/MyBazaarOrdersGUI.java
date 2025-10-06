package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

/**
 * GUI for managing bazaar orders
 */
public class MyBazaarOrdersGUI {
    
    private final SkyblockPlugin plugin;
    private final Player player;
    
    public MyBazaarOrdersGUI(SkyblockPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }
    
    public void openGUI(Player player) {
        // TODO: Implement bazaar orders GUI
        player.sendMessage("§7Bazaar orders GUI not yet implemented");
    }
}
