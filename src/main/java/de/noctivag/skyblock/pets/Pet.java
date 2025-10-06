package de.noctivag.skyblock.pets;

import org.bukkit.Material;

import java.util.UUID;

/**
 * Represents an individual pet instance
 */
public class Pet {
    private final String petId;
    private final UUID ownerId;
    private final PetType petType;
    private final int level;
    private final long xp;
    private final boolean isActive;
    private final long createdAt;
    private final PetStats stats;

    public Pet(String petId, UUID ownerId, PetType petType, int level, long xp, boolean isActive) {
        this.petId = petId;
        this.ownerId = ownerId;
        this.petType = petType;
        this.level = Math.max(1, Math.min(level, petType.getMaxLevel()));
        this.xp = Math.max(0, xp);
        this.isActive = isActive;
        this.createdAt = System.currentTimeMillis();
        this.stats = new PetStats(this);
    }

    public Pet(String petId, UUID ownerId, PetType petType, int level, long xp, boolean isActive, long createdAt) {
        this.petId = petId;
        this.ownerId = ownerId;
        this.petType = petType;
        this.level = Math.max(1, Math.min(level, petType.getMaxLevel()));
        this.xp = Math.max(0, xp);
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.stats = new PetStats(this);
    }

    // Getters
    public String getPetId() { return petId; }
    public UUID getOwnerId() { return ownerId; }
    public PetType getPetType() { return petType; }
    public int getLevel() { return level; }
    public long getXp() { return xp; }
    public boolean isActive() { return isActive; }
    public long getCreatedAt() { return createdAt; }
    public PetStats getStats() { return stats; }

    /**
     * Get pet display name
     */
    public String getDisplayName() {
        return petType.getColoredDisplayName() + " " + getTierDisplayName();
    }

    /**
     * Get pet tier
     */
    public int getTier() {
        return petType.getTierFromLevel(level);
    }

    /**
     * Get tier display name
     */
    public String getTierDisplayName() {
        return petType.getTierDisplayName(getTier());
    }

    /**
     * Get tier color
     */
    public String getTierColor() {
        return petType.getTierColor(getTier());
    }

    /**
     * Get pet lore
     */
    public String[] getPetLore() {
        return new String[]{
            "&7" + petType.getDescription(),
            "",
            "&7Level: &a" + level + "/" + petType.getMaxLevel(),
            "&7Tier: " + getTierColor() + getTierDisplayName(),
            "&7XP: &e" + formatNumber(xp) + "/" + formatNumber(getXPRequiredForNextLevel()),
            "&7Category: &a" + petType.getCategory(),
            "",
            "&7Abilities:",
            "&a• " + petType.getAbilities()[0],
            "&a• " + petType.getAbilities()[1],
            "&a• " + petType.getAbilities()[2],
            "",
            "&7Benefits:",
            "&e• " + petType.getBenefits()[0],
            "&e• " + petType.getBenefits()[1],
            "&e• " + petType.getBenefits()[2],
            "",
            "&7Stats:",
            "&c❤ Health: &a" + stats.getHealth(),
            "&9🛡 Defense: &a" + stats.getDefense(),
            "&c⚔ Strength: &a" + stats.getStrength(),
            "&a⚡ Speed: &a" + stats.getSpeed(),
            "&b🧠 Intelligence: &a" + stats.getIntelligence(),
            "",
            "&7Status: " + (isActive ? "&aActive" : "&7Inactive"),
            "&7Created: &a" + getFormattedCreationTime(),
            "",
            "&eClick to " + (isActive ? "deactivate" : "activate") + " pet"
        };
    }

    /**
     * Get XP required for next level
     */
    public long getXPRequiredForNextLevel() {
        if (level >= petType.getMaxLevel()) {
            return 0; // Max level reached
        }
        return petType.getXPRequirement(level + 1);
    }

    /**
     * Get XP progress for current level
     */
    public long getXPProgress() {
        if (level >= petType.getMaxLevel()) {
            return 0; // Max level reached
        }
        long currentLevelXP = petType.getXPRequirement(level);
        return xp - currentLevelXP;
    }

    /**
     * Get XP required for current level
     */
    public long getXPRequiredForCurrentLevel() {
        if (level >= petType.getMaxLevel()) {
            return 0; // Max level reached
        }
        long currentLevelXP = petType.getXPRequirement(level);
        long nextLevelXP = petType.getXPRequirement(level + 1);
        return nextLevelXP - currentLevelXP;
    }

    /**
     * Get level progress percentage
     */
    public double getLevelProgressPercentage() {
        if (level >= petType.getMaxLevel()) {
            return 100.0; // Max level reached
        }
        long currentLevelXP = petType.getXPRequirement(level);
        long nextLevelXP = petType.getXPRequirement(level + 1);
        long progress = xp - currentLevelXP;
        long required = nextLevelXP - currentLevelXP;
        
        if (required == 0) return 100.0;
        return Math.min(100.0, (double) progress / required * 100.0);
    }

    /**
     * Check if pet is at max level
     */
    public boolean isMaxLevel() {
        return level >= petType.getMaxLevel();
    }

    /**
     * Get pet age in days
     */
    public long getAgeInDays() {
        return (System.currentTimeMillis() - createdAt) / (1000 * 60 * 60 * 24);
    }

