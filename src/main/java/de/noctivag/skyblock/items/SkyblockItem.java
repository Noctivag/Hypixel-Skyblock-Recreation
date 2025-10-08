package de.noctivag.skyblock.items;

import de.noctivag.skyblock.stats.StatType;
import java.util.HashMap;
import java.util.Map;

/**
 * Repräsentiert ein Custom-Item mit Stats, Rarity, Reforge, Ability etc.
 */
public class SkyblockItem {
    private String name;
    private String rarity;
    private ReforgeType reforgeType;
    private String ability;
    private final Map<StatType, Double> stats = new HashMap<>();
    private final EnchantmentContainer enchantments = new EnchantmentContainer();
    private DungeonStar dungeonStar = new DungeonStar();
    private int hotPotatoBooks = 0;
    private final EnchantmentContainer ultimateEnchantments = new EnchantmentContainer();
    public int getHotPotatoBooks() { return hotPotatoBooks; }
    public void setHotPotatoBooks(int books) { this.hotPotatoBooks = Math.max(0, books); }
    public void addHotPotatoBook() { this.hotPotatoBooks = Math.min(10, this.hotPotatoBooks + 1); }
    public EnchantmentContainer getUltimateEnchantments() { return ultimateEnchantments; }
    public DungeonStar getDungeonStar() { return dungeonStar; }
    public void setDungeonStar(DungeonStar star) { this.dungeonStar = star; }

    public SkyblockItem(String name, String rarity) {
        this.name = name;
        this.rarity = rarity;
    }

    public void setStat(StatType type, double value) {
        stats.put(type, value);
    }
    public double getStat(StatType type) {
        return stats.getOrDefault(type, 0.0);
    }
    public Map<StatType, Double> getAllStats() {
        return stats;
    }
    public String getName() { return name; }
    public String getRarity() { return rarity; }
    public ReforgeType getReforgeType() { return reforgeType; }
    public void setReforgeType(ReforgeType reforgeType) { this.reforgeType = reforgeType; }
    public String getAbility() { return ability; }
    public void setAbility(String ability) { this.ability = ability; }
    public EnchantmentContainer getEnchantments() { return enchantments; }
}
