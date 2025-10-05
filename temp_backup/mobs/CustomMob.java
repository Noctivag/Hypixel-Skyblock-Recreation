package de.noctivag.skyblock.mobs;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public abstract class CustomMob implements Listener {

    protected final SkyblockPluginRefactored plugin;
    protected final String name;
    protected final EntityType entityType;
    protected final double maxHealth;
    protected final double damage;
    protected final int level;
    protected final int xpReward;
    protected final double coinReward;
    
    protected LivingEntity entity;
    protected Location spawnLocation;
    protected final Random random = new Random();

    public CustomMob(SkyblockPluginRefactored plugin, String name, EntityType entityType, 
                     double maxHealth, double damage, int level, int xpReward, double coinReward) {
        this.plugin = plugin;
        this.name = name;
        this.entityType = entityType;
        this.maxHealth = maxHealth;
        this.damage = damage;
        this.level = level;
        this.xpReward = xpReward;
        this.coinReward = coinReward;
        
        // Register this as an event listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public abstract void spawn(Location location);
    public abstract void onSpawn();
    public abstract void onDeath(Player killer);
    public abstract void onTick();

    protected void setupEntity(LivingEntity entity) {
        this.entity = entity;
        this.spawnLocation = entity.getLocation();
        
        // Set basic attributes
        entity.setMaxHealth(maxHealth);
        entity.setHealth(maxHealth);
        
        // Set custom name
        entity.setCustomName("§c" + name + " §7[Lv" + level + "]");
        entity.setCustomNameVisible(true);
        entity.setRemoveWhenFarAway(false);
        
        // Start AI
        startAI();
        
        onSpawn();
    }

    private void startAI() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || entity.isDead()) {
                    cancel();
                    return;
                }
                
                onTick();
            }
        }.runTaskTimer(plugin, 0L, 20L); // Run every second
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() != entity) return;
        
        Player killer = event.getEntity().getKiller();
        if (killer != null) {
            onDeath(killer);
        }
        
        // Clean up
        entity = null;
    }

    protected void dropLoot(Player killer) {
        if (killer == null || entity == null) return;
        
        Location dropLocation = entity.getLocation();
        
        // Drop coins
        if (coinReward > 0) {
            dropLocation.getWorld().dropItem(dropLocation, 
                new org.bukkit.inventory.ItemStack(org.bukkit.Material.GOLD_INGOT, (int) coinReward));
        }
        
        // Drop experience
        if (xpReward > 0) {
            killer.giveExp(xpReward);
        }
        
        // Custom loot drops (implemented by subclasses)
        dropCustomLoot(killer, dropLocation);
    }

    protected abstract void dropCustomLoot(Player killer, Location dropLocation);

    protected void sendMessageToNearbyPlayers(String message) {
        if (entity == null) return;
        
        List<Player> nearbyPlayers = entity.getWorld().getPlayers();
        for (Player player : nearbyPlayers) {
            if (player.getLocation().distance(entity.getLocation()) <= 50) {
                player.sendMessage(message);
            }
        }
    }

    protected boolean isNearPlayer(Player player, double distance) {
        if (entity == null || player == null) return false;
        return entity.getLocation().distance(player.getLocation()) <= distance;
    }

    protected void teleportTo(Location location) {
        if (entity != null) {
            entity.teleport(location);
        }
    }

    protected void damagePlayer(Player player, double damage) {
        if (player != null && entity != null) {
            player.damage(damage, entity);
        }
    }

    // Getters
    public String getName() {
        return name;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getDamage() {
        return damage;
    }

    public int getLevel() {
        return level;
    }

    public int getXpReward() {
        return xpReward;
    }

    public double getCoinReward() {
        return coinReward;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public boolean isAlive() {
        return entity != null && !entity.isDead();
    }
}
