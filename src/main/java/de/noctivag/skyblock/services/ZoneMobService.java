package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.enums.ZoneType;
import de.noctivag.skyblock.mobs.ZoneMob;
import de.noctivag.skyblock.zones.*;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service für die Verwaltung von zonen-spezifischen Mobs
 */
public class ZoneMobService {

    private final SkyblockPlugin plugin;
    private final Map<ZoneType, List<ZoneMob>> zoneMobs;
    private final Map<UUID, ZoneMob> activeMobs;
    private final Random random = new Random();

    public ZoneMobService(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.zoneMobs = new ConcurrentHashMap<>();
        this.activeMobs = new ConcurrentHashMap<>();
        
        initializeZoneMobs();
    }

    /**
     * Initialisiert die Mobs für jede Zone
     */
    private void initializeZoneMobs() {
        // Dwarven Mines Mobs
        List<ZoneMob> dwarvenMinesMobs = new ArrayList<>();
        // TODO: Implement these mob classes
        // new IceWalker(),
        // new Goblin(plugin),
        // new PowderGhast()
        zoneMobs.put(ZoneType.DWARVEN_MINES, dwarvenMinesMobs);

        // Crystal Hollows Mobs
        List<ZoneMob> crystalHollowsMobs = new ArrayList<>();
        // TODO: Implement these mob classes
        // new TeamTreasurite(),
        // new Corleone()
        zoneMobs.put(ZoneType.CRYSTAL_HOLLOWS, crystalHollowsMobs);

        // Crimson Isle Mobs
        List<ZoneMob> crimsonIsleMobs = new ArrayList<>();
        // TODO: Implement this mob class
        // new MagmaCubeBoss()
        zoneMobs.put(ZoneType.CRIMSON_ISLE, crimsonIsleMobs);

        plugin.getLogger().info("ZoneMobService initialized with " + zoneMobs.size() + " zones");
    }

    /**
     * Spawnt einen zufälligen Mob in der gegebenen Zone
     */
    public CompletableFuture<LivingEntity> spawnRandomMob(ZoneType zone, Location location) {
        return CompletableFuture.supplyAsync(() -> {
            List<ZoneMob> mobs = zoneMobs.get(zone);
            if (mobs == null || mobs.isEmpty()) {
                return null;
            }

            // Weighted random selection
            ZoneMob selectedMob = selectWeightedMob(mobs);
            if (selectedMob == null) {
                return null;
            }

            // Set spawn location and spawn the mob
            selectedMob.setSpawnLocation(location);
            selectedMob.spawn();
            LivingEntity entity = selectedMob.getEntity();
            if (entity != null) {
                activeMobs.put(entity.getUniqueId(), selectedMob);

                if (plugin.getSettingsConfig().isDebugMode()) {
                    plugin.getLogger().info("Spawned " + selectedMob.getName() + " in " + zone.getDisplayName());
                }
            }

            return entity;
        });
    }

    /**
     * Spawnt einen spezifischen Mob
     */
    public CompletableFuture<LivingEntity> spawnSpecificMob(ZoneType zone, String mobName, Location location) {
        return CompletableFuture.supplyAsync(() -> {
            List<ZoneMob> mobs = zoneMobs.get(zone);
            if (mobs == null) {
                return null;
            }

            ZoneMob selectedMob = mobs.stream()
                .filter(mob -> mob.getName().equalsIgnoreCase(mobName))
                .findFirst()
                .orElse(null);

            if (selectedMob == null) {
                return null;
            }

            selectedMob.setSpawnLocation(location);
            selectedMob.spawn();
            LivingEntity entity = selectedMob.getEntity();
            if (entity != null) {
                activeMobs.put(entity.getUniqueId(), selectedMob);
            }

            return entity;
        });
    }

    /**
     * Gets the zone type for a mob (helper method)
     */
    private String getZoneTypeForMob(ZoneMob mob) {
        // Find which zone this mob belongs to
        for (Map.Entry<ZoneType, List<ZoneMob>> entry : zoneMobs.entrySet()) {
            if (entry.getValue().contains(mob)) {
                return entry.getKey().name();
            }
        }
        return "UNKNOWN";
    }

