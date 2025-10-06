package de.noctivag.skyblock.minions;

import org.bukkit.Material;

/**
 * Represents a minion upgrade that can be applied to minions
 */
public class MinionUpgrade {
    private final String id;
    private final String displayName;
    private final String description;
    private final Material material;
    private final String rarity;
    private final UpgradeType upgradeType;
    private final double effectValue;
    private final long cost;
    private final boolean stackable;

    public MinionUpgrade(String id, String displayName, String description, Material material, 
                        String rarity, UpgradeType upgradeType, double effectValue, long cost, boolean stackable) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.material = material;
        this.rarity = rarity;
        this.upgradeType = upgradeType;
        this.effectValue = effectValue;
        this.cost = cost;
        this.stackable = stackable;
    }

    // Getters
    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public Material getMaterial() { return material; }
    public String getRarity() { return rarity; }
    public UpgradeType getUpgradeType() { return upgradeType; }
    public double getEffectValue() { return effectValue; }
    public long getCost() { return cost; }
    public boolean isStackable() { return stackable; }

    /**
     * Get colored display name
     */
    public String getColoredDisplayName() {
        return getRarityColor() + displayName;
    }

    /**
     * Get rarity color
     */
    public String getRarityColor() {
        switch (rarity.toLowerCase()) {
            case "common": return "&7";
            case "uncommon": return "&a";
            case "rare": return "&9";
            case "epic": return "&5";
            case "legendary": return "&6";
            case "mythic": return "&d";
            case "special": return "&c";
            default: return "&7";
        }
    }

    /**
     * Get upgrade lore
     */
    public String[] getUpgradeLore() {
        return new String[]{
            "&7" + description,
            "",
            "&7Type: &a" + upgradeType.getDisplayName(),
            "&7Effect: &a" + getEffectDescription(),
            "&7Cost: &e" + formatNumber(cost) + " coins",
            "",
            stackable ? "&a✓ Stackable" : "&c✗ Not Stackable",
            "",
            "&eClick to apply to minion"
        };
    }

    /**
     * Get effect description
     */
    public String getEffectDescription() {
        switch (upgradeType) {
            case SPEED:
                return "+" + (effectValue * 100) + "% Speed";
            case STORAGE:
                return "+" + (int) effectValue + " Storage";
            case FUEL_EFFICIENCY:
                return "+" + (effectValue * 100) + "% Fuel Efficiency";
            case DOUBLE_DROPS:
                return "+" + (effectValue * 100) + "% Double Drop Chance";
            case AUTO_SMELTER:
                return "Auto-smelts ores";
            case AUTO_SELLER:
                return "Auto-sells items";
            case COMPACTOR:
                return "Compacts items";
            case SUPER_COMPACTOR:
                return "Super-compacts items";
            case DIAMOND_SPREADING:
                return "Produces diamonds";
            case FLINT_SHOVEL:
                return "Produces flint from gravel";
            case MINION_EXPANDER:
                return "Increases minion range";
            case BUDDY_UPGRADE:
                return "Spawns a buddy";
            default:
                return "Unknown effect";
        }
    }

    /**
     * Check if this upgrade can be applied to a minion type
     */
    public boolean canApplyTo(MinionType minionType) {
        switch (upgradeType) {
            case AUTO_SMELTER:
                // Only applies to mining minions
                return minionType.getCategory().equals("Mining");
            case FLINT_SHOVEL:
                // Only applies to gravel minion
                return minionType == MinionType.GRAVEL_MINION;
            case DIAMOND_SPREADING:
                // Applies to all minions
                return true;
            default:
                return true;
        }
    }

    /**
     * Get upgrade icon
     */
    public String getUpgradeIcon() {
        switch (upgradeType) {
            case SPEED: return "⚡";
            case STORAGE: return "📦";
            case FUEL_EFFICIENCY: return "⛽";
            case DOUBLE_DROPS: return "💎";
            case AUTO_SMELTER: return "🔥";
            case AUTO_SELLER: return "💰";
            case COMPACTOR: return "📦";
            case SUPER_COMPACTOR: return "💎";
            case DIAMOND_SPREADING: return "💎";
            case FLINT_SHOVEL: return "⛏";
            case MINION_EXPANDER: return "📏";
            case BUDDY_UPGRADE: return "👥";
            default: return "✨";
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

    /**
     * Create default upgrades
     */
    public static MinionUpgrade[] createDefaultUpgrades() {
        return new MinionUpgrade[]{
            // Speed upgrades
            new MinionUpgrade("speed_1", "Minion Speed I", "Increases minion speed by 10%", 
                Material.SUGAR, "common", UpgradeType.SPEED, 0.1, 1000, true),
            new MinionUpgrade("speed_2", "Minion Speed II", "Increases minion speed by 25%", 
                Material.SUGAR, "uncommon", UpgradeType.SPEED, 0.25, 5000, true),
            new MinionUpgrade("speed_3", "Minion Speed III", "Increases minion speed by 50%", 
                Material.SUGAR, "rare", UpgradeType.SPEED, 0.5, 25000, true),
            
            // Storage upgrades
            new MinionUpgrade("storage_1", "Minion Storage I", "Increases minion storage by 3 slots", 
                Material.CHEST, "common", UpgradeType.STORAGE, 3, 2000, false),
            new MinionUpgrade("storage_2", "Minion Storage II", "Increases minion storage by 6 slots", 
                Material.CHEST, "uncommon", UpgradeType.STORAGE, 6, 10000, false),
            new MinionUpgrade("storage_3", "Minion Storage III", "Increases minion storage by 9 slots", 
                Material.CHEST, "rare", UpgradeType.STORAGE, 9, 50000, false),
            
            // Special upgrades
            new MinionUpgrade("auto_smelter", "Auto Smelter", "Automatically smelts ores", 
                Material.FURNACE, "uncommon", UpgradeType.AUTO_SMELTER, 1.0, 15000, false),
            new MinionUpgrade("auto_seller", "Auto Seller", "Automatically sells items", 
                Material.EMERALD, "rare", UpgradeType.AUTO_SELLER, 1.0, 50000, false),
            new MinionUpgrade("compactor", "Compactor", "Compacts items into blocks", 
                Material.PISTON, "uncommon", UpgradeType.COMPACTOR, 1.0, 20000, false),
            new MinionUpgrade("super_compactor", "Super Compactor 3000", "Super-compacts items", 
                Material.PISTON, "epic", UpgradeType.SUPER_COMPACTOR, 1.0, 100000, false),
            new MinionUpgrade("diamond_spreading", "Diamond Spreading", "Produces diamonds", 
                Material.DIAMOND, "legendary", UpgradeType.DIAMOND_SPREADING, 0.1, 200000, false),
            new MinionUpgrade("flint_shovel", "Flint Shovel", "Produces flint from gravel", 
                Material.FLINT, "uncommon", UpgradeType.FLINT_SHOVEL, 1.0, 10000, false),
            new MinionUpgrade("minion_expander", "Minion Expander", "Increases minion range", 
                Material.REDSTONE, "rare", UpgradeType.MINION_EXPANDER, 1.0, 30000, false),
            new MinionUpgrade("buddy_upgrade", "Buddy Upgrade", "Spawns a buddy to help", 
                Material.PLAYER_HEAD, "epic", UpgradeType.BUDDY_UPGRADE, 1.0, 150000, false)
        };
    }

    /**
     * Get upgrade by ID
     */
    public static MinionUpgrade getById(String id) {
        for (MinionUpgrade upgrade : createDefaultUpgrades()) {
            if (upgrade.getId().equals(id)) {
                return upgrade;
            }
        }
        return null;
    }

    /**
     * Get upgrades by type
     */
    public static MinionUpgrade[] getByType(UpgradeType upgradeType) {
        return java.util.Arrays.stream(createDefaultUpgrades())
                .filter(upgrade -> upgrade.getUpgradeType() == upgradeType)
                .toArray(MinionUpgrade[]::new);
    }

    /**
     * Get upgrades by rarity
     */
    public static MinionUpgrade[] getByRarity(String rarity) {
        return java.util.Arrays.stream(createDefaultUpgrades())
                .filter(upgrade -> upgrade.getRarity().equalsIgnoreCase(rarity))
                .toArray(MinionUpgrade[]::new);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MinionUpgrade that = (MinionUpgrade) obj;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return getColoredDisplayName();
    }

    /**
     * Enum for upgrade types
     */
    public enum UpgradeType {
        SPEED("Speed", "Increases minion action speed"),
        STORAGE("Storage", "Increases minion storage capacity"),
        FUEL_EFFICIENCY("Fuel Efficiency", "Increases fuel efficiency"),
        DOUBLE_DROPS("Double Drops", "Chance for double drops"),
        AUTO_SMELTER("Auto Smelter", "Automatically smelts items"),
        AUTO_SELLER("Auto Seller", "Automatically sells items"),
        COMPACTOR("Compactor", "Compacts items into blocks"),
        SUPER_COMPACTOR("Super Compactor", "Super-compacts items"),
        DIAMOND_SPREADING("Diamond Spreading", "Produces diamonds"),
        FLINT_SHOVEL("Flint Shovel", "Produces flint from gravel"),
        MINION_EXPANDER("Minion Expander", "Increases minion range"),
        BUDDY_UPGRADE("Buddy Upgrade", "Spawns a buddy");

        private final String displayName;
        private final String description;

        UpgradeType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
}