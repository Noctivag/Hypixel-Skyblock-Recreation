package de.noctivag.skyblock.engine;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.features.stats.types.PrimaryStat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ReforgeMatrixManager - Manages reforge stat matrices with external JSON configuration
 * 
 * This manager handles the precise reforge stat calculations based on:
 * - Item type (weapon, armor, accessory, tool)
 * - Item rarity (common, uncommon, rare, epic, legendary, mythic)
 * - Reforge type (sharp, heavy, light, wise, pure, fierce, etc.)
 * 
 * All reforge values are stored in an external JSON file for easy maintenance
 * and balance adjustments without code changes.
 */
public class ReforgeMatrixManager {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final File reforgeMatrixFile;
    private final Map<String, ReforgeMatrix> reforgeMatrices = new ConcurrentHashMap<>();
    private final JSONParser jsonParser = new JSONParser();
    
    public ReforgeMatrixManager(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.reforgeMatrixFile = new File(SkyblockPlugin.getDataFolder(), "reforge_matrix.json");
        initializeReforgeMatrix();
    }
    
    /**
     * Initialize reforge matrix from JSON file
     */
    private void initializeReforgeMatrix() {
        if (!reforgeMatrixFile.exists()) {
            createDefaultReforgeMatrix();
        }
        
        loadReforgeMatrix();
    }
    
