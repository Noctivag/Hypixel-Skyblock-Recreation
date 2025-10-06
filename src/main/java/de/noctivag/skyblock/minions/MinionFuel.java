package de.noctivag.skyblock.minions;

import org.bukkit.Material;

/**
 * Represents fuel that can be used in minions
 */
public class MinionFuel {
    private final String id;
        private final String displayName;
        private final String description;
    private final Material material;
    private final String rarity;
    private final double efficiencyMultiplier;
    private final long duration; // Duration in milliseconds
    private final long cost;
    private final boolean isStackable;

    public MinionFuel(String id, String displayName, String description, Material material, 
                     String rarity, double efficiencyMultiplier, long duration, long cost, boolean isStackable) {
        this.id = id;
            this.displayName = displayName;
        this.description = description;
        this.material = material;
        this.rarity = rarity;
        this.efficiencyMultiplier = efficiencyMultiplier;
            this.duration = duration;
        this.cost = cost;
        this.isStackable = isStackable;
        }
        
    // Getters
    public String getId() { return id; }
        public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public Material getMaterial() { return material; }
    public String getRarity() { return rarity; }
    public double getEfficiencyMultiplier() { return efficiencyMultiplier; }
        public long getDuration() { return duration; }
    public long getCost() { return cost; }
    public boolean isStackable() { return isStackable; }

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
     * Get fuel lore
     */
    public String[] getFuelLore() {
        return new String[]{
            "&7" + description,
            "",
            "&7Efficiency: &a+" + (efficiencyMultiplier * 100) + "%",
            "&7Duration: &a" + formatDuration(duration),
            "&7Cost: &e" + formatNumber(cost) + " coins",
            "",
            isStackable ? "&a✓ Stackable" : "&c✗ Not Stackable",
            "",
            "&eClick to add to minion"
        };
    }

    /**
     * Get fuel icon
     */
    public String getFuelIcon() {
        switch (rarity.toLowerCase()) {
            case "common": return "⚡";
            case "uncommon": return "🔥";
            case "rare": return "💎";
            case "epic": return "🌟";
            case "legendary": return "👑";
            case "mythic": return "✨";
            case "special": return "🎯";
            default: return "⚡";
        }
    }

    /**
     * Get fuel type
     */
    public FuelType getFuelType() {
        if (efficiencyMultiplier >= 2.0) return FuelType.SUPER_FUEL;
        if (efficiencyMultiplier >= 1.5) return FuelType.ADVANCED_FUEL;
        if (efficiencyMultiplier >= 1.2) return FuelType.IMPROVED_FUEL;
        return FuelType.BASIC_FUEL;
    }

    /**
     * Get fuel category
     */
    public String getFuelCategory() {
        return getFuelType().getDisplayName();
    }

    /**
     * Check if this fuel is better than another
     */
    public boolean isBetterThan(MinionFuel other) {
        return this.efficiencyMultiplier > other.efficiencyMultiplier;
    }

    /**
     * Get fuel value (efficiency per cost)
     */
    public double getFuelValue() {
        if (cost == 0) return Double.MAX_VALUE;
        return efficiencyMultiplier / (cost / 1000.0); // Efficiency per 1000 coins
    }

    /**
     * Get estimated fuel efficiency per hour
     */
    public double getEfficiencyPerHour() {
        double hours = duration / (1000.0 * 60.0 * 60.0); // Convert to hours
        return efficiencyMultiplier / hours;
    }

