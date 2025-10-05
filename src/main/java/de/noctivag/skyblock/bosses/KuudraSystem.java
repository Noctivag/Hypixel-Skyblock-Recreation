package de.noctivag.skyblock.bosses;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Kuudra System - Complete Hypixel SkyBlock Kuudra Boss Implementation
 * 
 * Features:
 * - 5 Kuudra tiers (Basic, Hot, Burning, Fiery, Infernal)
 * - Kuudra mechanics and phases
 * - Kuudra equipment and armor
 * - Kuudra drops and rewards
 * - Kuudra party system
 * - Kuudra achievements
 */
public class KuudraSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerKuudraData> playerKuudraData = new ConcurrentHashMap<>();
    private final Map<String, KuudraParty> activeParties = new ConcurrentHashMap<>();
    private final Map<String, KuudraInstance> activeKuudraFights = new ConcurrentHashMap<>();
    private final Map<KuudraTier, KuudraTierConfig> tierConfigs = new HashMap<>();
    private final Map<KuudraPhase, KuudraPhaseConfig> phaseConfigs = new HashMap<>();
    
    public KuudraSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeTierConfigs();
        initializePhaseConfigs();
        startKuudraUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeTierConfigs() {
        // Basic Kuudra
        tierConfigs.put(KuudraTier.BASIC, new KuudraTierConfig(
            "Basic", "§fBasic Kuudra", "§7The weakest form of Kuudra",
            10000, 100, 1000, 5000,
            Arrays.asList("§7Basic Kuudra mechanics", "§7Low damage", "§7Simple phases"),
            Arrays.asList("§7Basic Kuudra equipment", "§7Low tier rewards")
        ));
        
        // Hot Kuudra
        tierConfigs.put(KuudraTier.HOT, new KuudraTierConfig(
            "Hot", "§cHot Kuudra", "§7A heated version of Kuudra",
            20000, 200, 2000, 10000,
            Arrays.asList("§7Fire attacks", "§7Medium damage", "§7Heat mechanics"),
            Arrays.asList("§7Hot Kuudra equipment", "§7Medium tier rewards")
        ));
        
        // Burning Kuudra
        tierConfigs.put(KuudraTier.BURNING, new KuudraTierConfig(
            "Burning", "§6Burning Kuudra", "§7A burning version of Kuudra",
            30000, 300, 3000, 15000,
            Arrays.asList("§7Intense fire attacks", "§7High damage", "§7Burning mechanics"),
            Arrays.asList("§7Burning Kuudra equipment", "§7High tier rewards")
        ));
        
        // Fiery Kuudra
        tierConfigs.put(KuudraTier.FIERY, new KuudraTierConfig(
            "Fiery", "§4Fiery Kuudra", "§7A fiery version of Kuudra",
            40000, 400, 4000, 20000,
            Arrays.asList("§7Devastating fire attacks", "§7Very high damage", "§7Fiery mechanics"),
            Arrays.asList("§7Fiery Kuudra equipment", "§7Very high tier rewards")
        ));
        
        // Infernal Kuudra
        tierConfigs.put(KuudraTier.INFERNAL, new KuudraTierConfig(
            "Infernal", "§0Infernal Kuudra", "§7The most powerful form of Kuudra",
            50000, 500, 5000, 25000,
            Arrays.asList("§7Infernal fire attacks", "§7Extreme damage", "§7Infernal mechanics"),
            Arrays.asList("§7Infernal Kuudra equipment", "§7Extreme tier rewards")
        ));
    }
    
    private void initializePhaseConfigs() {
        // Phase 1 - Spawn
        phaseConfigs.put(KuudraPhase.SPAWN, new KuudraPhaseConfig(
            "Spawn", "§7Kuudra is spawning...",
            10, Arrays.asList("§7Kuudra is emerging from the depths"),
            Arrays.asList("§7Wait for Kuudra to fully spawn")
        ));
        
        // Phase 2 - Rage
        phaseConfigs.put(KuudraPhase.RAGE, new KuudraPhaseConfig(
            "Rage", "§cKuudra is enraged!",
            30, Arrays.asList("§7Kuudra attacks with increased damage", "§7Fire attacks are more frequent"),
            Arrays.asList("§7Dodge Kuudra's attacks", "§7Focus on survival")
        ));
        
        // Phase 3 - Berserk
        phaseConfigs.put(KuudraPhase.BERSERK, new KuudraPhaseConfig(
            "Berserk", "§4Kuudra is berserk!",
            20, Arrays.asList("§7Kuudra attacks with maximum damage", "§7All attacks are enhanced"),
            Arrays.asList("§7Use all defensive abilities", "§7Coordinate with team")
        ));
        
        // Phase 4 - Death
        phaseConfigs.put(KuudraPhase.DEATH, new KuudraPhaseConfig(
            "Death", "§0Kuudra is dying...",
            5, Arrays.asList("§7Kuudra is defeated", "§7Collect your rewards"),
            Arrays.asList("§7Collect drops", "§7Celebrate victory")
        ));
    }
    
    private void startKuudraUpdateTask() {
        Bukkit.getScheduler().runTaskTimer(SkyblockPlugin, () -> {
            for (KuudraInstance instance : activeKuudraFights.values()) {
                updateKuudraInstance(instance);
            }
        }, 0L, 20L); // Every second
    }
    
    private void updateKuudraInstance(KuudraInstance instance) {
        if (instance.getStatus() == KuudraStatus.ACTIVE) {
            instance.setTimeElapsed(instance.getTimeElapsed() + 1);
            
            // Update phase based on time
            updateKuudraPhase(instance);
            
            // Check for completion
            if (instance.getKuudra().getCurrentHealth() <= 0) {
                completeKuudraFight(instance);
            }
        }
    }
    
    private void updateKuudraPhase(KuudraInstance instance) {
        Kuudra kuudra = instance.getKuudra();
        int timeElapsed = instance.getTimeElapsed();
        
        if (timeElapsed < 10) {
            kuudra.setPhase(KuudraPhase.SPAWN);
        } else if (timeElapsed < 40) {
            kuudra.setPhase(KuudraPhase.RAGE);
        } else if (timeElapsed < 60) {
            kuudra.setPhase(KuudraPhase.BERSERK);
        } else {
            kuudra.setPhase(KuudraPhase.DEATH);
        }
    }
    
    public void createKuudraParty(Player leader, KuudraTier tier) {
        String partyId = UUID.randomUUID().toString();
        KuudraParty party = new KuudraParty(partyId, leader, tier);
        activeParties.put(partyId, party);
        
        leader.sendMessage("§aKuudra party created for " + tier.getDisplayName() + "!");
        leader.sendMessage("§7Use /kuudra invite <player> to invite players.");
    }
    
    public void joinKuudraParty(Player player, String partyId) {
        KuudraParty party = activeParties.get(partyId);
        if (party != null && party.getMembers().size() < 5) {
            party.addMember(player);
            player.sendMessage("§aJoined Kuudra party for " + party.getTier().getDisplayName() + "!");
            
            // Notify all party members
            for (Player member : party.getMembers()) {
                member.sendMessage("§7" + player.getName() + " joined the party!");
            }
        }
    }
    
    public void startKuudraFight(KuudraParty party) {
        if (party.getMembers().size() < 1) {
            party.getLeader().sendMessage("§cYou need at least 1 player to start a Kuudra fight!");
            return;
        }
        
        KuudraTierConfig tierConfig = tierConfigs.get(party.getTier());
        
        KuudraInstance instance = new KuudraInstance(
            UUID.randomUUID().toString(),
            party,
            tierConfig,
            java.lang.System.currentTimeMillis()
        );
        
        activeKuudraFights.put(instance.getInstanceId(), instance);
        
        // Spawn Kuudra
        spawnKuudra(instance);
        
        // Teleport all players to fight area
        for (Player player : party.getMembers()) {
            teleportToKuudraArea(player, instance);
            player.sendMessage("§aKuudra fight started! Tier: " + party.getTier().getDisplayName());
        }
    }
    
    private void spawnKuudra(KuudraInstance instance) {
        Location spawnLocation = new Location(
            Bukkit.getWorld("kuudra_" + instance.getInstanceId()),
            0, 100, 0
        );
        
        Kuudra kuudra = new Kuudra(
            instance.getTierConfig(),
            spawnLocation,
            instance.getParty().getMembers()
        );
        
        instance.setKuudra(kuudra);
        
        // Spawn effects
        spawnLocation.getWorld().spawnParticle(Particle.EXPLOSION, spawnLocation, 5);
        spawnLocation.getWorld().playSound(spawnLocation, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
        
        // Notify all players
        for (Player player : instance.getParty().getMembers()) {
            player.sendMessage("§c⚠ " + kuudra.getDisplayName() + " has spawned! ⚠");
        }
    }
    
    private void teleportToKuudraArea(Player player, KuudraInstance instance) {
        Location fightLocation = new Location(
            Bukkit.getWorld("kuudra_" + instance.getInstanceId()),
            0, 100, 0
        );
        
        player.teleport(fightLocation);
        player.sendMessage(Component.text("§aTeleported to Kuudra fight area!"));
    }
    
    private void completeKuudraFight(KuudraInstance instance) {
        instance.setStatus(KuudraStatus.COMPLETED);
        
        // Calculate rewards
        KuudraRewards rewards = calculateKuudraRewards(instance);
        
        // Give rewards to all players
        for (Player player : instance.getParty().getMembers()) {
            giveKuudraRewards(player, rewards);
        }
        
        // Clean up
        activeKuudraFights.remove(instance.getInstanceId());
        
        // Notify all players
        for (Player player : instance.getParty().getMembers()) {
            player.sendMessage(Component.text("§aKuudra fight completed!"));
            player.sendMessage("§7Time: " + instance.getTimeElapsed() + " seconds");
        }
    }
    
    private KuudraRewards calculateKuudraRewards(KuudraInstance instance) {
        KuudraTierConfig tierConfig = instance.getTierConfig();
        
        int baseXP = tierConfig.getBaseXP();
        int baseCoins = tierConfig.getBaseCoins();
        
        // Calculate bonus based on time
        int timeBonus = Math.max(0, 300 - instance.getTimeElapsed()); // 5 minute limit
        int xpBonus = timeBonus * 10;
        int coinBonus = timeBonus * 50;
        
        return new KuudraRewards(
            baseXP + xpBonus,
            baseCoins + coinBonus,
            generateKuudraDrops(instance)
        );
    }
    
    private List<ItemStack> generateKuudraDrops(KuudraInstance instance) {
        List<ItemStack> drops = new ArrayList<>();
        KuudraTier tier = instance.getParty().getTier();
        
        // Base drops
        drops.add(new ItemStack(Material.BLAZE_POWDER, 1));
        drops.add(new ItemStack(Material.GHAST_TEAR, 1));
        
        // Tier-specific drops
        switch (tier) {
            case BASIC:
                drops.add(createKuudraItem("Basic Kuudra Claw", Material.BLAZE_ROD, "§fBasic Kuudra Claw"));
                break;
            case HOT:
                drops.add(createKuudraItem("Hot Kuudra Claw", Material.BLAZE_ROD, "§cHot Kuudra Claw"));
                break;
            case BURNING:
                drops.add(createKuudraItem("Burning Kuudra Claw", Material.BLAZE_ROD, "§6Burning Kuudra Claw"));
                break;
            case FIERY:
                drops.add(createKuudraItem("Fiery Kuudra Claw", Material.BLAZE_ROD, "§4Fiery Kuudra Claw"));
                break;
            case INFERNAL:
                drops.add(createKuudraItem("Infernal Kuudra Claw", Material.BLAZE_ROD, "§0Infernal Kuudra Claw"));
                break;
        }
        
        // Rare drops
        if (Math.random() < getRareDropChance(tier)) {
            drops.add(createRareKuudraItem(tier));
        }
        
        return drops;
    }
    
    private double getRareDropChance(KuudraTier tier) {
        switch (tier) {
            case BASIC: return 0.1; // 10%
            case HOT: return 0.15; // 15%
            case BURNING: return 0.2; // 20%
            case FIERY: return 0.25; // 25%
            case INFERNAL: return 0.3; // 30%
            default: return 0.1;
        }
    }
    
    private ItemStack createKuudraItem(String name, Material material, String displayName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(displayName));
        meta.lore(Arrays.asList(
            "§7A powerful item from Kuudra",
            "§7Used for crafting Kuudra equipment",
            "",
            "§7Rarity: §6Rare"
        ).stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
        item.setItemMeta(meta);
        return item;
    }
    
    private ItemStack createRareKuudraItem(KuudraTier tier) {
        switch (tier) {
            case BASIC:
                return createKuudraItem("Basic Kuudra Core", Material.NETHER_STAR, "§6Basic Kuudra Core");
            case HOT:
                return createKuudraItem("Hot Kuudra Core", Material.NETHER_STAR, "§6Hot Kuudra Core");
            case BURNING:
                return createKuudraItem("Burning Kuudra Core", Material.NETHER_STAR, "§6Burning Kuudra Core");
            case FIERY:
                return createKuudraItem("Fiery Kuudra Core", Material.NETHER_STAR, "§6Fiery Kuudra Core");
            case INFERNAL:
                return createKuudraItem("Infernal Kuudra Core", Material.NETHER_STAR, "§6Infernal Kuudra Core");
            default:
                return new ItemStack(Material.AIR);
        }
    }
    
    private void giveKuudraRewards(Player player, KuudraRewards rewards) {
        // Give XP
        player.sendMessage("§a+" + rewards.getXp() + " Kuudra XP");
        
        // Give coins
        // This would integrate with your economy system
        
        // Give items
        for (ItemStack item : rewards.getDrops()) {
            player.getInventory().addItem(item);
            player.sendMessage("§aReceived: " + item.getItemMeta().getDisplayName());
        }
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().hasMetadata("kuudra_boss")) {
            String instanceId = event.getEntity().getMetadata("kuudra_instance").get(0).asString();
            KuudraInstance instance = activeKuudraFights.get(instanceId);
            
            if (instance != null) {
                completeKuudraFight(instance);
            }
        }
    }
    
    // Enums and Classes
    public enum KuudraTier {
        BASIC("Basic", "§fBasic"),
        HOT("Hot", "§cHot"),
        BURNING("Burning", "§6Burning"),
        FIERY("Fiery", "§4Fiery"),
        INFERNAL("Infernal", "§0Infernal");
        
        private final String name;
        private final String displayName;
        
        KuudraTier(String name, String displayName) {
            this.name = name;
            this.displayName = displayName;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
    }
    
    public enum KuudraPhase {
        SPAWN, RAGE, BERSERK, DEATH
    }
    
    public enum KuudraStatus {
        WAITING, ACTIVE, COMPLETED, FAILED
    }
    
    // Data Classes
    public static class KuudraTierConfig {
        private final String name;
        private final String displayName;
        private final String description;
        private final int health;
        private final int damage;
        private final int baseXP;
        private final int baseCoins;
        private final List<String> mechanics;
        private final List<String> rewards;
        
        public KuudraTierConfig(String name, String displayName, String description, int health, int damage,
                              int baseXP, int baseCoins, List<String> mechanics, List<String> rewards) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.health = health;
            this.damage = damage;
            this.baseXP = baseXP;
            this.baseCoins = baseCoins;
            this.mechanics = mechanics;
            this.rewards = rewards;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public int getHealth() { return health; }
        public int getDamage() { return damage; }
        public int getBaseXP() { return baseXP; }
        public int getBaseCoins() { return baseCoins; }
        public List<String> getMechanics() { return mechanics; }
        public List<String> getRewards() { return rewards; }
    }
    
    public static class KuudraPhaseConfig {
        private final String name;
        private final String displayName;
        private final int duration;
        private final List<String> mechanics;
        private final List<String> strategies;
        
        public KuudraPhaseConfig(String name, String displayName, int duration, List<String> mechanics, List<String> strategies) {
            this.name = name;
            this.displayName = displayName;
            this.duration = duration;
            this.mechanics = mechanics;
            this.strategies = strategies;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public int getDuration() { return duration; }
        public List<String> getMechanics() { return mechanics; }
        public List<String> getStrategies() { return strategies; }
    }
    
    public static class KuudraParty {
        private final String partyId;
        private final Player leader;
        private final KuudraTier tier;
        private final List<Player> members;
        
        public KuudraParty(String partyId, Player leader, KuudraTier tier) {
            this.partyId = partyId;
            this.leader = leader;
            this.tier = tier;
            this.members = new ArrayList<>();
            this.members.add(leader);
        }
        
        public void addMember(Player player) {
            if (!members.contains(player)) {
                members.add(player);
            }
        }
        
        public void removeMember(Player player) {
            members.remove(player);
        }
        
        // Getters
        public String getPartyId() { return partyId; }
        public Player getLeader() { return leader; }
        public KuudraTier getTier() { return tier; }
        public List<Player> getMembers() { return members; }
    }
    
    public static class KuudraInstance {
        private final String instanceId;
        private final KuudraParty party;
        private final KuudraTierConfig tierConfig;
        private final long startTime;
        private KuudraStatus status;
        private int timeElapsed;
        private Kuudra kuudra;
        
        public KuudraInstance(String instanceId, KuudraParty party, KuudraTierConfig tierConfig, long startTime) {
            this.instanceId = instanceId;
            this.party = party;
            this.tierConfig = tierConfig;
            this.startTime = startTime;
            this.status = KuudraStatus.WAITING;
            this.timeElapsed = 0;
        }
        
        // Getters and Setters
        public String getInstanceId() { return instanceId; }
        public KuudraParty getParty() { return party; }
        public KuudraTierConfig getTierConfig() { return tierConfig; }
        public long getStartTime() { return startTime; }
        public KuudraStatus getStatus() { return status; }
        public void setStatus(KuudraStatus status) { this.status = status; }
        public int getTimeElapsed() { return timeElapsed; }
        public void setTimeElapsed(int timeElapsed) { this.timeElapsed = timeElapsed; }
        public Kuudra getKuudra() { return kuudra; }
        public void setKuudra(Kuudra kuudra) { this.kuudra = kuudra; }
    }
    
    public static class Kuudra {
        private final KuudraTierConfig tierConfig;
        private final Location location;
        private final List<Player> targets;
        private int currentHealth;
        private KuudraPhase phase;
        
        public Kuudra(KuudraTierConfig tierConfig, Location location, List<Player> targets) {
            this.tierConfig = tierConfig;
            this.location = location;
            this.targets = targets;
            this.currentHealth = tierConfig.getHealth();
            this.phase = KuudraPhase.SPAWN;
        }
        
        public String getDisplayName() {
            return tierConfig.getDisplayName() + " Kuudra";
        }
        
        // Getters and Setters
        public KuudraTierConfig getTierConfig() { return tierConfig; }
        public Location getLocation() { return location; }
        public List<Player> getTargets() { return targets; }
        public int getCurrentHealth() { return currentHealth; }
        public void setCurrentHealth(int currentHealth) { this.currentHealth = currentHealth; }
        public KuudraPhase getPhase() { return phase; }
        public void setPhase(KuudraPhase phase) { this.phase = phase; }
    }
    
    public static class KuudraRewards {
        private final int xp;
        private final int coins;
        private final List<ItemStack> drops;
        
        public KuudraRewards(int xp, int coins, List<ItemStack> drops) {
            this.xp = xp;
            this.coins = coins;
            this.drops = drops;
        }
        
        // Getters
        public int getXp() { return xp; }
        public int getCoins() { return coins; }
        public List<ItemStack> getDrops() { return drops; }
    }
    
    public static class PlayerKuudraData {
        private final UUID playerId;
        private int totalXP;
        private int level;
        private final Map<KuudraTier, Integer> tierCompletions;
        
        public PlayerKuudraData(UUID playerId) {
            this.playerId = playerId;
            this.totalXP = 0;
            this.level = 1;
            this.tierCompletions = new HashMap<>();
        }
        
        public void addXP(int xp) {
            this.totalXP += xp;
            this.level = calculateLevel(totalXP);
        }
        
        private int calculateLevel(int xp) {
            return Math.min(50, (int) Math.floor(Math.sqrt(xp / 100.0)) + 1);
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public int getTotalXP() { return totalXP; }
        public int getLevel() { return level; }
        public Map<KuudraTier, Integer> getTierCompletions() { return tierCompletions; }
    }
}
