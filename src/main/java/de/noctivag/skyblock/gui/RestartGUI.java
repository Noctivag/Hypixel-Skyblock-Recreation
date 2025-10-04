package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RestartGUI implements org.bukkit.inventory.InventoryHolder {
    private final SkyblockPlugin plugin;
    private final Inventory inventory;

    public static final String TITLE = "§c» §6Server-Restart §c«";

    public RestartGUI(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(this, 27, ColorUtils.translate(TITLE));
        setupItems();
    }

    private void setupItems() {
        // Schedule Short (60s)
        inventory.setItem(10, createGuiItem(Material.GREEN_WOOL, "§aNeustart in 60s", "§7Plan einen Neustart in 60 Sekunden"));
        // Schedule Medium (5m)
        inventory.setItem(12, createGuiItem(Material.YELLOW_WOOL, "§eNeustart in 5m", "§7Plan einen Neustart in 5 Minuten"));
        // Schedule Long (1h)
        inventory.setItem(14, createGuiItem(Material.ORANGE_WOOL, "§6Neustart in 1h", "§7Plan einen Neustart in 1 Stunde"));

        // Cancel
        inventory.setItem(16, createGuiItem(Material.RED_WOOL, "§cNeustart abbrechen", "§7Brich den geplanten Neustart ab"));

        // Info
        inventory.setItem(13, createGuiItem(Material.PAPER, "§fInfo", "§7Klicke um eine Aktion auszuwählen"));

        // Close
        inventory.setItem(22, createGuiItem(Material.ARROW, "§cSchließen", "§7Schließe dieses Menü"));
    }

    private ItemStack createGuiItem(Material mat, String name, String loreLine) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(ColorUtils.translate(name));
        List<Component> lore = new ArrayList<>();
        lore.add(ColorUtils.translate(loreLine));
        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
