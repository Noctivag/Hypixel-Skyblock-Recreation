package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

/**
 * GUI for all recipes
 */
public class AllRecipesGUI {
    
    private final SkyblockPlugin plugin;
    private final Player player;
    
    public AllRecipesGUI(SkyblockPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }
    
    public void openGUI(Player player) {
        // TODO: Implement all recipes GUI
        player.sendMessage("§7All recipes GUI not yet implemented");
    }
}
