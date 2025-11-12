package de.noctivag.skyblock.items.accessories.mining;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class MinersPrize extends BaseAccessory {
    public MinersPrize() {
        super(AccessoryTier.RING, AccessoryFamily.STANDALONE);
        this.rarity = Rarity.RARE;
        this.defense = 5;
    }
    @Override
    protected Material getMaterial() { return Material.GOLD_NUGGET; }
    @Override
    protected String getDisplayName() { return "Miner's Prize"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§6Ability: Mining Fortune", "§7Grants §a+20 ☘ Mining Fortune§7.");
    }
}
