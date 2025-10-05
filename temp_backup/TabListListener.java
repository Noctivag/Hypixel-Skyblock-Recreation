package de.noctivag.skyblock;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.utils.ColorUtils;
import de.noctivag.skyblock.messages.JoinMessageManager;
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
    private final SkyblockPlugin SkyblockPlugin;

    public TabListListener(@NotNull SkyblockPlugin SkyblockPlugin, @NotNull Map<String, String> prefixMap,
                           @NotNull Map<String, String> nickMap, @NotNull JoinMessageManager joinMessageManager) {
        this.SkyblockPlugin = Objects.requireNonNull(SkyblockPlugin, "SkyblockPlugin cannot be null");
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
            SkyblockPlugin.getServer().getScheduler().runTask(SkyblockPlugin, () -> {
                try {
                    if (player.isOnline()) {
                        player.displayName(displayName);
                        player.playerListName(displayName);
                    }
                } catch (Exception e) {
                    SkyblockPlugin.getLogger().warning("Fehler beim Setzen des Displaynamens für " + playerName);
                }
            });

            // Setze die Join-Nachricht (nur wenn nicht leer)
            String joinMsgRaw = joinMessageManager.getJoinMessage(player);
            if (joinMsgRaw != null && !joinMsgRaw.isEmpty()) {
                Component joinMessage = ColorUtils.translate(joinMsgRaw);
                event.joinMessage(joinMessage);
            }
        } catch (Exception e) {
            SkyblockPlugin.getLogger().severe("Fehler im TabListListener: " + e.getMessage());
            try {
                // TODO: Implement proper ConfigManager interface
                // if (SkyblockPlugin.getConfigManager() != null && ((ConfigManager) SkyblockPlugin.getConfigManager()).isDebugMode()) {
                if (false) { // Placeholder for debug mode check
                    SkyblockPlugin.getLogger().log(Level.SEVERE, "Stacktrace:", e);
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

            SkyblockPlugin.getServer().getScheduler().runTask(SkyblockPlugin, () -> {
                try {
                    if (player.isOnline()) {
                        player.displayName(displayName);
                        player.playerListName(displayName);
                    }
                } catch (Exception e) {
                    SkyblockPlugin.getLogger().warning("Fehler beim Aktualisieren des Displaynamens für " + playerName);
                }
            });
        } catch (Exception e) {
            SkyblockPlugin.getLogger().severe("Fehler beim Aktualisieren der Spieleranzeige: " + e.getMessage());
        }
    }
}
