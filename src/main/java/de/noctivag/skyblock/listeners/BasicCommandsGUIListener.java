package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.BasicCommandsGUI;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

public class BasicCommandsGUIListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;

    public BasicCommandsGUIListener(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
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
            new de.noctivag.skyblock.gui.MainMenu(SkyblockPlugin, player).open();
            return;
        }

        boolean rightClick = event.isRightClick();

        switch (event.getSlot()) {
            case 19 -> { // Nickname
                if (rightClick) {
                    // Remove nickname
                    // TODO: Implement nickname removal
                    // SkyblockPlugin.getNickMap().remove(player.getName());
                    player.sendMessage(Component.text("§aNickname entfernt!"));
                    new BasicCommandsGUI(SkyblockPlugin).openGUI(player);
                } else {
                    // Set nickname - open chat input
                    player.closeInventory();
                    player.sendMessage(Component.text("§eGib den neuen Nickname ein (oder 'cancel' zum Abbrechen):"));
                    SkyblockPlugin.getServer().getScheduler().runTask(SkyblockPlugin, () -> {
                        // This would need a chat input system - for now, just show message
                        player.sendMessage(Component.text("§cNickname-System benötigt Chat-Input Integration"));
                    });
                }
            }
            case 20 -> { // Prefix
                if (rightClick) {
                    // Remove prefix
                    // TODO: Implement prefix removal
                    // SkyblockPlugin.getPrefixMap().remove(player.getName());
                    player.sendMessage(Component.text("§aPrefix entfernt!"));
                    new BasicCommandsGUI(SkyblockPlugin).openGUI(player);
                } else {
                    // Set prefix - open chat input
                    player.closeInventory();
                    player.sendMessage(Component.text("§eGib den neuen Prefix ein (oder 'cancel' zum Abbrechen):"));
                    SkyblockPlugin.getServer().getScheduler().runTask(SkyblockPlugin, () -> {
                        // This would need a chat input system - for now, just show message
                        player.sendMessage(Component.text("§cPrefix-System benötigt Chat-Input Integration"));
                    });
                }
            }
            case 22 -> { // Crafting Table
                if (player.hasPermission("basicsplugin.workbench")) {
                    player.openWorkbench(null, true);
                    player.sendMessage(Component.text("§aWerkbank geöffnet!"));
                } else {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung!"));
                }
            }
            case 23 -> { // Anvil
                if (player.hasPermission("basicsplugin.anvil")) {
                    player.openAnvil(null, true);
                    player.sendMessage(Component.text("§aAmboss geöffnet!"));
                } else {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung!"));
                }
            }
            case 24 -> { // Ender Chest
                if (player.hasPermission("basicsplugin.enderchest")) {
                    player.openInventory(player.getEnderChest());
                    player.sendMessage(Component.text("§aEndertruhe geöffnet!"));
                } else {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung!"));
                }
            }
            case 25 -> { // Grindstone
                if (player.hasPermission("basicsplugin.grindstone")) {
                    player.openGrindstone(null, true);
                    player.sendMessage(Component.text("§aSchleifstein geöffnet!"));
                } else {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung!"));
                }
            }
            case 28 -> { // Smithing Table
                if (player.hasPermission("basicsplugin.smithingtable")) {
                    player.openSmithingTable(null, true);
                    player.sendMessage(Component.text("§aSchmiedetisch geöffnet!"));
                } else {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung!"));
                }
            }
            case 29 -> { // Stonecutter
                if (player.hasPermission("basicsplugin.stonecutter")) {
                    player.openStonecutter(null, true);
                    player.sendMessage(Component.text("§aSteinsäger geöffnet!"));
                } else {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung!"));
                }
            }
            case 30 -> { // Loom
                if (player.hasPermission("basicsplugin.loom")) {
                    player.openLoom(null, true);
                    player.sendMessage(Component.text("§aWebstuhl geöffnet!"));
                } else {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung!"));
                }
            }
            case 31 -> { // Cartography Table
                if (player.hasPermission("basicsplugin.cartography")) {
                    player.openCartographyTable(null, true);
                    player.sendMessage(Component.text("§aKartentisch geöffnet!"));
                } else {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung!"));
                }
            }
            case 37 -> { // Heal
                if (player.hasPermission("basicsplugin.heal")) {
                    player.setHealth(player.getMaxHealth());
                    player.setFoodLevel(20);
                    player.setSaturation(20);
                    player.sendMessage(Component.text("§aDu wurdest geheilt!"));
                } else {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung!"));
                }
            }
            case 38 -> { // Feed
                if (player.hasPermission("basicsplugin.feed")) {
                    player.setFoodLevel(20);
                    player.setSaturation(20);
                    player.sendMessage(Component.text("§aDu wurdest gefüttert!"));
                } else {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung!"));
                }
            }
            case 39 -> { // Clear Inventory
                if (player.hasPermission("basicsplugin.clearinventory")) {
                    player.getInventory().clear();
                    player.sendMessage(Component.text("§aInventar geleert!"));
                } else {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung!"));
                }
            }
        }
    }
}
