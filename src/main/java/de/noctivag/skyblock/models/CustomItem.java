package de.noctivag.skyblock.models;

import de.noctivag.skyblock.enums.CustomItemType;
import de.noctivag.skyblock.enums.Rarity;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repräsentiert ein Custom Item mit Stats und Fähigkeiten
 */
public class CustomItem {
    
    private final CustomItemType itemType;
    private final Map<String, Double> stats;
    private int enchantmentLevel;
    private boolean reforged;
    private String reforge;
    
    public CustomItem(CustomItemType itemType) {
        this.itemType = itemType;
        this.stats = new HashMap<>();
        this.enchantmentLevel = 0;
        this.reforged = false;
        this.reforge = "";
        
        initializeDefaultStats();
    }
    
    /**
     * Initialisiert die Standard-Stats basierend auf dem Item-Typ
     */
    private void initializeDefaultStats() {
        switch (itemType) {
            case HYPERION:
                stats.put("damage", 260.0);
                stats.put("strength", 150.0);
                stats.put("crit_damage", 50.0);
                stats.put("intelligence", 350.0);
                break;
            case SCYLLA:
                stats.put("damage", 270.0);
                stats.put("strength", 140.0);
                stats.put("crit_damage", 55.0);
                stats.put("intelligence", 300.0);
                break;
            case VALKYRIE:
                stats.put("damage", 250.0);
                stats.put("strength", 160.0);
                stats.put("crit_damage", 45.0);
                stats.put("intelligence", 400.0);
                break;
            case ASTRAEA:
                stats.put("damage", 265.0);
                stats.put("strength", 145.0);
                stats.put("crit_damage", 52.0);
                stats.put("intelligence", 325.0);
                break;
            case SPIRIT_SCEPTRE:
                stats.put("damage", 220.0);
                stats.put("intelligence", 500.0);
                stats.put("crit_damage", 30.0);
                break;
            case TERMINATOR:
                stats.put("damage", 310.0);
                stats.put("strength", 100.0);
                stats.put("crit_damage", 80.0);
                stats.put("crit_chance", 30.0);
                break;
            case DAEDALUS_AXE:
                stats.put("damage", 180.0);
                stats.put("strength", 200.0);
                stats.put("crit_damage", 75.0);
                break;
            case DIVANS_DRILL:
                stats.put("mining_speed", 1500.0);
                stats.put("breaking_power", 10.0);
                break;
            case SHADOW_ASSASSIN_HELMET:
                stats.put("health", 130.0);
                stats.put("defense", 80.0);
                stats.put("crit_damage", 25.0);
                stats.put("intelligence", 150.0);
                break;
            case SHADOW_ASSASSIN_CHESTPLATE:
                stats.put("health", 200.0);
                stats.put("defense", 120.0);
                stats.put("crit_damage", 35.0);
                stats.put("intelligence", 200.0);
                break;
            case SHADOW_ASSASSIN_LEGGINGS:
                stats.put("health", 180.0);
                stats.put("defense", 110.0);
                stats.put("crit_damage", 30.0);
                stats.put("intelligence", 175.0);
                break;
            case SHADOW_ASSASSIN_BOOTS:
                stats.put("health", 120.0);
                stats.put("defense", 70.0);
                stats.put("crit_damage", 20.0);
                stats.put("intelligence", 125.0);
                break;
            case NECRON_HELMET:
                stats.put("health", 150.0);
                stats.put("defense", 100.0);
                stats.put("strength", 40.0);
                stats.put("crit_damage", 30.0);
                stats.put("intelligence", 50.0);
                break;
            case NECRON_CHESTPLATE:
                stats.put("health", 260.0);
                stats.put("defense", 160.0);
                stats.put("strength", 60.0);
                stats.put("crit_damage", 40.0);
                stats.put("intelligence", 70.0);
                break;
            case NECRON_LEGGINGS:
                stats.put("health", 230.0);
                stats.put("defense", 140.0);
                stats.put("strength", 50.0);
                stats.put("crit_damage", 35.0);
                stats.put("intelligence", 60.0);
                break;
            case NECRON_BOOTS:
                stats.put("health", 140.0);
                stats.put("defense", 90.0);
                stats.put("strength", 30.0);
                stats.put("crit_damage", 25.0);
                stats.put("intelligence", 40.0);
                break;
            case SUPERIOR_DRAGON_HELMET:
                stats.put("health", 90.0);
                stats.put("defense", 130.0);
                stats.put("strength", 10.0);
                stats.put("crit_damage", 14.0);
                stats.put("crit_chance", 2.0);
                break;
            case SUPERIOR_DRAGON_CHESTPLATE:
                stats.put("health", 140.0);
                stats.put("defense", 200.0);
                stats.put("strength", 16.0);
                stats.put("crit_damage", 20.0);
                stats.put("crit_chance", 3.0);
                break;
            case SUPERIOR_DRAGON_LEGGINGS:
                stats.put("health", 120.0);
                stats.put("defense", 170.0);
                stats.put("strength", 14.0);
                stats.put("crit_damage", 18.0);
                stats.put("crit_chance", 2.5);
                break;
            case SUPERIOR_DRAGON_BOOTS:
                stats.put("health", 80.0);
                stats.put("defense", 110.0);
                stats.put("strength", 8.0);
                stats.put("crit_damage", 12.0);
                stats.put("crit_chance", 1.5);
                break;
            case OVERFLUX_CAPACITOR:
                stats.put("strength", 60.0);
                stats.put("crit_damage", 125.0);
                break;
            case WARDEN_HEART:
                stats.put("health", 500.0);
                stats.put("defense", 100.0);
                stats.put("strength", 50.0);
                break;
            default:
                break;
        }
    }
    
