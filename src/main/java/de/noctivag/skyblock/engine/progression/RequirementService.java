package de.noctivag.skyblock.engine.progression;
import java.util.UUID;

import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.engine.progression.types.HypixelSkillType;
import de.noctivag.skyblock.engine.progression.types.SkillRequirement;
import de.noctivag.skyblock.engine.progression.types.ContentRequirement;
import de.noctivag.skyblock.engine.progression.types.QuestRequirement;
import de.noctivag.skyblock.engine.progression.types.ContentType;
import de.noctivag.skyblock.engine.progression.types.RequirementResult;
import de.noctivag.skyblock.engine.progression.types.RequirementFailure;
import de.noctivag.skyblock.engine.progression.types.RequirementProgress;
import de.noctivag.skyblock.engine.progression.types.RequirementProgressItem;
import de.noctivag.skyblock.engine.progression.types.RequirementType;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Requirement Service - Progression Phasenmodell Implementation
 * 
 * Central service for checking if players meet requirements for accessing
 * specific content, dungeons, zones, bosses, and other progression gates.
 * 
 * This service enforces the "Grind" as a core design element by implementing
 * strict time-gated progression requirements.
 */
public class RequirementService implements Service {
    
    private final HypixelSkillSystem skillSystem;
    private final Map<ContentType, List<ContentRequirement>> contentRequirements;
    private final Map<String, List<QuestRequirement>> questRequirements;
    
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    
    public RequirementService(HypixelSkillSystem skillSystem) {
        this.skillSystem = skillSystem;
        this.contentRequirements = new ConcurrentHashMap<>();
        this.questRequirements = new ConcurrentHashMap<>();
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        
        // Initialize content requirements
        initializeContentRequirements();
        
        // Initialize quest requirements
        initializeQuestRequirements();
        
        status = SystemStatus.RUNNING;
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        
        // Clear requirements
        contentRequirements.clear();
        questRequirements.clear();
        
        status = SystemStatus.DISABLED;
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status == SystemStatus.DISABLED) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }
    
    @Override
    public String getName() {
        return "RequirementService";
    }
    
    /**
     * Check if a player meets all requirements for specific content
     */
    public CompletableFuture<RequirementResult> checkRequirements(UUID playerId, ContentType contentType, String contentId) {
        return CompletableFuture.supplyAsync(() -> {
            List<ContentRequirement> requirements = contentRequirements.get(contentType);
            if (requirements == null || requirements.isEmpty()) {
                return new RequirementResult(true, "No requirements found for " + contentType, new ArrayList<>());
            }
            
            List<RequirementFailure> failures = new ArrayList<>();
            
            for (ContentRequirement requirement : requirements) {
                if (requirement.getContentId() != null && !requirement.getContentId().equals(contentId)) {
                    continue; // Skip requirements not for this specific content
                }
                
                if (!meetsRequirement(playerId, requirement)) {
                    failures.add(new RequirementFailure(
                        requirement.getType(),
                        requirement.getDescription(),
                        requirement.getCurrentValue(playerId),
                        requirement.getRequiredValue()
                    ));
                }
            }
            
            boolean meetsAllRequirements = failures.isEmpty();
            String message = meetsAllRequirements ? 
                "All requirements met for " + contentType + " (" + contentId + ")" :
                "Requirements not met for " + contentType + " (" + contentId + ")";
                
            return new RequirementResult(meetsAllRequirements, message, failures);
        });
    }
    
    /**
     * Check if a player meets a specific requirement
     */
    private boolean meetsRequirement(UUID playerId, ContentRequirement requirement) {
        switch (requirement.getType()) {
            case SKILL_LEVEL -> {
                SkillRequirement skillReq = (SkillRequirement) requirement;
                int playerLevel = 0; // TODO: Fix skillSystem.getPlayerSkillLevel(playerId, skillReq.getSkillType());
                return playerLevel >= skillReq.getRequiredLevel();
            }
            case QUEST_COMPLETION -> {
                QuestRequirement questReq = (QuestRequirement) requirement;
                return isQuestCompleted(playerId, questReq.getQuestId());
            }
            case COMBINED_REQUIREMENTS -> {
                List<ContentRequirement> subRequirements = requirement.getSubRequirements();
                return subRequirements.stream().allMatch(subReq -> meetsRequirement(playerId, subReq));
            }
            case MINIMUM_COINS -> {
                // TODO: Integrate with economy system
                return true; // Placeholder
            }
            case COLLECTION_LEVEL -> {
                // TODO: Integrate with collections system
                return true; // Placeholder
            }
            case PLAYER_LEVEL -> {
                // TODO: Integrate with overall player level system
                return true; // Placeholder
            }
            default -> {
                return false; // Unknown requirement type
            }
        }
    }
    
    /**
     * Check if a quest is completed by a player
     */
    private boolean isQuestCompleted(UUID playerId, String questId) {
        // TODO: Integrate with quest system
        return false; // Placeholder
    }
    
    /**
     * Get all requirements for a specific content type
     */
    public List<ContentRequirement> getRequirements(ContentType contentType) {
        return new ArrayList<>(contentRequirements.getOrDefault(contentType, new ArrayList<>()));
    }
    
    /**
     * Get requirements for specific content
     */
    public List<ContentRequirement> getRequirements(ContentType contentType, String contentId) {
        return contentRequirements.getOrDefault(contentType, new ArrayList<>())
            .stream()
            .filter(req -> req.getContentId() == null || req.getContentId().equals(contentId))
            .toList();
    }
    
    /**
     * Add a new content requirement
     */
    public void addRequirement(ContentType contentType, ContentRequirement requirement) {
        contentRequirements.computeIfAbsent(contentType, k -> new ArrayList<>()).add(requirement);
    }
    
    /**
     * Remove a content requirement
     */
    public boolean removeRequirement(ContentType contentType, ContentRequirement requirement) {
        List<ContentRequirement> requirements = contentRequirements.get(contentType);
        return requirements != null && requirements.remove(requirement);
    }
    
    /**
     * Get player's progress towards requirements
     */
    public CompletableFuture<RequirementProgress> getRequirementProgress(UUID playerId, ContentType contentType, String contentId) {
        return CompletableFuture.supplyAsync(() -> {
            List<ContentRequirement> requirements = getRequirements(contentType, contentId);
            List<RequirementProgressItem> progressItems = new ArrayList<>();
            
            for (ContentRequirement requirement : requirements) {
                progressItems.add(new RequirementProgressItem(
                    requirement.getDescription(),
                    requirement.getCurrentValue(playerId),
                    requirement.getRequiredValue(),
                    meetsRequirement(playerId, requirement)
                ));
            }
            
            return new RequirementProgress(contentType, contentId, progressItems);
        });
    }
    
    /**
     * Initialize content requirements for all game content
     */
    private void initializeContentRequirements() {
        // Dungeon Requirements
        initializeDungeonRequirements();
        
        // Zone Requirements
        initializeZoneRequirements();
        
        // Boss Requirements
        initializeBossRequirements();
        
        // Island Requirements
        initializeIslandRequirements();
        
        // Minion Requirements
        initializeMinionRequirements();
    }
    
    /**
     * Initialize dungeon access requirements
     */
    private void initializeDungeonRequirements() {
        // Catacombs Entry Requirements
        addRequirement(ContentType.DUNGEON, new SkillRequirement(
            "catacombs_entry",
            "Catacombs Entry",
            "Minimum Combat Level 15 required",
            HypixelSkillType.COMBAT,
            15
        ));
        
        // Floor 1 Requirements
        addRequirement(ContentType.DUNGEON, new SkillRequirement(
            "catacombs_f1",
            "Catacombs Floor 1",
            "Minimum Combat Level 18 required",
            HypixelSkillType.COMBAT,
            18
        ));
        
        // Floor 2 Requirements
        addRequirement(ContentType.DUNGEON, new SkillRequirement(
            "catacombs_f2",
            "Catacombs Floor 2",
            "Minimum Combat Level 22 required",
            HypixelSkillType.COMBAT,
            22
        ));
        
        // Floor 3 Requirements
        addRequirement(ContentType.DUNGEON, new SkillRequirement(
            "catacombs_f3",
            "Catacombs Floor 3",
            "Minimum Combat Level 26 required",
            HypixelSkillType.COMBAT,
            26
        ));
        
        // Floor 4 Requirements
        addRequirement(ContentType.DUNGEON, new SkillRequirement(
            "catacombs_f4",
            "Catacombs Floor 4",
            "Minimum Combat Level 30 required",
            HypixelSkillType.COMBAT,
            30
        ));
        
        // Floor 5 Requirements
        addRequirement(ContentType.DUNGEON, new SkillRequirement(
            "catacombs_f5",
            "Catacombs Floor 5",
            "Minimum Combat Level 35 required",
            HypixelSkillType.COMBAT,
            35
        ));
        
        // Floor 6 Requirements
        addRequirement(ContentType.DUNGEON, new SkillRequirement(
            "catacombs_f6",
            "Catacombs Floor 6",
            "Minimum Combat Level 40 required",
            HypixelSkillType.COMBAT,
            40
        ));
        
        // Floor 7 Requirements (Master Mode)
        addRequirement(ContentType.DUNGEON, new SkillRequirement(
            "catacombs_f7",
            "Catacombs Floor 7 (Master)",
            "Minimum Combat Level 45 required",
            HypixelSkillType.COMBAT,
            45
        ));
    }
    
    /**
     * Initialize zone access requirements
     */
    private void initializeZoneRequirements() {
        // Deep Caverns Entry
        addRequirement(ContentType.ZONE, new SkillRequirement(
            "deep_caverns_entry",
            "Deep Caverns Entry",
            "Minimum Mining Level 12 required",
            HypixelSkillType.MINING,
            12
        ));
        
        // Blazing Fortress Entry
        addRequirement(ContentType.ZONE, new SkillRequirement(
            "blazing_fortress_entry",
            "Blazing Fortress Entry",
            "Minimum Combat Level 20 required",
            HypixelSkillType.COMBAT,
            20
        ));
        
        // The End Entry
        addRequirement(ContentType.ZONE, new SkillRequirement(
            "the_end_entry",
            "The End Entry",
            "Minimum Combat Level 25 required",
            HypixelSkillType.COMBAT,
            25
        ));
        
        // Crimson Isle Entry
        addRequirement(ContentType.ZONE, new SkillRequirement(
            "crimson_isle_entry",
            "Crimson Isle Entry",
            "Minimum Combat Level 35 required",
            HypixelSkillType.COMBAT,
            35
        ));
        
        // Crystal Hollows Entry
        addRequirement(ContentType.ZONE, new SkillRequirement(
            "crystal_hollows_entry",
            "Crystal Hollows Entry",
            "Minimum Mining Level 30 required",
            HypixelSkillType.MINING,
            30
        ));
    }
    
    /**
     * Initialize boss access requirements
     */
    private void initializeBossRequirements() {
        // Dragon Fight Requirements
        addRequirement(ContentType.BOSS, new SkillRequirement(
            "dragon_fight",
            "Dragon Fight",
            "Minimum Combat Level 18 required",
            HypixelSkillType.COMBAT,
            18
        ));
        
        // Slayer Boss Requirements
        addRequirement(ContentType.BOSS, new SkillRequirement(
            "zombie_slayer_t1",
            "Zombie Slayer Tier 1",
            "Minimum Combat Level 12 required",
            HypixelSkillType.COMBAT,
            12
        ));
        
        addRequirement(ContentType.BOSS, new SkillRequirement(
            "spider_slayer_t1",
            "Spider Slayer Tier 1",
            "Minimum Combat Level 14 required",
            HypixelSkillType.COMBAT,
            14
        ));
        
        addRequirement(ContentType.BOSS, new SkillRequirement(
            "wolf_slayer_t1",
            "Wolf Slayer Tier 1",
            "Minimum Combat Level 16 required",
            HypixelSkillType.COMBAT,
            16
        ));
        
        // Higher Tier Slayer Requirements
        addRequirement(ContentType.BOSS, new SkillRequirement(
            "zombie_slayer_t4",
            "Zombie Slayer Tier 4",
            "Minimum Combat Level 25 required",
            HypixelSkillType.COMBAT,
            25
        ));
        
        addRequirement(ContentType.BOSS, new SkillRequirement(
            "spider_slayer_t4",
            "Spider Slayer Tier 4",
            "Minimum Combat Level 27 required",
            HypixelSkillType.COMBAT,
            27
        ));
        
        addRequirement(ContentType.BOSS, new SkillRequirement(
            "wolf_slayer_t4",
            "Wolf Slayer Tier 4",
            "Minimum Combat Level 29 required",
            HypixelSkillType.COMBAT,
            29
        ));
    }
    
    /**
     * Initialize island access requirements
     */
    private void initializeIslandRequirements() {
        // Private Island Expansion Requirements
        addRequirement(ContentType.ISLAND, new SkillRequirement(
            "island_expansion_1",
            "Island Expansion 1",
            "Minimum Carpentry Level 5 required",
            HypixelSkillType.CARPENTRY,
            5
        ));
        
        addRequirement(ContentType.ISLAND, new SkillRequirement(
            "island_expansion_2",
            "Island Expansion 2",
            "Minimum Carpentry Level 10 required",
            HypixelSkillType.CARPENTRY,
            10
        ));
        
        addRequirement(ContentType.ISLAND, new SkillRequirement(
            "island_expansion_3",
            "Island Expansion 3",
            "Minimum Carpentry Level 15 required",
            HypixelSkillType.CARPENTRY,
            15
        ));
    }
    
    /**
     * Initialize minion access requirements
     */
    private void initializeMinionRequirements() {
        // Higher Tier Minion Requirements
        addRequirement(ContentType.MINION, new SkillRequirement(
            "minion_tier_6",
            "Minion Tier 6",
            "Minimum Collection Level 5 required",
            HypixelSkillType.MINING, // Placeholder - should be collection level
            5
        ));
        
        addRequirement(ContentType.MINION, new SkillRequirement(
            "minion_tier_8",
            "Minion Tier 8",
            "Minimum Collection Level 8 required",
            HypixelSkillType.MINING, // Placeholder - should be collection level
            8
        ));
        
        addRequirement(ContentType.MINION, new SkillRequirement(
            "minion_tier_10",
            "Minion Tier 10",
            "Minimum Collection Level 12 required",
            HypixelSkillType.MINING, // Placeholder - should be collection level
            12
        ));
    }
    
    /**
     * Initialize quest requirements
     */
    private void initializeQuestRequirements() {
        // TODO: Implement quest system integration
        // This will be expanded when the quest system is implemented
    }
    
    /**
     * Get all available content for a player based on their requirements
     */
    public CompletableFuture<List<ContentType>> getAvailableContent(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            List<ContentType> availableContent = new ArrayList<>();
            
            for (ContentType contentType : ContentType.values()) {
                List<ContentRequirement> requirements = contentRequirements.get(contentType);
                if (requirements == null || requirements.isEmpty()) {
                    availableContent.add(contentType);
                    continue;
                }
                
                boolean meetsAllRequirements = requirements.stream()
                    .allMatch(req -> meetsRequirement(playerId, req));
                    
                if (meetsAllRequirements) {
                    availableContent.add(contentType);
                }
            }
            
            return availableContent;
        });
    }
}
