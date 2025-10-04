package de.noctivag.plugin.skyblock.minions;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Minions System für Hypixel SkyBlock
 */
public class MinionsSystem implements Listener {
    
    private final Plugin plugin;
    private final Map<UUID, List<Minion>> playerMinions = new ConcurrentHashMap<>();
    private final Map<UUID, Minion> activeMinions = new ConcurrentHashMap<>();
    
    // Minion Definition
    public static class Minion {
        private final String id;
        private final String name;
        private final MinionType type;
        private final int level;
        private final int xp;
        private final Location location;
        private final UUID ownerId;
        private final long lastAction;
        private final int actionCount;
        private final boolean isActive;
        private final List<ItemStack> inventory;
        private final int maxInventorySize;
        
        public Minion(String id, String name, MinionType type, int level, int xp, Location location, 
                     UUID ownerId, long lastAction, int actionCount, boolean isActive, 
                     List<ItemStack> inventory, int maxInventorySize) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.level = level;
            this.xp = xp;
            this.location = location;
            this.ownerId = ownerId;
            this.lastAction = lastAction;
            this.actionCount = actionCount;
            this.isActive = isActive;
            this.inventory = inventory;
            this.maxInventorySize = maxInventorySize;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public MinionType getType() { return type; }
        public int getLevel() { return level; }
        public int getXp() { return xp; }
        public Location getLocation() { return location; }
        public UUID getOwnerId() { return ownerId; }
        public long getLastAction() { return lastAction; }
        public int getActionCount() { return actionCount; }
        public boolean isActive() { return isActive; }
        public List<ItemStack> getInventory() { return inventory; }
        public int getMaxInventorySize() { return maxInventorySize; }
        
        public long getActionInterval() {
            return getActionIntervalForLevel(level);
        }
        
        public int getXpForNextLevel() {
            return getXpRequiredForLevel(level + 1) - xp;
        }
        
        public int getXpRequiredForLevel(int targetLevel) {
            if (targetLevel <= 1) return 0;
            return (int) (100 * Math.pow(1.5, targetLevel - 1));
        }
        
        public boolean canPerformAction() {
            return System.currentTimeMillis() - lastAction >= getActionInterval();
        }
        
        public void performAction() {
            // Hier würde die Minion-Aktion implementiert werden
            // z.B. Block abbauen, Item sammeln, etc.
        }
        
        public void addToInventory(ItemStack item) {
            if (inventory.size() < maxInventorySize) {
                inventory.add(item);
            }
        }
        
        public boolean isInventoryFull() {
            return inventory.size() >= maxInventorySize;
        }
    }
    
