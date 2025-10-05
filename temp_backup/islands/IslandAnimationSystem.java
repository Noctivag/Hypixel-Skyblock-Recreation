package de.noctivag.skyblock.islands;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Island Animation System - Special animations for island events
 * Compatible with Multi-Server System
 */
public class IslandAnimationSystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, IslandAnimation> activeIslandAnimations = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> islandAnimationTasks = new ConcurrentHashMap<>();
    
    public IslandAnimationSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
    }
    
    public void playIslandSpawnAnimation(UUID islandId, Location center, Player player) {
        IslandAnimation animation = new IslandAnimation(
            "§aIsland Spawned!",
            Arrays.asList(
                new ParticleEffect(Particle.HAPPY_VILLAGER, center.clone().add(0, 2, 0), 0.5, 0.5, 0.5, 5, 10L),
                new ParticleEffect(Particle.HEART, center.clone().add(0, 3, 0), 0.3, 0.3, 0.3, 3, 15L),
                new ParticleEffect(Particle.CLOUD, center.clone().add(0, 1, 0), 0.8, 0.2, 0.8, 4, 20L)
            ),
            Arrays.asList(
                new SoundEffect(Sound.ENTITY_PLAYER_LEVELUP, center, 0.8f, 1.2f, 0L),
                new SoundEffect(Sound.ENTITY_VILLAGER_YES, center, 0.6f, 1.0f, 500L)
            ),
            200L // 10 seconds
        );
        
        startIslandAnimation(islandId, animation);
        
        // Send message to player
        if (player != null) {
            player.sendMessage(Component.text("§a§l✨ Your island has been created!"));
            player.sendMessage(Component.text("§7Welcome to your new adventure!"));
        }
    }
    
    public void playIslandLevelUpAnimation(UUID islandId, Location center, int newLevel, Player player) {
        IslandAnimation animation = new IslandAnimation(
            "§6Island Level Up!",
            List.of(
                new ParticleEffect(Particle.ENCHANT, center.clone().add(0, 2, 0), 0.6, 0.6, 0.6, 8, 5L),
                new ParticleEffect(Particle.HAPPY_VILLAGER, center.clone().add(0, 3, 0), 0.4, 0.4, 0.4, 5, 8L),
                new ParticleEffect(Particle.FIREWORK, center.clone().add(0, 4, 0), 0.3, 0.3, 0.3, 2, 12L),
                new ParticleEffect(Particle.HAPPY_VILLAGER, center.clone().add(0, 1, 0), 0.5, 0.5, 0.5, 3, 15L)
            ),
            Arrays.asList(
                new SoundEffect(Sound.ENTITY_PLAYER_LEVELUP, center, 1.0f, 1.5f, 0L),
                new SoundEffect(Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, center, 0.8f, 1.0f, 200L),
                new SoundEffect(Sound.ENTITY_VILLAGER_CELEBRATE, center, 0.6f, 1.2f, 400L)
            ),
            300L // 15 seconds
        );
        
        startIslandAnimation(islandId, animation);
        
        // Send message to player
        if (player != null) {
            player.sendMessage(Component.text("§6§l🎉 Island Level Up!"));
            player.sendMessage("§7Your island is now level §e" + newLevel + "§7!");
            player.sendMessage(Component.text("§7New features and areas unlocked!"));
        }
    }
    
    public void playIslandExpansionAnimation(UUID islandId, Location center, int newSize, Player player) {
        IslandAnimation animation = new IslandAnimation(
            "§bIsland Expanded!",
            Arrays.asList(
                new ParticleEffect(Particle.CLOUD, center.clone().add(0, 1, 0), 1.0, 0.3, 1.0, 6, 8L),
                new ParticleEffect(Particle.HAPPY_VILLAGER, center.clone().add(0, 2, 0), 0.6, 0.6, 0.6, 4, 12L),
                new ParticleEffect(Particle.HEART, center.clone().add(0, 3, 0), 0.4, 0.4, 0.4, 2, 18L)
            ),
            Arrays.asList(
                new SoundEffect(Sound.BLOCK_GRASS_BREAK, center, 0.5f, 1.0f, 0L),
                new SoundEffect(Sound.ENTITY_VILLAGER_YES, center, 0.7f, 1.1f, 300L)
            ),
            250L // 12.5 seconds
        );
        
        startIslandAnimation(islandId, animation);
        
        // Send message to player
        if (player != null) {
            player.sendMessage(Component.text("§b§l📈 Island Expanded!"));
            player.sendMessage("§7Your island size is now §e" + newSize + "x" + newSize + "§7!");
            player.sendMessage(Component.text("§7More space for your adventures!"));
        }
    }
    
    public void playIslandDestructionAnimation(UUID islandId, Location center, Player player) {
        IslandAnimation animation = new IslandAnimation(
            "§cIsland Destroyed!",
            Arrays.asList(
                new ParticleEffect(Particle.LARGE_SMOKE, center.clone().add(0, 1, 0), 0.8, 0.8, 0.8, 5, 5L),
                new ParticleEffect(Particle.FLAME, center.clone().add(0, 2, 0), 0.5, 0.5, 0.5, 3, 8L),
                new ParticleEffect(Particle.SOUL_FIRE_FLAME, center.clone().add(0, 3, 0), 0.3, 0.3, 0.3, 2, 12L)
            ),
            Arrays.asList(
                new SoundEffect(Sound.ENTITY_GENERIC_EXPLODE, center, 0.8f, 1.0f, 0L),
                new SoundEffect(Sound.ENTITY_WITHER_AMBIENT, center, 0.6f, 1.2f, 200L)
            ),
            200L // 10 seconds
        );
        
        startIslandAnimation(islandId, animation);
        
        // Send message to player
        if (player != null) {
            player.sendMessage(Component.text("§c§l💥 Island Destroyed!"));
            player.sendMessage(Component.text("§7Your island has been removed!"));
        }
    }
    
    public void playIslandTeleportAnimation(UUID islandId, Location center, Player player) {
        IslandAnimation animation = new IslandAnimation(
            "§dTeleporting to Island!",
            Arrays.asList(
                new ParticleEffect(Particle.PORTAL, center.clone().add(0, 1, 0), 0.6, 0.6, 0.6, 8, 5L),
                new ParticleEffect(Particle.END_ROD, center.clone().add(0, 2, 0), 0.4, 0.4, 0.4, 4, 10L),
                new ParticleEffect(Particle.HAPPY_VILLAGER, center.clone().add(0, 3, 0), 0.3, 0.3, 0.3, 2, 15L)
            ),
            Arrays.asList(
                new SoundEffect(Sound.ENTITY_ENDERMAN_TELEPORT, center, 0.7f, 1.0f, 0L),
                new SoundEffect(Sound.ENTITY_VILLAGER_YES, center, 0.5f, 1.2f, 300L)
            ),
            150L // 7.5 seconds
        );
        
        startIslandAnimation(islandId, animation);
        
        // Send message to player
        if (player != null) {
            player.sendMessage(Component.text("§d§l🌍 Teleporting to your island..."));
        }
    }
    
    public void playIslandUpgradeAnimation(UUID islandId, Location center, String upgradeType, Player player) {
        IslandAnimation animation = new IslandAnimation(
            "§eIsland Upgraded!",
            List.of(
                new ParticleEffect(Particle.ENCHANT, center.clone().add(0, 2, 0), 0.5, 0.5, 0.5, 6, 8L),
                new ParticleEffect(Particle.HAPPY_VILLAGER, center.clone().add(0, 3, 0), 0.3, 0.3, 0.3, 4, 12L),
                new ParticleEffect(Particle.HAPPY_VILLAGER, center.clone().add(0, 1, 0), 0.4, 0.4, 0.4, 3, 15L)
            ),
            Arrays.asList(
                new SoundEffect(Sound.ENTITY_PLAYER_LEVELUP, center, 0.8f, 1.3f, 0L),
                new SoundEffect(Sound.ENTITY_VILLAGER_CELEBRATE, center, 0.6f, 1.1f, 400L)
            ),
            200L // 10 seconds
        );
        
        startIslandAnimation(islandId, animation);
        
        // Send message to player
        if (player != null) {
            player.sendMessage(Component.text("§e§l⚡ Island Upgraded!"));
            player.sendMessage("§7" + upgradeType + " has been upgraded!");
        }
    }
    
    public void playIslandEventAnimation(UUID islandId, Location center, String eventType, Player player) {
        IslandAnimation animation = switch (eventType.toLowerCase()) {
            case "storm" -> new IslandAnimation(
                "§9Storm Event!",
                Arrays.asList(
                    new ParticleEffect(Particle.CLOUD, center.clone().add(0, 3, 0), 1.0, 0.5, 1.0, 8, 5L),
                    new ParticleEffect(Particle.SPLASH, center.clone().add(0, 1, 0), 0.6, 0.6, 0.6, 5, 8L),
                    new ParticleEffect(Particle.HAPPY_VILLAGER, center.clone().add(0, 2, 0), 0.4, 0.4, 0.4, 2, 12L)
                ),
                Arrays.asList(
                    new SoundEffect(Sound.WEATHER_RAIN_ABOVE, center, 0.7f, 1.0f, 0L),
                    new SoundEffect(Sound.ENTITY_VILLAGER_YES, center, 0.5f, 1.1f, 500L)
                ),
                300L
            );
            case "sunny" -> new IslandAnimation(
                "§6Sunny Day!",
                Arrays.asList(
                    new ParticleEffect(Particle.END_ROD, center.clone().add(0, 3, 0), 0.5, 0.5, 0.5, 6, 8L),
                    new ParticleEffect(Particle.HAPPY_VILLAGER, center.clone().add(0, 2, 0), 0.4, 0.4, 0.4, 4, 12L),
                    new ParticleEffect(Particle.HEART, center.clone().add(0, 1, 0), 0.3, 0.3, 0.3, 3, 15L)
                ),
                Arrays.asList(
                    new SoundEffect(Sound.AMBIENT_CAVE, center, 0.4f, 1.2f, 0L),
                    new SoundEffect(Sound.ENTITY_VILLAGER_CELEBRATE, center, 0.6f, 1.0f, 400L)
                ),
                250L
            );
            case "night" -> new IslandAnimation(
                "§5Night Time!",
                Arrays.asList(
                    new ParticleEffect(Particle.END_ROD, center.clone().add(0, 2, 0), 0.4, 0.4, 0.4, 5, 10L),
                    new ParticleEffect(Particle.SOUL_FIRE_FLAME, center.clone().add(0, 1, 0), 0.3, 0.3, 0.3, 3, 15L),
                    new ParticleEffect(Particle.HAPPY_VILLAGER, center.clone().add(0, 3, 0), 0.2, 0.2, 0.2, 2, 20L)
                ),
                Arrays.asList(
                    new SoundEffect(Sound.AMBIENT_CAVE, center, 0.5f, 0.8f, 0L),
                    new SoundEffect(Sound.ENTITY_VILLAGER_YES, center, 0.4f, 1.3f, 600L)
                ),
                300L
            );
            default -> new IslandAnimation(
                "§aSpecial Event!",
                Arrays.asList(
                    new ParticleEffect(Particle.HAPPY_VILLAGER, center.clone().add(0, 2, 0), 0.5, 0.5, 0.5, 4, 10L),
                    new ParticleEffect(Particle.HEART, center.clone().add(0, 3, 0), 0.3, 0.3, 0.3, 3, 15L)
                ),
                Arrays.asList(
                    new SoundEffect(Sound.ENTITY_VILLAGER_CELEBRATE, center, 0.6f, 1.0f, 0L)
                ),
                200L
            );
        };
        
        startIslandAnimation(islandId, animation);
        
        // Send message to player
        if (player != null) {
            player.sendMessage(Component.text("§a§l🎉 Special Island Event!"));
            player.sendMessage("§7" + eventType + " is happening on your island!");
        }
    }
    
    public void startIslandAnimation(UUID islandId, IslandAnimation animation) {
        if (activeIslandAnimations.containsKey(islandId)) {
            stopIslandAnimation(islandId);
        }
        
        activeIslandAnimations.put(islandId, animation);
        
        BukkitTask task = new BukkitRunnable() {
            private int ticks = 0;
            private final long maxTicks = animation.getDuration() / 50; // Convert ms to ticks
            
            @Override
            public void run() {
                if (ticks >= maxTicks) {
                    cancel();
                    activeIslandAnimations.remove(islandId);
                    islandAnimationTasks.remove(islandId);
                    return;
                }
                
                animation.play();
                ticks++;
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 1L);
        
        islandAnimationTasks.put(islandId, task);
    }
    
    public void stopIslandAnimation(UUID islandId) {
        BukkitTask task = islandAnimationTasks.remove(islandId);
        if (task != null) {
            task.cancel();
        }
        activeIslandAnimations.remove(islandId);
    }
    
    public Map<UUID, IslandAnimation> getActiveIslandAnimations() {
        return activeIslandAnimations;
    }
    
    public void cleanup() {
        for (BukkitTask task : islandAnimationTasks.values()) {
            task.cancel();
        }
        islandAnimationTasks.clear();
        activeIslandAnimations.clear();
    }
    
    // Inner classes
    public static class IslandAnimation {
        private final String title;
        private final List<ParticleEffect> particleEffects;
        private final List<SoundEffect> soundEffects;
        private final long duration;
        
        public IslandAnimation(String title, List<ParticleEffect> particleEffects, List<SoundEffect> soundEffects, long duration) {
            this.title = title;
            this.particleEffects = particleEffects;
            this.soundEffects = soundEffects;
            this.duration = duration;
        }
        
        public void play() {
            for (ParticleEffect effect : particleEffects) {
                effect.play();
            }
            for (SoundEffect effect : soundEffects) {
                effect.play();
            }
        }
        
        public String getTitle() { return title; }
        public long getDuration() { return duration; }
    }
    
    public static class ParticleEffect {
        private final Particle particle;
        private final Location location;
        private final double offsetX, offsetY, offsetZ;
        private final int count;
        private final long interval;
        private long lastPlayed = 0;
        
        public ParticleEffect(Particle particle, Location location, double offsetX, double offsetY, double offsetZ, int count, long interval) {
            this.particle = particle;
            this.location = location;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.count = count;
            this.interval = interval;
        }
        
        public void play() {
            if (java.lang.System.currentTimeMillis() - lastPlayed >= interval) {
                location.getWorld().spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, 0.1);
                lastPlayed = java.lang.System.currentTimeMillis();
            }
        }
    }
    
    public static class SoundEffect {
        private final Sound sound;
        private final Location location;
        private final float volume, pitch;
        private final long delay;
        private long lastPlayed = 0;
        
        public SoundEffect(Sound sound, Location location, float volume, float pitch, long delay) {
            this.sound = sound;
            this.location = location;
            this.volume = volume;
            this.pitch = pitch;
            this.delay = delay;
        }
        
        public void play() {
            if (java.lang.System.currentTimeMillis() - lastPlayed >= delay) {
                location.getWorld().playSound(location, sound, volume, pitch);
                lastPlayed = java.lang.System.currentTimeMillis();
            }
        }
    }
}
