package de.noctivag.skyblock.achievements.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.achievements.Achievement;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedHashSet;
import java.util.Set;
import net.kyori.adventure.text.Component;

public class AchievementGUIListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;

    public AchievementGUIListener(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = PlainTextComponentSerializer.plainText().serialize(event.getView().title());
        if (title == null) return;

        // Main achievement overview
        if (title.contains("Erfolge") && !title.contains(" - ")) {
            event.setCancelled(true);
            if (!(event.getWhoClicked() instanceof Player player)) return;
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getItemMeta() == null) return;
            ItemMeta meta = clicked.getItemMeta();
            String display = PlainTextComponentSerializer.plainText().serialize(meta.displayName());

            // Filter handling
            if (display.contains("Filter: Alle")) {
                player.sendMessage(Component.text("§7Filter gesetzt: §fAlle"));
                new AchievementGUI(SkyblockPlugin).openMainGUI(player);
                return;
            }
            if (display.contains("Filter: Freigeschaltet") || display.contains("Filter: §a")) {
                player.sendMessage(Component.text("§7Filter gesetzt: §aFreigeschaltet"));
                // Open each category as needed filtered later; here user should click a category next
                return;
            }
            if (display.contains("Filter: Nicht freigeschaltet") || display.contains("Filter: §7")) {
                player.sendMessage(Component.text("§7Filter gesetzt: §7Nicht freigeschaltet"));
                return;
            }

            // Determine category names from Achievement enum prefixes
            Set<String> categories = new LinkedHashSet<>();
            for (Achievement a : Achievement.values()) {
                String key = a.name().split("_")[0];
                categories.add(key);
            }

            for (String cat : categories) {
                String formatted = formatCategoryName(cat);
                if (display.contains(formatted)) {
                    new AchievementGUI(SkyblockPlugin).openCategoryGUI(player, cat);
                    return;
                }
            }
        }

        // Category view (title contains " - ")
        if (title.contains("Erfolge - ") || (title.contains("Erfolge") && title.contains(" - "))) {
            event.setCancelled(true);
            if (!(event.getWhoClicked() instanceof Player player)) return;
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getItemMeta() == null) return;
            ItemMeta meta = clicked.getItemMeta();
            String display = PlainTextComponentSerializer.plainText().serialize(meta.displayName());

            // Back button
            if (display.toLowerCase().contains("zurück") || display.toLowerCase().contains("« zurück")) {
                new AchievementGUI(SkyblockPlugin).openMainGUI(player);
                return;
            }

            // Achievement item clicked - show details
            // Strip prefix like "✔ " or "✖ " if present
            String cleaned = display.replace("✔ ", "").replace("✖ ", "").replace("§a", "").replace("§c", "").trim();
            // Find achievement by display name
            for (Achievement a : Achievement.values()) {
                if (a.getDisplayName().equalsIgnoreCase(cleaned) || a.getDisplayName().contains(cleaned)) {
                    // TODO: Implement proper AchievementManager interface
                    // boolean unlocked = ((AchievementManager) SkyblockPlugin.getAchievementManager()).hasAchievement(player, a);
                    boolean unlocked = false; // Placeholder
                    player.sendMessage("§e" + a.getDisplayName() + " - " + (unlocked ? "§aFreigeschaltet" : "§cNicht freigeschaltet"));
                    return;
                }
            }
        }
    }

    private String formatCategoryName(String category) {
        return switch (category) {
            case "FIRST" -> "Allgemein";
            case "REACH" -> "Level";
            case "KIT" -> "Kits";
            case "MINING" -> "Bergbau";
            case "PVP" -> "Kampf";
            case "EXPLORER" -> "Entdecker";
            default -> category;
        };
    }
}
