package de.noctivag.skyblock;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Main Skyblock Plugin - Folia Compatible
 * Central plugin class that manages all skyblock systems
 */
public class SkyblockPlugin extends JavaPlugin implements Listener {

    private static SkyblockPlugin instance;
    private boolean isFoliaServer = false;
    
    // Core systems
    private DatabaseManager databaseManager;
    private WorldManager worldManager;
    
    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Enabling Skyblock Plugin v" + getDescription().getVersion());

        try {
            // 1. Folia detection
            detectFoliaServer();
            
            // 2. Load default configuration
            saveDefaultConfig();

            // 3. Initialize core systems
            initializeCoreSystems();

            // 4. Register event listeners
            getServer().getPluginManager().registerEvents(this, this);

            // 5. Register commands
            registerCommands();

            // 6. Create hub world if not exists
            createHubWorld();

            getLogger().info("Skyblock Plugin successfully enabled!");
            getLogger().info("Features: Hub-System, Player-Handling, Event-System, Folia-Support");
            if (isFoliaServer) {
                getLogger().info("Folia server detected - Using Folia-compatible features!");
            }

        } catch (Exception e) {
            getLogger().severe("Error during plugin startup: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Skyblock Plugin successfully disabled!");
        
        // Save all player data
        if (databaseManager != null) {
            databaseManager.saveAllPlayerData();
        }
    }

    public static SkyblockPlugin getInstance() {
        return instance;
    }

    /**
     * Detect if the server is using Folia
     */
    private void detectFoliaServer() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFoliaServer = true;
            getLogger().info("Folia server detected!");
        } catch (ClassNotFoundException e) {
            isFoliaServer = false;
            getLogger().info("Standard Bukkit/Spigot server detected");
        }
    }

    /**
     * Check if the server is using Folia
     */
    public boolean isFoliaServer() {
        return isFoliaServer;
    }

    /**
     * Initialize core systems
     */
    private void initializeCoreSystems() {
        // Initialize database manager
        databaseManager = new DatabaseManager(this);
        
        // Initialize world manager
        worldManager = new WorldManager(this);
    }

    /**
     * Register commands
     */
    private void registerCommands() {
        // Register basic commands
        getCommand("hub").setExecutor(this);
        getCommand("skyblock").setExecutor(this);
    }

    /**
     * Create hub world if it doesn't exist
     */
    private void createHubWorld() {
        String hubWorldName = "hub";
        World hubWorld = Bukkit.getWorld(hubWorldName);
        
        if (hubWorld == null) {
            getLogger().info("Creating hub world...");
            
            try {
                WorldCreator worldCreator = new WorldCreator(hubWorldName);
                worldCreator.environment(World.Environment.NORMAL);
                worldCreator.generateStructures(false);
                
                hubWorld = worldCreator.createWorld();
                
                if (hubWorld != null) {
                    // Set spawn point
                    hubWorld.setSpawnLocation(0, 65, 0);
                    getLogger().info("Hub world created successfully!");
                } else {
                    getLogger().severe("Failed to create hub world!");
                }
            } catch (Exception e) {
                if (isFoliaServer) {
                    getLogger().info("Hub world will be created by the server when needed (Folia)");
                } else {
                    getLogger().severe("Failed to create hub world: " + e.getMessage());
                }
            }
        } else {
            getLogger().info("Hub world already exists!");
        }
    }

    /**
     * Command handler
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("hub")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
                return true;
            }

            Player player = (Player) sender;
            teleportToHub(player);
            return true;
        }
        
        if (command.getName().equalsIgnoreCase("skyblock")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
                return true;
            }

            Player player = (Player) sender;
            player.sendMessage("§aSkyblock Plugin v" + getDescription().getVersion());
            player.sendMessage("§7Folia Support: " + (isFoliaServer ? "§aEnabled" : "§cDisabled"));
            return true;
        }

        return false;
    }

    /**
     * Teleport player to hub
     */
    private void teleportToHub(Player player) {
        World hubWorld = Bukkit.getWorld("hub");
        
        if (hubWorld != null) {
            // Use synchronous teleport (works on both server types)
            player.teleport(hubWorld.getSpawnLocation());
            player.sendMessage("§aDu wurdest zum Hub teleportiert!");
        } else {
            player.sendMessage("§cDer Hub konnte nicht gefunden werden!");
        }
    }

    /**
     * Event handler for player join
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Welcome message
        String joinMessage = "§aWillkommen auf dem Skyblock Server, " + player.getName() + "!";
        event.setJoinMessage(joinMessage);
        
        // Teleport to hub if it's the first world
        if (player.getWorld().getName().equals("world")) {
            // Use BukkitRunnable (works on both server types)
            Bukkit.getScheduler().runTaskLater(this, () -> {
                teleportToHub(player);
            }, 20L); // 1 second delay
        }
        
        // Initialize player data
        if (databaseManager != null) {
            databaseManager.loadPlayerData(player.getUniqueId());
        }
    }

    /**
     * Event handler for player quit
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        String quitMessage = "§c" + player.getName() + " hat den Skyblock Server verlassen.";
        event.setQuitMessage(quitMessage);
        
        // Save player data
        if (databaseManager != null) {
            databaseManager.savePlayerData(player.getUniqueId());
        }
    }

    /**
     * Get database manager
     */
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    /**
     * Get world manager
     */
    public WorldManager getWorldManager() {
        return worldManager;
    }
}
