package de.noctivag.skyblock.slayers;

import org.bukkit.Material;

/**
 * Represents a specific tier of a slayer
 */
public class SlayerTier {
    private final SlayerType slayerType;
    private final int tier;

    // Tier-specific properties
    private final long requirement;
    private final long cost;
    private final long reward;
    private final long xpReward;
    private final String bossName;
    private final int health;
    private final int damage;
    private final int defense;

    public SlayerTier(SlayerType slayerType, int tier) {
        this.slayerType = slayerType;
        this.tier = tier;

        // Calculate tier-specific properties
        this.requirement = slayerType.getTierRequirement(tier);
        this.cost = slayerType.getSlayerCost(tier);
        this.reward = slayerType.getSlayerReward(tier);
        this.xpReward = slayerType.getSlayerXPReward(tier);
        this.bossName = slayerType.getBossForTier(tier);

        // Calculate boss stats based on tier
        this.health = calculateBossHealth(tier);
        this.damage = calculateBossDamage(tier);
        this.defense = calculateBossDefense(tier);
    }

    // Getters
    public SlayerType getSlayerType() { return slayerType; }
    public int getTier() { return tier; }
    public long getRequirement() { return requirement; }
    public long getCost() { return cost; }
    public long getReward() { return reward; }
    public long getXpReward() { return xpReward; }
    public String getBossName() { return bossName; }
    public int getHealth() { return health; }
    public int getDamage() { return damage; }
    public int getDefense() { return defense; }

    /**
     * Get tier display name
     */
    public String getTierDisplayName() {
        return getRomanNumeral(tier);
    }

    /**
     * Get full display name with tier
     */
    public String getFullDisplayName() {
        return slayerType.getColor() + slayerType.getIcon() + " " + slayerType.getDisplayName() + " " + getTierDisplayName();
    }

    /**
     * Get tier description
     */
    public String getTierDescription() {
        return "&7Tier " + getTierDisplayName() + " " + slayerType.getDisplayName();
    }

    /**
     * Get tier lore
     */
    public String[] getTierLore() {
        return new String[]{
            getTierDescription(),
            "",
            "&7Boss: &c" + bossName,
            "&7Health: &c" + formatNumber(health),
            "&7Damage: &c" + damage,
            "&7Defense: &9" + defense,
            "",
            "&7Requirement: &a" + formatNumber(requirement) + " XP",
            "&7Cost: &e" + formatNumber(cost) + " coins",
            "&7Reward: &a" + formatNumber(reward) + " coins",
            "&7XP Reward: &b" + formatNumber(xpReward) + " XP",
            "",
            "&7Location: &a" + slayerType.getSpawnLocation(),
            "&7Difficulty: " + slayerType.getDifficultyColor() + slayerType.getDifficulty(),
            "",
            "&eClick to start this slayer quest"
        };
    }

    /**
     * Check if this is the max tier
     */
    public boolean isMaxTier() {
        return tier >= slayerType.getMaxTier();
    }

    /**
     * Get next tier
     */
    public SlayerTier getNextTier() {
        if (isMaxTier()) return null;
        return new SlayerTier(slayerType, tier + 1);
    }

    /**
     * Get previous tier
     */
    public SlayerTier getPreviousTier() {
        if (tier <= 1) return null;
        return new SlayerTier(slayerType, tier - 1);
    }

    /**
     * Get tier rarity
     */
    public String getTierRarity() {
        if (tier >= 9) return "mythic";
        if (tier >= 7) return "legendary";
        if (tier >= 5) return "epic";
        if (tier >= 3) return "rare";
        return "uncommon";
    }

    /**
     * Get tier color
     */
    public String getTierColor() {
        switch (getTierRarity()) {
            case "mythic": return "&d";
            case "legendary": return "&6";
            case "epic": return "&5";
            case "rare": return "&b";
            case "uncommon": return "&a";
            default: return "&7";
        }
    }

    /**
     * Get tier material for GUI display
     */
    public Material getTierMaterial() {
        switch (getTierRarity()) {
            case "mythic": return Material.NETHER_STAR;
            case "legendary": return Material.DRAGON_EGG;
            case "epic": return Material.EMERALD_BLOCK;
            case "rare": return Material.DIAMOND_BLOCK;
            case "uncommon": return Material.GOLD_BLOCK;
            default: return Material.IRON_BLOCK;
        }
    }

    /**
     * Get estimated completion time in minutes
     */
    public int getEstimatedCompletionTime() {
        // Base time increases with tier
        return 5 + (tier * 2); // 5-23 minutes
    }

    /**
     * Get tier difficulty multiplier
     */
    public double getDifficultyMultiplier() {
        return 1.0 + (tier - 1) * 0.5; // 1.0x to 5.0x difficulty
    }

    /**
     * Calculate boss health based on tier
     */
    private int calculateBossHealth(int tier) {
        int baseHealth = 10000; // Base health
        return (int) (baseHealth * Math.pow(2, tier - 1)); // Exponential growth
    }

    /**
     * Calculate boss damage based on tier
     */
    private int calculateBossDamage(int tier) {
        int baseDamage = 100; // Base damage
        return baseDamage + (tier - 1) * 50; // Linear growth
    }

    /**
     * Calculate boss defense based on tier
     */
    private int calculateBossDefense(int tier) {
        int baseDefense = 50; // Base defense
        return baseDefense + (tier - 1) * 25; // Linear growth
    }

    /**
     * Convert tier number to Roman numeral
     */
    private String getRomanNumeral(int number) {
        switch (number) {
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            case 4: return "IV";
            case 5: return "V";
            case 6: return "VI";
            case 7: return "VII";
            case 8: return "VIII";
            case 9: return "IX";
            default: return String.valueOf(number);
        }
    }

    /**
     * Format large numbers
     */
    private String formatNumber(long number) {
        if (number >= 1_000_000_000_000L) {
            return String.format("%.1fT", number / 1_000_000_000_000.0);
        } else if (number >= 1_000_000_000L) {
            return String.format("%.1fB", number / 1_000_000_000.0);
        } else if (number >= 1_000_000L) {
            return String.format("%.1fM", number / 1_000_000.0);
        } else if (number >= 1_000L) {
            return String.format("%.1fK", number / 1_000.0);
        } else {
            return String.valueOf(number);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SlayerTier that = (SlayerTier) obj;
        return tier == that.tier && slayerType == that.slayerType;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(slayerType, tier);
    }

    @Override
    public String toString() {
        return getFullDisplayName();
    }
}
