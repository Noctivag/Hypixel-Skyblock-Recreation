package de.noctivag.skyblock.skyblock.slayer;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Material;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Slayer System für Hypixel SkyBlock
 */
public class SlayerSystem implements Listener {
    
    private final SkyblockPlugin plugin;
    private final Map<UUID, Map<SlayerType, Integer>> playerSlayerLevels = new ConcurrentHashMap<>();
    private final Map<UUID, Map<SlayerType, Integer>> playerSlayerXP = new ConcurrentHashMap<>();
    private final Map<UUID, SlayerQuest> activeQuests = new ConcurrentHashMap<>();
    
    // Slayer Types
    public enum SlayerType {
        ZOMBIE("Zombie", "🧟", "Zombie Slayer", "Bekämpfe Zombies und ihre Bosse"),
        SPIDER("Spider", "🕷", "Spider Slayer", "Bekämpfe Spinnen und ihre Bosse"),
        WOLF("Wolf", "🐺", "Wolf Slayer", "Bekämpfe Wölfe und ihre Bosse"),
        ENDERMAN("Enderman", "👁", "Enderman Slayer", "Bekämpfe Endermen und ihre Bosse"),
        BLAZE("Blaze", "🔥", "Blaze Slayer", "Bekämpfe Blazes und ihre Bosse");
        
        private final String name;
        private final String icon;
        private final String displayName;
        private final String description;
        
