package de.noctivag.plugin.managers;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.cosmetics.ParticleEffect;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

@SuppressWarnings("unused")
public class FeatureManager {
    private final Plugin plugin;
    private final Map<UUID, Set<String>> unlockedCosmetics;
    private final Map<UUID, String> activeEffects;
    private final Map<String, ParticleEffect> availableEffects;
    private final Map<UUID, BukkitRunnable> activeTasks;
    private final Map<UUID, String> playerStatus;
    private final Map<String, Team> teams;

    public FeatureManager(Plugin plugin) {
        this.plugin = plugin;
        this.unlockedCosmetics = new HashMap<>();
        this.activeEffects = new HashMap<>();
        this.availableEffects = new HashMap<>();
        this.activeTasks = new HashMap<>();
        this.playerStatus = new HashMap<>();
        this.teams = new HashMap<>();
        loadData();
        initializeEffects();
    }

    private void loadData() {
        FileConfiguration config = plugin.getConfig();

        // Lade Kosmetik-Daten
        Optional.ofNullable(config.getConfigurationSection("cosmetics.unlocked"))
            .ifPresent(section -> section.getKeys(false).forEach(uuidStr -> {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    List<String> effects = config.getStringList("cosmetics.unlocked." + uuidStr);
                    unlockedCosmetics.put(uuid, new HashSet<>(effects));
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Ungültige UUID in cosmetics.unlocked: " + uuidStr);
                }
            }));

