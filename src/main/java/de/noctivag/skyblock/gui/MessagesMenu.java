package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MessagesMenu implements InventoryHolder {
    private final SkyblockPlugin SkyblockPlugin;
    private final Inventory inventory;

    public MessagesMenu(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.inventory = Bukkit.createInventory(this, 27, Component.text("§f§lNachrichten"));
        initializeItems();
    }

    private void initializeItems() {
        // View current join message
        setItem(10, createGuiItem(Material.PAPER, "§eDeine Join-Message",
            "§7Zeige deine aktuelle Nachricht"));

        // Toggle enabled/disabled (handled in GUIListener)
        setItem(13, createGuiItem(Material.REDSTONE_TORCH, "§cEin-/Ausschalten",
            "§7Aktiviert / Deaktiviert deine Join-Message"));

        // Clear custom message
        setItem(16, createGuiItem(Material.BARRIER, "§cNachricht löschen",
            "§7Entfernt deine benutzerdefinierte Nachricht"));

        // Back button
        setItem(22, createGuiItem(Material.ARROW, "§7Zurück",
            "§7Zum Hauptmenü"));

        // Fill empty slots
        ItemStack filler = createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) inventory.setItem(i, filler);
        }
    }

    public void open(Player player) {
        player.playSound(player.getLocation(), org.bukkit.Sound.UI_TOAST_OUT, 0.6f, 1.15f);
        player.openInventory(inventory);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private void setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }

    private @NotNull ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) meta.lore(java.util.Arrays.stream(lore).map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        return item;
    }
}
