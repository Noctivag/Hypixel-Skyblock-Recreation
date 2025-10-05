package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;

import org.bukkit.entity.Player;
import de.noctivag.skyblock.SkyblockPlugin;
import net.kyori.adventure.text.Component;

/**
 * Reforge GUI class - placeholder implementation
 */
public class ReforgeGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final Player player;
    
    public ReforgeGUI(SkyblockPlugin SkyblockPlugin, Player player, Object reforgeSystem, Object reforgeStoneSystem, Object statModificationSystem) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.player = player;
    }
    
    public void openGUI(Player player) {
        player.sendMessage(Component.text("§cReforge GUI not implemented yet!"));
    }
}
