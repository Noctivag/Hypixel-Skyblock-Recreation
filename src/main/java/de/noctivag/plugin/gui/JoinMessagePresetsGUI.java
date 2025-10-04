package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;


public class JoinMessagePresetsGUI extends CustomGUI {
    private final Plugin plugin;

    public JoinMessagePresetsGUI(Plugin plugin) {
        super(54, Component.text("§d§lPreset Messages"));
        this.plugin = plugin;
    }

    public void open(Player player) {
        clearInventory();

        // Header
        setItem(4, createGuiItem(Material.BOOK, "§d§lPreset Messages",
            "§7Wähle eine vordefinierte Nachricht"));

        // Join Message Presets
        setItem(19, createGuiItem(Material.GREEN_BANNER, "§aWillkommen zurück!",
            "§7Setze Join-Message auf:",
            "§e'§aWillkommen zurück, {player}!§e'",
            "",
            "§eLinksklick: §7Als Join-Message setzen"));

        setItem(20, createGuiItem(Material.GREEN_BANNER, "§aHallo {player}!",
            "§7Setze Join-Message auf:",
            "§e'§aHallo {player}!§e'",
            "",
            "§eLinksklick: §7Als Join-Message setzen"));

        setItem(21, createGuiItem(Material.GREEN_BANNER, "§a{player} ist beigetreten!",
            "§7Setze Join-Message auf:",
            "§e'§a{player} ist dem Server beigetreten!§e'",
            "",
            "§eLinksklick: §7Als Join-Message setzen"));

        setItem(22, createGuiItem(Material.GREEN_BANNER, "§a{player} ist online!",
            "§7Setze Join-Message auf:",
            "§e'§a{player} ist jetzt online!§e'",
            "",
            "§eLinksklick: §7Als Join-Message setzen"));

        // Leave Message Presets
        setItem(28, createGuiItem(Material.RED_BANNER, "§cAuf Wiedersehen!",
            "§7Setze Leave-Message auf:",
            "§e'§cAuf Wiedersehen, {player}!§e'",
            "",
            "§eLinksklick: §7Als Leave-Message setzen"));

        setItem(29, createGuiItem(Material.RED_BANNER, "§c{player} hat den Server verlassen",
            "§7Setze Leave-Message auf:",
            "§e'§c{player} hat den Server verlassen§e'",
            "",
            "§eLinksklick: §7Als Leave-Message setzen"));

        setItem(30, createGuiItem(Material.RED_BANNER, "§c{player} ist offline",
            "§7Setze Leave-Message auf:",
            "§e'§c{player} ist jetzt offline§e'",
            "",
            "§eLinksklick: §7Als Leave-Message setzen"));

        setItem(31, createGuiItem(Material.RED_BANNER, "§cBis bald, {player}!",
            "§7Setze Leave-Message auf:",
            "§e'§cBis bald, {player}!§e'",
            "",
            "§eLinksklick: §7Als Leave-Message setzen"));

        // Custom Messages
        setItem(37, createGuiItem(Material.WRITABLE_BOOK, "§eEigene Nachricht",
            "§7Erstelle eine eigene Nachricht",
            "",
            "§eLinksklick: §7Chat-Input öffnen"));

        // Back
        setItem(49, createGuiItem(Material.ARROW, "§7Zurück", "§7Zu Join/Leave Messages"));
        player.openInventory(getInventory());
    }
}
