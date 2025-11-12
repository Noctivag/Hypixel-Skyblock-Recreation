package de.noctivag.skyblock.items.accessories.special;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class TreasureTalisman extends BaseAccessory {
    public TreasureTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.TREASURE);
        this.rarity = Rarity.EPIC;
        this.intelligence = 10;    }
    @Override
    protected Material getMaterial() { return Material.GOLD_BLOCK; }
    @Override
    protected String getDisplayName() { return "Treasure"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Treasure hunter");
    }
}
