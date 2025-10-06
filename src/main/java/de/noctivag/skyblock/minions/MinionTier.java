package de.noctivag.skyblock.minions;

import org.bukkit.Material;

/**
 * Represents a specific tier of a minion
 */
public class MinionTier {
    private final MinionType minionType;
    private final int tier;
    
    // Tier-specific properties
    private final double actionTime;
    private final int storageCapacity;
    private final int fuelSlots;
    private final int upgradeSlots;
    private final int minionSlotsRequired;
    private final long upgradeCost;
    private final long upgradeExperience;

    public MinionTier(MinionType minionType, int tier) {
        this.minionType = minionType;
        this.tier = tier;
        
        // Calculate tier-specific properties
        this.actionTime = minionType.getActionTime(tier);
        this.storageCapacity = minionType.getStorageCapacity(tier);
        this.fuelSlots = minionType.getFuelSlots(tier);
        this.upgradeSlots = minionType.getUpgradeSlots(tier);
        this.minionSlotsRequired = minionType.getMinionSlotsRequired(tier);
        this.upgradeCost = minionType.getUpgradeCost(tier);
        this.upgradeExperience = minionType.getUpgradeExperience(tier);
    }

    // Getters
    public MinionType getMinionType() { return minionType; }
    public int getTier() { return tier; }
    public double getActionTime() { return actionTime; }
    public int getStorageCapacity() { return storageCapacity; }
    public int getFuelSlots() { return fuelSlots; }
    public int getUpgradeSlots() { return upgradeSlots; }
    public int getMinionSlotsRequired() { return minionSlotsRequired; }
    public long getUpgradeCost() { return upgradeCost; }
    public long getUpgradeExperience() { return upgradeExperience; }

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
        return minionType.getColor() + minionType.getIcon() + " " + minionType.getDisplayName() + " " + getTierDisplayName();
    }

    /**
     * Get tier description
     */
    public String getTierDescription() {
        return "&7Tier " + getTierDisplayName() + " " + minionType.getCategory() + " Minion";
    }

    /**
     * Get tier lore information
     */
    public String[] getTierLore() {
        return new String[]{
            getTierDescription(),
            "",
            "&7Action Time: &a" + String.format("%.1f", actionTime) + "s",
            "&7Storage: &a" + storageCapacity + " items",
            "&7Fuel Slots: &a" + fuelSlots,
            "&7Upgrade Slots: &a" + upgradeSlots,
            "&7Minion Slots: &a" + minionSlotsRequired,
            "",
            "&7Produces: &a" + minionType.getMaterial().name().toLowerCase().replace("_", " "),
            "&7Category: &a" + minionType.getCategory(),
            "",
            upgradeCost > 0 ? "&7Upgrade Cost: &e" + formatNumber(upgradeCost) + " coins" : "&cMax Tier Reached"
        };
    }

    /**
     * Check if this is the max tier
     */
    public boolean isMaxTier() {
        return tier >= minionType.getMaxTier();
    }

    /**
     * Get next tier
     */
    public MinionTier getNextTier() {
        if (isMaxTier()) return null;
        return new MinionTier(minionType, tier + 1);
    }

    /**
     * Get previous tier
     */
    public MinionTier getPreviousTier() {
        if (tier <= 1) return null;
        return new MinionTier(minionType, tier - 1);
    }

    /**
     * Get tier efficiency multiplier
     */
    public double getEfficiencyMultiplier() {
        // Higher tiers are more efficient
        return 1.0 + (tier - 1) * 0.1; // 10% efficiency increase per tier
    }

    /**
     * Get tier rarity
     */
    public String getTierRarity() {
        if (tier >= 12) return "mythic";
        if (tier >= 10) return "legendary";
        if (tier >= 8) return "epic";
        if (tier >= 6) return "rare";
        if (tier >= 4) return "uncommon";
        return "common";
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
     * Get production rate per hour
     */
    public double getProductionRatePerHour() {
        // Calculate how many items this minion produces per hour
        double actionsPerHour = 3600.0 / actionTime; // 3600 seconds in an hour
        return actionsPerHour * getEfficiencyMultiplier();
    }

    /**
     * Get estimated daily production
     */
    public double getEstimatedDailyProduction() {
        return getProductionRatePerHour() * 24; // 24 hours in a day
    }

    /**
     * Get estimated weekly production
     */
    public double getEstimatedWeeklyProduction() {
        return getEstimatedDailyProduction() * 7; // 7 days in a week
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
            case 10: return "X";
            case 11: return "XI";
            case 12: return "XII";
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
        MinionTier that = (MinionTier) obj;
        return tier == that.tier && minionType == that.minionType;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(minionType, tier);
    }

    @Override
    public String toString() {
        return getFullDisplayName();
    }
}
