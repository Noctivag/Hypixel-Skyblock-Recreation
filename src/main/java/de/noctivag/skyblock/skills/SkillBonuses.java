package de.noctivag.skyblock.skills;

/**
 * Represents the stat bonuses granted by a skill level
 */
public class SkillBonuses {
    private final SkillType skillType;
    private final int level;
    
    // Stat bonuses
    private double healthBonus;
    private double defenseBonus;
    private double strengthBonus;
    private double speedBonus;
    private double intelligenceBonus;
    private double critChanceBonus;
    private double critDamageBonus;
    private double attackSpeedBonus;
    private double ferocityBonus;
    private double magicFindBonus;
    private double petLuckBonus;
    private double seaCreatureChanceBonus;
    private double fishingSpeedBonus;
    private double miningSpeedBonus;
    private double foragingSpeedBonus;
    private double farmingSpeedBonus;

    public SkillBonuses(SkillType skillType, int level) {
        this.skillType = skillType;
        this.level = level;
        
        // Calculate bonuses based on skill type and level
        this.healthBonus = calculateHealthBonus();
        this.defenseBonus = calculateDefenseBonus();
        this.strengthBonus = calculateStrengthBonus();
        this.speedBonus = calculateSpeedBonus();
        this.intelligenceBonus = calculateIntelligenceBonus();
        this.critChanceBonus = calculateCritChanceBonus();
        this.critDamageBonus = calculateCritDamageBonus();
        this.attackSpeedBonus = calculateAttackSpeedBonus();
        this.ferocityBonus = calculateFerocityBonus();
        this.magicFindBonus = calculateMagicFindBonus();
        this.petLuckBonus = calculatePetLuckBonus();
        this.seaCreatureChanceBonus = calculateSeaCreatureChanceBonus();
        this.fishingSpeedBonus = calculateFishingSpeedBonus();
        this.miningSpeedBonus = calculateMiningSpeedBonus();
        this.foragingSpeedBonus = calculateForagingSpeedBonus();
        this.farmingSpeedBonus = calculateFarmingSpeedBonus();
    }

    private double calculateHealthBonus() {
        switch (skillType) {
            case COMBAT:
                return level * 2.0; // +2 HP per level
            case FARMING:
                return level * 1.0; // +1 HP per level
            case FISHING:
                return level * 1.0; // +1 HP per level
            default:
                return 0;
        }
    }

    private double calculateDefenseBonus() {
        switch (skillType) {
            case MINING:
                return level * 1.0; // +1 Defense per level
            default:
                return 0;
        }
    }

    private double calculateStrengthBonus() {
        switch (skillType) {
            case FORAGING:
                return level * 1.0; // +1 Strength per level
            default:
                return 0;
        }
    }

    private double calculateSpeedBonus() {
        switch (skillType) {
            case CARPENTRY:
                return level * 0.1; // +0.1 Speed per level
            default:
                return 0;
        }
    }

    private double calculateIntelligenceBonus() {
        switch (skillType) {
            case ENCHANTING:
                return level * 1.0; // +1 Intelligence per level
            case ALCHEMY:
                return level * 1.0; // +1 Intelligence per level
            case RUNECRAFTING:
                return level * 1.0; // +1 Intelligence per level
            default:
                return 0;
        }
    }

    private double calculateCritChanceBonus() {
        return 0; // No skills provide crit chance bonus
    }

    private double calculateCritDamageBonus() {
        return 0; // No skills provide crit damage bonus
    }

    private double calculateAttackSpeedBonus() {
        return 0; // No skills provide attack speed bonus
    }

    private double calculateFerocityBonus() {
        return 0; // No skills provide ferocity bonus
    }

    private double calculateMagicFindBonus() {
        return 0; // No skills provide magic find bonus
    }

    private double calculatePetLuckBonus() {
        return 0; // No skills provide pet luck bonus
    }

    private double calculateSeaCreatureChanceBonus() {
        switch (skillType) {
            case FISHING:
                return level * 0.1; // +0.1% Sea Creature Chance per level
            default:
                return 0;
        }
    }

    private double calculateFishingSpeedBonus() {
        switch (skillType) {
            case FISHING:
                return level * 0.1; // +0.1% Fishing Speed per level
            default:
                return 0;
        }
    }

    private double calculateMiningSpeedBonus() {
        switch (skillType) {
            case MINING:
                return level * 0.1; // +0.1% Mining Speed per level
            default:
                return 0;
        }
    }

