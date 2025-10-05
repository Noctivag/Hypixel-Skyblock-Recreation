package de.noctivag.skyblock.kit.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.kit.KitManager;
import de.noctivag.skyblock.kit.KitShop;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

public class KitShopListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final KitManager kitManager;

    public KitShopListener(SkyblockPlugin SkyblockPlugin, KitManager kitManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.kitManager = kitManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = PlainTextComponentSerializer.plainText().serialize(event.getView().title());
        if (!title.equals("§8» §6Kit-Shop §8«")) {
            return;
        }

        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player) || event.getCurrentItem() == null) {
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();
        ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null) return;
        String itemName = PlainTextComponentSerializer.plainText().serialize(meta.displayName());

        // Extrahiere den Kit-Namen aus dem Item-Namen (§6» §e{KIT_NAME} Kit)
        if (itemName.contains("Kit")) {
            String kitName = itemName.split("§e")[1].split(" Kit")[0];
            handleKitPurchase(player, kitName);
        }
    }

    private void handleKitPurchase(Player player, String kitName) {
        KitShop.KitInfo kitInfo = kitManager.getKitShop().getKitInfo(kitName.toLowerCase());

        if (kitInfo == null) {
            player.sendMessage(Component.text("§cDieses Kit existiert nicht!"));
            return;
        }

        // Prüfe Cooldown
        long remainingCooldown = kitManager.getRemainingCooldown(player, kitName);
        if (remainingCooldown > 0) {
            player.sendMessage("§cDu musst noch " + formatTime(remainingCooldown) + " warten!");
            return;
        }

        // Prüfe VIP-Status
        if (kitName.equalsIgnoreCase("vip") && !player.hasPermission("kit.vip")) {
            player.sendMessage(Component.text("§cDieses Kit ist nur für VIP-Spieler verfügbar!"));
            return;
        }

        // Prüfe und ziehe Kosten ab
        if (kitInfo.getPrice() > 0) {
            if (!SkyblockPlugin.getEconomyManager().hasBalance(player, kitInfo.getPrice())) {
                player.sendMessage("§cDu hast nicht genug Geld! Du benötigst " +
                    SkyblockPlugin.getEconomyManager().formatMoney(kitInfo.getPrice()));
                return;
            }
            boolean success = SkyblockPlugin.getEconomyManager().withdrawMoney(player, kitInfo.getPrice());
            if (!success) {
                player.sendMessage(Component.text("Transaction failed. Not enough balance."));
                return;
            }
            player.sendMessage("§7Du hast das Kit für §e" +
                SkyblockPlugin.getEconomyManager().formatMoney(kitInfo.getPrice()) + " §7gekauft!");
        }

        // Gebe Kit-Items
        kitManager.giveKit(player, kitName);

        // Setze Cooldown
        kitManager.setCooldown(player, kitName);

        // Aktualisiere GUI
        new KitShopGUI(SkyblockPlugin).openGUI(player);
    }

    private String formatTime(long seconds) {
        if (seconds < 60) {
            return seconds + " Sekunden";
        } else if (seconds < 3600) {
            return String.format("%d Minuten %d Sekunden", seconds / 60, seconds % 60);
        } else {
            long hours = seconds / 3600;
            long minutes = (seconds % 3600) / 60;
            return String.format("%d Stunden %d Minuten", hours, minutes);
        }
    }
}
