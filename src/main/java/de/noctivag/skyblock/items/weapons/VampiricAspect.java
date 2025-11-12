package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class VampiricAspect extends CustomItem {
    public VampiricAspect() {
        super("VOIDLING_KATANA", "Voidling Katana", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(248);
        setStrength(143);
        setCritChance(17);
        setIntelligence(88);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Void energy");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
