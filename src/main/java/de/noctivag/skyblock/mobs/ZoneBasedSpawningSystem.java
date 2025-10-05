package de.noctivag.skyblock.mobs;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Zone-based mob spawning system
 */
public class ZoneBasedSpawningSystem {
    private final Map<String, SpawnZone> spawnZones;
    private final Map<UUID, Set<String>> playerZones;

    public ZoneBasedSpawningSystem() {
        this.spawnZones = new HashMap<>();
        this.playerZones = new HashMap<>();
    }

    public void addSpawnZone(String zoneId, SpawnZone zone) {
        spawnZones.put(zoneId, zone);
    }

    public SpawnZone getSpawnZone(String zoneId) {
        return spawnZones.get(zoneId);
    }

    public Collection<SpawnZone> getAllZones() {
        return spawnZones.values();
    }

    public void updatePlayerZone(Player player, String zoneId) {
        playerZones.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>()).add(zoneId);
    }

    public void removePlayerZone(Player player, String zoneId) {
        Set<String> zones = playerZones.get(player.getUniqueId());
        if (zones != null) {
            zones.remove(zoneId);
            if (zones.isEmpty()) {
                playerZones.remove(player.getUniqueId());
            }
        }
    }

    public Set<String> getPlayerZones(Player player) {
        return playerZones.getOrDefault(player.getUniqueId(), Collections.emptySet());
    }

    public boolean isPlayerInZone(Player player, String zoneId) {
        return getPlayerZones(player).contains(zoneId);
    }

    public List<SpawnZone> getActiveZones(Player player) {
        Set<String> playerZoneIds = getPlayerZones(player);
        return spawnZones.entrySet().stream()
                .filter(entry -> playerZoneIds.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Represents a spawn zone
     */
    public static class SpawnZone {
        private final String id;
        private final Location center;
        private final double radius;
        private final int maxMobs;
        private final Set<String> mobTypes;
        private final Map<UUID, Entity> activeMobs;

        public SpawnZone(String id, Location center, double radius, int maxMobs) {
            this.id = id;
            this.center = center;
            this.radius = radius;
            this.maxMobs = maxMobs;
            this.mobTypes = new HashSet<>();
            this.activeMobs = new HashMap<>();
        }

        public String getId() {
            return id;
        }

        public Location getCenter() {
            return center;
        }

        public double getRadius() {
            return radius;
        }

        public int getMaxMobs() {
            return maxMobs;
        }

        public Set<String> getMobTypes() {
            return new HashSet<>(mobTypes);
        }

        public void addMobType(String mobType) {
            mobTypes.add(mobType);
        }

        public Map<UUID, Entity> getActiveMobs() {
            return new HashMap<>(activeMobs);
        }

        public void addMob(UUID mobId, Entity mob) {
            activeMobs.put(mobId, mob);
        }

        public void removeMob(UUID mobId) {
            activeMobs.remove(mobId);
        }

        public boolean canSpawnMore() {
            return activeMobs.size() < maxMobs;
        }

        public boolean isInZone(Location location) {
            return center.getWorld().equals(location.getWorld()) && 
                   center.distance(location) <= radius;
        }
    }
}
