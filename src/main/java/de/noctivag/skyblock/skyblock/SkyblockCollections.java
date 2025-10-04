package de.noctivag.skyblock.skyblock;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SkyblockCollections {
    private final Map<Material, Integer> collections = new HashMap<>();
    private final Map<Material, Set<Integer>> milestones = new HashMap<>();
    
    public void addCollection(Material material, int amount) {
        int current = collections.getOrDefault(material, 0);
        collections.put(material, current + amount);
    }
    
    public int getCollection(Material material) {
        return collections.getOrDefault(material, 0);
    }
    
    public void addMilestone(Material material, int milestone) {
        milestones.computeIfAbsent(material, k -> new java.util.HashSet<>()).add(milestone);
    }
    
    public boolean hasMilestone(Material material, int milestone) {
        return milestones.getOrDefault(material, new java.util.HashSet<>()).contains(milestone);
    }
    
    public Set<Integer> getMilestones(Material material) {
        return milestones.getOrDefault(material, new java.util.HashSet<>());
    }
    
    public Map<Material, Integer> getAllCollections() {
        return new HashMap<>(collections);
    }
    
    public int getTotalCollections() {
        return collections.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    public void save() {
        // Save collections data
        // Implementation would go here
    }
}
