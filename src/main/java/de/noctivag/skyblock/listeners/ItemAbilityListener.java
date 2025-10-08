package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.abilities.AILaserAbility;
import de.noctivag.skyblock.abilities.ItemAbility;
import de.noctivag.skyblock.items.SkyblockItem;
import de.noctivag.skyblock.items.SkyblockItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Listener für Item-Abilities (Rechtsklick auf Skyblock-Item)
 */
public class ItemAbilityListener implements Listener {
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) return;
        // Demo: Prüfe auf unser Beispiel-Item
        if (item.getItemMeta() != null && item.getItemMeta().hasDisplayName()
                && item.getItemMeta().displayName().toString().contains("Aspect of the AI")) {
            // Cooldown prüfen
            long now = System.currentTimeMillis();
            long last = cooldowns.getOrDefault(player.getUniqueId(), 0L);
            ItemAbility ability = new AILaserAbility();
            if (now - last < ability.getCooldownTicks() * 50L) {
                player.sendMessage("§cAbility ist noch im Cooldown!");
                return;
            }
            cooldowns.put(player.getUniqueId(), now);
            ability.onUse(player, event);
        }
    }
}
