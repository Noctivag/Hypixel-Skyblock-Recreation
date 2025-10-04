package de.noctivag.plugin.features.cosmetics;

import de.noctivag.plugin.core.api.Service;
import de.noctivag.plugin.features.cosmetics.config.CosmeticConfig;
import de.noctivag.plugin.features.cosmetics.config.CosmeticConfig.CosmeticCategory;
import de.noctivag.plugin.features.cosmetics.config.CosmeticConfig.CosmeticRarity;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simplified Advanced Cosmetics System
 */
public class AdvancedCosmeticsSystem implements Service {
    
    private final Map<UUID, Map<String, Boolean>> playerCosmetics = new ConcurrentHashMap<>();
    private final Map<String, CosmeticConfig> cosmeticConfigs = new ConcurrentHashMap<>();
    
    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            // Initialize basic cosmetic configurations
            initializeBasicCosmetics();
        });
    }
    
    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            // Save player cosmetics
            savePlayerCosmetics();
        });
    }
    
    @Override
    public boolean isInitialized() {
        return !cosmeticConfigs.isEmpty();
    }
    
    @Override
    public String getName() {
        return "AdvancedCosmeticsSystem";
    }
    
    @Override
    public int getPriority() {
        return 50;
    }
    
    @Override
    public boolean isRequired() {
        return false;
    }
    
    /**
     * Get player cosmetics
     */
    public Map<String, Boolean> getPlayerCosmetics(UUID playerId) {
        return playerCosmetics.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>());
    }
    
    /**
     * Unlock cosmetic for player
     */
    public boolean unlockCosmetic(UUID playerId, String cosmeticId) {
        Map<String, Boolean> playerCosmeticMap = getPlayerCosmetics(playerId);
        playerCosmeticMap.put(cosmeticId, true);
        return true;
    }
    
    /**
     * Check if player has cosmetic
     */
    public boolean hasCosmetic(UUID playerId, String cosmeticId) {
        Map<String, Boolean> playerCosmeticMap = getPlayerCosmetics(playerId);
        return playerCosmeticMap.getOrDefault(cosmeticId, false);
    }
    
    /**
     * Get all available cosmetics
     */
    public Collection<CosmeticConfig> getAllCosmetics() {
        return cosmeticConfigs.values();
    }
    
    /**
     * Get cosmetics by category
     */
    public List<CosmeticConfig> getCosmeticsByCategory(CosmeticCategory category) {
        return cosmeticConfigs.values().stream()
                .filter(config -> config.getCategory() == category)
                .toList();
    }
    
    /**
     * Initialize basic cosmetics
     */
    private void initializeBasicCosmetics() {
        // Basic hat cosmetics
        createCosmetic("basic_hat", "Basic Hat", "A simple hat", 
            CosmeticCategory.HATS, CosmeticRarity.COMMON);
        
        // Basic cloak cosmetics
        createCosmetic("basic_cloak", "Basic Cloak", "A simple cloak", 
            CosmeticCategory.CLOAK, CosmeticRarity.COMMON);
        
        // Basic wing cosmetics
        createCosmetic("basic_wings", "Basic Wings", "Simple wings", 
            CosmeticCategory.WINGS, CosmeticRarity.UNCOMMON);
        
        // Basic aura cosmetics
        createCosmetic("basic_aura", "Basic Aura", "A simple aura", 
            CosmeticCategory.AURA, CosmeticRarity.RARE);
        
        // Basic trail cosmetics
        createCosmetic("basic_trail", "Basic Trail", "A simple trail", 
            CosmeticCategory.TRAIL, CosmeticRarity.UNCOMMON);
    }
    
    /**
     * Create cosmetic configuration
     */
    private void createCosmetic(String id, String name, String description, 
                               CosmeticCategory category, CosmeticRarity rarity) {
        CosmeticConfig config = new CosmeticConfig(
            id, name, description, category, rarity, 
            new ArrayList<>(), new HashMap<>()
        );
        cosmeticConfigs.put(id, config);
    }
    
    /**
     * Save player cosmetics
     */
    private void savePlayerCosmetics() {
        // Simplified save implementation
    }
}