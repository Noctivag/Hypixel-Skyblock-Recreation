package de.noctivag.skyblock.items.accessories.farming;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class WoodAffinityTalisman extends BaseAccessory {
    public WoodAffinityTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.WOOD_AFFINITY);
        this.rarity = Rarity.COMMON;
        this.speed = 2;    }
    @Override
    protected Material getMaterial() { return Material.OAK_LOG; }
    @Override
    protected String getDisplayName() { return "WoodAffinity"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Foraging boost");
    }
}
