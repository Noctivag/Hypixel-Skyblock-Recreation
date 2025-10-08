package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.gui.accessory.AccessoryBagGUI;
import de.noctivag.skyblock.items.AccessoryBag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AccessoryBagGUIListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        String title = event.getView().title().toString();
        if (title.contains("Accessory Bag")) {
            event.setCancelled(true);
            // Hier könnten Item-Entfernen/Hinzufügen-Logik etc. ergänzt werden
        }
    }
}
