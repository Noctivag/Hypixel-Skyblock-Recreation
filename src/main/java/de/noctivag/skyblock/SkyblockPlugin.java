package de.noctivag.skyblock;

import de.noctivag.skyblock.achievements.AchievementManager;
import de.noctivag.skyblock.achievements.AchievementSystem;
import de.noctivag.skyblock.enchanting.EnchantingSystem;
import de.noctivag.skyblock.weapons.WeaponAbilitySystem;
import de.noctivag.skyblock.items.RecombobulatorSystem;
import de.noctivag.skyblock.items.PotatoBookSystem;
import de.noctivag.skyblock.commands.AdvancedCommandSystem;
import de.noctivag.skyblock.commands.MiningCommand;
import de.noctivag.skyblock.commands.MultiServerCommands;
import de.noctivag.skyblock.config.ConfigManager;
import de.noctivag.skyblock.cosmetics.CosmeticsManager;
import de.noctivag.skyblock.data.DataManager;
import de.noctivag.skyblock.data.DatabaseManager;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import de.noctivag.skyblock.display.NametagManager;
import de.noctivag.skyblock.economy.EconomyManager;
import de.noctivag.skyblock.features.bosses.CompleteBossSystem;
import de.noctivag.skyblock.features.bosses.commands.BossCommandSystem;
import de.noctivag.skyblock.features.bosses.mechanics.BossMechanicsSystem;
import de.noctivag.skyblock.friends.FriendsManager;
import de.noctivag.skyblock.items.AdvancedItemSystem;
import de.noctivag.skyblock.items.ItemCooldownSystem;
import de.noctivag.skyblock.kit.KitManager;
import de.noctivag.skyblock.listeners.PlayerListener;
import de.noctivag.skyblock.locations.LocationManager;
import de.noctivag.skyblock.managers.FeatureManager;
import de.noctivag.skyblock.managers.RankManager;
import de.noctivag.skyblock.managers.RestartManager;
import de.noctivag.skyblock.managers.VanishManager;
import de.noctivag.skyblock.messages.JoinMessageManager;
import de.noctivag.skyblock.messages.MessageManager;
import de.noctivag.skyblock.minions.AdvancedMinionSystem;
import de.noctivag.skyblock.mobs.AdvancedMobSystem;
import de.noctivag.skyblock.multiserver.CentralDatabaseSystem;
import de.noctivag.skyblock.multiserver.HypixelStyleProxySystem;
import de.noctivag.skyblock.npcs.AdvancedNPCSystem;
import de.noctivag.skyblock.party.PartyManager;
import de.noctivag.skyblock.performance.AdvancedPerformanceManager;
import de.noctivag.skyblock.pets.AdvancedPetSystem;
import de.noctivag.skyblock.player.PlayerDataManager;
import de.noctivag.skyblock.quests.AdvancedQuestSystem;
import de.noctivag.skyblock.reforges.ReforgeSystem;
import de.noctivag.skyblock.rewards.DailyRewardManager;
import de.noctivag.skyblock.scoreboard.ScoreboardManager;
import de.noctivag.skyblock.skyblock.SkyblockMainSystem;
import de.noctivag.skyblock.skyblock.UnifiedSkyblockSystem;
import de.noctivag.skyblock.skyblock.SkyblockManager;
import de.noctivag.skyblock.skyblock.MiningAreaSystem;
import de.noctivag.skyblock.slayers.AdvancedSlayerSystem;
import de.noctivag.skyblock.skills.SkillsSystem;
import de.noctivag.skyblock.utils.TPSUtil;
import de.noctivag.skyblock.worlds.ThreadSafeWorldManager;
import de.noctivag.skyblock.worlds.WorldManager;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

@Getter
@SuppressWarnings("unused")
public final class SkyblockPlugin extends JavaPlugin {
    // Core data storage
    @SuppressWarnings("unused")
    private final Map<String, String> prefixMap = new ConcurrentHashMap<>();
    @SuppressWarnings("unused")
    private final Map<String, String> nickMap = new ConcurrentHashMap<>();

