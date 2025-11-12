package de.noctivag.skyblock.items.accessories.mining;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class MineAffinityArtifactTalisman extends BaseAccessory {
    public MineAffinityArtifactTalisman() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.MINE_AFFINITY);
        this.rarity = Rarity.RARE;
        this.defense = 6;    }
    @Override
    protected Material getMaterial() { return Material.COAL; }
    @Override
    protected String getDisplayName() { return "MineAffinityArtifact"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Superior mining");
    }
}
