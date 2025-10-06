package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.multiserver.ServerTemplate;
import de.noctivag.skyblock.multiserver.ServerTemplateSystem;
import net.kyori.adventure.text.Component;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Commands für das Server Template System
 */
public class TemplateCommands implements CommandExecutor, TabCompleter {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final ServerTemplateSystem templateSystem;
    
    public TemplateCommands(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.templateSystem = new ServerTemplateSystem(SkyblockPlugin);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("§cDieser Befehl kann nur von Spielern verwendet werden!"));
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            showHelp(player);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "list":
                listTemplates(player);
                break;
                
            case "info":
                if (args.length < 2) {
                    player.sendMessage(Component.text("§cVerwendung: /template info <template-name>"));
                    return true;
                }
                showTemplateInfo(player, args[1]);
                break;
                
            case "create":
                if (args.length < 3) {
                    player.sendMessage(Component.text("§cVerwendung: /template create <name> <display-name>"));
                    return true;
                }
                createTemplate(player, args[1], args[2]);
                break;
                
            case "delete":
                if (args.length < 2) {
                    player.sendMessage(Component.text("§cVerwendung: /template delete <template-name>"));
                    return true;
                }
                deleteTemplate(player, args[1]);
                break;
                
            case "createworld":
                if (args.length < 3) {
                    player.sendMessage(Component.text("§cVerwendung: /template createworld <template-name> <world-name>"));
                    return true;
                }
                createWorldFromTemplate(player, args[1], args[2]);
                break;
                
            case "copyworld":
                if (args.length < 3) {
                    player.sendMessage(Component.text("§cVerwendung: /template copyworld <template-world> <new-world>"));
                    return true;
                }
                copyTemplateWorld(player, args[1], args[2]);
                break;
                
