package de.noctivag.plugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.utils.ColorUtils;
import de.noctivag.plugin.messages.JoinMessageManager;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class TabListListener implements Listener {
    private final Map<String, String> prefixMap;
    private final Map<String, String> nickMap;
    private final JoinMessageManager joinMessageManager;
    private final Plugin plugin;

    public TabListListener(@NotNull Plugin plugin, @NotNull Map<String, String> prefixMap,
                           @NotNull Map<String, String> nickMap, @NotNull JoinMessageManager joinMessageManager) {
        this.plugin = Objects.requireNonNull(plugin, "Plugin cannot be null");
        this.prefixMap = Objects.requireNonNull(prefixMap, "prefixMap cannot be null");
        this.nickMap = Objects.requireNonNull(nickMap, "nickMap cannot be null");
        this.joinMessageManager = Objects.requireNonNull(joinMessageManager, "JoinMessageManager cannot be null");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        try {
            Player player = event.getPlayer();

            String playerName = player.getName();
            String prefix = prefixMap.getOrDefault(playerName, "");
            String nick = nickMap.getOrDefault(playerName, playerName);

            // Erstelle den DisplayName nur einmal
            Component displayName = Component.empty()
                    .append(ColorUtils.translate(prefix))
                    .append(Component.space())
                    .append(ColorUtils.translate(nick));

            // Setze den DisplayName und TabList-Namen synchron
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                try {
                    if (player.isOnline()) {
                        player.displayName(displayName);
                        player.playerListName(displayName);
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("Fehler beim Setzen des Displaynamens für " + playerName);
                }
            });

            // Setze die Join-Nachricht (nur wenn nicht leer)
            String joinMsgRaw = joinMessageManager.getJoinMessage(player);
            if (joinMsgRaw != null && !joinMsgRaw.isEmpty()) {
                Component joinMessage = ColorUtils.translate(joinMsgRaw);
                event.joinMessage(joinMessage);
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Fehler im TabListListener: " + e.getMessage());
            try {
                // TODO: Implement proper ConfigManager interface
                // if (plugin.getConfigManager() != null && ((ConfigManager) plugin.getConfigManager()).isDebugMode()) {
                if (false) { // Placeholder for debug mode check
                    plugin.getLogger().log(Level.SEVERE, "Stacktrace:", e);
                }
            } catch (Exception ignored) {
                // Nothing we can do here
            }
        }
    }

    /**
     * Aktualisiert den Tab-Listen-Eintrag für einen Spieler
     *
     * @param player Der Spieler, dessen Anzeige aktualisiert werden soll
     */
    @SuppressWarnings("unused")
    public void updatePlayerDisplay(@NotNull Player player) {
        try {
            String playerName = player.getName();
            String prefix = prefixMap.getOrDefault(playerName, "");
            String nick = nickMap.getOrDefault(playerName, playerName);

            Component displayName = Component.empty()
                    .append(ColorUtils.translate(prefix))
                    .append(Component.space())
                    .append(ColorUtils.translate(nick));

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                try {
                    if (player.isOnline()) {
                        player.displayName(displayName);
                        player.playerListName(displayName);
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("Fehler beim Aktualisieren des Displaynamens für " + playerName);
                }
            });
        } catch (Exception e) {
            plugin.getLogger().severe("Fehler beim Aktualisieren der Spieleranzeige: " + e.getMessage());
        }
    }
}
