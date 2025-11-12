package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class BalrogsSword extends CustomItem {
    public BalrogsSword() {
        super("BALROGS_SWORD", "Balrog's Sword", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(240);
        setStrength(135);
        setCritChance(15);
        setIntelligence(85);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Fire demon");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
