package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class ClaymoreSword extends CustomItem {
    public ClaymoreSword() {
        super("CLAYMORE", "Claymore", ItemRarity.EPIC, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(160);
        setStrength(100);
        setCritChance(5);
        setIntelligence(65);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Heavy blade");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