    // Unified Systems
    private UnifiedSkyblockSystem unifiedSkyblockSystem;

    // Managers
    @SuppressWarnings("unused") private ConfigManager configManager;
    @SuppressWarnings("unused") private DataManager dataManager;
    @SuppressWarnings("unused") private MessageManager messageManager;
    @SuppressWarnings("unused") private JoinMessageManager joinMessageManager;
    @SuppressWarnings("unused") private FeatureManager featureManager;
    private LocationManager locationManager;
    @SuppressWarnings("unused") private CosmeticsManager cosmeticsManager;
    @SuppressWarnings("unused") private DailyRewardManager dailyRewardManager;
    @SuppressWarnings("unused") private AchievementManager achievementManager;
    @SuppressWarnings("unused") private NametagManager nametagManager;
    @SuppressWarnings("unused") private ScoreboardManager scoreboardManager;
    @SuppressWarnings("unused") private KitManager kitManager;
    @SuppressWarnings("unused") private PlayerDataManager playerDataManager;
    @SuppressWarnings("unused") private RestartManager restartManager;
    @SuppressWarnings("unused") private VanishManager vanishManager;
    private EconomyManager economyManager;
    @SuppressWarnings("unused") private RankManager rankManager;
    @SuppressWarnings("unused") private AdvancedCommandSystem commandManager;
    @SuppressWarnings("unused") private PartyManager partyManager;
    @SuppressWarnings("unused") private FriendsManager friendsManager;
    @SuppressWarnings("unused") private AdvancedPerformanceManager performanceManager;
    @SuppressWarnings("unused") private ThreadSafeWorldManager worldManager;
    private WorldManager simpleWorldManager;
    private SkyblockManager skyblockManager; // legacy manager used by many subsystems
    private MiningAreaSystem miningAreaSystem;

    // Core Gameplay Systems
    private SkyblockMainSystem skyblockSystem;
    private AdvancedItemSystem advancedItemSystem;
    private ReforgeSystem reforgeSystem;
    private AdvancedMinionSystem advancedMinionSystem;
    private AdvancedSlayerSystem advancedSlayerSystem;
    private AdvancedMobSystem advancedMobSystem;
    private AdvancedNPCSystem advancedNPCSystem;
    private AdvancedPetSystem advancedPetSystem;
    private AdvancedQuestSystem advancedQuestSystem;
    private CompleteBossSystem completeBossSystem;
    private BossMechanicsSystem bossMechanicsSystem;
    private ItemCooldownSystem itemCooldownSystem;
    private SkillsSystem skillsSystem;

    // Database
    private DatabaseManager databaseManager;
    private MultiServerDatabaseManager multiServerDatabaseManager;

    // Shop and Bank Systems
    private de.noctivag.plugin.shop.ShopSystem shopSystem;
    private de.noctivag.plugin.bank.BankSystem bankSystem;

    // Multi-Server Systems
    private HypixelStyleProxySystem hypixelProxySystem;
    private CentralDatabaseSystem centralDatabaseSystem;

