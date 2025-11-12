package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class EdibleMace extends CustomItem {
    public EdibleMace() {
        super("EDIBLE_MACE", "Edible Mace", ItemRarity.RARE, ItemType.SWORD, Material.STICK);
        setDamage(50);
        setStrength(30);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Eat Mace");
        lore.add("§7Right click to §aeat§7!");
        return lore;
    }
}
