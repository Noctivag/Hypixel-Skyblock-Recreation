package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class SorrowLeggings extends CustomItem {
    public SorrowLeggings() {
        super("SORROW_LEGGINGS", "Sorrow Leggings", ItemRarity.LEGENDARY, ItemType.LEGGINGS, Material.DIAMOND_LEGGINGS);
        setHealth(96);
        setDefense(96);
        setStrength(21);
        setIntelligence(54);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Full Set Bonus: Sadness aura");
        lore.add("§7Powerful set bonus effect.");
        return lore;
    }
}
