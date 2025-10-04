package de.noctivag.plugin.listeners;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.gui.RanksGUI;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class RanksGUIListener implements Listener {
    private final Plugin plugin;

    public RanksGUIListener(Plugin plugin) {
        this.plugin = plugin;
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
            new de.noctivag.plugin.gui.AdminMenu(plugin).open(admin);
            return;
        }

        if (display.contains("Spieler wechseln") || display.contains("Spieler auswählen")) {
            new de.noctivag.plugin.gui.PlayerSelectorMenu(plugin).open(admin);
            return;
        }

        // Left-click: set rank; Right-click: open permission editor
        boolean rightClick = event.isRightClick();
        for (String key : plugin.getRankManager().getAllRankKeys()) {
            String disp = plugin.getRankManager().getDisplayName(key);
            if (display.contains(disp)) {
                if (rightClick) {
                    new de.noctivag.plugin.gui.RankPermissionsGUI(plugin, key).open(admin);
                } else {
                    Player target = ((de.noctivag.plugin.gui.RanksGUI) event.getInventory().getHolder()).getTarget();
                    if (target == null) target = admin; // fallback to admin
                    plugin.getRankManager().setPlayerRank(target, key);
                    admin.sendMessage("§aRang gesetzt für §e" + target.getName() + "§a: §e" + disp);
                }
                return;
            }
        }
    }
}


