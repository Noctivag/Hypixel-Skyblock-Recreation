package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AnimatedGUI extends CustomGUI {
    protected final SkyblockPlugin plugin;
    protected final Player player;
    protected final Map<Integer, ItemStack> originalItems = new ConcurrentHashMap<>();
    protected final Map<Integer, BukkitTask> animations = new ConcurrentHashMap<>();
    protected final Map<Integer, AnimationType> animationTypes = new ConcurrentHashMap<>();
    
    public AnimatedGUI(SkyblockPlugin plugin, Player player, int size, String title) {
        super(size, Component.text(title).color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.plugin = plugin;
        this.player = player;
    }
    
    public abstract void setupItems();
    
    public void open() {
        setupItems();
        storeOriginalItems();
        startAnimations();
        playOpenSound();
        player.openInventory(getInventory());
    }
    
    private void storeOriginalItems() {
        for (int i = 0; i < getInventory().getSize(); i++) {
            ItemStack item = getInventory().getItem(i);
            if (item != null) {
                originalItems.put(i, item.clone());
            }
        }
    }
    
    private void startAnimations() {
        for (Map.Entry<Integer, AnimationType> entry : animationTypes.entrySet()) {
            int slot = entry.getKey();
            AnimationType type = entry.getValue();
            
            BukkitTask task = switch (type) {
                case PULSE -> startPulseAnimation(slot);
                case ROTATE -> startRotateAnimation(slot);
                case GLOW -> startGlowAnimation(slot);
                case SPARKLE -> startSparkleAnimation(slot);
                case BOUNCE -> startBounceAnimation(slot);
                case FADE -> startFadeAnimation(slot);
                case RAINBOW -> startRainbowAnimation(slot);
                case FIRE -> startFireAnimation(slot);
            };
            
            if (task != null) {
                animations.put(slot, task);
            }
        }
    }
    
    private BukkitTask startPulseAnimation(int slot) {
        return new BukkitRunnable() {
            int tick = 0;
            
            @Override
            public void run() {
                if (!player.isOnline() || !player.getOpenInventory().getTopInventory().equals(getInventory())) {
                    cancel();
                    return;
                }
                
                ItemStack original = originalItems.get(slot);
                if (original == null) return;
                
                ItemStack animated = original.clone();
                ItemMeta meta = animated.getItemMeta();
                if (meta != null) {
                    double scale = 1.0 + 0.3 * Math.sin(tick * 0.2);
                    String name = meta.getDisplayName();
                    if (name != null) {
                        meta.displayName(Component.text(name).color(NamedTextColor.GOLD));
                    }
                    animated.setItemMeta(meta);
                }
                
                getInventory().setItem(slot, animated);
                tick++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    private BukkitTask startRotateAnimation(int slot) {
        return new BukkitRunnable() {
            int tick = 0;
            
            @Override
            public void run() {
                if (!player.isOnline() || !player.getOpenInventory().getTopInventory().equals(getInventory())) {
                    cancel();
                    return;
                }
                
                ItemStack original = originalItems.get(slot);
                if (original == null) return;
                
                ItemStack animated = original.clone();
                ItemMeta meta = animated.getItemMeta();
                if (meta != null) {
                    String name = meta.getDisplayName();
                    if (name != null) {
                        // Rotate through colors
                        NamedTextColor[] colors = {
                            NamedTextColor.RED, NamedTextColor.GOLD, NamedTextColor.YELLOW,
                            NamedTextColor.GREEN, NamedTextColor.AQUA, NamedTextColor.BLUE,
                            NamedTextColor.LIGHT_PURPLE, NamedTextColor.DARK_PURPLE
                        };
                        NamedTextColor color = colors[tick % colors.length];
                        meta.displayName(Component.text(name).color(color));
                    }
                    animated.setItemMeta(meta);
                }
                
                getInventory().setItem(slot, animated);
                tick++;
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
    
    private BukkitTask startGlowAnimation(int slot) {
        return new BukkitRunnable() {
            int tick = 0;
            
            @Override
            public void run() {
                if (!player.isOnline() || !player.getOpenInventory().getTopInventory().equals(getInventory())) {
                    cancel();
                    return;
                }
                
                ItemStack original = originalItems.get(slot);
                if (original == null) return;
                
                ItemStack animated = original.clone();
                ItemMeta meta = animated.getItemMeta();
                if (meta != null) {
                    String name = meta.getDisplayName();
                    if (name != null) {
                        double glow = 0.5 + 0.5 * Math.sin(tick * 0.3);
                        NamedTextColor color = glow > 0.7 ? NamedTextColor.YELLOW : NamedTextColor.GOLD;
                        meta.displayName(Component.text(name).color(color).decorate(TextDecoration.BOLD));
                    }
                    animated.setItemMeta(meta);
                }
                
                getInventory().setItem(slot, animated);
                tick++;
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private BukkitTask startSparkleAnimation(int slot) {
        return new BukkitRunnable() {
            int tick = 0;
            
            @Override
            public void run() {
                if (!player.isOnline() || !player.getOpenInventory().getTopInventory().equals(getInventory())) {
                    cancel();
                    return;
                }
                
                ItemStack original = originalItems.get(slot);
                if (original == null) return;
                
                ItemStack animated = original.clone();
                ItemMeta meta = animated.getItemMeta();
                if (meta != null) {
                    String name = meta.getDisplayName();
                    if (name != null) {
                        // Add sparkle effect
                        String sparkle = tick % 4 == 0 ? " ✨" : "";
                        meta.displayName(Component.text(name + sparkle).color(NamedTextColor.YELLOW));
                    }
                    animated.setItemMeta(meta);
                }
                
                getInventory().setItem(slot, animated);
                tick++;
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }
    
    private BukkitTask startBounceAnimation(int slot) {
        return new BukkitRunnable() {
            int tick = 0;
            
            @Override
            public void run() {
                if (!player.isOnline() || !player.getOpenInventory().getTopInventory().equals(getInventory())) {
                    cancel();
                    return;
                }
                
                ItemStack original = originalItems.get(slot);
                if (original == null) return;
                
                ItemStack animated = original.clone();
                ItemMeta meta = animated.getItemMeta();
                if (meta != null) {
                    String name = meta.getDisplayName();
                    if (name != null) {
                        // Bounce effect
                        String bounce = tick % 6 < 3 ? " ⬆️" : " ⬇️";
                        meta.displayName(Component.text(name + bounce).color(NamedTextColor.GREEN));
                    }
                    animated.setItemMeta(meta);
                }
                
                getInventory().setItem(slot, animated);
                tick++;
            }
        }.runTaskTimer(plugin, 0L, 4L);
    }
    
    private BukkitTask startFadeAnimation(int slot) {
        return new BukkitRunnable() {
            int tick = 0;
            
            @Override
            public void run() {
                if (!player.isOnline() || !player.getOpenInventory().getTopInventory().equals(getInventory())) {
                    cancel();
                    return;
                }
                
                ItemStack original = originalItems.get(slot);
                if (original == null) return;
                
                ItemStack animated = original.clone();
                ItemMeta meta = animated.getItemMeta();
                if (meta != null) {
                    String name = meta.getDisplayName();
                    if (name != null) {
                        // Fade effect
                        double fade = 0.3 + 0.7 * Math.sin(tick * 0.2);
                        NamedTextColor color = fade > 0.6 ? NamedTextColor.WHITE : NamedTextColor.GRAY;
                        meta.displayName(Component.text(name).color(color));
                    }
                    animated.setItemMeta(meta);
                }
                
                getInventory().setItem(slot, animated);
                tick++;
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private BukkitTask startRainbowAnimation(int slot) {
        return new BukkitRunnable() {
            int tick = 0;
            
            @Override
            public void run() {
                if (!player.isOnline() || !player.getOpenInventory().getTopInventory().equals(getInventory())) {
                    cancel();
                    return;
                }
                
                ItemStack original = originalItems.get(slot);
                if (original == null) return;
                
                ItemStack animated = original.clone();
                ItemMeta meta = animated.getItemMeta();
                if (meta != null) {
                    String name = meta.getDisplayName();
                    if (name != null) {
                        // Rainbow effect
                        NamedTextColor[] rainbow = {
                            NamedTextColor.RED, NamedTextColor.GOLD, NamedTextColor.YELLOW,
                            NamedTextColor.GREEN, NamedTextColor.AQUA, NamedTextColor.BLUE,
                            NamedTextColor.LIGHT_PURPLE
                        };
                        NamedTextColor color = rainbow[tick % rainbow.length];
                        meta.displayName(Component.text(name).color(color).decorate(TextDecoration.BOLD));
                    }
                    animated.setItemMeta(meta);
                }
                
                getInventory().setItem(slot, animated);
                tick++;
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
    
    private BukkitTask startFireAnimation(int slot) {
        return new BukkitRunnable() {
            int tick = 0;
            
            @Override
            public void run() {
                if (!player.isOnline() || !player.getOpenInventory().getTopInventory().equals(getInventory())) {
                    cancel();
                    return;
                }
                
                ItemStack original = originalItems.get(slot);
                if (original == null) return;
                
                ItemStack animated = original.clone();
                ItemMeta meta = animated.getItemMeta();
                if (meta != null) {
                    String name = meta.getDisplayName();
                    if (name != null) {
                        // Fire effect
                        String fire = tick % 3 == 0 ? " 🔥" : "";
                        meta.displayName(Component.text(name + fire).color(NamedTextColor.RED).decorate(TextDecoration.BOLD));
                    }
                    animated.setItemMeta(meta);
                }
                
                getInventory().setItem(slot, animated);
                tick++;
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
    
    protected void setAnimatedItem(int slot, Material material, String name, AnimationType animationType, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) {
                meta.lore(Arrays.stream(lore).map(Component::text).toList());
            }
            item.setItemMeta(meta);
        }
        
        getInventory().setItem(slot, item);
        animationTypes.put(slot, animationType);
    }
    
    public void setItem(int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) {
                meta.lore(Arrays.stream(lore).map(Component::text).toList());
            }
            item.setItemMeta(meta);
        }
        
        getInventory().setItem(slot, item);
    }
    
    protected void playOpenSound() {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
    }
    
    protected void playClickSound() {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 1.2f);
    }
    
    protected void playErrorSound() {
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1.0f);
    }
    
    protected void playSuccessSound() {
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1.0f);
    }
    
    public void close() {
        // Stop all animations
        for (BukkitTask task : animations.values()) {
            task.cancel();
        }
        animations.clear();
        animationTypes.clear();
        originalItems.clear();
    }
    
    public enum AnimationType {
        PULSE,
        ROTATE,
        GLOW,
        SPARKLE,
        BOUNCE,
        FADE,
        RAINBOW,
        FIRE
    }
}