        // Lade aktive Effekte
        Optional.ofNullable(config.getConfigurationSection("cosmetics.active"))
            .ifPresent(section -> section.getKeys(false).forEach(uuidStr -> {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    String effect = config.getString("cosmetics.active." + uuidStr);
                    if (effect != null) {
                        activeEffects.put(uuid, effect);
                    }
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Ungültige UUID in cosmetics.active: " + uuidStr);
                }
            }));

        // Lade Status-Daten
        Optional.ofNullable(config.getConfigurationSection("status"))
            .ifPresent(section -> section.getKeys(false).forEach(uuidStr -> {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    String status = config.getString("status." + uuidStr);
                    if (status != null) {
                        playerStatus.put(uuid, status);
                    }
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Ungültige UUID in status: " + uuidStr);
                }
            }));
    }

    public void saveData() {
        FileConfiguration config = plugin.getConfig();

        // Speichere Kosmetik-Daten
        for (Map.Entry<UUID, Set<String>> entry : unlockedCosmetics.entrySet()) {
            String path = "cosmetics.unlocked." + entry.getKey();
            config.set(path, new ArrayList<>(entry.getValue()));
        }

        // Speichere aktive Effekte
        for (Map.Entry<UUID, String> entry : activeEffects.entrySet()) {
            String path = "cosmetics.active." + entry.getKey();
            config.set(path, entry.getValue());
        }

        // Speichere Status-Daten
        for (Map.Entry<UUID, String> entry : playerStatus.entrySet()) {
            config.set("status." + entry.getKey(), entry.getValue());
        }

        // TODO: Implement proper ConfigManager interface
        // ((ConfigManager) plugin.getConfigManager()).saveConfig("config");
    }

    // Kosmetik-Methoden
    public void unlockEffect(Player player, String effectName) {
        unlockedCosmetics.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>()).add(effectName.toLowerCase());
        saveData();
    }

    public boolean hasUnlocked(Player player, String effectName) {
        return unlockedCosmetics.getOrDefault(player.getUniqueId(), Collections.emptySet()).contains(effectName.toLowerCase());
    }

    public void activateEffect(Player player, String effectName) {
        UUID uuid = player.getUniqueId();
        deactivateEffect(player);

        ParticleEffect effect = availableEffects.get(effectName.toLowerCase());
        if (effect == null) return;

        activeEffects.put(uuid, effectName.toLowerCase());
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) { cancel(); return; }
                effect.spawn(player.getLocation());
            }
        };
        task.runTaskTimer(plugin, 0L, 5L);
        activeTasks.put(uuid, task);
    }

    public void deactivateEffect(Player player) {
        UUID uuid = player.getUniqueId();
        BukkitRunnable task = activeTasks.remove(uuid);
        if (task != null) task.cancel();
        activeEffects.remove(uuid);
    }

    // Status-Methoden
    public void setStatus(Player player, String status) { playerStatus.put(player.getUniqueId(), status); updatePlayerDisplays(player); saveData(); }
    public String getStatus(Player player) { return playerStatus.getOrDefault(player.getUniqueId(), ""); }
    public void removeStatus(Player player) { playerStatus.remove(player.getUniqueId()); updatePlayerDisplays(player); saveData(); }

    private void initializeEffects() {
        // Consolidated effect registration using the simpler ParticleEffect(name, Particle, cost, permission, description)
        registerEffect("herzen", new ParticleEffect("Herzen", Particle.HEART, 0, "", "Umgibt dich mit schwebenden Herzen"));
        registerEffect("funken", new ParticleEffect("Funken", Particle.CRIT, 0, "", "Hinterlässt funkelnde Spuren"));

        // Premium effects
        registerEffect("feuerfluegel", new ParticleEffect("Feuerflügel", Particle.FLAME, 1000, "cosmetics.wings.fire", "Majestätische Feuerflügel"));
        registerEffect("wasserfluegel", new ParticleEffect("Wasserflügel", Particle.HAPPY_VILLAGER, 1000, "cosmetics.wings.water", "Glitzernde Wasserflügel"));
        registerEffect("enderkrone", new ParticleEffect("Enderkrone", Particle.PORTAL, 2000, "cosmetics.crown.ender", "Mystische Enderkrone"));
        registerEffect("eisspiral", new ParticleEffect("Eisspiral", Particle.CLOUD, 1500, "cosmetics.spiral.ice", "Wirbelnde Eispartikel"));
        registerEffect("notenschild", new ParticleEffect("Notenschild", Particle.NOTE, 1500, "cosmetics.shield.music", "Schild aus tanzenden Noten"));
        registerEffect("zaubervortex", new ParticleEffect("Zaubervortex", Particle.ENCHANT, 2500, "cosmetics.vortex.magic", "Magischer Wirbelsturm"));
        registerEffect("regenbogenaura", new ParticleEffect("Regenbogenaura", Particle.HAPPY_VILLAGER, 3000, "cosmetics.rainbow", "Schillernder Regenbogeneffekt"));
        registerEffect("galaxienebel", new ParticleEffect("Galaxienebel", Particle.END_ROD, 5000, "cosmetics.galaxy", "Kosmische Galaxienpartikel"));
    }

    private void registerEffect(String key, ParticleEffect effect) { availableEffects.put(key.toLowerCase(), effect); }

    // Nametag/Display Update
    private String colorize(String text) { return text == null ? "" : text.replace('&','§'); }

    public void updatePlayerDisplays(Player player) {
        // TODO: Implement proper PrefixMap and NickMap interfaces
        // String prefix = ((Map<String, String>) plugin.getPrefixMap()).getOrDefault(player.getUniqueId().toString(), "");
        // String nickname = ((Map<String, String>) plugin.getNickMap()).getOrDefault(player.getUniqueId().toString(), player.getName());
        String prefix = "";
        String nickname = player.getName();
        String status = getStatus(player);
        String teamName = "nt_" + player.getUniqueId().toString().substring(0, 8);
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = teams.get(teamName);
        if (team != null) team.unregister();
        team = scoreboard.registerNewTeam(teamName);
        teams.put(teamName, team);
        team.prefix(Component.text(colorize(prefix)));
        team.displayName(Component.text(nickname));
        team.suffix(Component.text(colorize(status)));
        team.addEntry(player.getName());
    }

    public void updateAllPlayers() { for (Player player : Bukkit.getOnlinePlayers()) updatePlayerDisplays(player); }

    // Getter
    public Collection<ParticleEffect> getAvailableEffects() { return availableEffects.values(); }
    public String getActiveEffect(Player player) { return activeEffects.get(player.getUniqueId()); }
    public void cleanup() { activeTasks.values().forEach(BukkitRunnable::cancel); activeTasks.clear(); activeEffects.clear(); teams.values().forEach(Team::unregister); teams.clear(); }
}
