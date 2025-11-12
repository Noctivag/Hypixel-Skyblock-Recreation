package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class TacticiansEdge extends CustomItem {
    public TacticiansEdge() {
        super("TACTICIANS_EDGE", "Tactician's Edge", ItemRarity.EPIC, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(150);
        setStrength(95);
        setCritChance(15);
        setIntelligence(60);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Intelligence boost");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
