package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class NecronLeggings extends CustomItem {
    public NecronLeggings() {
        super("NECRON_LEGGINGS", "Necron Leggings", ItemRarity.LEGENDARY, ItemType.LEGGINGS, Material.DIAMOND_LEGGINGS);
        setHealth(120);
        setDefense(90);
        setStrength(30);
        setIntelligence(45);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Full Set Bonus: Catacombs power");
        lore.add("§7Powerful set bonus effect.");
        return lore;
    }
}