        SlayerType(String name, String icon, String displayName, String description) {
            this.name = name;
            this.icon = icon;
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getIcon() { return icon; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    // Slayer Quest
    public static class SlayerQuest {
        private final SlayerType type;
        private final int tier;
        private final EntityType targetEntity;
        private final int requiredKills;
        private int currentKills;
        private final long startTime;
        private final UUID playerId;
        
        public SlayerQuest(SlayerType type, int tier, EntityType targetEntity, int requiredKills, UUID playerId) {
            this.type = type;
            this.tier = tier;
            this.targetEntity = targetEntity;
            this.requiredKills = requiredKills;
            this.currentKills = 0;
            this.startTime = System.currentTimeMillis();
            this.playerId = playerId;
        }
        
        // Getters
        public SlayerType getType() { return type; }
        public int getTier() { return tier; }
        public EntityType getTargetEntity() { return targetEntity; }
        public int getRequiredKills() { return requiredKills; }
        public int getCurrentKills() { return currentKills; }
        public long getStartTime() { return startTime; }
        public UUID getPlayerId() { return playerId; }
        
        public boolean isCompleted() {
            return currentKills >= requiredKills;
        }
        
        public void addKill() {
            this.currentKills++;
        }
        
        public int getRemainingKills() {
            return requiredKills - currentKills;
        }
    }
    
    // Slayer Boss Definition
    public static class SlayerBoss {
        private final String name;
        private final SlayerType type;
        private final int tier;
        private final double health;
        private final double damage;
        private final List<ItemStack> drops;
        private final int xpReward;
        private final int coinReward;
        
        public SlayerBoss(String name, SlayerType type, int tier, double health, double damage, 
                         List<ItemStack> drops, int xpReward, int coinReward) {
            this.name = name;
            this.type = type;
            this.tier = tier;
            this.health = health;
            this.damage = damage;
            this.drops = drops;
            this.xpReward = xpReward;
            this.coinReward = coinReward;
        }
        
        // Getters
        public String getName() { return name; }
        public SlayerType getType() { return type; }
        public int getTier() { return tier; }
        public double getHealth() { return health; }
        public double getDamage() { return damage; }
        public List<ItemStack> getDrops() { return drops; }
        public int getXpReward() { return xpReward; }
        public int getCoinReward() { return coinReward; }
    }
    
    // Slayer Boss Registry
    private final Map<String, SlayerBoss> slayerBosses = new HashMap<>();
    
    public SlayerSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        initializeSlayerBosses();
    }
    
    /**
     * Initialisiert Slayer Bosse
     */
    private void initializeSlayerBosses() {
        // Zombie Slayer Bosse
        registerSlayerBoss(new SlayerBoss("Revenant Horror I", SlayerType.ZOMBIE, 1, 1000, 50,
            Arrays.asList(new ItemStack(Material.ROTTEN_FLESH, 10)), 100, 1000));
        registerSlayerBoss(new SlayerBoss("Revenant Horror II", SlayerType.ZOMBIE, 2, 2000, 100,
            Arrays.asList(new ItemStack(Material.ROTTEN_FLESH, 20)), 200, 2000));
        registerSlayerBoss(new SlayerBoss("Revenant Horror III", SlayerType.ZOMBIE, 3, 5000, 200,
            Arrays.asList(new ItemStack(Material.ROTTEN_FLESH, 50)), 500, 5000));
        registerSlayerBoss(new SlayerBoss("Revenant Horror IV", SlayerType.ZOMBIE, 4, 10000, 400,
            Arrays.asList(new ItemStack(Material.ROTTEN_FLESH, 100)), 1000, 10000));
        registerSlayerBoss(new SlayerBoss("Revenant Horror V", SlayerType.ZOMBIE, 5, 20000, 800,
            Arrays.asList(new ItemStack(Material.ROTTEN_FLESH, 200)), 2000, 20000));
        
        // Spider Slayer Bosse
        registerSlayerBoss(new SlayerBoss("Tarantula Broodfather I", SlayerType.SPIDER, 1, 800, 40,
            Arrays.asList(new ItemStack(Material.STRING, 10)), 80, 800));
        registerSlayerBoss(new SlayerBoss("Tarantula Broodfather II", SlayerType.SPIDER, 2, 1600, 80,
            Arrays.asList(new ItemStack(Material.STRING, 20)), 160, 1600));
        registerSlayerBoss(new SlayerBoss("Tarantula Broodfather III", SlayerType.SPIDER, 3, 4000, 160,
            Arrays.asList(new ItemStack(Material.STRING, 50)), 400, 4000));
        registerSlayerBoss(new SlayerBoss("Tarantula Broodfather IV", SlayerType.SPIDER, 4, 8000, 320,
            Arrays.asList(new ItemStack(Material.STRING, 100)), 800, 8000));
        registerSlayerBoss(new SlayerBoss("Tarantula Broodfather V", SlayerType.SPIDER, 5, 16000, 640,
            Arrays.asList(new ItemStack(Material.STRING, 200)), 1600, 16000));
        
        // Wolf Slayer Bosse
        registerSlayerBoss(new SlayerBoss("Sven Packmaster I", SlayerType.WOLF, 1, 1200, 60,
            Arrays.asList(new ItemStack(Material.BONE, 10)), 120, 1200));
        registerSlayerBoss(new SlayerBoss("Sven Packmaster II", SlayerType.WOLF, 2, 2400, 120,
            Arrays.asList(new ItemStack(Material.BONE, 20)), 240, 2400));
        registerSlayerBoss(new SlayerBoss("Sven Packmaster III", SlayerType.WOLF, 3, 6000, 240,
            Arrays.asList(new ItemStack(Material.BONE, 50)), 600, 6000));
        registerSlayerBoss(new SlayerBoss("Sven Packmaster IV", SlayerType.WOLF, 4, 12000, 480,
            Arrays.asList(new ItemStack(Material.BONE, 100)), 1200, 12000));
        registerSlayerBoss(new SlayerBoss("Sven Packmaster V", SlayerType.WOLF, 5, 24000, 960,
            Arrays.asList(new ItemStack(Material.BONE, 200)), 2400, 24000));
        
        // Enderman Slayer Bosse
        registerSlayerBoss(new SlayerBoss("Voidgloom Seraph I", SlayerType.ENDERMAN, 1, 2000, 100,
            Arrays.asList(new ItemStack(Material.ENDER_PEARL, 10)), 200, 2000));
        registerSlayerBoss(new SlayerBoss("Voidgloom Seraph II", SlayerType.ENDERMAN, 2, 4000, 200,
            Arrays.asList(new ItemStack(Material.ENDER_PEARL, 20)), 400, 4000));
        registerSlayerBoss(new SlayerBoss("Voidgloom Seraph III", SlayerType.ENDERMAN, 3, 10000, 400,
            Arrays.asList(new ItemStack(Material.ENDER_PEARL, 50)), 1000, 10000));
        registerSlayerBoss(new SlayerBoss("Voidgloom Seraph IV", SlayerType.ENDERMAN, 4, 20000, 800,
            Arrays.asList(new ItemStack(Material.ENDER_PEARL, 100)), 2000, 20000));
        registerSlayerBoss(new SlayerBoss("Voidgloom Seraph V", SlayerType.ENDERMAN, 5, 40000, 1600,
            Arrays.asList(new ItemStack(Material.ENDER_PEARL, 200)), 4000, 40000));
        
        // Blaze Slayer Bosse
        registerSlayerBoss(new SlayerBoss("Inferno Demonlord I", SlayerType.BLAZE, 1, 3000, 150,
            Arrays.asList(new ItemStack(Material.BLAZE_ROD, 10)), 300, 3000));
        registerSlayerBoss(new SlayerBoss("Inferno Demonlord II", SlayerType.BLAZE, 2, 6000, 300,
            Arrays.asList(new ItemStack(Material.BLAZE_ROD, 20)), 600, 6000));
        registerSlayerBoss(new SlayerBoss("Inferno Demonlord III", SlayerType.BLAZE, 3, 15000, 600,
            Arrays.asList(new ItemStack(Material.BLAZE_ROD, 50)), 1500, 15000));
        registerSlayerBoss(new SlayerBoss("Inferno Demonlord IV", SlayerType.BLAZE, 4, 30000, 1200,
            Arrays.asList(new ItemStack(Material.BLAZE_ROD, 100)), 3000, 30000));
        registerSlayerBoss(new SlayerBoss("Inferno Demonlord V", SlayerType.BLAZE, 5, 60000, 2400,
            Arrays.asList(new ItemStack(Material.BLAZE_ROD, 200)), 6000, 60000));
    }
    
    /**
     * Registriert einen Slayer Boss
     */
    private void registerSlayerBoss(SlayerBoss boss) {
        slayerBosses.put(boss.getName(), boss);
    }
    
    /**
     * Startet eine Slayer Quest
     */
    public boolean startSlayerQuest(Player player, SlayerType type, int tier) {
        UUID playerId = player.getUniqueId();
        
        // Prüfe ob bereits eine Quest aktiv ist
        if (activeQuests.containsKey(playerId)) {
            player.sendMessage("§cDu hast bereits eine aktive Slayer Quest!");
            return false;
        }
        
        // Prüfe Slayer Level
        int currentLevel = getSlayerLevel(playerId, type);
        if (currentLevel < tier - 1) {
            player.sendMessage("§cDu benötigst " + type.getDisplayName() + " Level " + (tier - 1) + "!");
            return false;
        }
        
        // Erstelle Quest
        EntityType targetEntity = getTargetEntityForSlayer(type);
        int requiredKills = getRequiredKillsForTier(tier);
        
        SlayerQuest quest = new SlayerQuest(type, tier, targetEntity, requiredKills, playerId);
        activeQuests.put(playerId, quest);
        
        player.sendMessage("§a§lSLAYER QUEST GESTARTET!");
        player.sendMessage("§e" + type.getIcon() + " " + type.getDisplayName() + " Tier " + tier);
        player.sendMessage("§7Töte " + requiredKills + " " + targetEntity.name());
        
        return true;
    }
    
    /**
     * Event-Handler für Entity-Death
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            UUID playerId = player.getUniqueId();
            
            // Prüfe aktive Quest
            SlayerQuest quest = activeQuests.get(playerId);
            if (quest != null && quest.getTargetEntity() == event.getEntity().getType()) {
                quest.addKill();
                
                player.sendMessage("§a+" + quest.getCurrentKills() + "/" + quest.getRequiredKills() + 
                                 " " + quest.getType().getIcon() + " " + quest.getType().getDisplayName());
                
                if (quest.isCompleted()) {
                    completeSlayerQuest(player, quest);
                }
            }
        }
    }
    
    /**
     * Schließt eine Slayer Quest ab
     */
    private void completeSlayerQuest(Player player, SlayerQuest quest) {
        UUID playerId = player.getUniqueId();
        
        // Entferne Quest
        activeQuests.remove(playerId);
        
        // Gib Belohnungen
        int xpReward = getXpRewardForTier(quest.getTier());
        int coinReward = getCoinRewardForTier(quest.getTier());
        
        addSlayerXP(playerId, quest.getType(), xpReward);
        
        player.sendMessage("§6§lSLAYER QUEST ABGESCHLOSSEN!");
        player.sendMessage("§e" + quest.getType().getIcon() + " " + quest.getType().getDisplayName() + " Tier " + quest.getTier());
        player.sendMessage("§a+" + xpReward + " " + quest.getType().getDisplayName() + " XP");
        player.sendMessage("§6+" + coinReward + " Coins");
        
        // Spiele Sound und Effekte
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
    }
    
    /**
     * Fügt Slayer XP hinzu
     */
    public void addSlayerXP(UUID playerId, SlayerType type, int xp) {
        Map<SlayerType, Integer> xpMap = playerSlayerXP.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>());
        Map<SlayerType, Integer> levelMap = playerSlayerLevels.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>());
        
