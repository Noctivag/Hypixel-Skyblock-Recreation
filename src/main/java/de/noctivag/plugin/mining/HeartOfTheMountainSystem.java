package de.noctivag.plugin.mining;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.data.DatabaseManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;

/**
 * Heart of the Mountain System - Hypixel Skyblock Style
 * 
 * Features:
 * - Heart of the Mountain upgrades
 * - Mining statistics
 * - Mining rewards
 * - Mining GUI
 * - Mining challenges
 */
public class HeartOfTheMountainSystem {
    private final Plugin plugin;
    private final DatabaseManager databaseManager;
    private final Map<UUID, HeartStats> playerStats = new HashMap<>();

    public HeartOfTheMountainSystem(Plugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public boolean upgradeHeart(Player player, HeartUpgradeType type) {
        HeartStats stats = playerStats.computeIfAbsent(player.getUniqueId(), k -> new HeartStats());
        
        if (stats.canUpgrade(type)) {
            stats.addUpgrade(type);
            player.sendMessage("§aHeart of the Mountain Upgrade '" + type.getDisplayName() + "' erfolgreich!");
            return true;
        }
        return false;
    }

    public HeartStats getPlayerStats(Player player) {
        return playerStats.computeIfAbsent(player.getUniqueId(), k -> new HeartStats());
    }

    public void openHeartGUI(Player player) {
        // Create heart GUI
        org.bukkit.inventory.Inventory gui = org.bukkit.Bukkit.createInventory(null, 54, Component.text("§6§lHeart of the Mountain"));
        
        HeartStats stats = getPlayerStats(player);
        
        // Add upgrade items
        for (HeartUpgradeType type : HeartUpgradeType.values()) {
            ItemStack upgradeItem = new ItemStack(type.getMaterial());
            ItemMeta meta = upgradeItem.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text(type.getDisplayName()));
                meta.lore(Arrays.asList(
                    Component.text("§7" + type.getDescription()),
                    Component.text("§eCost: §a" + type.getCost() + " coins"),
                    Component.text("§7Level: §a" + stats.getUpgradeLevel(type))
                ));
                upgradeItem.setItemMeta(meta);
            }
            gui.addItem(upgradeItem);
        }

        player.openInventory(gui);
    }

    public boolean startChallenge(Player player, HeartChallengeType type) {
        HeartStats stats = getPlayerStats(player);
        
        if (stats.canStartChallenge(type)) {
            stats.startChallenge(type);
            player.sendMessage("§aChallenge '" + type.getDisplayName() + "' gestartet!");
            return true;
        }
        return false;
    }

    public List<HeartReward> getAvailableRewards(Player player) {
        HeartStats stats = getPlayerStats(player);
        List<HeartReward> rewards = new ArrayList<>();
        
        // Add rewards based on stats
        if (stats.getLevel() >= 10) {
            rewards.add(new HeartReward("Mining Speed Boost", "Increases mining speed by 10%"));
        }
        if (stats.getLevel() >= 25) {
            rewards.add(new HeartReward("Fortune Boost", "Increases fortune by 15%"));
        }
        
        return rewards;
    }

    public boolean resetHeart(Player player) {
        playerStats.remove(player.getUniqueId());
        player.sendMessage("§aHeart of the Mountain zurückgesetzt!");
        return true;
    }

    public static class HeartStats {
        private int level = 1;
        private int xp = 0;
        private final Map<HeartUpgradeType, Integer> upgrades = new HashMap<>();
        private final Map<HeartChallengeType, Boolean> activeChallenges = new HashMap<>();

        public int getLevel() { return level; }
        public int getXp() { return xp; }
        public int getXpToNextLevel() { return level * 1000; }
        public Map<HeartUpgradeType, Integer> getUpgrades() { return upgrades; }
        
        public double getMiningSpeedBonus() {
            return upgrades.getOrDefault(HeartUpgradeType.MINING_SPEED, 0) * 5.0;
        }
        
        public double getFortuneBonus() {
            return upgrades.getOrDefault(HeartUpgradeType.FORTUNE, 0) * 3.0;
        }
        
        public double getTitaniumChance() {
            return upgrades.getOrDefault(HeartUpgradeType.TITANIUM_CHANCE, 0) * 2.0;
        }

        public boolean canUpgrade(HeartUpgradeType type) {
            return level >= type.getRequiredLevel() && xp >= type.getCost();
        }

        public void addUpgrade(HeartUpgradeType type) {
            upgrades.put(type, upgrades.getOrDefault(type, 0) + 1);
            xp -= type.getCost();
        }

        public int getUpgradeLevel(HeartUpgradeType type) {
            return upgrades.getOrDefault(type, 0);
        }

        public boolean canStartChallenge(HeartChallengeType type) {
            return !activeChallenges.getOrDefault(type, false);
        }

        public void startChallenge(HeartChallengeType type) {
            activeChallenges.put(type, true);
        }
    }

    public enum HeartUpgradeType {
        MINING_SPEED("§aMining Speed", "§7Increases mining speed", Material.DIAMOND_PICKAXE, 1000, 5),
        FORTUNE("§6Fortune", "§7Increases fortune chance", Material.GOLDEN_PICKAXE, 2000, 10),
        TITANIUM_CHANCE("§bTitanium Chance", "§7Increases titanium spawn chance", Material.NETHERITE_PICKAXE, 5000, 20);

        private final String displayName;
        private final String description;
        private final Material material;
        private final int cost;
        private final int requiredLevel;

        HeartUpgradeType(String displayName, String description, Material material, int cost, int requiredLevel) {
            this.displayName = displayName;
            this.description = description;
            this.material = material;
            this.cost = cost;
            this.requiredLevel = requiredLevel;
        }

        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getMaterial() { return material; }
        public int getCost() { return cost; }
        public int getRequiredLevel() { return requiredLevel; }
    }

    public enum HeartChallengeType {
        MINING_SPEED_CHALLENGE("§aMining Speed Challenge", "§7Mine 1000 blocks in 10 minutes"),
        FORTUNE_CHALLENGE("§6Fortune Challenge", "§7Get 50 fortune procs in 5 minutes"),
        TITANIUM_CHALLENGE("§bTitanium Challenge", "§7Find 10 titanium ores");

        private final String displayName;
        private final String description;

        HeartChallengeType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }

    public static class HeartReward {
        private final String name;
        private final String description;

        public HeartReward(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
    }
}
