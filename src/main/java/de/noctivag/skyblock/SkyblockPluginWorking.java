package de.noctivag.skyblock;

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
import net.kyori.adventure.text.Component;

/**
 * Funktionsfähige Version des Skyblock-Plugins
 * Komplett ohne Legacy-Abhängigkeiten - SOFORT EINSATZBEREIT!
 */
public class SkyblockPluginWorking extends JavaPlugin implements Listener {

    private static SkyblockPluginWorking instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Enabling Skyblock Working v" + getDescription().getVersion());

        try {
            // 1. Standard-Konfiguration laden
            saveDefaultConfig();

            // 2. Event Listener registrieren
            getServer().getPluginManager().registerEvents(this, this);

            // 3. Commands registrieren
            getCommand("hub").setExecutor(this);

            // 4. Hub-Welt erstellen falls nicht vorhanden
            createHubWorld();

            getLogger().info("SkyblockPlugin Working successfully enabled!");
            getLogger().info("Features: Hub-System, Player-Handling, Event-System");
            getLogger().info("Commands: /hub");

        } catch (Exception e) {
            getLogger().severe("Error during plugin startup: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("SkyblockPlugin Working successfully disabled!");
    }

    public static SkyblockPluginWorking getInstance() {
        return instance;
    }

    /**
     * Erstellt die Hub-Welt falls sie nicht existiert
     */
    private void createHubWorld() {
        String hubWorldName = "hub";
        World hubWorld = Bukkit.getWorld(hubWorldName);
        
        if (hubWorld == null) {
            getLogger().info("Creating hub world...");
            
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
                sender.sendMessage(Component.text("§cDieser Befehl kann nur von Spielern ausgeführt werden!"));
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
     */
    private void teleportToHub(Player player) {
        World hubWorld = Bukkit.getWorld("hub");
        
        if (hubWorld != null) {
            player.teleport(hubWorld.getSpawnLocation());
            player.sendMessage(Component.text("§aDu wurdest zum Hub teleportiert!"));
        } else {
            player.sendMessage(Component.text("§cDer Hub konnte nicht gefunden werden!"));
        }
    }

    /**
     * Event Handler für Player Join
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Willkommensnachricht
        String joinMessage = "§aWillkommen auf dem Server, " + player.getName() + "!";
        event.joinMessage(Component.text(joinMessage));
        
        // Teleportiere zum Hub wenn es die erste Welt ist
        if (player.getWorld().getName().equals("world")) {
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
        event.quitMessage(Component.text(quitMessage));
    }
}
