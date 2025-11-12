package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class PotionArtifactTalisman extends BaseAccessory {
    public PotionArtifactTalisman() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.POTION_AFFINITY);
        this.rarity = Rarity.RARE;
        this.intelligence = 7;    }
    @Override
    protected Material getMaterial() { return Material.BREWING_STAND; }
    @Override
    protected String getDisplayName() { return "PotionArtifact"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Superior potions");
    }
}
