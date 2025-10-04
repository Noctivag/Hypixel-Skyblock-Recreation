package de.noctivag.plugin.listeners;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.gui.BasicCommandsGUI;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class BasicCommandsGUIListener implements Listener {
    private final Plugin plugin;

    public BasicCommandsGUIListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getInventory().getHolder() instanceof BasicCommandsGUI)) return;
        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getItemMeta() == null) return;
        ItemMeta meta = clicked.getItemMeta();
        String display = PlainTextComponentSerializer.plainText().serialize(meta.displayName());
        if (display == null) return;

        if (display.contains("Zurück")) {
            new de.noctivag.plugin.gui.MainMenu(plugin).open(player);
            return;
        }

        boolean rightClick = event.isRightClick();

        switch (event.getSlot()) {
            case 19 -> { // Nickname
                if (rightClick) {
                    // Remove nickname
                    plugin.getNickMap().remove(player.getName());
                    player.sendMessage("§aNickname entfernt!");
                    new BasicCommandsGUI(plugin).openGUI(player);
                } else {
                    // Set nickname - open chat input
                    player.closeInventory();
                    player.sendMessage("§eGib den neuen Nickname ein (oder 'cancel' zum Abbrechen):");
                    plugin.getServer().getScheduler().runTask(plugin, () -> {
                        // This would need a chat input system - for now, just show message
                        player.sendMessage("§cNickname-System benötigt Chat-Input Integration");
                    });
                }
            }
            case 20 -> { // Prefix
                if (rightClick) {
                    // Remove prefix
                    plugin.getPrefixMap().remove(player.getName());
                    player.sendMessage("§aPrefix entfernt!");
                    new BasicCommandsGUI(plugin).openGUI(player);
                } else {
                    // Set prefix - open chat input
                    player.closeInventory();
                    player.sendMessage("§eGib den neuen Prefix ein (oder 'cancel' zum Abbrechen):");
                    plugin.getServer().getScheduler().runTask(plugin, () -> {
                        // This would need a chat input system - for now, just show message
                        player.sendMessage("§cPrefix-System benötigt Chat-Input Integration");
                    });
                }
            }
            case 22 -> { // Crafting Table
                if (player.hasPermission("basicsplugin.workbench")) {
                    player.openWorkbench(null, true);
                    player.sendMessage("§aWerkbank geöffnet!");
                } else {
                    player.sendMessage("§cDu hast keine Berechtigung!");
                }
            }
            case 23 -> { // Anvil
                if (player.hasPermission("basicsplugin.anvil")) {
                    player.openAnvil(null, true);
                    player.sendMessage("§aAmboss geöffnet!");
                } else {
                    player.sendMessage("§cDu hast keine Berechtigung!");
                }
            }
            case 24 -> { // Ender Chest
                if (player.hasPermission("basicsplugin.enderchest")) {
                    player.openInventory(player.getEnderChest());
                    player.sendMessage("§aEndertruhe geöffnet!");
                } else {
                    player.sendMessage("§cDu hast keine Berechtigung!");
                }
            }
            case 25 -> { // Grindstone
                if (player.hasPermission("basicsplugin.grindstone")) {
                    player.openGrindstone(null, true);
                    player.sendMessage("§aSchleifstein geöffnet!");
                } else {
                    player.sendMessage("§cDu hast keine Berechtigung!");
                }
            }
            case 28 -> { // Smithing Table
                if (player.hasPermission("basicsplugin.smithingtable")) {
                    player.openSmithingTable(null, true);
                    player.sendMessage("§aSchmiedetisch geöffnet!");
                } else {
                    player.sendMessage("§cDu hast keine Berechtigung!");
                }
            }
            case 29 -> { // Stonecutter
                if (player.hasPermission("basicsplugin.stonecutter")) {
                    player.openStonecutter(null, true);
                    player.sendMessage("§aSteinsäger geöffnet!");
                } else {
                    player.sendMessage("§cDu hast keine Berechtigung!");
                }
            }
            case 30 -> { // Loom
                if (player.hasPermission("basicsplugin.loom")) {
                    player.openLoom(null, true);
                    player.sendMessage("§aWebstuhl geöffnet!");
                } else {
                    player.sendMessage("§cDu hast keine Berechtigung!");
                }
            }
            case 31 -> { // Cartography Table
                if (player.hasPermission("basicsplugin.cartography")) {
                    player.openCartographyTable(null, true);
                    player.sendMessage("§aKartentisch geöffnet!");
                } else {
                    player.sendMessage("§cDu hast keine Berechtigung!");
                }
            }
            case 37 -> { // Heal
                if (player.hasPermission("basicsplugin.heal")) {
                    player.setHealth(player.getMaxHealth());
                    player.setFoodLevel(20);
                    player.setSaturation(20);
                    player.sendMessage("§aDu wurdest geheilt!");
                } else {
                    player.sendMessage("§cDu hast keine Berechtigung!");
                }
            }
            case 38 -> { // Feed
                if (player.hasPermission("basicsplugin.feed")) {
                    player.setFoodLevel(20);
                    player.setSaturation(20);
                    player.sendMessage("§aDu wurdest gefüttert!");
                } else {
                    player.sendMessage("§cDu hast keine Berechtigung!");
                }
            }
            case 39 -> { // Clear Inventory
                if (player.hasPermission("basicsplugin.clearinventory")) {
                    player.getInventory().clear();
                    player.sendMessage("§aInventar geleert!");
                } else {
                    player.sendMessage("§cDu hast keine Berechtigung!");
                }
            }
        }
    }
}
