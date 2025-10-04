package de.noctivag.plugin.skyblock.collections;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Collections-System für Hypixel SkyBlock
 */
public class CollectionsSystem implements Listener {
    
    private final Plugin plugin;
    private final Map<UUID, Map<String, Integer>> playerCollections = new ConcurrentHashMap<>();
    
    // Collection-Definitionen
    public enum Collection {
        COBBLESTONE("Cobblestone", Material.COBBLESTONE, "Mining"),
        COAL("Coal", Material.COAL, "Mining"),
        IRON("Iron", Material.IRON_INGOT, "Mining"),
        GOLD("Gold", Material.GOLD_INGOT, "Mining"),
        DIAMOND("Diamond", Material.DIAMOND, "Mining"),
        EMERALD("Emerald", Material.EMERALD, "Mining"),
        REDSTONE("Redstone", Material.REDSTONE, "Mining"),
        LAPIS("Lapis Lazuli", Material.LAPIS_LAZULI, "Mining"),
        QUARTZ("Quartz", Material.QUARTZ, "Mining"),
        OBSIDIAN("Obsidian", Material.OBSIDIAN, "Mining"),
        GLOWSTONE("Glowstone", Material.GLOWSTONE, "Mining"),
        GRAVEL("Gravel", Material.GRAVEL, "Mining"),
        SAND("Sand", Material.SAND, "Mining"),
        CLAY("Clay", Material.CLAY, "Mining"),
        ICE("Ice", Material.ICE, "Mining"),
        SNOW("Snow", Material.SNOWBALL, "Mining"),
        NETHERRACK("Netherrack", Material.NETHERRACK, "Mining"),
        SOUL_SAND("Soul Sand", Material.SOUL_SAND, "Mining"),
        END_STONE("End Stone", Material.END_STONE, "Mining"),
        OAK_LOG("Oak Wood", Material.OAK_LOG, "Foraging"),
        BIRCH_LOG("Birch Wood", Material.BIRCH_LOG, "Foraging"),
        SPRUCE_LOG("Spruce Wood", Material.SPRUCE_LOG, "Foraging"),
        JUNGLE_LOG("Jungle Wood", Material.JUNGLE_LOG, "Foraging"),
        ACACIA_LOG("Acacia Wood", Material.ACACIA_LOG, "Foraging"),
        DARK_OAK_LOG("Dark Oak Wood", Material.DARK_OAK_LOG, "Foraging"),
        MANGROVE_LOG("Mangrove Wood", Material.MANGROVE_LOG, "Foraging"),
        CHERRY_LOG("Cherry Wood", Material.CHERRY_LOG, "Foraging"),
        WHEAT("Wheat", Material.WHEAT, "Farming"),
        CARROT("Carrot", Material.CARROT, "Farming"),
        POTATO("Potato", Material.POTATO, "Farming"),
        BEETROOT("Beetroot", Material.BEETROOT, "Farming"),
        NETHER_WART("Nether Wart", Material.NETHER_WART, "Farming"),
        SUGAR_CANE("Sugar Cane", Material.SUGAR_CANE, "Farming"),
        CACTUS("Cactus", Material.CACTUS, "Farming"),
        MELON("Melon", Material.MELON, "Farming"),
        PUMPKIN("Pumpkin", Material.PUMPKIN, "Farming"),
        COCOA_BEANS("Cocoa Beans", Material.COCOA_BEANS, "Farming"),
        MUSHROOM("Mushroom", Material.RED_MUSHROOM, "Farming"),
        RAW_FISH("Raw Fish", Material.COD, "Fishing"),
        RAW_SALMON("Raw Salmon", Material.SALMON, "Fishing"),
        PUFFERFISH("Pufferfish", Material.PUFFERFISH, "Fishing"),
        TROPICAL_FISH("Tropical Fish", Material.TROPICAL_FISH, "Fishing"),
        INK_SAC("Ink Sac", Material.INK_SAC, "Fishing"),
        SPONGE("Sponge", Material.SPONGE, "Fishing"),
        PRISMARINE_SHARD("Prismarine Shard", Material.PRISMARINE_SHARD, "Fishing"),
        PRISMARINE_CRYSTALS("Prismarine Crystals", Material.PRISMARINE_CRYSTALS, "Fishing"),
        ROTTEN_FLESH("Rotten Flesh", Material.ROTTEN_FLESH, "Combat"),
        BONE("Bone", Material.BONE, "Combat"),
        STRING("String", Material.STRING, "Combat"),
        SPIDER_EYE("Spider Eye", Material.SPIDER_EYE, "Combat"),
        GUNPOWDER("Gunpowder", Material.GUNPOWDER, "Combat"),
        ENDER_PEARL("Ender Pearl", Material.ENDER_PEARL, "Combat"),
        BLAZE_ROD("Blaze Rod", Material.BLAZE_ROD, "Combat"),
        GHAST_TEAR("Ghast Tear", Material.GHAST_TEAR, "Combat"),
        MAGMA_CREAM("Magma Cream", Material.MAGMA_CREAM, "Combat"),
        NETHER_STAR("Nether Star", Material.NETHER_STAR, "Combat"),
        DRAGON_EGG("Dragon Egg", Material.DRAGON_EGG, "Combat");
        
