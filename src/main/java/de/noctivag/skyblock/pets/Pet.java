package de.noctivag.skyblock.pets;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;

/**
 * Pet - Einzelnes Haustier mit Level, XP und Effekten
 * 
 * Verantwortlich für:
 * - Pet-Level und -XP
 * - Pet-Boosts und -Effekte
 * - Pet-Inventar
 * - Pet-Upgrades
 * - Pet-Following
 */
public class Pet {
    private final UUID ownerId;
    private final PetSystem.PetType type;
    private int level;
    private double xp;
    private boolean isActive;
    private final Map<String, Object> upgrades = new HashMap<>();
    private final List<ItemStack> inventory = new ArrayList<>();
    private final int maxInventorySize = 27;
    private long lastFeedTime;
    private int hunger;
    private int happiness;
    
    public Pet(UUID ownerId, PetSystem.PetType type, int level) {
        this.ownerId = ownerId;
        this.type = type;
        this.level = level;
        this.xp = 0.0;
        this.isActive = false;
        this.hunger = 100;
        this.happiness = 100;
        this.lastFeedTime = System.currentTimeMillis();
        
        initializeUpgrades();
    }
    
    private void initializeUpgrades() {
        upgrades.put("speed", 0);
        upgrades.put("health", 0);
        upgrades.put("damage", 0);
        upgrades.put("xp_boost", 0);
        upgrades.put("auto_feed", false);
        upgrades.put("auto_collect", false);
        upgrades.put("auto_sell", false);
    }
    
    public void addXP(double xp) {
        this.xp += xp;
        checkLevelUp();
    }
    
    private void checkLevelUp() {
        double xpRequired = getXPRequiredForLevel(level + 1);
        if (xp >= xpRequired) {
            levelUp();
        }
    }
    
    public void levelUp() {
        level++;
        xp = 0.0;
        
        // Apply level-based upgrades
        if (level % 5 == 0) {
            // Every 5 levels, increase health
            upgrades.put("health", (Integer) upgrades.get("health") + 1);
        }
        
        if (level % 10 == 0) {
            // Every 10 levels, increase speed
            upgrades.put("speed", (Integer) upgrades.get("speed") + 1);
        }
    }
    
    private double getXPRequiredForLevel(int level) {
        // Hypixel-like XP calculation
        if (level <= 1) return 0;
        if (level <= 10) return level * 100;
        if (level <= 20) return 1000 + (level - 10) * 200;
        if (level <= 30) return 3000 + (level - 20) * 300;
        if (level <= 40) return 6000 + (level - 30) * 400;
        if (level <= 50) return 10000 + (level - 40) * 500;
        if (level <= 60) return 15000 + (level - 50) * 600;
        if (level <= 70) return 21000 + (level - 60) * 700;
        if (level <= 80) return 28000 + (level - 70) * 800;
        if (level <= 90) return 36000 + (level - 80) * 900;
        if (level <= 100) return 45000 + (level - 90) * 1000;
        return 55000 + (level - 100) * 1500; // Beyond level 100
    }
    
    public double getLevelUpCost() {
        return 1000.0 * Math.pow(1.5, level - 1);
    }
    
    public void feed(ItemStack food) {
        if (food == null) return;
        
        // Check if item is valid food
        int foodValue = getFoodValue(food.getType());
        if (foodValue <= 0) return;
        
        // Increase hunger
        hunger = Math.min(100, hunger + foodValue);
        lastFeedTime = System.currentTimeMillis();
        
        // Increase happiness
        happiness = Math.min(100, happiness + (foodValue / 2));
    }
    
    private int getFoodValue(Material material) {
        return switch (material) {
            case BREAD -> 5;
            case COOKED_BEEF -> 8;
            case COOKED_PORKCHOP -> 8;
            case COOKED_CHICKEN -> 6;
            case COOKED_MUTTON -> 6;
            case COOKED_RABBIT -> 5;
            case COOKED_SALMON -> 6;
            case COOKED_COD -> 5;
            case GOLDEN_APPLE -> 20;
            case GOLDEN_CARROT -> 15;
            case ENCHANTED_GOLDEN_APPLE -> 50;
            default -> 0;
        };
    }
    
    public void addUpgrade(String upgradeType, Object value) {
        upgrades.put(upgradeType, value);
    }
    
