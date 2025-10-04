package de.noctivag.plugin.features.tools.types;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Player Tools class for storing player tool data
 */
public class PlayerTools {
    private UUID playerId;
    private Map<ToolType, Integer> toolLevels;
    private Map<ToolType, Long> toolExperience;
    private Map<ToolType, Boolean> toolUnlocked;

    public PlayerTools(UUID playerId) {
        this.playerId = playerId;
        this.toolLevels = new HashMap<>();
        this.toolExperience = new HashMap<>();
        this.toolUnlocked = new HashMap<>();
        
        // Initialize all tools as locked
        for (ToolType toolType : ToolType.values()) {
            toolLevels.put(toolType, 0);
            toolExperience.put(toolType, 0L);
            toolUnlocked.put(toolType, false);
        }
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Map<ToolType, Integer> getToolLevels() {
        return toolLevels;
    }

    public Map<ToolType, Long> getToolExperience() {
        return toolExperience;
    }

    public Map<ToolType, Boolean> getToolUnlocked() {
        return toolUnlocked;
    }

    public int getToolLevel(ToolType toolType) {
        return toolLevels.getOrDefault(toolType, 0);
    }

    public long getToolExperience(ToolType toolType) {
        return toolExperience.getOrDefault(toolType, 0L);
    }

    public boolean isToolUnlocked(ToolType toolType) {
        return toolUnlocked.getOrDefault(toolType, false);
    }

    public void setToolLevel(ToolType toolType, int level) {
        toolLevels.put(toolType, level);
    }

    public void setToolExperience(ToolType toolType, long experience) {
        toolExperience.put(toolType, experience);
    }

    public void setToolUnlocked(ToolType toolType, boolean unlocked) {
        toolUnlocked.put(toolType, unlocked);
    }

    public void addToolExperience(ToolType toolType, long experience) {
        long currentExp = getToolExperience(toolType);
        toolExperience.put(toolType, currentExp + experience);
    }
}
