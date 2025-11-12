package de.noctivag.skyblock.items.accessories.special;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class TreasureRingTalisman extends BaseAccessory {
    public TreasureRingTalisman() {
        super(AccessoryTier.RING, AccessoryFamily.TREASURE);
        this.rarity = Rarity.LEGENDARY;
        this.intelligence = 15;    }
    @Override
    protected Material getMaterial() { return Material.GOLD_BLOCK; }
    @Override
    protected String getDisplayName() { return "TreasureRing"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Enhanced treasure");
    }
}
