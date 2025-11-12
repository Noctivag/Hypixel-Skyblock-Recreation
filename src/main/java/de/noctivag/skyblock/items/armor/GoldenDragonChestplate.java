package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class GoldenDragonChestplate extends CustomItem {
    public GoldenDragonChestplate() {
        super("GOLDEN_DRAGON_CHESTPLATE", "GoldenDragon Chestplate", ItemRarity.LEGENDARY, ItemType.CHESTPLATE, Material.DIAMOND_CHESTPLATE);
        setHealth(120);
        setDefense(120);
        setStrength(32);
        setIntelligence(100);
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
