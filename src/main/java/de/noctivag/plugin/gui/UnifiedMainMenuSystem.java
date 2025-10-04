package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;

/**
 * Unified Main Menu System - Konsolidiert alle Hauptmenü-Implementierungen
 *
 * Ersetzt:
 * - UltimateMainMenu
 * - MainMenu
 * - EnhancedMainMenu
 * - IntegratedMenuSystem
 *
 * Features:
 * - Einheitliche Menü-Struktur
 * - Konfigurierbare Menü-Modi
 * - Hypixel-Style Design
 * - Alle Plugin-Features integriert
 */
public class UnifiedMainMenuSystem extends CustomGUI {
    private final Plugin plugin;
    private final Player player;
    private final HypixelMenuStyleSystem styleSystem;
    private final MenuMode menuMode;

    public enum MenuMode {
        ULTIMATE,    // Vollständiges Menü mit allen Features
        ENHANCED,    // Erweiterte Version mit Kategorien
        SIMPLE,      // Einfache Version für neue Spieler
        INTEGRATED   // Integriertes System-Menü
    }

    public UnifiedMainMenuSystem(Plugin plugin, Player player, MenuMode mode) {
        super(54, Component.text(getMenuTitle(mode)).color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.plugin = plugin;
        this.player = player;
        this.menuMode = mode;
        this.styleSystem = new HypixelMenuStyleSystem(plugin);
        setupItems();
        playOpenSound();
    }

    private static String getMenuTitle(MenuMode mode) {
        return switch (mode) {
            case ULTIMATE -> "§6§l⚡ ULTIMATE MENU ⚡";
            case ENHANCED -> "§6§lEnhanced Hauptmenü";
            case SIMPLE -> "§6✧ Hauptmenü ✧";
            case INTEGRATED -> "§6§l⚡ INTEGRATED MENU ⚡";
        };
    }

    private void setupItems() {
        // Setup Hypixel-style border
        styleSystem.setupHypixelBorder(getInventory());

        switch (menuMode) {
            case ULTIMATE -> setupUltimateMenu();
            case ENHANCED -> setupEnhancedMenu();
            case SIMPLE -> setupSimpleMenu();
            case INTEGRATED -> setupIntegratedMenu();
        }
    }

    private void setupUltimateMenu() {
        // Header with player info
        styleSystem.setupPlayerHeader(getInventory(), player, 4);

        // Category headers and sections
        setupCoreFeatures();
        setupCombatEvents();
        setupSocialEconomy();
        setupUtilityAdmin();
        setupNewSystems();
        setupHypixelFeatures();
        setupNavigationInfo();
    }

    private void setupEnhancedMenu() {
        // Row 1 - Core Features
        setItem(10, Material.PLAYER_HEAD, "§a§lProfil",
            "§7Zeige dein Profil und Statistiken",
            "§eKlicke zum Öffnen");

        setItem(11, Material.DIAMOND_SWORD, "§c§lEvents & Bosses",
            "§7Nimm an Events teil und kämpfe gegen Bosses",
            "§eKlicke zum Öffnen");

        setItem(12, Material.NETHER_STAR, "§d§lCosmetics",
            "§7Kaufe und aktiviere Cosmetics",
            "§eKlicke zum Öffnen");

        setItem(13, Material.GOLD_INGOT, "§6§lAchievements",
            "§7Schaue deine Achievements an",
            "§eKlicke zum Öffnen");

        setItem(14, Material.CHEST, "§e§lDaily Rewards",
            "§7Hole deine täglichen Belohnungen",
            "§eKlicke zum Öffnen");

        // Row 2 - Social Features
        setItem(15, Material.CAKE, "§b§lParty System",
            "§7Erstelle oder trete einer Party bei",
            "§eKlicke zum Öffnen");

        setItem(16, Material.PLAYER_HEAD, "§b§lFreunde",
            "§7Verwalte deine Freunde",
            "§eKlicke zum Öffnen");

        setItem(17, Material.COMPASS, "§e§lTeleportation",
            "§7Warp, Home und Teleportation",
            "§eKlicke zum Öffnen");

        setItem(18, Material.ENDER_CHEST, "§5§lWarps",
            "§7Besuche verschiedene Warps",
            "§eKlicke zum Öffnen");

        setItem(19, Material.BOOK, "§f§lKits",
            "§7Kaufe und verwende Kits",
            "§eKlicke zum Öffnen");

        // Row 3 - Utility Features
        setItem(20, Material.ANVIL, "§7§lBasic Commands",
            "§7Nickname, Prefix und Werkzeuge",
            "§eKlicke zum Öffnen");

        setItem(21, Material.WRITABLE_BOOK, "§a§lJoin/Leave Messages",
            "§7Bearbeite deine Join/Leave Nachrichten",
            "§eKlicke zum Öffnen");

        setItem(22, Material.REDSTONE, "§c§lEinstellungen",
            "§7Plugin-Einstellungen und Features",
            "§eKlicke zum Öffnen");

        setItem(23, Material.COMMAND_BLOCK, "§6§lAdmin Panel",
            "§7Admin-Tools und Verwaltung",
            "§eKlicke zum Öffnen");

        setItem(24, Material.EMERALD, "§a§lEconomy",
            "§7Geld und Wirtschaftssystem",
            "§eKlicke zum Öffnen");

        // Row 4 - Information
        setItem(25, Material.KNOWLEDGE_BOOK, "§b§lPlugin Features",
            "§7Alle verfügbaren Features",
            "§eKlicke zum Öffnen");

        setItem(26, Material.CLOCK, "§e§lEvent Zeitplan",
            "§7Zeigt kommende Events",
            "§eKlicke zum Öffnen");

        setItem(27, Material.BARRIER, "§c§lSchließen",
            "§7Schließe das Menü",
            "§eKlicke zum Schließen");
    }

    private void setupSimpleMenu() {
        // Player summary (center top)
        setItem(4, createPlayerHead(player, "§e" + player.getName(),
            "§7Level: §a" + plugin.getPlayerDataManager().getLevel(player),
            "§7Coins: §6" + plugin.getEconomyManager().formatMoney(plugin.getEconomyManager().getBalance(player))));

        // Row 2: Primary navigation
        setItem(10, createGuiItem(Material.NETHER_STAR, "§d§lKosmetik",
            "§7Partikel, Trails, Effekte"));

        setItem(12, createGuiItem(Material.ENDER_PEARL, "§b§lWarps",
            "§7Schnellreise & Kategorien"));

        setItem(14, createGuiItem(Material.CHEST, "§6§lTägliche Belohnungen",
            "§7Tägliches Einloggen belohnt!"));

        setItem(16, createGuiItem(Material.EXPERIENCE_BOTTLE, "§e§lErfolge",
            "§7Übersicht & Fortschritt"));

        // Row 4: Utilities
        setItem(28, createGuiItem(Material.DIAMOND_SWORD, "§a§lKit-Shop",
            "§7Kits kaufen & verwalten"));

        setItem(30, createGuiItem(Material.DRAGON_EGG, "§5§lEvents",
            "§7Aktive Events & Zeitplan"));

        setItem(32, createGuiItem(Material.BOOK, "§9§lNachrichten",
            "§7Join-Message verwalten"));

        setItem(34, createGuiItem(Material.REDSTONE_TORCH, "§c§lEinstellungen",
            "§7Feature-Toggles & Optionen"));

        // Row 3: Additional features
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

    private void setupIntegratedMenu() {
        // Header
        styleSystem.setupHeaderSection(getInventory(), 4, "INTEGRATED MENU", "Access all plugin features from one central location");

        // Core systems row
        setupCoreSystems();

        // Item systems row
        setupItemSystems();

        // Pet and accessory systems row
        setupPetAndAccessorySystems();

        // Utility systems row
        setupUtilitySystems();

        // Footer
        setupFooter();
    }

    // Alle Setup-Methoden aus den ursprünglichen Klassen hier integrieren...
    private void setupCoreFeatures() {
        setItem(10, styleSystem.createCategoryHeader("CORE FEATURES", "Essential player features and tools"));

        setItem(11, styleSystem.createFeatureItem(Material.PLAYER_HEAD, "👤 PROFILE",
            "View detailed player profile and statistics", true,
            "• Statistics and progress tracking",
            "• Achievements and rewards",
            "• Settings and preferences"));

        boolean canClaim = plugin.getDailyRewardManager() != null && plugin.getDailyRewardManager().canClaimReward(player);
        Material rewardIcon = canClaim ? Material.EMERALD : Material.GRAY_DYE;
        setItem(12, styleSystem.createFeatureItem(rewardIcon, "🎁 DAILY REWARDS",
            "Claim your daily rewards and build streaks", canClaim,
            "• Current streak: " + (plugin.getDailyRewardManager() != null ? plugin.getDailyRewardManager().getCurrentStreak(player) : 0) + " days",
            "• Bonus rewards for longer streaks",
            "• Special weekend bonuses"));

        setItem(13, styleSystem.createFeatureItem(Material.GOLD_INGOT, "🏆 ACHIEVEMENTS",
            "View and track your achievement progress", true,
            "• Completed: " + (plugin.getAchievementManager() != null ? plugin.getAchievementManager().getCompletedAchievements(player) : 0) + "/" + (plugin.getAchievementManager() != null ? plugin.getAchievementManager().getTotalAchievements() : 100),
            "• Progress tracking and rewards",
            "• Special achievement categories"));

        setItem(14, styleSystem.createFeatureItem(Material.NETHER_STAR, "✨ COSMETICS",
            "Customize your appearance with cosmetics", true,
            "• Particle effects and trails",
            "• Wings and halos",
            "• Sound effects and animations"));

        setItem(15, styleSystem.createFeatureItem(Material.CHEST, "📦 KITS",
            "Purchase and use various kits", true,
            "• Different kit categories",
            "• Cooldowns and pricing",
            "• Kit management system"));

        setItem(16, styleSystem.createFeatureItem(Material.ENDER_PEARL, "🌍 WARPS",
            "Travel to different server locations", true,
            "• Public and private warps",
            "• Warp categories and favorites",
            "• Quick travel system"));

        setItem(17, styleSystem.createFeatureItem(Material.COMPASS, "🧭 TELEPORTATION",
            "Advanced teleportation and navigation", true,
            "• Home system with multiple homes",
            "• TPA and RTP functionality",
            "• Teleportation tools and utilities"));

        setItem(18, styleSystem.createFeatureItem(Material.WRITABLE_BOOK, "💬 MESSAGES",
            "Customize join/leave messages", true,
            "• Custom message templates",
            "• Presets and themes",
            "• Message management tools"));
    }

    private void setupCombatEvents() {
        setItem(19, styleSystem.createCategoryHeader("COMBAT & EVENTS", "Fight, compete, and earn rewards"));

        setItem(20, styleSystem.createFeatureItem(Material.DRAGON_HEAD, "⚔️ EVENTS & BOSSES",
            "Participate in events and fight powerful bosses", true,
            "• 8 different boss types with unique mechanics",
            "• Automatic event scheduling system",
            "• Unique rewards and rare drops",
            "• Advanced arena system"));

        setItem(21, styleSystem.createFeatureItem(Material.DIAMOND_SWORD, "⚔️ PVP ARENA",
            "Fight against other players in competitive matches", true,
            "• Multiple PvP arena types",
            "• Rankings and statistics tracking",
            "• Rewards and prizes for winners"));

        setItem(22, styleSystem.createFeatureItem(Material.ZOMBIE_HEAD, "👹 MOB ARENA",
            "Battle against waves of monsters", true,
            "• Multiple difficulty waves",
            "• Progressive difficulty scaling",
            "• Special rewards for completion"));

        setItem(23, styleSystem.createFeatureItem(Material.IRON_SWORD, "⚔️ DUEL SYSTEM",
            "Challenge other players to 1v1 duels", true,
            "• 1v1 combat system",
            "• Various duel modes and rules",
            "• Ranking and leaderboard system"));

        setItem(24, styleSystem.createFeatureItem(Material.GOLDEN_SWORD, "🏆 TOURNAMENT",
            "Participate in regular tournaments", true,
            "• Regular tournament events",
            "• Large prize pools",
            "• Tournament rankings and rewards"));

        setItem(25, styleSystem.createFeatureItem(Material.BOOK, "📚 BATTLE PASS",
            "Complete challenges and earn rewards", true,
            "• Daily and weekly challenges",
            "• Progressive reward system",
            "• Premium battle pass features"));

        setItem(26, styleSystem.createFeatureItem(Material.MAP, "🗺️ QUESTS",
            "Complete quests and earn rewards", true,
            "• Daily quest system",
            "• Weekly special quests",
            "• Event-specific quest challenges"));

        setItem(27, styleSystem.createFeatureItem(Material.PAPER, "📊 STATISTICS",
            "View your detailed statistics", true,
            "• Combat and PvP statistics",
            "• Event participation tracking",
            "• General gameplay statistics"));

        setItem(28, styleSystem.createFeatureItem(Material.GOLD_BLOCK, "🏅 LEADERBOARDS",
            "View server leaderboards and rankings", true,
            "• Multiple ranking categories",
            "• Top player displays",
            "• Personal ranking positions"));
    }

    private void setupSocialEconomy() {
        setItem(29, styleSystem.createCategoryHeader("SOCIAL & ECONOMY", "Connect with players and manage your wealth"));

        setItem(30, styleSystem.createFeatureItem(Material.CAKE, "🎉 PARTY SYSTEM",
            "Create or join parties with other players", true,
            "• Party creation and management",
            "• Party events and activities",
            "• Shared rewards and benefits"));

        setItem(31, styleSystem.createFeatureItem(Material.PLAYER_HEAD, "👥 FRIENDS",
            "Manage your friends list and social connections", true,
            "• Add and remove friends",
            "• Friend status and activity",
            "• Private messaging system"));

        setItem(32, styleSystem.createFeatureItem(Material.SHIELD, "🏰 GUILD SYSTEM",
            "Join or create guilds for team play", true,
            "• Guild creation and management",
            "• Guild wars and competitions",
            "• Guild benefits and rewards"));

        setItem(33, styleSystem.createFeatureItem(Material.WRITABLE_BOOK, "💬 CHAT CHANNELS",
            "Access different communication channels", true,
            "• Global and local chat",
            "• Private channel system",
            "• Channel moderation tools"));

        setItem(34, styleSystem.createFeatureItem(Material.EMERALD, "💰 ECONOMY",
            "Manage your money and economic activities", true,
            "• Balance tracking and management",
            "• Transaction history",
            "• Economic statistics"));

        setItem(35, styleSystem.createFeatureItem(Material.CHEST, "🛒 SHOP",
            "Purchase various items and services", true,
            "• Multiple item categories",
            "• Special offers and sales",
            "• Shopping cart functionality"));

        setItem(36, styleSystem.createFeatureItem(Material.GOLD_INGOT, "🏪 AUCTION HOUSE",
            "Buy and sell items through auctions", true,
            "• Create and manage auctions",
            "• Place bids on items",
            "• Sales history and tracking"));

        setItem(37, styleSystem.createFeatureItem(Material.ENDER_CHEST, "🏦 BANK",
            "Secure storage and management of your money", true,
            "• Account management",
            "• Interest and investment options",
            "• Transaction security"));

        setItem(38, styleSystem.createFeatureItem(Material.DIAMOND_PICKAXE, "💼 JOBS",
            "Earn money through various job activities", true,
            "• Multiple job categories",
            "• Level progression system",
            "• Job-specific rewards"));
    }

    private void setupUtilityAdmin() {
        setItem(39, styleSystem.createCategoryHeader("UTILITY & ADMIN", "Tools and administrative functions"));

        setItem(40, styleSystem.createFeatureItem(Material.REDSTONE, "⚙️ SETTINGS",
            "Configure plugin settings and preferences", true,
            "• Feature toggles and options",
            "• Notification preferences",
            "• Personal customization settings"));

        setItem(41, styleSystem.createFeatureItem(Material.COMMAND_BLOCK, "👑 ADMIN PANEL",
            "Access administrative tools and controls", player.hasPermission("basics.admin"),
            "• Player management tools",
            "• Server administration",
            "• Plugin configuration"));
    }

    private void setupNewSystems() {
        setItem(42, styleSystem.createCategoryHeader("ADVANCED SYSTEMS", "Advanced item and system management"));

        setItem(43, styleSystem.createFeatureItem(Material.BOOK, "🥔 POTATO BOOKS",
            "Upgrade armor and weapons with potato books", true,
            "• Hot Potato Books for stat boosts",
            "• Fuming Potato Books for enhanced upgrades",
            "• Stat improvement system"));

        setItem(44, styleSystem.createFeatureItem(Material.REDSTONE, "🔮 RECOMBOBULATOR",
            "Upgrade item rarity and quality", true,
            "• Item rarity upgrade system",
            "• Success rate calculations",
            "• Cost and resource management"));

        setItem(45, styleSystem.createFeatureItem(Material.NETHER_STAR, "⭐ DUNGEON STARS",
            "Enhance dungeon items with stars", true,
            "• 1-5 star upgrade system",
            "• Dungeon-specific bonuses",
            "• Essence collection system"));

        setItem(46, styleSystem.createFeatureItem(Material.BONE, "🐾 PET ITEMS",
            "Enhance and upgrade your pets", true,
            "• Pet candy for experience",
            "• Pet food for stat boosts",
            "• Pet upgrade and evolution system"));
    }

    private void setupHypixelFeatures() {
        setItem(47, styleSystem.createCategoryHeader("HYPIXEL FEATURES", "Special Hypixel Skyblock-style features"));

        setItem(48, styleSystem.createFeatureItem(Material.COOKIE, "🍪 BOOSTER COOKIE",
            "Temporary bonuses and enhancements", true,
            "• +25% Mining Speed boost",
            "• +20% Combat Damage increase",
            "• +50% Magic Find enhancement",
            "• 4-day duration with multiple bonuses"));

        setItem(49, styleSystem.createFeatureItem(Material.BOOK, "📖 RECIPE BOOK",
            "Discover and view item recipes", true,
            "• Weapon and armor recipes",
            "• Tool and accessory recipes",
            "• Pet and special item recipes",
            "• Progress tracking system"));
    }

    private void setupNavigationInfo() {
        setItem(47, styleSystem.createFeatureItem(Material.CLOCK, "⏰ EVENT SCHEDULE",
            "View upcoming events and schedules", true,
            "• Next events and timers",
            "• Event history and participation",
            "• Special event notifications"));

        setItem(48, styleSystem.createFeatureItem(Material.BEACON, "🖥️ SERVER INFO",
            "View server information and statistics", true,
            "• Online players: " + plugin.getServer().getOnlinePlayers().size(),
            "• Server TPS and performance",
            "• Uptime and status"));

        setItem(49, styleSystem.createFeatureItem(Material.NETHER_STAR, "⚡ QUICK ACTIONS",
            "Access quick actions and shortcuts", true,
            "• Teleport to spawn",
            "• Sort inventory",
            "• Quick commands"));

        styleSystem.setupNavigationFooter(getInventory(), 50, false, false);
    }

    private void setupCoreSystems() {
        setItem(10, styleSystem.createCategoryHeader("CORE SYSTEMS", "Essential character and profile systems"));

        setItem(11, styleSystem.createFeatureItem(Material.BOOK, "📊 STATS SYSTEM",
            "View and manage your character statistics", true,
            "• Character stat overview",
            "• Bonus and modifier tracking",
            "• Stat comparison tools"));

        setItem(12, styleSystem.createFeatureItem(Material.PLAYER_HEAD, "👤 PROFILE SYSTEM",
            "Access your detailed player profile", true,
            "• Player progress tracking",
            "• Achievement and statistics",
            "• Profile customization options"));
    }

    private void setupItemSystems() {
        setItem(19, styleSystem.createCategoryHeader("ITEM SYSTEMS", "Enhance and upgrade your items"));

        setItem(20, styleSystem.createFeatureItem(Material.ANVIL, "🔨 REFORGE SYSTEM",
            "Reforge items to enhance their statistics", true,
            "• Multiple reforge types available",
            "• Stat enhancement system",
            "• Cost and success rate tracking"));

        setItem(21, styleSystem.createFeatureItem(Material.NETHER_STAR, "⭐ REFORGE STONES",
            "Manage and use reforge stones", true,
            "• Stone crafting and management",
            "• Stone usage optimization",
            "• Stone collection system"));

        setItem(22, styleSystem.createFeatureItem(Material.ENCHANTING_TABLE, "✨ ENCHANTING SYSTEM",
            "Enchant items with custom enchantments", true,
            "• Custom enchantment types",
            "• Enchantment level management",
            "• Enchanting cost calculations"));
    }

    private void setupPetAndAccessorySystems() {
        setItem(28, styleSystem.createCategoryHeader("PET & ACCESSORY", "Manage pets and equip accessories"));

        setItem(29, styleSystem.createFeatureItem(Material.WOLF_SPAWN_EGG, "🐾 PET SYSTEM",
            "Manage your pets and their abilities", true,
            "• Pet activation and management",
            "• Pet leveling and upgrades",
            "• Pet ability enhancement"));

        setItem(30, styleSystem.createFeatureItem(Material.GOLD_INGOT, "💍 ACCESSORY SYSTEM",
            "Equip accessories to boost your stats", true,
            "• Ring and necklace management",
            "• Accessory stat bonuses",
            "• Accessory upgrade system"));
    }

    private void setupUtilitySystems() {
        setItem(37, styleSystem.createCategoryHeader("UTILITY", "Settings and help systems"));

        setItem(38, styleSystem.createFeatureItem(Material.REDSTONE, "⚙️ SETTINGS",
            "Configure plugin settings and preferences", true,
            "• Feature toggles and options",
            "• Personal preference settings",
            "• System configuration"));

        setItem(39, styleSystem.createFeatureItem(Material.BOOK, "❓ HELP",
            "Get help with plugin features", true,
            "• Tutorials and guides",
            "• Feature documentation",
            "• Support and assistance"));

        styleSystem.setupNavigationFooter(getInventory(), 40, false, false);
    }

    private void setupFooter() {
        setItem(49, styleSystem.createStatusItem(Material.GREEN_DYE, "SYSTEM STATUS",
            "All integrated systems are operational", true));
    }

    private void playOpenSound() {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
    }

    public void setItem(int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) {
                meta.lore(Arrays.stream(lore).map(Component::text).toList());
            }
            item.setItemMeta(meta);
        }

        getInventory().setItem(slot, item);
    }

    // Factory-Methoden für verschiedene Menü-Modi
    public static UnifiedMainMenuSystem createUltimateMenu(Plugin plugin, Player player) {
        return new UnifiedMainMenuSystem(plugin, player, MenuMode.ULTIMATE);
    }

    public static UnifiedMainMenuSystem createEnhancedMenu(Plugin plugin, Player player) {
        return new UnifiedMainMenuSystem(plugin, player, MenuMode.ENHANCED);
    }

    public static UnifiedMainMenuSystem createSimpleMenu(Plugin plugin, Player player) {
        return new UnifiedMainMenuSystem(plugin, player, MenuMode.SIMPLE);
    }

    public static UnifiedMainMenuSystem createIntegratedMenu(Plugin plugin, Player player) {
        return new UnifiedMainMenuSystem(plugin, player, MenuMode.INTEGRATED);
    }
}
