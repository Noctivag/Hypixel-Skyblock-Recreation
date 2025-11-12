package de.noctivag.skyblock.items.accessories.combat;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class TarantulaTalisman extends BaseAccessory {
    public TarantulaTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.TARANTULA);
        this.rarity = Rarity.EPIC;
        this.strength = 5;
        this.critDamage = 5;
        this.enrichable = true;
    }
    @Override
    protected Material getMaterial() { return Material.SPIDER_EYE; }
    @Override
    protected String getDisplayName() { return "Tarantula Talisman"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§6Ability: Eight Legs", "§7Grants §c+5 ❁ Strength §7and", "§7§9+5% ☠ Crit Damage §7against spiders.");
    }
}
