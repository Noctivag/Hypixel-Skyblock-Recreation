package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class EventMenuListener implements Listener {
    private final Plugin plugin;

    public EventMenuListener(Plugin plugin) {
        this.plugin = plugin;
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
            new MainMenu(plugin).open(player);
            return;
        }

        if (display.contains("Drachen-Event")) {
            player.closeInventory();
            plugin.joinEvent(player, "dragon");
            return;
        }

        if (display.contains("Wither-Boss")) {
            player.closeInventory();
            plugin.joinEvent(player, "wither");
            return;
        }

        if (display.contains("Zombie-Horde")) {
            player.closeInventory();
            plugin.joinEvent(player, "zombie");
            return;
        }

        if (display.contains("Nächste Events") || display.toLowerCase().contains("nächste")) {
            player.sendMessage("§eNächste Events: Drachen-Event in 30 Minuten, Wither-Boss in 1 Stunde.");
            return;
        }

        if (display.contains("Event-Shop") || display.toLowerCase().contains("event-shop")) {
            player.sendMessage("§eEvent-Shop ist noch nicht implementiert.");
        }
    }
}
