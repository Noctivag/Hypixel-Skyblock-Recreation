package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class RevenantFalchion extends CustomItem {
    public RevenantFalchion() {
        super("REVENANT_FALCHION", "Revenant Falchion", ItemRarity.RARE, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(130);
        setStrength(70);
        setCritChance(10);
        setIntelligence(45);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Undead bonus");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
