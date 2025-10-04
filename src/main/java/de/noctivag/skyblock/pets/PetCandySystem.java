package de.noctivag.skyblock.pets;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.core.CorePlatform;
// import de.noctivag.skyblock.core.PlayerProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;

/**
 * Pet Candy System - Special items that boost pet XP and abilities
 * 
 * Features:
 * - Different types of pet candy
 * - XP boost effects
 * - Temporary ability boosts
 * - Candy rarity system
 * - Candy crafting recipes
 * - Candy effects duration
 */
public class PetCandySystem {
    private final SkyblockPlugin plugin;
    private final CorePlatform corePlatform;
    private final Map<String, PetCandy> petCandies = new HashMap<>();
    private final Map<UUID, List<CandyEffect>> activeEffects = new HashMap<>();
    
    public PetCandySystem(SkyblockPlugin plugin, CorePlatform corePlatform) {
        this.plugin = plugin;
        this.corePlatform = corePlatform;
        initializePetCandies();
    }
    
    private void initializePetCandies() {
        // Common Candies
        petCandies.put("BASIC_CANDY", new PetCandy(
            "BASIC_CANDY", "Basic Candy", "§fBasic Candy", Material.SUGAR,
            Arrays.asList("§7A simple candy that gives", "§7your pet a small XP boost."),
            CandyRarity.COMMON, 100, 1.5, 300, // 5 minutes
            Arrays.asList("§7+50% XP for 5 minutes"),
            Arrays.asList("§7- 1x Sugar", "§7- 1x Wheat")
        ));
        
        petCandies.put("SWEET_CANDY", new PetCandy(
            "SWEET_CANDY", "Sweet Candy", "§aSweet Candy", Material.COOKIE,
            Arrays.asList("§7A delicious candy that", "§7significantly boosts pet XP."),
            CandyRarity.UNCOMMON, 250, 2.0, 600, // 10 minutes
            Arrays.asList("§7+100% XP for 10 minutes"),
            Arrays.asList("§7- 1x Cookie", "§7- 1x Sugar", "§7- 1x Milk")
        ));
        
        // Rare Candies
        petCandies.put("GOLDEN_CANDY", new PetCandy(
            "GOLDEN_CANDY", "Golden Candy", "§6Golden Candy", Material.GOLDEN_APPLE,
            Arrays.asList("§7A precious golden candy", "§7that greatly enhances your pet."),
            CandyRarity.RARE, 500, 3.0, 900, // 15 minutes
            Arrays.asList("§7+200% XP for 15 minutes", "§7+50% All Pet Stats"),
            Arrays.asList("§7- 1x Golden Apple", "§7- 1x Sugar", "§7- 1x Gold Ingot")
        ));
        
        petCandies.put("MAGICAL_CANDY", new PetCandy(
            "MAGICAL_CANDY", "Magical Candy", "§dMagical Candy", Material.ENCHANTED_GOLDEN_APPLE,
            Arrays.asList("§7A mystical candy infused", "§7with magical properties."),
            CandyRarity.EPIC, 1000, 4.0, 1200, // 20 minutes
            Arrays.asList("§7+300% XP for 20 minutes", "§7+100% All Pet Stats", "§7Unlocks hidden abilities"),
            Arrays.asList("§7- 1x Enchanted Golden Apple", "§7- 1x Lapis Lazuli", "§7- 1x Redstone")
        ));
        
        // Legendary Candies
        petCandies.put("DIVINE_CANDY", new PetCandy(
            "DIVINE_CANDY", "Divine Candy", "§bDivine Candy", Material.NETHER_STAR,
            Arrays.asList("§7A divine candy blessed", "§7by the gods themselves."),
            CandyRarity.LEGENDARY, 2500, 5.0, 1800, // 30 minutes
            Arrays.asList("§7+400% XP for 30 minutes", "§7+200% All Pet Stats", "§7Grants temporary evolution"),
            Arrays.asList("§7- 1x Nether Star", "§7- 1x Diamond", "§7- 1x Emerald")
        ));
        
        petCandies.put("MYTHICAL_CANDY", new PetCandy(
            "MYTHICAL_CANDY", "Mythical Candy", "§5Mythical Candy", Material.DRAGON_EGG,
            Arrays.asList("§7A legendary candy from", "§7the depths of mythology."),
            CandyRarity.MYTHIC, 5000, 10.0, 3600, // 1 hour
            Arrays.asList("§7+500% XP for 1 hour", "§7+300% All Pet Stats", "§7Permanent ability unlock"),
            Arrays.asList("§7- 1x Dragon Egg", "§7- 1x Nether Star", "§7- 1x Ender Pearl")
        ));
        
        // Special Candies
        petCandies.put("SPEED_CANDY", new PetCandy(
            "SPEED_CANDY", "Speed Candy", "§eSpeed Candy", Material.FEATHER,
            Arrays.asList("§7A light candy that", "§7makes your pet incredibly fast."),
            CandyRarity.UNCOMMON, 300, 1.0, 600, // 10 minutes
            Arrays.asList("§7+300% Pet Speed for 10 minutes"),
            Arrays.asList("§7- 1x Feather", "§7- 1x Sugar", "§7- 1x Redstone")
        ));
        
        petCandies.put("STRENGTH_CANDY", new PetCandy(
            "STRENGTH_CANDY", "Strength Candy", "§c§lStrength Candy", Material.IRON_INGOT,
            Arrays.asList("§7A powerful candy that increases your pet's strength."),
            CandyRarity.UNCOMMON, 300, 1.0, 600, // 10 minutes
            Arrays.asList("§7+200% Pet Damage for 10 minutes"),
            Arrays.asList("§7- 1x Iron Ingot", "§7- 1x Sugar", "§7- 1x Coal")
        ));
        
        petCandies.put("LUCK_CANDY", new PetCandy(
            "LUCK_CANDY", "Luck Candy", "§b§lLuck Candy", Material.EMERALD,
            Arrays.asList("§7A lucky candy that increases your pet's fortune."),
            CandyRarity.RARE, 400, 1.0, 900, // 15 minutes
            Arrays.asList("§7+250% Pet Luck for 15 minutes"),
            Arrays.asList("§7- 1x Emerald", "§7- 1x Sugar", "§7- 1x Gold Ingot")
        ));
    }
    
