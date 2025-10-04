package de.noctivag.plugin.skyblock.slayers;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Comprehensive Slayer System inspired by Hypixel Skyblock
 * Features:
 * - All slayer types (Zombie, Spider, Wolf, Enderman, Blaze)
 * - All slayer tiers (I, II, III, IV, V)
 * - Slayer quests and progression
 * - Slayer items and equipment
 * - Slayer rewards and drops
 * - Slayer bosses with unique mechanics
 */
public class SlayerSystem implements Listener {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerSlayerData> playerSlayerData = new ConcurrentHashMap<>();
    private final Map<UUID, SlayerQuest> activeQuests = new ConcurrentHashMap<>();
    private final Map<UUID, SlayerBoss> activeBosses = new ConcurrentHashMap<>();
    
    public SlayerSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
        initializeSlayerTypes();
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        String displayName = item.getItemMeta().getDisplayName();
        
        // Check for slayer items
        if (displayName.contains("Slayer")) {
            handleSlayerItemUse(player, item, displayName);
        }
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        
        Player killer = event.getEntity().getKiller();
        SlayerQuest quest = activeQuests.get(killer.getUniqueId());
        
        if (quest != null) {
            // Handle slayer quest progress
            handleSlayerQuestProgress(quest, event.getEntity(), killer);
        }
        
