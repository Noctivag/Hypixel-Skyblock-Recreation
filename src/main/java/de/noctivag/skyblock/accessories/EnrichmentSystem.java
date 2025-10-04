package de.noctivag.skyblock.accessories;

import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * EnrichmentSystem - Hypixel SkyBlock Enrichment System
 * 
 * Features:
 * - Apply enrichments to Legendary+ accessories
 * - Various enrichment types with different stat bonuses
 * - Enrichment management and removal
 * - Integration with accessory bag system
 */
public class EnrichmentSystem implements Listener {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, Map<String, EnrichmentType>> playerEnrichments = new ConcurrentHashMap<>();
    private final Map<EnrichmentType, EnrichmentConfig> enrichmentConfigs = new HashMap<>();
    
    public EnrichmentSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        initializeEnrichmentConfigs();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeEnrichmentConfigs() {
        enrichmentConfigs.put(EnrichmentType.DAMAGE, new EnrichmentConfig(
            "Damage", "§cDamage Enrichment", Material.REDSTONE,
            "§7Increases damage by §a+2", 2.0, 5000
        ));
        enrichmentConfigs.put(EnrichmentType.STRENGTH, new EnrichmentConfig(
            "Strength", "§cStrength Enrichment", Material.IRON_SWORD,
            "§7Increases strength by §a+2", 2.0, 5000
        ));
        enrichmentConfigs.put(EnrichmentType.CRITICAL_CHANCE, new EnrichmentConfig(
            "Critical Chance", "§eCritical Chance Enrichment", Material.EMERALD,
            "§7Increases critical chance by §a+0.2%", 0.2, 5000
        ));
        enrichmentConfigs.put(EnrichmentType.CRITICAL_DAMAGE, new EnrichmentConfig(
            "Critical Damage", "§eCritical Damage Enrichment", Material.DIAMOND,
            "§7Increases critical damage by §a+1%", 1.0, 5000
        ));
        enrichmentConfigs.put(EnrichmentType.INTELLIGENCE, new EnrichmentConfig(
            "Intelligence", "§bIntelligence Enrichment", Material.BOOK,
            "§7Increases intelligence by §a+2", 2.0, 5000
        ));
        enrichmentConfigs.put(EnrichmentType.HEALTH, new EnrichmentConfig(
            "Health", "§aHealth Enrichment", Material.APPLE,
            "§7Increases health by §a+2", 2.0, 5000
        ));
        enrichmentConfigs.put(EnrichmentType.DEFENSE, new EnrichmentConfig(
            "Defense", "§7Defense Enrichment", Material.SHIELD,
            "§7Increases defense by §a+2", 2.0, 5000
        ));
        enrichmentConfigs.put(EnrichmentType.MINING_SPEED, new EnrichmentConfig(
            "Mining Speed", "§7Mining Speed Enrichment", Material.DIAMOND_PICKAXE,
            "§7Increases mining speed by §a+1%", 1.0, 5000
        ));
        enrichmentConfigs.put(EnrichmentType.FARMING_FORTUNE, new EnrichmentConfig(
            "Farming Fortune", "§aFarming Fortune Enrichment", Material.WHEAT,
            "§7Increases farming fortune by §a+1", 1.0, 5000
        ));
        enrichmentConfigs.put(EnrichmentType.FORAGING_FORTUNE, new EnrichmentConfig(
            "Foraging Fortune", "§6Foraging Fortune Enrichment", Material.OAK_LOG,
            "§7Increases foraging fortune by §a+1", 1.0, 5000
        ));
        enrichmentConfigs.put(EnrichmentType.MINING_FORTUNE, new EnrichmentConfig(
            "Mining Fortune", "§7Mining Fortune Enrichment", Material.COAL,
            "§7Increases mining fortune by §a+1", 1.0, 5000
        ));
        enrichmentConfigs.put(EnrichmentType.SPEED, new EnrichmentConfig(
            "Speed", "§fSpeed Enrichment", Material.FEATHER,
            "§7Increases speed by §a+1", 1.0, 5000
        ));
        enrichmentConfigs.put(EnrichmentType.MAGIC_FIND, new EnrichmentConfig(
            "Magic Find", "§dMagic Find Enrichment", Material.ENDER_PEARL,
            "§7Increases magic find by §a+0.5%", 0.5, 5000
        ));
        enrichmentConfigs.put(EnrichmentType.PET_LUCK, new EnrichmentConfig(
            "Pet Luck", "§6Pet Luck Enrichment", Material.BONE,
            "§7Increases pet luck by §a+0.5%", 0.5, 5000
        ));
        enrichmentConfigs.put(EnrichmentType.SEA_CREATURE_CHANCE, new EnrichmentConfig(
            "Sea Creature Chance", "§bSea Creature Chance Enrichment", Material.FISHING_ROD,
            "§7Increases sea creature chance by §a+0.1%", 0.1, 5000
        ));
    }
    
