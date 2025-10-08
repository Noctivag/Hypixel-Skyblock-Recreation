package de.noctivag.skyblock.magic;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import de.noctivag.skyblock.core.stats.PlayerStatData;

/**
 * Mana System - Hypixel Skyblock Style
 *
 * Features:
 * - Mana Pool (Base Mana)
 * - Mana Regeneration
 * - Mana Cost for Abilities
 * - Mana Display (Action Bar)
 * - Mana Bonuses from Items
 * - Mana Bonuses from Skills
 * - Mana Bonuses from Pets
 * - Mana Bonuses from Armor
 * - Mana Bonuses from Weapons
 * - Mana Bonuses from Accessories
 * - Mana Bonuses from Potions
 * - Mana Bonuses from Enchantments
 * - Mana Bonuses from Reforges
 * - Mana Bonuses from Gemstones
 * - Mana Bonuses from HotM
 * - Mana Bonuses from Fairy Souls
 */
public class ManaSystem implements Listener {
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerStatData> playerStats = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> manaTasks = new ConcurrentHashMap<>();

    public ManaSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        startManaUpdateTask();
    }

    public void initialize() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void startManaUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerStatData> entry : playerStats.entrySet()) {
                    PlayerStatData stats = entry.getValue();
                    // Regeneration
                    double maxMana = stats.getMaxMana();
                    double mana = stats.getMana();
                    double regen = stats.getManaRegen();
                    if (mana < maxMana) {
                        stats.setMana(Math.min(maxMana, mana + regen));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        playerStats.computeIfAbsent(playerId, PlayerStatData::new);
        startManaDisplayTask(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        BukkitTask task = manaTasks.remove(playerId);
        if (task != null) task.cancel();
        playerStats.remove(playerId);
    }

    private void startManaDisplayTask(Player player) {
        UUID playerId = player.getUniqueId();
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    this.cancel();
                    return;
                }
                PlayerStatData stats = playerStats.get(playerId);
                if (stats != null) displayMana(player, stats);
            }
        }.runTaskTimer(plugin, 0L, 10L);
        manaTasks.put(playerId, task);
    }

    private void displayMana(Player player, PlayerStatData stats) {
        String manaDisplay = createManaDisplay(stats);
        player.sendActionBar(Component.text(manaDisplay));
    }

    private String createManaDisplay(PlayerStatData stats) {
        StringBuilder display = new StringBuilder();
        double mana = stats.getMana();
        double maxMana = stats.getMaxMana();
        double manaPercentage = (maxMana > 0) ? (mana / maxMana) * 100 : 0;
        String manaBar = createManaBar(manaPercentage);
        display.append("§b⚡ ").append(manaBar).append(" ")
               .append("§f").append(String.format("%.0f", mana))
               .append("§7/")
               .append("§f").append(String.format("%.0f", maxMana))
               .append("§b Mana");
        double regen = stats.getManaRegen();
        if (regen > 0) {
            display.append("§a (+").append(String.format("%.1f", regen)).append("/s)");
        }
        return display.toString();
    }

    private String createManaBar(double manaPercentage) {
        StringBuilder manaBar = new StringBuilder();
        String color;
        if (manaPercentage >= 75) {
            color = "§b";
        } else if (manaPercentage >= 50) {
            color = "§9";
        } else if (manaPercentage >= 25) {
            color = "§e";
        } else {
            color = "§c";
        }
        int filledBars = (int) (manaPercentage / 5);
        int emptyBars = 20 - filledBars;
        manaBar.append(color);
        for (int i = 0; i < filledBars; i++) manaBar.append("█");
        manaBar.append("§7");
        for (int i = 0; i < emptyBars; i++) manaBar.append("█");
        return manaBar.toString();
    }

    public boolean useMana(Player player, double amount) {
        PlayerStatData stats = playerStats.get(player.getUniqueId());
        if (stats == null) return false;
        if (stats.getMana() < amount) {
            player.sendMessage("§cNicht genug Mana! Benötigt: " + String.format("%.0f", amount) + " Mana");
            return false;
        }
        stats.setMana(stats.getMana() - amount);
        return true;
    }

    public void restoreMana(Player player, double amount) {
        PlayerStatData stats = playerStats.get(player.getUniqueId());
        if (stats != null) {
            stats.setMana(Math.min(stats.getMaxMana(), stats.getMana() + amount));
        }
    }

    public void setMaxMana(Player player, double maxMana) {
        PlayerStatData stats = playerStats.get(player.getUniqueId());
        if (stats != null) {
            stats.setMaxMana(maxMana);
        }
    }

    public double getCurrentMana(Player player) {
        PlayerStatData stats = playerStats.get(player.getUniqueId());
        return stats != null ? stats.getMana() : 0.0;
    }

    public double getMaxMana(Player player) {
        PlayerStatData stats = playerStats.get(player.getUniqueId());
        return stats != null ? stats.getMaxMana() : 0.0;
    }

    public double getManaRegen(Player player) {
        PlayerStatData stats = playerStats.get(player.getUniqueId());
        return stats != null ? stats.getManaRegen() : 0.0;
    }
}
