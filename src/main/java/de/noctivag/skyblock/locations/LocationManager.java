package de.noctivag.skyblock.locations;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.data.DatabaseManager;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

/**
 * Location Manager - Basic implementation
 */
import java.util.*;

public class LocationManager {
    private final SkyblockPlugin SkyblockPlugin;
    private final DatabaseManager databaseManager;

    // Homes: Map<Player-UUID, Map<Home-Name, Home>>
    private final Map<UUID, Map<String, Home>> playerHomes = new HashMap<>();
    // Warps: Map<Warp-Name, Warp>
    private final Map<String, Warp> warps = new HashMap<>();

    public LocationManager(SkyblockPlugin SkyblockPlugin, DatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
    }

    public void openLocationNavigationGUI(Player player) {
        player.sendMessage(Component.text("§aLocation Navigation GUI geöffnet!"));
    }

    public void teleportToLocation(Player player, String location) {
        player.sendMessage("§aTeleportiere zu: " + location);
        // Basic teleportation logic would go here
    }

    // Persist locations to the database (basic stub to satisfy SkyblockPlugin auto-save call)
    public void saveLocations() {
        SkyblockPlugin.getLogger().info("LocationManager: saveLocations called (stub)");
        // TODO: Persist playerHomes and warps to databaseManager
    }

    public Warp getWarp(String warpName) {
        return warps.get(warpName.toLowerCase());
    }

    public Set<String> getHomeNames(Player player) {
        Map<String, Home> homes = playerHomes.getOrDefault(player.getUniqueId(), Collections.emptyMap());
        return new HashSet<>(homes.keySet());
    }

    public Home getHome(Player player, String homeName) {
        Map<String, Home> homes = playerHomes.get(player.getUniqueId());
        if (homes == null) return null;
        return homes.get(homeName.toLowerCase());
    }

    public int getPlayerHomeCount(Player player) {
        Map<String, Home> homes = playerHomes.get(player.getUniqueId());
        return homes != null ? homes.size() : 0;
    }

    public int getMaxHomes() {
        return 5; // Kann konfigurierbar gemacht werden
    }

    public void setHome(Player player, String homeName, org.bukkit.Location location) {
        playerHomes.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>())
            .put(homeName.toLowerCase(), new Home(homeName, player.getName(), location));
    }

    public void setWarp(String warpName, org.bukkit.Location location, String permission, String description) {
        warps.put(warpName.toLowerCase(), new Warp(warpName, location, permission, description));
    }

    public List<String> getWarpNames() {
        return new ArrayList<>(warps.keySet());
    }

    public List<EnhancedWarp> getWarpsByCategory(EnhancedWarp.WarpCategory category) {
        List<EnhancedWarp> result = new ArrayList<>();
        for (Warp w : warps.values()) {
            if (w instanceof EnhancedWarp ew && ew.getCategory() == category) {
                result.add(ew);
            }
        }
        return result;
    }
}
