package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class SuperiorDragonHelmet extends CustomItem {
    public SuperiorDragonHelmet() {
        super("SUPERIOR_DRAGON_HELMET", "SuperiorDragon Helmet", ItemRarity.LEGENDARY, ItemType.HELMET, Material.DIAMOND_HELMET);
        setHealth(112);
        setDefense(87);
        setStrength(25);
        setIntelligence(50);
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
