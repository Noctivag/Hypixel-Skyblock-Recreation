package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class WitherBlade extends CustomItem {
    public WitherBlade() {
        super("WITHER_BLADE", "Wither Blade", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(240);
        setStrength(140);
        setCritChance(16);
        setIntelligence(85);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Wither effect");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