    /**
     * Create default reforge matrix JSON file
     */
    private void createDefaultReforgeMatrix() {
        try {
            SkyblockPlugin.getDataFolder().mkdirs();
            
            JSONObject reforgeMatrix = new JSONObject();
            
            // Weapon Reforges
            JSONObject weaponReforges = new JSONObject();
            
            // Sharp Reforge
            JSONObject sharp = new JSONObject();
            sharp.put("displayName", "Sharp");
            sharp.put("color", "§c");
            sharp.put("description", "Increases damage and critical chance");
            
            JSONObject sharpStats = new JSONObject();
            sharpStats.put("common", createRarityStats(15, 0, 0, 0, 0, 0, 0, 0, 0));
            sharpStats.put("uncommon", createRarityStats(20, 0, 0, 0, 0, 0, 0, 0, 0));
            sharpStats.put("rare", createRarityStats(25, 0, 0, 0, 0, 0, 0, 0, 0));
            sharpStats.put("epic", createRarityStats(30, 0, 0, 0, 0, 0, 0, 0, 0));
            sharpStats.put("legendary", createRarityStats(35, 0, 0, 0, 0, 0, 0, 0, 0));
            sharpStats.put("mythic", createRarityStats(40, 0, 0, 0, 0, 0, 0, 0, 0));
            sharp.put("stats", sharpStats);
            weaponReforges.put("sharp", sharp);
            
            // Heavy Reforge
            JSONObject heavy = new JSONObject();
            heavy.put("displayName", "Heavy");
            heavy.put("color", "§8");
            heavy.put("description", "Increases damage and defense");
            
            JSONObject heavyStats = new JSONObject();
            heavyStats.put("common", createRarityStats(10, 10, 0, 0, 0, 0, 0, 0, 0));
            heavyStats.put("uncommon", createRarityStats(15, 15, 0, 0, 0, 0, 0, 0, 0));
            heavyStats.put("rare", createRarityStats(20, 20, 0, 0, 0, 0, 0, 0, 0));
            heavyStats.put("epic", createRarityStats(25, 25, 0, 0, 0, 0, 0, 0, 0));
            heavyStats.put("legendary", createRarityStats(30, 30, 0, 0, 0, 0, 0, 0, 0));
            heavyStats.put("mythic", createRarityStats(35, 35, 0, 0, 0, 0, 0, 0, 0));
            heavy.put("stats", heavyStats);
            weaponReforges.put("heavy", heavy);
            
            // Light Reforge
            JSONObject light = new JSONObject();
            light.put("displayName", "Light");
            light.put("color", "§f");
            light.put("description", "Increases speed and critical chance");
            
            JSONObject lightStats = new JSONObject();
            lightStats.put("common", createRarityStats(0, 0, 0, 0, 20, 0, 0, 0, 0));
            lightStats.put("uncommon", createRarityStats(0, 0, 0, 0, 25, 0, 0, 0, 0));
            lightStats.put("rare", createRarityStats(0, 0, 0, 0, 30, 0, 0, 0, 0));
            lightStats.put("epic", createRarityStats(0, 0, 0, 0, 35, 0, 0, 0, 0));
            lightStats.put("legendary", createRarityStats(0, 0, 0, 0, 40, 0, 0, 0, 0));
            lightStats.put("mythic", createRarityStats(0, 0, 0, 0, 45, 0, 0, 0, 0));
            light.put("stats", lightStats);
            weaponReforges.put("light", light);
            
            // Wise Reforge
            JSONObject wise = new JSONObject();
            wise.put("displayName", "Wise");
            wise.put("color", "§b");
            wise.put("description", "Increases intelligence and ability damage");
            
            JSONObject wiseStats = new JSONObject();
            wiseStats.put("common", createRarityStats(0, 0, 0, 20, 0, 0, 0, 0, 0));
            wiseStats.put("uncommon", createRarityStats(0, 0, 0, 25, 0, 0, 0, 0, 0));
            wiseStats.put("rare", createRarityStats(0, 0, 0, 30, 0, 0, 0, 0, 0));
            wiseStats.put("epic", createRarityStats(0, 0, 0, 35, 0, 0, 0, 0, 0));
            wiseStats.put("legendary", createRarityStats(0, 0, 0, 40, 0, 0, 0, 0, 0));
            wiseStats.put("mythic", createRarityStats(0, 0, 0, 45, 0, 0, 0, 0, 0));
            wise.put("stats", wiseStats);
            weaponReforges.put("wise", wise);
            
            // Pure Reforge
            JSONObject pure = new JSONObject();
            pure.put("displayName", "Pure");
            pure.put("color", "§d");
            pure.put("description", "Increases critical chance and critical damage");
            
            JSONObject pureStats = new JSONObject();
            pureStats.put("common", createRarityStats(0, 0, 0, 0, 0, 5, 10, 0, 0));
            pureStats.put("uncommon", createRarityStats(0, 0, 0, 0, 0, 7, 15, 0, 0));
            pureStats.put("rare", createRarityStats(0, 0, 0, 0, 0, 10, 20, 0, 0));
            pureStats.put("epic", createRarityStats(0, 0, 0, 0, 0, 12, 25, 0, 0));
            pureStats.put("legendary", createRarityStats(0, 0, 0, 0, 0, 15, 30, 0, 0));
            pureStats.put("mythic", createRarityStats(0, 0, 0, 0, 0, 18, 35, 0, 0));
            pure.put("stats", pureStats);
            weaponReforges.put("pure", pure);
            
            // Fierce Reforge
            JSONObject fierce = new JSONObject();
            fierce.put("displayName", "Fierce");
            fierce.put("color", "§4");
            fierce.put("description", "Increases strength and critical damage");
            
            JSONObject fierceStats = new JSONObject();
            fierceStats.put("common", createRarityStats(10, 0, 0, 0, 0, 0, 15, 0, 0));
            fierceStats.put("uncommon", createRarityStats(15, 0, 0, 0, 0, 0, 20, 0, 0));
            fierceStats.put("rare", createRarityStats(20, 0, 0, 0, 0, 0, 25, 0, 0));
            fierceStats.put("epic", createRarityStats(25, 0, 0, 0, 0, 0, 30, 0, 0));
            fierceStats.put("legendary", createRarityStats(30, 0, 0, 0, 0, 0, 35, 0, 0));
            fierceStats.put("mythic", createRarityStats(35, 0, 0, 0, 0, 0, 40, 0, 0));
            fierce.put("stats", fierceStats);
            weaponReforges.put("fierce", fierce);
            
            reforgeMatrix.put("weapon", weaponReforges);
            
            // Armor Reforges
            JSONObject armorReforges = new JSONObject();
            
            // Pure Armor Reforge
            JSONObject pureArmor = new JSONObject();
            pureArmor.put("displayName", "Pure");
            pureArmor.put("color", "§d");
            pureArmor.put("description", "Increases critical chance and critical damage");
            
            JSONObject pureArmorStats = new JSONObject();
            pureArmorStats.put("common", createRarityStats(0, 0, 0, 0, 0, 3, 8, 0, 0));
            pureArmorStats.put("uncommon", createRarityStats(0, 0, 0, 0, 0, 5, 12, 0, 0));
            pureArmorStats.put("rare", createRarityStats(0, 0, 0, 0, 0, 7, 16, 0, 0));
            pureArmorStats.put("epic", createRarityStats(0, 0, 0, 0, 0, 10, 20, 0, 0));
            pureArmorStats.put("legendary", createRarityStats(0, 0, 0, 0, 0, 12, 24, 0, 0));
            pureArmorStats.put("mythic", createRarityStats(0, 0, 0, 0, 0, 15, 28, 0, 0));
            pureArmor.put("stats", pureArmorStats);
            armorReforges.put("pure", pureArmor);
            
            // Heavy Armor Reforge
            JSONObject heavyArmor = new JSONObject();
            heavyArmor.put("displayName", "Heavy");
            heavyArmor.put("color", "§8");
            heavyArmor.put("description", "Increases defense and health");
            
            JSONObject heavyArmorStats = new JSONObject();
            heavyArmorStats.put("common", createRarityStats(0, 15, 0, 0, 0, 0, 0, 0, 0));
            heavyArmorStats.put("uncommon", createRarityStats(0, 20, 0, 0, 0, 0, 0, 0, 0));
            heavyArmorStats.put("rare", createRarityStats(0, 25, 0, 0, 0, 0, 0, 0, 0));
            heavyArmorStats.put("epic", createRarityStats(0, 30, 0, 0, 0, 0, 0, 0, 0));
            heavyArmorStats.put("legendary", createRarityStats(0, 35, 0, 0, 0, 0, 0, 0, 0));
            heavyArmorStats.put("mythic", createRarityStats(0, 40, 0, 0, 0, 0, 0, 0, 0));
            heavyArmor.put("stats", heavyArmorStats);
            armorReforges.put("heavy", heavyArmor);
            
            // Wise Armor Reforge
            JSONObject wiseArmor = new JSONObject();
            wiseArmor.put("displayName", "Wise");
            wiseArmor.put("color", "§b");
            wiseArmor.put("description", "Increases intelligence and mana");
            
            JSONObject wiseArmorStats = new JSONObject();
            wiseArmorStats.put("common", createRarityStats(0, 0, 0, 15, 0, 0, 0, 0, 0));
            wiseArmorStats.put("uncommon", createRarityStats(0, 0, 0, 20, 0, 0, 0, 0, 0));
            wiseArmorStats.put("rare", createRarityStats(0, 0, 0, 25, 0, 0, 0, 0, 0));
            wiseArmorStats.put("epic", createRarityStats(0, 0, 0, 30, 0, 0, 0, 0, 0));
            wiseArmorStats.put("legendary", createRarityStats(0, 0, 0, 35, 0, 0, 0, 0, 0));
            wiseArmorStats.put("mythic", createRarityStats(0, 0, 0, 40, 0, 0, 0, 0, 0));
            wiseArmor.put("stats", wiseArmorStats);
            armorReforges.put("wise", wiseArmor);
            
            reforgeMatrix.put("armor", armorReforges);
            
            // Accessory Reforges
            JSONObject accessoryReforges = new JSONObject();
            
            // Bizarre Reforge
            JSONObject bizarre = new JSONObject();
            bizarre.put("displayName", "Bizarre");
            bizarre.put("color", "§5");
            bizarre.put("description", "Increases intelligence and ability damage");
            
            JSONObject bizarreStats = new JSONObject();
            bizarreStats.put("common", createRarityStats(0, 0, 0, 10, 0, 0, 0, 0, 0));
            bizarreStats.put("uncommon", createRarityStats(0, 0, 0, 15, 0, 0, 0, 0, 0));
            bizarreStats.put("rare", createRarityStats(0, 0, 0, 20, 0, 0, 0, 0, 0));
            bizarreStats.put("epic", createRarityStats(0, 0, 0, 25, 0, 0, 0, 0, 0));
            bizarreStats.put("legendary", createRarityStats(0, 0, 0, 30, 0, 0, 0, 0, 0));
            bizarreStats.put("mythic", createRarityStats(0, 0, 0, 35, 0, 0, 0, 0, 0));
            bizarre.put("stats", bizarreStats);
            accessoryReforges.put("bizarre", bizarre);
            
            // Forceful Reforge
            JSONObject forceful = new JSONObject();
            forceful.put("displayName", "Forceful");
            forceful.put("color", "§c");
            forceful.put("description", "Increases strength and damage");
            
            JSONObject forcefulStats = new JSONObject();
            forcefulStats.put("common", createRarityStats(8, 0, 0, 0, 0, 0, 0, 0, 0));
            forcefulStats.put("uncommon", createRarityStats(12, 0, 0, 0, 0, 0, 0, 0, 0));
            forcefulStats.put("rare", createRarityStats(16, 0, 0, 0, 0, 0, 0, 0, 0));
            forcefulStats.put("epic", createRarityStats(20, 0, 0, 0, 0, 0, 0, 0, 0));
            forcefulStats.put("legendary", createRarityStats(24, 0, 0, 0, 0, 0, 0, 0, 0));
            forcefulStats.put("mythic", createRarityStats(28, 0, 0, 0, 0, 0, 0, 0, 0));
            forceful.put("stats", forcefulStats);
            accessoryReforges.put("forceful", forceful);
            
            reforgeMatrix.put("accessory", accessoryReforges);
            
            // Write to file
            try (FileWriter file = new FileWriter(reforgeMatrixFile)) {
                file.write(reforgeMatrix.toJSONString());
                file.flush();
            }
            
        } catch (IOException e) {
            SkyblockPlugin.getLogger().severe("Failed to create default reforge matrix: " + e.getMessage());
        }
    }
    
