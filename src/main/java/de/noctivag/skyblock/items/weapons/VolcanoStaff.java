package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class VolcanoStaff extends CustomItem {
    public VolcanoStaff() {
        super("MAGMA_STAFF", "Magma Staff", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(235);
        setStrength(138);
        setCritChance(14);
        setIntelligence(82);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Lava burst");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
