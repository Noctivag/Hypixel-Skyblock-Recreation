package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.enums.DungeonClass;
import de.noctivag.skyblock.core.PlayerProfile;
import org.bukkit.entity.Player;

public class ClassManager {

    private final SkyblockPlugin plugin;

    public ClassManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean setPlayerClass(Player player, DungeonClass dungeonClass) {
        if (player == null || dungeonClass == null) {
            return false;
        }

        // TODO: Implement proper service manager integration
        // PlayerProfileService playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        // if (playerProfileService == null) {
        //     plugin.getLogger().warning("PlayerProfileService not available for class change.");
        //     return false;
        // }
        
        // PlayerProfile profile = playerProfileService.getCachedProfile(player.getUniqueId());
        PlayerProfile profile = null; // Placeholder
        if (profile == null) {
            plugin.getLogger().warning("Player profile not found for " + player.getName());
            return false;
        }

        DungeonClass oldClass = profile.getDungeonClass();
        profile.setDungeonClass(dungeonClass);
        
        player.sendMessage("§aDeine Dungeon-Klasse wurde von " + 
            (oldClass != null ? oldClass.getDisplayName() : "§7Keine") + 
            " §azu " + dungeonClass.getDisplayName() + " §ageändert!");
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Player " + player.getName() + " changed class from " + 
                (oldClass != null ? oldClass.name() : "null") + " to " + dungeonClass.name());
        }

        return true;
    }

    public DungeonClass getPlayerClass(Player player) {
        if (player == null) {
            return DungeonClass.ARCHER; // Default
        }

        // TODO: Implement proper service manager integration
        // PlayerProfileService playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        // if (playerProfileService == null) {
        //     return DungeonClass.ARCHER; // Default
        // }
        
        // PlayerProfile profile = playerProfileService.getCachedProfile(player.getUniqueId());
        PlayerProfile profile = null; // Placeholder
        if (profile == null) {
            return DungeonClass.ARCHER; // Default
        }

        return profile.getDungeonClass();
    }

    public boolean canChangeClass(Player player) {
        // In a real implementation, you might want to check:
        // - If player is in a dungeon
        // - If player has enough coins
        // - If player has unlocked the class
        // For now, always allow class changes
        return true;
    }
}