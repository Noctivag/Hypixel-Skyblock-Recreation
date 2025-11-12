package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class LastBreathSword extends CustomItem {
    public LastBreathSword() {
        super("LAST_BREATH", "Last Breath", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(260);
        setStrength(155);
        setCritChance(25);
        setIntelligence(95);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Final strike");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
