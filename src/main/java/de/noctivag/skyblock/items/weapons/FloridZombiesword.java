package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class FloridZombiesword extends CustomItem {
    public FloridZombiesword() {
        super("FLORID_ZOMBIE_SWORD", "Florid Zombie Sword", ItemRarity.RARE, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(95);
        setStrength(55);
        setCritChance(5);
        setIntelligence(35);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Zombie slayer");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
