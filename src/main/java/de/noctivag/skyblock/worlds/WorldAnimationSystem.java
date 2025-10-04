package de.noctivag.skyblock.worlds;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * World Animation System - Environmental effects and animations for worlds and islands
 * Compatible with Multi-Server System
 */
public class WorldAnimationSystem {
    private final SkyblockPlugin plugin;
    private final Map<String, WorldAnimation> activeAnimations = new ConcurrentHashMap<>();
    private final Map<String, BukkitTask> animationTasks = new ConcurrentHashMap<>();
    private final Map<String, WorldEffect> worldEffects = new ConcurrentHashMap<>();

    public WorldAnimationSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        // databaseManager parameter accepted for compatibility with multiserver wiring; not used currently
        initializeWorldEffects();
        startGlobalAnimationTask();
    }

    private void initializeWorldEffects() {
        // Hub World Effects
        worldEffects.put("hub", new WorldEffect(
            "hub",
            "§6Hub World",
            List.of(
                new ParticleAnimation(Particle.HAPPY_VILLAGER, 0.5, 0.5, 0.5, 1, 20L),
                new ParticleAnimation(Particle.END_ROD, 0.3, 0.3, 0.3, 2, 30L),
                new ParticleAnimation(Particle.NOTE, 0.2, 0.2, 0.2, 3, 15L)
            ),
            List.of(
                new SoundAnimation(Sound.AMBIENT_CAVE, 0.3f, 1.0f, 200L),
                new SoundAnimation(Sound.BLOCK_NOTE_BLOCK_PLING, 0.5f, 1.2f, 150L)
            )
        ));

        // Private Island Effects
        worldEffects.put("private_island", new WorldEffect(
            "private_island",
            "§aPrivate Island",
            List.of(
                new ParticleAnimation(Particle.HEART, 0.4, 0.4, 0.4, 1, 25L),
                new ParticleAnimation(Particle.HAPPY_VILLAGER, 0.3, 0.3, 0.3, 1, 40L),
                new ParticleAnimation(Particle.CLOUD, 0.2, 0.1, 0.2, 2, 35L)
            ),
            List.of(
                new SoundAnimation(Sound.AMBIENT_UNDERWATER_ENTER, 0.2f, 1.0f, 300L),
                new SoundAnimation(Sound.BLOCK_GRASS_STEP, 0.3f, 1.1f, 180L)
            )
        ));

        // Public Island Effects
        worldEffects.put("public_island", new WorldEffect(
            "public_island",
            "§bPublic Island",
            List.of(
                new ParticleAnimation(Particle.ENCHANT, 0.4, 0.4, 0.4, 2, 20L),
                new ParticleAnimation(Particle.HAPPY_VILLAGER, 0.3, 0.3, 0.3, 1, 30L),
                new ParticleAnimation(Particle.FLAME, 0.2, 0.2, 0.2, 1, 25L)
            ),
            List.of(
                new SoundAnimation(Sound.AMBIENT_CAVE, 0.4f, 1.0f, 250L),
                new SoundAnimation(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.3f, 1.2f, 200L)
            )
        ));

        // Dungeon Effects
        worldEffects.put("dungeon", new WorldEffect(
            "dungeon",
            "§4Dungeon",
            List.of(
                new ParticleAnimation(Particle.LARGE_SMOKE, 0.5, 0.5, 0.5, 3, 15L),
                new ParticleAnimation(Particle.FLAME, 0.3, 0.3, 0.3, 2, 20L),
                new ParticleAnimation(Particle.SOUL_FIRE_FLAME, 0.2, 0.2, 0.2, 1, 25L)
            ),
            List.of(
                new SoundAnimation(Sound.AMBIENT_CAVE, 0.6f, 0.8f, 100L),
                new SoundAnimation(Sound.ENTITY_WITHER_AMBIENT, 0.3f, 1.0f, 300L)
            )
        ));

        // Mining Island Effects
        worldEffects.put("mining_island", new WorldEffect(
            "mining_island",
            "§7Mining Island",
            List.of(
                new ParticleAnimation(Particle.CRIT, 0.4, 0.4, 0.4, 2, 18L),
                new ParticleAnimation(Particle.HAPPY_VILLAGER, 0.3, 0.3, 0.3, 1, 35L),
                new ParticleAnimation(Particle.CLOUD, 0.2, 0.1, 0.2, 1, 40L)
            ),
            List.of(
                new SoundAnimation(Sound.BLOCK_STONE_BREAK, 0.3f, 1.0f, 200L),
                new SoundAnimation(Sound.ENTITY_IRON_GOLEM_STEP, 0.2f, 1.1f, 250L)
            )
        ));

        // Farming Island Effects
        worldEffects.put("farming_island", new WorldEffect(
            "farming_island",
            "§2Farming Island",
            List.of(
                new ParticleAnimation(Particle.HAPPY_VILLAGER, 0.4, 0.4, 0.4, 2, 25L),
                new ParticleAnimation(Particle.HEART, 0.3, 0.3, 0.3, 1, 30L),
                new ParticleAnimation(Particle.CLOUD, 0.2, 0.1, 0.2, 2, 35L)
            ),
            List.of(
                new SoundAnimation(Sound.BLOCK_GRASS_STEP, 0.3f, 1.0f, 180L),
                new SoundAnimation(Sound.ENTITY_CHICKEN_AMBIENT, 0.2f, 1.2f, 300L)
            )
        ));

        // Combat Island Effects
        worldEffects.put("combat_island", new WorldEffect(
            "combat_island",
            "§cCombat Island",
            List.of(
                new ParticleAnimation(Particle.FLAME, 0.4, 0.4, 0.4, 2, 15L),
                new ParticleAnimation(Particle.SMOKE, 0.3, 0.3, 0.3, 1, 20L),
                new ParticleAnimation(Particle.CRIT, 0.2, 0.2, 0.2, 1, 25L)
            ),
            List.of(
                new SoundAnimation(Sound.ENTITY_ZOMBIE_AMBIENT, 0.3f, 1.0f, 200L),
                new SoundAnimation(Sound.ENTITY_SKELETON_AMBIENT, 0.2f, 1.1f, 250L)
            )
        ));
    }

    private void startGlobalAnimationTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    String worldName = player.getWorld().getName();
                    WorldEffect effect = getWorldEffect(worldName);

                    if (effect != null) {
                        playWorldEffects(player, effect);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
    }

    public void startWorldAnimation(String worldName, WorldAnimation animation) {
        if (activeAnimations.containsKey(worldName)) {
            stopWorldAnimation(worldName);
        }

        activeAnimations.put(worldName, animation);

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    cancel();
                    activeAnimations.remove(worldName);
                    return;
                }

                for (Player player : world.getPlayers()) {
                    animation.play(player);
                }
            }
        }.runTaskTimer(plugin, 0L, animation.getInterval());

        animationTasks.put(worldName, task);
    }

    public void stopWorldAnimation(String worldName) {
        BukkitTask task = animationTasks.remove(worldName);
        if (task != null) {
            task.cancel();
        }
        activeAnimations.remove(worldName);
    }

    public void playWorldEffects(Player player, WorldEffect effect) {
        for (ParticleAnimation particleAnim : effect.getParticleAnimations()) {
            if (System.currentTimeMillis() - particleAnim.getLastPlayed() >= particleAnim.getInterval()) {
                particleAnim.play(player);
                particleAnim.setLastPlayed(System.currentTimeMillis());
            }
        }

        for (SoundAnimation soundAnim : effect.getSoundAnimations()) {
            if (System.currentTimeMillis() - soundAnim.getLastPlayed() >= soundAnim.getInterval()) {
                soundAnim.play(player);
                soundAnim.setLastPlayed(System.currentTimeMillis());
            }
        }
    }

    public WorldEffect getWorldEffect(String worldName) {
        // Map world names to effect types
        if (worldName.contains("hub")) return worldEffects.get("hub");
        if (worldName.contains("private") || worldName.contains("island")) return worldEffects.get("private_island");
        if (worldName.contains("public")) return worldEffects.get("public_island");
        if (worldName.contains("dungeon")) return worldEffects.get("dungeon");
        if (worldName.contains("mining")) return worldEffects.get("mining_island");
        if (worldName.contains("farming")) return worldEffects.get("farming_island");
        if (worldName.contains("combat")) return worldEffects.get("combat_island");

        return worldEffects.get("private_island"); // Default
    }

    @SuppressWarnings("unused")
    public void createIslandAnimation(UUID islandId, Location center, IslandAnimationType type) {
        String animationId = "island_" + islandId.toString();
        // use the center parameter at least once to avoid unused-parameter warnings; it may be used in future enhancements
        Objects.requireNonNull(center, "center");

        WorldAnimation animation = switch (type) {
            case SPAWN -> new WorldAnimation(
            List.of(
                new ParticleAnimation(Particle.HAPPY_VILLAGER, 0.5, 0.5, 0.5, 3, 10L),
                new ParticleAnimation(Particle.HEART, 0.3, 0.3, 0.3, 2, 15L)
            ),
                List.of(
                    new SoundAnimation(Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1.0f, 50L)
                ),
                20L
            );
            case LEVEL_UP -> new WorldAnimation(
                List.of(
                    new ParticleAnimation(Particle.ENCHANT, 0.6, 0.6, 0.6, 5, 5L),
                    new ParticleAnimation(Particle.HAPPY_VILLAGER, 0.4, 0.4, 0.4, 3, 8L),
                    // Use a standard Bukkit particle for fireworks sparks (NamespacedKey lookup isn't available here)
                    new ParticleAnimation(Particle.FIREWORK, 0.3, 0.3, 0.3, 1, 12L)
                ),
                List.of(
                    new SoundAnimation(Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.2f, 30L),
                    new SoundAnimation(Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.6f, 1.0f, 40L)
                ),
                10L
            );
            case EXPANSION -> new WorldAnimation(
                List.of(
                    new ParticleAnimation(Particle.CLOUD, 0.8, 0.4, 0.8, 4, 8L),
                    new ParticleAnimation(Particle.HAPPY_VILLAGER, 0.5, 0.5, 0.5, 2, 12L)
                ),
                List.of(
                    new SoundAnimation(Sound.BLOCK_GRASS_BREAK, 0.4f, 1.0f, 60L)
                ),
                15L
            );
            case DESTRUCTION -> new WorldAnimation(
                List.of(
                    new ParticleAnimation(Particle.LARGE_SMOKE, 0.6, 0.6, 0.6, 3, 5L),
                    new ParticleAnimation(Particle.FLAME, 0.4, 0.4, 0.4, 2, 8L)
                ),
                List.of(
                    new SoundAnimation(Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1.0f, 40L)
                ),
                10L
            );
        };

        startWorldAnimation(animationId, animation);

        // Auto-stop after 10 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                stopWorldAnimation(animationId);
            }
        }.runTaskLater(plugin, 200L);
    }

    public Map<String, WorldAnimation> getActiveAnimations() {
        return activeAnimations;
    }

    public void cleanup() {
        for (BukkitTask task : animationTasks.values()) {
            task.cancel();
        }
        animationTasks.clear();
        activeAnimations.clear();
    }

    // Inner classes
    public static class WorldEffect {
        private final String id;
        private final String displayName;
        private final List<ParticleAnimation> particleAnimations;
        private final List<SoundAnimation> soundAnimations;

        public WorldEffect(String id, String displayName, List<ParticleAnimation> particleAnimations, List<SoundAnimation> soundAnimations) {
            this.id = id;
            this.displayName = displayName;
            this.particleAnimations = particleAnimations;
            this.soundAnimations = soundAnimations;
        }

        public String getId() { return id; }
        public String getDisplayName() { return displayName; }
        public List<ParticleAnimation> getParticleAnimations() { return particleAnimations; }
        public List<SoundAnimation> getSoundAnimations() { return soundAnimations; }
    }

    public static class WorldAnimation {
        private final List<ParticleAnimation> particleAnimations;
        private final List<SoundAnimation> soundAnimations;
        private final long interval;

        public WorldAnimation(List<ParticleAnimation> particleAnimations, List<SoundAnimation> soundAnimations, long interval) {
            this.particleAnimations = particleAnimations;
            this.soundAnimations = soundAnimations;
            this.interval = interval;
        }

        public void play(Player player) {
            for (ParticleAnimation particleAnim : particleAnimations) {
                particleAnim.play(player);
            }
            for (SoundAnimation soundAnim : soundAnimations) {
                soundAnim.play(player);
            }
        }

        public long getInterval() { return interval; }
    }

    public static class ParticleAnimation {
        private final Particle particle;
        private final double offsetX, offsetY, offsetZ;
        private final int count;
        private final long interval;
        private long lastPlayed = 0;

        public ParticleAnimation(Particle particle, double offsetX, double offsetY, double offsetZ, int count, long interval) {
            this.particle = particle;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.count = count;
            this.interval = interval;
        }

        public void play(Player player) {
            Location loc = player.getLocation().add(0, 1, 0);
            player.getWorld().spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ, 0.1);
        }

        public long getInterval() { return interval; }
        public long getLastPlayed() { return lastPlayed; }
        public void setLastPlayed(long lastPlayed) { this.lastPlayed = lastPlayed; }
    }

    public static class SoundAnimation {
        private final Sound sound;
        private final float volume, pitch;
        private final long interval;
        private long lastPlayed = 0;

        public SoundAnimation(Sound sound, float volume, float pitch, long interval) {
            this.sound = sound;
            this.volume = volume;
            this.pitch = pitch;
            this.interval = interval;
        }

        public void play(Player player) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }

        public long getInterval() { return interval; }
        public long getLastPlayed() { return lastPlayed; }
        public void setLastPlayed(long lastPlayed) { this.lastPlayed = lastPlayed; }
    }

    public enum IslandAnimationType {
        SPAWN, LEVEL_UP, EXPANSION, DESTRUCTION
    }
}
