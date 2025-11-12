package de.noctivag.skyblock.items.accessories.farming;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class FarmingTalisman extends BaseAccessory {
    public FarmingTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.FARMING);
        this.rarity = Rarity.COMMON;
        this.speed = 10;
    }
    @Override
    protected Material getMaterial() { return Material.WHEAT; }
    @Override
    protected String getDisplayName() { return "Farming Talisman"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§6Ability: Green Thumb", "§7Grants §a+10 ✦ Speed §7on farming islands.");
    }
}
