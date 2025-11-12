package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class HunterKnife extends CustomItem {
    public HunterKnife() {
        super("HUNTER_KNIFE", "Hunter Knife", ItemRarity.RARE, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(90);
        setStrength(50);
        setCritChance(15);
        setIntelligence(30);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Fast attack speed");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
