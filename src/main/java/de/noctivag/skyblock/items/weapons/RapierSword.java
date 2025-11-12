package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class RapierSword extends CustomItem {
    public RapierSword() {
        super("RAPIER", "Rapier", ItemRarity.RARE, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(85);
        setStrength(45);
        setCritChance(20);
        setIntelligence(30);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Fast strikes");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
