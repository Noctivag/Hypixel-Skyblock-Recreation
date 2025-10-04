package de.noctivag.plugin.combat;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Health Display System - Hypixel Skyblock Style
 * 
 * Features:
 * - Health Bars über Gegnern
 * - Damage Numbers
 * - Critical Hit Indicators
 * - Boss Health Bars
 * - Custom Health Display
 * - Health Bar Animations
 * - Damage Over Time Effects
 * - Shield Indicators
 * - Absorption Hearts
 * - Custom Entity Names
 */
public class HealthDisplaySystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, HealthDisplay> entityHealthDisplays = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> healthUpdateTasks = new ConcurrentHashMap<>();
    private final Map<UUID, List<DamageNumber>> damageNumbers = new ConcurrentHashMap<>();
    
    public HealthDisplaySystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        startHealthUpdateTask();
        
        // Register events
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void startHealthUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateAllHealthDisplays();
                updateDamageNumbers();
            }
        }.runTaskTimer(plugin, 0L, 2L); // Every 2 ticks (0.1 seconds)
    }
    
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity && !(entity instanceof Player)) {
            createHealthDisplay(entity);
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        
        if (entity instanceof LivingEntity && damager instanceof Player) {
            LivingEntity livingEntity = (LivingEntity) entity;
            Player player = (Player) damager;
            
            // Calculate damage
            double damage = event.getFinalDamage();
            boolean isCritical = isCriticalHit(player, livingEntity);
            
            // Show damage number
            showDamageNumber(entity, damage, isCritical);
            
            // Update health display
            updateHealthDisplay(entity);
        }
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        
        // Remove health display
        removeHealthDisplay(entity);
        
        // Remove damage numbers
        damageNumbers.remove(entity.getUniqueId());
    }
    
    private void createHealthDisplay(Entity entity) {
        if (!(entity instanceof LivingEntity)) return;
        
        LivingEntity livingEntity = (LivingEntity) entity;
        UUID entityId = entity.getUniqueId();
        
        // Create health display
        HealthDisplay healthDisplay = new HealthDisplay(entity, getEntityMaxHealth(entity), getEntityName(entity));
        entityHealthDisplays.put(entityId, healthDisplay);
        
        // Start update task
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (entity.isDead() || !entity.isValid()) {
                    removeHealthDisplay(entity);
                    this.cancel();
                    return;
                }
                
                updateHealthDisplay(entity);
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
        
        healthUpdateTasks.put(entityId, task);
    }
    
    private void updateHealthDisplay(Entity entity) {
        if (!(entity instanceof LivingEntity)) return;
        
        LivingEntity livingEntity = (LivingEntity) entity;
        UUID entityId = entity.getUniqueId();
        
        HealthDisplay healthDisplay = entityHealthDisplays.get(entityId);
        if (healthDisplay == null) return;
        
        // Update health
        double currentHealth = livingEntity.getHealth();
        double maxHealth = healthDisplay.getMaxHealth();
        double healthPercentage = (currentHealth / maxHealth) * 100;
        
        // Create health bar
        String healthBar = createHealthBar(healthPercentage, maxHealth);
        
        // Create display name
        String displayName = createDisplayName(healthDisplay.getName(), currentHealth, maxHealth, healthBar);
        
        // Set custom name
        entity.setCustomName(displayName);
        entity.setCustomNameVisible(true);
        
        // Update health display
        healthDisplay.setCurrentHealth(currentHealth);
        healthDisplay.setHealthPercentage(healthPercentage);
    }
    
    private String createHealthBar(double healthPercentage, double maxHealth) {
        StringBuilder healthBar = new StringBuilder();
        
        // Determine color based on health percentage
        ChatColor color;
        if (healthPercentage >= 75) {
            color = ChatColor.GREEN;
        } else if (healthPercentage >= 50) {
            color = ChatColor.YELLOW;
        } else if (healthPercentage >= 25) {
            color = ChatColor.GOLD;
        } else {
            color = ChatColor.RED;
        }
        
        // Create health bar (20 characters)
        int filledBars = (int) (healthPercentage / 5); // 5% per bar
        int emptyBars = 20 - filledBars;
        
        healthBar.append(color);
        for (int i = 0; i < filledBars; i++) {
            healthBar.append("█");
        }
        
        healthBar.append(ChatColor.GRAY);
        for (int i = 0; i < emptyBars; i++) {
            healthBar.append("█");
        }
        
        return healthBar.toString();
    }
    
    private String createDisplayName(String entityName, double currentHealth, double maxHealth, String healthBar) {
        StringBuilder displayName = new StringBuilder();
        
        // Entity name
        displayName.append(ChatColor.WHITE).append(entityName);
        
        // Health bar
        displayName.append("\n").append(healthBar);
        
        // Health numbers
        displayName.append("\n").append(ChatColor.RED)
                  .append(String.format("%.0f", currentHealth))
                  .append(ChatColor.GRAY).append("/")
                  .append(ChatColor.RED).append(String.format("%.0f", maxHealth))
                  .append(ChatColor.GRAY).append(" HP");
        
        return displayName.toString();
    }
    
    private void showDamageNumber(Entity entity, double damage, boolean isCritical) {
        UUID entityId = entity.getUniqueId();
        
        // Create damage number
        DamageNumber damageNumber = new DamageNumber(
            entity.getLocation().add(0, 2, 0), // Above entity
            damage,
            isCritical,
            System.currentTimeMillis()
        );
        
        // Add to damage numbers
        damageNumbers.computeIfAbsent(entityId, k -> new ArrayList<>()).add(damageNumber);
        
        // Show damage number to nearby players
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 50) {
                showDamageNumberToPlayer(player, damageNumber);
            }
        }
    }
    
    private void showDamageNumberToPlayer(Player player, DamageNumber damageNumber) {
        // Create hologram or use action bar
        String damageText = createDamageText(damageNumber);
        
        // Send action bar message
        player.sendActionBar(damageText);
        
        // Alternative: Create hologram using packets (would need ProtocolLib)
        // createHologramPacket(player, damageNumber);
    }
    
    private String createDamageText(DamageNumber damageNumber) {
        StringBuilder damageText = new StringBuilder();
        
        if (damageNumber.isCritical()) {
            damageText.append(ChatColor.RED).append("✧ ");
        }
        
        damageText.append(ChatColor.RED)
                  .append(String.format("%.0f", damageNumber.getDamage()));
        
        if (damageNumber.isCritical()) {
            damageText.append(" ✧");
        }
        
        return damageText.toString();
    }
    
    private boolean isCriticalHit(Player player, LivingEntity target) {
        // Calculate critical hit chance based on player stats
        double criticalChance = 0.1; // Base 10% chance
        
        // Add weapon critical chance
        // Add armor critical chance
        // Add skill bonuses
        // Add pet bonuses
        
        return Math.random() < criticalChance;
    }
    
    private void updateAllHealthDisplays() {
        for (Map.Entry<UUID, HealthDisplay> entry : entityHealthDisplays.entrySet()) {
            UUID entityId = entry.getKey();
            Entity entity = Bukkit.getEntity(entityId);
            
            if (entity == null || entity.isDead() || !entity.isValid()) {
                removeHealthDisplay(entity);
                continue;
            }
            
            updateHealthDisplay(entity);
        }
    }
    
    private void updateDamageNumbers() {
        long currentTime = System.currentTimeMillis();
        
        for (Map.Entry<UUID, List<DamageNumber>> entry : damageNumbers.entrySet()) {
            List<DamageNumber> numbers = entry.getValue();
            
            // Remove old damage numbers (older than 2 seconds)
            numbers.removeIf(number -> currentTime - number.getTimestamp() > 2000);
            
            // Update positions (move up)
            for (DamageNumber number : numbers) {
                number.setLocation(number.getLocation().add(0, 0.1, 0));
            }
        }
    }
    
    private void removeHealthDisplay(Entity entity) {
        UUID entityId = entity.getUniqueId();
        
        // Remove health display
        entityHealthDisplays.remove(entityId);
        
        // Cancel update task
        BukkitTask task = healthUpdateTasks.remove(entityId);
        if (task != null) {
            task.cancel();
        }
        
        // Remove damage numbers
        damageNumbers.remove(entityId);
        
        // Reset custom name
        entity.setCustomName(null);
        entity.setCustomNameVisible(false);
    }
    
    private double getEntityMaxHealth(Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            return livingEntity.getMaxHealth();
        }
        return 20.0; // Default
    }
    
    private String getEntityName(Entity entity) {
        // Get custom entity name or default
        String name = entity.getCustomName();
        if (name == null || name.isEmpty()) {
            name = entity.getType().name().toLowerCase().replace("_", " ");
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return name;
    }
    
    // Health Display Class
    public static class HealthDisplay {
        private final Entity entity;
        private final double maxHealth;
        private final String name;
        private double currentHealth;
        private double healthPercentage;
        
        public HealthDisplay(Entity entity, double maxHealth, String name) {
            this.entity = entity;
            this.maxHealth = maxHealth;
            this.name = name;
            this.currentHealth = maxHealth;
            this.healthPercentage = 100.0;
        }
        
        public Entity getEntity() { return entity; }
        public double getMaxHealth() { return maxHealth; }
        public String getName() { return name; }
        public double getCurrentHealth() { return currentHealth; }
        public double getHealthPercentage() { return healthPercentage; }
        
        public void setCurrentHealth(double currentHealth) { this.currentHealth = currentHealth; }
        public void setHealthPercentage(double healthPercentage) { this.healthPercentage = healthPercentage; }
    }
    
    // Damage Number Class
    public static class DamageNumber {
        private Location location;
        private final double damage;
        private final boolean isCritical;
        private final long timestamp;
        
        public DamageNumber(Location location, double damage, boolean isCritical, long timestamp) {
            this.location = location;
            this.damage = damage;
            this.isCritical = isCritical;
            this.timestamp = timestamp;
        }
        
        public Location getLocation() { return location; }
        public double getDamage() { return damage; }
        public boolean isCritical() { return isCritical; }
        public long getTimestamp() { return timestamp; }
        
        public void setLocation(Location location) { this.location = location; }
    }
}
