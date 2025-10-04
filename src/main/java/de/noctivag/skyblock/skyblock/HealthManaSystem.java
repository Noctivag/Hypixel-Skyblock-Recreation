package de.noctivag.skyblock.skyblock;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
// import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
// import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import net.kyori.adventure.text.Component;
// import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Comprehensive Health and Mana System inspired by Hypixel Skyblock
 * Features:
 * - Dynamic health calculation based on skills, armor, and accessories
 * - Mana system with regeneration and consumption
 * - Health/Mana display in action bar
 * - Damage calculation with critical hits
 * - Absorption hearts system
 * - Health/Mana boosters and modifiers
 */
public class HealthManaSystem implements Listener {

    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerHealthManaData> playerData = new ConcurrentHashMap<>();
    private final Map<UUID, Thread> regenerationTasks = new ConcurrentHashMap<>();
    private final Map<UUID, Thread> displayTasks = new ConcurrentHashMap<>();

    // Health and Mana constants
    private static final double BASE_HEALTH = 100.0;
    private static final double BASE_MANA = 100.0;
    private static final double MANA_REGEN_RATE = 1.0; // per second
    private static final double HEALTH_REGEN_RATE = 0.5; // per second
    private static final int DISPLAY_UPDATE_INTERVAL = 20; // ticks (1 second)
    private static final int REGEN_UPDATE_INTERVAL = 20; // ticks (1 second)

