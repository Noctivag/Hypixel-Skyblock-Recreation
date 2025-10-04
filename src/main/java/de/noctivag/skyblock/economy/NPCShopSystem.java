package de.noctivag.skyblock.economy;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.core.PlayerProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;

/**
 * NPC Shop System - NPC-basierte Shops mit verschiedenen Kategorien
 * 
 * Verantwortlich für:
 * - NPC Shop Management
 * - Shop Categories
 * - Dynamic Pricing
 * - Stock Management
 * - Shop Interactions
 * - Quest Integration
 */
public class NPCShopSystem {
    private final EconomySystem economySystem;
    private final Map<String, NPCShop> shops = new HashMap<>();
    private final Map<String, ShopCategory> categories = new HashMap<>();
    private final Map<String, List<ShopItem>> shopItems = new HashMap<>();
    
    public NPCShopSystem(EconomySystem economySystem) {
        this.economySystem = economySystem;
        initializeShops();
        initializeCategories();
        initializeShopItems();
    }
    
    private void initializeShops() {
        // Create different NPC shops
        shops.put("mining_shop", new NPCShop("mining_shop", "§6§lMining Shop", "§7Tools and materials for mining"));
        shops.put("farming_shop", new NPCShop("farming_shop", "§a§lFarming Shop", "§7Seeds and farming tools"));
        shops.put("combat_shop", new NPCShop("combat_shop", "§c§lCombat Shop", "§7Weapons and armor"));
        shops.put("magic_shop", new NPCShop("magic_shop", "§d§lMagic Shop", "§7Enchantments and magical items"));
        shops.put("general_shop", new NPCShop("general_shop", "§f§lGeneral Shop", "§7Basic items and supplies"));
        shops.put("rare_shop", new NPCShop("rare_shop", "§5§lRare Shop", "§7Rare and legendary items"));
        shops.put("food_shop", new NPCShop("food_shop", "§e§lFood Shop", "§7Food and consumables"));
        shops.put("building_shop", new NPCShop("building_shop", "§7§lBuilding Shop", "§7Building materials and blocks"));
    }
    
    private void initializeCategories() {
        categories.put("tools", new ShopCategory("tools", "§6§lTools", Material.DIAMOND_PICKAXE));
        categories.put("weapons", new ShopCategory("weapons", "§c§lWeapons", Material.DIAMOND_SWORD));
        categories.put("armor", new ShopCategory("armor", "§b§lArmor", Material.DIAMOND_CHESTPLATE));
        categories.put("materials", new ShopCategory("materials", "§e§lMaterials", Material.DIAMOND));
        categories.put("food", new ShopCategory("food", "§a§lFood", Material.BREAD));
        categories.put("blocks", new ShopCategory("blocks", "§7§lBlocks", Material.STONE));
        categories.put("magic", new ShopCategory("magic", "§d§lMagic", Material.ENCHANTING_TABLE));
        categories.put("rare", new ShopCategory("rare", "§5§lRare", Material.NETHER_STAR));
    }
    
