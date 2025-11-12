package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class SuperiorDragonChestplate extends CustomItem {
    public SuperiorDragonChestplate() {
        super("SUPERIOR_DRAGON_CHESTPLATE", "SuperiorDragon Chestplate", ItemRarity.LEGENDARY, ItemType.CHESTPLATE, Material.DIAMOND_CHESTPLATE);
        setHealth(180);
        setDefense(140);
        setStrength(40);
        setIntelligence(80);
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
