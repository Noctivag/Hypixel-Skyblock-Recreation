package de.noctivag.skyblock.items.accessories.mining;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class MithrilGourmand extends BaseAccessory {
    public MithrilGourmand() {
        super(AccessoryTier.RING, AccessoryFamily.STANDALONE);
        this.rarity = Rarity.RARE;
        this.defense = 8;
        this.intelligence = 5;
    }
    @Override
    protected Material getMaterial() { return Material.PRISMARINE_CRYSTALS; }
    @Override
    protected String getDisplayName() { return "Mithril Gourmand"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§6Ability: Mithril Lover", "§7Grants §a+50 ☘ Mining Fortune§7", "§7when mining Mithril.");
    }
}
