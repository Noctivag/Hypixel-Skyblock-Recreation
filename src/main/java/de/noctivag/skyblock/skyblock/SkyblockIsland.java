package de.noctivag.skyblock.skyblock;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class SkyblockIsland {
    private final UUID owner;
    private final Location spawnLocation;
    private final Location minBound;
    private final Location maxBound;
    private int size;
    private boolean hasVisitors;
    private final Set<UUID> trustedMembers = new HashSet<>();

    // Y-bounds for operations such as reset/protection
    private int minY;
    private int maxY;

    public SkyblockIsland(UUID owner, Location center) {
        this.owner = owner;
        this.size = 100; // Starting island size
        this.spawnLocation = center.clone().add(0, 1, 0);
        this.minBound = center.clone().subtract(size / 2.0, 0, size / 2.0);
        this.maxBound = center.clone().add(size / 2.0, 0, size / 2.0);
        this.hasVisitors = false;

        // sensible default Y-range around the island spawn
        int centerY = center.getBlockY();
        this.minY = Math.max(0, centerY - 10);
        World w = center.getWorld();
        this.maxY = (w != null) ? Math.min(w.getMaxHeight(), centerY + 60) : centerY + 60;
    }

    // Trust management
    public void addTrusted(UUID player) {
        if (player == null) return;
        trustedMembers.add(player);
        try { de.noctivag.plugin.data.SimpleIslandStorage.addTrusted(owner, player); } catch (Exception ignored) {}
    }

    public void removeTrusted(UUID player) {
        if (player == null) return;
        trustedMembers.remove(player);
        try { de.noctivag.plugin.data.SimpleIslandStorage.removeTrusted(owner, player); } catch (Exception ignored) {}
    }

    public boolean isTrusted(UUID player) {
        if (player == null) return false;
        return trustedMembers.contains(player);
    }

    public void loadTrustedMembers(Set<UUID> members) {
        trustedMembers.clear();
        if (members != null) trustedMembers.addAll(members);
    }

    // -----------------------------
    // Island generation and helpers
    // -----------------------------

    /**
     * Generate a small starter island (classic Skyblock layout).
     */
    public void generateStarterIsland() {
        World world = spawnLocation.getWorld();
        if (world == null) return;

        // Folia compatibility: Use virtual thread to defer chunk operations
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(100); // Small delay to ensure world is ready
                
                // Get plugin instance
                org.bukkit.plugin.SkyblockSkyblockPlugin  org.bukkit.Bukkit.getPluginManager().getPlugin("BasicsPlugin");
                if (plugin == null) {
                    System.err.println("BasicsPlugin not found!");
                    return;
                }
                
                // Execute on main thread using Bukkit scheduler (Folia-compatible)
                org.bukkit.Bukkit.getScheduler().runTask(plugin, () -> {
                    try {
                        // Ensure chunk is loaded
                        world.getChunkAt(spawnLocation).load(true);
                        
                        // Generate the classic Skyblock island
                        Location center = spawnLocation.clone().subtract(0, 1, 0);
                        generateIslandBlocks(center);
                    } catch (Exception e) {
                        // Log error but don't crash
                        plugin.getLogger().warning("Error generating starter island: " + e.getMessage());
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                // Log error but don't crash
                System.err.println("Error scheduling island generation: " + e.getMessage());
            }
        });
    }

    private void generateIslandBlocks(Location center) {
        // Main platform (3x3)
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block block = center.clone().add(x, 0, z).getBlock();
                block.setType(Material.GRASS_BLOCK);
            }
        }

        // Add a simple tree (vertical trunk + leaves)
        center.clone().add(0, 1, 0).getBlock().setType(Material.OAK_LOG);
        center.clone().add(0, 2, 0).getBlock().setType(Material.OAK_LOG);
        center.clone().add(0, 3, 0).getBlock().setType(Material.OAK_LOG);
        center.clone().add(0, 4, 0).getBlock().setType(Material.OAK_LOG);

        // Add leaves
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = 2; y <= 4; y++) {
                    if (x == 0 && z == 0) continue; // Skip trunk
                    Block block = center.clone().add(x, y, z).getBlock();
                    if (block.getType() == Material.AIR) {
                        block.setType(Material.OAK_LEAVES);
                    }
                }
            }
        }

        // Add chest with starter items (chest only - inventory handling belongs to plugin logic elsewhere)
        center.clone().add(1, 1, 0).getBlock().setType(Material.CHEST);

        // Add water and lava sources for cobblestone generator
        center.clone().add(-1, 1, 0).getBlock().setType(Material.WATER);
        center.clone().add(0, 1, 1).getBlock().setType(Material.LAVA);
    }

    /**
     * Expand the island's protection bounds by an incremental amount.
     */
    public void expandIsland() {
        if (size >= 1000) return; // Max size

        size += 50;
        Location center = spawnLocation.clone().subtract(0, 1, 0);
        this.minBound.setX(center.getX() - size / 2.0);
        this.minBound.setZ(center.getZ() - size / 2.0);
        this.maxBound.setX(center.getX() + size / 2.0);
        this.maxBound.setZ(center.getZ() + size / 2.0);
    }

    /**
     * Check if a location is within horizontal bounds and Y-bounds of the island.
     */
    public boolean isWithinBounds(Location location) {
        if (location == null) return false;
        if (!location.getWorld().equals(spawnLocation.getWorld())) return false;
        double x = location.getX();
        double z = location.getZ();
        int y = location.getBlockY();
        return x >= minBound.getX() && x <= maxBound.getX() &&
               z >= minBound.getZ() && z <= maxBound.getZ() &&
               y >= minY && y <= maxY;
    }

    public void setHasVisitors(boolean hasVisitors) {
        this.hasVisitors = hasVisitors;
    }

    // Getters
    public UUID getOwner() { return owner; }
    public Location getSpawnLocation() { return spawnLocation; }
    public Location getMinBound() { return minBound; }
    public Location getMaxBound() { return maxBound; }
    public int getSize() { return size; }
    public boolean hasVisitors() { return hasVisitors; }

    // -----------------------------
    // Additional utility features
    // -----------------------------

    /**
     * Teleports a player to this island's safe spawn location. Returns true on success.
     */
    public boolean teleport(Player player) {
        if (player == null) return false;
        Location safe = findSafeSpawn();
        if (safe == null) return false;
        try {
            player.teleport(safe);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Find a safe spawn location at/near the island spawn. This will try to find the highest non-solid block
     * at the island X/Z and return one block above it. Falls back to the configured spawnLocation if necessary.
     */
    public Location findSafeSpawn() {
        World world = spawnLocation.getWorld();
        if (world == null) return spawnLocation.clone();
        Location probe = spawnLocation.clone();

        try {
            // Use world's highest block at the spawn X/Z
            Block highest = world.getHighestBlockAt(probe);
            if (highest.getType() != Material.AIR) {
                Location safe = highest.getLocation().add(0, 1, 0);
                safe.setPitch(spawnLocation.getPitch());
                safe.setYaw(spawnLocation.getYaw());
                return safe;
            }
        } catch (Exception ignored) {}

        // fallback: small upward scan
        int start = Math.max(minY, spawnLocation.getBlockY());
        int end = Math.min(maxY, spawnLocation.getBlockY() + 20);
        for (int y = start; y <= end; y++) {
            Block b = world.getBlockAt(spawnLocation.getBlockX(), y, spawnLocation.getBlockZ());
            if (b.getType() == Material.AIR) {
                return b.getLocation().add(0, 0, 0);
            }
        }

        return spawnLocation.clone();
    }

    /**
     * Reset the island within its bounds to air (keeps bedrock and world border untouched). Use with caution.
     * This is intentionally conservative in Y range to avoid world-wide clearing.
     */
    public void resetIsland() {
        World world = spawnLocation.getWorld();
        if (world == null) return;

        int minX = Math.min(minBound.getBlockX(), maxBound.getBlockX());
        int maxX = Math.max(minBound.getBlockX(), maxBound.getBlockX());
        int minZ = Math.min(minBound.getBlockZ(), maxBound.getBlockZ());
        int maxZ = Math.max(minBound.getBlockZ(), maxBound.getBlockZ());

        int lowerY = Math.max(1, spawnLocation.getBlockY() - 10);
        int upperY = Math.min(world.getMaxHeight(), spawnLocation.getBlockY() + 60);

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = lowerY; y <= upperY; y++) {
                    Block b = world.getBlockAt(x, y, z);
                    Material t = b.getType();
                    if (t == Material.BEDROCK) continue;
                    b.setType(Material.AIR);
                }
            }
        }

        // regenerate a small starter platform so island isn't empty
        generateStarterIsland();
    }

    // -----------------------------
    // Persistence helpers (simple Map serialization)
    // -----------------------------

    /**
     * Convert island data to a plain Map for simple storage (SQLiteStorage or other serialization layers can use this).
     */
    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("owner", owner.toString());
        m.put("size", size);
        m.put("hasVisitors", hasVisitors);
        m.put("world", spawnLocation.getWorld() != null ? spawnLocation.getWorld().getName() : "");
        m.put("x", spawnLocation.getX());
        m.put("y", spawnLocation.getY());
        m.put("z", spawnLocation.getZ());
        m.put("minY", minY);
        m.put("maxY", maxY);
        List<String> trusted = new ArrayList<>();
        for (UUID u : trustedMembers) trusted.add(u.toString());
        m.put("trusted", trusted);
        return m;
    }

    /**
     * Recreate a SkyblockIsland from a Map produced by toMap(). Returns null if required keys are missing.
     */
    @SuppressWarnings("unchecked")
    public static SkyblockIsland fromMap(Map<String, Object> map) {
        if (map == null) return null;
        try {
            String ownerStr = (String) map.get("owner");
            if (ownerStr == null) return null;
            UUID owner = UUID.fromString(ownerStr);

            String worldName = (String) map.getOrDefault("world", "");
            World world = Bukkit.getWorld(worldName);
            double x = ((Number)map.getOrDefault("x", 0)).doubleValue();
            double y = ((Number)map.getOrDefault("y", 64)).doubleValue();
            double z = ((Number)map.getOrDefault("z", 0)).doubleValue();

            Location loc = (world != null) ? new Location(world, x, y, z) : new Location(Bukkit.getWorlds().get(0), x, y, z);

            SkyblockIsland island = new SkyblockIsland(owner, loc.subtract(0, 1, 0));
            island.size = ((Number)map.getOrDefault("size", 100)).intValue();
            island.hasVisitors = (Boolean) map.getOrDefault("hasVisitors", false);
            island.minY = ((Number)map.getOrDefault("minY", island.minY)).intValue();
            island.maxY = ((Number)map.getOrDefault("maxY", island.maxY)).intValue();

            List<String> trusted = (List<String>) map.getOrDefault("trusted", new ArrayList<String>());
            Set<UUID> trustedSet = new HashSet<>();
            for (String s : trusted) {
                try { trustedSet.add(UUID.fromString(s)); } catch (Exception ignored) {}
            }
            island.loadTrustedMembers(trustedSet);

            // Recalculate bounds based on size and spawn
            Location center = island.spawnLocation.clone().subtract(0,1,0);
            island.minBound.setX(center.getX() - island.size / 2.0);
            island.minBound.setZ(center.getZ() - island.size / 2.0);
            island.maxBound.setX(center.getX() + island.size / 2.0);
            island.maxBound.setZ(center.getZ() + island.size / 2.0);

            return island;
        } catch (Exception e) {
            return null;
        }
    }

    public void save() {
        // Save island data
        try {
            de.noctivag.plugin.data.SimpleIslandStorage.saveIsland(this);
        } catch (Exception ignored) {}
    }
}
