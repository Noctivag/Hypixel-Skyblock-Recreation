package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.enums.SkillType;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.models.PlayerProfile;
import de.noctivag.skyblock.services.PlayerProfileService;
import de.noctivag.skyblock.SkyblockPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Skill-Menü GUI für die Anzeige aller Spieler-Skills
 * Zeigt Level, Fortschritt und nächste Boni für jeden Skill an
 */
public class SkillMenu extends Menu {
    
    private final SkyblockPlugin plugin;
    private final PlayerProfileService profileService;
    
    // Skill-Slots (3x3 Grid in der Mitte)
    private static final int[] SKILL_SLOTS = {
        19, 20, 21,  // Reihe 1
        28, 29, 30,  // Reihe 2
        37, 38, 39   // Reihe 3
    };
    
    public SkillMenu(Player player, SkyblockPlugin plugin) {
        super(player, 54); // 6 Reihen
        this.plugin = plugin;
        this.profileService = plugin.getServiceManager().getService(PlayerProfileService.class);
    }
    
    @Override
    public String getMenuTitle() {
        return "§6§lSkills";
    }
    
    @Override
    public void setMenuItems() {
        // Fülle Border
        fillBorderWith(BORDER_MATERIAL);
        
        // Lade Spielerprofil asynchron
        loadAndDisplaySkills();
        
        // Navigation-Buttons
        addNavigationButtons();
    }
    
