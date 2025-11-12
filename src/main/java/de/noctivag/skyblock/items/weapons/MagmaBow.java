package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class MagmaBow extends CustomItem {
    public MagmaBow() {
        super("MAGMA_BOW", "Magma Bow", ItemRarity.EPIC, ItemType.BOW, Material.DIAMOND_SWORD);
        setDamage(150);
        setStrength(90);
        setCritChance(10);
        setIntelligence(60);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Fire arrows");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
