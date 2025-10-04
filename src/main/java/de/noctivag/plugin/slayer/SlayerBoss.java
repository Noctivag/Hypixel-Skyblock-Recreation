package de.noctivag.plugin.slayer;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Slayer Boss - Represents a spawned slayer boss with AI and mechanics
 */
public class SlayerBoss {
    
    private final LivingEntity entity;
    private final SlayerSystem.SlayerType type;
    private final SlayerSystem.SlayerTier tier;
    private final Player spawner;
    private final int maxHealth;
    private final int damage;
    private final long spawnTime;
    
    private int phase;
    private long lastAttack;
    private long lastSpecialAbility;
    
    public SlayerBoss(LivingEntity entity, SlayerSystem.SlayerType type, SlayerSystem.SlayerTier tier, 
                     Player spawner, int maxHealth, int damage) {
        this.entity = entity;
        this.type = type;
        this.tier = tier;
        this.spawner = spawner;
        this.maxHealth = maxHealth;
        this.damage = damage;
        this.spawnTime = System.currentTimeMillis();
        
        this.phase = 1;
        this.lastAttack = System.currentTimeMillis();
        this.lastSpecialAbility = System.currentTimeMillis();
    }
    
    /**
     * Update boss AI and mechanics
     */
    public void update() {
        if (!isAlive()) return;
        
        long currentTime = System.currentTimeMillis();
        
        // Basic AI - find and attack nearest player
        updateTargeting();
        
        // Special abilities based on type and phase
        if (currentTime - lastSpecialAbility >= getSpecialAbilityCooldown()) {
            useSpecialAbility();
            lastSpecialAbility = currentTime;
        }
        
        // Phase transitions
        updatePhase();
        
        // Visual effects
        updateVisualEffects();
    }
    
    /**
     * Update boss targeting
     */
    private void updateTargeting() {
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;
        
        for (Entity nearby : entity.getNearbyEntities(50, 50, 50)) {
            if (nearby instanceof Player player) {
                double distance = player.getLocation().distance(entity.getLocation());
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestPlayer = player;
                }
            }
        }
        