    private void initializeShopItems() {
        // Mining Shop Items
        List<ShopItem> miningItems = new ArrayList<>();
        miningItems.add(new ShopItem("DIAMOND_PICKAXE", 1, 500.0, 100, "§6§lDiamond Pickaxe", Arrays.asList("§7Efficient mining tool", "§7Durability: 1561")));
        miningItems.add(new ShopItem("IRON_PICKAXE", 1, 100.0, 200, "§f§lIron Pickaxe", Arrays.asList("§7Good mining tool", "§7Durability: 250")));
        miningItems.add(new ShopItem("STONE_PICKAXE", 1, 20.0, 500, "§7§lStone Pickaxe", Arrays.asList("§7Basic mining tool", "§7Durability: 131")));
        miningItems.add(new ShopItem("COAL", 64, 128.0, 1000, "§8§lCoal", Arrays.asList("§7Fuel for furnaces", "§7Stack of 64")));
        miningItems.add(new ShopItem("IRON_INGOT", 32, 160.0, 500, "§f§lIron Ingot", Arrays.asList("§7Refined iron", "§7Stack of 32")));
        miningItems.add(new ShopItem("GOLD_INGOT", 16, 128.0, 300, "§6§lGold Ingot", Arrays.asList("§7Refined gold", "§7Stack of 16")));
        miningItems.add(new ShopItem("DIAMOND", 8, 120.0, 100, "§b§lDiamond", Arrays.asList("§7Rare gemstone", "§7Stack of 8")));
        miningItems.add(new ShopItem("EMERALD", 4, 48.0, 50, "§a§lEmerald", Arrays.asList("§7Village currency", "§7Stack of 4")));
        shopItems.put("mining_shop", miningItems);
        
        // Farming Shop Items
        List<ShopItem> farmingItems = new ArrayList<>();
        farmingItems.add(new ShopItem("WHEAT_SEEDS", 64, 32.0, 1000, "§e§lWheat Seeds", Arrays.asList("§7Plant to grow wheat", "§7Stack of 64")));
        farmingItems.add(new ShopItem("CARROT", 64, 32.0, 1000, "§6§lCarrot", Arrays.asList("§7Nutritious vegetable", "§7Stack of 64")));
        farmingItems.add(new ShopItem("POTATO", 64, 32.0, 1000, "§e§lPotato", Arrays.asList("§7Versatile crop", "§7Stack of 64")));
        farmingItems.add(new ShopItem("BEETROOT_SEEDS", 64, 32.0, 1000, "§c§lBeetroot Seeds", Arrays.asList("§7Plant to grow beetroot", "§7Stack of 64")));
        farmingItems.add(new ShopItem("SUGAR_CANE", 64, 64.0, 500, "§a§lSugar Cane", Arrays.asList("§7Sweet plant", "§7Stack of 64")));
        farmingItems.add(new ShopItem("CACTUS", 32, 32.0, 300, "§a§lCactus", Arrays.asList("§7Desert plant", "§7Stack of 32")));
        farmingItems.add(new ShopItem("PUMPKIN_SEEDS", 32, 16.0, 200, "§6§lPumpkin Seeds", Arrays.asList("§7Plant to grow pumpkins", "§7Stack of 32")));
        farmingItems.add(new ShopItem("MELON_SEEDS", 32, 16.0, 200, "§a§lMelon Seeds", Arrays.asList("§7Plant to grow melons", "§7Stack of 32")));
        shopItems.put("farming_shop", farmingItems);
        
        // Combat Shop Items
        List<ShopItem> combatItems = new ArrayList<>();
        combatItems.add(new ShopItem("DIAMOND_SWORD", 1, 500.0, 50, "§b§lDiamond Sword", Arrays.asList("§7Sharp weapon", "§7Damage: 7", "§7Durability: 1561")));
        combatItems.add(new ShopItem("IRON_SWORD", 1, 100.0, 100, "§f§lIron Sword", Arrays.asList("§7Good weapon", "§7Damage: 6", "§7Durability: 250")));
        combatItems.add(new ShopItem("STONE_SWORD", 1, 20.0, 200, "§7§lStone Sword", Arrays.asList("§7Basic weapon", "§7Damage: 5", "§7Durability: 131")));
        combatItems.add(new ShopItem("DIAMOND_CHESTPLATE", 1, 800.0, 25, "§b§lDiamond Chestplate", Arrays.asList("§7Strong armor", "§7Protection: 8", "§7Durability: 528")));
        combatItems.add(new ShopItem("IRON_CHESTPLATE", 1, 200.0, 50, "§f§lIron Chestplate", Arrays.asList("§7Good armor", "§7Protection: 6", "§7Durability: 240")));
        combatItems.add(new ShopItem("LEATHER_CHESTPLATE", 1, 50.0, 100, "§6§lLeather Chestplate", Arrays.asList("§7Basic armor", "§7Protection: 3", "§7Durability: 80")));
        combatItems.add(new ShopItem("BOW", 1, 100.0, 100, "§6§lBow", Arrays.asList("§7Ranged weapon", "§7Durability: 384")));
        combatItems.add(new ShopItem("ARROW", 64, 32.0, 1000, "§7§lArrow", Arrays.asList("§7Ammunition", "§7Stack of 64")));
        shopItems.put("combat_shop", combatItems);
        
        // Magic Shop Items
        List<ShopItem> magicItems = new ArrayList<>();
        magicItems.add(new ShopItem("ENCHANTING_TABLE", 1, 1000.0, 10, "§d§lEnchanting Table", Arrays.asList("§7Enchant your items", "§7Rare magical item")));
        magicItems.add(new ShopItem("EXPERIENCE_BOTTLE", 16, 200.0, 100, "§a§lExperience Bottle", Arrays.asList("§7Grants experience", "§7Stack of 16")));
        magicItems.add(new ShopItem("ENDER_PEARL", 8, 80.0, 50, "§5§lEnder Pearl", Arrays.asList("§7Teleportation item", "§7Stack of 8")));
        magicItems.add(new ShopItem("BLAZE_POWDER", 16, 64.0, 100, "§c§lBlaze Powder", Arrays.asList("§7Magical ingredient", "§7Stack of 16")));
        magicItems.add(new ShopItem("GHAST_TEAR", 4, 100.0, 25, "§f§lGhast Tear", Arrays.asList("§7Magical ingredient", "§7Stack of 4")));
        magicItems.add(new ShopItem("MAGMA_CREAM", 16, 80.0, 100, "§6§lMagma Cream", Arrays.asList("§7Magical ingredient", "§7Stack of 16")));
        magicItems.add(new ShopItem("FERMENTED_SPIDER_EYE", 8, 40.0, 100, "§8§lFermented Spider Eye", Arrays.asList("§7Magical ingredient", "§7Stack of 8")));
        magicItems.add(new ShopItem("GUNPOWDER", 32, 64.0, 200, "§7§lGunpowder", Arrays.asList("§7Explosive ingredient", "§7Stack of 32")));
        shopItems.put("magic_shop", magicItems);
        
        // General Shop Items
        List<ShopItem> generalItems = new ArrayList<>();
        generalItems.add(new ShopItem("BREAD", 64, 32.0, 1000, "§e§lBread", Arrays.asList("§7Basic food", "§7Stack of 64")));
        generalItems.add(new ShopItem("COOKED_BEEF", 32, 64.0, 500, "§c§lCooked Beef", Arrays.asList("§7Nutritious food", "§7Stack of 32")));
        generalItems.add(new ShopItem("COOKED_PORKCHOP", 32, 64.0, 500, "§d§lCooked Porkchop", Arrays.asList("§7Nutritious food", "§7Stack of 32")));
        generalItems.add(new ShopItem("COOKED_CHICKEN", 32, 48.0, 500, "§f§lCooked Chicken", Arrays.asList("§7Light food", "§7Stack of 32")));
        generalItems.add(new ShopItem("APPLE", 32, 16.0, 1000, "§c§lApple", Arrays.asList("§7Healthy fruit", "§7Stack of 32")));
        generalItems.add(new ShopItem("GOLDEN_APPLE", 4, 200.0, 50, "§6§lGolden Apple", Arrays.asList("§7Magical fruit", "§7Stack of 4")));
        generalItems.add(new ShopItem("WATER_BUCKET", 1, 10.0, 100, "§b§lWater Bucket", Arrays.asList("§7Portable water", "§7Durability: 1")));
        generalItems.add(new ShopItem("LAVA_BUCKET", 1, 50.0, 50, "§c§lLava Bucket", Arrays.asList("§7Portable lava", "§7Durability: 1")));
        shopItems.put("general_shop", generalItems);
        
        // Rare Shop Items
        List<ShopItem> rareItems = new ArrayList<>();
        rareItems.add(new ShopItem("NETHER_STAR", 1, 5000.0, 5, "§5§lNether Star", Arrays.asList("§7Legendary item", "§7Used for beacons")));
        rareItems.add(new ShopItem("DRAGON_EGG", 1, 10000.0, 1, "§d§lDragon Egg", Arrays.asList("§7Ultra rare item", "§7From the Ender Dragon")));
        rareItems.add(new ShopItem("ELYTRA", 1, 3000.0, 10, "§b§lElytra", Arrays.asList("§7Wings for flying", "§7Durability: 432")));
        rareItems.add(new ShopItem("TOTEM_OF_UNDYING", 1, 2000.0, 15, "§6§lTotem of Undying", Arrays.asList("§7Prevents death", "§7Single use")));
        rareItems.add(new ShopItem("SHULKER_SHELL", 2, 1000.0, 20, "§d§lShulker Shell", Arrays.asList("§7Rare material", "§7Stack of 2")));
        rareItems.add(new ShopItem("HEART_OF_THE_SEA", 1, 1500.0, 10, "§b§lHeart of the Sea", Arrays.asList("§7Ocean treasure", "§7Used for conduits")));
        rareItems.add(new ShopItem("NAUTILUS_SHELL", 4, 800.0, 25, "§f§lNautilus Shell", Arrays.asList("§7Ocean treasure", "§7Stack of 4")));
        rareItems.add(new ShopItem("TRIDENT", 1, 2500.0, 8, "§b§lTrident", Arrays.asList("§7Ocean weapon", "§7Durability: 250")));
        shopItems.put("rare_shop", rareItems);
        
        // Food Shop Items
        List<ShopItem> foodItems = new ArrayList<>();
        foodItems.add(new ShopItem("GOLDEN_CARROT", 16, 200.0, 100, "§6§lGolden Carrot", Arrays.asList("§7Magical food", "§7Stack of 16")));
        foodItems.add(new ShopItem("GOLDEN_APPLE", 8, 400.0, 50, "§6§lGolden Apple", Arrays.asList("§7Magical fruit", "§7Stack of 8")));
        foodItems.add(new ShopItem("ENCHANTED_GOLDEN_APPLE", 2, 2000.0, 10, "§6§lEnchanted Golden Apple", Arrays.asList("§7Ultra magical fruit", "§7Stack of 2")));
        foodItems.add(new ShopItem("COOKED_SALMON", 32, 48.0, 200, "§c§lCooked Salmon", Arrays.asList("§7Ocean food", "§7Stack of 32")));
        foodItems.add(new ShopItem("COOKED_COD", 32, 32.0, 300, "§f§lCooked Cod", Arrays.asList("§7Ocean food", "§7Stack of 32")));
        foodItems.add(new ShopItem("COOKED_MUTTON", 32, 48.0, 200, "§e§lCooked Mutton", Arrays.asList("§7Mountain food", "§7Stack of 32")));
        foodItems.add(new ShopItem("COOKED_RABBIT", 32, 32.0, 300, "§f§lCooked Rabbit", Arrays.asList("§7Forest food", "§7Stack of 32")));
        foodItems.add(new ShopItem("PUMPKIN_PIE", 16, 24.0, 500, "§6§lPumpkin Pie", Arrays.asList("§7Sweet dessert", "§7Stack of 16")));
        shopItems.put("food_shop", foodItems);
        
        // Building Shop Items
        List<ShopItem> buildingItems = new ArrayList<>();
        buildingItems.add(new ShopItem("STONE", 64, 32.0, 1000, "§7§lStone", Arrays.asList("§7Basic building block", "§7Stack of 64")));
        buildingItems.add(new ShopItem("COBBLESTONE", 64, 16.0, 1000, "§8§lCobblestone", Arrays.asList("§7Basic building block", "§7Stack of 64")));
        buildingItems.add(new ShopItem("OAK_PLANKS", 64, 32.0, 1000, "§6§lOak Planks", Arrays.asList("§7Wooden building block", "§7Stack of 64")));
        buildingItems.add(new ShopItem("BRICKS", 64, 48.0, 500, "§c§lBricks", Arrays.asList("§7Decorative block", "§7Stack of 64")));
        buildingItems.add(new ShopItem("GLASS", 32, 16.0, 1000, "§f§lGlass", Arrays.asList("§7Transparent block", "§7Stack of 32")));
        buildingItems.add(new ShopItem("OBSIDIAN", 8, 400.0, 50, "§8§lObsidian", Arrays.asList("§7Strong block", "§7Stack of 8")));
        buildingItems.add(new ShopItem("IRON_BLOCK", 4, 200.0, 100, "§f§lIron Block", Arrays.asList("§7Metal block", "§7Stack of 4")));
        buildingItems.add(new ShopItem("GOLD_BLOCK", 2, 400.0, 50, "§6§lGold Block", Arrays.asList("§7Precious block", "§7Stack of 2")));
        shopItems.put("building_shop", buildingItems);
    }
    
