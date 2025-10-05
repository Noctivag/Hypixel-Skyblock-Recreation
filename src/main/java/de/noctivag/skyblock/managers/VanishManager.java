package de.noctivag.skyblock.managers;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;

public class VanishManager {
    private final SkyblockPlugin SkyblockPlugin;
    private final Set<UUID> vanished = ConcurrentHashMap.newKeySet();

    public VanishManager(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    public boolean isVanished(Player player) {
        return vanished.contains(player.getUniqueId());
    }

    public void setVanished(Player player, boolean vanish) {
        if (vanish) {
            if (vanished.add(player.getUniqueId())) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.hidePlayer(SkyblockPlugin, player);
                }
                player.sendMessage(Component.text("§7Du bist nun im Vanish-Modus."));
            }
        } else {
            if (vanished.remove(player.getUniqueId())) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.showPlayer(SkyblockPlugin, player);
                }
                player.sendMessage(Component.text("§7Du hast den Vanish-Modus verlassen."));
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

