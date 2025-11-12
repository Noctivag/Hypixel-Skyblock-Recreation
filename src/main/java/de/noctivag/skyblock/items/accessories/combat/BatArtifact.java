package de.noctivag.skyblock.items.accessories.combat;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class BatArtifact extends BaseAccessory {
    public BatArtifact() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.BAT);
        this.rarity = Rarity.EPIC;
        this.health = 5;
        this.speed = 3;
        this.intelligence = 3;
    }
    @Override
    protected Material getMaterial() { return Material.BONE; }
    @Override
    protected String getDisplayName() { return "Bat Artifact"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§6Ability: Superior Night Vision", "§7Grants significant stats during night.");
    }
}
