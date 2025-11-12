package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class StormChestplate extends CustomItem {
    public StormChestplate() {
        super("STORM_CHESTPLATE", "Storm Chestplate", ItemRarity.LEGENDARY, ItemType.CHESTPLATE, Material.DIAMOND_CHESTPLATE);
        setHealth(140);
        setDefense(80);
        setStrength(20);
        setIntelligence(160);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Full Set Bonus: Mana regen");
        lore.add("§7Powerful set bonus effect.");
        return lore;
    }
}
