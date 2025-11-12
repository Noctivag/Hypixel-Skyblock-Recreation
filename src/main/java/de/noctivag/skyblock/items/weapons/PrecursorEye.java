package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class PrecursorEye extends CustomItem {
    public PrecursorEye() {
        super("PRECURSOR_EYE", "Precursor Eye", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(275);
        setStrength(160);
        setCritChance(30);
        setIntelligence(100);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Ancient power");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