    // Minion Types
    public enum MinionType {
        COAL("Coal Minion", "⛏", "Sammelt Kohle", Material.COAL_ORE, Material.COAL),
        IRON("Iron Minion", "⛏", "Sammelt Eisen", Material.IRON_ORE, Material.IRON_INGOT),
        GOLD("Gold Minion", "⛏", "Sammelt Gold", Material.GOLD_ORE, Material.GOLD_INGOT),
        DIAMOND("Diamond Minion", "⛏", "Sammelt Diamanten", Material.DIAMOND_ORE, Material.DIAMOND),
        EMERALD("Emerald Minion", "⛏", "Sammelt Smaragde", Material.EMERALD_ORE, Material.EMERALD),
        REDSTONE("Redstone Minion", "⛏", "Sammelt Redstone", Material.REDSTONE_ORE, Material.REDSTONE),
        LAPIS("Lapis Minion", "⛏", "Sammelt Lapis", Material.LAPIS_ORE, Material.LAPIS_LAZULI),
        QUARTZ("Quartz Minion", "⛏", "Sammelt Quarz", Material.NETHER_QUARTZ_ORE, Material.QUARTZ),
        OBSIDIAN("Obsidian Minion", "⛏", "Sammelt Obsidian", Material.OBSIDIAN, Material.OBSIDIAN),
        GLOWSTONE("Glowstone Minion", "⛏", "Sammelt Glowstone", Material.GLOWSTONE, Material.GLOWSTONE),
        GRAVEL("Gravel Minion", "⛏", "Sammelt Kies", Material.GRAVEL, Material.GRAVEL),
        SAND("Sand Minion", "⛏", "Sammelt Sand", Material.SAND, Material.SAND),
        CLAY("Clay Minion", "⛏", "Sammelt Ton", Material.CLAY, Material.CLAY),
        ICE("Ice Minion", "⛏", "Sammelt Eis", Material.ICE, Material.ICE),
        SNOW("Snow Minion", "⛏", "Sammelt Schnee", Material.SNOW, Material.SNOWBALL),
        NETHERRACK("Netherrack Minion", "⛏", "Sammelt Netherrack", Material.NETHERRACK, Material.NETHERRACK),
        SOUL_SAND("Soul Sand Minion", "⛏", "Sammelt Soul Sand", Material.SOUL_SAND, Material.SOUL_SAND),
        END_STONE("End Stone Minion", "⛏", "Sammelt End Stone", Material.END_STONE, Material.END_STONE),
        OAK("Oak Minion", "🌲", "Sammelt Eichenholz", Material.OAK_LOG, Material.OAK_LOG),
        BIRCH("Birch Minion", "🌲", "Sammelt Birkenholz", Material.BIRCH_LOG, Material.BIRCH_LOG),
        SPRUCE("Spruce Minion", "🌲", "Sammelt Fichtenholz", Material.SPRUCE_LOG, Material.SPRUCE_LOG),
        JUNGLE("Jungle Minion", "🌲", "Sammelt Dschungelholz", Material.JUNGLE_LOG, Material.JUNGLE_LOG),
        ACACIA("Acacia Minion", "🌲", "Sammelt Akazienholz", Material.ACACIA_LOG, Material.ACACIA_LOG),
        DARK_OAK("Dark Oak Minion", "🌲", "Sammelt Schwarzeichenholz", Material.DARK_OAK_LOG, Material.DARK_OAK_LOG),
        MANGROVE("Mangrove Minion", "🌲", "Sammelt Mangrovenholz", Material.MANGROVE_LOG, Material.MANGROVE_LOG),
        CHERRY("Cherry Minion", "🌲", "Sammelt Kirschholz", Material.CHERRY_LOG, Material.CHERRY_LOG),
        WHEAT("Wheat Minion", "🌾", "Sammelt Weizen", Material.WHEAT, Material.WHEAT),
        CARROT("Carrot Minion", "🌾", "Sammelt Karotten", Material.CARROT, Material.CARROT),
        POTATO("Potato Minion", "🌾", "Sammelt Kartoffeln", Material.POTATO, Material.POTATO),
        BEETROOT("Beetroot Minion", "🌾", "Sammelt Rüben", Material.BEETROOT, Material.BEETROOT),
        NETHER_WART("Nether Wart Minion", "🌾", "Sammelt Nether Wart", Material.NETHER_WART, Material.NETHER_WART),
        SUGAR_CANE("Sugar Cane Minion", "🌾", "Sammelt Zuckerrohr", Material.SUGAR_CANE, Material.SUGAR_CANE),
        CACTUS("Cactus Minion", "🌾", "Sammelt Kakteen", Material.CACTUS, Material.CACTUS),
        MELON("Melon Minion", "🌾", "Sammelt Melonen", Material.MELON, Material.MELON),
        PUMPKIN("Pumpkin Minion", "🌾", "Sammelt Kürbisse", Material.PUMPKIN, Material.PUMPKIN),
        COCOA("Cocoa Minion", "🌾", "Sammelt Kakao", Material.COCOA_BEANS, Material.COCOA_BEANS),
        MUSHROOM("Mushroom Minion", "🌾", "Sammelt Pilze", Material.RED_MUSHROOM, Material.RED_MUSHROOM),
        FISH("Fish Minion", "🎣", "Sammelt Fisch", Material.COD, Material.COD),
        SALMON("Salmon Minion", "🎣", "Sammelt Lachs", Material.SALMON, Material.SALMON),
        PUFFERFISH("Pufferfish Minion", "🎣", "Sammelt Kugelfisch", Material.PUFFERFISH, Material.PUFFERFISH),
        TROPICAL_FISH("Tropical Fish Minion", "🎣", "Sammelt Tropenfisch", Material.TROPICAL_FISH, Material.TROPICAL_FISH),
        INK_SAC("Ink Sac Minion", "🎣", "Sammelt Tintenbeutel", Material.INK_SAC, Material.INK_SAC),
        SPONGE("Sponge Minion", "🎣", "Sammelt Schwämme", Material.SPONGE, Material.SPONGE),
        PRISMARINE_SHARD("Prismarine Shard Minion", "🎣", "Sammelt Prismarine Shards", Material.PRISMARINE_SHARD, Material.PRISMARINE_SHARD),
        PRISMARINE_CRYSTALS("Prismarine Crystals Minion", "🎣", "Sammelt Prismarine Crystals", Material.PRISMARINE_CRYSTALS, Material.PRISMARINE_CRYSTALS),
        ROTTEN_FLESH("Rotten Flesh Minion", "⚔", "Sammelt Verrottetes Fleisch", Material.ROTTEN_FLESH, Material.ROTTEN_FLESH),
        BONE("Bone Minion", "⚔", "Sammelt Knochen", Material.BONE, Material.BONE),
        STRING("String Minion", "⚔", "Sammelt Faden", Material.STRING, Material.STRING),
        SPIDER_EYE("Spider Eye Minion", "⚔", "Sammelt Spinnenaugen", Material.SPIDER_EYE, Material.SPIDER_EYE),
        GUNPOWDER("Gunpowder Minion", "⚔", "Sammelt Schießpulver", Material.GUNPOWDER, Material.GUNPOWDER),
        ENDER_PEARL("Ender Pearl Minion", "⚔", "Sammelt Ender Perlen", Material.ENDER_PEARL, Material.ENDER_PEARL),
        BLAZE_ROD("Blaze Rod Minion", "⚔", "Sammelt Blaze Stäbe", Material.BLAZE_ROD, Material.BLAZE_ROD),
        GHAST_TEAR("Ghast Tear Minion", "⚔", "Sammelt Ghast Tränen", Material.GHAST_TEAR, Material.GHAST_TEAR),
        MAGMA_CREAM("Magma Cream Minion", "⚔", "Sammelt Magma Creme", Material.MAGMA_CREAM, Material.MAGMA_CREAM),
        NETHER_STAR("Nether Star Minion", "⚔", "Sammelt Nether Sterne", Material.NETHER_STAR, Material.NETHER_STAR),
        DRAGON_EGG("Dragon Egg Minion", "⚔", "Sammelt Dracheneier", Material.DRAGON_EGG, Material.DRAGON_EGG);
        
