package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.items.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import net.kyori.adventure.text.Component;
import java.util.stream.Collectors;

/**
 * ItemCommands - Commands for managing Hypixel SkyBlock items and tools
 * 
 * Commands:
 * - /items - Open item GUI
 * - /items gui [category] - Open specific category GUI
 * - /items give <item> [amount] - Give item to player
 * - /items ability <item> - Activate item ability
 * - /items list [category] - List all items in category
 * - /items info <item> - Show item information
 */
public class ItemCommands implements CommandExecutor, TabCompleter {
    private final SkyblockPlugin SkyblockPlugin;
    private final CompleteItemGUI itemGUI;
    private final ItemAbilitySystem abilitySystem;
    
    public ItemCommands(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.itemGUI = new CompleteItemGUI(SkyblockPlugin);
        this.abilitySystem = new ItemAbilitySystem(SkyblockPlugin);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            // Open main item GUI
            itemGUI.openItemGUI(player);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "gui":
                handleGUICommand(player, args);
                break;
            case "give":
                handleGiveCommand(player, args);
                break;
            case "ability":
                handleAbilityCommand(player, args);
                break;
            case "list":
                handleListCommand(player, args);
                break;
            case "info":
                handleInfoCommand(player, args);
                break;
            case "help":
                handleHelpCommand(player);
                break;
            default:
                player.sendMessage(Component.text("§cUnknown subcommand! Use /items help for help."));
                break;
        }
        