    /**
     * Create rarity stats JSON object
     * Order: Health, Defense, Strength, Intelligence, Speed, Crit Chance, Crit Damage, Attack Speed, Ferocity
     */
    private JSONObject createRarityStats(double health, double defense, double strength, double intelligence, 
                                       double speed, double critChance, double critDamage, double attackSpeed, double ferocity) {
        JSONObject stats = new JSONObject();
        stats.put("health", health);
        stats.put("defense", defense);
        stats.put("strength", strength);
        stats.put("intelligence", intelligence);
        stats.put("speed", speed);
        stats.put("critChance", critChance);
        stats.put("critDamage", critDamage);
        stats.put("attackSpeed", attackSpeed);
        stats.put("ferocity", ferocity);
        return stats;
    }
    
    /**
     * Load reforge matrix from JSON file
     */
    private void loadReforgeMatrix() {
        try (FileReader reader = new FileReader(reforgeMatrixFile)) {
            JSONObject reforgeMatrix = (JSONObject) jsonParser.parse(reader);
            
            // Load weapon reforges
            JSONObject weaponReforges = (JSONObject) reforgeMatrix.get("weapon");
            if (weaponReforges != null) {
                loadReforgeCategory("weapon", weaponReforges);
            }
            
            // Load armor reforges
            JSONObject armorReforges = (JSONObject) reforgeMatrix.get("armor");
            if (armorReforges != null) {
                loadReforgeCategory("armor", armorReforges);
            }
            
            // Load accessory reforges
            JSONObject accessoryReforges = (JSONObject) reforgeMatrix.get("accessory");
            if (accessoryReforges != null) {
                loadReforgeCategory("accessory", accessoryReforges);
            }
            
        } catch (IOException | ParseException e) {
            SkyblockPlugin.getLogger().severe("Failed to load reforge matrix: " + e.getMessage());
        }
    }
    
