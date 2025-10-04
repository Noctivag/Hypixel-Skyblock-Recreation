package de.noctivag.skyblock.features.weapons.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

/**
 * Legendary Weapon Type enum for legendary weapons
 */
public enum LegendaryWeaponType {
    HYPERION("Hyperion", "Legendary sword", Material.DIAMOND_SWORD),
    VALKYRIE("Valkyrie", "Legendary sword", Material.DIAMOND_SWORD),
    SCYLLA("Scylla", "Legendary sword", Material.DIAMOND_SWORD),
    ASTRAEA("Astraea", "Legendary sword", Material.DIAMOND_SWORD),
    TERMINATOR("Terminator", "Legendary bow", Material.BOW),
    JUJU_SHORTBOW("Juju Shortbow", "Legendary bow", Material.BOW),
    BONEMERANG("Bonemerang", "Legendary bow", Material.BOW),
    SOUL_WHIP("Soul Whip", "Legendary whip", Material.FISHING_ROD),
    GIANTS_SWORD("Giants Sword", "Legendary sword", Material.DIAMOND_SWORD),
    DARK_CLAYMORE("Dark Claymore", "Legendary sword", Material.DIAMOND_SWORD),
    SHADOW_FURY("Shadow Fury", "Legendary sword", Material.DIAMOND_SWORD),
    LIVID_DAGGER("Livid Dagger", "Legendary dagger", Material.DIAMOND_SWORD),
    FOT("Flower of Truth", "Legendary sword", Material.DIAMOND_SWORD),
    SPIRIT_SCEPTRE("Spirit Sceptre", "Legendary staff", Material.BLAZE_ROD),
    ICE_SPRAY_WAND("Ice Spray Wand", "Legendary wand", Material.BLAZE_ROD),
    FROZEN_SCYTHE("Frozen Scythe", "Legendary scythe", Material.DIAMOND_SWORD),
    VENOMS_TOUCH("Venoms Touch", "Legendary bow", Material.BOW),
    LAST_BREATH("Last Breath", "Legendary bow", Material.BOW),
    MACHINE_GUN_BOW("Machine Gun Bow", "Legendary bow", Material.BOW),
    ARTISANAL_SHORTBOW("Artisanal Shortbow", "Legendary bow", Material.BOW),
    MOSQUITO_BOW("Mosquito Bow", "Legendary bow", Material.BOW),
    SILENT_DEATH("Silent Death", "Legendary dagger", Material.DIAMOND_SWORD),
    VOIDEDGE_KATANA("Voidedge Katana", "Legendary katana", Material.DIAMOND_SWORD),
    VOIDWALKER_KATANA("Voidwalker Katana", "Legendary katana", Material.DIAMOND_SWORD),
    VOIDGLOOM_SERAPH("Voidgloom Seraph", "Legendary katana", Material.DIAMOND_SWORD),
    VOIDGLOOM_SERAPH_2("Voidgloom Seraph 2", "Legendary katana", Material.DIAMOND_SWORD),
    VOIDGLOOM_SERAPH_3("Voidgloom Seraph 3", "Legendary katana", Material.DIAMOND_SWORD),
    VOIDGLOOM_SERAPH_4("Voidgloom Seraph 4", "Legendary katana", Material.DIAMOND_SWORD),
    VOIDGLOOM_SERAPH_5("Voidgloom Seraph 5", "Legendary katana", Material.DIAMOND_SWORD),
    VOIDGLOOM_SERAPH_6("Voidgloom Seraph 6", "Legendary katana", Material.DIAMOND_SWORD),
    VOIDGLOOM_SERAPH_7("Voidgloom Seraph 7", "Legendary katana", Material.DIAMOND_SWORD),
    VOIDGLOOM_SERAPH_8("Voidgloom Seraph 8", "Legendary katana", Material.DIAMOND_SWORD),
    VOIDGLOOM_SERAPH_9("Voidgloom Seraph 9", "Legendary katana", Material.DIAMOND_SWORD),
    VOIDGLOOM_SERAPH_10("Voidgloom Seraph 10", "Legendary katana", Material.DIAMOND_SWORD);

    private final String name;
    private final String description;
    private final Material material;

    LegendaryWeaponType(String name, String description, Material material) {
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
}
