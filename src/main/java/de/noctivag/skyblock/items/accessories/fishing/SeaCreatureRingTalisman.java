package de.noctivag.skyblock.items.accessories.fishing;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class SeaCreatureRingTalisman extends BaseAccessory {
    public SeaCreatureRingTalisman() {
        super(AccessoryTier.RING, AccessoryFamily.SEA_CREATURE);
        this.rarity = Rarity.UNCOMMON;
        this.speed = 2;
        this.health = 4;    }
    @Override
    protected Material getMaterial() { return Material.PRISMARINE_SHARD; }
    @Override
    protected String getDisplayName() { return "SeaCreatureRing"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Enhanced fishing");
    }
}
