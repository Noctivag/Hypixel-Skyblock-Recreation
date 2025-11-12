package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class GraveSword extends CustomItem {
    public GraveSword() {
        super("GRAVE_SWORD", "Grave Sword", ItemRarity.EPIC, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(145);
        setStrength(90);
        setCritChance(10);
        setIntelligence(60);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Undead power");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
