package de.noctivag.skyblock.locations.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.locations.EnhancedWarp;
import de.noctivag.skyblock.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WarpGUI {
    private final SkyblockPlugin plugin;
    private static final String MAIN_MENU_TITLE = "§8» §6Warp-Menü §8«";
    private static final String CATEGORY_MENU_TITLE = "§8» §6Warps: §e%s §8«";

    public WarpGUI(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    public void openMainMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, ColorUtils.translate(MAIN_MENU_TITLE));
        decorateInventory(inv);

        // Featured Warps in der oberen Reihe
        List<EnhancedWarp> featuredWarps = new ArrayList<>();
        for (String name : plugin.getLocationManager().getWarpNames()) {
            var w = plugin.getLocationManager().getWarp(name);
            // Type conversion issue - using LocationManager.Warp instead of EnhancedWarp
            // if (w instanceof EnhancedWarp ew && ew.isFeatured()) featuredWarps.add(ew);
        }
        int slot = 10;
        for (EnhancedWarp warp : featuredWarps) {
            if (slot > 16) break;
            inv.setItem(slot++, createWarpItem(warp, player));
        }

        // Kategorien in der unteren Hälfte
        slot = 28;
        for (EnhancedWarp.WarpCategory category : EnhancedWarp.WarpCategory.values()) {
            if (slot > 34) break;

            List<EnhancedWarp> warpsInCategory = plugin.getLocationManager().getWarpsByCategory(category);
            if (!warpsInCategory.isEmpty()) {
                inv.setItem(slot++, createCategoryItem(category, warpsInCategory.size()));
            }
        }

        // Spieler-Statistiken (PlayerDataManager/discovery optional; use safe fallbacks)
        ItemStack statsItem = createGuiItem(
            Material.PLAYER_HEAD,
            "§6» §eSpielerprofil",
            Arrays.asList(
                "§7Kontostand: §e" + plugin.getEconomyManager().formatMoney(plugin.getEconomyManager().getBalance(player)),
                "§7Level: §e" + "1",
                "",
                "§7Entdeckte Warps: §e" + 0
            )
        );
        inv.setItem(49, statsItem);

        // Back button
        inv.setItem(45, createGuiItem(Material.ARROW, "§7Zurück", Arrays.asList("§7Zum Hauptmenü")));

        player.openInventory(inv);
    }

    public void openCategoryMenu(Player player, EnhancedWarp.WarpCategory category) {
        String title = String.format(CATEGORY_MENU_TITLE, category.getDisplayName());
        Inventory inv = Bukkit.createInventory(null, 54, ColorUtils.translate(title));
        decorateInventory(inv);

        // Build list from available warps
        List<EnhancedWarp> warps = new ArrayList<>();
        for (String name : plugin.getLocationManager().getWarpNames()) {
            var w = plugin.getLocationManager().getWarp(name);
            // Type conversion issue - using LocationManager.Warp instead of EnhancedWarp
            // if (w instanceof EnhancedWarp ew && ew.getCategory() == category) warps.add(ew);
        }

        int slot = 10;
        for (EnhancedWarp warp : warps) {
            if (slot % 9 == 8) slot += 2;
            if (slot > 43) break;
            inv.setItem(slot++, createWarpItem(warp, player));
        }

        // Zurück-Button
        ItemStack backButton = createGuiItem(
            Material.ARROW,
            "§c« Zurück",
            Arrays.asList("§7Zurück zum Hauptmenü")
        );
        inv.setItem(49, backButton);

        player.openInventory(inv);
    }

    private ItemStack createWarpItem(EnhancedWarp warp, Player player) {
        List<String> lore = new ArrayList<>();
        lore.add(warp.getDescription());
        lore.add("");

        // Anforderungen (Player level support optional)
        if (warp.getMinLevel() > 0) {
            int playerLevel = plugin.getPlayerDataManager().getLevel(player);
            boolean hasLevel = playerLevel >= warp.getMinLevel();
            lore.add("§7Level: " + (hasLevel ? "§a" : "§c") + playerLevel + "§7/§e" + warp.getMinLevel());
        }

        if (warp.getPrice() > 0) {
            boolean canAfford = plugin.getEconomyManager().hasBalance(player, warp.getPrice());
            lore.add("§7Preis: " + (canAfford ? "§a" : "§c") +
                     plugin.getEconomyManager().formatMoney(warp.getPrice()));
        }

        if (!warp.getPermission().isEmpty() && !player.hasPermission(warp.getPermission())) {
            lore.add("§cBenötigt spezielle Berechtigung");
        }

        lore.add("");
        lore.add(canUseWarp(warp, player) ? "§eKlicke zum Teleportieren!" : "§cNicht verfügbar");

        return createGuiItem(warp.getIcon(), "§6» §e" + warp.getName(), lore);
    }

    private ItemStack createCategoryItem(EnhancedWarp.WarpCategory category, int warpCount) {
        return createGuiItem(
            category.getIcon(),
            "§6» §e" + category.getDisplayName(),
            Arrays.asList(
                category.getDescription(),
                "",
                "§7Verfügbare Warps: §e" + warpCount,
                "",
                "§eKlicke zum Öffnen!"
            )
        );
    }

    private boolean canUseWarp(EnhancedWarp warp, Player player) {
        int playerLevel = plugin.getPlayerDataManager().getLevel(player);
        return (warp.getPermission().isEmpty() || player.hasPermission(warp.getPermission())) &&
               playerLevel >= warp.getMinLevel() &&
               plugin.getEconomyManager().hasBalance(player, warp.getPrice());
    }

    private void decorateInventory(Inventory inv) {
        ItemStack border = createGuiItem(Material.BLACK_STAINED_GLASS_PANE, "§r", Collections.emptyList());
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, border);
            inv.setItem(i + 45, border);
        }
        for (int i = 9; i < 45; i += 9) {
            inv.setItem(i, border);
            inv.setItem(i + 8, border);
        }
    }

    private ItemStack createGuiItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        // Use Adventure components for display name and lore
        meta.displayName(ColorUtils.translate(name));
        List<Component> loreComponents = new ArrayList<>();
        for (String line : lore) loreComponents.add(ColorUtils.translate(line));
        meta.lore(loreComponents);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }
}
