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
 * Duel System GUI - 1v1 dueling system
 */
public class DuelSystemGUI extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final Player player;

    public DuelSystemGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(54, Component.text("§6§l⚔️ Duel System").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.SkyblockPlugin = SkyblockPlugin;
        this.player = player;
        setupItems();
        playOpenSound();
    }

    private void setupItems() {
        // Header
        setItem(4, createGuiItem(Material.IRON_SWORD, "§6§l⚔️ Duel System",
            "§7Challenge other players to 1v1 duels",
            "§eChoose your duel type"));

        // Challenge Player
        setItem(10, createGuiItem(Material.EMERALD, "§a§l🎯 Challenge Player",
            "§7Challenge a specific player",
            "§7• Select opponent",
            "§7• Choose duel type",
            "§7• Set wager amount",
            "§eClick to challenge"));

        // Accept Challenges
        setItem(11, createGuiItem(Material.GOLD_INGOT, "§6§l✅ Accept Challenges",
            "§7View and accept pending challenges",
            "§7• Pending challenges",
            "§7• Challenge details",
            "§7• Accept or decline",
            "§eClick to view"));

        // Duel History
        setItem(12, createGuiItem(Material.BOOK, "§b§l📚 Duel History",
            "§7View your duel history",
            "§7• Past duels",
            "§7• Win/Loss record",
            "§7• Statistics",
            "§eClick to view"));

        // Duel Settings
        setItem(13, createGuiItem(Material.REDSTONE_TORCH, "§e§l⚙️ Duel Settings",
            "§7Configure duel preferences",
            "§7• Auto-accept settings",
            "§7• Notification preferences",
            "§7• Duel rules",
            "§eClick to configure"));

        // Duel Leaderboard
        setItem(14, createGuiItem(Material.DIAMOND, "§d§l🏆 Duel Leaderboard",
            "§7View top duel players",
            "§7• Top players",
            "§7• Win streaks",
            "§7• Rankings",
            "§eClick to view"));

        // Duel Shop
        setItem(15, createGuiItem(Material.CHEST, "§c§l🛒 Duel Shop",
            "§7Buy duel exclusive items",
            "§7• Duel weapons",
            "§7• Special abilities",
            "§7• Cosmetics",
            "§eClick to open"));

        // Back
        setItem(49, createGuiItem(Material.BARRIER, "§c§lBack",
            "§7Return to main menu"));
    }

    public void onClick(Player player, int slot, ItemStack item, ClickType clickType) {
        switch (slot) {
            case 10 -> openChallengePlayer(player);
            case 11 -> showPendingChallenges(player);
            case 12 -> showDuelHistory(player);
            case 13 -> openDuelSettings(player);
            case 14 -> showDuelLeaderboard(player);
            case 15 -> openDuelShop(player);
            case 49 -> {
                player.closeInventory();
                new UnifiedMainMenuSystem(SkyblockPlugin, player, UnifiedMainMenuSystem.MenuMode.ULTIMATE).open(player);
            }
        }
    }

    private void openChallengePlayer(Player player) {
        player.sendMessage(Component.text("§aSpieler-Auswahl wird geöffnet..."));
        // TODO: Implement player selection GUI
    }

    private void showPendingChallenges(Player player) {
        player.sendMessage(Component.text("§a=== Ausstehende Herausforderungen ==="));
        player.sendMessage(Component.text("§eKeine ausstehenden Herausforderungen"));
        // TODO: Implement pending challenges GUI
    }

    private void showDuelHistory(Player player) {
        player.sendMessage(Component.text("§a=== Duel-Verlauf ==="));
        player.sendMessage(Component.text("§eGewonnen: 0"));
        player.sendMessage(Component.text("§eVerloren: 0"));
        player.sendMessage(Component.text("§eWin-Streak: 0"));
        // TODO: Implement duel history GUI
    }

    private void openDuelSettings(Player player) {
        player.sendMessage(Component.text("§aDuel-Einstellungen werden geöffnet..."));
        // TODO: Implement duel settings GUI
    }

    private void showDuelLeaderboard(Player player) {
        player.sendMessage(Component.text("§a=== Duel-Rangliste ==="));
        player.sendMessage(Component.text("§e1. Player1 - 15 Wins"));
        player.sendMessage(Component.text("§e2. Player2 - 12 Wins"));
        player.sendMessage(Component.text("§e3. Player3 - 10 Wins"));
        // TODO: Implement duel leaderboard GUI
    }

    private void openDuelShop(Player player) {
        player.sendMessage(Component.text("§aDuel Shop wird geöffnet..."));
        // TODO: Implement duel shop GUI
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
