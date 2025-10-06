package de.noctivag.skyblock.slayers;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main Slayers System - Manages slayer quests, boss spawning, and progression
 */
public class SlayersSystem implements Service, Listener {
    
    private final SkyblockPlugin plugin;
    private final DatabaseManager databaseManager;
    private final Map<UUID, PlayerSlayers> playerSlayers;
    private final Map<String, SlayerQuest> activeQuests;
    private final BukkitTask slayerTask;
    private SystemStatus status = SystemStatus.UNINITIALIZED;

    public SlayersSystem(SkyblockPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.playerSlayers = new ConcurrentHashMap<>();
        this.activeQuests = new ConcurrentHashMap<>();
        
        // Start slayer management task
        this.slayerTask = new BukkitRunnable() {
            @Override
            public void run() {
                processSlayerQuests();
            }
        }.runTaskTimer(plugin, 20L, 20L); // Run every second
    }

    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing SlayersSystem...");

        // Register event listeners
        Bukkit.getPluginManager().registerEvents(this, plugin);

        // Load all online player data
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadPlayerSlayers(player.getUniqueId());
        }

        status = SystemStatus.RUNNING;
        plugin.getLogger().info("SlayersSystem initialized successfully!");
    }

    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down SlayersSystem...");

        // Cancel slayer task
        if (slayerTask != null && !slayerTask.isCancelled()) {
            slayerTask.cancel();
        }

        // Save all player data
        saveAllPlayerSlayers();

        // End all active quests
        for (SlayerQuest quest : activeQuests.values()) {
            endQuest(quest.getQuestId());
        }

        playerSlayers.clear();
        activeQuests.clear();
        status = SystemStatus.SHUTDOWN;
        plugin.getLogger().info("SlayersSystem shutdown complete!");
    }

    @Override
    public String getName() {
        return "SlayersSystem";
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
        // Slayers system is always enabled when running
    }

    /**
     * Load player slayers from database
     */
    public void loadPlayerSlayers(UUID playerId) {
        try {
            // Load from database (placeholder - implement actual database loading)
            PlayerSlayers slayers = new PlayerSlayers(playerId);
            playerSlayers.put(playerId, slayers);
            plugin.getLogger().info("Loaded slayers for player: " + playerId);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load slayers for player " + playerId + ": " + e.getMessage());
            // Create new slayers if loading fails
            playerSlayers.put(playerId, new PlayerSlayers(playerId));
        }
    }

    /**
     * Save player slayers to database
     */
    public void savePlayerSlayers(UUID playerId) {
        try {
            // Save to database (placeholder - implement actual database saving)
            plugin.getLogger().info("Saved slayers for player: " + playerId);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save slayers for player " + playerId + ": " + e.getMessage());
        }
    }

    /**
     * Save all player slayers
     */
    public void saveAllPlayerSlayers() {
        for (UUID playerId : playerSlayers.keySet()) {
            savePlayerSlayers(playerId);
        }
    }

    /**
     * Get player slayers
     */
    public PlayerSlayers getPlayerSlayers(UUID playerId) {
        return playerSlayers.get(playerId);
    }

    /**
     * Get player slayers (by Player object)
     */
    public PlayerSlayers getPlayerSlayers(Player player) {
        return getPlayerSlayers(player.getUniqueId());
    }

    /**
     * Start a slayer quest
     */
    public boolean startSlayerQuest(Player player, SlayerTier slayerTier, Location spawnLocation) {
        PlayerSlayers playerSlayers = getPlayerSlayers(player);
        if (playerSlayers == null) {
            loadPlayerSlayers(player.getUniqueId());
            playerSlayers = getPlayerSlayers(player);
        }

        // Check if player already has an active quest
        if (hasActiveQuest(player.getUniqueId())) {
            player.sendMessage("§cYou already have an active slayer quest!");
            return false;
        }

        // Check if player has enough XP for this tier
        if (playerSlayers.getCurrentTier(slayerTier.getSlayerType()) < slayerTier.getTier() - 1) {
            player.sendMessage("§cYou need to complete the previous tier first!");
            return false;
        }

        // Generate unique quest ID
        String questId = player.getUniqueId().toString() + "_" + System.currentTimeMillis();
        
        // Create slayer quest
        SlayerQuest quest = new SlayerQuest(questId, player.getUniqueId(), slayerTier, spawnLocation);
        
        // Add to active quests
        activeQuests.put(questId, quest);
        
        player.sendMessage("§aSlayer quest started: " + quest.getDisplayName());
        player.sendMessage("§7" + quest.getDescription());
        return true;
    }

    /**
     * End a slayer quest
     */
    public boolean endQuest(String questId) {
        SlayerQuest quest = activeQuests.remove(questId);
        if (quest == null) {
            return false; // Quest not found
        }
        
        // Give rewards if quest was completed
        if (quest.isCompleted()) {
            giveQuestRewards(quest);
        }
        
        return true;
    }

    /**
     * Get slayer quest by ID
     */
    public SlayerQuest getQuest(String questId) {
        return activeQuests.get(questId);
    }

    /**
     * Get player's active quest
     */
    public SlayerQuest getPlayerActiveQuest(UUID playerId) {
        return activeQuests.values().stream()
                .filter(quest -> quest.getPlayerId().equals(playerId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all active quests
     */
    public Collection<SlayerQuest> getAllActiveQuests() {
        return new ArrayList<>(activeQuests.values());
    }

    /**
     * Get quests by slayer type
     */
    public List<SlayerQuest> getQuestsBySlayerType(SlayerType slayerType) {
        return activeQuests.values().stream()
                .filter(quest -> quest.getSlayerTier().getSlayerType() == slayerType)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Check if player has an active quest
     */
    public boolean hasActiveQuest(UUID playerId) {
        return getPlayerActiveQuest(playerId) != null;
    }

    /**
     * Check if player has an active quest (by Player object)
     */
    public boolean hasActiveQuest(Player player) {
        return hasActiveQuest(player.getUniqueId());
    }

    /**
     * Add XP to player's slayer
     */
    public boolean addSlayerXP(Player player, SlayerType slayerType, long xp) {
        PlayerSlayers playerSlayers = getPlayerSlayers(player);
        if (playerSlayers == null) return false;
        
        boolean tieredUp = playerSlayers.addXP(slayerType, xp);
        
        if (tieredUp) {
            int newTier = playerSlayers.getCurrentTier(slayerType);
            player.sendMessage("§a§lSLAYER LEVEL UP! §r" + slayerType.getColoredDisplayName() + " §aTier " + newTier);
        }
        
        return tieredUp;
    }

    /**
     * Get slayer statistics for a player
     */
    public PlayerSlayers.SlayerStatistics getSlayerStatistics(Player player) {
        PlayerSlayers playerSlayers = getPlayerSlayers(player);
        if (playerSlayers == null) return null;
        
        return playerSlayers.getSlayerStatistics();
    }

    /**
     * Get slayer type by name
     */
    public SlayerType getSlayerTypeByName(String name) {
        for (SlayerType slayerType : SlayerType.values()) {
            if (slayerType.getDisplayName().equalsIgnoreCase(name)) {
                return slayerType;
            }
        }
        return null;
    }

    /**
     * Get slayer tier for a slayer type and tier number
     */
    public SlayerTier getSlayerTier(SlayerType slayerType, int tier) {
        return new SlayerTier(slayerType, tier);
    }

    /**
     * Get all slayer types
     */
    public SlayerType[] getAllSlayerTypes() {
        return SlayerType.values();
    }

    /**
     * Get slayer types by difficulty
     */
    public SlayerType[] getSlayerTypesByDifficulty(String difficulty) {
        return java.util.Arrays.stream(SlayerType.values())
                .filter(slayerType -> slayerType.getDifficulty().equalsIgnoreCase(difficulty))
                .toArray(SlayerType[]::new);
    }

    /**
     * Process slayer quests
     */
    private void processSlayerQuests() {
        // Process quest logic here
        // This would include quest progression, boss spawning, etc.
        for (SlayerQuest quest : activeQuests.values()) {
            // Quest processing logic would go here
            if (!quest.isPlayerOnline()) {
                // Remove quests for offline players
                endQuest(quest.getQuestId());
            }
        }
    }

    /**
     * Give quest rewards
     */
    private void giveQuestRewards(SlayerQuest quest) {
        Player player = quest.getPlayer();
        if (player == null || !player.isOnline()) return;
        
        SlayerTier slayerTier = quest.getSlayerTier();
        
        // Give XP reward
        addSlayerXP(player, slayerTier.getSlayerType(), slayerTier.getXpReward());
        
        // Give coin reward (placeholder - integrate with economy system)
        player.sendMessage("§aQuest completed! Rewards: " + slayerTier.getXpReward() + " XP, " + slayerTier.getReward() + " coins");
        
        // Update player statistics
        PlayerSlayers playerSlayers = getPlayerSlayers(player);
        if (playerSlayers != null) {
            PlayerSlayers.SlayerData slayerData = playerSlayers.getSlayerData(slayerTier.getSlayerType());
            slayerData.addQuestCompleted();
            slayerData.addBossKilled();
        }
    }

    // Event Handlers

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        loadPlayerSlayers(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        savePlayerSlayers(player.getUniqueId());
        
        // End player's active quest
        SlayerQuest activeQuest = getPlayerActiveQuest(player.getUniqueId());
        if (activeQuest != null) {
            endQuest(activeQuest.getQuestId());
        }
    }
}
