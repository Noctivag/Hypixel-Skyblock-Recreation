package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.RanksGUI;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

public class RanksGUIListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;

    public RanksGUIListener(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player admin)) return;
        if (!(event.getInventory().getHolder() instanceof RanksGUI)) return;
        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getItemMeta() == null) return;
        ItemMeta meta = clicked.getItemMeta();
        String display = PlainTextComponentSerializer.plainText().serialize(meta.displayName());
        if (display == null) return;

        if (display.contains("Zurück")) {
            new de.noctivag.skyblock.gui.AdminMenu(SkyblockPlugin).open(admin);
            return;
        }

        if (display.contains("Spieler wechseln") || display.contains("Spieler auswählen")) {
            new de.noctivag.skyblock.gui.PlayerSelectorMenu(SkyblockPlugin).open(admin);
            return;
        }

        // Left-click: set rank; Right-click: open permission editor
        boolean rightClick = event.isRightClick();
        for (String key : SkyblockPlugin.getRankManager().getAllRankKeys()) {
            String disp = SkyblockPlugin.getRankManager().getDisplayName(key);
            if (display.contains(disp)) {
                if (rightClick) {
                    new de.noctivag.skyblock.gui.RankPermissionsGUI(SkyblockPlugin, key).open(admin);
                } else {
                    Player target = ((de.noctivag.skyblock.gui.RanksGUI) event.getInventory().getHolder()).getTarget();
                    if (target == null) target = admin; // fallback to admin
                    SkyblockPlugin.getRankManager().setPlayerRank(target, key);
                    admin.sendMessage("§aRang gesetzt für §e" + target.getName() + "§a: §e" + disp);
                }
                return;
            }
        }
    }
}


