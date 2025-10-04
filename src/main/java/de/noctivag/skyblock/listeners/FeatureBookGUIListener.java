package de.noctivag.skyblock.listeners;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.gui.FeatureBookGUI;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class FeatureBookGUIListener implements Listener {
    private final SkyblockPlugin plugin;

    public FeatureBookGUIListener(SkyblockPlugin plugin) {
        this.plugin = plugin;
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
            new de.noctivag.plugin.gui.MainMenu(plugin).open(player);
            return;
        }

        switch (event.getSlot()) {
            case 10 -> new de.noctivag.plugin.gui.BasicCommandsGUI(plugin).openGUI(player);
            case 11 -> new de.noctivag.plugin.gui.JoinMessageGUI(plugin).openGUI(player);
            case 12 -> {
                player.sendMessage("§6§l=== Economy System ===");
                player.sendMessage("§7• Kontostand: §e" + plugin.getEconomyManager().formatMoney(plugin.getEconomyManager().getBalance(player)));
                player.sendMessage("§7• Kostenbefreiung: §e" + (plugin.getEconomyManager().hasCostExemption(player) ? "Ja" : "Nein"));
                player.sendMessage("§7• Währung: §e" + plugin.getEconomyManager().getCurrencyName());
                player.sendMessage("§7• Economy Implementation: §eInternal (self-contained)");
            }
            case 13 -> {
                String rank = plugin.getRankManager().getPlayerRank(player);
                String displayName = plugin.getRankManager().getDisplayName(rank);
                player.sendMessage("§6§l=== Rank System ===");
                player.sendMessage("§7• Dein Rang: §e" + displayName);
                player.sendMessage("§7• Rang-Key: §e" + rank);
                player.sendMessage("§7• Verfügbare Ränge: §e" + String.join(", ", plugin.getRankManager().getAllRankKeys()));
            }
            case 19 -> {
                player.sendMessage("§6§l=== Teleportation ===");
                player.sendMessage("§7• /spawn - Zum Spawn teleportieren");
                player.sendMessage("§7• /back - Zur letzten Position");
                player.sendMessage("§7• /rtp - Zufällige Teleportation");
                player.sendMessage("§7• /tpa <spieler> - Teleport-Anfrage senden");
                player.sendMessage("§7• /tpaccept - Anfrage annehmen");
                player.sendMessage("§7• /tpdeny - Anfrage ablehnen");
                player.sendMessage("§7• /warp <name> - Zu Warp teleportieren");
                player.sendMessage("§7• /home <name> - Zu Home teleportieren");
            }
            case 20 -> {
                player.sendMessage("§6§l=== Social Features ===");
                player.sendMessage("§7• /party <invite|accept|leave|disband> - Party verwalten");
                player.sendMessage("§7• /friends <add|accept|remove> - Freunde verwalten");
                player.sendMessage("§7• /afk - AFK Status umschalten");
                player.sendMessage("§7• /ping - Ping anzeigen");
            }
            case 21 -> new de.noctivag.plugin.gui.CosmeticsMenu(plugin, plugin.getCosmeticsManager()).openGUI(player);
            case 22 -> new de.noctivag.plugin.achievements.gui.AchievementGUI(plugin).openMainGUI(player);
            case 28 -> new de.noctivag.plugin.kit.gui.KitShopGUI(plugin).openGUI(player);
            case 29 -> new de.noctivag.plugin.gui.DailyRewardGUI(plugin).open(player);
            case 30 -> new de.noctivag.plugin.gui.EventMenu().open(player);
            case 31 -> {
                player.sendMessage("§6§l=== Scoreboard ===");
                player.sendMessage("§7• Zeigt deine Statistiken");
                player.sendMessage("§7• Server-Informationen");
                player.sendMessage("§7• TPS und Online-Spieler");
                player.sendMessage("§7• Automatisch aktualisiert");
            }
            case 37 -> {
                if (player.hasPermission("basicsplugin.admin")) {
                    new de.noctivag.plugin.gui.AdminMenu(plugin).open(player);
                } else {
                    player.sendMessage("§cDu hast keine Berechtigung für Admin-Features!");
                }
            }
        }
    }
}
