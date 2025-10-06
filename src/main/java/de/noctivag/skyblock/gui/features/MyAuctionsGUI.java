package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

public class MyAuctionsGUI {
    private final SkyblockPlugin plugin;
    private final Player player;
    
    public MyAuctionsGUI(SkyblockPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }
    
    public void openGUI(Player player) {
        player.sendMessage("§7My auctions GUI not yet implemented");
    }
}