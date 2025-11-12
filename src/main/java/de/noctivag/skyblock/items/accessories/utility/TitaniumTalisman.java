package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class TitaniumTalisman extends BaseAccessory {
    public TitaniumTalisman() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.STANDALONE);
        this.rarity = Rarity.LEGENDARY;
        this.defense = 10;
        this.intelligence = 5;    }
    @Override
    protected Material getMaterial() { return Material.DIAMOND; }
    @Override
    protected String getDisplayName() { return "Titanium"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Titanium power");
    }
}
