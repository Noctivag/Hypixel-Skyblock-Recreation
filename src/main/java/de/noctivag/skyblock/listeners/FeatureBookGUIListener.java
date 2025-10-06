package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.FeatureBookGUI;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

public class FeatureBookGUIListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;

    public FeatureBookGUIListener(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getInventory().getHolder() instanceof FeatureBookGUI)) return;
        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getItemMeta() == null) return;
        ItemMeta meta = clicked.getItemMeta();
        if (meta.displayName() == null) return;
        String display = PlainTextComponentSerializer.plainText().serialize(meta.displayName());
        if (display == null) return;

        if (display.contains("Zurück")) {
            new de.noctivag.skyblock.gui.MainMenu(SkyblockPlugin, player).open();
            return;
        }

        switch (event.getSlot()) {
            case 10 -> new de.noctivag.skyblock.gui.BasicCommandsGUI(SkyblockPlugin).openGUI(player);
            case 11 -> new de.noctivag.skyblock.gui.JoinMessageGUI(SkyblockPlugin).openGUI(player);
            case 12 -> {
                player.sendMessage(Component.text("§6§l=== Economy System ==="));
                player.sendMessage("§7• Kontostand: §e" + SkyblockPlugin.getEconomyManager().formatMoney(SkyblockPlugin.getEconomyManager().getBalance(player)));
                player.sendMessage("§7• Kostenbefreiung: §e" + (SkyblockPlugin.getEconomyManager().hasCostExemption(player) ? "Ja" : "Nein"));
                player.sendMessage("§7• Währung: §e" + SkyblockPlugin.getEconomyManager().getCurrencyName());
                player.sendMessage(Component.text("§7• Economy Implementation: §eInternal (self-contained)"));
            }
            case 13 -> {
                String rank = "default"; // Placeholder
                String displayName = "Default"; // Placeholder
                player.sendMessage(Component.text("§6§l=== Rank System ==="));
                player.sendMessage("§7• Dein Rang: §e" + displayName);
                player.sendMessage("§7• Rang-Key: §e" + rank);
                player.sendMessage("§7• Verfügbare Ränge: §edefault, vip, premium");
            }
            case 19 -> {
                player.sendMessage(Component.text("§6§l=== Teleportation ==="));
                player.sendMessage(Component.text("§7• /spawn - Zum Spawn teleportieren"));
                player.sendMessage(Component.text("§7• /back - Zur letzten Position"));
                player.sendMessage(Component.text("§7• /rtp - Zufällige Teleportation"));
                player.sendMessage(Component.text("§7• /tpa <spieler> - Teleport-Anfrage senden"));
                player.sendMessage(Component.text("§7• /tpaccept - Anfrage annehmen"));
                player.sendMessage(Component.text("§7• /tpdeny - Anfrage ablehnen"));
                player.sendMessage(Component.text("§7• /warp <name> - Zu Warp teleportieren"));
                player.sendMessage(Component.text("§7• /home <name> - Zu Home teleportieren"));
            }
            case 20 -> {
                player.sendMessage(Component.text("§6§l=== Social Features ==="));
                player.sendMessage(Component.text("§7• /party <invite|accept|leave|disband> - Party verwalten"));
                player.sendMessage(Component.text("§7• /friends <add|accept|remove> - Freunde verwalten"));
                player.sendMessage(Component.text("§7• /afk - AFK Status umschalten"));
                player.sendMessage(Component.text("§7• /ping - Ping anzeigen"));
            }
            case 21 -> new de.noctivag.skyblock.gui.CosmeticsMenu(SkyblockPlugin, SkyblockPlugin.getCosmeticsManager()).openGUI(player);
            case 22 -> new de.noctivag.skyblock.achievements.gui.AchievementGUI(SkyblockPlugin).openMainGUI(player);
            case 28 -> new de.noctivag.skyblock.kit.gui.KitShopGUI(SkyblockPlugin).openGUI(player);
            case 29 -> new de.noctivag.skyblock.gui.DailyRewardGUI(SkyblockPlugin).open();
            case 30 -> new de.noctivag.skyblock.gui.EventMenu(SkyblockPlugin, player).open();
            case 31 -> {
                player.sendMessage(Component.text("§6§l=== Scoreboard ==="));
                player.sendMessage(Component.text("§7• Zeigt deine Statistiken"));
                player.sendMessage(Component.text("§7• Server-Informationen"));
                player.sendMessage(Component.text("§7• TPS und Online-Spieler"));
                player.sendMessage(Component.text("§7• Automatisch aktualisiert"));
            }
            case 37 -> {
                if (player.hasPermission("basicsplugin.admin")) {
                    new de.noctivag.skyblock.gui.AdminMenu(SkyblockPlugin).open();
                } else {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung für Admin-Features!"));
                }
            }
        }
    }
}
