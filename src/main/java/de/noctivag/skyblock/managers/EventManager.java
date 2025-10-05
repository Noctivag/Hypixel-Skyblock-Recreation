package de.noctivag.skyblock.managers;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import java.util.*;
import java.time.Duration;

public class EventManager {
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<String, BossBar> activeBossBars = new HashMap<>();
    private final Map<String, Set<UUID>> eventParticipants = new HashMap<>();
    private final Map<UUID, Integer> playerPoints = new HashMap<>();

    public EventManager(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        scheduleEvents();
    }

    private void scheduleEvents() {
        // Dragon Event alle 2 Stunden - use virtual thread for Folia compatibility
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(20L * 60 * 120 * 50); // Initial delay: 2 hours in ms
                while (SkyblockPlugin.isEnabled()) {
                    startDragonEvent();
                    Thread.sleep(20L * 60 * 120 * 50); // 2 hours = 7,200,000 ms
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Wither Event jede Stunde - use virtual thread for Folia compatibility
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(20L * 60 * 60 * 50); // Initial delay: 1 hour in ms
                while (SkyblockPlugin.isEnabled()) {
                    startWitherEvent();
                    Thread.sleep(20L * 60 * 60 * 50); // 1 hour = 3,600,000 ms
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Zombie-Horde alle 30 Minuten - use virtual thread for Folia compatibility
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(20L * 60 * 30 * 50); // Initial delay: 30 minutes in ms
                while (SkyblockPlugin.isEnabled()) {
                    startZombieHorde();
                    Thread.sleep(20L * 60 * 30 * 50); // 30 minutes = 1,800,000 ms
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public void startDragonEvent() {
        World end = Bukkit.getWorlds().stream()
                .filter(w -> w.getEnvironment() == World.Environment.THE_END)
                .findFirst()
                .orElse(null);

        if (end == null) return;

        BossBar bossBar = Bukkit.createBossBar(
                "§5§lDrachen-Event", BarColor.PURPLE, BarStyle.SOLID);
        activeBossBars.put("dragon", bossBar);

        // Spawn Dragon
        Location spawnLoc = end.getSpawnLocation();
        EnderDragon dragon = (EnderDragon) end.spawnEntity(spawnLoc, EntityType.ENDER_DRAGON);
        dragon.setCustomName("§5§lEvent Dragon");
        dragon.setCustomNameVisible(true);

        // Ankündigung
        Bukkit.broadcast(Component.text("§5§lDas Drachen-Event hat begonnen!"));
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.showTitle(Title.title(
                    Component.text("§5§lDrachen-Event"),
                    Component.text("§7Der Kampf beginnt!"),
                    Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))
            ));
            bossBar.addPlayer(p);
        });

        // Event-Ende nach 15 Minuten - use virtual thread for Folia compatibility
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(20L * 60 * 15 * 50); // 15 minutes = 900,000 ms
                if (SkyblockPlugin.isEnabled()) {
                    endEvent("dragon");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public void startWitherEvent() {
        World nether = Bukkit.getWorlds().stream()
                .filter(w -> w.getEnvironment() == World.Environment.NETHER)
                .findFirst()
                .orElse(null);

        if (nether == null) return;

        BossBar bossBar = Bukkit.createBossBar(
                "§4§lWither-Event", BarColor.RED, BarStyle.SOLID);
        activeBossBars.put("wither", bossBar);

        // Spawn Wither
        Location spawnLoc = nether.getSpawnLocation();
        Wither wither = (Wither) nether.spawnEntity(spawnLoc, EntityType.WITHER);
        wither.setCustomName("§4§lEvent Wither");
        wither.setCustomNameVisible(true);

        // Ankündigung
        Bukkit.broadcast(Component.text("§4§lDas Wither-Event hat begonnen!"));
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.showTitle(Title.title(
                    Component.text("§4§lWither-Event"),
                    Component.text("§7Der Kampf beginnt in der Hölle!"),
                    Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))
            ));
            bossBar.addPlayer(p);
        });

        // Event-Ende nach 10 Minuten - use virtual thread for Folia compatibility
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(20L * 60 * 10 * 50); // 10 minutes = 600,000 ms
                if (SkyblockPlugin.isEnabled()) {
                    endEvent("wither");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public void startZombieHorde() {
        World overworld = Bukkit.getWorlds().stream()
                .filter(w -> w.getEnvironment() == World.Environment.NORMAL)
                .findFirst()
                .orElse(null);

        if (overworld == null) return;

        BossBar bossBar = Bukkit.createBossBar(
                "§2§lZombie-Horde", BarColor.GREEN, BarStyle.SOLID);
        activeBossBars.put("zombie", bossBar);

        // Spawn Zombies
        Location spawnLoc = overworld.getSpawnLocation();
        for (int i = 0; i < 50; i++) {
            overworld.spawnEntity(spawnLoc, EntityType.ZOMBIE);
        }

        // Ankündigung
        Bukkit.broadcast(Component.text("§2§lDie Zombie-Horde ist da!"));
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.showTitle(Title.title(
                    Component.text("§2§lZombie-Horde"),
                    Component.text("§7Überlebe den Angriff!"),
                    Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))
            ));
            bossBar.addPlayer(p);
        });

        // Event-Ende nach 5 Minuten - use virtual thread for Folia compatibility
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(20L * 60 * 5 * 50); // 5 minutes = 300,000 ms
                if (SkyblockPlugin.isEnabled()) {
                    endEvent("zombie");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private void endEvent(String eventId) {
        BossBar bossBar = activeBossBars.remove(eventId);
        if (bossBar != null) {
            bossBar.removeAll();
        }

        Set<UUID> participants = eventParticipants.remove(eventId);
        if (participants != null) {
            participants.forEach(uuid -> {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    addEventPoints(player, 100); // Basis-Punkte für Teilnahme
                }
            });
        }
    }

    public void addEventPoints(Player player, int points) {
        UUID uuid = player.getUniqueId();
        playerPoints.merge(uuid, points, Integer::sum);
        player.sendMessage(Component.text("§a+" + points + " Event-Punkte!"));
    }

    public int getEventPoints(Player player) {
        return playerPoints.getOrDefault(player.getUniqueId(), 0);
    }

    public void joinEvent(Player player, String eventId) {
        eventParticipants.computeIfAbsent(eventId, k -> new HashSet<>())
                .add(player.getUniqueId());
        player.sendMessage(Component.text("§aDu nimmst nun am Event teil!"));
    }

    // Missing methods that are called by other classes
    public boolean isPlayerInEvent(Player player) {
        return eventParticipants.values().stream()
                .anyMatch(participants -> participants.contains(player.getUniqueId()));
    }

    public String getPlayerEvent(Player player) {
        return eventParticipants.entrySet().stream()
                .filter(entry -> entry.getValue().contains(player.getUniqueId()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public List<String> getAvailableEvents() {
        return new ArrayList<>(activeBossBars.keySet());
    }
}
