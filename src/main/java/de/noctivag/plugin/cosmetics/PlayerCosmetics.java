package de.noctivag.plugin.cosmetics;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerCosmetics {
    private final UUID playerId;
    private final Map<String, String> activeCosmetics;

    public PlayerCosmetics(UUID playerId) {
        this.playerId = playerId;
        this.activeCosmetics = new ConcurrentHashMap<>();
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Map<String, String> getActiveCosmetics() {
        return activeCosmetics;
    }

    public void setCosmetic(String category, String cosmetic) {
        activeCosmetics.put(category, cosmetic);
    }

    public String getCosmetic(String category) {
        return activeCosmetics.get(category);
    }
}
