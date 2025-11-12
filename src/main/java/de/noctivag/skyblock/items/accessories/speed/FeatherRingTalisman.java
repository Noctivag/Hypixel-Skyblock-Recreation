package de.noctivag.skyblock.items.accessories.speed;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class FeatherRingTalisman extends BaseAccessory {
    public FeatherRingTalisman() {
        super(AccessoryTier.RING, AccessoryFamily.FEATHER);
        this.rarity = Rarity.UNCOMMON;
        this.speed = 5;    }
    @Override
    protected Material getMaterial() { return Material.FEATHER; }
    @Override
    protected String getDisplayName() { return "FeatherRing"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Enhanced speed");
    }
}
