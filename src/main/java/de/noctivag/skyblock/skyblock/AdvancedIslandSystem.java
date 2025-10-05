package de.noctivag.skyblock.skyblock;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Island System - Hypixel Skyblock Style
 */
public class AdvancedIslandSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerIsland> playerIslands = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> islandTasks = new ConcurrentHashMap<>();
    
    public AdvancedIslandSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        startIslandUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void startIslandUpdateTask() {
        // Use virtual thread for Folia compatibility
        Thread.ofVirtual().start(() -> {
            try {
                while (SkyblockPlugin.isEnabled()) {
                    for (Map.Entry<UUID, PlayerIsland> entry : playerIslands.entrySet()) {
                        PlayerIsland island = entry.getValue();
                        island.update();
                    }
                    Thread.sleep(1000); // Every second = 1000 ms
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ENCHANTING_TABLE) {
            openIslandGUI(player);
        }
    }
    
    private void openIslandGUI(Player player) {
        player.sendMessage(Component.text("§aIsland GUI geöffnet!"));
    }
    
    public void createIsland(Player player, IslandType type) {
        UUID islandId = UUID.randomUUID();
        PlayerIsland island = new PlayerIsland(islandId, player.getUniqueId(), type);
        playerIslands.put(islandId, island);
        
        player.sendMessage(Component.text("§aIsland erstellt!"));
    }
    
    public void teleportToIsland(Player player, UUID islandId) {
        PlayerIsland island = playerIslands.get(islandId);
        if (island == null) {
            player.sendMessage(Component.text("§cIsland nicht gefunden!"));
            return;
        }
        
        // Teleport player to island
        player.teleport(island.getSpawnLocation());
        player.sendMessage(Component.text("§aZu Island teleportiert!"));
    }
    
    public PlayerIsland getPlayerIsland(UUID playerId) {
        return playerIslands.values().stream()
                .filter(island -> island.getOwner().equals(playerId))
                .findFirst()
                .orElse(null);
    }
    
    public List<PlayerIsland> getAllIslands() {
        return new ArrayList<>(playerIslands.values());
    }
    
    public enum IslandType {
        BASIC("§fBasic", Material.GRASS_BLOCK, 50, 50),
        COAL("§8Coal", Material.COAL_BLOCK, 100, 100),
        IRON("§fIron", Material.IRON_BLOCK, 150, 150),
        GOLD("§6Gold", Material.GOLD_BLOCK, 200, 200),
        DIAMOND("§bDiamond", Material.DIAMOND_BLOCK, 250, 250),
        EMERALD("§aEmerald", Material.EMERALD_BLOCK, 300, 300),
        NETHER("§cNether", Material.NETHERRACK, 350, 350),
        END("§5End", Material.END_STONE, 400, 400);
        
        private final String displayName;
        private final Material icon;
        private final int width;
        private final int height;
        
        IslandType(String displayName, Material icon, int width, int height) {
            this.displayName = displayName;
            this.icon = icon;
            this.width = width;
            this.height = height;
        }
        
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public int getWidth() { return width; }
        public int getHeight() { return height; }
    }
    
    public static class PlayerIsland {
        private final UUID id;
        private final UUID owner;
        private final IslandType type;
        private final Location spawnLocation;
        private final Set<UUID> members = new HashSet<>();
        private final Map<UUID, IslandRole> memberRoles = new ConcurrentHashMap<>();
        private int level = 1;
        private long experience = 0;
        private long lastUpdate;
        
        public PlayerIsland(UUID id, UUID owner, IslandType type) {
            this.id = id;
            this.owner = owner;
            this.type = type;
            this.spawnLocation = new Location(Bukkit.getWorlds().get(0), 0, 100, 0); // Placeholder
            this.lastUpdate = java.lang.System.currentTimeMillis();
            
            members.add(owner);
            memberRoles.put(owner, IslandRole.OWNER);
        }
        
        public void update() {
            long currentTime = java.lang.System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;
            
            if (timeDiff >= 60000) {
                saveToDatabase();
                lastUpdate = currentTime;
            }
        }
        
        private void saveToDatabase() {
            // Save island data to database
        }
        
        public void addMember(UUID playerId, IslandRole role) {
            members.add(playerId);
            memberRoles.put(playerId, role);
        }
        
        public void removeMember(UUID playerId) {
            members.remove(playerId);
            memberRoles.remove(playerId);
        }
        
        public void setMemberRole(UUID playerId, IslandRole role) {
            memberRoles.put(playerId, role);
        }
        
        public void addExperience(long exp) {
            experience += exp;
            checkLevelUp();
        }
        
        private void checkLevelUp() {
            long requiredExp = level * 1000;
            if (experience >= requiredExp) {
                level++;
                experience -= requiredExp;
            }
        }
        
        public UUID getId() { return id; }
        public UUID getOwner() { return owner; }
        public IslandType getType() { return type; }
        public Location getSpawnLocation() { return spawnLocation; }
        public Set<UUID> getMembers() { return members; }
        public Map<UUID, IslandRole> getMemberRoles() { return memberRoles; }
        public int getLevel() { return level; }
        public long getExperience() { return experience; }
    }
    
    public enum IslandRole {
        OWNER("§cOwner", 4),
        CO_OWNER("§6Co-Owner", 3),
        MODERATOR("§aModerator", 2),
        MEMBER("§7Member", 1);
        
        private final String displayName;
        private final int level;
        
        IslandRole(String displayName, int level) {
            this.displayName = displayName;
            this.level = level;
        }
        
        public String getDisplayName() { return displayName; }
        public int getLevel() { return level; }
    }
}
