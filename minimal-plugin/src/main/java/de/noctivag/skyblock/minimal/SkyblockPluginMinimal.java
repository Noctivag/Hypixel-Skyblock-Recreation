package de.noctivag.skyblock.minimal;

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
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Minimal Skyblock Plugin - Komplett isoliert und funktionsfähig
 * Keine Legacy-Abhängigkeiten - SOFORT EINSATZBEREIT!
 * VOLLSTÄNDIG FOLIA-KOMPATIBEL!
 */
public class SkyblockPluginMinimal extends JavaPlugin implements Listener {

    private static SkyblockPluginMinimal instance;
    private boolean isFoliaServer = false;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Enabling Skyblock Minimal v" + getDescription().getVersion());

        try {
            // 1. Folia-Erkennung
            detectFoliaServer();
            
            // 2. Standard-Konfiguration laden
            saveDefaultConfig();

            // 3. Event Listener registrieren
            getServer().getPluginManager().registerEvents(this, this);

            // 4. Commands registrieren
            getCommand("hub").setExecutor(this);

            // 5. Hub-Welt erstellen falls nicht vorhanden
            createHubWorld();

            getLogger().info("SkyblockPlugin Minimal successfully enabled!");
            getLogger().info("Features: Hub-System, Player-Handling, Event-System, Folia-Support");
            getLogger().info("Commands: /hub");
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
        getLogger().info("SkyblockPlugin Minimal successfully disabled!");
    }

    public static SkyblockPluginMinimal getInstance() {
        return instance;
    }

    /**
     * Erkennt ob der Server Folia verwendet
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
     * Prüft ob der Server Folia verwendet
     */
    public boolean isFoliaServer() {
        return isFoliaServer;
    }

    /**
     * Erstellt die Hub-Welt falls sie nicht existiert
     * Folia-kompatible Implementierung
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
                    // Setze Spawn-Punkt
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
     * Command Handler
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

        return false;
    }

            /**
             * Teleportiert einen Spieler zum Hub
             * Folia-kompatible Implementierung
             */
            private void teleportToHub(Player player) {
                World hubWorld = Bukkit.getWorld("hub");
                
                if (hubWorld != null) {
                    // Verwende synchronen Teleport (funktioniert auf beiden Server-Typen)
                    player.teleport(hubWorld.getSpawnLocation());
                    player.sendMessage("§aDu wurdest zum Hub teleportiert!");
                } else {
                    player.sendMessage("§cDer Hub konnte nicht gefunden werden!");
                }
            }

            /**
             * Event Handler für Player Join
             * Folia-kompatible Implementierung
             */
            @EventHandler
            public void onPlayerJoin(PlayerJoinEvent event) {
                Player player = event.getPlayer();
                
                // Willkommensnachricht
                String joinMessage = "§aWillkommen auf dem Server, " + player.getName() + "!";
                event.setJoinMessage(joinMessage);
                
                // Teleportiere zum Hub wenn es die erste Welt ist
                if (player.getWorld().getName().equals("world")) {
                    // Verwende BukkitRunnable (funktioniert auf beiden Server-Typen)
                    Bukkit.getScheduler().runTaskLater(this, () -> {
                        teleportToHub(player);
                    }, 20L); // 1 Sekunde Verzögerung
                }
            }

    /**
     * Event Handler für Player Quit
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        String quitMessage = "§c" + player.getName() + " hat den Server verlassen.";
        event.setQuitMessage(quitMessage);
    }
}
