package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class RagnarokAxe extends CustomItem {
    public RagnarokAxe() {
        super("RAGNAROK_AXE", "Ragnarok Axe", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(250);
        setStrength(140);
        setCritChance(20);
        setIntelligence(90);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Berserker");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
