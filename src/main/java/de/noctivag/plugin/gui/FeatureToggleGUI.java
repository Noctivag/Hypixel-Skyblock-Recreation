package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class FeatureToggleGUI {
    private final Plugin plugin;
    private static final Component GUI_TITLE = Component.text("§6§lFeature-Einstellungen");

    public FeatureToggleGUI(Plugin plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, GUI_TITLE);

        // Scoreboard-Einstellungen
        inv.setItem(10, createToggleItem("Scoreboard - Rang",
            plugin.getConfigManager().isScoreboardFeatureEnabled("show-rank"),
            Material.NAME_TAG));

        inv.setItem(11, createToggleItem("Scoreboard - Geld",
            plugin.getConfigManager().isScoreboardFeatureEnabled("show-money"),
            Material.GOLD_INGOT));

        inv.setItem(12, createToggleItem("Scoreboard - Online-Spieler",
            plugin.getConfigManager().isScoreboardFeatureEnabled("show-online-players"),
            Material.PLAYER_HEAD));

        inv.setItem(13, createToggleItem("Scoreboard - TPS",
            plugin.getConfigManager().isScoreboardFeatureEnabled("show-tps"),
            Material.CLOCK));

        inv.setItem(14, createToggleItem("Scoreboard - Website",
            plugin.getConfigManager().isScoreboardFeatureEnabled("show-website"),
            Material.BOOK));

        // Kit-System
        inv.setItem(15, createToggleItem("Kit-System",
            plugin.getConfigManager().isFeatureEnabled("kits"),
            Material.CHEST));

        // Weitere Features können hier hinzugefügt werden

        // Back button
        inv.setItem(22, createGuiItem(Material.ARROW, "§7Zurück", "§7Zum Hauptmenü"));

        player.openInventory(inv);
    }

    private ItemStack createToggleItem(String name, boolean enabled, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("§f" + name));
        meta.lore(Arrays.asList(
            Component.text(""),
            enabled ? Component.text("§aAktiviert") : Component.text("§cDeaktiviert"),
            Component.text(""),
            Component.text("§7Klicke zum Umschalten")
        ));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) {
                meta.lore(Arrays.stream(lore).map(Component::text).toList());
            }
            item.setItemMeta(meta);
        }
        return item;
    }
}
