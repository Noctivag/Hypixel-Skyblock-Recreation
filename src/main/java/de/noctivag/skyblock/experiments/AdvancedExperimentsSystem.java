package de.noctivag.skyblock.experiments;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Experiments System for Hypixel Skyblock-style experiments
 * Includes research, upgrades, and scientific discoveries
 */
public class AdvancedExperimentsSystem {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerExperimentData> playerExperimentData = new ConcurrentHashMap<>();
    private final Map<String, ExperimentType> experimentTypes = new HashMap<>();
    private final Map<String, ResearchProject> researchProjects = new HashMap<>();
    private final Map<UUID, List<ActiveExperiment>> activeExperiments = new ConcurrentHashMap<>();
    
    public AdvancedExperimentsSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeExperimentTypes();
        initializeResearchProjects();
        startExperimentTask();
    }
    
    /**
     * Initialize all experiment types
     */
    private void initializeExperimentTypes() {
        // Mining Experiments
        experimentTypes.put("mining_efficiency", new ExperimentType(
            "Mining Efficiency", 
            Material.IRON_PICKAXE, 
            ExperimentCategory.MINING,
            Arrays.asList("§7Improves mining speed and efficiency", "§7Reduces block breaking time"),
            1000,
            300
        ));
        
        experimentTypes.put("mining_fortune", new ExperimentType(
            "Mining Fortune", 
            Material.EMERALD, 
            ExperimentCategory.MINING,
            Arrays.asList("§7Increases mining fortune", "§7More drops from mining"),
            1500,
            450
        ));
        
        experimentTypes.put("mining_xp", new ExperimentType(
            "Mining XP Boost", 
            Material.EXPERIENCE_BOTTLE, 
            ExperimentCategory.MINING,
            Arrays.asList("§7Increases mining XP gain", "§7Faster skill leveling"),
            2000,
            600
        ));
        
        // Farming Experiments
        experimentTypes.put("farming_growth", new ExperimentType(
            "Farming Growth", 
            Material.WHEAT, 
            ExperimentCategory.FARMING,
            Arrays.asList("§7Speeds up crop growth", "§7Faster harvest cycles"),
            1200,
            360
        ));
        
        experimentTypes.put("farming_fortune", new ExperimentType(
            "Farming Fortune", 
            Material.GOLDEN_HOE, 
            ExperimentCategory.FARMING,
            Arrays.asList("§7Increases farming fortune", "§7More crops per harvest"),
            1800,
            540
        ));
        
        experimentTypes.put("farming_xp", new ExperimentType(
            "Farming XP Boost", 
            Material.EXPERIENCE_BOTTLE, 
            ExperimentCategory.FARMING,
            Arrays.asList("§7Increases farming XP gain", "§7Faster skill leveling"),
            2500,
            750
        ));
        
        // Combat Experiments
        experimentTypes.put("combat_damage", new ExperimentType(
            "Combat Damage", 
            Material.DIAMOND_SWORD, 
            ExperimentCategory.COMBAT,
            Arrays.asList("§7Increases combat damage", "§7More damage to mobs"),
            2000,
            600
        ));
        
        experimentTypes.put("combat_defense", new ExperimentType(
            "Combat Defense", 
            Material.DIAMOND_CHESTPLATE, 
            ExperimentCategory.COMBAT,
            Arrays.asList("§7Increases defense", "§7Reduces incoming damage"),
            2200,
            660
        ));
        
        experimentTypes.put("combat_xp", new ExperimentType(
            "Combat XP Boost", 
            Material.EXPERIENCE_BOTTLE, 
            ExperimentCategory.COMBAT,
            Arrays.asList("§7Increases combat XP gain", "§7Faster skill leveling"),
            3000,
            900
        ));
        
        // Fishing Experiments
        experimentTypes.put("fishing_speed", new ExperimentType(
            "Fishing Speed", 
            Material.FISHING_ROD, 
            ExperimentCategory.FISHING,
            Arrays.asList("§7Increases fishing speed", "§7Faster catch times"),
            1500,
            450
        ));
        
        experimentTypes.put("fishing_luck", new ExperimentType(
            "Fishing Luck", 
            Material.PRISMARINE_SHARD, 
            ExperimentCategory.FISHING,
            Arrays.asList("§7Increases fishing luck", "§7Better catches"),
            2000,
            600
        ));
        
        experimentTypes.put("fishing_xp", new ExperimentType(
            "Fishing XP Boost", 
            Material.EXPERIENCE_BOTTLE, 
            ExperimentCategory.FISHING,
            Arrays.asList("§7Increases fishing XP gain", "§7Faster skill leveling"),
            2500,
            750
        ));
        
        // Foraging Experiments
        experimentTypes.put("foraging_speed", new ExperimentType(
            "Foraging Speed", 
            Material.OAK_LOG, 
            ExperimentCategory.FORAGING,
            Arrays.asList("§7Increases foraging speed", "§7Faster tree chopping"),
            1300,
            390
        ));
        
        experimentTypes.put("foraging_fortune", new ExperimentType(
            "Foraging Fortune", 
            Material.GOLDEN_AXE, 
            ExperimentCategory.FORAGING,
            Arrays.asList("§7Increases foraging fortune", "§7More wood per tree"),
            1700,
            510
        ));
        
        experimentTypes.put("foraging_xp", new ExperimentType(
            "Foraging XP Boost", 
            Material.EXPERIENCE_BOTTLE, 
            ExperimentCategory.FORAGING,
            Arrays.asList("§7Increases foraging XP gain", "§7Faster skill leveling"),
            2300,
            690
        ));
        
        // Special Experiments
        experimentTypes.put("magic_find", new ExperimentType(
            "Magic Find", 
            Material.NETHER_STAR, 
            ExperimentCategory.SPECIAL,
            Arrays.asList("§7Increases magic find", "§7Better rare drops"),
            5000,
            1500
        ));
        
        experimentTypes.put("pet_luck", new ExperimentType(
            "Pet Luck", 
            Material.BONE, 
            ExperimentCategory.SPECIAL,
            Arrays.asList("§7Increases pet luck", "§7Better pet drops"),
            4000,
            1200
        ));
        
        experimentTypes.put("coin_gain", new ExperimentType(
            "Coin Gain", 
            Material.GOLD_INGOT, 
            ExperimentCategory.SPECIAL,
            Arrays.asList("§7Increases coin gain", "§7More coins from activities"),
            3000,
            900
        ));
    }
    
    /**
     * Initialize all research projects
     */
    private void initializeResearchProjects() {
        // Basic Research
        researchProjects.put("basic_mining", new ResearchProject(
            "Basic Mining Research", 
            Material.STONE, 
            Arrays.asList("mining_efficiency"),
            Arrays.asList("§7Unlocks basic mining improvements", "§7Required for advanced mining research"),
            500,
            1000
        ));
        
        researchProjects.put("basic_farming", new ResearchProject(
            "Basic Farming Research", 
            Material.WHEAT, 
            Arrays.asList("farming_growth"),
            Arrays.asList("§7Unlocks basic farming improvements", "§7Required for advanced farming research"),
            500,
            1000
        ));
        
        researchProjects.put("basic_combat", new ResearchProject(
            "Basic Combat Research", 
            Material.IRON_SWORD, 
            Arrays.asList("combat_damage"),
            Arrays.asList("§7Unlocks basic combat improvements", "§7Required for advanced combat research"),
            500,
            1000
        ));
        
        // Advanced Research
        researchProjects.put("advanced_mining", new ResearchProject(
            "Advanced Mining Research", 
            Material.DIAMOND_PICKAXE, 
            Arrays.asList("mining_fortune", "mining_xp"),
            Arrays.asList("§7Unlocks advanced mining improvements", "§7Requires basic mining research"),
            2000,
            5000
        ));
        
        researchProjects.put("advanced_farming", new ResearchProject(
            "Advanced Farming Research", 
            Material.GOLDEN_HOE, 
            Arrays.asList("farming_fortune", "farming_xp"),
            Arrays.asList("§7Unlocks advanced farming improvements", "§7Requires basic farming research"),
            2000,
            5000
        ));
        
        researchProjects.put("advanced_combat", new ResearchProject(
            "Advanced Combat Research", 
            Material.DIAMOND_SWORD, 
            Arrays.asList("combat_defense", "combat_xp"),
            Arrays.asList("§7Unlocks advanced combat improvements", "§7Requires basic combat research"),
            2000,
            5000
        ));
        
        // Special Research
        researchProjects.put("magic_research", new ResearchProject(
            "Magic Research", 
            Material.ENCHANTING_TABLE, 
            Arrays.asList("magic_find", "pet_luck"),
            Arrays.asList("§7Unlocks magic-related improvements", "§7Requires advanced research in all categories"),
            5000,
            15000
        ));
        
        researchProjects.put("economy_research", new ResearchProject(
            "Economy Research", 
            Material.GOLD_BLOCK, 
            Arrays.asList("coin_gain"),
            Arrays.asList("§7Unlocks economy-related improvements", "§7Requires advanced research in all categories"),
            5000,
            15000
        ));
    }
    
    /**
     * Start the experiment task
     */
    private void startExperimentTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateActiveExperiments();
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
    }
    
    /**
     * Update all active experiments
     */
    private void updateActiveExperiments() {
        for (Map.Entry<UUID, List<ActiveExperiment>> entry : activeExperiments.entrySet()) {
            UUID playerId = entry.getKey();
            List<ActiveExperiment> experiments = entry.getValue();
            
            // Remove completed experiments
            experiments.removeIf(ActiveExperiment::isCompleted);
            
            // Update experiment progress
            for (ActiveExperiment experiment : experiments) {
                experiment.updateProgress();
            }
        }
    }
    
    /**
     * Get player experiment data
     */
    public PlayerExperimentData getPlayerExperimentData(UUID playerId) {
        return playerExperimentData.computeIfAbsent(playerId, k -> new PlayerExperimentData());
    }
    
    /**
     * Get experiment type by ID
     */
    public ExperimentType getExperimentType(String experimentId) {
        return experimentTypes.get(experimentId);
    }
    
    /**
     * Get research project by ID
     */
    public ResearchProject getResearchProject(String projectId) {
        return researchProjects.get(projectId);
    }
    
    /**
     * Get all experiment types
     */
    public Map<String, ExperimentType> getAllExperimentTypes() {
        return new HashMap<>(experimentTypes);
    }
    
    /**
     * Get all research projects
     */
    public Map<String, ResearchProject> getAllResearchProjects() {
        return new HashMap<>(researchProjects);
    }
    
    /**
     * Get experiment types by category
     */
    public Map<String, ExperimentType> getExperimentTypesByCategory(ExperimentCategory category) {
        Map<String, ExperimentType> categoryExperiments = new HashMap<>();
        for (Map.Entry<String, ExperimentType> entry : experimentTypes.entrySet()) {
            if (entry.getValue().getCategory() == category) {
                categoryExperiments.put(entry.getKey(), entry.getValue());
            }
        }
        return categoryExperiments;
    }
    
    /**
     * Check if player can start an experiment
     */
    public boolean canStartExperiment(Player player, String experimentId) {
        PlayerExperimentData data = getPlayerExperimentData(player.getUniqueId());
        ExperimentType experiment = getExperimentType(experimentId);
        
        if (experiment == null) return false;
        
        // Check if player has enough coins
        if (data.getCoins() < experiment.getCost()) {
            return false;
        }
        
        // Check if player has required level
        if (data.getLevel() < getRequiredLevel(experimentId)) {
            return false;
        }
        
        // Check if experiment is already active
        List<ActiveExperiment> active = activeExperiments.getOrDefault(player.getUniqueId(), new ArrayList<>());
        for (ActiveExperiment activeExperiment : active) {
            if (activeExperiment.getExperimentType().equals(experimentId)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Start an experiment
     */
    public boolean startExperiment(Player player, String experimentId) {
        if (!canStartExperiment(player, experimentId)) {
            return false;
        }
        
        ExperimentType experiment = getExperimentType(experimentId);
        PlayerExperimentData data = getPlayerExperimentData(player.getUniqueId());
        
        // Remove coins
        data.removeCoins(experiment.getCost());
        
        // Create active experiment
        ActiveExperiment activeExperiment = new ActiveExperiment(experimentId, player.getUniqueId(), experiment.getDuration());
        List<ActiveExperiment> playerExperiments = activeExperiments.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        playerExperiments.add(activeExperiment);
        
        // Update statistics
        data.incrementStartedExperiments();
        data.addExperience(experiment.getCost() / 20);
        
        return true;
    }
    
    /**
     * Complete an experiment
     */
    public boolean completeExperiment(Player player, String experimentId) {
        List<ActiveExperiment> playerExperiments = activeExperiments.get(player.getUniqueId());
        if (playerExperiments == null) return false;
        
        for (ActiveExperiment experiment : playerExperiments) {
            if (experiment.getExperimentType().equals(experimentId) && experiment.isCompleted()) {
                playerExperiments.remove(experiment);
                
                // Apply experiment results
                applyExperimentResults(player, experimentId);
                
                // Update statistics
                PlayerExperimentData data = getPlayerExperimentData(player.getUniqueId());
                data.incrementCompletedExperiments();
                data.addExperience(1000);
                
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Apply experiment results to player
     */
    private void applyExperimentResults(Player player, String experimentId) {
        // This would apply the experiment results to the player
        // For now, we'll just send a message
        player.sendMessage("§aExperiment completed! Results applied.");
    }
    
    /**
     * Get required level for an experiment
     */
    private int getRequiredLevel(String experimentId) {
        ExperimentType experiment = getExperimentType(experimentId);
        if (experiment == null) return 1;
        
        switch (experiment.getCategory()) {
            case MINING:
            case FARMING:
            case COMBAT:
            case FISHING:
            case FORAGING:
                return 5;
            case SPECIAL:
                return 15;
            default:
                return 1;
        }
    }
    
    /**
     * Create an experiment item
     */
    public ItemStack createExperimentItem(String experimentId) {
        ExperimentType experiment = getExperimentType(experimentId);
        if (experiment == null) return null;
        
        ItemStack item = new ItemStack(experiment.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§6" + experiment.getName() + " Experiment");
            List<String> lore = new ArrayList<>(experiment.getDescription());
            lore.add("");
            lore.add("§7Category: " + experiment.getCategory().getDisplayName());
            lore.add("§7Cost: §a" + experiment.getCost() + " coins");
            lore.add("§7Duration: §a" + experiment.getDuration() + " minutes");
            lore.add("");
            lore.add("§7Right-click to start this experiment");
            lore.add("§7and unlock new improvements!");
            lore.add("");
            lore.add("§8A scientific experiment");
            meta.setLore(lore);
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Get player's experiment level
     */
    public int getExperimentLevel(UUID playerId) {
        PlayerExperimentData data = getPlayerExperimentData(playerId);
        return data.getLevel();
    }
    
    /**
     * Get player's experiment experience
     */
    public int getExperimentExperience(UUID playerId) {
        PlayerExperimentData data = getPlayerExperimentData(playerId);
        return data.getExperience();
    }
    
    /**
     * Add experiment experience to player
     */
    public void addExperimentExperience(UUID playerId, int experience) {
        PlayerExperimentData data = getPlayerExperimentData(playerId);
        data.addExperience(experience);
    }
    
    /**
     * Get player's experiment coins
     */
    public int getExperimentCoins(UUID playerId) {
        PlayerExperimentData data = getPlayerExperimentData(playerId);
        return data.getCoins();
    }
    
    /**
     * Add experiment coins to player
     */
    public void addExperimentCoins(UUID playerId, int coins) {
        PlayerExperimentData data = getPlayerExperimentData(playerId);
        data.addCoins(coins);
    }
    
    /**
     * Remove experiment coins from player
     */
    public void removeExperimentCoins(UUID playerId, int coins) {
        PlayerExperimentData data = getPlayerExperimentData(playerId);
        data.removeCoins(coins);
    }
    
    /**
     * Get player's experiment statistics
     */
    public Map<String, Integer> getExperimentStatistics(UUID playerId) {
        PlayerExperimentData data = getPlayerExperimentData(playerId);
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("level", data.getLevel());
        stats.put("experience", data.getExperience());
        stats.put("coins", data.getCoins());
        stats.put("started_experiments", data.getStartedExperiments());
        stats.put("completed_experiments", data.getCompletedExperiments());
        stats.put("total_experience", data.getTotalExperience());
        
        return stats;
    }
    
    /**
     * Get active experiments for a player
     */
    public List<ActiveExperiment> getActiveExperiments(UUID playerId) {
        return activeExperiments.getOrDefault(playerId, new ArrayList<>());
    }
    
    /**
     * Reset player's experiment data
     */
    public void resetExperimentData(UUID playerId) {
        playerExperimentData.remove(playerId);
        activeExperiments.remove(playerId);
    }
    
    /**
     * Save player's experiment data
     */
    public void saveExperimentData(UUID playerId) {
        PlayerExperimentData data = getPlayerExperimentData(playerId);
        // Save to database
        databaseManager.savePlayerExperimentData(playerId, data);
    }
    
    /**
     * Load player's experiment data
     */
    public void loadExperimentData(UUID playerId) {
        try {
            CompletableFuture<Object> future = databaseManager.loadPlayerExperimentData(playerId);
            PlayerExperimentData data = (PlayerExperimentData) future.get();
            if (data != null) {
                playerExperimentData.put(playerId, data);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load experiment data for player " + playerId + ": " + e.getMessage());
        }
    }
    
    /**
     * Shutdown the experiment system
     */
    public void shutdown() {
        // Save all player data
        for (UUID playerId : playerExperimentData.keySet()) {
            saveExperimentData(playerId);
        }
        
        // Clear data
        playerExperimentData.clear();
        activeExperiments.clear();
    }
}
