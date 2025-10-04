package de.noctivag.skyblock.skyblock;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.data.SQLiteStorage;
import de.noctivag.skyblock.worlds.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SkyblockManager {
    private final SkyblockPlugin plugin;
    private final WorldManager worldManager;
    private final Map<UUID, SkyblockProfile> profiles = new ConcurrentHashMap<>();
    private final Map<UUID, SkyblockIsland> islands = new ConcurrentHashMap<>();
    private final Map<UUID, SkyblockSkills> skills = new ConcurrentHashMap<>();
    private final Map<UUID, SkyblockCollections> collections = new ConcurrentHashMap<>();
    private final Map<UUID, SkyblockBank> banks = new ConcurrentHashMap<>();

    // Skyblock worlds
    private World hubWorld;
    private World privateIslandWorld;
    private World publicIslandWorld;

    public SkyblockManager(SkyblockPlugin plugin, WorldManager worldManager) {
        this.plugin = plugin;
        this.worldManager = worldManager;
        initializeWorlds();
        startAutoSave();
    }

    /**
     * Load a player's profile from persistent storage (SQLite) or fallback to file-based YAML.
     */
    private SkyblockProfile loadProfile(UUID uuid, String playerName) {
        try {
            SkyblockProfile p = SQLiteStorage.loadProfile(uuid);
            if (p != null) return p;
        } catch (Exception e) {
            plugin.getLogger().warning("SQLite loadProfile failed for " + uuid + ": " + e.getMessage());
        }
        // Fallback to YAML-based loader
        SkyblockProfile yamlProfile = SkyblockProfile.load(uuid);
        if (yamlProfile != null) return yamlProfile;
        // If nothing found, create new
        return new SkyblockProfile(uuid, playerName);
    }

    private SkyblockIsland loadIsland(UUID owner) {
        try {
            SkyblockIsland island = SQLiteStorage.loadIsland(owner);
            if (island != null) return island;
        } catch (Exception e) {
            plugin.getLogger().warning("SQLite loadIsland failed for " + owner + ": " + e.getMessage());
        }
        return null;
    }

    private void initializeWorlds() {
        plugin.getLogger().info("Initializing Skyblock worlds...");

        // Warte auf WorldManager Initialisierung
        if (!worldManager.isInitialized()) {
            plugin.getLogger().warning("WorldManager not initialized, deferring world initialization...");
            // Use thread-based delay for Folia compatibility instead of Bukkit scheduler
            Thread.ofVirtual().start(() -> {
                try {
                    Thread.sleep(1000); // 1 second delay
                    initializeWorlds();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    plugin.getLogger().warning("World initialization retry interrupted");
                }
            });
            return;
        }

        // Lade Skyblock-Welten
        hubWorld = worldManager.getWorld("skyblock_hub");
        privateIslandWorld = worldManager.getWorld("skyblock_private");
        publicIslandWorld = worldManager.getWorld("skyblock_public");

        // Validiere Welten
        if (hubWorld == null) {
            plugin.getLogger().severe("Failed to load skyblock_hub world!");
        } else {
            plugin.getLogger().info("Loaded skyblock_hub world successfully");
        }

        if (privateIslandWorld == null) {
            plugin.getLogger().severe("Failed to load skyblock_private world!");
        } else {
            plugin.getLogger().info("Loaded skyblock_private world successfully");
        }

        if (publicIslandWorld == null) {
            plugin.getLogger().severe("Failed to load skyblock_public world!");
        } else {
            plugin.getLogger().info("Loaded skyblock_public world successfully");
        }

        plugin.getLogger().info("Skyblock world initialization completed");
    }

    public void createProfile(Player player) {
        UUID uuid = player.getUniqueId();

        // Create new profile
        SkyblockProfile profile = loadProfile(uuid, player.getName());
        profiles.put(uuid, profile);

        // Create island
        SkyblockIsland island = createIsland(player);
        islands.put(uuid, island);

        // Persist profile & island
        try {
            profile.save(); // existing YAML save for compatibility
            SQLiteStorage.saveProfile(profile);
        } catch (Exception e) { plugin.getLogger().warning("Failed to persist profile for " + player.getName()); }
        try {
            island.save(); // placeholder
            SQLiteStorage.saveIsland(island);
        } catch (Exception e) { plugin.getLogger().warning("Failed to persist island for " + player.getName()); }

        // Initialize skills
        SkyblockSkills playerSkills = new SkyblockSkills();
        skills.put(uuid, playerSkills);

        // Initialize collections
        SkyblockCollections playerCollections = new SkyblockCollections();
        collections.put(uuid, playerCollections);

        // Initialize bank
        SkyblockBank playerBank = new SkyblockBank();
        banks.put(uuid, playerBank);

        // Give starter items
        giveStarterItems(player);

        // Teleport to island
        teleportToIsland(player);

        player.sendMessage("§a§lWelcome to Skyblock!");
        player.sendMessage("§7Your island has been created at: §e" + island.getSpawnLocation());
    }

    /**
     * Ensure a player's profile and island are loaded (used on join)
     */
    public void ensureProfileLoaded(Player player) {
        UUID uuid = player.getUniqueId();
        if (hasProfile(uuid)) return;
        SkyblockProfile profile = loadProfile(uuid, player.getName());
        profiles.put(uuid, profile);

        SkyblockIsland island = loadIsland(uuid);
        if (island != null) {
            islands.put(uuid, island);
        } else {
            // If no island exists and this is a first join (default true), create starter island
            try {
                if (profile.isFirstJoin()) {
                    plugin.getLogger().info("Creating starter island for new player: " + player.getName());
                    SkyblockIsland created = createIsland(player);
                    islands.put(uuid, created);
                    profile.setFirstJoin(false);
                    // Persist
                    profile.save();
                    SQLiteStorage.saveProfile(profile);
                    created.save();
                    SQLiteStorage.saveIsland(created);
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to auto-create island for " + player.getName() + ": " + e.getMessage());
            }
        }
    }

    private SkyblockIsland createIsland(Player player) {
        // Generate island location
        Location islandLoc = generateIslandLocation();

        // Create island structure
        SkyblockIsland island = new SkyblockIsland(player.getUniqueId(), islandLoc);
        island.generateStarterIsland();

        return island;
    }

    private Location generateIslandLocation() {
        // Prüfe ob private Island Welt verfügbar ist
        if (privateIslandWorld == null) {
            plugin.getLogger().warning("Private island world not available, using fallback location");
            World fallbackWorld = worldManager.getWorld("skyblock_private");
            if (fallbackWorld == null) {
                fallbackWorld = Bukkit.getWorlds().isEmpty() ? null : Bukkit.getWorlds().get(0);
            }
            if (fallbackWorld == null) {
                plugin.getLogger().severe("No worlds available for island generation!");
                return null;
            }
            return new Location(fallbackWorld, 0, 100, 0);
        }

        // Generate random island coordinates
        Random random = new Random();
        int x = random.nextInt(10000) - 5000;
        int z = random.nextInt(10000) - 5000;

        // Stelle sicher, dass die Location sicher ist
        Location location = new Location(privateIslandWorld, x, 100, z);
        Location safeLocation = worldManager.getSafeSpawnLocation("skyblock_private");
        if (safeLocation != null) {
            location.setY(safeLocation.getY());
        }

        return location;
    }

    private void giveStarterItems(Player player) {
        // Starter items like in Hypixel Skyblock
        player.getInventory().addItem(new ItemStack(Material.WOODEN_PICKAXE));
        player.getInventory().addItem(new ItemStack(Material.WOODEN_AXE));
        player.getInventory().addItem(new ItemStack(Material.WOODEN_SHOVEL));
        player.getInventory().addItem(new ItemStack(Material.WOODEN_SWORD));
        player.getInventory().addItem(new ItemStack(Material.BREAD, 16));
        player.getInventory().addItem(new ItemStack(Material.OAK_SAPLING, 4));
        player.getInventory().addItem(new ItemStack(Material.WHEAT_SEEDS, 8));
        player.getInventory().addItem(new ItemStack(Material.CARROT, 4));
        player.getInventory().addItem(new ItemStack(Material.POTATO, 4));
        player.getInventory().addItem(new ItemStack(Material.BEETROOT_SEEDS, 4));
    }

    public void teleportToIsland(Player player) {
        SkyblockIsland island = islands.get(player.getUniqueId());
        if (island != null) {
            Location spawnLocation = island.getSpawnLocation();
            if (spawnLocation != null && spawnLocation.getWorld() != null) {
                player.teleport(spawnLocation);
            } else {
                plugin.getLogger().warning("Invalid island spawn location for player: " + player.getName());
                // Fallback zur Hub-Welt
                teleportToHub(player);
            }
        } else {
            plugin.getLogger().warning("No island found for player: " + player.getName());
            // Fallback zur Hub-Welt
            teleportToHub(player);
        }
    }

    public void teleportToHub(Player player) {
        Location hubSpawn = worldManager.getSafeSpawnLocation("skyblock_hub");
        if (hubSpawn != null) {
            player.teleport(hubSpawn);
        } else {
            plugin.getLogger().severe("No valid hub spawn location available for player: " + player.getName());
            // Letzter Fallback
            if (!Bukkit.getWorlds().isEmpty()) {
                player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            }
        }
    }

    public void addCollection(Player player, Material material, int amount) {
        SkyblockCollections playerCollections = collections.get(player.getUniqueId());
        if (playerCollections != null) {
            playerCollections.addCollection(material, amount);

            // Check for collection milestones
            checkCollectionMilestones(player, material);
        }
    }

    private void checkCollectionMilestones(Player player, Material material) {
        SkyblockCollections playerCollections = collections.get(player.getUniqueId());
        if (playerCollections == null) return;

        int total = playerCollections.getCollection(material);

        // Check milestones (like Hypixel: 50, 100, 250, 500, 1000, etc.)
        int[] milestones = {50, 100, 250, 500, 1000, 2500, 5000, 10000, 25000, 50000, 100000, 250000, 500000, 1000000};

        for (int milestone : milestones) {
            if (total >= milestone && !playerCollections.hasMilestone(material, milestone)) {
                playerCollections.addMilestone(material, milestone);
                player.sendMessage("§a§lCOLLECTION MILESTONE!");
                player.sendMessage("§7" + material.name() + " Collection: §e" + milestone);
                player.sendMessage("§7Reward: §6+1 Collection Level");

                // Give reward
                giveCollectionReward(player, material, milestone);
            }
        }
    }

    private void giveCollectionReward(Player player, Material material, int milestone) {
        // Give collection rewards based on milestone
        switch (milestone) {
            case 50 -> {
                player.getInventory().addItem(new ItemStack(material, 8));
                plugin.getEconomyManager().giveMoney(player, 100);
            }
            case 100 -> {
                player.getInventory().addItem(new ItemStack(material, 16));
                plugin.getEconomyManager().giveMoney(player, 250);
            }
            case 250 -> {
                player.getInventory().addItem(new ItemStack(material, 32));
                plugin.getEconomyManager().giveMoney(player, 500);
            }
            case 500 -> {
                player.getInventory().addItem(new ItemStack(material, 64));
                plugin.getEconomyManager().giveMoney(player, 1000);
            }
            case 1000 -> {
                player.getInventory().addItem(new ItemStack(material, 64));
                player.getInventory().addItem(new ItemStack(material, 64));
                plugin.getEconomyManager().giveMoney(player, 2500);
            }
            // Add more milestones...
        }
    }

    public void addSkillXP(Player player, SkyblockSkill skill, double xp) {
        SkyblockSkills playerSkills = skills.get(player.getUniqueId());
        if (playerSkills != null) {
            int oldLevel = playerSkills.getLevel(skill);
            playerSkills.addXP(skill, xp);
            int newLevel = playerSkills.getLevel(skill);

            if (newLevel > oldLevel) {
                player.sendMessage("§a§lSKILL LEVEL UP!");
                player.sendMessage("§7" + skill.getDisplayName() + " Level: §e" + oldLevel + " §7→ §a" + newLevel);

                // Give skill rewards
                giveSkillReward(player, skill, newLevel);
            }
        }
    }

    private void giveSkillReward(Player player, SkyblockSkill skill, int level) {
        // Give skill rewards based on level
        switch (skill) {
            case MINING -> {
                if (level % 5 == 0) {
                    plugin.getEconomyManager().giveMoney(player, level * 100);
                }
            }
            case FARMING -> {
                if (level % 5 == 0) {
                    plugin.getEconomyManager().giveMoney(player, level * 80);
                }
            }
            case COMBAT -> {
                if (level % 5 == 0) {
                    plugin.getEconomyManager().giveMoney(player, level * 120);
                }
            }
            case ALCHEMY -> {
                if (level % 5 == 0) {
                    plugin.getEconomyManager().giveMoney(player, level * 90);
                }
            }
            case FISHING -> {
                if (level % 5 == 0) {
                    plugin.getEconomyManager().giveMoney(player, level * 70);
                }
            }
            case RUNECRAFTING -> {
                if (level % 5 == 0) {
                    plugin.getEconomyManager().giveMoney(player, level * 110);
                }
            }
            case FORAGING -> {
                if (level % 5 == 0) {
                    plugin.getEconomyManager().giveMoney(player, level * 85);
                }
            }
            case CARPENTRY -> {
                if (level % 5 == 0) {
                    plugin.getEconomyManager().giveMoney(player, level * 95);
                }
            }
            case SOCIAL -> {
                if (level % 5 == 0) {
                    plugin.getEconomyManager().giveMoney(player, level * 60);
                }
            }
            case ENCHANTING -> {
                if (level % 5 == 0) {
                    plugin.getEconomyManager().giveMoney(player, level * 100);
                }
            }
            // Add more skills...
        }
    }

    private void startAutoSave() {
        // Use virtual thread for Folia compatibility instead of BukkitRunnable
        Thread.ofVirtual().start(() -> {
            while (plugin.isEnabled()) {
                try {
                    Thread.sleep(20 * 60 * 5L * 50); // Save every 5 minutes = 15,000,000 ms
                    if (plugin.isEnabled()) {
                        saveAllData();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private void saveAllData() {
        // Save all Skyblock data
        for (SkyblockProfile profile : profiles.values()) {
            profile.save();
        }
        for (SkyblockIsland island : islands.values()) {
            island.save();
        }
        for (SkyblockSkills skill : skills.values()) {
            skill.save();
        }
        for (SkyblockCollections collection : collections.values()) {
            collection.save();
        }
        for (SkyblockBank bank : banks.values()) {
            bank.save();
        }
    }

    // Getters
    public SkyblockProfile getProfile(UUID uuid) { return profiles.get(uuid); }
    public SkyblockIsland getIsland(UUID uuid) { return islands.get(uuid); }
    public SkyblockSkills getSkills(UUID uuid) {
        // Return existing skills or create new default skills so callers never receive null
        return skills.computeIfAbsent(uuid, k -> new SkyblockSkills());
    }
    public SkyblockCollections getCollections(UUID uuid) { return collections.get(uuid); }
    public SkyblockBank getBank(UUID uuid) { return banks.get(uuid); }

    public boolean hasProfile(UUID uuid) { return profiles.containsKey(uuid); }

    // Skyblock Skill Enum
    public enum SkyblockSkill {
        MINING("Mining", Material.DIAMOND_PICKAXE),
        FARMING("Farming", Material.WHEAT),
        COMBAT("Combat", Material.DIAMOND_SWORD),
        FORAGING("Foraging", Material.OAK_LOG),
        FISHING("Fishing", Material.FISHING_ROD),
        ENCHANTING("Enchanting", Material.ENCHANTING_TABLE),
        ALCHEMY("Alchemy", Material.BREWING_STAND),
        CARPENTRY("Carpentry", Material.CRAFTING_TABLE),
        RUNECRAFTING("Runecrafting", Material.END_STONE),
        SOCIAL("Social", Material.PLAYER_HEAD);

        private final String displayName;
        private final Material icon;

        SkyblockSkill(String displayName, Material icon) {
            this.displayName = displayName;
            this.icon = icon;
        }

        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
    }

    public boolean isLocationInAnyIsland(org.bukkit.Location loc) {
        if (loc == null) return false;
        for (SkyblockIsland island : islands.values()) {
            if (island != null && island.isWithinBounds(loc)) return true;
        }
        return false;
    }

    public SkyblockIsland getIslandAt(org.bukkit.Location loc) {
        if (loc == null) return null;
        for (SkyblockIsland island : islands.values()) {
            if (island != null && island.isWithinBounds(loc)) return island;
        }
        return null;
    }
}
