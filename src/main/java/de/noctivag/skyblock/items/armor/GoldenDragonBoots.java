package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class GoldenDragonBoots extends CustomItem {
    public GoldenDragonBoots() {
        super("GOLDEN_DRAGON_BOOTS", "GoldenDragon Boots", ItemRarity.LEGENDARY, ItemType.BOOTS, Material.DIAMOND_BOOTS);
        setHealth(60);
        setDefense(60);
        setStrength(16);
        setIntelligence(50);
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