    /**
     * Wählt einen Mob basierend auf Gewichtung aus
     */
    private ZoneMob selectWeightedMob(List<ZoneMob> mobs) {
        // Simple weighted selection based on level (higher level = lower chance)
        int totalWeight = mobs.stream().mapToInt(ZoneMob::getLevel).sum();
        int randomWeight = random.nextInt(totalWeight);
        
        int currentWeight = 0;
        for (ZoneMob mob : mobs) {
            currentWeight += mob.getLevel();
            if (randomWeight < currentWeight) {
                return mob;
            }
        }
        
        return mobs.get(0); // Fallback
    }

    /**
     * Aktiviert die Fähigkeit eines Mobs
     */
    public void activateMobAbility(LivingEntity entity) {
        ZoneMob zoneMob = activeMobs.get(entity.getUniqueId());
        if (zoneMob != null) {
            zoneMob.useAbility(entity);
        }
    }

    /**
     * Behandelt den Tod eines Mobs
     */
    public void handleMobDeath(LivingEntity entity) {
        ZoneMob zoneMob = activeMobs.remove(entity.getUniqueId());
        if (zoneMob != null) {
            zoneMob.onDeath(entity);
            
            if (plugin.getSettingsConfig().isDebugMode()) {
                plugin.getLogger().info("Handled death of " + zoneMob.getName());
            }
        }
    }

    /**
     * Gibt alle Mobs einer Zone zurück
     */
    public List<ZoneMob> getZoneMobs(ZoneType zone) {
        return zoneMobs.getOrDefault(zone, new ArrayList<>());
    }

    /**
     * Gibt alle aktiven Mobs zurück
     */
    public Map<UUID, ZoneMob> getActiveMobs() {
        return new HashMap<>(activeMobs);
    }

    /**
     * Entfernt einen aktiven Mob
     */
    public void removeActiveMob(UUID entityId) {
        activeMobs.remove(entityId);
    }

    /**
     * Spawnt Mobs in der Nähe eines Spielers basierend auf der Zone
     */
    public CompletableFuture<Void> spawnMobsNearPlayer(Player player, int count) {
        return CompletableFuture.runAsync(() -> {
            Location playerLoc = player.getLocation();
            ZoneType zone = determineZone(playerLoc);
            
            if (zone == null) {
                return;
            }

            for (int i = 0; i < count; i++) {
                // Random location near player
                Location spawnLoc = playerLoc.clone().add(
                    (random.nextDouble() - 0.5) * 20,
                    0,
                    (random.nextDouble() - 0.5) * 20
                );
                
                spawnRandomMob(zone, spawnLoc);
            }
        });
    }

    /**
     * Bestimmt die Zone basierend auf der Location
     */
    private ZoneType determineZone(Location location) {
        // Simple zone determination based on world name
        String worldName = location.getWorld().getName().toLowerCase();
        
        if (worldName.contains("dwarven") || worldName.contains("mines")) {
            return ZoneType.DWARVEN_MINES;
        } else if (worldName.contains("crystal") || worldName.contains("hollows")) {
            return ZoneType.CRYSTAL_HOLLOWS;
        } else if (worldName.contains("crimson") || worldName.contains("isle")) {
            return ZoneType.CRIMSON_ISLE;
        } else if (worldName.contains("end")) {
            return ZoneType.END;
        } else if (worldName.contains("spider") || worldName.contains("den")) {
            return ZoneType.SPIDERS_DEN;
        } else if (worldName.contains("blazing") || worldName.contains("fortress")) {
            return ZoneType.BLAZING_FORTRESS;
        } else if (worldName.contains("dungeon")) {
            return ZoneType.DUNGEON;
        } else if (worldName.contains("rift")) {
            return ZoneType.RIFT;
        }
        
        return null; // Unknown zone
    }

    /**
     * Gibt Statistiken über die aktiven Mobs zurück
     */
    public String getMobStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("§6Zone Mob Statistics:\n");
        
        for (Map.Entry<ZoneType, List<ZoneMob>> entry : zoneMobs.entrySet()) {
            ZoneType zone = entry.getKey();
            List<ZoneMob> mobs = entry.getValue();
            
            long activeCount = activeMobs.values().stream()
                .filter(mob -> zone.equals(getZoneTypeForMob(mob)))
                .count();
            
            stats.append("§7").append(zone.getDisplayName()).append(": ")
                 .append("§a").append(activeCount).append("§7/").append(mobs.size()).append(" active\n");
        }
        
        return stats.toString();
    }
}
