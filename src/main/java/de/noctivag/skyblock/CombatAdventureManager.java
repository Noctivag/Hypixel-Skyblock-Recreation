package de.noctivag.skyblock;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.slayers.CompleteSlayerSystem;
import de.noctivag.skyblock.dungeons.CompleteDungeonSystem;
import de.noctivag.skyblock.combat.CompleteCombatSystem;
import de.noctivag.skyblock.gui.CombatAdventureGUI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Combat & Adventure Manager - Central manager for all combat and adventure systems
 *
 * Features:
 * - Integrated system management
 * - Cross-system interactions
 * - Player data synchronization
 * - Event coordination
 * - Performance optimization
 */
@Getter
public class CombatAdventureManager implements Listener {

    private final SkyblockPlugin plugin;
    private final CompleteSlayerSystem slayerSystem;
    private final CompleteDungeonSystem dungeonSystem;
    private final CompleteCombatSystem combatSystem;
    private final CombatAdventureGUI guiSystem;

    public CombatAdventureManager(SkyblockPlugin plugin) {
        this.plugin = plugin;

        // Initialize systems
        de.noctivag.plugin.core.CorePlatform corePlatform = new de.noctivag.plugin.core.CorePlatform(plugin, plugin.getMultiServerDatabaseManager());
        this.slayerSystem = new CompleteSlayerSystem(plugin, corePlatform, plugin.getDatabaseManager());
        this.dungeonSystem = new CompleteDungeonSystem(plugin, plugin.getMultiServerDatabaseManager());
        this.combatSystem = new CompleteCombatSystem(plugin, plugin.getMultiServerDatabaseManager());
        this.guiSystem = new CombatAdventureGUI(plugin, slayerSystem, dungeonSystem, combatSystem);

        plugin.getLogger().info("Combat & Adventure Manager initialized successfully!");
    }

    public void initialize() {
        // Register events after full initialization
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Initialize player data for all systems
        slayerSystem.getPlayerSlayerData(player.getUniqueId());
        dungeonSystem.getPlayerDungeonData(player.getUniqueId());
        combatSystem.getPlayerCombatStats(player.getUniqueId());

        // Load player data from database
        loadPlayerData(player);

        // Send welcome message
        player.sendMessage("§a§lWelcome to Combat & Adventure!");
        player.sendMessage("§7Use §e/slayer §7for slayer quests");
        player.sendMessage("§7Use §e/dungeon §7for dungeon runs");
        player.sendMessage("§7Use §e/combat §7for combat abilities");
        player.sendMessage("§7Use §e/ca §7to open the main menu");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Save player data to database
        savePlayerData(player);
    }

    @SuppressWarnings("unused")
    private void loadPlayerData(Player player) {
        // Load slayer data
        // Load dungeon data
        // Load combat data
        // This would integrate with the database systems
    }

    @SuppressWarnings("unused")
    private void savePlayerData(Player player) {
        // Save slayer data
        // Save dungeon data
        // Save combat data
        // This would integrate with the database systems
    }

    // Cross-system integration methods
    public void giveCombatXP(Player player, int xp) {
        combatSystem.getPlayerCombatStats(player.getUniqueId()).addCombatXP(xp);
    }

    public void giveSlayerXP(Player player, de.noctivag.plugin.slayers.SlayerType slayerType, double xp) {
        slayerSystem.getPlayerSlayerData(player.getUniqueId()).addXP(slayerType, xp);
    }

    public void giveDungeonXP(Player player, int xp) {
        dungeonSystem.getPlayerDungeonData(player.getUniqueId()).addDungeonXP(xp);
    }

    // System status methods
    public boolean isPlayerInSlayerQuest(Player player) {
        return slayerSystem.hasActiveQuest(player.getUniqueId());
    }

    public boolean isPlayerInDungeon(Player player) {
        return dungeonSystem.hasActiveInstance(player.getUniqueId());
    }

    public boolean isPlayerInCombat(Player player) {
        // Check if player has active combat effects or is in combat session
        return combatSystem.getPlayerCombatStats(player.getUniqueId()).isCombatMode();
    }

    // Performance monitoring
    public void logSystemPerformance() {
        plugin.getLogger().info("=== Combat & Adventure System Performance ===");
        // plugin.getLogger().info("Active Slayer Quests: " + slayerSystem.getActiveQuests().size());
        // plugin.getLogger().info("Active Dungeon Instances: " + dungeonSystem.getActiveInstances().size());
        // plugin.getLogger().info("Active Combat Sessions: " + combatSystem.getActiveSessions().size());
        plugin.getLogger().info("=============================================");
    }

    // Cleanup methods
    public void cleanupExpiredData() {
        // Clean up expired slayer quests
        // Clean up expired dungeon instances
        // Clean up expired combat sessions
    }

    public void shutdown() {
        plugin.getLogger().info("Shutting down Combat & Adventure Manager...");

        // Save all player data
        for (Player player : Bukkit.getOnlinePlayers()) {
            savePlayerData(player);
        }

        // Cleanup systems
        cleanupExpiredData();

        plugin.getLogger().info("Combat & Adventure Manager shutdown complete!");
    }
}
