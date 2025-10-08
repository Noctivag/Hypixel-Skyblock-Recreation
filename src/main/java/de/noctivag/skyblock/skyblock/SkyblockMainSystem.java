package de.noctivag.skyblock.skyblock;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import de.noctivag.skyblock.skyblock.collections.CollectionsSystem;
import de.noctivag.skyblock.skyblock.data.PlayerSkyblockData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import de.noctivag.skyblock.SkyblockPlugin;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main Skyblock System that integrates all Skyblock features
 * Features:
 * - Centralized system management
 * - Player data integration
 * - System initialization and cleanup
 * - Cross-system communication
 * - Performance optimization
 */
public class SkyblockMainSystem implements Listener {
    public de.noctivag.skyblock.features.menu.SkyblockMenuSystem getMenuSystem() {
        // Return null for now as we're using the legacy skyblock.SkyblockMenuSystem
        return null;
    }

    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;

    // Core systems
    private HealthManaSystem healthManaSystem;
    private SkyblockMenuSystem menuSystem; // Legacy menu system
    private AdvancedSkillsSystem skillsSystem;
    private CollectionsSystem collectionsSystem;
    private PetSystem petSystem;

    // Player data management
    private final Map<UUID, PlayerSkyblockData> playerData = new ConcurrentHashMap<>();

    public SkyblockMainSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;

