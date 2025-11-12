package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class LeapingSword extends CustomItem {
    public LeapingSword() {
        super("LEAPING_SWORD", "Leaping Sword", ItemRarity.RARE, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(100);
        setStrength(60);
        setCritChance(0);
        setIntelligence(35);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Leap attack");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
