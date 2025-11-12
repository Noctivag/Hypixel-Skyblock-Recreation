package de.noctivag.skyblock.items.accessories.fishing;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class BaitRingTalisman extends BaseAccessory {
    public BaitRingTalisman() {
        super(AccessoryTier.RING, AccessoryFamily.BAIT);
        this.rarity = Rarity.RARE;
        this.intelligence = 5;    }
    @Override
    protected Material getMaterial() { return Material.COD; }
    @Override
    protected String getDisplayName() { return "BaitRing"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Better bait chance");
    }
}
