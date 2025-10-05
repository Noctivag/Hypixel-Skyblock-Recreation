package de.noctivag.skyblock.features.npcs;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.npcs.types.CompleteNPCType;
import de.noctivag.skyblock.features.npcs.types.NPCCategory;
import de.noctivag.skyblock.features.npcs.types.NPCRarity;
import de.noctivag.skyblock.features.npcs.quests.Quest;
import de.noctivag.skyblock.features.npcs.quests.QuestStatus;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.*;

public class PlayerNPCs {
    private final UUID playerId;
    private final Map<CompleteNPCType, Boolean> discoveredNPCs;
    private final Map<CompleteNPCType, LocalDateTime> firstInteractionTime;
    private final Map<CompleteNPCType, LocalDateTime> lastInteractionTime;
    private final Map<CompleteNPCType, Integer> interactionCount;
    private final Map<CompleteNPCType, Long> timeSpent;
    private final Map<NPCCategory, Integer> categoryProgress;
    private final Map<NPCRarity, Integer> rarityProgress;
    private final Map<Quest, QuestStatus> activeQuests;
    private final Map<Quest, LocalDateTime> completedQuests;
    private final Map<Quest, Integer> questCompletionCount;

    public PlayerNPCs(Player player) {
        this.playerId = player.getUniqueId();
        this.discoveredNPCs = new HashMap<>();
        this.firstInteractionTime = new HashMap<>();
        this.lastInteractionTime = new HashMap<>();
        this.interactionCount = new HashMap<>();
        this.timeSpent = new HashMap<>();
        this.categoryProgress = new HashMap<>();
        this.rarityProgress = new HashMap<>();
        this.activeQuests = new HashMap<>();
        this.completedQuests = new HashMap<>();
        this.questCompletionCount = new HashMap<>();
        // Load existing NPC data from database or file here
    }

    public UUID getPlayerId() {
        return playerId;
    }

    /**
     * Discover a new NPC
     */
    public void discoverNPC(CompleteNPCType npcType) {
        if (!discoveredNPCs.containsKey(npcType)) {
            discoveredNPCs.put(npcType, true);
            firstInteractionTime.put(npcType, LocalDateTime.now());
            lastInteractionTime.put(npcType, LocalDateTime.now());
            interactionCount.put(npcType, 1);
            timeSpent.put(npcType, 0L);
            
            // Update category and rarity progress
            NPCCategory category = npcType.getCategory();
            NPCRarity rarity = npcType.getRarity();
            
            categoryProgress.put(category, categoryProgress.getOrDefault(category, 0) + 1);
            rarityProgress.put(rarity, rarityProgress.getOrDefault(rarity, 0) + 1);
        }
    }

    /**
     * Interact with an NPC
     */
    public void interactWithNPC(CompleteNPCType npcType) {
        if (!discoveredNPCs.containsKey(npcType)) {
            discoverNPC(npcType);
        } else {
            lastInteractionTime.put(npcType, LocalDateTime.now());
            interactionCount.put(npcType, interactionCount.getOrDefault(npcType, 0) + 1);
        }
    }

    /**
     * Record time spent with an NPC
     */
    public void recordTimeSpent(CompleteNPCType npcType, long timeSpent) {
        if (discoveredNPCs.containsKey(npcType)) {
            this.timeSpent.put(npcType, this.timeSpent.getOrDefault(npcType, 0L) + timeSpent);
        }
    }

    /**
     * Start a quest
     */
    public void startQuest(Quest quest) {
        activeQuests.put(quest, QuestStatus.ACTIVE);
    }

    /**
     * Complete a quest
     */
    public void completeQuest(Quest quest) {
        activeQuests.remove(quest);
        completedQuests.put(quest, LocalDateTime.now());
        questCompletionCount.put(quest, questCompletionCount.getOrDefault(quest, 0) + 1);
    }

    /**
     * Fail a quest
     */
    public void failQuest(Quest quest) {
        activeQuests.put(quest, QuestStatus.FAILED);
    }

    /**
     * Abandon a quest
     */
    public void abandonQuest(Quest quest) {
        activeQuests.remove(quest);
    }

