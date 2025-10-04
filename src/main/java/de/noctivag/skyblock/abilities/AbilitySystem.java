package de.noctivag.skyblock.abilities;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import de.noctivag.skyblock.magic.ManaSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Ability System - Hypixel Skyblock Style
 */
public class AbilitySystem implements Listener {
    private final SkyblockPlugin plugin;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final MultiServerDatabaseManager databaseManager;
    private final ManaSystem manaSystem;
    private final Map<UUID, PlayerAbilities> playerAbilities = new ConcurrentHashMap<>();
    private final Map<AbilityType, AbilityConfig> abilityConfigs = new HashMap<>();
    private final Map<UUID, Map<AbilityType, Long>> playerCooldowns = new ConcurrentHashMap<>();
    @SuppressWarnings("unused")
    private final Map<UUID, List<ActiveAbility>> activeAbilities = new ConcurrentHashMap<>();
    @SuppressWarnings("unused")
    private final Map<UUID, BukkitTask> abilityTasks = new ConcurrentHashMap<>();

    public AbilitySystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager, ManaSystem manaSystem) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.manaSystem = manaSystem;
        initializeAbilityConfigs();
        startAbilityUpdateTask();

        // Register events
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void initializeAbilityConfigs() {
        // Combat Abilities
        abilityConfigs.put(AbilityType.FIREBALL, new AbilityConfig(
            "Fireball", "§cFireball", Material.FIRE_CHARGE,
            "§7Shoots a fireball that explodes on impact.",
            AbilityCategory.COMBAT, AbilityBehavior.ACTIVE, 5000, 50, 10.0, 5.0,
            Arrays.asList("§7- Shoots a fireball", "§7- Explodes on impact", "§7- Sets enemies on fire"),
            Arrays.asList("§7- 50 Mana", "§7- 5 second cooldown", "§7- 10 block range")
        ));

        abilityConfigs.put(AbilityType.LIGHTNING_STRIKE, new AbilityConfig(
            "Lightning Strike", "§eLightning Strike", Material.LIGHTNING_ROD,
            "§7Strikes lightning at the target location.",
            AbilityCategory.COMBAT, AbilityBehavior.ACTIVE, 8000, 75, 15.0, 8.0,
            Arrays.asList("§7- Strikes lightning", "§7- Damages all enemies in area", "§7- Stuns enemies"),
            Arrays.asList("§7- 75 Mana", "§7- 8 second cooldown", "§7- 15 block range")
        ));

        abilityConfigs.put(AbilityType.ICE_SHARD, new AbilityConfig(
            "Ice Shard", "§bIce Shard", Material.ICE,
            "§7Launches an ice shard that freezes enemies.",
            AbilityCategory.COMBAT, AbilityBehavior.ACTIVE, 6000, 60, 12.0, 6.0,
            Arrays.asList("§7- Launches ice shard", "§7- Freezes enemies", "§7- Slows movement"),
            Arrays.asList("§7- 60 Mana", "§7- 6 second cooldown", "§7- 12 block range")
        ));

        // Utility Abilities
        abilityConfigs.put(AbilityType.TELEPORT, new AbilityConfig(
            "Teleport", "§dTeleport", Material.ENDER_PEARL,
            "§7Teleports you to the target location.",
            AbilityCategory.UTILITY, AbilityBehavior.ACTIVE, 10000, 100, 20.0, 10.0,
            Arrays.asList("§7- Teleports to target", "§7- Instant movement", "§7- No fall damage"),
            Arrays.asList("§7- 100 Mana", "§7- 10 second cooldown", "§7- 20 block range")
        ));

        abilityConfigs.put(AbilityType.INVISIBILITY, new AbilityConfig(
            "Invisibility", "§fInvisibility", Material.GLASS,
            "§7Makes you invisible for a short time.",
            AbilityCategory.UTILITY, AbilityBehavior.TOGGLE, 15000, 80, 0.0, 15.0,
            Arrays.asList("§7- Makes you invisible", "§7- Enemies can't see you", "§7- Movement speed bonus"),
            Arrays.asList("§7- 80 Mana", "§7- 15 second cooldown", "§7- 10 second duration")
        ));

        // Movement Abilities
        abilityConfigs.put(AbilityType.DASH, new AbilityConfig(
            "Dash", "§aDash", Material.FEATHER,
            "§7Quickly dashes forward.",
            AbilityCategory.MOVEMENT, AbilityBehavior.ACTIVE, 3000, 30, 8.0, 3.0,
            Arrays.asList("§7- Dashes forward", "§7- Quick movement", "§7- Momentum boost"),
            Arrays.asList("§7- 30 Mana", "§7- 3 second cooldown", "§7- 8 block distance")
        ));

        abilityConfigs.put(AbilityType.FLIGHT, new AbilityConfig(
            "Flight", "§eFlight", Material.ELYTRA,
            "§7Allows you to fly for a short time.",
            AbilityCategory.MOVEMENT, AbilityBehavior.TOGGLE, 20000, 120, 0.0, 20.0,
            Arrays.asList("§7- Allows flight", "§7- Free movement", "§7- Speed boost"),
            Arrays.asList("§7- 120 Mana", "§7- 20 second cooldown", "§7- 15 second duration")
        ));

        // Support Abilities
        abilityConfigs.put(AbilityType.HEAL, new AbilityConfig(
            "Heal", "§aHeal", Material.GOLDEN_APPLE,
            "§7Heals you and nearby allies.",
            AbilityCategory.SUPPORT, AbilityBehavior.ACTIVE, 12000, 90, 8.0, 12.0,
            Arrays.asList("§7- Heals you and allies", "§7- Restores health", "§7- Area effect"),
            Arrays.asList("§7- 90 Mana", "§7- 12 second cooldown", "§7- 8 block radius")
        ));

        abilityConfigs.put(AbilityType.SHIELD, new AbilityConfig(
            "Shield", "§bShield", Material.SHIELD,
            "§7Creates a protective shield around you.",
            AbilityCategory.SUPPORT, AbilityBehavior.ACTIVE, 15000, 100, 5.0, 15.0,
            Arrays.asList("§7- Creates protective shield", "§7- Absorbs damage", "§7- Area protection"),
            Arrays.asList("§7- 100 Mana", "§7- 15 second cooldown", "§7- 5 block radius")
        ));
    }

    private void startAbilityUpdateTask() {
        Thread.ofVirtual().start(() -> {
            while (plugin.isEnabled()) {
                try {
                    updateActiveAbilities();
                    updateCooldowns();
                    Thread.sleep(1000); // Every second = 1000 ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.hasItemMeta()) {
            // Check for ability items
            AbilityType abilityType = getAbilityFromItem(item);
            if (abilityType != null) {
                useAbility(player, abilityType);
            }
        }
    }

    private AbilityType getAbilityFromItem(ItemStack item) {
        if (item == null) return null;
        if (!item.hasItemMeta()) return null;
        var meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return null;

        List<String> lore = new ArrayList<>();
        var metaLore = meta.lore();
        if (metaLore != null) {
            for (var obj : metaLore) {
                lore.add(obj == null ? "" : obj.toString());
            }
        }

        for (String line : lore) {
            if (line != null && line.contains("Ability:")) {
                String abilityName = line.substring(line.indexOf("Ability:") + 9).trim();
                return getAbilityTypeByName(abilityName);
            }
        }

        return null;
    }

    private AbilityType getAbilityTypeByName(String name) {
        for (AbilityType type : AbilityType.values()) {
            if (type.name().equalsIgnoreCase(name.replace(" ", "_"))) {
                return type;
            }
        }
        return null;
    }

    public boolean useAbility(Player player, AbilityType abilityType) {
        AbilityConfig config = abilityConfigs.get(abilityType);
        if (config == null) {
            player.sendMessage(Component.text("Ability nicht gefunden!").color(NamedTextColor.RED));
            return false;
        }

        // Check cooldown
        if (!canUseAbility(player, abilityType)) {
            long cooldownLeft = getCooldownLeft(player, abilityType);
            player.sendMessage(Component.text("Ability ist noch " + (cooldownLeft / 1000) + " Sekunden im Cooldown!").color(NamedTextColor.RED));
            return false;
        }

        // Check mana
        if (!manaSystem.useMana(player, config.getManaCost())) {
            return false;
        }

        // Use ability
        executeAbility(player, abilityType, config);

        // Set cooldown
        setCooldown(player, abilityType, config.getCooldown());

        return true;
    }

    private void executeAbility(Player player, AbilityType abilityType, AbilityConfig config) {
        switch (abilityType) {
            case FIREBALL:
                executeFireball(player, config);
                break;
            case LIGHTNING_STRIKE:
                executeLightningStrike(player, config);
                break;
            case ICE_SHARD:
                executeIceShard(player, config);
                break;
            case TELEPORT:
                executeTeleport(player, config);
                break;
            case INVISIBILITY:
                executeInvisibility(player, config);
                break;
            case DASH:
                executeDash(player, config);
                break;
            case FLIGHT:
                executeFlight(player, config);
                break;
            case HEAL:
                executeHeal(player, config);
                break;
            case SHIELD:
                executeShield(player, config);
                break;
        }
    }

    private void executeFireball(Player player, AbilityConfig config) {
        // Create fireball projectile
        // Location targetLocation = player.getTargetBlock(null, (int) config.getRange()).getLocation();

        // Particle effects
        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);

        // Sound effects
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1.0f, 1.0f);

        // Damage nearby enemies
        for (Entity entity : player.getNearbyEntities(config.getRange(), config.getRange(), config.getRange())) {
            if (entity instanceof LivingEntity && entity != player) {
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.damage(config.getDamage(), player);
                livingEntity.setFireTicks(100); // 5 seconds of fire
            }
        }

        player.sendMessage(Component.text("Fireball abgefeuert!").color(NamedTextColor.RED));
    }

    private void executeLightningStrike(Player player, AbilityConfig config) {
        // Strike lightning at target location
        // Location targetLocation = player.getTargetBlock(null, (int) config.getRange()).getLocation();
        // Strike lightning at target location (use getTargetBlockExact with fallback)
        org.bukkit.block.Block targetBlock = player.getTargetBlockExact((int) config.getRange());
        Location targetLocation;
        if (targetBlock != null) {
            targetLocation = targetBlock.getLocation();
        } else {
            targetLocation = player.getLocation().add(player.getLocation().getDirection().multiply(config.getRange()));
        }

        // Strike lightning
        player.getWorld().strikeLightning(targetLocation);

        // Particle effects
        player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, targetLocation, 30, 1.0, 1.0, 1.0, 0.2);

        // Sound effects
        player.getWorld().playSound(targetLocation, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);

        // Damage nearby enemies
        for (Entity entity : targetLocation.getWorld().getNearbyEntities(targetLocation, 3, 3, 3)) {
            if (entity instanceof LivingEntity && entity != player) {
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.damage(config.getDamage(), player);
            }
        }

        player.sendMessage(Component.text("Lightning Strike ausgeführt!").color(NamedTextColor.YELLOW));
    }

    private void executeIceShard(Player player, AbilityConfig config) {
        // Create ice shard projectile
        // Location targetLocation = player.getTargetBlock(null, (int) config.getRange()).getLocation();

        // Particle effects
        player.getWorld().spawnParticle(Particle.SNOWFLAKE, player.getLocation(), 25, 0.5, 0.5, 0.5, 0.1);

        // Sound effects
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 0.5f);

        // Damage and freeze nearby enemies
        for (Entity entity : player.getNearbyEntities(config.getRange(), config.getRange(), config.getRange())) {
            if (entity instanceof LivingEntity && entity != player) {
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.damage(config.getDamage(), player);
                livingEntity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.SLOWNESS, 100, 2));
            }
        }

        player.sendMessage(Component.text("Ice Shard abgefeuert!").color(NamedTextColor.AQUA));
    }

    private void executeTeleport(Player player, AbilityConfig config) {
        // Teleport to target location
        // Location targetLocation = player.getTargetBlock(null, (int) config.getRange()).getLocation();
        // targetLocation.setY(targetLocation.getY() + 1); // Teleport above the block
        // Teleport to target location (use getTargetBlockExact with fallback)
        org.bukkit.block.Block tb = player.getTargetBlockExact((int) config.getRange());
        Location targetLocation;
        if (tb != null) {
            targetLocation = tb.getLocation().add(0, 1, 0);
        } else {
            targetLocation = player.getLocation().add(player.getLocation().getDirection().multiply(config.getRange()));
        }

        // Particle effects
        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 50, 0.5, 0.5, 0.5, 0.1);
        player.getWorld().spawnParticle(Particle.PORTAL, targetLocation, 50, 0.5, 0.5, 0.5, 0.1);

        // Sound effects
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        player.getWorld().playSound(targetLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);

        // Teleport player
        player.teleport(targetLocation);

        player.sendMessage(Component.text("Teleportiert!").color(NamedTextColor.LIGHT_PURPLE));
    }

    private void executeInvisibility(Player player, AbilityConfig config) {
        // Duration in ticks (from config.duration as seconds). Fallback to 10s if unset
        long ticks = (long) (config.getDuration() * 20);
        if (ticks <= 0) ticks = 200L; // default 10s

        player.addPotionEffect(new org.bukkit.potion.PotionEffect(
                org.bukkit.potion.PotionEffectType.INVISIBILITY, (int) ticks, 0));

        // Particle effects
        player.getWorld().spawnParticle(Particle.SMOKE, player.getLocation(), 30, 0.5, 0.5, 0.5, 0.1);
        // Sound effects
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
        player.sendMessage(Component.text("Unsichtbar!").color(NamedTextColor.WHITE));
    }

    private void executeDash(Player player, AbilityConfig config) {
        // Dash forward
        Location dashLocation = player.getLocation().add(player.getLocation().getDirection().multiply(config.getRange()));

        // Particle effects
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);

        // Sound effects
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f);

        // Teleport player
        player.teleport(dashLocation);

        player.sendMessage(Component.text("Gedasht!").color(NamedTextColor.GREEN));
    }

    private void executeFlight(Player player, AbilityConfig config) {
        // Allow flight
        player.setAllowFlight(true);
        player.setFlying(true);

        // Particle effects
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 30, 0.5, 0.5, 0.5, 0.1);

        // Sound effects
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 1.0f);

         // Disable flight after duration - use virtual thread for Folia compatibility
         Thread.ofVirtual().start(() -> {
             try {
                 Thread.sleep((long) (config.getDuration() * 1000)); // Convert seconds to ms
                 player.setAllowFlight(false);
                 player.setFlying(false);
                 player.sendMessage(Component.text("Flug beendet!").color(NamedTextColor.YELLOW));
             } catch (InterruptedException e) {
                 Thread.currentThread().interrupt();
             }
         });

        player.sendMessage(Component.text("Flug aktiviert!").color(NamedTextColor.YELLOW));
    }

    @SuppressWarnings("deprecation")
    private void executeHeal(Player player, AbilityConfig config) {
        // Heal player safely using getMaxHealth() fallback
        double playerMax = player.getMaxHealth();
        double newHealth = Math.min(player.getHealth() + config.getDamage(), playerMax);
        player.setHealth(newHealth);

        // Heal nearby allies
        for (Entity entity : player.getNearbyEntities(config.getRange(), config.getRange(), config.getRange())) {
            if (entity instanceof Player && entity != player) {
                Player ally = (Player) entity;
                double allyMax = ally.getMaxHealth();
                double allyNewHealth = Math.min(ally.getHealth() + config.getDamage(), allyMax);
                ally.setHealth(allyNewHealth);
            }
        }

        // Particle effects
        player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.1);

        // Sound effects
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

        player.sendMessage(Component.text("Geheilt!").color(NamedTextColor.GREEN));
    }

    private void executeShield(Player player, AbilityConfig config) {
        long ticks = (long) (config.getDuration() * 20);
        if (ticks <= 0) ticks = 200L; // default 10s

        // Create shield effect (Resistance level 2)
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(
                org.bukkit.potion.PotionEffectType.RESISTANCE, (int) ticks, 2));

        // Particle & sound
        player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, player.getLocation(), 30, 0.5, 0.5, 0.5, 0.1);
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, 1.0f, 1.0f);
        player.sendMessage(Component.text("Schild aktiviert!").color(NamedTextColor.BLUE));
    }

    private void setCooldown(Player player, AbilityType abilityType, long cooldown) {
        // Store expiry time (ms) instead of last-used timestamp
        long expiry = System.currentTimeMillis() + cooldown;
        playerCooldowns.computeIfAbsent(player.getUniqueId(), k -> new ConcurrentHashMap<>())
                .put(abilityType, expiry);
    }

    private void updateActiveAbilities() {
        // Update active abilities
        for (Map.Entry<UUID, List<ActiveAbility>> entry : activeAbilities.entrySet()) {
            List<ActiveAbility> abilities = entry.getValue();

            // Remove expired abilities
            abilities.removeIf(ability -> System.currentTimeMillis() - ability.getStartTime() > ability.getDuration());
        }
    }

    private void updateCooldowns() {
        // Update cooldowns (remove expired ones)
        for (Map.Entry<UUID, Map<AbilityType, Long>> entry : playerCooldowns.entrySet()) {
            Map<AbilityType, Long> cooldowns = entry.getValue();

            long now = System.currentTimeMillis();
            cooldowns.entrySet().removeIf(e -> e.getValue() == null || now >= e.getValue());
        }
    }

    private boolean canUseAbility(Player player, AbilityType abilityType) {
        Map<AbilityType, Long> cooldowns = playerCooldowns.get(player.getUniqueId());
        if (cooldowns == null) return true;

        Long expiry = cooldowns.get(abilityType);
        if (expiry == null) return true;
        return System.currentTimeMillis() >= expiry;
    }

    private long getCooldownLeft(Player player, AbilityType abilityType) {
        Map<AbilityType, Long> cooldowns = playerCooldowns.get(player.getUniqueId());
        if (cooldowns == null) return 0;

        Long expiry = cooldowns.get(abilityType);
        if (expiry == null) return 0;
        long timeLeft = expiry - System.currentTimeMillis();
        return Math.max(0, timeLeft);
    }

    public PlayerAbilities getPlayerAbilities(UUID playerId) {
        return playerAbilities.computeIfAbsent(playerId, k -> new PlayerAbilities(playerId));
    }

    public AbilityConfig getAbilityConfig(AbilityType type) {
        return abilityConfigs.get(type);
    }

    public List<AbilityType> getAllAbilityTypes() {
        return new ArrayList<>(abilityConfigs.keySet());
    }

    // Ability Type Enum
    public enum AbilityType {
        // Combat Abilities
        FIREBALL, LIGHTNING_STRIKE, ICE_SHARD,

        // Utility Abilities
        TELEPORT, INVISIBILITY,

        // Movement Abilities
        DASH, FLIGHT,

        // Support Abilities
        HEAL, SHIELD
    }

    // Ability Category Enum
    public enum AbilityCategory {
        COMBAT("§cCombat", 1.0),
        UTILITY("§6Utility", 1.2),
        MOVEMENT("§aMovement", 1.1),
        SUPPORT("§bSupport", 1.3);

        private final String displayName;
        private final double multiplier;

        AbilityCategory(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }

        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }

    // Ability Behavior Enum (for ability behavior)
    public enum AbilityBehavior {
        ACTIVE("Active", 1.0),
        PASSIVE("Passive", 0.0),
        TOGGLE("Toggle", 1.5),
        CHANNELED("Channeled", 2.0);

        private final String displayName;
        private final double multiplier;

        AbilityBehavior(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }

        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }

    // Ability Config Class
    public static class AbilityConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final AbilityCategory category;
        private final AbilityBehavior type;
        private final int cooldown;
        private final double manaCost;
        private final double range;
        private final double damage;
        private final double duration;
        private final List<String> effects;
        private final List<String> requirements;

        public AbilityConfig(String name, String displayName, Material icon, String description,
                           AbilityCategory category, AbilityBehavior type, int cooldown, double manaCost,
                           double range, double damage, List<String> effects, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
            this.category = category;
            this.type = type;
            this.cooldown = cooldown;
            this.manaCost = manaCost;
            this.range = range;
            this.damage = damage;
            this.duration = 0.0; // Default duration
            this.effects = effects;
            this.requirements = requirements;
        }

        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public AbilityCategory getCategory() { return category; }
        public AbilityBehavior getType() { return type; }
        public int getCooldown() { return cooldown; }
        public double getManaCost() { return manaCost; }
        public double getRange() { return range; }
        public double getDamage() { return damage; }
        public double getDuration() { return duration; }
        public List<String> getEffects() { return effects; }
        public List<String> getRequirements() { return requirements; }
    }

    // Player Abilities Class
    public static class PlayerAbilities {
        private final UUID playerId;
        private final Map<AbilityType, Integer> abilityLevels = new ConcurrentHashMap<>();
        private final Map<AbilityType, Long> abilityCooldowns = new ConcurrentHashMap<>();
        private final List<ActiveAbility> activeAbilities = new ArrayList<>();

        public PlayerAbilities(UUID playerId) {
            this.playerId = playerId;
        }

        public void addAbility(AbilityType abilityType, int level) {
            abilityLevels.put(abilityType, level);
        }

        public int getAbilityLevel(AbilityType abilityType) {
            return abilityLevels.getOrDefault(abilityType, 0);
        }

        public void setAbilityCooldown(AbilityType abilityType, long cooldown) {
            abilityCooldowns.put(abilityType, cooldown);
        }

        public long getAbilityCooldown(AbilityType abilityType) {
            return abilityCooldowns.getOrDefault(abilityType, 0L);
        }

        public void addActiveAbility(ActiveAbility ability) {
            activeAbilities.add(ability);
        }

        public void removeActiveAbility(ActiveAbility ability) {
            activeAbilities.remove(ability);
        }

        public List<ActiveAbility> getActiveAbilities() {
            return activeAbilities;
        }

        // Getters
        public UUID getPlayerId() { return playerId; }
        public Map<AbilityType, Integer> getAbilityLevels() { return abilityLevels; }
        public Map<AbilityType, Long> getAbilityCooldowns() { return abilityCooldowns; }
    }

    // Active Ability Class
    public static class ActiveAbility {
        private final AbilityType abilityType;
        private final long startTime;
        private final long duration;
        private final UUID casterId;

        public ActiveAbility(AbilityType abilityType, long startTime, long duration, UUID casterId) {
            this.abilityType = abilityType;
            this.startTime = startTime;
            this.duration = duration;
            this.casterId = casterId;
        }

        public AbilityType getAbilityType() { return abilityType; }
        public long getStartTime() { return startTime; }
        public long getDuration() { return duration; }
        public UUID getCasterId() { return casterId; }
    }
}
