package de.noctivag.skyblock;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import de.noctivag.skyblock.utils.ColorUtils;

/**
 * Öffentliche API für andere Plugins
 */
public class PluginAPI {
    private static SkyblockPlugin SkyblockPlugin;
    private static PluginAPI instance;

    private PluginAPI(SkyblockPlugin SkyblockPlugin) {
        PluginAPI.SkyblockPlugin = SkyblockPlugin;
    }

    public static PluginAPI getInstance() {
        if (instance == null) {
            throw new IllegalStateException("API wurde noch nicht initialisiert!");
        }
        return instance;
    }

    static void init(SkyblockPlugin SkyblockPlugin) {
        if (instance != null) {
            throw new IllegalStateException("API wurde bereits initialisiert!");
        }
        instance = new PluginAPI(SkyblockPlugin);
    }

    // Prefix Methoden
    public void setPrefix(Player player, String prefix) {
        // TODO: Implement proper PrefixMap interface
        // ((Map<String, String>) SkyblockPlugin.getPrefixMap()).put(player.getName(), prefix);
        updatePlayerDisplay(player);
    }

    public String getPrefix(Player player) {
        // TODO: Implement proper PrefixMap interface
        // return ((Map<String, String>) SkyblockPlugin.getPrefixMap()).getOrDefault(player.getName(), "");
        return "";
    }

    public void removePrefix(Player player) {
        // TODO: Implement proper PrefixMap interface
        // ((Map<String, String>) SkyblockPlugin.getPrefixMap()).remove(player.getName());
        updatePlayerDisplay(player);
    }

    // Nickname Methoden
    public void setNickname(Player player, String nickname) {
        // TODO: Implement proper NickMap interface
        // ((Map<String, String>) SkyblockPlugin.getNickMap()).put(player.getName(), nickname);
        updatePlayerDisplay(player);
    }

    public String getNickname(Player player) {
        // TODO: Implement proper NickMap interface
        // return ((Map<String, String>) SkyblockPlugin.getNickMap()).getOrDefault(player.getName(), player.getName());
        return player.getName();
    }

    public void removeNickname(Player player) {
        // TODO: Implement proper NickMap interface
        // ((Map<String, String>) SkyblockPlugin.getNickMap()).remove(player.getName());
        updatePlayerDisplay(player);
    }

    // Join Message Methoden
    public void setJoinMessage(Player player, String message) {
        // TODO: Implement proper JoinMessageManager interface
        // ((JoinMessageManager) SkyblockPlugin.getJoinMessageManager()).setCustomMessage(player.getName(), message);
    }

    public void removeJoinMessage(Player player) {
        // TODO: Implement proper JoinMessageManager interface
        // ((JoinMessageManager) SkyblockPlugin.getJoinMessageManager()).removeCustomMessage(player.getName());
    }

    public void setJoinMessageEnabled(Player player, boolean enabled) {
        // TODO: Implement proper JoinMessageManager interface
        // ((JoinMessageManager) SkyblockPlugin.getJoinMessageManager()).setMessageEnabled(player.getName(), enabled);
    }

    // Hilfsmethoden
    private void updatePlayerDisplay(Player player) {
        String prefix = getPrefix(player);
        String nick = getNickname(player);
        Component displayName = Component.empty()
                .append(ColorUtils.translate(prefix))
                .append(Component.space())
                .append(ColorUtils.translate(nick));
        player.displayName(displayName);
        player.playerListName(displayName);
    }
}