    /**
     * Check if an NPC has been discovered
     */
    public boolean hasDiscoveredNPC(CompleteNPCType npcType) {
        return discoveredNPCs.getOrDefault(npcType, false);
    }

    /**
     * Get first interaction time for an NPC
     */
    public LocalDateTime getFirstInteractionTime(CompleteNPCType npcType) {
        return firstInteractionTime.get(npcType);
    }

    /**
     * Get last interaction time for an NPC
     */
    public LocalDateTime getLastInteractionTime(CompleteNPCType npcType) {
        return lastInteractionTime.get(npcType);
    }

    /**
     * Get interaction count for an NPC
     */
    public int getInteractionCount(CompleteNPCType npcType) {
        return interactionCount.getOrDefault(npcType, 0);
    }

    /**
     * Get time spent with an NPC
     */
    public long getTimeSpent(CompleteNPCType npcType) {
        return timeSpent.getOrDefault(npcType, 0L);
    }

    /**
     * Get active quests
     */
    public Map<Quest, QuestStatus> getActiveQuests() {
        return new HashMap<>(activeQuests);
    }

    /**
     * Get completed quests
     */
    public Map<Quest, LocalDateTime> getCompletedQuests() {
        return new HashMap<>(completedQuests);
    }

    /**
     * Get quest completion count
     */
    public Map<Quest, Integer> getQuestCompletionCount() {
        return new HashMap<>(questCompletionCount);
    }

    /**
     * Get all discovered NPCs
     */
    public Map<CompleteNPCType, Boolean> getAllDiscoveredNPCs() {
        return new HashMap<>(discoveredNPCs);
    }

    /**
     * Get all interaction times
     */
    public Map<CompleteNPCType, LocalDateTime> getAllFirstInteractionTimes() {
        return new HashMap<>(firstInteractionTime);
    }

    /**
     * Get all last interaction times
     */
    public Map<CompleteNPCType, LocalDateTime> getAllLastInteractionTimes() {
        return new HashMap<>(lastInteractionTime);
    }

    /**
     * Get all interaction counts
     */
    public Map<CompleteNPCType, Integer> getAllInteractionCounts() {
        return new HashMap<>(interactionCount);
    }

    /**
     * Get all time spent
     */
    public Map<CompleteNPCType, Long> getAllTimeSpent() {
        return new HashMap<>(timeSpent);
    }

    /**
     * Get total discovered NPCs
     */
    public int getTotalDiscoveredNPCs() {
        return discoveredNPCs.size();
    }