        private final String name;
        private final Material material;
        private final String category;
        
        Collection(String name, Material material, String category) {
            this.name = name;
            this.material = material;
            this.category = category;
        }
        
        public String getName() { return name; }
        public Material getMaterial() { return material; }
        public String getCategory() { return category; }
    }
    
    // Collection-Milestones
    private static final Map<Integer, Integer> MILESTONES = Map.of(
        50, 1,
        100, 2,
        250, 3,
        1000, 4,
        2500, 5,
        5000, 6,
        10000, 7,
        25000, 8,
        50000, 9,
        100000, 10
    );
    
    public CollectionsSystem(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Fügt Items zu einer Collection hinzu
     */
    public void addToCollection(UUID playerId, Material material, int amount) {
        Collection collection = getCollectionByMaterial(material);
        if (collection == null) return;
        
        Map<String, Integer> collections = playerCollections.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>());
        int currentAmount = collections.getOrDefault(collection.name(), 0);
        int newAmount = currentAmount + amount;
        collections.put(collection.name(), newAmount);
        
        // Prüfe Milestones
        checkMilestones(playerId, collection, newAmount);
    }
    
    /**
     * Gibt die Collection-Menge für einen Spieler zurück
     */
    public int getCollectionAmount(UUID playerId, Collection collection) {
        return playerCollections.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>())
            .getOrDefault(collection.name(), 0);
    }
    
    /**
     * Prüft Milestones
     */
    private void checkMilestones(UUID playerId, Collection collection, int amount) {
        for (Map.Entry<Integer, Integer> milestone : MILESTONES.entrySet()) {
            if (amount >= milestone.getKey()) {
                Player player = plugin.getServer().getPlayer(playerId);
                if (player != null) {
                    player.sendMessage("§6§lCOLLECTION MILESTONE!");
                    player.sendMessage("§e" + collection.getName() + " Level " + milestone.getValue());
                    player.sendMessage("§7" + amount + " " + collection.getName() + " gesammelt!");
                }
            }
        }
    }
    
    /**
     * Event-Handler für Block-Break
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material material = event.getBlock().getType();
        
        // Bestimme die Menge basierend auf dem Material
        int amount = getCollectionAmount(material);
        addToCollection(player.getUniqueId(), material, amount);
    }
    
    /**
     * Event-Handler für Entity-Death
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            
            // Füge Drops zur Collection hinzu
            for (ItemStack drop : event.getDrops()) {
                addToCollection(player.getUniqueId(), drop.getType(), drop.getAmount());
            }
        }
    }
    
    /**
     * Event-Handler für Fishing
     */
    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH && event.getCaught() != null) {
            Player player = event.getPlayer();
            Entity caughtEntity = event.getCaught();
            // Handle caught fish entity - this would need proper fish item conversion
            // For now, just add a generic fish to collection
            addToCollection(player.getUniqueId(), Material.COD, 1);
        }
    }
    
    /**
     * Gibt die Collection-Menge für ein Material zurück
     */
    private int getCollectionAmount(Material material) {
        return switch (material) {
            case COAL_ORE -> 1;
            case IRON_ORE -> 1;
            case GOLD_ORE -> 1;
            case DIAMOND_ORE -> 1;
            case EMERALD_ORE -> 1;
            case REDSTONE_ORE -> 4;
            case LAPIS_ORE -> 8;
            case NETHER_QUARTZ_ORE -> 1;
            case OAK_LOG, BIRCH_LOG, SPRUCE_LOG, JUNGLE_LOG, ACACIA_LOG, DARK_OAK_LOG, MANGROVE_LOG, CHERRY_LOG -> 1;
            case WHEAT -> 1;
            case CARROT, POTATO, BEETROOT -> 1;
            case NETHER_WART -> 1;
            case SUGAR_CANE -> 1;
            case CACTUS -> 1;
            case MELON -> 3;
            case PUMPKIN -> 1;
            case COCOA_BEANS -> 3;
            default -> 1;
        };
    }
    
    /**
     * Gibt eine Collection anhand des Materials zurück
     */
    private Collection getCollectionByMaterial(Material material) {
        for (Collection collection : Collection.values()) {
            if (collection.getMaterial() == material) {
                return collection;
            }
        }
        return null;
    }
    
    /**
     * Gibt alle Collections eines Spielers zurück
     */
    public Map<Collection, Integer> getAllCollections(UUID playerId) {
        Map<Collection, Integer> collections = new HashMap<>();
        Map<String, Integer> playerData = playerCollections.getOrDefault(playerId, new HashMap<>());
        
        for (Collection collection : Collection.values()) {
            collections.put(collection, playerData.getOrDefault(collection.name(), 0));
        }
        
        return collections;
    }
    
    /**
     * Gibt Collections einer bestimmten Kategorie zurück
     */
    public Map<Collection, Integer> getCollectionsByCategory(UUID playerId, String category) {
        return getAllCollections(playerId).entrySet().stream()
            .filter(entry -> entry.getKey().getCategory().equals(category))
            .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), HashMap::putAll);
    }
}
