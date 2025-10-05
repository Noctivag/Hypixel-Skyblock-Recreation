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
 * Friends GUI - Friend management system
 */
public class FriendsGUI extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final Player player;

    public FriendsGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(54, Component.text("§6§l👥 Friends").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.SkyblockPlugin = SkyblockPlugin;
        this.player = player;
        setupItems();
        playOpenSound();
    }

    private void setupItems() {
        // Header
        setItem(4, createGuiItem(Material.PLAYER_HEAD, "§6§l👥 Friends",
            "§7Manage your friends list",
            "§eConnect with other players"));

        // Friend List
        setItem(10, createGuiItem(Material.EMERALD, "§a§l👥 Friend List",
            "§7View your friends",
            "§7• Online friends",
            "§7• Offline friends",
            "§7• Friend status",
            "§eClick to view"));

        // Add Friend
        setItem(11, createGuiItem(Material.GOLD_INGOT, "§6§l➕ Add Friend",
            "§7Add new friends",
            "§7• Search players",
            "§7• Send friend requests",
            "§7• Accept requests",
            "§eClick to add"));

        // Friend Requests
        setItem(12, createGuiItem(Material.CLOCK, "§e§l📨 Friend Requests",
            "§7Manage friend requests",
            "§7• Pending requests",
            "§7• Sent requests",
            "§7• Accept/decline",
            "§eClick to manage"));

        // Friend Settings
        setItem(13, createGuiItem(Material.REDSTONE_TORCH, "§c§l⚙️ Friend Settings",
            "§7Configure friend preferences",
            "§7• Privacy settings",
            "§7• Notification preferences",
            "§7• Friend limits",
            "§eClick to configure"));

        // Friend Statistics
        setItem(14, createGuiItem(Material.BOOK, "§b§l📊 Friend Statistics",
            "§7View friend statistics",
            "§7• Total friends",
            "§7• Online friends",
            "§7• Friend activity",
            "§eClick to view"));

        // Friend Activities
        setItem(15, createGuiItem(Material.CAKE, "§d§l🎉 Friend Activities",
            "§7View friend activities",
            "§7• Recent activities",
            "§7• Achievements",
            "§7• Status updates",
            "§eClick to view"));

        // Back
        setItem(49, createGuiItem(Material.BARRIER, "§c§lBack",
            "§7Return to main menu"));
    }

    public void onClick(Player player, int slot, ItemStack item, ClickType clickType) {
        switch (slot) {
            case 10 -> showFriendList(player);
            case 11 -> openAddFriend(player);
            case 12 -> showFriendRequests(player);
            case 13 -> openFriendSettings(player);
            case 14 -> showFriendStatistics(player);
            case 15 -> showFriendActivities(player);
            case 49 -> {
                player.closeInventory();
                new UnifiedMainMenuSystem(SkyblockPlugin, player, UnifiedMainMenuSystem.MenuMode.ENHANCED).open(player);
            }
        }
    }

    private void showFriendList(Player player) {
        player.sendMessage(Component.text("§a=== Freundesliste ==="));
        player.sendMessage(Component.text("§eOnline Freunde: 0"));
        player.sendMessage(Component.text("§eOffline Freunde: 0"));
        player.sendMessage(Component.text("§aKeine Freunde gefunden"));
        // TODO: Implement friend list GUI
    }

    private void openAddFriend(Player player) {
        player.sendMessage(Component.text("§aFreund hinzufügen wird geöffnet..."));
        // TODO: Implement add friend GUI
    }

    private void showFriendRequests(Player player) {
        player.sendMessage(Component.text("§a=== Freundschaftsanfragen ==="));
        player.sendMessage(Component.text("§eAusstehende Anfragen: 0"));
        player.sendMessage(Component.text("§eGesendete Anfragen: 0"));
        // TODO: Implement friend requests GUI
    }

    private void openFriendSettings(Player player) {
        player.sendMessage(Component.text("§aFreundes-Einstellungen werden geöffnet..."));
        // TODO: Implement friend settings GUI
    }

    private void showFriendStatistics(Player player) {
        player.sendMessage(Component.text("§a=== Freundes-Statistiken ==="));
        player.sendMessage(Component.text("§eGesamte Freunde: 0"));
        player.sendMessage(Component.text("§eOnline Freunde: 0"));
        player.sendMessage(Component.text("§eFreundeslimit: 50"));
    }

    private void showFriendActivities(Player player) {
        player.sendMessage(Component.text("§a=== Freundes-Aktivitäten ==="));
        player.sendMessage(Component.text("§eKeine aktuellen Aktivitäten"));
        // TODO: Implement friend activities GUI
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
