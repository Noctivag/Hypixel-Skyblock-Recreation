package de.noctivag.skyblock.armor;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
// import java.util.List;

/**
 * CompleteArmorGUI - GUI for browsing all Hypixel SkyBlock armor sets
 * 
 * Features:
 * - Category-based browsing
 * - Detailed armor information
 * - Ability descriptions
 * - Set bonus information
 * - Recipe requirements
 */
public class CompleteArmorGUI {
    private final SkyblockPlugin plugin;
    private final ArmorAbilitySystem abilitySystem;
    
    public CompleteArmorGUI(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.abilitySystem = new ArmorAbilitySystem(plugin, null);
    }
    
    /**
     * Open main armor GUI
     */
    public void openArmorGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lHypixel SkyBlock Armor");
        
        // Category buttons
        setItem(gui, 10, Material.DRAGON_EGG, "§6§lDragon Armor", 
            "§7Browse all dragon armor sets",
            "§7• 7 Different dragon types",
            "§7• Legendary rarity",
            "§7• Powerful abilities",
            "",
            "§eClick to browse!");
        
        setItem(gui, 12, Material.IRON_PICKAXE, "§e§lMining Armor", 
            "§7Browse mining-focused armor sets",
            "§7• Enhanced mining speed",
            "§7• Mining bonuses",
            "§7• Special mining abilities",
            "",
            "§eClick to browse!");
        
        setItem(gui, 14, Material.DIAMOND_SWORD, "§c§lCombat Armor", 
            "§7Browse combat-focused armor sets",
            "§7• High damage and defense",
            "§7• Combat abilities",
            "§7• Wither Lord armor",
            "",
            "§eClick to browse!");
        
        setItem(gui, 16, Material.FISHING_ROD, "§b§lFishing Armor", 
            "§7Browse fishing-focused armor sets",
            "§7• Sea creature bonuses",
            "§7• Fishing abilities",
            "§7• Water-based effects",
            "",
            "§eClick to browse!");
        
        setItem(gui, 20, Material.JACK_O_LANTERN, "§d§lEvent Armor", 
            "§7Browse event and seasonal armor",
            "§7• Halloween, Christmas, Easter",
            "§7• Special event bonuses",
            "§7• Limited time availability",
            "",
            "§eClick to browse!");
        
        setItem(gui, 22, Material.NETHERITE_CHESTPLATE, "§4§lCrimson Isle Armor", 
            "§7Browse Crimson Isle armor sets",
            "§7• Nether-based armor",
            "§7• Fire resistance",
            "§7• Powerful abilities",
            "",
            "§eClick to browse!");
        
        setItem(gui, 24, Material.EMERALD, "§5§lSpecial Armor", 
            "§7Browse special and unique armor",
            "§7• Unique abilities",
            "§7• Special effects",
            "§7• Rare armor sets",
            "",
            "§eClick to browse!");
        
        setItem(gui, 28, Material.LEATHER_CHESTPLATE, "§7§lBasic Armor", 
            "§7Browse basic armor sets",
            "§7• Beginner-friendly",
            "§7• Easy to obtain",
            "§7• Good starting stats",
            "",
            "§eClick to browse!");
        
        setItem(gui, 30, Material.SKELETON_SKULL, "§8§lDungeon Armor", 
            "§7Browse dungeon boss armor",
            "§7• Boss-specific armor",
            "§7• Dungeon bonuses",
            "§7• Floor requirements",
            "",
            "§eClick to browse!");
        
        setItem(gui, 32, Material.WITHER_SKELETON_SKULL, "§0§lSlayer Armor", 
            "§7Browse slayer boss armor",
            "§7• Slayer-specific armor",
            "§7• Slayer bonuses",
            "§7• Tier requirements",
            "",
            "§eClick to browse!");
        
        // Navigation
        setItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the armor GUI");
        
