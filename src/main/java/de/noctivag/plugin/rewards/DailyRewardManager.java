package de.noctivag.plugin.rewards;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.api.RewardAPI;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@SuppressWarnings("deprecation")
public class DailyRewardManager implements RewardAPI {
    private final Plugin plugin;
    private final Map<Integer, DailyReward> rewards;
    private final Map<UUID, LocalDateTime> lastClaimTimes;
    private final Map<UUID, Integer> streaks;
    private final File rewardsFile;
    private FileConfiguration rewardsConfig;

    public DailyRewardManager(Plugin plugin) {
        this.plugin = plugin;
        this.rewards = new HashMap<>();
        this.lastClaimTimes = new HashMap<>();
        this.streaks = new HashMap<>();
        this.rewardsFile = new File(plugin.getDataFolder(), "daily_rewards.yml");
        // Defer heavy initialization to avoid 'this' escaping during construction
    }

    /**
     * Initialize data and defaults. Call once after construction when plugin is ready.
     */
    public void init() {
        loadData();
        initializeDefaultRewards();
    }

    private void loadData() {
        if (!rewardsFile.exists()) {
            plugin.saveResource("daily_rewards.yml", false);
        }
        rewardsConfig = YamlConfiguration.loadConfiguration(rewardsFile);

        ConfigurationSection playerData = rewardsConfig.getConfigurationSection("players");
        if (playerData != null) {
            for (String uuidStr : playerData.getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    String lastClaimStr = playerData.getString(uuidStr + ".lastClaim");
                    int streak = playerData.getInt(uuidStr + ".streak", 0);

                    if (lastClaimStr != null) {
                        lastClaimTimes.put(uuid, LocalDateTime.parse(lastClaimStr));
                        streaks.put(uuid, streak);
                    }
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in rewards data: " + uuidStr);
                }
            }
        }

        ConfigurationSection rewardsData = rewardsConfig.getConfigurationSection("rewards");
        if (rewardsData == null) {
            plugin.getLogger().warning("No rewards data found, initializing defaults");
            initializeDefaultRewards();
            return;
        }

