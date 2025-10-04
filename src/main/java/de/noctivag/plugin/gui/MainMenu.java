package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class MainMenu extends CustomGUI {
    private final Plugin plugin;

    @SuppressWarnings("unused")
    public MainMenu(Plugin plugin) {
        super(54, Component.text("§6✧ Hauptmenü ✧"));
        this.plugin = plugin;
    }

    public void open(Player player) {
        clearInventory();
        setupItems(player);
        player.openInventory(getInventory());
    }

    private void setupItems(Player player) {
        // Border
        for (int i = 0; i < 9; i++) {
            setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            setItem(i + 45, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }
        for (int i = 0; i < 5; i++) {
            setItem(i * 9, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            setItem(i * 9 + 8, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }

        // Player summary (center top)
        setItem(4, createPlayerHead(player, "§e" + player.getName(),
            "§7Level: §a" + plugin.getPlayerDataManager().getLevel(player),
            "§7Coins: §6" + plugin.getEconomyManager().formatMoney(plugin.getEconomyManager().getBalance(player))));

        // Row 2: Primary navigation (slots 10,12,14,16 expected by GUIListener)
        setItem(10, createGuiItem(Material.NETHER_STAR, "§d§lKosmetik",
            "§7Partikel, Trails, Effekte"));

        setItem(12, createGuiItem(Material.ENDER_PEARL, "§b§lWarps",
            "§7Schnellreise & Kategorien"));

        setItem(14, createGuiItem(Material.CHEST, "§6§lTägliche Belohnungen",
            "§7Tägliches Einloggen belohnt!"));

        setItem(16, createGuiItem(Material.EXPERIENCE_BOTTLE, "§e§lErfolge",
            "§7Übersicht & Fortschritt"));

        // Row 4: Utilities (slots 28,30,32,34 expected by GUIListener)
        setItem(28, createGuiItem(Material.DIAMOND_SWORD, "§a§lKit-Shop",
            "§7Kits kaufen & verwalten"));

        setItem(30, createGuiItem(Material.DRAGON_EGG, "§5§lEvents",
            "§7Aktive Events & Zeitplan"));

        setItem(32, createGuiItem(Material.BOOK, "§9§lNachrichten",
            "§7Join-Message verwalten"));

        setItem(34, createGuiItem(Material.REDSTONE_TORCH, "§c§lEinstellungen",
            "§7Feature-Toggles & Optionen"));

        // Row 3: Additional features (slots 19,21,23,25)
        setItem(19, createGuiItem(Material.PLAYER_HEAD, "§e§lProfil",
            "§7Deine Statistiken und Einstellungen"));

        setItem(21, createGuiItem(Material.COMMAND_BLOCK, "§e§lBasic Commands",
            "§7Nickname, Prefix, Workbenches"));

        setItem(23, createGuiItem(Material.WRITTEN_BOOK, "§e§lJoin/Leave Messages",
            "§7Verwalte deine Nachrichten"));

        setItem(25, createGuiItem(Material.WRITTEN_BOOK, "§e§lPlugin Features",
            "§7Alle verfügbaren Features"));

        // Close Button
        setItem(49, createGuiItem(Material.BARRIER, "§c§lSchließen",
            "§7Schließe das Menü."));
    }
}
