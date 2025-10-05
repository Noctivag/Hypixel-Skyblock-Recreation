package de.noctivag.skyblock.skyblock;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SlayerSystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<UUID, Map<SlayerType, Integer>> playerSlayerLevels = new ConcurrentHashMap<>();
    private final Map<UUID, Map<SlayerType, Integer>> playerSlayerXP = new ConcurrentHashMap<>();
    private final Map<UUID, SlayerQuest> activeQuests = new ConcurrentHashMap<>();
    
    public SlayerSystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        initializeSlayerData();
    }
    
    private void initializeSlayerData() {
        // Initialize slayer data for all players
        for (Player player : Bukkit.getOnlinePlayers()) {
            initializePlayerSlayerData(player.getUniqueId());
        }
    }
    
    private void initializePlayerSlayerData(UUID playerId) {
        playerSlayerLevels.computeIfAbsent(playerId, k -> new HashMap<>());
        playerSlayerXP.computeIfAbsent(playerId, k -> new HashMap<>());
        
        // Initialize all slayer types at level 0
        for (SlayerType type : SlayerType.values()) {
            playerSlayerLevels.get(playerId).put(type, 0);
            playerSlayerXP.get(playerId).put(type, 0);
        }
    }
    
    public void startSlayerQuest(Player player, SlayerType type, SlayerTier tier) {
        if (activeQuests.containsKey(player.getUniqueId())) {
            player.sendMessage(Component.text("§cYou already have an active slayer quest!"));
            return;
        }
        
        // Check if player has required level
        int requiredLevel = tier.getRequiredLevel();
        int playerLevel = getSlayerLevel(player.getUniqueId(), type);
        
        if (playerLevel < requiredLevel) {
            player.sendMessage("§cYou need " + type.getName() + " Slayer Level " + requiredLevel + " to start this quest!");
            return;
        }
        
        // Check if player has enough coins
        double cost = tier.getCost();
        if (!SkyblockPlugin.getEconomyManager().hasBalance(player, cost)) {
            player.sendMessage("§cYou don't have enough coins! Cost: " + SkyblockPlugin.getEconomyManager().formatMoney(cost));
            return;
        }
        
        // Start quest
        SkyblockPlugin.getEconomyManager().withdrawMoney(player, cost);
        SlayerQuest quest = new SlayerQuest(player.getUniqueId(), type, tier);
        activeQuests.put(player.getUniqueId(), quest);
        
        player.sendMessage(Component.text("§a§lSLAYER QUEST STARTED!"));
        player.sendMessage("§7Type: §e" + type.getName());
        player.sendMessage("§7Tier: §e" + tier.getName());
        player.sendMessage("§7Cost: §6" + SkyblockPlugin.getEconomyManager().formatMoney(cost));
        player.sendMessage("§7Objective: §eKill " + tier.getKillsRequired() + " " + type.getName() + "s");
    }
    
    public void completeSlayerQuest(Player player) {
        SlayerQuest quest = activeQuests.get(player.getUniqueId());
        if (quest == null) {
            player.sendMessage(Component.text("§cYou don't have an active slayer quest!"));
            return;
        }
        
        if (!quest.isCompleted()) {
            player.sendMessage(Component.text("§cYour slayer quest is not completed yet!"));
            return;
        }
        
        // Give rewards
        giveSlayerRewards(player, quest);
        
        // Add XP
        addSlayerXP(player, quest.getType(), quest.getTier().getXPReward());
        
        // Remove quest
        activeQuests.remove(player.getUniqueId());
        
        player.sendMessage(Component.text("§a§lSLAYER QUEST COMPLETED!"));
        player.sendMessage("§7Type: §e" + quest.getType().getName());
        player.sendMessage("§7Tier: §e" + quest.getTier().getName());
        player.sendMessage("§7XP Gained: §e" + quest.getTier().getXPReward());
    }
    
    private void giveSlayerRewards(Player player, SlayerQuest quest) {
        SlayerTier tier = quest.getTier();
        SlayerType type = quest.getType();
        
        // Give coins
        SkyblockPlugin.getEconomyManager().giveMoney(player, tier.getCoinReward());
        
        // Give items based on slayer type and tier
        switch (type) {
            case ZOMBIE -> {
                player.getInventory().addItem(new ItemStack(Material.ROTTEN_FLESH, tier.getTier()));
                player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, tier.getTier()));
                if (tier.getTier() >= 3) {
                    player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
                }
            }
            case SPIDER -> {
                player.getInventory().addItem(new ItemStack(Material.STRING, tier.getTier() * 2));
                player.getInventory().addItem(new ItemStack(Material.SPIDER_EYE, tier.getTier()));
                if (tier.getTier() >= 3) {
                    player.getInventory().addItem(new ItemStack(Material.COBWEB, 1));
                }
            }
            case WOLF -> {
                player.getInventory().addItem(new ItemStack(Material.BONE, tier.getTier()));
                player.getInventory().addItem(new ItemStack(Material.LEATHER, tier.getTier()));
                if (tier.getTier() >= 3) {
                    player.getInventory().addItem(new ItemStack(Material.WOLF_SPAWN_EGG, 1));
                }
            }
            case ENDERMAN -> {
                player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, tier.getTier()));
                player.getInventory().addItem(new ItemStack(Material.ENDER_EYE, tier.getTier()));
                if (tier.getTier() >= 3) {
                    player.getInventory().addItem(new ItemStack(Material.ENDER_CHEST, 1));
                }
            }
            case BLAZE -> {
                player.getInventory().addItem(new ItemStack(Material.BLAZE_POWDER, tier.getTier()));
                player.getInventory().addItem(new ItemStack(Material.BLAZE_ROD, tier.getTier()));
                if (tier.getTier() >= 3) {
                    player.getInventory().addItem(new ItemStack(Material.FIRE_CHARGE, 1));
                }
            }
        }
        
        player.sendMessage(Component.text("§a§lREWARDS RECEIVED!"));
        player.sendMessage("§7Coins: §6" + SkyblockPlugin.getEconomyManager().formatMoney(tier.getCoinReward()));
        player.sendMessage("§7Items: §e" + tier.getTier() + " items");
    }
    
    public void addSlayerXP(Player player, SlayerType type, int xp) {
        UUID playerId = player.getUniqueId();
        initializePlayerSlayerData(playerId);
        
        int currentXP = playerSlayerXP.get(playerId).get(type);
        int newXP = currentXP + xp;
        playerSlayerXP.get(playerId).put(type, newXP);
        
        // Check for level up
        int oldLevel = playerSlayerLevels.get(playerId).get(type);
        int newLevel = calculateSlayerLevel(newXP);
        
        if (newLevel > oldLevel) {
            playerSlayerLevels.get(playerId).put(type, newLevel);
            player.sendMessage(Component.text("§a§lSLAYER LEVEL UP!"));
            player.sendMessage("§7" + type.getName() + " Slayer: §e" + oldLevel + " §7→ §a" + newLevel);
            
            // Give level up rewards
            giveSlayerLevelRewards(player, type, newLevel);
        }
    }
    
    private void giveSlayerLevelRewards(Player player, SlayerType type, int level) {
        // Give rewards based on slayer type and level
        switch (type) {
            case ZOMBIE -> {
                if (level % 5 == 0) {
                    SkyblockPlugin.getEconomyManager().giveMoney(player, level * 100);
                    player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
                }
            }
            case SPIDER -> {
                if (level % 5 == 0) {
                    SkyblockPlugin.getEconomyManager().giveMoney(player, level * 100);
                    player.getInventory().addItem(new ItemStack(Material.COBWEB, 1));
                }
            }
            case WOLF -> {
                if (level % 5 == 0) {
                    SkyblockPlugin.getEconomyManager().giveMoney(player, level * 100);
                    player.getInventory().addItem(new ItemStack(Material.WOLF_SPAWN_EGG, 1));
                }
            }
            case ENDERMAN -> {
                if (level % 5 == 0) {
                    SkyblockPlugin.getEconomyManager().giveMoney(player, level * 100);
                    player.getInventory().addItem(new ItemStack(Material.ENDER_CHEST, 1));
                }
            }
            case BLAZE -> {
                if (level % 5 == 0) {
                    SkyblockPlugin.getEconomyManager().giveMoney(player, level * 100);
                    player.getInventory().addItem(new ItemStack(Material.FIRE_CHARGE, 1));
                }
            }
        }
    }
    
    private int calculateSlayerLevel(int xp) {
        // Hypixel-like slayer level calculation
        if (xp < 100) return 0;
        if (xp < 250) return 1;
        if (xp < 500) return 2;
        if (xp < 750) return 3;
        if (xp < 1000) return 4;
        if (xp < 1500) return 5;
        if (xp < 2000) return 6;
        if (xp < 2500) return 7;
        if (xp < 3000) return 8;
        if (xp < 3500) return 9;
        if (xp < 4000) return 10;
        if (xp < 4500) return 11;
        if (xp < 5000) return 12;
        if (xp < 5500) return 13;
        if (xp < 6000) return 14;
        if (xp < 6500) return 15;
        if (xp < 7000) return 16;
        if (xp < 7500) return 17;
        if (xp < 8000) return 18;
        if (xp < 8500) return 19;
        if (xp < 9000) return 20;
        if (xp < 9500) return 21;
        if (xp < 10000) return 22;
        if (xp < 10500) return 23;
        if (xp < 11000) return 24;
        if (xp < 11500) return 25;
        if (xp < 12000) return 26;
        if (xp < 12500) return 27;
        if (xp < 13000) return 28;
        if (xp < 13500) return 29;
        if (xp < 14000) return 30;
        if (xp < 14500) return 31;
        if (xp < 15000) return 32;
        if (xp < 15500) return 33;
        if (xp < 16000) return 34;
        if (xp < 16500) return 35;
        if (xp < 17000) return 36;
        if (xp < 17500) return 37;
        if (xp < 18000) return 38;
        if (xp < 18500) return 39;
        if (xp < 19000) return 40;
        if (xp < 19500) return 41;
        if (xp < 20000) return 42;
        if (xp < 20500) return 43;
        if (xp < 21000) return 44;
        if (xp < 21500) return 45;
        if (xp < 22000) return 46;
        if (xp < 22500) return 47;
        if (xp < 23000) return 48;
        if (xp < 23500) return 49;
        return 50; // Max level
    }
    
    public int getSlayerLevel(UUID playerId, SlayerType type) {
        return playerSlayerLevels.getOrDefault(playerId, new HashMap<>()).getOrDefault(type, 0);
    }
    
    public int getSlayerXP(UUID playerId, SlayerType type) {
        return playerSlayerXP.getOrDefault(playerId, new HashMap<>()).getOrDefault(type, 0);
    }
    
    public SlayerQuest getActiveQuest(UUID playerId) {
        return activeQuests.get(playerId);
    }
    
    public enum SlayerType {
        ZOMBIE("Zombie", Material.ZOMBIE_HEAD),
        SPIDER("Spider", Material.SPIDER_EYE),
        WOLF("Wolf", Material.WOLF_SPAWN_EGG),
        ENDERMAN("Enderman", Material.ENDER_PEARL),
        BLAZE("Blaze", Material.BLAZE_POWDER);
        
        private final String name;
        private final Material icon;
        
        SlayerType(String name, Material icon) {
            this.name = name;
            this.icon = icon;
        }
        
        public String getName() { return name; }
        public Material getIcon() { return icon; }
    }
    
    public enum SlayerTier {
        TIER_1("Tier I", 1, 100, 50, 100, 0),
        TIER_2("Tier II", 2, 200, 100, 200, 1),
        TIER_3("Tier III", 3, 300, 150, 300, 2),
        TIER_4("Tier IV", 4, 400, 200, 400, 3),
        TIER_5("Tier V", 5, 500, 250, 500, 4);
        
        private final String name;
        private final int tier;
        private final int killsRequired;
        private final int xpReward;
        private final int coinReward;
        private final int requiredLevel;
        
        SlayerTier(String name, int tier, int killsRequired, int xpReward, int coinReward, int requiredLevel) {
            this.name = name;
            this.tier = tier;
            this.killsRequired = killsRequired;
            this.xpReward = xpReward;
            this.coinReward = coinReward;
            this.requiredLevel = requiredLevel;
        }
        
        public String getName() { return name; }
        public int getTier() { return tier; }
        public int getKillsRequired() { return killsRequired; }
        public int getXPReward() { return xpReward; }
        public int getCoinReward() { return coinReward; }
        public int getRequiredLevel() { return requiredLevel; }
        public double getCost() { return tier * 100.0; }
    }
    
    public static class SlayerQuest {
        private final UUID playerId;
        private final SlayerType type;
        private final SlayerTier tier;
        private final long startTime;
        private int kills;
        private boolean completed;
        
        public SlayerQuest(UUID playerId, SlayerType type, SlayerTier tier) {
            this.playerId = playerId;
            this.type = type;
            this.tier = tier;
            this.startTime = java.lang.System.currentTimeMillis();
            this.kills = 0;
            this.completed = false;
        }
        
        public void addKill() {
            kills++;
            if (kills >= tier.getKillsRequired()) {
                completed = true;
            }
        }
        
        public boolean isCompleted() {
            return completed;
        }
        
        public long getTimeRemaining() {
            // Slayer quests have a 2-hour time limit
            long timeLimit = 2 * 60 * 60 * 1000L; // 2 hours in milliseconds
            long elapsed = java.lang.System.currentTimeMillis() - startTime;
            return Math.max(0, timeLimit - elapsed);
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public SlayerType getType() { return type; }
        public SlayerTier getTier() { return tier; }
        public long getStartTime() { return startTime; }
        public int getKills() { return kills; }
    }
}
