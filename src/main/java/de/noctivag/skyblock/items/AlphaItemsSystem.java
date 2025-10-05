package de.noctivag.skyblock.items;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.CorePlatform;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Alpha Items System - Hypixel Skyblock Style
 * 
 * Features:
 * - Alpha items with special properties
 * - Alpha item crafting and upgrading
 * - Alpha item effects and bonuses
 * - Alpha item rarity system
 * - Alpha item GUI interface
 */
public class AlphaItemsSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final CorePlatform corePlatform;
    private final Map<String, AlphaItem> alphaItems = new HashMap<>();
    private final Map<UUID, List<AlphaItem>> playerAlphaItems = new ConcurrentHashMap<>();
    
    public AlphaItemsSystem(SkyblockPlugin SkyblockPlugin, CorePlatform corePlatform) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.corePlatform = corePlatform;
        initializeAlphaItems();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeAlphaItems() {
        // Alpha Weapons
        alphaItems.put("ALPHA_SWORD", new AlphaItem(
            "ALPHA_SWORD", "Alpha Sword", "§d§lAlpha Sword", Material.DIAMOND_SWORD,
            AlphaItemRarity.ALPHA, AlphaItemType.WEAPON,
            "§7A legendary sword forged in the depths of the Alpha dimension.",
            Arrays.asList("§7+100% Damage", "§7+50% Critical Chance", "§7+25% Critical Damage"),
            Arrays.asList("§7- 1x Diamond Sword", "§7- 1x Nether Star", "§7- 1x Ender Pearl"),
            Arrays.asList("ALPHA_DAMAGE", "ALPHA_CRITICAL", "ALPHA_SPECIAL")
        ));
        
        alphaItems.put("ALPHA_BOW", new AlphaItem(
            "ALPHA_BOW", "Alpha Bow", "§d§lAlpha Bow", Material.BOW,
            AlphaItemRarity.ALPHA, AlphaItemType.WEAPON,
            "§7A mystical bow that never misses its target.",
            Arrays.asList("§7+75% Damage", "§7+100% Accuracy", "§7+30% Arrow Speed"),
            Arrays.asList("§7- 1x Bow", "§7- 1x Nether Star", "§7- 1x String"),
            Arrays.asList("ALPHA_ACCURACY", "ALPHA_SPEED", "ALPHA_PIERCE")
        ));
        
        // Alpha Armor
        alphaItems.put("ALPHA_HELMET", new AlphaItem(
            "ALPHA_HELMET", "Alpha Helmet", "§d§lAlpha Helmet", Material.DIAMOND_HELMET,
            AlphaItemRarity.ALPHA, AlphaItemType.ARMOR,
            "§7A helmet that grants incredible mental powers.",
            Arrays.asList("§7+80% Defense", "§7+50% Intelligence", "§7+25% Mana"),
            Arrays.asList("§7- 1x Diamond Helmet", "§7- 1x Nether Star", "§7- 1x Lapis Lazuli"),
            Arrays.asList("ALPHA_DEFENSE", "ALPHA_INTELLIGENCE", "ALPHA_MANA")
        ));
        
        alphaItems.put("ALPHA_CHESTPLATE", new AlphaItem(
            "ALPHA_CHESTPLATE", "Alpha Chestplate", "§d§lAlpha Chestplate", Material.DIAMOND_CHESTPLATE,
            AlphaItemRarity.ALPHA, AlphaItemType.ARMOR,
            "§7A chestplate that protects against all forms of damage.",
            Arrays.asList("§7+100% Defense", "§7+30% Health", "§7+20% Resistance"),
            Arrays.asList("§7- 1x Diamond Chestplate", "§7- 1x Nether Star", "§7- 1x Iron Ingot"),
            Arrays.asList("ALPHA_DEFENSE", "ALPHA_HEALTH", "ALPHA_RESISTANCE")
        ));
        
        // Alpha Tools
        alphaItems.put("ALPHA_PICKAXE", new AlphaItem(
            "ALPHA_PICKAXE", "Alpha Pickaxe", "§d§lAlpha Pickaxe", Material.DIAMOND_PICKAXE,
            AlphaItemRarity.ALPHA, AlphaItemType.TOOL,
            "§7A pickaxe that can mine through any material instantly.",
            Arrays.asList("§7+200% Mining Speed", "§7+100% Fortune", "§7+50% Efficiency"),
            Arrays.asList("§7- 1x Diamond Pickaxe", "§7- 1x Nether Star", "§7- 1x Coal"),
            Arrays.asList("ALPHA_MINING", "ALPHA_FORTUNE", "ALPHA_EFFICIENCY")
        ));
        
        // Alpha Accessories
        alphaItems.put("ALPHA_RING", new AlphaItem(
            "ALPHA_RING", "Alpha Ring", "§d§lAlpha Ring", Material.GOLD_INGOT,
            AlphaItemRarity.ALPHA, AlphaItemType.ACCESSORY,
            "§7A ring that grants incredible powers to its wearer.",
            Arrays.asList("§7+25% All Stats", "§7+50% Luck", "§7+30% Magic Find"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Nether Star", "§7- 1x Diamond"),
            Arrays.asList("ALPHA_ALL_STATS", "ALPHA_LUCK", "ALPHA_MAGIC_FIND")
        ));
        
        // Alpha Special Items
        alphaItems.put("ALPHA_ORB", new AlphaItem(
            "ALPHA_ORB", "Alpha Orb", "§d§lAlpha Orb", Material.END_CRYSTAL,
            AlphaItemRarity.ALPHA, AlphaItemType.SPECIAL,
            "§7A mysterious orb that contains the power of the Alpha dimension.",
            Arrays.asList("§7+100% All Stats", "§7+200% XP Gain", "§7+150% Coin Gain"),
            Arrays.asList("§7- 1x End Crystal", "§7- 1x Nether Star", "§7- 1x Dragon Egg"),
            Arrays.asList("ALPHA_ALL_BOOST", "ALPHA_XP_BOOST", "ALPHA_COIN_BOOST")
        ));
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        if (title.contains("Alpha Items")) {
            event.setCancelled(true);
            handleAlphaItemsGUIClick(player, event.getSlot());
        }
    }
    
    public void openAlphaItemsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§d§lAlpha Items"));
        
        // Alpha item categories
        addGUIItem(gui, 10, Material.DIAMOND_SWORD, "§c§lAlpha Weapons", 
            Arrays.asList("§7Legendary weapons from the", "§7Alpha dimension", "", "§eClick to view"));
        
        addGUIItem(gui, 11, Material.DIAMOND_CHESTPLATE, "§b§lAlpha Armor", 
            Arrays.asList("§7Legendary armor from the", "§7Alpha dimension", "", "§eClick to view"));
        
        addGUIItem(gui, 12, Material.DIAMOND_PICKAXE, "§7§lAlpha Tools", 
            Arrays.asList("§7Legendary tools from the", "§7Alpha dimension", "", "§eClick to view"));
        
        addGUIItem(gui, 13, Material.GOLD_INGOT, "§6§lAlpha Accessories", 
            Arrays.asList("§7Legendary accessories from the", "§7Alpha dimension", "", "§eClick to view"));
        
        addGUIItem(gui, 14, Material.END_CRYSTAL, "§d§lAlpha Special", 
            Arrays.asList("§7Special items from the", "§7Alpha dimension", "", "§eClick to view"));
        
        // My alpha items
        List<AlphaItem> myAlphaItems = playerAlphaItems.getOrDefault(player.getUniqueId(), new ArrayList<>());
        addGUIItem(gui, 19, Material.CHEST, "§e§lMy Alpha Items", 
            Arrays.asList("§7View your alpha items", "§7• " + myAlphaItems.size() + " items owned", "", "§eClick to view"));
        
        // Alpha crafting
        addGUIItem(gui, 20, Material.CRAFTING_TABLE, "§9§lAlpha Crafting", 
            Arrays.asList("§7Craft alpha items", "§7• View recipes", "§7• Create items", "", "§eClick to craft"));
        
        // Alpha upgrades
        addGUIItem(gui, 21, Material.ANVIL, "§6§lAlpha Upgrades", 
            Arrays.asList("§7Upgrade your alpha items", "§7• Enhance properties", "§7• Increase power", "", "§eClick to upgrade"));
        
        // Close button
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", 
            Arrays.asList("§7Close alpha items", "", "§eClick to close"));
        
        player.openInventory(gui);
    }
    
    public void openAlphaItemCategoryGUI(Player player, AlphaItemType type) {
        Inventory gui = Bukkit.createInventory(null, 54, "§d§lAlpha Items - " + type.getDisplayName());
        
        List<AlphaItem> categoryItems = alphaItems.values().stream()
            .filter(item -> item.getType() == type)
            .toList();
        
        int slot = 10;
        for (AlphaItem item : categoryItems) {
            if (slot >= 44) break;
            
            ItemStack itemStack = createAlphaItemStack(item);
            gui.setItem(slot, itemStack);
            slot++;
            if (slot % 9 == 8) slot += 2;
        }
        
        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§l← Back", 
            Arrays.asList("§7Return to alpha items", "", "§eClick to go back"));
        
        player.openInventory(gui);
    }
    
    public void openMyAlphaItemsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§e§lMy Alpha Items"));
        
        List<AlphaItem> myAlphaItems = playerAlphaItems.getOrDefault(player.getUniqueId(), new ArrayList<>());
        
        int slot = 10;
        for (AlphaItem item : myAlphaItems) {
            if (slot >= 44) break;
            
            ItemStack itemStack = createAlphaItemStack(item);
            gui.setItem(slot, itemStack);
            slot++;
            if (slot % 9 == 8) slot += 2;
        }
        
        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§l← Back", 
            Arrays.asList("§7Return to alpha items", "", "§eClick to go back"));
        
        player.openInventory(gui);
    }
    
    public boolean craftAlphaItem(Player player, String itemId) {
        AlphaItem alphaItem = alphaItems.get(itemId);
        if (alphaItem == null) return false;
        
        // Check if player has required materials
        for (String material : alphaItem.getCraftingMaterials()) {
            // Parse material requirement
            String[] parts = material.replace("§7- ", "").split("x ");
            if (parts.length != 2) continue;
            
            int amount = Integer.parseInt(parts[0]);
            Material mat = Material.valueOf(parts[1].toUpperCase());
            
            if (!player.getInventory().contains(mat, amount)) {
                player.sendMessage("§cYou don't have enough " + mat.name() + "!");
                return false;
            }
        }
        
        // Remove materials
        for (String material : alphaItem.getCraftingMaterials()) {
            String[] parts = material.replace("§7- ", "").split("x ");
            if (parts.length != 2) continue;
            
            int amount = Integer.parseInt(parts[0]);
            Material mat = Material.valueOf(parts[1].toUpperCase());
            
            player.getInventory().removeItem(new ItemStack(mat, amount));
        }
        
        // Give alpha item
        giveAlphaItem(player, alphaItem);
        
        player.sendMessage(Component.text("§a§lALPHA ITEM CRAFTED!"));
        player.sendMessage("§7Item: " + alphaItem.getDisplayName());
        
        return true;
    }
    
    public void giveAlphaItem(Player player, AlphaItem alphaItem) {
        List<AlphaItem> myAlphaItems = playerAlphaItems.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        myAlphaItems.add(alphaItem);
        
        // Give physical item
        ItemStack itemStack = createAlphaItemStack(alphaItem);
        player.getInventory().addItem(itemStack);
    }
    
    public ItemStack createAlphaItemStack(AlphaItem alphaItem) {
        ItemStack item = new ItemStack(alphaItem.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(alphaItem.getDisplayName()));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Type: §eAlpha Item");
            lore.add("§7Rarity: " + alphaItem.getRarity().getDisplayName());
            lore.add("§7Category: " + alphaItem.getType().getDisplayName());
            lore.add("");
            lore.add("§7Description:");
            lore.add(alphaItem.getDescription());
            lore.add("");
            lore.add("§7Stats:");
            lore.addAll(alphaItem.getStats());
            lore.add("");
            lore.add("§7Abilities:");
            lore.addAll(alphaItem.getAbilities());
            lore.add("");
            lore.add("§d§lALPHA ITEM");
            
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    private void handleAlphaItemsGUIClick(Player player, int slot) {
        switch (slot) {
            case 10: // Alpha Weapons
                openAlphaItemCategoryGUI(player, AlphaItemType.WEAPON);
                break;
            case 11: // Alpha Armor
                openAlphaItemCategoryGUI(player, AlphaItemType.ARMOR);
                break;
            case 12: // Alpha Tools
                openAlphaItemCategoryGUI(player, AlphaItemType.TOOL);
                break;
            case 13: // Alpha Accessories
                openAlphaItemCategoryGUI(player, AlphaItemType.ACCESSORY);
                break;
            case 14: // Alpha Special
                openAlphaItemCategoryGUI(player, AlphaItemType.SPECIAL);
                break;
            case 19: // My Alpha Items
                openMyAlphaItemsGUI(player);
                break;
            case 20: // Alpha Crafting
                player.sendMessage(Component.text("§eAlpha Crafting coming soon!"));
                break;
            case 21: // Alpha Upgrades
                player.sendMessage(Component.text("§eAlpha Upgrades coming soon!"));
                break;
            case 49: // Close
                player.closeInventory();
                break;
        }
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, List<String> description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(description.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public AlphaItem getAlphaItem(String itemId) {
        return alphaItems.get(itemId);
    }
    
    public Map<String, AlphaItem> getAllAlphaItems() {
        return new HashMap<>(alphaItems);
    }
    
    public List<AlphaItem> getPlayerAlphaItems(UUID playerId) {
        return playerAlphaItems.getOrDefault(playerId, new ArrayList<>());
    }
    
    // Alpha Item Classes
    public static class AlphaItem {
        private final String id;
        private final String name;
        private final String displayName;
        private final Material material;
        private final AlphaItemRarity rarity;
        private final AlphaItemType type;
        private final String description;
        private final List<String> stats;
        private final List<String> craftingMaterials;
        private final List<String> abilities;
        
        public AlphaItem(String id, String name, String displayName, Material material,
                        AlphaItemRarity rarity, AlphaItemType type, String description,
                        List<String> stats, List<String> craftingMaterials, List<String> abilities) {
            this.id = id;
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.rarity = rarity;
            this.type = type;
            this.description = description;
            this.stats = stats;
            this.craftingMaterials = craftingMaterials;
            this.abilities = abilities;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public AlphaItemRarity getRarity() { return rarity; }
        public AlphaItemType getType() { return type; }
        public String getDescription() { return description; }
        public List<String> getStats() { return stats; }
        public List<String> getCraftingMaterials() { return craftingMaterials; }
        public List<String> getAbilities() { return abilities; }
    }
    
    public enum AlphaItemRarity {
        ALPHA("§d§lALPHA", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        AlphaItemRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public enum AlphaItemType {
        WEAPON("§cWeapon", "Legendary weapons from the Alpha dimension"),
        ARMOR("§bArmor", "Legendary armor from the Alpha dimension"),
        TOOL("§7Tool", "Legendary tools from the Alpha dimension"),
        ACCESSORY("§6Accessory", "Legendary accessories from the Alpha dimension"),
        SPECIAL("§dSpecial", "Special items from the Alpha dimension");
        
        private final String displayName;
        private final String description;
        
        AlphaItemType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
}
