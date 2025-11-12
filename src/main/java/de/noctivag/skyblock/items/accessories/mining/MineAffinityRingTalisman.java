package de.noctivag.skyblock.items.accessories.mining;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class MineAffinityRingTalisman extends BaseAccessory {
    public MineAffinityRingTalisman() {
        super(AccessoryTier.RING, AccessoryFamily.MINE_AFFINITY);
        this.rarity = Rarity.UNCOMMON;
        this.defense = 4;    }
    @Override
    protected Material getMaterial() { return Material.COAL; }
    @Override
    protected String getDisplayName() { return "MineAffinityRing"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Enhanced mining");
    }
}
