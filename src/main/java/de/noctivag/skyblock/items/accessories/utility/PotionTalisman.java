package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class PotionTalisman extends BaseAccessory {
    public PotionTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.POTION_AFFINITY);
        this.rarity = Rarity.COMMON;
        this.intelligence = 3;    }
    @Override
    protected Material getMaterial() { return Material.BREWING_STAND; }
    @Override
    protected String getDisplayName() { return "Potion"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Potion boost");
    }
}
