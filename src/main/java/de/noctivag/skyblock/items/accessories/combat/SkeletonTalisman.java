package de.noctivag.skyblock.items.accessories.combat;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class SkeletonTalisman extends BaseAccessory {
    public SkeletonTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.SKELETON);
        this.rarity = Rarity.COMMON;
        this.health = 2;
        this.critChance = 2;    }
    @Override
    protected Material getMaterial() { return Material.BONE; }
    @Override
    protected String getDisplayName() { return "Skeleton"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Skeleton stats");
    }
}
