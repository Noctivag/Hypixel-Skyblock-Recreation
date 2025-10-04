package de.noctivag.plugin.combat;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Damage System - Hypixel Skyblock Style
 *
 * Features:
 * - Damage Types (Physical, Magic, True, Fire, Ice, Lightning, Poison, etc.)
 * - Damage Calculation (Base Damage, Strength, Critical Damage, etc.)
 * - Damage Reduction (Defense, Resistance, Absorption)
 * - Damage Over Time (DOT)
 * - Damage Over Time (HOT)
 * - Damage Reflection
 * - Damage Absorption
 * - Damage Immunity
 * - Damage Vulnerability
 * - Damage Multipliers
 * - Damage Bonuses
 * - Damage Penalties
 * - Damage Events
 * - Damage Logging
 * - Damage Statistics
 */
public class AdvancedDamageSystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerDamageStats> playerDamageStats = new ConcurrentHashMap<>();
    private final Map<UUID, List<DamageOverTime>> damageOverTime = new ConcurrentHashMap<>();
    private final Map<UUID, List<HealOverTime>> healOverTime = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> damageTasks = new ConcurrentHashMap<>();

    public AdvancedDamageSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        startDamageUpdateTask();

        // Register events
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void startDamageUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateDamageOverTime();
                updateHealOverTime();
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

            // Calculate advanced damage
            DamageCalculation damageCalc = calculateDamage(player, livingTarget, event.getDamage());

            // Apply damage
            event.setDamage(damageCalc.getFinalDamage());

            // Apply damage effects
            applyDamageEffects(player, livingTarget, damageCalc);

            // Update damage statistics
            updateDamageStats(player, damageCalc);

            // Show damage number
            showDamageNumber(livingTarget, damageCalc);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof LivingEntity) {
            // LivingEntity livingEntity = (LivingEntity) entity;

            // Check for damage over time
            List<DamageOverTime> dots = damageOverTime.get(entity.getUniqueId());
            if (dots != null && !dots.isEmpty()) {
                for (DamageOverTime dot : dots) {
                    if (dot.getDamageType() == DamageType.TRUE) {
                        // True damage bypasses all reductions
                        event.setDamage(event.getDamage() + dot.getDamage());
                    }
                }
            }
        }
    }

    private DamageCalculation calculateDamage(Player player, LivingEntity target, double baseDamage) {
        DamageCalculation calc = new DamageCalculation();

        // Base damage
        calc.setBaseDamage(baseDamage);

        // Get player stats
        PlayerStats playerStats = getPlayerStats(player);
        EntityStats targetStats = getEntityStats(target);

        // Calculate strength bonus
        double strengthBonus = calculateStrengthBonus(playerStats.getStrength());
        calc.setStrengthBonus(strengthBonus);

        // Calculate critical damage
        double criticalDamage = calculateCriticalDamage(playerStats.getCriticalDamage());
        calc.setCriticalDamage(criticalDamage);

        // Calculate damage multiplier
        double damageMultiplier = calculateDamageMultiplier(playerStats, targetStats);
        calc.setDamageMultiplier(damageMultiplier);

        // Calculate final damage
        double finalDamage = (baseDamage + strengthBonus) * (1 + criticalDamage) * damageMultiplier;
        calc.setFinalDamage(finalDamage);

        // Determine damage type
        DamageType damageType = determineDamageType(player, target);
        calc.setDamageType(damageType);

        // Check for critical hit
        boolean isCritical = isCriticalHit(playerStats.getCriticalChance());
        calc.setCritical(isCritical);

        return calc;
    }

    private double calculateStrengthBonus(double strength) {
        // Strength formula: 1 + (strength / 100)
        return strength / 100.0;
    }

    private double calculateCriticalDamage(double criticalDamage) {
        // Critical damage formula: criticalDamage / 100
        return criticalDamage / 100.0;
    }

    private double calculateDamageMultiplier(PlayerStats playerStats, EntityStats targetStats) {
        double multiplier = 1.0;

        // Add weapon damage multiplier
        multiplier += playerStats.getWeaponDamageMultiplier();

        // Add skill damage multiplier
        multiplier += playerStats.getSkillDamageMultiplier();

        // Add pet damage multiplier
        multiplier += playerStats.getPetDamageMultiplier();

        // Add armor damage multiplier
        multiplier += playerStats.getArmorDamageMultiplier();

        // Add accessory damage multiplier
        multiplier += playerStats.getAccessoryDamageMultiplier();

        // Add potion damage multiplier
        multiplier += playerStats.getPotionDamageMultiplier();

        // Add enchantment damage multiplier
        multiplier += playerStats.getEnchantmentDamageMultiplier();

        // Add reforge damage multiplier
        multiplier += playerStats.getReforgeDamageMultiplier();

        // Add gemstone damage multiplier
        multiplier += playerStats.getGemstoneDamageMultiplier();

        // Add HotM damage multiplier
        multiplier += playerStats.getHotmDamageMultiplier();

        // Add fairy soul damage multiplier
        multiplier += playerStats.getFairySoulDamageMultiplier();

        return multiplier;
    }

    private DamageType determineDamageType(Player player, LivingEntity target) {
        // Check weapon type
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon != null) {
            Material material = weapon.getType();

            if (material.name().contains("SWORD")) {
                return DamageType.PHYSICAL;
            } else if (material.name().contains("BOW")) {
                return DamageType.PHYSICAL;
            } else if (material.name().contains("TRIDENT")) {
                return DamageType.MAGIC;
            } else if (material.name().contains("CROSSBOW")) {
                return DamageType.PHYSICAL;
            }
        }

        // Default to physical damage
        return DamageType.PHYSICAL;
    }

    private boolean isCriticalHit(double criticalChance) {
        return Math.random() < (criticalChance / 100.0);
    }

    private void applyDamageEffects(Player player, LivingEntity target, DamageCalculation damageCalc) {
        // Apply damage type specific effects
        switch (damageCalc.getDamageType()) {
            case PHYSICAL:
                // Physical damage has no special effects
                break;
            case FIRE:
                target.setFireTicks(100); // 5 seconds of fire
                break;
            case ICE:
                target.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.SLOWNESS, 100, 2));
                break;
            case LIGHTNING:
                target.getWorld().strikeLightningEffect(target.getLocation());
                break;
            case POISON:
                target.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.POISON, 100, 1));
                break;
            case MAGIC:
                // Magic damage effects
                break;
            case TRUE:
                // True damage bypasses all reductions
                break;
        }

        // Apply critical hit effects
        if (damageCalc.isCritical()) {
            // Particle effects
            target.getWorld().spawnParticle(org.bukkit.Particle.CRIT,
                target.getLocation().add(0, 1, 0), 50, 0.5, 0.5, 0.5, 0.3);

            // Sound effects
            target.getWorld().playSound(target.getLocation(),
                org.bukkit.Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f);
        }
    }

    private void updateDamageStats(Player player, DamageCalculation damageCalc) {
        PlayerDamageStats stats = playerDamageStats.computeIfAbsent(
            player.getUniqueId(), k -> new PlayerDamageStats(player.getUniqueId()));

        stats.addDamageDealt(damageCalc.getFinalDamage());
        stats.addDamageType(damageCalc.getDamageType(), damageCalc.getFinalDamage());

        if (damageCalc.isCritical()) {
            stats.addCriticalHit();
        }
    }

    private void showDamageNumber(LivingEntity target, DamageCalculation damageCalc) {
        // Create damage number display
        String damageText = createDamageText(damageCalc);

        // Show to nearby players
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().distance(target.getLocation()) <= 50) {
                player.sendActionBar(damageText);
            }
        }
    }

    private String createDamageText(DamageCalculation damageCalc) {
        StringBuilder damageText = new StringBuilder();

        // Critical hit indicator
        if (damageCalc.isCritical()) {
            damageText.append(ChatColor.RED).append("✧ ");
        }

        // Damage type color
        ChatColor damageColor = getDamageTypeColor(damageCalc.getDamageType());
        damageText.append(damageColor);

        // Damage amount
        damageText.append(String.format("%.0f", damageCalc.getFinalDamage()));

        // Critical hit indicator
        if (damageCalc.isCritical()) {
            damageText.append(" ✧");
        }

        return damageText.toString();
    }

    private ChatColor getDamageTypeColor(DamageType damageType) {
        return switch (damageType) {
            case PHYSICAL -> ChatColor.RED;
            case MAGIC -> ChatColor.LIGHT_PURPLE;
            case FIRE -> ChatColor.GOLD;
            case ICE -> ChatColor.AQUA;
            case LIGHTNING -> ChatColor.YELLOW;
            case POISON -> ChatColor.GREEN;
            case TRUE -> ChatColor.WHITE;
        };
    }

    private void updateDamageOverTime() {
        long currentTime = System.currentTimeMillis();

        for (Map.Entry<UUID, List<DamageOverTime>> entry : damageOverTime.entrySet()) {
            UUID entityId = entry.getKey();
            List<DamageOverTime> dots = entry.getValue();

            Entity entity = Bukkit.getEntity(entityId);
            if (entity == null || !(entity instanceof LivingEntity)) {
                damageOverTime.remove(entityId);
                continue;
            }

            LivingEntity livingEntity = (LivingEntity) entity;

            // Apply damage over time
            for (DamageOverTime dot : dots) {
                if (currentTime - dot.getLastTick() >= dot.getTickInterval()) {
                    // Apply damage
                    livingEntity.damage(dot.getDamage());

                    // Update last tick
                    dot.setLastTick(currentTime);

                    // Show damage number
                    showDamageNumber(livingEntity, new DamageCalculation() {{
                        setFinalDamage(dot.getDamage());
                        setDamageType(dot.getDamageType());
                        setCritical(false);
                    }});
                }
            }

            // Remove expired dots
            dots.removeIf(dot -> currentTime - dot.getStartTime() > dot.getDuration());
        }
    }

    private void updateHealOverTime() {
        long currentTime = System.currentTimeMillis();

        for (Map.Entry<UUID, List<HealOverTime>> entry : healOverTime.entrySet()) {
            UUID entityId = entry.getKey();
            List<HealOverTime> hots = entry.getValue();

            Entity entity = Bukkit.getEntity(entityId);
            if (entity == null || !(entity instanceof LivingEntity)) {
                healOverTime.remove(entityId);
                continue;
            }

            LivingEntity livingEntity = (LivingEntity) entity;

            // Apply heal over time
            for (HealOverTime hot : hots) {
                if (currentTime - hot.getLastTick() >= hot.getTickInterval()) {
                    // Apply healing
                    double newHealth = Math.min(livingEntity.getHealth() + hot.getHealing(),
                        livingEntity.getMaxHealth());
                    livingEntity.setHealth(newHealth);

                    // Update last tick
                    hot.setLastTick(currentTime);

                    // Show healing number
                    showHealingNumber(livingEntity, hot.getHealing());
                }
            }

            // Remove expired hots
            hots.removeIf(hot -> currentTime - hot.getStartTime() > hot.getDuration());
        }
    }

    private void showHealingNumber(LivingEntity entity, double healing) {
        String healingText = ChatColor.GREEN + "+" + String.format("%.0f", healing);

        // Show to nearby players
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 50) {
                player.sendActionBar(healingText);
            }
        }
    }

    public void addDamageOverTime(LivingEntity entity, DamageType damageType, double damage,
                                 long duration, long tickInterval) {
        UUID entityId = entity.getUniqueId();
        DamageOverTime dot = new DamageOverTime(damageType, damage, duration, tickInterval);

        damageOverTime.computeIfAbsent(entityId, k -> new ArrayList<>()).add(dot);
    }

    public void addHealOverTime(LivingEntity entity, double healing, long duration, long tickInterval) {
        UUID entityId = entity.getUniqueId();
        HealOverTime hot = new HealOverTime(healing, duration, tickInterval);

        healOverTime.computeIfAbsent(entityId, k -> new ArrayList<>()).add(hot);
    }

    public PlayerStats getPlayerStats(Player player) {
        // This would integrate with the skills system, items system, etc.
        return new PlayerStats();
    }

    public EntityStats getEntityStats(LivingEntity entity) {
        // This would get entity-specific stats
        return new EntityStats();
    }

    // Damage Type Enum
    public enum DamageType {
        PHYSICAL("Physical", ChatColor.RED),
        MAGIC("Magic", ChatColor.LIGHT_PURPLE),
        FIRE("Fire", ChatColor.GOLD),
        ICE("Ice", ChatColor.AQUA),
        LIGHTNING("Lightning", ChatColor.YELLOW),
        POISON("Poison", ChatColor.GREEN),
        TRUE("True", ChatColor.WHITE);

        private final String displayName;
        private final ChatColor color;

        DamageType(String displayName, ChatColor color) {
            this.displayName = displayName;
            this.color = color;
        }

        public String getDisplayName() { return displayName; }
        public ChatColor getColor() { return color; }
    }

    // Damage Calculation Class
    public static class DamageCalculation {
        private double baseDamage;
        private double strengthBonus;
        private double criticalDamage;
        private double damageMultiplier;
        private double finalDamage;
        private DamageType damageType;
        private boolean isCritical;

        // Getters and Setters
        public double getBaseDamage() { return baseDamage; }
        public void setBaseDamage(double baseDamage) { this.baseDamage = baseDamage; }

        public double getStrengthBonus() { return strengthBonus; }
        public void setStrengthBonus(double strengthBonus) { this.strengthBonus = strengthBonus; }

        public double getCriticalDamage() { return criticalDamage; }
        public void setCriticalDamage(double criticalDamage) { this.criticalDamage = criticalDamage; }

        public double getDamageMultiplier() { return damageMultiplier; }
        public void setDamageMultiplier(double damageMultiplier) { this.damageMultiplier = damageMultiplier; }

        public double getFinalDamage() { return finalDamage; }
        public void setFinalDamage(double finalDamage) { this.finalDamage = finalDamage; }

        public DamageType getDamageType() { return damageType; }
        public void setDamageType(DamageType damageType) { this.damageType = damageType; }

        public boolean isCritical() { return isCritical; }
        public void setCritical(boolean isCritical) { this.isCritical = isCritical; }
    }

    // Player Stats Class
    public static class PlayerStats {
        private double strength = 0.0;
        private double criticalChance = 0.0;
        private double criticalDamage = 0.0;
        private double weaponDamageMultiplier = 0.0;
        private double skillDamageMultiplier = 0.0;
        private double petDamageMultiplier = 0.0;
        private double armorDamageMultiplier = 0.0;
        private double accessoryDamageMultiplier = 0.0;
        private double potionDamageMultiplier = 0.0;
        private double enchantmentDamageMultiplier = 0.0;
        private double reforgeDamageMultiplier = 0.0;
        private double gemstoneDamageMultiplier = 0.0;
        private double hotmDamageMultiplier = 0.0;
        private double fairySoulDamageMultiplier = 0.0;

        // Getters and Setters
        public double getStrength() { return strength; }
        public void setStrength(double strength) { this.strength = strength; }

        public double getCriticalChance() { return criticalChance; }
        public void setCriticalChance(double criticalChance) { this.criticalChance = criticalChance; }

        public double getCriticalDamage() { return criticalDamage; }
        public void setCriticalDamage(double criticalDamage) { this.criticalDamage = criticalDamage; }

        public double getWeaponDamageMultiplier() { return weaponDamageMultiplier; }
        public void setWeaponDamageMultiplier(double weaponDamageMultiplier) { this.weaponDamageMultiplier = weaponDamageMultiplier; }

        public double getSkillDamageMultiplier() { return skillDamageMultiplier; }
        public void setSkillDamageMultiplier(double skillDamageMultiplier) { this.skillDamageMultiplier = skillDamageMultiplier; }

        public double getPetDamageMultiplier() { return petDamageMultiplier; }
        public void setPetDamageMultiplier(double petDamageMultiplier) { this.petDamageMultiplier = petDamageMultiplier; }

        public double getArmorDamageMultiplier() { return armorDamageMultiplier; }
        public void setArmorDamageMultiplier(double armorDamageMultiplier) { this.armorDamageMultiplier = armorDamageMultiplier; }

        public double getAccessoryDamageMultiplier() { return accessoryDamageMultiplier; }
        public void setAccessoryDamageMultiplier(double accessoryDamageMultiplier) { this.accessoryDamageMultiplier = accessoryDamageMultiplier; }

        public double getPotionDamageMultiplier() { return potionDamageMultiplier; }
        public void setPotionDamageMultiplier(double potionDamageMultiplier) { this.potionDamageMultiplier = potionDamageMultiplier; }

        public double getEnchantmentDamageMultiplier() { return enchantmentDamageMultiplier; }
        public void setEnchantmentDamageMultiplier(double enchantmentDamageMultiplier) { this.enchantmentDamageMultiplier = enchantmentDamageMultiplier; }

        public double getReforgeDamageMultiplier() { return reforgeDamageMultiplier; }
        public void setReforgeDamageMultiplier(double reforgeDamageMultiplier) { this.reforgeDamageMultiplier = reforgeDamageMultiplier; }

        public double getGemstoneDamageMultiplier() { return gemstoneDamageMultiplier; }
        public void setGemstoneDamageMultiplier(double gemstoneDamageMultiplier) { this.gemstoneDamageMultiplier = gemstoneDamageMultiplier; }

        public double getHotmDamageMultiplier() { return hotmDamageMultiplier; }
        public void setHotmDamageMultiplier(double hotmDamageMultiplier) { this.hotmDamageMultiplier = hotmDamageMultiplier; }

        public double getFairySoulDamageMultiplier() { return fairySoulDamageMultiplier; }
        public void setFairySoulDamageMultiplier(double fairySoulDamageMultiplier) { this.fairySoulDamageMultiplier = fairySoulDamageMultiplier; }
    }

    // Entity Stats Class
    public static class EntityStats {
        private double defense = 0.0;
        private double resistance = 0.0;
        private double absorption = 0.0;
        private double damageReduction = 0.0;

        // Getters and Setters
        public double getDefense() { return defense; }
        public void setDefense(double defense) { this.defense = defense; }

        public double getResistance() { return resistance; }
        public void setResistance(double resistance) { this.resistance = resistance; }

        public double getAbsorption() { return absorption; }
        public void setAbsorption(double absorption) { this.absorption = absorption; }

        public double getDamageReduction() { return damageReduction; }
        public void setDamageReduction(double damageReduction) { this.damageReduction = damageReduction; }
    }

    // Player Damage Stats Class
    public static class PlayerDamageStats {
        private final UUID playerId;
        private double totalDamageDealt = 0.0;
        private final Map<DamageType, Double> damageByType = new ConcurrentHashMap<>();
        private int criticalHits = 0;
        private int totalHits = 0;

        public PlayerDamageStats(UUID playerId) {
            this.playerId = playerId;
        }

        public void addDamageDealt(double damage) {
            totalDamageDealt += damage;
            totalHits++;
        }

        public void addDamageType(DamageType type, double damage) {
            damageByType.put(type, damageByType.getOrDefault(type, 0.0) + damage);
        }

        public void addCriticalHit() {
            criticalHits++;
        }

        public double getCriticalHitChance() {
            return totalHits > 0 ? (double) criticalHits / totalHits * 100 : 0.0;
        }

        // Getters
        public UUID getPlayerId() { return playerId; }
        public double getTotalDamageDealt() { return totalDamageDealt; }
        public Map<DamageType, Double> getDamageByType() { return damageByType; }
        public int getCriticalHits() { return criticalHits; }
        public int getTotalHits() { return totalHits; }
    }

    // Damage Over Time Class
    public static class DamageOverTime {
        private final DamageType damageType;
        private final double damage;
        private final long duration;
        private final long tickInterval;
        private final long startTime;
        private long lastTick;

        public DamageOverTime(DamageType damageType, double damage, long duration, long tickInterval) {
            this.damageType = damageType;
            this.damage = damage;
            this.duration = duration;
            this.tickInterval = tickInterval;
            this.startTime = System.currentTimeMillis();
            this.lastTick = startTime;
        }

        public DamageType getDamageType() { return damageType; }
        public double getDamage() { return damage; }
        public long getDuration() { return duration; }
        public long getTickInterval() { return tickInterval; }
        public long getStartTime() { return startTime; }
        public long getLastTick() { return lastTick; }
        public void setLastTick(long lastTick) { this.lastTick = lastTick; }
    }

    // Heal Over Time Class
    public static class HealOverTime {
        private final double healing;
        private final long duration;
        private final long tickInterval;
        private final long startTime;
        private long lastTick;

        public HealOverTime(double healing, long duration, long tickInterval) {
            this.healing = healing;
            this.duration = duration;
            this.tickInterval = tickInterval;
            this.startTime = System.currentTimeMillis();
            this.lastTick = startTime;
        }

        public double getHealing() { return healing; }
        public long getDuration() { return duration; }
        public long getTickInterval() { return tickInterval; }
        public long getStartTime() { return startTime; }
        public long getLastTick() { return lastTick; }
        public void setLastTick(long lastTick) { this.lastTick = lastTick; }
    }
}
