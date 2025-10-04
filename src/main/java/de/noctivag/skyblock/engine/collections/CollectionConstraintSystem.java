package de.noctivag.skyblock.engine.collections;

import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.engine.collections.types.*;
import de.noctivag.skyblock.engine.collections.types.CollectionItemProgress;
import de.noctivag.skyblock.engine.collections.types.CollectionViolation;
import de.noctivag.skyblock.engine.collections.types.CollectionProcessResult;
import de.noctivag.skyblock.engine.collections.types.CollectionEligibilityResult;
import de.noctivag.skyblock.engine.collections.types.ItemEligibility;
import de.noctivag.skyblock.engine.collections.types.CollectionProgressInfo;
import de.noctivag.skyblock.engine.collections.types.CollectionConstraintStatistics;
import de.noctivag.skyblock.engine.collections.types.CollectionViolationType;

import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Collection Constraint System - Economic Protection Implementation
 * 
 * Implements the core constraint logic that ensures only naturally obtained
 * items count towards collection progression. This is the fundamental
 * mechanism for protecting the economy and enforcing the "Grind" design.
 * 
 * The system enforces that:
 * - Only items with allowed provenance sources count towards collections
 * - Items from trade (AH/Bazaar) are explicitly excluded
 * - Collection progress is tracked per item provenance
 * - Violations are logged and prevented
 */
public class CollectionConstraintSystem implements Service {
    
    private final ItemProvenanceSystem provenanceSystem;
    private final HypixelCollectionsSystem collectionsSystem;
    
    private final Map<UUID, Map<CollectionType, Map<CollectionItem, CollectionItemProgress>>> playerCollectionProgress;
    private final Map<UUID, List<CollectionViolation>> playerViolations;
    
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    
    public CollectionConstraintSystem(ItemProvenanceSystem provenanceSystem, 
                                     HypixelCollectionsSystem collectionsSystem) {
        this.provenanceSystem = provenanceSystem;
        this.collectionsSystem = collectionsSystem;
        this.playerCollectionProgress = new ConcurrentHashMap<>();
        this.playerViolations = new ConcurrentHashMap<>();
    }
    
    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.INITIALIZING;
            
            // Initialize constraint system
            initializeConstraintSystem();
            
