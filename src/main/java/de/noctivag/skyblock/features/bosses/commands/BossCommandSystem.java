package de.noctivag.skyblock.features.bosses.commands;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.features.bosses.CompleteBossSystem;
import de.noctivag.skyblock.features.bosses.mechanics.BossMechanicsSystem;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;

/**
 * Boss Command System - Verwaltung aller Bosse
 * 
 * Commands:
 * /boss spawn <bossId> - Spawnt einen Boss
 * /boss list - Zeigt alle verfügbaren Bosse
 * /boss info <bossId> - Zeigt Boss-Informationen
 * /boss kill - Tötet alle aktiven Bosse
 * /boss mechanics <bossId> - Zeigt Boss-Mechaniken
 */
public class BossCommandSystem implements CommandExecutor, TabCompleter {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final CompleteBossSystem bossSystem;
    private final BossMechanicsSystem mechanicsSystem;
    
    public BossCommandSystem(SkyblockPlugin SkyblockPlugin, CompleteBossSystem bossSystem, BossMechanicsSystem mechanicsSystem) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.bossSystem = bossSystem;
        this.mechanicsSystem = mechanicsSystem;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("§cNur Spieler können diesen Befehl verwenden!")
                .color(NamedTextColor.RED));
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            sendHelpMessage(player);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "spawn" -> handleSpawnCommand(player, args);
            case "list" -> handleListCommand(player, args);
            case "info" -> handleInfoCommand(player, args);
            case "kill" -> handleKillCommand(player, args);
            case "mechanics" -> handleMechanicsCommand(player, args);
            case "help" -> sendHelpMessage(player);
            default -> {
                player.sendMessage(Component.text("§cUnbekannter Befehl! Verwende /boss help")
                    .color(NamedTextColor.RED));
            }
        }
        
        return true;
    }
    
    /**
     * Behandelt spawn Befehl
     */
    private void handleSpawnCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("§cVerwendung: /boss spawn <bossId>")
                .color(NamedTextColor.RED));
            return;
        }
        
        String bossId = args[1].toUpperCase();
        Location spawnLocation = player.getLocation();
        
        // Spawne Boss
        bossSystem.spawnBoss(bossId, spawnLocation, player);
        
        player.sendMessage(Component.text("§a§lBOSS GESPAWNT!")
            .color(NamedTextColor.GREEN));
        player.sendMessage(Component.text("§7§l» §6" + bossId + " wurde gespawnt!")
            .color(NamedTextColor.GOLD));
    }
    
    /**
     * Behandelt list Befehl
     */
    private void handleListCommand(Player player, String[] args) {
        player.sendMessage(Component.text("§6§l=== VERFÜGBARE BOSSE ===")
            .color(NamedTextColor.GOLD));
        
        // Weltbosse
        player.sendMessage(Component.text("§e§lWeltbosse:")
            .color(NamedTextColor.YELLOW));
        player.sendMessage(Component.text("§7• ENDER_DRAGON - Enderdrache (15M HP)")
            .color(NamedTextColor.GRAY));
        player.sendMessage(Component.text("§7• ARACHNE - Arachne (100K HP)")
            .color(NamedTextColor.GRAY));
        player.sendMessage(Component.text("§7• ENDSTONE_PROTECTOR - Endstein-Beschützer (5M HP)")
            .color(NamedTextColor.GRAY));
        
        // Slayer-Bosse
        player.sendMessage(Component.text("§c§lSlayer-Bosse:")
            .color(NamedTextColor.RED));
        player.sendMessage(Component.text("§7• REVENANT_T1-T5 - Revenant Horror (Zombie Slayer)")
            .color(NamedTextColor.GRAY));
        player.sendMessage(Component.text("§7• TARANTULA_T1-T4 - Tarantula Broodfather (Spider Slayer)")
            .color(NamedTextColor.GRAY));
        player.sendMessage(Component.text("§7• SVEN_T1-T4 - Sven Packmaster (Wolf Slayer)")
            .color(NamedTextColor.GRAY));
        player.sendMessage(Component.text("§7• VOIDGLOOM_T1-T4 - Voidgloom Seraph (Enderman Slayer)")
            .color(NamedTextColor.GRAY));
        player.sendMessage(Component.text("§7• BLAZE_T1-T4 - Inferno Demonlord (Blaze Slayer)")
            .color(NamedTextColor.GRAY));
        player.sendMessage(Component.text("§7• VAMPIRE_T1-T5 - Riftstalker Bloodfiend (Vampire Slayer)")
            .color(NamedTextColor.GRAY));
        
        // Drachen-Bosse
        player.sendMessage(Component.text("§5§lDrachen-Bosse:")
            .color(NamedTextColor.DARK_PURPLE));
        player.sendMessage(Component.text("§7• PROTECTOR, OLD, WISE, UNSTABLE, STRONG, YOUNG, SUPERIOR, HOLY")
            .color(NamedTextColor.GRAY));
        
        // Catacombs-Bosse
        player.sendMessage(Component.text("§9§lCatacombs-Bosse:")
            .color(NamedTextColor.BLUE));
        player.sendMessage(Component.text("§7• BONZO (F1), SCARF (F2), PROFESSOR (F3), THORN (F4)")
            .color(NamedTextColor.GRAY));
        player.sendMessage(Component.text("§7• LIVID (F5), SADAN (F6), NECRON (F7)")
            .color(NamedTextColor.GRAY));
        
        // Kuudra-Bosse
        player.sendMessage(Component.text("§4§lKuudra-Bosse:")
            .color(NamedTextColor.DARK_RED));
        player.sendMessage(Component.text("§7• BASIC_KUUDRA, HOT_KUUDRA, BURNING_KUUDRA")
            .color(NamedTextColor.GRAY));
        player.sendMessage(Component.text("§7• FIERY_KUUDRA, INFERNO_KUUDRA")
            .color(NamedTextColor.GRAY));
    }
    
    /**
     * Behandelt info Befehl
     */
    private void handleInfoCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("§cVerwendung: /boss info <bossId>")
                .color(NamedTextColor.RED));
            return;
        }
        
        String bossId = args[1].toUpperCase();
        
        // Hier würden die Boss-Informationen aus dem System geladen werden
        player.sendMessage(Component.text("§6§lBOSS INFORMATIONEN: " + bossId)
            .color(NamedTextColor.GOLD));
        player.sendMessage(Component.text("§7§l» §fBoss-ID: §e" + bossId)
            .color(NamedTextColor.WHITE));
        
        // Beispiel-Informationen basierend auf Boss-Typ
        if (bossId.startsWith("REVENANT_T")) {
            int tier = Integer.parseInt(bossId.substring(9));
            player.sendMessage(Component.text("§7§l» §fTyp: §2Zombie Slayer Boss")
                .color(NamedTextColor.WHITE));
            player.sendMessage(Component.text("§7§l» §fTier: §a" + tier)
                .color(NamedTextColor.WHITE));
            player.sendMessage(Component.text("§7§l» §fHP: §c" + getRevenantHealth(tier))
                .color(NamedTextColor.WHITE));
            player.sendMessage(Component.text("§7§l» §fSchaden: §c" + getRevenantDamage(tier))
                .color(NamedTextColor.WHITE));
        } else if (bossId.equals("ENDER_DRAGON")) {
            player.sendMessage(Component.text("§7§l» §fTyp: §5Weltboss")
                .color(NamedTextColor.WHITE));
            player.sendMessage(Component.text("§7§l» §fHP: §c15,000,000")
                .color(NamedTextColor.WHITE));
            player.sendMessage(Component.text("§7§l» §fSchaden: §c2,200")
                .color(NamedTextColor.WHITE));
            player.sendMessage(Component.text("§7§l» §fSpawn-Zeit: §b8 Stunden")
                .color(NamedTextColor.WHITE));
        } else if (bossId.startsWith("KUUDRABOSS")) {
            player.sendMessage(Component.text("§7§l» §fTyp: §4Kuudra Boss")
                .color(NamedTextColor.WHITE));
            player.sendMessage(Component.text("§7§l» §fStandort: §6Crimson Isles")
                .color(NamedTextColor.WHITE));
            player.sendMessage(Component.text("§7§l» §fTeamwork: §aErforderlich")
                .color(NamedTextColor.WHITE));
        }
    }
    
    /**
     * Behandelt kill Befehl
     */
    private void handleKillCommand(Player player, String[] args) {
        // Hier würde die Logik zum Töten aller aktiven Bosse implementiert
        player.sendMessage(Component.text("§c§lALLE BOSSE GETÖTET!")
            .color(NamedTextColor.RED));
        player.sendMessage(Component.text("§7§l» §fAlle aktiven Bosse wurden entfernt")
            .color(NamedTextColor.WHITE));
    }
    
    /**
     * Behandelt mechanics Befehl
     */
    private void handleMechanicsCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("§cVerwendung: /boss mechanics <bossId>")
                .color(NamedTextColor.RED));
            return;
        }
        
        String bossId = args[1].toUpperCase();
        
        player.sendMessage(Component.text("§6§lBOSS-MECHANIKEN: " + bossId)
            .color(NamedTextColor.GOLD));
        
        // Beispiel-Mechaniken basierend auf Boss-Typ
        if (bossId.equals("ENDER_DRAGON")) {
            player.sendMessage(Component.text("§7§l» §fPhase 1 (100-80% HP): §ePerch Phase")
                .color(NamedTextColor.WHITE));
            player.sendMessage(Component.text("§7§l  - Dragon Breath Angriff")
                .color(NamedTextColor.GRAY));
            player.sendMessage(Component.text("§7§l  - Spawnt Endermen")
                .color(NamedTextColor.GRAY));
            
            player.sendMessage(Component.text("§7§l» §fPhase 2 (80-50% HP): §eFlying Phase")
                .color(NamedTextColor.WHITE));
            player.sendMessage(Component.text("§7§l  - Fireball Angriff")
                .color(NamedTextColor.GRAY));
            player.sendMessage(Component.text("§7§l  - Dragon Charge")
                .color(NamedTextColor.GRAY));
            
            player.sendMessage(Component.text("§7§l» §fPhase 3 (50-20% HP): §ePerch Phase 2")
                .color(NamedTextColor.WHITE));
            player.sendMessage(Component.text("§7§l  - Verstärkte Angriffe")
                .color(NamedTextColor.GRAY));
            
            player.sendMessage(Component.text("§7§l» §fPhase 4 (20-0% HP): §eFinal Phase")
                .color(NamedTextColor.WHITE));
            player.sendMessage(Component.text("§7§l  - Alle Angriffe verstärkt")
                .color(NamedTextColor.GRAY));
        } else if (bossId.startsWith("REVENANT_T")) {
            player.sendMessage(Component.text("§7§l» §fPhase 1 (100-50% HP): §aNormal Phase")
                .color(NamedTextColor.WHITE));
            player.sendMessage(Component.text("§7§l  - Spawnt Zombies")
                .color(NamedTextColor.GRAY));
            player.sendMessage(Component.text("§7§l  - Heilt sich selbst")
                .color(NamedTextColor.GRAY));
            
            player.sendMessage(Component.text("§7§l» §fPhase 2 (50-0% HP): §cEnraged Phase")
                .color(NamedTextColor.WHITE));
            player.sendMessage(Component.text("§7§l  - Verstärkte Angriffe")
                .color(NamedTextColor.GRAY));
            player.sendMessage(Component.text("§7§l  - Schnellere Heilung")
                .color(NamedTextColor.GRAY));
        } else if (bossId.equals("LIVID")) {
            player.sendMessage(Component.text("§7§l» §fSpezial: §dLivid Klone")
                .color(NamedTextColor.WHITE));
            player.sendMessage(Component.text("§7§l  - Spawnt 4 Klone")
                .color(NamedTextColor.GRAY));
            player.sendMessage(Component.text("§7§l  - Nur echter Livid nimmt Schaden")
                .color(NamedTextColor.GRAY));
            player.sendMessage(Component.text("§7§l  - Teleportiert zu Spielern")
                .color(NamedTextColor.GRAY));
        }
    }
    
    /**
     * Sendet Hilfe-Nachricht
     */
    private void sendHelpMessage(Player player) {
        player.sendMessage(Component.text("§6§l=== BOSS COMMAND SYSTEM ===")
            .color(NamedTextColor.GOLD));
        player.sendMessage(Component.text("§7/boss spawn <bossId> §8- Spawnt einen Boss")
            .color(NamedTextColor.GRAY));
        player.sendMessage(Component.text("§7/boss list §8- Zeigt alle verfügbaren Bosse")
            .color(NamedTextColor.GRAY));
        player.sendMessage(Component.text("§7/boss info <bossId> §8- Zeigt Boss-Informationen")
            .color(NamedTextColor.GRAY));
        player.sendMessage(Component.text("§7/boss kill §8- Tötet alle aktiven Bosse")
            .color(NamedTextColor.GRAY));
        player.sendMessage(Component.text("§7/boss mechanics <bossId> §8- Zeigt Boss-Mechaniken")
            .color(NamedTextColor.GRAY));
        player.sendMessage(Component.text("§7/boss help §8- Zeigt diese Hilfe")
            .color(NamedTextColor.GRAY));
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.addAll(Arrays.asList("spawn", "list", "info", "kill", "mechanics", "help"));
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "spawn", "info", "mechanics" -> {
                    // Boss-IDs
                    completions.addAll(Arrays.asList(
                        "ENDER_DRAGON", "ARACHNE", "ENDSTONE_PROTECTOR",
                        "REVENANT_T1", "REVENANT_T2", "REVENANT_T3", "REVENANT_T4", "REVENANT_T5",
                        "TARANTULA_T1", "TARANTULA_T2", "TARANTULA_T3", "TARANTULA_T4",
                        "SVEN_T1", "SVEN_T2", "SVEN_T3", "SVEN_T4",
                        "VOIDGLOOM_T1", "VOIDGLOOM_T2", "VOIDGLOOM_T3", "VOIDGLOOM_T4",
                        "BLAZE_T1", "BLAZE_T2", "BLAZE_T3", "BLAZE_T4",
                        "VAMPIRE_T1", "VAMPIRE_T2", "VAMPIRE_T3", "VAMPIRE_T4", "VAMPIRE_T5",
                        "PROTECTOR", "OLD", "WISE", "UNSTABLE", "STRONG", "YOUNG", "SUPERIOR", "HOLY",
                        "BONZO", "SCARF", "PROFESSOR", "THORN", "LIVID", "SADAN", "NECRON",
                        "BASIC_KUUDRA", "HOT_KUUDRA", "BURNING_KUUDRA", "FIERY_KUUDRA", "INFERNO_KUUDRA"
                    ));
                }
            }
        }
        
        return completions;
    }
    
    // Helper-Methoden für Boss-Statistiken
    private int getRevenantHealth(int tier) {
        return switch (tier) {
            case 1 -> 100000;
            case 2 -> 500000;
            case 3 -> 2000000;
            case 4 -> 10000000;
            case 5 -> 50000000;
            default -> 100000;
        };
    }
    
    private int getRevenantDamage(int tier) {
        return switch (tier) {
            case 1 -> 200;
            case 2 -> 500;
            case 3 -> 1000;
            case 4 -> 2500;
            case 5 -> 5000;
            default -> 200;
        };
    }
}