    /**
     * Erstellt ein ItemStack aus diesem Custom Item
     */
    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(itemType.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            // Set display name
            meta.setDisplayName(itemType.getDisplayName());
            
            // Create lore
            List<String> lore = new ArrayList<>();
            lore.add("");
            
            // Add stats
            for (Map.Entry<String, Double> entry : stats.entrySet()) {
                String statName = formatStatName(entry.getKey());
                String statValue = String.format("%.0f", entry.getValue());
                lore.add("§7" + statName + ": §a+" + statValue);
            }
            
            lore.add("");
            lore.add("§7" + itemType.getDescription());
            lore.add("");
            
            // Add ability description
            String abilityDescription = getAbilityDescription();
            if (!abilityDescription.isEmpty()) {
                lore.add("§6Item Ability: " + getAbilityName());
                lore.add("§7" + abilityDescription);
                lore.add("");
            }
            
            // Add rarity
            lore.add(itemType.getRarity().getColorCode() + itemType.getRarity().getGermanName().toUpperCase());
            
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Formatiert den Stat-Namen für die Anzeige
     */
    private String formatStatName(String statKey) {
        switch (statKey) {
            case "damage": return "Damage";
            case "strength": return "Strength";
            case "crit_damage": return "Crit Damage";
            case "crit_chance": return "Crit Chance";
            case "intelligence": return "Intelligence";
            case "health": return "Health";
            case "defense": return "Defense";
            case "speed": return "Speed";
            case "mining_speed": return "Mining Speed";
            case "breaking_power": return "Breaking Power";
            default: return statKey;
        }
    }
    
    /**
     * Gibt den Namen der Item-Fähigkeit zurück
     */
    private String getAbilityName() {
        switch (itemType) {
            case HYPERION: return "Implosion";
            case SCYLLA: return "Healing Circle";
            case VALKYRIE: return "Shadow Fury";
            case ASTRAEA: return "Lightning";
            case SPIRIT_SCEPTRE: return "BAT Power";
            case TERMINATOR: return "Salvation";
            case DAEDALUS_AXE: return "Seismic Wave";
            default: return "";
        }
    }
    
    /**
     * Gibt die Beschreibung der Item-Fähigkeit zurück
     */
    private String getAbilityDescription() {
        switch (itemType) {
            case HYPERION: 
                return "Verursacht eine Explosion die Gegner anzieht und Schaden verursacht.";
            case SCYLLA: 
                return "Erstellt einen Heilkreis der Verbündete heilt.";
            case VALKYRIE: 
                return "Teleportiert dich zum Ziel und verursacht Schaden.";
            case ASTRAEA: 
                return "Ruft Blitze herbei die Gegner treffen.";
            case SPIRIT_SCEPTRE: 
                return "Verschießt zielsuchende Fledermaus-Projektile.";
            case TERMINATOR: 
                return "Pfeile prallen bei Kills ab und treffen weitere Gegner.";
            case DAEDALUS_AXE: 
                return "Erhöht Schaden pro 50 Stärke um 100%.";
            default: 
                return "";
        }
    }
    
    // Getters
    public CustomItemType getItemType() {
        return itemType;
    }
    
    public Map<String, Double> getStats() {
        return new HashMap<>(stats);
    }
    
    public double getStat(String statKey) {
        return stats.getOrDefault(statKey, 0.0);
    }
    
    public int getEnchantmentLevel() {
        return enchantmentLevel;
    }
    
    public boolean isReforged() {
        return reforged;
    }
    
    public String getReforge() {
        return reforge;
    }
    
    // Setters
    public void setStat(String statKey, double value) {
        stats.put(statKey, value);
    }
    
    public void addStat(String statKey, double value) {
        stats.put(statKey, stats.getOrDefault(statKey, 0.0) + value);
    }
    
    public void setEnchantmentLevel(int level) {
        this.enchantmentLevel = level;
    }
    
    public void setReforge(String reforge) {
        this.reforge = reforge;
        this.reforged = true;
    }
}
