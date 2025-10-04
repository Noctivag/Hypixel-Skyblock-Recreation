package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.achievements.AchievementSystem;
import de.noctivag.plugin.achievements.AchievementDefinition;
import de.noctivag.plugin.achievements.AchievementCategory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;

public class EnhancedAchievementGUI extends CustomGUI {
    private final Plugin plugin;
    private final Player player;
    private final AchievementSystem achievementSystem;
    private String currentCategory = "all";

    public EnhancedAchievementGUI(Plugin plugin, Player player) {
        super(54, Component.text("§6§lAchievements"));
        this.plugin = plugin;
        this.player = player;
        // Placeholder - method not implemented
        this.achievementSystem = null;
        setupItems();
    }

    private void setupItems() {
        // Header
        setItem(4, Material.GOLD_INGOT, "§6§lAchievement System", 
            "§7Fortschritt: §e" + achievementSystem.getCompletedAchievements(player) + 
            "§7/§e" + achievementSystem.getTotalAchievements(),
            "§7Abgeschlossen: §e" + String.format("%.1f", achievementSystem.getCompletionPercentage(player)) + "%");
        
        // Category buttons
        setItem(10, Material.DRAGON_HEAD, "§5§lEvents", 
            "§7Event-bezogene Achievements",
            "§eKlicke zum Anzeigen");
        
        setItem(11, Material.EMERALD, "§a§lEconomy", 
            "§7Wirtschafts-Achievements",
            "§eKlicke zum Anzeigen");
        
        setItem(12, Material.NETHER_STAR, "§d§lCosmetics", 
            "§7Kosmetik-Achievements",
            "§eKlicke zum Anzeigen");
        
        setItem(13, Material.PLAYER_HEAD, "§b§lSocial", 
            "§7Soziale Achievements",
            "§eKlicke zum Anzeigen");
        
        setItem(14, Material.DIAMOND_SWORD, "§c§lCombat", 
            "§7Kampf-Achievements",
            "§eKlicke zum Anzeigen");
        
        setItem(15, Material.COMPASS, "§e§lExploration", 
            "§7Entdeckungs-Achievements",
            "§eKlicke zum Anzeigen");
        
        setItem(16, Material.BOOK, "§f§lAlle", 
            "§7Alle Achievements anzeigen",
            "§eKlicke zum Anzeigen");
        
        // Show achievements based on current category
        showAchievements();
        
        // Navigation
        setItem(45, Material.ARROW, "§7§lZurück", "§7Zurück zum Hauptmenü");
        setItem(49, Material.BARRIER, "§c§lSchließen", "§7Schließe das Menü");
    }

    private void showAchievements() {
        List<AchievementDefinition> achievements;
        
        if ("all".equals(currentCategory)) {
            achievements = achievementSystem.getAllAchievements().stream()
                .map(achievement -> achievementSystem.getAchievementDefinition(achievement.getId()))
                .collect(java.util.stream.Collectors.toList());
        } else {
            AchievementCategory category = AchievementCategory.valueOf(currentCategory.toUpperCase());
            achievements = achievementSystem.getAchievementsByCategory(category);
        }
        
        int slot = 19;
        for (AchievementDefinition achievement : achievements) {
            if (slot >= 44) break; // Don't overflow
            
            boolean completed = achievementSystem.hasAchievement(player, achievement.getId());
            int progress = achievementSystem.getAchievementProgress(player, achievement.getId());
            
            Material icon = completed ? achievement.getIcon() : Material.GRAY_DYE;
            String name = completed ? "§a§l✓ " + achievement.getName() : "§7§l" + achievement.getName();
            
            List<String> lore = new ArrayList<>();
            lore.add("§7" + achievement.getDescription());
            lore.add("");
            
            if (completed) {
                lore.add("§a§lAbgeschlossen!");
                lore.add("§eBelohnung erhalten: §f" + achievement.getReward());
            } else {
                lore.add("§7Fortschritt: §e" + progress + "§7/§e" + achievement.getTarget());
                lore.add("§eBelohnung: §f" + achievement.getReward());
                
                // Show progress bar
                int progressPercent = (int) ((double) progress / achievement.getTarget() * 100);
                String progressBar = createProgressBar(progressPercent);
                lore.add("§7" + progressBar + " §e" + progressPercent + "%");
            }
            
            lore.add("");
            lore.add("§7Kategorie: §f" + achievement.getCategory());
            
            setItem(slot, icon, name, lore.toArray(new String[0]));
            slot++;
        }
        
        // Fill remaining slots with glass panes
        while (slot < 44) {
            setItem(slot, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
            slot++;
        }
    }

    private String createProgressBar(int percentage) {
        StringBuilder bar = new StringBuilder("§8[");
        int filled = percentage / 10;
        
        for (int i = 0; i < 10; i++) {
            if (i < filled) {
                bar.append("§a█");
            } else {
                bar.append("§7█");
            }
        }
        
        bar.append("§8]");
        return bar.toString();
    }

    public void setCategory(String category) {
        this.currentCategory = category;
        setupItems();
    }

    public void setItem(int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) {
                meta.lore(Arrays.stream(lore).map(Component::text).toList());
            }
            item.setItemMeta(meta);
        }
        
        getInventory().setItem(slot, item);
    }
}
