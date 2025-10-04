package de.noctivag.skyblock.minions;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Minion Upgrade - Upgrade System für Minions
 * 
 * Verantwortlich für:
 * - Speed Upgrades
 * - Storage Upgrades
 * - Fuel Upgrades
 * - Auto Sell Upgrades
 * - Compactor Upgrades
 */
public class MinionUpgrade {
    
    public enum UpgradeType {
        SPEED("Speed", "§eSpeed Upgrade", Material.SUGAR, "§7Increases minion production speed"),
        STORAGE("Storage", "§bStorage Upgrade", Material.CHEST, "§7Increases minion storage capacity"),
        AUTO_SELL("Auto Sell", "§aAuto Sell", Material.EMERALD, "§7Automatically sells minion products"),
        COMPACTOR("Compactor", "§6Compactor", Material.HOPPER, "§7Compacts items into blocks"),
        SUPER_COMPACTOR("Super Compactor", "§dSuper Compactor", Material.ENDER_CHEST, "§7Super compacts items"),
        DIAMOND_SPREADING("Diamond Spreading", "§bDiamond Spreading", Material.DIAMOND, "§7Chance to produce diamonds"),
        BUDGET_HOPPER("Budget Hopper", "§7Budget Hopper", Material.HOPPER, "§7Automatically sells items"),
        FUEL("Fuel", "§cFuel", Material.COAL, "§7Provides fuel for minion");
        
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        
        UpgradeType(String name, String displayName, Material icon, String description) {
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
    }
    
    private final UpgradeType type;
    private final int level;
    private final double speedMultiplier;
    private final int storageIncrease;
    private final boolean autoSell;
    private final boolean compactor;
    private final boolean superCompactor;
    private final boolean diamondSpreading;
    private final boolean budgetHopper;
    private final long fuelDuration; // in milliseconds
    private final double fuelMultiplier;
    
    public MinionUpgrade(UpgradeType type, int level) {
        this.type = type;
        this.level = level;
        
        // Calculate upgrade values based on type and level
        switch (type) {
            case SPEED:
                this.speedMultiplier = 1.0 + (level * 0.1); // 10% per level
                this.storageIncrease = 0;
                this.autoSell = false;
                this.compactor = false;
                this.superCompactor = false;
                this.diamondSpreading = false;
                this.budgetHopper = false;
                this.fuelDuration = 0;
                this.fuelMultiplier = 1.0;
                break;
                
            case STORAGE:
                this.speedMultiplier = 1.0;
                this.storageIncrease = level * 64; // 64 slots per level
                this.autoSell = false;
                this.compactor = false;
                this.superCompactor = false;
                this.diamondSpreading = false;
                this.budgetHopper = false;
                this.fuelDuration = 0;
                this.fuelMultiplier = 1.0;
                break;
                
            case AUTO_SELL:
                this.speedMultiplier = 1.0;
                this.storageIncrease = 0;
                this.autoSell = true;
                this.compactor = false;
                this.superCompactor = false;
                this.diamondSpreading = false;
                this.budgetHopper = false;
                this.fuelDuration = 0;
                this.fuelMultiplier = 1.0;
                break;
                
            case COMPACTOR:
                this.speedMultiplier = 1.0;
                this.storageIncrease = 0;
                this.autoSell = false;
                this.compactor = true;
                this.superCompactor = false;
                this.diamondSpreading = false;
                this.budgetHopper = false;
                this.fuelDuration = 0;
                this.fuelMultiplier = 1.0;
                break;
                
            case SUPER_COMPACTOR:
                this.speedMultiplier = 1.0;
                this.storageIncrease = 0;
                this.autoSell = false;
                this.compactor = false;
                this.superCompactor = true;
                this.diamondSpreading = false;
                this.budgetHopper = false;
                this.fuelDuration = 0;
                this.fuelMultiplier = 1.0;
                break;
                
            case DIAMOND_SPREADING:
                this.speedMultiplier = 1.0;
                this.storageIncrease = 0;
                this.autoSell = false;
                this.compactor = false;
                this.superCompactor = false;
                this.diamondSpreading = true;
                this.budgetHopper = false;
                this.fuelDuration = 0;
                this.fuelMultiplier = 1.0;
                break;
                
            case BUDGET_HOPPER:
                this.speedMultiplier = 1.0;
                this.storageIncrease = 0;
                this.autoSell = false;
                this.compactor = false;
                this.superCompactor = false;
                this.diamondSpreading = false;
                this.budgetHopper = true;
                this.fuelDuration = 0;
                this.fuelMultiplier = 1.0;
                break;
                
            case FUEL:
                this.speedMultiplier = 1.0;
                this.storageIncrease = 0;
                this.autoSell = false;
                this.compactor = false;
                this.superCompactor = false;
                this.diamondSpreading = false;
                this.budgetHopper = false;
                this.fuelDuration = level * 3600000L; // 1 hour per level
                this.fuelMultiplier = 1.0 + (level * 0.05); // 5% per level
                break;
                
            default:
                this.speedMultiplier = 1.0;
                this.storageIncrease = 0;
                this.autoSell = false;
                this.compactor = false;
                this.superCompactor = false;
                this.diamondSpreading = false;
                this.budgetHopper = false;
                this.fuelDuration = 0;
                this.fuelMultiplier = 1.0;
                break;
        }
    }
    
