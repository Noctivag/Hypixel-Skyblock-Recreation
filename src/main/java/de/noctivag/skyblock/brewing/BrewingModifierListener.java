package de.noctivag.skyblock.brewing;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

/**
 * Listener for brewing modifier events
 */
public class BrewingModifierListener implements Listener {
    
    private final SkyblockPlugin plugin;
    private final BrewingModifierManager modifierManager;
    
    public BrewingModifierListener(SkyblockPlugin plugin, BrewingModifierManager modifierManager) {
        this.plugin = plugin;
        this.modifierManager = modifierManager;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID playerUuid = event.getPlayer().getUniqueId();
        
        // Load player modifiers when they join
        // Implementation would load from database
        
        plugin.getLogger().info("Loaded brewing modifiers for player: " + event.getPlayer().getName());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerUuid = event.getPlayer().getUniqueId();
        
        // Save player modifiers when they leave
        // Implementation would save to database
        
        plugin.getLogger().info("Saved brewing modifiers for player: " + event.getPlayer().getName());
    }
}
