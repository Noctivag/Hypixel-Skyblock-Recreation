package de.noctivag.plugin.listeners;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.skyblock.SkyblockIsland;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ProtectionListener implements Listener {
    private final Plugin plugin;

    public ProtectionListener(Plugin plugin) {
        this.plugin = plugin;
    }

    private boolean canModify(Player player, org.bukkit.Location loc) {
        if (player.hasPermission("basicsplugin.island.bypass")) return true;
        var manager = plugin.getSkyblockManager();
        if (manager == null) return true; // no skyblock manager -> allow
        SkyblockIsland island = manager.getIslandAt(loc);
        if (island == null) return true; // not in island
        // Allow owner or trusted members
        if (island.getOwner().equals(player.getUniqueId())) return true;
        if (island.isTrusted(player.getUniqueId())) return true;
        return false;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!canModify(player, event.getBlock().getLocation())) {
            event.setCancelled(true);
            player.sendMessage("§cYou cannot build on another player's island.");
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!canModify(player, event.getBlock().getLocation())) {
            event.setCancelled(true);
            player.sendMessage("§cYou cannot break blocks on another player's island.");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        Player player = event.getPlayer();
        if (!canModify(player, event.getClickedBlock().getLocation())) {
            event.setCancelled(true);
            player.sendMessage("§cYou cannot interact with blocks on another player's island.");
        }
    }
}