        private final String name;
        private final String icon;
        private final String description;
        private final Material sourceBlock;
        private final Material outputItem;
        
        MinionType(String name, String icon, String description, Material sourceBlock, Material outputItem) {
            this.name = name;
            this.icon = icon;
            this.description = description;
            this.sourceBlock = sourceBlock;
            this.outputItem = outputItem;
        }
        
        public String getName() { return name; }
        public String getIcon() { return icon; }
        public String getDescription() { return description; }
        public Material getSourceBlock() { return sourceBlock; }
        public Material getOutputItem() { return outputItem; }
    }
    
    public MinionsSystem(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startMinionTick();
    }
    
    /**
     * Startet den Minion-Tick
     */
    private void startMinionTick() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (Minion minion : activeMinions.values()) {
                if (minion.canPerformAction()) {
                    minion.performAction();
                }
            }
        }, 20L, 20L); // Jede Sekunde
    }
    
    /**
     * Erstellt einen neuen Minion
     */
    public boolean createMinion(Player player, MinionType type, Location location) {
        UUID playerId = player.getUniqueId();
        
        // Prüfe ob Spieler bereits zu viele Minions hat
        List<Minion> minions = playerMinions.getOrDefault(playerId, new ArrayList<>());
        int maxMinions = getMaxMinionsForPlayer(playerId);
        if (minions.size() >= maxMinions) {
            player.sendMessage("§cDu hast bereits die maximale Anzahl an Minions!");
            return false;
        }
        
        // Erstelle Minion
        String minionId = UUID.randomUUID().toString();
        Minion minion = new Minion(minionId, type.getName(), type, 1, 0, location, playerId, 
                                 System.currentTimeMillis(), 0, true, new ArrayList<>(), 15);
        
        // Füge zu Spieler-Minions hinzu
        minions.add(minion);
        playerMinions.put(playerId, minions);
        activeMinions.put(UUID.fromString(minionId), minion);
        
        player.sendMessage("§aMinion erstellt!");
        player.sendMessage("§e" + type.getIcon() + " " + type.getName());
        player.sendMessage("§7" + type.getDescription());
        
        return true;
    }
    
    /**
     * Entfernt einen Minion
     */
    public boolean removeMinion(Player player, String minionId) {
        UUID playerId = player.getUniqueId();
        List<Minion> minions = playerMinions.get(playerId);
        
        if (minions == null) {
            player.sendMessage("§cDu hast keine Minions!");
            return false;
        }
        
        Minion minion = minions.stream()
            .filter(m -> m.getId().equals(minionId))
            .findFirst()
            .orElse(null);
        
        if (minion == null) {
            player.sendMessage("§cMinion nicht gefunden!");
            return false;
        }
        
        // Entferne Minion
        minions.remove(minion);
        activeMinions.remove(UUID.fromString(minionId));
        
        player.sendMessage("§aMinion entfernt!");
        
        return true;
    }
    
    /**
     * Sammelt Items von einem Minion
     */
    public boolean collectFromMinion(Player player, String minionId) {
        UUID playerId = player.getUniqueId();
        List<Minion> minions = playerMinions.get(playerId);
        
        if (minions == null) {
            player.sendMessage("§cDu hast keine Minions!");
            return false;
        }
        
        Minion minion = minions.stream()
            .filter(m -> m.getId().equals(minionId))
            .findFirst()
            .orElse(null);
        
        if (minion == null) {
            player.sendMessage("§cMinion nicht gefunden!");
            return false;
        }
        
        if (minion.getInventory().isEmpty()) {
            player.sendMessage("§cMinion hat keine Items!");
            return false;
        }
        
        // Gib Items an Spieler
        for (ItemStack item : minion.getInventory()) {
            player.getInventory().addItem(item);
        }
        
        // Leere Minion-Inventar
        minion.getInventory().clear();
        
        player.sendMessage("§aItems gesammelt!");
        player.sendMessage("§7" + minion.getInventory().size() + " Items erhalten");
        
        return true;
    }
    
    /**
     * Gibt die maximale Anzahl an Minions für einen Spieler zurück
     */
    private int getMaxMinionsForPlayer(UUID playerId) {
        // Hier würde normalerweise das SkyBlock-Level geprüft werden
        // Für jetzt geben wir eine feste Anzahl zurück
        return 5;
    }
    
    /**
     * Gibt das Aktions-Intervall für ein Level zurück
     */
    private static long getActionIntervalForLevel(int level) {
        // Höhere Level = schnellere Aktionen
        return Math.max(1000, 5000 - (level * 100)); // Mindestens 1 Sekunde
    }
    
    /**
     * Gibt alle Minions eines Spielers zurück
     */
    public List<Minion> getPlayerMinions(UUID playerId) {
        return new ArrayList<>(playerMinions.getOrDefault(playerId, new ArrayList<>()));
    }
    
    /**
     * Gibt einen Minion zurück
     */
    public Minion getMinion(String minionId) {
        return activeMinions.get(UUID.fromString(minionId));
    }
    
    /**
     * Gibt alle aktiven Minions zurück
     */
    public Map<UUID, Minion> getActiveMinions() {
        return new HashMap<>(activeMinions);
    }
    
    /**
     * Gibt Minions eines bestimmten Typs zurück
     */
    public List<Minion> getMinionsByType(MinionType type) {
        return activeMinions.values().stream()
            .filter(minion -> minion.getType() == type)
            .toList();
    }
}
