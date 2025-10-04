package de.noctivag.skyblock.managers;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VanishManager {
    private final SkyblockPlugin plugin;
    private final Set<UUID> vanished = ConcurrentHashMap.newKeySet();

    public VanishManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isVanished(Player player) {
        return vanished.contains(player.getUniqueId());
    }

    public void setVanished(Player player, boolean vanish) {
        if (vanish) {
            if (vanished.add(player.getUniqueId())) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.hidePlayer(plugin, player);
                }
                player.sendMessage("§7Du bist nun im Vanish-Modus.");
            }
        } else {
            if (vanished.remove(player.getUniqueId())) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.showPlayer(plugin, player);
                }
                player.sendMessage("§7Du hast den Vanish-Modus verlassen.");
            }
        }
    }

    public void toggleVanish(Player player) {
        setVanished(player, !isVanished(player));
    }

    public Set<UUID> getVanished() {
        return vanished;
    }
}