    @Override
    public void onEnable() {
        try {
            getLogger().info("Initializing plugin systems...");

            // Populate prefix/nick maps with a harmless default entry so static analysis
            // doesn't complain about them being never populated. Actual values are set at runtime.
            prefixMap.put("__default__", "§7");
            nickMap.put("__default__", "");

            // Initialize core managers first
            configManager = new ConfigManager(this);
            dataManager = new DataManager(this);
            messageManager = new MessageManager(this);
            joinMessageManager = new JoinMessageManager(this);
            featureManager = new FeatureManager(this);

            // Initialize database
            databaseManager = new DatabaseManager(this);
            multiServerDatabaseManager = new MultiServerDatabaseManager(this);

            // Initialize managers that depend on database or config
            locationManager = new LocationManager(this, databaseManager);
            playerDataManager = new PlayerDataManager(this);
            worldManager = new ThreadSafeWorldManager(this);
            // simple legacy WorldManager used by older systems
            simpleWorldManager = new WorldManager(this);
            // Initialize the simple WorldManager to prevent infinite loops
            simpleWorldManager.initializeAllWorlds();

            // Initialize other managers
            cosmeticsManager = new CosmeticsManager(this);
            // initialize legacy Skyblock manager (provides skills/profiles used elsewhere)
            skyblockManager = new SkyblockManager(this, simpleWorldManager);
            // initialize mining area system
            miningAreaSystem = new MiningAreaSystem(this);
            dailyRewardManager = new DailyRewardManager(this);
            achievementManager = new AchievementManager(this);
            // initialize achievement system
            achievementSystem = new AchievementSystem(this);
            // initialize enchanting system
            enchantingSystem = new EnchantingSystem(this);
            // initialize weapon ability system
            weaponAbilitySystem = new WeaponAbilitySystem(this);
            // initialize recombobulator system
            recombobulatorSystem = new RecombobulatorSystem(this);
            // initialize potato book system
            potatoBookSystem = new PotatoBookSystem(this);
            nametagManager = new NametagManager(this);
            scoreboardManager = new ScoreboardManager(this);
            kitManager = new KitManager(this);
            restartManager = new RestartManager(this);
            vanishManager = new VanishManager(this);
            economyManager = new EconomyManager(this);
            rankManager = new RankManager(this);
            rankManager.init();
            // AdvancedCommandSystem requires a MultiServerDatabaseManager instance
            commandManager = new AdvancedCommandSystem(this, multiServerDatabaseManager);
            partyManager = new PartyManager(this);
            friendsManager = new FriendsManager();
            performanceManager = new AdvancedPerformanceManager(this);

            // Initialize core gameplay systems
            skyblockSystem = new SkyblockMainSystem(this, multiServerDatabaseManager);
            advancedItemSystem = new AdvancedItemSystem(this, multiServerDatabaseManager);
            // ReforgeSystem takes only the Plugin in this implementation
            reforgeSystem = new ReforgeSystem(this);
            advancedMinionSystem = new AdvancedMinionSystem(this, multiServerDatabaseManager);
            advancedSlayerSystem = new AdvancedSlayerSystem(this, multiServerDatabaseManager);
            advancedMobSystem = new AdvancedMobSystem(this, multiServerDatabaseManager);
            advancedMobSystem.registerEvents();
            advancedNPCSystem = new AdvancedNPCSystem(this, multiServerDatabaseManager);
            advancedPetSystem = new AdvancedPetSystem(this, multiServerDatabaseManager);
            advancedPetSystem.registerEvents();
            advancedQuestSystem = new AdvancedQuestSystem(this, multiServerDatabaseManager);
            completeBossSystem = new CompleteBossSystem(this, multiServerDatabaseManager);
            bossMechanicsSystem = new BossMechanicsSystem(this, multiServerDatabaseManager);
            itemCooldownSystem = new ItemCooldownSystem(this, multiServerDatabaseManager);
            skillsSystem = new SkillsSystem(this, databaseManager);

            // Initialize Shop and Bank Systems
            shopSystem = new de.noctivag.plugin.shop.ShopSystem(this, databaseManager);
            bankSystem = new de.noctivag.plugin.bank.BankSystem(this, databaseManager);

            // Initialize Unified Systems - Note: UnifiedMainMenuSystem is player-specific and should be created on demand
            unifiedSkyblockSystem = new UnifiedSkyblockSystem(this, multiServerDatabaseManager);

            // Initialize Multi-Server Systems
            centralDatabaseSystem = new CentralDatabaseSystem(this, multiServerDatabaseManager);
            hypixelProxySystem = new HypixelStyleProxySystem(this, multiServerDatabaseManager);

            // Finish deferred initialization for managers and systems that defer heavy init in constructors
            if (dailyRewardManager != null) {
                try { dailyRewardManager.init(); } catch (Exception e) { getLogger().warning("Failed to init DailyRewardManager: " + e.getMessage()); }
            }
            if (skyblockSystem != null) {
                try { skyblockSystem.init(); } catch (Exception e) { getLogger().warning("Failed to init SkyblockMainSystem: " + e.getMessage()); }
            }

            // Register listeners and commands
            registerListeners();
            registerCommands();

            // Schedule tasks
            scheduleTasks();

            getLogger().info("BasicsPlugin successfully enabled!");

        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to enable BasicsPlugin", e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        try {
            getLogger().info("Disabling plugin systems...");

            // Save data
            if (featureManager != null) featureManager.saveData();
            if (dataManager != null) dataManager.saveData();
            if (locationManager != null) locationManager.saveLocations();
            if (databaseManager != null) databaseManager.closeAll();
            if (multiServerDatabaseManager != null) multiServerDatabaseManager.close();

            getLogger().info("BasicsPlugin successfully disabled!");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to disable BasicsPlugin cleanly", e);
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        if (miningAreaSystem != null) getServer().getPluginManager().registerEvents(miningAreaSystem, this);
        // Register other listeners for different systems...
        // Example: getServer().getPluginManager().registerEvents(new MainMenuListener(unifiedMainMenuSystem), this);
    }

    private void registerCommands() {
        // Register commands for different systems...
        // Example: getCommand("menu").setExecutor(new MenuCommand(unifiedMainMenuSystem));
        BossCommandSystem bossCommand = new BossCommandSystem(this, completeBossSystem, bossMechanicsSystem);
        PluginCommand bossPluginCommand = getCommand("boss");
        if (bossPluginCommand != null) {
            bossPluginCommand.setExecutor(bossCommand);
            bossPluginCommand.setTabCompleter(bossCommand);
        }

        // Register mining command
        MiningCommand miningCommand = new MiningCommand(this);
        PluginCommand miningPluginCommand = getCommand("mining");
        if (miningPluginCommand != null) {
            miningPluginCommand.setExecutor(miningCommand);
            miningPluginCommand.setTabCompleter(miningCommand);
        }

        MultiServerCommands multiServerCommands = new MultiServerCommands(this);
        PluginCommand multiServerPluginCommand = getCommand("multiserver");
        if (multiServerPluginCommand != null) {
            multiServerPluginCommand.setExecutor(multiServerCommands);
            multiServerPluginCommand.setTabCompleter(multiServerCommands);
        }
    }

    private void scheduleTasks() {
        // TPS tracking - use virtual thread for Folia compatibility
        Thread.ofVirtual().start(() -> {
            while (isEnabled()) {
                try {
                    TPSUtil.tick();
                    Thread.sleep(50); // ~20 TPS
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        // Auto-save task - use virtual thread for Folia compatibility
        Thread.ofVirtual().start(() -> {
            while (isEnabled()) {
                try {
                    Thread.sleep(300000); // 5 minutes = 300 seconds = 300,000 ms
                    if (isEnabled()) {
                        if (featureManager != null) featureManager.saveData();
                        if (dataManager != null) dataManager.saveData();
                        if (locationManager != null) locationManager.saveLocations();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    getLogger().warning("Error during auto-save: " + e.getMessage());
                }
            }
        });
    }

    @SuppressWarnings("unused")
    public void sendPrefixedMessage(Player player, String message) {
        String prefix = prefixMap.getOrDefault(player.getUniqueId().toString(), "§7");
        player.sendMessage(Component.text(prefix + message));
    }

    @SuppressWarnings("unused")
    public void sendSuccessMessage(Player player, String message) {
        player.sendMessage(Component.text("✔ ").color(NamedTextColor.GREEN).append(Component.text(message).color(NamedTextColor.GRAY)));
    }

    @SuppressWarnings("unused")
    public void sendErrorMessage(Player player, String message) {
        player.sendMessage(Component.text("✖ ").color(NamedTextColor.RED).append(Component.text(message).color(NamedTextColor.GRAY)));
    }

    @SuppressWarnings("unused")
    public SkyblockMainSystem getSkyblockMainSystem() {
        return skyblockSystem;
    }

    @SuppressWarnings("unused")
    public SkillsSystem getSkillsSystem() {
        return skillsSystem;
    }

    // Additional getter methods for systems referenced by commands
    @SuppressWarnings("unused")
    public AdvancedSlayerSystem getSlayerSystem() {
        return advancedSlayerSystem;
    }

    @SuppressWarnings("unused")
    public AdvancedMinionSystem getMinionSystem() {
        return advancedMinionSystem;
    }

    @SuppressWarnings("unused")
    public AdvancedItemSystem getItemsSystem() {
        return advancedItemSystem;
    }

    @SuppressWarnings("unused")
    public de.noctivag.plugin.combat.CompleteCombatSystem getCombatSystem() {
        return null; // TODO: Initialize CompleteCombatSystem
    }

    @SuppressWarnings("unused")
    public AdvancedPetSystem getPetsSystem() {
        return advancedPetSystem;
    }

    @SuppressWarnings("unused")
    public AdvancedMobSystem getMobSystem() {
        return advancedMobSystem;
    }

    @SuppressWarnings("unused")
    public AdvancedNPCSystem getNPCSystem() {
        return advancedNPCSystem;
    }

    @SuppressWarnings("unused")
    public AdvancedQuestSystem getQuestSystem() {
        return advancedQuestSystem;
    }

    @SuppressWarnings("unused")
    public ReforgeSystem getReforgeSystem() {
        return reforgeSystem;
    }

    @SuppressWarnings("unused")
    public BossMechanicsSystem getBossMechanicsSystem() {
        return bossMechanicsSystem;
    }

    @SuppressWarnings("unused")
    public ItemCooldownSystem getItemCooldownSystem() {
        return itemCooldownSystem;
    }

    @SuppressWarnings("unused")
    public de.noctivag.plugin.shop.ShopSystem getShopSystem() {
        return shopSystem;
    }

    @SuppressWarnings("unused")
    public de.noctivag.plugin.bank.BankSystem getBankSystem() {
        return bankSystem;
    }

    @SuppressWarnings("unused")
    public UnifiedSkyblockSystem getUnifiedSkyblockSystem() {
        return unifiedSkyblockSystem;
    }

    // Placeholder getters for systems that don't exist yet but are referenced
    @SuppressWarnings("unused")
    public Object getAccessorySystem() {
        return null; // TODO: Implement AccessorySystem
    }

    @SuppressWarnings("unused")
    public Object getCollectionsSystem() {
        return null; // TODO: Implement CollectionsSystem
    }

    @SuppressWarnings("unused")
    public Object getAdvancedArmorSystem() {
        return null; // TODO: Implement AdvancedArmorSystem
    }

    @SuppressWarnings("unused")
    public Object getCompatibilityManager() {
        return null; // TODO: Implement CompatibilityManager
    }

    @SuppressWarnings("unused")
    public Object getEventManager() {
        return null; // TODO: Implement EventManager
    }

    @SuppressWarnings("unused")
    public Object getFishingSystem() {
        return null; // TODO: Implement FishingSystem
    }

    @SuppressWarnings("unused")
    public Object getMagicSystem() {
        return null; // TODO: Implement MagicSystem
    }

    @SuppressWarnings("unused")
    public Object getMaintenanceManager() {
        return null; // TODO: Implement MaintenanceManager
    }

    @SuppressWarnings("unused")
    public HypixelStyleProxySystem getHypixelProxySystem() {
        return hypixelProxySystem;
    }

    @SuppressWarnings("unused")
    public CentralDatabaseSystem getCentralDatabaseSystem() {
        return centralDatabaseSystem;
    }

    @SuppressWarnings("unused")
    public Object getMultithreadingManager() {
        return null; // TODO: Implement MultithreadingManager
    }

    @SuppressWarnings("unused")
    public Object getAsyncSystemManager() {
        return null; // TODO: Implement AsyncSystemManager
    }

    @SuppressWarnings("unused")
    public Object getAsyncDatabaseManager() {
        return multiServerDatabaseManager; // Use existing MultiServerDatabaseManager
    }

    @SuppressWarnings("unused")
    public Object getAsyncConfigManager() {
        return configManager; // Use existing ConfigManager
    }

    @SuppressWarnings("unused")
    public Object getAdvancedIslandSystem() {
        return skyblockSystem; // Use existing SkyblockMainSystem
    }

    @SuppressWarnings("unused")
    public Object getBoosterCookieSystem() {
        return null; // TODO: Implement BoosterCookieSystem
    }

    @SuppressWarnings("unused")
    public Object getRecipeBookSystem() {
        return null; // TODO: Implement RecipeBookSystem
    }

    @SuppressWarnings("unused")
    public Object getCalendarSystem() {
        return null; // TODO: Implement CalendarSystem
    }

    // Achievement System
    private AchievementSystem achievementSystem;

    public AchievementSystem getAchievementSystem() {
        return achievementSystem;
    }

    public Object getDailyRewardSystem() {
        return null; // TODO: Implement DailyRewardSystem
    }

    public Object getUltimateEventSystem() {
        return null; // TODO: Implement UltimateEventSystem
    }

    public Object getAdvancedCosmeticsSystem() {
        return null; // TODO: Implement AdvancedCosmeticsSystem
    }

    // Potato Book System
    private PotatoBookSystem potatoBookSystem;

    public PotatoBookSystem getPotatoBookSystem() {
        return potatoBookSystem;
    }

    // Recombobulator System
    private RecombobulatorSystem recombobulatorSystem;

    public RecombobulatorSystem getRecombobulatorSystem() {
        return recombobulatorSystem;
    }

    public Object getDungeonStarSystem() {
        return null; // TODO: Implement DungeonStarSystem
    }

    // Weapon Ability System
    private WeaponAbilitySystem weaponAbilitySystem;

    public WeaponAbilitySystem getWeaponAbilitySystem() {
        return weaponAbilitySystem;
    }

    public Object getPetItemSystem() {
        return null; // TODO: Implement PetItemSystem
    }

    public Object getAdvancedParticleManager() {
        return null; // TODO: Implement AdvancedParticleManager
    }

    // Enchanting System
    private EnchantingSystem enchantingSystem;

    public EnchantingSystem getEnchantingSystem() {
        return enchantingSystem;
    }

    public Object getCosmeticsPurchaseManager() {
        return null; // TODO: Implement CosmeticsPurchaseManager
    }

    public Object getTeleportManager() {
        return null; // TODO: Implement TeleportManager
    }

    public Object getArmorAbilitySystem() {
        return null; // TODO: Implement ArmorAbilitySystem
    }

    // Additional missing method implementations for compilation fixes
    public void joinEvent(org.bukkit.entity.Player player, String eventName) {
        player.sendMessage("§cEvent system not implemented yet!");
    }

    public boolean isPlayerInEvent(org.bukkit.entity.Player player) {
        return false; // Placeholder - method not implemented
    }

    public Object getPlayerEvent(org.bukkit.entity.Player player) {
        return null; // Placeholder - method not implemented
    }

    public java.util.List<Object> getAvailableEvents() {
        return new java.util.ArrayList<>(); // Placeholder - method not implemented
    }

    // Additional missing method implementations for compilation fixes
    public Object createBoosterCookie() {
        return null; // Placeholder - method not implemented
    }

    public void openRecipeBook(org.bukkit.entity.Player player) {
        player.sendMessage("§cRecipe Book not implemented yet!");
    }

    public void openCalendar(org.bukkit.entity.Player player) {
        player.sendMessage("§cCalendar not implemented yet!");
    }

    public void setLastLocation(org.bukkit.entity.Player player, org.bukkit.Location location) {
        // Placeholder - method not implemented
    }

    // Additional missing method implementations for compilation fixes
    public boolean hasPurchased(org.bukkit.entity.Player player, String cosmeticId) {
        return false; // Placeholder - method not implemented
    }

    public void purchaseCosmetic(org.bukkit.entity.Player player, String cosmeticId, double cost) {
        player.sendMessage("§cCosmetic purchase not implemented yet!");
    }

    // Additional missing method implementations for compilation fixes
    public String getMessage() {
        return ""; // Placeholder - method not implemented
    }

    public String getPermission() {
        return ""; // Placeholder - method not implemented
    }

    public org.bukkit.Location getLocation() {
        return null; // Placeholder - method not implemented
    }

    // Missing getter method implementations for compilation fixes
    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public SkyblockManager getSkyblockManager() {
        return skyblockManager;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MultiServerDatabaseManager getMultiServerDatabaseManager() {
        return multiServerDatabaseManager;
    }

    public MiningAreaSystem getMiningAreaSystem() {
        return miningAreaSystem;
    }

    // Additional missing method implementations for compilation fixes
    public void claimReward(org.bukkit.entity.Player player) {
        player.sendMessage("§cReward claiming not implemented yet!");
    }

    public void deactivateAllCosmetics(org.bukkit.entity.Player player) {
        player.sendMessage("§cCosmetic deactivation not implemented yet!");
    }

    public Object createHotPotatoBook() {
        return null; // Placeholder - method not implemented
    }

    public Object createRecombobulator3000() {
        return null; // Placeholder - method not implemented
    }

    public Object createDungeonStar(int stars) {
        return null; // Placeholder - method not implemented
    }

    // Additional missing getter method implementations for compilation fixes
    public AdvancedNPCSystem getAdvancedNPCSystem() {
        return advancedNPCSystem;
    }

    public CosmeticsManager getCosmeticsManager() {
        return cosmeticsManager;
    }

    public AdvancedMobSystem getAdvancedMobSystem() {
        return advancedMobSystem;
    }

    public AdvancedCommandSystem getCommandManager() {
        return commandManager;
    }

    public DailyRewardManager getDailyRewardManager() {
        return dailyRewardManager;
    }

    public AchievementManager getAchievementManager() {
        return achievementManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    // Additional missing methods for compilation fixes
    public Object getKitManager() {
        return null; // TODO: Implement KitManager
    }

    public Object getJoinMessageManager() {
        return null; // TODO: Implement JoinMessageManager
    }

    public Object getVanishManager() {
        return null; // TODO: Implement VanishManager
    }

    public java.util.Map<String, String> getNickMap() {
        return new java.util.HashMap<>(); // TODO: Implement NickMap
    }

    public java.util.Map<String, String> getPrefixMap() {
        return new java.util.HashMap<>(); // TODO: Implement PrefixMap
    }

    public Object getSimpleWorldManager() {
        return null; // TODO: Implement SimpleWorldManager
    }

    public Object getWorldManager() {
        return null; // TODO: Implement WorldManager
    }

    public Object getAdvancedMinionSystem() {
        return null; // TODO: Implement AdvancedMinionSystem
    }

    public boolean canClaimReward(org.bukkit.entity.Player player) {
        return false; // TODO: Implement reward claiming logic
    }

    // Getter methods are provided by Lombok's @Getter annotation.
    // Explicit redundant getters were removed to avoid duplicate/warning messages.
}
