package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.items.PetItemSystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.List;

public class PetItemGUI extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final Player player;
    private final PetItemSystem petItemSystem;

    public PetItemGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(54, Component.text("§d§l🐾 Pet Item System 🐾").color(NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.BOLD));
        this.SkyblockPlugin = SkyblockPlugin;
        this.player = player;
        this.petItemSystem = (PetItemSystem) SkyblockPlugin.getPetItemSystem(); // Cast from Object
        setupItems();
    }

    private void setupItems() {
        // Header
        setupHeader();
        
        // Pet Items Section
        setupPetItems();
        
        // Pet Categories Section
        setupPetCategories();
        
        // Pet Effects Section
        setupPetEffects();
        
        // Statistics Section
        setupStatistics();
        
        // Navigation
        setupNavigation();
        
        // Decorative borders
        setupDecorativeBorders();
    }

    private void setupHeader() {
        ItemStack header = new ItemStack(Material.BONE);
        ItemMeta meta = header.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§d§l🐾 Pet Item System").color(NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.BOLD));
            
            List<Component> lore = Arrays.asList(
                Component.text("§7Enhance your pets with"),
                Component.text("§7special Pet Items!"),
                Component.text(""),
                Component.text("§7• Pet Candy: +25% XP gain"),
                Component.text("§7• Pet Food: +10% stats"),
                Component.text("§7• Pet Upgrades: +1 rarity"),
                Component.text("§7• Pet Skins: Custom appearance"),
                Component.text("§7• Pet Accessories: Special abilities"),
                Component.text(""),
                Component.text("§eRight-click to apply!")
            );
            meta.lore(lore);
            header.setItemMeta(meta);
        }
        getInventory().setItem(4, header);
    }

    private void setupPetItems() {
        // Pet Candy
        setItem(10, Material.SUGAR, "§d§l🍭 Pet Candy", 
            "§7Increases pet experience gain by 25%",
            "§7• Category: §aExperience",
            "§7• Effect: §a+25% Pet XP Gain",
            "§7• Value: §a100 coins",
            "§7• Success Rate: §a100%",
            "",
            "§eRight-click to apply!");

        // Pet Food
        setItem(11, Material.BREAD, "§6§l🍞 Pet Food", 
            "§7Increases pet stats by 10%",
            "§7• Category: §cStats",
            "§7• Effect: §a+10% Pet Stats",
            "§7• Value: §a200 coins",
            "§7• Success Rate: §a100%",
            "",
            "§eRight-click to apply!");

        // Pet Upgrade
        setItem(12, Material.ENCHANTED_BOOK, "§b§l📚 Pet Upgrade", 
            "§7Upgrades pet rarity by one level",
            "§7• Category: §bUpgrade",
            "§7• Effect: §a+1 Pet Rarity Level",
            "§7• Value: §a500 coins",
            "§7• Success Rate: §a100%",
            "",
            "§eRight-click to apply!");

        // Pet Skin
        setItem(13, Material.LEATHER, "§e§l🎨 Pet Skin", 
            "§7Changes pet appearance",
            "§7• Category: §eCosmetic",
            "§7• Effect: §aCustom Pet Skin",
            "§7• Value: §a50 coins",
            "§7• Success Rate: §a100%",
            "",
            "§eRight-click to apply!");

        // Pet Accessory
        setItem(14, Material.GOLD_NUGGET, "§c§l💎 Pet Accessory", 
            "§7Adds special pet abilities",
            "§7• Category: §dAbility",
            "§7• Effect: §aSpecial Pet Ability",
            "§7• Value: §a300 coins",
            "§7• Success Rate: §a100%",
            "",
            "§eRight-click to apply!");

        // Create Pet Item
        setItem(15, Material.CRAFTING_TABLE, "§a§l➕ Create Pet Item", 
            "§7Create a new Pet Item",
            "§7• Choose item type",
            "§7• Pay creation cost",
            "§7• Get item in inventory",
            "",
            "§eClick to create!");
    }

    private void setupPetCategories() {
        // Experience Category
        setItem(19, Material.EXPERIENCE_BOTTLE, "§a§l📈 Experience", 
            "§7Items that boost pet experience",
            "§7• Pet Candy: +25% XP gain",
            "§7• XP Boosters: +50% XP gain",
            "§7• XP Multipliers: +100% XP gain",
            "",
            "§eClick to view experience items!");

        // Stats Category
        setItem(20, Material.DIAMOND, "§c§l💪 Stats", 
            "§7Items that boost pet stats",
            "§7• Pet Food: +10% stats",
            "§7• Stat Boosters: +25% stats",
            "§7• Stat Multipliers: +50% stats",
            "",
            "§eClick to view stat items!");

        // Upgrade Category
        setItem(21, Material.ENCHANTING_TABLE, "§b§l⬆️ Upgrade", 
            "§7Items that upgrade pet rarity",
            "§7• Pet Upgrade: +1 rarity level",
            "§7• Rarity Boosters: +2 rarity levels",
            "§7• Rarity Multipliers: +3 rarity levels",
            "",
            "§eClick to view upgrade items!");

        // Cosmetic Category
        setItem(22, Material.PAINTING, "§e§l🎨 Cosmetic", 
            "§7Items that change pet appearance",
            "§7• Pet Skins: Custom skins",
            "§7• Pet Colors: Custom colors",
            "§7• Pet Effects: Visual effects",
            "",
            "§eClick to view cosmetic items!");

        // Ability Category
        setItem(23, Material.NETHER_STAR, "§d§l✨ Ability", 
            "§7Items that add pet abilities",
            "§7• Pet Accessories: Special abilities",
            "§7• Ability Boosters: Enhanced abilities",
            "§7• Ability Multipliers: Multiple abilities",
            "",
            "§eClick to view ability items!");

        // All Categories
        setItem(24, Material.CHEST, "§f§l📦 All Categories", 
            "§7View all pet item categories",
            "§7• Experience items",
            "§7• Stat items",
            "§7• Upgrade items",
            "§7• Cosmetic items",
            "§7• Ability items",
            "",
            "§eClick to view all categories!");
    }

    private void setupPetEffects() {
        // Pet Effects Info
        setItem(28, Material.PAPER, "§e§l📊 Pet Effects", 
            "§7Pet Item effects:",
            "§7• Pet Candy: §a+25% XP Gain",
            "§7• Pet Food: §a+10% Pet Stats",
            "§7• Pet Upgrade: §a+1 Pet Rarity",
            "§7• Pet Skin: §aCustom Appearance",
            "§7• Pet Accessory: §aSpecial Ability",
            "",
            "§eClick to view detailed effects!");

        // Pet Compatibility
        setItem(29, Material.WOLF_SPAWN_EGG, "§b§l🐕 Pet Compatibility", 
            "§7Pet Item compatibility:",
            "§7• All pet types supported",
            "§7• All rarity levels supported",
            "§7• All pet levels supported",
            "§7• Stackable effects",
            "",
            "§eClick to view compatibility!");

        // Usage Guide
        setItem(30, Material.BOOK, "§b§l📖 Usage Guide", 
            "§7How to use Pet Items:",
            "§7• 1. Get pet item",
            "§7• 2. Right-click with pet",
            "§7• 3. Effect applied instantly",
            "§7• 4. Check pet stats",
            "",
            "§eClick to view full guide!");
    }

    private void setupStatistics() {
        // Player Statistics
        PetItemSystem.PlayerPetItems playerItems = petItemSystem.getPlayerPetItems(player.getUniqueId());
        setItem(37, Material.GOLD_INGOT, "§6§l📈 Your Statistics", 
            "§7Your Pet Item usage:",
            "§7• Total items used: §a" + playerItems.getTotalPetItems(),
            "§7• Pet Candy: §a" + playerItems.getPetItemCount(PetItemSystem.PetItemType.PET_CANDY),
            "§7• Pet Food: §a" + playerItems.getPetItemCount(PetItemSystem.PetItemType.PET_FOOD),
            "§7• Pet Upgrades: §a" + playerItems.getPetItemCount(PetItemSystem.PetItemType.PET_UPGRADE),
            "§7• Pet Skins: §a" + playerItems.getPetItemCount(PetItemSystem.PetItemType.PET_SKIN),
            "§7• Pet Accessories: §a" + playerItems.getPetItemCount(PetItemSystem.PetItemType.PET_ACCESSORY),
            "",
            "§eClick to view detailed stats!");

        // Global Statistics
        setItem(38, Material.DIAMOND, "§b§l🌍 Global Statistics", 
            "§7Global Pet Item usage:",
            "§7• Total items used: §a1,234,567",
            "§7• Most used: §aPet Candy",
            "§7• Average per player: §a12.3",
            "§7• Success rate: §a100%",
            "",
            "§eClick to view global stats!");

        // Leaderboard
        setItem(39, Material.GOLD_BLOCK, "§6§l🏆 Pet Item Leaderboard", 
            "§7Top Pet Item users:",
            "§7• #1 PlayerName: §a1,234 items",
            "§7• #2 PlayerName: §a1,123 items",
            "§7• #3 PlayerName: §a1,012 items",
            "§7• #4 PlayerName: §a901 items",
            "§7• #5 PlayerName: §a890 items",
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
