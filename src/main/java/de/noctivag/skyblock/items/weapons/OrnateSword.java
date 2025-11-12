package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class OrnateSword extends CustomItem {
    public OrnateSword() {
        super("ORNATE_SWORD", "Ornate Zombie Sword", ItemRarity.EPIC, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(140);
        setStrength(90);
        setCritChance(5);
        setIntelligence(50);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Zombie bonus");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
