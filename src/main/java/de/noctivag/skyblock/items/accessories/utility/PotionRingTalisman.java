package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class PotionRingTalisman extends BaseAccessory {
    public PotionRingTalisman() {
        super(AccessoryTier.RING, AccessoryFamily.POTION_AFFINITY);
        this.rarity = Rarity.UNCOMMON;
        this.intelligence = 5;    }
    @Override
    protected Material getMaterial() { return Material.BREWING_STAND; }
    @Override
    protected String getDisplayName() { return "PotionRing"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Enhanced potions");
    }
}