            default:
                showHelp(player);
                break;
        }
        
        return true;
    }
    
    private void listTemplates(Player player) {
        Map<String, ServerTemplate> templates = templateSystem.getTemplates();
        
        player.sendMessage(Component.text("§6§l=== Verfügbare Server Templates ==="));
        player.sendMessage(Component.text("§eAnzahl Templates: §f" + templates.size()));
        
        // Gruppiere Templates nach Typ
        Map<String, List<ServerTemplate>> groupedTemplates = new HashMap<>();
        for (ServerTemplate template : templates.values()) {
            String category = getTemplateCategory(template);
            groupedTemplates.computeIfAbsent(category, k -> new ArrayList<>()).add(template);
        }
        
        for (Map.Entry<String, List<ServerTemplate>> entry : groupedTemplates.entrySet()) {
            player.sendMessage(Component.text("§6§l" + entry.getKey() + ":"));
            for (ServerTemplate template : entry.getValue()) {
                String status = template.isPersistent() ? "§aPersistent" : "§cTemporary";
                player.sendMessage(Component.text("§7- " + template.getName() + " §8(" + template.getDisplayName() + ") " + status));
            }
        }
    }
    
    private String getTemplateCategory(ServerTemplate template) {
        if (template.getName().equals("skyblock_hub")) {
            return "Haupt-Server Templates";
        } else if (template.getName().equals("private_island") || template.getName().equals("garden")) {
            return "Persistente Spieler-Templates";
        } else if (template.getName().startsWith("catacombs_") || template.getName().startsWith("master_mode_")) {
            return "Dungeon Templates";
        } else if (isPublicIsland(template.getName())) {
            return "Public Island Templates";
        } else {
            return "Special Templates";
        }
    }
    
    private boolean isPublicIsland(String templateName) {
        return templateName.equals("spiders_den") || templateName.equals("the_end") || 
               templateName.equals("the_park") || templateName.equals("gold_mine") ||
               templateName.equals("deep_caverns") || templateName.equals("dwarven_mines") ||
               templateName.equals("crystal_hollows") || templateName.equals("the_barn") ||
               templateName.equals("mushroom_desert") || templateName.equals("blazing_fortress") ||
               templateName.equals("the_nether") || templateName.equals("crimson_isle") ||
               templateName.equals("rift") || templateName.equals("kuudra");
    }
    
    private void showTemplateInfo(Player player, String templateName) {
        ServerTemplate template = templateSystem.getTemplate(templateName);
        if (template == null) {
            player.sendMessage(Component.text("§cTemplate nicht gefunden: " + templateName));
            return;
        }
        
        player.sendMessage(Component.text("§6§l=== Template Info: " + templateName + " ==="));
        player.sendMessage(Component.text("§eName: §f" + template.getName()));
        player.sendMessage(Component.text("§eDisplay Name: §f" + template.getDisplayName()));
        player.sendMessage(Component.text("§eDescription: §f" + template.getDescription()));
        player.sendMessage(Component.text("§eWorld Type: §f" + template.getWorldType()));
        player.sendMessage(Component.text("§ePersistent: §f" + (template.isPersistent() ? "Ja" : "Nein")));
        player.sendMessage(Component.text("§eGame Server: §f" + (template.isGameServer() ? "Ja" : "Nein")));
        player.sendMessage(Component.text("§eGenerate Structures: §f" + (template.isGenerateStructures() ? "Ja" : "Nein")));
        
        player.sendMessage(Component.text("§6§lSettings:"));
        Map<String, Object> settings = template.getSettings();
        for (Map.Entry<String, Object> entry : settings.entrySet()) {
            player.sendMessage(Component.text("§7- " + entry.getKey() + ": " + entry.getValue()));
        }
    }
    
    private void createTemplate(Player player, String name, String displayName) {
        if (!player.hasPermission("basicsplugin.template.create")) {
            player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
            return;
        }
        
        // Erstelle ein Standard-Template
        templateSystem.createTemplate(name, displayName, "Custom Template", WorldType.NORMAL, false, true);
        player.sendMessage(Component.text("§aTemplate '" + name + "' erfolgreich erstellt!"));
    }
    
    private void deleteTemplate(Player player, String templateName) {
        if (!player.hasPermission("basicsplugin.template.delete")) {
            player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
            return;
        }
        
        boolean deleted = templateSystem.deleteTemplate(templateName);
        if (deleted) {
            player.sendMessage(Component.text("§aTemplate '" + templateName + "' erfolgreich gelöscht!"));
        } else {
            player.sendMessage(Component.text("§cTemplate '" + templateName + "' nicht gefunden!"));
        }
    }
    
    private void createWorldFromTemplate(Player player, String templateName, String worldName) {
        if (!player.hasPermission("basicsplugin.template.createworld")) {
            player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
            return;
        }
        
        player.sendMessage(Component.text("§aErstelle Welt '" + worldName + "' aus Template '" + templateName + "'..."));
        
        CompletableFuture<org.bukkit.World> future = templateSystem.createWorldFromTemplate(templateName, worldName);
        future.thenAccept(world -> {
            if (world != null) {
                player.sendMessage(Component.text("§aWelt '" + worldName + "' erfolgreich erstellt!"));
            } else {
                player.sendMessage(Component.text("§cFehler beim Erstellen der Welt '" + worldName + "'!"));
            }
        });
    }
    
    private void copyTemplateWorld(Player player, String templateWorldName, String newWorldName) {
        if (!player.hasPermission("basicsplugin.template.copyworld")) {
            player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
            return;
        }
        
        player.sendMessage(Component.text("§aKopiere Template-Welt '" + templateWorldName + "' zu '" + newWorldName + "'..."));
        
        CompletableFuture<Boolean> future = templateSystem.copyTemplateWorld(templateWorldName, newWorldName);
        future.thenAccept(success -> {
            if (success) {
                player.sendMessage(Component.text("§aWelt '" + newWorldName + "' erfolgreich kopiert!"));
            } else {
                player.sendMessage(Component.text("§cFehler beim Kopieren der Welt '" + newWorldName + "'!"));
            }
        });
    }
    
    private void showHelp(Player player) {
        player.sendMessage(Component.text("§6§l=== Template Commands ==="));
        player.sendMessage(Component.text("§e/template list §7- Liste alle Templates"));
        player.sendMessage(Component.text("§e/template info <name> §7- Zeige Template-Informationen"));
        if (player.hasPermission("basicsplugin.template.create")) {
            player.sendMessage(Component.text("§e/template create <name> <display> §7- Erstelle neues Template"));
        }
        if (player.hasPermission("basicsplugin.template.delete")) {
            player.sendMessage(Component.text("§e/template delete <name> §7- Lösche Template"));
        }
        if (player.hasPermission("basicsplugin.template.createworld")) {
            player.sendMessage(Component.text("§e/template createworld <template> <world> §7- Erstelle Welt aus Template"));
        }
        if (player.hasPermission("basicsplugin.template.copyworld")) {
            player.sendMessage(Component.text("§e/template copyworld <template> <world> §7- Kopiere Template-Welt"));
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.addAll(Arrays.asList("list", "info", "create", "delete", "createworld", "copyworld"));
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "info":
                case "delete":
                case "createworld":
                    // Füge alle verfügbaren Template-Namen hinzu
                    completions.addAll(templateSystem.getTemplates().keySet());
                    break;
                case "copyworld":
                    // Füge alle verfügbaren Welt-Namen hinzu
                    SkyblockPlugin.getServer().getWorlds().forEach(world -> completions.add(world.getName()));
                    break;
            }
        }
        
        return completions;
    }
}
