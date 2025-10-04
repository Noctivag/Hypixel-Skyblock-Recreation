package de.noctivag.skyblock.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.gui.NPCManagementGUI;
import de.noctivag.skyblock.npcs.AdvancedNPCSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NPCCommands implements CommandExecutor, TabCompleter {
    private final SkyblockPlugin plugin;

    public NPCCommands(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        if (!player.hasPermission("basicsplugin.npcs")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        switch (command.getName().toLowerCase()) {
            case "npcs" -> handleNPCsCommand(player, args);
            case "npctool" -> handleNPCToolCommand(player, args);
        }

        return true;
    }

    private void handleNPCsCommand(Player player, String[] args) {
        if (args.length == 0) {
            // Open NPC Management GUI
            new NPCManagementGUI(plugin, player).open(player);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "list" -> {
                AdvancedNPCSystem npcSystem = plugin.getAdvancedNPCSystem();
                player.sendMessage("§6=== Active NPCs ===");
                npcSystem.getActiveNPCs().forEach((id, npc) -> {
                    player.sendMessage("§7- §e" + npc.getDisplayName() + " §7(ID: " + id + ")");
                });
            }
            case "reload" -> {
                if (player.hasPermission("basicsplugin.admin")) {
                    // Reload NPCs from database
                    player.sendMessage("§aNPCs reloaded from database!");
                } else {
                    player.sendMessage("§cYou don't have permission to reload NPCs!");
                }
            }
            case "remove" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /npcs remove <npc_id>");
                    return;
                }
                
                if (player.hasPermission("basicsplugin.admin")) {
                    AdvancedNPCSystem npcSystem = plugin.getAdvancedNPCSystem();
                    npcSystem.removeNPC(args[1]);
                    player.sendMessage("§aNPC removed successfully!");
                } else {
                    player.sendMessage("§cYou don't have permission to remove NPCs!");
                }
            }
            case "help" -> {
                player.sendMessage("§6=== NPC Commands ===");
                player.sendMessage("§7/npcs - Open NPC Management GUI");
                player.sendMessage("§7/npcs list - List all active NPCs");
                player.sendMessage("§7/npcs reload - Reload NPCs from database");
                player.sendMessage("§7/npcs remove <id> - Remove specific NPC");
                player.sendMessage("§7/npctool - Get NPC creation tool");
            }
            default -> {
                player.sendMessage("§cUnknown subcommand! Use /npcs help for help.");
            }
        }
    }

    private void handleNPCToolCommand(Player player, String[] args) {
        if (!player.hasPermission("basicsplugin.npctool")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return;
        }

        AdvancedNPCSystem npcSystem = plugin.getAdvancedNPCSystem();
        player.getInventory().addItem(npcSystem.createNPCTool());
        player.sendMessage("§aNPC Tool added to your inventory!");
        player.sendMessage("§7Right-click on a block to place an NPC");
        player.sendMessage("§7Left-click on an NPC to manage it");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("npcs")) {
            if (args.length == 1) {
                List<String> subcommands = Arrays.asList("list", "reload", "remove", "help");
                for (String subcommand : subcommands) {
                    if (subcommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                        completions.add(subcommand);
                    }
                }
            } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
                // Add NPC IDs for completion
                AdvancedNPCSystem npcSystem = plugin.getAdvancedNPCSystem();
                completions.addAll(npcSystem.getActiveNPCs().keySet());
            }
        }

        return completions;
    }
}