    public UpgradeType getType() { return type; }
    public int getLevel() { return level; }
    public double getSpeedMultiplier() { return speedMultiplier; }
    public int getStorageIncrease() { return storageIncrease; }
    public boolean isAutoSell() { return autoSell; }
    public boolean isCompactor() { return compactor; }
    public boolean isSuperCompactor() { return superCompactor; }
    public boolean isDiamondSpreading() { return diamondSpreading; }
    public boolean isBudgetHopper() { return budgetHopper; }
    public long getFuelDuration() { return fuelDuration; }
    public double getFuelMultiplier() { return fuelMultiplier; }
    
    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(type.getIcon());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(type.getDisplayName() + " " + level));
            
            List<String> lore = Arrays.asList(
                type.getDescription(),
                "§7",
                "§7Level: §e" + level,
                "§7"
            );
            
            // Add specific upgrade info
            switch (type) {
                case SPEED:
                    lore.add("§7Speed Multiplier: §e" + String.format("%.1f", speedMultiplier) + "x");
                    break;
                case STORAGE:
                    lore.add("§7Storage Increase: §e+" + storageIncrease + " slots");
                    break;
                case AUTO_SELL:
                    lore.add("§7Auto Sell: §aEnabled");
                    break;
                case COMPACTOR:
                    lore.add("§7Compactor: §aEnabled");
                    break;
                case SUPER_COMPACTOR:
                    lore.add("§7Super Compactor: §aEnabled");
                    break;
                case DIAMOND_SPREADING:
                    lore.add("§7Diamond Spreading: §aEnabled");
                    break;
                case BUDGET_HOPPER:
                    lore.add("§7Budget Hopper: §aEnabled");
                    break;
                case FUEL:
                    lore.add("§7Fuel Duration: §e" + (fuelDuration / 3600000) + " hours");
                    lore.add("§7Fuel Multiplier: §e" + String.format("%.1f", fuelMultiplier) + "x");
                    break;
            }
            
            List<Component> componentLore = new ArrayList<>();
            for (String line : lore) {
                componentLore.add(Component.text(line));
            }
            meta.lore(componentLore);
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    public static MinionUpgrade fromItemStack(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return null;
        
        String displayName = meta.getDisplayName();
        
        // Parse upgrade type and level from display name
        for (UpgradeType type : UpgradeType.values()) {
            if (displayName.contains(type.getDisplayName())) {
                // Extract level from display name
                String[] parts = displayName.split(" ");
                if (parts.length > 0) {
                    try {
                        int level = Integer.parseInt(parts[parts.length - 1]);
                        return new MinionUpgrade(type, level);
                    } catch (NumberFormatException e) {
                        return new MinionUpgrade(type, 1);
                    }
                }
                return new MinionUpgrade(type, 1);
            }
        }
        
        return null;
    }
    
    public static List<MinionUpgrade> getAllUpgrades() {
        List<MinionUpgrade> upgrades = new ArrayList<>();
        
        for (UpgradeType type : UpgradeType.values()) {
            // Create upgrades for levels 1-5
            for (int level = 1; level <= 5; level++) {
                upgrades.add(new MinionUpgrade(type, level));
            }
        }
        
        return upgrades;
    }
    
    @Override
    public String toString() {
        return "MinionUpgrade{" +
                "type=" + type +
                ", level=" + level +
                ", speedMultiplier=" + speedMultiplier +
                ", storageIncrease=" + storageIncrease +
                ", autoSell=" + autoSell +
                ", compactor=" + compactor +
                ", superCompactor=" + superCompactor +
                ", diamondSpreading=" + diamondSpreading +
                ", budgetHopper=" + budgetHopper +
                ", fuelDuration=" + fuelDuration +
                ", fuelMultiplier=" + fuelMultiplier +
                '}';
    }
}
