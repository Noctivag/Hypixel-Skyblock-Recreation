package de.noctivag.skyblock.items.accessories.fishing;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class SeaCreatureTalisman extends BaseAccessory {
    public SeaCreatureTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.SEA_CREATURE);
        this.rarity = Rarity.COMMON;
        this.speed = 1;    }
    @Override
    protected Material getMaterial() { return Material.PRISMARINE_SHARD; }
    @Override
    protected String getDisplayName() { return "SeaCreature"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Sea creature boost");
    }
}
