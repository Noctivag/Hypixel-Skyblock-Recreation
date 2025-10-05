package de.noctivag.skyblock.rewards;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DailyRewardSystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<UUID, String> lastClaimDate = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> streakCount = new ConcurrentHashMap<>();
    private final Map<UUID, Set<String>> claimedRewards = new ConcurrentHashMap<>();
    
    // Daily rewards configuration
    private final List<DailyReward> dailyRewards = Arrays.asList(
        new DailyReward(1, "§a§lTag 1", "§7Willkommens-Belohnung", Material.WOODEN_SWORD, 100.0, 
            Arrays.asList(new ItemStack(Material.BREAD, 5), new ItemStack(Material.WOODEN_SWORD))),
        new DailyReward(2, "§e§lTag 2", "§7Erste Schritte", Material.STONE_SWORD, 150.0, 
            Arrays.asList(new ItemStack(Material.COOKED_BEEF, 3), new ItemStack(Material.STONE_SWORD))),
        new DailyReward(3, "§6§lTag 3", "§7Auf dem Weg", Material.IRON_SWORD, 200.0, 
            Arrays.asList(new ItemStack(Material.GOLDEN_APPLE), new ItemStack(Material.IRON_SWORD))),
        new DailyReward(4, "§b§lTag 4", "§7Fortschritt", Material.DIAMOND_SWORD, 300.0, 
            Arrays.asList(new ItemStack(Material.DIAMOND, 2), new ItemStack(Material.DIAMOND_SWORD))),
        new DailyReward(5, "§d§lTag 5", "§7Wöchentlicher Bonus", Material.NETHERITE_SWORD, 500.0, 
            Arrays.asList(new ItemStack(Material.NETHERITE_INGOT), new ItemStack(Material.NETHERITE_SWORD))),
        new DailyReward(6, "§c§lTag 6", "§7Premium Belohnung", Material.ENCHANTED_GOLDEN_APPLE, 400.0, 
            Arrays.asList(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2), new ItemStack(Material.EXPERIENCE_BOTTLE, 10))),
        new DailyReward(7, "§5§lTag 7", "§7Wochenende Special", Material.BEACON, 1000.0, 
            Arrays.asList(new ItemStack(Material.BEACON), new ItemStack(Material.EMERALD, 5), new ItemStack(Material.DIAMOND, 3)))
    );
    
    public DailyRewardSystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public boolean canClaimReward(Player player) {
        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String lastClaim = lastClaimDate.get(player.getUniqueId());
        
        if (lastClaim == null) {
            return true; // First time claiming
        }
        
        if (lastClaim.equals(today)) {
            return false; // Already claimed today
        }
        
        // Check if it's the next day (streak continues)
        LocalDate lastClaimDate = LocalDate.parse(lastClaim);
        LocalDate todayDate = LocalDate.now();
        
        return todayDate.isAfter(lastClaimDate);
    }
    
    public void claimReward(Player player) {
        if (!canClaimReward(player)) {
            player.sendMessage(Component.text("§cDu hast deine tägliche Belohnung bereits erhalten!"));
            return;
        }
        
        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String lastClaim = lastClaimDate.get(player.getUniqueId());
        
        // Check streak
        int currentStreak = getCurrentStreak(player);
        if (lastClaim != null) {
            LocalDate lastClaimDate = LocalDate.parse(lastClaim);
            LocalDate todayDate = LocalDate.now();
            
            if (todayDate.isAfter(lastClaimDate.plusDays(1))) {
                // Streak broken
                currentStreak = 1;
                player.sendMessage(Component.text("§cDeine Streak wurde zurückgesetzt!"));
            } else {
                // Streak continues
                currentStreak++;
            }
        } else {
            currentStreak = 1;
        }
        
        // Update streak
        streakCount.put(player.getUniqueId(), currentStreak);
        lastClaimDate.put(player.getUniqueId(), today);
        
        // Get reward for current streak
        DailyReward reward = getRewardForStreak(currentStreak);
        if (reward == null) {
            reward = dailyRewards.get(6); // Default to day 7 reward
        }
        
        // Give rewards
        giveReward(player, reward, currentStreak);
        
        // Show streak info
        showStreakInfo(player, currentStreak);
        
        // Check for streak achievements
        checkStreakAchievements(player, currentStreak);
    }
    
    private void giveReward(Player player, DailyReward reward, int streak) {
        // Give money
        double moneyReward = reward.getMoneyReward();
        
        // Streak bonus
        if (streak >= 7) {
            moneyReward *= 2.0; // Double reward for 7+ day streak
        } else if (streak >= 3) {
            moneyReward *= 1.5; // 50% bonus for 3+ day streak
        }
        
        SkyblockPlugin.getEconomyManager().giveMoney(player, moneyReward);
        
        // Give items
        for (ItemStack item : reward.getItems()) {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(item);
            } else {
                player.getWorld().dropItem(player.getLocation(), item);
            }
        }
        
        // Announce reward
        announceReward(player, reward, moneyReward, streak);
        
        // Play sound
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        
        // Particle effect
        player.getWorld().spawnParticle(org.bukkit.Particle.FIREWORK, 
            player.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0.1);
    }
    
    private void announceReward(Player player, DailyReward reward, double moneyReward, int streak) {
        player.sendMessage(Component.text("§6§l[Tägliche Belohnung erhalten!]"));
        player.sendMessage("§e" + reward.getName());
        player.sendMessage("§7" + reward.getDescription());
        player.sendMessage("§aGeld: §f" + SkyblockPlugin.getEconomyManager().formatMoney(moneyReward));
        
        if (streak >= 7) {
            player.sendMessage(Component.text("§6§lStreak-Bonus: §e2x Belohnung!"));
        } else if (streak >= 3) {
            player.sendMessage(Component.text("§6§lStreak-Bonus: §e1.5x Belohnung!"));
        }
        
        player.sendMessage("§7Aktuelle Streak: §e" + streak + " Tage");
    }
    
    private void showStreakInfo(Player player, int streak) {
        player.sendMessage(Component.text("§6§l[Streak Info]"));
        player.sendMessage("§7Aktuelle Streak: §e" + streak + " Tage");
        
        if (streak < 7) {
            int daysUntilBonus = 7 - streak;
            player.sendMessage("§7Noch §e" + daysUntilBonus + " Tage §7bis zum Streak-Bonus!");
        } else {
            player.sendMessage(Component.text("§6§lDu erhältst den maximalen Streak-Bonus!"));
        }
        
        // Show next reward preview
        DailyReward nextReward = getRewardForStreak(streak + 1);
        if (nextReward != null) {
            player.sendMessage("§7Nächste Belohnung: §e" + nextReward.getName());
        }
    }
    
    private void checkStreakAchievements(Player player, int streak) {
        // Check for streak achievements
        if (streak == 7) {
            // 7 day streak achievement
            // Placeholder - method not implemented
            // if (SkyblockPlugin.getAchievementSystem() != null) {
            //     SkyblockPlugin.getAchievementSystem().checkAchievement(player, "week_streak", 1);
            // }
        } else if (streak == 30) {
            // 30 day streak achievement
            // Placeholder - method not implemented
            // if (SkyblockPlugin.getAchievementSystem() != null) {
            //     SkyblockPlugin.getAchievementSystem().checkAchievement(player, "month_streak", 1);
            // }
        }
    }
    
    private DailyReward getRewardForStreak(int streak) {
        int dayIndex = (streak - 1) % 7; // Cycle through 7 days
        return dailyRewards.get(dayIndex);
    }
    
    public int getCurrentStreak(Player player) {
        return streakCount.getOrDefault(player.getUniqueId(), 0);
    }
    
    public String getLastClaimDate(Player player) {
        return lastClaimDate.get(player.getUniqueId());
    }
    
    public List<DailyReward> getDailyRewards() {
        return new ArrayList<>(dailyRewards);
    }
    
    public DailyReward getRewardForDay(int day) {
        if (day >= 1 && day <= dailyRewards.size()) {
            return dailyRewards.get(day - 1);
        }
        return null;
    }
    
    public boolean hasClaimedToday(Player player) {
        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        return today.equals(lastClaimDate.get(player.getUniqueId()));
    }
    
    public int getDaysUntilNextReward(Player player) {
        if (canClaimReward(player)) {
            return 0;
        }
        
        String lastClaim = lastClaimDate.get(player.getUniqueId());
        if (lastClaim == null) {
            return 0;
        }
        
        LocalDate lastClaimDate = LocalDate.parse(lastClaim);
        LocalDate today = LocalDate.now();
        
        return (int) java.time.temporal.ChronoUnit.DAYS.between(today, lastClaimDate.plusDays(1));
    }
    
    // Daily Reward Class
    public static class DailyReward {
        private final int day;
        private final String name;
        private final String description;
        private final Material icon;
        private final double moneyReward;
        private final List<ItemStack> items;
        
        public DailyReward(int day, String name, String description, Material icon, 
                          double moneyReward, List<ItemStack> items) {
            this.day = day;
            this.name = name;
            this.description = description;
            this.icon = icon;
            this.moneyReward = moneyReward;
            this.items = new ArrayList<>(items);
        }
        
        // Getters
        public int getDay() { return day; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public double getMoneyReward() { return moneyReward; }
        public List<ItemStack> getItems() { return new ArrayList<>(items); }
    }
}
