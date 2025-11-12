package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class AstraeaSword extends CustomItem {
    public AstraeaSword() {
        super("ASTRAEA", "Astraea", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(280);
        setStrength(160);
        setCritChance(15);
        setCritDamage(100);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Supernova §e§lRIGHT CLICK");
        lore.add("§7Creates a massive explosion");
        lore.add("§7dealing §c50,000 §7damage");
        lore.add("§7to all nearby enemies.");
        lore.add("§8Mana Cost: §3200");
        return lore;
    }
}
