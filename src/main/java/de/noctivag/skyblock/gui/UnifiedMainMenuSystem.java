package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

/**
 * Unified Main Menu System - Placeholder implementation
 */
public class UnifiedMainMenuSystem {
    
    public enum MenuMode {
        BASIC, ADVANCED, ULTIMATE
    }
    
    private final SkyblockPlugin plugin;
    private final Player player;
    private final MenuMode mode;
    
    public UnifiedMainMenuSystem(SkyblockPlugin plugin, Player player, MenuMode mode) {
        this.plugin = plugin;
        this.player = player;
        this.mode = mode;
    }
    
    public void open(Player player) {
        player.sendMessage("§aUnified Main Menu opened in " + mode + " mode!");
        // TODO: Implement actual menu functionality
    }
}
