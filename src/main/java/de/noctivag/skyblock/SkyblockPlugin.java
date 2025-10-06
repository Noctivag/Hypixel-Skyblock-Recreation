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

import de.noctivag.skyblock.database.DatabaseManager;
import de.noctivag.skyblock.worlds.WorldManager;

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
    private de.noctivag.skyblock.mobs.MobManager mobManager;
    private de.noctivag.skyblock.mobs.SpawningService spawningService;
    private de.noctivag.skyblock.brewing.AdvancedBrewingSystem brewingManager;
    
    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Enabling Skyblock Plugin v" + getPluginMeta().getVersion());

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
        
                // Shutdown services
                if (spawningService != null) {
                    spawningService.shutdown();
                }
                if (mobManager != null) {
                    mobManager.shutdown();
                }
                if (questSystem != null) {
                    questSystem.shutdown();
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
        
        // Initialize mob manager
        mobManager = new de.noctivag.skyblock.mobs.MobManager(this);
        mobManager.initialize();
        
                // Initialize spawning service
                spawningService = new de.noctivag.skyblock.mobs.SpawningService(this, mobManager);
                spawningService.initialize();

        // Initialize quest system
        questSystem = new de.noctivag.skyblock.quests.QuestSystem(this);
        questSystem.initialize();

        // Initialize brewing manager
        brewingManager = new de.noctivag.skyblock.brewing.AdvancedBrewingSystem(this, (de.noctivag.skyblock.database.MultiServerDatabaseManager) databaseManager);
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
            player.sendMessage("§aSkyblock Plugin v" + getPluginMeta().getVersion());
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
    
    /**
     * Get mob manager
     */
    public de.noctivag.skyblock.mobs.MobManager getMobManager() {
        return mobManager;
    }
    
    /**
     * Get spawning service
     */
    public de.noctivag.skyblock.mobs.SpawningService getSpawningService() {
        return spawningService;
    }

    /**
     * Get economy manager (placeholder)
     */
    public de.noctivag.skyblock.economy.EconomyInterface getEconomyManager() {
        return new de.noctivag.skyblock.economy.EconomyInterface() {
            @Override
            public void depositMoney(Player player, int amount) {
                // Placeholder implementation
                player.sendMessage("§aYou received " + amount + " coins!");
            }
            
            @Override
            public void depositMoney(Player player, double amount) {
                // Placeholder implementation
                player.sendMessage("§aYou received " + amount + " coins!");
            }
            
            @Override
            public double getBalance(Player player) {
                return 1000.0; // Placeholder
            }
            
            @Override
            public void giveMoney(Player player, double amount) {
                player.sendMessage("§aYou received " + amount + " coins!");
            }
            
            @Override
            public boolean withdrawMoney(Player player, double amount) {
                player.sendMessage("§cYou spent " + amount + " coins!");
                return true; // Placeholder - always successful
            }
            
            @Override
            public void setBalanceSilent(Player player, double amount) {
                // Placeholder
            }
            
            @Override
            public void resetBalance(Player player) {
                player.sendMessage("§eYour balance has been reset!");
            }
            
            @Override
            public boolean hasBalance(Player player, double amount) {
                return true; // Placeholder
            }
            
            @Override
            public boolean hasCostExemption(Player player) {
                return false; // Placeholder
            }
            
            @Override
            public String formatMoney(double amount) {
                return String.format("%.2f coins", amount);
            }
        };
    }

    /**
     * Get accessory system (placeholder)
     */
    public Object getAccessorySystem() {
        return null; // Placeholder
    }

    /**
     * Get collections system (placeholder)
     */
    public Object getCollectionsSystem() {
        return null; // Placeholder
    }

    /**
     * Get advanced armor system (placeholder)
     */
    public Object getAdvancedArmorSystem() {
        return null; // Placeholder
    }

    /**
     * Get compatibility manager (placeholder)
     */
    public Object getCompatibilityManager() {
        return null; // Placeholder
    }

    /**
     * Get slayer system (placeholder)
     */
    public Object getSlayerSystem() {
        return null; // Placeholder
    }

    /**
     * Get fishing system (placeholder)
     */
    public Object getFishingSystem() {
        return null; // Placeholder
    }

    /**
     * Get magic system (placeholder)
     */
    public Object getMagicSystem() {
        return null; // Placeholder
    }

    /**
     * Get combat system (placeholder)
     */
    public Object getCombatSystem() {
        return null; // Placeholder
    }

    /**
     * Get items system (placeholder)
     */
    public Object getItemsSystem() {
        return null; // Placeholder
    }

    /**
     * Get maintenance manager (placeholder)
     */
    public Object getMaintenanceManager() {
        return null; // Placeholder
    }

    /**
     * Get event manager (placeholder)
     */
    public Object getEventManager() {
        return null; // Placeholder
    }

    // Quest System
    private de.noctivag.skyblock.quests.QuestSystem questSystem;

    /**
     * Get quest system
     */
    public de.noctivag.skyblock.quests.QuestSystem getQuestSystem() {
        return questSystem;
    }

    /**
     * Get brewing manager
     */
    public de.noctivag.skyblock.brewing.AdvancedBrewingSystem getBrewingManager() {
        return brewingManager;
    }

    /**
     * Get cosmetics manager (placeholder)
     */
    public Object getCosmeticsManager() {
        return null; // Placeholder
    }

    /**
     * Get advanced NPC system (placeholder)
     */
    public Object getAdvancedNPCSystem() {
        return null; // Placeholder
    }

    /**
     * Get skyblock manager (placeholder)
     */
    public Object getSkyblockManager() {
        return null; // Placeholder
    }

    /**
     * Get hypixel proxy system (placeholder)
     */
    public Object getHypixelProxySystem() {
        return null; // Placeholder
    }

    /**
     * Get central database system (placeholder)
     */
    public Object getCentralDatabaseSystem() {
        return null; // Placeholder
    }

    /**
     * Get multithreading manager (placeholder)
     */
    public Object getMultithreadingManager() {
        return null; // Placeholder
    }

    /**
     * Get async system manager (placeholder)
     */
    public Object getAsyncSystemManager() {
        return null; // Placeholder
    }

    /**
     * Get async database manager (placeholder)
     */
    public Object getAsyncDatabaseManager() {
        return null; // Placeholder
    }

    /**
     * Get async config manager (placeholder)
     */
    public Object getAsyncConfigManager() {
        return null; // Placeholder
    }

    /**
     * Get core platform (placeholder)
     */
    public Object getCorePlatform() {
        return null; // Placeholder
    }

    /**
     * Get advanced island system (placeholder)
     */
    public Object getAdvancedIslandSystem() {
        return null; // Placeholder
    }

    /**
     * Get booster cookie system (placeholder)
     */
    public Object getBoosterCookieSystem() {
        return null; // Placeholder
    }

    /**
     * Get recipe book system (placeholder)
     */
    public Object getRecipeBookSystem() {
        return null; // Placeholder
    }

    /**
     * Get calendar system (placeholder)
     */
    public Object getCalendarSystem() {
        return null; // Placeholder
    }

    /**
     * Get live world (placeholder)
     */
    public Object getLiveWorld(String worldName) {
        return null; // Placeholder
    }

}
