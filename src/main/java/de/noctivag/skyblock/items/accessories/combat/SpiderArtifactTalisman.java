package de.noctivag.skyblock.items.accessories.combat;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class SpiderArtifactTalisman extends BaseAccessory {
    public SpiderArtifactTalisman() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.SPIDER);
        this.rarity = Rarity.RARE;
        this.strength = 6;
        this.critDamage = 5;    }
    @Override
    protected Material getMaterial() { return Material.SPIDER_EYE; }
    @Override
    protected String getDisplayName() { return "SpiderArtifact"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Superior spider damage");
    }
}
