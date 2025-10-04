package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.items.PotatoBookSystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.List;

public class PotatoBookGUI extends CustomGUI {
    private final SkyblockPlugin plugin;
    private final Player player;
    private final PotatoBookSystem potatoBookSystem;

    public PotatoBookGUI(SkyblockPlugin plugin, Player player) {
        super(54, Component.text("§6§l🥔 Potato Book System 🥔").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.plugin = plugin;
        this.player = player;
        this.potatoBookSystem = null; // PotatoBookSystem not implemented yet
        setupItems();
    }

    private void setupItems() {
        // Header
        setupHeader();
        
        // Hot Potato Books Section
        setupHotPotatoBooks();
        
        // Fuming Potato Books Section
        setupFumingPotatoBooks();
        
        // Statistics Section
        setupStatistics();
        
        // Navigation
        setupNavigation();
        
        // Decorative borders
        setupDecorativeBorders();
    }

    private void setupHeader() {
        ItemStack header = new ItemStack(Material.BOOK);
        ItemMeta meta = header.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6§l🥔 Potato Book System").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
            
            List<Component> lore = Arrays.asList(
                Component.text("§7Upgrade your armor and weapons"),
                Component.text("§7with Potato Books!"),
                Component.text(""),
                Component.text("§7• Hot Potato Books: §a+10% Stats"),
                Component.text("§7• Fuming Potato Books: §d+15% Stats"),
                Component.text(""),
                Component.text("§eClick on items to upgrade them!")
            );
            meta.lore(lore);
            header.setItemMeta(meta);
        }
        getInventory().setItem(4, header);
    }

    private void setupHotPotatoBooks() {
        // Hot Potato Book Info
        setItem(10, Material.BOOK, "§6§l📖 Hot Potato Book", 
            "§7Increases item stats by 10%",
            "§7• Can be applied up to 10 times",
            "§7• Success rate: 100%",
            "§7• Cost: Free",
            "",
            "§eRight-click to apply!");

        // Create Hot Potato Book
        setItem(11, Material.BOOK, "§6§l➕ Create Hot Potato Book", 
            "§7Create a new Hot Potato Book",
            "§7• Cost: §a100 coins",
            "§7• Used for armor/weapon upgrades",
            "",
            "§eClick to create!");

        // Hot Potato Book Statistics
        PotatoBookSystem.PlayerPotatoBooks playerBooks = potatoBookSystem.getPlayerPotatoBooks(player.getUniqueId());
        setItem(12, Material.PAPER, "§6§l📊 Hot Potato Statistics", 
            "§7Your Hot Potato Book usage:",
            "§7• Total applied: §a" + playerBooks.getTotalHotPotatoBooks(),
            "§7• Success rate: §a" + String.format("%.1f", playerBooks.getSuccessRate()) + "%",
            "§7• Failed applications: §c" + playerBooks.getFailedApplications(),
            "",
            "§eClick to view details!");
    }

    private void setupFumingPotatoBooks() {
        // Fuming Potato Book Info
        setItem(19, Material.ENCHANTED_BOOK, "§d§l📚 Fuming Potato Book", 
            "§7Increases item stats by 15%",
            "§7• Requires 10 Hot Potato Books first",
            "§7• Can be applied up to 10 times",
            "§7• Success rate: 100%",
            "",
            "§eRight-click to apply!");

        // Create Fuming Potato Book
        setItem(20, Material.ENCHANTED_BOOK, "§d§l➕ Create Fuming Potato Book", 
            "§7Create a new Fuming Potato Book",
            "§7• Cost: §a500 coins",
            "§7• Used for advanced upgrades",
            "",
            "§eClick to create!");

        // Fuming Potato Book Statistics
        PotatoBookSystem.PlayerPotatoBooks playerBooks = potatoBookSystem.getPlayerPotatoBooks(player.getUniqueId());
        setItem(21, Material.PAPER, "§d§l📊 Fuming Potato Statistics", 
            "§7Your Fuming Potato Book usage:",
            "§7• Total applied: §d" + playerBooks.getTotalFumingPotatoBooks(),
            "§7• Success rate: §a" + String.format("%.1f", playerBooks.getSuccessRate()) + "%",
            "§7• Failed applications: §c" + playerBooks.getFailedApplications(),
            "",
            "§eClick to view details!");
    }

    private void setupStatistics() {
        // Overall Statistics
        PotatoBookSystem.PlayerPotatoBooks playerBooks = potatoBookSystem.getPlayerPotatoBooks(player.getUniqueId());
        setItem(28, Material.GOLD_INGOT, "§6§l📈 Overall Statistics", 
            "§7Your Potato Book usage:",
            "§7• Total Hot Potato Books: §a" + playerBooks.getTotalHotPotatoBooks(),
            "§7• Total Fuming Potato Books: §d" + playerBooks.getTotalFumingPotatoBooks(),
            "§7• Total successful: §a" + playerBooks.getSuccessfulApplications(),
            "§7• Total failed: §c" + playerBooks.getFailedApplications(),
            "§7• Overall success rate: §a" + String.format("%.1f", playerBooks.getSuccessRate()) + "%",
            "",
            "§eClick to view detailed stats!");

        // Upgrade Guide
        setItem(29, Material.BOOK, "§b§l📖 Upgrade Guide", 
            "§7How to upgrade items:",
            "§7• 1. Apply Hot Potato Books (up to 10)",
            "§7• 2. Apply Fuming Potato Books (up to 10)",
            "§7• 3. Use Recombobulator for rarity",
            "§7• 4. Add Dungeon Stars for dungeons",
            "",
            "§eClick to view full guide!");

        // Cost Calculator
        setItem(30, Material.PAPER, "§e§l🧮 Cost Calculator", 
            "§7Calculate upgrade costs:",
            "§7• Hot Potato Books: §a100 coins each",
            "§7• Fuming Potato Books: §a500 coins each",
            "§7• Total for full upgrade: §a6,000 coins",
            "",
            "§eClick to calculate!");
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