        player.openInventory(gui);
    }
    
    /**
     * Open dragon armor GUI
     */
    public void openDragonArmorGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lDragon Armor Sets");
        
        // Dragon armor sets
        setItem(gui, 10, Material.DIAMOND_CHESTPLATE, "§a§lProtector Dragon Armor", 
            "§7A dragon armor set that focuses on defense.",
            "§7• +250 Defense",
            "§7• +50 Health",
            "§7• Protector's Shield Ability",
            "§7• 60s Cooldown",
            "",
            "§6Recipe:",
            "§7• 240x Protector Dragon Fragment",
            "§7• 1x Diamond Chestplate",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 12, Material.DIAMOND_CHESTPLATE, "§e§lOld Dragon Armor", 
            "§7An ancient dragon armor with wisdom.",
            "§7• +200 Defense",
            "§7• +100 Health",
            "§7• Ancient Wisdom Ability",
            "§7• 60s Cooldown",
            "",
            "§6Recipe:",
            "§7• 240x Old Dragon Fragment",
            "§7• 1x Diamond Chestplate",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 14, Material.DIAMOND_CHESTPLATE, "§b§lWise Dragon Armor", 
            "§7A dragon armor set that enhances intelligence.",
            "§7• +150 Intelligence",
            "§7• +50 Defense",
            "§7• Wise's Intelligence Ability",
            "§7• 60s Cooldown",
            "",
            "§6Recipe:",
            "§7• 240x Wise Dragon Fragment",
            "§7• 1x Diamond Chestplate",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 16, Material.DIAMOND_CHESTPLATE, "§f§lYoung Dragon Armor", 
            "§7A dragon armor set that provides speed.",
            "§7• +150 Speed",
            "§7• +50 Defense",
            "§7• Young's Speed Ability",
            "§7• 60s Cooldown",
            "",
            "§6Recipe:",
            "§7• 240x Young Dragon Fragment",
            "§7• 1x Diamond Chestplate",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 19, Material.DIAMOND_CHESTPLATE, "§c§lStrong Dragon Armor", 
            "§7A dragon armor set that focuses on strength.",
            "§7• +150 Strength",
            "§7• +50 Defense",
            "§7• Strong's Strength Ability",
            "§7• 60s Cooldown",
            "",
            "§6Recipe:",
            "§7• 240x Strong Dragon Fragment",
            "§7• 1x Diamond Chestplate",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 21, Material.DIAMOND_CHESTPLATE, "§d§lUnstable Dragon Armor", 
            "§7A dragon armor set with chaotic effects.",
            "§7• +100 Critical Chance",
            "§7• +50 Defense",
            "§7• Unstable's Chaos Ability",
            "§7• 60s Cooldown",
            "",
            "§6Recipe:",
            "§7• 240x Unstable Dragon Fragment",
            "§7• 1x Diamond Chestplate",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 23, Material.DIAMOND_CHESTPLATE, "§6§lSuperior Dragon Armor", 
            "§7The ultimate dragon armor with superior stats.",
            "§7• +75 All Stats",
            "§7• +100 Defense",
            "§7• Superior's Superiority Ability",
            "§7• 60s Cooldown",
            "",
            "§6Recipe:",
            "§7• 240x Superior Dragon Fragment",
            "§7• 1x Diamond Chestplate",
            "",
            "§eRight-click to activate ability!");
        
        // Navigation
        setItem(gui, 45, Material.ARROW, "§a§lBack", "§7Return to main armor GUI");
        setItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the armor GUI");
        
        player.openInventory(gui);
    }
    
    /**
     * Open mining armor GUI
     */
    public void openMiningArmorGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§e§lMining Armor Sets");
        
        // Mining armor sets
        setItem(gui, 10, Material.IRON_PICKAXE, "§6§lMineral Armor", 
            "§7Armor that enhances mining efficiency.",
            "§7• +390 Defense",
            "§7• +50 Speed",
            "§7• Triple Mining Ability",
            "§7• 45s Cooldown",
            "",
            "§6Recipe:",
            "§7• 64x Enchanted Iron",
            "§7• 32x Enchanted Coal",
            "§7• 16x Enchanted Redstone",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 12, Material.BLUE_ICE, "§b§lGlacite Armor", 
            "§7Armor made from glacite with enhanced mining.",
            "§7• +415 Defense",
            "§7• +50 Speed",
            "§7• +50 Mining Speed",
            "§7• Expert Miner Ability",
            "§7• 45s Cooldown",
            "",
            "§6Recipe:",
            "§7• 128x Glacite",
            "§7• 64x Enchanted Ice",
            "§7• 32x Enchanted Packed Ice",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 14, Material.NETHERITE_CHESTPLATE, "§8§lSorrow Armor", 
            "§7Armor infused with sorrow for mining power.",
            "§7• +500 Defense",
            "§7• +100 Mining Speed",
            "§7• +25 Magic Find",
            "§7• Sorrow's Mining Ability",
            "§7• 45s Cooldown",
            "",
            "§6Recipe:",
            "§7• 256x Sorrow",
            "§7• 128x Enchanted Netherrack",
            "§7• 64x Enchanted Quartz",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 16, Material.NETHERITE_CHESTPLATE, "§5§lDIVAN's Armor", 
            "§7The ultimate mining armor by DIVAN.",
            "§7• +600 Defense",
            "§7• +150 Mining Speed",
            "§7• +50 Magic Find",
            "§7• DIVAN's Mining Mastery",
            "§7• 45s Cooldown",
            "",
            "§6Recipe:",
            "§7• 512x DIVAN's Alloy",
            "§7• 256x Enchanted Mithril",
            "§7• 128x Enchanted Titanium",
            "",
            "§eRight-click to activate ability!");
        
        // Navigation
        setItem(gui, 45, Material.ARROW, "§a§lBack", "§7Return to main armor GUI");
        setItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the armor GUI");
        
        player.openInventory(gui);
    }
    
    /**
     * Set item in GUI with lore
     */
    private void setItem(Inventory gui, int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(net.kyori.adventure.text.Component.text(name));
            meta.lore(Arrays.stream(lore).map(line -> net.kyori.adventure.text.Component.text(line)).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        
        gui.setItem(slot, item);
    }
}
