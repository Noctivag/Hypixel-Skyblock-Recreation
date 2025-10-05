package de.noctivag.skyblock.mobs;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * Configuration for mob spawning
 */
public class MobConfig {
    private final EntityType entityType;
    private final int maxMobs;
    private final double spawnRadius;
    private final long respawnDelay;
    private final boolean persistent;

    public MobConfig(EntityType entityType, int maxMobs, double spawnRadius, long respawnDelay, boolean persistent) {
        this.entityType = entityType;
        this.maxMobs = maxMobs;
        this.spawnRadius = spawnRadius;
        this.respawnDelay = respawnDelay;
        this.persistent = persistent;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public int getMaxMobs() {
        return maxMobs;
    }

    public double getSpawnRadius() {
        return spawnRadius;
    }

    public long getRespawnDelay() {
        return respawnDelay;
    }

    public boolean isPersistent() {
        return persistent;
    }
    
    public String getDisplayName() {
        return entityType.name();
    }
    
    public double getHealth() {
        return 20.0; // Default health
    }

    public static class Builder {
        private EntityType entityType;
        private int maxMobs = 5;
        private double spawnRadius = 10.0;
        private long respawnDelay = 5000L;
        private boolean persistent = false;

        public Builder entityType(EntityType entityType) {
            this.entityType = entityType;
            return this;
        }

        public Builder maxMobs(int maxMobs) {
            this.maxMobs = maxMobs;
            return this;
        }

        public Builder spawnRadius(double spawnRadius) {
            this.spawnRadius = spawnRadius;
            return this;
        }

        public Builder respawnDelay(long respawnDelay) {
            this.respawnDelay = respawnDelay;
            return this;
        }

        public Builder persistent(boolean persistent) {
            this.persistent = persistent;
            return this;
        }

        public MobConfig build() {
            if (entityType == null) {
                throw new IllegalStateException("EntityType is required");
            }
            return new MobConfig(entityType, maxMobs, spawnRadius, respawnDelay, persistent);
        }
    }
}
