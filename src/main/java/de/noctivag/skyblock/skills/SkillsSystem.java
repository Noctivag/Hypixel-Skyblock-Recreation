package de.noctivag.skyblock.skills;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.material.Crops;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main Skills System - Manages player skill progression and XP tracking
 */
public class SkillsSystem implements Service, Listener {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerSkills> playerSkills;
    private SystemStatus status = SystemStatus.UNINITIALIZED;

    public SkillsSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.playerSkills = new ConcurrentHashMap<>();
    }

    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing SkillsSystem...");

        // Register event listeners
        Bukkit.getPluginManager().registerEvents(this, plugin);

        // Load all online player data
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadPlayerSkills(player.getUniqueId());
        }

        status = SystemStatus.RUNNING;
        plugin.getLogger().info("SkillsSystem initialized successfully!");
    }

    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down SkillsSystem...");

        // Save all player data
        saveAllPlayerSkills();

        playerSkills.clear();
        status = SystemStatus.SHUTDOWN;
        plugin.getLogger().info("SkillsSystem shutdown complete!");
    }

    @Override
    public String getName() {
        return "SkillsSystem";
    }

    @Override
    public SystemStatus getStatus() {
        return status;
    }

    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }

    @Override
    public void setEnabled(boolean enabled) {
        // Skills system is always enabled when running
    }

    /**
     * Load player skills from database
     */
    public void loadPlayerSkills(UUID playerId) {
        try {
            // Load from database (placeholder - implement actual database loading)
            PlayerSkills skills = new PlayerSkills(playerId);
            playerSkills.put(playerId, skills);
            plugin.getLogger().info("Loaded skills for player: " + playerId);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load skills for player " + playerId + ": " + e.getMessage());
            // Create new skills if loading fails
            playerSkills.put(playerId, new PlayerSkills(playerId));
        }
    }

    /**
     * Save player skills to database
     */
    public void savePlayerSkills(UUID playerId) {
        try {
            PlayerSkills skills = playerSkills.get(playerId);
            if (skills != null) {
                // Save to database (placeholder - implement actual database saving)
                plugin.getLogger().info("Saved skills for player: " + playerId);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save skills for player " + playerId + ": " + e.getMessage());
        }
    }

    /**
     * Save all player skills
     */
    public void saveAllPlayerSkills() {
        for (UUID playerId : playerSkills.keySet()) {
            savePlayerSkills(playerId);
        }
    }
    
    /**
     * Get or create player skills for UUID
     */
    public PlayerSkills getOrCreatePlayerSkills(UUID playerUUID) {
        // TODO: Implement proper player skills management
        return new PlayerSkills(playerUUID);
    }

    /**
     * Add XP to player skill
     */
    public void addXP(Player player, String skillName, int xpAmount) {
        PlayerSkills skills = getOrCreatePlayerSkills(player.getUniqueId());
        // TODO: Implement proper XP addition based on skill name
        plugin.getLogger().info("Added " + xpAmount + " XP to " + skillName + " for player " + player.getName());
    }

    /**
     * Get player skills
     */
    public PlayerSkills getPlayerSkills(UUID playerId) {
        return playerSkills.get(playerId);
    }

    /**
     * Get player skills (by Player object)
     */
    public PlayerSkills getPlayerSkills(Player player) {
        return getPlayerSkills(player.getUniqueId());
    }

    /**
     * Add XP to a player's skill
     */
    public boolean addSkillXP(Player player, SkillType skill, long xp) {
        PlayerSkills skills = getPlayerSkills(player);
        if (skills == null) {
            loadPlayerSkills(player.getUniqueId());
            skills = getPlayerSkills(player);
        }

        boolean levelUp = skills.addXP(skill, xp);
        
        if (levelUp) {
            int newLevel = skills.getLevel(skill);
            player.sendMessage("§a§lSKILL LEVEL UP! §r" + skill.getColor() + skill.getIcon() + " " + skill.getDisplayName() + " §a" + newLevel);
            player.sendMessage("§7" + skill.getDescription());
            
            // Apply stat bonuses
            applySkillBonuses(player);
        }

        return levelUp;
    }

    /**
     * Apply skill bonuses to player
     */
    public void applySkillBonuses(Player player) {
        PlayerSkills skills = getPlayerSkills(player);
        if (skills == null) return;

        SkillBonuses combinedBonuses = skills.getCombinedBonuses();
        
        // Apply bonuses to player (placeholder - implement actual stat application)
        // This would typically involve updating player attributes, health, etc.
        plugin.getLogger().info("Applied skill bonuses to player " + player.getName() + 
            " - Health: +" + combinedBonuses.getHealthBonus() + 
            ", Defense: +" + combinedBonuses.getDefenseBonus() +
            ", Strength: +" + combinedBonuses.getStrengthBonus());
    }

    /**
     * Get XP for breaking a specific block type
     */
    private long getMiningXP(org.bukkit.Material material) {
        switch (material) {
            case COAL_ORE:
            case DEEPSLATE_COAL_ORE:
                return 5;
            case IRON_ORE:
            case DEEPSLATE_IRON_ORE:
                return 10;
            case GOLD_ORE:
            case DEEPSLATE_GOLD_ORE:
                return 15;
            case DIAMOND_ORE:
            case DEEPSLATE_DIAMOND_ORE:
                return 25;
            case EMERALD_ORE:
            case DEEPSLATE_EMERALD_ORE:
                return 30;
            case REDSTONE_ORE:
            case DEEPSLATE_REDSTONE_ORE:
                return 8;
            case LAPIS_ORE:
            case DEEPSLATE_LAPIS_ORE:
                return 12;
            case NETHER_GOLD_ORE:
                return 20;
            case NETHER_QUARTZ_ORE:
                return 15;
            case ANCIENT_DEBRIS:
                return 100;
            default:
                return 1;
        }
    }

    /**
     * Get XP for farming a specific crop
     */
    private long getFarmingXP(org.bukkit.Material material) {
        switch (material) {
            case WHEAT:
                return 4;
            case CARROTS:
                return 4;
            case POTATOES:
                return 4;
            case BEETROOTS:
                return 4;
            case PUMPKIN:
                return 4;
            case MELON:
                return 4;
            case SUGAR_CANE:
                return 2;
            case CACTUS:
                return 2;
            case COCOA:
                return 3;
            case NETHER_WART:
                return 3;
            default:
                return 1;
        }
    }

    /**
     * Get XP for foraging a specific log type
     */
    private long getForagingXP(org.bukkit.Material material) {
        switch (material) {
            case OAK_LOG:
            case BIRCH_LOG:
            case SPRUCE_LOG:
            case JUNGLE_LOG:
            case ACACIA_LOG:
            case DARK_OAK_LOG:
                return 6;
            case MANGROVE_LOG:
            case CHERRY_LOG:
                return 6;
            default:
                return 1;
        }
    }

    /**
     * Get XP for killing a specific entity
     */
    private long getCombatXP(org.bukkit.entity.EntityType entityType) {
        switch (entityType) {
            case ZOMBIE:
                return 5;
            case SKELETON:
                return 5;
            case SPIDER:
                return 5;
            case CREEPER:
                return 8;
            case ENDERMAN:
                return 15;
            case BLAZE:
                return 10;
            case GHAST:
                return 12;
            case WITHER_SKELETON:
                return 20;
            case ENDER_DRAGON:
                return 100;
            case WITHER:
                return 50;
            default:
                return 2;
        }
    }

    // Event Handlers

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        loadPlayerSkills(player.getUniqueId());
        applySkillBonuses(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        savePlayerSkills(player.getUniqueId());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        org.bukkit.Material material = event.getBlock().getType();

        // Mining XP
        if (material.name().contains("ORE") || material == org.bukkit.Material.COBBLESTONE || 
            material == org.bukkit.Material.STONE || material == org.bukkit.Material.DEEPSLATE) {
            long xp = getMiningXP(material);
            addSkillXP(player, SkillType.MINING, xp);
        }
        // Farming XP
        else if (material.name().contains("CROP") || material == org.bukkit.Material.WHEAT || 
                 material == org.bukkit.Material.CARROTS || material == org.bukkit.Material.POTATOES ||
                 material == org.bukkit.Material.BEETROOTS || material == org.bukkit.Material.PUMPKIN ||
                 material == org.bukkit.Material.MELON || material == org.bukkit.Material.SUGAR_CANE ||
                 material == org.bukkit.Material.CACTUS || material == org.bukkit.Material.COCOA ||
                 material == org.bukkit.Material.NETHER_WART) {
            long xp = getFarmingXP(material);
            addSkillXP(player, SkillType.FARMING, xp);
        }
        // Foraging XP
        else if (material.name().contains("LOG") || material == org.bukkit.Material.OAK_LEAVES ||
                 material == org.bukkit.Material.BIRCH_LEAVES || material == org.bukkit.Material.SPRUCE_LEAVES ||
                 material == org.bukkit.Material.JUNGLE_LEAVES || material == org.bukkit.Material.ACACIA_LEAVES ||
                 material == org.bukkit.Material.DARK_OAK_LEAVES) {
            long xp = getForagingXP(material);
            addSkillXP(player, SkillType.FORAGING, xp);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            org.bukkit.entity.EntityType entityType = event.getEntityType();
            
            long xp = getCombatXP(entityType);
            addSkillXP(player, SkillType.COMBAT, xp);
        }
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Player player = event.getPlayer();
            addSkillXP(player, SkillType.FISHING, 5);
        }
    }
}