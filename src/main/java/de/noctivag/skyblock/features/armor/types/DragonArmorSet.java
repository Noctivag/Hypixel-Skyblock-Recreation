package de.noctivag.skyblock.features.armor.types;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.armor.sets.ArmorSetManager.ArmorSet;
import de.noctivag.skyblock.features.armor.sets.ArmorSetManager.SetBonus;
import org.bukkit.Material;
import java.util.List;
import java.util.Map;

/**
 * Dragon Armor Set
 */
public class DragonArmorSet extends ArmorSet {
    
    public enum DragonType {
        PROTECTOR, OLD, WISE, UNSTABLE, STRONG, YOUNG, SUPERIOR, HOLY
    }
    
    private final DragonType dragonType;
    private final Material helmetMaterial;
    private final Material chestplateMaterial;
    private final Material leggingsMaterial;
    private final Material bootsMaterial;
    
    public DragonArmorSet(String id, String name, String description, List<String> pieces, 
                         Map<Integer, SetBonus> bonuses, DragonType dragonType,
                         Material helmetMaterial, Material chestplateMaterial, 
                         Material leggingsMaterial, Material bootsMaterial) {
        super(id, name, description, pieces, bonuses);
        this.dragonType = dragonType;
        this.helmetMaterial = helmetMaterial;
        this.chestplateMaterial = chestplateMaterial;
        this.leggingsMaterial = leggingsMaterial;
        this.bootsMaterial = bootsMaterial;
    }
    
    public DragonType getDragonType() { return dragonType; }
    public Material getHelmetMaterial() { return helmetMaterial; }
    public Material getChestplateMaterial() { return chestplateMaterial; }
    public Material getLeggingsMaterial() { return leggingsMaterial; }
    public Material getBootsMaterial() { return bootsMaterial; }
    
    public static List<DragonArmorSet> getAllDragonSets() {
        // Return all dragon armor sets
        return List.of();
    }
    
    public static DragonArmorSet getDragonSet(DragonType type) {
        // Return specific dragon armor set
        return null;
    }
}
