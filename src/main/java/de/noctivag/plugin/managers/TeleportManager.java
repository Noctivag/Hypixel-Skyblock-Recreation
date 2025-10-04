package de.noctivag.plugin.managers;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportManager {
    private final Plugin plugin;
    private final Map<UUID, Location> lastLocationByPlayer = new HashMap<>();
    private Location spawnLocation;

    public TeleportManager(Plugin plugin) {
        this.plugin = plugin;
        // Fix: Only set spawnLocation if worlds are loaded
        if (!plugin.getServer().getWorlds().isEmpty()) {
            World world = plugin.getServer().getWorlds().get(0);
            if (world != null) this.spawnLocation = world.getSpawnLocation();
        } else {
            this.spawnLocation = null;
            plugin.getLogger().warning("No worlds loaded during TeleportManager initialization; spawn location not set.");
        }
    }

    public void setLastLocation(Player player, Location location) {
        if (player == null || location == null) return;
        lastLocationByPlayer.put(player.getUniqueId(), location.clone());
    }

    public Location getLastLocation(Player player) {
        return lastLocationByPlayer.get(player.getUniqueId());
    }

    public boolean back(Player player) {
        Location last = getLastLocation(player);
        if (last == null) return false;
        player.teleport(last);
        return true;
    }

    public void setSpawn(Location location) {
        this.spawnLocation = location == null ? null : location.clone();
        if (location != null && location.getWorld() != null) {
            location.getWorld().setSpawnLocation(location);
        }
    }

    public Location getSpawn() {
        return spawnLocation;
    }
}
