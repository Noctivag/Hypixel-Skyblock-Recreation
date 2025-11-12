package de.noctivag.skyblock.items.accessories.speed;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class SpeedArtifact extends BaseAccessory {
    public SpeedArtifact() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.SPEED);
        this.rarity = Rarity.RARE;
        this.speed = 3;
    }
    @Override
    protected Material getMaterial() { return Material.SUGAR; }
    @Override
    protected String getDisplayName() { return "Speed Artifact"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Grants a large", "§7amount of §f✦ Speed§7.");
    }
}