    public void openShop(Player player, String shopId) {
        NPCShop shop = shops.get(shopId);
        if (shop == null) return;
        
        List<ShopItem> items = shopItems.get(shopId);
        if (items == null) return;
        
        // Create shop GUI
        org.bukkit.inventory.Inventory inventory = org.bukkit.Bukkit.createInventory(null, 54, shop.getDisplayName());
        
        // Add items to inventory
        for (int i = 0; i < items.size() && i < 45; i++) {
            ShopItem item = items.get(i);
            ItemStack itemStack = createShopItemStack(item);
            inventory.setItem(i, itemStack);
        }
        
        // Add navigation items
        ItemStack backButton = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backButton.getItemMeta();
        if (backMeta != null) {
            backMeta.displayName(Component.text("§7§lBack"));
            backButton.setItemMeta(backMeta);
        }
        inventory.setItem(49, backButton);
        
        player.openInventory(inventory);
    }
    
    private ItemStack createShopItemStack(ShopItem item) {
        Material material = Material.valueOf(item.getItemId());
        ItemStack itemStack = new ItemStack(material, item.getAmount());
        ItemMeta meta = itemStack.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(item.getDisplayName()));
            
            List<String> lore = new ArrayList<>(item.getDescription());
            lore.add("");
            lore.add("§7Price: §6" + item.getPrice() + " coins");
            lore.add("§7Stock: §e" + item.getStock());
            lore.add("");
            lore.add("§eLeft-click to buy!");
            
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            itemStack.setItemMeta(meta);
        }
        
        return itemStack;
    }
    
    public boolean buyItem(Player player, String shopId, String itemId, int amount) {
        List<ShopItem> items = shopItems.get(shopId);
        if (items == null) return false;
        
        ShopItem item = null;
        for (ShopItem i : items) {
            if (i.getItemId().equals(itemId)) {
                item = i;
                break;
            }
        }
        
        if (item == null) return false;
        if (item.getStock() < amount) return false;
        
        double totalCost = item.getPrice() * amount;
        PlayerProfile profile = economySystem.corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null || !profile.tryRemoveCoins(totalCost)) return false;
        
        // Give item to player
        Material material = Material.valueOf(itemId);
        player.getInventory().addItem(new ItemStack(material, amount));
        
        // Update stock
        item.setStock(item.getStock() - amount);
        
        player.sendMessage("§a§lITEM PURCHASED!");
        player.sendMessage("§7Item: §e" + item.getDisplayName());
        player.sendMessage("§7Amount: §e" + amount);
        player.sendMessage("§7Total Cost: §6" + totalCost + " coins");
        
        return true;
    }
    
    public Map<String, NPCShop> getShops() {
        return new HashMap<>(shops);
    }
    
    public Map<String, ShopCategory> getCategories() {
        return new HashMap<>(categories);
    }
    
    public Map<String, List<ShopItem>> getShopItems() {
        return new HashMap<>(shopItems);
    }
    
    // NPC Shop Class
    public static class NPCShop {
        private final String id;
        private final String displayName;
        private final String description;
        
        public NPCShop(String id, String displayName, String description) {
            this.id = id;
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getId() { return id; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    // Shop Category Class
    public static class ShopCategory {
        private final String id;
        private final String displayName;
        private final Material icon;
        
        public ShopCategory(String id, String displayName, Material icon) {
            this.id = id;
            this.displayName = displayName;
            this.icon = icon;
        }
        
        public String getId() { return id; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
    }
    
    // Shop Item Class
    public static class ShopItem {
        private final String itemId;
        private final int amount;
        private final double price;
        private int stock;
        private final String displayName;
        private final List<String> description;
        
        public ShopItem(String itemId, int amount, double price, int stock, String displayName, List<String> description) {
            this.itemId = itemId;
            this.amount = amount;
            this.price = price;
            this.stock = stock;
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getItemId() { return itemId; }
        public int getAmount() { return amount; }
        public double getPrice() { return price; }
        public int getStock() { return stock; }
        public String getDisplayName() { return displayName; }
        public List<String> getDescription() { return description; }
        
        public void setStock(int stock) { this.stock = stock; }
    }
}