    /**
     * Get total interaction count
     */
    public int getTotalInteractionCount() {
        return interactionCount.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Get total time spent
     */
    public long getTotalTimeSpent() {
        return timeSpent.values().stream().mapToLong(Long::longValue).sum();
    }

    /**
     * Get total active quests
     */
    public int getTotalActiveQuests() {
        return activeQuests.size();
    }

    /**
     * Get total completed quests
     */
    public int getTotalCompletedQuests() {
        return completedQuests.size();
    }

    /**
     * Get discovered NPCs by category
     */
    public Map<CompleteNPCType, Boolean> getDiscoveredNPCsByCategory(NPCCategory category) {
        Map<CompleteNPCType, Boolean> result = new HashMap<>();
        for (Map.Entry<CompleteNPCType, Boolean> entry : discoveredNPCs.entrySet()) {
            if (entry.getKey().getCategory() == category) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    /**
     * Get discovered NPCs by rarity
     */
    public Map<CompleteNPCType, Boolean> getDiscoveredNPCsByRarity(NPCRarity rarity) {
        Map<CompleteNPCType, Boolean> result = new HashMap<>();
        for (Map.Entry<CompleteNPCType, Boolean> entry : discoveredNPCs.entrySet()) {
            if (entry.getKey().getRarity() == rarity) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    /**
     * Get category progress
     */
    public int getCategoryProgress(NPCCategory category) {
        return categoryProgress.getOrDefault(category, 0);
    }

    /**
     * Get rarity progress
     */
    public int getRarityProgress(NPCRarity rarity) {
        return rarityProgress.getOrDefault(rarity, 0);
    }

    /**
     * Get completion percentage for a category
     */
    public double getCategoryCompletionPercentage(NPCCategory category) {
        int discoveredInCategory = getCategoryProgress(category);
        int totalInCategory = CompleteNPCType.getNPCCountByCategory(category);
        
        if (totalInCategory == 0) {
            return 0.0;
        }
        
        return (double) discoveredInCategory / totalInCategory * 100.0;
    }

    /**
     * Get completion percentage for a rarity
     */
    public double getRarityCompletionPercentage(NPCRarity rarity) {
        int discoveredOfRarity = getRarityProgress(rarity);
        int totalOfRarity = CompleteNPCType.getNPCCountByRarity(rarity);
        
        if (totalOfRarity == 0) {
            return 0.0;
        }
        
        return (double) discoveredOfRarity / totalOfRarity * 100.0;
    }

    /**
     * Get overall completion percentage
     */
    public double getOverallCompletionPercentage() {
        int totalNPCs = CompleteNPCType.getTotalNPCCount();
        if (totalNPCs == 0) {
            return 0.0;
        }
        return (double) getTotalDiscoveredNPCs() / totalNPCs * 100.0;
    }

    /**
     * Get most interacted NPC
     */
    public CompleteNPCType getMostInteractedNPC() {
        return interactionCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Get NPC with most time spent
     */
    public CompleteNPCType getNPCWithMostTimeSpent() {
        return timeSpent.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Get recently discovered NPCs (last 24 hours)
     */
    public Map<CompleteNPCType, LocalDateTime> getRecentlyDiscoveredNPCs() {
        Map<CompleteNPCType, LocalDateTime> recent = new HashMap<>();
        LocalDateTime cutoff = LocalDateTime.now().minusDays(1);
        
        for (Map.Entry<CompleteNPCType, LocalDateTime> entry : firstInteractionTime.entrySet()) {
            if (entry.getValue().isAfter(cutoff)) {
                recent.put(entry.getKey(), entry.getValue());
            }
        }
        
        return recent;
    }

    /**
     * Get NPCs by interaction frequency
     */
    public Map<CompleteNPCType, Integer> getNPCsByInteractionFrequency() {
        return new HashMap<>(interactionCount);
    }

    /**
     * Get NPCs by time spent
     */
    public Map<CompleteNPCType, Long> getNPCsByTimeSpent() {
        return new HashMap<>(timeSpent);
    }

    /**
     * Get interaction statistics
     */
    public String getInteractionStatistics() {
        return String.format(
                "Discovered: %d/%d (%.1f%%) | Interactions: %d | Time: %d min | Quests: %d active, %d completed",
                getTotalDiscoveredNPCs(),
                CompleteNPCType.getTotalNPCCount(),
                getOverallCompletionPercentage(),
                getTotalInteractionCount(),
                getTotalTimeSpent() / 60000, // Convert to minutes
                getTotalActiveQuests(),
                getTotalCompletedQuests()
        );
    }

    /**
     * Get category statistics
     */
    public Map<NPCCategory, String> getCategoryStatistics() {
        Map<NPCCategory, String> stats = new HashMap<>();
        
        for (NPCCategory category : NPCCategory.values()) {
            int discovered = getCategoryProgress(category);
            int total = CompleteNPCType.getNPCCountByCategory(category);
            double percentage = getCategoryCompletionPercentage(category);
            
            stats.put(category, String.format("%d/%d (%.1f%%)", discovered, total, percentage));
        }
        
        return stats;
    }

    /**
     * Get rarity statistics
     */
    public Map<NPCRarity, String> getRarityStatistics() {
        Map<NPCRarity, String> stats = new HashMap<>();
        
        for (NPCRarity rarity : NPCRarity.values()) {
            int discovered = getRarityProgress(rarity);
            int total = CompleteNPCType.getNPCCountByRarity(rarity);
            double percentage = getRarityCompletionPercentage(rarity);
            
            stats.put(rarity, String.format("%d/%d (%.1f%%)", discovered, total, percentage));
        }
        
        return stats;
    }

    /**
     * Get quest statistics
     */
    public String getQuestStatistics() {
        return String.format(
                "Active: %d | Completed: %d | Total: %d",
                getTotalActiveQuests(),
                getTotalCompletedQuests(),
                getTotalActiveQuests() + getTotalCompletedQuests()
        );
    }
}
