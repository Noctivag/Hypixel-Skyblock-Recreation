package de.noctivag.skyblock.skyblock;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Prevents non-members from placing/breaking blocks on islands.
 * Owner and members are allowed. Operators and players with "island.bypass"
 * permission can bypass protection.
 */
public class IslandProtectionListener implements Listener {
    private final IslandManager islandManager;

    public IslandProtectionListener(SkyblockPlugin plugin) {
        this.islandManager = IslandManager.getInstance(plugin);
    }

    private boolean canModify(Player player, de.noctivag.plugin.skyblock.IslandManager.Island island) {
        if (player.isOp() || player.hasPermission("island.bypass")) return true;
        if (island == null) return true; // not an island world
        return island.isMember(player.getUniqueId());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        var world = e.getBlock().getWorld();
        var island = islandManager.getIslandByWorldName(world.getName());
        if (island == null) return;
        if (!canModify(p, island)) {
            e.setCancelled(true);
            p.sendMessage("§cDu darfst hier nicht bauen/abbauen. (Insel-Schutz)");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        var world = e.getBlock().getWorld();
        var island = islandManager.getIslandByWorldName(world.getName());
        if (island == null) return;
        if (!canModify(p, island)) {
            e.setCancelled(true);
            p.sendMessage("§cDu darfst hier nicht bauen/abbauen. (Insel-Schutz)");
        }
    }
}
