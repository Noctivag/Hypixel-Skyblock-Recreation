package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class DivanBoots extends CustomItem {
    public DivanBoots() {
        super("DIVAN_BOOTS", "Divan Boots", ItemRarity.LEGENDARY, ItemType.BOOTS, Material.DIAMOND_BOOTS);
        setHealth(60);
        setDefense(80);
        setStrength(10);
        setIntelligence(40);
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
