package de.noctivag.plugin.features.weapons.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

/**
 * Weapon Type enum for weapon types
 */
public enum WeaponType {
    SWORD("Sword", "Melee weapon", Material.DIAMOND_SWORD),
    BOW("Bow", "Ranged weapon", Material.BOW),
    CROSSBOW("Crossbow", "Ranged weapon", Material.CROSSBOW),
    TRIDENT("Trident", "Thrown weapon", Material.TRIDENT),
    AXE("Axe", "Melee weapon", Material.DIAMOND_AXE),
    PICKAXE("Pickaxe", "Mining weapon", Material.DIAMOND_PICKAXE),
    SHOVEL("Shovel", "Digging weapon", Material.DIAMOND_SHOVEL),
    HOE("Hoe", "Farming weapon", Material.DIAMOND_HOE),
    FISHING_ROD("Fishing Rod", "Fishing weapon", Material.FISHING_ROD),
    SHEARS("Shears", "Cutting weapon", Material.SHEARS),
    FLINT_AND_STEEL("Flint and Steel", "Ignition weapon", Material.FLINT_AND_STEEL),
    BUCKET("Bucket", "Liquid weapon", Material.BUCKET),
    COMPASS("Compass", "Navigation weapon", Material.COMPASS),
    CLOCK("Clock", "Time weapon", Material.CLOCK),
    MAP("Map", "Mapping weapon", Material.MAP),
    LEAD("Lead", "Animal control weapon", Material.LEAD),
    NAME_TAG("Name Tag", "Naming weapon", Material.NAME_TAG),
    SADDLE("Saddle", "Riding weapon", Material.SADDLE);

    private final String name;
    private final String description;
    private final Material material;

    WeaponType(String name, String description, Material material) {
        this.name = name;
        this.description = description;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Material getMaterial() {
        return material;
    }

    public double getBaseDamage() {
        switch (this) {
            case SWORD: return 7.0;
            case BOW: return 6.0;
            case CROSSBOW: return 6.0;
            case TRIDENT: return 9.0;
            case AXE: return 9.0;
            case PICKAXE: return 2.0;
            case SHOVEL: return 2.5;
            case HOE: return 1.0;
            case FISHING_ROD: return 0.0;
            case SHEARS: return 0.0;
            case FLINT_AND_STEEL: return 1.0;
            case BUCKET: return 0.0;
            case COMPASS: return 0.0;
            case CLOCK: return 0.0;
            case MAP: return 0.0;
            case LEAD: return 0.0;
            case NAME_TAG: return 0.0;
            case SADDLE: return 0.0;
            default: return 1.0;
        }
    }

    public double getBaseCritChance() {
        switch (this) {
            case SWORD: return 0.3;
            case BOW: return 0.25;
            case CROSSBOW: return 0.2;
            case TRIDENT: return 0.35;
            case AXE: return 0.2;
            case PICKAXE: return 0.1;
            case SHOVEL: return 0.1;
            case HOE: return 0.05;
            case FISHING_ROD: return 0.0;
            case SHEARS: return 0.0;
            case FLINT_AND_STEEL: return 0.0;
            case BUCKET: return 0.0;
            case COMPASS: return 0.0;
            case CLOCK: return 0.0;
            case MAP: return 0.0;
            case LEAD: return 0.0;
            case NAME_TAG: return 0.0;
            case SADDLE: return 0.0;
            default: return 0.1;
        }
    }

    public double getBaseCritDamage() {
        switch (this) {
            case SWORD: return 0.5;
            case BOW: return 0.4;
            case CROSSBOW: return 0.35;
            case TRIDENT: return 0.6;
            case AXE: return 0.3;
            case PICKAXE: return 0.2;
            case SHOVEL: return 0.2;
            case HOE: return 0.1;
            case FISHING_ROD: return 0.0;
            case SHEARS: return 0.0;
            case FLINT_AND_STEEL: return 0.0;
            case BUCKET: return 0.0;
            case COMPASS: return 0.0;
            case CLOCK: return 0.0;
            case MAP: return 0.0;
            case LEAD: return 0.0;
            case NAME_TAG: return 0.0;
            case SADDLE: return 0.0;
            default: return 0.2;
        }
    }

    public double getBaseAttackSpeed() {
        switch (this) {
            case SWORD: return 1.6;
            case BOW: return 1.0;
            case CROSSBOW: return 1.25;
            case TRIDENT: return 1.1;
            case AXE: return 1.0;
            case PICKAXE: return 1.2;
            case SHOVEL: return 1.0;
            case HOE: return 4.0;
            case FISHING_ROD: return 1.0;
            case SHEARS: return 1.0;
            case FLINT_AND_STEEL: return 1.0;
            case BUCKET: return 1.0;
            case COMPASS: return 1.0;
            case CLOCK: return 1.0;
            case MAP: return 1.0;
            case LEAD: return 1.0;
            case NAME_TAG: return 1.0;
            case SADDLE: return 1.0;
            default: return 1.0;
        }
    }

    public double getBaseStrength() {
        switch (this) {
            case SWORD: return 0.0;
            case BOW: return 0.0;
            case CROSSBOW: return 0.0;
            case TRIDENT: return 0.0;
            case AXE: return 0.0;
            case PICKAXE: return 0.0;
            case SHOVEL: return 0.0;
            case HOE: return 0.0;
            case FISHING_ROD: return 0.0;
            case SHEARS: return 0.0;
            case FLINT_AND_STEEL: return 0.0;
            case BUCKET: return 0.0;
            case COMPASS: return 0.0;
            case CLOCK: return 0.0;
            case MAP: return 0.0;
            case LEAD: return 0.0;
            case NAME_TAG: return 0.0;
            case SADDLE: return 0.0;
            default: return 0.0;
        }
    }

    public double getBaseFerocity() {
        switch (this) {
            case SWORD: return 0.0;
            case BOW: return 0.0;
            case CROSSBOW: return 0.0;
            case TRIDENT: return 0.0;
            case AXE: return 0.0;
            case PICKAXE: return 0.0;
            case SHOVEL: return 0.0;
            case HOE: return 0.0;
            case FISHING_ROD: return 0.0;
            case SHEARS: return 0.0;
            case FLINT_AND_STEEL: return 0.0;
            case BUCKET: return 0.0;
            case COMPASS: return 0.0;
            case CLOCK: return 0.0;
            case MAP: return 0.0;
            case LEAD: return 0.0;
            case NAME_TAG: return 0.0;
            case SADDLE: return 0.0;
            default: return 0.0;
        }
    }
}
