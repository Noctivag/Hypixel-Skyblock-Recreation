package de.noctivag.skyblock.items.accessories.fishing;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class SeaCreatureArtifactTalisman extends BaseAccessory {
    public SeaCreatureArtifactTalisman() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.SEA_CREATURE);
        this.rarity = Rarity.RARE;
        this.speed = 3;
        this.health = 8;    }
    @Override
    protected Material getMaterial() { return Material.PRISMARINE_SHARD; }
    @Override
    protected String getDisplayName() { return "SeaCreatureArtifact"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Superior fishing");
    }
}
