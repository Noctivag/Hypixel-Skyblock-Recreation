package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class SilverFang extends CustomItem {
    public SilverFang() {
        super("SILVER_FANG", "Silver Fang", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(190);
        setStrength(120);
        setCritChance(25);
        setIntelligence(75);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Wolf slayer");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
