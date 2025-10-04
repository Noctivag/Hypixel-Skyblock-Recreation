package de.noctivag.skyblock.skyblock;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Skills System inspired by Hypixel Skyblock
 * Features:
 * - 12 main skills with unique progression
 * - Skill-based stat bonuses
 * - Skill-specific abilities and perks
 * - XP gain from various activities
 * - Skill level requirements for items/areas
 */
public class AdvancedSkillsSystem implements Listener {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final HealthManaSystem healthManaSystem;
    private final Map<UUID, PlayerSkillsData> playerSkills = new ConcurrentHashMap<>();
    
    public AdvancedSkillsSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager, 
                               HealthManaSystem healthManaSystem) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.healthManaSystem = healthManaSystem;
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material blockType = event.getBlock().getType();
        
        // Mining skill XP
        if (isMiningBlock(blockType)) {
            addSkillXP(player, SkillType.MINING, getMiningXP(blockType));
        }
        
        // Foraging skill XP
        if (isForagingBlock(blockType)) {
            addSkillXP(player, SkillType.FORAGING, getForagingXP(blockType));
        }
        
        // Farming skill XP
        if (isFarmingBlock(blockType)) {
            addSkillXP(player, SkillType.FARMING, getFarmingXP(blockType));
        }
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        
        Player player = event.getEntity().getKiller();
        String entityType = event.getEntity().getType().name();
        
        // Combat skill XP
        addSkillXP(player, SkillType.COMBAT, getCombatXP(entityType));
    }
    
    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Player player = event.getPlayer();
            addSkillXP(player, SkillType.FISHING, getFishingXP());
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null) return;
        
        // Enchanting skill XP
        if (item.getType() == Material.ENCHANTED_BOOK) {
            addSkillXP(player, SkillType.ENCHANTING, 10);
        }
        
        // Alchemy skill XP
        if (item.getType() == Material.POTION) {
            addSkillXP(player, SkillType.ALCHEMY, 5);
        }
    }
    
    public void addSkillXP(Player player, SkillType skillType, double xp) {
        UUID playerId = player.getUniqueId();
        PlayerSkillsData data = getPlayerSkillsData(playerId);
        
        double currentXP = data.getSkillXP(skillType);
        int currentLevel = data.getSkillLevel(skillType);
        
        // Apply XP multipliers
        xp *= getXPMultiplier(player, skillType);
        
        double newXP = currentXP + xp;
        int newLevel = calculateLevel(newXP);
        
        data.setSkillXP(skillType, newXP);
        data.setSkillLevel(skillType, newLevel);
        
        // Check for level up
        if (newLevel > currentLevel) {
            handleLevelUp(player, skillType, newLevel);
        }
        
        // Update health/mana stats
        updatePlayerStats(player, data);
        
        // Send XP message
        player.sendMessage("§a+" + String.format("%.1f", xp) + " " + skillType.getDisplayName() + " XP");
    }
    
    private void handleLevelUp(Player player, SkillType skillType, int newLevel) {
        player.sendMessage("§6§lLEVEL UP! §e" + skillType.getDisplayName() + " §7Level §e" + newLevel);
        
        // Apply level-up bonuses
        applyLevelUpBonuses(player, skillType, newLevel);
        
        // Send level-up effects
        player.showTitle(net.kyori.adventure.title.Title.title(
            net.kyori.adventure.text.Component.text("§6§lLEVEL UP!"),
            net.kyori.adventure.text.Component.text("§e" + skillType.getDisplayName() + " §7Level §e" + newLevel),
            net.kyori.adventure.title.Title.Times.times(
                java.time.Duration.ofMillis(500),
                java.time.Duration.ofMillis(2000),
                java.time.Duration.ofMillis(500)
            )
        ));
    }
    
    private void applyLevelUpBonuses(Player player, SkillType skillType, int level) {
        UUID playerId = player.getUniqueId();
        PlayerSkillsData data = getPlayerSkillsData(playerId);
        
        switch (skillType) {
            case COMBAT:
                // Combat level bonuses
                double healthBonus = level * 2.0; // +2 health per level
                data.addHealthBonus(healthBonus);
                healthManaSystem.getPlayerData(playerId).setHealthFromSkills(data.getTotalHealthBonus());
                break;
                
            case MINING:
                // Mining level bonuses
                double defenseBonus = level * 1.0; // +1 defense per level
                data.addDefenseBonus(defenseBonus);
                break;
                
            case FARMING:
                // Farming level bonuses
                double healthRegenBonus = level * 0.1; // +0.1 health regen per level
                data.addHealthRegenBonus(healthRegenBonus);
                break;
                
            case FORAGING:
                // Foraging level bonuses
                double strengthBonus = level * 0.5; // +0.5 strength per level
                data.addStrengthBonus(strengthBonus);
                break;
                
            case FISHING:
                // Fishing level bonuses
                double luckBonus = level * 0.2; // +0.2 luck per level
                data.addLuckBonus(luckBonus);
                break;
                
            case ENCHANTING:
                // Enchanting level bonuses
                double manaBonus = level * 1.5; // +1.5 mana per level
                data.addManaBonus(manaBonus);
                healthManaSystem.getPlayerData(playerId).setManaFromSkills(data.getTotalManaBonus());
                break;
                
            case ALCHEMY:
                // Alchemy level bonuses
                double manaRegenBonus = level * 0.1; // +0.1 mana regen per level
                data.addManaRegenBonus(manaRegenBonus);
                break;
                
            case CARPENTRY:
                // Carpentry level bonuses
                double speedBonus = level * 0.1; // +0.1 speed per level
                data.addSpeedBonus(speedBonus);
                break;
                
            case RUNECRAFTING:
                // Runecrafting level bonuses
                double intelligenceBonus = level * 1.0; // +1 intelligence per level
                data.addIntelligenceBonus(intelligenceBonus);
                break;
                
            case TAMING:
                // Taming level bonuses
                double petLuckBonus = level * 0.3; // +0.3 pet luck per level
                data.addPetLuckBonus(petLuckBonus);
                break;
                
            case SOCIAL:
                // Social level bonuses
                double magicFindBonus = level * 0.1; // +0.1 magic find per level
                data.addMagicFindBonus(magicFindBonus);
                break;
                
            case CATACOMBS:
                // Catacombs level bonuses
                double dungeonBonus = level * 2.0; // +2 dungeon stats per level
                data.addDungeonBonus(dungeonBonus);
                break;
        }
    }
    
    private void updatePlayerStats(Player player, PlayerSkillsData data) {
        UUID playerId = player.getUniqueId();
        HealthManaSystem.PlayerHealthManaData healthManaData = healthManaSystem.getPlayerData(playerId);
        
        if (healthManaData != null) {
            healthManaData.setHealthFromSkills(data.getTotalHealthBonus());
            healthManaData.setManaFromSkills(data.getTotalManaBonus());
            healthManaData.setDefenseFromSkills(data.getTotalDefenseBonus());
            healthManaData.setHealthRegenMultiplierFromSkills(data.getTotalHealthRegenBonus());
            healthManaData.setManaRegenMultiplierFromSkills(data.getTotalManaRegenBonus());
        }
    }
    
    private double getXPMultiplier(Player player, SkillType skillType) {
        double multiplier = 1.0;
        
        // Add multipliers from accessories, potions, etc.
        // This would integrate with other systems
        
        return multiplier;
    }
    
    private int calculateLevel(double xp) {
        // XP formula similar to Hypixel Skyblock
        if (xp < 50) return 1;
        if (xp < 125) return 2;
        if (xp < 200) return 3;
        if (xp < 300) return 4;
        if (xp < 500) return 5;
        if (xp < 750) return 6;
        if (xp < 1000) return 7;
        if (xp < 1500) return 8;
        if (xp < 2000) return 9;
        if (xp < 3500) return 10;
        if (xp < 5000) return 11;
        if (xp < 7500) return 12;
        if (xp < 10000) return 13;
        if (xp < 15000) return 14;
        if (xp < 20000) return 15;
        if (xp < 30000) return 16;
        if (xp < 50000) return 17;
        if (xp < 75000) return 18;
        if (xp < 100000) return 19;
        if (xp < 200000) return 20;
        if (xp < 300000) return 21;
        if (xp < 400000) return 22;
        if (xp < 500000) return 23;
        if (xp < 600000) return 24;
        if (xp < 700000) return 25;
        if (xp < 800000) return 26;
        if (xp < 900000) return 27;
        if (xp < 1000000) return 28;
        if (xp < 1100000) return 29;
        if (xp < 1200000) return 30;
        if (xp < 1300000) return 31;
        if (xp < 1400000) return 32;
        if (xp < 1500000) return 33;
        if (xp < 1600000) return 34;
        if (xp < 1700000) return 35;
        if (xp < 1800000) return 36;
        if (xp < 1900000) return 37;
        if (xp < 2000000) return 38;
        if (xp < 2100000) return 39;
        if (xp < 2200000) return 40;
        if (xp < 2300000) return 41;
        if (xp < 2400000) return 42;
        if (xp < 2500000) return 43;
        if (xp < 2600000) return 44;
        if (xp < 2700000) return 45;
        if (xp < 2800000) return 46;
        if (xp < 2900000) return 47;
        if (xp < 3000000) return 48;
        if (xp < 3100000) return 49;
        if (xp < 3200000) return 50;
        if (xp < 3300000) return 51;
        if (xp < 3400000) return 52;
        if (xp < 3500000) return 53;
        if (xp < 3600000) return 54;
        if (xp < 3700000) return 55;
        if (xp < 3800000) return 56;
        if (xp < 3900000) return 57;
        if (xp < 4000000) return 58;
        if (xp < 4100000) return 59;
        return 60; // Max level
    }
    
    private boolean isMiningBlock(Material material) {
        return material.name().contains("ORE") || 
               material == Material.COBBLESTONE || 
               material == Material.STONE ||
               material == Material.NETHERRACK ||
               material == Material.END_STONE;
    }
    
    private boolean isForagingBlock(Material material) {
        return material.name().contains("LOG") || 
               material.name().contains("WOOD") ||
               material == Material.OAK_LEAVES;
    }
    
    private boolean isFarmingBlock(Material material) {
        return material == Material.WHEAT ||
               material == Material.CARROTS ||
               material == Material.POTATOES ||
               material == Material.PUMPKIN ||
               material == Material.MELON ||
               material == Material.SUGAR_CANE ||
               material == Material.CACTUS ||
               material == Material.NETHER_WART;
    }
    
    private double getMiningXP(Material material) {
        return switch (material) {
            case COBBLESTONE -> 1.0;
            case COAL_ORE -> 5.0;
            case IRON_ORE -> 10.0;
            case GOLD_ORE -> 15.0;
            case DIAMOND_ORE -> 25.0;
            case EMERALD_ORE -> 30.0;
            case REDSTONE_ORE -> 8.0;
            case LAPIS_ORE -> 12.0;
            case NETHER_QUARTZ_ORE -> 20.0;
            case OBSIDIAN -> 50.0;
            default -> 1.0;
        };
    }
    
    private double getForagingXP(Material material) {
        return switch (material) {
            case OAK_LOG, BIRCH_LOG, SPRUCE_LOG -> 2.0;
            case JUNGLE_LOG, ACACIA_LOG, DARK_OAK_LOG -> 3.0;
            case OAK_LEAVES, BIRCH_LEAVES, SPRUCE_LEAVES -> 1.0;
            case JUNGLE_LEAVES, ACACIA_LEAVES, DARK_OAK_LEAVES -> 1.5;
            default -> 1.0;
        };
    }
    
    private double getFarmingXP(Material material) {
        return switch (material) {
            case WHEAT -> 2.0;
            case CARROTS, POTATOES -> 2.5;
            case PUMPKIN, MELON -> 3.0;
            case SUGAR_CANE -> 2.0;
            case CACTUS -> 2.5;
            case NETHER_WART -> 4.0;
            default -> 1.0;
        };
    }
    
    private double getCombatXP(String entityType) {
        return switch (entityType) {
            case "ZOMBIE", "SKELETON", "SPIDER" -> 5.0;
            case "CREEPER" -> 8.0;
            case "ENDERMAN" -> 15.0;
            case "BLAZE" -> 20.0;
            case "GHAST" -> 25.0;
            case "WITHER_SKELETON" -> 50.0;
            case "ENDER_DRAGON" -> 500.0;
            case "WITHER" -> 200.0;
            default -> 3.0;
        };
    }
    
    private double getFishingXP() {
        return 10.0; // Base fishing XP
    }
    
    public PlayerSkillsData getPlayerSkillsData(UUID playerId) {
        return playerSkills.computeIfAbsent(playerId, k -> new PlayerSkillsData(playerId));
    }
    
    public int getSkillLevel(Player player, SkillType skillType) {
        return getPlayerSkillsData(player.getUniqueId()).getSkillLevel(skillType);
    }
    
    public double getSkillXP(Player player, SkillType skillType) {
        return getPlayerSkillsData(player.getUniqueId()).getSkillXP(skillType);
    }
    
    public enum SkillType {
        COMBAT("§cCombat", "§7Fight monsters and bosses"),
        MINING("§7Mining", "§7Mine ores and gems"),
        FARMING("§eFarming", "§7Grow crops and plants"),
        FORAGING("§6Foraging", "§7Chop trees and gather wood"),
        FISHING("§bFishing", "§7Catch fish and sea creatures"),
        ENCHANTING("§dEnchanting", "§7Enchant items and books"),
        ALCHEMY("§5Alchemy", "§7Brew potions and elixirs"),
        CARPENTRY("§7Carpentry", "§7Craft furniture and blocks"),
        RUNECRAFTING("§9Runecrafting", "§7Create magical runes"),
        TAMING("§6Taming", "§7Bond with pets and mounts"),
        SOCIAL("§3Social", "§7Interact with other players"),
        CATACOMBS("§5Catacombs", "§7Explore the catacombs");
        
        private final String displayName;
        private final String description;
        
        SkillType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public static class PlayerSkillsData {
        private final UUID playerId;
        private final Map<SkillType, Double> skillXP = new HashMap<>();
        private final Map<SkillType, Integer> skillLevels = new HashMap<>();
        
        // Stat bonuses from skills
        private double totalHealthBonus = 0.0;
        private double totalManaBonus = 0.0;
        private double totalDefenseBonus = 0.0;
        private double totalStrengthBonus = 0.0;
        private double totalSpeedBonus = 0.0;
        private double totalLuckBonus = 0.0;
        private double totalIntelligenceBonus = 0.0;
        private double totalHealthRegenBonus = 0.0;
        private double totalManaRegenBonus = 0.0;
        private double totalPetLuckBonus = 0.0;
        private double totalMagicFindBonus = 0.0;
        private double totalDungeonBonus = 0.0;
        
        public PlayerSkillsData(UUID playerId) {
            this.playerId = playerId;
            
            // Initialize all skills
            for (SkillType skill : SkillType.values()) {
                skillXP.put(skill, 0.0);
                skillLevels.put(skill, 1);
            }
        }
        
        public double getSkillXP(SkillType skillType) {
            return skillXP.getOrDefault(skillType, 0.0);
        }
        
        public void setSkillXP(SkillType skillType, double xp) {
            skillXP.put(skillType, xp);
        }
        
        public int getSkillLevel(SkillType skillType) {
            return skillLevels.getOrDefault(skillType, 1);
        }
        
        public void setSkillLevel(SkillType skillType, int level) {
            skillLevels.put(skillType, level);
        }
        
        // Stat bonus methods
        public void addHealthBonus(double bonus) { totalHealthBonus += bonus; }
        public void addManaBonus(double bonus) { totalManaBonus += bonus; }
        public void addDefenseBonus(double bonus) { totalDefenseBonus += bonus; }
        public void addStrengthBonus(double bonus) { totalStrengthBonus += bonus; }
        public void addSpeedBonus(double bonus) { totalSpeedBonus += bonus; }
        public void addLuckBonus(double bonus) { totalLuckBonus += bonus; }
        public void addIntelligenceBonus(double bonus) { totalIntelligenceBonus += bonus; }
        public void addHealthRegenBonus(double bonus) { totalHealthRegenBonus += bonus; }
        public void addManaRegenBonus(double bonus) { totalManaRegenBonus += bonus; }
        public void addPetLuckBonus(double bonus) { totalPetLuckBonus += bonus; }
        public void addMagicFindBonus(double bonus) { totalMagicFindBonus += bonus; }
        public void addDungeonBonus(double bonus) { totalDungeonBonus += bonus; }
        
        public double getTotalHealthBonus() { return totalHealthBonus; }
        public double getTotalManaBonus() { return totalManaBonus; }
        public double getTotalDefenseBonus() { return totalDefenseBonus; }
        public double getTotalStrengthBonus() { return totalStrengthBonus; }
        public double getTotalSpeedBonus() { return totalSpeedBonus; }
        public double getTotalLuckBonus() { return totalLuckBonus; }
        public double getTotalIntelligenceBonus() { return totalIntelligenceBonus; }
        public double getTotalHealthRegenBonus() { return totalHealthRegenBonus; }
        public double getTotalManaRegenBonus() { return totalManaRegenBonus; }
        public double getTotalPetLuckBonus() { return totalPetLuckBonus; }
        public double getTotalMagicFindBonus() { return totalMagicFindBonus; }
        public double getTotalDungeonBonus() { return totalDungeonBonus; }
    }
    
    public void initializePlayer(Player player) {
        // Initialize player skills data
        UUID playerId = player.getUniqueId();
        if (!playerSkills.containsKey(playerId)) {
            playerSkills.put(playerId, new PlayerSkillsData(playerId));
        }
    }
    
    public void openSkillsMenu(Player player) {
        // Open skills menu for player
        player.sendMessage("Skills menu opened!");
    }
}
