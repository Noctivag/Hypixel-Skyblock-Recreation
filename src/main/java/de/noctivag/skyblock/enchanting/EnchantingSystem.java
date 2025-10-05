package de.noctivag.skyblock.enchanting;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;

/**
 * EnchantingSystem - Complete enchanting system for Hypixel Skyblock
 * 
 * Features:
 * - Enchanting table functionality
 * - Enchantment books
 * - Enchantment combining
 * - Enchantment levels and costs
 * - Enchanting XP system
 */
public class EnchantingSystem implements Service {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<UUID, PlayerEnchantingData> playerData = new ConcurrentHashMap<>();
    private final Map<String, CustomEnchantment> enchantments = new HashMap<>();
    private final Map<EnchantmentType, List<CustomEnchantment>> enchantmentsByType = new HashMap<>();
    
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    
    public EnchantingSystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.INITIALIZING;
            
            // Initialize all enchantments
            initializeEnchantments();
            
            status = SystemStatus.ENABLED;
            SkyblockPlugin.getLogger().info("§a[EnchantingSystem] Initialized " + enchantments.size() + " enchantments");
        });
    }
    
    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.SHUTTING_DOWN;
            status = SystemStatus.UNINITIALIZED;
        });
    }
    
    @Override
    public boolean isInitialized() {
        return status == SystemStatus.ENABLED;
    }
    
    @Override
    public int getPriority() {
        return 50;
    }
    
    @Override
    public boolean isRequired() {
        return false;
    }
    
    @Override
    public String getName() {
        return "EnchantingSystem";
    }
    
    /**
     * Initialize all enchantments
     */
    private void initializeEnchantments() {
        // Combat Enchantments
        initializeCombatEnchantments();
        
        // Mining Enchantments
        initializeMiningEnchantments();
        
        // Farming Enchantments
        initializeFarmingEnchantments();
        
        // Fishing Enchantments
        initializeFishingEnchantments();
        
        // Special Enchantments
        initializeSpecialEnchantments();
    }
    
    /**
     * Initialize combat enchantments
     */
    private void initializeCombatEnchantments() {
        List<CustomEnchantment> combatEnchantments = new ArrayList<>();
        
        // Sharpness
        combatEnchantments.add(new CustomEnchantment(
            "sharpness", "Sharpness", "Increases melee damage",
            EnchantmentType.COMBAT, EnchantmentRarity.COMMON, 1, 5,
            new int[]{5, 10, 15, 20, 30} // XP costs per level
        ));
        
        // Smite
        combatEnchantments.add(new CustomEnchantment(
            "smite", "Smite", "Increases damage to undead mobs",
            EnchantmentType.COMBAT, EnchantmentRarity.COMMON, 1, 5,
            new int[]{5, 10, 15, 20, 30}
        ));
        
        // Bane of Arthropods
        combatEnchantments.add(new CustomEnchantment(
            "bane_of_arthropods", "Bane of Arthropods", "Increases damage to arthropod mobs",
            EnchantmentType.COMBAT, EnchantmentRarity.COMMON, 1, 5,
            new int[]{5, 10, 15, 20, 30}
        ));
        
        // Fire Aspect
        combatEnchantments.add(new CustomEnchantment(
            "fire_aspect", "Fire Aspect", "Sets targets on fire",
            EnchantmentType.COMBAT, EnchantmentRarity.UNCOMMON, 1, 2,
            new int[]{10, 20}
        ));
        
        // Looting
        combatEnchantments.add(new CustomEnchantment(
            "looting", "Looting", "Increases mob drops",
            EnchantmentType.COMBAT, EnchantmentRarity.UNCOMMON, 1, 3,
            new int[]{10, 20, 30}
        ));
        
        // Sweeping Edge
        combatEnchantments.add(new CustomEnchantment(
            "sweeping_edge", "Sweeping Edge", "Increases sweeping attack damage",
            EnchantmentType.COMBAT, EnchantmentRarity.RARE, 1, 3,
            new int[]{15, 30, 45}
        ));
        
        // Knockback
        combatEnchantments.add(new CustomEnchantment(
            "knockback", "Knockback", "Knocks back enemies",
            EnchantmentType.COMBAT, EnchantmentRarity.COMMON, 1, 2,
            new int[]{5, 10}
        ));
        
        // Unbreaking
        combatEnchantments.add(new CustomEnchantment(
            "unbreaking", "Unbreaking", "Increases item durability",
            EnchantmentType.COMBAT, EnchantmentRarity.COMMON, 1, 3,
            new int[]{5, 10, 15}
        ));
        
        // Mending
        combatEnchantments.add(new CustomEnchantment(
            "mending", "Mending", "Repairs item with XP",
            EnchantmentType.COMBAT, EnchantmentRarity.RARE, 1, 1,
            new int[]{50}
        ));
        
        enchantmentsByType.put(EnchantmentType.COMBAT, combatEnchantments);
        combatEnchantments.forEach(enchantment -> enchantments.put(enchantment.getId(), enchantment));
    }
    
    /**
     * Initialize mining enchantments
     */
    private void initializeMiningEnchantments() {
        List<CustomEnchantment> miningEnchantments = new ArrayList<>();
        
        // Efficiency
        miningEnchantments.add(new CustomEnchantment(
            "efficiency", "Efficiency", "Increases mining speed",
            EnchantmentType.MINING, EnchantmentRarity.COMMON, 1, 5,
            new int[]{5, 10, 15, 20, 30}
        ));
        
        // Fortune
        miningEnchantments.add(new CustomEnchantment(
            "fortune", "Fortune", "Increases block drops",
            EnchantmentType.MINING, EnchantmentRarity.UNCOMMON, 1, 3,
            new int[]{10, 20, 30}
        ));
        
        // Silk Touch
        miningEnchantments.add(new CustomEnchantment(
            "silk_touch", "Silk Touch", "Mines blocks in their original form",
            EnchantmentType.MINING, EnchantmentRarity.RARE, 1, 1,
            new int[]{50}
        ));
        
        enchantmentsByType.put(EnchantmentType.MINING, miningEnchantments);
        miningEnchantments.forEach(enchantment -> enchantments.put(enchantment.getId(), enchantment));
    }
    
    /**
     * Initialize farming enchantments
     */
    private void initializeFarmingEnchantments() {
        List<CustomEnchantment> farmingEnchantments = new ArrayList<>();
        
        // Harvesting
        farmingEnchantments.add(new CustomEnchantment(
            "harvesting", "Harvesting", "Increases crop yield",
            EnchantmentType.FARMING, EnchantmentRarity.COMMON, 1, 5,
            new int[]{5, 10, 15, 20, 30}
        ));
        
        // Replenish
        farmingEnchantments.add(new CustomEnchantment(
            "replenish", "Replenish", "Automatically replants crops",
            EnchantmentType.FARMING, EnchantmentRarity.RARE, 1, 1,
            new int[]{50}
        ));
        
        enchantmentsByType.put(EnchantmentType.FARMING, farmingEnchantments);
        farmingEnchantments.forEach(enchantment -> enchantments.put(enchantment.getId(), enchantment));
    }
    
    /**
     * Initialize fishing enchantments
     */
    private void initializeFishingEnchantments() {
        List<CustomEnchantment> fishingEnchantments = new ArrayList<>();
        
        // Lure
        fishingEnchantments.add(new CustomEnchantment(
            "lure", "Lure", "Increases fishing speed",
            EnchantmentType.FISHING, EnchantmentRarity.COMMON, 1, 3,
            new int[]{5, 10, 15}
        ));
        
        // Luck of the Sea
        fishingEnchantments.add(new CustomEnchantment(
            "luck_of_the_sea", "Luck of the Sea", "Increases chance of treasure",
            EnchantmentType.FISHING, EnchantmentRarity.UNCOMMON, 1, 3,
            new int[]{10, 20, 30}
        ));
        
        enchantmentsByType.put(EnchantmentType.FISHING, fishingEnchantments);
        fishingEnchantments.forEach(enchantment -> enchantments.put(enchantment.getId(), enchantment));
    }
    
    /**
     * Initialize special enchantments
     */
    private void initializeSpecialEnchantments() {
        List<CustomEnchantment> specialEnchantments = new ArrayList<>();
        
        // Protection
        specialEnchantments.add(new CustomEnchantment(
            "protection", "Protection", "Reduces damage taken",
            EnchantmentType.SPECIAL, EnchantmentRarity.COMMON, 1, 4,
            new int[]{5, 10, 15, 20}
        ));
        
        // Feather Falling
        specialEnchantments.add(new CustomEnchantment(
            "feather_falling", "Feather Falling", "Reduces fall damage",
            EnchantmentType.SPECIAL, EnchantmentRarity.COMMON, 1, 4,
            new int[]{5, 10, 15, 20}
        ));
        
        // Aqua Affinity
        specialEnchantments.add(new CustomEnchantment(
            "aqua_affinity", "Aqua Affinity", "Increases mining speed underwater",
            EnchantmentType.SPECIAL, EnchantmentRarity.RARE, 1, 1,
            new int[]{30}
        ));
        
        // Respiration
        specialEnchantments.add(new CustomEnchantment(
            "respiration", "Respiration", "Increases underwater breathing time",
            EnchantmentType.SPECIAL, EnchantmentRarity.UNCOMMON, 1, 3,
            new int[]{10, 20, 30}
        ));
        
        enchantmentsByType.put(EnchantmentType.SPECIAL, specialEnchantments);
        specialEnchantments.forEach(enchantment -> enchantments.put(enchantment.getId(), enchantment));
    }
    
    /**
     * Get player enchanting data
     */
    public PlayerEnchantingData getPlayerData(UUID playerId) {
        return playerData.computeIfAbsent(playerId, k -> new PlayerEnchantingData(playerId));
    }
    
    /**
     * Enchant an item
     */
    public boolean enchantItem(Player player, ItemStack item, String enchantmentId, int level) {
        CustomEnchantment enchantment = enchantments.get(enchantmentId);
        if (enchantment == null) return false;
        
        if (level < enchantment.getMinLevel() || level > enchantment.getMaxLevel()) {
            return false;
        }
        
        PlayerEnchantingData playerEnchantingData = getPlayerData(player.getUniqueId());
        int cost = enchantment.getXPCost(level - 1);
        
        if (playerEnchantingData.getEnchantingXP() < cost) {
            player.sendMessage("§cNot enough enchanting XP! Need: " + cost);
            return false;
        }
        
        // Apply enchantment
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        
        // Add custom enchantment lore
        List<Component> lore = meta.lore();
        if (lore == null) lore = new ArrayList<>();
        
        String enchantmentLore = enchantment.getColor() + enchantment.getName() + " " + level;
        lore.add(Component.text(enchantmentLore));
        meta.lore(lore);
        item.setItemMeta(meta);
        
        // Deduct XP
        playerEnchantingData.addEnchantingXP(-cost);
        
        // Add enchanting XP
        playerEnchantingData.addEnchantingXP(level * 2);
        
        player.sendMessage("§aSuccessfully enchanted item with " + enchantment.getName() + " " + level);
        return true;
    }
    
    /**
     * Get available enchantments for item
     */
    public List<CustomEnchantment> getAvailableEnchantments(ItemStack item) {
        List<CustomEnchantment> available = new ArrayList<>();
        
        // Determine item type and get appropriate enchantments
        if (item.getType().name().contains("SWORD") || item.getType().name().contains("AXE")) {
            available.addAll(enchantmentsByType.getOrDefault(EnchantmentType.COMBAT, new ArrayList<>()));
        } else if (item.getType().name().contains("PICKAXE") || item.getType().name().contains("SHOVEL")) {
            available.addAll(enchantmentsByType.getOrDefault(EnchantmentType.MINING, new ArrayList<>()));
        } else if (item.getType().name().contains("HOE")) {
            available.addAll(enchantmentsByType.getOrDefault(EnchantmentType.FARMING, new ArrayList<>()));
        } else if (item.getType().name().contains("FISHING")) {
            available.addAll(enchantmentsByType.getOrDefault(EnchantmentType.FISHING, new ArrayList<>()));
        }
        
        // Always add special enchantments
        available.addAll(enchantmentsByType.getOrDefault(EnchantmentType.SPECIAL, new ArrayList<>()));
        
        return available;
    }
    
    /**
     * Get all enchantments
     */
    public Map<String, CustomEnchantment> getAllEnchantments() {
        return new HashMap<>(enchantments);
    }
    
    /**
     * Get enchantments by type
     */
    public List<CustomEnchantment> getEnchantmentsByType(EnchantmentType type) {
        return enchantmentsByType.getOrDefault(type, new ArrayList<>());
    }
    
    /**
     * Get enchantment by ID
     */
    public CustomEnchantment getEnchantment(String id) {
        return enchantments.get(id);
    }
}
