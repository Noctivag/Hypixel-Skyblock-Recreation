package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.CommandUsageGUI;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

public class CommandUsageGUIListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;

    public CommandUsageGUIListener(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player admin)) return;
        if (!(event.getInventory().getHolder() instanceof CommandUsageGUI)) return;
        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getItemMeta() == null) return;
        ItemMeta meta = clicked.getItemMeta();
        String display = PlainTextComponentSerializer.plainText().serialize(meta.displayName());
        if (display == null) return;

        if (display.contains("Zurück")) {
            new de.noctivag.skyblock.gui.AdminMenu(SkyblockPlugin).open(admin);
            return;
        }

        // Extract command name from display (remove / prefix)
        if (display.startsWith("§e/")) {
            String command = display.substring(3); // remove "§e/"
            boolean rightClick = event.isRightClick();
            
            if (rightClick) {
                // Open command settings GUI (future extension)
                admin.sendMessage("§eCommand-Einstellungen für §7/" + command + " §e(noch nicht implementiert)");
            } else {
                // Toggle enabled/disabled
                boolean currentlyEnabled = true; // Placeholder
                // TODO: Implement command manager
                // boolean currentlyEnabled = SkyblockPlugin.getCommandManager().isCommandEnabled(command);
                // SkyblockPlugin.getCommandManager().setEnabled(command, !currentlyEnabled);
                admin.sendMessage("§aCommand §e/" + command + " §a" + 
                    (!currentlyEnabled ? "aktiviert" : "deaktiviert"));
                
                // Refresh GUI
                new CommandUsageGUI(SkyblockPlugin).open(admin);
            }
        }
    }
}
