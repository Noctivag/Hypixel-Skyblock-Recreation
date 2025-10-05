package de.noctivag.skyblock.worlds;

import java.util.Map;

/**
 * World configuration class
 */
public class WorldConfig {
    
    private final String worldName;
    private final String templateName;
    private final Map<String, Object> settings;
    private final boolean autoReset;
    private final long resetInterval;
    
    public WorldConfig(String worldName, String templateName, Map<String, Object> settings, boolean autoReset, long resetInterval) {
        this.worldName = worldName;
        this.templateName = templateName;
        this.settings = settings;
        this.autoReset = autoReset;
        this.resetInterval = resetInterval;
    }
    
    public String getWorldName() {
        return worldName;
    }
    
    public String getTemplateName() {
        return templateName;
    }
    
    public Map<String, Object> getSettings() {
        return settings;
    }
    
    public boolean isAutoReset() {
        return autoReset;
    }
    
    public long getResetInterval() {
        return resetInterval;
    }
}
