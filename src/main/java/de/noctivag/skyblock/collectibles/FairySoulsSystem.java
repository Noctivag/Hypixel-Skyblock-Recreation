package de.noctivag.skyblock.collectibles;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * Fairy Souls System - Hypixel Skyblock Style
 */
public class FairySoulsSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerFairySouls> playerFairySouls = new ConcurrentHashMap<>();
    private final Map<FairySoulType, FairySoulConfig> fairySoulConfigs = new HashMap<>();

    public FairySoulsSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeFairySoulConfigs();
        startFairySoulUpdateTask();

        // Register events
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }

    private void initializeFairySoulConfigs() {
        // Common Fairy Souls
        fairySoulConfigs.put(FairySoulType.COMMON_HEALTH, new FairySoulConfig(
            "Common Health Fairy Soul", "§fCommon Health Fairy Soul", Material.HEART_OF_THE_SEA,
            "§7A common fairy soul that provides health.",
            FairySoulRarity.COMMON, FairySoulCategory.HEALTH, 1, 0.1, Collections.singletonList("§7+1 Health"),
            Arrays.asList("§7- 1x Heart of the Sea", "§7- 1x Prismarine Shard")
        ));

        fairySoulConfigs.put(FairySoulType.COMMON_DEFENSE, new FairySoulConfig(
            "Common Defense Fairy Soul", "§fCommon Defense Fairy Soul", Material.SHIELD,
            "§7A common fairy soul that provides defense.",
            FairySoulRarity.COMMON, FairySoulCategory.DEFENSE, 1, 0.1, Collections.singletonList("§7+1 Defense"),
            Arrays.asList("§7- 1x Shield", "§7- 1x Iron Ingot")
        ));

        fairySoulConfigs.put(FairySoulType.COMMON_STRENGTH, new FairySoulConfig(
            "Common Strength Fairy Soul", "§fCommon Strength Fairy Soul", Material.DIAMOND_SWORD,
            "§7A common fairy soul that provides strength.",
            FairySoulRarity.COMMON, FairySoulCategory.STRENGTH, 1, 0.1, Collections.singletonList("§7+1 Strength"),
            Arrays.asList("§7- 1x Diamond Sword", "§7- 1x Diamond")
        ));

        fairySoulConfigs.put(FairySoulType.COMMON_SPEED, new FairySoulConfig(
            "Common Speed Fairy Soul", "§fCommon Speed Fairy Soul", Material.FEATHER,
            "§7A common fairy soul that provides speed.",
            FairySoulRarity.COMMON, FairySoulCategory.SPEED, 1, 0.1, Collections.singletonList("§7+1 Speed"),
            Arrays.asList("§7- 1x Feather", "§7- 1x String")
        ));

        fairySoulConfigs.put(FairySoulType.COMMON_INTELLIGENCE, new FairySoulConfig(
            "Common Intelligence Fairy Soul", "§fCommon Intelligence Fairy Soul", Material.ENCHANTING_TABLE,
            "§7A common fairy soul that provides intelligence.",
            FairySoulRarity.COMMON, FairySoulCategory.INTELLIGENCE, 1, 0.1, Collections.singletonList("§7+1 Intelligence"),
            Arrays.asList("§7- 1x Enchanting Table", "§7- 1x Lapis Lazuli")
        ));

        // Uncommon Fairy Souls
        fairySoulConfigs.put(FairySoulType.UNCOMMON_HEALTH, new FairySoulConfig(
            "Uncommon Health Fairy Soul", "§aUncommon Health Fairy Soul", Material.HEART_OF_THE_SEA,
            "§7An uncommon fairy soul that provides health.",
            FairySoulRarity.UNCOMMON, FairySoulCategory.HEALTH, 2, 0.2, Collections.singletonList("§7+2 Health"),
            Arrays.asList("§7- 2x Heart of the Sea", "§7- 2x Prismarine Shard", "§7- 1x Gold Ingot")
        ));

        fairySoulConfigs.put(FairySoulType.UNCOMMON_DEFENSE, new FairySoulConfig(
            "Uncommon Defense Fairy Soul", "§aUncommon Defense Fairy Soul", Material.SHIELD,
            "§7An uncommon fairy soul that provides defense.",
            FairySoulRarity.UNCOMMON, FairySoulCategory.DEFENSE, 2, 0.2, Collections.singletonList("§7+2 Defense"),
            Arrays.asList("§7- 2x Shield", "§7- 2x Iron Ingot", "§7- 1x Gold Ingot")
        ));

        fairySoulConfigs.put(FairySoulType.UNCOMMON_STRENGTH, new FairySoulConfig(
            "Uncommon Strength Fairy Soul", "§aUncommon Strength Fairy Soul", Material.DIAMOND_SWORD,
            "§7An uncommon fairy soul that provides strength.",
            FairySoulRarity.UNCOMMON, FairySoulCategory.STRENGTH, 2, 0.2, Collections.singletonList("§7+2 Strength"),
            Arrays.asList("§7- 2x Diamond Sword", "§7- 2x Diamond", "§7- 1x Gold Ingot")
        ));

        fairySoulConfigs.put(FairySoulType.UNCOMMON_SPEED, new FairySoulConfig(
            "Uncommon Speed Fairy Soul", "§aUncommon Speed Fairy Soul", Material.FEATHER,
            "§7An uncommon fairy soul that provides speed.",
            FairySoulRarity.UNCOMMON, FairySoulCategory.SPEED, 2, 0.2, Collections.singletonList("§7+2 Speed"),
            Arrays.asList("§7- 2x Feather", "§7- 2x String", "§7- 1x Gold Ingot")
        ));

        fairySoulConfigs.put(FairySoulType.UNCOMMON_INTELLIGENCE, new FairySoulConfig(
            "Uncommon Intelligence Fairy Soul", "§aUncommon Intelligence Fairy Soul", Material.ENCHANTING_TABLE,
            "§7An uncommon fairy soul that provides intelligence.",
            FairySoulRarity.UNCOMMON, FairySoulCategory.INTELLIGENCE, 2, 0.2, Collections.singletonList("§7+2 Intelligence"),
            Arrays.asList("§7- 2x Enchanting Table", "§7- 2x Lapis Lazuli", "§7- 1x Gold Ingot")
        ));

        // Rare Fairy Souls
        fairySoulConfigs.put(FairySoulType.RARE_HEALTH, new FairySoulConfig(
            "Rare Health Fairy Soul", "§9Rare Health Fairy Soul", Material.HEART_OF_THE_SEA,
            "§7A rare fairy soul that provides health.",
            FairySoulRarity.RARE, FairySoulCategory.HEALTH, 3, 0.3, Collections.singletonList("§7+3 Health"),
            Arrays.asList("§7- 3x Heart of the Sea", "§7- 3x Prismarine Shard", "§7- 1x Emerald")
        ));

        fairySoulConfigs.put(FairySoulType.RARE_DEFENSE, new FairySoulConfig(
            "Rare Defense Fairy Soul", "§9Rare Defense Fairy Soul", Material.SHIELD,
            "§7A rare fairy soul that provides defense.",
            FairySoulRarity.RARE, FairySoulCategory.DEFENSE, 3, 0.3, Collections.singletonList("§7+3 Defense"),
            Arrays.asList("§7- 3x Shield", "§7- 3x Iron Ingot", "§7- 1x Emerald")
        ));

        fairySoulConfigs.put(FairySoulType.RARE_STRENGTH, new FairySoulConfig(
            "Rare Strength Fairy Soul", "§9Rare Strength Fairy Soul", Material.DIAMOND_SWORD,
            "§7A rare fairy soul that provides strength.",
            FairySoulRarity.RARE, FairySoulCategory.STRENGTH, 3, 0.3, Collections.singletonList("§7+3 Strength"),
            Arrays.asList("§7- 3x Diamond Sword", "§7- 3x Diamond", "§7- 1x Emerald")
        ));

        fairySoulConfigs.put(FairySoulType.RARE_SPEED, new FairySoulConfig(
            "Rare Speed Fairy Soul", "§9Rare Speed Fairy Soul", Material.FEATHER,
            "§7A rare fairy soul that provides speed.",
            FairySoulRarity.RARE, FairySoulCategory.SPEED, 3, 0.3, Collections.singletonList("§7+3 Speed"),
            Arrays.asList("§7- 3x Feather", "§7- 3x String", "§7- 1x Emerald")
        ));

        fairySoulConfigs.put(FairySoulType.RARE_INTELLIGENCE, new FairySoulConfig(
            "Rare Intelligence Fairy Soul", "§9Rare Intelligence Fairy Soul", Material.ENCHANTING_TABLE,
            "§7A rare fairy soul that provides intelligence.",
            FairySoulRarity.RARE, FairySoulCategory.INTELLIGENCE, 3, 0.3, Collections.singletonList("§7+3 Intelligence"),
            Arrays.asList("§7- 3x Enchanting Table", "§7- 3x Lapis Lazuli", "§7- 1x Emerald")
        ));

        // Epic Fairy Souls
        fairySoulConfigs.put(FairySoulType.EPIC_HEALTH, new FairySoulConfig(
            "Epic Health Fairy Soul", "§5Epic Health Fairy Soul", Material.HEART_OF_THE_SEA,
            "§7An epic fairy soul that provides health.",
            FairySoulRarity.EPIC, FairySoulCategory.HEALTH, 4, 0.4, Collections.singletonList("§7+4 Health"),
            Arrays.asList("§7- 4x Heart of the Sea", "§7- 4x Prismarine Shard", "§7- 1x Diamond")
        ));

        fairySoulConfigs.put(FairySoulType.EPIC_DEFENSE, new FairySoulConfig(
            "Epic Defense Fairy Soul", "§5Epic Defense Fairy Soul", Material.SHIELD,
            "§7An epic fairy soul that provides defense.",
            FairySoulRarity.EPIC, FairySoulCategory.DEFENSE, 4, 0.4, Collections.singletonList("§7+4 Defense"),
            Arrays.asList("§7- 4x Shield", "§7- 4x Iron Ingot", "§7- 1x Diamond")
        ));

        fairySoulConfigs.put(FairySoulType.EPIC_STRENGTH, new FairySoulConfig(
            "Epic Strength Fairy Soul", "§5Epic Strength Fairy Soul", Material.DIAMOND_SWORD,
            "§7An epic fairy soul that provides strength.",
            FairySoulRarity.EPIC, FairySoulCategory.STRENGTH, 4, 0.4, Collections.singletonList("§7+4 Strength"),
            Arrays.asList("§7- 4x Diamond Sword", "§7- 4x Diamond", "§7- 1x Emerald")
        ));

        fairySoulConfigs.put(FairySoulType.EPIC_SPEED, new FairySoulConfig(
            "Epic Speed Fairy Soul", "§5Epic Speed Fairy Soul", Material.FEATHER,
            "§7An epic fairy soul that provides speed.",
            FairySoulRarity.EPIC, FairySoulCategory.SPEED, 4, 0.4, Collections.singletonList("§7+4 Speed"),
            Arrays.asList("§7- 4x Feather", "§7- 4x String", "§7- 1x Diamond")
        ));

        fairySoulConfigs.put(FairySoulType.EPIC_INTELLIGENCE, new FairySoulConfig(
            "Epic Intelligence Fairy Soul", "§5Epic Intelligence Fairy Soul", Material.ENCHANTING_TABLE,
            "§7An epic fairy soul that provides intelligence.",
            FairySoulRarity.EPIC, FairySoulCategory.INTELLIGENCE, 4, 0.4, Collections.singletonList("§7+4 Intelligence"),
            Arrays.asList("§7- 4x Enchanting Table", "§7- 4x Lapis Lazuli", "§7- 1x Diamond")
        ));

        // Legendary Fairy Souls
        fairySoulConfigs.put(FairySoulType.LEGENDARY_HEALTH, new FairySoulConfig(
            "Legendary Health Fairy Soul", "§6Legendary Health Fairy Soul", Material.HEART_OF_THE_SEA,
            "§7A legendary fairy soul that provides health.",
            FairySoulRarity.LEGENDARY, FairySoulCategory.HEALTH, 5, 0.5, Collections.singletonList("§7+5 Health"),
            Arrays.asList("§7- 5x Heart of the Sea", "§7- 5x Prismarine Shard", "§7- 1x Nether Star")
        ));

        fairySoulConfigs.put(FairySoulType.LEGENDARY_DEFENSE, new FairySoulConfig(
            "Legendary Defense Fairy Soul", "§6Legendary Defense Fairy Soul", Material.SHIELD,
            "§7A legendary fairy soul that provides defense.",
            FairySoulRarity.LEGENDARY, FairySoulCategory.DEFENSE, 5, 0.5, Collections.singletonList("§7+5 Defense"),
            Arrays.asList("§7- 5x Shield", "§7- 5x Iron Ingot", "§7- 1x Nether Star")
        ));

        fairySoulConfigs.put(FairySoulType.LEGENDARY_STRENGTH, new FairySoulConfig(
            "Legendary Strength Fairy Soul", "§6Legendary Strength Fairy Soul", Material.DIAMOND_SWORD,
            "§7A legendary fairy soul that provides strength.",
            FairySoulRarity.LEGENDARY, FairySoulCategory.STRENGTH, 5, 0.5, Collections.singletonList("§7+5 Strength"),
            Arrays.asList("§7- 5x Diamond Sword", "§7- 5x Diamond", "§7- 1x Nether Star")
        ));

        fairySoulConfigs.put(FairySoulType.LEGENDARY_SPEED, new FairySoulConfig(
            "Legendary Speed Fairy Soul", "§6Legendary Speed Fairy Soul", Material.FEATHER,
            "§7A legendary fairy soul that provides speed.",
            FairySoulRarity.LEGENDARY, FairySoulCategory.SPEED, 5, 0.5, Collections.singletonList("§7+5 Speed"),
            Arrays.asList("§7- 5x Feather", "§7- 5x String", "§7- 1x Nether Star")
        ));

        fairySoulConfigs.put(FairySoulType.LEGENDARY_INTELLIGENCE, new FairySoulConfig(
            "Legendary Intelligence Fairy Soul", "§6Legendary Intelligence Fairy Soul", Material.ENCHANTING_TABLE,
            "§7A legendary fairy soul that provides intelligence.",
            FairySoulRarity.LEGENDARY, FairySoulCategory.INTELLIGENCE, 5, 0.5, Collections.singletonList("§7+5 Intelligence"),
            Arrays.asList("§7- 5x Enchanting Table", "§7- 5x Lapis Lazuli", "§7- 1x Nether Star")
        ));
    }

    private void startFairySoulUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerFairySouls> entry : playerFairySouls.entrySet()) {
                    PlayerFairySouls fairySouls = entry.getValue();
                    fairySouls.update();
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L); // Every second
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Check if player is interacting with a fairy soul
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BEACON) {
            // Check if this is a fairy soul location
            Location location = event.getClickedBlock().getLocation();
            FairySoulType fairySoulType = getFairySoulAtLocation(location);

            if (fairySoulType != null) {
                collectFairySoul(player, fairySoulType, location);
            }
        }
    }

    private FairySoulType getFairySoulAtLocation(Location location) {
        Objects.requireNonNull(location);
        // This would check if there's a fairy soul at the given location
        // For now, return a placeholder fairy soul type
        return FairySoulType.COMMON_HEALTH;
    }

    private void collectFairySoul(Player player, FairySoulType fairySoulType, Location location) {
        PlayerFairySouls playerFairySouls = getPlayerFairySouls(player.getUniqueId());

        // Check if player already has this fairy soul
        if (playerFairySouls.hasFairySoul(fairySoulType)) {
            player.sendMessage(Component.text("§cDu hast diese Fairy Soul bereits gesammelt!"));
            return;
        }

        // Collect fairy soul
        playerFairySouls.addFairySoul(fairySoulType);

        // Apply fairy soul effects
        applyFairySoulEffects(player, fairySoulType);

        // Show collection message
        showFairySoulCollectionMessage(player, fairySoulType);

        // Play effects
        playFairySoulEffects(player, location);

        // Update database
        databaseManager.executeUpdate("""
            INSERT INTO player_fairy_souls (uuid, fairy_soul_type, location_x, location_y, location_z, timestamp)
            VALUES (?, ?, ?, ?, ?, NOW())
        """, player.getUniqueId(), fairySoulType.name(), location.getX(), location.getY(), location.getZ());
    }

    @SuppressWarnings("deprecation")
    private void applyFairySoulEffects(Player player, FairySoulType fairySoulType) {
        FairySoulConfig config = fairySoulConfigs.get(fairySoulType);
        if (config == null) return;

        // Apply effects based on fairy soul type
        switch (config.getCategory()) {
            case HEALTH:
                // Increase max health using Player API (Attribute constant not available in some APIs)
                double currentMax = player.getMaxHealth();
                player.setMaxHealth(currentMax + config.getStatBonus());
                break;
            case DEFENSE:
                // Add defense effect
                player.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.RESISTANCE, 200, config.getStatBonus() - 1));
                break;
            case STRENGTH:
                // Add strength effect
                player.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.STRENGTH, 200, config.getStatBonus() - 1));
                break;
            case SPEED:
                // Add speed effect
                player.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.SPEED, 200, config.getStatBonus() - 1));
                break;
            case INTELLIGENCE:
                // Add intelligence effect (custom effect)
                // This would be handled by the mana system
                break;
        }
    }

    private void showFairySoulCollectionMessage(Player player, FairySoulType fairySoulType) {
        FairySoulConfig config = fairySoulConfigs.get(fairySoulType);
        if (config == null) return;

        Component message = createFairySoulCollectionMessage(config);
        // Send as Adventure Component (modern API)
        player.sendMessage(message);

        // Show title using modern Paper API
        player.showTitle(Title.title(
            Component.empty(),
            Component.text(config.getDisplayName()),
            Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(2), Duration.ofMillis(500))
        ));
    }

    private Component createFairySoulCollectionMessage(FairySoulConfig config) {
        // Build a composed Component using Adventure to avoid ChatColor deprecation
        NamedTextColor color = getFairySoulRarityColor(config.getRarity());
        Component header = Component.text("✨ FAIRY SOUL COLLECTED! ✨ ").color(color);
        Component name = Component.text(config.getDisplayName()).color(NamedTextColor.WHITE);

        Component effects = Component.empty();
        for (String effect : config.getEffects()) {
            effects = effects.append(Component.text("\n")).append(Component.text(effect));
        }

        return header.append(name).append(effects);
    }

    private NamedTextColor getFairySoulRarityColor(FairySoulRarity rarity) {
        return switch (rarity) {
            case COMMON -> NamedTextColor.WHITE;
            case UNCOMMON -> NamedTextColor.GREEN;
            case RARE -> NamedTextColor.BLUE;
            case EPIC -> NamedTextColor.LIGHT_PURPLE;
            case LEGENDARY -> NamedTextColor.GOLD;
        };
    }

    private void playFairySoulEffects(Player player, Location location) {
        // Particle effects
        player.getWorld().spawnParticle(Particle.ENCHANT, location, 50, 0.5, 0.5, 0.5, 0.3);
        player.getWorld().spawnParticle(Particle.HEART, location.clone().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.1);

        // Sound effects
        player.getWorld().playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
        player.getWorld().playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
    }

    public PlayerFairySouls getPlayerFairySouls(UUID playerId) {
        return playerFairySouls.computeIfAbsent(playerId, k -> new PlayerFairySouls(playerId));
    }

    @SuppressWarnings("unused")
    public FairySoulConfig getFairySoulConfig(FairySoulType type) {
        return fairySoulConfigs.get(type);
    }

    @SuppressWarnings("unused")
    public List<FairySoulType> getAllFairySoulTypes() {
        return new ArrayList<>(fairySoulConfigs.keySet());
    }

    // Fairy Soul Type Enum
    public enum FairySoulType {
        // Common Fairy Souls
        COMMON_HEALTH, COMMON_DEFENSE, COMMON_STRENGTH, COMMON_SPEED, COMMON_INTELLIGENCE,

        // Uncommon Fairy Souls
        UNCOMMON_HEALTH, UNCOMMON_DEFENSE, UNCOMMON_STRENGTH, UNCOMMON_SPEED, UNCOMMON_INTELLIGENCE,

        // Rare Fairy Souls
        RARE_HEALTH, RARE_DEFENSE, RARE_STRENGTH, RARE_SPEED, RARE_INTELLIGENCE,

        // Epic Fairy Souls
        EPIC_HEALTH, EPIC_DEFENSE, EPIC_STRENGTH, EPIC_SPEED, EPIC_INTELLIGENCE,

        // Legendary Fairy Souls
        LEGENDARY_HEALTH, LEGENDARY_DEFENSE, LEGENDARY_STRENGTH, LEGENDARY_SPEED, LEGENDARY_INTELLIGENCE
    }

    // Fairy Soul Rarity Enum
    @SuppressWarnings("unused")
    public enum FairySoulRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 2.5),
        LEGENDARY("§6Legendary", 3.0);

        private final String displayName;
        private final double multiplier;

        FairySoulRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }

        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }

    // Fairy Soul Category Enum
    @SuppressWarnings("unused")
    public enum FairySoulCategory {
        HEALTH("§cHealth", 1.0),
        DEFENSE("§bDefense", 1.2),
        STRENGTH("§cStrength", 1.1),
        SPEED("§aSpeed", 1.1),
        INTELLIGENCE("§bIntelligence", 1.3);

        private final String displayName;
        private final double multiplier;

        FairySoulCategory(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }

        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }

    // Fairy Soul Config Class
    @SuppressWarnings("unused")
    public static class FairySoulConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final FairySoulRarity rarity;
        private final FairySoulCategory category;
        private final int statBonus;
        private final double successRate;
        private final List<String> effects;
        private final List<String> materials;

        public FairySoulConfig(String name, String displayName, Material icon, String description,
                             FairySoulRarity rarity, FairySoulCategory category, int statBonus, double successRate,
                             List<String> effects, List<String> materials) {
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
            this.rarity = rarity;
            this.category = category;
            this.statBonus = statBonus;
            this.successRate = successRate;
            this.effects = effects;
            this.materials = materials;
        }

        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public FairySoulRarity getRarity() { return rarity; }
        public FairySoulCategory getCategory() { return category; }
        public int getStatBonus() { return statBonus; }
        public double getSuccessRate() { return successRate; }
        public List<String> getEffects() { return effects; }
        public List<String> getMaterials() { return materials; }
    }

    // Player Fairy Souls Class
    @SuppressWarnings("unused")
    public static class PlayerFairySouls {
        private final UUID playerId;
        private final Set<FairySoulType> collectedFairySouls = new HashSet<>();
        private final Map<FairySoulRarity, Integer> rarityCounts = new ConcurrentHashMap<>();
        private final Map<FairySoulCategory, Integer> categoryCounts = new ConcurrentHashMap<>();
        private int totalFairySouls = 0;
        private long lastUpdate;

        public PlayerFairySouls(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }

        public void update() {
            long currentTime = java.lang.System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;

            // Update statistics every minute
            if (timeDiff >= 60000) {
                // Save to database
                saveToDatabase();
                lastUpdate = currentTime;
            }
        }

        private void saveToDatabase() {
            // Save fairy soul statistics to database
            // This would integrate with the database system
        }

        public void addFairySoul(FairySoulType fairySoulType) {
            collectedFairySouls.add(fairySoulType);

            // Update rarity count
            String rarityName = fairySoulType.name().split("_")[0];
            FairySoulRarity rarity = FairySoulRarity.valueOf(rarityName);
            rarityCounts.put(rarity, rarityCounts.getOrDefault(rarity, 0) + 1);

            // Update category count
            String categoryName = fairySoulType.name().split("_")[1];
            FairySoulCategory category = FairySoulCategory.valueOf(categoryName);
            categoryCounts.put(category, categoryCounts.getOrDefault(category, 0) + 1);

            totalFairySouls++;
        }

        public boolean hasFairySoul(FairySoulType fairySoulType) {
            return collectedFairySouls.contains(fairySoulType);
        }

        public int getRarityCount(FairySoulRarity rarity) {
            return rarityCounts.getOrDefault(rarity, 0);
        }

        public int getCategoryCount(FairySoulCategory category) {
            return categoryCounts.getOrDefault(category, 0);
        }

        public double getCollectionProgress() {
            // Calculate collection progress as percentage
            return totalFairySouls > 0 ? (double) totalFairySouls / 25 * 100 : 0.0; // 25 total fairy souls
        }

        // Getters
        public UUID getPlayerId() { return playerId; }
        public Set<FairySoulType> getCollectedFairySouls() { return collectedFairySouls; }
        public Map<FairySoulRarity, Integer> getRarityCounts() { return rarityCounts; }
        public Map<FairySoulCategory, Integer> getCategoryCounts() { return categoryCounts; }
        public int getTotalFairySouls() { return totalFairySouls; }
    }
}
