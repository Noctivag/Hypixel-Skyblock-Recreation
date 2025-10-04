package de.noctivag.skyblock.features.cosmetics.config;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * Cosmetic Configuration
 */
public class CosmeticConfig {
    private final String id;
    private final String name;
    private final String description;
    private final CosmeticCategory category;
    private final CosmeticRarity rarity;
    private final List<String> requirements;
    private final Map<String, Object> properties;
    
    public CosmeticConfig(String id, String name, String description, CosmeticCategory category,
                         CosmeticRarity rarity, List<String> requirements, Map<String, Object> properties) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.rarity = rarity;
        this.requirements = requirements;
        this.properties = properties;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public CosmeticCategory getCategory() { return category; }
    public CosmeticRarity getRarity() { return rarity; }
    public List<String> getRequirements() { return requirements; }
    public Map<String, Object> getProperties() { return properties; }
    
    public enum CosmeticCategory {
        PET_SKINS, CAPES, HATS, PARTICLES, EMOTES, TITLES, BADGES, CLOAK, WINGS, AURA, TRAIL
    }
    
    public enum CosmeticRarity {
        COMMON("§fCommon"),
        UNCOMMON("§aUncommon"),
        RARE("§9Rare"),
        EPIC("§5Epic"),
        LEGENDARY("§6Legendary"),
        MYTHIC("§dMythic");
        
        private final String displayName;
        
        CosmeticRarity(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() { return displayName; }
    }
}
