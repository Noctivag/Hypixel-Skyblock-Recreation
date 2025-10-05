package de.noctivag.skyblock.commands;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.gui.MiningGUI;
import de.noctivag.skyblock.skyblock.MiningAreaSystem;
import de.noctivag.skyblock.skyblock.SkyblockManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;

public class MiningCommand implements CommandExecutor, TabCompleter {
    private final SkyblockPluginRefactored SkyblockPlugin;

    public MiningCommand(SkyblockPluginRefactored SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden.");
            return true;
        }

        if (args.length == 0) {
            // Open mining GUI
            new MiningGUI(SkyblockPlugin, player).open(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "gui" -> new MiningGUI(SkyblockPlugin, player).open(player);

            case "teleport", "tp" -> {
                if (args.length < 2) {
                    player.sendMessage(Component.text("§cUsage: /mining teleport <area>"));
                    return true;
                }

                String areaId = args[1].toLowerCase();
                MiningAreaSystem mining = SkyblockPlugin.getMiningAreaSystem();
                if (mining == null) {
                    player.sendMessage(Component.text("§cMining system not available."));
                    return true;
                }

                MiningAreaSystem.MiningArea area = mining.getMiningArea(areaId);

                if (area == null) {
                    player.sendMessage("§cUnknown mining area: " + areaId);
                    return true;
                }

                // Check if player has required level
                SkyblockManager sky = SkyblockPlugin.getSkyblockManager();
                if (sky == null) {
                    player.sendMessage(Component.text("§cSkyblock system not available."));
                    return true;
                }

                var skills = sky.getSkills(player.getUniqueId());
                int playerLevel = skills.getLevel(SkyblockManager.SkyblockSkill.MINING);
                int requiredLevel = getRequiredLevelForArea(area);

                if (playerLevel < requiredLevel) {
                    player.sendMessage("§cYou need Mining Level " + requiredLevel + " to access this area!");
                    player.sendMessage("§7Your Mining Level: §e" + playerLevel);
                    return true;
                }

                // Teleport to area
                teleportToArea(player, area);
            }

            case "list" -> {
                player.sendMessage(Component.text("§6§lAvailable Mining Areas:"));
                MiningAreaSystem mining = SkyblockPlugin.getMiningAreaSystem();
                if (mining == null) {
                    player.sendMessage(Component.text("§cMining system not available."));
                    return true;
                }

                Map<String, MiningAreaSystem.MiningArea> areas = mining.getAllMiningAreas();

                SkyblockManager sky = SkyblockPlugin.getSkyblockManager();
                for (Map.Entry<String, MiningAreaSystem.MiningArea> entry : areas.entrySet()) {
                    MiningAreaSystem.MiningArea area = entry.getValue();

                    int playerLevel = 0;
                    int requiredLevel = getRequiredLevelForArea(area);
                    if (sky != null) {
                        var skills = sky.getSkills(player.getUniqueId());
                        playerLevel = skills.getLevel(SkyblockManager.SkyblockSkill.MINING);
                    }

                    boolean canAccess = playerLevel >= requiredLevel;
                    String status = canAccess ? "§a§l✓" : "§c§l✗";

                    player.sendMessage("§7• " + status + " §e" + area.getName() + " §7(Level " + requiredLevel + ")");
                }
            }

            case "stats" -> {
                SkyblockManager sky = SkyblockPlugin.getSkyblockManager();
                if (sky == null) {
                    player.sendMessage(Component.text("§cSkyblock system not available."));
                    return true;
                }

                var skills = sky.getSkills(player.getUniqueId());
                int miningLevel = skills.getLevel(SkyblockManager.SkyblockSkill.MINING);
                double miningXP = skills.getXP(SkyblockManager.SkyblockSkill.MINING);
                double xpToNext = skills.getXPToNextLevel(SkyblockManager.SkyblockSkill.MINING);

                player.sendMessage(Component.text("§6§lYour Mining Stats:"));
                player.sendMessage("§7Mining Level: §e" + miningLevel);
                player.sendMessage("§7Mining XP: §e" + miningXP);
                player.sendMessage("§7XP to Next Level: §e" + xpToNext);

                MiningAreaSystem mining = SkyblockPlugin.getMiningAreaSystem();
                if (mining == null) {
                    player.sendMessage(Component.text("§7Current Area: §cUnknown (mining system unavailable)"));
                    return true;
                }

                // The API returns a MiningArea object for the player's current area.
                MiningAreaSystem.MiningArea currentArea = mining.getPlayerCurrentArea(player.getUniqueId());
                if (currentArea != null) {
                    player.sendMessage("§7Current Area: §e" + currentArea.getName());
                } else {
                    player.sendMessage(Component.text("§7Current Area: §cNone"));
                }
            }

            case "help" -> {
                player.sendMessage(Component.text("§6§lMining Command Help:"));
                player.sendMessage(Component.text("§7/mining §e- Open mining GUI"));
                player.sendMessage(Component.text("§7/mining gui §e- Open mining GUI"));
                player.sendMessage(Component.text("§7/mining teleport <area> §e- Teleport to mining area"));
                player.sendMessage(Component.text("§7/mining list §e- List all mining areas"));
                player.sendMessage(Component.text("§7/mining stats §e- Show your mining stats"));
                player.sendMessage(Component.text("§7/mining help §e- Show this help"));
            }
            default -> {
                player.sendMessage("§cUnknown subcommand: " + subCommand);
                player.sendMessage(Component.text("§7Use §e/mining help §7for help"));
            }
        }

        return true;
    }

    private int getRequiredLevelForArea(MiningAreaSystem.MiningArea area) {
        // Get the highest required level for any block in this area
        int maxLevel = 0;
        for (int level : area.getRequiredLevels().values()) {
            if (level > maxLevel) {
                maxLevel = level;
            }
        }
        return maxLevel;
    }

    private void teleportToArea(Player player, MiningAreaSystem.MiningArea area) {
        // Teleport to the center of the mining area (defensive and precise)
        Location min = area.getMinBound();
        Location max = area.getMaxBound();

        // Start from a safe clone of min
        Location center = min.clone();
        if (center.getWorld() == null) {
            // fallback to player's world
            center.setWorld(player.getWorld());
        }

        double centerX = (min.getX() + max.getX()) / 2.0;
        double centerZ = (min.getZ() + max.getZ()) / 2.0;
        double centerY = Math.max(min.getY(), Math.min(max.getY(), min.getY() + 1.0));

        center.setX(centerX);
        center.setY(centerY);
        center.setZ(centerZ);

        try {
            player.teleport(center);
            player.sendMessage(Component.text("§a§lTELEPORTED TO MINING AREA!"));
            player.sendMessage("§7Area: §e" + area.getName());
            player.sendMessage("§7Location: §e" + center.getBlockX() + ", " + center.getBlockY() + ", " + center.getBlockZ());
        } catch (Exception e) {
            // If teleport fails for any reason, send user-friendly message
            player.sendMessage("§cTeleport fehlgeschlagen: " + e.getMessage());
            SkyblockPlugin.getLogger().warning("Failed to teleport player " + player.getName() + " to mining area " + area.getId() + ": " + e.getMessage());
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("gui", "teleport", "tp", "list", "stats", "help");
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp"))) {
            MiningAreaSystem mining = SkyblockPlugin.getMiningAreaSystem();
            if (mining == null) return new ArrayList<>();
            // return a mutable list of area ids (keys)
            return new ArrayList<>(mining.getAllMiningAreas().keySet());
        }
        return new ArrayList<>();
    }
}
