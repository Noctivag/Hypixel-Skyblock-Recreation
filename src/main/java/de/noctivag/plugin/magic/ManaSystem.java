package de.noctivag.plugin.magic;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerMana> playerMana = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> manaTasks = new ConcurrentHashMap<>();

    public ManaSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        startManaUpdateTask();
    }
    
    public void initialize() {
        // Register events after constructor is complete
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void startManaUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerMana> entry : playerMana.entrySet()) {
                    PlayerMana mana = entry.getValue();
                    mana.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // PlayerMana mana = getPlayerMana(player.getUniqueId());

        // Start mana display task
        startManaDisplayTask(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Stop mana display task
        BukkitTask task = manaTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }

        // Remove player mana
        playerMana.remove(playerId);
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

                PlayerMana mana = getPlayerMana(playerId);
                displayMana(player, mana);
            }
        }.runTaskTimer(plugin, 0L, 10L); // Every 0.5 seconds

        manaTasks.put(playerId, task);
    }

    private void displayMana(Player player, PlayerMana mana) {
        // Create mana display
        String manaDisplay = createManaDisplay(mana);

        // Send action bar message using modern API
        player.sendActionBar(net.kyori.adventure.text.Component.text(manaDisplay));
    }

    private String createManaDisplay(PlayerMana mana) {
        StringBuilder display = new StringBuilder();

        // Mana bar
        double manaPercentage = (mana.getCurrentMana() / mana.getMaxMana()) * 100;
        String manaBar = createManaBar(manaPercentage);

        // Mana text
        display.append("§b⚡ ").append(manaBar).append(" ")
               .append("§f").append(String.format("%.0f", mana.getCurrentMana()))
               .append("§7/")
               .append("§f").append(String.format("%.0f", mana.getMaxMana()))
               .append("§b Mana");

        // Regeneration indicator
        if (mana.getManaRegeneration() > 0) {
            display.append("§a (+").append(String.format("%.1f", mana.getManaRegeneration())).append("/s)");
        }

        return display.toString();
    }

    private String createManaBar(double manaPercentage) {
        StringBuilder manaBar = new StringBuilder();

        // Determine color based on mana percentage
        String color;
        if (manaPercentage >= 75) {
            color = "§b"; // AQUA
        } else if (manaPercentage >= 50) {
            color = "§9"; // BLUE
        } else if (manaPercentage >= 25) {
            color = "§e"; // YELLOW
        } else {
            color = "§c"; // RED
        }

        // Create mana bar (20 characters)
        int filledBars = (int) (manaPercentage / 5); // 5% per bar
        int emptyBars = 20 - filledBars;

        manaBar.append(color);
        for (int i = 0; i < filledBars; i++) {
            manaBar.append("█");
        }

        manaBar.append("§7"); // GRAY
        for (int i = 0; i < emptyBars; i++) {
            manaBar.append("█");
        }

        return manaBar.toString();
    }

    public boolean useMana(Player player, double amount) {
        PlayerMana mana = getPlayerMana(player.getUniqueId());

        if (mana.getCurrentMana() < amount) {
            player.sendMessage("§cNicht genug Mana! Benötigt: " + String.format("%.0f", amount) + " Mana");
            return false;
        }

        mana.useMana(amount);
        return true;
    }

    public void restoreMana(Player player, double amount) {
        PlayerMana mana = getPlayerMana(player.getUniqueId());
        mana.restoreMana(amount);
    }

    public void setMaxMana(Player player, double maxMana) {
        PlayerMana mana = getPlayerMana(player.getUniqueId());
        mana.setMaxMana(maxMana);
    }

    public void addManaBonus(Player player, ManaBonusType type, double bonus) {
        PlayerMana mana = getPlayerMana(player.getUniqueId());
        mana.addManaBonus(type, bonus);
    }

    public void removeManaBonus(Player player, ManaBonusType type, double bonus) {
        PlayerMana mana = getPlayerMana(player.getUniqueId());
        mana.removeManaBonus(type, bonus);
    }

    public PlayerMana getPlayerMana(UUID playerId) {
        return playerMana.computeIfAbsent(playerId, k -> new PlayerMana(playerId));
    }

    public double getCurrentMana(Player player) {
        PlayerMana mana = getPlayerMana(player.getUniqueId());
        return mana.getCurrentMana();
    }

    public double getMaxMana(Player player) {
        PlayerMana mana = getPlayerMana(player.getUniqueId());
        return mana.getMaxMana();
    }

    public double getManaRegeneration(Player player) {
        PlayerMana mana = getPlayerMana(player.getUniqueId());
        return mana.getManaRegeneration();
    }

    // Mana Bonus Type Enum
    public enum ManaBonusType {
        BASE("Base Mana", 100.0),
        SKILL("Skill Bonus", 0.0),
        ITEM("Item Bonus", 0.0),
        PET("Pet Bonus", 0.0),
        ARMOR("Armor Bonus", 0.0),
        WEAPON("Weapon Bonus", 0.0),
        ACCESSORY("Accessory Bonus", 0.0),
        POTION("Potion Bonus", 0.0),
        ENCHANTMENT("Enchantment Bonus", 0.0),
        REFORGE("Reforge Bonus", 0.0),
        GEMSTONE("Gemstone Bonus", 0.0),
        HOTM("HotM Bonus", 0.0),
        FAIRY_SOUL("Fairy Soul Bonus", 0.0);

        private final String displayName;
        private final double baseValue;

        ManaBonusType(String displayName, double baseValue) {
            this.displayName = displayName;
            this.baseValue = baseValue;
        }

        public String getDisplayName() { return displayName; }
        public double getBaseValue() { return baseValue; }
    }

    // Player Mana Class
    public static class PlayerMana {
        private final UUID playerId;
        private double currentMana;
        private double maxMana;
        private double manaRegeneration;
        private final Map<ManaBonusType, Double> manaBonuses = new ConcurrentHashMap<>();
        private long lastRegeneration;

        public PlayerMana(UUID playerId) {
            this.playerId = playerId;
            this.currentMana = 100.0; // Base mana
            this.maxMana = 100.0; // Base max mana
            this.manaRegeneration = 1.0; // Base regeneration
            this.lastRegeneration = System.currentTimeMillis();

            // Initialize base bonuses
            manaBonuses.put(ManaBonusType.BASE, 100.0);
        }

        public void update() {
            // Regenerate mana
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - lastRegeneration;

            if (timeDiff >= 1000) { // Every second
                double regenerationAmount = manaRegeneration * (timeDiff / 1000.0);
                restoreMana(regenerationAmount);
                lastRegeneration = currentTime;
            }

            // Update max mana based on bonuses
            updateMaxMana();
        }

        private void updateMaxMana() {
            double totalBonus = 0.0;
            for (Map.Entry<ManaBonusType, Double> entry : manaBonuses.entrySet()) {
                totalBonus += entry.getValue();
            }

            double newMaxMana = totalBonus;

            // Ensure current mana doesn't exceed new max
            if (currentMana > newMaxMana) {
                currentMana = newMaxMana;
            }

            maxMana = newMaxMana;
        }

        public void useMana(double amount) {
            currentMana = Math.max(0, currentMana - amount);
        }

        public void restoreMana(double amount) {
            currentMana = Math.min(maxMana, currentMana + amount);
        }

        public void addManaBonus(ManaBonusType type, double bonus) {
            manaBonuses.put(type, manaBonuses.getOrDefault(type, 0.0) + bonus);
        }

        public void removeManaBonus(ManaBonusType type, double bonus) {
            double currentBonus = manaBonuses.getOrDefault(type, 0.0);
            manaBonuses.put(type, Math.max(0, currentBonus - bonus));
        }

        public void setManaBonus(ManaBonusType type, double bonus) {
            manaBonuses.put(type, bonus);
        }

        public double getManaBonus(ManaBonusType type) {
            return manaBonuses.getOrDefault(type, 0.0);
        }

        public double getTotalManaBonus() {
            return manaBonuses.values().stream().mapToDouble(Double::doubleValue).sum();
        }

        // Getters and Setters
        public UUID getPlayerId() { return playerId; }
        public double getCurrentMana() { return currentMana; }
        public double getMaxMana() { return maxMana; }
        public double getManaRegeneration() { return manaRegeneration; }
        public Map<ManaBonusType, Double> getManaBonuses() { return manaBonuses; }

        public void setCurrentMana(double currentMana) { this.currentMana = currentMana; }
        public void setMaxMana(double maxMana) { this.maxMana = maxMana; }
        public void setManaRegeneration(double manaRegeneration) { this.manaRegeneration = manaRegeneration; }
    }
}