        for (String dayStr : rewardsData.getKeys(false)) {
            try {
                loadReward(rewardsData, dayStr);
            } catch (Exception e) {
                plugin.getLogger().warning("Error loading reward for day " + dayStr + ": " + e.getMessage());
            }
        }
    }

    private void loadReward(ConfigurationSection rewardsData, String dayStr) {
        int day = Integer.parseInt(dayStr);
        ConfigurationSection daySection = rewardsData.getConfigurationSection(dayStr);
        if (daySection == null) {
            throw new IllegalStateException("Invalid day section for day " + dayStr);
        }

        List<DailyReward.RewardItem> items = new ArrayList<>();
        ConfigurationSection itemsSection = daySection.getConfigurationSection("items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection == null) continue;

                String typeStr = itemSection.getString("type");
                if (typeStr == null) {
                    plugin.getLogger().warning("Missing type for reward item in day " + dayStr);
                    continue;
                }

                try {
                    DailyReward.RewardType type = DailyReward.RewardType.valueOf(typeStr.toUpperCase());
                    Object value = itemSection.get("value");
                    if (value == null) {
                        plugin.getLogger().warning("Missing value for reward item in day " + dayStr);
                        continue;
                    }
                    int amount = itemSection.getInt("amount", 1);
                    items.add(new DailyReward.RewardItem(type, value, amount));
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid reward type: " + typeStr + " in day " + dayStr);
                }
            }
        }

        boolean isSpecial = daySection.getBoolean("special", false);
        String iconStr = daySection.getString("icon", "CHEST");
        Material icon = Material.valueOf(iconStr != null ? iconStr : "CHEST");
        rewards.put(day, new DailyReward(day, items, isSpecial, icon));
    }

    public void saveData() {
        rewardsConfig = new YamlConfiguration();

        // Speichere Spielerdaten
        for (Map.Entry<UUID, LocalDateTime> entry : lastClaimTimes.entrySet()) {
            String path = "players." + entry.getKey().toString();
            rewardsConfig.set(path + ".lastClaim", entry.getValue().toString());
            rewardsConfig.set(path + ".streak", streaks.getOrDefault(entry.getKey(), 0));
        }

        // Speichere Belohnungen
        for (Map.Entry<Integer, DailyReward> entry : rewards.entrySet()) {
            String path = "rewards." + entry.getKey();
            DailyReward reward = entry.getValue();

            rewardsConfig.set(path + ".special", reward.isSpecialReward());
            rewardsConfig.set(path + ".icon", reward.getDisplayIcon().name());

            int i = 0;
            for (DailyReward.RewardItem item : reward.getRewards()) {
                String itemPath = path + ".items." + i;
                rewardsConfig.set(itemPath + ".type", item.getType().name());
                rewardsConfig.set(itemPath + ".value", item.getValue());
                rewardsConfig.set(itemPath + ".amount", item.getAmount());
                i++;
            }
        }

        try {
            rewardsConfig.save(rewardsFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Fehler beim Speichern der Daily Rewards: " + e.getMessage());
        }
    }

    private void initializeDefaultRewards() {
        if (rewards.isEmpty()) {
            // Tag 1 - Starter Rewards
            List<DailyReward.RewardItem> day1Rewards = Arrays.asList(
                new DailyReward.RewardItem(DailyReward.RewardType.EXP, 100, 1),
                new DailyReward.RewardItem(DailyReward.RewardType.ITEM, new ItemStack(Material.BREAD, 16), 1),
                new DailyReward.RewardItem(DailyReward.RewardType.ITEM, new ItemStack(Material.TORCH, 32), 1)
            );
            rewards.put(1, new DailyReward(1, day1Rewards, false, Material.BREAD));

            // Tag 2 - Building Materials
            List<DailyReward.RewardItem> day2Rewards = Arrays.asList(
                new DailyReward.RewardItem(DailyReward.RewardType.EXP, 150, 1),
                new DailyReward.RewardItem(DailyReward.RewardType.ITEM, new ItemStack(Material.COBBLESTONE, 64), 1),
                new DailyReward.RewardItem(DailyReward.RewardType.ITEM, new ItemStack(Material.OAK_PLANKS, 32), 1)
            );
            rewards.put(2, new DailyReward(2, day2Rewards, false, Material.COBBLESTONE));

            // Tag 3 - Tools
            List<DailyReward.RewardItem> day3Rewards = Arrays.asList(
                new DailyReward.RewardItem(DailyReward.RewardType.EXP, 200, 1),
                new DailyReward.RewardItem(DailyReward.RewardType.ITEM, createEnchantedItem(Material.IRON_PICKAXE, "efficiency", 2), 1),
                new DailyReward.RewardItem(DailyReward.RewardType.ITEM, new ItemStack(Material.COOKED_BEEF, 16), 1)
            );
            rewards.put(3, new DailyReward(3, day3Rewards, false, Material.IRON_PICKAXE));

            // Tag 4 - Combat Gear
            List<DailyReward.RewardItem> day4Rewards = Arrays.asList(
                new DailyReward.RewardItem(DailyReward.RewardType.EXP, 250, 1),
                new DailyReward.RewardItem(DailyReward.RewardType.ITEM, createEnchantedItem(Material.IRON_SWORD, "sharpness", 1), 1),
                new DailyReward.RewardItem(DailyReward.RewardType.ITEM, new ItemStack(Material.GOLDEN_APPLE, 2), 1)
            );
            rewards.put(4, new DailyReward(4, day4Rewards, false, Material.IRON_SWORD));

            // Tag 5 - Exploration
            List<DailyReward.RewardItem> day5Rewards = Arrays.asList(
                new DailyReward.RewardItem(DailyReward.RewardType.EXP, 300, 1),
                new DailyReward.RewardItem(DailyReward.RewardType.ITEM, new ItemStack(Material.ENDER_PEARL, 4), 1),
                new DailyReward.RewardItem(DailyReward.RewardType.ITEM, new ItemStack(Material.MAP, 3), 1)
            );
            rewards.put(5, new DailyReward(5, day5Rewards, false, Material.ENDER_PEARL));

            // Tag 6 - Rare Materials
            List<DailyReward.RewardItem> day6Rewards = Arrays.asList(
                new DailyReward.RewardItem(DailyReward.RewardType.EXP, 400, 1),
                new DailyReward.RewardItem(DailyReward.RewardType.ITEM, new ItemStack(Material.DIAMOND, 3), 1),
                new DailyReward.RewardItem(DailyReward.RewardType.ITEM, new ItemStack(Material.EMERALD, 5), 1)
            );
            rewards.put(6, new DailyReward(6, day6Rewards, false, Material.DIAMOND));

            // Tag 7 - Weekly Special Reward
            List<DailyReward.RewardItem> day7Rewards = Arrays.asList(
                new DailyReward.RewardItem(DailyReward.RewardType.EXP, 1000, 1),
                new DailyReward.RewardItem(DailyReward.RewardType.ITEM, createEnchantedItem(Material.DIAMOND_SWORD, "sharpness", 3), 1),
                new DailyReward.RewardItem(DailyReward.RewardType.ITEM, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2), 1),
                new DailyReward.RewardItem(DailyReward.RewardType.ITEM, new ItemStack(Material.ELYTRA, 1), 1)
            );
            rewards.put(7, new DailyReward(7, day7Rewards, true, Material.NETHER_STAR));

            saveData();
        }
    }

    public boolean canClaimReward(Player player) {
        UUID uuid = player.getUniqueId();
        if (!lastClaimTimes.containsKey(uuid)) {
            return true;
        }

        LocalDateTime lastClaim = lastClaimTimes.get(uuid);
        LocalDateTime now = LocalDateTime.now();
        return lastClaim.toLocalDate().isBefore(now.toLocalDate());
    }

    @SuppressWarnings("unused")
    public void claimReward(Player player) {
        if (!canClaimReward(player)) {
            player.sendMessage("§cDu hast deine tägliche Belohnung bereits abgeholt!");
            return;
        }

        UUID uuid = player.getUniqueId();
        LocalDateTime now = LocalDateTime.now();
        lastClaimTimes.put(uuid, now);

        int currentStreak = streaks.getOrDefault(uuid, 0) + 1;
        streaks.put(uuid, currentStreak);

        // Gebe Belohnungen
        DailyReward reward = rewards.get(currentStreak % 7 == 0 ? 7 : currentStreak % 7);
        if (reward != null) {
            giveRewards(player, reward);
            player.sendMessage("§aDu hast deine tägliche Belohnung abgeholt!");
            player.sendMessage("§7Aktuelle Streak: §e" + currentStreak + " Tage");
        } else {
            plugin.getLogger().warning("No reward found for streak day: " + currentStreak % 7);
            player.sendMessage("§cEs ist ein Fehler aufgetreten. Bitte kontaktiere einen Administrator.");
        }

        saveData();
    }

    private void giveRewards(Player player, DailyReward reward) {
        for (DailyReward.RewardItem item : reward.getRewards()) {
            switch (item.getType()) {
                case EXP:
                    int expAmount = ((Number) item.getValue()).intValue() * item.getAmount();
                    player.giveExp(expAmount);
                    player.sendMessage("§7+ §a" + expAmount + " EXP §7Punkte");
                    break;

                case ITEM:
                    if (item.getValue() instanceof ItemStack) {
                        ItemStack itemStack = ((ItemStack) item.getValue()).clone();
                        itemStack.setAmount(item.getAmount());
                        player.getInventory().addItem(itemStack);
                        String itemName = itemStack.getType().toString().toLowerCase().replace("_", " ");
                        player.sendMessage("§7+ §e" + itemStack.getAmount() + "x " + itemName);
                    }
                    break;

                case KIT:
                    // Give access to a specific kit
                    String kitName = item.getValue().toString();
                    // TODO: Implement proper KitManager interface
                    // ((KitManager) plugin.getKitManager()).giveKit(player, kitName);
                    player.sendMessage("§7+ §6Kit: §e" + kitName);
                    break;

                case PERMISSION:
                    // Temporary permissions could be implemented here
                    String permission = item.getValue().toString();
                    player.sendMessage("§7+ §bBerechtigung: §e" + permission + " §7(temporär)");
                    break;

                case COINS:
                    // Coins are no longer used - convert to EXP instead
                    int bonusExp = ((Number) item.getValue()).intValue() * item.getAmount();
                    player.giveExp(bonusExp);
                    player.sendMessage("§7+ §a" + bonusExp + " Bonus EXP §7(anstatt Coins)");
                    break;
            }
        }
    }

    private ItemStack createEnchantedItem(Material material, String enchantmentName, int level) {
        ItemStack item = new ItemStack(material);
        try {
            org.bukkit.enchantments.Enchantment enchantment = org.bukkit.enchantments.Enchantment.getByKey(
                org.bukkit.NamespacedKey.minecraft(enchantmentName.toLowerCase())
            );
            if (enchantment != null) {
                item.addUnsafeEnchantment(enchantment, level);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Could not add enchantment " + enchantmentName + " to " + material);
        }
        return item;
    }

    @Override
    public DailyReward getReward(int day) {
        return rewards.get(day);
    }

    @Override
    public int getCurrentStreak(Player player) {
        return streaks.getOrDefault(player.getUniqueId(), 0);
    }

    @Override
    public LocalDateTime getLastClaimTime(Player player) {
        return lastClaimTimes.get(player.getUniqueId());
    }

    @Override
    public void setReward(int day, DailyReward reward) {
        rewards.put(day, reward);
        saveData();
    }

    @Override
    public void removeReward(int day) {
        rewards.remove(day);
        saveData();
    }

    private void loadRewards() {
        // TODO: Implement proper ConfigManager interface
        // FileConfiguration config = ((ConfigManager) plugin.getConfigManager()).getConfig();
        FileConfiguration config = null; // Placeholder
        rewards.clear();

        ConfigurationSection rewardsSection = config.getConfigurationSection("daily-rewards");
        if (rewardsSection == null) {
            plugin.getLogger().warning("No daily-rewards section found in config");
            return;
        }

        for (String dayKey : rewardsSection.getKeys(false)) {
            try {
                int day = Integer.parseInt(dayKey);
                ConfigurationSection daySection = rewardsSection.getConfigurationSection(dayKey);
                if (daySection == null) {
                    plugin.getLogger().warning("Invalid day section for day " + dayKey);
                    continue;
                }

                List<DailyReward.RewardItem> items = new ArrayList<>();
                ConfigurationSection itemsSection = daySection.getConfigurationSection("items");
                if (itemsSection != null) {
                    for (String itemKey : itemsSection.getKeys(false)) {
                        ConfigurationSection itemSection = itemsSection.getConfigurationSection(itemKey);
                        if (itemSection == null) continue;

                        String typeStr = itemSection.getString("type");
                        if (typeStr == null) {
                            plugin.getLogger().warning("Missing type for reward item in day " + dayKey);
                            continue;
                        }

                        try {
                            DailyReward.RewardType type = DailyReward.RewardType.valueOf(typeStr.toUpperCase());
                            Object value = itemSection.get("value");
                            if (value == null) {
                                plugin.getLogger().warning("Missing value for reward item in day " + dayKey);
                                continue;
                            }
                            int amount = itemSection.getInt("amount", 1);
                            items.add(new DailyReward.RewardItem(type, value, amount));
                        } catch (IllegalArgumentException e) {
                            plugin.getLogger().warning("Invalid reward type: " + typeStr + " in day " + dayKey);
                        }
                    }
                }

                boolean isSpecial = daySection.getBoolean("special", false);
                String iconStr = daySection.getString("icon", "CHEST");
                Material icon = Material.valueOf(iconStr != null ? iconStr : "CHEST");
                rewards.put(day, new DailyReward(day, items, isSpecial, icon));
            } catch (NumberFormatException e) {
                plugin.getLogger().warning("Invalid day number: " + dayKey);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid material for icon in day " + dayKey + ": " + e.getMessage());
            }
        }
    }
}