            status = SystemStatus.ENABLED;
        });
    }
    
    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.SHUTTING_DOWN;
            
            // Save constraint data
            saveConstraintData();
            
            status = SystemStatus.UNINITIALIZED;
        });
    }
    
    @Override
    public boolean isInitialized() {
        return status == SystemStatus.ENABLED;
    }
    
    @Override
    public String getName() {
        return "CollectionConstraintSystem";
    }
    
    /**
     * Process items for collection progression with constraint enforcement
     * 
     * This is the main method that enforces collection constraints.
     * Only items with allowed provenance sources will count towards collections.
     */
    public CompletableFuture<CollectionProcessResult> processItemsForCollection(
            UUID playerId, CollectionType collectionType, CollectionItem item, 
            int amount, ItemStack[] items) {
        
        return CompletableFuture.supplyAsync(() -> {
            List<CollectionItemProgress> eligibleItems = new ArrayList<>();
            List<CollectionViolation> violations = new ArrayList<>();
            int totalEligibleAmount = 0;
            int totalIneligibleAmount = 0;
            
            // Process each item stack
            for (ItemStack itemStack : items) {
                if (itemStack == null || itemStack.getAmount() <= 0) continue;
                
                // Check item provenance
                ItemProvenance provenance = provenanceSystem.getItemProvenance(itemStack).join();
                
                if (provenance == null) {
                    // Item without provenance - create violation
                    CollectionViolation violation = new CollectionViolation(
                        playerId, collectionType, item, itemStack.getAmount(),
                        "Item without provenance data", CollectionViolationType.MISSING_PROVENANCE
                    );
                    violations.add(violation);
                    totalIneligibleAmount += itemStack.getAmount();
                    continue;
                }
                
                // Check if item is eligible for collections
                if (!provenance.isEligibleForCollections()) {
                    // Item from trade source - create violation
                    CollectionViolation violation = new CollectionViolation(
                        playerId, collectionType, item, itemStack.getAmount(),
                        "Item from trade source: " + provenance.getSource().getDisplayName(),
                        CollectionViolationType.TRADE_SOURCE
                    );
                    violations.add(violation);
                    totalIneligibleAmount += itemStack.getAmount();
                    continue;
                }
                
                // Item is eligible - add to collection progress
                CollectionItemProgress progress = new CollectionItemProgress(
                    item, itemStack.getAmount(), provenance, System.currentTimeMillis()
                );
                eligibleItems.add(progress);
                totalEligibleAmount += itemStack.getAmount();
            }
            
            // Update player collection progress
            if (!eligibleItems.isEmpty()) {
                updatePlayerCollectionProgress(playerId, collectionType, item, eligibleItems);
            }
            
            // Log violations
            if (!violations.isEmpty()) {
                logViolations(playerId, violations);
            }
            
            // Create result
            return new CollectionProcessResult(
                totalEligibleAmount, totalIneligibleAmount, 
                eligibleItems, violations
            );
        });
    }
    
    /**
     * Check if items are eligible for collection progression
     */
    public CompletableFuture<CollectionEligibilityResult> checkCollectionEligibility(
            UUID playerId, CollectionType collectionType, CollectionItem item, ItemStack[] items) {
        
        return CompletableFuture.supplyAsync(() -> {
            List<ItemEligibility> itemEligibilities = new ArrayList<>();
            int totalEligibleAmount = 0;
            int totalIneligibleAmount = 0;
            
            for (ItemStack itemStack : items) {
                if (itemStack == null || itemStack.getAmount() <= 0) continue;
                
                ItemProvenance provenance = provenanceSystem.getItemProvenance(itemStack).join();
                boolean isEligible = provenance != null && provenance.isEligibleForCollections();
                
                ItemEligibility eligibility = new ItemEligibility(
                    itemStack, provenance, isEligible, 
                    isEligible ? itemStack.getAmount() : 0,
                    !isEligible ? itemStack.getAmount() : 0
                );
                
                itemEligibilities.add(eligibility);
                
                if (isEligible) {
                    totalEligibleAmount += itemStack.getAmount();
                } else {
                    totalIneligibleAmount += itemStack.getAmount();
                }
            }
            
            return new CollectionEligibilityResult(
                itemEligibilities, totalEligibleAmount, totalIneligibleAmount
            );
        });
    }
    
    /**
     * Get player's collection progress with constraint information
     */
    public CollectionProgressInfo getPlayerCollectionProgress(UUID playerId, CollectionType collectionType) {
        Map<CollectionType, Map<CollectionItem, CollectionItemProgress>> playerProgress = 
            playerCollectionProgress.get(playerId);
        
        if (playerProgress == null || playerProgress.get(collectionType) == null) {
            return new CollectionProgressInfo(playerId, collectionType, new HashMap<>(), 0, 0);
        }
        
        Map<CollectionItem, CollectionItemProgress> collectionProgress = playerProgress.get(collectionType);
        
        int totalEligibleAmount = collectionProgress.values().stream()
            .mapToInt(CollectionItemProgress::getAmount)
            .sum();
        
        int totalIneligibleAmount = playerViolations.getOrDefault(playerId, new ArrayList<>())
            .stream()
            .filter(violation -> violation.getCollectionType() == collectionType)
            .mapToInt(CollectionViolation::getAmount)
            .sum();
        
        return new CollectionProgressInfo(
            playerId, collectionType, collectionProgress, 
            totalEligibleAmount, totalIneligibleAmount
        );
    }
    
    /**
     * Get player's collection violations
     */
    public List<CollectionViolation> getPlayerViolations(UUID playerId) {
        return new ArrayList<>(playerViolations.getOrDefault(playerId, new ArrayList<>()));
    }
    
    /**
     * Get violations for specific collection type
     */
    public List<CollectionViolation> getPlayerViolations(UUID playerId, CollectionType collectionType) {
        return playerViolations.getOrDefault(playerId, new ArrayList<>())
            .stream()
            .filter(violation -> violation.getCollectionType() == collectionType)
            .toList();
    }
    
    /**
     * Get collection statistics with constraint information
     */
    public CollectionConstraintStatistics getCollectionStatistics(UUID playerId) {
        Map<CollectionType, CollectionProgressInfo> progressInfo = new HashMap<>();
        
        for (CollectionType collectionType : CollectionType.values()) {
            progressInfo.put(collectionType, getPlayerCollectionProgress(playerId, collectionType));
        }
        
        List<CollectionViolation> allViolations = getPlayerViolations(playerId);
        
        return new CollectionConstraintStatistics(playerId, progressInfo, allViolations);
    }
    
    /**
     * Update player collection progress
     */
    private void updatePlayerCollectionProgress(UUID playerId, CollectionType collectionType, 
                                               CollectionItem item, List<CollectionItemProgress> progressItems) {
        
        playerCollectionProgress.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>())
            .computeIfAbsent(collectionType, k -> new ConcurrentHashMap<>())
            .computeIfAbsent(item, k -> new CollectionItemProgress(item, 0, null, System.currentTimeMillis()))
            .addProgress(progressItems);
    }
    
    /**
     * Log collection violations
     */
    private void logViolations(UUID playerId, List<CollectionViolation> violations) {
        playerViolations.computeIfAbsent(playerId, k -> new ArrayList<>()).addAll(violations);
        
        // Keep only last 100 violations per player
        List<CollectionViolation> playerViolationList = playerViolations.get(playerId);
        if (playerViolationList.size() > 100) {
            playerViolationList.subList(0, playerViolationList.size() - 100).clear();
        }
        
        // Log violations for monitoring
        for (CollectionViolation violation : violations) {
            System.out.println("Collection Violation: " + violation);
        }
    }
    
    /**
     * Initialize constraint system
     */
    private void initializeConstraintSystem() {
        System.out.println("Collection Constraint System initialized - Economic protection active");
    }
    
    /**
     * Save constraint data to persistent storage
     */
    private void saveConstraintData() {
        // TODO: Implement database persistence for constraint data
        // This will integrate with the existing database system
    }
    
    /**
     * Validate collection integrity
     */
    public CompletableFuture<Boolean> validateCollectionIntegrity(UUID playerId, CollectionType collectionType) {
        return CompletableFuture.supplyAsync(() -> {
            CollectionProgressInfo progressInfo = getPlayerCollectionProgress(playerId, collectionType);
            List<CollectionViolation> violations = getPlayerViolations(playerId, collectionType);
            
            // Check for suspicious patterns
            int totalEligible = progressInfo.getTotalEligibleAmount();
            int totalIneligible = progressInfo.getTotalIneligibleAmount();
            
            // If more than 50% of items are ineligible, flag for review
            double ineligiblePercentage = (double) totalIneligible / (totalEligible + totalIneligible) * 100.0;
            
            if (ineligiblePercentage > 50.0) {
                System.out.println("Warning: High ineligible percentage for player " + playerId + 
                    " in " + collectionType + ": " + ineligiblePercentage + "%");
                return false;
            }
            
            return true;
        });
    }
}
