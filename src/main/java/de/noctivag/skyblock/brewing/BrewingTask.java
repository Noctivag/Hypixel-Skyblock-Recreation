package de.noctivag.skyblock.brewing;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Brewing task for handling brewing processes
 */
public class BrewingTask extends BukkitRunnable {
    
    private final SkyblockPlugin plugin;
    private final Player player;
    private final String recipeId;
    private final int brewingTime;
    private int currentTime;
    
    public BrewingTask(SkyblockPlugin plugin, Player player, String recipeId, int brewingTime) {
        this.plugin = plugin;
        this.player = player;
        this.recipeId = recipeId;
        this.brewingTime = brewingTime;
        this.currentTime = 0;
    }
    
    @Override
    public void run() {
        currentTime++;
        
        if (currentTime >= brewingTime) {
            // Complete brewing
            plugin.getBrewingManager().completeBrewing(player, recipeId);
            cancel();
        } else {
            // Update brewing progress
            int progress = (currentTime * 100) / brewingTime;
            player.sendMessage("§eBrewing progress: " + progress + "%");
        }
    }
}