        // Defer initialization to avoid 'this' escaping during construction.
        // Call init() once the SkyblockPlugin is fully enabled.
    }

    /**
     * Initialize systems and register events. Call after construction when SkyblockPlugin is ready.
     */
    public void init() {
        // Initialize all systems
        initializeSystems();

        // Register this listener once initialization is complete
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }

    private void initializeSystems() {
        // Initialize core systems in dependency order
        healthManaSystem = new HealthManaSystem(SkyblockPlugin, databaseManager);
        skillsSystem = new AdvancedSkillsSystem(SkyblockPlugin, databaseManager);
        // collectionsSystem = new CollectionsSystem(SkyblockPlugin, databaseManager, skillsSystem);
        // Initialize CollectionsSystem with the skyblock-specific implementation that only requires the SkyblockPlugin
        collectionsSystem = new CollectionsSystem(SkyblockPlugin);
        petSystem = new PetSystem(SkyblockPlugin, databaseManager);
        menuSystem = new SkyblockMenuSystem(SkyblockPlugin);

        // Register commands
        registerCommands();

        SkyblockPlugin.getLogger().info("Skyblock systems initialized successfully!");
    }

    private void registerCommands() {
        // Register Skyblock commands (defensive: check for null to avoid NPEs when commands missing in SkyblockPlugin.yml)
        var skyblockCmd = SkyblockPlugin.getCommand("skyblock");
        if (skyblockCmd != null) skyblockCmd.setExecutor(new SkyblockCommand(this)); else SkyblockPlugin.getLogger().warning("Command 'skyblock' not defined in SkyblockPlugin.yml");
        var skillsCmd = SkyblockPlugin.getCommand("skills");
        if (skillsCmd != null) skillsCmd.setExecutor(new SkillsCommand(this)); else SkyblockPlugin.getLogger().warning("Command 'skills' not defined in SkyblockPlugin.yml");
        var collectionsCmd = SkyblockPlugin.getCommand("collections");
        if (collectionsCmd != null) collectionsCmd.setExecutor(new CollectionsCommand(this)); else SkyblockPlugin.getLogger().warning("Command 'collections' not defined in SkyblockPlugin.yml");
        var petsCmd = SkyblockPlugin.getCommand("pets");
        if (petsCmd != null) petsCmd.setExecutor(new PetsCommand(this)); else SkyblockPlugin.getLogger().warning("Command 'pets' not defined in SkyblockPlugin.yml");
        var combatCmd = SkyblockPlugin.getCommand("combat");
        if (combatCmd != null) combatCmd.setExecutor(new CombatCommand(this)); else SkyblockPlugin.getLogger().warning("Command 'combat' not defined in SkyblockPlugin.yml");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Load player data
        PlayerSkyblockData data = loadPlayerData(playerId);
        playerData.put(playerId, data);

        // Initialize player in all systems
        initializePlayerInSystems(player);

        // Send welcome message
        player.sendMessage(Component.text("§6§lWelcome to Skyblock!"));
        player.sendMessage(Component.text("§7Use §e/skyblock §7to open the main menu!"));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Save player data
        savePlayerData(playerId);

        // Cleanup player from all systems
        cleanupPlayerFromSystems(player);

        // Remove from memory
        playerData.remove(playerId);
    }

    private void initializePlayerInSystems(Player player) {
        // Initialize player in all systems
        // This is handled by each system's PlayerJoinEvent
    }

    private void cleanupPlayerFromSystems(Player player) {
        // Cleanup player from all systems
        // This is handled by each system's PlayerQuitEvent
    }

    private PlayerSkyblockData loadPlayerData(UUID playerId) {
        // Load from database or create new
        PlayerSkyblockData data = new PlayerSkyblockData(playerId);

        // Load player data from database
        if (databaseManager != null) {
            // Use a simple synchronous approach for now
            // databaseManager.loadPlayerData(playerId).thenAccept(data -> {
            //     if (data != null) {
            //         playerData.put(playerId, data);
            //     }
            // });
        }
        // if (databaseManager != null && databaseManager.isConnected()) {
        //     data = databaseManager.loadPlayerSkyblockData(playerId);
        // }

        return data;
    }

    private void savePlayerData(UUID playerId) {
        PlayerSkyblockData data = playerData.get(playerId);
        if (data == null) return;

        // Save player data to database
        if (databaseManager != null) {
            // Use a simple synchronous approach for now
            // databaseManager.savePlayerData(playerId, data).thenRun(() -> {
            //     SkyblockPlugin.getLogger().info("Player data saved for " + playerId);
            // });
        }
        // if (databaseManager != null && databaseManager.isConnected()) {
        //     databaseManager.savePlayerSkyblockData(playerId, data);
        // }
    }

    /**
     * Shuts down all subsystems and cleans up resources.
     */
    public void shutdown() {
        // Clear player data and log shutdown
        playerData.clear();
        SkyblockPlugin.getLogger().info("SkyblockMainSystem has been shut down.");
    }

    // Public API methods
    public void openMainMenu(Player player) {
        menuSystem.openMainMenu(player);
    }

    public void openSkillsMenu(Player player) {
        menuSystem.openSkillsMenu(player);
    }

    public void openCollectionsMenu(Player player) {
        menuSystem.openCollectionsMenu(player);
    }

    public void openProfileMenu(Player player) {
        menuSystem.openProfileMenu(player);
    }

    public void openFastTravelMenu(Player player) {
        menuSystem.openFastTravelMenu(player);
    }

    public HealthManaSystem getHealthManaSystem() {
        return healthManaSystem;
    }

    public AdvancedSkillsSystem getSkillsSystem() {
        return skillsSystem;
    }

    public CollectionsSystem getCollectionsSystem() {
        return collectionsSystem;
    }

    public PetSystem getPetSystem() {
        return petSystem;
    }

    public PlayerSkyblockData getPlayerData(Player player) {
        return playerData.get(player.getUniqueId());
    }

    public static class PlayerSkyblockData {
        private final UUID playerId;
        private long joinTime;
        private long totalPlayTime;
        private int skyblockLevel;
        private double skyblockXP;
        private Map<String, Object> customData;

        public PlayerSkyblockData(UUID playerId) {
            this.playerId = playerId;
            this.joinTime = java.lang.System.currentTimeMillis();
            this.totalPlayTime = 0;
            this.skyblockLevel = 1;
            this.skyblockXP = 0.0;
            this.customData = new HashMap<>();
        }

        public UUID getPlayerId() { return playerId; }
        public long getJoinTime() { return joinTime; }
        public void setJoinTime(long joinTime) { this.joinTime = joinTime; }
        public long getTotalPlayTime() { return totalPlayTime; }
        public void setTotalPlayTime(long totalPlayTime) { this.totalPlayTime = totalPlayTime; }
        public int getSkyblockLevel() { return skyblockLevel; }
        public void setSkyblockLevel(int skyblockLevel) { this.skyblockLevel = skyblockLevel; }
        public double getSkyblockXP() { return skyblockXP; }
        public void setSkyblockXP(double skyblockXP) { this.skyblockXP = skyblockXP; }
        public Map<String, Object> getCustomData() { return customData; }
    }
}

// Command classes
class SkyblockCommand implements org.bukkit.command.CommandExecutor {
    private final SkyblockMainSystem system;

    public SkyblockCommand(SkyblockMainSystem system) {
        this.system = system;
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            system.openMainMenu(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "menu":
                system.openMainMenu(player);
                break;
            case "skills":
                system.openSkillsMenu(player);
                break;
            case "collections":
                system.openCollectionsMenu(player);
                break;
            case "profile":
                system.openProfileMenu(player);
                break;
            case "travel":
                system.openFastTravelMenu(player);
                break;
            default:
                player.sendMessage(Component.text("§cUnknown subcommand! Use /skyblock for the main menu."));
                break;
        }

        return true;
    }
}

class SkillsCommand implements org.bukkit.command.CommandExecutor {
    private final SkyblockMainSystem system;

    public SkillsCommand(SkyblockMainSystem system) {
        this.system = system;
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;
        system.openSkillsMenu(player);
        return true;
    }
}

class CollectionsCommand implements org.bukkit.command.CommandExecutor {
    private final SkyblockMainSystem system;

