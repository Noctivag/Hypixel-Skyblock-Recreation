package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.JoinMessagePresetsGUI;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

public class JoinMessagePresetsGUIListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;

    public JoinMessagePresetsGUIListener(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getInventory().getHolder() instanceof JoinMessagePresetsGUI)) return;
        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getItemMeta() == null) return;
        ItemMeta meta = clicked.getItemMeta();
        if (meta.displayName() == null) return;
        String display = PlainTextComponentSerializer.plainText().serialize(meta.displayName());
        if (display == null) return;

        if (display.contains("Zurück")) {
            new de.noctivag.skyblock.gui.JoinMessageGUI(SkyblockPlugin).open();
            return;
        }

        switch (event.getSlot()) {
            case 19 -> {
                // SkyblockPlugin.setJoinMessage(player, "§aWillkommen zurück, {player}!"); // TODO: Implement method in SkyblockPlugin class
                player.sendMessage(Component.text("§aJoin-Message gesetzt: §eWillkommen zurück, {player}!"));
                new JoinMessagePresetsGUI(SkyblockPlugin).open(player);
            }
            case 20 -> {
                // SkyblockPlugin.setJoinMessage(player, "§aHallo {player}!"); // TODO: Implement method in SkyblockPlugin class
                player.sendMessage(Component.text("§aJoin-Message gesetzt: §eHallo {player}!"));
                new JoinMessagePresetsGUI(SkyblockPlugin).open(player);
            }
            case 21 -> {
                // SkyblockPlugin.setJoinMessage(player, "§a{player} ist dem Server beigetreten!"); // TODO: Implement method in SkyblockPlugin class
                player.sendMessage(Component.text("§aJoin-Message gesetzt: §e{player} ist dem Server beigetreten!"));
                new JoinMessagePresetsGUI(SkyblockPlugin).open(player);
            }
            case 22 -> {
                // SkyblockPlugin.setJoinMessage(player, "§a{player} ist jetzt online!"); // TODO: Implement method in SkyblockPlugin class
                player.sendMessage(Component.text("§aJoin-Message gesetzt: §e{player} ist jetzt online!"));
                new JoinMessagePresetsGUI(SkyblockPlugin).open(player);
            }
            case 37 -> {
                player.closeInventory();
                player.sendMessage(Component.text("§eGib deine eigene Nachricht ein (oder 'cancel' zum Abbrechen):"));
                player.sendMessage(Component.text("§cCustom-Message-System benötigt Chat-Input Integration"));
            }
        }
    }
}
