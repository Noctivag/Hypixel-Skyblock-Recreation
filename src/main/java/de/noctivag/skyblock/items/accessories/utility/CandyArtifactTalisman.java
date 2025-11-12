package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class CandyArtifactTalisman extends BaseAccessory {
    public CandyArtifactTalisman() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.CANDY);
        this.rarity = Rarity.EPIC;
        this.speed = 6;    }
    @Override
    protected Material getMaterial() { return Material.COOKIE; }
    @Override
    protected String getDisplayName() { return "CandyArtifact"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Sweetest tooth");
    }
}
