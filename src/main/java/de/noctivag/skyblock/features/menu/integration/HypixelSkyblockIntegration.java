package de.noctivag.skyblock.features.menu.integration;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.menu.SkyblockMenuSystem;
import de.noctivag.skyblock.features.menu.types.MenuType;

import de.noctivag.skyblock.features.dungeons.DungeonSystem;
import de.noctivag.skyblock.features.minions.AdvancedMinionSystem;
import de.noctivag.skyblock.features.collections.AdvancedCollectionsSystem;
import de.noctivag.skyblock.features.skills.AdvancedSkillsSystem;
import de.noctivag.skyblock.features.armor.DragonArmorSystem;
import de.noctivag.skyblock.features.weapons.LegendaryWeaponsSystem;
import de.noctivag.skyblock.features.pets.AdvancedPetsSystem;
import de.noctivag.skyblock.features.events.AdvancedEventsSystem;
import de.noctivag.skyblock.features.economy.AdvancedEconomySystem;
import de.noctivag.skyblock.features.cosmetics.AdvancedCosmeticsSystem;
import de.noctivag.skyblock.features.stats.CompleteStatsSystem;
import de.noctivag.skyblock.features.weapons.CompleteWeaponsSystem;
import de.noctivag.skyblock.features.armor.CompleteArmorSystem;
import de.noctivag.skyblock.features.tools.CompleteToolsSystem;
import de.noctivag.skyblock.features.enchantments.CompleteEnchantmentsSystem;

import org.bukkit.entity.Player;
import de.noctivag.skyblock.SkyblockPlugin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Complete Hypixel SkyBlock Integration System
 * Integrates all systems exactly like the original Hypixel SkyBlock
 */
public class HypixelSkyblockIntegration {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final SkyblockMenuSystem menuSystem;
    
    // All integrated systems
    private final DungeonSystem dungeonSystem;
    private final AdvancedMinionSystem minionSystem;
    private final AdvancedCollectionsSystem collectionsSystem;
    private final AdvancedSkillsSystem skillsSystem;
    private final DragonArmorSystem dragonArmorSystem;
    private final LegendaryWeaponsSystem legendaryWeaponsSystem;
    private final AdvancedPetsSystem petsSystem;
    private final AdvancedEventsSystem eventsSystem;
    private final AdvancedEconomySystem economySystem;
    private final AdvancedCosmeticsSystem cosmeticsSystem;
    private final CompleteStatsSystem statsSystem;
    private final CompleteWeaponsSystem weaponsSystem;
    private final CompleteArmorSystem armorSystem;
    private final CompleteToolsSystem toolsSystem;
    private final CompleteEnchantmentsSystem enchantmentsSystem;
    
    // Player session management
    private final Map<UUID, PlayerSkyblockSession> playerSessions = new ConcurrentHashMap<>();
    
    public HypixelSkyblockIntegration(SkyblockPlugin SkyblockPlugin, SkyblockMenuSystem menuSystem) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.menuSystem = menuSystem;
        
