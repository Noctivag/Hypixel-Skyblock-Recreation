package de.noctivag.skyblock.weapons;

/**
 * Weapon ability class
 */
public class WeaponAbility {
    
    private final String id;
    private final String name;
    private final String description;
    private final WeaponType weaponType;
    private final AbilityType abilityType;
    private final int manaCost;
    private final int cooldown;
    private final double range;
    private final String effectDescription;
    
    public WeaponAbility(String id, String name, String description, 
                        WeaponType weaponType, AbilityType abilityType, 
                        int manaCost, int cooldown, double range, 
                        String effectDescription) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.weaponType = weaponType;
        this.abilityType = abilityType;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
        this.range = range;
        this.effectDescription = effectDescription;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public WeaponType getWeaponType() {
        return weaponType;
    }
    
    public AbilityType getAbilityType() {
        return abilityType;
    }
    
    public int getManaCost() {
        return manaCost;
    }
    
    public int getCooldown() {
        return cooldown;
    }
    
    public double getRange() {
        return range;
    }
    
    public String getEffectDescription() {
        return effectDescription;
    }
}
