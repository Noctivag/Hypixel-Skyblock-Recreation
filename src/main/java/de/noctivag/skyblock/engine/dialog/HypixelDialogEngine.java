package de.noctivag.skyblock.engine.dialog;
import java.util.UUID;

import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.engine.dialog.types.DialogNode;
import de.noctivag.skyblock.engine.dialog.types.DialogOption;
import de.noctivag.skyblock.engine.dialog.types.DialogCondition;
import de.noctivag.skyblock.engine.dialog.types.DialogAction;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hypixel Dialog Engine
 * 
 * Implements a flexible, tree-structured dialog system for NPC interactions.
 * Supports complex quest lines, conditional responses, and action execution.
 * Used for NPCs like Maddox the Slayer and other quest-givers.
 */
public class HypixelDialogEngine implements Service {
    
    private final Map<String, DialogNode> dialogNodes = new ConcurrentHashMap<>();
    private final Map<UUID, DialogSession> activeSessions = new ConcurrentHashMap<>();
    private final Map<String, List<DialogNode>> npcDialogs = new ConcurrentHashMap<>();
    
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    
    public HypixelDialogEngine() {
        initializeDialogs();
    }
    
    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.INITIALIZING;
            
            // Initialize all dialog trees
            initializeDialogs();
            
            // Load dialog progress from database
            loadDialogProgress();
            
