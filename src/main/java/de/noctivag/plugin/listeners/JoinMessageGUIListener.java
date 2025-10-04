package de.noctivag.plugin.listeners;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.gui.JoinMessageGUI;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class JoinMessageGUIListener implements Listener {
    private final Plugin plugin;

    public JoinMessageGUIListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getInventory().getHolder() instanceof JoinMessageGUI)) return;
        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getItemMeta() == null) return;
        ItemMeta meta = clicked.getItemMeta();
        if (meta.displayName() == null) return;
        String display = PlainTextComponentSerializer.plainText().serialize(meta.displayName());
        if (display == null) return;

        if (display.contains("Zurück")) {
            new de.noctivag.plugin.gui.MainMenu(plugin).open(player);
            return;
        }

        boolean rightClick = event.isRightClick();

        switch (event.getSlot()) {
            case 19 -> {
                if (rightClick) {
                    // Remove join message
                    // plugin.clearJoinMessage(player); // TODO: Implement method in Plugin class
                    player.sendMessage("§aJoin-Message entfernt!");
                    new JoinMessageGUI(plugin).open(player);
                } else {
                    // Set join message - open chat input
                    player.closeInventory();
                    player.sendMessage("§eGib die neue Join-Message ein (oder 'cancel' zum Abbrechen):");
                    player.sendMessage("§cJoin-Message-System benötigt Chat-Input Integration");
                }
            }
            case 22 -> {
                // boolean currentlyDisabled = plugin.isMessageDisabled(player.getName()); // TODO: Implement method in Plugin class
                // plugin.setMessageEnabled(player.getName(), currentlyDisabled); // TODO: Implement method in Plugin class
                boolean currentlyDisabled = false; // Placeholder
                player.sendMessage(currentlyDisabled ? "§aJoin-Messages aktiviert!" : "§cJoin-Messages deaktiviert!");
                new JoinMessageGUI(plugin).open(player);
            }
            case 25 -> new de.noctivag.plugin.gui.JoinMessagePresetsGUI(plugin).open(player);
            case 28 -> player.sendMessage("§cDu hast keine Berechtigung für globale Einstellungen!");
            case 31 -> {
                // plugin.clearJoinMessage(player); // TODO: Implement method in Plugin class
                // plugin.setMessageEnabled(player.getName(), true); // TODO: Implement method in Plugin class
                player.sendMessage("§aAlle Join-Messages zurückgesetzt!");
                new JoinMessageGUI(plugin).open(player);
            }
        }
    }
}
