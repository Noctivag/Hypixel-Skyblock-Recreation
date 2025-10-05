package de.noctivag.skyblock.loot;

import java.util.ArrayList;
import java.util.List;

/**
 * Loot Table - Represents a loot table
 */
public class LootTable {
    
    private final String id;
    private final List<LootEntry> lootEntries;
    
    public LootTable(String id) {
        this.id = id;
        this.lootEntries = new ArrayList<>();
    }
    
    /**
     * Get the loot table ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Get the loot entries
     */
    public List<LootEntry> getLootEntries() {
        return new ArrayList<>(lootEntries);
    }
    
    /**
     * Add a loot entry
     */
    public void addLootEntry(LootEntry entry) {
        lootEntries.add(entry);
    }
    
    /**
     * Remove a loot entry
     */
    public boolean removeLootEntry(LootEntry entry) {
        return lootEntries.remove(entry);
    }
    
    /**
     * Clear all loot entries
     */
    public void clearLootEntries() {
        lootEntries.clear();
    }
}

