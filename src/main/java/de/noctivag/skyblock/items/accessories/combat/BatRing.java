package de.noctivag.skyblock.items.accessories.combat;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class BatRing extends BaseAccessory {
    public BatRing() {
        super(AccessoryTier.RING, AccessoryFamily.BAT);
        this.rarity = Rarity.EPIC;
        this.health = 3;
        this.speed = 2;
        this.intelligence = 2;
    }
    @Override
    protected Material getMaterial() { return Material.BONE; }
    @Override
    protected String getDisplayName() { return "Bat Ring"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§6Ability: Enhanced Night Vision", "§7Grants improved stats during night.");
    }
}