    /**
     * Load reforge category from JSON
     */
    private void loadReforgeCategory(String category, JSONObject reforges) {
        for (Object key : reforges.keySet()) {
            String reforgeName = (String) key;
            JSONObject reforgeData = (JSONObject) reforges.get(reforgeName);
            
            String displayName = (String) reforgeData.get("displayName");
            String color = (String) reforgeData.get("color");
            String description = (String) reforgeData.get("description");
            JSONObject stats = (JSONObject) reforgeData.get("stats");
            
            ReforgeMatrix matrix = new ReforgeMatrix(displayName, color, description);
            
            // Load stats for each rarity
            for (Object rarityKey : stats.keySet()) {
                String rarity = (String) rarityKey;
                JSONObject rarityStats = (JSONObject) stats.get(rarity);
                
                Map<PrimaryStat, Double> statMap = new HashMap<>();
                statMap.put(PrimaryStat.HEALTH, getDoubleValue(rarityStats, "health"));
                statMap.put(PrimaryStat.DEFENSE, getDoubleValue(rarityStats, "defense"));
                statMap.put(PrimaryStat.STRENGTH, getDoubleValue(rarityStats, "strength"));
                statMap.put(PrimaryStat.INTELLIGENCE, getDoubleValue(rarityStats, "intelligence"));
                statMap.put(PrimaryStat.SPEED, getDoubleValue(rarityStats, "speed"));
                statMap.put(PrimaryStat.CRIT_CHANCE, getDoubleValue(rarityStats, "critChance"));
                statMap.put(PrimaryStat.CRIT_DAMAGE, getDoubleValue(rarityStats, "critDamage"));
                statMap.put(PrimaryStat.ATTACK_SPEED, getDoubleValue(rarityStats, "attackSpeed"));
                statMap.put(PrimaryStat.FEROCITY, getDoubleValue(rarityStats, "ferocity"));
                
                matrix.setRarityStats(rarity, statMap);
            }
            
            reforgeMatrices.put(category + "_" + reforgeName, matrix);
        }
    }
    
