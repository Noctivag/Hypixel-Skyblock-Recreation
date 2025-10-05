package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.RankPermissionsGUI;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

public class RankPermissionsGUIListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;

    public RankPermissionsGUIListener(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
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
            new de.noctivag.skyblock.gui.RanksGUI(SkyblockPlugin, admin).open(admin);
            return;
        }

        // Toggle permission: green (enabled) -> remove; gray (disabled) -> add
        String permission = display.replace("§a", "").replace("§c", "");
        String rankKey = permsGUI.getRankKey();

        if (SkyblockPlugin.getRankManager().hasPermission(rankKey, permission)) {
            SkyblockPlugin.getRankManager().removePermission(rankKey, permission);
            admin.sendMessage("§ePermission entfernt: §c" + permission);
        } else {
            SkyblockPlugin.getRankManager().addPermission(rankKey, permission);
            admin.sendMessage("§ePermission hinzugefügt: §a" + permission);
        }

        // Refresh GUI
        new de.noctivag.skyblock.gui.RankPermissionsGUI(SkyblockPlugin, rankKey).open(admin);
    }
}


