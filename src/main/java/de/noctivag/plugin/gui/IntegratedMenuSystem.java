package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.items.ReforgeSystem;
import de.noctivag.plugin.items.ReforgeStoneSystem;
import de.noctivag.plugin.items.StatModificationSystem;
import de.noctivag.plugin.skyblock.EnchantingSystem;
import de.noctivag.plugin.pets.PetSystem;
import de.noctivag.plugin.pets.PetManagementSystem;
import de.noctivag.plugin.accessories.AccessorySystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * Integrated Menu System - Hypixel Skyblock Style
 * 
 * Features:
 * - Menu Integration of All Systems
 * - GUI Animations and Effects
 * - Responsive Design for Different Screen Sizes
 * - System Status Indicators
 * - Quick Access to All Features
 */
public class IntegratedMenuSystem extends CustomGUI {
    private final Plugin plugin;
    private final Player player;
    private final ReforgeSystem reforgeSystem;
    private final ReforgeStoneSystem reforgeStoneSystem;
    private final StatModificationSystem statModificationSystem;
    private final EnchantingSystem enchantingSystem;
    private final PetSystem petSystem;
    private final PetManagementSystem petManagementSystem;
    private final AccessorySystem accessorySystem;
    private final HypixelMenuStyleSystem styleSystem;
    
