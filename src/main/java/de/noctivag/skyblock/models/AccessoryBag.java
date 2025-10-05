package de.noctivag.skyblock.models;

import de.noctivag.skyblock.enums.Rarity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * AccessoryBag für die Verwaltung von Accessoires
 * Speichert alle einzigartigen Accessoires des Spielers mit ihren Seltenheitsstufen
 */
public class AccessoryBag {
    
    private final Map<String, Rarity> accessories;
    private final Map<String, Integer> accessoryCounts;
    
    public AccessoryBag() {
        this.accessories = new HashMap<>();
        this.accessoryCounts = new HashMap<>();
    }
    
    /**
     * Fügt ein Accessoire hinzu
     * @param accessoryName Name des Accessoires
     * @param rarity Seltenheitsstufe
     */
    public void addAccessory(String accessoryName, Rarity rarity) {
        accessories.put(accessoryName, rarity);
        accessoryCounts.put(accessoryName, accessoryCounts.getOrDefault(accessoryName, 0) + 1);
    }
    
    /**
     * Entfernt ein Accessoire
     * @param accessoryName Name des Accessoires
     * @return true wenn entfernt wurde
     */
    public boolean removeAccessory(String accessoryName) {
        if (accessories.containsKey(accessoryName)) {
            int count = accessoryCounts.getOrDefault(accessoryName, 1);
            if (count > 1) {
                accessoryCounts.put(accessoryName, count - 1);
            } else {
                accessories.remove(accessoryName);
                accessoryCounts.remove(accessoryName);
            }
            return true;
        }
        return false;
    }
    
    /**
     * Prüft ob ein Accessoire vorhanden ist
     * @param accessoryName Name des Accessoires
     * @return true wenn vorhanden
     */
    public boolean hasAccessory(String accessoryName) {
        return accessories.containsKey(accessoryName);
    }
    
    /**
     * Gibt die Seltenheitsstufe eines Accessoires zurück
     * @param accessoryName Name des Accessoires
     * @return Rarity oder null
     */
    public Rarity getAccessoryRarity(String accessoryName) {
        return accessories.get(accessoryName);
    }
    
    /**
     * Gibt die Anzahl eines Accessoires zurück
     * @param accessoryName Name des Accessoires
     * @return Anzahl
     */
    public int getAccessoryCount(String accessoryName) {
        return accessoryCounts.getOrDefault(accessoryName, 0);
    }
    
    /**
     * Gibt alle Accessoire-Namen zurück
     * @return Set aller Accessoire-Namen
     */
    public Set<String> getAllAccessories() {
        return accessories.keySet();
    }

    /**
     * Gibt alle Accessoires zurück (Alias für getAllAccessories)
     * @return Set aller Accessoire-Namen
     */
    public Set<String> getAccessories() {
        return accessories.keySet();
    }
    
    /**
     * Gibt alle Accessoires mit ihrer Seltenheitsstufe zurück
     * @return Map von Accessoire-Namen zu Rarity
     */
    public Map<String, Rarity> getAllAccessoriesWithRarity() {
        return new HashMap<>(accessories);
    }
    
    /**
     * Gibt die Gesamtanzahl aller Accessoires zurück
     * @return Gesamtanzahl
     */
    public int getTotalAccessoryCount() {
        return accessoryCounts.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Gibt die Anzahl der einzigartigen Accessoires zurück
     * @return Anzahl einzigartiger Accessoires
     */
    public int getUniqueAccessoryCount() {
        return accessories.size();
    }
    
    /**
     * Gibt die Anzahl der Accessoires einer bestimmten Seltenheitsstufe zurück
     * @param rarity Die Seltenheitsstufe
     * @return Anzahl
     */
    public int getAccessoryCountByRarity(Rarity rarity) {
        return (int) accessories.values().stream()
            .filter(r -> r == rarity)
            .count();
    }
    
    /**
     * Leert den AccessoryBag
     */
    public void clear() {
        accessories.clear();
        accessoryCounts.clear();
    }
    
    /**
     * Prüft ob der AccessoryBag leer ist
     * @return true wenn leer
     */
    public boolean isEmpty() {
        return accessories.isEmpty();
    }
    
    @Override
    public String toString() {
        return "AccessoryBag{" +
                "accessories=" + accessories.size() +
                ", totalCount=" + getTotalAccessoryCount() +
                '}';
    }
}
