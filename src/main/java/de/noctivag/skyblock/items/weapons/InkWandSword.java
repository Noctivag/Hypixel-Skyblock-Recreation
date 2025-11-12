package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class InkWandSword extends CustomItem {
    public InkWandSword() {
        super("INK_WAND", "Ink Wand", ItemRarity.EPIC, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(135);
        setStrength(85);
        setCritChance(15);
        setIntelligence(55);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Squid magic");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
