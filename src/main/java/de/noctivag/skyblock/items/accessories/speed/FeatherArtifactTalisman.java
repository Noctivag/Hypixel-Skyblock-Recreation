package de.noctivag.skyblock.items.accessories.speed;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class FeatherArtifactTalisman extends BaseAccessory {
    public FeatherArtifactTalisman() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.FEATHER);
        this.rarity = Rarity.RARE;
        this.speed = 7;    }
    @Override
    protected Material getMaterial() { return Material.FEATHER; }
    @Override
    protected String getDisplayName() { return "FeatherArtifact"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Superior speed");
    }
}
