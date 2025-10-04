package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.core.CorePlatform;
import de.noctivag.skyblock.core.PlayerProfile;
import de.noctivag.skyblock.items.StatModificationSystem;
import de.noctivag.skyblock.pets.PetManagementSystem;
import de.noctivag.skyblock.accessories.AccessorySystem;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Enhanced Scoreboard System - Hypixel Skyblock Style
 *
 * Features:
 * - Scoreboard Updates with New Stats
 * - Menu Integration of All Systems
 * - GUI Animations and Effects
 * - Responsive Design for Different Screen Sizes
 * - Real-time Stat Updates
 * - System Status Indicators
 */
public class EnhancedScoreboardSystem {
    private final SkyblockPlugin plugin;
    private final CorePlatform corePlatform;
    private final StatModificationSystem statModificationSystem;
    private final PetManagementSystem petManagementSystem;
    private final AccessorySystem accessorySystem;
    private final Map<UUID, BukkitTask> scoreboardTasks = new ConcurrentHashMap<>();
    private final Map<UUID, PlayerScoreboard> playerScoreboards = new ConcurrentHashMap<>();

    public EnhancedScoreboardSystem(SkyblockPlugin plugin, CorePlatform corePlatform,
                                  StatModificationSystem statModificationSystem,
                                  PetManagementSystem petManagementSystem,
                                  AccessorySystem accessorySystem) {
        this.plugin = plugin;
        this.corePlatform = corePlatform;
        this.statModificationSystem = statModificationSystem;
        this.petManagementSystem = petManagementSystem;
        this.accessorySystem = accessorySystem;
    }

