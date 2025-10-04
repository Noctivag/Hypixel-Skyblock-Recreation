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

public class UltimateMainMenu extends CustomGUI {
    private final Plugin plugin;
    private final Player player;
    private final HypixelMenuStyleSystem styleSystem;

    public UltimateMainMenu(Plugin plugin, Player player) {
        super(54, Component.text("§6§l⚡ ULTIMATE MENU ⚡").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.plugin = plugin;
        this.player = player;
        this.styleSystem = new HypixelMenuStyleSystem(plugin);
        setupItems();
        playOpenSound();
    }

    private void setupItems() {
        // Setup Hypixel-style border
        styleSystem.setupHypixelBorder(getInventory());
        
        // Header with player info
        setupPlayerHeader();
        
        // Category headers and sections
        setupCoreFeatures();
        setupCombatEvents();
        setupSocialEconomy();
        setupUtilityAdmin();
        setupNewSystems();
        setupHypixelFeatures();
        
        // Navigation footer
        setupNavigationInfo();
    }

    private void setupPlayerHeader() {
        // Use Hypixel-style player header
        styleSystem.setupPlayerHeader(getInventory(), player, 4);
    }

    private void setupCoreFeatures() {
        // Category header
        setItem(10, styleSystem.createCategoryHeader("CORE FEATURES", "Essential player features and tools"));
        
        // Profile
        setItem(11, styleSystem.createFeatureItem(Material.PLAYER_HEAD, "👤 PROFILE", 
            "View detailed player profile and statistics", true,
            "• Statistics and progress tracking",
            "• Achievements and rewards",
            "• Settings and preferences"));

        // Daily Rewards
        boolean canClaim = false; // DailyRewardSystem not implemented yet
        Material rewardIcon = canClaim ? Material.EMERALD : Material.GRAY_DYE;
        setItem(12, styleSystem.createFeatureItem(rewardIcon, "🎁 DAILY REWARDS", 
            "Claim your daily rewards and build streaks", canClaim,
            "• Current streak: 0 days", // DailyRewardSystem not implemented yet
            "• Bonus rewards for longer streaks",
            "• Special weekend bonuses"));

        // Achievements
        setItem(13, styleSystem.createFeatureItem(Material.GOLD_INGOT, "🏆 ACHIEVEMENTS", 
            "View and track your achievement progress", true,
            "• Completed: 0/100", // AchievementSystem not implemented yet
            "• Progress tracking and rewards",
            "• Special achievement categories"));

        // Cosmetics
        setItem(14, styleSystem.createFeatureItem(Material.NETHER_STAR, "✨ COSMETICS", 
            "Customize your appearance with cosmetics", true,
            "• Particle effects and trails",
            "• Wings and halos",
            "• Sound effects and animations"));

        // Kits
        setItem(15, styleSystem.createFeatureItem(Material.CHEST, "📦 KITS", 
            "Purchase and use various kits", true,
            "• Different kit categories",
            "• Cooldowns and pricing",
            "• Kit management system"));

        // Warps
        setItem(16, styleSystem.createFeatureItem(Material.ENDER_PEARL, "🌍 WARPS", 
            "Travel to different server locations", true,
            "• Public and private warps",
            "• Warp categories and favorites",
            "• Quick travel system"));

        // Teleportation
        setItem(17, styleSystem.createFeatureItem(Material.COMPASS, "🧭 TELEPORTATION", 
            "Advanced teleportation and navigation", true,
            "• Home system with multiple homes",
            "• TPA and RTP functionality",
            "• Teleportation tools and utilities"));

        // Messages
        setItem(18, styleSystem.createFeatureItem(Material.WRITABLE_BOOK, "💬 MESSAGES", 
            "Customize join/leave messages", true,
            "• Custom message templates",
            "• Presets and themes",
            "• Message management tools"));
    }

    private void setupCombatEvents() {
        // Category header
        setItem(19, styleSystem.createCategoryHeader("COMBAT & EVENTS", "Fight, compete, and earn rewards"));
        
        // Events & Bosses
        setItem(20, styleSystem.createFeatureItem(Material.DRAGON_HEAD, "⚔️ EVENTS & BOSSES", 
            "Participate in events and fight powerful bosses", true,
            "• 8 different boss types with unique mechanics",
            "• Automatic event scheduling system",
            "• Unique rewards and rare drops",
            "• Advanced arena system"));

        // PvP Arena
        setItem(21, styleSystem.createFeatureItem(Material.DIAMOND_SWORD, "⚔️ PVP ARENA", 
            "Fight against other players in competitive matches", true,
            "• Multiple PvP arena types",
            "• Rankings and statistics tracking",
            "• Rewards and prizes for winners"));

        // Mob Arena
        setItem(22, styleSystem.createFeatureItem(Material.ZOMBIE_HEAD, "👹 MOB ARENA", 
            "Battle against waves of monsters", true,
            "• Multiple difficulty waves",
            "• Progressive difficulty scaling",
            "• Special rewards for completion"));

        // Duel System
        setItem(23, styleSystem.createFeatureItem(Material.IRON_SWORD, "⚔️ DUEL SYSTEM", 
            "Challenge other players to 1v1 duels", true,
            "• 1v1 combat system",
            "• Various duel modes and rules",
            "• Ranking and leaderboard system"));

        // Tournament
        setItem(24, styleSystem.createFeatureItem(Material.GOLDEN_SWORD, "🏆 TOURNAMENT", 
            "Participate in regular tournaments", true,
            "• Regular tournament events",
            "• Large prize pools",
            "• Tournament rankings and rewards"));

        // Battle Pass
        setItem(25, styleSystem.createFeatureItem(Material.BOOK, "📚 BATTLE PASS", 
            "Complete challenges and earn rewards", true,
            "• Daily and weekly challenges",
            "• Progressive reward system",
            "• Premium battle pass features"));

        // Quests
        setItem(26, styleSystem.createFeatureItem(Material.MAP, "🗺️ QUESTS", 
            "Complete quests and earn rewards", true,
            "• Daily quest system",
            "• Weekly special quests",
            "• Event-specific quest challenges"));

        // Statistics
        setItem(27, styleSystem.createFeatureItem(Material.PAPER, "📊 STATISTICS", 
            "View your detailed statistics", true,
            "• Combat and PvP statistics",
            "• Event participation tracking",
            "• General gameplay statistics"));

        // Leaderboards
        setItem(28, styleSystem.createFeatureItem(Material.GOLD_BLOCK, "🏅 LEADERBOARDS", 
            "View server leaderboards and rankings", true,
            "• Multiple ranking categories",
            "• Top player displays",
            "• Personal ranking positions"));
    }

    private void setupSocialEconomy() {
        // Category header
        setItem(29, styleSystem.createCategoryHeader("SOCIAL & ECONOMY", "Connect with players and manage your wealth"));
        
        // Party System
        setItem(30, styleSystem.createFeatureItem(Material.CAKE, "🎉 PARTY SYSTEM", 
            "Create or join parties with other players", true,
            "• Party creation and management",
            "• Party events and activities",
            "• Shared rewards and benefits"));

        // Friends
        setItem(31, styleSystem.createFeatureItem(Material.PLAYER_HEAD, "👥 FRIENDS", 
            "Manage your friends list and social connections", true,
            "• Add and remove friends",
            "• Friend status and activity",
            "• Private messaging system"));

        // Guild System
        setItem(32, styleSystem.createFeatureItem(Material.SHIELD, "🏰 GUILD SYSTEM", 
            "Join or create guilds for team play", true,
            "• Guild creation and management",
            "• Guild wars and competitions",
            "• Guild benefits and rewards"));

        // Chat Channels
        setItem(33, styleSystem.createFeatureItem(Material.WRITABLE_BOOK, "💬 CHAT CHANNELS", 
            "Access different communication channels", true,
            "• Global and local chat",
            "• Private channel system",
            "• Channel moderation tools"));

        // Economy
        setItem(34, styleSystem.createFeatureItem(Material.EMERALD, "💰 ECONOMY", 
            "Manage your money and economic activities", true,
            "• Balance tracking and management",
            "• Transaction history",
            "• Economic statistics"));

        // Shop
        setItem(35, styleSystem.createFeatureItem(Material.CHEST, "🛒 SHOP", 
            "Purchase various items and services", true,
            "• Multiple item categories",
            "• Special offers and sales",
            "• Shopping cart functionality"));

        // Auction House
        setItem(36, styleSystem.createFeatureItem(Material.GOLD_INGOT, "🏪 AUCTION HOUSE", 
            "Buy and sell items through auctions", true,
            "• Create and manage auctions",
            "• Place bids on items",
            "• Sales history and tracking"));

        // Bank
        setItem(37, styleSystem.createFeatureItem(Material.ENDER_CHEST, "🏦 BANK", 
            "Secure storage and management of your money", true,
            "• Account management",
            "• Interest and investment options",
            "• Transaction security"));

        // Jobs
        setItem(38, styleSystem.createFeatureItem(Material.DIAMOND_PICKAXE, "💼 JOBS", 
            "Earn money through various job activities", true,
            "• Multiple job categories",
            "• Level progression system",
            "• Job-specific rewards"));
    }

    private void setupNewSystems() {
        // Category header
        setItem(42, styleSystem.createCategoryHeader("ADVANCED SYSTEMS", "Advanced item and system management"));
        
        // Potato Book System
        setItem(43, styleSystem.createFeatureItem(Material.BOOK, "🥔 POTATO BOOKS", 
            "Upgrade armor and weapons with potato books", true,
            "• Hot Potato Books for stat boosts",
            "• Fuming Potato Books for enhanced upgrades",
            "• Stat improvement system"));

        // Recombobulator System
        setItem(44, styleSystem.createFeatureItem(Material.REDSTONE, "🔮 RECOMBOBULATOR", 
            "Upgrade item rarity and quality", true,
            "• Item rarity upgrade system",
            "• Success rate calculations",
            "• Cost and resource management"));

        // Dungeon Star System
        setItem(45, styleSystem.createFeatureItem(Material.NETHER_STAR, "⭐ DUNGEON STARS", 
            "Enhance dungeon items with stars", true,
            "• 1-5 star upgrade system",
            "• Dungeon-specific bonuses",
            "• Essence collection system"));

        // Pet Item System
        setItem(46, styleSystem.createFeatureItem(Material.BONE, "🐾 PET ITEMS", 
            "Enhance and upgrade your pets", true,
            "• Pet candy for experience",
            "• Pet food for stat boosts",
            "• Pet upgrade and evolution system"));
    }

    private void setupUtilityAdmin() {
        // Category header
        setItem(39, styleSystem.createCategoryHeader("UTILITY & ADMIN", "Tools and administrative functions"));
        
        // Settings
        setItem(40, styleSystem.createFeatureItem(Material.REDSTONE, "⚙️ SETTINGS", 
            "Configure plugin settings and preferences", true,
            "• Feature toggles and options",
            "• Notification preferences",
            "• Personal customization settings"));

        // Admin Panel
        setItem(41, styleSystem.createFeatureItem(Material.COMMAND_BLOCK, "👑 ADMIN PANEL", 
            "Access administrative tools and controls", player.hasPermission("basics.admin"),
            "• Player management tools",
            "• Server administration",
            "• Plugin configuration"));
    }

    private void setupHypixelFeatures() {
        // Category header
        setItem(47, styleSystem.createCategoryHeader("HYPIXEL FEATURES", "Special Hypixel Skyblock-style features"));
        
        // Booster Cookie
        setItem(48, styleSystem.createFeatureItem(Material.COOKIE, "🍪 BOOSTER COOKIE", 
            "Temporary bonuses and enhancements", true,
            "• +25% Mining Speed boost",
            "• +20% Combat Damage increase",
            "• +50% Magic Find enhancement",
            "• 4-day duration with multiple bonuses"));

        // Recipe Book
        setItem(49, styleSystem.createFeatureItem(Material.BOOK, "📖 RECIPE BOOK", 
            "Discover and view item recipes", true,
            "• Weapon and armor recipes",
            "• Tool and accessory recipes",
            "• Pet and special item recipes",
            "• Progress tracking system"));
    }

    private void setupNavigationInfo() {
        // Event Schedule
        setItem(47, styleSystem.createFeatureItem(Material.CLOCK, "⏰ EVENT SCHEDULE", 
            "View upcoming events and schedules", true,
            "• Next events and timers",
            "• Event history and participation",
            "• Special event notifications"));

        // Server Info
        setItem(48, styleSystem.createFeatureItem(Material.BEACON, "🖥️ SERVER INFO", 
            "View server information and statistics", true,
            "• Online players: " + plugin.getServer().getOnlinePlayers().size(),
            "• Server TPS and performance",
            "• Uptime and status"));

        // Quick Actions
        setItem(49, styleSystem.createFeatureItem(Material.NETHER_STAR, "⚡ QUICK ACTIONS", 
            "Access quick actions and shortcuts", true,
            "• Teleport to spawn",
            "• Sort inventory",
            "• Quick commands"));

        // Navigation footer
        styleSystem.setupNavigationFooter(getInventory(), 50, false, false);
    }

    // Removed setupDecorativeBorders - now handled by HypixelMenuStyleSystem

    private void playOpenSound() {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
    }

    private String getServerUptime() {
        long uptime = System.currentTimeMillis() - (System.currentTimeMillis() - 1000 * 60 * 60); // Placeholder
        long hours = uptime / (1000 * 60 * 60);
        long minutes = (uptime % (1000 * 60 * 60)) / (1000 * 60);
        return hours + "h " + minutes + "m";
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
}
