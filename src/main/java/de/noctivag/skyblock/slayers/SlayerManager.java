package de.noctivag.skyblock.slayers;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.slayers.quests.SlayerQuest;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Slayer Manager - Manages slayer quests and bosses
 */
public class SlayerManager {
    
    private final SkyblockPlugin plugin;
    private final Map<UUID, SlayerQuest> activeQuests = new HashMap<>();
    
    public SlayerManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("SlayerManager initialized.");
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
        
        // Create new quest
        UUID questId = UUID.randomUUID();
        long timeLimit = 300000; // 5 minutes default
        
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
     * Get all active quests
     */
    public Map<UUID, SlayerQuest> getActiveQuests() {
        return new HashMap<>(activeQuests);
    }
}

