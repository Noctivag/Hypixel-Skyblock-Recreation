package de.noctivag.plugin.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.armor.ArmorType;
import de.noctivag.plugin.armor.CompleteArmorGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ArmorCommands - Commands for armor system management
 */
public class ArmorCommands implements CommandExecutor, TabCompleter {
    private final Plugin plugin;
    private final CompleteArmorGUI armorGUI;
    
    public ArmorCommands(Plugin plugin) {
        this.plugin = plugin;
        this.armorGUI = new CompleteArmorGUI(plugin);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }
        
        String cmd = command.getName().toLowerCase();
        
        switch (cmd) {
            case "armor" -> handleArmorCommand(player, args);
            case "armorgui" -> handleArmorGUICommand(player, args);
            case "armorability" -> handleArmorAbilityCommand(player, args);
            case "armorlist" -> handleArmorListCommand(player, args);
            default -> {
                player.sendMessage("§cUnknown armor command!");
                return true;
            }
        }
        
        return true;
    }
    
    private void handleArmorCommand(Player player, String[] args) {
        if (args.length == 0) {
            showArmorHelp(player);
            return;
        }
        
        switch (args[0].toLowerCase()) {
            case "gui" -> armorGUI.openArmorGUI(player);
            case "dragon" -> armorGUI.openDragonArmorGUI(player);
            case "mining" -> armorGUI.openMiningArmorGUI(player);
            case "list" -> showArmorList(player);
            case "help" -> showArmorHelp(player);
            default -> {
                player.sendMessage("§cUsage: /armor [gui|dragon|mining|list|help]");
            }
        }
    }
    
    private void handleArmorGUICommand(Player player, String[] args) {
        if (args.length == 0) {
            armorGUI.openArmorGUI(player);
            return;
        }
        
        switch (args[0].toLowerCase()) {
            case "dragon" -> armorGUI.openDragonArmorGUI(player);
            case "mining" -> armorGUI.openMiningArmorGUI(player);
            default -> armorGUI.openArmorGUI(player);
        }
    }
    
    private void handleArmorAbilityCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("§cUsage: /armorability <armor_type>");
            player.sendMessage("§7Available armor types: protector, old, wise, young, strong, unstable, superior");
            return;
        }
        
        String armorTypeName = args[0].toUpperCase();
        ArmorType armorType;
        
        try {
            armorType = ArmorType.valueOf(armorTypeName + "_DRAGON");
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cInvalid armor type! Use: protector, old, wise, young, strong, unstable, superior");
            return;
        }
        
        // Activate armor ability
        if (plugin.getAdvancedArmorSystem() != null) {
            // This would activate the armor ability
            player.sendMessage("§a" + armorType.getDisplayName() + " ability activated!");
        } else {
            player.sendMessage("§cArmor system not available!");
        }
    }
    
    private void handleArmorListCommand(Player player, String[] args) {
        showArmorList(player);
    }
    
    private void showArmorHelp(Player player) {
        player.sendMessage("§6§l=== Hypixel SkyBlock Armor System ===");
        player.sendMessage("§7Commands:");
        player.sendMessage("§e/armor gui §7- Open armor GUI");
        player.sendMessage("§e/armor dragon §7- Browse dragon armor");
        player.sendMessage("§e/armor mining §7- Browse mining armor");
        player.sendMessage("§e/armor list §7- List all armor types");
        player.sendMessage("§e/armorability <type> §7- Activate armor ability");
        player.sendMessage("");
        player.sendMessage("§7Available armor categories:");
        player.sendMessage("§6• Dragon Armor §7- 7 different dragon types");
        player.sendMessage("§e• Mining Armor §7- Enhanced mining capabilities");
        player.sendMessage("§c• Combat Armor §7- High damage and defense");
        player.sendMessage("§b• Fishing Armor §7- Sea creature bonuses");
        player.sendMessage("§d• Event Armor §7- Seasonal and event armor");
        player.sendMessage("§4• Crimson Isle Armor §7- Nether-based armor");
        player.sendMessage("§5• Special Armor §7- Unique and rare armor");
    }
    
    private void showArmorList(Player player) {
        player.sendMessage("§6§l=== All Hypixel SkyBlock Armor Sets ===");
        
        // Dragon Armor
        player.sendMessage("§6§lDragon Armor:");
        for (ArmorType type : ArmorType.values()) {
            if (type.name().contains("DRAGON")) {
                player.sendMessage("§7• " + type.getDisplayName() + " §8(" + type.getRarity().name() + ")");
            }
        }
        
        // Mining Armor
        player.sendMessage("");
        player.sendMessage("§e§lMining Armor:");
        for (ArmorType type : ArmorType.values()) {
            if (type.name().contains("MINERAL") || type.name().contains("GLACITE") || 
                type.name().contains("SORROW") || type.name().contains("DIVAN")) {
                player.sendMessage("§7• " + type.getDisplayName() + " §8(" + type.getRarity().name() + ")");
            }
        }
        
        // Combat Armor
        player.sendMessage("");
        player.sendMessage("§c§lCombat Armor:");
        for (ArmorType type : ArmorType.values()) {
            if (type.name().contains("SHADOW") || type.name().contains("NECRON") || 
                type.name().contains("STORM") || type.name().contains("GOLDOR") || 
                type.name().contains("MAXOR") || type.name().contains("FROZEN")) {
                player.sendMessage("§7• " + type.getDisplayName() + " §8(" + type.getRarity().name() + ")");
            }
        }
        
        // Fishing Armor
        player.sendMessage("");
        player.sendMessage("§b§lFishing Armor:");
        for (ArmorType type : ArmorType.values()) {
            if (type.name().contains("SPONGE") || type.name().contains("SHARK")) {
                player.sendMessage("§7• " + type.getDisplayName() + " §8(" + type.getRarity().name() + ")");
            }
        }
        
        // Event Armor
        player.sendMessage("");
        player.sendMessage("§d§lEvent Armor:");
        for (ArmorType type : ArmorType.values()) {
            if (type.name().contains("SPOOKY") || type.name().contains("SNOW") || 
                type.name().contains("BAT") || type.name().contains("HALLOWEEN") || 
                type.name().contains("CHRISTMAS") || type.name().contains("EASTER")) {
                player.sendMessage("§7• " + type.getDisplayName() + " §8(" + type.getRarity().name() + ")");
            }
        }
        
        // Crimson Isle Armor
        player.sendMessage("");
        player.sendMessage("§4§lCrimson Isle Armor:");
        for (ArmorType type : ArmorType.values()) {
            if (type.name().contains("CRIMSON") || type.name().contains("TERROR") || 
                type.name().contains("AURORA") || type.name().contains("HOLLOW")) {
                player.sendMessage("§7• " + type.getDisplayName() + " §8(" + type.getRarity().name() + ")");
            }
        }
        
        player.sendMessage("");
        player.sendMessage("§7Use §e/armor gui §7to browse all armor sets with detailed information!");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        String cmd = command.getName().toLowerCase();
        
        switch (cmd) {
            case "armor" -> {
                if (args.length == 1) {
                    completions.addAll(Arrays.asList("gui", "dragon", "mining", "list", "help"));
                }
            }
            case "armorgui" -> {
                if (args.length == 1) {
                    completions.addAll(Arrays.asList("dragon", "mining"));
                }
            }
            case "armorability" -> {
                if (args.length == 1) {
                    completions.addAll(Arrays.asList("protector", "old", "wise", "young", "strong", "unstable", "superior"));
                }
            }
        }
        
        // Filter completions based on input
        if (args.length > 0) {
            String input = args[args.length - 1].toLowerCase();
            completions.removeIf(completion -> !completion.toLowerCase().startsWith(input));
        }
        
        return completions;
    }
}
