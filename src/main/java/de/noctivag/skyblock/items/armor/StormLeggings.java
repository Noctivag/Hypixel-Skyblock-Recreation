package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class StormLeggings extends CustomItem {
    public StormLeggings() {
        super("STORM_LEGGINGS", "Storm Leggings", ItemRarity.LEGENDARY, ItemType.LEGGINGS, Material.DIAMOND_LEGGINGS);
        setHealth(105);
        setDefense(60);
        setStrength(15);
        setIntelligence(120);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Full Set Bonus: Mana regen");
        lore.add("§7Powerful set bonus effect.");
        return lore;
    }
}