    /**
     * Format duration
     */
    private String formatDuration(long duration) {
        long seconds = duration / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) {
            return days + "d " + (hours % 24) + "h";
        } else if (hours > 0) {
            return hours + "h " + (minutes % 60) + "m";
        } else if (minutes > 0) {
            return minutes + "m " + (seconds % 60) + "s";
        } else {
            return seconds + "s";
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
     * Create default fuels
     */
    public static MinionFuel[] createDefaultFuels() {
        return new MinionFuel[]{
            // Basic Fuels
            new MinionFuel("coal", "Coal", "Basic fuel for minions", 
                Material.COAL, "common", 1.1, 3600000, 100, true), // 1 hour
            new MinionFuel("charcoal", "Charcoal", "Basic fuel for minions", 
                Material.CHARCOAL, "common", 1.1, 3600000, 100, true), // 1 hour
            new MinionFuel("lava_bucket", "Lava Bucket", "Long-lasting fuel", 
                Material.LAVA_BUCKET, "uncommon", 1.2, 14400000, 500, false), // 4 hours
            
            // Improved Fuels
            new MinionFuel("blaze_rod", "Blaze Rod", "Improved fuel efficiency", 
                Material.BLAZE_ROD, "uncommon", 1.25, 7200000, 1000, true), // 2 hours
            new MinionFuel("coal_block", "Coal Block", "Compressed coal fuel", 
                Material.COAL_BLOCK, "uncommon", 1.3, 10800000, 1500, true), // 3 hours
            new MinionFuel("magma_cream", "Magma Cream", "Hot fuel for minions", 
                Material.MAGMA_CREAM, "rare", 1.4, 14400000, 2000, true), // 4 hours
            
            // Advanced Fuels
            new MinionFuel("enchanted_coal", "Enchanted Coal", "Magically enhanced coal", 
                Material.COAL, "rare", 1.5, 21600000, 5000, true), // 6 hours
            new MinionFuel("enchanted_charcoal", "Enchanted Charcoal", "Magically enhanced charcoal", 
                Material.CHARCOAL, "rare", 1.5, 21600000, 5000, true), // 6 hours
            new MinionFuel("enchanted_lava_bucket", "Enchanted Lava Bucket", "Superheated lava", 
                Material.LAVA_BUCKET, "epic", 1.6, 43200000, 10000, false), // 12 hours
            
            // Super Fuels
            new MinionFuel("enchanted_blaze_rod", "Enchanted Blaze Rod", "Magically enhanced blaze rod", 
                Material.BLAZE_ROD, "epic", 1.8, 43200000, 15000, true), // 12 hours
            new MinionFuel("enchanted_coal_block", "Enchanted Coal Block", "Magically compressed coal", 
                Material.COAL_BLOCK, "epic", 2.0, 64800000, 20000, true), // 18 hours
            new MinionFuel("enchanted_magma_cream", "Enchanted Magma Cream", "Superheated magma", 
                Material.MAGMA_CREAM, "legendary", 2.2, 86400000, 30000, true), // 24 hours
            
            // Special Fuels
            new MinionFuel("catalyst", "Catalyst", "Special fuel that doubles minion speed", 
                Material.NETHER_STAR, "mythic", 2.5, 172800000, 100000, false), // 48 hours
            new MinionFuel("hyper_catalyst", "Hyper Catalyst", "Ultimate fuel for minions", 
                Material.DRAGON_EGG, "mythic", 3.0, 259200000, 200000, false), // 72 hours
            new MinionFuel("plasma_bucket", "Plasma Bucket", "Experimental plasma fuel", 
                Material.LAVA_BUCKET, "special", 4.0, 604800000, 500000, false) // 168 hours (1 week)
        };
    }

    /**
     * Get fuel by ID
     */
    public static MinionFuel getById(String id) {
        for (MinionFuel fuel : createDefaultFuels()) {
            if (fuel.getId().equals(id)) {
                return fuel;
            }
        }
        return null;
    }
    
    /**
     * Get fuels by type
     */
    public static MinionFuel[] getByType(FuelType fuelType) {
        return java.util.Arrays.stream(createDefaultFuels())
                .filter(fuel -> fuel.getFuelType() == fuelType)
                .toArray(MinionFuel[]::new);
    }

    /**
     * Get fuels by rarity
     */
    public static MinionFuel[] getByRarity(String rarity) {
        return java.util.Arrays.stream(createDefaultFuels())
                .filter(fuel -> fuel.getRarity().equalsIgnoreCase(rarity))
                .toArray(MinionFuel[]::new);
    }

    /**
     * Get best fuel by efficiency
     */
    public static MinionFuel getBestFuel() {
        return java.util.Arrays.stream(createDefaultFuels())
                .max((a, b) -> Double.compare(a.getEfficiencyMultiplier(), b.getEfficiencyMultiplier()))
                .orElse(null);
    }

    /**
     * Get best fuel by value
     */
    public static MinionFuel getBestValueFuel() {
        return java.util.Arrays.stream(createDefaultFuels())
                .max((a, b) -> Double.compare(a.getFuelValue(), b.getFuelValue()))
                .orElse(null);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MinionFuel that = (MinionFuel) obj;
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
     * Enum for fuel types
     */
    public enum FuelType {
        BASIC_FUEL("Basic Fuel", "Basic fuel for minions"),
        IMPROVED_FUEL("Improved Fuel", "Improved fuel efficiency"),
        ADVANCED_FUEL("Advanced Fuel", "Advanced fuel technology"),
        SUPER_FUEL("Super Fuel", "Supercharged fuel");

        private final String displayName;
        private final String description;

        FuelType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
}