package de.noctivag.skyblock.mobs;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central service for managing all custom mobs
 * Handles spawning, tracking, and event processing
 */
public class MobManager implements Service, Listener {
    
    private final SkyblockPlugin plugin;
    private final Map<UUID, CustomMob> activeMobs = new ConcurrentHashMap<>();
    private final Map<String, Class<? extends CustomMob>> mobTypes = new ConcurrentHashMap<>();
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    private boolean enabled = true;
    
    public MobManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        registerMobTypes();
    }
    
    /**
     * Register all available mob types
     */
    private void registerMobTypes() {
        // Register mob types here
        mobTypes.put("LAPIS_ZOMBIE", de.noctivag.skyblock.mobs.impl.LapisZombie.class);
        mobTypes.put("CRYPT_GHOUL", de.noctivag.skyblock.mobs.impl.CryptGhoul.class);
        mobTypes.put("ZOMBIE_VILLAGER", de.noctivag.skyblock.mobs.impl.ZombieVillager.class);
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing MobManager...");
        
        // Register event listeners
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("MobManager initialized successfully!");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down MobManager...");
        
        // Remove all active mobs
        for (CustomMob mob : activeMobs.values()) {
            mob.remove();
        }
        activeMobs.clear();
        
        status = SystemStatus.SHUTDOWN;
        plugin.getLogger().info("MobManager shutdown complete!");
    }
    
    @Override
    public String getName() {
        return "MobManager";
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * Spawn a mob by ID at the specified location
     */
    public CustomMob spawnMob(String mobId, Location location) {
        if (!enabled) {
            return null;
        }
        
        Class<? extends CustomMob> mobClass = mobTypes.get(mobId);
        if (mobClass == null) {
            plugin.getLogger().warning("Unknown mob type: " + mobId);
            return null;
        }
        
        try {
            // Create mob instance using reflection
            CustomMob mob = mobClass.getDeclaredConstructor(String.class, Location.class)
                .newInstance(mobId, location);
            
            // Spawn the mob
            mob.spawn();
            
            // Register the mob
            activeMobs.put(mob.getUuid(), mob);
            
            plugin.getLogger().info("Spawned mob: " + mobId + " at " + location);
            return mob;
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to spawn mob " + mobId + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get a mob by its UUID
     */
    public CustomMob getMob(UUID uuid) {
        return activeMobs.get(uuid);
    }
    
    /**
     * Get a mob by its Bukkit entity
     */
    public CustomMob getMob(Entity entity) {
        for (CustomMob mob : activeMobs.values()) {
            if (mob.getEntity() != null && mob.getEntity().equals(entity)) {
                return mob;
            }
        }
        return null;
    }
    
    /**
     * Remove a mob
     */
    public void removeMob(UUID uuid) {
        CustomMob mob = activeMobs.remove(uuid);
        if (mob != null) {
            mob.remove();
        }
    }
    
    /**
     * Get all active mobs
     */
    public Map<UUID, CustomMob> getActiveMobs() {
        return new ConcurrentHashMap<>(activeMobs);
    }
    
    /**
     * Register a new mob type
     */
    public void registerMobType(String mobId, Class<? extends CustomMob> mobClass) {
        mobTypes.put(mobId, mobClass);
        plugin.getLogger().info("Registered mob type: " + mobId);
    }
    
    /**
     * Handle entity damage events
     */
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!enabled) {
            return;
        }
        
        CustomMob attackerMob = getMob(event.getDamager());
        CustomMob targetMob = getMob(event.getEntity());
        
        if (attackerMob != null && event.getEntity() instanceof Player) {
            // Custom mob attacking a player
            Player target = (Player) event.getEntity();
            attackerMob.attack(target);
            event.setCancelled(true); // Handle damage ourselves
        }
        
        if (targetMob != null && event.getDamager() instanceof Player) {
            // Player attacking a custom mob
            Player attacker = (Player) event.getDamager();
            targetMob.takeDamage(event.getDamage());
            targetMob.executeAbilities(AbilityTrigger.ON_DAMAGE, attacker);
            
            if (targetMob.isDead()) {
                // Handle death
                targetMob.executeAbilities(AbilityTrigger.ON_DEATH, attacker);
                removeMob(targetMob.getUuid());
            }
        }
    }
    
    /**
     * Handle entity death events
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!enabled) {
            return;
        }
        
        CustomMob mob = getMob(event.getEntity());
        if (mob != null) {
            // Handle custom mob death
            mob.executeAbilities(AbilityTrigger.ON_DEATH);
            removeMob(mob.getUuid());
            
            // Clear drops (we'll handle loot through LootService)
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
    }
}
