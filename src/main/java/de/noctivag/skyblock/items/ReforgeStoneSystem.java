package de.noctivag.skyblock.items;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.CorePlatform;
import de.noctivag.skyblock.core.PlayerProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Reforge Stone System - Hypixel Skyblock Style
 * 
 * Features:
 * - Reforge Stone Types (Sharp, Heavy, Light, Wise, Pure, Fierce, etc.)
 * - Reforge Stone Integration with Reforge System
 * - Stat Modification System
 * - Reforge Costs and Risks
 * - Reforge Stone Crafting
 * - Reforge Stone Trading
 */
public class ReforgeStoneSystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final CorePlatform corePlatform;
    private final ReforgeSystem reforgeSystem;
    private final Map<String, ReforgeStone> reforgeStones = new HashMap<>();
    private final Map<UUID, Map<String, Integer>> playerReforgeStones = new ConcurrentHashMap<>();
    
    public ReforgeStoneSystem(SkyblockPlugin SkyblockPlugin, CorePlatform corePlatform, ReforgeSystem reforgeSystem) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.corePlatform = corePlatform;
        this.reforgeSystem = reforgeSystem;
        initializeReforgeStones();
    }
    
    private void initializeReforgeStones() {
        // Weapon Reforge Stones
        createReforgeStone("SHARP_STONE", "Sharp Reforge Stone", Material.DIAMOND_SWORD, 
            ReforgeSystem.ReforgeType.SHARP, 1000, 0.8, 
            Arrays.asList("§7Increases damage and critical chance.", "§7Success Rate: 80%"),
            Arrays.asList("§7- 1x Diamond", "§7- 1x Iron Ingot", "§7- 1x Coal"));
        
        createReforgeStone("HEAVY_STONE", "Heavy Reforge Stone", Material.IRON_SWORD,
            ReforgeSystem.ReforgeType.HEAVY, 800, 0.7,
            Arrays.asList("§7Increases damage and defense.", "§7Success Rate: 70%"),
            Arrays.asList("§7- 1x Iron Ingot", "§7- 1x Coal", "§7- 1x Stone"));
        
        createReforgeStone("LIGHT_STONE", "Light Reforge Stone", Material.GOLDEN_SWORD,
            ReforgeSystem.ReforgeType.LIGHT, 1200, 0.9,
            Arrays.asList("§7Increases speed and critical chance.", "§7Success Rate: 90%"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Feather", "§7- 1x String"));
        
        createReforgeStone("WISE_STONE", "Wise Reforge Stone", Material.ENCHANTED_BOOK,
            ReforgeSystem.ReforgeType.WISE, 1500, 0.6,
            Arrays.asList("§7Increases intelligence and mana.", "§7Success Rate: 60%"),
            Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Lapis Lazuli", "§7- 1x Redstone"));
        
        createReforgeStone("PURE_STONE", "Pure Reforge Stone", Material.DIAMOND,
            ReforgeSystem.ReforgeType.PURE, 2000, 0.5,
            Arrays.asList("§7Increases all stats equally.", "§7Success Rate: 50%"),
            Arrays.asList("§7- 1x Diamond", "§7- 1x Emerald", "§7- 1x Gold Ingot"));
        
        createReforgeStone("FIERCE_STONE", "Fierce Reforge Stone", Material.NETHERITE_SWORD,
            ReforgeSystem.ReforgeType.FIERCE, 2500, 0.4,
            Arrays.asList("§7Increases damage and critical damage.", "§7Success Rate: 40%"),
            Arrays.asList("§7- 1x Netherite Ingot", "§7- 1x Blaze Powder", "§7- 1x Nether Brick"));
        
        // Armor Reforge Stones
        createReforgeStone("PROTECTIVE_STONE", "Protective Reforge Stone", Material.SHIELD,
            ReforgeSystem.ReforgeType.PROTECTIVE, 800, 0.8,
            Arrays.asList("§7Increases defense and health.", "§7Success Rate: 80%"),
            Arrays.asList("§7- 1x Iron Ingot", "§7- 1x Leather", "§7- 1x String"));
        
        createReforgeStone("SPEEDY_STONE", "Speedy Reforge Stone", Material.FEATHER,
            ReforgeSystem.ReforgeType.SPEEDY, 1000, 0.7,
            Arrays.asList("§7Increases speed and agility.", "§7Success Rate: 70%"),
            Arrays.asList("§7- 1x Feather", "§7- 1x String", "§7- 1x Leather"));
        
        createReforgeStone("INTELLIGENT_STONE", "Intelligent Reforge Stone", Material.ENCHANTING_TABLE,
            ReforgeSystem.ReforgeType.INTELLIGENT, 1200, 0.6,
            Arrays.asList("§7Increases intelligence and mana regeneration.", "§7Success Rate: 60%"),
            Arrays.asList("§7- 1x Enchanting Table", "§7- 1x Lapis Lazuli", "§7- 1x Book"));
        
        createReforgeStone("TOUGH_STONE", "Tough Reforge Stone", Material.IRON_CHESTPLATE,
            ReforgeSystem.ReforgeType.TOUGH, 1500, 0.5,
            Arrays.asList("§7Increases defense and resistance.", "§7Success Rate: 50%"),
            Arrays.asList("§7- 1x Iron Ingot", "§7- 1x Coal", "§7- 1x Stone"));
        
        // Accessory Reforge Stones
        createReforgeStone("LUCKY_STONE", "Lucky Reforge Stone", Material.GOLD_INGOT,
            ReforgeSystem.ReforgeType.LUCKY, 1000, 0.7,
            Arrays.asList("§7Increases luck and critical chance.", "§7Success Rate: 70%"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Emerald", "§7- 1x Diamond"));
        
        createReforgeStone("MAGICAL_STONE", "Magical Reforge Stone", Material.END_CRYSTAL,
            ReforgeSystem.ReforgeType.MAGICAL, 1500, 0.6,
            Arrays.asList("§7Increases magic damage and mana.", "§7Success Rate: 60%"),
            Arrays.asList("§7- 1x End Crystal", "§7- 1x Ender Pearl", "§7- 1x Blaze Powder"));
        
        createReforgeStone("POWERFUL_STONE", "Powerful Reforge Stone", Material.NETHER_STAR,
            ReforgeSystem.ReforgeType.POWERFUL, 2000, 0.4,
            Arrays.asList("§7Increases all combat stats.", "§7Success Rate: 40%"),
            Arrays.asList("§7- 1x Nether Star", "§7- 1x Diamond", "§7- 1x Gold Ingot"));
        
        // Tool Reforge Stones
        createReforgeStone("EFFICIENT_STONE", "Efficient Reforge Stone", Material.DIAMOND_PICKAXE,
            ReforgeSystem.ReforgeType.EFFICIENT, 800, 0.8,
            Arrays.asList("§7Increases mining speed and efficiency.", "§7Success Rate: 80%"),
            Arrays.asList("§7- 1x Diamond", "§7- 1x Iron Ingot", "§7- 1x Coal"));
        
        createReforgeStone("FORTUNATE_STONE", "Fortunate Reforge Stone", Material.GOLDEN_PICKAXE,
            ReforgeSystem.ReforgeType.FORTUNATE, 1200, 0.6,
            Arrays.asList("§7Increases luck and fortune.", "§7Success Rate: 60%"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Emerald", "§7- 1x Diamond"));
        
        createReforgeStone("SPEEDY_TOOL_STONE", "Speedy Tool Reforge Stone", Material.NETHERITE_PICKAXE,
            ReforgeSystem.ReforgeType.SPEEDY_TOOL, 1500, 0.5,
            Arrays.asList("§7Increases speed and haste.", "§7Success Rate: 50%"),
            Arrays.asList("§7- 1x Netherite Ingot", "§7- 1x Redstone", "§7- 1x Coal"));
    }
    
    private void createReforgeStone(String id, String name, Material material, 
                                  ReforgeSystem.ReforgeType reforgeType, int cost, double successRate,
                                  List<String> description, List<String> materials) {
        ReforgeStone stone = new ReforgeStone(id, name, material, reforgeType, cost, successRate, description, materials);
        reforgeStones.put(id, stone);
    }
    
    public boolean useReforgeStone(Player player, ItemStack item, String stoneId) {
        ReforgeStone stone = reforgeStones.get(stoneId);
        if (stone == null) {
            player.sendMessage(Component.text("§cReforge stone not found!"));
            return false;
        }
        
        // Check if player has the stone
        if (!hasReforgeStone(player, stoneId)) {
            player.sendMessage("§cYou don't have a " + stone.getName() + "!");
            return false;
        }
        
        // Check if player has enough coins
        PlayerProfile profile = corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null) return false;
        
        if (!profile.tryRemoveCoins(stone.getCost())) {
            player.sendMessage("§cYou don't have enough coins! Cost: " + stone.getCost());
            return false;
        }
        
        // Attempt reforge
        boolean success = reforgeSystem.reforgeItem(player, item, stone.getReforgeType());
        
        if (success) {
            // Remove stone from inventory
            removeReforgeStone(player, stoneId);
            player.sendMessage(Component.text("§a§lREFORGE SUCCESSFUL!"));
            player.sendMessage("§7Used: §e" + stone.getName());
            player.sendMessage("§7Cost: §6" + stone.getCost() + " coins");
        } else {
            // Return coins on failure
            profile.addCoins(stone.getCost());
            player.sendMessage(Component.text("§c§lREFORGE FAILED!"));
            player.sendMessage("§7Used: §e" + stone.getName());
            player.sendMessage("§7Coins returned: §6" + stone.getCost());
        }
        
        return success;
    }
    
    public boolean craftReforgeStone(Player player, String stoneId) {
        ReforgeStone stone = reforgeStones.get(stoneId);
        if (stone == null) {
            player.sendMessage(Component.text("§cReforge stone not found!"));
            return false;
        }
        
        // Check if player has all required materials
        if (!hasCraftingMaterials(player, stone)) {
            player.sendMessage(Component.text("§cYou don't have all required materials!"));
            return false;
        }
        
        // Consume materials
        consumeCraftingMaterials(player, stone);
        
        // Give stone to player
        player.getInventory().addItem(stone.createStoneItem());
        
        player.sendMessage(Component.text("§a§lREFORGE STONE CRAFTED!"));
        player.sendMessage("§7Stone: §e" + stone.getName());
        
        return true;
    }
    
    private boolean hasReforgeStone(Player player, String stoneId) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.hasDisplayName()) {
                    String displayName = meta.displayName().toString();
                    if (displayName.contains("Reforge Stone") && displayName.contains(stoneId.replace("_", " "))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private void removeReforgeStone(Player player, String stoneId) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.hasDisplayName()) {
                    String displayName = meta.displayName().toString();
                    if (displayName.contains("Reforge Stone") && displayName.contains(stoneId.replace("_", " "))) {
                        if (item.getAmount() > 1) {
                            item.setAmount(item.getAmount() - 1);
                        } else {
                            player.getInventory().setItem(i, null);
                        }
                        return;
                    }
                }
            }
        }
    }
    
    private boolean hasCraftingMaterials(Player player, ReforgeStone stone) {
        for (String material : stone.getMaterials()) {
            if (!hasMaterial(player, material)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean hasMaterial(Player player, String material) {
        if (material.contains("Diamond")) {
            return player.getInventory().contains(Material.DIAMOND);
        } else if (material.contains("Iron Ingot")) {
            return player.getInventory().contains(Material.IRON_INGOT);
        } else if (material.contains("Gold Ingot")) {
            return player.getInventory().contains(Material.GOLD_INGOT);
        } else if (material.contains("Coal")) {
            return player.getInventory().contains(Material.COAL);
        } else if (material.contains("Stone")) {
            return player.getInventory().contains(Material.STONE);
        } else if (material.contains("Feather")) {
            return player.getInventory().contains(Material.FEATHER);
        } else if (material.contains("String")) {
            return player.getInventory().contains(Material.STRING);
        } else if (material.contains("Leather")) {
            return player.getInventory().contains(Material.LEATHER);
        } else if (material.contains("Lapis Lazuli")) {
            return player.getInventory().contains(Material.LAPIS_LAZULI);
        } else if (material.contains("Redstone")) {
            return player.getInventory().contains(Material.REDSTONE);
        } else if (material.contains("Emerald")) {
            return player.getInventory().contains(Material.EMERALD);
        } else if (material.contains("Enchanted Book")) {
            return player.getInventory().contains(Material.ENCHANTED_BOOK);
        } else if (material.contains("Book")) {
            return player.getInventory().contains(Material.BOOK);
        } else if (material.contains("Netherite Ingot")) {
            return player.getInventory().contains(Material.NETHERITE_INGOT);
        } else if (material.contains("Blaze Powder")) {
            return player.getInventory().contains(Material.BLAZE_POWDER);
        } else if (material.contains("Nether Brick")) {
            return player.getInventory().contains(Material.NETHER_BRICK);
        } else if (material.contains("End Crystal")) {
            return player.getInventory().contains(Material.END_CRYSTAL);
        } else if (material.contains("Ender Pearl")) {
            return player.getInventory().contains(Material.ENDER_PEARL);
        } else if (material.contains("Nether Star")) {
            return player.getInventory().contains(Material.NETHER_STAR);
        }
        return false;
    }
    
    private void consumeCraftingMaterials(Player player, ReforgeStone stone) {
        for (String material : stone.getMaterials()) {
            consumeMaterial(player, material);
        }
    }
    
    private void consumeMaterial(Player player, String material) {
        if (material.contains("Diamond")) {
            player.getInventory().removeItem(new ItemStack(Material.DIAMOND, 1));
        } else if (material.contains("Iron Ingot")) {
            player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 1));
        } else if (material.contains("Gold Ingot")) {
            player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 1));
        } else if (material.contains("Coal")) {
            player.getInventory().removeItem(new ItemStack(Material.COAL, 1));
        } else if (material.contains("Stone")) {
            player.getInventory().removeItem(new ItemStack(Material.STONE, 1));
        } else if (material.contains("Feather")) {
            player.getInventory().removeItem(new ItemStack(Material.FEATHER, 1));
        } else if (material.contains("String")) {
            player.getInventory().removeItem(new ItemStack(Material.STRING, 1));
        } else if (material.contains("Leather")) {
            player.getInventory().removeItem(new ItemStack(Material.LEATHER, 1));
        } else if (material.contains("Lapis Lazuli")) {
            player.getInventory().removeItem(new ItemStack(Material.LAPIS_LAZULI, 1));
        } else if (material.contains("Redstone")) {
            player.getInventory().removeItem(new ItemStack(Material.REDSTONE, 1));
        } else if (material.contains("Emerald")) {
            player.getInventory().removeItem(new ItemStack(Material.EMERALD, 1));
        } else if (material.contains("Enchanted Book")) {
            player.getInventory().removeItem(new ItemStack(Material.ENCHANTED_BOOK, 1));
        } else if (material.contains("Book")) {
            player.getInventory().removeItem(new ItemStack(Material.BOOK, 1));
        } else if (material.contains("Netherite Ingot")) {
            player.getInventory().removeItem(new ItemStack(Material.NETHERITE_INGOT, 1));
        } else if (material.contains("Blaze Powder")) {
            player.getInventory().removeItem(new ItemStack(Material.BLAZE_POWDER, 1));
        } else if (material.contains("Nether Brick")) {
            player.getInventory().removeItem(new ItemStack(Material.NETHER_BRICK, 1));
        } else if (material.contains("End Crystal")) {
            player.getInventory().removeItem(new ItemStack(Material.END_CRYSTAL, 1));
        } else if (material.contains("Ender Pearl")) {
            player.getInventory().removeItem(new ItemStack(Material.ENDER_PEARL, 1));
        } else if (material.contains("Nether Star")) {
            player.getInventory().removeItem(new ItemStack(Material.NETHER_STAR, 1));
        }
    }
    
    public Map<String, ReforgeStone> getAllReforgeStones() {
        return new HashMap<>(reforgeStones);
    }
    
    public ReforgeStone getReforgeStone(String stoneId) {
        return reforgeStones.get(stoneId);
    }
    
    public boolean canUseStone(ItemStack item, ReforgeStone stone) {
        if (item == null || stone == null) return false;
        
        // Check if the item can be reforged with this stone
        Material material = item.getType();
        
        // Basic compatibility check based on item type
        switch (stone.getCategory()) {
            case WEAPON:
                return material.name().contains("SWORD") || material.name().contains("AXE") || 
                       material.name().contains("BOW") || material.name().contains("CROSSBOW");
            case ARMOR:
                return material.name().contains("HELMET") || material.name().contains("CHESTPLATE") || 
                       material.name().contains("LEGGINGS") || material.name().contains("BOOTS");
            case ACCESSORY:
                return material.name().contains("RING") || material.name().contains("TALISMAN") || 
                       material.name().contains("ARTIFACT") || material.name().contains("RELIC");
            default:
                return false;
        }
    }
    
    public ItemStack createReforgeStoneItem(String stoneId) {
        ReforgeStone stone = reforgeStones.get(stoneId);
        if (stone == null) return null;
        
        return createReforgeStoneItem(stone);
    }
    
    public ItemStack createReforgeStoneItem(ReforgeStone stone) {
        if (stone == null) return null;
        
        ItemStack item = new ItemStack(stone.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6" + stone.getName()));
            
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("§7Reforge Stone"));
            lore.add(Component.text(""));
            lore.addAll(stone.getDescription().stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            lore.add(Component.text(""));
            lore.add(Component.text("§7Cost: §6" + stone.getCost() + " coins"));
            lore.add(Component.text("§7Success Rate: §a" + (int)(stone.getSuccessRate() * 100) + "%"));
            lore.add(Component.text(""));
            lore.add(Component.text("§eClick to use this reforge stone!"));
            
            meta.lore(lore);
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    // Reforge Category Enum
    public enum ReforgeCategory {
        WEAPON, ARMOR, ACCESSORY, TOOL
    }
    
    // Reforge Stone Class
    public static class ReforgeStone {
        private final String id;
        private final String name;
        private final Material material;
        private final ReforgeSystem.ReforgeType reforgeType;
        private final int cost;
        private final double successRate;
        private final List<String> description;
        private final List<String> materials;
        
        public ReforgeStone(String id, String name, Material material, ReforgeSystem.ReforgeType reforgeType,
                           int cost, double successRate, List<String> description, List<String> materials) {
            this.id = id;
            this.name = name;
            this.material = material;
            this.reforgeType = reforgeType;
            this.cost = cost;
            this.successRate = successRate;
            this.description = description;
            this.materials = materials;
        }
        
        public ItemStack createStoneItem() {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            
            if (meta != null) {
                meta.displayName(Component.text("§6§l" + name));
                
                List<String> lore = new ArrayList<>();
                lore.add("§7Reforge Type: §e" + reforgeType.name());
                lore.add("§7Cost: §6" + cost + " coins");
                lore.add("§7Success Rate: §a" + (int)(successRate * 100) + "%");
                lore.add("");
                lore.addAll(description);
                lore.add("");
                lore.add("§7Materials Required:");
                lore.addAll(materials);
                lore.add("");
                lore.add("§eRight-click on an item to reforge!");
                
                meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
                item.setItemMeta(meta);
            }
            
            return item;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDisplayName() { return name; }
        public Material getMaterial() { return material; }
        public ReforgeSystem.ReforgeType getReforgeType() { return reforgeType; }
        public ReforgeCategory getCategory() { 
            // Determine category based on reforge type
            switch (reforgeType) {
                case SHARP, HEAVY, LIGHT, WISE, PURE, FIERCE:
                    return ReforgeCategory.WEAPON;
                case PROTECTIVE, TOUGH:
                    return ReforgeCategory.ARMOR;
                case LUCKY, MAGICAL, POWERFUL:
                    return ReforgeCategory.ACCESSORY;
                case EFFICIENT, FORTUNATE, SPEEDY_TOOL:
                    return ReforgeCategory.TOOL;
                default:
                    return ReforgeCategory.ACCESSORY;
            }
        }
        public int getCost() { return cost; }
        public double getSuccessRate() { return successRate; }
        public List<String> getDescription() { return description; }
        public List<String> getMaterials() { return materials; }
        public List<String> getEffects() { return description; }
    }
}
