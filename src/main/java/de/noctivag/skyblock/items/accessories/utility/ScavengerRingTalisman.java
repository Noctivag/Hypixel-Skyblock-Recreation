package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class ScavengerRingTalisman extends BaseAccessory {
    public ScavengerRingTalisman() {
        super(AccessoryTier.RING, AccessoryFamily.SCAVENGER);
        this.rarity = Rarity.EPIC;
        this.health = 4;    }
    @Override
    protected Material getMaterial() { return Material.GOLD_NUGGET; }
    @Override
    protected String getDisplayName() { return "ScavengerRing"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Lots more coins");
    }
}
