package de.noctivag.plugin.bosses;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.core.CorePlatform;
import de.noctivag.plugin.core.PlayerProfile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import net.kyori.adventure.text.Component;

import java.util.*;

/**
 * Missing Bosses System - Hypixel Skyblock Style Bosses
 * Implements various missing bosses from Hypixel Skyblock
 */
public class MissingBossesSystem {

    private final Plugin plugin;
    private final CorePlatform corePlatform;
    private final Map<UUID, BossFight> activeBossFights;
    private final Map<String, BossConfig> bossConfigs;

    public MissingBossesSystem(Plugin plugin, CorePlatform corePlatform) {
        this.plugin = plugin;
        this.corePlatform = corePlatform;
        this.activeBossFights = new HashMap<>();
        this.bossConfigs = new HashMap<>();
        initializeBossConfigs();
    }

    private void initializeBossConfigs() {
        // Dragon Boss
        bossConfigs.put("dragon", new BossConfig(
            "Dragon", EntityType.ENDER_DRAGON, 1000, 50,
            Arrays.asList("§7Dragon Boss", "§7High tier rewards"),
            Arrays.asList("§7Flying mechanics", "§7Fire breath attack")
        ));

        // Wither Boss
        bossConfigs.put("wither", new BossConfig(
            "Wither", EntityType.WITHER, 800, 40,
            Arrays.asList("§7Wither Boss", "§7Good rewards"),
            Arrays.asList("§7Wither skulls", "§7Wither effect")
        ));

        // Ender Dragon Variant
        bossConfigs.put("ender_dragon_variant", new BossConfig(
            "Ender Dragon Variant", EntityType.ENDER_DRAGON, 1200, 60,
            Arrays.asList("§7Variant Dragon", "§7Special rewards"),
            Arrays.asList("§7Enhanced abilities", "§7Special mechanics")
        ));
    }

    public void spawnBoss(Player player, String bossType) {
        BossConfig config = bossConfigs.get(bossType);
        if (config == null) {
            player.sendMessage("§cUnknown boss type: " + bossType);
            return;
        }

        Location spawnLocation = player.getLocation().add(10, 0, 10);
        LivingEntity boss = (LivingEntity) player.getWorld().spawnEntity(spawnLocation, config.getEntityType());

        // Configure boss
        boss.customName(Component.text("§c§l" + config.getName()));
        boss.setCustomNameVisible(true);
        boss.setMaxHealth(config.getHealth());
        boss.setHealth(config.getHealth());

        // Add special effects
        boss.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1));
        boss.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1));

        // Create boss fight
        BossFight fight = new BossFight(boss, player, config);
        activeBossFights.put(boss.getUniqueId(), fight);

        // Use plugin logger so the field is accessed
        if (plugin != null) {
            plugin.getLogger().info("Spawned boss '" + config.getName() + "' for player " + player.getName());
        }

        player.sendMessage("§a§lBOSS SPAWNED: " + config.getName());
        player.sendMessage("§7Health: " + config.getHealth() + " HP");
        player.sendMessage("§7Damage: " + config.getDamage() + " DMG");
    }

    public void handleBossDeath(LivingEntity boss) {
        BossFight fight = activeBossFights.remove(boss.getUniqueId());
        if (fight == null) return;

        Player player = fight.getPlayer();
        BossConfig config = fight.getConfig();

        // Give rewards
        giveBossRewards(player, config);

        if (plugin != null) {
            plugin.getLogger().info("Boss '" + config.getName() + "' defeated by " + player.getName());
        }

        player.sendMessage("§a§lBOSS DEFEATED: " + config.getName());
        player.sendMessage("§7Rewards have been given!");
    }

    private void giveBossRewards(Player player, BossConfig config) {
        PlayerProfile profile = corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null) return;

        // Give coins
        double coinReward = config.getHealth() * 0.1;
        profile.addCoins(coinReward);

        // Give XP
        int xpReward = (int) (config.getHealth() / 10);
        // Add XP to profile if method exists

        // Give items
        ItemStack rewardItem = createBossRewardItem(config);
        player.getInventory().addItem(rewardItem);

        player.sendMessage("§a+$" + coinReward + " coins");
        player.sendMessage("§a+" + xpReward + " XP");
    }

    private ItemStack createBossRewardItem(BossConfig config) {
        ItemStack item = new ItemStack(Material.DRAGON_EGG);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(net.kyori.adventure.text.Component.text("§6§l" + config.getName() + " Trophy"));
        meta.lore(Arrays.asList(
            net.kyori.adventure.text.Component.text("§7Boss Trophy"),
            net.kyori.adventure.text.Component.text("§7Defeated: " + config.getName()),
            net.kyori.adventure.text.Component.text("§7Health: " + config.getHealth()),
            net.kyori.adventure.text.Component.text("§7Damage: " + config.getDamage())
        ));

        item.setItemMeta(meta);
        return item;
    }

    public void startBossFight(Player player, String bossType) {
        if (activeBossFights.values().stream().anyMatch(fight -> fight.getPlayer().equals(player))) {
            player.sendMessage("§cYou are already in a boss fight!");
            return;
        }

        spawnBoss(player, bossType);
    }

    public void endBossFight(Player player) {
        activeBossFights.values().removeIf(fight -> {
            if (fight.getPlayer().equals(player)) {
                fight.getBoss().remove();
                return true;
            }
            return false;
        });
    }

    public boolean isInBossFight(Player player) {
        return activeBossFights.values().stream().anyMatch(fight -> fight.getPlayer().equals(player));
    }

    public BossFight getBossFight(Player player) {
        return activeBossFights.values().stream()
            .filter(fight -> fight.getPlayer().equals(player))
            .findFirst()
            .orElse(null);
    }

    public Collection<BossFight> getActiveBossFights() {
        return activeBossFights.values();
    }

    public Map<String, BossConfig> getBossConfigs() {
        return bossConfigs;
    }

    // Inner classes
    public static class BossConfig {
        private final String name;
        private final EntityType entityType;
        private final double health;
        private final double damage;
        private final List<String> rewards;
        private final List<String> mechanics;

        public BossConfig(String name, EntityType entityType, double health, double damage,
                         List<String> rewards, List<String> mechanics) {
            this.name = name;
            this.entityType = entityType;
            this.health = health;
            this.damage = damage;
            this.rewards = rewards;
            this.mechanics = mechanics;
        }

        // Getters
        public String getName() { return name; }
        public EntityType getEntityType() { return entityType; }
        public double getHealth() { return health; }
        public double getDamage() { return damage; }
        public List<String> getRewards() { return rewards; }
        public List<String> getMechanics() { return mechanics; }
    }

    public static class BossFight {
        private final LivingEntity boss;
        private final Player player;
        private final BossConfig config;
        private final long startTime;

        public BossFight(LivingEntity boss, Player player, BossConfig config) {
            this.boss = boss;
            this.player = player;
            this.config = config;
            this.startTime = System.currentTimeMillis();
        }

        // Getters
        public LivingEntity getBoss() { return boss; }
        public Player getPlayer() { return player; }
        public BossConfig getConfig() { return config; }
        public long getStartTime() { return startTime; }
        public long getDuration() { return System.currentTimeMillis() - startTime; }
    }
}
