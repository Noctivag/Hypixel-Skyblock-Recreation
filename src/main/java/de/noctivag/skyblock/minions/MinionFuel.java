package de.noctivag.skyblock.minions;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Minion Fuel - Fuel System für Minions
 * 
 * Verantwortlich für:
 * - Fuel Types
 * - Fuel Duration
 * - Fuel Multipliers
 * - Fuel Management
 */
public class MinionFuel {
    
    public enum FuelType {
        COAL("Coal", "§8Coal", Material.COAL, 3600000L, 1.1, "§7Basic fuel for minions"),
        CHARCOAL("Charcoal", "§8Charcoal", Material.CHARCOAL, 3600000L, 1.1, "§7Basic fuel for minions"),
        COAL_BLOCK("Coal Block", "§8Coal Block", Material.COAL_BLOCK, 36000000L, 1.2, "§7Efficient fuel block"),
        LAVA_BUCKET("Lava Bucket", "§cLava Bucket", Material.LAVA_BUCKET, 7200000L, 1.25, "§7Hot fuel for minions"),
        ENCHANTED_COAL("Enchanted Coal", "§8Enchanted Coal", Material.COAL, 18000000L, 1.3, "§7Enchanted fuel"),
        ENCHANTED_CHARCOAL("Enchanted Charcoal", "§8Enchanted Charcoal", Material.CHARCOAL, 18000000L, 1.3, "§7Enchanted fuel"),
        ENCHANTED_COAL_BLOCK("Enchanted Coal Block", "§8Enchanted Coal Block", Material.COAL_BLOCK, 180000000L, 1.4, "§7Enchanted fuel block"),
        ENCHANTED_LAVA_BUCKET("Enchanted Lava Bucket", "§cEnchanted Lava Bucket", Material.LAVA_BUCKET, 36000000L, 1.5, "§7Enchanted hot fuel"),
        CATALYST("Catalyst", "§dCatalyst", Material.NETHER_STAR, 86400000L, 2.0, "§7Ultimate fuel catalyst"),
        HAMSTER_WHEEL("Hamster Wheel", "§eHamster Wheel", Material.SPONGE, 7200000L, 1.0, "§7Cute but effective"),
        FOUL_FLESH("Foul Flesh", "§2Foul Flesh", Material.ROTTEN_FLESH, 18000000L, 1.0, "§7Gross but works"),
        ENCHANTED_BREAD("Enchanted Bread", "§eEnchanted Bread", Material.BREAD, 14400000L, 1.0, "§7Nutritious fuel"),
        ENCHANTED_CARROT("Enchanted Carrot", "§6Enchanted Carrot", Material.CARROT, 14400000L, 1.0, "§7Healthy fuel"),
        ENCHANTED_POTATO("Enchanted Potato", "§eEnchanted Potato", Material.POTATO, 14400000L, 1.0, "§7Starchy fuel"),
        ENCHANTED_PUMPKIN("Enchanted Pumpkin", "§6Enchanted Pumpkin", Material.PUMPKIN, 14400000L, 1.0, "§7Seasonal fuel"),
        ENCHANTED_MELON("Enchanted Melon", "§aEnchanted Melon", Material.MELON, 14400000L, 1.0, "§7Sweet fuel"),
        ENCHANTED_SEEDS("Enchanted Seeds", "§eEnchanted Seeds", Material.WHEAT_SEEDS, 14400000L, 1.0, "§7Growing fuel"),
        ENCHANTED_SUGAR_CANE("Enchanted Sugar Cane", "§fEnchanted Sugar Cane", Material.SUGAR_CANE, 14400000L, 1.0, "§7Sweet fuel"),
        ENCHANTED_COCOA("Enchanted Cocoa", "§6Enchanted Cocoa", Material.COCOA_BEANS, 14400000L, 1.0, "§7Chocolate fuel"),
        ENCHANTED_CACTUS("Enchanted Cactus", "§aEnchanted Cactus", Material.CACTUS, 14400000L, 1.0, "§7Prickly fuel"),
        ENCHANTED_NETHER_WART("Enchanted Nether Wart", "§cEnchanted Nether Wart", Material.NETHER_WART, 14400000L, 1.0, "§7Magical fuel");
        