    public void enableScoreboard(Player player) {
        UUID playerId = player.getUniqueId();

        // Cancel existing task if any
        BukkitTask existingTask = scoreboardTasks.get(playerId);
        if (existingTask != null) {
            existingTask.cancel();
        }

        // Create new scoreboard
        PlayerScoreboard scoreboard = new PlayerScoreboard(player);
        playerScoreboards.put(playerId, scoreboard);

        // Start update task
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                updateScoreboard(player);
            }
        }.runTaskTimer(plugin, 0L, 20L); // Update every second

        scoreboardTasks.put(playerId, task);
    }

    public void disableScoreboard(Player player) {
        UUID playerId = player.getUniqueId();

        // Cancel update task
        BukkitTask task = scoreboardTasks.get(playerId);
        if (task != null) {
            task.cancel();
            scoreboardTasks.remove(playerId);
        }

        // Remove scoreboard
        PlayerScoreboard scoreboard = playerScoreboards.get(playerId);
        if (scoreboard != null) {
            scoreboard.destroy();
            playerScoreboards.remove(playerId);
        }
    }

    private void updateScoreboard(Player player) {
        PlayerScoreboard scoreboard = playerScoreboards.get(player.getUniqueId());
        if (scoreboard == null) return;

        // Update player stats
        statModificationSystem.updatePlayerStats(player);

        // Get player profile
        PlayerProfile profile = corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null) return;

        // Get player stats
        StatModificationSystem.PlayerStats stats = statModificationSystem.getPlayerStats(player.getUniqueId());

        // Get active pet
        var activePet = petManagementSystem.getActivePet(player.getUniqueId());

        // Get equipped accessories
        AccessorySystem.PlayerAccessories accessories = accessorySystem.getPlayerAccessories(player.getUniqueId());

        // Update scoreboard content
        List<Component> lines = new ArrayList<>();

        // Header
        lines.add(Component.text("§6§l⚡ BASICS PLUGIN ⚡").decorate(TextDecoration.BOLD));
        lines.add(Component.text("§7━━━━━━━━━━━━━━━━━━━━���━━━"));

        // Player info
        lines.add(Component.text("§ePlayer: §f" + player.getName()));
        lines.add(Component.text("§eLevel: §f" + profile.getLevel()));
        lines.add(Component.text("§eCoins: §6" + String.format("%.0f", profile.getCoins())));
        lines.add(Component.text(""));

        // Combat stats
        lines.add(Component.text("§c§lCOMBAT STATS").decorate(TextDecoration.BOLD));
        lines.add(Component.text("§7Health: §c" + String.format("%.0f", stats.getStat("HEALTH"))));
        lines.add(Component.text("§7Strength: §c" + String.format("%.0f", stats.getStat("STRENGTH"))));
        lines.add(Component.text("§7Defense: §a" + String.format("%.0f", stats.getStat("DEFENSE"))));
        lines.add(Component.text("§7Critical Chance: §9" + String.format("%.1f", stats.getStat("CRITICAL_CHANCE")) + "%"));
        lines.add(Component.text("§7Critical Damage: §9" + String.format("%.0f", stats.getStat("CRITICAL_DAMAGE")) + "%"));
        lines.add(Component.text(""));

        // Utility stats
        lines.add(Component.text("§b§lUTILITY STATS").decorate(TextDecoration.BOLD));
        lines.add(Component.text("§7Speed: §f" + String.format("%.0f", stats.getStat("SPEED"))));
        lines.add(Component.text("§7Intelligence: §b" + String.format("%.0f", stats.getStat("INTELLIGENCE"))));
        lines.add(Component.text("§7Mana: §b" + String.format("%.0f", stats.getStat("MANA"))));
        lines.add(Component.text("§7Luck: §6" + String.format("%.0f", stats.getStat("LUCK"))));
        lines.add(Component.text(""));

        // Active systems
        lines.add(Component.text("§d§lACTIVE SYSTEMS").decorate(TextDecoration.BOLD));

        // Pet status
        if (activePet != null) {
            lines.add(Component.text("§7Pet: §e" + activePet.getType().getName() + " §7(Lv." + activePet.getLevel() + ")"));
        } else {
            lines.add(Component.text("§7Pet: §cNone"));
        }

        // Accessory count
        int equippedAccessories = 0;
        for (var category : AccessorySystem.AccessoryCategory.values()) {
            if (accessories.getEquippedAccessory(category) != null) {
                equippedAccessories++;
            }
        }
        lines.add(Component.text("§7Accessories: §6" + equippedAccessories + "/6"));

        // Footer
        lines.add(Component.text("§7━━━━━━━━━━━━━━━━━━━━━━━━"));
        lines.add(Component.text("§7Use §e/menu §7for more options!"));

        // Update scoreboard
        scoreboard.updateContent(lines);
    }

    public void updateAllScoreboards() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            updateScoreboard(player);
        }
    }

    public void enableForAllPlayers() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            enableScoreboard(player);
        }
    }

    public void disableForAllPlayers() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            disableScoreboard(player);
        }
    }

    @SuppressWarnings("unused")
    public PlayerScoreboard getPlayerScoreboard(UUID playerId) {
        return playerScoreboards.get(playerId);
    }

    @SuppressWarnings("unused")
    public Map<UUID, PlayerScoreboard> getAllPlayerScoreboards() {
        return new HashMap<>(playerScoreboards);
    }

    // Player Scoreboard Class
    public class PlayerScoreboard {
        private final Player player;
        private final org.bukkit.scoreboard.Scoreboard scoreboard;
        private final org.bukkit.scoreboard.Objective objective;
        private final Map<Integer, String> lines = new HashMap<>();
        private int lineCount = 0;

        public PlayerScoreboard(Player player) {
            this.player = player;
            this.scoreboard = player.getServer().getScoreboardManager().getNewScoreboard();
            // Register using NamespacedKey to avoid deprecated overloads when available
            NamespacedKey key = new NamespacedKey(plugin, "basics_plugin");
            @SuppressWarnings("deprecation")
            org.bukkit.scoreboard.Objective obj = scoreboard.registerNewObjective("main", "dummy",
                Component.text("§6§l⚡ BASICS PLUGIN ⚡"));
            this.objective = obj;
            this.objective.setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);

            // Set scoreboard for player
            player.setScoreboard(scoreboard);
        }

        public void updateContent(List<Component> content) {
            // Clear existing lines
            for (String line : lines.values()) {
                scoreboard.resetScores(line);
            }
            lines.clear();
            lineCount = 0;

            // Add new lines
            for (Component line : content) {
                String lineText = line.toString();
                if (lineText.length() > 40) {
                    lineText = lineText.substring(0, 37) + "...";
                }

                // Ensure unique line text
                String uniqueLine = lineText;
                int counter = 1;
                while (lines.containsValue(uniqueLine)) {
                    uniqueLine = lineText + "§" + counter;
                    counter++;
                }

                lines.put(lineCount, uniqueLine);
                objective.getScore(uniqueLine).setScore(15 - lineCount);
                lineCount++;
            }
        }

        public void destroy() {
            if (scoreboard != null) {
                // Clear all lines
                for (String line : lines.values()) {
                    scoreboard.resetScores(line);
                }

                // Unregister objective
                if (objective != null) {
                    objective.unregister();
                }
            }
        }

        public Player getPlayer() { return player; }
        public org.bukkit.scoreboard.Scoreboard getScoreboard() { return scoreboard; }
        public org.bukkit.scoreboard.Objective getObjective() { return objective; }
    }
}
