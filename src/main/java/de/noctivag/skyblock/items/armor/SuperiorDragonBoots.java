package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class SuperiorDragonBoots extends CustomItem {
    public SuperiorDragonBoots() {
        super("SUPERIOR_DRAGON_BOOTS", "SuperiorDragon Boots", ItemRarity.LEGENDARY, ItemType.BOOTS, Material.DIAMOND_BOOTS);
        setHealth(90);
        setDefense(70);
        setStrength(20);
        setIntelligence(40);
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
