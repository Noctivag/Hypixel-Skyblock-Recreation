package de.noctivag.plugin.items;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * CompleteItemGUI - GUI for browsing all Hypixel SkyBlock items and tools
 * 
 * Features:
 * - Category-based browsing
 * - Detailed item information
 * - Ability descriptions
 * - Recipe requirements
 * - Rarity information
 */
public class CompleteItemGUI {
    private final Plugin plugin;
    private final ItemAbilitySystem abilitySystem;
    
    public CompleteItemGUI(Plugin plugin) {
        this.plugin = plugin;
        this.abilitySystem = new ItemAbilitySystem(plugin);
    }
    
    /**
     * Open main item GUI
     */
    public void openItemGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lHypixel SkyBlock Items & Tools");
        
        // Category buttons
        setItem(gui, 10, Material.DRAGON_EGG, "§6§lDragon Weapons", 
            "§7Browse all dragon weapons",
            "§7• 3 Dragon weapon types",
            "§7• Legendary rarity",
            "§7• Powerful dragon abilities",
            "",
            "§eClick to browse!");
        
        setItem(gui, 12, Material.SKELETON_SKULL, "§8§lDungeon Weapons", 
            "§7Browse dungeon boss weapons",
            "§7• 11 Dungeon weapons",
            "§7• Epic-Legendary rarity",
            "§7• Boss-specific abilities",
            "",
            "§eClick to browse!");
        
        setItem(gui, 14, Material.WITHER_SKELETON_SKULL, "§0§lSlayer Weapons", 
            "§7Browse slayer boss weapons",
            "§7• 7 Slayer weapons",
            "§7• Epic-Mythic rarity",
            "§7• Slayer-specific abilities",
            "",
            "§eClick to browse!");
        
        setItem(gui, 16, Material.IRON_PICKAXE, "§e§lMining Tools", 
            "§7Browse mining tools and drills",
            "§7• 10 Mining tools",
            "§7• Common-Mythic rarity",
            "§7• Mining speed bonuses",
            "",
            "§eClick to browse!");
        
        setItem(gui, 20, Material.FISHING_ROD, "§b§lFishing Rods", 
            "§7Browse fishing rods and tools",
            "§7• 6 Fishing rods",
            "§7• Epic-Legendary rarity",
            "§7• Sea creature bonuses",
            "",
            "§eClick to browse!");
        
        setItem(gui, 22, Material.BLAZE_ROD, "§d§lMagic Weapons", 
            "§7Browse magical weapons and wands",
            "§7• 8 Magic weapons",
            "§7• Epic-Legendary rarity",
            "§7• Magical abilities",
            "",
            "§eClick to browse!");
        
        setItem(gui, 24, Material.BOW, "§c§lBows & Crossbows", 
            "§7Browse bows and crossbows",
            "§7• 5 Bow types",
            "§7• Rare-Mythic rarity",
            "§7• Ranged combat abilities",
            "",
            "§eClick to browse!");
        
        setItem(gui, 28, Material.EMERALD, "§5§lSpecial Items", 
            "§7Browse special and unique items",
            "§7• 8 Special items",
            "§7• Common-Legendary rarity",
            "§7• Unique abilities",
            "",
            "§eClick to browse!");
        
        // Navigation
        setItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the item GUI");
        
