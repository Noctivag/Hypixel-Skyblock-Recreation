package de.noctivag.skyblock.systems;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.worlds.RollingRestartWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * Hub-Spawn-System - Alle Spieler spawnen immer im Hub
 * Deaktiviert Standard-Minecraft-Welt und leitet alle Spieler zum Hub um
 */
public class HubSpawnSystem implements Listener {
    
    private final SkyblockPluginRefactored plugin;
    private final RollingRestartWorldManager rollingRestartManager;
    
    public HubSpawnSystem(SkyblockPluginRefactored plugin) {
        this.plugin = plugin;
        Object manager = plugin.getRollingRestartWorldManager();
        this.rollingRestartManager = (manager instanceof RollingRestartWorldManager) ? 
            (RollingRestartWorldManager) manager : null;
        
        // Registriere Events
        Bukkit.getPluginManager().registerEvents(this, plugin);
        
        plugin.getLogger().info("Hub-Spawn-System initialized - All players will spawn in Hub");
    }
    
    /**
     * Teleportiert einen Spieler zum Hub (Folia-kompatibel mit teleportAsync)
     */
    public void teleportToHubAsync(Player player) {
        plugin.getLogger().info("Attempting to teleport player " + player.getName() + " to Hub (Async)...");
        
        if (rollingRestartManager != null) {
            World hub = rollingRestartManager.getLiveWorld("hub");
            if (hub != null) {
                Location hubSpawn = hub.getSpawnLocation();
                if (hubSpawn != null) {
                    player.teleportAsync(hubSpawn).thenAccept(success -> {
                        if (success) {
                            player.sendMessage(Component.text("§aWillkommen im Skyblock Hub!")
                                .color(NamedTextColor.GREEN));
                            plugin.getLogger().info("Successfully teleported " + player.getName() + " to Hub: " + hub.getName());
                        } else {
                            plugin.getLogger().warning("Failed to teleport " + player.getName() + " to Hub");
                        }
                    });
                    return;
                }
            } else {
                plugin.getLogger().warning("Hub world not found via RollingRestartManager");
            }
        }
        
        // Fallback: Standard-Hub
        World defaultHub = Bukkit.getWorld("hub");
        if (defaultHub != null) {
            player.teleportAsync(defaultHub.getSpawnLocation()).thenAccept(success -> {
                if (success) {
                    player.sendMessage(Component.text("§aWillkommen im Skyblock Hub!")
                        .color(NamedTextColor.GREEN));
                    plugin.getLogger().info("Successfully teleported " + player.getName() + " to default Hub");
                } else {
                    plugin.getLogger().warning("Failed to teleport " + player.getName() + " to default Hub");
                }
            });
            return;
        }
        
        // Fallback: Hub-A oder Hub-B direkt
        World hubA = Bukkit.getWorld("hub_a");
        if (hubA != null) {
            player.teleportAsync(hubA.getSpawnLocation()).thenAccept(success -> {
                if (success) {
                    player.sendMessage(Component.text("§aWillkommen im Skyblock Hub!")
                        .color(NamedTextColor.GREEN));
                    plugin.getLogger().info("Successfully teleported " + player.getName() + " to hub_a");
                } else {
                    plugin.getLogger().warning("Failed to teleport " + player.getName() + " to hub_a");
                }
            });
            return;
        }
        
        World hubB = Bukkit.getWorld("hub_b");
        if (hubB != null) {
            player.teleportAsync(hubB.getSpawnLocation()).thenAccept(success -> {
                if (success) {
                    player.sendMessage(Component.text("§aWillkommen im Skyblock Hub!")
                        .color(NamedTextColor.GREEN));
                    plugin.getLogger().info("Successfully teleported " + player.getName() + " to hub_b");
                } else {
                    plugin.getLogger().warning("Failed to teleport " + player.getName() + " to hub_b");
                }
            });
            return;
        }
        
        // Letzter Fallback: Standard-Welt (falls Hub nicht verfügbar)
        World world = Bukkit.getWorld("world");
        if (world != null) {
            player.teleportAsync(world.getSpawnLocation()).thenAccept(success -> {
                if (success) {
                    player.sendMessage(Component.text("§eHub nicht verfügbar - Spawn in Standard-Welt")
                        .color(NamedTextColor.YELLOW));
                    plugin.getLogger().warning("Hub not available, teleported " + player.getName() + " to standard world");
                } else {
                    plugin.getLogger().warning("Failed to teleport " + player.getName() + " to standard world");
                }
            });
        } else {
            player.sendMessage(Component.text("§cKeine Welt verfügbar!")
                .color(NamedTextColor.RED));
            plugin.getLogger().severe("No worlds available for teleportation!");
        }
    }

