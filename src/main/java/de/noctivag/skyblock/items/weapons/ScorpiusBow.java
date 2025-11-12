package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class ScorpiusBow extends CustomItem {
    public ScorpiusBow() {
        super("SCORPIUS_BOW", "Scorpius Bow", ItemRarity.LEGENDARY, ItemType.BOW, Material.DIAMOND_SWORD);
        setDamage(170);
        setStrength(100);
        setCritChance(20);
        setIntelligence(75);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Poison arrows");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
