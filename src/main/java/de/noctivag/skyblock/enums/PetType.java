package de.noctivag.skyblock.enums;

import org.bukkit.Material;

public enum PetType {
    // Legendary Pets
    ENDER_DRAGON(Material.DRAGON_HEAD, "§dEnder Dragon", "§7Das stärkste Pet für reinen Schaden"),
    ELEPHANT(Material.ELEPHANT_SPAWN_EGG, "§6Elephant", "§7Das beste Pet für das Farmen"),
    GRIFFIN(Material.PHANTOM_MEMBRANE, "§bGriffin", "§7Nützlich während Mythological Ritual Events"),
    
    // Epic Pets
    TIGER(Material.ORANGE_DYE, "§6Tiger", "§7Erhöht Crit-Chance und Crit-Damage"),
    LION(Material.YELLOW_DYE, "§eLion", "§7Erhöht Stärke und Geschwindigkeit"),
    WOLF(Material.BONE, "§7Wolf", "§7Erhöht Crit-Damage und Gesundheit"),
    
    // Rare Pets
    HORSE(Material.SADDLE, "§fHorse", "§7Erhöht Geschwindigkeit und Sprungkraft"),
    CAT(Material.STRING, "§eCat", "§7Erhöht Glück und Erfahrung"),
    DOG(Material.BONE, "§6Dog", "§7Erhöht Gesundheit und Verteidigung"),
    
    // Uncommon Pets
    CHICKEN(Material.EGG, "§fChicken", "§7Erhöht Farming Fortune"),
    COW(Material.LEATHER, "§7Cow", "§7Erhöht Gesundheit"),
    PIG(Material.PORKCHOP, "§dPig", "§7Erhöht Geschwindigkeit"),
    
    // Common Pets
    RABBIT(Material.RABBIT_FOOT, "§fRabbit", "§7Erhöht Sprungkraft"),
    SHEEP(Material.WHITE_WOOL, "§fSheep", "§7Erhöht Glück"),
    BAT(Material.COAL, "§8Bat", "§7Erhöht Nachtsicht");

    private final Material icon;
    private final String displayName;
    private final String description;

    PetType(Material icon, String displayName, String description) {
        this.icon = icon;
        this.displayName = displayName;
        this.description = description;
    }

    public Material getIcon() {
        return icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public Rarity getRarity() {
        switch (this) {
            case ENDER_DRAGON:
            case ELEPHANT:
            case GRIFFIN:
                return Rarity.LEGENDARY;
            case TIGER:
            case LION:
            case WOLF:
                return Rarity.EPIC;
            case HORSE:
            case CAT:
            case DOG:
                return Rarity.RARE;
            case CHICKEN:
            case COW:
            case PIG:
                return Rarity.UNCOMMON;
            default:
                return Rarity.COMMON;
        }
    }

    public int getMaxLevel() {
        return getRarity() == Rarity.LEGENDARY ? 100 : 50;
    }

    public double getBaseStatBoost() {
        switch (getRarity()) {
            case LEGENDARY:
                return 10.0;
            case EPIC:
                return 7.5;
            case RARE:
                return 5.0;
            case UNCOMMON:
                return 3.0;
            default:
                return 1.5;
        }
    }
}
