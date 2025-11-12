package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class SpiritSceptre extends CustomItem {
    public SpiritSceptre() {
        super("SPIRIT_SCEPTRE", "Spirit Sceptre", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(195);
        setStrength(125);
        setCritChance(15);
        setIntelligence(75);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Spirit summon");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
