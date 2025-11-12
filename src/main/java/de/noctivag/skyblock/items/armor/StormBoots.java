package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class StormBoots extends CustomItem {
    public StormBoots() {
        super("STORM_BOOTS", "Storm Boots", ItemRarity.LEGENDARY, ItemType.BOOTS, Material.DIAMOND_BOOTS);
        setHealth(70);
        setDefense(40);
        setStrength(10);
        setIntelligence(80);
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
