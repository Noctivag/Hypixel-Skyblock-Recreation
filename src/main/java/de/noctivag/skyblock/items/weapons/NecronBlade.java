package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class NecronBlade extends CustomItem {
    public NecronBlade() {
        super("NECRON_BLADE", "Necron's Blade", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(260);
        setStrength(150);
        setCritChance(15);
        setIntelligence(95);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Wither Lord");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
