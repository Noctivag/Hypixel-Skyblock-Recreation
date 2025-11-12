package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class OminousBlade extends CustomItem {
    public OminousBlade() {
        super("OMINOUS_BLADE", "Ominous Blade", ItemRarity.RARE, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(110);
        setStrength(65);
        setCritChance(10);
        setIntelligence(40);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Shadow damage");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