    /**
     * Get formatted creation time
     */
    public String getFormattedCreationTime() {
        long days = getAgeInDays();
        if (days == 0) return "Today";
        if (days == 1) return "1 day ago";
        if (days < 7) return days + " days ago";
        if (days < 30) return (days / 7) + " weeks ago";
        if (days < 365) return (days / 30) + " months ago";
        return (days / 365) + " years ago";
    }

    /**
     * Get pet rarity
     */
    public String getRarity() {
        return petType.getRarity();
    }

    /**
     * Get pet rarity color
     */
    public String getRarityColor() {
        return petType.getRarityColor();
    }

    /**
     * Get pet material for GUI display
     */
    public Material getMaterial() {
        return petType.getMaterial();
    }

    /**
     * Get pet category
     */
    public String getCategory() {
        return petType.getCategory();
    }

    /**
     * Get pet abilities
     */
    public String[] getAbilities() {
        return petType.getAbilities();
    }

    /**
     * Get pet benefits
     */
    public String[] getBenefits() {
        return petType.getBenefits();
    }

    /**
     * Get pet description
     */
    public String getDescription() {
        return petType.getDescription();
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
        Pet pet = (Pet) obj;
        return petId.equals(pet.petId);
    }

    @Override
    public int hashCode() {
        return petId.hashCode();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    /**
     * Pet statistics inner class
     */
    public static class PetStats {
        private final Pet pet;
        private final int health;
        private final int defense;
        private final int strength;
        private final int speed;
        private final int intelligence;

        public PetStats(Pet pet) {
            this.pet = pet;
            
            // Calculate stats based on pet type, level, and tier
            int baseStat = 10 + (pet.getLevel() / 10); // Base stat increases with level
            int tierMultiplier = 1 + pet.getTier(); // Tier multiplier
            
            this.health = calculateStat(baseStat, tierMultiplier, "health");
            this.defense = calculateStat(baseStat, tierMultiplier, "defense");
            this.strength = calculateStat(baseStat, tierMultiplier, "strength");
            this.speed = calculateStat(baseStat, tierMultiplier, "speed");
            this.intelligence = calculateStat(baseStat, tierMultiplier, "intelligence");
        }

        // Getters
        public Pet getPet() { return pet; }
        public int getHealth() { return health; }
        public int getDefense() { return defense; }
        public int getStrength() { return strength; }
        public int getSpeed() { return speed; }
        public int getIntelligence() { return intelligence; }

        /**
         * Calculate stat based on pet type and category
         */
        private int calculateStat(int baseStat, int tierMultiplier, String statType) {
            int stat = baseStat * tierMultiplier;
            
            // Apply category bonuses
            switch (pet.getPetType().getCategory()) {
                case "Combat":
                    if (statType.equals("strength") || statType.equals("health")) {
                        stat = (int) (stat * 1.2); // 20% bonus for combat stats
                    }
                    break;
                case "Mining":
                    if (statType.equals("defense") || statType.equals("strength")) {
                        stat = (int) (stat * 1.2); // 20% bonus for mining stats
                    }
                    break;
                case "Farming":
                    if (statType.equals("speed") || statType.equals("intelligence")) {
                        stat = (int) (stat * 1.2); // 20% bonus for farming stats
                    }
                    break;
                case "Foraging":
                    if (statType.equals("speed") || statType.equals("strength")) {
                        stat = (int) (stat * 1.2); // 20% bonus for foraging stats
                    }
                    break;
                case "Fishing":
                    if (statType.equals("intelligence") || statType.equals("speed")) {
                        stat = (int) (stat * 1.2); // 20% bonus for fishing stats
                    }
                    break;
                case "Special":
                    // Special pets get balanced stats
                    stat = (int) (stat * 1.1); // 10% bonus to all stats
                    break;
            }
            
            return Math.max(1, stat); // Minimum stat of 1
        }

        /**
         * Get total stat points
         */
        public int getTotalStats() {
            return health + defense + strength + speed + intelligence;
        }

        /**
         * Get average stat
         */
        public double getAverageStat() {
            return getTotalStats() / 5.0;
        }

        /**
         * Get highest stat
         */
        public int getHighestStat() {
            return Math.max(Math.max(Math.max(health, defense), Math.max(strength, speed)), intelligence);
        }

        /**
         * Get lowest stat
         */
        public int getLowestStat() {
            return Math.min(Math.min(Math.min(health, defense), Math.min(strength, speed)), intelligence);
        }

        /**
         * Get stat distribution
         */
        public String[] getStatDistribution() {
            return new String[]{
                "&7Stat Distribution:",
                "",
                "&c❤ Health: &a" + health + " &7(" + String.format("%.1f", (double) health / getTotalStats() * 100) + "%)",
                "&9🛡 Defense: &a" + defense + " &7(" + String.format("%.1f", (double) defense / getTotalStats() * 100) + "%)",
                "&c⚔ Strength: &a" + strength + " &7(" + String.format("%.1f", (double) strength / getTotalStats() * 100) + "%)",
                "&a⚡ Speed: &a" + speed + " &7(" + String.format("%.1f", (double) speed / getTotalStats() * 100) + "%)",
                "&b🧠 Intelligence: &a" + intelligence + " &7(" + String.format("%.1f", (double) intelligence / getTotalStats() * 100) + "%)",
                "",
                "&7Total Stats: &a" + getTotalStats(),
                "&7Average: &a" + String.format("%.1f", getAverageStat()),
                "&7Highest: &a" + getHighestStat(),
                "&7Lowest: &a" + getLowestStat()
            };
        }
    }
}