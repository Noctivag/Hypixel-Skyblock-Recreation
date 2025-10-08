package de.noctivag.skyblock.features.weapons.types;

/**
 * Repräsentiert eine aktive oder passive Waffenfähigkeit (Ability) aus Hypixel Skyblock
 */
public class WeaponAbility {
    private final String name;
    private final String description;
    private final int manaCost;
    private final int cooldownTicks;
    private final boolean isPassive;

    public WeaponAbility(String name, String description, int manaCost, int cooldownTicks, boolean isPassive) {
        this.name = name;
        this.description = description;
        this.manaCost = manaCost;
        this.cooldownTicks = cooldownTicks;
        this.isPassive = isPassive;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getManaCost() { return manaCost; }
    public int getCooldownTicks() { return cooldownTicks; }
    public boolean isPassive() { return isPassive; }
}
