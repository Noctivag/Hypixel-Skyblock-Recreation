package de.noctivag.plugin.social;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a party with members and leader
 */
public class Party {
    
    private UUID leader;
    private final Set<UUID> members;
    private final int maxSize;
    
    public Party(UUID leader) {
        this.leader = leader;
        this.members = new HashSet<>();
        this.members.add(leader);
        this.maxSize = 5; // Maximum party size
    }
    
    public UUID getLeader() {
        return leader;
    }
    
    public void setLeader(UUID leader) {
        this.leader = leader;
    }
    
    public Set<UUID> getMembers() {
        return new HashSet<>(members);
    }
    
    public void addMember(UUID memberId) {
        members.add(memberId);
    }
    
    public void removeMember(UUID memberId) {
        members.remove(memberId);
    }
    
    public boolean isFull() {
        return members.size() >= maxSize;
    }
    
    public int getSize() {
        return members.size();
    }
    
    public int getMaxSize() {
        return maxSize;
    }
    
    public boolean contains(UUID playerId) {
        return members.contains(playerId);
    }
    
    @Override
    public String toString() {
        return "Party{" +
                "leader=" + leader +
                ", members=" + members +
                ", maxSize=" + maxSize +
                '}';
    }
}
