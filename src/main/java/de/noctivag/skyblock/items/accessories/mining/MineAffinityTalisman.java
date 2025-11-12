package de.noctivag.skyblock.items.accessories.mining;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class MineAffinityTalisman extends BaseAccessory {
    public MineAffinityTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.MINE_AFFINITY);
        this.rarity = Rarity.COMMON;
        this.defense = 2;    }
    @Override
    protected Material getMaterial() { return Material.COAL; }
    @Override
    protected String getDisplayName() { return "MineAffinity"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Mining boost");
    }
}
