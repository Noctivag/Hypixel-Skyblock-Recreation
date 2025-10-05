package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.JoinMessageGUI;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

public class JoinMessageGUIListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;

    public JoinMessageGUIListener(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
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
            new de.noctivag.skyblock.gui.MainMenu(SkyblockPlugin).open(player);
            return;
        }

        boolean rightClick = event.isRightClick();

        switch (event.getSlot()) {
            case 19 -> {
                if (rightClick) {
                    // Remove join message
                    // SkyblockPlugin.clearJoinMessage(player); // TODO: Implement method in SkyblockPlugin class
                    player.sendMessage(Component.text("§aJoin-Message entfernt!"));
                    new JoinMessageGUI(SkyblockPlugin).open(player);
                } else {
                    // Set join message - open chat input
                    player.closeInventory();
                    player.sendMessage(Component.text("§eGib die neue Join-Message ein (oder 'cancel' zum Abbrechen):"));
                    player.sendMessage(Component.text("§cJoin-Message-System benötigt Chat-Input Integration"));
                }
            }
            case 22 -> {
                // boolean currentlyDisabled = SkyblockPlugin.isMessageDisabled(player.getName()); // TODO: Implement method in SkyblockPlugin class
                // SkyblockPlugin.setMessageEnabled(player.getName(), currentlyDisabled); // TODO: Implement method in SkyblockPlugin class
                boolean currentlyDisabled = false; // Placeholder
                player.sendMessage(currentlyDisabled ? "§aJoin-Messages aktiviert!" : "§cJoin-Messages deaktiviert!");
                new JoinMessageGUI(SkyblockPlugin).open(player);
            }
            case 25 -> new de.noctivag.skyblock.gui.JoinMessagePresetsGUI(SkyblockPlugin).open(player);
            case 28 -> player.sendMessage(Component.text("§cDu hast keine Berechtigung für globale Einstellungen!"));
            case 31 -> {
                // SkyblockPlugin.clearJoinMessage(player); // TODO: Implement method in SkyblockPlugin class
                // SkyblockPlugin.setMessageEnabled(player.getName(), true); // TODO: Implement method in SkyblockPlugin class
                player.sendMessage(Component.text("§aAlle Join-Messages zurückgesetzt!"));
                new JoinMessageGUI(SkyblockPlugin).open(player);
            }
        }
    }
}
