package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;


public class FeatureBookGUI extends CustomGUI {
    private final Plugin plugin;

    public FeatureBookGUI(Plugin plugin) {
        super(54, Component.text("§6§lPlugin Features"));
        this.plugin = plugin;
    }

    public void open(Player player) {
        clearInventory();

        // Header
        setItem(4, createGuiItem(Material.WRITTEN_BOOK, "§6§lPlugin Features",
            "§7Alle verfügbaren Plugin-Features"));

        // Core Features
        setItem(10, createGuiItem(Material.COMMAND_BLOCK, "§eBasic Commands",
            "§7• Nickname & Prefix",
            "§7• Workbenches (Werkbank, Amboss, etc.)",
            "§7• Utility Commands (Heal, Feed, Clear)",
            "",
            "§eLinksklick: §7Öffnen"));

        setItem(11, createGuiItem(Material.WRITTEN_BOOK, "§eJoin/Leave Messages",
            "§7• Custom Join/Leave Messages",
            "§7• Preset Messages",
            "§7• Toggle Messages",
            "",
            "§eLinksklick: §7Öffnen"));

        setItem(12, createGuiItem(Material.EMERALD, "§eEconomy System",
            "§7• Coins & Balance",
            "§7• Rank-based Cost Exemption",
            "§7• Economy: Internal",
            "",
            "§eLinksklick: §7Info anzeigen"));

        setItem(13, createGuiItem(Material.NAME_TAG, "§eRank System",
            "§7• Player Ranks",
            "§7• Permission Management",
            "§7• Rank-based Benefits",
            "",
            "§eLinksklick: §7Info anzeigen"));

        // Teleportation Features
        setItem(19, createGuiItem(Material.ENDER_PEARL, "§eTeleportation",
            "§7• /spawn, /back, /rtp",
            "§7• TPA System (Request/Accept/Deny)",
            "§7• Warp System",
            "§7• Home System",
            "",
            "§eLinksklick: §7Info anzeigen"));

        setItem(20, createGuiItem(Material.PLAYER_HEAD, "§eSocial Features",
            "§7• Party System",
            "§7• Friends System",
            "§7• AFK Status",
            "",
            "§eLinksklick: §7Info anzeigen"));

        setItem(21, createGuiItem(Material.FIREWORK_ROCKET, "§eCosmetics",
            "§7• Particle Effects",
            "§7• Sound Effects",
            "§7• Wings, Halos, Trails",
            "§7• Gadgets",
            "",
            "§eLinksklick: §7Öffnen"));

        setItem(22, createGuiItem(Material.EXPERIENCE_BOTTLE, "§eAchievements",
            "§7• Tiered Achievements",
            "§7• Progress Tracking",
            "§7• Coin Rewards",
            "",
            "§eLinksklick: §7Öffnen"));

        // Advanced Features
        setItem(28, createGuiItem(Material.DIAMOND_SWORD, "§eKit System",
            "§7• Pre-configured Kits",
            "§7• Kit Shop",
            "§7• Kit Cooldowns",
            "",
            "§eLinksklick: §7Öffnen"));

        setItem(29, createGuiItem(Material.CHEST, "§eDaily Rewards",
            "§7• Daily Login Rewards",
            "§7• Streak System",
            "§7• Special Rewards",
            "",
            "§eLinksklick: §7Öffnen"));

        setItem(30, createGuiItem(Material.CLOCK, "§eEvents",
            "§7• Server Events",
            "§7• Event Participation",
            "§7• Event Rewards",
            "",
            "§eLinksklick: §7Öffnen"));

        setItem(31, createGuiItem(Material.BOOKSHELF, "§eScoreboard",
            "§7• Dynamic Scoreboard",
            "§7• Player Stats Display",
            "§7• Server Information",
            "",
            "§eLinksklick: §7Info anzeigen"));

        // Admin Features
        if (player.hasPermission("basicsplugin.admin")) {
            setItem(37, createGuiItem(Material.REDSTONE_BLOCK, "§cAdmin Features",
                "§7• Player Management",
                "§7• Command Management",
                "§7• Rank Management",
                "§7• Economy Management",
                "",
                "§eLinksklick: §7Öffnen"));
        }

        // Back
        setItem(49, createGuiItem(Material.ARROW, "§7Zurück", "§7Zum Hauptmenü"));
        player.openInventory(getInventory());
    }
}
