package de.noctivag.skyblock.config;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Comprehensive Skyblock Configuration Wrapper
 * Provides type-safe access to all configuration values
 *
 * This class wraps the config.yml and provides convenient methods
 * to access all settings with proper defaults
 */
public class SkyblockConfig {

    private final SkyblockPlugin plugin;
    private FileConfiguration config;

    public SkyblockConfig(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    /**
     * Reload configuration from disk
     */
    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    // =====================================
    // GENERAL SETTINGS
    // =====================================

    public String getLanguage() {
        return config.getString("general.language", "en_US");
    }

    public int getAutoSaveInterval() {
        return config.getInt("general.auto-save-interval", 300);
    }

    public boolean isDebugMode() {
        return config.getBoolean("general.debug-mode", false);
    }

    public String getPluginPrefix() {
        return config.getString("general.plugin-prefix", "&8[&6SkyBlock&8] &7");
    }

    // =====================================
    // FEATURE TOGGLES
    // =====================================

    public boolean isSkillsSystemEnabled() {
        return config.getBoolean("features.skills-system", true);
    }

    public boolean isCollectionsSystemEnabled() {
        return config.getBoolean("features.collections-system", true);
    }

    public boolean isMinionsSystemEnabled() {
        return config.getBoolean("features.minions-system", true);
    }

    public boolean isPetsSystemEnabled() {
        return config.getBoolean("features.pets-system", true);
    }

    public boolean isSlayersSystemEnabled() {
        return config.getBoolean("features.slayers-system", true);
    }

    public boolean isDungeonsSystemEnabled() {
        return config.getBoolean("features.dungeons-system", true);
    }

    public boolean isCombatEnhancementsEnabled() {
        return config.getBoolean("features.combat-enhancements", true);
    }

    public boolean isCustomDamageSystemEnabled() {
        return config.getBoolean("features.custom-damage-system", true);
    }

    public boolean isBazaarSystemEnabled() {
        return config.getBoolean("features.bazaar-system", true);
    }

    public boolean isAuctionHouseEnabled() {
        return config.getBoolean("features.auction-house", true);
    }

    public boolean isBankingSystemEnabled() {
        return config.getBoolean("features.banking-system", true);
    }

    public boolean isPlayerTradingEnabled() {
        return config.getBoolean("features.player-trading", true);
    }

    public boolean isRecipeBookEnabled() {
        return config.getBoolean("features.recipe-book", true);
    }

    public boolean isAchievementsEnabled() {
        return config.getBoolean("features.achievements", true);
    }

    public boolean isBestiarySystemEnabled() {
        return config.getBoolean("features.bestiary-system", true);
    }

    public boolean isCalendarSystemEnabled() {
        return config.getBoolean("features.calendar-system", true);
    }

    public boolean isDailyRewardsEnabled() {
        return config.getBoolean("features.daily-rewards", true);
    }

    public boolean isFastTravelEnabled() {
        return config.getBoolean("features.fast-travel", true);
    }

    public boolean isWardrobeSystemEnabled() {
        return config.getBoolean("features.wardrobe-system", true);
    }

    public boolean isAccessoryBagEnabled() {
        return config.getBoolean("features.accessory-bag", true);
    }

    public boolean isCustomEnchantsEnabled() {
        return config.getBoolean("features.custom-enchantments", true);
    }

    public boolean isReforgeSystemEnabled() {
        return config.getBoolean("features.reforge-system", true);
    }

    public boolean isBrewingSystemEnabled() {
        return config.getBoolean("features.brewing-system", true);
    }

    public boolean isCustomIslandsEnabled() {
        return config.getBoolean("features.custom-islands", true);
    }

    public boolean isZoneSystemEnabled() {
        return config.getBoolean("features.zone-system", true);
    }

    public boolean isCustomMobsEnabled() {
        return config.getBoolean("features.custom-mobs", true);
    }

    public boolean isNpcSystemEnabled() {
        return config.getBoolean("features.npc-system", true);
    }

    public boolean isFriendsSystemEnabled() {
        return config.getBoolean("features.friends-system", false);
    }

    public boolean isGuildSystemEnabled() {
        return config.getBoolean("features.guild-system", false);
    }

    public boolean isPartySystemEnabled() {
        return config.getBoolean("features.party-system", true);
    }

    public boolean isParticleEffectsEnabled() {
        return config.getBoolean("features.particle-effects", true);
    }

    public boolean isChatCosmeticsEnabled() {
        return config.getBoolean("features.chat-cosmetics", true);
    }

    // =====================================
    // SKILLS SYSTEM
    // =====================================

    public boolean isSkillsEnabled() {
        return config.getBoolean("skills.enabled", true);
    }

    public int getMaxSkillLevel(String skill) {
        return config.getInt("skills.max-levels." + skill.toLowerCase(), 50);
    }

    public double getSkillXpMultiplier(String skill) {
        return config.getDouble("skills.xp-multipliers." + skill.toLowerCase(), 1.0);
    }

    public boolean areSkillStatBonusesEnabled() {
        return config.getBoolean("skills.stat-bonuses-enabled", true);
    }

    public boolean areSkillLevelupsAnnounced() {
        return config.getBoolean("skills.announce-levelups", true);
    }

    public boolean showSkillXpMessages() {
        return config.getBoolean("skills.show-xp-messages", true);
    }

    public double getSkillXpMessageThreshold() {
        return config.getDouble("skills.xp-message-threshold", 1.0);
    }

    // =====================================
    // COLLECTIONS SYSTEM
    // =====================================

    public boolean isCollectionsEnabled() {
        return config.getBoolean("collections.enabled", true);
    }

    public boolean areCollectionMultipliersEnabled() {
        return config.getBoolean("collections.multipliers-enabled", true);
    }

    public double getDefaultCollectionMultiplier() {
        return config.getDouble("collections.default-multiplier", 1.0);
    }

    public boolean areCollectionMilestonesAnnounced() {
        return config.getBoolean("collections.announce-milestones", true);
    }

    public boolean showCollectionMessages() {
        return config.getBoolean("collections.show-collection-messages", false);
    }

    public boolean autoUnlockRecipes() {
        return config.getBoolean("collections.auto-unlock-recipes", true);
    }

    // =====================================
    // MINIONS SYSTEM
    // =====================================

    public boolean isMinionsEnabled() {
        return config.getBoolean("minions.enabled", true);
    }

    public int getMaxMinionsPerPlayer() {
        return config.getInt("minions.max-minions-per-player", 30);
    }

    public double getMinionSpeedMultiplier() {
        return config.getDouble("minions.speed-multiplier", 1.0);
    }

    public boolean areMinionUpgradesAllowed() {
        return config.getBoolean("minions.allow-upgrades", true);
    }

    public boolean isMinionFuelSystemEnabled() {
        return config.getBoolean("minions.fuel-system-enabled", true);
    }

    public double getMinionStorageMultiplier() {
        return config.getDouble("minions.storage-multiplier", 1.0);
    }

    public boolean isMinionAutoSellEnabled() {
        return config.getBoolean("minions.auto-sell-enabled", true);
    }

    public boolean requireIslandForMinionPlacement() {
        return config.getBoolean("minions.placement.require-island", true);
    }

    public boolean checkMinionDistance() {
        return config.getBoolean("minions.placement.check-distance", true);
    }

    public int getMinDistanceBetweenMinions() {
        return config.getInt("minions.placement.min-distance-between", 3);
    }

    // =====================================
    // PETS SYSTEM
    // =====================================

    public boolean isPetsEnabled() {
        return config.getBoolean("pets.enabled", true);
    }

    public int getMaxPetLevel() {
        return config.getInt("pets.max-level", 100);
    }

    public boolean areLegendaryPetsAllowed() {
        return config.getBoolean("pets.allow-legendary", true);
    }

    public boolean areMythicPetsAllowed() {
        return config.getBoolean("pets.allow-mythic", true);
    }

    public double getPetXpMultiplier() {
        return config.getDouble("pets.xp-multiplier", 1.0);
    }

    public boolean isPetAutoPickupEnabled() {
        return config.getBoolean("pets.auto-pickup", true);
    }

    public boolean arePetAbilitiesEnabled() {
        return config.getBoolean("pets.abilities-enabled", true);
    }

    public boolean arePetDropsEnabled() {
        return config.getBoolean("pets.pet-drops-enabled", true);
    }

    // =====================================
    // SLAYERS SYSTEM
    // =====================================

    public boolean isSlayersEnabled() {
        return config.getBoolean("slayers.enabled", true);
    }

    public boolean isSlayerTypeEnabled(String type) {
        return config.getBoolean("slayers.types." + type.toLowerCase(), true);
    }

    public double getSlayerXpMultiplier() {
        return config.getDouble("slayers.xp-multiplier", 1.0);
    }

    public int getBossSpawnDelay() {
        return config.getInt("slayers.boss-spawn-delay", 5);
    }

    public boolean areSlayerDropsEnabled() {
        return config.getBoolean("slayers.drops-enabled", true);
    }

    public double getSlayerDropRateMultiplier() {
        return config.getDouble("slayers.drop-rate-multiplier", 1.0);
    }

    public boolean requireQuestForBoss() {
        return config.getBoolean("slayers.require-quest", true);
    }

    // =====================================
    // DUNGEONS SYSTEM
    // =====================================

    public boolean isDungeonsEnabled() {
        return config.getBoolean("dungeons.enabled", true);
    }

    public boolean isDungeonTypeEnabled(String type) {
        return config.getBoolean("dungeons.types." + type.toLowerCase(), true);
    }

    public int getDefaultMaxFloor() {
        return config.getInt("dungeons.default-max-floor", 0);
    }

    public int getMaxPartySize() {
        return config.getInt("dungeons.max-party-size", 5);
    }

    public boolean requireFullParty() {
        return config.getBoolean("dungeons.require-full-party", false);
    }

    public boolean areDungeonClassesEnabled() {
        return config.getBoolean("dungeons.classes-enabled", true);
    }

    public boolean isDungeonClassEnabled(String className) {
        return config.getBoolean("dungeons.classes." + className.toLowerCase(), true);
    }

    public double getDungeonLootMultiplier() {
        return config.getDouble("dungeons.rewards.loot-multiplier", 1.0);
    }

    public boolean isEssenceEnabled() {
        return config.getBoolean("dungeons.rewards.essence-enabled", true);
    }

    public boolean areDungeonStarsEnabled() {
        return config.getBoolean("dungeons.rewards.stars-enabled", true);
    }

    public boolean areSecretsEnabled() {
        return config.getBoolean("dungeons.mechanics.secrets-enabled", true);
    }

    public boolean arePuzzlesEnabled() {
        return config.getBoolean("dungeons.mechanics.puzzles-enabled", true);
    }

    public boolean isBloodRoomEnabled() {
        return config.getBoolean("dungeons.mechanics.blood-room-enabled", true);
    }

    public boolean areBossMechanicsEnabled() {
        return config.getBoolean("dungeons.mechanics.boss-mechanics-enabled", true);
    }

    // =====================================
    // COMBAT SYSTEM
    // =====================================

    public boolean isCombatEnabled() {
        return config.getBoolean("combat.enabled", true);
    }

    public boolean isCustomDamageEnabled() {
        return config.getBoolean("combat.custom-damage-enabled", true);
    }

    public boolean isDefenseSystemEnabled() {
        return config.getBoolean("combat.defense-system", true);
    }

    public boolean areCriticalHitsEnabled() {
        return config.getBoolean("combat.critical-hits", true);
    }

    public boolean isStrengthScalingEnabled() {
        return config.getBoolean("combat.strength-scaling", true);
    }

    public boolean areCombatAbilitiesEnabled() {
        return config.getBoolean("combat.abilities-enabled", true);
    }

    public boolean areWeaponAbilitiesEnabled() {
        return config.getBoolean("combat.weapon-abilities", true);
    }

    public boolean areArmorSetBonusesEnabled() {
        return config.getBoolean("combat.armor-set-bonuses", true);
    }

    public boolean isHealthRegenEnabled() {
        return config.getBoolean("combat.health-regen.enabled", true);
    }

    public double getHealthRegenAmount() {
        return config.getDouble("combat.health-regen.amount-per-tick", 0.5);
    }

    public int getHealthRegenDelay() {
        return config.getInt("combat.health-regen.delay-after-damage", 60);
    }

    public boolean isManaSystemEnabled() {
        return config.getBoolean("combat.mana.enabled", true);
    }

    public int getDefaultMaxMana() {
        return config.getInt("combat.mana.max-default", 100);
    }

    public double getManaRegenPerTick() {
        return config.getDouble("combat.mana.regen-per-tick", 1.0);
    }

    public double getManaCostMultiplier() {
        return config.getDouble("combat.mana.cost-multiplier", 1.0);
    }

    // =====================================
    // ECONOMY SYSTEM
    // =====================================

    public boolean isEconomyEnabled() {
        return config.getBoolean("economy.enabled", true);
    }

    public double getStartingBalance() {
        return config.getDouble("economy.starting-balance", 100.0);
    }

    public String getCurrencyName() {
        return config.getString("economy.currency.name", "Coins");
    }

    public String getCurrencySymbol() {
        return config.getString("economy.currency.symbol", "⛃");
    }

    public String getCurrencyPlural() {
        return config.getString("economy.currency.plural", "Coins");
    }

    public boolean isBazaarEnabled() {
        return config.getBoolean("economy.bazaar.enabled", true);
    }

    public double getBazaarSalesTax() {
        return config.getDouble("economy.bazaar.sales-tax", 1.0);
    }

    public int getBazaarUpdateInterval() {
        return config.getInt("economy.bazaar.update-interval", 100);
    }

    public boolean areInstantTransactionsEnabled() {
        return config.getBoolean("economy.bazaar.instant-transactions", true);
    }

    public boolean isAuctionEnabled() {
        return config.getBoolean("economy.auction-house.enabled", true);
    }

    public double getAuctionListingFee() {
        return config.getDouble("economy.auction-house.listing-fee", 1.0);
    }

    public int getMaxAuctionDuration() {
        return config.getInt("economy.auction-house.max-duration", 48);
    }

    public boolean isBinEnabled() {
        return config.getBoolean("economy.auction-house.bin-enabled", true);
    }

    public boolean isBankingEnabled() {
        return config.getBoolean("economy.banking.enabled", true);
    }

    public boolean isInterestEnabled() {
        return config.getBoolean("economy.banking.interest-enabled", true);
    }

    public double getInterestRate() {
        return config.getDouble("economy.banking.interest-rate", 0.1);
    }

    public double getMaxBankBalance() {
        return config.getDouble("economy.banking.max-balance", 1000000000.0);
    }

    // =====================================
    // ITEMS SYSTEM
    // =====================================

    public boolean areCustomItemsEnabled() {
        return config.getBoolean("items.custom-items-enabled", true);
    }

    public boolean areItemAbilitiesEnabled() {
        return config.getBoolean("items.abilities-enabled", true);
    }

    public boolean areItemStatsEnabled() {
        return config.getBoolean("items.stats-enabled", true);
    }

    public boolean isRaritySystemEnabled() {
        return config.getBoolean("items.rarity-system", true);
    }

    public boolean isReforgingEnabled() {
        return config.getBoolean("items.reforging-enabled", true);
    }

    public boolean isRecombobulatorEnabled() {
        return config.getBoolean("items.recombobulator-enabled", true);
    }

    public boolean areGemstonesEnabled() {
        return config.getBoolean("items.gemstones-enabled", true);
    }

    public boolean areDungeonStarsEnabled() {
        return config.getBoolean("items.dungeon-stars-enabled", true);
    }

    public boolean areWeaponAbilitiesItemEnabled() {
        return config.getBoolean("items.weapons.abilities-enabled", true);
    }

    public boolean areCooldownsEnabled() {
        return config.getBoolean("items.weapons.cooldowns-enabled", true);
    }

    public boolean areManaCostsEnabled() {
        return config.getBoolean("items.weapons.mana-costs-enabled", true);
    }

    public boolean areArmorSetBonusesItemEnabled() {
        return config.getBoolean("items.armor.set-bonuses-enabled", true);
    }

    public boolean isFullSetBonusOnly() {
        return config.getBoolean("items.armor.full-set-bonus-only", false);
    }

    public boolean isEfficiencyScalingEnabled() {
        return config.getBoolean("items.tools.efficiency-scaling", true);
    }

    public boolean isFortuneEnabled() {
        return config.getBoolean("items.tools.fortune-enabled", true);
    }

    // =====================================
    // ENCHANTMENTS SYSTEM
    // =====================================

    public boolean isEnchantsEnabled() {
        return config.getBoolean("enchantments.enabled", true);
    }

    public boolean areCustomEnchantsEnabledFull() {
        return config.getBoolean("enchantments.custom-enchants", true);
    }

    public double getMaxLevelMultiplier() {
        return config.getDouble("enchantments.max-level-multiplier", 1.0);
    }

    public double getXpMultiplier() {
        return config.getDouble("enchantments.costs.xp-multiplier", 1.0);
    }

    public boolean isLapisRequired() {
        return config.getBoolean("enchantments.costs.lapis-required", true);
    }

    public boolean isCustomEnchantEnabled(String enchantName) {
        return config.getBoolean("enchantments.custom." + enchantName.toLowerCase(), true);
    }

    // =====================================
    // RECIPE BOOK
    // =====================================

    public boolean isRecipeBookEnabledFull() {
        return config.getBoolean("recipe-book.enabled", true);
    }

    public boolean autoUnlockFromCollections() {
        return config.getBoolean("recipe-book.auto-unlock-from-collections", true);
    }

    public boolean autoUnlockFromAchievements() {
        return config.getBoolean("recipe-book.auto-unlock-from-achievements", true);
    }

    public boolean showRequirements() {
        return config.getBoolean("recipe-book.show-requirements", true);
    }

    public boolean isQuickCraftEnabled() {
        return config.getBoolean("recipe-book.quick-craft-enabled", true);
    }

    // =====================================
    // ACHIEVEMENTS & BESTIARY
    // =====================================

    public boolean isAchievementsEnabledFull() {
        return config.getBoolean("achievements.enabled", true);
    }

    public boolean announceAchievementCompletion() {
        return config.getBoolean("achievements.announce-completion", true);
    }

    public boolean areAchievementRewardsEnabled() {
        return config.getBoolean("achievements.rewards-enabled", true);
    }

    public boolean isBestiaryEnabled() {
        return config.getBoolean("bestiary.enabled", true);
    }

    public boolean trackKills() {
        return config.getBoolean("bestiary.track-kills", true);
    }

    public boolean areMilestoneRewardsEnabled() {
        return config.getBoolean("bestiary.milestone-rewards", true);
    }

    public boolean showBestiaryInMenu() {
        return config.getBoolean("bestiary.show-in-menu", true);
    }

    // =====================================
    // MOBS & SPAWNING
    // =====================================

    public boolean isCustomSpawningEnabled() {
        return config.getBoolean("mobs.custom-spawning", true);
    }

    public boolean isCustomAiEnabled() {
        return config.getBoolean("mobs.custom-ai-enabled", true);
    }

    public double getSpawnRateMultiplier() {
        return config.getDouble("mobs.spawn-rate-multiplier", 1.0);
    }

    public boolean areBossesEnabled() {
        return config.getBoolean("mobs.bosses-enabled", true);
    }

    public boolean isBossSpawnNotificationEnabled() {
        return config.getBoolean("mobs.boss-spawn-notification", true);
    }

    public boolean areCustomDropsEnabled() {
        return config.getBoolean("mobs.custom-drops-enabled", true);
    }

    public double getDropRateMultiplier() {
        return config.getDouble("mobs.drop-rate-multiplier", 1.0);
    }

    // =====================================
    // PERFORMANCE
    // =====================================

    public boolean isAsyncEnabled() {
        return config.getBoolean("performance.async-enabled", true);
    }

    public int getThreadPoolSize() {
        return config.getInt("performance.thread-pool-size", 8);
    }

    public boolean isMonitorTps() {
        return config.getBoolean("performance.monitor-tps", true);
    }

    public int getTpsSampleInterval() {
        return config.getInt("performance.tps-sample-interval", 5);
    }

    public boolean isAsyncChunkLoading() {
        return config.getBoolean("performance.async-chunk-loading", true);
    }

    public int getBatchSize() {
        return config.getInt("performance.batch-size", 100);
    }

    public int getPlayerDataCacheSize() {
        return config.getInt("performance.cache.player-data-cache-size", 500);
    }

    public int getItemCacheSize() {
        return config.getInt("performance.cache.item-cache-size", 1000);
    }

    public int getCollectionCacheMinutes() {
        return config.getInt("performance.cache.collection-cache-minutes", 10);
    }

    // =====================================
    // DEBUG
    // =====================================

    public boolean isDebugEnabled() {
        return config.getBoolean("debug.enabled", false);
    }

    public boolean logQueries() {
        return config.getBoolean("debug.log-queries", false);
    }

    public boolean logItems() {
        return config.getBoolean("debug.log-items", false);
    }

    public boolean logSkillXp() {
        return config.getBoolean("debug.log-skill-xp", false);
    }

    public boolean isPerformanceMonitoring() {
        return config.getBoolean("debug.performance-monitoring", true);
    }

    // =====================================
    // WORLDS & ISLANDS
    // =====================================

    public boolean isWorldEnabled(String worldName) {
        return config.getBoolean("worlds." + worldName + ".enabled", true);
    }

    public int getWorldRequiredLevel(String worldName) {
        return config.getInt("worlds." + worldName + ".required-level", 0);
    }

    // =====================================
    // MESSAGES
    // =====================================

    public String getMessagePrefix() {
        return config.getString("messages.prefix", "&8[&6SkyBlock&8] &7");
    }

    public String getErrorPrefix() {
        return config.getString("messages.error-prefix", "&8[&cError&8] &7");
    }

    public String getSuccessPrefix() {
        return config.getString("messages.success-prefix", "&8[&a✓&8] &7");
    }

    public String getJoinMessage() {
        return config.getString("messages.join-message", "&7[&a+&7] &e%player%");
    }

    public String getQuitMessage() {
        return config.getString("messages.quit-message", "&7[&c-&7] &e%player%");
    }

    public String getNoPermissionMessage() {
        return config.getString("messages.no-permission", "&cYou don't have permission to do that!");
    }
}
