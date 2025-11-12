package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class ScavengerTalisman extends BaseAccessory {
    public ScavengerTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.SCAVENGER);
        this.rarity = Rarity.RARE;
        this.health = 2;    }
    @Override
    protected Material getMaterial() { return Material.GOLD_NUGGET; }
    @Override
    protected String getDisplayName() { return "Scavenger"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7More coins");
    }
}
