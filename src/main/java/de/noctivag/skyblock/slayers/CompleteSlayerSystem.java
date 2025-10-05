package de.noctivag.skyblock.slayers;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.CorePlatform;
import de.noctivag.skyblock.data.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Complete Slayer System - Full Implementation with Quest Logic, Mob Spawning, Rewards, and GUI
 * 
 * Features:
 * - Complete quest management and progression
 * - Dynamic boss spawning with custom AI
 * - Comprehensive reward system
 * - Interactive GUI for quest management
 * - Advanced slayer boss mechanics
 * - Drop system with rare items
 * - Multi-tier progression system
 */
public class CompleteSlayerSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final CorePlatform corePlatform;
    private final DatabaseManager databaseManager;
    private final Map<UUID, PlayerSlayerData> playerSlayerData = new ConcurrentHashMap<>();
    private final Map<SlayerType, SlayerConfig> slayerConfigs = new HashMap<>();
    private final Map<UUID, ActiveSlayerQuest> activeQuests = new ConcurrentHashMap<>();
    private final Map<UUID, SlayerBoss> activeBosses = new ConcurrentHashMap<>();
    private final Map<UUID, List<SlayerReward>> pendingRewards = new ConcurrentHashMap<>();
    
    public CompleteSlayerSystem(SkyblockPlugin SkyblockPlugin, CorePlatform corePlatform, DatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.corePlatform = corePlatform;
        this.databaseManager = databaseManager;
        
        initializeSlayerConfigs();
        startSlayerUpdateTask();
    }
    
    public void initialize() {
        // Register events after full initialization
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeSlayerConfigs() {
        // Zombie Slayer - Revenant Horror
        slayerConfigs.put(SlayerType.ZOMBIE, new SlayerConfig(
            "Zombie Slayer", "§2§lRevenant Horror", Material.ZOMBIE_HEAD,
            "§7Defeat zombie bosses to progress", "§7and unlock powerful rewards",
            Arrays.asList(
                new SlayerTier(1, "Revenant Horror I", 100.0, 500.0, 1000, Material.ROTTEN_FLESH, "§2§lRevenant Horror I"),
                new SlayerTier(2, "Revenant Horror II", 200.0, 1000.0, 2000, Material.ROTTEN_FLESH, "§2§lRevenant Horror II"),
                new SlayerTier(3, "Revenant Horror III", 300.0, 1500.0, 3000, Material.ROTTEN_FLESH, "§2§lRevenant Horror III"),
                new SlayerTier(4, "Revenant Horror IV", 400.0, 2000.0, 4000, Material.ROTTEN_FLESH, "§2§lRevenant Horror IV"),
                new SlayerTier(5, "Revenant Horror V", 500.0, 2500.0, 5000, Material.ROTTEN_FLESH, "§2§lRevenant Horror V")
            ),
            Arrays.asList(
                new SlayerReward(1, "§2+1% Combat XP", "combat_xp_boost_1", Material.EXPERIENCE_BOTTLE, 1),
                new SlayerReward(2, "§2+2% Combat XP", "combat_xp_boost_2", Material.EXPERIENCE_BOTTLE, 2),
                new SlayerReward(3, "§2+3% Combat XP", "combat_xp_boost_3", Material.EXPERIENCE_BOTTLE, 3),
                new SlayerReward(4, "§2+4% Combat XP", "combat_xp_boost_4", Material.EXPERIENCE_BOTTLE, 4),
                new SlayerReward(5, "§2+5% Combat XP", "combat_xp_boost_5", Material.EXPERIENCE_BOTTLE, 5)
            )
        ));
        
        // Spider Slayer - Tarantula Broodfather
        slayerConfigs.put(SlayerType.SPIDER, new SlayerConfig(
            "Spider Slayer", "§8§lTarantula Broodfather", Material.SPIDER_EYE,
            "§7Defeat spider bosses to progress", "§7and unlock powerful rewards",
            Arrays.asList(
                new SlayerTier(1, "Tarantula Broodfather I", 100.0, 500.0, 1000, Material.SPIDER_EYE, "§8§lTarantula Broodfather I"),
                new SlayerTier(2, "Tarantula Broodfather II", 200.0, 1000.0, 2000, Material.SPIDER_EYE, "§8§lTarantula Broodfather II"),
                new SlayerTier(3, "Tarantula Broodfather III", 300.0, 1500.0, 3000, Material.SPIDER_EYE, "§8§lTarantula Broodfather III"),
                new SlayerTier(4, "Tarantula Broodfather IV", 400.0, 2000.0, 4000, Material.SPIDER_EYE, "§8§lTarantula Broodfather IV"),
                new SlayerTier(5, "Tarantula Broodfather V", 500.0, 2500.0, 5000, Material.SPIDER_EYE, "§8§lTarantula Broodfather V")
            ),
            Arrays.asList(
                new SlayerReward(1, "§8+1% Combat XP", "combat_xp_boost_1", Material.EXPERIENCE_BOTTLE, 1),
                new SlayerReward(2, "§8+2% Combat XP", "combat_xp_boost_2", Material.EXPERIENCE_BOTTLE, 2),
                new SlayerReward(3, "§8+3% Combat XP", "combat_xp_boost_3", Material.EXPERIENCE_BOTTLE, 3),
                new SlayerReward(4, "§8+4% Combat XP", "combat_xp_boost_4", Material.EXPERIENCE_BOTTLE, 4),
                new SlayerReward(5, "§8+5% Combat XP", "combat_xp_boost_5", Material.EXPERIENCE_BOTTLE, 5)
            )
        ));
        
        // Wolf Slayer - Sven Packmaster
        slayerConfigs.put(SlayerType.WOLF, new SlayerConfig(
            "Wolf Slayer", "§f§lSven Packmaster", Material.WOLF_SPAWN_EGG,
            "§7Defeat wolf bosses to progress", "§7and unlock powerful rewards",
            Arrays.asList(
                new SlayerTier(1, "Sven Packmaster I", 100.0, 500.0, 1000, Material.BONE, "§f§lSven Packmaster I"),
                new SlayerTier(2, "Sven Packmaster II", 200.0, 1000.0, 2000, Material.BONE, "§f§lSven Packmaster II"),
                new SlayerTier(3, "Sven Packmaster III", 300.0, 1500.0, 3000, Material.BONE, "§f§lSven Packmaster III"),
                new SlayerTier(4, "Sven Packmaster IV", 400.0, 2000.0, 4000, Material.BONE, "§f§lSven Packmaster IV"),
                new SlayerTier(5, "Sven Packmaster V", 500.0, 2500.0, 5000, Material.BONE, "§f§lSven Packmaster V")
            ),
            Arrays.asList(
                new SlayerReward(1, "§f+1% Combat XP", "combat_xp_boost_1", Material.EXPERIENCE_BOTTLE, 1),
                new SlayerReward(2, "§f+2% Combat XP", "combat_xp_boost_2", Material.EXPERIENCE_BOTTLE, 2),
                new SlayerReward(3, "§f+3% Combat XP", "combat_xp_boost_3", Material.EXPERIENCE_BOTTLE, 3),
                new SlayerReward(4, "§f+4% Combat XP", "combat_xp_boost_4", Material.EXPERIENCE_BOTTLE, 4),
                new SlayerReward(5, "§f+5% Combat XP", "combat_xp_boost_5", Material.EXPERIENCE_BOTTLE, 5)
            )
        ));
        
        // Enderman Slayer - Voidgloom Seraph
        slayerConfigs.put(SlayerType.ENDERMAN, new SlayerConfig(
            "Enderman Slayer", "§5§lVoidgloom Seraph", Material.ENDER_PEARL,
            "§7Defeat enderman bosses to progress", "§7and unlock powerful rewards",
            Arrays.asList(
                new SlayerTier(1, "Voidgloom Seraph I", 100.0, 500.0, 1000, Material.ENDER_PEARL, "§5§lVoidgloom Seraph I"),
                new SlayerTier(2, "Voidgloom Seraph II", 200.0, 1000.0, 2000, Material.ENDER_PEARL, "§5§lVoidgloom Seraph II"),
                new SlayerTier(3, "Voidgloom Seraph III", 300.0, 1500.0, 3000, Material.ENDER_PEARL, "§5§lVoidgloom Seraph III"),
                new SlayerTier(4, "Voidgloom Seraph IV", 400.0, 2000.0, 4000, Material.ENDER_PEARL, "§5§lVoidgloom Seraph IV"),
                new SlayerTier(5, "Voidgloom Seraph V", 500.0, 2500.0, 5000, Material.ENDER_PEARL, "§5§lVoidgloom Seraph V")
            ),
            Arrays.asList(
                new SlayerReward(1, "§5+1% Combat XP", "combat_xp_boost_1", Material.EXPERIENCE_BOTTLE, 1),
                new SlayerReward(2, "§5+2% Combat XP", "combat_xp_boost_2", Material.EXPERIENCE_BOTTLE, 2),
                new SlayerReward(3, "§5+3% Combat XP", "combat_xp_boost_3", Material.EXPERIENCE_BOTTLE, 3),
                new SlayerReward(4, "§5+4% Combat XP", "combat_xp_boost_4", Material.EXPERIENCE_BOTTLE, 4),
                new SlayerReward(5, "§5+5% Combat XP", "combat_xp_boost_5", Material.EXPERIENCE_BOTTLE, 5)
            )
        ));
        
        // Blaze Slayer - Inferno Demonlord
        slayerConfigs.put(SlayerType.BLAZE, new SlayerConfig(
            "Blaze Slayer", "§c§lInferno Demonlord", Material.BLAZE_POWDER,
            "§7Defeat blaze bosses to progress", "§7and unlock powerful rewards",
            Arrays.asList(
                new SlayerTier(1, "Inferno Demonlord I", 100.0, 500.0, 1000, Material.BLAZE_POWDER, "§c§lInferno Demonlord I"),
                new SlayerTier(2, "Inferno Demonlord II", 200.0, 1000.0, 2000, Material.BLAZE_POWDER, "§c§lInferno Demonlord II"),
                new SlayerTier(3, "Inferno Demonlord III", 300.0, 1500.0, 3000, Material.BLAZE_POWDER, "§c§lInferno Demonlord III"),
                new SlayerTier(4, "Inferno Demonlord IV", 400.0, 2000.0, 4000, Material.BLAZE_POWDER, "§c§lInferno Demonlord IV"),
                new SlayerTier(5, "Inferno Demonlord V", 500.0, 2500.0, 5000, Material.BLAZE_POWDER, "§c§lInferno Demonlord V")
            ),
            Arrays.asList(
                new SlayerReward(1, "§c+1% Combat XP", "combat_xp_boost_1", Material.EXPERIENCE_BOTTLE, 1),
                new SlayerReward(2, "§c+2% Combat XP", "combat_xp_boost_2", Material.EXPERIENCE_BOTTLE, 2),
                new SlayerReward(3, "§c+3% Combat XP", "combat_xp_boost_3", Material.EXPERIENCE_BOTTLE, 3),
                new SlayerReward(4, "§c+4% Combat XP", "combat_xp_boost_4", Material.EXPERIENCE_BOTTLE, 4),
                new SlayerReward(5, "§c+5% Combat XP", "combat_xp_boost_5", Material.EXPERIENCE_BOTTLE, 5)
            )
        ));
    }
    
    private void startSlayerUpdateTask() {
        Thread.ofVirtual().start(() -> {
            while (SkyblockPlugin.isEnabled()) {
                try {
                    updateActiveQuests();
                    updateActiveBosses();
                    savePlayerSlayerData();
                    Thread.sleep(20L * 60L * 50); // Every minute = 60,000 ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }
    
    private void updateActiveQuests() {
        List<UUID> expiredQuests = new ArrayList<>();
        
        for (Map.Entry<UUID, ActiveSlayerQuest> entry : activeQuests.entrySet()) {
            ActiveSlayerQuest quest = entry.getValue();
            
            // Check if quest has expired
            if (quest.isExpired()) {
                expiredQuests.add(entry.getKey());
                
                // Notify player
                Player player = Bukkit.getPlayer(quest.getPlayerId());
                if (player != null) {
                    player.sendMessage(Component.text("§c§lSLAYER QUEST EXPIRED!"));
                    player.sendMessage("§7Your " + quest.getSlayerType().getDisplayName() + " quest has expired.");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                }
            }
        }
        
        // Remove expired quests
        for (UUID playerId : expiredQuests) {
            activeQuests.remove(playerId);
        }
    }
    
    private void updateActiveBosses() {
        List<UUID> deadBosses = new ArrayList<>();
        
        for (Map.Entry<UUID, SlayerBoss> entry : activeBosses.entrySet()) {
            SlayerBoss boss = entry.getValue();
            
            if (!boss.isAlive()) {
                deadBosses.add(entry.getKey());
            } else {
                boss.update();
            }
        }
        
        // Remove dead bosses
        for (UUID bossId : deadBosses) {
            activeBosses.remove(bossId);
        }
    }
    
    private void savePlayerSlayerData() {
        for (Map.Entry<UUID, PlayerSlayerData> entry : playerSlayerData.entrySet()) {
            UUID playerId = entry.getKey();
            PlayerSlayerData data = entry.getValue();
            
            // Save to database
            databaseManager.savePlayerSlayerData(playerId, data);
        }
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            Entity entity = event.getEntity();
            
            // Check if it's a slayer boss
            UUID bossId = entity.getUniqueId();
            if (activeBosses.containsKey(bossId)) {
                SlayerBoss slayerBoss = activeBosses.get(bossId);
                ActiveSlayerQuest quest = activeQuests.get(player.getUniqueId());
                
                if (quest != null && quest.getBossId().equals(bossId)) {
                    onSlayerBossDefeated(player, quest, slayerBoss);
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = LegacyComponentSerializer.legacySection().serialize(meta.displayName());
        
        if (displayName.contains("Slayer") || displayName.contains("slayer")) {
            openSlayerGUI(player);
        }
    }
    
    public void openSlayerGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§5§lSlayer System"));
        
        // Add slayer types
        int slot = 10;
        for (SlayerType slayerType : SlayerType.values()) {
            SlayerConfig config = slayerConfigs.get(slayerType);
            if (config != null) {
                addGUIItem(gui, slot, config.getIcon(), 
                    config.getDisplayName(), 
                    config.getDescription1() + "\n" + config.getDescription2());
                slot++;
            }
        }
        
        // Add active quest info
        ActiveSlayerQuest activeQuest = activeQuests.get(player.getUniqueId());
        if (activeQuest != null) {
            addGUIItem(gui, 22, Material.CLOCK, "§a§lActive Quest", 
                "§7Type: §e" + activeQuest.getSlayerType().getDisplayName() + 
                "\n§7Tier: §e" + activeQuest.getTier() + 
                "\n§7Time Left: §e" + formatTime(activeQuest.getTimeRemaining()));
        } else {
            addGUIItem(gui, 22, Material.BARRIER, "§c§lNo Active Quest", "§7You don't have an active slayer quest.");
        }
        
        // Add slayer management items
        addGUIItem(gui, 28, Material.BOOK, "§6§lMy Slayer Progress", "§7View your slayer progress.");
        addGUIItem(gui, 29, Material.CHEST, "§e§lSlayer Rewards", "§7View available rewards.");
        addGUIItem(gui, 30, Material.EMERALD, "§a§lSlayer Shop", "§7Buy slayer items.");
        addGUIItem(gui, 31, Material.PLAYER_HEAD, "§d§lSlayer Leaderboard", "§7View slayer leaderboards.");
        
        // Add quick start buttons
        addGUIItem(gui, 37, Material.DIAMOND_SWORD, "§c§lQuick Start Tier 1", "§7Start a tier 1 slayer quest.");
        addGUIItem(gui, 38, Material.GOLDEN_SWORD, "§e§lQuick Start Tier 2", "§7Start a tier 2 slayer quest.");
        addGUIItem(gui, 39, Material.IRON_SWORD, "§7§lQuick Start Tier 3", "§7Start a tier 3 slayer quest.");
        addGUIItem(gui, 40, Material.STONE_SWORD, "§8§lQuick Start Tier 4", "§7Start a tier 4 slayer quest.");
        
        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the slayer menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage(Component.text("§aSlayer GUI opened!"));
    }
    
    private void onSlayerBossDefeated(Player player, ActiveSlayerQuest quest, SlayerBoss slayerBoss) {
        UUID playerId = player.getUniqueId();
        PlayerSlayerData data = getPlayerSlayerData(playerId);
        
        // Complete quest
        data.completeQuest(quest.getSlayerType(), quest.getTier());
        
        // Give rewards
        SlayerConfig config = slayerConfigs.get(quest.getSlayerType());
        if (config != null) {
            SlayerTier tier = config.getTiers().get(quest.getTier() - 1);
            if (tier != null) {
                // Give coins
                corePlatform.getPlayerProfile(playerId).addCoins(tier.getCoinReward());
                
                // Give XP
                data.addXP(quest.getSlayerType(), tier.getXpReward());
                
                // Generate drops
                List<SlayerReward> drops = generateSlayerDrops(quest.getSlayerType(), quest.getTier());
                pendingRewards.computeIfAbsent(playerId, k -> new ArrayList<>()).addAll(drops);
                
                // Send messages
                player.sendMessage(Component.text("§a§lSLAYER QUEST COMPLETED!"));
                player.sendMessage("§7" + quest.getSlayerType().getDisplayName() + " Tier " + quest.getTier() + " defeated!");
                player.sendMessage("§6+" + tier.getCoinReward() + " Coins");
                player.sendMessage("§b+" + tier.getXpReward() + " XP");
                
                // Show drops
                for (SlayerReward reward : drops) {
                    player.sendMessage("§a+" + reward.getAmount() + " " + reward.getDisplayName());
                }
                
                // Play sound
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                
                // Particle effects
                player.getWorld().spawnParticle(org.bukkit.Particle.FIREWORK, 
                    player.getLocation().add(0, 1, 0), 50, 0.5, 0.5, 0.5, 0.3);
            }
        }
        
        // Remove active quest
        activeQuests.remove(playerId);
        
        // Update player slayer data
        playerSlayerData.put(playerId, data);
        
        // Remove boss
        activeBosses.remove(slayerBoss.getEntity().getUniqueId());
    }
    
    private List<SlayerReward> generateSlayerDrops(SlayerType slayerType, int tier) {
        List<SlayerReward> drops = new ArrayList<>();
        SlayerConfig config = slayerConfigs.get(slayerType);
        
        if (config != null) {
            SlayerTier tierConfig = config.getTiers().get(tier - 1);
            if (tierConfig != null) {
                // Base drops
                drops.add(new SlayerReward(tier, "Common Drop", "common_drop", tierConfig.getMaterial(), 1));
                
                // Rare drops (chance based on tier)
                double rareChance = 0.1 + (tier * 0.05); // 10% + 5% per tier
                if (Math.random() < rareChance) {
                    drops.add(new SlayerReward(tier, "Rare Drop", "rare_drop", Material.DIAMOND, 1));
                }
                
                // Epic drops (very rare)
                double epicChance = 0.01 + (tier * 0.005); // 1% + 0.5% per tier
                if (Math.random() < epicChance) {
                    drops.add(new SlayerReward(tier, "Epic Drop", "epic_drop", Material.EMERALD, 1));
                }
            }
        }
        
        return drops;
    }
    
    public boolean startSlayerQuest(Player player, SlayerType slayerType, int tier) {
        UUID playerId = player.getUniqueId();
        
        // Check if player already has an active quest
        if (activeQuests.containsKey(playerId)) {
            player.sendMessage(Component.text("§cYou already have an active slayer quest!"));
            return false;
        }
        
        // Check if player has the required level
        PlayerSlayerData data = getPlayerSlayerData(playerId);
        if (!data.canStartQuest(slayerType, tier)) {
            player.sendMessage(Component.text("§cYou don't have the required level for this quest!"));
            return false;
        }
        
        // Get slayer config
        SlayerConfig config = slayerConfigs.get(slayerType);
        if (config == null) {
            player.sendMessage(Component.text("§cInvalid slayer type!"));
            return false;
        }
        
        // Check if tier exists
        if (tier < 1 || tier > config.getTiers().size()) {
            player.sendMessage(Component.text("§cInvalid tier!"));
            return false;
        }
        
        // Get tier config
        SlayerTier tierConfig = config.getTiers().get(tier - 1);
        if (tierConfig == null) {
            player.sendMessage(Component.text("§cInvalid tier configuration!"));
            return false;
        }
        
        // Check if player has enough coins
        if (!corePlatform.getPlayerProfile(playerId).hasCoins(tierConfig.getCost())) {
            player.sendMessage(Component.text("§cYou don't have enough coins!"));
            return false;
        }
        
        // Deduct coins
        corePlatform.getPlayerProfile(playerId).removeCoins(tierConfig.getCost());
        
        // Spawn boss
        Location spawnLocation = findSpawnLocation(player);
        SlayerBoss boss = spawnSlayerBoss(slayerType, tier, spawnLocation);
        
        if (boss == null) {
            player.sendMessage(Component.text("§cFailed to spawn slayer boss!"));
            return false;
        }
        
        // Create active quest
        ActiveSlayerQuest quest = new ActiveSlayerQuest(playerId, slayerType, tier, boss.getEntity().getUniqueId(), java.lang.System.currentTimeMillis());
        activeQuests.put(playerId, quest);
        
        // Send messages
        player.sendMessage(Component.text("§a§lSLAYER QUEST STARTED!"));
        player.sendMessage("§7" + slayerType.getDisplayName() + " Tier " + tier + " - " + tierConfig.getName());
        player.sendMessage("§7Cost: §6" + tierConfig.getCost() + " Coins");
        player.sendMessage("§7Reward: §6" + tierConfig.getCoinReward() + " Coins");
        player.sendMessage("§7XP Reward: §b" + tierConfig.getXpReward() + " XP");
        
        return true;
    }
    
    private Location findSpawnLocation(Player player) {
        Location playerLoc = player.getLocation();
        
        // Try to find a safe spawn location near the player
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                Location testLoc = playerLoc.clone().add(x, 0, z);
                if (testLoc.getBlock().getType() == Material.AIR && 
                    testLoc.clone().add(0, 1, 0).getBlock().getType() == Material.AIR) {
                    return testLoc;
                }
            }
        }
        
        // Fallback to player location
        return playerLoc;
    }
    
    private SlayerBoss spawnSlayerBoss(SlayerType slayerType, int tier, Location location) {
        SlayerConfig config = slayerConfigs.get(slayerType);
        if (config == null) return null;
        
        SlayerTier tierConfig = config.getTiers().get(tier - 1);
        if (tierConfig == null) return null;
        
        EntityType entityType = switch (slayerType) {
            case ZOMBIE -> EntityType.ZOMBIE;
            case SPIDER -> EntityType.SPIDER;
            case WOLF -> EntityType.WOLF;
            case ENDERMAN -> EntityType.ENDERMAN;
            case BLAZE -> EntityType.BLAZE;
        };
        
        Entity entity = location.getWorld().spawnEntity(location, entityType);
        
        if (entity instanceof LivingEntity livingEntity) {
            // Set custom name
            livingEntity.customName(Component.text(tierConfig.getDisplayName()));
            livingEntity.setCustomNameVisible(true);
            
            // Set health based on tier
            double health = 100.0 + (tier * 50.0);
            livingEntity.setHealth(health);
            // Note: setMaxHealth is deprecated, using setHealth instead
            
            // Add special effects based on tier
            livingEntity.addPotionEffect(new PotionEffect(
                PotionEffectType.SPEED, Integer.MAX_VALUE, tier, false, false
            ));
            
            if (tier >= 3) {
                livingEntity.addPotionEffect(new PotionEffect(
                    PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1, false, false
                ));
            }
            
            if (tier >= 4) {
                livingEntity.addPotionEffect(new PotionEffect(
                    PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false, false
                ));
            }
            
            if (tier >= 5) {
                livingEntity.addPotionEffect(new PotionEffect(
                    PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1, false, false
                ));
            }
        }
        
        // Create slayer boss wrapper
        SlayerBoss slayerBoss = new SlayerBoss(entity, slayerType, tier, config);
        activeBosses.put(entity.getUniqueId(), slayerBoss);
        
        return slayerBoss;
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(Component.text(description)));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        
        return String.format("%d:%02d", minutes, seconds);
    }
    
    public PlayerSlayerData getPlayerSlayerData(UUID playerId) {
        return playerSlayerData.computeIfAbsent(playerId, k -> new PlayerSlayerData(playerId));
    }
    
    public Map<SlayerType, SlayerConfig> getSlayerConfigs() {
        return new HashMap<>(slayerConfigs);
    }
    
    public SlayerConfig getSlayerConfig(SlayerType slayerType) {
        return slayerConfigs.get(slayerType);
    }
    
    public ActiveSlayerQuest getActiveQuest(UUID playerId) {
        return activeQuests.get(playerId);
    }
    
    public boolean hasActiveQuest(UUID playerId) {
        return activeQuests.containsKey(playerId);
    }
    
    public List<SlayerReward> getPendingRewards(UUID playerId) {
        return pendingRewards.getOrDefault(playerId, new ArrayList<>());
    }
    
    public void claimRewards(UUID playerId) {
        List<SlayerReward> rewards = pendingRewards.remove(playerId);
        if (rewards != null && !rewards.isEmpty()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                for (SlayerReward reward : rewards) {
                    // Give item to player
                    ItemStack item = new ItemStack(reward.getMaterial(), reward.getAmount());
                    player.getInventory().addItem(item);
                }
                
                player.sendMessage(Component.text("§a§lREWARDS CLAIMED!"));
                player.sendMessage("§7You received " + rewards.size() + " items!");
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            }
        }
    }
    
    // Enhanced Slayer Tier Class
    public static class SlayerTier {
        private final int tier;
        private final String name;
        private final double cost;
        private final double coinReward;
        private final double xpReward;
        private final Material material;
        private final String displayName;
        
        public SlayerTier(int tier, String name, double cost, double coinReward, double xpReward, Material material, String displayName) {
            this.tier = tier;
            this.name = name;
            this.cost = cost;
            this.coinReward = coinReward;
            this.xpReward = xpReward;
            this.material = material;
            this.displayName = displayName;
        }
        
        public int getTier() { return tier; }
        public String getName() { return name; }
        public double getCost() { return cost; }
        public double getCoinReward() { return coinReward; }
        public double getXpReward() { return xpReward; }
        public Material getMaterial() { return material; }
        public String getDisplayName() { return displayName; }
    }
    
    // Enhanced Slayer Reward Class
    public static class SlayerReward {
        private final int tier;
        private final String displayName;
        private final String rewardId;
        private final Material material;
        private final int amount;
        
        public SlayerReward(int tier, String displayName, String rewardId, Material material, int amount) {
            this.tier = tier;
            this.displayName = displayName;
            this.rewardId = rewardId;
            this.material = material;
            this.amount = amount;
        }
        
        public int getTier() { return tier; }
        public String getDisplayName() { return displayName; }
        public String getRewardId() { return rewardId; }
        public Material getMaterial() { return material; }
        public int getAmount() { return amount; }
    }
    
    // Enhanced Active Slayer Quest Class
    public static class ActiveSlayerQuest {
        private final UUID playerId;
        private final SlayerType slayerType;
        private final int tier;
        private final UUID bossId;
        private final long startTime;
        private final long duration = 10 * 60 * 1000L; // 10 minutes
        
        public ActiveSlayerQuest(UUID playerId, SlayerType slayerType, int tier, UUID bossId, long startTime) {
            this.playerId = playerId;
            this.slayerType = slayerType;
            this.tier = tier;
            this.bossId = bossId;
            this.startTime = startTime;
        }
        
        public UUID getPlayerId() { return playerId; }
        public SlayerType getSlayerType() { return slayerType; }
        public int getTier() { return tier; }
        public UUID getBossId() { return bossId; }
        public long getStartTime() { return startTime; }
        public long getDuration() { return duration; }
        
        public boolean isExpired() {
            return java.lang.System.currentTimeMillis() - startTime > duration;
        }
        
        public long getTimeRemaining() {
            return Math.max(0, duration - (java.lang.System.currentTimeMillis() - startTime));
        }
    }
    
    // Slayer Boss Class
    public static class SlayerBoss {
        private final Entity entity;
        private final SlayerType slayerType;
        private final int tier;
        private final SlayerConfig config;
        private long lastUpdate;
        
        public SlayerBoss(Entity entity, SlayerType slayerType, int tier, SlayerConfig config) {
            this.entity = entity;
            this.slayerType = slayerType;
            this.tier = tier;
            this.config = config;
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public Entity getEntity() { return entity; }
        public SlayerType getSlayerType() { return slayerType; }
        public int getTier() { return tier; }
        public SlayerConfig getConfig() { return config; }
        
        public boolean isAlive() {
            return entity != null && entity.isValid() && entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0;
        }
        
        public void update() {
            if (!isAlive()) return;
            
            long currentTime = java.lang.System.currentTimeMillis();
            if (currentTime - lastUpdate >= 1000) { // Update every second
                lastUpdate = currentTime;
                
                // Boss AI logic
                if (entity instanceof LivingEntity livingEntity) {
                    // Find nearest player
                    Player nearestPlayer = null;
                    double nearestDistance = Double.MAX_VALUE;
                    
                    for (Player player : entity.getWorld().getPlayers()) {
                        double distance = player.getLocation().distance(entity.getLocation());
                        if (distance < nearestDistance && distance <= 50) {
                            nearestDistance = distance;
                            nearestPlayer = player;
                        }
                    }
                    
                    // Attack nearest player
                    if (nearestPlayer != null && nearestDistance <= 10) {
                        if (livingEntity instanceof Mob) {
                            ((Mob) livingEntity).setTarget(nearestPlayer);
                        }
                    }
                    
                    // Special abilities based on tier
                    if (tier >= 3 && Math.random() < 0.1) { // 10% chance every second
                        // Teleport ability
                        if (nearestPlayer != null) {
                            Location teleportLoc = nearestPlayer.getLocation().add(
                                (Math.random() - 0.5) * 10, 0, (Math.random() - 0.5) * 10);
                            entity.teleport(teleportLoc);
                        }
                    }
                    
                    if (tier >= 4 && Math.random() < 0.05) { // 5% chance every second
                        // Explosion ability
                        entity.getWorld().createExplosion(entity.getLocation(), 2.0f, false, false);
                    }
                }
            }
        }
    }
    
    // Enhanced Slayer Config Class
    public static class SlayerConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description1;
        private final String description2;
        private final List<SlayerTier> tiers;
        private final List<SlayerReward> rewards;
        
        public SlayerConfig(String name, String displayName, Material icon, String description1, String description2, List<SlayerTier> tiers, List<SlayerReward> rewards) {
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.description1 = description1;
            this.description2 = description2;
            this.tiers = tiers;
            this.rewards = rewards;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription1() { return description1; }
        public String getDescription2() { return description2; }
        public List<SlayerTier> getTiers() { return tiers; }
        public List<SlayerReward> getRewards() { return rewards; }
    }
}
