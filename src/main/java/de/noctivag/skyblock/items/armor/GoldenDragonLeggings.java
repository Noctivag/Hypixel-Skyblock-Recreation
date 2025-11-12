package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class GoldenDragonLeggings extends CustomItem {
    public GoldenDragonLeggings() {
        super("GOLDEN_DRAGON_LEGGINGS", "GoldenDragon Leggings", ItemRarity.LEGENDARY, ItemType.LEGGINGS, Material.DIAMOND_LEGGINGS);
        setHealth(90);
        setDefense(90);
        setStrength(24);
        setIntelligence(75);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Full Set Bonus: Dragon power");
        lore.add("§7Powerful set bonus effect.");
        return lore;
    }
}
