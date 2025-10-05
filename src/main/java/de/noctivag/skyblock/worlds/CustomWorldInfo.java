package de.noctivag.skyblock.worlds;

import org.bukkit.World;

/**
 * Custom world information class
 */
public class CustomWorldInfo {
    
    private final String worldName;
    private final World world;
    private final String templateName;
    private final long creationTime;
    private final boolean isActive;
    
    public CustomWorldInfo(String worldName, World world, String templateName, long creationTime, boolean isActive) {
        this.worldName = worldName;
        this.world = world;
        this.templateName = templateName;
        this.creationTime = creationTime;
        this.isActive = isActive;
    }
    
    public String getWorldName() {
        return worldName;
    }
    
    public World getWorld() {
        return world;
    }
    
    public String getTemplateName() {
        return templateName;
    }
    
    public long getCreationTime() {
        return creationTime;
    }
    
    public boolean isActive() {
        return isActive;
    }
}
