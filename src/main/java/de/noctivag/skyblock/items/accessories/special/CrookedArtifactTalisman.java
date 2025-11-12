package de.noctivag.skyblock.items.accessories.special;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class CrookedArtifactTalisman extends BaseAccessory {
    public CrookedArtifactTalisman() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.CROOKED_ARTIFACT);
        this.rarity = Rarity.RARE;
        this.health = 15;    }
    @Override
    protected Material getMaterial() { return Material.STICK; }
    @Override
    protected String getDisplayName() { return "CrookedArtifact"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Crooked stats");
    }
}
