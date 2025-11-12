package de.noctivag.skyblock.items.accessories.farming;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class WoodAffinityRingTalisman extends BaseAccessory {
    public WoodAffinityRingTalisman() {
        super(AccessoryTier.RING, AccessoryFamily.WOOD_AFFINITY);
        this.rarity = Rarity.UNCOMMON;
        this.speed = 4;    }
    @Override
    protected Material getMaterial() { return Material.OAK_LOG; }
    @Override
    protected String getDisplayName() { return "WoodAffinityRing"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Enhanced foraging");
    }
}
