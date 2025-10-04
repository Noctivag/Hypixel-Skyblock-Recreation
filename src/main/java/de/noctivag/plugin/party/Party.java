package de.noctivag.plugin.party;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Party {
    private final UUID leader;
    private final Set<UUID> members;
    private final Set<UUID> invites;

    public Party(UUID leader) {
        this.leader = leader;
        this.members = new HashSet<>();
        this.invites = new HashSet<>();
        this.members.add(leader);
    }

    public UUID getLeader() {
        return leader;
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public Set<UUID> getInvites() {
        return invites;
    }
}