    public void useCandy(Player player, Pet pet, String candyId) {
        PetCandy candy = petCandies.get(candyId);
        if (candy == null) return;
        
        // Check if player has the candy
        if (!hasCandy(player, candyId)) {
            player.sendMessage("§cYou don't have this candy!");
            return;
        }
        
        // Remove candy from inventory
        removeCandy(player, candyId);
        
        // Apply candy effects
        applyCandyEffects(player, pet, candy);
        
        player.sendMessage("§a§lCANDY USED!");
        player.sendMessage("§7Pet: §e" + pet.getType().getName());
        player.sendMessage("§7Candy: " + candy.getDisplayName());
        player.sendMessage("§7Duration: §e" + (candy.getDuration() / 60) + " minutes");
    }
    
    private void applyCandyEffects(Player player, Pet pet, PetCandy candy) {
        UUID playerId = player.getUniqueId();
        
        // Add XP boost
        double xpBoost = candy.getXpMultiplier();
        pet.addXP(candy.getBaseXp() * xpBoost);
        
        // Create candy effect
        CandyEffect effect = new CandyEffect(
            candy.getId(),
            candy.getDisplayName(),
            candy.getXpMultiplier(),
            candy.getDuration(),
            System.currentTimeMillis()
        );
        
        // Add to active effects
        activeEffects.computeIfAbsent(playerId, k -> new ArrayList<>()).add(effect);
        
        // Apply stat boosts if any
        if (candy.getDisplayName().contains("Speed")) {
            // Apply speed boost
        } else if (candy.getDisplayName().contains("Strength")) {
            // Apply strength boost
        } else if (candy.getDisplayName().contains("Luck")) {
            // Apply luck boost
        }
        
        // Schedule effect removal
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            removeCandyEffect(playerId, effect);
        }, candy.getDuration() * 20L); // Convert seconds to ticks
    }
    
    private void removeCandyEffect(UUID playerId, CandyEffect effect) {
        List<CandyEffect> effects = activeEffects.get(playerId);
        if (effects != null) {
            effects.remove(effect);
            if (effects.isEmpty()) {
                activeEffects.remove(playerId);
            }
        }
    }
    
    public double getXpMultiplier(UUID playerId) {
        List<CandyEffect> effects = activeEffects.get(playerId);
        if (effects == null) return 1.0;
        
        return effects.stream()
            .mapToDouble(CandyEffect::getXpMultiplier)
            .reduce(1.0, (a, b) -> a * b);
    }
    
    public List<CandyEffect> getActiveEffects(UUID playerId) {
        return activeEffects.getOrDefault(playerId, new ArrayList<>());
    }
    
    private boolean hasCandy(Player player, String candyId) {
        PetCandy candy = petCandies.get(candyId);
        if (candy == null) return false;
        
        return player.getInventory().contains(candy.getMaterial());
    }
    
    private void removeCandy(Player player, String candyId) {
        PetCandy candy = petCandies.get(candyId);
        if (candy == null) return;
        
        player.getInventory().removeItem(new ItemStack(candy.getMaterial(), 1));
    }
    
    public ItemStack createCandyItem(String candyId) {
        PetCandy candy = petCandies.get(candyId);
        if (candy == null) return null;
        
        ItemStack item = new ItemStack(candy.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(candy.getDisplayName()));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Type: §ePet Candy");
            lore.add("§7Rarity: " + candy.getRarity().getDisplayName());
            lore.add("§7Base XP: §e" + candy.getBaseXp());
            lore.add("§7XP Multiplier: §e" + candy.getXpMultiplier() + "x");
            lore.add("§7Duration: §e" + (candy.getDuration() / 60) + " minutes");
            lore.add("");
            lore.add("§7Description:");
            lore.addAll(candy.getDescription());
            lore.add("");
            lore.add("§7Effects:");
            lore.addAll(candy.getEffects());
            lore.add("");
            lore.add("§7Crafting:");
            lore.addAll(candy.getCraftingMaterials());
            lore.add("");
            lore.add("§eRight-click on a pet to use!");
            
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    public PetCandy getPetCandy(String candyId) {
        return petCandies.get(candyId);
    }
    
    public Map<String, PetCandy> getAllPetCandies() {
        return new HashMap<>(petCandies);
    }
    
    // Candy Classes
    public static class PetCandy {
        private final String id;
        private final String name;
        private final String displayName;
        private final Material material;
        private final List<String> description;
        private final CandyRarity rarity;
        private final int baseXp;
        private final double xpMultiplier;
        private final int duration; // in seconds
        private final List<String> effects;
        private final List<String> craftingMaterials;
        
        public PetCandy(String id, String name, String displayName, Material material,
                       List<String> description, CandyRarity rarity, int baseXp, double xpMultiplier,
                       int duration, List<String> effects, List<String> craftingMaterials) {
            this.id = id;
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.rarity = rarity;
            this.baseXp = baseXp;
            this.xpMultiplier = xpMultiplier;
            this.duration = duration;
            this.effects = effects;
            this.craftingMaterials = craftingMaterials;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public List<String> getDescription() { return description; }
        public CandyRarity getRarity() { return rarity; }
        public int getBaseXp() { return baseXp; }
        public double getXpMultiplier() { return xpMultiplier; }
        public int getDuration() { return duration; }
        public List<String> getEffects() { return effects; }
        public List<String> getCraftingMaterials() { return craftingMaterials; }
    }
    
    public enum CandyRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        CandyRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class CandyEffect {
        private final String candyId;
        private final String candyName;
        private final double xpMultiplier;
        private final int duration;
        private final long startTime;
        
        public CandyEffect(String candyId, String candyName, double xpMultiplier, int duration, long startTime) {
            this.candyId = candyId;
            this.candyName = candyName;
            this.xpMultiplier = xpMultiplier;
            this.duration = duration;
            this.startTime = startTime;
        }
        
        public boolean isExpired() {
            return System.currentTimeMillis() - startTime > duration * 1000L;
        }
        
        public long getTimeRemaining() {
            long elapsed = System.currentTimeMillis() - startTime;
            return Math.max(0, (duration * 1000L) - elapsed);
        }
        
        // Getters
        public String getCandyId() { return candyId; }
        public String getCandyName() { return candyName; }
        public double getXpMultiplier() { return xpMultiplier; }
        public int getDuration() { return duration; }
        public long getStartTime() { return startTime; }
    }
}