    public CollectionsCommand(SkyblockMainSystem system) {
        this.system = system;
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;
        system.openCollectionsMenu(player);
        return true;
    }
}

class PetsCommand implements org.bukkit.command.CommandExecutor {
    private final SkyblockMainSystem system;

    public PetsCommand(SkyblockMainSystem system) {
        this.system = system;
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // Open pets menu
            player.sendMessage(Component.text("§eOpening pets menu..."));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "list":
                List<PetSystem.Pet> pets = system.getPetSystem().getPlayerPets(player);
                player.sendMessage(Component.text("§6§lYour Pets:"));
                for (PetSystem.Pet pet : pets) {
                    player.sendMessage("§7- §e" + pet.getName() + " §7(Level " + pet.getLevel() + ")");
                }
                break;
            case "active":
                PetSystem.Pet activePet = system.getPetSystem().getActivePet(player);
                if (activePet != null) {
                    player.sendMessage("§aActive Pet: §e" + activePet.getName() + " §7(Level " + activePet.getLevel() + ")");
                } else {
                    player.sendMessage(Component.text("§cNo active pet!"));
                }
                break;
            default:
                player.sendMessage(Component.text("§cUnknown subcommand! Use /pets list or /pets active"));
                break;
        }

        return true;
    }
}

class CombatCommand implements org.bukkit.command.CommandExecutor {
    private final SkyblockMainSystem system;

    public CombatCommand(SkyblockMainSystem system) {
        this.system = system;
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0 || args[0].equalsIgnoreCase("stats")) {
            // Zeige vereinheitlichte Stats aus PlayerStatData
            var statsSystem = system.getHealthManaSystem(); // oder ein globales Stat-System
            var stats = statsSystem.getPlayerStats(player.getUniqueId());
            player.sendMessage(Component.text("§a§lSpieler-Statistiken:"));
            player.sendMessage("§7Health: §c" + stats.getHealth() + "/" + stats.getMaxHealth());
            player.sendMessage("§7Mana: §b" + stats.getMana() + "/" + stats.getMaxMana());
            player.sendMessage("§7Defense: §a" + stats.getDefense());
            player.sendMessage("§7Strength: §c" + stats.getStrength());
            player.sendMessage("§7Speed: §f" + stats.getSpeed());
            player.sendMessage("§7Crit Chance: §e" + stats.getCritChance() + "%");
            player.sendMessage("§7Crit Damage: §e" + stats.getCritDamage() + "%");
            player.sendMessage("§7Intelligence: §b" + stats.getIntelligence());
            player.sendMessage("§7Ferocity: §d" + stats.getFerocity());
            player.sendMessage("§7True Defense: §a" + stats.getTrueDefense());
            player.sendMessage("§7Magic Find: §9" + stats.getMagicFind());
            player.sendMessage("§7Pristine: §b" + stats.getPristine());
            player.sendMessage("§7Fortune: §6" + stats.getFortune());
            return true;
        }
        player.sendMessage(Component.text("§cUnknown subcommand! Use /combat stats"));
        return true;
    }

    // Missing methods for IslandCommand and other systems
    public boolean hasProfile(UUID playerId) {
        return false; // playerData not available in this context
    }

    public void createProfile(Player player) {
        if (!hasProfile(player.getUniqueId())) {
            PlayerSkyblockData data = new PlayerSkyblockData(player.getUniqueId());
            // playerData.put(player.getUniqueId(), data); // playerData not available in this context
            // Initialize systems for this player
            // if (healthManaSystem != null) {
            //     healthManaSystem.initializePlayer(player);
            // }
        }
    }

    public void teleportToIsland(Player player) {
        // Placeholder implementation
        player.sendMessage(Component.text("§aTeleporting to your island..."));
        // TODO: Implement actual island teleportation
    }

    public PlayerSkyblockData getProfile(UUID playerId) {
        return null; // playerData not available in this context
    }

    public Object getIsland(UUID playerId) {
        // Placeholder implementation
        return new Object(); // TODO: Return actual island object
    }

    public void teleportToHub(Player player) {
        // Placeholder implementation
        player.sendMessage(Component.text("§aTeleporting to hub..."));
        // TODO: Implement actual hub teleportation
    }

    public Object getSkills(UUID playerId) {
        // Placeholder implementation
        return new Object(); // TODO: Return actual skills object
    }

    public void addCollection(Player player, org.bukkit.Material material, int amount) {
        // Placeholder implementation
        // if (collectionsSystem != null) {
        //     // TODO: Implement actual collection addition
        // }
    }

    public void addSkillXP(Player player, de.noctivag.skyblock.skyblock.SkyblockManager.SkyblockSkill skill, double xp) {
        // Placeholder implementation
        // if (skillsSystem != null) {
        //     // TODO: Implement actual skill XP addition
        // }
    }

    // Missing method for MiningCommand and MiningAreaSystem
}
