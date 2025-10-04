package de.noctivag.skyblock.features.collections.config;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * Collection Configuration
 */
public class CollectionConfig {
    private final String id;
    private final String name;
    private final String description;
    private final CollectionCategory category;
    private final List<String> items;
    private final Map<Integer, String> milestones;
    private final Map<String, Object> properties;
    
    public CollectionConfig(String id, String name, String description, CollectionCategory category,
                          List<String> items, Map<Integer, String> milestones, Map<String, Object> properties) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.items = items;
        this.milestones = milestones;
        this.properties = properties;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public CollectionCategory getCategory() { return category; }
    public List<String> getItems() { return items; }
    public Map<Integer, String> getMilestones() { return milestones; }
    public Map<String, Object> getProperties() { return properties; }
    
    public enum CollectionCategory {
        FARMING, MINING, COMBAT, FORAGING, FISHING, ENCHANTING, ALCHEMY, TAMING, CARPENTRY, RUNECRAFTING, SOCIAL, DUNGEONEERING
    }
}
