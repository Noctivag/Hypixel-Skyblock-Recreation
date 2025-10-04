package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.items.RecombobulatorSystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.List;

public class RecombobulatorGUI extends CustomGUI {
    private final SkyblockPlugin plugin;
    private final Player player;
    private final RecombobulatorSystem recombobulatorSystem;

    public RecombobulatorGUI(SkyblockPlugin plugin, Player player) {
        super(54, Component.text("§d§l🔮 Recombobulator 3000 🔮").color(NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.BOLD));
        this.plugin = plugin;
        this.player = player;
        this.recombobulatorSystem = null; // RecombobulatorSystem not implemented yet
        setupItems();
    }

    private void setupItems() {
        // Header
        setupHeader();
        
        // Recombobulator Info Section
        setupRecombobulatorInfo();
        
        // Rarity System Section
        setupRaritySystem();
        
        // Success Rates Section
        setupSuccessRates();
        
        // Statistics Section
        setupStatistics();
        
        // Navigation
        setupNavigation();
        
        // Decorative borders
        setupDecorativeBorders();
    }

    private void setupHeader() {
        ItemStack header = new ItemStack(Material.REDSTONE);
        ItemMeta meta = header.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§d§l🔮 Recombobulator 3000").color(NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.BOLD));
            
            List<Component> lore = Arrays.asList(
                Component.text("§7Upgrade item rarity with"),
                Component.text("§7the Recombobulator 3000!"),
                Component.text(""),
                Component.text("§7• Upgrade rarity levels"),
                Component.text("§7• Success rates vary by rarity"),
                Component.text("§7• Costs increase with rarity"),
                Component.text(""),
                Component.text("§eRight-click to use!")
            );
            meta.lore(lore);
            header.setItemMeta(meta);
        }
        getInventory().setItem(4, header);
    }

    private void setupRecombobulatorInfo() {
        // Recombobulator 3000 Info
        setItem(10, Material.REDSTONE, "§d§l🔮 Recombobulator 3000", 
            "§7A mysterious device that can",
            "§7upgrade the rarity of items.",
            "",
            "§7• Right-click to use",
            "§7• Success rates vary",
            "§7• Costs increase with rarity",
            "",
            "§eClick to learn more!");

        // Create Recombobulator
        setItem(11, Material.REDSTONE, "§d§l➕ Create Recombobulator", 
            "§7Create a new Recombobulator 3000",
            "§7• Cost: §a1,000 coins",
            "§7• Used for rarity upgrades",
            "",
            "§eClick to create!");

        // Usage Guide
        setItem(12, Material.BOOK, "§b§l📖 Usage Guide", 
            "§7How to use Recombobulator:",
            "§7• 1. Hold item in main hand",
            "§7• 2. Right-click with Recombobulator",
            "§7• 3. Success rate depends on rarity",
            "§7• 4. Item rarity increases on success",
            "",
            "§eClick to view full guide!");
    }

    private void setupRaritySystem() {
        // Common to Uncommon
        setItem(19, Material.WHITE_DYE, "§f§lCommon → §aUncommon", 
            "§7Upgrade from Common to Uncommon",
            "§7• Success rate: §a90%",
            "§7• Cost: §a100 coins",
            "§7• Effect: §a+10% stats",
            "",
            "§eClick to view details!");

        // Uncommon to Rare
        setItem(20, Material.GREEN_DYE, "§a§lUncommon → §9Rare", 
            "§7Upgrade from Uncommon to Rare",
            "§7• Success rate: §a80%",
            "§7• Cost: §a250 coins",
            "§7• Effect: §a+15% stats",
            "",
            "§eClick to view details!");

        // Rare to Epic
        setItem(21, Material.BLUE_DYE, "§9§lRare → §5Epic", 
            "§7Upgrade from Rare to Epic",
            "§7• Success rate: §a70%",
            "§7• Cost: §a500 coins",
            "§7• Effect: §a+20% stats",
            "",
            "§eClick to view details!");

        // Epic to Legendary
        setItem(22, Material.PURPLE_DYE, "§5§lEpic → §6Legendary", 
            "§7Upgrade from Epic to Legendary",
            "§7• Success rate: §a60%",
            "§7• Cost: §a1,000 coins",
            "§7• Effect: §a+25% stats",
            "",
            "§eClick to view details!");

        // Legendary to Mythic
        setItem(23, Material.ORANGE_DYE, "§6§lLegendary → §dMythic", 
            "§7Upgrade from Legendary to Mythic",
            "§7• Success rate: §a50%",
            "§7• Cost: §a2,000 coins",
            "§7• Effect: §a+30% stats",
            "",
            "§eClick to view details!");

        // Mythic to Divine
        setItem(24, Material.CYAN_DYE, "§d§lMythic → §bDivine", 
            "§7Upgrade from Mythic to Divine",
            "§7• Success rate: §a40%",
            "§7• Cost: §a5,000 coins",
            "§7• Effect: §a+35% stats",
            "",
            "§eClick to view details!");
    }

    private void setupSuccessRates() {
        // Success Rate Chart
        setItem(28, Material.PAPER, "§e§l📊 Success Rate Chart", 
            "§7Recombobulator success rates:",
            "§7• Common → Uncommon: §a90%",
            "§7• Uncommon → Rare: §a80%",
            "§7• Rare → Epic: §a70%",
            "§7• Epic → Legendary: §a60%",
            "§7• Legendary → Mythic: §a50%",
            "§7• Mythic → Divine: §a40%",
            "",
            "§eClick to view detailed chart!");

        // Cost Calculator
        setItem(29, Material.PAPER, "§e§l🧮 Cost Calculator", 
            "§7Calculate total upgrade costs:",
            "§7• Common → Uncommon: §a100 coins",
            "§7• Uncommon → Rare: §a250 coins",
            "§7• Rare → Epic: §a500 coins",
            "§7• Epic → Legendary: §a1,000 coins",
            "§7• Legendary → Mythic: §a2,000 coins",
            "§7• Mythic → Divine: §a5,000 coins",
            "§7• Total: §a8,850 coins",
            "",
            "§eClick to calculate!");

        // Tips and Tricks
        setItem(30, Material.BOOK, "§b§l💡 Tips & Tricks", 
            "§7Tips for successful recombobulation:",
            "§7• Higher rarity = lower success rate",
            "§7• Costs increase exponentially",
            "§7• Divine is the highest rarity",
            "§7• Failed attempts consume Recombobulator",
            "",
            "§eClick to view more tips!");
    }

    private void setupStatistics() {
        // Player Statistics
        RecombobulatorSystem.PlayerRecombobulator playerRecombobulator = recombobulatorSystem.getPlayerRecombobulator(player.getUniqueId());
        setItem(37, Material.GOLD_INGOT, "§6§l📈 Your Statistics", 
            "§7Your Recombobulator usage:",
            "§7• Total attempts: §a" + playerRecombobulator.getTotalRecombobulations(),
            "§7• Successful: §a" + playerRecombobulator.getSuccessfulRecombobulations(),
            "§7• Failed: §c" + playerRecombobulator.getFailedRecombobulations(),
            "§7• Success rate: §a" + String.format("%.1f", playerRecombobulator.getSuccessRate()) + "%",
            "",
            "§eClick to view detailed stats!");

        // Global Statistics
        setItem(38, Material.DIAMOND, "§b§l🌍 Global Statistics", 
            "§7Global Recombobulator usage:",
            "§7• Total attempts: §a1,234,567",
            "§7• Successful: §a987,654",
            "§7• Failed: §c246,913",
            "§7• Global success rate: §a80.0%",
            "",
            "§eClick to view global stats!");

        // Leaderboard
        setItem(39, Material.GOLD_BLOCK, "§6§l🏆 Recombobulator Leaderboard", 
            "§7Top Recombobulator users:",
            "§7• #1 PlayerName: §a1,234 successful",
            "§7• #2 PlayerName: §a1,123 successful",
            "§7• #3 PlayerName: §a1,012 successful",
            "§7• #4 PlayerName: §a901 successful",
            "§7• #5 PlayerName: §a890 successful",
            "",
            "§eClick to view full leaderboard!");
    }

    private void setupNavigation() {
        // Back to Main Menu
        setItem(45, Material.ARROW, "§7§l⬅️ Back to Main Menu", 
            "§7Return to the main menu",
            "",
            "§eClick to go back!");

        // Close
        setItem(49, Material.BARRIER, "§c§l❌ Close", 
            "§7Close this menu",
            "",
            "§eClick to close!");

        // Refresh
        setItem(51, Material.EMERALD, "§a§l🔄 Refresh", 
            "§7Refresh this menu",
            "",
            "§eClick to refresh!");
    }

    private void setupDecorativeBorders() {
        // Top border
        for (int i = 0; i < 9; i++) {
            setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        }
        
        // Side borders
        for (int i = 9; i < 54; i += 9) {
            setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        }
        for (int i = 17; i < 54; i += 9) {
            setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        }
        
        // Bottom border
        for (int i = 45; i < 54; i++) {
            if (i != 49) { // Don't override close button
                setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
            }
        }
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
