package de.noctivag.skyblock.collections;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.collections.CollectionsSystem.CollectionType;
import de.noctivag.skyblock.collections.CollectionsSystem.CollectionReward;
import de.noctivag.skyblock.collections.CollectionsSystem.CollectionRecipe;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player Collections - Individuelle Collection-Daten für Spieler
 * 
 * Verantwortlich für:
 * - Collection Amounts
 * - Collection Rewards
 * - Collection Recipes
 * - Collection Progress
 */
public class PlayerCollections {
    private final UUID playerId;
    private final Map<CollectionType, Integer> collectionAmounts = new ConcurrentHashMap<>();
    private final Map<CollectionType, Set<CollectionReward>> unlockedRewards = new ConcurrentHashMap<>();
    private final Map<CollectionType, Set<CollectionRecipe>> unlockedRecipes = new ConcurrentHashMap<>();
    private final Map<CollectionType, Long> lastCollection = new ConcurrentHashMap<>();
    
    public PlayerCollections(UUID playerId) {
        this.playerId = playerId;
        initializeCollections();
    }
    
    private void initializeCollections() {
        for (CollectionType collectionType : CollectionsSystem.CollectionType.values()) {
            collectionAmounts.put(collectionType, 0);
            unlockedRewards.put(collectionType, new HashSet<>());
            unlockedRecipes.put(collectionType, new HashSet<>());
            lastCollection.put(collectionType, java.lang.System.currentTimeMillis());
        }
    }
    
    public int addToCollection(CollectionType collectionType, int amount) {
        int currentAmount = collectionAmounts.get(collectionType);
        int newAmount = currentAmount + amount;
        collectionAmounts.put(collectionType, newAmount);
        lastCollection.put(collectionType, java.lang.System.currentTimeMillis());
        return newAmount;
    }
    
    public int getCollectionAmount(CollectionType collectionType) {
        return collectionAmounts.getOrDefault(collectionType, 0);
    }
    
    public long getLastCollection(CollectionType collectionType) {
        return lastCollection.getOrDefault(collectionType, 0L);
    }
    
    public void addReward(CollectionType collectionType, CollectionReward reward) {
        unlockedRewards.get(collectionType).add(reward);
    }
    
    public boolean hasReward(CollectionType collectionType, CollectionReward reward) {
        return unlockedRewards.get(collectionType).contains(reward);
    }
    
    public Set<CollectionReward> getUnlockedRewards(CollectionType collectionType) {
        return new HashSet<>(unlockedRewards.get(collectionType));
    }
    
    public void addRecipe(CollectionType collectionType, CollectionRecipe recipe) {
        unlockedRecipes.get(collectionType).add(recipe);
    }
    
    public boolean hasRecipe(CollectionType collectionType, CollectionRecipe recipe) {
        return unlockedRecipes.get(collectionType).contains(recipe);
    }
    
    public Set<CollectionRecipe> getUnlockedRecipes(CollectionType collectionType) {
        return new HashSet<>(unlockedRecipes.get(collectionType));
    }
    
    public Map<CollectionType, Integer> getAllCollectionAmounts() {
        return new HashMap<>(collectionAmounts);
    }
    
    public Map<CollectionType, Set<CollectionReward>> getAllUnlockedRewards() {
        Map<CollectionType, Set<CollectionReward>> result = new HashMap<>();
        for (Map.Entry<CollectionType, Set<CollectionReward>> entry : unlockedRewards.entrySet()) {
            result.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return result;
    }
    
    public Map<CollectionType, Set<CollectionRecipe>> getAllUnlockedRecipes() {
        Map<CollectionType, Set<CollectionRecipe>> result = new HashMap<>();
        for (Map.Entry<CollectionType, Set<CollectionRecipe>> entry : unlockedRecipes.entrySet()) {
            result.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return result;
    }
    
    public int getTotalCollections() {
        return collectionAmounts.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    public CollectionType getHighestCollection() {
        return collectionAmounts.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(CollectionsSystem.CollectionType.FARMING);
    }
    
    public CollectionType getLowestCollection() {
        return collectionAmounts.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(CollectionsSystem.CollectionType.FARMING);
    }
    
    public List<CollectionType> getCollectionsByAmount() {
        List<CollectionType> collections = new ArrayList<>(Arrays.asList(CollectionsSystem.CollectionType.values()));
        collections.sort((a, b) -> Integer.compare(getCollectionAmount(b), getCollectionAmount(a)));
        return collections;
    }
    
    public int getUnlockedRewardsCount(CollectionType collectionType) {
        return unlockedRewards.get(collectionType).size();
    }
    
    public int getUnlockedRecipesCount(CollectionType collectionType) {
        return unlockedRecipes.get(collectionType).size();
    }
    
    public int getTotalUnlockedRewards() {
        return unlockedRewards.values().stream().mapToInt(Set::size).sum();
    }
    
    public int getTotalUnlockedRecipes() {
        return unlockedRecipes.values().stream().mapToInt(Set::size).sum();
    }
    
    public void resetCollection(CollectionType collectionType) {
        collectionAmounts.put(collectionType, 0);
        unlockedRewards.get(collectionType).clear();
        unlockedRecipes.get(collectionType).clear();
        lastCollection.put(collectionType, java.lang.System.currentTimeMillis());
    }
    
    public void resetAllCollections() {
        for (CollectionType collectionType : CollectionsSystem.CollectionType.values()) {
            resetCollection(collectionType);
        }
    }
    
    public String getCollectionSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("§6§lCollection Summary:\n");
        
        for (CollectionType collectionType : getCollectionsByAmount()) {
            int amount = getCollectionAmount(collectionType);
            int rewards = getUnlockedRewardsCount(collectionType);
            int recipes = getUnlockedRecipesCount(collectionType);
            
            summary.append(collectionType.getDisplayName()).append(" §7Amount: §e").append(amount)
                   .append(" §7Rewards: §a").append(rewards)
                   .append(" §7Recipes: §b").append(recipes).append("\n");
        }
        
        return summary.toString();
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    @Override
    public String toString() {
        return "PlayerCollections{" +
                "playerId=" + playerId +
                ", totalCollections=" + getTotalCollections() +
                ", highestCollection=" + getHighestCollection() +
                ", totalRewards=" + getTotalUnlockedRewards() +
                ", totalRecipes=" + getTotalUnlockedRecipes() +
                '}';
    }
}
