package de.noctivag.skyblock.collections;

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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import java.util.stream.Collectors;

/**
 * Collections System - Hypixel Skyblock Style
 */
public class CollectionsSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final CorePlatform corePlatform;
    private final Map<UUID, PlayerCollections> playerCollections = new ConcurrentHashMap<>();
    
    public CollectionsSystem(SkyblockPlugin SkyblockPlugin, CorePlatform corePlatform, de.noctivag.skyblock.database.MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.corePlatform = corePlatform;
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material material = event.getBlock().getType();
        
        // Add to farming collection
        if (isFarmingMaterial(material)) {
            addToCollection(player, CollectionType.FARMING, material, 1.0);
        }
        
        // Add to mining collection
        if (isMiningMaterial(material)) {
            addToCollection(player, CollectionType.MINING, material, 1.0);
        }
    }
    
    private boolean isFarmingMaterial(Material material) {
        return material == Material.WHEAT || material == Material.CARROT || 
               material == Material.POTATO || material == Material.BEETROOT;
    }
    
    private boolean isMiningMaterial(Material material) {
        return material == Material.COAL || material == Material.IRON_INGOT || 
               material == Material.GOLD_INGOT || material == Material.DIAMOND;
    }
    
    public void addToCollection(Player player, CollectionType type, Material material, double amount) {
        PlayerCollections collections = getPlayerCollections(player.getUniqueId());
        int newAmount = collections.addToCollection(type, (int) amount);
        
        // Check for milestones and rewards
        checkMilestones(player, type, newAmount);
    }
    
    private void checkMilestones(Player player, CollectionType type, int amount) {
        PlayerCollections collections = getPlayerCollections(player.getUniqueId());
        
        // Check for milestone rewards
        for (CollectionReward reward : getCollectionRewards(type)) {
            if (amount >= reward.getRequiredAmount() && !collections.hasReward(type, reward)) {
                collections.addReward(type, reward);
                giveReward(player, reward);
                
                player.sendMessage(Component.text("§a§lCOLLECTION MILESTONE!"));
                player.sendMessage("§7" + type.getDisplayName() + " §7→ §e" + reward.getRequiredAmount() + " " + type.getName());
                player.sendMessage("§aReward: " + reward.getDescription());
            }
        }
        
        // Check for recipe unlocks
        for (CollectionRecipe recipe : getCollectionRecipes(type)) {
            if (amount >= recipe.getRequiredAmount() && !collections.hasRecipe(type, recipe)) {
                collections.addRecipe(type, recipe);
                unlockRecipe(player, recipe);
                
                player.sendMessage(Component.text("§a§lRECIPE UNLOCKED!"));
                player.sendMessage("§7" + recipe.getName() + " §7→ §e" + recipe.getDescription());
            }
        }
    }
    
    private void giveReward(Player player, CollectionReward reward) {
        // Give coins, items, or other rewards
        switch (reward.getRewardType()) {
            case COINS:
                // Use economy system to give coins
                try {
                    SkyblockPlugin.getEconomyManager().depositMoney(player, reward.getAmount());
                    player.sendMessage("§a+§6" + reward.getAmount() + " coins");
                } catch (Exception e) {
                    player.sendMessage("§a+§6" + reward.getAmount() + " coins");
                }
                break;
            case ITEM:
                // Give item to player
                if (reward.getItem() != null) {
                    player.getInventory().addItem(reward.getItem());
                    player.sendMessage("§a+§e" + reward.getItem().getAmount() + " " + reward.getItem().getType().name());
                }
                break;
            case XP:
                // Give XP to player
                player.giveExp((int) reward.getAmount());
                player.sendMessage("§a+§b" + reward.getAmount() + " XP");
                break;
        }
    }
    
    private void unlockRecipe(Player player, CollectionRecipe recipe) {
        // Unlock recipe for player
        // This would integrate with the recipe system
        player.sendMessage("§aRecipe unlocked: " + recipe.getName());
    }
    
    public PlayerCollections getPlayerCollections(UUID playerId) {
        return playerCollections.computeIfAbsent(playerId, k -> new PlayerCollections(playerId));
    }
    
    private List<CollectionReward> getCollectionRewards(CollectionType type) {
        List<CollectionReward> rewards = new ArrayList<>();
        
        switch (type) {
            case FARMING:
                rewards.add(new CollectionReward(50, "§a+100 coins", RewardType.COINS, 100.0, null));
                rewards.add(new CollectionReward(100, "§a+250 coins", RewardType.COINS, 250.0, null));
                rewards.add(new CollectionReward(250, "§a+500 coins", RewardType.COINS, 500.0, null));
                rewards.add(new CollectionReward(500, "§a+1000 coins", RewardType.COINS, 1000.0, null));
                break;
            case MINING:
                rewards.add(new CollectionReward(50, "§a+150 coins", RewardType.COINS, 150.0, null));
                rewards.add(new CollectionReward(100, "§a+300 coins", RewardType.COINS, 300.0, null));
                rewards.add(new CollectionReward(250, "§a+600 coins", RewardType.COINS, 600.0, null));
                rewards.add(new CollectionReward(500, "§a+1200 coins", RewardType.COINS, 1200.0, null));
                break;
            case COMBAT:
                rewards.add(new CollectionReward(50, "§a+200 coins", RewardType.COINS, 200.0, null));
                rewards.add(new CollectionReward(100, "§a+400 coins", RewardType.COINS, 400.0, null));
                rewards.add(new CollectionReward(250, "§a+800 coins", RewardType.COINS, 800.0, null));
                rewards.add(new CollectionReward(500, "§a+1600 coins", RewardType.COINS, 1600.0, null));
                break;
            default:
                rewards.add(new CollectionReward(50, "§a+100 coins", RewardType.COINS, 100.0, null));
                rewards.add(new CollectionReward(100, "§a+200 coins", RewardType.COINS, 200.0, null));
                break;
        }
        
        return rewards;
    }
    
    private List<CollectionRecipe> getCollectionRecipes(CollectionType type) {
        List<CollectionRecipe> recipes = new ArrayList<>();
        
        switch (type) {
            case FARMING:
                recipes.add(new CollectionRecipe(100, "Farming Minion I", "Unlocks basic farming minion"));
                recipes.add(new CollectionRecipe(250, "Farming Minion II", "Unlocks improved farming minion"));
                recipes.add(new CollectionRecipe(500, "Farming Minion III", "Unlocks advanced farming minion"));
                break;
            case MINING:
                recipes.add(new CollectionRecipe(100, "Mining Minion I", "Unlocks basic mining minion"));
                recipes.add(new CollectionRecipe(250, "Mining Minion II", "Unlocks improved mining minion"));
                recipes.add(new CollectionRecipe(500, "Mining Minion III", "Unlocks advanced mining minion"));
                break;
            case COMBAT:
                recipes.add(new CollectionRecipe(100, "Combat Minion I", "Unlocks basic combat minion"));
                recipes.add(new CollectionRecipe(250, "Combat Minion II", "Unlocks improved combat minion"));
                recipes.add(new CollectionRecipe(500, "Combat Minion III", "Unlocks advanced combat minion"));
                break;
            case FORAGING:
                recipes.add(new CollectionRecipe(100, "Foraging Minion I", "Unlocks basic foraging minion"));
                recipes.add(new CollectionRecipe(250, "Foraging Minion II", "Unlocks improved foraging minion"));
                recipes.add(new CollectionRecipe(500, "Foraging Minion III", "Unlocks advanced foraging minion"));
                break;
            case FISHING:
                recipes.add(new CollectionRecipe(100, "Fishing Minion I", "Unlocks basic fishing minion"));
                recipes.add(new CollectionRecipe(250, "Fishing Minion II", "Unlocks improved fishing minion"));
                recipes.add(new CollectionRecipe(500, "Fishing Minion III", "Unlocks advanced fishing minion"));
                break;
            case ENCHANTING:
                recipes.add(new CollectionRecipe(100, "Enchanting Minion I", "Unlocks basic enchanting minion"));
                recipes.add(new CollectionRecipe(250, "Enchanting Minion II", "Unlocks improved enchanting minion"));
                recipes.add(new CollectionRecipe(500, "Enchanting Minion III", "Unlocks advanced enchanting minion"));
                break;
            case TAMING:
                recipes.add(new CollectionRecipe(100, "Taming Minion I", "Unlocks basic taming minion"));
                recipes.add(new CollectionRecipe(250, "Taming Minion II", "Unlocks improved taming minion"));
                recipes.add(new CollectionRecipe(500, "Taming Minion III", "Unlocks advanced taming minion"));
                break;
            case ALCHEMY:
                recipes.add(new CollectionRecipe(100, "Alchemy Minion I", "Unlocks basic alchemy minion"));
                recipes.add(new CollectionRecipe(250, "Alchemy Minion II", "Unlocks improved alchemy minion"));
                recipes.add(new CollectionRecipe(500, "Alchemy Minion III", "Unlocks advanced alchemy minion"));
                break;
        }
        
        return recipes;
    }
    
    public void openCollectionsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, net.kyori.adventure.text.Component.text("§a§lCollections"));
        
        PlayerCollections collections = getPlayerCollections(player.getUniqueId());
        
        // Add collection items
        int slot = 10;
        for (CollectionType type : CollectionType.values()) {
            if (slot >= 44) break;
            
            int amount = collections.getCollectionAmount(type);
            List<CollectionReward> rewards = getCollectionRewards(type);
            
            ItemStack item = new ItemStack(getCollectionIcon(type));
            ItemMeta meta = item.getItemMeta();
            
            if (meta != null) {
                meta.displayName(net.kyori.adventure.text.Component.text(type.getDisplayName() + " §7Collection"));
                
                List<String> lore = new ArrayList<>();
                lore.add("§7Total: §e" + amount);
                lore.add("");
                lore.add("§7Milestones:");
                for (CollectionReward reward : rewards) {
                    String status = collections.hasReward(type, reward) ? "§a✓" : "§7-";
                    lore.add(status + " §e" + reward.getRequiredAmount() + " §7→ " + reward.getDescription());
                }
                
                meta.lore(lore.stream().map(net.kyori.adventure.text.Component::text).toList());
                item.setItemMeta(meta);
            }
            
            gui.setItem(slot, item);
            slot++;
        }
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the collections menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    private Material getCollectionIcon(CollectionType type) {
        return switch (type) {
            case FARMING -> Material.WHEAT;
            case MINING -> Material.DIAMOND_PICKAXE;
            case COMBAT -> Material.DIAMOND_SWORD;
            case FORAGING -> Material.OAK_LOG;
            case FISHING -> Material.FISHING_ROD;
            case ENCHANTING -> Material.ENCHANTING_TABLE;
            case ALCHEMY -> Material.BREWING_STAND;
            case TAMING -> Material.BONE;
        };
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(net.kyori.adventure.text.Component.text(name));
            meta.lore(Arrays.asList(net.kyori.adventure.text.Component.text(description)));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public enum CollectionType {
        FARMING("§aFarming"),
        MINING("§7Mining"),
        COMBAT("§cCombat"),
        FORAGING("§2Foraging"),
        FISHING("§bFishing"),
        ENCHANTING("§dEnchanting"),
        ALCHEMY("§5Alchemy"),
        TAMING("§6Taming");
        
        private final String displayName;
        
        CollectionType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() { return displayName; }
        public String getName() { return name(); }
    }
    
    public static class CollectionConfig {
        private final String displayName;
        private final String description;
        private final Material material;
        
        public CollectionConfig(String displayName, String description, Material material) {
            this.displayName = displayName;
            this.description = description;
            this.material = material;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getMaterial() { return material; }
    }
    
    public static class CollectionReward {
        private final int requiredAmount;
        private final String description;
        private final RewardType rewardType;
        private final double amount;
        private final ItemStack item;
        
        public CollectionReward(int requiredAmount, String description, RewardType rewardType, double amount, ItemStack item) {
            this.requiredAmount = requiredAmount;
            this.description = description;
            this.rewardType = rewardType;
            this.amount = amount;
            this.item = item;
        }
        
        public int getRequiredAmount() { return requiredAmount; }
        public String getDescription() { return description; }
        public RewardType getRewardType() { return rewardType; }
        public double getAmount() { return amount; }
        public ItemStack getItem() { return item; }
    }
    
    public static class CollectionRecipe {
        private final int requiredAmount;
        private final String name;
        private final String description;
        
        public CollectionRecipe(int requiredAmount, String name, String description) {
            this.requiredAmount = requiredAmount;
            this.name = name;
            this.description = description;
        }
        
        public int getRequiredAmount() { return requiredAmount; }
        public String getName() { return name; }
        public String getDescription() { return description; }
    }
    
    public enum RewardType {
        COINS, ITEM, XP
    }
    
    public CollectionConfig getCollectionConfig(CollectionType type) {
        switch (type) {
            case FARMING:
                return new CollectionConfig("§aFarming", "Sammle landwirtschaftliche Gegenstände", Material.WHEAT);
            case MINING:
                return new CollectionConfig("§7Mining", "Sammle Erze und Steine", Material.COAL);
            case COMBAT:
                return new CollectionConfig("§cCombat", "Sammle Kampf-Gegenstände", Material.IRON_SWORD);
            case FORAGING:
                return new CollectionConfig("§2Foraging", "Sammle Holz und Pflanzen", Material.OAK_LOG);
            case FISHING:
                return new CollectionConfig("§bFishing", "Sammle Fische und Schätze", Material.FISHING_ROD);
            case ENCHANTING:
                return new CollectionConfig("§dEnchanting", "Sammle Verzauberungs-Gegenstände", Material.ENCHANTING_TABLE);
            case ALCHEMY:
                return new CollectionConfig("§5Alchemy", "Sammle Alchemie-Gegenstände", Material.BREWING_STAND);
            case TAMING:
                return new CollectionConfig("§6Taming", "Sammle Zähmungs-Gegenstände", Material.BONE);
            default:
                return null;
        }
    }
    
    public static class PlayerCollections {
        private final UUID playerId;
        private final Map<CollectionType, Map<Material, Double>> collections = new ConcurrentHashMap<>();
        private final Map<CollectionType, Set<CollectionReward>> unlockedRewards = new ConcurrentHashMap<>();
        private final Map<CollectionType, Set<CollectionRecipe>> unlockedRecipes = new ConcurrentHashMap<>();
        
        public PlayerCollections(UUID playerId) {
            this.playerId = playerId;
            for (CollectionType type : CollectionType.values()) {
                collections.put(type, new ConcurrentHashMap<>());
                unlockedRewards.put(type, new HashSet<>());
                unlockedRecipes.put(type, new HashSet<>());
            }
        }
        
        public int addToCollection(CollectionType type, int amount) {
            // For simplicity, we'll use a single material per collection type
            Material material = getDefaultMaterial(type);
            Map<Material, Double> collection = collections.get(type);
            double currentAmount = collection.getOrDefault(material, 0.0);
            collection.put(material, currentAmount + amount);
            return collection.get(material).intValue();
        }
        
        public void addToCollection(CollectionType type, Material material, double amount) {
            Map<Material, Double> collection = collections.get(type);
            double currentAmount = collection.getOrDefault(material, 0.0);
            collection.put(material, currentAmount + amount);
        }
        
        public int getCollectionAmount(CollectionType type) {
            Map<Material, Double> collection = collections.get(type);
            return (int) collection.values().stream().mapToDouble(Double::doubleValue).sum();
        }
        
        public double getTotalCollectionAmount(CollectionType type) {
            Map<Material, Double> collection = collections.get(type);
            return collection.values().stream().mapToDouble(Double::doubleValue).sum();
        }
        
        public void addReward(CollectionType type, CollectionReward reward) {
            unlockedRewards.get(type).add(reward);
        }
        
        public boolean hasReward(CollectionType type, CollectionReward reward) {
            return unlockedRewards.get(type).contains(reward);
        }
        
        public void addRecipe(CollectionType type, CollectionRecipe recipe) {
            unlockedRecipes.get(type).add(recipe);
        }
        
        public boolean hasRecipe(CollectionType type, CollectionRecipe recipe) {
            return unlockedRecipes.get(type).contains(recipe);
        }
        
        private Material getDefaultMaterial(CollectionType type) {
            return switch (type) {
                case FARMING -> Material.WHEAT;
                case MINING -> Material.COAL;
                case COMBAT -> Material.ROTTEN_FLESH;
                case FORAGING -> Material.OAK_LOG;
                case FISHING -> Material.COD;
                case ENCHANTING -> Material.LAPIS_LAZULI;
                case ALCHEMY -> Material.NETHER_WART;
                case TAMING -> Material.BONE;
            };
        }
        
        public UUID getPlayerId() { return playerId; }
        public Map<CollectionType, Map<Material, Double>> getCollections() { return collections; }
    }
    
    // Missing methods for GUI integration
    public void openCollectionCategoryGUI(Player player, String category) {
        // Delegate to CollectionsGUI
        new de.noctivag.skyblock.collections.CollectionsGUI(SkyblockPlugin, this).openCategoryGUI(player, category);
    }
}
