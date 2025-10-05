package de.noctivag.skyblock.mining;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;

/**
 * Heart of the Mountain Command System - Hypixel Skyblock Style
 */
public class HeartOfTheMountainCommand implements CommandExecutor, TabCompleter {
    private final SkyblockPlugin SkyblockPlugin;
    private final HeartOfTheMountainSystem heartSystem;

    public HeartOfTheMountainCommand(SkyblockPlugin SkyblockPlugin, HeartOfTheMountainSystem heartSystem) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.heartSystem = heartSystem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }

        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "upgrade":
                handleUpgrade(player, args);
                break;
            case "stats":
                handleStats(player, args);
                break;
            case "gui":
                handleGUI(player, args);
                break;
            case "challenge":
                handleChallenge(player, args);
                break;
            case "rewards":
                handleRewards(player, args);
                break;
            case "reset":
                handleReset(player, args);
                break;
            default:
                showHelp(player);
                break;
        }

        return true;
    }

    private void handleUpgrade(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("§cVerwendung: /heartofthemountain upgrade <upgrade-type>"));
            return;
        }

        String upgradeType = args[1].toUpperCase();
        try {
            HeartOfTheMountainSystem.HeartUpgradeType type = HeartOfTheMountainSystem.HeartUpgradeType.valueOf(upgradeType);
            if (heartSystem.upgradeHeart(player, type)) {
                player.sendMessage("§aHeart of the Mountain Upgrade '" + type.getDisplayName() + "' erfolgreich!");
            } else {
                player.sendMessage(Component.text("§cUpgrade konnte nicht durchgeführt werden!"));
            }
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cUngültiger Upgrade-Typ: " + upgradeType);
        }
    }

    private void handleStats(Player player, String[] args) {
        HeartOfTheMountainSystem.HeartStats stats = heartSystem.getPlayerStats(player);
        
        player.sendMessage(Component.text("§6§l=== HEART OF THE MOUNTAIN STATISTIKEN ==="));
        player.sendMessage("§7Level: §a" + stats.getLevel());
        player.sendMessage("§7XP: §a" + stats.getXp() + "§7/§a" + stats.getXpToNextLevel());
        player.sendMessage("§7Upgrades: §a" + stats.getUpgrades().size());
        player.sendMessage("§7Mining Speed: §a+" + stats.getMiningSpeedBonus() + "%");
        player.sendMessage("§7Fortune Bonus: §a+" + stats.getFortuneBonus() + "%");
        player.sendMessage("§7Titanium Chance: §a+" + stats.getTitaniumChance() + "%");
    }

    private void handleGUI(Player player, String[] args) {
        heartSystem.openHeartGUI(player);
    }

    private void handleChallenge(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("§cVerwendung: /heartofthemountain challenge <challenge-type>"));
            return;
        }

        String challengeType = args[1].toUpperCase();
        try {
            HeartOfTheMountainSystem.HeartChallengeType type = HeartOfTheMountainSystem.HeartChallengeType.valueOf(challengeType);
            if (heartSystem.startChallenge(player, type)) {
                player.sendMessage("§aChallenge '" + type.getDisplayName() + "' gestartet!");
            } else {
                player.sendMessage(Component.text("§cChallenge konnte nicht gestartet werden!"));
            }
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cUngültiger Challenge-Typ: " + challengeType);
        }
    }

    private void handleRewards(Player player, String[] args) {
        List<HeartOfTheMountainSystem.HeartReward> rewards = heartSystem.getAvailableRewards(player);
        
        player.sendMessage(Component.text("§6§l=== VERFÜGBARE BELOHNUNGEN ==="));
        if (rewards.isEmpty()) {
            player.sendMessage(Component.text("§7Keine Belohnungen verfügbar."));
        } else {
            for (HeartOfTheMountainSystem.HeartReward reward : rewards) {
                player.sendMessage("§7- " + reward.getName() + " §8- " + reward.getDescription());
            }
        }
    }

    private void handleReset(Player player, String[] args) {
        if (heartSystem.resetHeart(player)) {
            player.sendMessage(Component.text("§aHeart of the Mountain zurückgesetzt!"));
        } else {
            player.sendMessage(Component.text("§cReset konnte nicht durchgeführt werden!"));
        }
    }

    private void showHelp(Player player) {
        player.sendMessage(Component.text("§6§l=== HEART OF THE MOUNTAIN BEFEHLE ==="));
        player.sendMessage(Component.text("§e/heartofthemountain upgrade <type> §7- Upgradet dein Heart of the Mountain"));
        player.sendMessage(Component.text("§e/heartofthemountain stats §7- Zeigt deine Statistiken"));
        player.sendMessage(Component.text("§e/heartofthemountain gui §7- Öffnet das Heart GUI"));
        player.sendMessage(Component.text("§e/heartofthemountain challenge <type> §7- Startet eine Challenge"));
        player.sendMessage(Component.text("§e/heartofthemountain rewards §7- Zeigt verfügbare Belohnungen"));
        player.sendMessage(Component.text("§e/heartofthemountain reset §7- Setzt dein Heart zurück"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Arrays.asList("upgrade", "stats", "gui", "challenge", "rewards", "reset"));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("upgrade")) {
                for (HeartOfTheMountainSystem.HeartUpgradeType type : HeartOfTheMountainSystem.HeartUpgradeType.values()) {
                    completions.add(type.name().toLowerCase());
                }
            } else if (args[0].equalsIgnoreCase("challenge")) {
                for (HeartOfTheMountainSystem.HeartChallengeType type : HeartOfTheMountainSystem.HeartChallengeType.values()) {
                    completions.add(type.name().toLowerCase());
                }
            }
        }

        return completions;
    }
}