    /**
     * Lädt das Spielerprofil und zeigt die Skills an
     */
    private void loadAndDisplaySkills() {
        UUID playerUUID = player.getUniqueId();
        
        CompletableFuture<PlayerProfile> profileFuture = profileService.loadProfile(playerUUID);
        profileFuture.thenAccept(profile -> {
            if (profile != null) {
                // Zeige Skills im Haupt-Thread an
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    displaySkills(profile);
                });
            } else {
                // Fallback: Zeige Standard-Skills
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    displayDefaultSkills();
                });
            }
        }).exceptionally(throwable -> {
            plugin.getLogger().warning("Error loading player profile for skills: " + throwable.getMessage());
            // Fallback: Zeige Standard-Skills
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                displayDefaultSkills();
            });
            return null;
        });
    }
    
    /**
     * Zeigt die Skills basierend auf dem Spielerprofil an
     * @param profile Das Spielerprofil
     */
    private void displaySkills(PlayerProfile profile) {
        SkillType[] skills = SkillType.values();
        
        for (int i = 0; i < Math.min(skills.length, SKILL_SLOTS.length); i++) {
            SkillType skill = skills[i];
            int slot = SKILL_SLOTS[i];
            
            ItemStack skillItem = createSkillItem(skill, profile);
            inventory.setItem(slot, skillItem);
        }
        
        // Zeige Gesamt-Level in der Mitte
        displayTotalLevel(profile);
    }
    
    /**
     * Zeigt Standard-Skills an (Fallback)
     */
    private void displayDefaultSkills() {
        SkillType[] skills = SkillType.values();
        
        for (int i = 0; i < Math.min(skills.length, SKILL_SLOTS.length); i++) {
            SkillType skill = skills[i];
            int slot = SKILL_SLOTS[i];
            
            ItemStack skillItem = createDefaultSkillItem(skill);
            inventory.setItem(slot, skillItem);
        }
        
        // Zeige Standard-Gesamt-Level
        displayDefaultTotalLevel();
    }
    
    /**
     * Erstellt ein Skill-Item basierend auf dem Spielerprofil
     * @param skill Der Skill-Typ
     * @param profile Das Spielerprofil
     * @return Skill-Item
     */
    private ItemStack createSkillItem(SkillType skill, PlayerProfile profile) {
        ItemStack item = new ItemStack(skill.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            // Skill-Level aus Profil (vereinfacht - in der echten Implementierung würde man echte Skill-Daten verwenden)
            int currentLevel = getSkillLevel(profile, skill);
            long currentXP = getSkillXP(profile, skill);
            long xpForNext = getXPForNextLevel(currentLevel);
            
            // Name
            meta.displayName(Component.text("§6" + skill.getGermanName())
                .color(NamedTextColor.GOLD));
            
            // Lore
            List<String> lore = new ArrayList<>();
            lore.add("§7Dein Skill-Level in §e" + skill.getGermanName());
            lore.add("§7Level: §6" + currentLevel);
            lore.add("§7Fortschritt: §2" + formatNumber(currentXP) + " §a/ §2" + formatNumber(xpForNext));
            
            // Fortschrittsbalken
            double progress = (double) currentXP / xpForNext;
            String progressBar = createProgressBar((int) (progress * 20), 20, 20);
            lore.add("§7" + progressBar);
            
            // Nächster Bonus
            String nextBonus = getNextSkillBonus(skill, currentLevel);
            lore.add("§7Nächster Bonus: §a" + nextBonus);
            lore.add("");
            lore.add("§eKlicke für Details!");
            
            meta.lore(lore.stream().map(Component::text).toList());
        }
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Erstellt ein Standard-Skill-Item (Fallback)
     * @param skill Der Skill-Typ
     * @return Standard-Skill-Item
     */
    private ItemStack createDefaultSkillItem(SkillType skill) {
        ItemStack item = new ItemStack(skill.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6" + skill.getGermanName())
                .color(NamedTextColor.GOLD));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Dein Skill-Level in §e" + skill.getGermanName());
            lore.add("§7Level: §61");
            lore.add("§7Fortschritt: §20 §a/ §250");
            lore.add("§7" + createProgressBar(0, 20, 20));
            lore.add("§7Nächster Bonus: §a" + getNextSkillBonus(skill, 1));
            lore.add("");
            lore.add("§eKlicke für Details!");
            
            meta.lore(lore.stream().map(Component::text).toList());
        }
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Zeigt das Gesamt-Level in der Mitte an
     * @param profile Das Spielerprofil
     */
    private void displayTotalLevel(PlayerProfile profile) {
        int totalLevel = calculateTotalLevel(profile);
        
        ItemStack totalItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = totalItem.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6§lGesamt-Level: §e" + totalLevel)
                .color(NamedTextColor.GOLD));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Die Summe aller deiner Skill-Level");
            lore.add("");
            lore.add("§7Skill-Level:");
            
            for (SkillType skill : SkillType.values()) {
                int level = getSkillLevel(profile, skill);
                lore.add("§7" + skill.getGermanName() + ": §6" + level);
            }
            
            meta.lore(lore.stream().map(Component::text).toList());
        }
        
        totalItem.setItemMeta(meta);
        inventory.setItem(22, totalItem); // Mitte des 3x3 Grids
    }
    
    /**
     * Zeigt das Standard-Gesamt-Level an (Fallback)
     */
    private void displayDefaultTotalLevel() {
        ItemStack totalItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = totalItem.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6§lGesamt-Level: §e10")
                .color(NamedTextColor.GOLD));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Die Summe aller deiner Skill-Level");
            lore.add("");
            lore.add("§7Skill-Level:");
            
            for (SkillType skill : SkillType.values()) {
                lore.add("§7" + skill.getGermanName() + ": §61");
            }
            
            meta.lore(lore.stream().map(Component::text).toList());
        }
        
        totalItem.setItemMeta(meta);
        inventory.setItem(22, totalItem);
    }
    
    /**
     * Berechnet das Gesamt-Level aller Skills
     * @param profile Das Spielerprofil
     * @return Gesamt-Level
     */
    private int calculateTotalLevel(PlayerProfile profile) {
        int total = 0;
        for (SkillType skill : SkillType.values()) {
            total += getSkillLevel(profile, skill);
        }
        return total;
    }
    
    /**
     * Gibt das Level eines Skills zurück (vereinfacht)
     * @param profile Das Spielerprofil
     * @param skill Der Skill-Typ
     * @return Skill-Level
     */
    private int getSkillLevel(PlayerProfile profile, SkillType skill) {
        // Vereinfachte Implementierung - in der echten Version würde man echte Skill-Daten verwenden
        return Math.max(1, (int) (profile.getLevel() / 10.0));
    }
    
    /**
     * Gibt die XP eines Skills zurück (vereinfacht)
     * @param profile Das Spielerprofil
     * @param skill Der Skill-Typ
     * @return Skill-XP
     */
    private long getSkillXP(PlayerProfile profile, SkillType skill) {
        // Vereinfachte Implementierung
        return (long) (profile.getPlayTime() / 1000); // 1 XP pro Sekunde Spielzeit
    }
    
    /**
     * Berechnet die XP für das nächste Level
     * @param currentLevel Aktuelles Level
     * @return XP für nächstes Level
     */
    private long getXPForNextLevel(int currentLevel) {
        return (long) (50 * Math.pow(1.5, currentLevel - 1));
    }
    
    /**
     * Gibt den nächsten Skill-Bonus zurück
     * @param skill Der Skill-Typ
     * @param currentLevel Aktuelles Level
     * @return Beschreibung des nächsten Bonus
     */
    private String getNextSkillBonus(SkillType skill, int currentLevel) {
        switch (skill) {
            case FARMING:
                return "§a+" + (currentLevel + 1) + "% Ernte-Ausbeute";
            case MINING:
                return "§a+" + (currentLevel + 1) + "% Mining-Geschwindigkeit";
            case FORAGING:
                return "§a+" + (currentLevel + 1) + "% Holz-Ausbeute";
            case FISHING:
                return "§a+" + (currentLevel + 1) + "% Angel-Chance";
            case COMBAT:
                return "§a+" + (currentLevel + 1) + "% Kampf-Schaden";
            case ENCHANTING:
                return "§a+" + (currentLevel + 1) + "% Verzauberungs-Qualität";
            case ALCHEMY:
                return "§a+" + (currentLevel + 1) + "% Trank-Dauer";
            case CARPENTRY:
                return "§a+" + (currentLevel + 1) + "% Handwerks-Effizienz";
            case RUNECRAFTING:
                return "§a+" + (currentLevel + 1) + "% Runen-Chance";
            case SOCIAL:
                return "§a+" + (currentLevel + 1) + "% Team-Bonus";
            default:
                return "§aNeuer Bonus verfügbar!";
        }
    }
}
