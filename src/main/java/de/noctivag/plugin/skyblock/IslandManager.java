package de.noctivag.plugin.skyblock;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * IslandManager with simple in-memory registry plus optional DB persistence.
 * It uses the existing skyblock_islands and island_members tables if a
 * MultiServerDatabaseManager is available and connected.
 */
public class IslandManager {

    private static IslandManager instance;

    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;

    // Map owner UUID -> Island
    private final Map<UUID, Island> islands = new ConcurrentHashMap<>();

    private IslandManager(Plugin plugin) {
        this.plugin = plugin;
        this.databaseManager = plugin.getMultiServerDatabaseManager();
    }

    public static synchronized IslandManager getInstance(Plugin plugin) {
        if (instance == null) instance = new IslandManager(plugin);
        return instance;
    }

    public boolean hasIsland(UUID owner) {
        return islands.containsKey(owner);
    }

    public Island getIslandByOwner(UUID owner) {
        return islands.get(owner);
    }

    /**
     * Find an island by its world name (e.g. island_<uuid>).
     */
    public Island getIslandByWorldName(String worldName) {
        if (worldName == null) return null;
        for (Island isl : islands.values()) {
            if (worldName.equals(isl.getWorldName())) return isl;
        }
        return null;
    }

    public Island getIslandOfPlayer(UUID player) {
        for (Island i : islands.values()) if (i.isMember(player)) return i;
        return null;
    }

