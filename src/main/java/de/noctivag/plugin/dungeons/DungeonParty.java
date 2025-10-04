package de.noctivag.plugin.dungeons;

import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dungeon Party - Manages party members and their readiness
 */
public class DungeonParty {
    
    private final UUID partyId;
    private final Player leader;
    private final Set<Player> members = ConcurrentHashMap.newKeySet();
    private final Map<UUID, Boolean> memberReady = new ConcurrentHashMap<>();
    
    public DungeonParty(UUID partyId, Player leader) {
        this.partyId = partyId;
        this.leader = leader;
        this.members.add(leader);
        this.memberReady.put(leader.getUniqueId(), false);
    }
    
    /**
     * Add member to party
     */
    public boolean addMember(Player player) {
        if (members.size() >= 5) {
            return false; // Party is full
        }
        
        if (members.add(player)) {
            memberReady.put(player.getUniqueId(), false);
            return true;
        }
        
        return false;
    }
    
    /**
     * Remove member from party
     */
    public boolean removeMember(Player player) {
        if (members.remove(player)) {
            memberReady.remove(player.getUniqueId());
            return true;
        }
        
        return false;
    }
    
    /**
     * Set member readiness
     */
    public void setMemberReady(Player player, boolean ready) {
        if (members.contains(player)) {
            memberReady.put(player.getUniqueId(), ready);
        }
    }
    
    /**
     * Check if member is ready
     */
    public boolean isMemberReady(Player player) {
        return memberReady.getOrDefault(player.getUniqueId(), false);
    }
    
    /**
     * Check if all members are ready
     */
    public boolean areAllMembersReady() {
        return memberReady.values().stream().allMatch(Boolean::booleanValue);
    }
    
    /**
     * Get party ID
     */
    public UUID getPartyId() {
        return partyId;
    }
    
    /**
     * Get leader
     */
    public Player getLeader() {
        return leader;
    }
    
    /**
     * Get members
     */
    public Set<Player> getMembers() {
        return new HashSet<>(members);
    }
    
    /**
     * Get member count
     */
    public int getMemberCount() {
        return members.size();
    }
    
    /**
     * Check if party is full
     */
    public boolean isFull() {
        return members.size() >= 5;
    }
    
    /**
     * Check if player is in party
     */
    public boolean containsPlayer(Player player) {
        return members.contains(player);
    }
    
    /**
     * Get ready member count
     */
    public int getReadyMemberCount() {
        return (int) memberReady.values().stream().mapToInt(ready -> ready ? 1 : 0).sum();
    }
    
    /**
     * Disband party
     */
    public void disband() {
        members.clear();
        memberReady.clear();
    }
}
