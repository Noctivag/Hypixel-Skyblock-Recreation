package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class StarfallSword extends CustomItem {
    public StarfallSword() {
        super("STARFALL", "Starfall", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(265);
        setStrength(155);
        setCritChance(25);
        setIntelligence(98);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Meteor strike");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
