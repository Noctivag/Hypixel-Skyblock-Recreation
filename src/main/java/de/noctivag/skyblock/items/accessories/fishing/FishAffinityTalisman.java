package de.noctivag.skyblock.items.accessories.fishing;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class FishAffinityTalisman extends BaseAccessory {
    public FishAffinityTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.FISHING);
        this.rarity = Rarity.COMMON;
        this.intelligence = 2;    }
    @Override
    protected Material getMaterial() { return Material.TROPICAL_FISH; }
    @Override
    protected String getDisplayName() { return "FishAffinity"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Fishing boost");
    }
}
