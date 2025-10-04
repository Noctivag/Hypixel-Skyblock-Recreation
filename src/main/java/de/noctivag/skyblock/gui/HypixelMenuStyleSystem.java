package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Hypixel Skyblock Style Menu System
 * 
 * Features:
 * - Consistent Hypixel Skyblock design patterns
 * - Color scheme and typography standards
 * - Border and layout management
 * - Player head integration
 * - Status indicators and progress bars
 * - Sound effects and animations
 */
public class HypixelMenuStyleSystem {
    
    // Hypixel Skyblock Color Constants
    public static final String GOLD = "§6";
    public static final String YELLOW = "§e";
    public static final String GREEN = "§a";
    public static final String RED = "§c";
    public static final String BLUE = "§b";
    public static final String PURPLE = "§d";
    public static final String AQUA = "§b";
    public static final String GRAY = "§7";
    public static final String DARK_GRAY = "§8";
    public static final String WHITE = "§f";
    public static final String DARK_RED = "§4";
    public static final String DARK_GREEN = "§2";
    public static final String DARK_BLUE = "§1";
    public static final String DARK_PURPLE = "§5";
    public static final String LIGHT_PURPLE = "§d";
    
    // Formatting Constants
    public static final String BOLD = "§l";
    public static final String UNDERLINE = "§n";
    public static final String ITALIC = "§o";
    public static final String STRIKETHROUGH = "§m";
    
    // Border Materials
    public static final Material BORDER_MATERIAL = Material.GRAY_STAINED_GLASS_PANE;
    public static final Material CORNER_MATERIAL = Material.BLACK_STAINED_GLASS_PANE;
    public static final Material SEPARATOR_MATERIAL = Material.LIGHT_GRAY_STAINED_GLASS_PANE;
    
    // Common Icons
    public static final String PLAYER_ICON = "👤";
    public static final String MONEY_ICON = "💰";
    public static final String LEVEL_ICON = "⭐";
    public static final String STATS_ICON = "📊";
    public static final String SETTINGS_ICON = "⚙️";
    public static final String CLOSE_ICON = "❌";
    public static final String BACK_ICON = "⬅️";
    public static final String NEXT_ICON = "➡️";
    public static final String REFRESH_ICON = "🔄";
    public static final String HELP_ICON = "❓";
    public static final String SUCCESS_ICON = "✓";
    public static final String ERROR_ICON = "✗";
    public static final String WARNING_ICON = "⚠️";
    public static final String INFO_ICON = "ℹ️";
    
    private final SkyblockPlugin plugin;
    
    public HypixelMenuStyleSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Create a Hypixel-style inventory with proper title formatting
     */
    public Inventory createHypixelInventory(String title, int size) {
        String formattedTitle = formatTitle(title);
        return Bukkit.createInventory(null, size, Component.text(formattedTitle));
    }
    
    /**
     * Format title with Hypixel styling
     */
    public String formatTitle(String title) {
        return GOLD + BOLD + "⚡ " + title + " ⚡";
    }
    
    /**
     * Create a standard Hypixel border for any inventory
     */
    public void setupHypixelBorder(Inventory inventory) {
        int size = inventory.getSize();
        int rows = size / 9;
        
        // Top and bottom borders
        for (int i = 0; i < 9; i++) {
            setBorderItem(inventory, i, BORDER_MATERIAL);
            setBorderItem(inventory, size - 9 + i, BORDER_MATERIAL);
        }
        
        // Left and right borders
        for (int row = 1; row < rows - 1; row++) {
            setBorderItem(inventory, row * 9, BORDER_MATERIAL);
            setBorderItem(inventory, row * 9 + 8, BORDER_MATERIAL);
        }
        
        // Corner decorations
        setBorderItem(inventory, 0, CORNER_MATERIAL);
        setBorderItem(inventory, 8, CORNER_MATERIAL);
        setBorderItem(inventory, size - 9, CORNER_MATERIAL);
        setBorderItem(inventory, size - 1, CORNER_MATERIAL);
    }
    
    /**
     * Create a decorative header section
     */
    public void setupHeaderSection(Inventory inventory, int startSlot, String title, String subtitle) {
        ItemStack headerItem = createHypixelItem(Material.NETHER_STAR, 
            GOLD + BOLD + title, 
            GRAY + subtitle,
            "",
            YELLOW + "Click for more information");
        inventory.setItem(startSlot, headerItem);
    }
    
