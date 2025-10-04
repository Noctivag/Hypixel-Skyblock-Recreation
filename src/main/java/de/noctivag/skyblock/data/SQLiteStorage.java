package de.noctivag.skyblock.data;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.skyblock.SkyblockIsland;
import de.noctivag.skyblock.skyblock.SkyblockProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Level;

public class SQLiteStorage {
    private static Connection connection;
    private static SkyblockPlugin plugin;

    public static synchronized void init(SkyblockPlugin pluginInstance) {
        plugin = pluginInstance;
        try {
            File dataDir = new File(plugin.getDataFolder(), "data");
            if (!dataDir.exists()) dataDir.mkdirs();
            File dbFile = new File(dataDir, "skyblock.db");
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            connection = DriverManager.getConnection(url);
            createTables();
            plugin.getLogger().info("SQLiteStorage initialized at " + dbFile.getAbsolutePath());
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize SQLite storage", e);
        }
    }

    private static void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS profiles ("
                + "uuid TEXT PRIMARY KEY,"
                + "playerName TEXT,"
                + "createdAt TEXT,"
                + "lastPlayed TEXT,"
                + "playtime INTEGER,"
                + "coins REAL,"
                + "islandLevel INTEGER,"
                + "firstJoin INTEGER"
                + ");");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS islands ("
                + "owner TEXT PRIMARY KEY,"
                + "world TEXT,"
                + "spawnX REAL, spawnY REAL, spawnZ REAL,"
                + "minX REAL, minY REAL, minZ REAL,"
                + "maxX REAL, maxY REAL, maxZ REAL,"
                + "size INTEGER,"
                + "hasVisitors INTEGER"
                + ");");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS island_trust ("
                + "owner TEXT,"
                + "member TEXT,"
                + "PRIMARY KEY(owner, member)"
                + ");");
        }
    }

    public static synchronized void saveProfile(SkyblockProfile profile) {
        if (connection == null) return;
        String sql = "INSERT OR REPLACE INTO profiles (uuid, playerName, createdAt, lastPlayed, playtime, coins, islandLevel, firstJoin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, profile.getUuid().toString());
            ps.setString(2, profile.getPlayerName());
            ps.setString(3, profile.getCreatedAt().toString());
            ps.setString(4, profile.getLastPlayed() != null ? profile.getLastPlayed().toString() : LocalDateTime.now().toString());
            ps.setInt(5, profile.getPlaytime());
            ps.setDouble(6, profile.getCoins());
            ps.setInt(7, profile.getIslandLevel());
            ps.setInt(8, profile.isFirstJoin() ? 1 : 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save profile to SQLite for " + profile.getUuid(), e);
        }
    }

    public static synchronized SkyblockProfile loadProfile(UUID uuid) {
        if (connection == null) return null;
        String sql = "SELECT * FROM profiles WHERE uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                String name = rs.getString("playerName");
                SkyblockProfile profile = new SkyblockProfile(uuid, name != null ? name : "Unknown");

                String lastPlayed = rs.getString("lastPlayed");
                if (lastPlayed != null) {
                    try {
                        profile.updateLastPlayed(); // keep current API, but we don't have setter for createdAt
                    } catch (Exception ignored) {}
                }
                // set non-final fields via reflection is avoided; keep `SkyblockProfile` semantics
                // Use profile.save() and profile.load() fallback where needed
                return profile;
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load profile from SQLite for " + uuid, e);
            return null;
        }
    }

    public static synchronized void saveIsland(SkyblockIsland island) {
        if (connection == null) return;
        String sql = "INSERT OR REPLACE INTO islands (owner, world, spawnX, spawnY, spawnZ, minX, minY, minZ, maxX, maxY, maxZ, size, hasVisitors) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, island.getOwner().toString());
            Location spawn = island.getSpawnLocation();
            ps.setString(2, spawn.getWorld() != null ? spawn.getWorld().getName() : "");
            ps.setDouble(3, spawn.getX());
            ps.setDouble(4, spawn.getY());
            ps.setDouble(5, spawn.getZ());
            Location min = island.getMinBound();
            ps.setDouble(6, min.getX());
            ps.setDouble(7, min.getY());
            ps.setDouble(8, min.getZ());
            Location max = island.getMaxBound();
            ps.setDouble(9, max.getX());
            ps.setDouble(10, max.getY());
            ps.setDouble(11, max.getZ());
            ps.setInt(12, island.getSize());
            ps.setInt(13, island.hasVisitors() ? 1 : 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save island to SQLite for " + island.getOwner(), e);
        }
    }

    public static synchronized SkyblockIsland loadIsland(UUID owner) {
        if (connection == null) return null;
        String sql = "SELECT * FROM islands WHERE owner = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, owner.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                String worldName = rs.getString("world");
                double sx = rs.getDouble("spawnX");
                double sy = rs.getDouble("spawnY");
                double sz = rs.getDouble("spawnZ");
                World world = Bukkit.getWorld(worldName);
                if (world == null) return null;
                Location spawn = new Location(world, sx, sy, sz);
                SkyblockIsland island = new SkyblockIsland(owner, spawn);
                int size = rs.getInt("size");
                // expand island to match stored size
                while (island.getSize() < size) island.expandIsland();
                island.setHasVisitors(rs.getInt("hasVisitors") == 1);
                // Load trusted members for this island
                try {
                    java.util.Set<UUID> trusted = loadTrusted(owner);
                    island.loadTrustedMembers(trusted);
                } catch (Exception ignored) {}
                return island;
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load island from SQLite for " + owner, e);
            return null;
        }
    }

    public static synchronized void addTrusted(UUID owner, UUID member) {
        if (connection == null) return;
        String sql = "INSERT OR REPLACE INTO island_trust (owner, member) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, owner.toString());
            ps.setString(2, member.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to add trusted member to SQLite for " + owner, e);
        }
    }

    public static synchronized void removeTrusted(UUID owner, UUID member) {
        if (connection == null) return;
        String sql = "DELETE FROM island_trust WHERE owner = ? AND member = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, owner.toString());
            ps.setString(2, member.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to remove trusted member from SQLite for " + owner, e);
        }
    }

    public static synchronized java.util.Set<UUID> loadTrusted(UUID owner) {
        java.util.Set<UUID> result = new java.util.HashSet<>();
        if (connection == null) return result;
        String sql = "SELECT member FROM island_trust WHERE owner = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, owner.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String m = rs.getString("member");
                    try { result.add(UUID.fromString(m)); } catch (Exception ignored) {}
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load trusted members from SQLite for " + owner, e);
        }
        return result;
    }

    public static synchronized void close() {
        if (connection != null) {
            try { connection.close(); } catch (SQLException ignored) {}
        }
    }
}
