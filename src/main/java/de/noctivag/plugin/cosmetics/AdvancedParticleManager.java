package de.noctivag.plugin.cosmetics;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AdvancedParticleManager {
    private final Plugin plugin;
    private final Map<UUID, ParticleEffect> activeEffects = new HashMap<>();
    private final Map<UUID, BukkitTask> effectTasks = new HashMap<>();

    public AdvancedParticleManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void spawnParticleShape(Player player, ParticleShape shape) {
        setPlayerEffect(player, shape, Particle.HEART, 10, 0.1);
    }

    public void setPlayerEffect(Player player, ParticleShape shape, Particle particle, int particleCount, double speed) {
        UUID playerId = player.getUniqueId();
        
        // Stop existing effect
        stopPlayerEffect(player);
        
        // Create new effect
        ParticleEffect effect = new ParticleEffect(shape, particle, particleCount, speed);
        activeEffects.put(playerId, effect);
        
        // Start effect task
        BukkitTask task = new BukkitRunnable() {
            private double time = 0;
            
            @Override
            public void run() {
                if (!player.isOnline() || !activeEffects.containsKey(playerId)) {
                    cancel();
                    return;
                }
                
                // Generate particle locations
                Location playerLoc = player.getLocation();
                var locations = shape.generateParticleLocations(player, particleCount, time);
                
                // Spawn particles
                for (Location loc : locations) {
                    // Rotate particles with player
                    Location rotatedLoc = rotateLocationAroundPlayer(loc, playerLoc, player.getYaw());
                    player.getWorld().spawnParticle(particle, rotatedLoc, 1, 0, 0, 0, speed);
                }
                
                time += 0.1;
            }
        }.runTaskTimer(plugin, 0L, 2L); // Run every 2 ticks (0.1 seconds)
        
        effectTasks.put(playerId, task);
        player.sendMessage("§aPartikel-Effekt aktiviert: §e" + shape.getDisplayName());
    }

    public void stopPlayerEffect(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Stop task
        BukkitTask task = effectTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }
        
        // Remove effect
        activeEffects.remove(playerId);
        player.sendMessage("§cPartikel-Effekt deaktiviert");
    }

    public boolean hasActiveEffect(Player player) {
        return activeEffects.containsKey(player.getUniqueId());
    }

    public ParticleEffect getPlayerEffect(Player player) {
        return activeEffects.get(player.getUniqueId());
    }

    public void cleanup() {
        // Stop all effects
        for (BukkitTask task : effectTasks.values()) {
            task.cancel();
        }
        effectTasks.clear();
        activeEffects.clear();
    }

    private Location rotateLocationAroundPlayer(Location particleLoc, Location playerLoc, float yaw) {
        // Convert to relative coordinates
        double x = particleLoc.getX() - playerLoc.getX();
        double z = particleLoc.getZ() - playerLoc.getZ();
        
        // Rotate around Y axis based on player's yaw
        double radians = Math.toRadians(yaw);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        
        double newX = x * cos - z * sin;
        double newZ = x * sin + z * cos;
        
        // Return absolute coordinates
        return new Location(playerLoc.getWorld(), 
            playerLoc.getX() + newX, 
            particleLoc.getY(), 
            playerLoc.getZ() + newZ);
    }

    public static class ParticleEffect {
        private final ParticleShape shape;
        private final Particle particle;
        private final int particleCount;
        private final double speed;

        public ParticleEffect(ParticleShape shape, Particle particle, int particleCount, double speed) {
            this.shape = shape;
            this.particle = particle;
            this.particleCount = particleCount;
            this.speed = speed;
        }

        public ParticleShape getShape() { return shape; }
        public Particle getParticle() { return particle; }
        public int getParticleCount() { return particleCount; }
        public double getSpeed() { return speed; }
    }
}
