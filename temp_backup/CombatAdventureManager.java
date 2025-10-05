package de.noctivag.skyblock;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;

import de.noctivag.skyblock.SkyblockPlugin;
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

    private final SkyblockPlugin SkyblockPlugin;
    private final CompleteSlayerSystem slayerSystem;
    private final CompleteDungeonSystem dungeonSystem;
    private final CompleteCombatSystem combatSystem;
    private final CombatAdventureGUI guiSystem;

    public CombatAdventureManager(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;

        // Initialize systems
        de.noctivag.skyblock.core.CorePlatform corePlatform = new de.noctivag.skyblock.core.CorePlatform(SkyblockPlugin, SkyblockPlugin.getMultiServerDatabaseManager());
        this.slayerSystem = new CompleteSlayerSystem(SkyblockPlugin, corePlatform, SkyblockPlugin.getDatabaseManager());
        this.dungeonSystem = new CompleteDungeonSystem(SkyblockPlugin, SkyblockPlugin.getMultiServerDatabaseManager());
        this.combatSystem = new CompleteCombatSystem(SkyblockPlugin, SkyblockPlugin.getMultiServerDatabaseManager());
        this.guiSystem = new CombatAdventureGUI(SkyblockPlugin, slayerSystem, dungeonSystem, combatSystem);

        SkyblockPlugin.getLogger().info("Combat & Adventure Manager initialized successfully!");
    }

    public void initialize() {
        // Register events after full initialization
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
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
        player.sendMessage(Component.text("§a§lWelcome to Combat & Adventure!"));
        player.sendMessage(Component.text("§7Use §e/slayer §7for slayer quests"));
        player.sendMessage(Component.text("§7Use §e/dungeon §7for dungeon runs"));
        player.sendMessage(Component.text("§7Use §e/combat §7for combat abilities"));
        player.sendMessage(Component.text("§7Use §e/ca §7to open the main menu"));
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

    public void giveSlayerXP(Player player, de.noctivag.skyblock.slayers.SlayerType slayerType, double xp) {
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
        SkyblockPlugin.getLogger().info("=== Combat & Adventure System Performance ===");
        // SkyblockPlugin.getLogger().info("Active Slayer Quests: " + slayerSystem.getActiveQuests().size());
        // SkyblockPlugin.getLogger().info("Active Dungeon Instances: " + dungeonSystem.getActiveInstances().size());
        // SkyblockPlugin.getLogger().info("Active Combat Sessions: " + combatSystem.getActiveSessions().size());
        SkyblockPlugin.getLogger().info("=============================================");
    }

    // Cleanup methods
    public void cleanupExpiredData() {
        // Clean up expired slayer quests
        // Clean up expired dungeon instances
        // Clean up expired combat sessions
    }

    public void shutdown() {
        SkyblockPlugin.getLogger().info("Shutting down Combat & Adventure Manager...");

        // Save all player data
        for (Player player : Bukkit.getOnlinePlayers()) {
            savePlayerData(player);
        }

        // Cleanup systems
        cleanupExpiredData();

        SkyblockPlugin.getLogger().info("Combat & Adventure Manager shutdown complete!");
    }
}
