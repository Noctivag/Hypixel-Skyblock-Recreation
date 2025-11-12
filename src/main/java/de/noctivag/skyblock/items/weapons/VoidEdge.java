package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class VoidEdge extends CustomItem {
    public VoidEdge() {
        super("VOID_EDGE", "Void Edge", ItemRarity.EPIC, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(165);
        setStrength(100);
        setCritChance(10);
        setIntelligence(65);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Void damage");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
