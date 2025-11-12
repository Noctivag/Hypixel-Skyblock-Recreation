package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class UnderdLordSword extends CustomItem {
    public UnderdLordSword() {
        super("UNDEAD_SWORD", "Undead Sword", ItemRarity.UNCOMMON, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(60);
        setStrength(35);
        setCritChance(0);
        setIntelligence(20);
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