        private final String name;
        private final String displayName;
        private final Material icon;
        private final long duration; // in milliseconds
        private final double speedMultiplier;
        private final String description;
        
        FuelType(String name, String displayName, Material icon, long duration, double speedMultiplier, String description) {
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.duration = duration;
            this.speedMultiplier = speedMultiplier;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public long getDuration() { return duration; }
        public double getSpeedMultiplier() { return speedMultiplier; }
        public String getDescription() { return description; }
    }
    
    private final FuelType type;
    private final long startTime;
    private final long endTime;
    private final boolean active;
    
    public MinionFuel(FuelType type) {
        this.type = type;
        this.startTime = System.currentTimeMillis();
        this.endTime = startTime + type.getDuration();
        this.active = true;
    }
    
    public MinionFuel(FuelType type, long startTime, long endTime, boolean active) {
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.active = active;
    }
    
    public FuelType getType() { return type; }
    public long getStartTime() { return startTime; }
    public long getEndTime() { return endTime; }
    public boolean isActive() { return active && System.currentTimeMillis() < endTime; }
    public long getRemainingTime() { return Math.max(0, endTime - System.currentTimeMillis()); }
    public double getSpeedMultiplier() { return type.getSpeedMultiplier(); }
    
    public boolean isExpired() {
        return System.currentTimeMillis() >= endTime;
    }
    
    public String getRemainingTimeFormatted() {
        long remaining = getRemainingTime();
        if (remaining <= 0) return "Expired";
        
        long hours = remaining / 3600000;
        long minutes = (remaining % 3600000) / 60000;
        long seconds = (remaining % 60000) / 1000;
        
        if (hours > 0) {
            return hours + "h " + minutes + "m " + seconds + "s";
        } else if (minutes > 0) {
            return minutes + "m " + seconds + "s";
        } else {
            return seconds + "s";
        }
    }
    
    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(type.getIcon());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(type.getDisplayName()));
            
            List<String> lore = Arrays.asList(
                type.getDescription(),
                "§7",
                "§7Duration: §e" + formatDuration(type.getDuration()),
                "§7Speed Multiplier: §e" + String.format("%.1f", type.getSpeedMultiplier()) + "x",
                "§7",
                "§7Right-click on a minion to use!"
            );
            
            List<Component> componentLore = new ArrayList<>();
            for (String line : lore) {
                componentLore.add(Component.text(line));
            }
            meta.lore(componentLore);
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    public static MinionFuel fromItemStack(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return null;
        
        String displayName = meta.getDisplayName();
        
        // Find matching fuel type
        for (FuelType type : FuelType.values()) {
            if (displayName.contains(type.getDisplayName())) {
                return new MinionFuel(type);
            }
        }
        
        return null;
    }
    
    private String formatDuration(long duration) {
        long hours = duration / 3600000;
        long minutes = (duration % 3600000) / 60000;
        
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }
    
    public static List<MinionFuel> getAllFuels() {
        List<MinionFuel> fuels = new ArrayList<>();
        
        for (FuelType type : FuelType.values()) {
            fuels.add(new MinionFuel(type));
        }
        
        return fuels;
    }
    
    public static MinionFuel getBestFuel() {
        return new MinionFuel(FuelType.CATALYST);
    }
    
    public static MinionFuel getCheapestFuel() {
        return new MinionFuel(FuelType.COAL);
    }
    
    @Override
    public String toString() {
        return "MinionFuel{" +
                "type=" + type +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", active=" + active +
                ", remainingTime=" + getRemainingTimeFormatted() +
                '}';
    }
}
