package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class BonemerangSword extends CustomItem {
    public BonemerangSword() {
        super("BONEMERANG", "Bonemerang", ItemRarity.RARE, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(80);
        setStrength(50);
        setCritChance(10);
        setIntelligence(35);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Throwable bone");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
