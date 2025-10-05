package de.noctivag.skyblock.mobs;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Abstract base class for all custom mobs
 * Provides a framework for custom mob behavior and abilities
 */
public abstract class CustomMob {
    
    protected final String mobId;
    protected final EntityType entityType;
    protected final Location spawnLocation;
    protected final UUID uuid;
    
    // Custom stats
    protected double maxHealth;
    protected double currentHealth;
    protected double damage;
    protected double defense;
    protected double combatXP;
    
    // Bukkit entity
    protected LivingEntity entity;
    
    // Abilities system
    protected final List<MobAbility> abilities = new ArrayList<>();
    
    public CustomMob(String mobId, EntityType entityType, Location spawnLocation, 
                     double maxHealth, double damage, double defense, double combatXP) {
        this.mobId = mobId;
        this.entityType = entityType;
        this.spawnLocation = spawnLocation;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.damage = damage;
        this.defense = defense;
        this.combatXP = combatXP;
        this.uuid = UUID.randomUUID();
    }
    
    /**
     * Get the mob's display name
     */
    public abstract String getName();
    
    /**
     * Get the entity type
     */
    public EntityType getEntityType() {
        return entityType;
    }
    
    /**
     * Get the loot table ID for this mob
     */
    public abstract String getLootTableId();
    
    /**
     * Spawn the mob in the world
     */
    public void spawn() {
        if (entity != null) {
            return; // Already spawned
        }
        
        entity = (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, entityType);
        applyBaseAttributes(entity);
        
        // Register abilities
        for (MobAbility ability : abilities) {
            ability.onSpawn(entity);
        }
    }
    
    /**
     * Apply base attributes to the Bukkit entity
     */
    public void applyBaseAttributes(Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            
            // Set custom name
            livingEntity.setCustomName(getName());
            livingEntity.setCustomNameVisible(true);
            
            // Set health
            livingEntity.setMaxHealth(maxHealth);
            livingEntity.setHealth(currentHealth);
            
            // Set AI
            livingEntity.setAI(true);
        }
    }
    
    /**
     * Execute abilities based on trigger
     */
    public void executeAbilities(AbilityTrigger trigger) {
        for (MobAbility ability : abilities) {
            if (ability.getTrigger() == trigger && ability.canExecute()) {
                ability.execute(entity, null); // Target will be set by the ability
            }
        }
    }
    
    /**
     * Execute abilities with a specific target
     */
    public void executeAbilities(AbilityTrigger trigger, Player target) {
        for (MobAbility ability : abilities) {
            if (ability.getTrigger() == trigger && ability.canExecute()) {
                ability.execute(entity, target);
            }
        }
    }
    
    /**
     * Take damage
     */
    public void takeDamage(double damage) {
        currentHealth = Math.max(0, currentHealth - damage);
        if (entity != null) {
            entity.setHealth(currentHealth);
        }
        
        // Execute damage abilities
        executeAbilities(AbilityTrigger.ON_DAMAGE);
    }
    
    /**
     * Attack a player
     */
    public void attack(Player target) {
        if (entity == null || target == null) {
            return;
        }
        
                // Calculate damage with defense
                double finalDamage = damage;
        target.damage(finalDamage, entity);
        
        // Execute attack abilities
        executeAbilities(AbilityTrigger.ON_ATTACK, target);
    }
    
    /**
     * Check if the mob is dead
     */
    public boolean isDead() {
        return currentHealth <= 0;
    }
    
    /**
     * Remove the mob from the world
     */
    public void remove() {
        if (entity != null) {
            entity.remove();
            entity = null;
        }
    }
    
    // Getters
    public String getMobId() { return mobId; }
    public UUID getUuid() { return uuid; }
    public Location getSpawnLocation() { return spawnLocation; }
    public double getMaxHealth() { return maxHealth; }
    public double getCurrentHealth() { return currentHealth; }
    public double getDamage() { return damage; }
    public double getDefense() { return defense; }
    public double getCombatXP() { return combatXP; }
    public LivingEntity getEntity() { return entity; }
    public List<MobAbility> getAbilities() { return new ArrayList<>(abilities); }
    
    // Setters
    public void setMaxHealth(double maxHealth) { this.maxHealth = maxHealth; }
    public void setCurrentHealth(double currentHealth) { this.currentHealth = currentHealth; }
    public void setDamage(double damage) { this.damage = damage; }
    public void setDefense(double defense) { this.defense = defense; }
    public void setCombatXP(double combatXP) { this.combatXP = combatXP; }
    
    /**
     * Add an ability to the mob
     */
    public void addAbility(MobAbility ability) {
        abilities.add(ability);
    }
}
