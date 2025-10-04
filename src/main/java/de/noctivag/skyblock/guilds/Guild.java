package de.noctivag.skyblock.guilds;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Guild - Represents a guild in the game
 */
public class Guild {
    private final String id;
    private final String name;
    private final String tag;
    private final String description;
    private final UUID leader;
    private final Set<UUID> members;
    private final Set<UUID> officers;
    private final Map<String, Object> settings;
    private final long createdAt;
    private int level;
    private double experience;
    private final Map<String, Integer> statistics;
    private double coins;
    private int maxMembers;
    private final Map<UUID, GuildRole> memberRoles = new HashMap<>();
    private final Map<UUID, Double> memberContributions = new HashMap<>();
    
    public Guild(String id, String name, String tag, String description, UUID leader) {
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.description = description;
        this.leader = leader;
        this.members = new HashSet<>();
        this.officers = new HashSet<>();
        this.settings = new HashMap<>();
        this.createdAt = System.currentTimeMillis();
        this.level = 1;
        this.experience = 0.0;
        this.statistics = new HashMap<>();
        this.coins = 0.0;
        this.maxMembers = 10;
        
        // Add leader as member
        this.members.add(leader);
        this.memberRoles.put(leader, GuildRole.LEADER);
    }
    
    public Guild(String id, String name, String tag, UUID leader, int level, long experience, double coins, int memberCount, int maxMembers, String description) {
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.description = description;
        this.leader = leader;
        this.members = new HashSet<>();
        this.officers = new HashSet<>();
        this.settings = new HashMap<>();
        this.createdAt = System.currentTimeMillis();
        this.level = level;
        this.experience = experience;
        this.statistics = new HashMap<>();
        this.coins = coins;
        this.maxMembers = maxMembers;
        
        // Add leader as member
        this.members.add(leader);
        this.memberRoles.put(leader, GuildRole.LEADER);
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getTag() {
        return tag;
    }
    
    public String getDescription() {
        return description;
    }
    
    public UUID getLeader() {
        return leader;
    }
    
    public Set<UUID> getMemberSet() {
        return new HashSet<>(members);
    }
    
    public Set<UUID> getOfficers() {
        return new HashSet<>(officers);
    }
    
    public Map<String, Object> getSettings() {
        return new HashMap<>(settings);
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public int getLevel() {
        return level;
    }
    
    public double getExperience() {
        return experience;
    }
    
    public Map<String, Integer> getStatistics() {
        return new HashMap<>(statistics);
    }
    
    public boolean isMember(UUID playerId) {
        return members.contains(playerId);
    }
    
    public boolean isOfficer(UUID playerId) {
        return officers.contains(playerId);
    }
    
    public boolean isLeader(UUID playerId) {
        return leader.equals(playerId);
    }
    
    public void addMember(UUID playerId) {
        members.add(playerId);
    }
    
    public void removeMember(UUID playerId) {
        members.remove(playerId);
        officers.remove(playerId);
    }
    
    public void promoteToOfficer(UUID playerId) {
        if (members.contains(playerId)) {
            officers.add(playerId);
        }
    }
    
    public void demoteFromOfficer(UUID playerId) {
        officers.remove(playerId);
    }
    
    public void setSetting(String key, Object value) {
        settings.put(key, value);
    }
    
    public Object getSetting(String key) {
        return settings.get(key);
    }
    
    public void addExperience(double amount) {
        // Experience logic would go here
    }
    
    public void incrementStatistic(String key) {
        statistics.put(key, statistics.getOrDefault(key, 0) + 1);
    }
    
    public void setStatistic(String key, int value) {
        statistics.put(key, value);
    }
    
    public int getStatistic(String key) {
        return statistics.getOrDefault(key, 0);
    }
    
    public int getMemberCount() {
        return members.size();
    }
    
    public int getOfficerCount() {
        return officers.size();
    }
    
    public boolean canInvite(UUID playerId) {
        return isLeader(playerId) || isOfficer(playerId);
    }
    
    public boolean canKick(UUID playerId) {
        return isLeader(playerId) || (isOfficer(playerId) && !isLeader(playerId));
    }
    
    public boolean canPromote(UUID playerId) {
        return isLeader(playerId);
    }
    
    public boolean canChangeSettings(UUID playerId) {
        return isLeader(playerId) || isOfficer(playerId);
    }
    
    // New methods for AdvancedGuildSystem compatibility
    public double getCoins() {
        return coins;
    }
    
    public void addCoins(double amount) {
        this.coins += amount;
    }
    
    public boolean removeCoins(double amount) {
        if (this.coins >= amount) {
            this.coins -= amount;
            return true;
        }
        return false;
    }
    
    public int getMaxMembers() {
        return maxMembers;
    }
    
    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }
    
    public UUID getOwner() {
        return leader;
    }
    
    public void setOwner(UUID owner) {
        // Note: leader is final, so we can't change it directly
        // This would require restructuring the class
    }
    
    public void addMember(UUID playerId, GuildRole role) {
        if (members.size() < maxMembers) {
            members.add(playerId);
            memberRoles.put(playerId, role);
            memberContributions.put(playerId, 0.0);
        }
    }
    
    public GuildRole getMemberRole(UUID playerId) {
        return memberRoles.get(playerId);
    }
    
    public void setMemberRole(UUID playerId, GuildRole role) {
        if (members.contains(playerId)) {
            memberRoles.put(playerId, role);
        }
    }
    
    public boolean hasMember(UUID playerId) {
        return members.contains(playerId);
    }
    
    public void addMemberContribution(UUID playerId, double amount) {
        memberContributions.compute(playerId, (k, v) -> (v == null ? 0.0 : v) + amount);
    }
    
    public void addMemberContribution(UUID playerId, double amount, int type) {
        addMemberContribution(playerId, amount);
    }
    
    public void broadcastMessage(String message) {
        // Logic to send message to all guild members
        for (UUID memberId : members) {
            org.bukkit.Bukkit.getPlayer(memberId).sendMessage(message);
        }
    }
    
    public void update() {
        // Logic to update guild stats, e.g., level up
        int newLevel = calculateLevel(experience);
        if (newLevel > level) {
            level = newLevel;
            broadcastMessage("§6[Gilde] §fDie Gilde ist auf Level " + level + " aufgestiegen!");
        }
    }
    
    public Set<UUID> keySet() {
        return members;
    }
    
    public Map<UUID, GuildRole> getMembers() {
        return memberRoles;
    }
    
    public Map<UUID, Double> getMemberContributions() {
        return memberContributions;
    }
    
    public double getTotalContribution() {
        return memberContributions.values().stream().mapToDouble(Double::doubleValue).sum();
    }
    
    private int calculateLevel(double exp) {
        // Simple level calculation - can be made more complex
        return (int) (exp / 1000) + 1;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
}
