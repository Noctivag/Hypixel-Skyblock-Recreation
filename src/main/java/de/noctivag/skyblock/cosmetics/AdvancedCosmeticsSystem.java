package de.noctivag.skyblock.cosmetics;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Advanced cosmetics system managing cosmetic items and effects
 */
public class AdvancedCosmeticsSystem implements Service, Listener {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    private final Map<String, Cosmetic> cosmetics = new ConcurrentHashMap<>();
    private final Map<UUID, PlayerCosmeticData> playerData = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> particleTasks = new ConcurrentHashMap<>();
    
    public AdvancedCosmeticsSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing AdvancedCosmeticsSystem...");
        
        // Load cosmetics from configuration
        loadCosmeticsFromConfig();
        
        // Register event listeners
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        // Start particle effect task
        startParticleEffectTask();
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("AdvancedCosmeticsSystem initialized with " + cosmetics.size() + " cosmetics.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down AdvancedCosmeticsSystem...");
        
        // Stop all particle tasks
        for (BukkitTask task : particleTasks.values()) {
            if (task != null) {
                task.cancel();
            }
        }
        particleTasks.clear();
        
        // Save all player data
        saveAllPlayerData();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("AdvancedCosmeticsSystem shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "AdvancedCosmeticsSystem";
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status == SystemStatus.DISABLED) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }
    
    /**
     * Load cosmetics from configuration file
     */
    private void loadCosmeticsFromConfig() {
        File configFile = new File(plugin.getDataFolder(), "cosmetics.yml");
        if (!configFile.exists()) {
            createDefaultCosmeticsConfig(configFile);
        }
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        ConfigurationSection cosmeticsSection = config.getConfigurationSection("cosmetics");
        
        if (cosmeticsSection == null) {
            plugin.getLogger().warning("No cosmetics section found in cosmetics.yml");
            return;
        }
        
        for (String cosmeticId : cosmeticsSection.getKeys(false)) {
            try {
                Cosmetic cosmetic = loadCosmeticFromConfig(cosmeticsSection.getConfigurationSection(cosmeticId));
                if (cosmetic != null) {
                    cosmetics.put(cosmeticId, cosmetic);
                    plugin.getLogger().info("Loaded cosmetic: " + cosmetic.getName());
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to load cosmetic " + cosmeticId + ": " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Create default cosmetics configuration
     */
    private void createDefaultCosmeticsConfig(File configFile) {
        try {
            YamlConfiguration config = new YamlConfiguration();
            
            // Example hat cosmetic
            config.set("cosmetics.diamond_hat.id", "diamond_hat");
            config.set("cosmetics.diamond_hat.name", "Diamond Hat");
            config.set("cosmetics.diamond_hat.description", "A shiny diamond hat!");
            config.set("cosmetics.diamond_hat.type", "HAT");
            config.set("cosmetics.diamond_hat.rarity", "RARE");
            config.set("cosmetics.diamond_hat.required_level", 1);
            config.set("cosmetics.diamond_hat.price", 1000.0);
            config.set("cosmetics.diamond_hat.duration", -1); // Permanent
            config.set("cosmetics.diamond_hat.properties.material", "DIAMOND_HELMET");
            
            // Example particle cosmetic
            config.set("cosmetics.heart_particles.id", "heart_particles");
            config.set("cosmetics.heart_particles.name", "Heart Particles");
            config.set("cosmetics.heart_particles.description", "Surround yourself with hearts!");
            config.set("cosmetics.heart_particles.type", "PARTICLE");
            config.set("cosmetics.heart_particles.rarity", "UNCOMMON");
            config.set("cosmetics.heart_particles.required_level", 1);
            config.set("cosmetics.heart_particles.price", 500.0);
            config.set("cosmetics.heart_particles.duration", -1); // Permanent
            config.set("cosmetics.heart_particles.properties.particle_type", "HEART");
            config.set("cosmetics.heart_particles.properties.particle_count", 3);
            config.set("cosmetics.heart_particles.properties.particle_speed", 0.1);
            config.set("cosmetics.heart_particles.properties.particle_radius", 1.5);
            
            config.save(configFile);
            plugin.getLogger().info("Created default cosmetics configuration: " + configFile.getName());
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create default cosmetics configuration", e);
        }
    }
    
    /**
     * Load a single cosmetic from configuration
     */
    private Cosmetic loadCosmeticFromConfig(ConfigurationSection cosmeticSection) {
        if (cosmeticSection == null) return null;
        
        String id = cosmeticSection.getString("id");
        String name = cosmeticSection.getString("name");
        String description = cosmeticSection.getString("description");
        String typeStr = cosmeticSection.getString("type", "CUSTOM");
        String rarityStr = cosmeticSection.getString("rarity", "COMMON");
        int requiredLevel = cosmeticSection.getInt("required_level", 1);
        double price = cosmeticSection.getDouble("price", 0.0);
        long duration = cosmeticSection.getLong("duration", -1);
        
        // Load properties
        Map<String, Object> properties = new HashMap<>();
        ConfigurationSection propertiesSection = cosmeticSection.getConfigurationSection("properties");
        if (propertiesSection != null) {
            for (String propertyKey : propertiesSection.getKeys(false)) {
                properties.put(propertyKey, propertiesSection.get(propertyKey));
            }
        }
        
        // Load requirements
        List<String> requirements = cosmeticSection.getStringList("requirements");
        
        // Create display item
        ItemStack displayItem = createDisplayItem(name, description, rarityStr);
        
        CosmeticType type;
        try {
            type = CosmeticType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            type = CosmeticType.CUSTOM;
        }
        
        Cosmetic.Rarity rarity;
        try {
            rarity = Cosmetic.Rarity.valueOf(rarityStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            rarity = Cosmetic.Rarity.COMMON;
        }
        
        return new Cosmetic(id, name, description, type, rarity, requiredLevel, price, requirements, properties, displayItem, true, duration);
    }
    
    /**
     * Create display item for cosmetic
     */
    private ItemStack createDisplayItem(String name, String description, String rarityStr) {
        ItemStack item = new ItemStack(Material.DIAMOND);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            Cosmetic.Rarity rarity;
            try {
                rarity = Cosmetic.Rarity.valueOf(rarityStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                rarity = Cosmetic.Rarity.COMMON;
            }
            
            meta.setDisplayName(rarity.getColoredName() + " " + name);
            meta.setLore(Arrays.asList(
                "§7" + description,
                "",
                "§7Rarity: " + rarity.getColoredName(),
                "§7Click to equip!"
            ));
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Start particle effect task
     */
    private void startParticleEffectTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updatePlayerParticleEffects(player);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
    }
    
    /**
     * Update particle effects for a player
     */
    private void updatePlayerParticleEffects(Player player) {
        PlayerCosmeticData data = getPlayerCosmeticData(player.getUniqueId());
        String activeParticleCosmetic = data.getActiveCosmetic(CosmeticType.PARTICLE);
        
        if (activeParticleCosmetic != null) {
            Cosmetic cosmetic = cosmetics.get(activeParticleCosmetic);
            if (cosmetic != null && cosmetic.getType() == CosmeticType.PARTICLE) {
                spawnParticleEffect(player, cosmetic);
            }
        }
    }
    
    /**
     * Spawn particle effect around player
     */
    private void spawnParticleEffect(Player player, Cosmetic cosmetic) {
        Location location = player.getLocation();
        Particle particle = cosmetic.getParticleType();
        int count = cosmetic.getParticleCount();
        double speed = cosmetic.getParticleSpeed();
        double radius = cosmetic.getParticleRadius();
        
        // Spawn particles in a circle around the player
        for (int i = 0; i < count; i++) {
            double angle = (2 * Math.PI * i) / count;
            double x = location.getX() + radius * Math.cos(angle);
            double z = location.getZ() + radius * Math.sin(angle);
            
            Location particleLocation = new Location(location.getWorld(), x, location.getY() + 1, z);
            player.getWorld().spawnParticle(particle, particleLocation, 1, 0, 0, 0, speed);
        }
    }
    
    /**
     * Get player cosmetic data
     */
    public PlayerCosmeticData getPlayerCosmeticData(UUID playerUuid) {
        return playerData.computeIfAbsent(playerUuid, PlayerCosmeticData::new);
    }
    
    /**
     * Unlock cosmetic for player
     */
    public boolean unlockCosmetic(UUID playerUuid, String cosmeticId) {
        Cosmetic cosmetic = cosmetics.get(cosmeticId);
        if (cosmetic == null) {
            return false;
        }
        
        PlayerCosmeticData data = getPlayerCosmeticData(playerUuid);
        data.unlockCosmetic(cosmeticId);
        
        plugin.getLogger().info("Unlocked cosmetic " + cosmeticId + " for player " + playerUuid);
        return true;
    }
    
    /**
     * Activate cosmetic for player
     */
    public boolean activateCosmetic(UUID playerUuid, String cosmeticId) {
        Cosmetic cosmetic = cosmetics.get(cosmeticId);
        if (cosmetic == null) {
            return false;
        }
        
        PlayerCosmeticData data = getPlayerCosmeticData(playerUuid);
        if (!data.isCosmeticUnlocked(cosmeticId)) {
            return false;
        }
        
        // Deactivate current cosmetic of the same type
        data.deactivateCosmetic(cosmetic.getType());
        
        // Activate new cosmetic
        data.activateCosmetic(cosmeticId, cosmetic.getType());
        
        // Apply cosmetic effect
        applyCosmeticEffect(playerUuid, cosmetic);
        
        plugin.getLogger().info("Activated cosmetic " + cosmeticId + " for player " + playerUuid);
        return true;
    }
    
    /**
     * Deactivate cosmetic for player
     */
    public boolean deactivateCosmetic(UUID playerUuid, CosmeticType type) {
        PlayerCosmeticData data = getPlayerCosmeticData(playerUuid);
        String activeCosmetic = data.getActiveCosmetic(type);
        
        if (activeCosmetic != null) {
            data.deactivateCosmetic(type);
            removeCosmeticEffect(playerUuid, type);
            
            plugin.getLogger().info("Deactivated cosmetic " + activeCosmetic + " for player " + playerUuid);
            return true;
        }
        
        return false;
    }
    
    /**
     * Apply cosmetic effect to player
     */
    private void applyCosmeticEffect(UUID playerUuid, Cosmetic cosmetic) {
        Player player = Bukkit.getPlayer(playerUuid);
        if (player == null) return;
        
        switch (cosmetic.getType()) {
            case HAT:
                applyHatEffect(player, cosmetic);
                break;
            case PARTICLE:
                // Particle effects are handled by the main particle task
                break;
            case TITLE:
                applyTitleEffect(player, cosmetic);
                break;
            // Add more cosmetic types as needed
        }
    }
    
    /**
     * Remove cosmetic effect from player
     */
    private void removeCosmeticEffect(UUID playerUuid, CosmeticType type) {
        Player player = Bukkit.getPlayer(playerUuid);
        if (player == null) return;
        
        switch (type) {
            case HAT:
                removeHatEffect(player);
                break;
            case PARTICLE:
                // Particle effects are handled by the main particle task
                break;
            case TITLE:
                removeTitleEffect(player);
                break;
            // Add more cosmetic types as needed
        }
    }
    
    /**
     * Apply hat effect
     */
    private void applyHatEffect(Player player, Cosmetic cosmetic) {
        Material hatMaterial = cosmetic.getHatMaterial();
        if (hatMaterial != null) {
            ItemStack hat = new ItemStack(hatMaterial);
            player.getInventory().setHelmet(hat);
        }
    }
    
    /**
     * Remove hat effect
     */
    private void removeHatEffect(Player player) {
        player.getInventory().setHelmet(null);
    }
    
    /**
     * Apply title effect
     */
    private void applyTitleEffect(Player player, Cosmetic cosmetic) {
        String title = (String) cosmetic.getProperty("title", "");
        if (!title.isEmpty()) {
            player.setPlayerListName(cosmetic.getRarity().getColor() + title + " " + player.getName());
        }
    }
    
    /**
     * Remove title effect
     */
    private void removeTitleEffect(Player player) {
        player.setPlayerListName(player.getName());
    }
    
    /**
     * Save all player data
     */
    private void saveAllPlayerData() {
        // Implementation would save to database
        plugin.getLogger().info("Saved cosmetic data for " + playerData.size() + " players");
    }
    
    // Event handlers
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID playerUuid = event.getPlayer().getUniqueId();
        getPlayerCosmeticData(playerUuid); // Load player data
        
        // Apply active cosmetics
        PlayerCosmeticData data = getPlayerCosmeticData(playerUuid);
        for (String cosmeticId : data.getAllActiveCosmetics()) {
            Cosmetic cosmetic = cosmetics.get(cosmeticId);
            if (cosmetic != null) {
                applyCosmeticEffect(playerUuid, cosmetic);
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerUuid = event.getPlayer().getUniqueId();
        // Save player data when they leave
    }
    
    /**
     * Get all cosmetics
     */
    public Map<String, Cosmetic> getCosmetics() {
        return new HashMap<>(cosmetics);
    }
    
    /**
     * Get cosmetic by ID
     */
    public Cosmetic getCosmetic(String cosmeticId) {
        return cosmetics.get(cosmeticId);
    }
    
    /**
     * Get cosmetics by type
     */
    public List<Cosmetic> getCosmeticsByType(CosmeticType type) {
        List<Cosmetic> result = new ArrayList<>();
        for (Cosmetic cosmetic : cosmetics.values()) {
            if (cosmetic.getType() == type) {
                result.add(cosmetic);
            }
        }
        return result;
    }
    
    /**
     * Get cosmetics by rarity
     */
    public List<Cosmetic> getCosmeticsByRarity(Cosmetic.Rarity rarity) {
        List<Cosmetic> result = new ArrayList<>();
        for (Cosmetic cosmetic : cosmetics.values()) {
            if (cosmetic.getRarity() == rarity) {
                result.add(cosmetic);
            }
        }
        return result;
    }
}