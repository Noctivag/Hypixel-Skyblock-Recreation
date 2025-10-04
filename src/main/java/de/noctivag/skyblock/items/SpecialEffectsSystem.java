package de.noctivag.skyblock.items;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Special Effects System - Hypixel Skyblock Style
 * 
 * Features:
 * - Weapon Special Effects (Fire Aspect, Lightning, Ice, Poison, etc.)
 * - Armor Special Effects (Thorns, Absorption, Regeneration, etc.)
 * - Set Bonuses (Full Set Effects)
 * - Ability Cooldowns
 * - Special Attack Effects
 * - Particle Effects
 * - Sound Effects
 * - Visual Effects
 * - Damage Over Time Effects
 * - Buff/Debuff Effects
 */
public class SpecialEffectsSystem implements Listener {
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, Map<SpecialEffect, Long>> playerCooldowns = new ConcurrentHashMap<>();
    private final Map<UUID, List<ActiveEffect>> activeEffects = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> effectTasks = new ConcurrentHashMap<>();
    
    public SpecialEffectsSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        startEffectUpdateTask();
        
        // Register events
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void startEffectUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateAllActiveEffects();
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity target = event.getEntity();
        
        if (damager instanceof Player && target instanceof LivingEntity) {
            Player player = (Player) damager;
            LivingEntity livingTarget = (LivingEntity) target;
            
            // Check for weapon effects
            ItemStack weapon = player.getInventory().getItemInMainHand();
            if (weapon != null && weapon.hasItemMeta()) {
                applyWeaponEffects(player, livingTarget, weapon, event);
            }
            
            // Check for armor effects
            applyArmorEffects(player, livingTarget, event);
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasLore()) {
                List<String> lore = meta.lore() != null ? 
                    meta.lore().stream().map(Component::toString).collect(java.util.stream.Collectors.toList()) : 
                    new java.util.ArrayList<>();
                for (String line : lore) {
                    if (line.contains("Ability:")) {
                        String abilityName = extractAbilityName(line);
                        SpecialEffect effect = getSpecialEffectByName(abilityName);
                        
                        if (effect != null && canUseAbility(player, effect)) {
                            useAbility(player, effect);
                        }
                    }
                }
            }
        }
    }
    
    private void applyWeaponEffects(Player player, LivingEntity target, ItemStack weapon, EntityDamageByEntityEvent event) {
        ItemMeta meta = weapon.getItemMeta();
        if (meta == null || !meta.hasLore()) return;
        
        List<String> lore = meta.lore() != null ? 
            meta.lore().stream().map(Component::toString).collect(java.util.stream.Collectors.toList()) : 
            new java.util.ArrayList<>();
        for (String line : lore) {
            // Fire Aspect Effect
            if (line.contains("Fire Aspect")) {
                applyFireAspect(target, player);
            }
            
            // Lightning Effect
            if (line.contains("Lightning")) {
                applyLightningEffect(target, player);
            }
            
            // Ice Effect
            if (line.contains("Ice")) {
                applyIceEffect(target, player);
            }
            
            // Poison Effect
            if (line.contains("Poison")) {
                applyPoisonEffect(target, player);
            }
            
            // Life Steal Effect
            if (line.contains("Life Steal")) {
                applyLifeSteal(player, event.getFinalDamage());
            }
            
            // Critical Hit Effect
            if (line.contains("Critical Hit")) {
                if (isCriticalHit(player)) {
                    event.setDamage(event.getDamage() * 2.0); // Double damage
                    showCriticalHitEffect(target);
                }
            }
        }
    }
    
    private void applyArmorEffects(Player player, LivingEntity target, EntityDamageByEntityEvent event) {
        // Check helmet
        ItemStack helmet = player.getInventory().getHelmet();
        if (helmet != null && helmet.hasItemMeta()) {
            applyArmorPieceEffects(player, target, helmet, event);
        }
        
        // Check chestplate
        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate != null && chestplate.hasItemMeta()) {
            applyArmorPieceEffects(player, target, chestplate, event);
        }
        
        // Check leggings
        ItemStack leggings = player.getInventory().getLeggings();
        if (leggings != null && leggings.hasItemMeta()) {
            applyArmorPieceEffects(player, target, leggings, event);
        }
        
        // Check boots
        ItemStack boots = player.getInventory().getBoots();
        if (boots != null && boots.hasItemMeta()) {
            applyArmorPieceEffects(player, target, boots, event);
        }
        
        // Check for set bonuses
        checkSetBonuses(player, target, event);
    }
    
    private void applyArmorPieceEffects(Player player, LivingEntity target, ItemStack armor, EntityDamageByEntityEvent event) {
        ItemMeta meta = armor.getItemMeta();
        if (meta == null || !meta.hasLore()) return;
        
        List<String> lore = meta.lore() != null ? 
            meta.lore().stream().map(Component::toString).collect(java.util.stream.Collectors.toList()) : 
            new java.util.ArrayList<>();
        for (String line : lore) {
            // Thorns Effect
            if (line.contains("Thorns")) {
                applyThornsEffect(player, target, event);
            }
            
            // Absorption Effect
            if (line.contains("Absorption")) {
                applyAbsorptionEffect(player);
            }
            
            // Regeneration Effect
            if (line.contains("Regeneration")) {
                applyRegenerationEffect(player);
            }
            
            // Resistance Effect
            if (line.contains("Resistance")) {
                applyResistanceEffect(player);
            }
        }
    }
    
    private void checkSetBonuses(Player player, LivingEntity target, EntityDamageByEntityEvent event) {
        // Check for full set bonuses
        if (hasFullSet(player, "Dragon Armor")) {
            applyDragonSetBonus(player, target, event);
        }
        
        if (hasFullSet(player, "Necron Armor")) {
            applyNecronSetBonus(player, target, event);
        }
        
        if (hasFullSet(player, "Storm Armor")) {
            applyStormSetBonus(player, target, event);
        }
        
        if (hasFullSet(player, "Goldor Armor")) {
            applyGoldorSetBonus(player, target, event);
        }
    }
    
    // Weapon Effects
    private void applyFireAspect(LivingEntity target, Player player) {
        target.setFireTicks(100); // 5 seconds of fire
        
        // Particle effect
        target.getWorld().spawnParticle(Particle.FLAME, target.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
        
        // Sound effect
        target.getWorld().playSound(target.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1.0f, 1.0f);
    }
    
    private void applyLightningEffect(LivingEntity target, Player player) {
        // Strike lightning
        target.getWorld().strikeLightningEffect(target.getLocation());
        
        // Damage
        target.damage(5.0, player);
        
        // Particle effect
        target.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, target.getLocation(), 30, 1.0, 1.0, 1.0, 0.2);
        
        // Sound effect
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
    }
    
    private void applyIceEffect(LivingEntity target, Player player) {
        // Apply slowness
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 2)); // 5 seconds, level 3
        
        // Particle effect
        target.getWorld().spawnParticle(Particle.SNOWFLAKE, target.getLocation(), 25, 0.5, 0.5, 0.5, 0.1);
        
        // Sound effect
        target.getWorld().playSound(target.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 0.5f);
    }
    
    private void applyPoisonEffect(LivingEntity target, Player player) {
        // Apply poison
        target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1)); // 5 seconds, level 2
        
        // Particle effect
        target.getWorld().spawnParticle(Particle.ENCHANT, target.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
        
        // Sound effect
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_SPIDER_AMBIENT, 1.0f, 1.0f);
    }
    
    private void applyLifeSteal(Player player, double damage) {
        double healAmount = damage * 0.1; // 10% of damage as healing
        double newHealth = Math.min(player.getHealth() + healAmount, 20.0);
        player.setHealth(newHealth);
        
        // Particle effect
        player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 5, 0.3, 0.3, 0.3, 0.1);
        
        // Sound effect
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1.5f);
    }
    
    private void showCriticalHitEffect(LivingEntity target) {
        // Particle effect
        target.getWorld().spawnParticle(Particle.CRIT, target.getLocation().add(0, 1, 0), 50, 0.5, 0.5, 0.5, 0.3);
        
        // Sound effect
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f);
    }
    
    // Armor Effects
    private void applyThornsEffect(Player player, LivingEntity target, EntityDamageByEntityEvent event) {
        // Reflect damage back to attacker
        double reflectDamage = event.getFinalDamage() * 0.3; // 30% reflection
        target.damage(reflectDamage, player);
        
        // Particle effect
        target.getWorld().spawnParticle(Particle.CRIT, target.getLocation(), 20, 0.5, 0.5, 0.5, 0.2);
        
        // Sound effect
        target.getWorld().playSound(target.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.0f, 1.0f);
    }
    
    private void applyAbsorptionEffect(Player player) {
        // Add absorption hearts
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 1)); // 10 seconds, 2 hearts
        
        // Particle effect
        player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 10, 0.3, 0.3, 0.3, 0.1);
        
        // Sound effect
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1.0f);
    }
    
    private void applyRegenerationEffect(Player player) {
        // Add regeneration
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1)); // 5 seconds, level 2
        
        // Particle effect
        player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 5, 0.3, 0.3, 0.3, 0.1);
        
        // Sound effect
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.3f, 1.2f);
    }
    
    private void applyResistanceEffect(Player player) {
        // Add resistance
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 200, 1)); // 10 seconds, level 2
        
        // Particle effect
        player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, player.getLocation().add(0, 1, 0), 15, 0.5, 0.5, 0.5, 0.1);
        
        // Sound effect
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, 0.5f, 1.0f);
    }
    
    // Set Bonuses
    private void applyDragonSetBonus(Player player, LivingEntity target, EntityDamageByEntityEvent event) {
        // Dragon's Breath - Fire damage to nearby enemies
        for (Entity nearby : target.getNearbyEntities(3, 3, 3)) {
            if (nearby instanceof LivingEntity && nearby != player) {
                LivingEntity nearbyEntity = (LivingEntity) nearby;
                nearbyEntity.setFireTicks(60); // 3 seconds of fire
                nearbyEntity.damage(3.0, player);
            }
        }
        
        // Particle effect
        target.getWorld().spawnParticle(Particle.DRAGON_BREATH, target.getLocation(), 30, 2.0, 2.0, 2.0, 0.1);
        
        // Sound effect
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 1.0f);
    }
    
    private void applyNecronSetBonus(Player player, LivingEntity target, EntityDamageByEntityEvent event) {
        // Necron's Wrath - Increased damage and lifesteal
        event.setDamage(event.getDamage() * 1.5); // 50% more damage
        applyLifeSteal(player, event.getFinalDamage() * 0.2); // 20% lifesteal
        
        // Particle effect
        target.getWorld().spawnParticle(Particle.SOUL, target.getLocation(), 25, 0.5, 0.5, 0.5, 0.1);
        
        // Sound effect
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_WITHER_SKELETON_AMBIENT, 1.0f, 0.8f);
    }
    
    private void applyStormSetBonus(Player player, LivingEntity target, EntityDamageByEntityEvent event) {
        // Storm's Fury - Lightning strikes
        applyLightningEffect(target, player);
        
        // Particle effect
        target.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, target.getLocation(), 40, 1.0, 1.0, 1.0, 0.2);
        
        // Sound effect
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 0.8f);
    }
    
    private void applyGoldorSetBonus(Player player, LivingEntity target, EntityDamageByEntityEvent event) {
        // Goldor's Protection - Damage reduction and thorns
        event.setDamage(event.getDamage() * 0.7); // 30% damage reduction
        applyThornsEffect(player, target, event);
        
        // Particle effect
        target.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, target.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
        
        // Sound effect
        target.getWorld().playSound(target.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1.0f, 0.8f);
    }
    
    // Utility Methods
    private boolean hasFullSet(Player player, String setName) {
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();
        
        return hasSetPiece(helmet, setName) && hasSetPiece(chestplate, setName) && 
               hasSetPiece(leggings, setName) && hasSetPiece(boots, setName);
    }
    
    private boolean hasSetPiece(ItemStack item, String setName) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return false;
        
        List<String> lore = meta.lore() != null ? 
            meta.lore().stream().map(Component::toString).collect(java.util.stream.Collectors.toList()) : 
            new java.util.ArrayList<>();
        for (String line : lore) {
            if (line.contains(setName)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isCriticalHit(Player player) {
        // Calculate critical hit chance based on player stats
        double criticalChance = 0.1; // Base 10% chance
        
        // Add weapon critical chance
        // Add armor critical chance
        // Add skill bonuses
        // Add pet bonuses
        
        return Math.random() < criticalChance;
    }
    
    private String extractAbilityName(String line) {
        // Extract ability name from lore line
        if (line.contains("Ability:")) {
            return line.substring(line.indexOf("Ability:") + 9).trim();
        }
        return "";
    }
    
    private SpecialEffect getSpecialEffectByName(String name) {
        // Map ability names to special effects
        return SpecialEffect.valueOf(name.toUpperCase().replace(" ", "_"));
    }
    
    private boolean canUseAbility(Player player, SpecialEffect effect) {
        UUID playerId = player.getUniqueId();
        Map<SpecialEffect, Long> cooldowns = playerCooldowns.get(playerId);
        
        if (cooldowns == null) return true;
        
        Long lastUsed = cooldowns.get(effect);
        if (lastUsed == null) return true;
        
        long cooldownTime = effect.getCooldown() * 1000; // Convert to milliseconds
        return System.currentTimeMillis() - lastUsed >= cooldownTime;
    }
    
    private void useAbility(Player player, SpecialEffect effect) {
        UUID playerId = player.getUniqueId();
        
        // Set cooldown
        playerCooldowns.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>()).put(effect, System.currentTimeMillis());
        
        // Apply effect
        effect.applyEffect(player);
        
        // Add to active effects
        activeEffects.computeIfAbsent(playerId, k -> new ArrayList<>()).add(new ActiveEffect(effect, System.currentTimeMillis()));
    }
    
    private void updateAllActiveEffects() {
        long currentTime = System.currentTimeMillis();
        
        for (Map.Entry<UUID, List<ActiveEffect>> entry : activeEffects.entrySet()) {
            List<ActiveEffect> effects = entry.getValue();
            
            // Remove expired effects
            effects.removeIf(effect -> currentTime - effect.getStartTime() > effect.getEffect().getDuration() * 1000);
        }
    }
    
    // Special Effect Enum
    public enum SpecialEffect {
        FIRE_ASPECT(5000, 10000), // 5 second cooldown, 10 second duration
        LIGHTNING(10000, 5000),
        ICE(8000, 8000),
        POISON(6000, 10000),
        LIFE_STEAL(0, 0), // Passive
        CRITICAL_HIT(0, 0), // Passive
        THORNS(0, 0), // Passive
        ABSORPTION(30000, 10000),
        REGENERATION(20000, 5000),
        RESISTANCE(25000, 10000);
        
        private final long cooldown;
        private final long duration;
        
        SpecialEffect(long cooldown, long duration) {
            this.cooldown = cooldown;
            this.duration = duration;
        }
        
        public long getCooldown() { return cooldown; }
        public long getDuration() { return duration; }
        
        public void applyEffect(Player player) {
            // Apply the specific effect
            switch (this) {
                case FIRE_ASPECT:
                    // Apply fire aspect effect
                    break;
                case LIGHTNING:
                    // Apply lightning effect
                    break;
                case ICE:
                    // Apply ice effect
                    break;
                case POISON:
                    // Apply poison effect
                    break;
                case ABSORPTION:
                    // Apply absorption effect
                    break;
                case REGENERATION:
                    // Apply regeneration effect
                    break;
                case RESISTANCE:
                    // Apply resistance effect
                    break;
                case LIFE_STEAL:
                    // Apply life steal effect (passive)
                    break;
                case CRITICAL_HIT:
                    // Apply critical hit effect (passive)
                    break;
                case THORNS:
                    // Apply thorns effect (passive)
                    break;
            }
        }
    }
    
    // Active Effect Class
    public static class ActiveEffect {
        private final SpecialEffect effect;
        private final long startTime;
        
        public ActiveEffect(SpecialEffect effect, long startTime) {
            this.effect = effect;
            this.startTime = startTime;
        }
        
        public SpecialEffect getEffect() { return effect; }
        public long getStartTime() { return startTime; }
    }
}
