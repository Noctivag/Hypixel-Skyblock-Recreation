package de.noctivag.plugin.combat;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Critical Hit System - Hypixel Skyblock Style
 *
 * Features:
 * - Critical Hit Chance Calculation
 * - Critical Hit Damage Calculation
 * - Critical Hit Effects (Particles, Sounds)
 * - Critical Hit Bonuses from Items
 * - Critical Hit Bonuses from Skills
 * - Critical Hit Bonuses from Pets
 * - Critical Hit Bonuses from Armor
 * - Critical Hit Bonuses from Weapons
 * - Critical Hit Bonuses from Accessories
 * - Critical Hit Bonuses from Potions
 * - Critical Hit Bonuses from Enchantments
 * - Critical Hit Bonuses from Reforges
 * - Critical Hit Bonuses from Gemstones
 * - Critical Hit Bonuses from HotM
 * - Critical Hit Bonuses from Fairy Souls
 * - Critical Hit Statistics
 * - Critical Hit Achievements
 */
public class CriticalHitSystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerCriticalStats> playerCriticalStats = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> criticalTasks = new ConcurrentHashMap<>();

    public CriticalHitSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        startCriticalUpdateTask();

        // Register events
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void startCriticalUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Update critical hit statistics
                for (Map.Entry<UUID, PlayerCriticalStats> entry : playerCriticalStats.entrySet()) {
                    PlayerCriticalStats stats = entry.getValue();
                    stats.update();
                }
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

            // Calculate critical hit
            CriticalHitCalculation criticalCalc = calculateCriticalHit(player, livingTarget);

            if (criticalCalc.isCritical()) {
                // Apply critical hit damage
                event.setDamage(event.getDamage() * criticalCalc.getCriticalMultiplier());

                // Apply critical hit effects
                applyCriticalHitEffects(player, livingTarget, criticalCalc);

                // Update critical hit statistics
                updateCriticalStats(player, criticalCalc);

                // Show critical hit message
                showCriticalHitMessage(player, criticalCalc);
            }
        }
    }

    private CriticalHitCalculation calculateCriticalHit(Player player, LivingEntity target) {
        CriticalHitCalculation calc = new CriticalHitCalculation();

        // Get player critical stats
        PlayerCriticalStats stats = getPlayerCriticalStats(player.getUniqueId());

        // Calculate critical hit chance
        double criticalChance = calculateCriticalChance(stats);
        calc.setCriticalChance(criticalChance);

        // Check if critical hit occurs
        boolean isCritical = Math.random() < (criticalChance / 100.0);
        calc.setCritical(isCritical);

        if (isCritical) {
            // Calculate critical hit damage
            double criticalDamage = calculateCriticalDamage(stats);
            calc.setCriticalDamage(criticalDamage);

            // Calculate critical hit multiplier
            double criticalMultiplier = 1.0 + (criticalDamage / 100.0);
            calc.setCriticalMultiplier(criticalMultiplier);

            // Determine critical hit type
            CriticalHitType criticalType = determineCriticalHitType(stats);
            calc.setCriticalType(criticalType);
        }

        return calc;
    }

    private double calculateCriticalChance(PlayerCriticalStats stats) {
        double baseChance = 0.0;

        // Base critical chance
        baseChance += stats.getBaseCriticalChance();

        // Weapon critical chance
        baseChance += stats.getWeaponCriticalChance();

        // Armor critical chance
        baseChance += stats.getArmorCriticalChance();

        // Accessory critical chance
        baseChance += stats.getAccessoryCriticalChance();

        // Skill critical chance
        baseChance += stats.getSkillCriticalChance();

        // Pet critical chance
        baseChance += stats.getPetCriticalChance();

        // Potion critical chance
        baseChance += stats.getPotionCriticalChance();

        // Enchantment critical chance
        baseChance += stats.getEnchantmentCriticalChance();

        // Reforge critical chance
        baseChance += stats.getReforgeCriticalChance();

        // Gemstone critical chance
        baseChance += stats.getGemstoneCriticalChance();

        // HotM critical chance
        baseChance += stats.getHotmCriticalChance();

        // Fairy soul critical chance
        baseChance += stats.getFairySoulCriticalChance();

        // Cap at 100%
        return Math.min(baseChance, 100.0);
    }

    private double calculateCriticalDamage(PlayerCriticalStats stats) {
        double baseDamage = 0.0;

        // Base critical damage
        baseDamage += stats.getBaseCriticalDamage();

        // Weapon critical damage
        baseDamage += stats.getWeaponCriticalDamage();

        // Armor critical damage
        baseDamage += stats.getArmorCriticalDamage();

        // Accessory critical damage
        baseDamage += stats.getAccessoryCriticalDamage();

        // Skill critical damage
        baseDamage += stats.getSkillCriticalDamage();

        // Pet critical damage
        baseDamage += stats.getPetCriticalDamage();

        // Potion critical damage
        baseDamage += stats.getPotionCriticalDamage();

        // Enchantment critical damage
        baseDamage += stats.getEnchantmentCriticalDamage();

        // Reforge critical damage
        baseDamage += stats.getReforgeCriticalDamage();

        // Gemstone critical damage
        baseDamage += stats.getGemstoneCriticalDamage();

        // HotM critical damage
        baseDamage += stats.getHotmCriticalDamage();

        // Fairy soul critical damage
        baseDamage += stats.getFairySoulCriticalDamage();

        return baseDamage;
    }

    private CriticalHitType determineCriticalHitType(PlayerCriticalStats stats) {
        // Determine critical hit type based on stats
        if (stats.getWeaponCriticalChance() > 50) {
            return CriticalHitType.WEAPON_CRITICAL;
        } else if (stats.getSkillCriticalChance() > 30) {
            return CriticalHitType.SKILL_CRITICAL;
        } else if (stats.getPetCriticalChance() > 20) {
            return CriticalHitType.PET_CRITICAL;
        } else {
            return CriticalHitType.BASE_CRITICAL;
        }
    }

    private void applyCriticalHitEffects(Player player, LivingEntity target, CriticalHitCalculation criticalCalc) {
        // Particle effects
        switch (criticalCalc.getCriticalType()) {
            case WEAPON_CRITICAL:
                target.getWorld().spawnParticle(Particle.CRIT,
                    target.getLocation().add(0, 1, 0), 100, 0.5, 0.5, 0.5, 0.5);
                break;
            case SKILL_CRITICAL:
                target.getWorld().spawnParticle(Particle.ENCHANT,
                    target.getLocation().add(0, 1, 0), 50, 0.5, 0.5, 0.5, 0.3);
                break;
            case PET_CRITICAL:
                target.getWorld().spawnParticle(Particle.HEART,
                    target.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0.2);
                break;
            case BASE_CRITICAL:
                target.getWorld().spawnParticle(Particle.CRIT,
                    target.getLocation().add(0, 1, 0), 50, 0.5, 0.5, 0.5, 0.3);
                break;
        }

        // Sound effects
        switch (criticalCalc.getCriticalType()) {
            case WEAPON_CRITICAL:
                target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f);
                break;
            case SKILL_CRITICAL:
                target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
                break;
            case PET_CRITICAL:
                target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.2f);
                break;
            case BASE_CRITICAL:
                target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 0.8f, 1.0f);
                break;
        }

        // Screen effects for player
        if (player.getLocation().distance(target.getLocation()) <= 10) {
            // Screen flash effect
            player.sendTitle("", ChatColor.RED + "CRITICAL HIT!", 5, 10, 5);
        }
    }

    private void updateCriticalStats(Player player, CriticalHitCalculation criticalCalc) {
        PlayerCriticalStats stats = playerCriticalStats.computeIfAbsent(
            player.getUniqueId(), k -> new PlayerCriticalStats(player.getUniqueId()));

        stats.addCriticalHit();
        stats.addCriticalDamage(criticalCalc.getCriticalDamage());
        stats.addCriticalType(criticalCalc.getCriticalType());
    }

    private void showCriticalHitMessage(Player player, CriticalHitCalculation criticalCalc) {
        String message = createCriticalHitMessage(criticalCalc);
        player.sendMessage(message);
    }

    private String createCriticalHitMessage(CriticalHitCalculation criticalCalc) {
        StringBuilder message = new StringBuilder();

        // Critical hit type color
        ChatColor typeColor = getCriticalHitTypeColor(criticalCalc.getCriticalType());
        message.append(typeColor);

        // Critical hit indicator
        message.append("✧ CRITICAL HIT! ✧ ");

        // Critical hit damage
        message.append(ChatColor.RED).append("+").append(String.format("%.0f", criticalCalc.getCriticalDamage())).append("% Damage");

        return message.toString();
    }

    private ChatColor getCriticalHitTypeColor(CriticalHitType type) {
        return switch (type) {
            case WEAPON_CRITICAL -> ChatColor.RED;
            case SKILL_CRITICAL -> ChatColor.LIGHT_PURPLE;
            case PET_CRITICAL -> ChatColor.GREEN;
            case BASE_CRITICAL -> ChatColor.YELLOW;
        };
    }

    public void addCriticalChanceBonus(Player player, CriticalBonusType type, double bonus) {
        PlayerCriticalStats stats = getPlayerCriticalStats(player.getUniqueId());
        stats.addCriticalChanceBonus(type, bonus);
    }

    public void addCriticalDamageBonus(Player player, CriticalBonusType type, double bonus) {
        PlayerCriticalStats stats = getPlayerCriticalStats(player.getUniqueId());
        stats.addCriticalDamageBonus(type, bonus);
    }

    public PlayerCriticalStats getPlayerCriticalStats(UUID playerId) {
        return playerCriticalStats.computeIfAbsent(playerId, k -> new PlayerCriticalStats(playerId));
    }

    public double getCriticalChance(Player player) {
        PlayerCriticalStats stats = getPlayerCriticalStats(player.getUniqueId());
        return calculateCriticalChance(stats);
    }

    public double getCriticalDamage(Player player) {
        PlayerCriticalStats stats = getPlayerCriticalStats(player.getUniqueId());
        return calculateCriticalDamage(stats);
    }

    // Critical Hit Type Enum
    public enum CriticalHitType {
        WEAPON_CRITICAL("Weapon Critical", ChatColor.RED),
        SKILL_CRITICAL("Skill Critical", ChatColor.LIGHT_PURPLE),
        PET_CRITICAL("Pet Critical", ChatColor.GREEN),
        BASE_CRITICAL("Base Critical", ChatColor.YELLOW);

        private final String displayName;
        private final ChatColor color;

        CriticalHitType(String displayName, ChatColor color) {
            this.displayName = displayName;
            this.color = color;
        }

        public String getDisplayName() { return displayName; }
        public ChatColor getColor() { return color; }
    }

    // Critical Bonus Type Enum
    public enum CriticalBonusType {
        BASE("Base", 0.0, 0.0),
        WEAPON("Weapon", 0.0, 0.0),
        ARMOR("Armor", 0.0, 0.0),
        ACCESSORY("Accessory", 0.0, 0.0),
        SKILL("Skill", 0.0, 0.0),
        PET("Pet", 0.0, 0.0),
        POTION("Potion", 0.0, 0.0),
        ENCHANTMENT("Enchantment", 0.0, 0.0),
        REFORGE("Reforge", 0.0, 0.0),
        GEMSTONE("Gemstone", 0.0, 0.0),
        HOTM("HotM", 0.0, 0.0),
        FAIRY_SOUL("Fairy Soul", 0.0, 0.0);

        private final String displayName;
        private final double baseChance;
        private final double baseDamage;

        CriticalBonusType(String displayName, double baseChance, double baseDamage) {
            this.displayName = displayName;
            this.baseChance = baseChance;
            this.baseDamage = baseDamage;
        }

        public String getDisplayName() { return displayName; }
        public double getBaseChance() { return baseChance; }
        public double getBaseDamage() { return baseDamage; }
    }

    // Critical Hit Calculation Class
    public static class CriticalHitCalculation {
        private double criticalChance;
        private boolean isCritical;
        private double criticalDamage;
        private double criticalMultiplier;
        private CriticalHitType criticalType;

        // Getters and Setters
        public double getCriticalChance() { return criticalChance; }
        public void setCriticalChance(double criticalChance) { this.criticalChance = criticalChance; }

        public boolean isCritical() { return isCritical; }
        public void setCritical(boolean isCritical) { this.isCritical = isCritical; }

        public double getCriticalDamage() { return criticalDamage; }
        public void setCriticalDamage(double criticalDamage) { this.criticalDamage = criticalDamage; }

        public double getCriticalMultiplier() { return criticalMultiplier; }
        public void setCriticalMultiplier(double criticalMultiplier) { this.criticalMultiplier = criticalMultiplier; }

        public CriticalHitType getCriticalType() { return criticalType; }
        public void setCriticalType(CriticalHitType criticalType) { this.criticalType = criticalType; }
    }

    // Player Critical Stats Class
    public static class PlayerCriticalStats {
        private final UUID playerId;
        private final Map<CriticalBonusType, Double> criticalChanceBonuses = new ConcurrentHashMap<>();
        private final Map<CriticalBonusType, Double> criticalDamageBonuses = new ConcurrentHashMap<>();
        private int totalCriticalHits = 0;
        private double totalCriticalDamage = 0.0;
        private final Map<CriticalHitType, Integer> criticalHitsByType = new ConcurrentHashMap<>();
        private long lastUpdate;

        public PlayerCriticalStats(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();

            // Initialize base bonuses
            criticalChanceBonuses.put(CriticalBonusType.BASE, 0.0);
            criticalDamageBonuses.put(CriticalBonusType.BASE, 0.0);
        }

        public void update() {
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;

            // Update statistics every minute
            if (timeDiff >= 60000) {
                // Save to database
                saveToDatabase();
                lastUpdate = currentTime;
            }
        }

        private void saveToDatabase() {
            // Save critical hit statistics to database
            // This would integrate with the database system
        }

        public void addCriticalHit() {
            totalCriticalHits++;
        }

        public void addCriticalDamage(double damage) {
            totalCriticalDamage += damage;
        }

        public void addCriticalType(CriticalHitType type) {
            criticalHitsByType.put(type, criticalHitsByType.getOrDefault(type, 0) + 1);
        }

        public void addCriticalChanceBonus(CriticalBonusType type, double bonus) {
            criticalChanceBonuses.put(type, criticalChanceBonuses.getOrDefault(type, 0.0) + bonus);
        }

        public void addCriticalDamageBonus(CriticalBonusType type, double bonus) {
            criticalDamageBonuses.put(type, criticalDamageBonuses.getOrDefault(type, 0.0) + bonus);
        }

        public double getTotalCriticalChance() {
            return criticalChanceBonuses.values().stream().mapToDouble(Double::doubleValue).sum();
        }

        // Getters for individual bonus types
        public double getBaseCriticalChance() { return criticalChanceBonuses.getOrDefault(CriticalBonusType.BASE, 0.0); }
        public double getWeaponCriticalChance() { return criticalChanceBonuses.getOrDefault(CriticalBonusType.WEAPON, 0.0); }
        public double getArmorCriticalChance() { return criticalChanceBonuses.getOrDefault(CriticalBonusType.ARMOR, 0.0); }
        public double getAccessoryCriticalChance() { return criticalChanceBonuses.getOrDefault(CriticalBonusType.ACCESSORY, 0.0); }
        public double getSkillCriticalChance() { return criticalChanceBonuses.getOrDefault(CriticalBonusType.SKILL, 0.0); }
        public double getPetCriticalChance() { return criticalChanceBonuses.getOrDefault(CriticalBonusType.PET, 0.0); }
        public double getPotionCriticalChance() { return criticalChanceBonuses.getOrDefault(CriticalBonusType.POTION, 0.0); }
        public double getEnchantmentCriticalChance() { return criticalChanceBonuses.getOrDefault(CriticalBonusType.ENCHANTMENT, 0.0); }
        public double getReforgeCriticalChance() { return criticalChanceBonuses.getOrDefault(CriticalBonusType.REFORGE, 0.0); }
        public double getGemstoneCriticalChance() { return criticalChanceBonuses.getOrDefault(CriticalBonusType.GEMSTONE, 0.0); }
        public double getHotmCriticalChance() { return criticalChanceBonuses.getOrDefault(CriticalBonusType.HOTM, 0.0); }
        public double getFairySoulCriticalChance() { return criticalChanceBonuses.getOrDefault(CriticalBonusType.FAIRY_SOUL, 0.0); }

        public double getBaseCriticalDamage() { return criticalDamageBonuses.getOrDefault(CriticalBonusType.BASE, 0.0); }
        public double getWeaponCriticalDamage() { return criticalDamageBonuses.getOrDefault(CriticalBonusType.WEAPON, 0.0); }
        public double getArmorCriticalDamage() { return criticalDamageBonuses.getOrDefault(CriticalBonusType.ARMOR, 0.0); }
        public double getAccessoryCriticalDamage() { return criticalDamageBonuses.getOrDefault(CriticalBonusType.ACCESSORY, 0.0); }
        public double getSkillCriticalDamage() { return criticalDamageBonuses.getOrDefault(CriticalBonusType.SKILL, 0.0); }
        public double getPetCriticalDamage() { return criticalDamageBonuses.getOrDefault(CriticalBonusType.PET, 0.0); }
        public double getPotionCriticalDamage() { return criticalDamageBonuses.getOrDefault(CriticalBonusType.POTION, 0.0); }
        public double getEnchantmentCriticalDamage() { return criticalDamageBonuses.getOrDefault(CriticalBonusType.ENCHANTMENT, 0.0); }
        public double getReforgeCriticalDamage() { return criticalDamageBonuses.getOrDefault(CriticalBonusType.REFORGE, 0.0); }
        public double getGemstoneCriticalDamage() { return criticalDamageBonuses.getOrDefault(CriticalBonusType.GEMSTONE, 0.0); }
        public double getHotmCriticalDamage() { return criticalDamageBonuses.getOrDefault(CriticalBonusType.HOTM, 0.0); }
        public double getFairySoulCriticalDamage() { return criticalDamageBonuses.getOrDefault(CriticalBonusType.FAIRY_SOUL, 0.0); }

        // Getters
        public UUID getPlayerId() { return playerId; }
        public int getTotalCriticalHits() { return totalCriticalHits; }
        public double getTotalCriticalDamage() { return totalCriticalDamage; }
        public Map<CriticalHitType, Integer> getCriticalHitsByType() { return criticalHitsByType; }
        public Map<CriticalBonusType, Double> getCriticalChanceBonuses() { return criticalChanceBonuses; }
        public Map<CriticalBonusType, Double> getCriticalDamageBonuses() { return criticalDamageBonuses; }
    }
}
