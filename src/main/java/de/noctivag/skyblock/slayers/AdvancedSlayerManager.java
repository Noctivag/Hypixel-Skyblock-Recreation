package de.noctivag.skyblock.slayers;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.slayers.quests.SlayerQuest;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Advanced Slayer Manager - Advanced manager for slayer quests
 */
public class AdvancedSlayerManager {
    
    private final SkyblockPlugin plugin;
    private final Map<UUID, SlayerQuest> activeQuests = new HashMap<>();
    private final Map<UUID, Integer> playerSlayerLevels = new HashMap<>();
    
    public AdvancedSlayerManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("AdvancedSlayerManager initialized.");
    }
    
    /**
     * Start a slayer quest for a player
     */
    public boolean startSlayerQuest(Player player, String slayerType, int tier) {
        UUID playerId = player.getUniqueId();
        
        // Check if player already has an active quest
        if (activeQuests.containsKey(playerId)) {
            player.sendMessage("§cYou already have an active slayer quest!");
            return false;
        }
        
        // Check if player has required level
        int requiredLevel = getRequiredLevel(slayerType, tier);
        int playerLevel = getPlayerSlayerLevel(player, slayerType);
        
        if (playerLevel < requiredLevel) {
            player.sendMessage("§cYou need level " + requiredLevel + " " + slayerType + " slayer to start this quest!");
            return false;
        }
        
        // Create new quest
        UUID questId = UUID.randomUUID();
        long timeLimit = getTimeLimit(tier);
        
        SlayerQuest quest = new SlayerQuest(questId, playerId, slayerType, tier, timeLimit);
        activeQuests.put(playerId, quest);
        
        player.sendMessage("§aStarted " + slayerType + " Slayer Quest Tier " + tier + "!");
        plugin.getLogger().log(Level.INFO, "Player " + player.getName() + " started " + slayerType + " Slayer Quest Tier " + tier);
        
        return true;
    }
    
    /**
     * Complete a slayer quest for a player
     */
    public boolean completeSlayerQuest(Player player) {
        UUID playerId = player.getUniqueId();
        SlayerQuest quest = activeQuests.get(playerId);
        
        if (quest == null) {
            player.sendMessage("§cYou don't have an active slayer quest!");
            return false;
        }
        
        quest.setCompleted(true);
        quest.setActive(false);
        activeQuests.remove(playerId);
        
        // Grant rewards
        grantSlayerRewards(player, quest);
        
        player.sendMessage("§aSlayer Quest completed!");
        plugin.getLogger().log(Level.INFO, "Player " + player.getName() + " completed " + quest.getSlayerType() + " Slayer Quest Tier " + quest.getTier());
        
        return true;
    }
    
    /**
     * Get the active quest for a player
     */
    public SlayerQuest getActiveQuest(Player player) {
        return activeQuests.get(player.getUniqueId());
    }
    
    /**
     * Check if a player has an active quest
     */
    public boolean hasActiveQuest(Player player) {
        return activeQuests.containsKey(player.getUniqueId());
    }
    
    /**
     * Cancel a slayer quest for a player
     */
    public boolean cancelSlayerQuest(Player player) {
        UUID playerId = player.getUniqueId();
        SlayerQuest quest = activeQuests.remove(playerId);
        
        if (quest == null) {
            player.sendMessage("§cYou don't have an active slayer quest!");
            return false;
        }
        
        quest.setActive(false);
        player.sendMessage("§cSlayer Quest cancelled!");
        plugin.getLogger().log(Level.INFO, "Player " + player.getName() + " cancelled " + quest.getSlayerType() + " Slayer Quest Tier " + quest.getTier());
        
        return true;
    }
    
    /**
     * Get the slayer level for a player
     */
    public int getPlayerSlayerLevel(Player player, String slayerType) {
        return playerSlayerLevels.getOrDefault(player.getUniqueId(), 0);
    }
    
    /**
     * Set the slayer level for a player
     */
    public void setPlayerSlayerLevel(Player player, String slayerType, int level) {
        playerSlayerLevels.put(player.getUniqueId(), level);
    }
    
    /**
     * Get all active quests
     */
    public Map<UUID, SlayerQuest> getActiveQuests() {
        return new HashMap<>(activeQuests);
    }
    
    /**
     * Get the required level for a slayer type and tier
     */
    private int getRequiredLevel(String slayerType, int tier) {
        return tier * 5; // Simple formula: tier * 5
    }
    
    /**
     * Get the time limit for a tier
     */
    private long getTimeLimit(int tier) {
        return 300000 + (tier * 60000); // 5 minutes + tier * 1 minute
    }
    
    /**
     * Grant slayer rewards to a player
     */
    private void grantSlayerRewards(Player player, SlayerQuest quest) {
        // Grant XP
        int xpReward = quest.getTier() * 100;
        player.sendMessage("§a+§e" + xpReward + " §aSlayer XP");
        
        // Grant coins
        int coinReward = quest.getTier() * 1000;
        player.sendMessage("§a+§e" + coinReward + " §acoins");
        
        // Grant items (placeholder)
        player.sendMessage("§a+§e1 §a" + quest.getSlayerType() + " Slayer Item");
    }
}