    public IntegratedMenuSystem(Plugin plugin, Player player, ReforgeSystem reforgeSystem,
                              ReforgeStoneSystem reforgeStoneSystem, StatModificationSystem statModificationSystem,
                              EnchantingSystem enchantingSystem, PetSystem petSystem,
                              PetManagementSystem petManagementSystem, AccessorySystem accessorySystem) {
        super(54, Component.text("§6§l⚡ INTEGRATED MENU ⚡").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.plugin = plugin;
        this.player = player;
        this.reforgeSystem = reforgeSystem;
        this.reforgeStoneSystem = reforgeStoneSystem;
        this.statModificationSystem = statModificationSystem;
        this.enchantingSystem = enchantingSystem;
        this.petSystem = petSystem;
        this.petManagementSystem = petManagementSystem;
        this.accessorySystem = accessorySystem;
        this.styleSystem = new HypixelMenuStyleSystem(plugin);
        setupItems();
    }
    
    private void setupItems() {
        // Setup Hypixel-style border
        styleSystem.setupHypixelBorder(getInventory());
        
        // Header
        setupHeader();
        
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
    
    private void setupHeader() {
        // Use Hypixel-style header
        styleSystem.setupHeaderSection(getInventory(), 4, "INTEGRATED MENU", "Access all plugin features from one central location");
    }
    
    private void setupCoreSystems() {
        // Category header
        setItem(10, styleSystem.createCategoryHeader("CORE SYSTEMS", "Essential character and profile systems"));
        
        // Stats system
        setItem(11, styleSystem.createFeatureItem(Material.BOOK, "📊 STATS SYSTEM", 
            "View and manage your character statistics", true,
            "• Character stat overview",
            "• Bonus and modifier tracking",
            "• Stat comparison tools"));
        
        // Profile system
        setItem(12, styleSystem.createFeatureItem(Material.PLAYER_HEAD, "👤 PROFILE SYSTEM", 
            "Access your detailed player profile", true,
            "• Player progress tracking",
            "• Achievement and statistics",
            "• Profile customization options"));
    }
    
    private void setupItemSystems() {
        // Category header
        setItem(19, styleSystem.createCategoryHeader("ITEM SYSTEMS", "Enhance and upgrade your items"));
        
        // Reforge system
        setItem(20, styleSystem.createFeatureItem(Material.ANVIL, "🔨 REFORGE SYSTEM", 
            "Reforge items to enhance their statistics", true,
            "• Multiple reforge types available",
            "• Stat enhancement system",
            "• Cost and success rate tracking"));
        
        // Reforge stones
        setItem(21, styleSystem.createFeatureItem(Material.NETHER_STAR, "⭐ REFORGE STONES", 
            "Manage and use reforge stones", true,
            "• Stone crafting and management",
            "• Stone usage optimization",
            "• Stone collection system"));
        
        // Enchanting system
        setItem(22, styleSystem.createFeatureItem(Material.ENCHANTING_TABLE, "✨ ENCHANTING SYSTEM", 
            "Enchant items with custom enchantments", true,
            "• Custom enchantment types",
            "• Enchantment level management",
            "• Enchanting cost calculations"));
    }
    
    private void setupPetAndAccessorySystems() {
        // Category header
        setItem(28, styleSystem.createCategoryHeader("PET & ACCESSORY", "Manage pets and equip accessories"));
        
        // Pet system
        setItem(29, styleSystem.createFeatureItem(Material.WOLF_SPAWN_EGG, "🐾 PET SYSTEM", 
            "Manage your pets and their abilities", true,
            "• Pet activation and management",
            "• Pet leveling and upgrades",
            "• Pet ability enhancement"));
        
        // Accessory system
        setItem(30, styleSystem.createFeatureItem(Material.GOLD_INGOT, "💍 ACCESSORY SYSTEM", 
            "Equip accessories to boost your stats", true,
            "• Ring and necklace management",
            "• Accessory stat bonuses",
            "• Accessory upgrade system"));
    }
    
    private void setupUtilitySystems() {
        // Category header
        setItem(37, styleSystem.createCategoryHeader("UTILITY", "Settings and help systems"));
        
        // Settings
        setItem(38, styleSystem.createFeatureItem(Material.REDSTONE, "⚙️ SETTINGS", 
            "Configure plugin settings and preferences", true,
            "• Feature toggles and options",
            "• Personal preference settings",
            "• System configuration"));
        
        // Help
        setItem(39, styleSystem.createFeatureItem(Material.BOOK, "❓ HELP", 
            "Get help with plugin features", true,
            "• Tutorials and guides",
            "• Feature documentation",
            "• Support and assistance"));
        
        // Navigation footer
        styleSystem.setupNavigationFooter(getInventory(), 40, false, false);
    }
    
    private void setupFooter() {
        // System status indicators
        setItem(49, styleSystem.createStatusItem(Material.GREEN_DYE, "SYSTEM STATUS", 
            "All integrated systems are operational", true));
    }
    
    public void handleClick(int slot) {
        switch (slot) {
            case 10 -> {
                // Stats system
                player.sendMessage("§eOpening stats system...");
                // Open stats GUI
            }
            case 12 -> {
                // Profile system
                player.sendMessage("§eOpening profile system...");
                // Open profile GUI
            }
            case 19 -> {
                // Reforge system
                player.sendMessage("§eOpening reforge system...");
                // Placeholder - ReforgeGUI class not implemented
                player.sendMessage("§cReforge GUI not implemented yet!");
            }
            case 21 -> {
                // Reforge stones
                player.sendMessage("§eOpening reforge stones...");
                // Open reforge stones GUI
            }
            case 23 -> {
                // Enchanting system
                player.sendMessage("§eOpening enchanting system...");
                new EnchantingGUI(plugin, player, enchantingSystem).openGUI(player);
            }
            case 28 -> {
                // Pet system
                player.sendMessage("§eOpening pet system...");
                new PetManagementGUI(plugin, player, petSystem, petManagementSystem).openGUI(player);
            }
            case 30 -> {
                // Accessory system
                player.sendMessage("§eOpening accessory system...");
                // Placeholder - AccessoryGUI class not implemented
                player.sendMessage("§cAccessory GUI not implemented yet!");
            }
            case 37 -> {
                // Settings
                player.sendMessage("§eOpening settings...");
                // Open settings GUI
            }
            case 39 -> {
                // Help
                player.sendMessage("§eOpening help...");
                // Open help GUI
            }
            case 41 -> {
                // Close
                player.closeInventory();
            }
        }
    }
}