    /**
     * Get double value from JSON object
     */
    private double getDoubleValue(JSONObject obj, String key) {
        Object value = obj.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return 0.0;
    }
    
    /**
     * Apply reforge stats to player profile
     */
    public void applyReforgeStats(StatCalculationService.PlayerStatProfile profile, ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return;
        }
        
        List<String> lore = meta.getLore();
        if (lore == null) {
            return;
        }
        
        // Find reforge line in lore
        String reforgeLine = null;
        String rarity = null;
        
        for (String line : lore) {
            if (line.contains("Reforge:")) {
                reforgeLine = line;
            } else if (line.contains("§") && (line.contains("COMMON") || line.contains("UNCOMMON") || 
                     line.contains("RARE") || line.contains("EPIC") || line.contains("LEGENDARY") || line.contains("MYTHIC"))) {
                rarity = extractRarity(line);
            }
        }
        
        if (reforgeLine == null || rarity == null) {
            return;
        }
        
        // Extract reforge name
        String reforgeName = extractReforgeName(reforgeLine);
        if (reforgeName == null) {
            return;
        }
        
        // Determine item category
        String category = determineItemCategory(item);
        
        // Get reforge matrix
        String matrixKey = category + "_" + reforgeName.toLowerCase();
        ReforgeMatrix matrix = reforgeMatrices.get(matrixKey);
        
        if (matrix != null) {
            Map<PrimaryStat, Double> stats = matrix.getRarityStats(rarity);
            if (stats != null) {
                // Apply stats to profile
                for (Map.Entry<PrimaryStat, Double> entry : stats.entrySet()) {
                    profile.addFlatBonus(entry.getKey(), entry.getValue());
                }
            }
        }
    }
    
    /**
     * Extract rarity from lore line
     */
    private String extractRarity(String line) {
        if (line.contains("COMMON")) return "common";
        if (line.contains("UNCOMMON")) return "uncommon";
        if (line.contains("RARE")) return "rare";
        if (line.contains("EPIC")) return "epic";
        if (line.contains("LEGENDARY")) return "legendary";
        if (line.contains("MYTHIC")) return "mythic";
        return "common";
    }
    
    /**
     * Extract reforge name from lore line
     */
    private String extractReforgeName(String line) {
        // Remove color codes and extract reforge name
        String cleanLine = line.replaceAll("§[0-9a-fk-or]", "");
        if (cleanLine.contains("Reforge:")) {
            String[] parts = cleanLine.split("Reforge:");
            if (parts.length > 1) {
                return parts[1].trim();
            }
        }
        return null;
    }
    
    /**
     * Determine item category based on material
     */
    private String determineItemCategory(ItemStack item) {
        Material material = item.getType();
        
        // Weapons
        if (material.name().contains("SWORD") || material.name().contains("AXE") || 
            material.name().contains("BOW") || material.name().contains("CROSSBOW") ||
            material.name().contains("TRIDENT")) {
            return "weapon";
        }
        
        // Armor
        if (material.name().contains("HELMET") || material.name().contains("CHESTPLATE") || 
            material.name().contains("LEGGINGS") || material.name().contains("BOOTS")) {
            return "armor";
        }
        
        // Tools
        if (material.name().contains("PICKAXE") || material.name().contains("SHOVEL") || 
            material.name().contains("HOE") || material.name().contains("SHEARS")) {
            return "tool";
        }
        
        // Accessories (default)
        return "accessory";
    }
    
    /**
     * Reforge Matrix - stores reforge data for all rarities
     */
    public static class ReforgeMatrix {
        private final String displayName;
        private final String color;
        private final String description;
        private final Map<String, Map<PrimaryStat, Double>> rarityStats = new HashMap<>();
        
        public ReforgeMatrix(String displayName, String color, String description) {
            this.displayName = displayName;
            this.color = color;
            this.description = description;
        }
        
        public void setRarityStats(String rarity, Map<PrimaryStat, Double> stats) {
            rarityStats.put(rarity, new HashMap<>(stats));
        }
        
        public Map<PrimaryStat, Double> getRarityStats(String rarity) {
            return rarityStats.get(rarity);
        }
        
        public String getDisplayName() { return displayName; }
        public String getColor() { return color; }
        public String getDescription() { return description; }
    }
}
