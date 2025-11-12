package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class DivanLeggings extends CustomItem {
    public DivanLeggings() {
        super("DIVAN_LEGGINGS", "Divan Leggings", ItemRarity.LEGENDARY, ItemType.LEGGINGS, Material.DIAMOND_LEGGINGS);
        setHealth(90);
        setDefense(120);
        setStrength(15);
        setIntelligence(60);
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