    /**
     * Teleportiert einen Spieler zum Hub (Synchron für normale Server)
     */
    public void teleportToHub(Player player) {
        plugin.getLogger().info("Attempting to teleport player " + player.getName() + " to Hub...");
        
        if (rollingRestartManager != null) {
            World hub = rollingRestartManager.getLiveWorld("hub");
            if (hub != null) {
                Location hubSpawn = hub.getSpawnLocation();
                if (hubSpawn != null) {
                    player.teleport(hubSpawn);
                    player.sendMessage(Component.text("§aWillkommen im Skyblock Hub!")
                        .color(NamedTextColor.GREEN));
                    plugin.getLogger().info("Successfully teleported " + player.getName() + " to Hub: " + hub.getName());
                    return;
                }
            } else {
                plugin.getLogger().warning("Hub world not found via RollingRestartManager");
            }
        }
        
        // Fallback: Standard-Hub
        World defaultHub = Bukkit.getWorld("hub");
        if (defaultHub != null) {
            player.teleport(defaultHub.getSpawnLocation());
            player.sendMessage(Component.text("§aWillkommen im Skyblock Hub!")
                .color(NamedTextColor.GREEN));
            plugin.getLogger().info("Successfully teleported " + player.getName() + " to default Hub");
            return;
        }
        
        // Fallback: Hub-A oder Hub-B direkt
        World hubA = Bukkit.getWorld("hub_a");
        if (hubA != null) {
            player.teleport(hubA.getSpawnLocation());
            player.sendMessage(Component.text("§aWillkommen im Skyblock Hub!")
                .color(NamedTextColor.GREEN));
            plugin.getLogger().info("Successfully teleported " + player.getName() + " to hub_a");
            return;
        }
        
        World hubB = Bukkit.getWorld("hub_b");
        if (hubB != null) {
            player.teleport(hubB.getSpawnLocation());
            player.sendMessage(Component.text("§aWillkommen im Skyblock Hub!")
                .color(NamedTextColor.GREEN));
            plugin.getLogger().info("Successfully teleported " + player.getName() + " to hub_b");
            return;
        }
        
        // Letzter Fallback: Standard-Welt (falls Hub nicht verfügbar)
        World world = Bukkit.getWorld("world");
        if (world != null) {
            player.teleport(world.getSpawnLocation());
            player.sendMessage(Component.text("§eHub nicht verfügbar - Spawn in Standard-Welt")
                .color(NamedTextColor.YELLOW));
            plugin.getLogger().warning("Hub not available, teleported " + player.getName() + " to standard world");
        } else {
            player.sendMessage(Component.text("§cKeine Welt verfügbar!")
                .color(NamedTextColor.RED));
            plugin.getLogger().severe("No worlds available for teleportation!");
        }
    }
    
    /**
     * Prüft ob eine Welt der Hub ist
     */
    private boolean isHubWorld(World world) {
        if (world == null) return false;
        
        String worldName = world.getName();
        return worldName.equals("hub") || 
               worldName.equals("hub_a") || 
               worldName.equals("hub_b");
    }
    