        player.openInventory(gui);
    }
    
    /**
     * Open dragon weapons GUI
     */
    public void openDragonWeaponsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lDragon Weapons");
        
        // Dragon weapons
        setItem(gui, 10, Material.DIAMOND_SWORD, "§6§lAspect of the Dragons", 
            "§7Summon dragon energy to devastate your enemies.",
            "§7• +225 Damage",
            "§7• +100 Strength",
            "§7• +25% Damage to End Mobs",
            "§7• Dragon Breath Ability",
            "§7• 60s Cooldown",
            "",
            "§6Recipe:",
            "§7• 1x Dragon Egg",
            "§7• 1x Diamond Sword",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 12, Material.DIAMOND_SWORD, "§a§lAspect of the End", 
            "§7Teleport behind enemies and deal massive damage.",
            "§7• +100 Damage",
            "§7• +100 Strength",
            "§7• Teleport 8 blocks",
            "§7• Instant Transmission Ability",
            "§7• 60s Cooldown",
            "",
            "§6Recipe:",
            "§7• 1x Ender Pearl",
            "§7• 1x Diamond Sword",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 14, Material.DIAMOND_SWORD, "§5§lAspect of the Void", 
            "§7Channel the power of the void to strike enemies.",
            "§7• +200 Damage",
            "§7• +150 Strength",
            "§7• +50% Damage to Endermen",
            "§7• Void Strike Ability",
            "§7• 60s Cooldown",
            "",
            "§6Recipe:",
            "§7• 1x Void Fragment",
            "§7• 1x Aspect of the End",
            "",
            "§eRight-click to activate ability!");
        
        // Navigation
        setItem(gui, 45, Material.ARROW, "§a§lBack", "§7Return to main item GUI");
        setItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the item GUI");
        
        player.openInventory(gui);
    }
    
    /**
     * Open dungeon weapons GUI
     */
    public void openDungeonWeaponsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§8§lDungeon Weapons");
        
        // Dungeon weapons
        setItem(gui, 10, Material.DIAMOND_SWORD, "§d§lHyperion", 
            "§7The most powerful weapon in the game.",
            "§7• +260 Damage",
            "§7• +150 Strength",
            "§7• +150 Intelligence",
            "§7• Wither Impact Ability",
            "§7• 30s Cooldown",
            "",
            "§6Recipe:",
            "§7• 1x Wither Catalyst",
            "§7• 1x Diamond Sword",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 12, Material.DIAMOND_SWORD, "§9§lScylla", 
            "§7A divine sword with healing abilities.",
            "§7• +260 Damage",
            "§7• +150 Strength",
            "§7• +150 Intelligence",
            "§7• Wither Impact Ability",
            "§7• 30s Cooldown",
            "",
            "§6Recipe:",
            "§7• 1x Wither Catalyst",
            "§7• 1x Diamond Sword",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 14, Material.DIAMOND_SWORD, "§b§lAstraea", 
            "§7A divine sword with protective abilities.",
            "§7• +260 Damage",
            "§7• +150 Strength",
            "§7• +150 Intelligence",
            "§7• Wither Impact Ability",
            "§7• 30s Cooldown",
            "",
            "§6Recipe:",
            "§7• 1x Wither Catalyst",
            "§7• 1x Diamond Sword",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 16, Material.DIAMOND_SWORD, "§f§lValkyrie", 
            "§7A divine sword with speed abilities.",
            "§7• +260 Damage",
            "§7• +150 Strength",
            "§7• +150 Intelligence",
            "§7• Wither Impact Ability",
            "§7• 30s Cooldown",
            "",
            "§6Recipe:",
            "§7• 1x Wither Catalyst",
            "§7• 1x Diamond Sword",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 19, Material.BONE, "§f§lBonemerang", 
            "§7A bone that returns to you after hitting enemies.",
            "§7• +180 Damage",
            "§7• Returns after hit",
            "§7• Multi-hit capability",
            "§7• Bone Toss Ability",
            "§7• 30s Cooldown",
            "",
            "§6Recipe:",
            "§7• 1x Bone",
            "§7• 1x String",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 21, Material.BOW, "§b§lSpirit Bow", 
            "§7A bow that channels spirit energy.",
            "§7• +200 Damage",
            "§7• +150 Strength",
            "§7• Ghost damage",
            "§7• Spirit Shot Ability",
            "§7• 30s Cooldown",
            "",
            "§6Recipe:",
            "§7• 1x Ghast Tear",
            "§7• 1x Bow",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 23, Material.STICK, "§b§lSpirit Scepter", 
            "§7A scepter that channels spirit magic.",
            "§7• +210 Damage",
            "§7• +200 Intelligence",
            "§7• Magic damage",
            "§7• Spirit Burst Ability",
            "§7• 30s Cooldown",
            "",
            "§6Recipe:",
            "§7• 1x Ghast Tear",
            "§7• 1x Stick",
            "",
            "§eRight-click to activate ability!");
        
        // Navigation
        setItem(gui, 45, Material.ARROW, "§a§lBack", "§7Return to main item GUI");
        setItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the item GUI");
        
        player.openInventory(gui);
    }
    
    /**
     * Open mining tools GUI
     */
    public void openMiningToolsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§e§lMining Tools");
        
        // Mining tools
        setItem(gui, 10, Material.DIAMOND_PICKAXE, "§b§lDiamond Pickaxe", 
            "§7A basic mining tool for efficient ore extraction.",
            "§7• +50 Mining Speed",
            "§7• Can mine ores",
            "§7• Basic Mining Ability",
            "§7• 120s Cooldown",
            "",
            "§6Recipe:",
            "§7• 3x Diamond",
            "§7• 2x Stick",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 12, Material.DIAMOND_PICKAXE, "§6§lStonk", 
            "§7An enhanced pickaxe with superior mining capabilities.",
            "§7• +80 Mining Speed",
            "§7• +25% Mining Fortune",
            "§7• Enhanced Mining Ability",
            "§7• 120s Cooldown",
            "",
            "§6Recipe:",
            "§7• 1x Diamond Pickaxe",
            "§7• 1x Efficiency V Book",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 14, Material.GOLDEN_PICKAXE, "§6§lGolden Pickaxe", 
            "§7A golden pickaxe with enhanced mining speed.",
            "§7• +60 Mining Speed",
            "§7• +10% Mining Fortune",
            "§7• Fast Mining Ability",
            "§7• 120s Cooldown",
            "",
            "§6Recipe:",
            "§7• 3x Gold Ingot",
            "§7• 2x Stick",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 16, Material.DIAMOND_PICKAXE, "§c§lMolten Pickaxe", 
            "§7A pickaxe forged in the depths of the nether.",
            "§7• +120 Mining Speed",
            "§7• +50% Mining Fortune",
            "§7• Fire Resistance",
            "§7• Molten Mining Ability",
            "§7• 120s Cooldown",
            "",
            "§6Recipe:",
            "§7• 1x Netherite Ingot",
            "§7• 1x Diamond Pickaxe",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 19, Material.DIAMOND_PICKAXE, "§7§lTitanium Pickaxe", 
            "§7A pickaxe made from titanium for ultimate mining power.",
            "§7• +150 Mining Speed",
            "§7• +75% Mining Fortune",
            "§7• Titanium Boost",
            "§7• Titanium Mining Ability",
            "§7• 120s Cooldown",
            "",
            "§6Recipe:",
            "§7• 1x Titanium",
            "§7• 1x Diamond Pickaxe",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 21, Material.DIAMOND_PICKAXE, "§e§lDrill Engine", 
            "§7A mechanical drill with incredible mining capabilities.",
            "§7• +140 Mining Speed",
            "§7• +60% Mining Fortune",
            "§7• Auto-repair",
            "§7• Mechanical Mining Ability",
            "§7• 120s Cooldown",
            "",
            "§6Recipe:",
            "§7• 1x Drill Core",
            "§7• 1x Diamond Pickaxe",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 23, Material.DIAMOND_PICKAXE, "§7§lTitanium Drill DR-X355", 
            "§7An advanced titanium drill with superior mining efficiency.",
            "§7• +180 Mining Speed",
            "§7• +100% Mining Fortune",
            "§7• Titanium Boost",
            "§7• Advanced Mining Ability",
            "§7• 120s Cooldown",
            "",
            "§6Recipe:",
            "§7• 1x Titanium",
            "§7• 1x Drill Engine",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 25, Material.DIAMOND_PICKAXE, "§7§lTitanium Drill DR-X455", 
            "§7A high-end titanium drill with exceptional mining power.",
            "§7• +220 Mining Speed",
            "§7• +150% Mining Fortune",
            "§7• Enhanced Titanium Boost",
            "§7• High-End Mining Ability",
            "§7• 120s Cooldown",
            "",
            "§6Recipe:",
            "§7• 1x Titanium",
            "§7• 1x Titanium Drill DR-X355",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 28, Material.DIAMOND_PICKAXE, "§7§lTitanium Drill DR-X555", 
            "§7The ultimate titanium drill with maximum mining efficiency.",
            "§7• +260 Mining Speed",
            "§7• +200% Mining Fortune",
            "§7• Maximum Titanium Boost",
            "§7• Ultimate Mining Ability",
            "§7• 120s Cooldown",
            "",
            "§6Recipe:",
            "§7• 1x Titanium",
            "§7• 1x Titanium Drill DR-X455",
            "",
            "§eRight-click to activate ability!");
        
        setItem(gui, 30, Material.DIAMOND_PICKAXE, "§6§lGauntlet", 
            "§7A powerful gauntlet that enhances all mining activities.",
            "§7• +200 Mining Speed",
            "§7• +100% Mining Fortune",
            "§7• All-in-one tool",
            "§7• Gauntlet Power Ability",
            "§7• 120s Cooldown",
            "",
            "§6Recipe:",
            "§7• 1x Gauntlet Core",
            "§7• 1x Diamond Pickaxe",
            "",
            "§eRight-click to activate ability!");
        
        // Navigation
        setItem(gui, 45, Material.ARROW, "§a§lBack", "§7Return to main item GUI");
        setItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the item GUI");
        
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
