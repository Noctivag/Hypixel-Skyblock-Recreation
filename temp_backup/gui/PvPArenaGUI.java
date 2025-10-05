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
 * PvP Arena GUI - PvP combat and arena management
 */
public class PvPArenaGUI extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final Player player;

    public PvPArenaGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(54, Component.text("§6§l⚔️ PvP Arena").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.SkyblockPlugin = SkyblockPlugin;
        this.player = player;
        setupItems();
        playOpenSound();
    }

    private void setupItems() {
        // Header
        setItem(4, createGuiItem(Material.DIAMOND_SWORD, "§6§l⚔️ PvP Arena",
            "§7Fight other players in arenas",
            "§eChoose your battle mode"));

        // Quick Match
        setItem(10, createGuiItem(Material.EMERALD, "§a§l⚡ Quick Match",
            "§7Join a random PvP match",
            "§7• Instant matchmaking",
            "§7• Balanced teams",
            "§7• Quick rewards",
            "§eClick to join"));

        // Ranked Matches
        setItem(11, createGuiItem(Material.GOLD_INGOT, "§6§l🏆 Ranked Matches",
            "§7Compete for ranking points",
            "§7• ELO rating system",
            "§7• Seasonal rewards",
            "§7• Leaderboards",
            "§eClick to join"));

        // Custom Matches
        setItem(12, createGuiItem(Material.COMPASS, "§b§l🎯 Custom Matches",
            "§7Create or join custom games",
            "§7• Custom rules",
            "§7• Private matches",
            "§7• Tournament mode",
            "§eClick to open"));

        // Arena Selection
        setItem(13, createGuiItem(Material.MAP, "§e§l🗺️ Arena Selection",
            "§7Choose your battle arena",
            "§7• Multiple arena types",
            "§7• Different environments",
            "§7• Special mechanics",
            "§eClick to select"));

        // PvP Statistics
        setItem(14, createGuiItem(Material.BOOK, "§d§l📊 PvP Statistics",
            "§7View your PvP performance",
            "§7• Win/Loss ratio",
            "§7• Kill/Death ratio",
            "§7• Ranking history",
            "§eClick to view"));

        // PvP Shop
        setItem(15, createGuiItem(Material.CHEST, "§c§l🛒 PvP Shop",
            "§7Buy PvP exclusive items",
            "§7• PvP weapons",
            "§7• Armor sets",
            "§7• Special abilities",
            "§eClick to open"));

        // Back
        setItem(49, createGuiItem(Material.BARRIER, "§c§lBack",
            "§7Return to main menu"));
    }

    public void onClick(Player player, int slot, ItemStack item, ClickType clickType) {
        switch (slot) {
            case 10 -> joinQuickMatch(player);
            case 11 -> joinRankedMatch(player);
            case 12 -> openCustomMatches(player);
            case 13 -> openArenaSelection(player);
            case 14 -> showPvPStatistics(player);
            case 15 -> openPvPShop(player);
            case 49 -> {
                player.closeInventory();
                new UnifiedMainMenuSystem(SkyblockPlugin, player, UnifiedMainMenuSystem.MenuMode.ULTIMATE).open(player);
            }
        }
    }

    private void joinQuickMatch(Player player) {
        player.sendMessage(Component.text("§aSuche nach einem Quick Match..."));
        // TODO: Implement quick match system
    }

    private void joinRankedMatch(Player player) {
        player.sendMessage(Component.text("§aSuche nach einem Ranked Match..."));
        // TODO: Implement ranked match system
    }

    private void openCustomMatches(Player player) {
        player.sendMessage(Component.text("§aCustom Matches werden geöffnet..."));
        // TODO: Implement custom matches GUI
    }

    private void openArenaSelection(Player player) {
        player.sendMessage(Component.text("§aArena-Auswahl wird geöffnet..."));
        // TODO: Implement arena selection GUI
    }

    private void showPvPStatistics(Player player) {
        player.sendMessage(Component.text("§a=== PvP Statistiken ==="));
        player.sendMessage(Component.text("§eWins: 0"));
        player.sendMessage(Component.text("§eLosses: 0"));
        player.sendMessage(Component.text("§eKills: 0"));
        player.sendMessage(Component.text("§eDeaths: 0"));
        player.sendMessage(Component.text("§eRanking: Unranked"));
    }

    private void openPvPShop(Player player) {
        player.sendMessage(Component.text("§aPvP Shop wird geöffnet..."));
        // TODO: Implement PvP shop GUI
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
