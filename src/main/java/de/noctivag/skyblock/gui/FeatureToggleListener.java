package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class FeatureToggleListener implements Listener {
    private final SkyblockPlugin plugin;
    private static final Component GUI_TITLE = Component.text("§6§lFeature-Einstellungen");

    public FeatureToggleListener(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getView().title().equals(GUI_TITLE)) return;

        event.setCancelled(true);

        if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;

        Component displayName = event.getCurrentItem().getItemMeta().displayName();
        if (displayName == null) return;

        String itemName = PlainTextComponentSerializer.plainText().serialize(displayName);
        switch (itemName) {
            case "Scoreboard - Rang":
                toggleScoreboardFeature(player, "show-rank");
                break;
            case "Scoreboard - Geld":
                toggleScoreboardFeature(player, "show-money");
                break;
            case "Scoreboard - Online-Spieler":
                toggleScoreboardFeature(player, "show-online-players");
                break;
            case "Scoreboard - TPS":
                toggleScoreboardFeature(player, "show-tps");
                break;
            case "Scoreboard - Website":
                toggleScoreboardFeature(player, "show-website");
                break;
            case "Kit-System":
                toggleFeature(player, "kits");
                break;
        }

        // GUI aktualisieren
        new FeatureToggleGUI(plugin).openGUI(player);
    }

    private void toggleScoreboardFeature(Player player, String feature) {
        if (!player.hasPermission("plugin.admin")) {
            player.sendMessage("§cDazu hast du keine Berechtigung!");
            return;
        }

        boolean currentState = plugin.getConfigManager().isScoreboardFeatureEnabled(feature);
        plugin.getConfigManager().setScoreboardFeature(feature, !currentState);
        plugin.getScoreboardManager().updateAllScoreboards();

        player.sendMessage(String.format("§7Das Feature §e%s §7wurde %s§7!",
            feature, !currentState ? "§aaktiviert" : "§cdeaktiviert"));
    }

    private void toggleFeature(Player player, String feature) {
        if (!player.hasPermission("plugin.admin")) {
            player.sendMessage("§cDazu hast du keine Berechtigung!");
            return;
        }

        boolean currentState = plugin.getConfigManager().isFeatureEnabled(feature);
        plugin.getConfigManager().setFeature(feature, !currentState);

        player.sendMessage(String.format("§7Das Feature §e%s §7wurde %s§7!",
            feature, !currentState ? "§aaktiviert" : "§cdeaktiviert"));
    }
}