            status = SystemStatus.ENABLED;
        });
    }
    
    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.SHUTTING_DOWN;
            
            // Save dialog progress to database
            saveDialogProgress();
            
            status = SystemStatus.UNINITIALIZED;
        });
    }
    
    @Override
    public boolean isInitialized() {
        return status == SystemStatus.ENABLED;
    }
    
    @Override
    public String getName() {
        return "HypixelDialogEngine";
    }
    
    /**
     * Start a dialog session with an NPC
     */
    public CompletableFuture<DialogSession> startDialog(UUID playerId, String npcId) {
        return CompletableFuture.supplyAsync(() -> {
            List<DialogNode> npcDialogNodes = npcDialogs.get(npcId);
            if (npcDialogNodes == null || npcDialogNodes.isEmpty()) {
                return null; // No dialog available for this NPC
            }
            
            // Find the starting dialog node
            DialogNode startNode = findStartNode(npcDialogNodes, playerId);
            if (startNode == null) {
                return null; // No valid start node found
            }
            
            // Create dialog session
            DialogSession session = new DialogSession(UUID.randomUUID(), playerId, npcId);
            activeSessions.put(playerId, session);
            
            return session;
        });
    }
    
    /**
     * Continue dialog with next node
     */
    public CompletableFuture<DialogNode> continueDialog(UUID playerId, String optionId) {
        return CompletableFuture.supplyAsync(() -> {
            DialogSession session = activeSessions.get(playerId);
            if (session == null) {
                return null; // No active session
            }
            
            // Find the selected option
            DialogOption selectedOption = session.getCurrentNode().getOption(optionId);
            if (selectedOption == null) {
                return null; // Invalid option
            }
            
            // Check if option is available
            if (!selectedOption.isAvailable(playerId)) {
                return null; // Option not available
            }
            
            // Execute option actions
            selectedOption.executeActions(playerId);
            
            // Move to next node
            DialogNode nextNode = selectedOption.getNextNode();
            if (nextNode == null) {
                // End dialog
                endDialog(playerId);
                return null;
            }
            
            // Update session
            session.setCurrentNode(nextNode);
            
            return nextNode;
        });
    }
    
    /**
     * End dialog session
     */
    public void endDialog(UUID playerId) {
        DialogSession session = activeSessions.remove(playerId);
        if (session != null) {
            // Execute end actions if any
            session.getCurrentNode().getEndActions().forEach(action -> action.execute(playerId));
        }
    }
    
    /**
     * Get active dialog session for a player
     */
    public DialogSession getActiveSession(UUID playerId) {
        return activeSessions.get(playerId);
    }
    
    /**
     * Check if a player has an active dialog session
     */
    public boolean hasActiveSession(UUID playerId) {
        return activeSessions.containsKey(playerId);
    }
    
    /**
     * Get dialog node by ID
     */
    public DialogNode getDialogNode(String nodeId) {
        return dialogNodes.get(nodeId);
    }
    
    /**
     * Get all dialog nodes for an NPC
     */
    public List<DialogNode> getNPCDialogs(String npcId) {
        return new ArrayList<>(npcDialogs.getOrDefault(npcId, new ArrayList<>()));
    }
    
    /**
     * Add a dialog node
     */
    public void addDialogNode(DialogNode node) {
        dialogNodes.put(node.getNodeId(), node);
        
        // Add to NPC dialogs if it has an NPC ID
        if (node.getNpcId() != null) {
            npcDialogs.computeIfAbsent(node.getNpcId(), k -> new ArrayList<>()).add(node);
        }
    }
    
    /**
     * Find the starting dialog node for an NPC
     */
    private DialogNode findStartNode(List<DialogNode> npcDialogNodes, UUID playerId) {
        // Look for a node marked as start node
        for (DialogNode node : npcDialogNodes) {
            if (node.isStartNode() && node.isAvailable(playerId)) {
                return node;
            }
        }
        
        // If no start node found, return the first available node
        for (DialogNode node : npcDialogNodes) {
            if (node.isAvailable(playerId)) {
                return node;
            }
        }
        
        return null;
    }
    
    /**
     * Initialize all dialog trees
     */
    private void initializeDialogs() {
        // Initialize Maddox the Slayer dialogs
        initializeMaddoxDialogs();
        
        // Initialize other NPC dialogs
        initializeOtherNPCDialogs();
    }
    
    /**
     * Initialize Maddox the Slayer dialogs
     */
    private void initializeMaddoxDialogs() {
        String npcId = "maddox_slayer";
        
        // Welcome dialog
        java.util.List<de.noctivag.skyblock.engine.dialog.types.DialogCondition> conditions = Arrays.asList(
                new DialogCondition("player_level", ">=", "5", "You must be at least level 5 to access slayer quests")
            );
        DialogNode welcomeNode = new DialogNode(
            "maddox_welcome",
            npcId,
            "Welcome to the Slayer's Den! I'm Maddox, and I'm here to help you become a true slayer.",
            true, // isStartNode
            conditions
        );
        
        // Add options to welcome dialog
        java.util.List<DialogAction> slayerActions = Arrays.asList(
            new DialogAction("give_item", "slayer_guide", "1", "Give slayer guide")
        );
        welcomeNode.addOption(new DialogOption(
            "slayer_info",
            "Tell me about slayer quests",
            "maddox_slayer_info",
            new ArrayList<>(),
            slayerActions
        ));
        
        welcomeNode.addOption(new DialogOption(
            "start_quest",
            "I want to start a slayer quest",
            "maddox_quest_selection",
            Arrays.asList(
                new DialogCondition("player_level", ">=", "10", "You must be at least level 10 to start slayer quests")
            )
        ));
        
        welcomeNode.addOption(new DialogOption(
            "claim_rewards",
            "I want to claim my rewards",
            "maddox_rewards",
            Arrays.asList(
                new DialogCondition("has_completed_quest", "==", "true", "You must complete a quest first")
            )
        ));
        
        welcomeNode.addOption(new DialogOption(
            "goodbye",
            "Goodbye",
            null, // End dialog
            null
        ));
        
        // Slayer info dialog
        DialogNode slayerInfoNode = new DialogNode(
            "maddox_slayer_info",
            npcId,
            "Slayer quests are special missions that pit you against powerful bosses. " +
            "Each slayer type has different bosses and rewards. Complete quests to earn " +
            "slayer XP, coins, and special items!",
            false,
            null
        );
        
        slayerInfoNode.addOption(new DialogOption(
            "back_to_main",
            "Back to main menu",
            "maddox_welcome",
            null
        ));
        
        // Quest selection dialog
        DialogNode questSelectionNode = new DialogNode(
            "maddox_quest_selection",
            npcId,
            "Choose your slayer quest type:",
            false,
            null
        );
        
        questSelectionNode.addOption(new DialogOption(
            "zombie_slayer",
            "Zombie Slayer",
            "maddox_zombie_quest",
            Arrays.asList(
                new DialogCondition("player_level", ">=", "10", "You must be at least level 10"),
                new DialogCondition("has_zombie_slayer_access", "==", "true", "You don't have access to zombie slayer quests")
            )
        ));
        
        questSelectionNode.addOption(new DialogOption(
            "spider_slayer",
            "Spider Slayer",
            "maddox_spider_quest",
            Arrays.asList(
                new DialogCondition("player_level", ">=", "15", "You must be at least level 15"),
                new DialogCondition("has_spider_slayer_access", "==", "true", "You don't have access to spider slayer quests")
            )
        ));
        
        questSelectionNode.addOption(new DialogOption(
            "wolf_slayer",
            "Wolf Slayer",
            "maddox_wolf_quest",
            Arrays.asList(
                new DialogCondition("player_level", ">=", "20", "You must be at least level 20"),
                new DialogCondition("has_wolf_slayer_access", "==", "true", "You don't have access to wolf slayer quests")
            )
        ));
        
        questSelectionNode.addOption(new DialogOption(
            "enderman_slayer",
            "Enderman Slayer",
            "maddox_enderman_quest",
            Arrays.asList(
                new DialogCondition("player_level", ">=", "25", "You must be at least level 25"),
                new DialogCondition("has_enderman_slayer_access", "==", "true", "You don't have access to enderman slayer quests")
            )
        ));
        
        questSelectionNode.addOption(new DialogOption(
            "blaze_slayer",
            "Blaze Slayer",
            "maddox_blaze_quest",
            Arrays.asList(
                new DialogCondition("player_level", ">=", "30", "You must be at least level 30"),
                new DialogCondition("has_blaze_slayer_access", "==", "true", "You don't have access to blaze slayer quests")
            )
        ));
        
        questSelectionNode.addOption(new DialogOption(
            "back_to_main",
            "Back to main menu",
            "maddox_welcome",
            null
        ));
        
        // Zombie quest dialog
        DialogNode zombieQuestNode = new DialogNode(
            "maddox_zombie_quest",
            npcId,
            "Zombie Slayer Quest: Defeat 100 zombies to summon the Zombie Boss. " +
            "The boss will be much stronger than regular zombies, so prepare well!",
            false,
            null
        );
        
        java.util.List<de.noctivag.skyblock.engine.dialog.types.DialogAction> actions = Arrays.asList(
                new DialogAction("start_quest", "zombie_slayer_1", "1", "Start zombie slayer quest"),
                new DialogAction("give_item", "zombie_slayer_sword", "1", "Give zombie slayer sword")
            );
        zombieQuestNode.addOption(new DialogOption(
            "accept_quest",
            "Accept Quest",
            "maddox_quest_accepted",
            new java.util.ArrayList<>(),
            actions
        ));
        
        zombieQuestNode.addOption(new DialogOption(
            "decline_quest",
            "Maybe later",
            "maddox_quest_selection",
            null
        ));
        
        // Quest accepted dialog
        DialogNode questAcceptedNode = new DialogNode(
            "maddox_quest_accepted",
            npcId,
            "Excellent! Your quest has been accepted. Good luck, slayer!",
            false,
            null
        );
        
        questAcceptedNode.addOption(new DialogOption(
            "back_to_main",
            "Back to main menu",
            "maddox_welcome",
            null
        ));
        
        // Rewards dialog
        DialogNode rewardsNode = new DialogNode(
            "maddox_rewards",
            npcId,
            "Here are your slayer rewards!",
            false,
            null
        );
        
        rewardsNode.addOption(new DialogOption(
            "back_to_main",
            "Back to main menu",
            "maddox_welcome",
            null
        ));
        
        // Add all nodes
        addDialogNode(welcomeNode);
        addDialogNode(slayerInfoNode);
        addDialogNode(questSelectionNode);
        addDialogNode(zombieQuestNode);
        addDialogNode(questAcceptedNode);
        addDialogNode(rewardsNode);
    }
    
    /**
     * Initialize other NPC dialogs
     */
    private void initializeOtherNPCDialogs() {
        // TODO: Implement other NPC dialogs
        // This would include dialogs for:
        // - Quest NPCs
        // - Shop NPCs
        // - Crafting NPCs
        // - Information NPCs
    }
    
    /**
     * Load dialog progress from database
     */
    private void loadDialogProgress() {
        // TODO: Implement database loading
        // This will integrate with the existing database system
    }
    
    /**
     * Save dialog progress to database
     */
    private void saveDialogProgress() {
        // TODO: Implement database saving
        // This will integrate with the existing database system
    }
}
