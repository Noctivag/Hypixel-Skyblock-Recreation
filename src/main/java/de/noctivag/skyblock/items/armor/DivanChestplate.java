package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class DivanChestplate extends CustomItem {
    public DivanChestplate() {
        super("DIVAN_CHESTPLATE", "Divan Chestplate", ItemRarity.LEGENDARY, ItemType.CHESTPLATE, Material.DIAMOND_CHESTPLATE);
        setHealth(120);
        setDefense(160);
        setStrength(20);
        setIntelligence(80);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Full Set Bonus: Mining power");
        lore.add("§7Powerful set bonus effect.");
        return lore;
    }
}
