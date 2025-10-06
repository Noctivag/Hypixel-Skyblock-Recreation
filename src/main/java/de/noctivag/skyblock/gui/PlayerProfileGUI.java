package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

public class PlayerProfileGUI {
    private final SkyblockPlugin plugin;
    private final Player player;
    
    public PlayerProfileGUI(SkyblockPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }
    
    public void openGUI(Player player) {
        player.sendMessage("§7Player profile GUI not yet implemented");
    }
}
