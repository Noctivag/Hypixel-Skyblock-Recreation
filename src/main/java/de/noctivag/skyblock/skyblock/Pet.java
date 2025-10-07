package de.noctivag.skyblock.skyblock;

import java.util.UUID;

/**
 * Pet - Represents a pet in the Skyblock system
 */
public class Pet {

    private final UUID uuid;
    private final String type;
    private final String name;
    private final int level;
    private final double experience;

    public Pet(String type, String name, int level, double experience) {
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.name = name;
        this.level = level;
        this.experience = experience;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public double getExperience() {
        return experience;
    }

    public void addExperience(double exp) {
        // TODO: Implement pet leveling logic
    }

    public String getDisplayName() {
        return "§f" + name + " §7(" + type + ")";
    }
}
