package de.noctivag.plugin.kit.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.kit.KitShop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitShopGUI {
    private final Plugin plugin;
    private static final String GUI_TITLE = "§8» §6Kit-Shop §8«";

    public KitShopGUI(Plugin plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, GUI_TITLE);

        // Fülle die Ränder mit Glasscheiben
        ItemStack border = createGuiItem(Material.BLACK_STAINED_GLASS_PANE, "§r");
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, border); // Obere Reihe
            inv.setItem(i + 45, border); // Untere Reihe
        }
        for (int i = 9; i < 45; i += 9) {
            inv.setItem(i, border); // Linke Seite
            inv.setItem(i + 8, border); // Rechte Seite
        }

        // Zeige alle verfügbaren Kits an
        int slot = 10;
        // TODO: Implement getAvailableKits() and getKitInfo() methods in Plugin class
        // for (String kitName : plugin.getAvailableKits()) {
        //     KitShop.KitInfo kitInfo = plugin.getKitInfo(kitName);
        //     if (kitInfo != null) {
        //         ItemStack kitItem = createKitDisplay(player, kitInfo);
        //         inv.setItem(slot, kitItem);
        //         if ((slot + 2) % 9 == 0) {
        //             slot += 3;
        //         } else {
        //             slot++;
        //         }
        //     }
        // }

        // Spieler-Info
        double balance = plugin.getEconomyManager().getBalance(player);
        ItemStack infoItem = createGuiItem(
            Material.PLAYER_HEAD,
            "§6» §eKonto-Informationen",
            Arrays.asList(
                "§7Kontostand: §e" + plugin.getEconomyManager().formatMoney(balance),
                "",
                "§7Klicke auf ein Kit, um es zu",
                "§7kaufen oder zu verwenden!"
            )
        );
        inv.setItem(49, infoItem);

        // Back button
        inv.setItem(45, createGuiItem(Material.ARROW, "§7Zurück", List.of("§7Zum Hauptmenü")));

        player.openInventory(inv);
    }

    private ItemStack createKitDisplay(Player player, KitShop.KitInfo kitInfo) {
        List<String> lore = new ArrayList<>();
        lore.add(kitInfo.getDescription());
        lore.add("");
        lore.add("§7Enthält:");

        // Zeige Kit-Inhalte
        for (ItemStack item : kitInfo.getItems()) {
            lore.add("§8» §7" + formatItemName(item));
        }

        lore.add("");

        // Preis und Cooldown
        if (kitInfo.getPrice() > 0) {
            lore.add("§7Preis: §e" + plugin.getEconomyManager().formatMoney(kitInfo.getPrice()));
        } else if (kitInfo.getName().equalsIgnoreCase("vip")) {
            lore.add("§6Nur für VIP-Spieler");
        } else {
            lore.add("§aKostenlos");
        }

        // long cooldown = plugin.getRemainingCooldown(player, kitInfo.getName());
        // if (cooldown > 0) {
        //     lore.add("§cCooldown: " + formatTime(cooldown));
        // } else {
        //     lore.add("§aVerfügbar!");
        // }
        lore.add("§aVerfügbar!"); // Placeholder

        return createGuiItem(kitInfo.getIcon(), "§6» §e" + kitInfo.getName() + " Kit", lore);
    }

    private String formatItemName(ItemStack item) {
        String name = item.getType().toString().toLowerCase().replace("_", " ");
        return name.substring(0, 1).toUpperCase() + name.substring(1) +
               (item.getAmount() > 1 ? " §8(§7x" + item.getAmount() + "§8)" : "");
    }

    private String formatTime(long seconds) {
        if (seconds < 60) {
            return seconds + "s";
        } else if (seconds < 3600) {
            return String.format("%dm %ds", seconds / 60, seconds % 60);
        } else {
            long hours = seconds / 3600;
            long minutes = (seconds % 3600) / 60;
            return String.format("%dh %dm", hours, minutes);
        }
    }

    private ItemStack createGuiItem(Material material, String name) {
        return createGuiItem(material, name, new ArrayList<>());
    }

    private ItemStack createGuiItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
