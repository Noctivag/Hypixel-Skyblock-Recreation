package de.noctivag.skyblock.display;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class NametagManager {
    private final SkyblockPlugin plugin;
    private final Map<UUID, String> playerStatus;
    private final Map<String, Team> teams;

    public NametagManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.playerStatus = new HashMap<>();
        this.teams = new HashMap<>();
    }

    public void updateNametag(Player player) {
        // TODO: Implement proper PrefixMap and NickMap interfaces
        // String prefix = ((Map<String, String>) plugin.getPrefixMap()).getOrDefault(player.getName(), "");
        // String nickname = ((Map<String, String>) plugin.getNickMap()).getOrDefault(player.getName(), player.getName());
        String prefix = "";
        String nickname = player.getName();
        String status = playerStatus.getOrDefault(player.getUniqueId(), "");

        // Create unique team name for player
        String teamName = "nt_" + player.getUniqueId().toString().substring(0, 8);
        Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();

        // Remove old team if exists
        Team oldTeam = teams.remove(teamName);
        if (oldTeam != null) {
            oldTeam.unregister();
        }

        // Create new team
        Team team = scoreboard.registerNewTeam(teamName);
        teams.put(teamName, team);

        // Set prefix
        if (!prefix.isEmpty()) {
            team.prefix(LegacyComponentSerializer.legacyAmpersand().deserialize(prefix + " "));
        }

        // Set display name
        player.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize(nickname));

        // Add status if present
        if (!status.isEmpty()) {
            Component statusComponent = Component.text(" [").color(NamedTextColor.GRAY)
                    .append(LegacyComponentSerializer.legacyAmpersand().deserialize(status))
                    .append(Component.text("]").color(NamedTextColor.GRAY));
            team.suffix(statusComponent);
        }

        // Add player to team
        team.addPlayer(player);
    }

    @NotNull
    public String getStatus(Player player) {
        return playerStatus.getOrDefault(player.getUniqueId(), "");
    }

    public void setStatus(Player player, String status) {
        playerStatus.put(player.getUniqueId(), status);
        updateNametag(player);
    }

    public void clearStatus(Player player) {
        playerStatus.remove(player.getUniqueId());
        updateNametag(player);
    }

    public void cleanup() {
        teams.values().forEach(Team::unregister);
        teams.clear();
        playerStatus.clear();
    }
}
