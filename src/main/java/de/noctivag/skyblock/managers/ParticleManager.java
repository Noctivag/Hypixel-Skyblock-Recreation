package de.noctivag.skyblock.managers;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class ParticleManager {
    private final SkyblockPlugin plugin;
    private final Map<UUID, BukkitTask> activeTasks;
    private final Map<UUID, ParticleEffectType> activeEffects;

    public ParticleManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.activeTasks = new HashMap<>();
        this.activeEffects = new HashMap<>();
    }

    @SuppressWarnings("unused")
    public void setPlayerEffect(Player player, ParticleEffectType effect) {
        UUID playerId = player.getUniqueId();
        clearEffect(player);

        if (effect == null) {
            return;
        }

        activeEffects.put(playerId, effect);
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    activeEffects.remove(playerId);
                    activeTasks.remove(playerId);
                    return;
                }

                effect.spawn(player);
            }
        }.runTaskTimer(plugin, 0L, effect.getInterval());

        activeTasks.put(playerId, task);
    }

    public void clearEffect(Player player) {
        UUID playerId = player.getUniqueId();
        BukkitTask task = activeTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }
        activeEffects.remove(playerId);
    }

    @SuppressWarnings("unused")
    public ParticleEffectType getPlayerEffect(Player player) {
        return activeEffects.get(player.getUniqueId());
    }

    public void cleanup() {
        activeTasks.values().forEach(BukkitTask::cancel);
        activeTasks.clear();
        activeEffects.clear();
    }

    public enum ParticleEffectType {
        HEART(Particle.HEART, 0.5, 0.5, 0.5, 1, 10L),
        FLAME(Particle.FLAME, 0.2, 0.2, 0.2, 3, 5L),
        WATER(Particle.SPLASH, 0.3, 0.3, 0.3, 5, 5L),
        CLOUD(Particle.CLOUD, 0.2, 0.0, 0.2, 1, 10L),
        SPELL(Particle.CURRENT_DOWN, 0.5, 0.5, 0.5, 3, 5L),
        NOTE(Particle.NOTE, 0.5, 0.5, 0.5, 1, 10L);

        private final Particle particle;
        private final double offsetX;
        private final double offsetY;
        private final double offsetZ;
        private final int count;
        private final long interval;

        ParticleEffectType(Particle particle, double offsetX, double offsetY, double offsetZ, int count, long interval) {
            this.particle = particle;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.count = count;
            this.interval = interval;
        }

        public void spawn(Player player) {
            player.getWorld().spawnParticle(
                particle,
                player.getLocation().add(0, 1, 0),
                count,
                offsetX,
                offsetY,
                offsetZ,
                0
            );
        }

        public long getInterval() {
            return interval;
        }
    }
}