        int currentXP = xpMap.getOrDefault(type, 0);
        int currentLevel = levelMap.getOrDefault(type, 0);
        
        int newXP = currentXP + xp;
        xpMap.put(type, newXP);
        
        // Prüfe Level-Up
        int newLevel = calculateSlayerLevel(newXP);
        if (newLevel > currentLevel) {
            levelMap.put(type, newLevel);
            onSlayerLevelUp(playerId, type, newLevel);
        }
    }
    
    /**
     * Berechnet Slayer Level basierend auf XP
     */
    private int calculateSlayerLevel(int totalXP) {
        int level = 0;
        int xpNeeded = 0;
        
        for (int i = 1; i <= 9; i++) {
            xpNeeded += getXpRequiredForLevel(i);
            if (totalXP >= xpNeeded) {
                level = i;
            } else {
                break;
            }
        }
        
        return level;
    }
    
    /**
     * Gibt XP-Anforderung für ein Level zurück
     */
    private int getXpRequiredForLevel(int level) {
        return switch (level) {
            case 1 -> 100;
            case 2 -> 200;
            case 3 -> 500;
            case 4 -> 1000;
            case 5 -> 2000;
            case 6 -> 5000;
            case 7 -> 10000;
            case 8 -> 20000;
            case 9 -> 50000;
            default -> 0;
        };
    }
    
