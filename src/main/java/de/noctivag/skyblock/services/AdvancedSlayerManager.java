package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.models.PlayerProfile;
import de.noctivag.skyblock.models.SlayerQuest;
import de.noctivag.skyblock.slayers.bosses.RevenantHorror;
import de.noctivag.skyblock.slayers.bosses.TarantulaBroodmother;
import de.noctivag.skyblock.slayers.bosses.SvenPackmaster;
import de.noctivag.skyblock.slayers.bosses.SlayerBoss;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AdvancedSlayerManager implements Listener {

    private final SkyblockPluginRefactored plugin;
    private final PlayerProfileService playerProfileService;
    private final Map<String, Integer> slayerXPRequirements = new HashMap<>();
    private final Map<UUID, SlayerBoss> activeBosses = new HashMap<>();

    public AdvancedSlayerManager(SkyblockPluginRefactored plugin, PlayerProfileService playerProfileService) {
        this.plugin = plugin;
        this.playerProfileService = playerProfileService;
        initializeSlayerRequirements();
        
        // Register this as an event listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void initializeSlayerRequirements() {
        // Initialize XP requirements for each slayer level (1-9)
        String[] slayerTypes = {"zombie", "spider", "wolf", "enderman", "blaze"};
        
        for (String slayerType : slayerTypes) {
            for (int level = 1; level <= 9; level++) {
                String levelKey = slayerType + "_" + level;
                int requiredXP = level * 100; // 100, 200, 300, etc.
                slayerXPRequirements.put(levelKey, requiredXP);
            }
        }

        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("AdvancedSlayerManager initialized with " + slayerXPRequirements.size() + " slayer requirements.");
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
                try {
                    SlayerBoss boss = createSlayerBoss(slayerType, tier, player.getLocation());
                    if (boss != null) {
                        boss.startBoss();
                        activeBosses.put(player.getUniqueId(), boss);
                        
                        if (plugin.getSettingsConfig().isDebugMode()) {
                            plugin.getLogger().info("Spawning " + slayerType + " boss tier " + tier + " for player " + player.getName());
                        }
                    }
                } catch (Exception e) {
                    plugin.getLogger().severe("Error spawning slayer boss: " + e.getMessage());
                }
            }
        }.runTaskLater(plugin, 20L); // 1 second delay
    }

    private SlayerBoss createSlayerBoss(String slayerType, int tier, org.bukkit.Location location) {
        switch (slayerType.toLowerCase()) {
            case "zombie":
                return new RevenantHorror(plugin, location, tier);
            case "spider":
                return new TarantulaBroodmother(plugin, location, tier);
            case "wolf":
                return new SvenPackmaster(plugin, location, tier);
            case "enderman":
                // TODO: Implement Enderman Slayer Boss
                return null;
            case "blaze":
                // TODO: Implement Blaze Slayer Boss
                return null;
            default:
                plugin.getLogger().warning("Unknown slayer type: " + slayerType);
                return null;
        }
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
        
        for (int i = 1; i <= 9; i++) {
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
        if (currentLevel >= 9) return 0; // Max level reached
        
        int currentXP = profile.getSlayerXP(slayerType);
        String nextLevelKey = slayerType + "_" + (currentLevel + 1);
        int requiredXP = slayerXPRequirements.getOrDefault(nextLevelKey, 0);
        
        return Math.max(0, requiredXP - currentXP);
    }

    public SlayerBoss getActiveBoss(Player player) {
        return activeBosses.get(player.getUniqueId());
    }

    public void removeActiveBoss(Player player) {
        SlayerBoss boss = activeBosses.remove(player.getUniqueId());
        if (boss != null) {
            boss.stopBoss();
        }
    }

    public void cleanup() {
        // Clean up all active bosses
        for (SlayerBoss boss : activeBosses.values()) {
            boss.stopBoss();
        }
        activeBosses.clear();
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("AdvancedSlayerManager cleaned up all active bosses");
        }
    }
}
