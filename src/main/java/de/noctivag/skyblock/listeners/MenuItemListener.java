package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.MainMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

public class MenuItemListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;

    public MenuItemListener(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.NETHER_STAR) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return;
        }

        String displayName = Component.text().append(meta.displayName()).build().toString();
        if (displayName.contains("§6✧ Hauptmenü ✧") || displayName.contains("Hauptmenü")) {
            event.setCancelled(true);
            new MainMenu(SkyblockPlugin, player).open();
            player.sendMessage(Component.text("§aHauptmenü geöffnet!"));
        }
    }
}
