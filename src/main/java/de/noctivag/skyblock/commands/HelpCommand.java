package de.noctivag.skyblock.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpCommand implements CommandExecutor {
    private final SkyblockPlugin plugin;
    private final Map<String, String> featureDescriptions;
    private final int ITEMS_PER_PAGE = 5;

    public HelpCommand(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.featureDescriptions = new HashMap<>();
        initializeDescriptions();
    }

    private void initializeDescriptions() {
        // Allgemeine Features
        featureDescriptions.put("prefix", "§e/prefix <Text> §7- Setzt deinen Chat-Prefix\n" +
            "§e/unprefix §7- Entfernt deinen Prefix");

        featureDescriptions.put("nick", "§e/nick <name> §7- Ändert deinen Anzeigenamen\n" +
            "§e/unnick §7- Setzt deinen Namen zurück");

        featureDescriptions.put("status", "§e/status <Text> §7- Setzt deinen Status (erscheint in Tab und Chat)\n" +
            "§e/status clear §7- Entfernt deinen Status");

        // Cosmetics
        featureDescriptions.put("cosmetics", "§e/cosmetics §7- Öffnet das Kosmetik-Menü\n" +
            "§7Hier findest du Partikel-Effekte und mehr!");

        // Werkzeuge
        featureDescriptions.put("tools", "§e/craftingtable §7- Öffnet eine Werkbank\n" +
            "§e/anvil §7- Öffnet einen Amboss\n" +
            "§e/enderchest §7- Öffnet deine Endertruhe\n" +
            "§e/grindstone §7- Öffnet einen Schleifstein\n" +
            "§e/smithingtable §7- Öffnet einen Schmiedetisch\n" +
            "§e/stonecutter §7- Öffnet einen Steinsäger\n" +
            "§e/loom §7- Öffnet einen Webstuhl\n" +
            "§e/cartography §7- Öffnet einen Kartentisch");

        // Admin-Befehle
        featureDescriptions.put("admin", "§e/status §7- Zeigt den Plugin-Status\n" +
            "§7Nur für Administratoren verfügbar.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int page = 1;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                // Wenn keine Zahl angegeben wurde, suche nach Kategorie
                String category = args[0].toLowerCase();
                if (featureDescriptions.containsKey(category)) {
                    sender.sendMessage("§8§m-----§r §6" + category.substring(0, 1).toUpperCase() + category.substring(1) + " §8§m-----");
                    sender.sendMessage(featureDescriptions.get(category));
                    return true;
                }
            }
        }

        List<String> categories = new ArrayList<>(featureDescriptions.keySet());
        int maxPages = (int) Math.ceil(categories.size() / (double) ITEMS_PER_PAGE);
        page = Math.min(Math.max(1, page), maxPages);

        sender.sendMessage("§8§m----------§r §6Hilfe (Seite " + page + "/" + maxPages + ") §8§m----------");

        int startIndex = (page - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, categories.size());

        for (int i = startIndex; i < endIndex; i++) {
            String category = categories.get(i);
            sender.sendMessage("§6" + category.substring(0, 1).toUpperCase() + category.substring(1));
            sender.sendMessage(featureDescriptions.get(category));
            sender.sendMessage("");
        }

        sender.sendMessage("§7Verwende §e/help <Seite> §7für weitere Seiten");
        sender.sendMessage("§7oder §e/help <Kategorie> §7für Details.");

        return true;
    }
}
