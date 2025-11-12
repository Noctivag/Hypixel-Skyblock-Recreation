package de.noctivag.skyblock.items.accessories.combat;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class SvenPackmaster extends BaseAccessory {
    public SvenPackmaster() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.SVEN);
        this.rarity = Rarity.LEGENDARY;
        this.strength = 12;
        this.health = 12;
        this.critDamage = 8;
        this.enrichable = true;
    }
    @Override
    protected Material getMaterial() { return Material.BONE; }
    @Override
    protected String getDisplayName() { return "Sven Packmaster"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§6Ability: Pack Leader", "§7Grants increased damage against wolves.");
    }
}
