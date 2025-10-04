package de.noctivag.plugin.items;

/**
 * Potato book class
 */
public class PotatoBook {
    
    private final String id;
    private final String name;
    private final String description;
    private final PotatoBookType type;
    private final PotatoBookRarity rarity;
    private final int minLevel;
    private final int maxLevel;
    private final PotatoBookEffect effect;
    
    public PotatoBook(String id, String name, String description, 
                     PotatoBookType type, PotatoBookRarity rarity, 
                     int minLevel, int maxLevel, PotatoBookEffect effect) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.rarity = rarity;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.effect = effect;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public PotatoBookType getType() {
        return type;
    }
    
    public PotatoBookRarity getRarity() {
        return rarity;
    }
    
    public int getMinLevel() {
        return minLevel;
    }
    
    public int getMaxLevel() {
        return maxLevel;
    }
    
    public PotatoBookEffect getEffect() {
        return effect;
    }
}
