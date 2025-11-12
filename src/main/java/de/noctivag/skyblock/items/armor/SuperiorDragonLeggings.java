package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class SuperiorDragonLeggings extends CustomItem {
    public SuperiorDragonLeggings() {
        super("SUPERIOR_DRAGON_LEGGINGS", "SuperiorDragon Leggings", ItemRarity.LEGENDARY, ItemType.LEGGINGS, Material.DIAMOND_LEGGINGS);
        setHealth(135);
        setDefense(105);
        setStrength(30);
        setIntelligence(60);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Full Set Bonus: Ultimate dragon");
        lore.add("§7Powerful set bonus effect.");
        return lore;
    }
}
