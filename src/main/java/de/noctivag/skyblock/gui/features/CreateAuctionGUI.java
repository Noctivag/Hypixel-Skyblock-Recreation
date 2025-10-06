package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

public class CreateAuctionGUI {
    private final SkyblockPlugin plugin;
    private final Player player;
    
    public CreateAuctionGUI(SkyblockPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }
    
    public void openGUI(Player player) {
        player.sendMessage("§7Create auction GUI not yet implemented");
    }
}