    /**
     * Wird aufgerufen wenn ein Spieler ein Slayer Level aufsteigt
     */
    private void onSlayerLevelUp(UUID playerId, SlayerType type, int newLevel) {
        Player player = plugin.getServer().getPlayer(playerId);
        if (player != null) {
            player.sendMessage("§6§lSLAYER LEVEL UP!");
            player.sendMessage("§e" + type.getIcon() + " " + type.getDisplayName() + " Level " + newLevel);
            player.sendMessage("§7" + type.getDescription());
            
            // Spiele Sound und Effekte
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }
    }
    
    /**
     * Gibt das Slayer Level eines Spielers zurück
     */
    public int getSlayerLevel(UUID playerId, SlayerType type) {
        return playerSlayerLevels.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>())
            .getOrDefault(type, 0);
    }
    
    /**
     * Gibt die Slayer XP eines Spielers zurück
     */
    public int getSlayerXP(UUID playerId, SlayerType type) {
        return playerSlayerXP.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>())
            .getOrDefault(type, 0);
    }
    
    /**
     * Gibt die aktive Quest eines Spielers zurück
     */
    public SlayerQuest getActiveQuest(UUID playerId) {
        return activeQuests.get(playerId);
    }
    
    /**
     * Gibt alle Slayer Bosse zurück
     */
    public Map<String, SlayerBoss> getAllSlayerBosses() {
        return new HashMap<>(slayerBosses);
    }
    
    /**
     * Gibt Slayer Bosse eines bestimmten Typs zurück
     */
    public List<SlayerBoss> getSlayerBossesByType(SlayerType type) {
        return slayerBosses.values().stream()
            .filter(boss -> boss.getType() == type)
            .toList();
    }
    
    // Helper Methods
    private EntityType getTargetEntityForSlayer(SlayerType type) {
        return switch (type) {
            case ZOMBIE -> EntityType.ZOMBIE;
            case SPIDER -> EntityType.SPIDER;
            case WOLF -> EntityType.WOLF;
            case ENDERMAN -> EntityType.ENDERMAN;
            case BLAZE -> EntityType.BLAZE;
        };
    }
    
    private int getRequiredKillsForTier(int tier) {
        return switch (tier) {
            case 1 -> 10;
            case 2 -> 25;
            case 3 -> 50;
            case 4 -> 100;
            case 5 -> 200;
            default -> 10;
        };
    }
    
    private int getXpRewardForTier(int tier) {
        return switch (tier) {
            case 1 -> 100;
            case 2 -> 200;
            case 3 -> 500;
            case 4 -> 1000;
            case 5 -> 2000;
            default -> 100;
        };
    }
    
    private int getCoinRewardForTier(int tier) {
        return switch (tier) {
            case 1 -> 1000;
            case 2 -> 2000;
            case 3 -> 5000;
            case 4 -> 10000;
            case 5 -> 20000;
            default -> 1000;
        };
    }
}
