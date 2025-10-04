package de.noctivag.plugin.features.armor;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.features.armor.types.ArmorRarity;
import de.noctivag.plugin.features.armor.types.ArmorSlot;
import de.noctivag.plugin.features.armor.types.DragonArmorType;

/**
 * Represents a dragon armor piece
 */
public class DragonArmorPiece {
    
    private final DragonArmorType armorType;
    private final ArmorSlot slot;
    private final ArmorRarity rarity;
    
    private int durability;
    private final int maxDurability;
    private int level;
    private int stars; // Dungeon stars
    private boolean reforged;
    private String reforge;
    
    public DragonArmorPiece(DragonArmorType armorType, ArmorSlot slot) {
        this.armorType = armorType;
        this.slot = slot;
        this.rarity = armorType.getRarity();
        this.maxDurability = getMaxDurabilityForSlot(slot);
        this.durability = maxDurability;
        this.level = 1;
        this.stars = 0;
        this.reforged = false;
        this.reforge = null;
    }
    
    /**
     * Get max durability for armor slot
     */
    private int getMaxDurabilityForSlot(ArmorSlot slot) {
        return switch (slot) {
            case HELMET -> 165;
            case CHESTPLATE -> 240;
            case LEGGINGS -> 225;
            case BOOTS -> 195;
        };
    }
    
    /**
     * Damage armor
     */
    public void damage(int amount) {
        this.durability = Math.max(0, durability - amount);
    }
    
    /**
     * Repair armor
     */
    public void repair() {
        this.durability = maxDurability;
    }
    
    /**
     * Check if armor is broken
     */
    public boolean isBroken() {
        return durability <= 0;
    }
    
    /**
     * Get durability percentage
     */
    public double getDurabilityPercentage() {
        if (maxDurability <= 0) return 100.0;
        return (double) durability / maxDurability * 100.0;
    }
    
    /**
     * Level up armor
     */
    public boolean levelUp() {
        if (level >= 50) return false; // Max level 50
        
        level++;
        return true;
    }
    
    /**
     * Add dungeon star
     */
    public boolean addStar() {
        if (stars >= 5) return false; // Max 5 stars
        
        stars++;
        return true;
    }
    
    /**
     * Reforge armor
     */
    public boolean reforge(String reforgeType) {
        this.reforge = reforgeType;
        this.reforged = true;
        return true;
    }
    
    /**
     * Remove reforge
     */
    public void removeReforge() {
        this.reforge = null;
        this.reforged = false;
    }
    
    /**
     * Get base stats for this armor piece
     */
    public ArmorStats getBaseStats() {
        DragonArmorType.ArmorBaseStats baseStats = armorType.getBaseStats();
        
        // Apply slot multiplier
        double slotMultiplier = getSlotMultiplier();
        
        return new ArmorStats(
            (int) (baseStats.getHealth() * slotMultiplier),
            (int) (baseStats.getDefense() * slotMultiplier),
            (int) (baseStats.getStrength() * slotMultiplier),
            (int) (baseStats.getIntelligence() * slotMultiplier),
            (int) (baseStats.getSpeed() * slotMultiplier)
        );
    }
    
    /**
     * Get slot multiplier for stats
     */
    private double getSlotMultiplier() {
        return switch (slot) {
            case HELMET -> 1.0;
            case CHESTPLATE -> 1.2;
            case LEGGINGS -> 1.1;
            case BOOTS -> 0.9;
        };
    }
    
    /**
     * Get total stats including upgrades
     */
    public ArmorStats getTotalStats() {
        ArmorStats baseStats = getBaseStats();
        
        // Apply level bonus
        double levelMultiplier = 1.0 + (level - 1) * 0.02; // 2% per level
        
        // Apply star bonus
        double starMultiplier = 1.0 + stars * 0.1; // 10% per star
        
        // Apply rarity multiplier
        double rarityMultiplier = rarity.getMultiplier();
        
        double totalMultiplier = levelMultiplier * starMultiplier * rarityMultiplier;
        
        return new ArmorStats(
            (int) (baseStats.getHealth() * totalMultiplier),
            (int) (baseStats.getDefense() * totalMultiplier),
            (int) (baseStats.getStrength() * totalMultiplier),
            (int) (baseStats.getIntelligence() * totalMultiplier),
            (int) (baseStats.getSpeed() * totalMultiplier)
        );
    }
    
    /**
     * Get armor display name
     */
    public String getDisplayName() {
        StringBuilder name = new StringBuilder();
        
        // Add rarity color
        name.append(rarity.getColor());
        
        // Add stars
        for (int i = 0; i < stars; i++) {
            name.append("⭐");
        }
        
        // Add armor name
        name.append(" ").append(armorType.getDisplayName()).append(" ").append(slot.getDisplayName());
        
        // Add level if > 1
        if (level > 1) {
            name.append(" [Lv.").append(level).append("]");
        }
        
        // Add reforge if any
        if (reforged && reforge != null) {
            name.append(" ").append(reforge);
        }
        
        return name.toString();
    }
    
    /**
     * Get armor lore
     */
    public String[] getLore() {
        ArmorStats stats = getTotalStats();
        
        return new String[]{
            "§7" + armorType.getDescription(),
            "",
            "§7Health: §a+" + stats.getHealth(),
            "§7Defense: §a+" + stats.getDefense(),
            "§7Strength: §a+" + stats.getStrength(),
            "§7Intelligence: §a+" + stats.getIntelligence(),
            "§7Speed: §a+" + stats.getSpeed(),
            "",
            "§7Durability: §a" + durability + "/" + maxDurability,
            "§7Stars: §e" + stars + "/5",
            "§7Level: §b" + level + "/50"
        };
    }
    
    // Getters
    public DragonArmorType getArmorType() {
        return armorType;
    }
    
    public ArmorSlot getSlot() {
        return slot;
    }
    
    public ArmorRarity getRarity() {
        return rarity;
    }
    
    public int getDurability() {
        return durability;
    }
    
    public int getMaxDurability() {
        return maxDurability;
    }
    
    public int getLevel() {
        return level;
    }
    
    public int getStars() {
        return stars;
    }
    
    public boolean isReforged() {
        return reforged;
    }
    
    public String getReforge() {
        return reforge;
    }
    
    @Override
    public String toString() {
        return getDisplayName();
    }
}