    public void openEnrichmentGUI(Player player, String accessoryId) {
        AccessoryBagSystem.AccessoryConfig accessoryConfig = getAccessoryConfig(accessoryId);
        if (accessoryConfig == null || !canBeEnriched(accessoryConfig)) {
            player.sendMessage("§cThis accessory cannot be enriched!");
            return;
        }
        
        Inventory gui = Bukkit.createInventory(null, 54, net.kyori.adventure.text.Component.text("§d§lEnrich " + accessoryConfig.getDisplayName()));
        
        // Add current enrichment info
        EnrichmentType currentEnrichment = getEnrichment(player.getUniqueId(), accessoryId);
        if (currentEnrichment != null) {
            EnrichmentConfig config = enrichmentConfigs.get(currentEnrichment);
            addGUIItem(gui, 4, config.getMaterial(), "§6§lCurrent Enrichment: " + config.getDisplayName(),
                config.getDescription(),
                "",
                "§eClick to remove enrichment"
            );
        } else {
            addGUIItem(gui, 4, Material.BARRIER, "§c§lNo Enrichment",
                "§7This accessory is not enriched",
                "",
                "§7Select an enrichment below to apply"
            );
        }
        
        // Add available enrichments
        int slot = 18;
        for (EnrichmentType type : EnrichmentType.values()) {
            if (slot >= 45) break;
            
            EnrichmentConfig config = enrichmentConfigs.get(type);
            List<String> lore = new ArrayList<>(Arrays.asList(config.getDescription()));
            lore.add("");
            lore.add("§7Cost: §a" + config.getCost() + " Bits");
            
            if (currentEnrichment == type) {
                lore.add("§cAlready applied!");
            } else {
                lore.add("§eClick to apply this enrichment");
            }
            
            addGUIItem(gui, slot, config.getMaterial(), config.getDisplayName(), 
                lore.toArray(new String[0]));
            slot++;
        }
        
        // Add navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Return to accessory bag");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close this menu");
        
        player.openInventory(gui);
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(net.kyori.adventure.text.Component.text(name));
            if (lore.length > 0) {
                meta.lore(Arrays.stream(lore)
                    .map(net.kyori.adventure.text.Component::text)
                    .collect(java.util.stream.Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        
        String title = event.getView().getTitle();
        if (title.startsWith("§d§lEnrich ")) {
            event.setCancelled(true);
            
            String accessoryName = title.replace("§d§lEnrich ", "");
            String accessoryId = getAccessoryIdByName(accessoryName);
            
            if (accessoryId == null) return;
            
            int slot = event.getSlot();
            
            if (slot == 45) {
                // Back to accessory bag
                AccessoryBagSystem bagSystem = getAccessoryBagSystem();
                if (bagSystem != null) {
                    bagSystem.openAccessoryBagGUI(player);
                }
            } else if (slot == 49) {
                // Close
                player.closeInventory();
            } else if (slot == 4) {
                // Remove current enrichment
                removeEnrichment(player.getUniqueId(), accessoryId);
                player.sendMessage("§aRemoved enrichment from " + accessoryName);
                openEnrichmentGUI(player, accessoryId);
            } else if (slot >= 18 && slot < 45) {
                // Apply enrichment
                EnrichmentType[] types = EnrichmentType.values();
                int enrichmentIndex = slot - 18;
                if (enrichmentIndex < types.length) {
                    EnrichmentType type = types[enrichmentIndex];
                    applyEnrichment(player, accessoryId, type);
                }
            }
        }
    }
    
    public boolean applyEnrichment(Player player, String accessoryId, EnrichmentType type) {
        AccessoryBagSystem.AccessoryConfig accessoryConfig = getAccessoryConfig(accessoryId);
        if (accessoryConfig == null || !canBeEnriched(accessoryConfig)) {
            player.sendMessage("§cThis accessory cannot be enriched!");
            return false;
        }
        
        EnrichmentConfig config = enrichmentConfigs.get(type);
        if (config == null) {
            player.sendMessage("§cInvalid enrichment type!");
            return false;
        }
        
        // Check if player has enough bits
        if (!hasEnoughBits(player, config.getCost())) {
            player.sendMessage("§cYou need " + config.getCost() + " bits to apply this enrichment!");
            return false;
        }
        
        // Remove existing enrichment first
        removeEnrichment(player.getUniqueId(), accessoryId);
        
        // Apply new enrichment
        setEnrichment(player.getUniqueId(), accessoryId, type);
        
        // Deduct bits
        deductBits(player, config.getCost());
        
        player.sendMessage("§aApplied " + config.getDisplayName() + " §ato " + accessoryConfig.getDisplayName());
        openEnrichmentGUI(player, accessoryId);
        return true;
    }
    
    public boolean removeEnrichment(UUID playerId, String accessoryId) {
        Map<String, EnrichmentType> playerEnrichmentMap = playerEnrichments.get(playerId);
        if (playerEnrichmentMap != null) {
            EnrichmentType removed = playerEnrichmentMap.remove(accessoryId);
            return removed != null;
        }
        return false;
    }
    
    public EnrichmentType getEnrichment(UUID playerId, String accessoryId) {
        Map<String, EnrichmentType> playerEnrichmentMap = playerEnrichments.get(playerId);
        if (playerEnrichmentMap != null) {
            return playerEnrichmentMap.get(accessoryId);
        }
        return null;
    }
    
    private void setEnrichment(UUID playerId, String accessoryId, EnrichmentType type) {
        playerEnrichments.computeIfAbsent(playerId, k -> new HashMap<>()).put(accessoryId, type);
    }
    
    private boolean canBeEnriched(AccessoryBagSystem.AccessoryConfig config) {
        return config.getRarity() == AccessoryBagSystem.AccessoryRarity.LEGENDARY ||
               config.getRarity() == AccessoryBagSystem.AccessoryRarity.MYTHIC;
    }
    
    private boolean hasEnoughBits(Player player, int cost) {
        // This would integrate with your economy system
        // For now, return true for testing
        return true;
    }
    
    private void deductBits(Player player, int cost) {
        // This would integrate with your economy system
        // For now, just send a message
        player.sendMessage("§7Deducted " + cost + " bits from your account");
    }
    
    private AccessoryBagSystem.AccessoryConfig getAccessoryConfig(String accessoryId) {
        // This would get the config from AccessoryBagSystem
        // For now, return null
        return null;
    }
    
    private String getAccessoryIdByName(String accessoryName) {
        // This would find the accessory ID by name
        // For now, return null
        return null;
    }
    
    private AccessoryBagSystem getAccessoryBagSystem() {
        // This would get the AccessoryBagSystem instance
        // For now, return null
        return null;
    }
    
    public Map<EnrichmentType, Double> getTotalEnrichmentBonuses(UUID playerId) {
        Map<EnrichmentType, Double> totalBonuses = new HashMap<>();
        
        Map<String, EnrichmentType> playerEnrichmentMap = playerEnrichments.get(playerId);
        if (playerEnrichmentMap != null) {
            for (EnrichmentType type : playerEnrichmentMap.values()) {
                EnrichmentConfig config = enrichmentConfigs.get(type);
                if (config != null) {
                    totalBonuses.merge(type, config.getBonusValue(), Double::sum);
                }
            }
        }
        
        return totalBonuses;
    }
    
    public enum EnrichmentType {
        DAMAGE,
        STRENGTH,
        CRITICAL_CHANCE,
        CRITICAL_DAMAGE,
        INTELLIGENCE,
        HEALTH,
        DEFENSE,
        MINING_SPEED,
        FARMING_FORTUNE,
        FORAGING_FORTUNE,
        MINING_FORTUNE,
        SPEED,
        MAGIC_FIND,
        PET_LUCK,
        SEA_CREATURE_CHANCE
    }
    
    public static class EnrichmentConfig {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final double bonusValue;
        private final int cost;
        
        public EnrichmentConfig(String name, String displayName, Material material, 
                              String description, double bonusValue, int cost) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.bonusValue = bonusValue;
            this.cost = cost;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public double getBonusValue() { return bonusValue; }
        public int getCost() { return cost; }
    }
}
