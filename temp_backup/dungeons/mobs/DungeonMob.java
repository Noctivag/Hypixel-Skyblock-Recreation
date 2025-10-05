package de.noctivag.skyblock.dungeons.mobs;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public abstract class DungeonMob {

    protected final SkyblockPluginRefactored plugin;
    protected final String name;
    protected final Location spawnLocation;
    protected final int floor;
    protected final double maxHealth;
    protected final double damage;
    protected LivingEntity entity;
    protected final Random random = new Random();

    public DungeonMob(SkyblockPluginRefactored plugin, String name, Location spawnLocation, int floor, double maxHealth, double damage) {
        this.plugin = plugin;
        this.name = name;
        this.spawnLocation = spawnLocation;
        this.floor = floor;
        this.maxHealth = maxHealth;
        this.damage = damage;
    }

    protected abstract void spawnBoss();
    public abstract void onSpawn();
    public abstract void onDeath(Player killer);
    public abstract void onTick();

    protected void setupEntity(LivingEntity entity) {
        this.entity = entity;
        
        // Set basic attributes
        entity.setMaxHealth(maxHealth);
        entity.setHealth(maxHealth);
        // Attribute werden über setMaxHealth gesetzt
        
        // Set custom name
        entity.setCustomName("§7" + name + " §7[Floor " + floor + "]");
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

    protected void dropLoot(Player killer) {
        if (killer == null || entity == null) return;
        
        Location dropLocation = entity.getLocation();
        
        // Drop coins
        double coinReward = floor * 100;
        dropLocation.getWorld().dropItem(dropLocation, 
            new org.bukkit.inventory.ItemStack(org.bukkit.Material.GOLD_INGOT, (int) coinReward));
        
        // Drop experience
        int xpReward = floor * 25;
        killer.giveExp(xpReward);
        
        // Custom loot drops (implemented by subclasses)
        dropCustomLoot(killer, dropLocation);
    }

    protected abstract void dropCustomLoot(Player killer, Location dropLocation);

    protected void sendMessageToNearbyPlayers(String message) {
        if (entity == null) return;
        
        entity.getWorld().getPlayers().forEach(player -> {
            if (player.getLocation().distance(entity.getLocation()) <= 30) {
                player.sendMessage(message);
            }
        });
    }

    protected boolean isNearPlayer(Player player, double distance) {
        if (entity == null || player == null) return false;
        return entity.getLocation().distance(player.getLocation()) <= distance;
    }

    protected Player getNearestPlayer(double maxDistance) {
        if (entity == null) return null;
        
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;
        
        for (Player player : entity.getWorld().getPlayers()) {
            double distance = entity.getLocation().distance(player.getLocation());
            if (distance < nearestDistance && distance <= maxDistance) {
                nearestDistance = distance;
                nearestPlayer = player;
            }
        }
        
        return nearestPlayer;
    }

    protected void damagePlayer(Player player, double damage) {
        if (player != null && entity != null) {
            player.damage(damage, entity);
        }
    }

    public void startMob() {
        spawnBoss();
    }

    public void stopMob() {
        if (entity != null && !entity.isDead()) {
            entity.remove();
        }
    }

    // Getters
    public String getName() {
        return name;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public int getFloor() {
        return floor;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getDamage() {
        return damage;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public boolean isAlive() {
        return entity != null && !entity.isDead();
    }
}