        // Initialize all systems
        this.dungeonSystem = new DungeonSystem();
        this.minionSystem = new AdvancedMinionSystem((de.noctivag.skyblock.SkyblockPlugin) SkyblockPlugin);
        this.collectionsSystem = new AdvancedCollectionsSystem();
        this.skillsSystem = new AdvancedSkillsSystem();
        this.dragonArmorSystem = new DragonArmorSystem();
        this.legendaryWeaponsSystem = new LegendaryWeaponsSystem();
        this.petsSystem = new AdvancedPetsSystem();
        this.eventsSystem = new AdvancedEventsSystem();
        this.economySystem = new AdvancedEconomySystem();
        this.cosmeticsSystem = new AdvancedCosmeticsSystem();
        this.statsSystem = new CompleteStatsSystem();
        this.weaponsSystem = new CompleteWeaponsSystem();
        this.armorSystem = new CompleteArmorSystem();
        this.toolsSystem = new CompleteToolsSystem();
        this.enchantmentsSystem = new CompleteEnchantmentsSystem();
    }
    
    /**
     * Initialize all systems like Hypixel SkyBlock
     */
    public CompletableFuture<Void> initializeAllSystems() {
        return CompletableFuture.runAsync(() -> {
            try {
                SkyblockPlugin.getLogger().info("Initializing complete Hypixel SkyBlock integration...");
                
                // Initialize all systems in parallel for better performance
                CompletableFuture.allOf(
                    dungeonSystem.initialize(),
                    minionSystem.initialize(),
                    collectionsSystem.initialize(),
                    skillsSystem.initialize(),
                    dragonArmorSystem.initialize(),
                    legendaryWeaponsSystem.initialize(),
                    petsSystem.initialize(),
                    eventsSystem.initialize(),
                    economySystem.initialize(),
                    cosmeticsSystem.initialize(),
                    statsSystem.initialize(),
                    weaponsSystem.initialize(),
                    armorSystem.initialize(),
                    toolsSystem.initialize(),
                    enchantmentsSystem.initialize()
                ).join();
                
                SkyblockPlugin.getLogger().info("All Hypixel SkyBlock systems initialized successfully!");
                
            } catch (Exception e) {
                SkyblockPlugin.getLogger().severe("Failed to initialize Hypixel SkyBlock systems: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Create player session like Hypixel SkyBlock
     */
    public PlayerSkyblockSession createPlayerSession(Player player) {
        UUID playerId = player.getUniqueId();
        
        if (playerSessions.containsKey(playerId)) {
            return playerSessions.get(playerId);
        }
        
        PlayerSkyblockSession session = new PlayerSkyblockSession(
            player, 
            dungeonSystem,
            minionSystem,
            collectionsSystem,
            skillsSystem,
            dragonArmorSystem,
            legendaryWeaponsSystem,
            petsSystem,
            eventsSystem,
            economySystem,
            cosmeticsSystem,
            statsSystem,
            weaponsSystem,
            armorSystem,
            toolsSystem,
            enchantmentsSystem
        );
        
        playerSessions.put(playerId, session);
        return session;
    }
    
    /**
     * Get player session
     */
    public PlayerSkyblockSession getPlayerSession(Player player) {
        return playerSessions.get(player.getUniqueId());
    }
    
    /**
     * Remove player session
     */
    public void removePlayerSession(Player player) {
        PlayerSkyblockSession session = playerSessions.remove(player.getUniqueId());
        if (session != null) {
            session.saveAll();
        }
    }
    
    /**
     * Handle menu action with full system integration
     */
    public void handleMenuAction(Player player, String action, String[] args) {
        PlayerSkyblockSession session = getPlayerSession(player);
        if (session == null) {
            session = createPlayerSession(player);
        }
        
        switch (action) {
            case "OPEN_WEAPONS_MENU":
                session.openWeaponsMenu();
                break;
                
            case "OPEN_ARMOR_MENU":
                session.openArmorMenu();
                break;
                
            case "OPEN_TOOLS_MENU":
                session.openToolsMenu();
                break;
                
            case "OPEN_ACCESSORIES_MENU":
                session.openAccessoriesMenu();
                break;
                
            case "OPEN_PETS_MENU":
                session.openPetsMenu();
                break;
                
            case "OPEN_MINIONS_MENU":
                session.openMinionsMenu();
                break;
                
            case "OPEN_COLLECTIONS_MENU":
                session.openCollectionsMenu();
                break;
                
            case "OPEN_SKILLS_MENU":
                session.openSkillsMenu();
                break;
                
            case "OPEN_DUNGEONS_MENU":
                session.openDungeonsMenu();
                break;
                
            case "OPEN_ECONOMY_MENU":
                session.openEconomyMenu();
                break;
                
            case "OPEN_EVENTS_MENU":
                session.openEventsMenu();
                break;
                
            case "OPEN_STATS_MENU":
                session.openStatsMenu();
                break;
                
            case "OPEN_ENCHANTMENTS_MENU":
                session.openEnchantmentsMenu();
                break;
                
            case "GIVE_WEAPON":
                if (args.length > 0) {
                    session.giveWeapon(args[0]);
                }
                break;
                
            case "GIVE_ARMOR":
                if (args.length > 0) {
                    session.giveArmor(args[0]);
                }
                break;
                
            case "GIVE_TOOL":
                if (args.length > 0) {
                    session.giveTool(args[0]);
                }
                break;
                
            case "GIVE_PET":
                if (args.length > 0) {
                    session.givePet(args[0]);
                }
                break;
                
            case "GIVE_MINION":
                if (args.length > 0) {
                    session.giveMinion(args[0]);
                }
                break;
                
            default:
                player.sendMessage("§cUnbekannte Aktion: " + action);
                break;
        }
    }
    
    /**
     * Shutdown all systems
     */
    public CompletableFuture<Void> shutdownAllSystems() {
        return CompletableFuture.runAsync(() -> {
            try {
                // Save all player sessions
                playerSessions.values().forEach(PlayerSkyblockSession::saveAll);
                playerSessions.clear();
                
                // Shutdown all systems
                CompletableFuture.allOf(
                    dungeonSystem.shutdown(),
                    minionSystem.shutdown(),
                    collectionsSystem.shutdown(),
                    skillsSystem.shutdown(),
                    dragonArmorSystem.shutdown(),
                    legendaryWeaponsSystem.shutdown(),
                    petsSystem.shutdown(),
                    eventsSystem.shutdown(),
                    economySystem.shutdown(),
                    cosmeticsSystem.shutdown(),
                    statsSystem.shutdown(),
                    weaponsSystem.shutdown(),
                    armorSystem.shutdown(),
                    toolsSystem.shutdown(),
                    enchantmentsSystem.shutdown()
                ).join();
                
                SkyblockPlugin.getLogger().info("All Hypixel SkyBlock systems shut down successfully!");
                
            } catch (Exception e) {
                SkyblockPlugin.getLogger().severe("Error shutting down Hypixel SkyBlock systems: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    // Getters for all systems
    public DungeonSystem getDungeonSystem() { return dungeonSystem; }
    public AdvancedMinionSystem getMinionSystem() { return minionSystem; }
    public AdvancedCollectionsSystem getCollectionsSystem() { return collectionsSystem; }
    public AdvancedSkillsSystem getSkillsSystem() { return skillsSystem; }
    public DragonArmorSystem getDragonArmorSystem() { return dragonArmorSystem; }
    public LegendaryWeaponsSystem getLegendaryWeaponsSystem() { return legendaryWeaponsSystem; }
    public AdvancedPetsSystem getPetsSystem() { return petsSystem; }
    public AdvancedEventsSystem getEventsSystem() { return eventsSystem; }
    public AdvancedEconomySystem getEconomySystem() { return economySystem; }
    public AdvancedCosmeticsSystem getCosmeticsSystem() { return cosmeticsSystem; }
    public CompleteStatsSystem getStatsSystem() { return statsSystem; }
    public CompleteWeaponsSystem getWeaponsSystem() { return weaponsSystem; }
    public CompleteArmorSystem getArmorSystem() { return armorSystem; }
    public CompleteToolsSystem getToolsSystem() { return toolsSystem; }
    public CompleteEnchantmentsSystem getEnchantmentsSystem() { return enchantmentsSystem; }
    
    /**
     * Get system status
     */
    public boolean isAllSystemsReady() {
        // Check if all systems are initialized (not null)
        return dungeonSystem != null &&
               minionSystem != null &&
               collectionsSystem != null &&
               skillsSystem != null &&
               dragonArmorSystem != null &&
               legendaryWeaponsSystem != null &&
               petsSystem != null &&
               eventsSystem != null &&
               economySystem != null &&
               cosmeticsSystem != null &&
               statsSystem != null &&
               weaponsSystem != null &&
               armorSystem != null &&
               toolsSystem != null &&
               enchantmentsSystem != null;
    }
}