    public boolean hasUpgrade(String upgradeType) {
        Object value = upgrades.get(upgradeType);
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof Integer) {
            return (Integer) value > 0;
        }
        return false;
    }
    
    public int getUpgradeLevel(String upgradeType) {
        Object value = upgrades.get(upgradeType);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return 0;
    }
    
    public void addToInventory(ItemStack item) {
        if (inventory.size() < maxInventorySize) {
            inventory.add(item);
        }
    }
    
    public List<ItemStack> getInventory() {
        return new ArrayList<>(inventory);
    }
    
    public void clearInventory() {
        inventory.clear();
    }
    
    public ItemStack createPetItem() {
        ItemStack item = new ItemStack(type.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            String rarityColor = type.getRarity().getDisplayName().substring(0, 2);
            meta.displayName(Component.text(rarityColor + "§l" + type.getName() + " Pet"));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Level: §e" + level);
            lore.add("§7Type: §e" + type.getName());
            lore.add("§7Rarity: " + type.getRarity().getDisplayName());
            lore.add("§7Category: §e" + type.getCategory());
            lore.add("§7XP: §e" + String.format("%.1f", xp) + "/" + String.format("%.1f", getXPRequiredForLevel(level + 1)));
            lore.add("§7Hunger: §e" + hunger + "/100");
            lore.add("§7Happiness: §e" + happiness + "/100");
            lore.add("");
            lore.add("§7Description:");
            lore.addAll(type.getDescription());
            lore.add("");
            lore.add("§7Stats:");
            for (String stat : type.getStats()) {
                lore.add("§7• " + stat);
            }
            lore.add("");
            lore.add("§7Abilities:");
            for (PetSystem.PetAbility ability : type.getAbilities()) {
                if (level >= ability.getUnlockLevel()) {
                    lore.add("§a✓ " + ability.getName() + " §7- " + ability.getDescription());
                } else {
                    lore.add("§7✗ " + ability.getName() + " §7(Level " + ability.getUnlockLevel() + ")");
                }
            }
            lore.add("");
            lore.add("§7Upgrades:");
            lore.add("§7• Speed: §e" + getUpgradeLevel("speed"));
            lore.add("§7• Health: §e" + getUpgradeLevel("health"));
            lore.add("§7• Damage: §e" + getUpgradeLevel("damage"));
            lore.add("§7• XP Boost: §e" + getUpgradeLevel("xp_boost"));
            lore.add("§7• Auto Feed: §e" + (hasUpgrade("auto_feed") ? "Yes" : "No"));
            lore.add("§7• Auto Collect: §e" + (hasUpgrade("auto_collect") ? "Yes" : "No"));
            lore.add("§7• Auto Sell: §e" + (hasUpgrade("auto_sell") ? "Yes" : "No"));
            lore.add("");
            lore.add("§7Inventory: §e" + inventory.size() + "/" + maxInventorySize);
            lore.add("");
            lore.add("§eRight-click to activate!");
            
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    public void updateStats() {
        // Update hunger and happiness over time
        long currentTime = System.currentTimeMillis();
        long timeSinceLastFeed = currentTime - lastFeedTime;
        
        // Decrease hunger over time (1 point per minute)
        if (timeSinceLastFeed > 60000) { // 1 minute
            int hungerDecrease = (int) (timeSinceLastFeed / 60000);
            hunger = Math.max(0, hunger - hungerDecrease);
            lastFeedTime = currentTime;
        }
        
        // Decrease happiness if hungry
        if (hunger < 20) {
            happiness = Math.max(0, happiness - 1);
        } else if (hunger > 80) {
            happiness = Math.min(100, happiness + 1);
        }
    }
    
    public double getXPBoost() {
        int xpBoostUpgrade = getUpgradeLevel("xp_boost");
        return 1.0 + (xpBoostUpgrade * 0.1); // 10% per level
    }
    
    public double getSpeedBoost() {
        int speedUpgrade = getUpgradeLevel("speed");
        return 1.0 + (speedUpgrade * 0.05); // 5% per level
    }
    
    public double getHealthBoost() {
        int healthUpgrade = getUpgradeLevel("health");
        return 1.0 + (healthUpgrade * 0.1); // 10% per level
    }
    
    public double getDamageBoost() {
        int damageUpgrade = getUpgradeLevel("damage");
        return 1.0 + (damageUpgrade * 0.1); // 10% per level
    }
    
    // Getters and Setters
    public UUID getOwnerId() { return ownerId; }
    public PetSystem.PetType getType() { return type; }
    public int getLevel() { return level; }
    public double getXP() { return xp; }
    public boolean isActive() { return isActive; }
    public Map<String, Object> getUpgrades() { return new HashMap<>(upgrades); }
    public int getHunger() { return hunger; }
    public int getHappiness() { return happiness; }
    public long getLastFeedTime() { return lastFeedTime; }
    
    public void setLevel(int level) { this.level = level; }
    public void setXP(double xp) { this.xp = xp; }
    public void setActive(boolean active) { this.isActive = active; }
    public void setHunger(int hunger) { this.hunger = hunger; }
    public void setHappiness(int happiness) { this.happiness = happiness; }
}
