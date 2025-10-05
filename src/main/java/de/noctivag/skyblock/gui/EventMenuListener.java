package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class EventMenuListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;

    public EventMenuListener(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = PlainTextComponentSerializer.plainText().serialize(event.getView().title());
        // No need to check for null title
        // Match the EventMenu title or fallback contains check
        if (!title.equals("§c§lEvent Menü") && !title.contains("Event")) return;

        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getItemMeta() == null) return;
        ItemMeta meta = clicked.getItemMeta();
        Component nameComp = meta.displayName();
        if (nameComp == null) return;
        String display = PlainTextComponentSerializer.plainText().serialize(nameComp);

        if (display.toLowerCase().contains("zurück") || display.toLowerCase().contains("zurueck") || display.contains("§c§lZurück")) {
            new MainMenu(SkyblockPlugin).open(player);
            return;
        }

        if (display.contains("Drachen-Event")) {
            player.closeInventory();
            SkyblockPlugin.joinEvent(player, "dragon");
            return;
        }

        if (display.contains("Wither-Boss")) {
            player.closeInventory();
            SkyblockPlugin.joinEvent(player, "wither");
            return;
        }

        if (display.contains("Zombie-Horde")) {
            player.closeInventory();
            SkyblockPlugin.joinEvent(player, "zombie");
            return;
        }

        if (display.contains("Nächste Events") || display.toLowerCase().contains("nächste")) {
            player.sendMessage(Component.text("§eNächste Events: Drachen-Event in 30 Minuten, Wither-Boss in 1 Stunde."));
            return;
        }

        if (display.contains("Event-Shop") || display.toLowerCase().contains("event-shop")) {
            player.sendMessage(Component.text("§eEvent-Shop ist noch nicht implementiert."));
        }
    }
}