        if (nearestPlayer != null && nearestDistance <= 20) {
            // Face the player
            entity.lookAt(nearestPlayer);
            
            // Attack if close enough
            if (nearestDistance <= 3 && System.currentTimeMillis() - lastAttack >= 1000) {
                attackPlayer(nearestPlayer);
                lastAttack = System.currentTimeMillis();
            }
        }
    }
    
    /**
     * Attack a player
     */
    private void attackPlayer(Player player) {
        // Deal damage
        player.damage(damage, entity);
        
        // Visual effects
        player.getWorld().spawnParticle(Particle.CRIT, player.getLocation(), 10, 0.5, 0.5, 0.5, 0.1);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
    }
    
    /**
     * Use special ability based on slayer type
     */
    private void useSpecialAbility() {
        switch (type) {
            case ZOMBIE -> useZombieAbility();
            case SPIDER -> useSpiderAbility();
            case WOLF -> useWolfAbility();
            case ENDERMAN -> useEndermanAbility();
            case BLAZE -> useBlazeAbility();
            case VAMPIRE -> useVampireAbility();
        }
    }
    
    /**
     * Zombie slayer special abilities
     */
    private void useZombieAbility() {
        if (tier.getTier() >= 3) {
            // Zombie Army - spawn zombie minions
            for (int i = 0; i < 3; i++) {
                Location spawnLoc = entity.getLocation().add(
                    (Math.random() - 0.5) * 10, 0, (Math.random() - 0.5) * 10);
                entity.getWorld().spawnEntity(spawnLoc, org.bukkit.entity.EntityType.ZOMBIE);
            }
        }
        
        if (tier.getTier() >= 4) {
            // Berserk - increase attack speed
            entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                org.bukkit.potion.PotionEffectType.SPEED, 200, 1, false, false));
        }
    }
    
    /**
     * Spider slayer special abilities
     */
    private void useSpiderAbility() {
        if (tier.getTier() >= 3) {
            // Web Trap - slow nearby players
            for (Entity nearby : entity.getNearbyEntities(10, 10, 10)) {
                if (nearby instanceof Player player) {
                    player.addPotionEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.SLOWNESS, 100, 2, false, false));
                }
            }
        }
        
        if (tier.getTier() >= 4) {
            // Spider Leap - jump to nearest player
            Player nearestPlayer = getNearestPlayer();
            if (nearestPlayer != null) {
                Location targetLoc = nearestPlayer.getLocation();
                entity.teleport(targetLoc);
            }
        }
    }
    
    /**
     * Wolf slayer special abilities
     */
    private void useWolfAbility() {
        if (tier.getTier() >= 3) {
            // Howl - summon wolf pack
            for (int i = 0; i < 2; i++) {
                Location spawnLoc = entity.getLocation().add(
                    (Math.random() - 0.5) * 8, 0, (Math.random() - 0.5) * 8);
                entity.getWorld().spawnEntity(spawnLoc, org.bukkit.entity.EntityType.WOLF);
            }
        }
        
        if (tier.getTier() >= 4) {
            // Pack Frenzy - increase speed and damage
            entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                org.bukkit.potion.PotionEffectType.SPEED, 200, 2, false, false));
            entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                org.bukkit.potion.PotionEffectType.STRENGTH, 200, 1, false, false));
        }
    }
    
    /**
     * Enderman slayer special abilities
     */
    private void useEndermanAbility() {
        if (tier.getTier() >= 3) {
            // Teleport Strike - teleport to player and attack
            Player nearestPlayer = getNearestPlayer();
            if (nearestPlayer != null) {
                Location teleportLoc = nearestPlayer.getLocation().add(0, 1, 0);
                entity.teleport(teleportLoc);
                attackPlayer(nearestPlayer);
            }
        }
        
        if (tier.getTier() >= 4) {
            // Void Aura - damage nearby players
            for (Entity nearby : entity.getNearbyEntities(5, 5, 5)) {
                if (nearby instanceof Player player) {
                    player.damage(damage * 0.5, entity);
                }
            }
        }
    }
    
    /**
     * Blaze slayer special abilities
     */
    private void useBlazeAbility() {
        if (tier.getTier() >= 3) {
            // Fire Storm - create fire around boss
            for (int x = -3; x <= 3; x++) {
                for (int z = -3; z <= 3; z++) {
                    Location fireLoc = entity.getLocation().add(x, 0, z);
                    if (fireLoc.getBlock().getType().isAir()) {
                        fireLoc.getBlock().setType(org.bukkit.Material.FIRE);
                    }
                }
            }
        }
        
        if (tier.getTier() >= 4) {
            // Explosion - explode after delay
            new org.bukkit.scheduler.BukkitRunnable() {
                @Override
                public void run() {
                    if (entity.isValid()) {
                        entity.getWorld().createExplosion(entity.getLocation(), 3.0f, false, false);
                    }
                }
            }.runTaskLater(org.bukkit.Bukkit.getPluginManager().getPlugin("Plugin"), 60L); // 3 seconds delay
        }
    }
    
    /**
     * Vampire slayer special abilities
     */
    private void useVampireAbility() {
        if (tier.getTier() >= 3) {
            // Blood Drain - heal from nearby players
            for (Entity nearby : entity.getNearbyEntities(5, 5, 5)) {
                if (nearby instanceof Player player) {
                    player.damage(damage * 0.3, entity);
                    entity.setHealth(Math.min(entity.getMaxHealth(), entity.getHealth() + damage * 0.2));
                }
            }
        }
        
        if (tier.getTier() >= 4) {
            // Vampire Form - become invulnerable temporarily
            entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                org.bukkit.potion.PotionEffectType.DAMAGE_RESISTANCE, 100, 10, false, false));
        }
    }
    
    /**
     * Update boss phase based on health
     */
    private void updatePhase() {
        double healthPercentage = entity.getHealth() / entity.getMaxHealth();
        
        if (healthPercentage <= 0.25 && phase < 4) {
            phase = 4;
            onPhaseChange(4);
        } else if (healthPercentage <= 0.5 && phase < 3) {
            phase = 3;
            onPhaseChange(3);
        } else if (healthPercentage <= 0.75 && phase < 2) {
            phase = 2;
            onPhaseChange(2);
        }
    }
    
    /**
     * Handle phase change
     */
    private void onPhaseChange(int newPhase) {
        // Announce phase change
        if (spawner != null) {
            spawner.sendMessage("§c§l" + type.getBossName() + " entered Phase " + newPhase + "!");
        }
        
        // Phase-specific effects
        switch (newPhase) {
            case 2 -> {
                // Increase speed
                entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
            }
            case 3 -> {
                // Increase damage
                entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1, false, false));
            }
            case 4 -> {
                // Berserk mode
                entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.SPEED, Integer.MAX_VALUE, 2, false, false));
                entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2, false, false));
            }
        }
    }
    
    /**
     * Update visual effects
     */
    private void updateVisualEffects() {
        // Boss glow effect
        entity.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, entity.getLocation(), 5, 1, 1, 1, 0.1);
        
        // Phase-specific effects
        if (phase >= 2) {
            entity.getWorld().spawnParticle(Particle.FLAME, entity.getLocation(), 3, 0.5, 0.5, 0.5, 0.1);
        }
        
        if (phase >= 3) {
            entity.getWorld().spawnParticle(Particle.CRIT, entity.getLocation(), 2, 0.3, 0.3, 0.3, 0.1);
        }
        
        if (phase >= 4) {
            entity.getWorld().spawnParticle(Particle.LAVA, entity.getLocation(), 1, 0.2, 0.2, 0.2, 0.1);
        }
    }
    
    /**
     * Get nearest player
     */
    private Player getNearestPlayer() {
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;
        
        for (Entity nearby : entity.getNearbyEntities(50, 50, 50)) {
            if (nearby instanceof Player player) {
                double distance = player.getLocation().distance(entity.getLocation());
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestPlayer = player;
                }
            }
        }
        
        return nearestPlayer;
    }
    
    /**
     * Get special ability cooldown based on tier
     */
    private long getSpecialAbilityCooldown() {
        return switch (tier.getTier()) {
            case 1, 2 -> 10000L; // 10 seconds
            case 3 -> 8000L;     // 8 seconds
            case 4 -> 6000L;     // 6 seconds
            case 5 -> 4000L;     // 4 seconds
            default -> 10000L;
        };
    }
    
    /**
     * Check if boss is alive
     */
    public boolean isAlive() {
        return entity != null && entity.isValid() && entity.getHealth() > 0;
    }
    
    // Getters
    public LivingEntity getEntity() { return entity; }
    public SlayerSystem.SlayerType getType() { return type; }
    public SlayerSystem.SlayerTier getTier() { return tier; }
    public Player getSpawner() { return spawner; }
    public int getMaxHealth() { return maxHealth; }
    public int getDamage() { return damage; }
    public long getSpawnTime() { return spawnTime; }
    public int getPhase() { return phase; }
}
