package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class MiningExpTalisman extends BaseAccessory {
    public MiningExpTalisman() {
        super(AccessoryTier.RING, AccessoryFamily.STANDALONE);
        this.rarity = Rarity.RARE;
        this.intelligence = 3;    }
    @Override
    protected Material getMaterial() { return Material.GOLD_INGOT; }
    @Override
    protected String getDisplayName() { return "MiningExp"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Mining XP boost");
    }
}
