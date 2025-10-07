package de.noctivag.skyblock.fairysouls;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Fairy Souls System - Collectible fairy souls scattered across the world
 */
public class FairySoulsSystem implements Service, Listener {

    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    private final Map<String, FairySoul> fairySouls;
    private final Map<UUID, Set<String>> collectedSouls;
    private final Random random;

    public FairySoulsSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.fairySouls = new HashMap<>();
        this.collectedSouls = new HashMap<>();
        this.random = new Random();
        initializeFairySouls();
    }

    private void initializeFairySouls() {
        // Initialize fairy souls in different locations
        addFairySoul("hub_1", new Location(Bukkit.getWorld("hub_a"), 10, 70, 10), "Hub Fairy Soul");
        addFairySoul("hub_2", new Location(Bukkit.getWorld("hub_a"), -10, 70, -10), "Hidden Hub Fairy Soul");
        addFairySoul("hub_3", new Location(Bukkit.getWorld("hub_a"), 0, 80, 0), "Sky Hub Fairy Soul");

        addFairySoul("village_1", new Location(Bukkit.getWorld("hub_a"), 50, 65, 50), "Village Fairy Soul");
        addFairySoul("village_2", new Location(Bukkit.getWorld("hub_a"), -50, 65, -50), "Village Cellar Fairy Soul");

        addFairySoul("mountain_1", new Location(Bukkit.getWorld("hub_a"), 100, 100, 100), "Mountain Peak Fairy Soul");
        addFairySoul("mountain_2", new Location(Bukkit.getWorld("hub_a"), -100, 120, -100), "Mountain Cave Fairy Soul");

        plugin.getLogger().info("FairySoulsSystem initialized with " + fairySouls.size() + " fairy souls.");
    }

    private void addFairySoul(String id, Location location, String name) {
        fairySouls.put(id, new FairySoul(id, location, name));
    }

    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing FairySoulsSystem...");

        // Register event listeners
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        status = SystemStatus.RUNNING;
        plugin.getLogger().info("FairySoulsSystem initialized.");
    }

    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        fairySouls.clear();
        collectedSouls.clear();
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("FairySoulsSystem shut down.");
    }

    @Override
    public SystemStatus getStatus() {
        return status;
    }

    @Override
    public String getName() {
        return "FairySoulsSystem";
    }

    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status != SystemStatus.RUNNING) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
            event.getFrom().getBlockY() == event.getTo().getBlockY() &&
            event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return; // Player didn't actually move
        }

        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Check if player is near any fairy souls
        for (FairySoul soul : fairySouls.values()) {
            if (isPlayerNearSoul(player, soul) && !hasPlayerCollectedSoul(playerId, soul.getId())) {
                collectFairySoul(player, soul);
                break;
            }
        }
    }

    private boolean isPlayerNearSoul(Player player, FairySoul soul) {
        Location playerLoc = player.getLocation();
        Location soulLoc = soul.getLocation();

        double distance = playerLoc.distance(soulLoc);
        return distance <= 2.0; // Within 2 blocks
    }

    private boolean hasPlayerCollectedSoul(UUID playerId, String soulId) {
        return collectedSouls.computeIfAbsent(playerId, k -> new HashSet<>()).contains(soulId);
    }

    private void collectFairySoul(Player player, FairySoul soul) {
        UUID playerId = player.getUniqueId();
        collectedSouls.computeIfAbsent(playerId, k -> new HashSet<>()).add(soul.getId());

        // Give rewards
        player.sendMessage("§d✨ You found a Fairy Soul! §7(" + soul.getName() + ")");
        player.sendMessage("§a+ 100 Skyblock Experience");
        player.sendMessage("§6+ 500 Coins");

        // Play sound effect
        player.playSound(player.getLocation(), "ENTITY_EXPERIENCE_ORB_PICKUP", 1.0f, 2.0f);

        // Check for milestones
        int totalCollected = collectedSouls.get(playerId).size();
        if (totalCollected % 5 == 0) {
            player.sendMessage("§d🎉 Fairy Soul Milestone! §7You've collected " + totalCollected + " Fairy Souls!");
        }

        // TODO: Implement actual reward giving (XP, coins, etc.)
    }

    public int getCollectedSouls(UUID playerId) {
        return collectedSouls.getOrDefault(playerId, new HashSet<>()).size();
    }

    public int getTotalFairySouls() {
        return fairySouls.size();
    }

    public void openFairySoulsMenu(Player player) {
        // TODO: Implement fairy souls GUI showing collected/total souls
        player.sendMessage("§dFairy Souls: §f" + getCollectedSouls(player.getUniqueId()) + "§7/§f" + getTotalFairySouls());
        player.sendMessage("§7Use §e/fairysouls §7to view your collection!");
    }

    public List<FairySoul> getNearbySouls(Player player, double radius) {
        List<FairySoul> nearby = new ArrayList<>();
        Location playerLoc = player.getLocation();

        for (FairySoul soul : fairySouls.values()) {
            if (playerLoc.distance(soul.getLocation()) <= radius) {
                nearby.add(soul);
            }
        }

        return nearby;
    }
}

class FairySoul {
    private final String id;
    private final Location location;
    private final String name;

    public FairySoul(String id, Location location, String name) {
        this.id = id;
        this.location = location;
        this.name = name;
    }

    public String getId() { return id; }
    public Location getLocation() { return location; }
    public String getName() { return name; }

    public ItemStack getDisplayItem() {
        ItemStack item = new ItemStack(Material.END_CRYSTAL);
        // TODO: Set item meta with fairy soul information
        return item;
    }
}
