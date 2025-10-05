package de.noctivag.skyblock.gui;

import org.bukkit.event.inventory.ClickType;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;

/**
 * Event Schedule GUI - Shows upcoming events
 */
public class EventScheduleGUI extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final Player player;

    public EventScheduleGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(54, Component.text("§6§l🕐 Event Schedule").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.SkyblockPlugin = SkyblockPlugin;
        this.player = player;
        setupItems();
        playOpenSound();
    }

    private void setupItems() {
        // Header
        setItem(4, createGuiItem(Material.CLOCK, "§6§l🕐 Event Schedule",
            "§7Upcoming events and schedules",
            "§eStay updated with server events"));

        // Today's Events
        setItem(10, createGuiItem(Material.SUNFLOWER, "§e§l☀️ Today's Events",
            "§7Events happening today",
            "§7• Boss Events: 14:00, 18:00, 22:00",
            "§7• PvP Tournament: 16:00",
            "§7• Mining Event: 20:00",
            "§eClick to view details"));

        // This Week
        setItem(11, createGuiItem(Material.CLOCK, "§b§l📅 This Week",
            "§7Events for the current week",
            "§7• Daily boss events",
            "§7• Weekend tournaments",
            "§7• Special events",
            "§eClick to view schedule"));

        // Monthly Events
        setItem(12, createGuiItem(Material.MAP, "§d§l🗓️ Monthly Events",
            "§7Special monthly events",
            "§7• Monthly tournaments",
            "§7• Seasonal events",
            "§7• Anniversary celebrations",
            "§eClick to view calendar"));

        // Event Notifications
        setItem(13, createGuiItem(Material.BELL, "§6§l🔔 Notifications",
            "§7Configure event notifications",
            "§7• Enable/disable notifications",
            "§7• Notification types",
            "§7• Timing preferences",
            "§eClick to configure"));

        // Event Rewards
        setItem(14, createGuiItem(Material.GOLD_INGOT, "§a§l🏆 Event Rewards",
            "§7View available event rewards",
            "§7• Participation rewards",
            "§7• Ranking rewards",
            "§7• Special prizes",
            "§eClick to view rewards"));

        // Event History
        setItem(15, createGuiItem(Material.BOOK, "§7§l📚 Event History",
            "§7View past events and results",
            "§7• Previous winners",
            "§7• Event statistics",
            "§7• Personal participation",
            "§eClick to view history"));

        // Back
        setItem(49, createGuiItem(Material.BARRIER, "§c§lBack",
            "§7Return to main menu"));
    }

    public void onClick(Player player, int slot, ItemStack item, ClickType clickType) {
        switch (slot) {
            case 10 -> showTodaysEvents(player);
            case 11 -> showWeeklySchedule(player);
            case 12 -> showMonthlyEvents(player);
            case 13 -> openNotificationSettings(player);
            case 14 -> showEventRewards(player);
            case 15 -> showEventHistory(player);
            case 49 -> {
                player.closeInventory();
                new UnifiedMainMenuSystem(SkyblockPlugin, player, UnifiedMainMenuSystem.MenuMode.ENHANCED).open(player);
            }
        }
    }

    private void showTodaysEvents(Player player) {
        player.sendMessage(Component.text("§a=== Heutige Events ==="));
        player.sendMessage(Component.text("§e14:00 - Boss Event: Ender Dragon"));
        player.sendMessage(Component.text("§e16:00 - PvP Tournament"));
        player.sendMessage(Component.text("§e18:00 - Boss Event: Wither"));
        player.sendMessage(Component.text("§e20:00 - Mining Event"));
        player.sendMessage(Component.text("§e22:00 - Boss Event: Elder Guardian"));
    }

    private void showWeeklySchedule(Player player) {
        player.sendMessage(Component.text("§a=== Wöchentlicher Zeitplan ==="));
        player.sendMessage(Component.text("§eMontag: Boss Events (14:00, 18:00, 22:00)"));
        player.sendMessage(Component.text("§eDienstag: PvP Tournament (16:00)"));
        player.sendMessage(Component.text("§eMittwoch: Mining Event (20:00)"));
        player.sendMessage(Component.text("§eDonnerstag: Boss Events (14:00, 18:00, 22:00)"));
        player.sendMessage(Component.text("§eFreitag: Special Event (19:00)"));
        player.sendMessage(Component.text("§eSamstag: Weekend Tournament (15:00)"));
        player.sendMessage(Component.text("§eSonntag: Boss Events (14:00, 18:00, 22:00)"));
    }

    private void showMonthlyEvents(Player player) {
        player.sendMessage(Component.text("§a=== Monatliche Events ==="));
        player.sendMessage(Component.text("§e1. Woche: Mining Championship"));
        player.sendMessage(Component.text("§e2. Woche: PvP Championship"));
        player.sendMessage(Component.text("§e3. Woche: Building Contest"));
        player.sendMessage(Component.text("§e4. Woche: Monthly Tournament"));
    }

    private void openNotificationSettings(Player player) {
        player.sendMessage(Component.text("§aEvent-Benachrichtigungen werden konfiguriert..."));
        // TODO: Implement notification settings GUI
    }

    private void showEventRewards(Player player) {
        player.sendMessage(Component.text("§a=== Event-Belohnungen ==="));
        player.sendMessage(Component.text("§eTeilnahme: 100 Coins + XP"));
        player.sendMessage(Component.text("§eTop 10: 500 Coins + Rare Items"));
        player.sendMessage(Component.text("§eTop 3: 1000 Coins + Legendary Items"));
        player.sendMessage(Component.text("§e1. Platz: 2000 Coins + Exclusive Items"));
    }

    private void showEventHistory(Player player) {
        player.sendMessage(Component.text("§aEvent-Verlauf wird geladen..."));
        // TODO: Implement event history GUI
    }

    private void playOpenSound() {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
    }

    public ItemStack createGuiItem(Material material, String name, String... lore) {
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
