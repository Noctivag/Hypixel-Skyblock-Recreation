package de.noctivag.plugin.skyblock;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.database.MultiServerDatabaseManager;
import de.noctivag.plugin.skyblock.collections.CollectionsSystem;
import de.noctivag.plugin.skyblock.data.PlayerSkyblockData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
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

    private final JavaPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;

    // Core systems
    private HealthManaSystem healthManaSystem;
    private SkyblockMenuSystem menuSystem;
    private AdvancedSkillsSystem skillsSystem;
    private CollectionsSystem collectionsSystem;
    private AdvancedCombatSystem combatSystem;
    private PetSystem petSystem;

    // Player data management
    private final Map<UUID, PlayerSkyblockData> playerData = new ConcurrentHashMap<>();

    public SkyblockMainSystem(JavaPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;

        // Defer initialization to avoid 'this' escaping during construction.
        // Call init() once the plugin is fully enabled.
    }

    /**
     * Initialize systems and register events. Call after construction when plugin is ready.
     */
    public void init() {
        // Initialize all systems
        initializeSystems();

        // Register this listener once initialization is complete
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void initializeSystems() {
        // Initialize core systems in dependency order
        healthManaSystem = new HealthManaSystem(plugin, databaseManager);
        skillsSystem = new AdvancedSkillsSystem(plugin, databaseManager, healthManaSystem);
        // collectionsSystem = new CollectionsSystem(plugin, databaseManager, skillsSystem);
        // Initialize CollectionsSystem with the skyblock-specific implementation that only requires the Plugin
        de.noctivag.plugin.Plugin mainPlugin = (de.noctivag.plugin.Plugin) plugin;
        collectionsSystem = new CollectionsSystem(mainPlugin);
        combatSystem = new AdvancedCombatSystem(plugin, databaseManager, healthManaSystem, skillsSystem);
        petSystem = new PetSystem(plugin, databaseManager, healthManaSystem, skillsSystem);
        menuSystem = new SkyblockMenuSystem(plugin, healthManaSystem);

        // Register commands
        registerCommands();

        plugin.getLogger().info("Skyblock systems initialized successfully!");
    }

    private void registerCommands() {
        // Register Skyblock commands (defensive: check for null to avoid NPEs when commands missing in plugin.yml)
        var skyblockCmd = plugin.getCommand("skyblock");
        if (skyblockCmd != null) skyblockCmd.setExecutor(new SkyblockCommand(this)); else plugin.getLogger().warning("Command 'skyblock' not defined in plugin.yml");
        var skillsCmd = plugin.getCommand("skills");
        if (skillsCmd != null) skillsCmd.setExecutor(new SkillsCommand(this)); else plugin.getLogger().warning("Command 'skills' not defined in plugin.yml");
        var collectionsCmd = plugin.getCommand("collections");
        if (collectionsCmd != null) collectionsCmd.setExecutor(new CollectionsCommand(this)); else plugin.getLogger().warning("Command 'collections' not defined in plugin.yml");
        var petsCmd = plugin.getCommand("pets");
        if (petsCmd != null) petsCmd.setExecutor(new PetsCommand(this)); else plugin.getLogger().warning("Command 'pets' not defined in plugin.yml");
        var combatCmd = plugin.getCommand("combat");
        if (combatCmd != null) combatCmd.setExecutor(new CombatCommand(this)); else plugin.getLogger().warning("Command 'combat' not defined in plugin.yml");
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
        player.sendMessage("§6§lWelcome to Skyblock!");
        player.sendMessage("§7Use §e/skyblock §7to open the main menu!");
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
            //     plugin.getLogger().info("Player data saved for " + playerId);
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
        plugin.getLogger().info("SkyblockMainSystem has been shut down.");
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

    public AdvancedCombatSystem getCombatSystem() {
        return combatSystem;
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
            this.joinTime = System.currentTimeMillis();
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
                player.sendMessage("§cUnknown subcommand! Use /skyblock for the main menu.");
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
            player.sendMessage("§eOpening pets menu...");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "list":
                List<PetSystem.Pet> pets = system.getPetSystem().getPlayerPets(player);
                player.sendMessage("§6§lYour Pets:");
                for (PetSystem.Pet pet : pets) {
                    player.sendMessage("§7- §e" + pet.getName() + " §7(Level " + pet.getLevel() + ")");
                }
                break;
            case "active":
                PetSystem.Pet activePet = system.getPetSystem().getActivePet(player);
                if (activePet != null) {
                    player.sendMessage("§aActive Pet: §e" + activePet.getName() + " §7(Level " + activePet.getLevel() + ")");
                } else {
                    player.sendMessage("§cNo active pet!");
                }
                break;
            default:
                player.sendMessage("§cUnknown subcommand! Use /pets list or /pets active");
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

        if (args.length == 0) {
            // Show combat stats
            AdvancedCombatSystem.PlayerCombatData combatData = system.getCombatSystem().getPlayerCombatData(player.getUniqueId());
            player.sendMessage("§c§lCombat Statistics:");
            player.sendMessage("§7Damage Dealt: §e" + String.format("%.1f", combatData.getDamageDealt()));
            player.sendMessage("§7Hits: §e" + combatData.getHits());
            player.sendMessage("§7Kills: §e" + combatData.getKills());
            player.sendMessage("§7Deaths: §e" + combatData.getDeaths());
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "stats":
                AdvancedCombatSystem.PlayerCombatData combatData = system.getCombatSystem().getPlayerCombatData(player.getUniqueId());
                player.sendMessage("§c§lCombat Statistics:");
                player.sendMessage("§7Damage Dealt: §e" + String.format("%.1f", combatData.getDamageDealt()));
                player.sendMessage("§7Hits: §e" + combatData.getHits());
                player.sendMessage("§7Kills: §e" + combatData.getKills());
                player.sendMessage("§7Deaths: §e" + combatData.getDeaths());
                break;
            default:
                player.sendMessage("§cUnknown subcommand! Use /combat stats");
                break;
        }

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
        player.sendMessage("§aTeleporting to your island...");
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
        player.sendMessage("§aTeleporting to hub...");
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
    
    public void addSkillXP(Player player, de.noctivag.plugin.skyblock.SkyblockManager.SkyblockSkill skill, double xp) {
        // Placeholder implementation
        // if (skillsSystem != null) {
        //     // TODO: Implement actual skill XP addition
        // }
    }
    
    // Missing method for MiningCommand and MiningAreaSystem
}
