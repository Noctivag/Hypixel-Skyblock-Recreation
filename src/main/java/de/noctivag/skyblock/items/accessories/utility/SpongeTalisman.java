package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class SpongeTalisman extends BaseAccessory {
    public SpongeTalisman() {
        super(AccessoryTier.EPIC, AccessoryFamily.STANDALONE);
        this.rarity = Rarity.EPIC;
        this.health = 10;
        this.defense = 5;    }
    @Override
    protected Material getMaterial() { return Material.SPONGE; }
    @Override
    protected String getDisplayName() { return "Sponge"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Water walking");
    }
}
