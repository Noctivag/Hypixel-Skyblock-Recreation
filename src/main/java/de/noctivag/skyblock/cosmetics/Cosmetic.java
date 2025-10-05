package de.noctivag.skyblock.cosmetics;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * Represents a cosmetic item
 */
public class Cosmetic {
    
    private final String id;
    private final String name;
    private final String description;
    private final CosmeticType type;
    private final Rarity rarity;
    private final int requiredLevel;
    private final double price;
    private final List<String> requirements;
    private final Map<String, Object> properties;
    private final ItemStack displayItem;
    private final boolean enabled;
    private final long duration; // -1 for permanent
    
    public Cosmetic(String id, String name, String description, CosmeticType type, Rarity rarity,
                   int requiredLevel, double price, List<String> requirements,
                   Map<String, Object> properties, ItemStack displayItem, boolean enabled, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.rarity = rarity;
        this.requiredLevel = requiredLevel;
        this.price = price;
        this.requirements = requirements;
        this.properties = properties;
        this.displayItem = displayItem;
        this.enabled = enabled;
        this.duration = duration;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public CosmeticType getType() { return type; }
    public Rarity getRarity() { return rarity; }
    public int getRequiredLevel() { return requiredLevel; }
    public double getPrice() { return price; }
    public List<String> getRequirements() { return requirements; }
    public Map<String, Object> getProperties() { return properties; }
    public ItemStack getDisplayItem() { return displayItem; }
    public boolean isEnabled() { return enabled; }
    public long getDuration() { return duration; }
    
    /**
     * Get property value
     */
    public Object getProperty(String key, Object defaultValue) {
        return properties.getOrDefault(key, defaultValue);
    }
    
    /**
     * Get property as string
     */
    public String getPropertyAsString(String key, String defaultValue) {
        Object value = properties.get(key);
        return value != null ? value.toString() : defaultValue;
    }
    
    /**
     * Get property as integer
     */
    public int getPropertyAsInt(String key, int defaultValue) {
        Object value = properties.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }
    
    /**
     * Get property as double
     */
    public double getPropertyAsDouble(String key, double defaultValue) {
        Object value = properties.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return defaultValue;
    }
    
    /**
     * Get property as boolean
     */
    public boolean getPropertyAsBoolean(String key, boolean defaultValue) {
        Object value = properties.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return defaultValue;
    }
    
    /**
     * Get hat material for HAT type cosmetics
     */
    public Material getHatMaterial() {
        if (type != CosmeticType.HAT) return null;
        
        String materialStr = getPropertyAsString("material", "DIAMOND_HELMET");
        try {
            return Material.valueOf(materialStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Material.DIAMOND_HELMET;
        }
    }
    
    /**
     * Get particle type for PARTICLE type cosmetics
     */
    public Particle getParticleType() {
        if (type != CosmeticType.PARTICLE) return Particle.HEART;
        
        String particleStr = getPropertyAsString("particle_type", "HEART");
        try {
            return Particle.valueOf(particleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Particle.HEART;
        }
    }
    
    /**
     * Get particle count for PARTICLE type cosmetics
     */
    public int getParticleCount() {
        return getPropertyAsInt("particle_count", 3);
    }
    
    /**
     * Get particle speed for PARTICLE type cosmetics
     */
    public double getParticleSpeed() {
        return getPropertyAsDouble("particle_speed", 0.1);
    }
    
    /**
     * Get particle radius for PARTICLE type cosmetics
     */
    public double getParticleRadius() {
        return getPropertyAsDouble("particle_radius", 1.5);
    }
    
    /**
     * Cosmetic rarity enum
     */
    public enum Rarity {
        COMMON("§f", "Common"),
        UNCOMMON("§a", "Uncommon"),
        RARE("§9", "Rare"),
        EPIC("§5", "Epic"),
        LEGENDARY("§6", "Legendary"),
        MYTHIC("§d", "Mythic"),
        SPECIAL("§c", "Special"),
        VERY_SPECIAL("§c", "Very Special");
        
        private final String color;
        private final String displayName;
        
        Rarity(String color, String displayName) {
            this.color = color;
            this.displayName = displayName;
        }
        
        public String getColor() {
            return color;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public String getColoredName() {
            return color + displayName;
        }
    }
}