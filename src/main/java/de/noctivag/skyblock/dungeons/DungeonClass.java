package de.noctivag.skyblock.dungeons;

import org.bukkit.Material;

/**
 * Enum representing all dungeon classes in Hypixel Skyblock
 */
public enum DungeonClass {
    ARCHER("Archer", "&a", "🏹", "Ranged damage dealer with high DPS", Material.BOW, 
           new String[]{"Arrow Storm", "Explosive Shot", "Piercing Shot"}, 
           new String[]{"High DPS", "Ranged Combat", "Mob Clearing"}),
    
    BERSERKER("Berserker", "&c", "⚔", "Melee damage dealer with high health", Material.DIAMOND_SWORD, 
              new String[]{"Berserk", "Rage", "Bloodlust"}, 
              new String[]{"High Health", "Melee Combat", "Tank"}),
    
    HEALER("Healer", "&d", "❤", "Support class that heals teammates", Material.GOLDEN_APPLE, 
           new String[]{"Heal", "Regeneration", "Revive"}, 
           new String[]{"Healing", "Support", "Team Survival"}),
    
    MAGE("Mage", "&b", "🔮", "Magic damage dealer with area effects", Material.BLAZE_ROD, 
         new String[]{"Fireball", "Lightning", "Teleport"}, 
         new String[]{"Magic Damage", "Area Effects", "Crowd Control"}),
    
    TANK("Tank", "&6", "🛡", "Defensive class that absorbs damage", Material.SHIELD, 
         new String[]{"Shield Wall", "Taunt", "Defensive Stance"}, 
         new String[]{"High Defense", "Damage Absorption", "Protection"});

    private final String displayName;
    private final String color;
    private final String icon;
    private final String description;
    private final Material material;
    private final String[] abilities;
    private final String[] strengths;

    DungeonClass(String displayName, String color, String icon, String description, Material material, 
                 String[] abilities, String[] strengths) {
        this.displayName = displayName;
        this.color = color;
        this.icon = icon;
        this.description = description;
        this.material = material;
        this.abilities = abilities;
        this.strengths = strengths;
    }

    public String getDisplayName() { return displayName; }
    public String getColor() { return color; }
    public String getIcon() { return icon; }
    public String getDescription() { return description; }
    public Material getMaterial() { return material; }
    public String[] getAbilities() { return abilities; }
    public String[] getStrengths() { return strengths; }

    /**
     * Get colored display name
     */
    public String getColoredDisplayName() {
        return color + icon + " " + displayName;
    }

    /**
     * Get class lore
     */
    public String[] getClassLore() {
        return new String[]{
            "&7" + description,
            "",
            "&7Abilities:",
            "&a• " + abilities[0],
            "&a• " + abilities[1],
            "&a• " + abilities[2],
            "",
            "&7Strengths:",
            "&e• " + strengths[0],
            "&e• " + strengths[1],
            "&e• " + strengths[2],
            "",
            "&eClick to select this class"
        };
    }

    /**
     * Get class statistics
     */
    public ClassStatistics getClassStatistics() {
        return new ClassStatistics(this);
    }

    /**
     * Class statistics inner class
     */
    public static class ClassStatistics {
        private final DungeonClass dungeonClass;
        private final int baseHealth;
        private final int baseDefense;
        private final int baseStrength;
        private final int baseSpeed;
        private final int baseIntelligence;
        private final int baseCritChance;
        private final int baseCritDamage;

        public ClassStatistics(DungeonClass dungeonClass) {
            this.dungeonClass = dungeonClass;
            
            // Set base stats based on class
            switch (dungeonClass) {
                case ARCHER:
                    this.baseHealth = 100;
                    this.baseDefense = 50;
                    this.baseStrength = 80;
                    this.baseSpeed = 120;
                    this.baseIntelligence = 60;
                    this.baseCritChance = 25;
                    this.baseCritDamage = 50;
                    break;
                case BERSERKER:
                    this.baseHealth = 150;
                    this.baseDefense = 80;
                    this.baseStrength = 120;
                    this.baseSpeed = 80;
                    this.baseIntelligence = 40;
                    this.baseCritChance = 20;
                    this.baseCritDamage = 40;
                    break;
                case HEALER:
                    this.baseHealth = 120;
                    this.baseDefense = 60;
                    this.baseStrength = 50;
                    this.baseSpeed = 90;
                    this.baseIntelligence = 100;
                    this.baseCritChance = 15;
                    this.baseCritDamage = 30;
                    break;
                case MAGE:
                    this.baseHealth = 80;
                    this.baseDefense = 40;
                    this.baseStrength = 60;
                    this.baseSpeed = 100;
                    this.baseIntelligence = 120;
                    this.baseCritChance = 30;
                    this.baseCritDamage = 60;
                    break;
                case TANK:
                    this.baseHealth = 200;
                    this.baseDefense = 150;
                    this.baseStrength = 70;
                    this.baseSpeed = 60;
                    this.baseIntelligence = 50;
                    this.baseCritChance = 10;
                    this.baseCritDamage = 20;
                    break;
                default:
                    this.baseHealth = 100;
                    this.baseDefense = 50;
                    this.baseStrength = 80;
                    this.baseSpeed = 100;
                    this.baseIntelligence = 60;
                    this.baseCritChance = 20;
                    this.baseCritDamage = 40;
                    break;
            }
        }

        // Getters
        public DungeonClass getDungeonClass() { return dungeonClass; }
        public int getBaseHealth() { return baseHealth; }
        public int getBaseDefense() { return baseDefense; }
        public int getBaseStrength() { return baseStrength; }
        public int getBaseSpeed() { return baseSpeed; }
        public int getBaseIntelligence() { return baseIntelligence; }
        public int getBaseCritChance() { return baseCritChance; }
        public int getBaseCritDamage() { return baseCritDamage; }

        /**
         * Get statistics lore
         */
        public String[] getStatisticsLore() {
            return new String[]{
                "&7Class Statistics:",
                "",
                "&c❤ Health: &a" + baseHealth,
                "&9🛡 Defense: &a" + baseDefense,
                "&c⚔ Strength: &a" + baseStrength,
                "&a⚡ Speed: &a" + baseSpeed,
                "&b🧠 Intelligence: &a" + baseIntelligence,
                "&6💥 Crit Chance: &a" + baseCritChance + "%",
                "&6💥 Crit Damage: &a" + baseCritDamage + "%"
            };
        }
    }
}
