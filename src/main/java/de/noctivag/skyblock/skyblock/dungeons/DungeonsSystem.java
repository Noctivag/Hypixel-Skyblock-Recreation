package de.noctivag.skyblock.skyblock.dungeons;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dungeons System für Hypixel SkyBlock
 */
public class DungeonsSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<UUID, DungeonRun> activeRuns = new ConcurrentHashMap<>();
    private final Map<String, Dungeon> dungeons = new HashMap<>();
    
    // Dungeon Definition
    public static class Dungeon {
        private final String id;
        private final String name;
        private final String description;
        private final int floor;
        private final int maxPlayers;
        private final int minLevel;
        private final List<DungeonRoom> rooms;
        private final DungeonBoss boss;
        private final List<ItemStack> rewards;
        private final int xpReward;
        private final int coinReward;
        
        public Dungeon(String id, String name, String description, int floor, int maxPlayers, 
                      int minLevel, List<DungeonRoom> rooms, DungeonBoss boss, 
                      List<ItemStack> rewards, int xpReward, int coinReward) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.floor = floor;
            this.maxPlayers = maxPlayers;
            this.minLevel = minLevel;
            this.rooms = rooms;
            this.boss = boss;
            this.rewards = rewards;
            this.xpReward = xpReward;
            this.coinReward = coinReward;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getFloor() { return floor; }
        public int getMaxPlayers() { return maxPlayers; }
        public int getMinLevel() { return minLevel; }
        public List<DungeonRoom> getRooms() { return rooms; }
        public DungeonBoss getBoss() { return boss; }
        public List<ItemStack> getRewards() { return rewards; }
        public int getXpReward() { return xpReward; }
        public int getCoinReward() { return coinReward; }
    }
    
    // Dungeon Room Definition
    public static class DungeonRoom {
        private final String id;
        private final String name;
        private final RoomType type;
        private final List<EntityType> enemies;
        private final int enemyCount;
        private final List<ItemStack> rewards;
        private final boolean isCompleted;
        
        public DungeonRoom(String id, String name, RoomType type, List<EntityType> enemies, 
                          int enemyCount, List<ItemStack> rewards) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.enemies = enemies;
            this.enemyCount = enemyCount;
            this.rewards = rewards;
            this.isCompleted = false;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public RoomType getType() { return type; }
        public List<EntityType> getEnemies() { return enemies; }
        public int getEnemyCount() { return enemyCount; }
        public List<ItemStack> getRewards() { return rewards; }
        public boolean isCompleted() { return isCompleted; }
    }
    
    // Room Types
    public enum RoomType {
        NORMAL("Normal", "Standard Raum mit Feinden"),
        PUZZLE("Puzzle", "Rätsel-Raum"),
        SECRET("Secret", "Geheimer Raum"),
        BOSS("Boss", "Boss-Raum"),
        BLOOD("Blood", "Blut-Raum"),
        TRAP("Trap", "Fallen-Raum");
        
        private final String name;
        private final String description;
        
        RoomType(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
    }
    
    // Dungeon Boss Definition
    public static class DungeonBoss {
        private final String name;
        private final EntityType entityType;
        private final double health;
        private final double damage;
        private final List<ItemStack> drops;
        private final int xpReward;
        private final int coinReward;
        
        public DungeonBoss(String name, EntityType entityType, double health, double damage, 
                          List<ItemStack> drops, int xpReward, int coinReward) {
            this.name = name;
            this.entityType = entityType;
            this.health = health;
            this.damage = damage;
            this.drops = drops;
            this.xpReward = xpReward;
            this.coinReward = coinReward;
        }
        
        // Getters
        public String getName() { return name; }
        public EntityType getEntityType() { return entityType; }
        public double getHealth() { return health; }
        public double getDamage() { return damage; }
        public List<ItemStack> getDrops() { return drops; }
        public int getXpReward() { return xpReward; }
        public int getCoinReward() { return coinReward; }
    }
    
    // Dungeon Run Definition
    public static class DungeonRun {
        private final UUID runId;
        private final Dungeon dungeon;
        private final List<UUID> players;
        private final long startTime;
        private final Map<String, Boolean> completedRooms;
        private final Map<UUID, Integer> playerKills;
        private final Map<UUID, Integer> playerDeaths;
        private final DungeonScore score;
        
        public DungeonRun(UUID runId, Dungeon dungeon, List<UUID> players) {
            this.runId = runId;
            this.dungeon = dungeon;
            this.players = players;
            this.startTime = java.lang.System.currentTimeMillis();
            this.completedRooms = new ConcurrentHashMap<>();
            this.playerKills = new ConcurrentHashMap<>();
            this.playerDeaths = new ConcurrentHashMap<>();
            this.score = new DungeonScore();
            
            // Initialisiere Spieler-Statistiken
            for (UUID playerId : players) {
                playerKills.put(playerId, 0);
                playerDeaths.put(playerId, 0);
            }
        }
        
        // Getters
        public UUID getRunId() { return runId; }
        public Dungeon getDungeon() { return dungeon; }
        public List<UUID> getPlayers() { return players; }
        public long getStartTime() { return startTime; }
        public Map<String, Boolean> getCompletedRooms() { return completedRooms; }
        public Map<UUID, Integer> getPlayerKills() { return playerKills; }
        public Map<UUID, Integer> getPlayerDeaths() { return playerDeaths; }
        public DungeonScore getScore() { return score; }
        
        public long getDuration() {
            return java.lang.System.currentTimeMillis() - startTime;
        }
        
        public boolean isCompleted() {
            return completedRooms.values().stream().allMatch(Boolean::booleanValue);
        }
    }
    
    // Dungeon Score Definition
    public static class DungeonScore {
        private int kills;
        private int deaths;
        private int secrets;
        private int puzzles;
        private long time;
        private int totalScore;
        
        public DungeonScore() {
            this.kills = 0;
            this.deaths = 0;
            this.secrets = 0;
            this.puzzles = 0;
            this.time = 0;
            this.totalScore = 0;
        }
        
        // Getters and Setters
        public int getKills() { return kills; }
        public void setKills(int kills) { this.kills = kills; }
        public int getDeaths() { return deaths; }
        public void setDeaths(int deaths) { this.deaths = deaths; }
        public int getSecrets() { return secrets; }
        public void setSecrets(int secrets) { this.secrets = secrets; }
        public int getPuzzles() { return puzzles; }
        public void setPuzzles(int puzzles) { this.puzzles = puzzles; }
        public long getTime() { return time; }
        public void setTime(long time) { this.time = time; }
        public int getTotalScore() { return totalScore; }
        public void setTotalScore(int totalScore) { this.totalScore = totalScore; }
        
        public void calculateTotalScore() {
            totalScore = (kills * 10) - (deaths * 5) + (secrets * 50) + (puzzles * 25);
        }
    }
    
    public DungeonsSystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        SkyblockPlugin.getServer().getPluginManager().registerEvents(this, SkyblockPlugin);
        initializeDungeons();
    }
    
    /**
     * Initialisiert alle Dungeons
     */
    private void initializeDungeons() {
        // Catacombs Floor 1
        List<DungeonRoom> floor1Rooms = Arrays.asList(
            new DungeonRoom("f1_r1", "Entrance", RoomType.NORMAL, 
                Arrays.asList(EntityType.ZOMBIE, EntityType.SKELETON), 10,
                Arrays.asList(new ItemStack(Material.ROTTEN_FLESH, 5))),
            new DungeonRoom("f1_r2", "Corridor", RoomType.NORMAL, 
                Arrays.asList(EntityType.ZOMBIE, EntityType.SPIDER), 8,
                Arrays.asList(new ItemStack(Material.BONE, 3))),
            new DungeonRoom("f1_r3", "Secret Room", RoomType.SECRET, 
                Arrays.asList(EntityType.SKELETON), 5,
                Arrays.asList(new ItemStack(Material.ARROW, 10))),
            new DungeonRoom("f1_r4", "Boss Room", RoomType.BOSS, 
                Arrays.asList(EntityType.ZOMBIE), 1,
                Arrays.asList(new ItemStack(Material.DIAMOND, 2)))
        );
        
        DungeonBoss floor1Boss = new DungeonBoss("Bonzo", EntityType.ZOMBIE, 1000, 50,
            Arrays.asList(new ItemStack(Material.DIAMOND, 5), new ItemStack(Material.GOLD_INGOT, 10)),
            500, 5000);
        
        Dungeon catacombsF1 = new Dungeon("catacombs_f1", "Catacombs Floor 1", 
            "Der erste Boden der Catacombs", 1, 5, 0, floor1Rooms, floor1Boss,
            Arrays.asList(new ItemStack(Material.DIAMOND, 10), new ItemStack(Material.GOLD_INGOT, 20)),
            1000, 10000);
        
        dungeons.put("catacombs_f1", catacombsF1);
        
        // Catacombs Floor 2
        List<DungeonRoom> floor2Rooms = Arrays.asList(
            new DungeonRoom("f2_r1", "Entrance", RoomType.NORMAL, 
                Arrays.asList(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER), 15,
                Arrays.asList(new ItemStack(Material.ROTTEN_FLESH, 8))),
            new DungeonRoom("f2_r2", "Corridor", RoomType.NORMAL, 
                Arrays.asList(EntityType.ZOMBIE, EntityType.SKELETON), 12,
                Arrays.asList(new ItemStack(Material.BONE, 5))),
            new DungeonRoom("f2_r3", "Puzzle Room", RoomType.PUZZLE, 
                Arrays.asList(EntityType.SKELETON), 8,
                Arrays.asList(new ItemStack(Material.ARROW, 15))),
            new DungeonRoom("f2_r4", "Secret Room", RoomType.SECRET, 
                Arrays.asList(EntityType.SPIDER), 6,
                Arrays.asList(new ItemStack(Material.STRING, 10))),
            new DungeonRoom("f2_r5", "Boss Room", RoomType.BOSS, 
                Arrays.asList(EntityType.ZOMBIE), 1,
                Arrays.asList(new ItemStack(Material.DIAMOND, 3)))
        );
        
        DungeonBoss floor2Boss = new DungeonBoss("Scarf", EntityType.ZOMBIE, 2000, 100,
            Arrays.asList(new ItemStack(Material.DIAMOND, 8), new ItemStack(Material.GOLD_INGOT, 15)),
            750, 7500);
        
        Dungeon catacombsF2 = new Dungeon("catacombs_f2", "Catacombs Floor 2", 
            "Der zweite Boden der Catacombs", 2, 5, 5, floor2Rooms, floor2Boss,
            Arrays.asList(new ItemStack(Material.DIAMOND, 15), new ItemStack(Material.GOLD_INGOT, 30)),
            1500, 15000);
        
        dungeons.put("catacombs_f2", catacombsF2);
        
        // Weitere Floors...
        // (Hier würden weitere Floors hinzugefügt werden)
    }
    
    /**
     * Startet eine Dungeon-Run
     */
    public boolean startDungeonRun(Player player, String dungeonId) {
        Dungeon dungeon = dungeons.get(dungeonId);
        if (dungeon == null) {
            player.sendMessage(Component.text("§cDungeon nicht gefunden!"));
            return false;
        }
        
        // Prüfe ob bereits eine Run aktiv ist
        if (activeRuns.containsKey(player.getUniqueId())) {
            player.sendMessage(Component.text("§cDu hast bereits eine aktive Dungeon-Run!"));
            return false;
        }
        
        // Prüfe Mindest-Level
        // Hier würde normalerweise das Catacombs-Level geprüft werden
        
        // Erstelle Dungeon-Run
        UUID runId = UUID.randomUUID();
        List<UUID> players = Arrays.asList(player.getUniqueId());
        DungeonRun run = new DungeonRun(runId, dungeon, players);
        activeRuns.put(player.getUniqueId(), run);
        
        player.sendMessage(Component.text("§a§lDUNGEON RUN GESTARTET!"));
        player.sendMessage("§e" + dungeon.getName());
        player.sendMessage("§7" + dungeon.getDescription());
        player.sendMessage("§7Spieler: " + players.size() + "/" + dungeon.getMaxPlayers());
        
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
            
            // Prüfe aktive Dungeon-Run
            DungeonRun run = activeRuns.get(playerId);
            if (run != null) {
                // Erhöhe Kill-Count
                int currentKills = run.getPlayerKills().getOrDefault(playerId, 0);
                run.getPlayerKills().put(playerId, currentKills + 1);
                
                // Erhöhe Score
                run.getScore().setKills(run.getScore().getKills() + 1);
                run.getScore().calculateTotalScore();
                
                player.sendMessage("§a+" + (currentKills + 1) + " Kills");
            }
        }
    }
    
    /**
     * Event-Handler für Player-Move (für Room-Completion)
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        // Prüfe aktive Dungeon-Run
        DungeonRun run = activeRuns.get(playerId);
        if (run != null) {
            // Hier würde die Room-Completion-Logik implementiert werden
            // basierend auf der Position des Spielers
        }
    }
    
    /**
     * Schließt eine Dungeon-Run ab
     */
    public void completeDungeonRun(UUID playerId) {
        DungeonRun run = activeRuns.get(playerId);
        if (run == null) return;
        
        // Entferne Run
        activeRuns.remove(playerId);
        
        // Berechne finale Belohnungen
        run.getScore().setTime(run.getDuration());
        run.getScore().calculateTotalScore();
        
        Player player = SkyblockPlugin.getServer().getPlayer(playerId);
        if (player != null) {
            player.sendMessage(Component.text("§6§lDUNGEON RUN ABGESCHLOSSEN!"));
            player.sendMessage("§e" + run.getDungeon().getName());
            player.sendMessage("§7Zeit: " + formatTime(run.getDuration()));
            player.sendMessage("§7Kills: " + run.getScore().getKills());
            player.sendMessage("§7Tode: " + run.getScore().getDeaths());
            player.sendMessage("§7Score: " + run.getScore().getTotalScore());
            
            // Gib Belohnungen
            for (ItemStack reward : run.getDungeon().getRewards()) {
                player.getInventory().addItem(reward);
            }
            
            player.sendMessage("§a+" + run.getDungeon().getXpReward() + " Catacombs XP");
            player.sendMessage("§6+" + run.getDungeon().getCoinReward() + " Coins");
        }
    }
    
    /**
     * Formatiert Zeit in lesbares Format
     */
    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        
        return String.format("%d:%02d", minutes, seconds);
    }
    
    /**
     * Gibt alle Dungeons zurück
     */
    public Map<String, Dungeon> getAllDungeons() {
        return new HashMap<>(dungeons);
    }
    
    /**
     * Gibt ein Dungeon zurück
     */
    public Dungeon getDungeon(String dungeonId) {
        return dungeons.get(dungeonId);
    }
    
    /**
     * Gibt die aktive Run eines Spielers zurück
     */
    public DungeonRun getActiveRun(UUID playerId) {
        return activeRuns.get(playerId);
    }
    
    /**
     * Gibt alle aktiven Runs zurück
     */
    public Map<UUID, DungeonRun> getActiveRuns() {
        return new HashMap<>(activeRuns);
    }
}
