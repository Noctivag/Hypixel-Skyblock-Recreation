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
        // Beispiel-Standardwerte für Material, Kategorie, Beschreibung, experienceValue
        Material defaultMaterial = Material.END_ROD;
        FairySoulCategory defaultCategory = FairySoulCategory.HUB;
        List<String> defaultDescription = Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses");
        int defaultExp = 1;
        // Initialize fairy souls in different locations
        addFairySoul("hub_1", "Hub Fairy Soul", defaultMaterial, defaultCategory, new Location(Bukkit.getWorld("hub_a"), 10, 70, 10), defaultDescription, defaultExp);
        addFairySoul("hub_2", "Hidden Hub Fairy Soul", defaultMaterial, defaultCategory, new Location(Bukkit.getWorld("hub_a"), -10, 70, -10), defaultDescription, defaultExp);
        addFairySoul("hub_3", "Sky Hub Fairy Soul", defaultMaterial, defaultCategory, new Location(Bukkit.getWorld("hub_a"), 0, 80, 0), defaultDescription, defaultExp);
        addFairySoul("village_1", "Village Fairy Soul", defaultMaterial, defaultCategory, new Location(Bukkit.getWorld("hub_a"), 50, 65, 50), defaultDescription, defaultExp);
        addFairySoul("village_2", "Village Cellar Fairy Soul", defaultMaterial, defaultCategory, new Location(Bukkit.getWorld("hub_a"), -50, 65, -50), defaultDescription, defaultExp);
        addFairySoul("mountain_1", "Mountain Peak Fairy Soul", defaultMaterial, defaultCategory, new Location(Bukkit.getWorld("hub_a"), 100, 100, 100), defaultDescription, defaultExp);
        addFairySoul("mountain_2", "Mountain Cave Fairy Soul", defaultMaterial, defaultCategory, new Location(Bukkit.getWorld("hub_a"), -100, 120, -100), defaultDescription, defaultExp);
        plugin.getLogger().info("FairySoulsSystem initialized with " + fairySouls.size() + " fairy souls.");
    }

    private void addFairySoul(String id, String name, Material material, FairySoulCategory category, Location location, List<String> description, int experienceValue) {
        fairySouls.put(id, new FairySoul(id, name, material, category, location, description, experienceValue));
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
