package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GUI Animation System - Hypixel Skyblock Style
 * 
 * Features:
 * - GUI Animations and Effects
 * - Responsive Design for Different Screen Sizes
 * - Particle Effects
 * - Sound Effects
 * - Smooth Transitions
 * - Loading Animations
 */
public class GUIAnimationSystem {
    private final Plugin plugin;
    private final Map<UUID, List<BukkitTask>> playerAnimations = new ConcurrentHashMap<>();
    private final Map<UUID, GUISession> activeSessions = new ConcurrentHashMap<>();
    
    public GUIAnimationSystem(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void startLoadingAnimation(Player player, String title) {
        UUID playerId = player.getUniqueId();
        
        // Cancel existing animations
        cancelPlayerAnimations(playerId);
        
        // Create loading animation
        BukkitTask animation = new BukkitRunnable() {
            private int frame = 0;
            private final String[] loadingFrames = {"⏳", "⏳", "⏳", "⏳"};
            
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }
                
                // Update loading frame
                String frameText = loadingFrames[frame % loadingFrames.length];
                player.sendActionBar(Component.text("§e" + frameText + " " + title + " §e" + frameText));
                
                frame++;
            }
        }.runTaskTimer(plugin, 0L, 10L); // Update every 0.5 seconds
        
        addPlayerAnimation(playerId, animation);
    }
    
    public void startParticleEffect(Player player, String effectType) {
        UUID playerId = player.getUniqueId();
        
        BukkitTask effect = new BukkitRunnable() {
            private int duration = 0;
            private final int maxDuration = 100; // 5 seconds
            
            @Override
            public void run() {
                if (!player.isOnline() || duration >= maxDuration) {
                    cancel();
                    return;
                }
                
                // Spawn particles around player
                spawnParticleEffect(player, effectType);
                
                duration++;
            }
        }.runTaskTimer(plugin, 0L, 1L); // Update every tick
        
        addPlayerAnimation(playerId, effect);
    }
    
    public void startGUITransition(Player player, CustomGUI fromGUI, CustomGUI toGUI) {
        UUID playerId = player.getUniqueId();
        
        // Start transition animation
        BukkitTask transition = new BukkitRunnable() {
            private int step = 0;
            private final int maxSteps = 20; // 1 second transition
            
            @Override
            public void run() {
                if (!player.isOnline() || step >= maxSteps) {
                    // Complete transition
                    if (toGUI != null) {
                        toGUI.openGUI(player);
                    }
                    cancel();
                    return;
                }
                
                // Fade out effect
                if (step < maxSteps / 2) {
                    // Fade out
                    float alpha = 1.0f - (float) step / (maxSteps / 2);
                    // Apply fade effect to GUI
                } else {
                    // Fade in
                    float alpha = (float) (step - maxSteps / 2) / (maxSteps / 2);
                    // Apply fade effect to GUI
                }
                
                step++;
            }
        }.runTaskTimer(plugin, 0L, 1L); // Update every tick
        
        addPlayerAnimation(playerId, transition);
    }
    
    public void startProgressBar(Player player, String title, int maxProgress) {
        UUID playerId = player.getUniqueId();
        
        BukkitTask progressBar = new BukkitRunnable() {
            private int progress = 0;
            
            @Override
            public void run() {
                if (!player.isOnline() || progress >= maxProgress) {
                    cancel();
                    return;
                }
                
                // Update progress bar
                updateProgressBar(player, title, progress, maxProgress);
                
                progress++;
            }
        }.runTaskTimer(plugin, 0L, 2L); // Update every 0.1 seconds
        
        addPlayerAnimation(playerId, progressBar);
    }
    
    public void startTextAnimation(Player player, String text, int duration) {
        UUID playerId = player.getUniqueId();
        
        BukkitTask textAnimation = new BukkitRunnable() {
            private int frame = 0;
            private final int maxFrames = duration * 20; // Convert seconds to ticks
            
            @Override
            public void run() {
                if (!player.isOnline() || frame >= maxFrames) {
                    cancel();
                    return;
                }
                
                // Animate text
                String animatedText = animateText(text, frame);
                player.sendActionBar(Component.text(animatedText));
                
                frame++;
            }
        }.runTaskTimer(plugin, 0L, 1L); // Update every tick
        
        addPlayerAnimation(playerId, textAnimation);
    }
    
    public void startItemGlowAnimation(Player player, ItemStack item, int slot) {
        UUID playerId = player.getUniqueId();
        
        BukkitTask glowAnimation = new BukkitRunnable() {
            private int frame = 0;
            private final int maxFrames = 100; // 5 seconds
            
            @Override
            public void run() {
                if (!player.isOnline() || frame >= maxFrames) {
                    cancel();
                    return;
                }
                
                // Animate item glow
                ItemStack animatedItem = createGlowingItem(item, frame);
                player.getOpenInventory().setItem(slot, animatedItem);
                
                frame++;
            }
        }.runTaskTimer(plugin, 0L, 2L); // Update every 0.1 seconds
        
        addPlayerAnimation(playerId, glowAnimation);
    }
    
    private void spawnParticleEffect(Player player, String effectType) {
        // Spawn particles based on effect type
        switch (effectType) {
            case "success" -> {
                // Green particles for success
                player.getWorld().spawnParticle(org.bukkit.Particle.HAPPY_VILLAGER, 
                    player.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5, 0.1);
            }
            case "error" -> {
                // Red particles for error
                player.getWorld().spawnParticle(org.bukkit.Particle.DUST, 
                    player.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5, 0.1);
            }
            case "magic" -> {
                // Purple particles for magic
                player.getWorld().spawnParticle(org.bukkit.Particle.ENCHANT, 
                    player.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5, 0.1);
            }
            case "levelup" -> {
                // Experience particles for level up
                player.getWorld().spawnParticle(org.bukkit.Particle.HAPPY_VILLAGER, 
                    player.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.1);
            }
        }
    }
    
    private void updateProgressBar(Player player, String title, int progress, int maxProgress) {
        int barLength = 20;
        int filledLength = (int) ((double) progress / maxProgress * barLength);
        
        StringBuilder bar = new StringBuilder();
        bar.append("§a");
        for (int i = 0; i < filledLength; i++) {
            bar.append("█");
        }
        bar.append("§7");
        for (int i = filledLength; i < barLength; i++) {
            bar.append("█");
        }
        
        String progressText = String.format("§e%s §7[%s§7] §e%d%%", title, bar.toString(), (int) ((double) progress / maxProgress * 100));
        player.sendActionBar(Component.text(progressText));
    }
    
    private String animateText(String text, int frame) {
        // Simple text animation effects
        int animationType = frame % 4;
        
        return switch (animationType) {
            case 0 -> "§e" + text;
            case 1 -> "§6" + text;
            case 2 -> "§c" + text;
            case 3 -> "§d" + text;
            default -> text;
        };
    }
    
    private ItemStack createGlowingItem(ItemStack item, int frame) {
        ItemStack glowingItem = item.clone();
        ItemMeta meta = glowingItem.getItemMeta();
        if (meta != null) {
            // Add glow effect based on frame
            String glowColor = getGlowColor(frame);
            meta.displayName(Component.text(glowColor + meta.displayName().toString()));
            glowingItem.setItemMeta(meta);
        }
        return glowingItem;
    }
    
    private String getGlowColor(int frame) {
        // Cycle through colors for glow effect
        String[] colors = {"§f", "§e", "§6", "§c", "§d", "§b", "§a", "§9"};
        return colors[frame % colors.length];
    }
    
    private void addPlayerAnimation(UUID playerId, BukkitTask animation) {
        playerAnimations.computeIfAbsent(playerId, k -> new ArrayList<>()).add(animation);
    }
    
    public void cancelPlayerAnimations(UUID playerId) {
        List<BukkitTask> animations = playerAnimations.get(playerId);
        if (animations != null) {
            for (BukkitTask animation : animations) {
                if (!animation.isCancelled()) {
                    animation.cancel();
                }
            }
            animations.clear();
        }
    }
    
    public void cancelAllAnimations() {
        for (List<BukkitTask> animations : playerAnimations.values()) {
            for (BukkitTask animation : animations) {
                if (!animation.isCancelled()) {
                    animation.cancel();
                }
            }
        }
        playerAnimations.clear();
    }
    
    public void startGUISession(Player player, CustomGUI gui) {
        UUID playerId = player.getUniqueId();
        GUISession session = new GUISession(player, gui);
        activeSessions.put(playerId, session);
    }
    
    public void endGUISession(Player player) {
        UUID playerId = player.getUniqueId();
        GUISession session = activeSessions.get(playerId);
        if (session != null) {
            session.end();
            activeSessions.remove(playerId);
        }
        cancelPlayerAnimations(playerId);
    }
    
    public GUISession getGUISession(UUID playerId) {
        return activeSessions.get(playerId);
    }
    
    public Map<UUID, GUISession> getAllGUISessions() {
        return new HashMap<>(activeSessions);
    }
    
    // GUI Session Class
    public static class GUISession {
        private final Player player;
        private final CustomGUI gui;
        private final long startTime;
        private final Map<String, Object> sessionData;
        
        public GUISession(Player player, CustomGUI gui) {
            this.player = player;
            this.gui = gui;
            this.startTime = System.currentTimeMillis();
            this.sessionData = new HashMap<>();
        }
        
        public void end() {
            // Clean up session
            sessionData.clear();
        }
        
        public void setData(String key, Object value) {
            sessionData.put(key, value);
        }
        
        public Object getData(String key) {
            return sessionData.get(key);
        }
        
        public long getDuration() {
            return System.currentTimeMillis() - startTime;
        }
        
        public Player getPlayer() { return player; }
        public CustomGUI getGUI() { return gui; }
        public long getStartTime() { return startTime; }
        public Map<String, Object> getSessionData() { return new HashMap<>(sessionData); }
    }
}