    /**
     * Create a player info header with stats
     */
    public void setupPlayerHeader(Inventory inventory, Player player, int slot) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
        
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(player);
            skullMeta.displayName(Component.text(GREEN + BOLD + PLAYER_ICON + " " + player.getName()));
            
            List<Component> lore = Arrays.asList(
                Component.text(GRAY + "Level: " + YELLOW + player.getLevel()),
                Component.text(GRAY + "Money: " + GREEN + formatMoney(getPlayerMoney(player))),
                Component.text(GRAY + "Achievements: " + GOLD + getCompletedAchievements(player) + "/" + getTotalAchievements()),
                Component.text(GRAY + "Streak: " + YELLOW + getDailyStreak(player) + " days"),
                Component.text(""),
                Component.text(YELLOW + "Click for detailed profile")
            );
            skullMeta.lore(lore);
            playerHead.setItemMeta(skullMeta);
        }
        
        inventory.setItem(slot, playerHead);
    }
    
    /**
     * Create a standard Hypixel menu item
     */
    public ItemStack createHypixelItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore != null && lore.length > 0) {
                List<Component> loreComponents = Arrays.stream(lore)
                    .map(Component::text)
                    .collect(Collectors.toList());
                meta.lore(loreComponents);
            }
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Create a status indicator item
     */
    public ItemStack createStatusItem(Material material, String status, String description, boolean isActive) {
        String color = isActive ? GREEN : RED;
        String icon = isActive ? SUCCESS_ICON : ERROR_ICON;
        
        return createHypixelItem(material,
            color + BOLD + icon + " " + status,
            GRAY + description,
            "",
            isActive ? GREEN + "Status: Active" : RED + "Status: Inactive"
        );
    }
    
    /**
     * Create a progress bar item
     */
    public ItemStack createProgressItem(Material material, String name, int current, int max, String unit) {
        double percentage = (double) current / max * 100;
        String progressBar = createProgressBar(percentage);
        
        return createHypixelItem(material,
            YELLOW + BOLD + name,
            GRAY + "Progress: " + progressBar,
            GRAY + "Current: " + GREEN + current + GRAY + "/" + WHITE + max + " " + unit,
            GRAY + "Percentage: " + YELLOW + String.format("%.1f", percentage) + "%"
        );
    }
    
    /**
     * Create a navigation item (back, next, close, etc.)
     */
    public ItemStack createNavigationItem(NavigationType type) {
        switch (type) {
            case CLOSE:
                return createHypixelItem(Material.BARRIER, 
                    RED + BOLD + CLOSE_ICON + " CLOSE",
                    GRAY + "Close this menu",
                    "",
                    YELLOW + "Click to close");
            case BACK:
                return createHypixelItem(Material.ARROW, 
                    GRAY + BOLD + BACK_ICON + " BACK",
                    GRAY + "Return to previous menu",
                    "",
                    YELLOW + "Click to go back");
            case NEXT:
                return createHypixelItem(Material.ARROW, 
                    GRAY + BOLD + NEXT_ICON + " NEXT",
                    GRAY + "Go to next page",
                    "",
                    YELLOW + "Click to continue");
            case REFRESH:
                return createHypixelItem(Material.EMERALD, 
                    GREEN + BOLD + REFRESH_ICON + " REFRESH",
                    GRAY + "Refresh this menu",
                    "",
                    YELLOW + "Click to refresh");
            case HELP:
                return createHypixelItem(Material.BOOK, 
                    BLUE + BOLD + HELP_ICON + " HELP",
                    GRAY + "Get help and information",
                    "",
                    YELLOW + "Click for help");
            default:
                return createHypixelItem(Material.STONE, "§7Unknown");
        }
    }
    
    /**
     * Create a category header item
     */
    public ItemStack createCategoryHeader(String categoryName, String description) {
        return createHypixelItem(Material.GOLD_INGOT,
            GOLD + BOLD + "━━━━━ " + categoryName + " ━━━━━",
            GRAY + description,
            "",
            YELLOW + "Category Information"
        );
    }
    
    /**
     * Create a feature item with status
     */
    public ItemStack createFeatureItem(Material material, String name, String description, 
                                     boolean isEnabled, String... additionalLore) {
        String statusColor = isEnabled ? GREEN : RED;
        String statusText = isEnabled ? "Enabled" : "Disabled";
        
        List<String> lore = new ArrayList<>();
        lore.add(GRAY + description);
        lore.add("");
        lore.add(GRAY + "Status: " + statusColor + statusText);
        if (additionalLore != null) {
            lore.addAll(Arrays.asList(additionalLore));
        }
        lore.add("");
        lore.add(YELLOW + "Click to " + (isEnabled ? "configure" : "enable"));
        
        return createHypixelItem(material, 
            (isEnabled ? GREEN : GRAY) + BOLD + name, 
            lore.toArray(new String[0]));
    }
    
    /**
     * Setup standard navigation footer
     */
    public void setupNavigationFooter(Inventory inventory, int startSlot, boolean showBack, boolean showNext) {
        int slot = startSlot;
        
        if (showBack) {
            inventory.setItem(slot++, createNavigationItem(NavigationType.BACK));
        }
        
        inventory.setItem(slot++, createNavigationItem(NavigationType.REFRESH));
        inventory.setItem(slot++, createNavigationItem(NavigationType.HELP));
        inventory.setItem(slot, createNavigationItem(NavigationType.CLOSE));
    }
    
    /**
     * Create a progress bar string
     */
    private String createProgressBar(double percentage) {
        int filled = (int) (percentage / 10);
        int empty = 10 - filled;
        
        StringBuilder bar = new StringBuilder();
        bar.append(GREEN);
        for (int i = 0; i < filled; i++) {
            bar.append("█");
        }
        bar.append(GRAY);
        for (int i = 0; i < empty; i++) {
            bar.append("█");
        }
        
        return bar.toString();
    }
    
    /**
     * Set border item with proper formatting
     */
    private void setBorderItem(Inventory inventory, int slot, Material material) {
        ItemStack borderItem = new ItemStack(material);
        ItemMeta meta = borderItem.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(" "));
            borderItem.setItemMeta(meta);
        }
        inventory.setItem(slot, borderItem);
    }
    
    /**
     * Format money with Hypixel style
     */
    private String formatMoney(double amount) {
        if (amount >= 1_000_000_000) {
            return String.format("%.1fB", amount / 1_000_000_000);
        } else if (amount >= 1_000_000) {
            return String.format("%.1fM", amount / 1_000_000);
        } else if (amount >= 1_000) {
            return String.format("%.1fK", amount / 1_000);
        } else {
            return String.format("%.0f", amount);
        }
    }
    
    /**
     * Get player money (placeholder implementation)
     */
    private double getPlayerMoney(Player player) {
        if (plugin.getEconomyManager() != null) {
            return plugin.getEconomyManager().getBalance(player);
        }
        return 0;
    }
    
    /**
     * Get completed achievements (placeholder implementation)
     */
    private int getCompletedAchievements(Player player) {
        // AchievementSystem not implemented yet
        return 0;
    }
    
    /**
     * Get total achievements (placeholder implementation)
     */
    private int getTotalAchievements() {
        // AchievementSystem not implemented yet
        return 100;
    }
    
    /**
     * Get daily streak (placeholder implementation)
     */
    private int getDailyStreak(Player player) {
        // DailyRewardSystem not implemented yet
        return 0;
    }
    
    /**
     * Navigation item types
     */
    public enum NavigationType {
        CLOSE, BACK, NEXT, REFRESH, HELP
    }
    
    /**
     * Menu categories for organization
     */
    public enum MenuCategory {
        PROFILE("Profile", GREEN),
        COMBAT("Combat", RED),
        ECONOMY("Economy", YELLOW),
        SOCIAL("Social", BLUE),
        UTILITY("Utility", PURPLE),
        ADMIN("Admin", DARK_RED);
        
        private final String displayName;
        private final String color;
        
        MenuCategory(String displayName, String color) {
            this.displayName = displayName;
            this.color = color;
        }
        
        public String getDisplayName() { return displayName; }
        public String getColor() { return color; }
    }
}
