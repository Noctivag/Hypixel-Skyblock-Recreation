package de.noctivag.plugin.listeners;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.gui.RankPermissionsGUI;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class RankPermissionsGUIListener implements Listener {
    private final Plugin plugin;

    public RankPermissionsGUIListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player admin)) return;
        if (!(event.getInventory().getHolder() instanceof RankPermissionsGUI permsGUI)) return;
        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getItemMeta() == null) return;
        ItemMeta meta = clicked.getItemMeta();
        String display = PlainTextComponentSerializer.plainText().serialize(meta.displayName());
        if (display == null) return;

        if (display.contains("Zurück")) {
            new de.noctivag.plugin.gui.RanksGUI(plugin, admin).open(admin);
            return;
        }

        // Toggle permission: green (enabled) -> remove; gray (disabled) -> add
        String permission = display.replace("§a", "").replace("§c", "");
        String rankKey = permsGUI.getRankKey();

        if (plugin.getRankManager().hasPermission(rankKey, permission)) {
            plugin.getRankManager().removePermission(rankKey, permission);
            admin.sendMessage("§ePermission entfernt: §c" + permission);
        } else {
            plugin.getRankManager().addPermission(rankKey, permission);
            admin.sendMessage("§ePermission hinzugefügt: §a" + permission);
        }

        // Refresh GUI
        new de.noctivag.plugin.gui.RankPermissionsGUI(plugin, rankKey).open(admin);
    }
}


