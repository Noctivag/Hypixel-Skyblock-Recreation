package de.noctivag.skyblock.abilities;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Interface für Item-Fähigkeiten (z.B. Rechtsklick)
 */
public interface ItemAbility {
    String getName();
    String getDescription();
    int getManaCost();
    int getCooldownTicks();
    void onUse(Player player, PlayerInteractEvent event);
}
