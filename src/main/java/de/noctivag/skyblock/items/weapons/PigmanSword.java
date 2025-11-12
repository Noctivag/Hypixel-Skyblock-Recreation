package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class PigmanSword extends CustomItem {
    public PigmanSword() {
        super("PIGMAN_SWORD", "Pigman Sword", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(180);
        setStrength(110);
        setCritChance(15);
        setIntelligence(70);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Nether bonus");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
