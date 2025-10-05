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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class FeatureToggleGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private static final Component GUI_TITLE = Component.text("§6§lFeature-Einstellungen");

    public FeatureToggleGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    public void openGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, GUI_TITLE);

        // Scoreboard-Einstellungen
        inv.setItem(10, createToggleItem("Scoreboard - Rang",
            SkyblockPlugin.getConfigManager().isScoreboardFeatureEnabled("show-rank"),
            Material.NAME_TAG));

        inv.setItem(11, createToggleItem("Scoreboard - Geld",
            SkyblockPlugin.getConfigManager().isScoreboardFeatureEnabled("show-money"),
            Material.GOLD_INGOT));

        inv.setItem(12, createToggleItem("Scoreboard - Online-Spieler",
            SkyblockPlugin.getConfigManager().isScoreboardFeatureEnabled("show-online-players"),
            Material.PLAYER_HEAD));

        inv.setItem(13, createToggleItem("Scoreboard - TPS",
            SkyblockPlugin.getConfigManager().isScoreboardFeatureEnabled("show-tps"),
            Material.CLOCK));

        inv.setItem(14, createToggleItem("Scoreboard - Website",
            SkyblockPlugin.getConfigManager().isScoreboardFeatureEnabled("show-website"),
            Material.BOOK));

        // Kit-System
        inv.setItem(15, createToggleItem("Kit-System",
            SkyblockPlugin.getConfigManager().isFeatureEnabled("kits"),
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
                meta.lore(java.util.Arrays.stream(lore).map(Component::text).collect(java.util.stream.Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        return item;
    }
}