        return true;
    }
    
    private void handleGUICommand(Player player, String[] args) {
        if (args.length == 1) {
            itemGUI.openItemGUI(player);
            return;
        }
        
        String category = args[1].toLowerCase();
        switch (category) {
            case "dragon":
            case "dragonweapons":
                itemGUI.openDragonWeaponsGUI(player);
                break;
            case "dungeon":
            case "dungeonweapons":
                itemGUI.openDungeonWeaponsGUI(player);
                break;
            case "slayer":
            case "slayerweapons":
                player.sendMessage(Component.text("§7Opening Slayer Weapons GUI..."));
                // Implement slayer weapons GUI
                if (SkyblockPlugin.getSlayerSystem() != null) {
                    SkyblockPlugin.getSlayerSystem().openSlayerWeaponsGUI(player);
                } else {
                    player.sendMessage(Component.text("§cSlayer-System ist nicht verfügbar!"));
                }
                break;
            case "mining":
            case "miningtools":
                itemGUI.openMiningToolsGUI(player);
                break;
            case "fishing":
            case "fishingrods":
                player.sendMessage(Component.text("§7Opening Fishing Rods GUI..."));
                // Implement fishing rods GUI
                if (SkyblockPlugin.getFishingSystem() != null) {
                    ((de.noctivag.skyblock.fishing.FishingSystem) SkyblockPlugin.getFishingSystem()).openFishingRodsGUI(player);
                } else {
                    player.sendMessage(Component.text("§cFishing-System ist nicht verfügbar!"));
                }
                break;
            case "magic":
            case "magicweapons":
                player.sendMessage(Component.text("§7Opening Magic Weapons GUI..."));
                // Implement magic weapons GUI
                if (SkyblockPlugin.getMagicSystem() != null) {
                    ((de.noctivag.skyblock.magic.MagicSystem) SkyblockPlugin.getMagicSystem()).openMagicWeaponsGUI(player);
                } else {
                    player.sendMessage(Component.text("§cMagic-System ist nicht verfügbar!"));
                }
                break;
            case "bows":
            case "crossbows":
                player.sendMessage(Component.text("§7Opening Bows & Crossbows GUI..."));
                // Implement bows GUI
                if (SkyblockPlugin.getCombatSystem() != null) {
                    SkyblockPlugin.getCombatSystem().openBowsGUI(player);
                } else {
                    player.sendMessage(Component.text("§cCombat-System ist nicht verfügbar!"));
                }
                break;
            case "special":
            case "specialitems":
                player.sendMessage(Component.text("§7Opening Special Items GUI..."));
                // Implement special items GUI
                if (SkyblockPlugin.getItemsSystem() != null) {
                    SkyblockPlugin.getItemsSystem().openSpecialItemsGUI(player);
                } else {
                    player.sendMessage(Component.text("§cItems-System ist nicht verfügbar!"));
                }
                break;
            default:
                player.sendMessage(Component.text("§cUnknown category! Available categories:"));
                player.sendMessage(Component.text("§7- dragon, dungeon, slayer, mining, fishing, magic, bows, special"));
                break;
        }
    }
    
    private void handleGiveCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("§cUsage: /items give <item> [amount]"));
            return;
        }
        
        String itemName = args[1];
        int amount = 1;
        
        if (args.length > 2) {
            try {
                amount = Integer.parseInt(args[2]);
                if (amount < 1 || amount > 64) {
                    player.sendMessage(Component.text("§cAmount must be between 1 and 64!"));
                    return;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(Component.text("§cInvalid amount! Must be a number."));
                return;
            }
        }
        
        // Find item type
        ItemType itemType = null;
        for (ItemType type : ItemType.values()) {
            if (type.name().equalsIgnoreCase(itemName) || 
                type.getDisplayName().equalsIgnoreCase(itemName)) {
                itemType = type;
                break;
            }
        }
        
        if (itemType == null) {
            player.sendMessage("§cUnknown item: " + itemName);
            player.sendMessage(Component.text("§7Use /items list to see all available items."));
            return;
        }
        
        // Create item
        ItemStack item = createItemFromType(itemType, amount);
        if (item != null) {
            player.getInventory().addItem(item);
            player.sendMessage("§aGave " + amount + "x " + itemType.getDisplayName() + " to " + player.getName());
        } else {
            player.sendMessage("§cFailed to create item: " + itemType.getDisplayName());
        }
    }
    
    private void handleAbilityCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("§cUsage: /items ability <item>"));
            return;
        }
        
        String itemName = args[1];
        
        // Find item type
        ItemType itemType = null;
        for (ItemType type : ItemType.values()) {
            if (type.name().equalsIgnoreCase(itemName) || 
                type.getDisplayName().equalsIgnoreCase(itemName)) {
                itemType = type;
                break;
            }
        }
        
        if (itemType == null) {
            player.sendMessage("§cUnknown item: " + itemName);
            player.sendMessage(Component.text("§7Use /items list to see all available items."));
            return;
        }
        
        // Check if player has the item
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem == null || heldItem.getType() == Material.AIR) {
            player.sendMessage(Component.text("§cYou must be holding the item to activate its ability!"));
            return;
        }
        
        // Activate ability
        abilitySystem.activateAbility(player, itemType);
    }
    
    private void handleListCommand(Player player, String[] args) {
        if (args.length == 1) {
            // List all categories
            player.sendMessage(Component.text("§6§l=== Hypixel SkyBlock Items & Tools ==="));
            player.sendMessage(Component.text("§7Categories:"));
            player.sendMessage(Component.text("§6• Dragon Weapons (3 items)"));
            player.sendMessage(Component.text("§8• Dungeon Weapons (11 items)"));
            player.sendMessage(Component.text("§0• Slayer Weapons (7 items)"));
            player.sendMessage(Component.text("§e• Mining Tools (10 items)"));
            player.sendMessage(Component.text("§b• Fishing Rods (6 items)"));
            player.sendMessage(Component.text("§d• Magic Weapons (8 items)"));
            player.sendMessage(Component.text("§c• Bows & Crossbows (5 items)"));
            player.sendMessage(Component.text("§5• Special Items (8 items)"));
            player.sendMessage("");
            player.sendMessage(Component.text("§7Use /items list <category> to see items in a category."));
            return;
        }
        
        String category = args[1].toLowerCase();
        ItemCategory itemCategory = null;
        
        switch (category) {
            case "dragon":
            case "dragonweapons":
                itemCategory = ItemCategory.DRAGON_WEAPONS;
                break;
            case "dungeon":
            case "dungeonweapons":
                itemCategory = ItemCategory.DUNGEON_WEAPONS;
                break;
            case "slayer":
            case "slayerweapons":
                itemCategory = ItemCategory.SLAYER_WEAPONS;
                break;
            case "mining":
            case "miningtools":
                itemCategory = ItemCategory.MINING_TOOLS;
                break;
            case "fishing":
            case "fishingrods":
                itemCategory = ItemCategory.FISHING_RODS;
                break;
            case "magic":
            case "magicweapons":
                itemCategory = ItemCategory.MAGIC_WEAPONS;
                break;
            case "bows":
            case "crossbows":
                itemCategory = ItemCategory.BOWS_CROSSBOWS;
                break;
            case "special":
            case "specialitems":
                itemCategory = ItemCategory.SPECIAL_ITEMS;
                break;
            default:
                player.sendMessage("§cUnknown category: " + category);
                player.sendMessage(Component.text("§7Available categories: dragon, dungeon, slayer, mining, fishing, magic, bows, special"));
                return;
        }
        
        // List items in category
        ItemType[] items = ItemType.getByCategory(itemCategory);
        player.sendMessage("§6§l=== " + itemCategory.getDisplayName() + " ===");
        player.sendMessage("§7" + itemCategory.getDescription());
        player.sendMessage("");
        
        for (ItemType itemType : items) {
            ItemRarity rarity = itemType.getRarity();
            player.sendMessage(rarity.getDisplayName() + " " + itemType.getDisplayName());
        }
        
        player.sendMessage("");
        player.sendMessage(Component.text("§7Use /items info <item> to see detailed information."));
    }
    
    private void handleInfoCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("§cUsage: /items info <item>"));
            return;
        }
        
        String itemName = args[1];
        
        // Find item type
        ItemType itemType = null;
        for (ItemType type : ItemType.values()) {
            if (type.name().equalsIgnoreCase(itemName) || 
                type.getDisplayName().equalsIgnoreCase(itemName)) {
                itemType = type;
                break;
            }
        }
        
        if (itemType == null) {
            player.sendMessage("§cUnknown item: " + itemName);
            player.sendMessage(Component.text("§7Use /items list to see all available items."));
            return;
        }
        
        // Show item information
        ItemRarity rarity = itemType.getRarity();
        ItemCategory category = itemType.getCategory();
        
        player.sendMessage("§6§l=== " + itemType.getDisplayName() + " ===");
        player.sendMessage("§7Category: " + category.getDisplayName());
        player.sendMessage("§7Rarity: " + rarity.getDisplayName());
        player.sendMessage("§7Description: " + category.getDescription());
        player.sendMessage("");
        player.sendMessage("§7Use /items give " + itemType.name() + " to get this item.");
        player.sendMessage("§7Use /items ability " + itemType.name() + " to activate its ability.");
    }
    
    private void handleHelpCommand(Player player) {
        player.sendMessage(Component.text("§6§l=== Item Commands Help ==="));
        player.sendMessage(Component.text("§7/item - Open the main item GUI"));
        player.sendMessage(Component.text("§7/items gui [category] - Open specific category GUI"));
        player.sendMessage(Component.text("§7/items give <item> [amount] - Give item to player"));
        player.sendMessage(Component.text("§7/items ability <item> - Activate item ability"));
        player.sendMessage(Component.text("§7/items list [category] - List all items in category"));
        player.sendMessage(Component.text("§7/items info <item> - Show item information"));
        player.sendMessage(Component.text("§7/items help - Show this help message"));
        player.sendMessage("");
        player.sendMessage(Component.text("§7Categories: dragon, dungeon, slayer, mining, fishing, magic, bows, special"));
    }
    
    private ItemStack createItemFromType(ItemType itemType, int amount) {
        // Get material and create item
        Material material = getMaterialForItemType(itemType);
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            // Set display name
            meta.displayName(net.kyori.adventure.text.Component.text(itemType.getDisplayName()));
            
            // Set lore
            ItemRarity rarity = itemType.getRarity();
            ItemCategory category = itemType.getCategory();
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Category: " + category.getDisplayName());
            lore.add("§7Rarity: " + rarity.getDisplayName());
            lore.add("");
            lore.add("§7" + category.getDescription());
            lore.add("");
            lore.add("§eRight-click to activate ability!");
            
            meta.lore(lore.stream().map(line -> net.kyori.adventure.text.Component.text(line)).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    private Material getMaterialForItemType(ItemType itemType) {
        // Map item types to materials
        switch (itemType) {
            // Dragon Weapons
            case ASPECT_OF_THE_DRAGONS:
            case ASPECT_OF_THE_END:
            case ASPECT_OF_THE_VOID:
                return Material.DIAMOND_SWORD;
                
            // Dungeon Weapons
            case HYPERION:
            case SCYLLA:
            case ASTRAEA:
            case VALKYRIE:
            case SPIRIT_SWORD:
            case ADAPTIVE_BLADE:
            case SHADOW_FURY:
            case LIVID_DAGGER:
                return Material.DIAMOND_SWORD;
            case BONEMERANG:
                return Material.BONE;
            case SPIRIT_BOW:
                return Material.BOW;
            case SPIRIT_SCEPTER:
                return Material.STICK;
                
            // Slayer Weapons
            case REVENANT_FALCHION:
            case REAPER_FALCHION:
            case REAPER_SCYTHE:
            case VOIDEDGE_KATANA:
            case VOIDWALKER_KATANA:
            case VOIDLING_KATANA:
                return Material.IRON_SWORD;
            case AXE_OF_THE_SHREDDED:
                return Material.IRON_AXE;
                
            // Mining Tools
            case DIAMOND_PICKAXE:
            case STONK:
            case MOLTEN_PICKAXE:
            case TITANIUM_PICKAXE:
            case DRILL_ENGINE:
            case TITANIUM_DRILL_DR_X355:
            case TITANIUM_DRILL_DR_X455:
            case TITANIUM_DRILL_DR_X555:
            case GAUNTLET:
                return Material.DIAMOND_PICKAXE;
            case GOLDEN_PICKAXE:
                return Material.GOLDEN_PICKAXE;
                
            // Fishing Rods
            case ROD_OF_THE_SEA:
            case CHALLENGING_ROD:
            case ROD_OF_LEGENDS:
            case SHARK_BAIT:
            case SHARK_SCALE_ROD:
            case AUGER_ROD:
                return Material.FISHING_ROD;
                
            // Magic Weapons
            case FIRE_VEIL_WAND:
                return Material.BLAZE_ROD;
            case FROZEN_SCYTHE:
                return Material.DIAMOND_SWORD;
            case VOODOO_DOLL:
            case BONZO_STAFF:
            case PROFESSOR_SCARF_STAFF:
                return Material.STICK;
            case SCARF_STUDIES:
                return Material.BOOK;
            case THORN_BOW:
            case LAST_BREATH:
                return Material.BOW;
                
            // Bows
            case JUJU_SHORTBOW:
            case TERMINATOR:
            case ARTISANAL_SHORTBOW:
            case MAGMA_BOW:
            case VENOM_TOUCH:
                return Material.BOW;
                
            // Special Items
            case GRAPPLING_HOOK:
                return Material.FISHING_ROD;
            case ENDER_PEARL:
                return Material.ENDER_PEARL;
            case AOTE:
            case AOTV:
                return Material.DIAMOND_SWORD;
            case PIGMAN_SWORD:
                return Material.GOLDEN_SWORD;
            case GOLDEN_APPLE:
                return Material.GOLDEN_APPLE;
            case ENCHANTED_GOLDEN_APPLE:
                return Material.ENCHANTED_GOLDEN_APPLE;
            case POTATO_WAR_ARMOR:
                return Material.DIAMOND_CHESTPLATE;
                
            default:
                return Material.DIAMOND_SWORD;
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            // Subcommands
            completions.addAll(Arrays.asList("gui", "give", "ability", "list", "info", "help"));
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "gui":
                case "list":
                    // Categories
                    completions.addAll(Arrays.asList("dragon", "dungeon", "slayer", "mining", "fishing", "magic", "bows", "special"));
                    break;
                case "give":
                case "ability":
                case "info":
                    // Item types
                    for (ItemType type : ItemType.values()) {
                        completions.add(type.name());
                    }
                    break;
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            // Amount for give command
            completions.addAll(Arrays.asList("1", "5", "10", "16", "32", "64"));
        }
        
        return completions;
    }
}
