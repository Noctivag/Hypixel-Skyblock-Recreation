package de.noctivag.skyblock.dungeons;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Complete Dungeon System - Full Implementation with Instance Management, Boss Mechanics, and Party Integration
 * 
 * Features:
 * - Complete dungeon instance management
 * - Advanced boss fight mechanics
 * - Party system with roles
 * - Comprehensive loot system
 * - Multi-floor progression
 * - Dungeon classes and abilities
 */
public class CompleteDungeonSystem implements Listener {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerDungeonData> playerDungeonData = new ConcurrentHashMap<>();
    private final Map<UUID, DungeonParty> activeParties = new ConcurrentHashMap<>();
    private final Map<UUID, DungeonInstance> activeInstances = new ConcurrentHashMap<>();
    private final Map<UUID, DungeonBoss> activeBosses = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> dungeonTasks = new ConcurrentHashMap<>();
    
    public CompleteDungeonSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        startDungeonUpdateTask();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void startDungeonUpdateTask() {
        Thread.ofVirtual().start(() -> {
            while (plugin.isEnabled()) {
                try {
                    updateActiveInstances();
                    updateActiveParties();
                    updateActiveBosses();
                    Thread.sleep(1000); // Every second = 1000 ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }
    
    private void updateActiveInstances() {
        List<UUID> expiredInstances = new ArrayList<>();
        
        for (Map.Entry<UUID, DungeonInstance> entry : activeInstances.entrySet()) {
            DungeonInstance instance = entry.getValue();
            if (instance.isActive()) {
                instance.update();
            } else {
                expiredInstances.add(entry.getKey());
            }
        }
        
        // Remove expired instances
        for (UUID instanceId : expiredInstances) {
            activeInstances.remove(instanceId);
        }
    }
    
    private void updateActiveParties() {
        List<UUID> expiredParties = new ArrayList<>();
        
        for (Map.Entry<UUID, DungeonParty> entry : activeParties.entrySet()) {
            DungeonParty party = entry.getValue();
            if (party.isActive()) {
                party.update();
            } else {
                expiredParties.add(entry.getKey());
            }
        }
        
        // Remove expired parties
        for (UUID partyId : expiredParties) {
            activeParties.remove(partyId);
        }
    }
    
    private void updateActiveBosses() {
        List<UUID> deadBosses = new ArrayList<>();
        
        for (Map.Entry<UUID, DungeonBoss> entry : activeBosses.entrySet()) {
            DungeonBoss boss = entry.getValue();
            if (!boss.isAlive()) {
                deadBosses.add(entry.getKey());
            } else {
                boss.update();
            }
        }
        
        // Remove dead bosses
        for (UUID bossId : deadBosses) {
            activeBosses.remove(bossId);
        }
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            Entity entity = event.getEntity();
            
            // Check if it's a dungeon boss
            UUID bossId = entity.getUniqueId();
            if (activeBosses.containsKey(bossId)) {
                DungeonBoss dungeonBoss = activeBosses.get(bossId);
                DungeonInstance instance = findInstanceByBoss(bossId);
                
                if (instance != null) {
                    onDungeonBossDefeated(player, instance, dungeonBoss);
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = LegacyComponentSerializer.legacySection().serialize(meta.displayName());
        
        if (displayName.contains("Dungeon") || displayName.contains("dungeon")) {
            openDungeonGUI(player);
        }
    }
    
    public void openDungeonGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§5§lDungeon System"));
        
        // Add dungeon floors
        addGUIItem(gui, 10, Material.STONE_BRICKS, "§a§lFloor 1 - Entrance", "§7The entrance to the Catacombs\n§7Difficulty: Easy");
        addGUIItem(gui, 11, Material.STONE_BRICKS, "§e§lFloor 2", "§7The second floor of the Catacombs\n§7Difficulty: Easy");
        addGUIItem(gui, 12, Material.STONE_BRICKS, "§6§lFloor 3", "§7The third floor of the Catacombs\n§7Difficulty: Medium");
        addGUIItem(gui, 13, Material.STONE_BRICKS, "§c§lFloor 4", "§7The fourth floor of the Catacombs\n§7Difficulty: Medium");
        addGUIItem(gui, 14, Material.STONE_BRICKS, "§5§lFloor 5", "§7The fifth floor of the Catacombs\n§7Difficulty: Hard");
        addGUIItem(gui, 15, Material.STONE_BRICKS, "§d§lFloor 6", "§7The sixth floor of the Catacombs\n§7Difficulty: Hard");
        addGUIItem(gui, 16, Material.STONE_BRICKS, "§4§lFloor 7", "§7The seventh floor of the Catacombs\n§7Difficulty: Master");
        
        // Add dungeon management items
        addGUIItem(gui, 18, Material.BOOK, "§6§lMy Dungeon Progress", "§7View your dungeon progress.");
        addGUIItem(gui, 19, Material.DIAMOND_SWORD, "§c§lStart Dungeon", "§7Start a new dungeon run.");
        addGUIItem(gui, 20, Material.CHEST, "§e§lDungeon Rewards", "§7View available rewards.");
        addGUIItem(gui, 21, Material.EMERALD, "§a§lDungeon Shop", "§7Buy dungeon items.");
        addGUIItem(gui, 22, Material.BARRIER, "§c§lLeave Dungeon", "§7Leave your current dungeon.");
        
        // Add party management
        addGUIItem(gui, 27, Material.PLAYER_HEAD, "§b§lParty Management", "§7Manage your dungeon party.");
        addGUIItem(gui, 28, Material.ARROW, "§a§lJoin Party", "§7Join a dungeon party.");
        addGUIItem(gui, 29, Material.BARRIER, "§c§lLeave Party", "§7Leave your current party.");
        
        // Add class selection
        addGUIItem(gui, 36, Material.DIAMOND_SWORD, "§c§lBerserker", "§7High damage, low defense");
        addGUIItem(gui, 37, Material.BOW, "§a§lArcher", "§7Ranged damage, medium defense");
        addGUIItem(gui, 38, Material.STICK, "§b§lMage", "§7Magic damage, low defense");
        addGUIItem(gui, 39, Material.SHIELD, "§e§lTank", "§7High defense, low damage");
        addGUIItem(gui, 40, Material.GOLDEN_APPLE, "§d§lHealer", "§7Healing support, medium defense");
        
        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the dungeon menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage("§aDungeon GUI opened!");
    }
    
    private DungeonInstance findInstanceByBoss(UUID bossId) {
        for (DungeonInstance instance : activeInstances.values()) {
            if (instance.getBossId() != null && instance.getBossId().equals(bossId)) {
                return instance;
            }
        }
        return null;
    }
    
    private void onDungeonBossDefeated(Player player, DungeonInstance instance, DungeonBoss dungeonBoss) {
        // Complete dungeon
        instance.complete();
        
        // Give rewards to all party members
        DungeonParty party = instance.getParty();
        if (party != null) {
            for (UUID memberId : party.getMembers()) {
                Player member = Bukkit.getPlayer(memberId);
                if (member != null) {
                    giveDungeonRewards(member, instance.getFloor());
                }
            }
        }
        
        // Send completion message
        player.sendMessage("§a§lDUNGEON COMPLETED!");
        player.sendMessage("§7Floor " + instance.getFloor().getFloor() + " completed!");
        
        // Play sound
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        
        // Remove instance
        activeInstances.remove(instance.getInstanceId());
    }
    
    private void giveDungeonRewards(Player player, DungeonFloor floor) {
        PlayerDungeonData data = getPlayerDungeonData(player.getUniqueId());
        
        // Give XP
        int xpReward = floor.getFloor() * 100;
        data.addDungeonXP(xpReward);
        
        // Give items
        ItemStack reward = new ItemStack(Material.DIAMOND, floor.getFloor());
        player.getInventory().addItem(reward);
        
        player.sendMessage("§a§lDUNGEON REWARDS!");
        player.sendMessage("§6+" + xpReward + " Dungeon XP");
        player.sendMessage("§a+" + floor.getFloor() + " Diamonds");
    }
    
    public boolean startDungeonRun(Player player, DungeonFloor floor) {
        UUID playerId = player.getUniqueId();
        
        // Check if player already has an active instance
        if (hasActiveInstance(playerId)) {
            player.sendMessage("§cYou already have an active dungeon run!");
            return false;
        }
        
        // Check if player has required level
        PlayerDungeonData data = getPlayerDungeonData(playerId);
        if (data.getDungeonLevel() < floor.getRequiredLevel()) {
            player.sendMessage("§cYou need Dungeon Level " + floor.getRequiredLevel() + " to enter this floor!");
            return false;
        }
        
        // Create dungeon instance
        DungeonInstance instance = new DungeonInstance(playerId, floor);
        activeInstances.put(instance.getInstanceId(), instance);
        
        // Teleport player to dungeon
        Location dungeonLocation = getDungeonLocation(floor);
        player.teleport(dungeonLocation);
        
        // Spawn boss
        spawnDungeonBoss(instance);
        
        // Send messages
        player.sendMessage("§a§lDUNGEON RUN STARTED!");
        player.sendMessage("§7Floor: " + floor.getDisplayName());
        player.sendMessage("§7Difficulty: " + floor.getDifficulty());
        
        return true;
    }
    
    private Location getDungeonLocation(DungeonFloor floor) {
        // Get or create dungeon world
        World dungeonWorld = Bukkit.getWorld("dungeons");
        if (dungeonWorld == null) {
            WorldCreator creator = new WorldCreator("dungeons");
            creator.environment(World.Environment.NORMAL);
            creator.type(WorldType.FLAT);
            dungeonWorld = creator.createWorld();
        }
        
        // Return floor-specific location
        return new Location(dungeonWorld, 0, 64, floor.getFloor() * 100);
    }
    
    private void spawnDungeonBoss(DungeonInstance instance) {
        DungeonFloor floor = instance.getFloor();
        Location spawnLocation = instance.getLocation();
        
        // Spawn boss based on floor
        EntityType bossType = getBossTypeForFloor(floor);
        Entity entity = spawnLocation.getWorld().spawnEntity(spawnLocation, bossType);
        
        if (entity instanceof LivingEntity livingEntity) {
            // Set boss properties
            livingEntity.customName(Component.text(floor.getBossName()));
            livingEntity.setCustomNameVisible(true);
            
            // Set health based on floor
            double health = 100.0 + (floor.getFloor() * 50.0);
            livingEntity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(health);
            livingEntity.setHealth(health);
            
            // Add special effects
            livingEntity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                org.bukkit.potion.PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false
            ));
        }
        
        // Create dungeon boss wrapper
        DungeonBoss dungeonBoss = new DungeonBoss(entity, floor);
        activeBosses.put(entity.getUniqueId(), dungeonBoss);
        
        // Set boss in instance
        instance.setBossId(entity.getUniqueId());
    }
    
    private EntityType getBossTypeForFloor(DungeonFloor floor) {
        return switch (floor) {
            case F1 -> EntityType.ZOMBIE;
            case F2 -> EntityType.SKELETON;
            case F3 -> EntityType.SPIDER;
            case F4 -> EntityType.ENDERMAN;
            case F5 -> EntityType.BLAZE;
            case F6 -> EntityType.WITHER_SKELETON;
            case F7 -> EntityType.WITHER;
        };
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(Component.text(description)));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public PlayerDungeonData getPlayerDungeonData(UUID playerId) {
        return playerDungeonData.computeIfAbsent(playerId, k -> new PlayerDungeonData(playerId));
    }
    
    public DungeonParty getActiveParty(UUID playerId) {
        return activeParties.get(playerId);
    }
    
    public DungeonInstance getActiveInstance(UUID playerId) {
        return activeInstances.get(playerId);
    }
    
    public boolean hasActiveInstance(UUID playerId) {
        return activeInstances.containsKey(playerId);
    }
    
    public boolean createParty(Player leader) {
        UUID leaderId = leader.getUniqueId();
        
        // Check if leader already has a party
        if (activeParties.containsKey(leaderId)) {
            leader.sendMessage("§cYou already have a party!");
            return false;
        }
        
        // Create new party
        DungeonParty party = new DungeonParty(leaderId);
        activeParties.put(leaderId, party);
        
        leader.sendMessage("§a§lPARTY CREATED!");
        leader.sendMessage("§7Use §e/party invite <player> §7to invite players!");
        
        return true;
    }
    
    public boolean joinParty(Player player, UUID leaderId) {
        DungeonParty party = activeParties.get(leaderId);
        if (party == null) {
            player.sendMessage("§cParty not found!");
            return false;
        }
        
        if (party.addMember(player.getUniqueId())) {
            player.sendMessage("§a§lJOINED PARTY!");
            player.sendMessage("§7You joined " + Bukkit.getPlayer(leaderId).getName() + "'s party!");
            return true;
        } else {
            player.sendMessage("§cParty is full!");
            return false;
        }
    }
    
    public boolean leaveParty(Player player) {
        UUID playerId = player.getUniqueId();
        DungeonParty party = activeParties.get(playerId);
        
        if (party == null) {
            player.sendMessage("§cYou are not in a party!");
            return false;
        }
        
        party.removeMember(playerId);
        activeParties.remove(playerId);
        
        player.sendMessage("§a§lLEFT PARTY!");
        return true;
    }
    
    // Enhanced Dungeon Floor Enum
    public enum DungeonFloor {
        F1("§aFloor 1", 1, 1, "Easy", "Entrance Guardian"),
        F2("§eFloor 2", 2, 3, "Easy", "Corrupted Guardian"),
        F3("§6Floor 3", 3, 5, "Medium", "Shadow Assassin"),
        F4("§cFloor 4", 4, 8, "Medium", "Voidgloom Seraph"),
        F5("§5Floor 5", 5, 12, "Hard", "Inferno Demonlord"),
        F6("§dFloor 6", 6, 16, "Hard", "Necron"),
        F7("§4Floor 7", 7, 20, "Master", "The Wither King");
        
        private final String displayName;
        private final int floor;
        private final int requiredLevel;
        private final String difficulty;
        private final String bossName;
        
        DungeonFloor(String displayName, int floor, int requiredLevel, String difficulty, String bossName) {
            this.displayName = displayName;
            this.floor = floor;
            this.requiredLevel = requiredLevel;
            this.difficulty = difficulty;
            this.bossName = bossName;
        }
        
        public String getDisplayName() { return displayName; }
        public int getFloor() { return floor; }
        public int getRequiredLevel() { return requiredLevel; }
        public String getDifficulty() { return difficulty; }
        public String getBossName() { return bossName; }
    }
    
    // Enhanced Dungeon Class Enum
    public enum DungeonClass {
        BERSERKER("§cBerserker", "§7High damage, low defense", Material.DIAMOND_SWORD, 1.5, 0.5),
        ARCHER("§aArcher", "§7Ranged damage, medium defense", Material.BOW, 1.2, 0.8),
        MAGE("§bMage", "§7Magic damage, low defense", Material.STICK, 1.3, 0.6),
        TANK("§eTank", "§7High defense, low damage", Material.SHIELD, 0.7, 2.0),
        HEALER("§dHealer", "§7Healing support, medium defense", Material.GOLDEN_APPLE, 0.8, 1.2);
        
        private final String displayName;
        private final String description;
        private final Material icon;
        private final double damageMultiplier;
        private final double defenseMultiplier;
        
        DungeonClass(String displayName, String description, Material icon, double damageMultiplier, double defenseMultiplier) {
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.damageMultiplier = damageMultiplier;
            this.defenseMultiplier = defenseMultiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public double getDamageMultiplier() { return damageMultiplier; }
        public double getDefenseMultiplier() { return defenseMultiplier; }
    }
    
    // Enhanced Player Dungeon Data Class
    public static class PlayerDungeonData {
        private final UUID playerId;
        private int dungeonLevel;
        private int dungeonXP;
        private DungeonClass selectedClass;
        private final Map<DungeonFloor, Integer> floorCompletions = new HashMap<>();
        private final Map<DungeonFloor, Long> bestTimes = new HashMap<>();
        
        public PlayerDungeonData(UUID playerId) {
            this.playerId = playerId;
            this.dungeonLevel = 1;
            this.dungeonXP = 0;
            this.selectedClass = DungeonClass.BERSERKER;
        }
        
        public UUID getPlayerId() { return playerId; }
        public int getDungeonLevel() { return dungeonLevel; }
        public int getDungeonXP() { return dungeonXP; }
        public DungeonClass getSelectedClass() { return selectedClass; }
        
        public void addDungeonXP(int xp) {
            this.dungeonXP += xp;
            checkLevelUp();
        }
        
        public void setSelectedClass(DungeonClass dungeonClass) {
            this.selectedClass = dungeonClass;
        }
        
        public void completeFloor(DungeonFloor floor, long time) {
            floorCompletions.put(floor, floorCompletions.getOrDefault(floor, 0) + 1);
            
            Long bestTime = bestTimes.get(floor);
            if (bestTime == null || time < bestTime) {
                bestTimes.put(floor, time);
            }
        }
        
        public int getFloorCompletions(DungeonFloor floor) {
            return floorCompletions.getOrDefault(floor, 0);
        }
        
        public long getBestTime(DungeonFloor floor) {
            return bestTimes.getOrDefault(floor, Long.MAX_VALUE);
        }
        
        private void checkLevelUp() {
            int requiredXP = getRequiredXP(dungeonLevel + 1);
            if (dungeonXP >= requiredXP) {
                dungeonLevel++;
            }
        }
        
        private int getRequiredXP(int level) {
            return level * 1000;
        }
    }
    
    // Enhanced Dungeon Party Class
    public static class DungeonParty {
        private final UUID leaderId;
        private final List<UUID> members = new ArrayList<>();
        private boolean active;
        private final Map<UUID, DungeonClass> memberClasses = new HashMap<>();
        
        public DungeonParty(UUID leaderId) {
            this.leaderId = leaderId;
            this.members.add(leaderId);
            this.active = true;
        }
        
        public UUID getLeaderId() { return leaderId; }
        public List<UUID> getMembers() { return new ArrayList<>(members); }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
        
        public boolean addMember(UUID playerId) {
            if (members.size() >= 5) return false;
            members.add(playerId);
            return true;
        }
        
        public void removeMember(UUID playerId) {
            members.remove(playerId);
            memberClasses.remove(playerId);
        }
        
        public void setMemberClass(UUID playerId, DungeonClass dungeonClass) {
            if (members.contains(playerId)) {
                memberClasses.put(playerId, dungeonClass);
            }
        }
        
        public DungeonClass getMemberClass(UUID playerId) {
            return memberClasses.getOrDefault(playerId, DungeonClass.BERSERKER);
        }
        
        public void update() {
            // Party update logic
            if (members.isEmpty()) {
                active = false;
            }
        }
    }
    
    // Enhanced Dungeon Instance Class
    public static class DungeonInstance {
        private final UUID instanceId;
        private final UUID playerId;
        private final DungeonFloor floor;
        private final long startTime;
        private boolean active;
        private UUID bossId;
        private DungeonParty party;
        private Location location;
        
        public DungeonInstance(UUID playerId, DungeonFloor floor) {
            this.instanceId = UUID.randomUUID();
            this.playerId = playerId;
            this.floor = floor;
            this.startTime = System.currentTimeMillis();
            this.active = true;
            this.location = new Location(Bukkit.getWorld("world"), 0, 64, 0);
        }
        
        public UUID getInstanceId() { return instanceId; }
        public UUID getPlayerId() { return playerId; }
        public DungeonFloor getFloor() { return floor; }
        public long getStartTime() { return startTime; }
        public boolean isActive() { return active; }
        public UUID getBossId() { return bossId; }
        public DungeonParty getParty() { return party; }
        public Location getLocation() { return location; }
        
        public void setBossId(UUID bossId) { this.bossId = bossId; }
        public void setParty(DungeonParty party) { this.party = party; }
        public void setLocation(Location location) { this.location = location; }
        
        public void complete() {
            this.active = false;
        }
        
        public void update() {
            // Instance update logic
            if (System.currentTimeMillis() - startTime > 30 * 60 * 1000) { // 30 minutes timeout
                active = false;
            }
        }
    }
    
    // Dungeon Boss Class
    public static class DungeonBoss {
        private final Entity entity;
        private final DungeonFloor floor;
        private long lastUpdate;
        
        public DungeonBoss(Entity entity, DungeonFloor floor) {
            this.entity = entity;
            this.floor = floor;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public Entity getEntity() { return entity; }
        public DungeonFloor getFloor() { return floor; }
        
        public boolean isAlive() {
            return entity != null && entity.isValid() && entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0;
        }
        
        public void update() {
            if (!isAlive()) return;
            
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdate >= 1000) { // Update every second
                lastUpdate = currentTime;
                
                // Boss AI logic
                if (entity instanceof LivingEntity livingEntity) {
                    // Find nearest player
                    Player nearestPlayer = null;
                    double nearestDistance = Double.MAX_VALUE;
                    
                    for (Player player : entity.getWorld().getPlayers()) {
                        double distance = player.getLocation().distance(entity.getLocation());
                        if (distance < nearestDistance && distance <= 50) {
                            nearestDistance = distance;
                            nearestPlayer = player;
                        }
                    }
                    
                    // Attack nearest player
                    if (nearestPlayer != null && nearestDistance <= 10) {
                        if (livingEntity instanceof Mob) {
                            ((Mob) livingEntity).setTarget(nearestPlayer);
                        }
                    }
                    
                    // Special abilities based on floor
                    if (floor.getFloor() >= 3 && Math.random() < 0.1) { // 10% chance every second
                        // Teleport ability
                        if (nearestPlayer != null) {
                            Location teleportLoc = nearestPlayer.getLocation().add(
                                (Math.random() - 0.5) * 10, 0, (Math.random() - 0.5) * 10);
                            entity.teleport(teleportLoc);
                        }
                    }
                    
                    if (floor.getFloor() >= 5 && Math.random() < 0.05) { // 5% chance every second
                        // Explosion ability
                        entity.getWorld().createExplosion(entity.getLocation(), 2.0f, false, false);
                    }
                }
            }
        }
    }
}
