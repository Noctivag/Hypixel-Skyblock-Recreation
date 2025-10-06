package de.noctivag.skyblock.wardrobe;

import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Represents an armor set in the wardrobe system
 */
public class ArmorSet {
    
    private final String id;
    private final String name;
    private final String color;
    private final String description;
    private final List<ItemStack> armor;
    private final long createdAt;
    
    public ArmorSet(String id, String name, String color, String description, List<ItemStack> armor) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.description = description;
        this.armor = armor;
        this.createdAt = System.currentTimeMillis();
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getColor() { return color; }
    public String getDescription() { return description; }
    public List<ItemStack> getArmor() { return armor; }
    public long getCreatedAt() { return createdAt; }
    
    /**
     * Get the display name with color
     */
    public String getDisplayName() {
        return color + name;
    }
    
    /**
     * Get the helmet
     */
    public ItemStack getHelmet() {
        return armor.size() > 0 ? armor.get(0) : null;
    }
    
    /**
     * Get the chestplate
     */
    public ItemStack getChestplate() {
        return armor.size() > 1 ? armor.get(1) : null;
    }
    
    /**
     * Get the leggings
     */
    public ItemStack getLeggings() {
        return armor.size() > 2 ? armor.get(2) : null;
    }
    
    /**
     * Get the boots
     */
    public ItemStack getBoots() {
        return armor.size() > 3 ? armor.get(3) : null;
    }
    
    /**
     * Check if this is a complete armor set
     */
    public boolean isComplete() {
        return armor.size() == 4 && armor.stream().allMatch(item -> item != null);
    }
    
    /**
     * Get the number of armor pieces
     */
    public int getArmorCount() {
        return (int) armor.stream().filter(item -> item != null).count();
    }
    
    /**
     * Get the total defense value of the armor set
     */
    public double getTotalDefense() {
        double defense = 0.0;
        
        for (ItemStack item : armor) {
            if (item != null) {
                switch (item.getType()) {
                    case LEATHER_HELMET:
                    case CHAINMAIL_HELMET:
                    case IRON_HELMET:
                    case GOLDEN_HELMET:
                    case DIAMOND_HELMET:
                    case NETHERITE_HELMET:
                        defense += 2.0;
                        break;
                    case LEATHER_CHESTPLATE:
                    case CHAINMAIL_CHESTPLATE:
                        defense += 5.0;
                        break;
                    case IRON_CHESTPLATE:
                    case GOLDEN_CHESTPLATE:
                        defense += 6.0;
                        break;
                    case DIAMOND_CHESTPLATE:
                        defense += 8.0;
                        break;
                    case NETHERITE_CHESTPLATE:
                        defense += 8.0;
                        break;
                    case LEATHER_LEGGINGS:
                    case CHAINMAIL_LEGGINGS:
                        defense += 3.0;
                        break;
                    case IRON_LEGGINGS:
                    case GOLDEN_LEGGINGS:
                        defense += 5.0;
                        break;
                    case DIAMOND_LEGGINGS:
                        defense += 6.0;
                        break;
                    case NETHERITE_LEGGINGS:
                        defense += 6.0;
                        break;
                    case LEATHER_BOOTS:
                    case CHAINMAIL_BOOTS:
                    case IRON_BOOTS:
                    case GOLDEN_BOOTS:
                    case DIAMOND_BOOTS:
                    case NETHERITE_BOOTS:
                        defense += 1.0;
                        break;
                    default:
                        // Non-armor items or unknown materials - no defense bonus
                        break;
                }
            }
        }
        
        return defense;
    }
    
    /**
     * Get the armor set rarity based on materials
     */
    public String getRarity() {
        if (armor.stream().anyMatch(item -> item != null && item.getType().name().contains("NETHERITE"))) {
            return "mythic";
        } else if (armor.stream().anyMatch(item -> item != null && item.getType().name().contains("DIAMOND"))) {
            return "legendary";
        } else if (armor.stream().anyMatch(item -> item != null && item.getType().name().contains("IRON"))) {
            return "rare";
        } else if (armor.stream().anyMatch(item -> item != null && item.getType().name().contains("GOLD"))) {
            return "epic";
        } else if (armor.stream().anyMatch(item -> item != null && item.getType().name().contains("CHAINMAIL"))) {
            return "uncommon";
        } else {
            return "common";
        }
    }
}
