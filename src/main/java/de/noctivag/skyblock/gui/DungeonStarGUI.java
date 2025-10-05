package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.items.DungeonStarSystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.List;

public class DungeonStarGUI extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final Player player;
    private final DungeonStarSystem dungeonStarSystem;

    public DungeonStarGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(54, Component.text("§6§l⭐ Dungeon Star System ⭐").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.SkyblockPlugin = SkyblockPlugin;
        this.player = player;
        // Placeholder - method not implemented
        this.dungeonStarSystem = null;
        setupItems();
    }

    private void setupItems() {
        // Header
        setupHeader();
        
        // Dungeon Stars Section
        setupDungeonStars();
        
        // Essence System Section
        setupEssenceSystem();
        
        // Dungeon Bonuses Section
        setupDungeonBonuses();
        
        // Statistics Section
        setupStatistics();
        
        // Navigation
        setupNavigation();
        
        // Decorative borders
        setupDecorativeBorders();
    }

    private void setupHeader() {
        ItemStack header = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = header.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6§l⭐ Dungeon Star System").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
            
            List<Component> lore = Arrays.asList(
                Component.text("§7Upgrade dungeon items with"),
                Component.text("§7Dungeon Stars for bonus stats!"),
                Component.text(""),
                Component.text("§7• 1-5 stars per item"),
                Component.text("§7• Only active in dungeons"),
                Component.text("§7• Requires essence to upgrade"),
                Component.text(""),
                Component.text("§eRight-click to apply!")
            );
            meta.lore(lore);
            header.setItemMeta(meta);
        }
        getInventory().setItem(4, header);
    }

    private void setupDungeonStars() {
        // 1 Star
        setItem(10, Material.NETHER_STAR, "§6§l⭐ 1 Star", 
            "§7Dungeon Star Level 1",
            "§7• Dungeon Bonus: §a+10% Stats",
            "§7• Required Essence: §a1x",
            "§7• Success Rate: §a100%",
            "",
            "§eClick to view details!");

        // 2 Stars
        setItem(11, Material.NETHER_STAR, "§6§l⭐⭐ 2 Stars", 
            "§7Dungeon Star Level 2",
            "§7• Dungeon Bonus: §a+20% Stats",
            "§7• Required Essence: §a2x",
            "§7• Success Rate: §a100%",
            "",
            "§eClick to view details!");

        // 3 Stars
        setItem(12, Material.NETHER_STAR, "§6§l⭐⭐⭐ 3 Stars", 
            "§7Dungeon Star Level 3",
            "§7• Dungeon Bonus: §a+30% Stats",
            "§7• Required Essence: §a4x",
            "§7• Success Rate: §a100%",
            "",
            "§eClick to view details!");

        // 4 Stars
        setItem(13, Material.NETHER_STAR, "§6§l⭐⭐⭐⭐ 4 Stars", 
            "§7Dungeon Star Level 4",
            "§7• Dungeon Bonus: §a+40% Stats",
            "§7• Required Essence: §a8x",
            "§7• Success Rate: §a100%",
            "",
            "§eClick to view details!");

        // 5 Stars
        setItem(14, Material.NETHER_STAR, "§6§l⭐⭐⭐⭐⭐ 5 Stars", 
            "§7Dungeon Star Level 5",
            "§7• Dungeon Bonus: §a+50% Stats",
            "§7• Required Essence: §a16x",
            "§7• Success Rate: §a100%",
            "",
            "§eClick to view details!");

        // Create Dungeon Star
        setItem(15, Material.NETHER_STAR, "§6§l➕ Create Dungeon Star", 
            "§7Create a new Dungeon Star",
            "§7• Cost: §a100 coins",
            "§7• Used for dungeon upgrades",
            "",
            "§eClick to create!");
    }

    private void setupEssenceSystem() {
        // Diamond Essence
        setItem(19, Material.DIAMOND, "§b§l💎 Diamond Essence", 
            "§7Diamond Essence for upgrades",
            "§7• Used for: §aArmor & Weapons",
            "§7• Cost: §a50 coins each",
            "§7• Success Rate: §a100%",
            "",
            "§eClick to view details!");

        // Gold Essence
        setItem(20, Material.GOLD_INGOT, "§6§l🥇 Gold Essence", 
            "§7Gold Essence for upgrades",
            "§7• Used for: §aSpecial Items",
            "§7• Cost: §a75 coins each",
            "§7• Success Rate: §a100%",
            "",
            "§eClick to view details!");

        // Iron Essence
        setItem(21, Material.IRON_INGOT, "§f§l⚙️ Iron Essence", 
            "§7Iron Essence for upgrades",
            "§7• Used for: §aBasic Items",
            "§7• Cost: §a25 coins each",
            "§7• Success Rate: §a100%",
            "",
            "§eClick to view details!");

        // Emerald Essence
        setItem(22, Material.EMERALD, "§a§l💚 Emerald Essence", 
            "§7Emerald Essence for upgrades",
            "§7• Used for: §aRare Items",
            "§7• Cost: §a100 coins each",
            "§7• Success Rate: §a100%",
            "",
            "§eClick to view details!");

        // Lapis Essence
        setItem(23, Material.LAPIS_LAZULI, "§9§l🔵 Lapis Essence", 
            "§7Lapis Essence for upgrades",
            "§7• Used for: §aMagic Items",
            "§7• Cost: §a60 coins each",
            "§7• Success Rate: §a100%",
            "",
            "§eClick to view details!");

        // Redstone Essence
        setItem(24, Material.REDSTONE, "§c§l🔴 Redstone Essence", 
            "§7Redstone Essence for upgrades",
            "§7• Used for: §aTechnical Items",
            "§7• Cost: §a40 coins each",
            "§7• Success Rate: §a100%",
            "",
            "§eClick to view details!");

        // Quartz Essence
        setItem(25, Material.QUARTZ, "§f§l⚪ Quartz Essence", 
            "§7Quartz Essence for upgrades",
            "§7• Used for: §aNether Items",
            "§7• Cost: §a80 coins each",
            "§7• Success Rate: §a100%",
            "",
            "§eClick to view details!");

        // Obsidian Essence
        setItem(26, Material.OBSIDIAN, "§5§l🖤 Obsidian Essence", 
            "§7Obsidian Essence for upgrades",
            "§7• Used for: §aEnd Items",
            "§7• Cost: §a150 coins each",
            "§7• Success Rate: §a100%",
            "",
            "§eClick to view details!");
    }

    private void setupDungeonBonuses() {
        // Dungeon Bonus Info
        setItem(28, Material.PAPER, "§e§l📊 Dungeon Bonuses", 
            "§7Dungeon Star bonuses:",
            "§7• 1 Star: §a+10% Stats in Dungeons",
            "§7• 2 Stars: §a+20% Stats in Dungeons",
            "§7• 3 Stars: §a+30% Stats in Dungeons",
            "§7• 4 Stars: §a+40% Stats in Dungeons",
            "§7• 5 Stars: §a+50% Stats in Dungeons",
            "",
            "§eClick to view detailed bonuses!");

        // Dungeon Types
        setItem(29, Material.STONE_BRICKS, "§7§l🏰 Dungeon Types", 
            "§7Supported dungeon types:",
            "§7• Catacombs (F1-F7)",
            "§7• Master Mode",
            "§7• Kuudra",
            "§7• All dungeon variants",
            "",
            "§eClick to view dungeon info!");

        // Upgrade Guide
        setItem(30, Material.BOOK, "§b§l📖 Upgrade Guide", 
            "§7How to upgrade dungeon items:",
            "§7• 1. Get dungeon item",
            "§7• 2. Collect required essence",
            "§7• 3. Right-click with essence",
            "§7• 4. Item gets dungeon star",
            "",
            "§eClick to view full guide!");
    }

    private void setupStatistics() {
        // Player Statistics
        DungeonStarSystem.PlayerDungeonStars playerStars = dungeonStarSystem.getPlayerDungeonStars(player.getUniqueId());
        setItem(37, Material.GOLD_INGOT, "§6§l📈 Your Statistics", 
            "§7Your Dungeon Star usage:",
            "§7• Total stars applied: §a" + playerStars.getTotalStars(),
            "§7• 1 Star items: §a" + playerStars.getStarCount(1),
            "§7• 2 Star items: §a" + playerStars.getStarCount(2),
            "§7• 3 Star items: §a" + playerStars.getStarCount(3),
            "§7• 4 Star items: §a" + playerStars.getStarCount(4),
            "§7• 5 Star items: §a" + playerStars.getStarCount(5),
            "§7• Total essence used: §a" + playerStars.getTotalEssenceUsed(),
            "",
            "§eClick to view detailed stats!");

        // Global Statistics
        setItem(38, Material.DIAMOND, "§b§l🌍 Global Statistics", 
            "§7Global Dungeon Star usage:",
            "§7• Total stars applied: §a123,456",
            "§7• 5 Star items: §a12,345",
            "§7• Most used essence: §aDiamond",
            "§7• Average stars per item: §a3.2",
            "",
            "§eClick to view global stats!");

        // Leaderboard
        setItem(39, Material.GOLD_BLOCK, "§6§l🏆 Dungeon Star Leaderboard", 
            "§7Top Dungeon Star users:",
            "§7• #1 PlayerName: §a1,234 stars",
            "§7• #2 PlayerName: §a1,123 stars",
            "§7• #3 PlayerName: §a1,012 stars",
            "§7• #4 PlayerName: §a901 stars",
            "§7• #5 PlayerName: §a890 stars",
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
                meta.lore(java.util.Arrays.stream(lore).map(Component::text).collect(java.util.stream.Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        
        getInventory().setItem(slot, item);
    }
}
