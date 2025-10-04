package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * Example Hypixel Skyblock Style Menu
 * 
 * This class demonstrates how to use the HypixelMenuStyleSystem
 * to create menus that look and feel like Hypixel Skyblock.
 */
public class ExampleHypixelMenu extends CustomGUI {
    private final SkyblockPlugin plugin;
    private final Player player;
    private final HypixelMenuStyleSystem styleSystem;

    public ExampleHypixelMenu(SkyblockPlugin plugin, Player player) {
        super(54, Component.text("§6§l⚡ EXAMPLE MENU ⚡").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.plugin = plugin;
        this.player = player;
        this.styleSystem = new HypixelMenuStyleSystem(plugin);
        setupItems();
    }

    private void setupItems() {
        // Setup Hypixel-style border
        styleSystem.setupHypixelBorder(getInventory());
        
        // Player header
        styleSystem.setupPlayerHeader(getInventory(), player, 4);
        
        // Category examples
        setupProfileCategory();
        setupCombatCategory();
        setupEconomyCategory();
        setupSocialCategory();
        
        // Navigation footer
        styleSystem.setupNavigationFooter(getInventory(), 50, false, false);
    }

    private void setupProfileCategory() {
        // Category header
        setItem(10, styleSystem.createCategoryHeader("PROFILE", "Player profile and statistics"));
        
        // Profile features
        setItem(11, styleSystem.createFeatureItem(Material.PLAYER_HEAD, "👤 PROFILE", 
            "View your detailed player profile", true,
            "• Statistics and achievements",
            "• Progress tracking",
            "• Profile customization"));

        setItem(12, styleSystem.createProgressItem(Material.EXPERIENCE_BOTTLE, "LEVEL", 
            player.getLevel(), 100, "levels"));

        setItem(13, styleSystem.createStatusItem(Material.EMERALD, "DAILY REWARD", 
            "Available for claim", true));
    }

    private void setupCombatCategory() {
        // Category header
        setItem(19, styleSystem.createCategoryHeader("COMBAT", "Fighting and combat systems"));
        
        // Combat features
        setItem(20, styleSystem.createFeatureItem(Material.DIAMOND_SWORD, "⚔️ PVP ARENA", 
            "Fight against other players", true,
            "• Multiple arena types",
            "• Ranking system",
            "• Rewards and prizes"));

        setItem(21, styleSystem.createFeatureItem(Material.DRAGON_HEAD, "🐉 BOSS FIGHTS", 
            "Challenge powerful bosses", true,
            "• 8 different boss types",
            "• Unique mechanics",
            "• Rare rewards"));

        setItem(22, styleSystem.createFeatureItem(Material.ZOMBIE_HEAD, "👹 MOB ARENA", 
            "Battle waves of monsters", true,
            "• Progressive difficulty",
            "• Wave-based combat",
            "• Survival rewards"));
    }

    private void setupEconomyCategory() {
        // Category header
        setItem(28, styleSystem.createCategoryHeader("ECONOMY", "Money and trading systems"));
        
        // Economy features
        setItem(29, styleSystem.createFeatureItem(Material.EMERALD, "💰 BANK", 
            "Manage your money securely", true,
            "• Multiple accounts",
            "• Interest rates",
            "• Investment options"));

        setItem(30, styleSystem.createFeatureItem(Material.CHEST, "🛒 SHOP", 
            "Buy items and services", true,
            "• Multiple categories",
            "• Special offers",
            "• Shopping cart"));

        setItem(31, styleSystem.createFeatureItem(Material.GOLD_INGOT, "🏪 AUCTION HOUSE", 
            "Trade items with other players", true,
            "• Buy and sell items",
            "• Auction system",
            "• Trading history"));
    }

    private void setupSocialCategory() {
        // Category header
        setItem(37, styleSystem.createCategoryHeader("SOCIAL", "Connect with other players"));
        
        // Social features
        setItem(38, styleSystem.createFeatureItem(Material.CAKE, "🎉 PARTY", 
            "Create or join parties", true,
            "• Party management",
            "• Shared activities",
            "• Party rewards"));

        setItem(39, styleSystem.createFeatureItem(Material.PLAYER_HEAD, "👥 FRIENDS", 
            "Manage your friends list", true,
            "• Add and remove friends",
            "• Friend status",
            "• Private messaging"));

        setItem(40, styleSystem.createFeatureItem(Material.SHIELD, "🏰 GUILD", 
            "Join or create guilds", true,
            "• Guild management",
            "• Guild wars",
            "• Guild benefits"));
    }

    /**
     * Handle menu clicks
     */
    public void handleClick(int slot) {
        switch (slot) {
            case 11 -> {
                player.sendMessage(styleSystem.GREEN + "Opening profile system...");
                // Open profile GUI
            }
            case 20 -> {
                player.sendMessage(styleSystem.RED + "Opening PvP arena...");
                // Open PvP arena GUI
            }
            case 21 -> {
                player.sendMessage(styleSystem.RED + "Opening boss fight system...");
                // Open boss fight GUI
            }
            case 22 -> {
                player.sendMessage(styleSystem.RED + "Opening mob arena...");
                // Open mob arena GUI
            }
            case 29 -> {
                player.sendMessage(styleSystem.YELLOW + "Opening bank system...");
                // Open bank GUI
            }
            case 30 -> {
                player.sendMessage(styleSystem.YELLOW + "Opening shop...");
                // Open shop GUI
            }
            case 31 -> {
                player.sendMessage(styleSystem.YELLOW + "Opening auction house...");
                // Open auction house GUI
            }
            case 38 -> {
                player.sendMessage(styleSystem.BLUE + "Opening party system...");
                // Open party GUI
            }
            case 39 -> {
                player.sendMessage(styleSystem.BLUE + "Opening friends system...");
                // Open friends GUI
            }
            case 40 -> {
                player.sendMessage(styleSystem.BLUE + "Opening guild system...");
                // Open guild GUI
            }
            case 50 -> {
                // Back button
                player.closeInventory();
            }
            case 51 -> {
                // Refresh button
                setupItems();
                player.sendMessage(styleSystem.GREEN + "Menu refreshed!");
            }
            case 52 -> {
                // Help button
                player.sendMessage(styleSystem.BLUE + "Help: Click on any item to access that feature!");
            }
            case 53 -> {
                // Close button
                player.closeInventory();
            }
        }
    }

    /**
     * Get the style system for external access
     */
    public HypixelMenuStyleSystem getStyleSystem() {
        return styleSystem;
    }
}
