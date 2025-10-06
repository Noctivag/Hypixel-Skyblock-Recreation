package de.noctivag.skyblock.events;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.mobs.SeaWalker;
import de.noctivag.skyblock.mobs.NightSquid;
import de.noctivag.skyblock.mobs.SeaGuardian;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Random;

public class SeaCreatureEvent implements Listener {

    private final SkyblockPluginRefactored plugin;
    private final Random random = new Random();

    public SeaCreatureEvent(SkyblockPluginRefactored plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) {
            return;
        }

        Player player = event.getPlayer();
        Location hookLocation = event.getHook().getLocation();

        // Chance to spawn sea creature
        double spawnChance = calculateSpawnChance(player);
        
        if (random.nextDouble() < spawnChance) {
            spawnSeaCreature(player, hookLocation);
        }
    }

    private double calculateSpawnChance(Player player) {
        // Base chance: 5%
        double baseChance = 0.05;
        
        // Increase chance based on fishing level (if implemented)
        // For now, just use base chance
        
        // Increase chance based on fishing rod quality
        // This would be implemented based on the fishing rod the player is using
        
        return baseChance;
    }

    private void spawnSeaCreature(Player player, Location location) {
        // Determine which sea creature to spawn
        double creatureRoll = random.nextDouble();
        
        if (creatureRoll < 0.6) {
            // 60% chance for Sea Walker
            spawnSeaWalker(player, location);
        } else if (creatureRoll < 0.9) {
            // 30% chance for Night Squid
            spawnNightSquid(player, location);
        } else {
            // 10% chance for Sea Guardian (mini-boss)
            spawnSeaGuardian(player, location);
        }
    }

    private void spawnSeaWalker(Player player, Location location) {
        SeaWalker seaWalker = new SeaWalker("sea_walker_" + System.currentTimeMillis(), location);
        seaWalker.spawn();
        
        player.sendMessage("§b§lSea Walker §7ist aus dem Wasser aufgestiegen!");
        
        if (isDebugMode()) {
            plugin.getLogger().info("Sea Walker spawned for player " + player.getName() + " at " + location);
        }
    }

    private void spawnNightSquid(Player player, Location location) {
        NightSquid nightSquid = new NightSquid("night_squid_" + System.currentTimeMillis(), location);
        nightSquid.spawn();
        
        player.sendMessage("§5§lNight Squid §7erscheint aus den Tiefen!");
        
        if (isDebugMode()) {
            plugin.getLogger().info("Night Squid spawned for player " + player.getName() + " at " + location);
        }
    }

    private void spawnSeaGuardian(Player player, Location location) {
        SeaGuardian seaGuardian = new SeaGuardian("sea_guardian_" + System.currentTimeMillis(), location);
        seaGuardian.spawn();
        
        player.sendMessage("§b§lSea Guardian §7erhebt sich aus den Tiefen!");
        player.sendMessage("§c§lVORSICHT: §7Dieser Boss ist sehr gefährlich!");
        
        // Notify nearby players
        location.getWorld().getPlayers().forEach(p -> {
            if (p.getLocation().distance(location) <= 50 && !p.equals(player)) {
                p.sendMessage("§b§lSea Guardian §7wurde von " + player.getName() + " beschworen!");
            }
        });
        
        if (isDebugMode()) {
            plugin.getLogger().info("Sea Guardian (mini-boss) spawned for player " + player.getName() + " at " + location);
        }
    }
    
    /**
     * Check if debug mode is enabled
     */
    public boolean isDebugMode() {
        return false; // Placeholder - always return false for now
    }
}
