package de.noctivag.skyblock.moderation;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ModerationManager {
    private final Set<UUID> mutedPlayers = new HashSet<>();
    private final Set<UUID> bannedPlayers = new HashSet<>();

    public void mutePlayer(UUID player) {
        mutedPlayers.add(player);
    }

    public void unmutePlayer(UUID player) {
        mutedPlayers.remove(player);
    }

    public boolean isMuted(UUID player) {
        return mutedPlayers.contains(player);
    }

    public void banPlayer(UUID player) {
        bannedPlayers.add(player);
    }

    public void unbanPlayer(UUID player) {
        bannedPlayers.remove(player);
    }

    public boolean isBanned(UUID player) {
        return bannedPlayers.contains(player);
    }
}

