package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class EmberRod extends CustomItem {
    public EmberRod() {
        super("EMBER_ROD", "Ember Rod", ItemRarity.EPIC, ItemType.FISHING_ROD, Material.DIAMOND_SWORD);
        setDamage(120);
        setStrength(60);
        setCritChance(0);
        setIntelligence(50);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Fire fishing rod");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
