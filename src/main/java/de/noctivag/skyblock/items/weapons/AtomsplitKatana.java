package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class AtomsplitKatana extends CustomItem {
    public AtomsplitKatana() {
        super("ATOMSPLIT_KATANA", "Atomsplit Katana", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(270);
        setStrength(150);
        setCritChance(30);
        setIntelligence(100);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Split atoms");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
