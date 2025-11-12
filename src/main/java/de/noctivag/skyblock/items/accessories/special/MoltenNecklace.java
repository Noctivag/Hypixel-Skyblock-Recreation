package de.noctivag.skyblock.items.accessories.special;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class MoltenNecklace extends BaseAccessory {
    public MoltenNecklace() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.STANDALONE);
        this.rarity = Rarity.LEGENDARY;
        this.strength = 15;
        this.critDamage = 10;
    }
    @Override
    protected Material getMaterial() { return Material.MAGMA_CREAM; }
    @Override
    protected String getDisplayName() { return "Molten Necklace"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§6Ability: Molten", "§7Grants immunity to fire damage.");
    }
}
