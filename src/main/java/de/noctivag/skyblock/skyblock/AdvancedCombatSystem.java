package de.noctivag.skyblock.skyblock;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.meta.ItemMeta;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Combat System inspired by Hypixel Skyblock
 * Features:
 * - Complex damage calculation with multiple stats
 * - Critical hit system with chance and multiplier
 * - Strength, Defense, and other combat stats
 * - Weapon abilities and special attacks
 * - Combat-based progression and rewards
 */
public class AdvancedCombatSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final HealthManaSystem healthManaSystem;
    private final AdvancedSkillsSystem skillsSystem;
    private final Map<UUID, PlayerCombatData> playerCombatData = new ConcurrentHashMap<>();
    private final Map<UUID, Long> lastAttackTime = new ConcurrentHashMap<>();
    
    public AdvancedCombatSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager,
                               HealthManaSystem healthManaSystem, AdvancedSkillsSystem skillsSystem) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        this.healthManaSystem = healthManaSystem;
        this.skillsSystem = skillsSystem;
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;
        
        Player attacker = (Player) event.getDamager();
        LivingEntity target = (LivingEntity) event.getEntity();
        
        // Calculate advanced damage
        double baseDamage = event.getDamage();
        double finalDamage = calculateDamage(attacker, target, baseDamage);
        
        // Apply damage
        event.setDamage(finalDamage);
        
        // Handle critical hits
        if (isCriticalHit(attacker)) {
            double critDamage = finalDamage * getCriticalMultiplier(attacker);
            event.setDamage(critDamage);
            
            // Send critical hit message
            attacker.sendMessage("§6§lCRITICAL HIT! §e" + String.format("%.1f", critDamage) + " damage");
            
            // Show critical hit effect
            showCriticalHitEffect(target);
        }
        
        // Update combat data
        updateCombatData(attacker, target, finalDamage);
        
        // Handle weapon abilities
        handleWeaponAbility(attacker, target, event);
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID playerId = player.getUniqueId();
        
        // Reset combat data
        playerCombatData.remove(playerId);
        lastAttackTime.remove(playerId);
        
        // Handle death penalties
        handleDeathPenalties(player);
    }
    
    private double calculateDamage(Player attacker, LivingEntity target, double baseDamage) {
        PlayerCombatData combatData = getPlayerCombatData(attacker.getUniqueId());
        
        // Base damage calculation
        double damage = baseDamage;
        
        // Add strength bonus
        double strength = getStrength(attacker);
        damage += strength * 0.5; // 1 strength = 0.5 damage
        
        // Add weapon damage
        double weaponDamage = getWeaponDamage(attacker);
        damage += weaponDamage;
        
        // Add skill bonuses
        double skillBonus = getSkillDamageBonus(attacker);
        damage += skillBonus;
        
        // Apply damage multipliers
        double damageMultiplier = getDamageMultiplier(attacker);
        damage *= damageMultiplier;
        
        // Apply target defense
        double targetDefense = getTargetDefense(target);
        damage = applyDefense(damage, targetDefense);
        
        // Apply armor reduction
        double armorReduction = getArmorReduction(target);
        damage *= (1.0 - armorReduction);
        
        return Math.max(1.0, damage);
    }
    
    private double getStrength(Player player) {
        double strength = 0.0;
        
        // Base strength
        strength += 5.0; // Default strength
        
        // Add strength from skills
        strength += skillsSystem.getPlayerSkillsData(player.getUniqueId()).getTotalStrengthBonus();
        
        // Add strength from armor
        strength += getStrengthFromArmor(player);
        
        // Add strength from accessories
        strength += getStrengthFromAccessories(player);
        
        // Add strength from potions/effects
        strength += getStrengthFromEffects(player);
        
        return strength;
    }
    
    private double getWeaponDamage(Player player) {
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon == null || weapon.getType() == Material.AIR) return 0.0;
        
        double damage = 0.0;
        
        // Base weapon damage
        damage += getBaseWeaponDamage(weapon.getType());
        
        // Add damage from weapon stats
        if (weapon.hasItemMeta() && weapon.getItemMeta().hasLore()) {
            List<String> lore = weapon.getItemMeta().getLore();
            for (String line : lore) {
                if (line.contains("⚔ Damage")) {
                    try {
                        String[] parts = line.split(" ");
                        damage += Double.parseDouble(parts[0].replace("+", "").replace("⚔", ""));
                    } catch (Exception ignored) {}
                }
            }
        }
        
        return damage;
    }
    
    private double getBaseWeaponDamage(Material material) {
        return switch (material) {
            case WOODEN_SWORD -> 20.0;
            case STONE_SWORD -> 25.0;
            case IRON_SWORD -> 30.0;
            case GOLDEN_SWORD -> 22.0;
            case DIAMOND_SWORD -> 35.0;
            case NETHERITE_SWORD -> 40.0;
            case WOODEN_AXE -> 25.0;
            case STONE_AXE -> 30.0;
            case IRON_AXE -> 35.0;
            case GOLDEN_AXE -> 27.0;
            case DIAMOND_AXE -> 40.0;
            case NETHERITE_AXE -> 45.0;
            default -> 0.0;
        };
    }
    
    private double getSkillDamageBonus(Player player) {
        // Add damage bonuses from combat skill
        int combatLevel = skillsSystem.getSkillLevel(player, AdvancedSkillsSystem.SkillType.COMBAT);
        return combatLevel * 2.0; // +2 damage per combat level
    }
    
    private double getDamageMultiplier(Player player) {
        double multiplier = 1.0;
        
        // Add multipliers from skills
        multiplier += skillsSystem.getPlayerSkillsData(player.getUniqueId()).getTotalStrengthBonus() * 0.01;
        
        // Add multipliers from effects
        multiplier += getDamageMultiplierFromEffects(player);
        
        return Math.max(0.1, multiplier);
    }
    
    private double getTargetDefense(LivingEntity target) {
        double defense = 0.0;
        
        if (target instanceof Player) {
            Player targetPlayer = (Player) target;
            defense += skillsSystem.getPlayerSkillsData(targetPlayer.getUniqueId()).getTotalDefenseBonus();
            defense += getDefenseFromArmor(targetPlayer);
        } else {
            // Mob defense
            defense += getMobDefense(target);
        }
        
        return defense;
    }
    
    private double getMobDefense(LivingEntity mob) {
        // Different mobs have different defense values
        return switch (mob.getType().name()) {
            case "ZOMBIE" -> 5.0;
            case "SKELETON" -> 3.0;
            case "SPIDER" -> 2.0;
            case "CREEPER" -> 0.0;
            case "ENDERMAN" -> 10.0;
            case "BLAZE" -> 8.0;
            case "GHAST" -> 5.0;
            case "WITHER_SKELETON" -> 15.0;
            default -> 1.0;
        };
    }
    
    private double applyDefense(double damage, double defense) {
        // Defense formula: damage * (1 - defense / (defense + 100))
        return damage * (1.0 - (defense / (defense + 100.0)));
    }
    
    private double getArmorReduction(LivingEntity target) {
        double reduction = 0.0;
        
        if (target instanceof Player) {
            Player targetPlayer = (Player) target;
            // Calculate armor reduction based on armor pieces
            for (ItemStack armor : targetPlayer.getInventory().getArmorContents()) {
                if (armor != null && armor.getType() != Material.AIR) {
                    reduction += getArmorReductionValue(armor.getType());
                }
            }
        }
        
        return Math.min(0.8, reduction); // Max 80% reduction
    }
    
    private double getArmorReductionValue(Material material) {
        return switch (material) {
            case LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS -> 0.02;
            case CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS -> 0.03;
            case IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS -> 0.04;
            case GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS -> 0.03;
            case DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS -> 0.05;
            case NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS -> 0.06;
            default -> 0.0;
        };
    }
    
    private boolean isCriticalHit(Player player) {
        double critChance = getCriticalChance(player);
        return Math.random() < critChance;
    }
    
    private double getCriticalChance(Player player) {
        double critChance = 0.0;
        
        // Base crit chance
        critChance += 0.1; // 10% base crit chance
        
        // Add crit chance from skills
        int combatLevel = skillsSystem.getSkillLevel(player, AdvancedSkillsSystem.SkillType.COMBAT);
        critChance += combatLevel * 0.002; // +0.2% per combat level
        
        // Add crit chance from accessories
        critChance += getCriticalChanceFromAccessories(player);
        
        // Add crit chance from effects
        critChance += getCriticalChanceFromEffects(player);
        
        return Math.min(1.0, critChance); // Max 100% crit chance
    }
    
    private double getCriticalMultiplier(Player player) {
        double multiplier = 1.5; // Base 1.5x crit multiplier
        
        // Add crit multiplier from skills
        int combatLevel = skillsSystem.getSkillLevel(player, AdvancedSkillsSystem.SkillType.COMBAT);
        multiplier += combatLevel * 0.01; // +1% per combat level
        
        // Add crit multiplier from accessories
        multiplier += getCriticalMultiplierFromAccessories(player);
        
        return Math.max(1.0, multiplier);
    }
    
    private void showCriticalHitEffect(LivingEntity target) {
        // Show critical hit particles and effects
        target.getWorld().spawnParticle(org.bukkit.Particle.CRIT, target.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
    }
    
    private void updateCombatData(Player attacker, LivingEntity target, double damage) {
        UUID attackerId = attacker.getUniqueId();
        PlayerCombatData data = getPlayerCombatData(attackerId);
        
        // Update combat stats
        data.addDamageDealt(damage);
        data.incrementHits();
        
        // Update last attack time
        lastAttackTime.put(attackerId, java.lang.System.currentTimeMillis());
        
        // Check for combat achievements
        checkCombatAchievements(attacker, target, data);
    }
    
    private void checkCombatAchievements(Player attacker, LivingEntity target, PlayerCombatData data) {
        // Check for various combat achievements
        if (data.getHits() >= 1000) {
            attacker.sendMessage("§6§lACHIEVEMENT UNLOCKED! §eCombat Master");
        }
        
        if (data.getDamageDealt() >= 100000) {
            attacker.sendMessage("§6§lACHIEVEMENT UNLOCKED! §eDamage Dealer");
        }
    }
    
    private void handleWeaponAbility(Player attacker, LivingEntity target, EntityDamageByEntityEvent event) {
        ItemStack weapon = attacker.getInventory().getItemInMainHand();
        if (weapon == null || weapon.getType() == Material.AIR) return;
        
        // Check for weapon abilities
        if (weapon.hasItemMeta() && weapon.getItemMeta().hasLore()) {
            List<String> lore = weapon.getItemMeta().getLore();
            for (String line : lore) {
                if (line.contains("Ability:")) {
                    // Handle weapon ability
                    handleWeaponAbility(attacker, target, line);
                }
            }
        }
    }
    
    private void handleWeaponAbility(Player attacker, LivingEntity target, String abilityLine) {
        // Parse and execute weapon abilities
        if (abilityLine.contains("Lightning Strike")) {
            executeLightningStrike(attacker, target);
        } else if (abilityLine.contains("Fireball")) {
            executeFireball(attacker, target);
        } else if (abilityLine.contains("Heal")) {
            executeHeal(attacker);
        }
    }
    
    private void executeLightningStrike(Player attacker, LivingEntity target) {
        // Check mana cost
        if (!healthManaSystem.consumeMana(attacker, 50)) {
            attacker.sendMessage("§cNot enough mana!");
            return;
        }
        
        // Deal lightning damage
        double lightningDamage = calculateDamage(attacker, target, 0) * 2.0;
        target.damage(lightningDamage, attacker);
        
        // Show lightning effect
        target.getWorld().strikeLightningEffect(target.getLocation());
        
        attacker.sendMessage("§eLightning Strike! §c" + String.format("%.1f", lightningDamage) + " damage");
    }
    
    private void executeFireball(Player attacker, LivingEntity target) {
        // Check mana cost
        if (!healthManaSystem.consumeMana(attacker, 30)) {
            attacker.sendMessage("§cNot enough mana!");
            return;
        }
        
        // Deal fire damage
        double fireDamage = calculateDamage(attacker, target, 0) * 1.5;
        target.damage(fireDamage, attacker);
        
        // Set target on fire
        target.setFireTicks(60);
        
        attacker.sendMessage("§cFireball! §c" + String.format("%.1f", fireDamage) + " damage");
    }
    
    private void executeHeal(Player attacker) {
        // Check mana cost
        if (!healthManaSystem.consumeMana(attacker, 40)) {
            attacker.sendMessage("§cNot enough mana!");
            return;
        }
        
        // Heal player
        double healAmount = 50.0;
        healthManaSystem.addHealth(attacker, healAmount);
        
        attacker.sendMessage("§aHealed for " + String.format("%.1f", healAmount) + " health!");
    }
    
    private void handleDeathPenalties(Player player) {
        // Apply death penalties
        player.sendMessage(Component.text("§cYou died! You lost some coins and experience."));
        
        // Implement death penalties (coin loss, XP loss, etc.)
        if (SkyblockPlugin.getEconomyManager() != null) {
            double coinLoss = Math.min(player.getLevel() * 100, 10000); // Max 10k coin loss
            SkyblockPlugin.getEconomyManager().withdrawMoney(player, coinLoss);
            player.sendMessage("§cDu hast " + coinLoss + " Coins durch den Tod verloren!");
        }
        
        // XP loss
        int xpLoss = Math.min(player.getLevel() * 10, 1000); // Max 1000 XP loss
        player.giveExp(-xpLoss);
        player.sendMessage("§cDu hast " + xpLoss + " XP durch den Tod verloren!");
        
        // Apply death effects
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(
            org.bukkit.potion.PotionEffectType.WEAKNESS, 200, 1));
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(
            org.bukkit.potion.PotionEffectType.SLOWNESS, 200, 1));
    }
    
    // Helper methods for getting stats from various sources
    private double getStrengthFromArmor(Player player) {
        double strength = 0.0;
        
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null || armor.getType() == Material.AIR) continue;
            
            if (armor.hasItemMeta() && armor.getItemMeta().hasLore()) {
                List<String> lore = armor.getItemMeta().getLore();
                for (String line : lore) {
                    if (line.contains("❁ Strength")) {
                        try {
                            String[] parts = line.split(" ");
                            strength += Double.parseDouble(parts[0].replace("+", "").replace("❁", ""));
                        } catch (Exception ignored) {}
                    }
                }
            }
        }
        
        return strength;
    }
    
    private double getDefenseFromArmor(Player player) {
        double defense = 0.0;
        
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null || armor.getType() == Material.AIR) continue;
            
            if (armor.hasItemMeta() && armor.getItemMeta().hasLore()) {
                List<String> lore = armor.getItemMeta().getLore();
                for (String line : lore) {
                    if (line.contains("❈ Defense")) {
                        try {
                            String[] parts = line.split(" ");
                            defense += Double.parseDouble(parts[0].replace("+", "").replace("❈", ""));
                        } catch (Exception ignored) {}
                    }
                }
            }
        }
        
        return defense;
    }
    
    private double getStrengthFromAccessories(Player player) {
        // This would integrate with the accessory system
        return 0.0;
    }
    
    private double getStrengthFromEffects(Player player) {
        // Check for strength effects
        return 0.0;
    }
    
    private double getDamageMultiplierFromEffects(Player player) {
        // Check for damage multiplier effects
        return 0.0;
    }
    
    private double getCriticalChanceFromAccessories(Player player) {
        // This would integrate with the accessory system
        return 0.0;
    }
    
    private double getCriticalChanceFromEffects(Player player) {
        // Check for crit chance effects
        return 0.0;
    }
    
    private double getCriticalMultiplierFromAccessories(Player player) {
        // This would integrate with the accessory system
        return 0.0;
    }
    
    public PlayerCombatData getPlayerCombatData(UUID playerId) {
        return playerCombatData.computeIfAbsent(playerId, k -> new PlayerCombatData(playerId));
    }
    
    public static class PlayerCombatData {
        private final UUID playerId;
        private double damageDealt = 0.0;
        private int hits = 0;
        private int kills = 0;
        private int deaths = 0;
        private double damageTaken = 0.0;
        
        public PlayerCombatData(UUID playerId) {
            this.playerId = playerId;
        }
        
        public void addDamageDealt(double damage) {
            this.damageDealt += damage;
        }
        
        public void incrementHits() {
            this.hits++;
        }
        
        public void incrementKills() {
            this.kills++;
        }
        
        public void incrementDeaths() {
            this.deaths++;
        }
        
        public void addDamageTaken(double damage) {
            this.damageTaken += damage;
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public double getDamageDealt() { return damageDealt; }
        public int getHits() { return hits; }
        public int getKills() { return kills; }
        public int getDeaths() { return deaths; }
        public double getDamageTaken() { return damageTaken; }
    }
}
