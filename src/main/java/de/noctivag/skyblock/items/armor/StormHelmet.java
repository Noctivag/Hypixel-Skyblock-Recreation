package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class StormHelmet extends CustomItem {
    public StormHelmet() {
        super("STORM_HELMET", "Storm Helmet", ItemRarity.LEGENDARY, ItemType.HELMET, Material.DIAMOND_HELMET);
        setHealth(87);
        setDefense(50);
        setStrength(12);
        setIntelligence(100);
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
