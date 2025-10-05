package de.noctivag.skyblock.skyblock;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DungeonSystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<UUID, DungeonParty> parties = new ConcurrentHashMap<>();
    private final Map<UUID, DungeonRun> activeRuns = new ConcurrentHashMap<>();
    private final Map<UUID, DungeonClass> playerClasses = new ConcurrentHashMap<>();
    
    public DungeonSystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        initializeDungeonWorlds();
    }
    
    private void initializeDungeonWorlds() {
        // Create dungeon worlds
        World dungeonWorld = Bukkit.getWorld("skyblock_dungeons");
        if (dungeonWorld == null) {
            SkyblockPlugin.getLogger().info("Dungeon world not found, it will be created by WorldManager");
        } else {
            SkyblockPlugin.getLogger().info("Dungeon world loaded successfully");
        }
    }
    
    public void createParty(Player leader) {
        UUID partyId = UUID.randomUUID();
        DungeonParty party = new DungeonParty(partyId, leader.getUniqueId());
        parties.put(partyId, party);
        
        leader.sendMessage("§a§lDUNGEON PARTY CREATED!");
        leader.sendMessage("§7Party ID: §e" + partyId.toString().substring(0, 8));
        leader.sendMessage("§7Use §e/party invite <player> §7to invite players!");
    }
    
    public void invitePlayer(Player leader, Player target) {
        DungeonParty party = getPlayerParty(leader.getUniqueId());
        if (party == null) {
            leader.sendMessage("§cYou are not in a party!");
            return;
        }
        
        if (!party.isLeader(leader.getUniqueId())) {
            leader.sendMessage("§cOnly the party leader can invite players!");
            return;
        }
        
        if (party.isFull()) {
            leader.sendMessage("§cParty is full!");
            return;
        }
        
        party.invitePlayer(target.getUniqueId());
        
        leader.sendMessage("§a§lINVITATION SENT!");
        leader.sendMessage("§7Invited: §e" + target.getName());
        
        target.sendMessage("§a§lDUNGEON PARTY INVITATION!");
        target.sendMessage("§7From: §e" + leader.getName());
        target.sendMessage("§7Use §e/party accept §7to join!");
    }
    
    public void acceptInvitation(Player player) {
        DungeonParty party = getPlayerParty(player.getUniqueId());
        if (party == null) {
            player.sendMessage(Component.text("§cYou don't have any pending invitations!"));
            return;
        }
        
        if (!party.hasInvitation(player.getUniqueId())) {
            player.sendMessage(Component.text("§cYou don't have any pending invitations!"));
            return;
        }
        
        party.acceptInvitation(player.getUniqueId());
        
        player.sendMessage(Component.text("§a§lJOINED PARTY!"));
        player.sendMessage("§7Party Leader: §e" + Bukkit.getPlayer(party.getLeader()).getName());
        
        // Notify party members
        for (UUID memberId : party.getMembers()) {
            Player member = Bukkit.getPlayer(memberId);
            if (member != null) {
                member.sendMessage("§a§l" + player.getName() + " joined the party!");
            }
        }
    }
    
    public void startDungeon(Player leader, DungeonFloor floor) {
        DungeonParty party = getPlayerParty(leader.getUniqueId());
        if (party == null) {
            leader.sendMessage("§cYou are not in a party!");
            return;
        }
        
        if (!party.isLeader(leader.getUniqueId())) {
            leader.sendMessage("§cOnly the party leader can start dungeons!");
            return;
        }
        
        if (party.getMembers().size() < 1) {
            leader.sendMessage("§cYou need at least 1 player to start a dungeon!");
            return;
        }
        
        // Check if all players have selected a class
        for (UUID memberId : party.getMembers()) {
            if (!playerClasses.containsKey(memberId)) {
                leader.sendMessage("§cAll players must select a dungeon class first!");
                return;
            }
        }
        
        // Create dungeon run
        UUID runId = UUID.randomUUID();
        DungeonRun run = new DungeonRun(runId, party, floor);
        activeRuns.put(runId, run);
        
        // Start dungeon
        run.startDungeon();
        
        leader.sendMessage("§a§lDUNGEON STARTED!");
        leader.sendMessage("§7Floor: §e" + floor.getName());
        leader.sendMessage("§7Difficulty: §e" + floor.getDifficulty());
    }
    
    public void selectClass(Player player, DungeonClass dungeonClass) {
        playerClasses.put(player.getUniqueId(), dungeonClass);
        
        player.sendMessage(Component.text("§a§lCLASS SELECTED!"));
        player.sendMessage("§7Class: §e" + dungeonClass.getName());
        player.sendMessage("§7Description: §7" + dungeonClass.getDescription());
        
        // Give class-specific items
        giveClassItems(player, dungeonClass);
    }
    
    private void giveClassItems(Player player, DungeonClass dungeonClass) {
        // Give class-specific items based on the class
        switch (dungeonClass) {
            case TANK -> {
                player.getInventory().addItem(new ItemStack(Material.IRON_CHESTPLATE));
                player.getInventory().addItem(new ItemStack(Material.IRON_LEGGINGS));
                player.getInventory().addItem(new ItemStack(Material.IRON_BOOTS));
                player.getInventory().addItem(new ItemStack(Material.IRON_HELMET));
                player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
                player.getInventory().addItem(new ItemStack(Material.SHIELD));
            }
            case ARCHER -> {
                player.getInventory().addItem(new ItemStack(Material.LEATHER_CHESTPLATE));
                player.getInventory().addItem(new ItemStack(Material.LEATHER_LEGGINGS));
                player.getInventory().addItem(new ItemStack(Material.LEATHER_BOOTS));
                player.getInventory().addItem(new ItemStack(Material.LEATHER_HELMET));
                player.getInventory().addItem(new ItemStack(Material.BOW));
                player.getInventory().addItem(new ItemStack(Material.ARROW, 64));
            }
            case MAGE -> {
                player.getInventory().addItem(new ItemStack(Material.GOLDEN_CHESTPLATE));
                player.getInventory().addItem(new ItemStack(Material.GOLDEN_LEGGINGS));
                player.getInventory().addItem(new ItemStack(Material.GOLDEN_BOOTS));
                player.getInventory().addItem(new ItemStack(Material.GOLDEN_HELMET));
                player.getInventory().addItem(new ItemStack(Material.BLAZE_ROD));
                player.getInventory().addItem(new ItemStack(Material.POTION, 3));
            }
            case HEALER -> {
                player.getInventory().addItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
                player.getInventory().addItem(new ItemStack(Material.CHAINMAIL_LEGGINGS));
                player.getInventory().addItem(new ItemStack(Material.CHAINMAIL_BOOTS));
                player.getInventory().addItem(new ItemStack(Material.CHAINMAIL_HELMET));
                player.getInventory().addItem(new ItemStack(Material.STICK));
                player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 5));
            }
            case BERSERKER -> {
                player.getInventory().addItem(new ItemStack(Material.DIAMOND_CHESTPLATE));
                player.getInventory().addItem(new ItemStack(Material.DIAMOND_LEGGINGS));
                player.getInventory().addItem(new ItemStack(Material.DIAMOND_BOOTS));
                player.getInventory().addItem(new ItemStack(Material.DIAMOND_HELMET));
                player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
                player.getInventory().addItem(new ItemStack(Material.POTION, 2));
            }
        }
    }
    
    public DungeonParty getPlayerParty(UUID playerId) {
        for (DungeonParty party : parties.values()) {
            if (party.getMembers().contains(playerId)) {
                return party;
            }
        }
        return null;
    }
    
    public DungeonClass getPlayerClass(UUID playerId) {
        return playerClasses.get(playerId);
    }
    
    public enum DungeonClass {
        TANK("Tank", "High health and defense, protects the party"),
        ARCHER("Archer", "Ranged damage dealer with high DPS"),
        MAGE("Mage", "Magic damage dealer with area effects"),
        HEALER("Healer", "Supports the party with healing and buffs"),
        BERSERKER("Berserker", "High damage melee fighter");
        
        private final String name;
        private final String description;
        
        DungeonClass(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
    }
    
    public enum DungeonFloor {
        ENTRANCE("Entrance", "Easy", 1, 100),
        FLOOR_1("Floor 1", "Easy", 2, 200),
        FLOOR_2("Floor 2", "Medium", 3, 300),
        FLOOR_3("Floor 3", "Medium", 4, 400),
        FLOOR_4("Floor 4", "Hard", 5, 500),
        FLOOR_5("Floor 5", "Hard", 6, 600),
        FLOOR_6("Floor 6", "Very Hard", 7, 700),
        FLOOR_7("Floor 7", "Very Hard", 8, 800),
        FLOOR_8("Floor 8", "Extreme", 9, 900),
        FLOOR_9("Floor 9", "Extreme", 10, 1000),
        FLOOR_10("Floor 10", "Legendary", 11, 1500);
        
        private final String name;
        private final String difficulty;
        private final int level;
        private final int baseReward;
        
        DungeonFloor(String name, String difficulty, int level, int baseReward) {
            this.name = name;
            this.difficulty = difficulty;
            this.level = level;
            this.baseReward = baseReward;
        }
        
        public String getName() { return name; }
        public String getDifficulty() { return difficulty; }
        public int getLevel() { return level; }
        public int getBaseReward() { return baseReward; }
    }
    
    public static class DungeonParty {
        private final UUID id;
        private final UUID leader;
        private final Set<UUID> members = new HashSet<>();
        private final Set<UUID> invitations = new HashSet<>();
        
        public DungeonParty(UUID id, UUID leader) {
            this.id = id;
            this.leader = leader;
            this.members.add(leader);
        }
        
        public void invitePlayer(UUID playerId) {
            invitations.add(playerId);
        }
        
        public void acceptInvitation(UUID playerId) {
            if (invitations.contains(playerId)) {
                invitations.remove(playerId);
                members.add(playerId);
            }
        }
        
        public boolean hasInvitation(UUID playerId) {
            return invitations.contains(playerId);
        }
        
        public boolean isLeader(UUID playerId) {
            return leader.equals(playerId);
        }
        
        public boolean isFull() {
            return members.size() >= 5; // Max 5 players
        }
        
        // Getters
        public UUID getId() { return id; }
        public UUID getLeader() { return leader; }
        public Set<UUID> getMembers() { return new HashSet<>(members); }
        public Set<UUID> getInvitations() { return new HashSet<>(invitations); }
    }
    
    public static class DungeonRun {
        private final UUID id;
        private final DungeonParty party;
        private final DungeonFloor floor;
        private final long startTime;
        private boolean completed;
        private boolean failed;
        
        public DungeonRun(UUID id, DungeonParty party, DungeonFloor floor) {
            this.id = id;
            this.party = party;
            this.floor = floor;
            this.startTime = java.lang.System.currentTimeMillis();
            this.completed = false;
            this.failed = false;
        }
        
        public void startDungeon() {
            // Teleport all party members to dungeon
            for (UUID memberId : party.getMembers()) {
                Player member = Bukkit.getPlayer(memberId);
                if (member != null) {
                    // Teleport to dungeon world
                    Location dungeonSpawn = new Location(Bukkit.getWorld("skyblock_dungeons"), 0, 100, 0);
                    member.teleport(dungeonSpawn);
                    
                    member.sendMessage("§a§lDUNGEON STARTED!");
                    member.sendMessage("§7Floor: §e" + floor.getName());
                    member.sendMessage("§7Difficulty: §e" + floor.getDifficulty());
                }
            }
        }
        
        public void completeDungeon() {
            this.completed = true;
            
            // Give rewards to all party members
            for (UUID memberId : party.getMembers()) {
                Player member = Bukkit.getPlayer(memberId);
                if (member != null) {
                    // Give coins
                    // Give items
                    // Give XP
                    
                    member.sendMessage("§a§lDUNGEON COMPLETED!");
                    member.sendMessage("§7Floor: §e" + floor.getName());
                    member.sendMessage("§7Reward: §6" + floor.getBaseReward() + " coins");
                }
            }
        }
        
        public void failDungeon() {
            this.failed = true;
            
            // Teleport all party members back to hub
            for (UUID memberId : party.getMembers()) {
                Player member = Bukkit.getPlayer(memberId);
                if (member != null) {
                    // Teleport to hub
                    Location hubSpawn = new Location(Bukkit.getWorld("skyblock_hub"), 0, 100, 0);
                    member.teleport(hubSpawn);
                    
                    member.sendMessage("§c§lDUNGEON FAILED!");
                    member.sendMessage("§7Floor: §e" + floor.getName());
                }
            }
        }
        
        // Getters
        public UUID getId() { return id; }
        public DungeonParty getParty() { return party; }
        public DungeonFloor getFloor() { return floor; }
        public long getStartTime() { return startTime; }
        public boolean isCompleted() { return completed; }
        public boolean isFailed() { return failed; }
    }
}
