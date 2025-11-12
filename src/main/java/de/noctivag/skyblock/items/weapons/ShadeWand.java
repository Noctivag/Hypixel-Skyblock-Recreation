package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class ShadeWand extends CustomItem {
    public ShadeWand() {
        super("SHADE_WAND", "Shade Wand", ItemRarity.EPIC, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(140);
        setStrength(88);
        setCritChance(12);
        setIntelligence(58);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Shadow magic");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
