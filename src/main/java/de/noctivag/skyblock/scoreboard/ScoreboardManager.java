package de.noctivag.skyblock.scoreboard;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.utils.TPSUtil;
import de.noctivag.skyblock.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Objects;

public class ScoreboardManager {
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<UUID, Scoreboard> playerScoreboards;
    private final org.bukkit.scoreboard.ScoreboardManager bukkitScoreboardManager;

    public ScoreboardManager(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.playerScoreboards = new HashMap<>();
        this.bukkitScoreboardManager = Bukkit.getScoreboardManager();

        // TPS-Tracking moved to SkyblockPlugin.onEnable() to avoid IllegalPluginAccessException
    }

    @SuppressWarnings("deprecation")
    public void setScoreboard(Player player) {
        Scoreboard board = bukkitScoreboardManager.getNewScoreboard();
        // Use Adventure Component for the objective display name
        // TODO: Implement proper ConfigManager interface
        // Component titleComp = ColorUtils.translate(((ConfigManager) SkyblockPlugin.getConfigManager()).getScoreboardTitle());
        Component titleComp = ColorUtils.translate("§6§lSkyblock");
        // Use string key for scoreboard objective
        Objective obj = board.registerNewObjective("main", "dummy", titleComp);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        int score = 15;
        // TODO: Implement proper ConfigManager interface
        // List<String> configuredLines = ((ConfigManager) SkyblockPlugin.getConfigManager()).getScoreboardLines();
        List<String> configuredLines = Arrays.asList("§7Welcome to Skyblock!", "§7Your level: §e1", "§7Coins: §6$0");

        for (String line : configuredLines) {
            if (shouldShowLine(line)) {
                String processedLine = processPlaceholders(line, player);
                obj.getScore(processedLine).setScore(score--);
            }
        }

        player.setScoreboard(board);
        playerScoreboards.put(player.getUniqueId(), board);
    }

    private boolean shouldShowLine(String line) {
        // TODO: Implement proper ConfigManager interface
        // if (line.contains("{player_rank}") && !((ConfigManager) SkyblockPlugin.getConfigManager()).isScoreboardFeatureEnabled("show-rank")) {
        //     return false;
        // }
        // if (line.contains("{player_money}") && !((ConfigManager) SkyblockPlugin.getConfigManager()).isScoreboardFeatureEnabled("show-money")) {
        //     return false;
        // }
        // if (line.contains("{online_players}") && !((ConfigManager) SkyblockPlugin.getConfigManager()).isScoreboardFeatureEnabled("show-online-players")) {
        //     return false;
        // }
        // TODO: Implement proper ConfigManager interface
        // if (!((ConfigManager) SkyblockPlugin.getConfigManager()).isScoreboardFeatureEnabled("show-tps") && line.contains("{server_tps}")) {
        //     return false;
        // }
        // if (!((ConfigManager) SkyblockPlugin.getConfigManager()).isScoreboardFeatureEnabled("show-website") && line.contains("example.com")) {
        //     return false;
        // }
        return true;
    }

    private String processPlaceholders(String line, Player player) {
        String result = Objects.requireNonNullElse(line, "");
        // Replace '&' with section sign for legacy color support in scoreboard strings
        result = result.replace('&', '§');
        result = result.replace("{player_name}", Objects.toString(player.getName(), ""));
        // TODO: Implement proper RankManager interface
        // result = result.replace("{player_rank}", Objects.toString(((RankManager) SkyblockPlugin.getRankManager()).getPlayerRank(player), ""));
        result = result.replace("{player_rank}", "Default");
        result = result.replace("{player_money}", Objects.toString(SkyblockPlugin.getEconomyManager().formatMoney(SkyblockPlugin.getEconomyManager().getBalance(player)), ""));
        result = result.replace("{server_tps}", Objects.toString(TPSUtil.getFormattedTPS(), ""));
        result = result.replace("{online_players}", String.valueOf(Bukkit.getOnlinePlayers().size()));
        result = result.replace("{max_players}", String.valueOf(Bukkit.getMaxPlayers()));
        return result;
    }

    public void removeScoreboard(Player player) {
        playerScoreboards.remove(player.getUniqueId());
        player.setScoreboard(bukkitScoreboardManager.getNewScoreboard());
    }

    public void updateScoreboard(Player player) {
        if (playerScoreboards.containsKey(player.getUniqueId())) {
            setScoreboard(player);
        }
    }

    public void updateAllScoreboards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateScoreboard(player);
        }
    }
}
