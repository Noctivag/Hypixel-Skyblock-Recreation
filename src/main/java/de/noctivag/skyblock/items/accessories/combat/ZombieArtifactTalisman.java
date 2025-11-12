package de.noctivag.skyblock.items.accessories.combat;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class ZombieArtifactTalisman extends BaseAccessory {
    public ZombieArtifactTalisman() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.ZOMBIE);
        this.rarity = Rarity.RARE;
        this.health = 12;
        this.defense = 4;    }
    @Override
    protected Material getMaterial() { return Material.ROTTEN_FLESH; }
    @Override
    protected String getDisplayName() { return "ZombieArtifact"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Superior zombie protection");
    }
}
