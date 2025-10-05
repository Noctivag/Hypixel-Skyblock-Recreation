package de.noctivag.skyblock.friends;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import org.bukkit.entity.Player;

import java.util.*;

public class FriendsManager {
    private final Map<UUID, Set<UUID>> friends = new HashMap<>();
    private final Map<UUID, Set<UUID>> requests = new HashMap<>();

    public Set<UUID> getFriends(UUID player) {
        return friends.computeIfAbsent(player, k -> new HashSet<>());
    }

    public void requestFriend(UUID from, UUID to) {
        requests.computeIfAbsent(to, k -> new HashSet<>()).add(from);
    }

    public boolean accept(Player to, Player from) {
        Set<UUID> reqs = requests.getOrDefault(to.getUniqueId(), Collections.emptySet());
        if (!reqs.remove(from.getUniqueId())) return false;
        getFriends(to.getUniqueId()).add(from.getUniqueId());
        getFriends(from.getUniqueId()).add(to.getUniqueId());
        return true;
    }

    public boolean removeFriend(Player a, Player b) {
        boolean changed = getFriends(a.getUniqueId()).remove(b.getUniqueId());
        getFriends(b.getUniqueId()).remove(a.getUniqueId());
        return changed;
    }
}


