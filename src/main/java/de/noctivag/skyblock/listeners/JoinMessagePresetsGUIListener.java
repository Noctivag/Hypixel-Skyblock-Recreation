package de.noctivag.skyblock.listeners;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.gui.JoinMessagePresetsGUI;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class JoinMessagePresetsGUIListener implements Listener {
    private final SkyblockPlugin plugin;

    public JoinMessagePresetsGUIListener(SkyblockPlugin plugin) {
        this.plugin = plugin;
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
            new de.noctivag.plugin.gui.JoinMessageGUI(plugin).open(player);
            return;
        }

        switch (event.getSlot()) {
            case 19 -> {
                // plugin.setJoinMessage(player, "§aWillkommen zurück, {player}!"); // TODO: Implement method in Plugin class
                player.sendMessage("§aJoin-Message gesetzt: §eWillkommen zurück, {player}!");
                new JoinMessagePresetsGUI(plugin).open(player);
            }
            case 20 -> {
                // plugin.setJoinMessage(player, "§aHallo {player}!"); // TODO: Implement method in Plugin class
                player.sendMessage("§aJoin-Message gesetzt: §eHallo {player}!");
                new JoinMessagePresetsGUI(plugin).open(player);
            }
            case 21 -> {
                // plugin.setJoinMessage(player, "§a{player} ist dem Server beigetreten!"); // TODO: Implement method in Plugin class
                player.sendMessage("§aJoin-Message gesetzt: §e{player} ist dem Server beigetreten!");
                new JoinMessagePresetsGUI(plugin).open(player);
            }
            case 22 -> {
                // plugin.setJoinMessage(player, "§a{player} ist jetzt online!"); // TODO: Implement method in Plugin class
                player.sendMessage("§aJoin-Message gesetzt: §e{player} ist jetzt online!");
                new JoinMessagePresetsGUI(plugin).open(player);
            }
            case 37 -> {
                player.closeInventory();
                player.sendMessage("§eGib deine eigene Nachricht ein (oder 'cancel' zum Abbrechen):");
                player.sendMessage("§cCustom-Message-System benötigt Chat-Input Integration");
            }
        }
    }
}
