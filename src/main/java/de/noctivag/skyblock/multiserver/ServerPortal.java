package de.noctivag.skyblock.multiserver;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;

import java.util.*;
import java.util.UUID;

/**
 * Portal-System für Server-Wechsel
 */
public class ServerPortal implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final ServerSwitcher serverSwitcher;
    private final Map<Location, String> portals = new HashMap<>();
    private final Map<UUID, Long> lastPortalUse = new HashMap<>();
    
    // Cooldown für Portal-Nutzung (in Millisekunden)
    private static final long PORTAL_COOLDOWN = 5000; // 5 Sekunden
    
    public ServerPortal(SkyblockPlugin SkyblockPlugin, ServerSwitcher serverSwitcher) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.serverSwitcher = serverSwitcher;
        SkyblockPlugin.getServer().getPluginManager().registerEvents(this, SkyblockPlugin);
        initializeDefaultPortals();
    }
    
    /**
     * Initialisiert Standard-Portale
     */
    private void initializeDefaultPortals() {
        // Hier würden normalerweise die Portal-Positionen aus der Konfiguration geladen
        SkyblockPlugin.getLogger().info("Initializing default server portals...");
    }
    
    /**
     * Registriert ein Portal an einer bestimmten Position
     */
    public void registerPortal(Location location, String targetServer) {
        portals.put(location, targetServer);
        SkyblockPlugin.getLogger().info("Registered portal at " + location + " -> " + targetServer);
    }
    
    /**
     * Entfernt ein Portal
     */
    public void unregisterPortal(Location location) {
        portals.remove(location);
        SkyblockPlugin.getLogger().info("Unregistered portal at " + location);
    }
    
    /**
     * Prüft ob eine Position ein Portal ist
     */
    public boolean isPortal(Location location) {
        return portals.containsKey(location);
    }
    
    /**
     * Gibt den Ziel-Server für ein Portal zurück
     */
    public String getPortalTarget(Location location) {
        return portals.get(location);
    }
    
    /**
     * Event-Handler für Spieler-Bewegung (Portal-Detection)
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        
        if (to == null) return;
        
        // Prüfe ob Spieler in ein Portal tritt
        if (isPortal(to)) {
            String targetServer = getPortalTarget(to);
            if (targetServer != null) {
                usePortal(player, targetServer);
            }
        }
    }
    
    /**
     * Event-Handler für Spieler-Interaktion (Portal-Aktivierung)
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        
        if (block == null) return;
        
        Location location = block.getLocation();
        
        // Prüfe ob Block ein Portal ist
        if (isPortal(location)) {
            String targetServer = getPortalTarget(location);
            if (targetServer != null) {
                event.setCancelled(true);
                usePortal(player, targetServer);
            }
        }
    }
    
    /**
     * Verwendet ein Portal
     */
    private void usePortal(Player player, String targetServer) {
        UUID playerId = player.getUniqueId();
        
        // Prüfe Cooldown
        if (isOnCooldown(playerId)) {
            long remainingTime = getRemainingCooldown(playerId);
            player.sendMessage(Component.text("§cPortal ist noch " + (remainingTime / 1000) + " Sekunden nicht verfügbar!"));
            return;
        }
        
        // Zeige Portal-Nachricht
        player.sendMessage(Component.text("§6§l=== Portal aktiviert ==="));
        player.sendMessage(Component.text("§eTeleportiere zu: §f" + getServerDisplayName(targetServer)));
        
        // Verwende ServerSwitcher
        serverSwitcher.switchPlayerToServer(player, targetServer)
            .thenAccept(success -> {
                if (success) {
                    // Aktualisiere Cooldown
                    lastPortalUse.put(playerId, java.lang.System.currentTimeMillis());
                }
            });
    }
    
    /**
     * Gibt den Anzeigenamen für einen Server zurück
     */
    private String getServerDisplayName(String serverType) {
        // TODO: Implement proper HypixelProxySystem interface
        // return ((HypixelProxySystem) SkyblockPlugin.getHypixelProxySystem()).getServerTypes().get(serverType) != null ? 
        //     ((HypixelProxySystem) SkyblockPlugin.getHypixelProxySystem()).getServerTypes().get(serverType).getDisplayName() : serverType;
        return serverType; // Placeholder
    }
    
    /**
     * Prüft ob ein Spieler auf Cooldown ist
     */
    private boolean isOnCooldown(UUID playerId) {
        Long lastUse = lastPortalUse.get(playerId);
        if (lastUse == null) {
            return false;
        }
        
        return (java.lang.System.currentTimeMillis() - lastUse) < PORTAL_COOLDOWN;
    }
    
    /**
     * Gibt die verbleibende Cooldown-Zeit zurück
     */
    private long getRemainingCooldown(UUID playerId) {
        Long lastUse = lastPortalUse.get(playerId);
        if (lastUse == null) {
            return 0;
        }
        
        long elapsed = java.lang.System.currentTimeMillis() - lastUse;
        return Math.max(0, PORTAL_COOLDOWN - elapsed);
    }
    
    /**
     * Erstellt ein Portal an einer bestimmten Position
     */
    public void createPortal(Location location, String targetServer, Material portalMaterial) {
        // Setze Portal-Block
        location.getBlock().setType(portalMaterial);
        
        // Registriere Portal
        registerPortal(location, targetServer);
        
        SkyblockPlugin.getLogger().info("Created portal at " + location + " -> " + targetServer);
    }
    
    /**
     * Entfernt ein Portal
     */
    public void removePortal(Location location) {
        // Entferne Portal-Block
        location.getBlock().setType(Material.AIR);
        
        // Entferne Portal-Registrierung
        unregisterPortal(location);
        
        SkyblockPlugin.getLogger().info("Removed portal at " + location);
    }
    
    /**
     * Gibt alle registrierten Portale zurück
     */
    public Map<Location, String> getPortals() {
        return new HashMap<>(portals);
    }
    
    /**
     * Bereinigt alle Portale
     */
    public void clearAllPortals() {
        for (Location location : new HashMap<>(portals).keySet()) {
            removePortal(location);
        }
    }
    
    // Missing method for ServerPortal and ServerSelectionGUI
    public Collection<String> getServerTypes() {
        // Placeholder implementation
        return Arrays.asList("skyblock", "hub", "dungeons", "mining", "farming");
    }
}