    /**
     * Prüft ob eine Welt eine Standard-Minecraft-Welt ist
     */
    private boolean isStandardMinecraftWorld(World world) {
        if (world == null) return false;
        
        String worldName = world.getName();
        return worldName.equals("world") || 
               worldName.equals("world_nether") || 
               worldName.equals("world_the_end");
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Folia-kompatible Teleportation zum Hub
        if (isFoliaServer()) {
            // Bei Folia: Verwende Virtual Thread für verzögerte Teleportation
            Thread.ofVirtual().start(() -> {
                try {
                    Thread.sleep(1000); // 1 Sekunde Verzögerung
                    if (player.isOnline()) {
                        // Bei Folia: Verwende teleportAsync für Threading-Kompatibilität
                        teleportToHubAsync(player);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        } else {
            // Bei normalen Servern: Verwende BukkitRunnable
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.isOnline()) {
                        teleportToHub(player);
                    }
                }
            }.runTaskLater(plugin, 20L); // 1 Sekunde Verzögerung
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        
        // Setze Respawn-Location zum Hub
        if (rollingRestartManager != null) {
            World hub = rollingRestartManager.getLiveWorld("hub");
            if (hub != null) {
                event.setRespawnLocation(hub.getSpawnLocation());
                return;
            }
        }
        
        // Fallback: Standard-Hub
        World defaultHub = Bukkit.getWorld("hub");
        if (defaultHub != null) {
            event.setRespawnLocation(defaultHub.getSpawnLocation());
            return;
        }
        
        // Letzter Fallback: Standard-Welt
        World world = Bukkit.getWorld("world");
        if (world != null) {
            event.setRespawnLocation(world.getSpawnLocation());
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        World toWorld = event.getTo().getWorld();
        
        // Verhindere Teleportation zu Standard-Minecraft-Welten
        if (isStandardMinecraftWorld(toWorld)) {
            event.setCancelled(true);
            player.sendMessage(Component.text("§cDu kannst nicht zu Standard-Minecraft-Welten teleportieren!")
                .color(NamedTextColor.RED));
            player.sendMessage(Component.text("§7Verwende §e/hub §7um zum Hub zu gelangen!")
                .color(NamedTextColor.GRAY));
            return;
        }
        
        // Wenn Spieler zu einer Welt außerhalb des Hub-Systems teleportiert wird,
        // leite ihn zum Hub um (außer es ist bereits der Hub)
        if (!isHubWorld(toWorld) && !isSkyblockWorld(toWorld)) {
            event.setCancelled(true);
            teleportToHub(player);
            player.sendMessage(Component.text("§eDu wurdest zum Hub teleportiert!")
                .color(NamedTextColor.YELLOW));
        }
    }
    
    /**
     * Prüft ob eine Welt eine Skyblock-Welt ist
     */
    private boolean isSkyblockWorld(World world) {
        if (world == null) return false;
        
        String worldName = world.getName();
        return worldName.startsWith("skyblock_") ||
               worldName.startsWith("gold_mine") ||
               worldName.startsWith("deep_caverns") ||
               worldName.startsWith("spiders_den") ||
               worldName.startsWith("blazing_fortress") ||
               worldName.startsWith("end") ||
               worldName.startsWith("park") ||
               worldName.startsWith("dwarven_mines") ||
               worldName.startsWith("crystal_hollows") ||
               worldName.startsWith("crimson_isle") ||
               worldName.startsWith("kuudra") ||
               worldName.startsWith("rift") ||
               worldName.startsWith("garden") ||
               worldName.startsWith("dungeon") ||
               worldName.startsWith("minigame") ||
               worldName.startsWith("event_arena") ||
               worldName.startsWith("pvp_arena");
    }
    
    /**
     * Setzt den Server-Spawn zum Hub (über World-Spawn-Location)
     */
    public void setServerSpawnToHub() {
        if (rollingRestartManager != null) {
            World hub = rollingRestartManager.getLiveWorld("hub");
            if (hub != null) {
                // Setze die Spawn-Location der Hub-Welt
                hub.setSpawnLocation(hub.getSpawnLocation());
                plugin.getLogger().info("Hub spawn location configured: " + hub.getName());
                return;
            }
        }
        
        // Fallback: Standard-Hub
        World defaultHub = Bukkit.getWorld("hub");
        if (defaultHub != null) {
            defaultHub.setSpawnLocation(defaultHub.getSpawnLocation());
            plugin.getLogger().info("Default Hub spawn location configured");
        }
    }
    
    /**
     * Deaktiviert Standard-Minecraft-Welten (Folia-kompatibel)
     */
    public void disableStandardWorlds() {
        // Prüfe ob Folia läuft
        if (isFoliaServer()) {
            plugin.getLogger().info("Folia detected - Standard worlds will be handled by server configuration");
            plugin.getLogger().info("Standard Minecraft worlds disabled - All players will spawn in Hub");
            return;
        }
        
        // Für normale Server: Entlade Standard-Minecraft-Welten falls sie geladen sind
        try {
            World world = Bukkit.getWorld("world");
            if (world != null && world.getPlayers().isEmpty()) {
                Bukkit.unloadWorld(world, false);
                plugin.getLogger().info("Unloaded standard world: world");
            }
            
            World nether = Bukkit.getWorld("world_nether");
            if (nether != null && nether.getPlayers().isEmpty()) {
                Bukkit.unloadWorld(nether, false);
                plugin.getLogger().info("Unloaded standard world: world_nether");
            }
            
            World end = Bukkit.getWorld("world_the_end");
            if (end != null && end.getPlayers().isEmpty()) {
                Bukkit.unloadWorld(end, false);
                plugin.getLogger().info("Unloaded standard world: world_the_end");
            }
        } catch (UnsupportedOperationException e) {
            plugin.getLogger().warning("Cannot unload worlds on this server type: " + e.getMessage());
        }
        
        plugin.getLogger().info("Standard Minecraft worlds disabled - All players will spawn in Hub");
    }
    
    /**
     * Prüft ob der Server Folia verwendet
     */
    private boolean isFoliaServer() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
