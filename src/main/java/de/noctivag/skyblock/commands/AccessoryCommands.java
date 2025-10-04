package de.noctivag.skyblock.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.accessories.AccessoryBagSystem;
import de.noctivag.skyblock.accessories.EnrichmentSystem;
import de.noctivag.skyblock.accessories.AccessoryIntegrationSystem;
import de.noctivag.skyblock.talismans.AdvancedTalismanSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * AccessoryCommands - Command handler for accessory-related commands
 * 
 * Commands:
 * - /accessorybag - Open accessory bag GUI
 * - /talismans - Open talisman GUI
 * - /enrich <accessory> - Open enrichment GUI for specific accessory
 * - /magicalpower - Show current magical power
 * - /accessorystats - Show all accessory stat bonuses
 */
public class AccessoryCommands implements CommandExecutor {
    
    private final AccessoryBagSystem accessoryBagSystem;
    private final EnrichmentSystem enrichmentSystem;
    private final AccessoryIntegrationSystem integrationSystem;
    private final AdvancedTalismanSystem talismanSystem;
    
    public AccessoryCommands(AccessoryBagSystem accessoryBagSystem, EnrichmentSystem enrichmentSystem,
                           AccessoryIntegrationSystem integrationSystem, AdvancedTalismanSystem talismanSystem) {
        this.accessoryBagSystem = accessoryBagSystem;
        this.enrichmentSystem = enrichmentSystem;
        this.integrationSystem = integrationSystem;
        this.talismanSystem = talismanSystem;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (command.getName().equalsIgnoreCase("accessorybag")) {
            accessoryBagSystem.openAccessoryBagGUI(player);
            return true;
        }
        
        if (command.getName().equalsIgnoreCase("talismans")) {
            talismanSystem.openTalismanGUI(player);
            return true;
        }
        
        if (command.getName().equalsIgnoreCase("enrich")) {
            if (args.length < 1) {
                player.sendMessage("§cUsage: /enrich <accessory_name>");
                return true;
            }
            
            String accessoryName = String.join(" ", args);
            enrichmentSystem.openEnrichmentGUI(player, accessoryName);
            return true;
        }
        
        if (command.getName().equalsIgnoreCase("magicalpower")) {
            int magicalPower = integrationSystem.getTotalMagicalPower(player.getUniqueId());
            player.sendMessage("§6§lMagical Power: §e" + magicalPower);
            
            Map<String, Double> accessoryPowers = integrationSystem.getPlayerStats(player.getUniqueId()).getAccessoryPowers();
            if (!accessoryPowers.isEmpty()) {
                player.sendMessage("§7Available Accessory Powers:");
                for (Map.Entry<String, Double> entry : accessoryPowers.entrySet()) {
                    player.sendMessage("§7- " + entry.getKey() + ": §a" + entry.getValue());
                }
            }
            return true;
        }
        
        if (command.getName().equalsIgnoreCase("accessorystats")) {
            AccessoryIntegrationSystem.PlayerAccessoryStats stats = integrationSystem.getPlayerStats(player.getUniqueId());
            
            player.sendMessage("§6§lAccessory Statistics:");
            player.sendMessage("§7Magical Power: §e" + stats.getMagicalPower());
            player.sendMessage("");
            
            Map<String, Double> bonuses = stats.getAllBonuses();
            if (bonuses.isEmpty()) {
                player.sendMessage("§7No accessory bonuses active.");
            } else {
                player.sendMessage("§7Active Bonuses:");
                for (Map.Entry<String, Double> entry : bonuses.entrySet()) {
                    String statName = formatStatName(entry.getKey());
                    double value = entry.getValue();
                    String sign = value > 0 ? "§a+" : "§c";
                    player.sendMessage("§7- " + statName + ": " + sign + value);
                }
            }
            return true;
        }
        
        return false;
    }
    
    private String formatStatName(String stat) {
        switch (stat) {
            case "damage": return "Damage";
            case "strength": return "Strength";
            case "critical_chance": return "Critical Chance";
            case "critical_damage": return "Critical Damage";
            case "intelligence": return "Intelligence";
            case "health": return "Health";
            case "defense": return "Defense";
            case "mining_speed": return "Mining Speed";
            case "farming_xp": return "Farming XP";
            case "foraging_xp": return "Foraging XP";
            case "fishing_xp": return "Fishing XP";
            case "farming_fortune": return "Farming Fortune";
            case "foraging_fortune": return "Foraging Fortune";
            case "mining_fortune": return "Mining Fortune";
            case "speed": return "Speed";
            case "mana": return "Mana";
            case "luck": return "Luck";
            case "xp": return "XP Gain";
            case "magic_find": return "Magic Find";
            case "pet_luck": return "Pet Luck";
            case "sea_creature_chance": return "Sea Creature Chance";
            default: return stat.substring(0, 1).toUpperCase() + stat.substring(1).replace("_", " ");
        }
    }
}
