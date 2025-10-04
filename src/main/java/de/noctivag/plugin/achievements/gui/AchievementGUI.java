package de.noctivag.plugin.achievements.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.achievements.Achievement;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "deprecation"})
public class AchievementGUI {
    private final Plugin plugin;
    private static final Component GUI_TITLE = Component.text("» ").color(net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY)
            .append(Component.text("Erfolge").color(net.kyori.adventure.text.format.NamedTextColor.GOLD))
            .append(Component.text(" »").color(net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY));

    public AchievementGUI(Plugin plugin) {
        this.plugin = plugin;
    }

    public void openMainGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, GUI_TITLE);
        decorateInventory(inv);

        // Statistik-Item
        // TODO: Implement proper AchievementManager interface
        // double completion = ((AchievementManager) plugin.getAchievementManager()).getCompletionPercentage(player);
        // int unlockedCount = ((AchievementManager) plugin.getAchievementManager()).getUnlockedAchievements(player).size();
        double completion = 0.0; // Placeholder
        int unlockedCount = 0; // Placeholder
        int totalCount = Achievement.values().length;

        ItemStack statsItem = createGuiItem(
            Material.EXPERIENCE_BOTTLE,
            "§6» §eErfolgs-Statistiken",
            Arrays.asList(
                "§7Abgeschlossen: §e" + String.format("%.1f", completion) + "%",
                "§7Freigeschaltet: §e" + unlockedCount + "§7/§e" + totalCount,
                "",
                "§7Gesamte Belohnungen:",
                "§e✧ " + calculateTotalExp(player) + " EXP",
                "§e✧ " + calculateTotalCoins(player) + " Coins"
            )
        );
        inv.setItem(4, statsItem);

        // Filterleiste
        inv.setItem(20, createGuiItem(
            Material.HOPPER,
            "§eFilter: §fAlle",
            List.of("§7Zeige alle Erfolge"))
        );
        inv.setItem(22, createGuiItem(
            Material.LIME_DYE,
            "§eFilter: §aFreigeschaltet",
            List.of("§7Nur freigeschaltete anzeigen"))
        );
        inv.setItem(24, createGuiItem(
            Material.GRAY_DYE,
            "§eFilter: §7Nicht freigeschaltet",
            List.of("§7Nur gesperrte anzeigen"))
        );

        // Kategorien
        Map<String, List<Achievement>> categories = Arrays.stream(Achievement.values())
            .collect(Collectors.groupingBy(ach -> ach.name().split("_")[0]));

        int slot = 19;
        for (var entry : categories.entrySet()) {
            String category = entry.getKey();
            List<Achievement> achievements = entry.getValue();

            Material icon = getCategoryIcon(category);
            double categoryCompletion = calculateCategoryCompletion(player, achievements);

            ItemStack categoryItem = createGuiItem(
                icon,
                "§6» §e" + formatCategoryName(category),
                List.of(
                    "§7Fortschritt: §e" + String.format("%.1f", categoryCompletion) + "%",
                    "§7Freigeschaltet: §e" + countUnlocked(player, achievements) + "§7/§e" + achievements.size(),
                    "",
                    "§eKlicke zum Ansehen!"
                )
            );

            inv.setItem(slot, categoryItem);
            slot += (slot % 9 == 7) ? 3 : 1;
        }

        // Back button
        inv.setItem(45, createGuiItem(Material.ARROW, "§7Zurück", Arrays.asList("§7Zum Hauptmenü")));

        player.openInventory(inv);
    }

    public void openCategoryGUI(Player player, String category) {
        openCategoryGUI(player, category, "ALL");
    }

    public void openCategoryGUI(Player player, String category, String filterMode) {
        Component title = GUI_TITLE.append(Component.text(" - ").color(net.kyori.adventure.text.format.NamedTextColor.GRAY))
                .append(Component.text(formatCategoryName(category)).color(net.kyori.adventure.text.format.NamedTextColor.YELLOW));
        Inventory inv = Bukkit.createInventory(null, 54, title);
        decorateInventory(inv);

        var categoryAchievements = Arrays.stream(Achievement.values())
            .filter(ach -> ach.name().startsWith(category))
            .toList();

        int slot = 10;
        for (Achievement achievement : categoryAchievements) {
            // TODO: Implement proper AchievementManager interface
            // if ("UNLOCKED".equalsIgnoreCase(filterMode) && !((AchievementManager) plugin.getAchievementManager()).hasAchievement(player, achievement)) {
            //     continue;
            // }
            // if ("LOCKED".equalsIgnoreCase(filterMode) && ((AchievementManager) plugin.getAchievementManager()).hasAchievement(player, achievement)) {
            //     continue;
            // }
            ItemStack achievementItem = createAchievementItem(player, achievement);
            inv.setItem(slot, achievementItem);
            slot += (slot % 9 == 7) ? 3 : 1;
        }

        // Zurück-Button
        ItemStack backButton = createGuiItem(
            Material.ARROW,
            "§c« Zurück",
            List.of("§7Zurück zur Übersicht")
        );
        inv.setItem(49, backButton);

        player.openInventory(inv);
    }

    private ItemStack createAchievementItem(Player player, Achievement achievement) {
        // TODO: Implement proper AchievementManager interface
        // boolean unlocked = ((AchievementManager) plugin.getAchievementManager()).hasAchievement(player, achievement);
        // int progress = ((AchievementManager) plugin.getAchievementManager()).getProgress(player, achievement);
        boolean unlocked = false; // Placeholder
        int progress = 0; // Placeholder

        List<String> lore = new ArrayList<>();
        lore.add(achievement.getDescription());
        lore.add("");

        // Füge Fortschritt hinzu, falls relevant
        if (!unlocked && requiresProgress(achievement)) {
            int required = getRequiredProgress(achievement);
            lore.add("§7Fortschritt: §e" + progress + "§7/§e" + required);
            lore.add(createProgressBar(progress, required));
        }

        // Tier-Info (auch wenn freigeschaltet, zeigen wir Stufe/Max)
        int currentTier = achievement.getTierForProgress(progress);
        int maxTier = achievement.getMaxTier();
        if (maxTier > 1) {
            lore.add("");
            lore.add("§7Tier: §e" + currentTier + "§7/§e" + maxTier);
            List<Integer> thresholds = achievement.getTierThresholds();
            int nextReq = currentTier < thresholds.size() ? thresholds.get(currentTier) : thresholds.get(thresholds.size() - 1);
            if (currentTier < maxTier) {
                lore.add("§7Nächstes Tier bei: §e" + nextReq);
            } else {
                lore.add("§aMax. Tier erreicht");
            }
        }

        ItemStack item = createGuiItem(
            achievement.getIcon(),
            (unlocked ? "§a✔ " : "§c✖ ") + achievement.getDisplayName(),
            lore
        );

        if (unlocked) {
            ItemMeta meta = item.getItemMeta();try {
                meta.addEnchant(org.bukkit.enchantments.Enchantment.FORTUNE, 1, true);
            } catch (NoSuchFieldError | IllegalArgumentException e) {
                // Fallback not needed since we're using LUCK_OF_THE_SEA directly
            }
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    private String createProgressBar(int current, int max) {
        int bars = 20;
        int filledBars = (int) ((double) current / max * bars);
        StringBuilder bar = new StringBuilder("§8[");

        for (int i = 0; i < bars; i++) {
            if (i < filledBars) {
                bar.append("§a|");
            } else {
                bar.append("§7|");
            }
        }

        bar.append("§8]");
        return bar.toString();
    }

    private void decorateInventory(Inventory inv) {
        ItemStack border = createGuiItem(Material.BLACK_STAINED_GLASS_PANE, "§r", List.of());
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, border);
            inv.setItem(i + 45, border);
        }
        for (int i = 9; i < 45; i += 9) {
            inv.setItem(i, border);
            inv.setItem(i + 8, border);
        }
    }

    private Material getCategoryIcon(String category) {
        return switch (category) {
            case "FIRST" -> Material.GRASS_BLOCK;
            case "REACH" -> Material.EXPERIENCE_BOTTLE;
            case "KIT" -> Material.CHEST;
            case "MINING" -> Material.DIAMOND_PICKAXE;
            case "PVP" -> Material.DIAMOND_SWORD;
            case "EXPLORER" -> Material.COMPASS;
            default -> Material.BOOK;
        };
    }

    private String formatCategoryName(String category) {
        return switch (category) {
            case "FIRST" -> "Allgemein";
            case "REACH" -> "Level";
            case "KIT" -> "Kits";
            case "MINING" -> "Bergbau";
            case "PVP" -> "Kampf";
            case "EXPLORER" -> "Entdecker";
            default -> category;
        };
    }

    private boolean requiresProgress(Achievement achievement) {
        return switch (achievement) {
            case REACH_LEVEL_10, REACH_LEVEL_50, FIRST_THOUSAND, MILLIONAIRE,
                 EXPLORER, MINING_EXPERT, PVP_MASTER -> true;
            default -> false;
        };
    }

    private int getRequiredProgress(Achievement achievement) {
        return switch (achievement) {
            case REACH_LEVEL_10 -> 10;
            case REACH_LEVEL_50 -> 50;
            case FIRST_THOUSAND -> 1000;
            case MILLIONAIRE -> 1000000;
            case EXPLORER -> 10;
            case MINING_EXPERT -> 1000;
            case PVP_MASTER -> 100;
            default -> 1;
        };
    }

    private double calculateCategoryCompletion(Player player, List<Achievement> achievements) {
        int unlocked = countUnlocked(player, achievements);
        return (double) unlocked / achievements.size() * 100;
    }

    private int countUnlocked(Player player, List<Achievement> achievements) {
        // TODO: Implement proper AchievementManager interface
        // return (int) achievements.stream()
        //     .filter(ach -> ((AchievementManager) plugin.getAchievementManager()).hasAchievement(player, ach))
        //     .count();
        return 0; // Placeholder
    }

    private int calculateTotalExp(Player player) {
        // TODO: Implement proper AchievementManager interface
        // return ((AchievementManager) plugin.getAchievementManager()).getUnlockedAchievements(player).stream()
        return 0; // Placeholder
    }

    private int calculateTotalCoins(Player player) {
        // TODO: Implement proper AchievementManager interface
        // return ((AchievementManager) plugin.getAchievementManager()).getUnlockedAchievements(player).stream()
        //     .mapToInt(Achievement::getCoinReward)
        //     .sum();
        return 0; // Placeholder
    }

    private ItemStack createGuiItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            var loreComponents = lore.stream()
                .map(Component::text)
                .collect(Collectors.toList());
            meta.lore(loreComponents);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }
        return item;
    }
}
