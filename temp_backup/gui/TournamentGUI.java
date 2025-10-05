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
 * Tournament GUI - Tournament management and participation
 */
public class TournamentGUI extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final Player player;

    public TournamentGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(54, Component.text("§6§l🏆 Tournament").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.SkyblockPlugin = SkyblockPlugin;
        this.player = player;
        setupItems();
        playOpenSound();
    }

    private void setupItems() {
        // Header
        setItem(4, createGuiItem(Material.GOLD_INGOT, "§6§l🏆 Tournament",
            "§7Compete in server tournaments",
            "§eJoin the competition"));

        // Active Tournaments
        setItem(10, createGuiItem(Material.EMERALD, "§a§l⚡ Active Tournaments",
            "§7Currently running tournaments",
            "§7• PvP Tournament: 16:00",
            "§7• Building Contest: 18:00",
            "§7• Mining Championship: 20:00",
            "§eClick to join"));

        // Upcoming Tournaments
        setItem(11, createGuiItem(Material.CLOCK, "§e§l⏰ Upcoming Tournaments",
            "§7Tournaments starting soon",
            "§7• Weekend Tournament: Tomorrow 15:00",
            "§7• Monthly Championship: Next week",
            "§7• Special Event: Next month",
            "§eClick to view schedule"));

        // Tournament History
        setItem(12, createGuiItem(Material.BOOK, "§b§l📚 Tournament History",
            "§7View past tournament results",
            "§7• Previous winners",
            "§7• Tournament statistics",
            "§7• Personal achievements",
            "§eClick to view"));

        // Tournament Rules
        setItem(13, createGuiItem(Material.WRITTEN_BOOK, "§c§l📋 Tournament Rules",
            "§7Read tournament rules and regulations",
            "§7• General rules",
            "§7• Specific tournament rules",
            "§7• Fair play guidelines",
            "§eClick to read"));

        // Tournament Rewards
        setItem(14, createGuiItem(Material.DIAMOND, "§d§l💎 Tournament Rewards",
            "§7View available tournament rewards",
            "§7• Participation rewards",
            "§7• Ranking rewards",
            "§7• Special prizes",
            "§eClick to view"));

        // Tournament Statistics
        setItem(15, createGuiItem(Material.PAPER, "§6§l📊 Tournament Statistics",
            "§7Your tournament performance",
            "§7• Tournament wins",
            "§7• Average ranking",
            "§7• Total participation",
            "§eClick to view"));

        // Back
        setItem(49, createGuiItem(Material.BARRIER, "§c§lBack",
            "§7Return to main menu"));
    }

    public void onClick(Player player, int slot, ItemStack item, ClickType clickType) {
        switch (slot) {
            case 10 -> showActiveTournaments(player);
            case 11 -> showUpcomingTournaments(player);
            case 12 -> showTournamentHistory(player);
            case 13 -> showTournamentRules(player);
            case 14 -> showTournamentRewards(player);
            case 15 -> showTournamentStatistics(player);
            case 49 -> {
                player.closeInventory();
                new UnifiedMainMenuSystem(SkyblockPlugin, player, UnifiedMainMenuSystem.MenuMode.ULTIMATE).open(player);
            }
        }
    }

    private void showActiveTournaments(Player player) {
        player.sendMessage(Component.text("§a=== Aktive Turniere ==="));
        player.sendMessage(Component.text("§ePvP Tournament - 16:00"));
        player.sendMessage(Component.text("§eBuilding Contest - 18:00"));
        player.sendMessage(Component.text("§eMining Championship - 20:00"));
        player.sendMessage(Component.text("§aKlicke auf ein Turnier, um teilzunehmen!"));
    }

    private void showUpcomingTournaments(Player player) {
        player.sendMessage(Component.text("§a=== Kommende Turniere ==="));
        player.sendMessage(Component.text("§eWeekend Tournament - Morgen 15:00"));
        player.sendMessage(Component.text("§eMonthly Championship - Nächste Woche"));
        player.sendMessage(Component.text("§eSpecial Event - Nächsten Monat"));
    }

    private void showTournamentHistory(Player player) {
        player.sendMessage(Component.text("§a=== Turnier-Verlauf ==="));
        player.sendMessage(Component.text("§eLetztes PvP Tournament: 3. Platz"));
        player.sendMessage(Component.text("§eBuilding Contest: 1. Platz"));
        player.sendMessage(Component.text("§eMining Championship: 5. Platz"));
    }

    private void showTournamentRules(Player player) {
        player.sendMessage(Component.text("§a=== Turnier-Regeln ==="));
        player.sendMessage(Component.text("§e1. Fair Play - Keine Cheats oder Exploits"));
        player.sendMessage(Component.text("§e2. Respekt - Behandle andere Spieler respektvoll"));
        player.sendMessage(Component.text("§e3. Pünktlichkeit - Sei rechtzeitig da"));
        player.sendMessage(Component.text("§e4. Disqualifikation bei Regelverstößen"));
    }

    private void showTournamentRewards(Player player) {
        player.sendMessage(Component.text("§a=== Turnier-Belohnungen ==="));
        player.sendMessage(Component.text("§eTeilnahme: 500 Coins + XP"));
        player.sendMessage(Component.text("§eTop 10: 1000 Coins + Rare Items"));
        player.sendMessage(Component.text("§eTop 3: 2500 Coins + Legendary Items"));
        player.sendMessage(Component.text("§e1. Platz: 5000 Coins + Exclusive Items"));
    }

    private void showTournamentStatistics(Player player) {
        player.sendMessage(Component.text("§a=== Turnier-Statistiken ==="));
        player.sendMessage(Component.text("§eTurniere gewonnen: 1"));
        player.sendMessage(Component.text("§eDurchschnittliche Platzierung: 3.5"));
        player.sendMessage(Component.text("§eGesamte Teilnahmen: 5"));
        player.sendMessage(Component.text("§eAktueller Rang: Gold"));
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