    private double calculateForagingSpeedBonus() {
        switch (skillType) {
            case FORAGING:
                return level * 0.1; // +0.1% Foraging Speed per level
            default:
                return 0;
        }
    }

    private double calculateFarmingSpeedBonus() {
        switch (skillType) {
            case FARMING:
                return level * 0.1; // +0.1% Farming Speed per level
            default:
                return 0;
        }
    }

    // Getters
    public SkillType getSkillType() { return skillType; }
    public int getLevel() { return level; }
    public double getHealthBonus() { return healthBonus; }
    public double getDefenseBonus() { return defenseBonus; }
    public double getStrengthBonus() { return strengthBonus; }
    public double getSpeedBonus() { return speedBonus; }
    public double getIntelligenceBonus() { return intelligenceBonus; }
    public double getCritChanceBonus() { return critChanceBonus; }
    public double getCritDamageBonus() { return critDamageBonus; }
    public double getAttackSpeedBonus() { return attackSpeedBonus; }
    public double getFerocityBonus() { return ferocityBonus; }
    public double getMagicFindBonus() { return magicFindBonus; }
    public double getPetLuckBonus() { return petLuckBonus; }
    public double getSeaCreatureChanceBonus() { return seaCreatureChanceBonus; }
    public double getFishingSpeedBonus() { return fishingSpeedBonus; }
    public double getMiningSpeedBonus() { return miningSpeedBonus; }
    public double getForagingSpeedBonus() { return foragingSpeedBonus; }
    public double getFarmingSpeedBonus() { return farmingSpeedBonus; }

    /**
     * Get total bonuses from all skills combined
     */
    public static SkillBonuses combine(SkillBonuses... bonuses) {
        double totalHealth = 0, totalDefense = 0, totalStrength = 0, totalSpeed = 0;
        double totalIntelligence = 0, totalCritChance = 0, totalCritDamage = 0;
        double totalAttackSpeed = 0, totalFerocity = 0, totalMagicFind = 0;
        double totalPetLuck = 0, totalSeaCreatureChance = 0, totalFishingSpeed = 0;
        double totalMiningSpeed = 0, totalForagingSpeed = 0, totalFarmingSpeed = 0;

        for (SkillBonuses bonus : bonuses) {
            if (bonus != null) {
                totalHealth += bonus.healthBonus;
                totalDefense += bonus.defenseBonus;
                totalStrength += bonus.strengthBonus;
                totalSpeed += bonus.speedBonus;
                totalIntelligence += bonus.intelligenceBonus;
                totalCritChance += bonus.critChanceBonus;
                totalCritDamage += bonus.critDamageBonus;
                totalAttackSpeed += bonus.attackSpeedBonus;
                totalFerocity += bonus.ferocityBonus;
                totalMagicFind += bonus.magicFindBonus;
                totalPetLuck += bonus.petLuckBonus;
                totalSeaCreatureChance += bonus.seaCreatureChanceBonus;
                totalFishingSpeed += bonus.fishingSpeedBonus;
                totalMiningSpeed += bonus.miningSpeedBonus;
                totalForagingSpeed += bonus.foragingSpeedBonus;
                totalFarmingSpeed += bonus.farmingSpeedBonus;
            }
        }

        // Create a combined bonuses object (using COMBAT as placeholder skill type)
        SkillBonuses combined = new SkillBonuses(SkillType.COMBAT, 0);
        combined.healthBonus = totalHealth;
        combined.defenseBonus = totalDefense;
        combined.strengthBonus = totalStrength;
        combined.speedBonus = totalSpeed;
        combined.intelligenceBonus = totalIntelligence;
        combined.critChanceBonus = totalCritChance;
        combined.critDamageBonus = totalCritDamage;
        combined.attackSpeedBonus = totalAttackSpeed;
        combined.ferocityBonus = totalFerocity;
        combined.magicFindBonus = totalMagicFind;
        combined.petLuckBonus = totalPetLuck;
        combined.seaCreatureChanceBonus = totalSeaCreatureChance;
        combined.fishingSpeedBonus = totalFishingSpeed;
        combined.miningSpeedBonus = totalMiningSpeed;
        combined.foragingSpeedBonus = totalForagingSpeed;
        combined.farmingSpeedBonus = totalFarmingSpeed;

        return combined;
    }
}