    /**
     * Create an island in-memory and persist to DB when possible.
     */
    public synchronized boolean createIsland(UUID owner) {
        if (hasIsland(owner)) return false;
        String islandId = UUID.randomUUID().toString();
        String worldName = getPlayerIslandWorldName(owner);
        Island island = new Island(islandId, owner, worldName);
        islands.put(owner, island);

        // Persist to DB if available (async-write but attempt sync here)
        if (databaseManager != null) {
            try {
                Connection conn = databaseManager.getConnection().get(5, TimeUnit.SECONDS);
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO skyblock_islands (island_id, owner_uuid) VALUES (?, ?) ON DUPLICATE KEY UPDATE owner_uuid = owner_uuid"
                )) {
                    ps.setString(1, islandId);
                    ps.setString(2, owner.toString());
                    ps.executeUpdate();
                }
                // Insert owner as member
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO island_members (island_id, member_uuid, role) VALUES (?, ?, 'owner') ON DUPLICATE KEY UPDATE role = role"
                )) {
                    ps.setString(1, islandId);
                    ps.setString(2, owner.toString());
                    ps.executeUpdate();
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to persist new island to DB (will continue in-memory)", e);
            }
        }

        // Ensure world creation is requested (on-demand)
        // plugin.getWorld(worldName); // TODO: Implement method in Plugin class
        return true;
    }

    public synchronized boolean addMember(UUID owner, UUID player) {
        Island i = islands.get(owner);
        if (i == null) return false;
        boolean added = i.addMember(player);
        if (!added) return false;

        if (databaseManager != null) {
            try {
                Connection conn = databaseManager.getConnection().get(5, TimeUnit.SECONDS);
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO island_members (island_id, member_uuid, role) VALUES (?, ?, 'visitor') ON DUPLICATE KEY UPDATE role = role"
                )) {
                    ps.setString(1, i.getIslandId());
                    ps.setString(2, player.toString());
                    ps.executeUpdate();
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to persist island member addition", e);
            }
        }
        return true;
    }

    public synchronized boolean removeMember(UUID owner, UUID player) {
        Island i = islands.get(owner);
        if (i == null) return false;
        boolean removed = i.removeMember(player);
        if (!removed) return false;

        if (databaseManager != null) {
            try {
                Connection conn = databaseManager.getConnection().get(5, TimeUnit.SECONDS);
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM island_members WHERE island_id = ? AND member_uuid = ?"
                )) {
                    ps.setString(1, i.getIslandId());
                    ps.setString(2, player.toString());
                    ps.executeUpdate();
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to persist island member removal", e);
            }
        }
        return true;
    }

    /**
     * Load islands and members from DB into memory. If DB is unavailable, this is a no-op.
     */
    public synchronized void loadAll() {
        if (databaseManager == null) {
            plugin.getLogger().info("IslandManager.loadAll: no database manager available, skipping load.");
            return;
        }

        try {
            Connection conn = databaseManager.getConnection().get(5, TimeUnit.SECONDS);
            // load islands
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT island_id, owner_uuid FROM skyblock_islands")) {
                while (rs.next()) {
                    String islandId = rs.getString("island_id");
                    String ownerStr = rs.getString("owner_uuid");
                    if (ownerStr == null) continue;
                    UUID owner = UUID.fromString(ownerStr);
                    String worldName = getPlayerIslandWorldName(owner);
                    Island island = new Island(islandId, owner, worldName);
                    islands.put(owner, island);
                }
            }

            // load members
            try (PreparedStatement ps = conn.prepareStatement("SELECT island_id, member_uuid FROM island_members")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String islandId = rs.getString("island_id");
                        String memberStr = rs.getString("member_uuid");
                        if (memberStr == null) continue;
                        UUID member = UUID.fromString(memberStr);
                        // find island by islandId
                        Island found = null;
                        for (Island isl : islands.values()) {
                            if (isl.getIslandId().equals(islandId)) { found = isl; break; }
                        }
                        if (found != null) found.addMember(member);
                    }
                }
            }

            plugin.getLogger().info("IslandManager: loaded " + islands.size() + " islands from database.");
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load islands from DB (will continue with in-memory state)", e);
        }
    }

    /**
     * Persist all in-memory islands to the DB. Best-effort; continues on error.
     */
    public synchronized void saveAll() {
        if (databaseManager == null) {
            plugin.getLogger().info("IslandManager.saveAll: no database manager available, skipping save.");
            return;
        }

        try {
            Connection conn = databaseManager.getConnection().get(5, TimeUnit.SECONDS);
            conn.setAutoCommit(false);
            try {
                // upsert islands
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO skyblock_islands (island_id, owner_uuid) VALUES (?, ?) ON DUPLICATE KEY UPDATE owner_uuid = ?"
                )) {
                    for (Island i : islands.values()) {
                        ps.setString(1, i.getIslandId());
                        ps.setString(2, i.getOwner().toString());
                        ps.setString(3, i.getOwner().toString());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                // refresh members: delete existing, insert current
                try (PreparedStatement del = conn.prepareStatement("DELETE FROM island_members WHERE island_id = ?");
                     PreparedStatement ins = conn.prepareStatement("INSERT INTO island_members (island_id, member_uuid, role) VALUES (?, ?, 'visitor')")) {
                    for (Island i : islands.values()) {
                        del.setString(1, i.getIslandId());
                        del.addBatch();
                        for (UUID m : i.getMembers()) {
                            ins.setString(1, i.getIslandId());
                            ins.setString(2, m.toString());
                            ins.addBatch();
                        }
                    }
                    del.executeBatch();
                    ins.executeBatch();
                }

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
            plugin.getLogger().info("IslandManager: saved " + islands.size() + " islands to database.");
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to save islands to DB", e);
        }
    }

    private static String getPlayerIslandWorldName(UUID playerId) {
        return "island_" + playerId.toString().replace('-', '_');
    }

    /**
     * Return a snapshot of all current island owners (UUIDs).
     * Useful for tab-completion and administrative tools.
     */
    public java.util.Set<UUID> getAllOwners() {
        return java.util.Collections.unmodifiableSet(islands.keySet());
    }

    /**
     * Delete an island owned by the given owner. Removes it from memory and attempts DB cleanup.
     * Returns true if deletion succeeded (or island did not exist).
     */
    public synchronized boolean deleteIsland(UUID owner) {
        Island i = islands.remove(owner);
        if (i == null) return false;

        if (databaseManager != null) {
            try {
                Connection conn = databaseManager.getConnection().get(5, TimeUnit.SECONDS);
                try (PreparedStatement ps1 = conn.prepareStatement("DELETE FROM island_members WHERE island_id = ?");
                     PreparedStatement ps2 = conn.prepareStatement("DELETE FROM skyblock_islands WHERE island_id = ?")) {
                    ps1.setString(1, i.getIslandId());
                    ps1.executeUpdate();
                    ps2.setString(1, i.getIslandId());
                    ps2.executeUpdate();
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to delete island from DB", e);
            }
        }
        return true;
    }

    /**
     * Transfer ownership of an island from currentOwner to newOwner. Returns true on success.
     * Will fail if currentOwner has no island or newOwner already owns an island.
     */
    public synchronized boolean transferOwnership(UUID currentOwner, UUID newOwner) {
        if (!islands.containsKey(currentOwner)) return false;
        if (islands.containsKey(newOwner)) return false; // avoid conflicts

        Island old = islands.remove(currentOwner);
        if (old == null) return false;

        String newWorldName = getPlayerIslandWorldName(newOwner);
        Island created = new Island(old.getIslandId(), newOwner, newWorldName);
        // copy members
        for (UUID m : old.getMembers()) created.addMember(m);
        // ensure new owner present
        created.addMember(newOwner);
        islands.put(newOwner, created);

        if (databaseManager != null) {
            try {
                Connection conn = databaseManager.getConnection().get(5, TimeUnit.SECONDS);
                // update island owner reference
                try (PreparedStatement ps = conn.prepareStatement("UPDATE skyblock_islands SET owner_uuid = ? WHERE island_id = ?")) {
                    ps.setString(1, newOwner.toString());
                    ps.setString(2, created.getIslandId());
                    ps.executeUpdate();
                }
                // update island_members: ensure new owner present and remove previous owner role if needed
                try (PreparedStatement del = conn.prepareStatement("DELETE FROM island_members WHERE island_id = ? AND member_uuid = ?");
                     PreparedStatement ins = conn.prepareStatement("INSERT INTO island_members (island_id, member_uuid, role) VALUES (?, ?, 'owner') ON DUPLICATE KEY UPDATE role = role")) {
                    del.setString(1, created.getIslandId());
                    del.setString(2, currentOwner.toString());
                    del.executeUpdate();

                    ins.setString(1, created.getIslandId());
                    ins.setString(2, newOwner.toString());
                    ins.executeUpdate();
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to persist island ownership transfer", e);
            }
        }

        // Ensure world creation for new owner naming
        // plugin.getWorld(created.getWorldName()); // TODO: Implement method in Plugin class

        return true;
    }

    public static class Island {
        private final String islandId;
        private final UUID owner;
        private final String worldName;
        private final Set<UUID> members = new CopyOnWriteArraySet<>();

        public Island(String islandId, UUID owner, String worldName) {
            this.islandId = islandId;
            this.owner = owner;
            this.worldName = worldName;
            this.members.add(owner);
        }

        public String getIslandId() { return islandId; }
        public UUID getOwner() { return owner; }
        public String getWorldName() { return worldName; }
        public Set<UUID> getMembers() { return members; }

        public boolean isMember(UUID uuid) { return members.contains(uuid); }
        public boolean addMember(UUID uuid) { return members.add(uuid); }
        public boolean removeMember(UUID uuid) { if (owner.equals(uuid)) return false; return members.remove(uuid); }
    }
}
