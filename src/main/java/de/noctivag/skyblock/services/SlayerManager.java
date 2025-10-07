package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.PlayerProfile;
import de.noctivag.skyblock.models.SlayerQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SlayerManager implements Listener {

    private final SkyblockPlugin plugin;
    private final PlayerProfileService playerProfileService;
    private final Map<String, Integer> slayerXPRequirements = new HashMap<>();

    public SlayerManager(SkyblockPlugin plugin, PlayerProfileService playerProfileService) {
        this.plugin = plugin;
        this.playerProfileService = playerProfileService;
        initializeSlayerRequirements();
        
        // Register this as an event listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void initializeSlayerRequirements() {
        // Initialize XP requirements for each slayer level
        slayerXPRequirements.put("zombie_1", 100);
        slayerXPRequirements.put("zombie_2", 250);
        slayerXPRequirements.put("zombie_3", 500);
        slayerXPRequirements.put("zombie_4", 1000);
        slayerXPRequirements.put("zombie_5", 2000);
        
        slayerXPRequirements.put("spider_1", 100);
        slayerXPRequirements.put("spider_2", 250);
        slayerXPRequirements.put("spider_3", 500);
        slayerXPRequirements.put("spider_4", 1000);
        slayerXPRequirements.put("spider_5", 2000);
        
        slayerXPRequirements.put("wolf_1", 100);
        slayerXPRequirements.put("wolf_2", 250);
        slayerXPRequirements.put("wolf_3", 500);
        slayerXPRequirements.put("wolf_4", 1000);
        slayerXPRequirements.put("wolf_5", 2000);
        
        slayerXPRequirements.put("enderman_1", 100);
        slayerXPRequirements.put("enderman_2", 250);
        slayerXPRequirements.put("enderman_3", 500);
        slayerXPRequirements.put("enderman_4", 1000);
        slayerXPRequirements.put("enderman_5", 2000);
        
        slayerXPRequirements.put("blaze_1", 100);
        slayerXPRequirements.put("blaze_2", 250);
        slayerXPRequirements.put("blaze_3", 500);
        slayerXPRequirements.put("blaze_4", 1000);
        slayerXPRequirements.put("blaze_5", 2000);

        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("SlayerManager initialized with " + slayerXPRequirements.size() + " slayer requirements.");
        }
    }

    public CompletableFuture<Boolean> startSlayerQuest(Player player, String slayerType, int tier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (player == null || slayerType == null || tier < 1 || tier > 5) {
                    return false;
                }

                PlayerProfile profile = playerProfileService.getCachedProfile(player.getUniqueId());
                if (profile == null) {
                    plugin.getLogger().warning("Player profile not found for " + player.getName());
                    return false;
                }

                // Check if player already has an active slayer quest
                if (profile.getActiveSlayerQuest() != null) {
                    player.sendMessage("§cDu hast bereits eine aktive Slayer-Quest!");
                    return false;
                }

                // Check if player has enough coins
                double questCost = getQuestCost(slayerType, tier);
                if (profile.getCoins() < questCost) {
                    player.sendMessage("§cDu benötigst " + questCost + " Coins für diese Quest!");
                    return false;
                }

                // Check if player has required slayer level
                int requiredLevel = getRequiredSlayerLevel(slayerType, tier);
                int currentLevel = getSlayerLevel(profile, slayerType);
                if (currentLevel < requiredLevel) {
                    player.sendMessage("§cDu benötigst Slayer-Level " + requiredLevel + " für diese Quest!");
                    return false;
                }

                // Create and start the quest
                SlayerQuest quest = new SlayerQuest(slayerType, tier, getQuestKillsRequired(tier));
                profile.setActiveSlayerQuest(quest);
                profile.removeCoins(questCost);

                player.sendMessage("§aSlayer-Quest gestartet: " + slayerType + " Tier " + tier);
                player.sendMessage("§7Töte " + quest.getKillsRequired() + " " + slayerType + " um den Boss zu beschwören!");

                if (plugin.getSettingsConfig().isDebugMode()) {
                    plugin.getLogger().info("Player " + player.getName() + " started slayer quest: " + slayerType + " Tier " + tier);
                }

                return true;
            } catch (Exception e) {
                plugin.getLogger().severe("Error starting slayer quest: " + e.getMessage());
                return false;
            }
        });
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        
        Player killer = event.getEntity().getKiller();
        String entityType = event.getEntity().getType().name().toLowerCase();
        
        // Check if this entity type is relevant for slayer quests
        if (!isSlayerEntity(entityType)) return;
        
        PlayerProfile profile = playerProfileService.getCachedProfile(killer.getUniqueId());
        if (profile == null || profile.getActiveSlayerQuest() == null) return;
        
        SlayerQuest quest = profile.getActiveSlayerQuest();
        if (!quest.getSlayerType().equals(entityType)) return;
        
        // Add kill to quest
        quest.addKill();
        
        if (quest.isCompleted()) {
            completeSlayerQuest(killer, profile, quest);
        } else {
            killer.sendMessage("§aSlayer-Quest: " + quest.getKillsCompleted() + "/" + quest.getKillsRequired() + " " + entityType + " getötet!");
        }
    }

    private void completeSlayerQuest(Player player, PlayerProfile profile, SlayerQuest quest) {
        // Give XP and rewards
        int xpGained = getQuestXPReward(quest.getSlayerType(), quest.getTier());
        profile.addSlayerXP(quest.getSlayerType(), xpGained);
        
        // Give coins reward
        double coinReward = getQuestCoinReward(quest.getSlayerType(), quest.getTier());
        profile.addCoins(coinReward);
        
        // Clear active quest
        profile.setActiveSlayerQuest(null);
        
        player.sendMessage("§a§lSlayer-Quest abgeschlossen!");
        player.sendMessage("§a+" + xpGained + " " + quest.getSlayerType() + " XP");
        player.sendMessage("§a+" + coinReward + " Coins");
        
        // Spawn slayer boss
        spawnSlayerBoss(player, quest.getSlayerType(), quest.getTier());
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Player " + player.getName() + " completed slayer quest: " + 
                quest.getSlayerType() + " Tier " + quest.getTier());
        }
    }

    private void spawnSlayerBoss(Player player, String slayerType, int tier) {
        // Spawn the appropriate slayer boss
        new BukkitRunnable() {
            @Override
            public void run() {
                // In a real implementation, you would spawn the actual boss entity
                player.sendMessage("§c§l" + slayerType.toUpperCase() + " BOSS TIER " + tier + " §c§lSPAWNED!");
                player.sendMessage("§7Bereite dich auf den Kampf vor!");
                
                if (plugin.getSettingsConfig().isDebugMode()) {
                    plugin.getLogger().info("Spawning " + slayerType + " boss tier " + tier + " for player " + player.getName());
                }
            }
        }.runTaskLater(plugin, 20L); // 1 second delay
    }

    private boolean isSlayerEntity(String entityType) {
        return entityType.equals("zombie") || entityType.equals("spider") || 
               entityType.equals("wolf") || entityType.equals("enderman") || 
               entityType.equals("blaze");
    }

    private double getQuestCost(String slayerType, int tier) {
        return tier * 1000; // 1000, 2000, 3000, 4000, 5000 coins
    }

    private int getRequiredSlayerLevel(String slayerType, int tier) {
        return tier - 1; // Tier 1 requires level 0, Tier 2 requires level 1, etc.
    }

    private int getQuestKillsRequired(int tier) {
        return tier * 10; // 10, 20, 30, 40, 50 kills
    }

    private int getQuestXPReward(String slayerType, int tier) {
        return tier * 50; // 50, 100, 150, 200, 250 XP
    }

    private double getQuestCoinReward(String slayerType, int tier) {
        return tier * 500; // 500, 1000, 1500, 2000, 2500 coins
    }

    public int getSlayerLevel(PlayerProfile profile, String slayerType) {
        int totalXP = profile.getSlayerXP(slayerType);
        int level = 0;
        
        for (int i = 1; i <= 5; i++) {
            String levelKey = slayerType + "_" + i;
            int requiredXP = slayerXPRequirements.getOrDefault(levelKey, 0);
            if (totalXP >= requiredXP) {
                level = i;
            } else {
                break;
            }
        }
        
        return level;
    }

    public int getSlayerXP(PlayerProfile profile, String slayerType) {
        return profile.getSlayerXP(slayerType);
    }

    public int getSlayerXPForNextLevel(PlayerProfile profile, String slayerType) {
        int currentLevel = getSlayerLevel(profile, slayerType);
        if (currentLevel >= 5) return 0; // Max level reached
        
        int currentXP = profile.getSlayerXP(slayerType);
        String nextLevelKey = slayerType + "_" + (currentLevel + 1);
        int requiredXP = slayerXPRequirements.getOrDefault(nextLevelKey, 0);
        
        return Math.max(0, requiredXP - currentXP);
    }
}