package de.noctivag.skyblock.farming;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Farming System - Hypixel Skyblock Style
 * XP für Ernten von Feldfrüchten, Levelaufstieg, Skill-Integration
 */
public class AdvancedFarmingSystem implements Listener {
    private final SkyblockPlugin plugin;
    private final Map<UUID, PlayerFarmingData> playerFarmingData = new ConcurrentHashMap<>();

    public AdvancedFarmingSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public PlayerFarmingData getPlayerFarmingData(UUID playerId) {
        return playerFarmingData.computeIfAbsent(playerId, PlayerFarmingData::new);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material blockType = event.getBlock().getType();
        int xp = getXPForCrop(blockType);
        if (xp > 0) {
            PlayerFarmingData data = getPlayerFarmingData(player.getUniqueId());
            data.addFarmingXP(xp);
            player.sendMessage("§a+" + xp + " Farming XP");
        }
    }

    private int getXPForCrop(Material material) {
        return switch (material) {
            case WHEAT, CARROTS, POTATOES, BEETROOTS -> 10;
            case PUMPKIN, MELON, SUGAR_CANE, CACTUS -> 15;
            case NETHER_WART, COCOA, BAMBOO -> 20;
            default -> 0;
        };
    }
}