    public HealthManaSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }
    
    public void initialize() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        startGlobalUpdateTask();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Load or create player data
        PlayerHealthManaData data = loadPlayerData(playerId);
        playerData.put(playerId, data);

        // Start regeneration and display tasks
        startPlayerTasks(player);

        // Update player's health and mana
        updatePlayerHealth(player);
        updatePlayerMana(player);

        // Send welcome message
        player.sendMessage("§a§lSkyBlock §7- Health & Mana System loaded!");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Save player data
        savePlayerData(playerId);

        // Cancel tasks
        cancelPlayerTasks(playerId);

        // Remove from memory
        playerData.remove(playerId);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        UUID playerId = player.getUniqueId();
        PlayerHealthManaData data = playerData.get(playerId);

        if (data == null) return;

        // Calculate actual damage with armor and resistance
        double baseDamage = event.getFinalDamage();
        double actualDamage = calculateActualDamage(player, baseDamage, data);

        // Apply damage to health
        data.setCurrentHealth(data.getCurrentHealth() - actualDamage);

        // Check for death
        if (data.getCurrentHealth() <= 0) {
            data.setCurrentHealth(0);
            // Handle death (could trigger respawn system)
            handlePlayerDeath(player, data);
        }

        // Update display
        updateHealthDisplay(player);

        // Send damage message
        player.sendMessage("§c-" + String.format("%.1f", actualDamage) + " ❤");
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        UUID playerId = player.getUniqueId();
        PlayerHealthManaData data = playerData.get(playerId);

        if (data == null) return;

        // Handle natural regeneration
        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
            event.setCancelled(true); // Cancel vanilla regeneration

            // Apply our custom regeneration
            double regenAmount = event.getAmount();
            data.setCurrentHealth(Math.min(data.getMaxHealth(), data.getCurrentHealth() + regenAmount));
            updateHealthDisplay(player);
        }
    }

    private void startGlobalUpdateTask() {
        Thread.ofVirtual().start(() -> {
            while (plugin.isEnabled()) {
                try {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        updatePlayerHealth(player);
                        updatePlayerMana(player);
                    }
                    Thread.sleep(20L * 5L * 50); // Every 5 seconds = 5,000 ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private void startPlayerTasks(Player player) {
        UUID playerId = player.getUniqueId();

        // Regeneration task - use virtual thread for Folia compatibility
        Thread regenThread = Thread.ofVirtual().start(() -> {
            while (plugin.isEnabled() && player.isOnline()) {
                try {
                    regenerateHealthMana(player);
                    Thread.sleep(REGEN_UPDATE_INTERVAL * 50); // Convert ticks to ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        regenerationTasks.put(playerId, regenThread);

        // Display task - use virtual thread for Folia compatibility
        Thread displayThread = Thread.ofVirtual().start(() -> {
            while (plugin.isEnabled() && player.isOnline()) {
                try {
                    updateHealthManaDisplay(player);
                    Thread.sleep(DISPLAY_UPDATE_INTERVAL * 50); // Convert ticks to ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        displayTasks.put(playerId, displayThread);
    }

    private void cancelPlayerTasks(UUID playerId) {
        Thread regenTask = regenerationTasks.remove(playerId);
        if (regenTask != null) {
            regenTask.interrupt();
        }

        Thread displayTask = displayTasks.remove(playerId);
        if (displayTask != null) {
            displayTask.interrupt();
        }
    }

    private void regenerateHealthMana(Player player) {
        UUID playerId = player.getUniqueId();
        PlayerHealthManaData data = playerData.get(playerId);

        if (data == null) return;

        // Regenerate mana
        if (data.getCurrentMana() < data.getMaxMana()) {
            double manaRegen = MANA_REGEN_RATE * getManaRegenMultiplier(player, data);
            data.setCurrentMana(Math.min(data.getMaxMana(), data.getCurrentMana() + manaRegen));
        }

        // Regenerate health (only if not in combat)
        if (!isInCombat(player) && data.getCurrentHealth() < data.getMaxHealth()) {
            double healthRegen = HEALTH_REGEN_RATE * getHealthRegenMultiplier(player, data);
            data.setCurrentHealth(Math.min(data.getMaxHealth(), data.getCurrentHealth() + healthRegen));
        }
    }

    private void updatePlayerHealth(Player player) {
        UUID playerId = player.getUniqueId();
        PlayerHealthManaData data = playerData.get(playerId);

        if (data == null) return;

        // Calculate max health based on skills, armor, accessories
        double maxHealth = calculateMaxHealth(player, data);
        data.setMaxHealth(maxHealth);

        // Ensure current health doesn't exceed max
        if (data.getCurrentHealth() > maxHealth) {
            data.setCurrentHealth(maxHealth);
        }

        // Update Minecraft health using modern Attribute API
        if (player instanceof org.bukkit.entity.LivingEntity) {
            org.bukkit.entity.LivingEntity livingEntity = (org.bukkit.entity.LivingEntity) player;
            livingEntity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(maxHealth);
        }
        player.setHealth(Math.min(20.0, (data.getCurrentHealth() / maxHealth) * 20.0));
    }

    private void updatePlayerMana(Player player) {
        UUID playerId = player.getUniqueId();
        PlayerHealthManaData data = playerData.get(playerId);

        if (data == null) return;

        // Calculate max mana based on skills, armor, accessories
        double maxMana = calculateMaxMana(player, data);
        data.setMaxMana(maxMana);

        // Ensure current mana doesn't exceed max
        if (data.getCurrentMana() > maxMana) {
            data.setCurrentMana(maxMana);
        }
    }

    private double calculateMaxHealth(Player player, PlayerHealthManaData data) {
        double health = BASE_HEALTH;

        // Add health from skills
        health += data.getHealthFromSkills();

        // Add health from armor
        health += getHealthFromArmor(player);

        // Add health from accessories
        health += getHealthFromAccessories(player);

        // Add health from potions/effects
        health += getHealthFromEffects(player);

        // Apply health multipliers
        health *= getHealthMultiplier(player, data);

        return Math.max(1.0, health);
    }

    private double calculateMaxMana(Player player, PlayerHealthManaData data) {
        double mana = BASE_MANA;

        // Add mana from skills
        mana += data.getManaFromSkills();

        // Add mana from armor
        mana += getManaFromArmor(player);

        // Add mana from accessories
        mana += getManaFromAccessories(player);

        // Add mana from potions/effects
        mana += getManaFromEffects(player);

        // Apply mana multipliers
        mana *= getManaMultiplier(player, data);

        return Math.max(0.0, mana);
    }

    private double getHealthFromArmor(Player player) {
        double health = 0.0;

        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null || armor.getType() == Material.AIR) continue;

            // Check for custom armor with health stats
            var meta = armor.getItemMeta();
            if (meta != null && meta.hasLore()) {
                List<Component> lore = meta.lore();
                if (lore == null) continue;
                for (Component line : lore) {
                    String lineText = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(line);
                    if (lineText.contains("❤ Health")) {
                        try {
                            String[] parts = lineText.split(" ");
                            health += Double.parseDouble(parts[0].replace("+", "").replace("❤", ""));
                        } catch (Exception ignored) {}
                    }
                }
            }
        }

        return health;
    }

    private double getManaFromArmor(Player player) {
        double mana = 0.0;

        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null || armor.getType() == Material.AIR) continue;

            // Check for custom armor with mana stats
            var meta = armor.getItemMeta();
            if (meta != null && meta.hasLore()) {
                List<Component> lore = meta.lore();
                if (lore == null) continue;
                for (Component line : lore) {
                    String lineText = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(line);
                    if (lineText.contains("✎ Mana")) {
                        try {
                            String[] parts = lineText.split(" ");
                            mana += Double.parseDouble(parts[0].replace("+", "").replace("✎", ""));
                        } catch (Exception ignored) {}
                    }
                }
            }
        }

        return mana;
    }

    private double getHealthFromAccessories(Player player) {
        // This would integrate with the accessory system
        return 0.0;
    }

    private double getManaFromAccessories(Player player) {
        // This would integrate with the accessory system
        return 0.0;
    }

    private double getHealthFromEffects(Player player) {
        // Check for health boost effects
        return 0.0;
    }

    private double getManaFromEffects(Player player) {
        // Check for mana boost effects
        return 0.0;
    }

    private double getHealthMultiplier(Player player, PlayerHealthManaData data) {
        double multiplier = 1.0;

        // Add multipliers from skills
        multiplier += data.getHealthMultiplierFromSkills();

        // Add multipliers from effects
        multiplier += getHealthMultiplierFromEffects(player);

        return Math.max(0.1, multiplier);
    }

    private double getManaMultiplier(Player player, PlayerHealthManaData data) {
        double multiplier = 1.0;

        // Add multipliers from skills
        multiplier += data.getManaMultiplierFromSkills();

        // Add multipliers from effects
        multiplier += getManaMultiplierFromEffects(player);

        return Math.max(0.1, multiplier);
    }

    private double getHealthMultiplierFromEffects(Player player) {
        // Check for health multiplier effects
        return 0.0;
    }

    private double getManaMultiplierFromEffects(Player player) {
        // Check for mana multiplier effects
        return 0.0;
    }

    private double getManaRegenMultiplier(Player player, PlayerHealthManaData data) {
        double multiplier = 1.0;

        // Add regen multipliers from skills
        multiplier += data.getManaRegenMultiplierFromSkills();

        // Add regen multipliers from effects
        multiplier += getManaRegenMultiplierFromEffects(player);

        return Math.max(0.1, multiplier);
    }

    private double getHealthRegenMultiplier(Player player, PlayerHealthManaData data) {
        double multiplier = 1.0;

        // Add regen multipliers from skills
        multiplier += data.getHealthRegenMultiplierFromSkills();

        // Add regen multipliers from effects
        multiplier += getHealthRegenMultiplierFromEffects(player);

        return Math.max(0.1, multiplier);
    }

    private double getManaRegenMultiplierFromEffects(Player player) {
        // Check for mana regen multiplier effects
        return 0.0;
    }

    private double getHealthRegenMultiplierFromEffects(Player player) {
        // Check for health regen multiplier effects
        return 0.0;
    }

    private double calculateActualDamage(Player player, double baseDamage, PlayerHealthManaData data) {
        double damage = baseDamage;

        // Apply defense reduction
        double defense = getDefense(player, data);
        damage = damage * (1.0 - (defense / (defense + 100.0)));

        // Apply absorption hearts
        double absorption = getAbsorptionHearts(player, data);
        if (absorption > 0) {
            double absorbed = Math.min(damage, absorption);
            damage -= absorbed;
            setAbsorptionHearts(player, data, absorption - absorbed);
        }

        return Math.max(0.0, damage);
    }

    private double getDefense(Player player, PlayerHealthManaData data) {
        double defense = 0.0;

        // Add defense from skills
        defense += data.getDefenseFromSkills();

        // Add defense from armor
        defense += getDefenseFromArmor(player);

        // Add defense from accessories
        defense += getDefenseFromAccessories(player);

        return defense;
    }

    private double getDefenseFromArmor(Player player) {
        double defense = 0.0;

        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null || armor.getType() == Material.AIR) continue;

            // Check for custom armor with defense stats
            var meta = armor.getItemMeta();
            if (meta != null && meta.hasLore()) {
                List<Component> lore = meta.lore();
                if (lore == null) continue;
                for (Component line : lore) {
                    String lineText = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(line);
                    if (lineText.contains("❈ Defense")) {
                        try {
                            String[] parts = lineText.split(" ");
                            defense += Double.parseDouble(parts[0].replace("+", "").replace("❈", ""));
                        } catch (Exception ignored) {}
                    }
                }
            }
        }

        return defense;
    }

    private double getDefenseFromAccessories(Player player) {
        // This would integrate with the accessory system
        return 0.0;
    }

    private double getAbsorptionHearts(Player player, PlayerHealthManaData data) {
        return data.getAbsorptionHearts();
    }

    private void setAbsorptionHearts(Player player, PlayerHealthManaData data, double absorption) {
        data.setAbsorptionHearts(Math.max(0.0, absorption));

        // Update Minecraft absorption
        if (absorption > 0) {
            player.setAbsorptionAmount((float) absorption);
        } else {
            player.setAbsorptionAmount(0.0f);
        }
    }

    private boolean isInCombat(Player player) {
        // Check if player is in combat (could be based on last damage taken)
        return false; // Simplified for now
    }

    private void updateHealthManaDisplay(Player player) {
        UUID playerId = player.getUniqueId();
        PlayerHealthManaData data = playerData.get(playerId);

        if (data == null) return;

        // Create action bar message
        String healthBar = createHealthBar(data.getCurrentHealth(), data.getMaxHealth());
        String manaBar = createManaBar(data.getCurrentMana(), data.getMaxMana());

        String message = "§c" + healthBar + " §7| §b" + manaBar;

        // Send action bar
        player.sendActionBar(Component.text(message));
    }

    private String createHealthBar(double current, double max) {
        int filled = (int) ((current / max) * 10);
        int empty = 10 - filled;

        StringBuilder bar = new StringBuilder();
        bar.append("❤ ");

        for (int i = 0; i < filled; i++) {
            bar.append("§c█");
        }
        for (int i = 0; i < empty; i++) {
            bar.append("§7█");
        }

        bar.append(" §f").append(String.format("%.0f/%.0f", current, max));

        return bar.toString();
    }

    private String createManaBar(double current, double max) {
        int filled = (int) ((current / max) * 10);
        int empty = 10 - filled;

        StringBuilder bar = new StringBuilder();
        bar.append("✎ ");

        for (int i = 0; i < filled; i++) {
            bar.append("§b█");
        }
        for (int i = 0; i < empty; i++) {
            bar.append("§7█");
        }

        bar.append(" §f").append(String.format("%.0f/%.0f", current, max));

        return bar.toString();
    }

    private void updateHealthDisplay(Player player) {
        UUID playerId = player.getUniqueId();
        PlayerHealthManaData data = playerData.get(playerId);

        if (data == null) return;

        // Update Minecraft health bar
        player.setHealth(Math.min(20.0, (data.getCurrentHealth() / data.getMaxHealth()) * 20.0));
    }

    private void handlePlayerDeath(Player player, PlayerHealthManaData data) {
        // Reset health and mana
        data.setCurrentHealth(data.getMaxHealth() * 0.5); // Respawn with 50% health
        data.setCurrentMana(data.getMaxMana() * 0.25); // Respawn with 25% mana

        // Send death message
        player.sendMessage("§c§lYou died! §7Respawned with reduced health and mana.");

        // Could trigger respawn system here
    }

    // Public API methods
    public boolean consumeMana(Player player, double amount) {
        UUID playerId = player.getUniqueId();
        PlayerHealthManaData data = playerData.get(playerId);

        if (data == null || data.getCurrentMana() < amount) {
            return false;
        }

        data.setCurrentMana(data.getCurrentMana() - amount);
        updateHealthManaDisplay(player);
        return true;
    }

    public void addMana(Player player, double amount) {
        UUID playerId = player.getUniqueId();
        PlayerHealthManaData data = playerData.get(playerId);

        if (data == null) return;

        data.setCurrentMana(Math.min(data.getMaxMana(), data.getCurrentMana() + amount));
        updateHealthManaDisplay(player);
    }

    public void addHealth(Player player, double amount) {
        UUID playerId = player.getUniqueId();
        PlayerHealthManaData data = playerData.get(playerId);

        if (data == null) return;

        data.setCurrentHealth(Math.min(data.getMaxHealth(), data.getCurrentHealth() + amount));
        updateHealthDisplay(player);
        updateHealthManaDisplay(player);
    }

    public void setAbsorptionHearts(Player player, double amount) {
        UUID playerId = player.getUniqueId();
        PlayerHealthManaData data = playerData.get(playerId);

        if (data == null) return;

        setAbsorptionHearts(player, data, amount);
    }

    public double getCurrentHealth(Player player) {
        UUID playerId = player.getUniqueId();
        PlayerHealthManaData data = playerData.get(playerId);
        return data != null ? data.getCurrentHealth() : 0.0;
    }

    public double getMaxHealth(Player player) {
        UUID playerId = player.getUniqueId();
        PlayerHealthManaData data = playerData.get(playerId);
        return data != null ? data.getMaxHealth() : BASE_HEALTH;
    }

    public double getCurrentMana(Player player) {
        UUID playerId = player.getUniqueId();
        PlayerHealthManaData data = playerData.get(playerId);
        return data != null ? data.getCurrentMana() : 0.0;
    }

    public double getMaxMana(Player player) {
        UUID playerId = player.getUniqueId();
        PlayerHealthManaData data = playerData.get(playerId);
        return data != null ? data.getMaxMana() : BASE_MANA;
    }

    public PlayerHealthManaData getPlayerData(UUID playerId) {
        return playerData.computeIfAbsent(playerId, this::loadPlayerData);
    }

    private PlayerHealthManaData loadPlayerData(UUID playerId) {
        // Load from database or create new
        PlayerHealthManaData data = new PlayerHealthManaData(playerId);

        // Try file-based storage as a fallback for persistence
        try {
            File dir = new File(plugin.getDataFolder(), "healthmana");
            if (!dir.exists()) dir.mkdirs();
            File f = new File(dir, playerId.toString() + ".yml");
            if (f.exists()) {
                YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
                data.setCurrentHealth(cfg.getDouble("currentHealth", BASE_HEALTH));
                data.setMaxHealth(cfg.getDouble("maxHealth", BASE_HEALTH));
                data.setCurrentMana(cfg.getDouble("currentMana", BASE_MANA));
                data.setMaxMana(cfg.getDouble("maxMana", BASE_MANA));
                data.setAbsorptionHearts(cfg.getDouble("absorptionHearts", 0.0));
                data.setHealthFromSkills(cfg.getDouble("healthFromSkills", 0.0));
                data.setManaFromSkills(cfg.getDouble("manaFromSkills", 0.0));
                data.setDefenseFromSkills(cfg.getDouble("defenseFromSkills", 0.0));
                data.setHealthMultiplierFromSkills(cfg.getDouble("healthMultiplierFromSkills", 0.0));
                data.setManaMultiplierFromSkills(cfg.getDouble("manaMultiplierFromSkills", 0.0));
                data.setHealthRegenMultiplierFromSkills(cfg.getDouble("healthRegenMultiplierFromSkills", 0.0));
                data.setManaRegenMultiplierFromSkills(cfg.getDouble("manaRegenMultiplierFromSkills", 0.0));
                plugin.getLogger().fine("Loaded HealthMana data for " + playerId);
            } else {
                // No file found, keep defaults
            }
        } catch (Exception ex) {
            plugin.getLogger().warning("Failed to load HealthMana data for " + playerId + ": " + ex.getMessage());
        }

        return data;
    }

    private void savePlayerData(UUID playerId) {
        PlayerHealthManaData data = playerData.get(playerId);
        if (data == null) return;

        try {
            File dir = new File(plugin.getDataFolder(), "healthmana");
            if (!dir.exists()) dir.mkdirs();
            File f = new File(dir, playerId.toString() + ".yml");
            YamlConfiguration cfg = new YamlConfiguration();
            cfg.set("currentHealth", data.getCurrentHealth());
            cfg.set("maxHealth", data.getMaxHealth());
            cfg.set("currentMana", data.getCurrentMana());
            cfg.set("maxMana", data.getMaxMana());
            cfg.set("absorptionHearts", data.getAbsorptionHearts());
            cfg.set("healthFromSkills", data.getHealthFromSkills());
            cfg.set("manaFromSkills", data.getManaFromSkills());
            cfg.set("defenseFromSkills", data.getDefenseFromSkills());
            cfg.set("healthMultiplierFromSkills", data.getHealthMultiplierFromSkills());
            cfg.set("manaMultiplierFromSkills", data.getManaMultiplierFromSkills());
            cfg.set("healthRegenMultiplierFromSkills", data.getHealthRegenMultiplierFromSkills());
            cfg.set("manaRegenMultiplierFromSkills", data.getManaRegenMultiplierFromSkills());
            cfg.save(f);
            plugin.getLogger().fine("Saved HealthMana data for " + playerId);
        } catch (Exception ex) {
            plugin.getLogger().warning("Failed to save HealthMana data for " + playerId + ": " + ex.getMessage());
        }
    }

    /**
     * Player Health and Mana Data class
     */
    public static class PlayerHealthManaData {
        private final UUID playerId;
        private double currentHealth;
        private double maxHealth;
        private double currentMana;
        private double maxMana;
        private double absorptionHearts;

        // Stats from skills
        private double healthFromSkills;
        private double manaFromSkills;
        private double defenseFromSkills;
        private double healthMultiplierFromSkills;
        private double manaMultiplierFromSkills;
        private double healthRegenMultiplierFromSkills;
        private double manaRegenMultiplierFromSkills;

        public PlayerHealthManaData(UUID playerId) {
            this.playerId = playerId;
            this.currentHealth = BASE_HEALTH;
            this.maxHealth = BASE_HEALTH;
            this.currentMana = BASE_MANA;
            this.maxMana = BASE_MANA;
            this.absorptionHearts = 0.0;

            // Initialize skill stats
            this.healthFromSkills = 0.0;
            this.manaFromSkills = 0.0;
            this.defenseFromSkills = 0.0;
            this.healthMultiplierFromSkills = 0.0;
            this.manaMultiplierFromSkills = 0.0;
            this.healthRegenMultiplierFromSkills = 0.0;
            this.manaRegenMultiplierFromSkills = 0.0;
        }

        // Getters and setters
        public UUID getPlayerId() { return playerId; }
        public double getCurrentHealth() { return currentHealth; }
        public void setCurrentHealth(double currentHealth) { this.currentHealth = currentHealth; }
        public double getMaxHealth() { return maxHealth; }
        public void setMaxHealth(double maxHealth) { this.maxHealth = maxHealth; }
        public double getCurrentMana() { return currentMana; }
        public void setCurrentMana(double currentMana) { this.currentMana = currentMana; }
        public double getMaxMana() { return maxMana; }
        public void setMaxMana(double maxMana) { this.maxMana = maxMana; }
        public double getAbsorptionHearts() { return absorptionHearts; }
        public void setAbsorptionHearts(double absorptionHearts) { this.absorptionHearts = absorptionHearts; }

        public double getHealthFromSkills() { return healthFromSkills; }
        public void setHealthFromSkills(double healthFromSkills) { this.healthFromSkills = healthFromSkills; }
        public double getManaFromSkills() { return manaFromSkills; }
        public void setManaFromSkills(double manaFromSkills) { this.manaFromSkills = manaFromSkills; }
        public double getDefenseFromSkills() { return defenseFromSkills; }
        public void setDefenseFromSkills(double defenseFromSkills) { this.defenseFromSkills = defenseFromSkills; }
        public double getHealthMultiplierFromSkills() { return healthMultiplierFromSkills; }
        public void setHealthMultiplierFromSkills(double healthMultiplierFromSkills) { this.healthMultiplierFromSkills = healthMultiplierFromSkills; }
        public double getManaMultiplierFromSkills() { return manaMultiplierFromSkills; }
        public void setManaMultiplierFromSkills(double manaMultiplierFromSkills) { this.manaMultiplierFromSkills = manaMultiplierFromSkills; }
        public double getHealthRegenMultiplierFromSkills() { return healthRegenMultiplierFromSkills; }
        public void setHealthRegenMultiplierFromSkills(double healthRegenMultiplierFromSkills) { this.healthRegenMultiplierFromSkills = healthRegenMultiplierFromSkills; }
        public double getManaRegenMultiplierFromSkills() { return manaRegenMultiplierFromSkills; }
        public void setManaRegenMultiplierFromSkills(double manaRegenMultiplierFromSkills) { this.manaRegenMultiplierFromSkills = manaRegenMultiplierFromSkills; }
    }
    
    public void initializePlayer(Player player) {
        // Initialize player health and mana
        double maxHealth = 20.0; // Default Minecraft health
        if (player instanceof org.bukkit.entity.LivingEntity) {
            org.bukkit.entity.LivingEntity livingEntity = (org.bukkit.entity.LivingEntity) player;
            maxHealth = livingEntity.getAttribute(Attribute.MAX_HEALTH).getBaseValue();
        }
        player.setHealth(maxHealth);
    }
}