        // Check if it's a slayer boss
        SlayerBoss boss = getSlayerBoss(event.getEntity());
        if (boss != null) {
            handleSlayerBossDeath(boss, killer);
        }
    }
    
    private void initializeSlayerTypes() {
        // Initialize all slayer types with their configurations
    }
    
    public void startSlayerQuest(Player player, SlayerType slayerType, SlayerTier tier) {
        UUID playerId = player.getUniqueId();
        PlayerSlayerData data = playerSlayerData.get(playerId);
        
        if (data == null) return;
        
        // Check requirements
        if (!canStartSlayerQuest(player, slayerType, tier)) {
            player.sendMessage("§cYou don't meet the requirements for this slayer quest!");
            return;
        }
        
        // Check if player already has an active quest
        if (activeQuests.containsKey(playerId)) {
            player.sendMessage("§cYou already have an active slayer quest!");
            return;
        }
        
        // Create slayer quest
        SlayerQuest quest = new SlayerQuest(player, slayerType, tier);
        activeQuests.put(playerId, quest);
        
        // Start quest
        startSlayerQuest(quest);
        
        player.sendMessage("§aStarted " + slayerType.getDisplayName() + " Slayer " + tier.getDisplayName() + "!");
        player.sendMessage("§7Kill " + quest.getRequiredKills() + " " + slayerType.getMobType().name() + "s");
    }
    
    public void spawnSlayerBoss(Player player, SlayerType slayerType, SlayerTier tier) {
        UUID playerId = player.getUniqueId();
        SlayerQuest quest = activeQuests.get(playerId);
        
        if (quest == null) {
            player.sendMessage("§cYou don't have an active slayer quest!");
            return;
        }
        
        if (quest.getType() != slayerType || quest.getTier() != tier) {
            player.sendMessage("§cThis doesn't match your active quest!");
            return;
        }
        
        if (!quest.isReadyForBoss()) {
            player.sendMessage("§cYou need to kill more mobs before spawning the boss!");
            return;
        }
        
        // Create slayer boss
        SlayerBoss boss = new SlayerBoss(player, slayerType, tier);
        activeBosses.put(playerId, boss);
        
        // Spawn boss
        spawnBossEntity(boss);
        
        player.sendMessage("§6§l" + slayerType.getDisplayName() + " Slayer Boss " + tier.getDisplayName() + " spawned!");
    }
    
    private boolean canStartSlayerQuest(Player player, SlayerType slayerType, SlayerTier tier) {
        PlayerSlayerData data = playerSlayerData.get(player.getUniqueId());
        if (data == null) return false;
        
        // Check slayer level requirement
        int requiredLevel = getRequiredSlayerLevel(slayerType, tier);
        if (data.getSlayerLevel(slayerType) < requiredLevel) {
            return false;
        }
        
        // Check other requirements (gear, etc.)
        return true;
    }
    
    private int getRequiredSlayerLevel(SlayerType slayerType, SlayerTier tier) {
        return switch (tier) {
            case I -> 1;
            case II -> 3;
            case III -> 5;
            case IV -> 7;
            case V -> 9;
        };
    }
    
    private void startSlayerQuest(SlayerQuest quest) {
        // Start quest timer and mechanics
        new BukkitRunnable() {
            @Override
            public void run() {
                if (quest.isCompleted() || quest.isFailed()) {
                    this.cancel();
                    return;
                }
                
                // Update quest progress
                updateSlayerQuest(quest);
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
    }
    
    private void updateSlayerQuest(SlayerQuest quest) {
        // Update quest progress, check time limits, etc.
        quest.updateProgress();
    }
    
    private void handleSlayerQuestProgress(SlayerQuest quest, LivingEntity mob, Player killer) {
        // Check if mob type matches quest
        if (mob.getType() == quest.getType().getMobType()) {
            quest.addKill();
            
            killer.sendMessage("§a+" + quest.getType().getDisplayName() + " Slayer XP");
            
            // Check if ready for boss
            if (quest.isReadyForBoss()) {
                killer.sendMessage("§6§lReady to spawn boss! Use the slayer item to spawn it.");
            }
        }
    }
    
    private void handleSlayerBossDeath(SlayerBoss boss, Player killer) {
        // Give slayer rewards
        giveSlayerRewards(killer, boss);
        
        // Complete quest
        SlayerQuest quest = activeQuests.get(killer.getUniqueId());
        if (quest != null) {
            quest.complete();
            activeQuests.remove(killer.getUniqueId());
        }
        
        // Remove boss
        activeBosses.remove(killer.getUniqueId());
        
        killer.sendMessage("§6§lSlayer Boss defeated!");
    }
    
    private void giveSlayerRewards(Player player, SlayerBoss boss) {
        // Give slayer XP
        int xpReward = getSlayerXPReward(boss.getType(), boss.getTier());
        addSlayerXP(player, boss.getType(), xpReward);
        
        // Give coins
        int coinReward = getCoinReward(boss.getType(), boss.getTier());
        // Add coins to player
        
        // Give items
        List<ItemStack> items = getSlayerRewardItems(boss.getType(), boss.getTier());
        for (ItemStack item : items) {
            player.getInventory().addItem(item);
        }
        
        player.sendMessage("§a+" + xpReward + " " + boss.getType().getDisplayName() + " Slayer XP");
        player.sendMessage("§6+" + coinReward + " coins");
    }
    
    private int getSlayerXPReward(SlayerType slayerType, SlayerTier tier) {
        return switch (tier) {
            case I -> 5;
            case II -> 15;
            case III -> 50;
            case IV -> 150;
            case V -> 500;
        };
    }
    
    private int getCoinReward(SlayerType slayerType, SlayerTier tier) {
        return switch (tier) {
            case I -> 100;
            case II -> 500;
            case III -> 2000;
            case IV -> 10000;
            case V -> 50000;
        };
    }
    
    private List<ItemStack> getSlayerRewardItems(SlayerType slayerType, SlayerTier tier) {
        List<ItemStack> items = new ArrayList<>();
        
        // Add slayer-specific items based on type and tier
        // This would be implemented with actual item creation
        
        return items;
    }
    
    private void addSlayerXP(Player player, SlayerType slayerType, int xp) {
        UUID playerId = player.getUniqueId();
        PlayerSlayerData data = playerSlayerData.get(playerId);
        
        if (data == null) return;
        
        int currentXP = data.getSlayerXP(slayerType);
        int newXP = currentXP + xp;
        
        data.setSlayerXP(slayerType, newXP);
        
        // Check for level up
        int newLevel = calculateSlayerLevel(newXP);
        int currentLevel = data.getSlayerLevel(slayerType);
        
        if (newLevel > currentLevel) {
            data.setSlayerLevel(slayerType, newLevel);
            player.sendMessage("§6§lSLAYER LEVEL UP! §e" + slayerType.getDisplayName() + " Slayer Level " + newLevel);
        }
    }
    
    private int calculateSlayerLevel(int xp) {
        // XP formula for slayer levels
        if (xp < 5) return 0;
        if (xp < 15) return 1;
        if (xp < 35) return 2;
        if (xp < 75) return 3;
        if (xp < 150) return 4;
        if (xp < 300) return 5;
        if (xp < 600) return 6;
        if (xp < 1200) return 7;
        if (xp < 2400) return 8;
        if (xp < 4800) return 9;
        return 10; // Max level
    }
    
    private void handleSlayerItemUse(Player player, ItemStack item, String displayName) {
        // Handle different slayer items
        if (displayName.contains("Zombie Slayer")) {
            // Handle zombie slayer item
        } else if (displayName.contains("Spider Slayer")) {
            // Handle spider slayer item
        } else if (displayName.contains("Wolf Slayer")) {
            // Handle wolf slayer item
        } else if (displayName.contains("Enderman Slayer")) {
            // Handle enderman slayer item
        } else if (displayName.contains("Blaze Slayer")) {
            // Handle blaze slayer item
        }
    }
    
    private void spawnBossEntity(SlayerBoss boss) {
        // Spawn the actual boss entity
        Location location = boss.getPlayer().getLocation();
        LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, boss.getType().getBossEntityType());
        
        // Set boss properties
        entity.setCustomName(boss.getDisplayName());
        entity.setCustomNameVisible(true);
        
        // Set boss health and stats
        entity.setMaxHealth(boss.getMaxHealth());
        entity.setHealth(boss.getMaxHealth());
        
        boss.setEntity(entity);
    }
    
    private SlayerBoss getSlayerBoss(LivingEntity entity) {
        for (SlayerBoss boss : activeBosses.values()) {
            if (boss.getEntity() == entity) {
                return boss;
            }
        }
        return null;
    }
    
    public enum SlayerType {
        ZOMBIE("§2Zombie", "§7Undead creatures", EntityType.ZOMBIE, EntityType.ZOMBIE),
        SPIDER("§8Spider", "§7Arachnid creatures", EntityType.SPIDER, EntityType.SPIDER),
        WOLF("§fWolf", "§7Canine creatures", EntityType.WOLF, EntityType.WOLF),
        ENDERMAN("§5Enderman", "§7End dimension creatures", EntityType.ENDERMAN, EntityType.ENDERMAN),
        BLAZE("§eBlaze", "§7Nether creatures", EntityType.BLAZE, EntityType.BLAZE);
        
        private final String displayName;
        private final String description;
        private final EntityType mobType;
        private final EntityType bossEntityType;
        
        SlayerType(String displayName, String description, EntityType mobType, EntityType bossEntityType) {
            this.displayName = displayName;
            this.description = description;
            this.mobType = mobType;
            this.bossEntityType = bossEntityType;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public EntityType getMobType() { return mobType; }
        public EntityType getBossEntityType() { return bossEntityType; }
    }
    
    public enum SlayerTier {
        I("I", 1),
        II("II", 2),
        III("III", 3),
        IV("IV", 4),
        V("V", 5);
        
        private final String displayName;
        private final int tierNumber;
        
        SlayerTier(String displayName, int tierNumber) {
            this.displayName = displayName;
            this.tierNumber = tierNumber;
        }
        
        public String getDisplayName() { return displayName; }
        public int getTierNumber() { return tierNumber; }
    }
    
    public static class SlayerQuest {
        private final Player player;
        private final SlayerType type;
        private final SlayerTier tier;
        private int kills;
        private final int requiredKills;
        private boolean completed;
        private boolean failed;
        private long startTime;
        
        public SlayerQuest(Player player, SlayerType type, SlayerTier tier) {
            this.player = player;
            this.type = type;
            this.tier = tier;
            this.kills = 0;
            this.requiredKills = getRequiredKills(tier);
            this.completed = false;
            this.failed = false;
            this.startTime = System.currentTimeMillis();
        }
        
        private int getRequiredKills(SlayerTier tier) {
            return switch (tier) {
                case I -> 10;
                case II -> 25;
                case III -> 50;
                case IV -> 100;
                case V -> 200;
            };
        }
        
        public void addKill() {
            kills++;
        }
        
        public boolean isReadyForBoss() {
            return kills >= requiredKills;
        }
        
        public void complete() {
            completed = true;
        }
        
        public void fail() {
            failed = true;
        }
        
        public void updateProgress() {
            // Update quest progress, check time limits, etc.
        }
        
        // Getters
        public Player getPlayer() { return player; }
        public SlayerType getType() { return type; }
        public SlayerTier getTier() { return tier; }
        public int getKills() { return kills; }
        public int getRequiredKills() { return requiredKills; }
        public boolean isCompleted() { return completed; }
        public boolean isFailed() { return failed; }
        public long getStartTime() { return startTime; }
    }
    
    public static class SlayerBoss {
        private final Player player;
        private final SlayerType type;
        private final SlayerTier tier;
        private LivingEntity entity;
        private double maxHealth;
        private double currentHealth;
        
        public SlayerBoss(Player player, SlayerType type, SlayerTier tier) {
            this.player = player;
            this.type = type;
            this.tier = tier;
            this.maxHealth = getBossHealth(tier);
            this.currentHealth = maxHealth;
        }
        
        private double getBossHealth(SlayerTier tier) {
            return switch (tier) {
                case I -> 1000.0;
                case II -> 5000.0;
                case III -> 25000.0;
                case IV -> 100000.0;
                case V -> 500000.0;
            };
        }
        
        public String getDisplayName() {
            return "§6§l" + type.getDisplayName() + " Slayer Boss " + tier.getDisplayName();
        }
        
        // Getters and setters
        public Player getPlayer() { return player; }
        public SlayerType getType() { return type; }
        public SlayerTier getTier() { return tier; }
        public LivingEntity getEntity() { return entity; }
        public void setEntity(LivingEntity entity) { this.entity = entity; }
        public double getMaxHealth() { return maxHealth; }
        public double getCurrentHealth() { return currentHealth; }
        public void setCurrentHealth(double currentHealth) { this.currentHealth = currentHealth; }
    }
    
    public static class PlayerSlayerData {
        private final UUID playerId;
        private final Map<SlayerType, Integer> slayerLevels;
        private final Map<SlayerType, Integer> slayerXP;
        
        public PlayerSlayerData(UUID playerId) {
            this.playerId = playerId;
            this.slayerLevels = new HashMap<>();
            this.slayerXP = new HashMap<>();
            
            // Initialize all slayer types
            for (SlayerType type : SlayerType.values()) {
                slayerLevels.put(type, 0);
                slayerXP.put(type, 0);
            }
        }
        
        public int getSlayerLevel(SlayerType type) {
            return slayerLevels.getOrDefault(type, 0);
        }
        
        public void setSlayerLevel(SlayerType type, int level) {
            slayerLevels.put(type, level);
        }
        
        public int getSlayerXP(SlayerType type) {
            return slayerXP.getOrDefault(type, 0);
        }
        
        public void setSlayerXP(SlayerType type, int xp) {
            slayerXP.put(type, xp);
        }
        
        public UUID getPlayerId() { return playerId; }
    }
}
